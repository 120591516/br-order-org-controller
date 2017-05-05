package br.order.org.controller.examitemvalue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.pojo.examitemvalue.OrganizationExamItemValue;
import br.crm.service.examitemvalue.OrgExamItemValueService;
import br.crm.vo.examitemvalue.OrgExamItemValueVo;

/** 
* (体检项特征词controller)
* @ClassName: OrgExamItemValueController 
* @Description: TODO(体检项特征词controller) 
* @author 王文腾
* @date 2016年9月13日 上午11:30:24 
*/
@Controller
@RequestMapping("/orgExamItemValue")
public class OrgExamItemValueController {
	/**
	 * {体检项特征词service}
	 */
	@Autowired
	private OrgExamItemValueService orgExamItemValueService;
	
	/** 
	* @Title: getOrgExamItemValueByPage 
	* @Description: TODO(分页获取检查项体征词表) 
	* @param page 页数
	* @param rows 行数
	* @param orgExamItemValueVo 条件查询对象
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="分页获取检查项体征词表",httpMethod="GET",response=JSONObject.class,notes="分页获取检查项体征词表")
	@RequestMapping("/getOrgExamItemValueByPage")
	@ResponseBody
	public JSONObject getOrgExamItemValueByPage(@ApiParam(required=true,name="page",value="page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required=true,name="rows",value="rows,每页显示条数")@RequestParam(value = "rows", defaultValue = "10", required = true)Integer rows,
			@ApiParam(required=true,name="orgExamItemValue",value="orgExamItemValue,条件查询参数对象")OrgExamItemValueVo orgExamItemValueVo){
		JSONObject message = new JSONObject();
		try {
			PageInfo<OrgExamItemValueVo> pageInfo = orgExamItemValueService.getOrgExamItemValueByPage(orgExamItemValueVo,page,rows);
			message.put("data", pageInfo);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: insertOrgExamItemValue 
	* @Description: TODO(新增检查项体征词) 
	* @param orgExamItemValue 新增检查项体征词对象
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="新增检查项体征词",httpMethod="POST",response=JSONObject.class,notes="新增检查项体征词")
	@RequestMapping(value="/insertOrgExamItemValue",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject insertOrgExamItemValue(@ApiParam(required=true,name="orgExamItemValue",value="orgExamItemValue,新增检查项体征词")OrganizationExamItemValue orgExamItemValue){
		JSONObject message = new JSONObject();
		try {
			//插入数据
			int i = orgExamItemValueService.insertOrgExamItemValue(orgExamItemValue);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: getOrgExamItemValueById 
	* @Description: TODO(根据用户id查询体征词信息) 
	* @param orgExamItemValueId 体检项特征词id
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="根据用户id查询体征词信息",httpMethod="GET",response=JSONObject.class,notes="根据用户id查询体征词信息")
	@RequestMapping("/getOrgExamItemValueById")
	@ResponseBody
	public JSONObject getOrgExamItemValueById(@ApiParam(required=true,name="orgExamItemValueId",value="orgExamItemValueId,体征词id")String orgExamItemValueId){
		JSONObject message = new JSONObject();
		try {
			OrganizationExamItemValue orgExamItemValue = orgExamItemValueService.getOrgExamItemValueById(orgExamItemValueId);
			message.put("data", orgExamItemValue);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: updateOrgExamItemValue 
	* @Description: TODO(修改体征词) 
	* @param orgExamItemValue 修改体征词对象
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="修改体征词",httpMethod="POST",response=JSONObject.class,notes="修改体征词")
	@RequestMapping("/updateOrgExamItemValue")
	@ResponseBody
	public JSONObject updateOrgExamItemValue(@ApiParam(required=true,name="orgExamItemValue",value="orgExamItemValue,体征词对象")OrganizationExamItemValue orgExamItemValue){
		JSONObject message = new JSONObject();
		try {
			int i = orgExamItemValueService.updateOrgExamItemValue(orgExamItemValue);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: deleteOrgExamItemValue 
	* @Description: TODO(删除体征词) 
	* @param orgExamItemValueId 特征词id
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="删除体征词",httpMethod="GET",response=JSONObject.class,notes="删除体征词")
	@RequestMapping("/deleteOrgExamItemValue")
	@ResponseBody
	public JSONObject deleteOrgExamItemValue(@ApiParam(required=true,name="orgExamItemValueId",value="orgExamItemValueId,体征词id")String orgExamItemValueId){
		JSONObject message = new JSONObject();
		try {
			OrganizationExamItemValue orgExamItemValue = new OrganizationExamItemValue();
			orgExamItemValue.setExamItemValueId(orgExamItemValueId);
			orgExamItemValue.setExamItemValueStatus(1);
			int i = orgExamItemValueService.updateOrgExamItemValue(orgExamItemValue);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
}
