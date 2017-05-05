    package br.order.org.controller.customerOrder;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.pojo.customer.order.CustomerOrder;
import br.crm.pojo.patient.Patient;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.customer.order.CustomerOrderService;
import br.crm.service.patient.PatientService;
import br.crm.vo.customer.order.CustomerOrderVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.org.controller.common.CommonController;

/**
 * @ClassName: CustomerOrderController
 * @Description: TODO(体检机构分页获取订单信息)
 * @author server
 * @date 2016年12月5日 下午2:20:02
 */
@Controller
@RequestMapping("/customerOrder")
public class CustomerOrderController {
	@Autowired
	private CustomerOrderService customerOrderService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private CommonController commonController;
	@Autowired
	private PatientService patientService;

	/** 
	* @Title: getCustomerOrderControllerByPage 
	* @Description: TODO(分页获取消费记录列表)
	* @param page
	* @param rows
	* @param customerOrderVo
	* @return    设定文件 
	* @return JSONObject    返回类型 
	*/
	@ApiOperation(value = "体检机构分页获取订单信息", httpMethod = "POST", response = JSONObject.class, notes = "分页获取消费记录列表")
	@RequestMapping(value = "/getCustomerOrderByPage")
	@ResponseBody
	public JSONObject getCustomerOrderControllerByPage(
			@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
			@ApiParam(required=true ,name="customerOrderVo",value="customerOrderVo,订单对象")CustomerOrderVo customerOrderVo){
		JSONObject message = new JSONObject();
		try {
			OrgUserVo organizationUser = commonController.getUserBySession();
			String orgId = organizationUser.getOrgId();
			customerOrderVo.setOrgId(orgId);
			PageInfo<CustomerOrderVo> pageInfo = customerOrderService.getCustomerOrderByPage(page, rows, customerOrderVo);
			message.put("data", pageInfo);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/** 
	* @Title: updateExamStatus 
	* @Description: TODO(体检机构开始体检接口)
	* @return    设定文件 
	* @return JSONObject    返回类型 
	*/
	@ApiOperation(value = "体检机构开始体检接口", httpMethod = "POST", response = JSONObject.class, notes = "体检机构开始体检接口")
	@RequestMapping(value = "/updateStartExamStatus")
	@ResponseBody
	public JSONObject updateStartExamStatus(
			@ApiParam(required=true ,name="customerOrderId",value="customerOrderId,订单表主键id")String customerOrderId){
		JSONObject message = new JSONObject();
		try {
			CustomerOrder customerOrder = customerOrderService.getCustomerOrderById(customerOrderId);
			if(customerOrder.getOrderStatus() == 2){
				customerOrder.setOrderStatus(3);
				int i = customerOrderService.updateCustomerOrder(customerOrder);
				if(i>0){
					message.put("data", "体检者已完成支付，请开始体检！");
					return InterfaceResultUtil.getReturnMapSuccess(message);
				}				
			}
			else{
				message.put("data","体检者未完成支付，不能体检，请先支付！");
				return InterfaceResultUtil.getReturnMapError(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: updateStartExamStatus 
	* @Description: TODO(体检机构体检完成接口)
	* @param patient
	* @return    设定文件 
	* @return JSONObject    返回类型 
	*/
	@ApiOperation(value = "体检机构体检完成接口", httpMethod = "POST", response = JSONObject.class, notes = "体检机构体检完成接口")
	@RequestMapping(value = "/updateEndExamStatus")
	@ResponseBody
	public JSONObject updateEndExamStatus(
			@ApiParam(required=true ,name="customerOrderId",value="customerOrderId,订单表主键id")String customerOrderId){
		JSONObject message = new JSONObject();
		try {
			CustomerOrder customerOrder = customerOrderService.getCustomerOrderById(customerOrderId);
			if(customerOrder.getOrderStatus() == 3){
				customerOrder.setOrderStatus(4);
				int i = customerOrderService.updateCustomerOrder(customerOrder);
				if(i>0){
					message.put("data", "体检已完成！");
					return InterfaceResultUtil.getReturnMapSuccess(message);
				}				
			}
			else{
				message.put("data","体检者未体检，不能结束体检！");
				return InterfaceResultUtil.getReturnMapError(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/** 
	* @Title: getCustomerOrder 
	* @Description:TODO(体检机构获取订单详情 )
	* @param customerOrderId
	* @return    设定文件 
	* @return JSONObject    返回类型 
	*/
	@ApiOperation(value = "获取订单信息详情", httpMethod = "PSOT", response = JSONObject.class, notes = "获取订单信息详情")
	@RequestMapping(value = "/getCustomerOrder")
	@ResponseBody
	public JSONObject getCustomerOrder(@ApiParam(required = true, name = "examSuiteId", value = "examSuiteId,套餐id")String examSuiteId,
			@ApiParam(required = true, name = "customerPatientName", value = "customerPatientName,体检人姓名")String customerPatientName,
			@ApiParam(required = true, name = "examTime", value = "examTime,体检时间")String examTime) {
		JSONObject message = new JSONObject();
		try {
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = format.format(new Date(Long.valueOf(examTime)));
			CustomerOrderVo customerOrderVo = customerOrderService.getCustomerOrderById(customerPatientName,examSuiteId,time);
			message.put("data", customerOrderVo);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
}
