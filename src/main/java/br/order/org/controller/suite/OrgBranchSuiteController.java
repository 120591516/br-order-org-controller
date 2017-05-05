package br.order.org.controller.suite;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.pojo.suite.OrganizationBranchSuite;
import br.crm.service.suite.OrgBranchSuiteService;
import br.crm.vo.permission.OrgUserVo;
import br.crm.vo.suite.OrgExamSuiteQu;
import br.crm.vo.suite.OrgExamSuiteVo;
import br.order.common.utils.InterfaceResultUtil;
import br.order.common.utils.TransCoding;
import br.order.org.controller.common.CommonController;

/**
 * 
* @ClassName: OrgBranchSuiteController 
* @Description: TODO 门店绑定套餐
* @author kangting
* @date 2016年9月12日 下午1:43:35 
*
 */
@Controller
@RequestMapping("/orgBranchSuite")
public class OrgBranchSuiteController {
	@Autowired
	private OrgBranchSuiteService orgBranchSuiteService;
	@Autowired
	private HttpServletRequest request;
	/**
	 * Session
	 */
	@Autowired
	private CommonController commonController;
	/**
	* @Title: getOrgBranchSuiteList 
	* @Description: TODO  门店绑定套餐列表
	* @param @param page 当前页 
	* @param @param rows 每页显示条数
	* @param @param orgExamSuiteQu 查询条件
	* @param @return    
	* @return JSONObject    
	* @throws
	 */
	@ApiOperation(value="门店套餐信息查询",httpMethod="GET",notes="获取全部门店套餐信息")
	@RequestMapping("/getOrgBranchSuiteList")
	@ResponseBody
	public JSONObject getOrgBranchSuiteList(
			@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
			@ApiParam(required =true, name = "orgExamSuiteQu", value = "orgExamSuiteQu,查询条件") OrgExamSuiteQu orgExamSuiteQu) {
		JSONObject mObject=new JSONObject();
		try {
			OrgUserVo brUser = commonController.getUserBySession();
			orgExamSuiteQu.setOrgId(brUser.getOrgId());
			if (orgExamSuiteQu != null && orgExamSuiteQu.getExamSuiteName() != null) {
				orgExamSuiteQu.setExamSuiteName(TransCoding.encodeStr(orgExamSuiteQu.getExamSuiteName()));
				}
			PageInfo<OrgExamSuiteVo> pageInfo=orgBranchSuiteService.getOrgBranchSuite(page,rows,orgExamSuiteQu);
			mObject.put("data", pageInfo);
			return InterfaceResultUtil.getReturnMapSuccess(mObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(mObject);
	}
	/**
	* @Title: getOrgBranchSuiteById 
	* @Description: TODO 根据id查看门店套餐信息
	* @param @param orgBranchSuiteId 门店套餐信息id
	* @param @return    
	* @return JSONObject    
	* @throws
	 */
	@ApiOperation(value="根据id查看门店套餐信息",httpMethod="GET",notes="根据id查看门店套餐信息")
	@RequestMapping("/getOrgBranchSuiteById")
	@ResponseBody
	public JSONObject getOrgBranchSuiteById(@ApiParam(required=true,value="门店套餐信息id",name="orgBranchSuiteId")String orgBranchSuiteId){
		 JSONObject mJsonObject=new JSONObject();
		 try {
			 OrgExamSuiteVo orgExamSuiteVo=orgBranchSuiteService.getBranchSuiteById(orgBranchSuiteId);
			mJsonObject.put("data", orgExamSuiteVo);
			return InterfaceResultUtil.getReturnMapSuccess(mJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return InterfaceResultUtil.getReturnMapError(mJsonObject);
	}
	/**
	 * 
	* @Title: insertOrgBranchSuite 
	* @Description: TODO 增加门店套餐綁定信息
	* @param @param branchId　门店id
	* @param @param suiteIds  选择的套餐id
	* @param @return    
	* @return JSONObject data 返回插入成功的条数
	* @throws
	 */
	@ApiOperation(value="增加门店套餐信息",httpMethod="POST",notes="添加门店信息")
	@RequestMapping("/insertOrgBranchSuite")
	@ResponseBody
	public JSONObject insertOrgBranchSuite(@ApiParam(value="门店id",required=true,name="branchId")String branchId,
			@ApiParam(value="List门店id",required=true,name="suiteIds")String suiteIds){
		 JSONObject mJsonObject=new JSONObject();
		 try {
				OrgUserVo brUser = commonController.getUserBySession();
			 OrganizationBranchSuite orgBranchSuite=new OrganizationBranchSuite();
			 orgBranchSuite.setBranchId(branchId);
			 orgBranchSuite.setCreatetime(new Date());
			 orgBranchSuite.setStatus(0);
			 orgBranchSuite.setEdittime(orgBranchSuite.getCreatetime());
			 orgBranchSuite.setEditPersonName(brUser.getUserName());
			 //orgBranchSuite.setEditPersonId(brUser.getUserId());
			 orgBranchSuite.setEditPersonPlat(1);
			int r=orgBranchSuiteService.insertBranchSuite(orgBranchSuite,suiteIds);
			mJsonObject.put("data", r);
			return InterfaceResultUtil.getReturnMapSuccess(mJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return InterfaceResultUtil.getReturnMapError(mJsonObject);
	}
/**
 * 	
* @Title: delectOrgBranchSuiteById 
* @Description: TODO 根据id删除门店套餐信息
* @param @param orgBranchSuiteId 门店绑定套餐信息id
* @param @return   
* @return JSONObject   
* @throws
 */
	@ApiOperation(value="根据id删除门店套餐信息",httpMethod="GET",notes="根据id删除门店套餐信息")
	@RequestMapping("/delectOrgBranchSuiteById")
	@ResponseBody
	public JSONObject delectOrgBranchSuiteById(@ApiParam(required=true,value="门店套餐信息id",name="orgBranchSuiteId")String orgBranchSuiteId){
		 JSONObject mJsonObject=new JSONObject();
		 try {
			 OrganizationBranchSuite orgBranchSuite=new OrganizationBranchSuite();
			 orgBranchSuite.setStatus(1);
			 orgBranchSuite.setEdittime(new Date());
			 orgBranchSuite.setBranchSuiteId(orgBranchSuiteId);
			int r=orgBranchSuiteService.updateOrgBranchSuite(orgBranchSuite);
			mJsonObject.put("data", r);
			return InterfaceResultUtil.getReturnMapSuccess(mJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return InterfaceResultUtil.getReturnMapError(mJsonObject);
	}
	 
}
