package br.order.org.controller.examitem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.pojo.examitem.OrganizationExamItemType;
import br.crm.service.examitem.OrgExamItemTypeService;

/** 
* (体检项类型controller) 
* @ClassName: OrgExamItemTypeController 
* @Description: TODO(体检项类型controller) 
* @author 王文腾
* @date 2016年9月13日 上午11:18:45 
*/
@Controller
@RequestMapping("/orgExamItemType")
public class OrgExamItemTypeController {
	/**
	 * {体检项类型service}
	 */
	@Autowired
	private OrgExamItemTypeService orgExamItemTypeService;
	
	/** 
	* @Title: getAllOrgExamItemType 
	* @Description: TODO(获取所有检查项目类型) 
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="获取所有检查项目类型",httpMethod="GET",response=JSONObject.class,notes="获取所有检查项目类型")
	@RequestMapping("/getAllOrgExamItemType")
	@ResponseBody
	public JSONObject getAllOrgExamItemType(){
		JSONObject message = new JSONObject();
		try {
			List<OrganizationExamItemType> list = orgExamItemTypeService.getAllOrgExamItemType();
			message.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: insertOrgExamItemType 
	* @Description: TODO(新增检查项类型) 
	* @param orgExamItemType 检查项类型对象
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="新增检查项类型",httpMethod="POST",response=JSONObject.class,notes="新增检查项类型")
	@RequestMapping(value="/insertOrgExamItemType",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject insertOrgExamItemType(@ApiParam(required=true,name="orgExamItemType",value="orgExamItemType,新增检查项类型")OrganizationExamItemType orgExamItemType){
		JSONObject message = new JSONObject();
		try {
			//插入数据
			int i = orgExamItemTypeService.insertOrgExamItemType(orgExamItemType);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: getOrgExamItemTypeById 
	* @Description: TODO(根据用户id查询检查项类型) 
	* @param orgExamItemTypeId 检查项类型id
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="根据用户id查询检查项类型",httpMethod="GET",response=JSONObject.class,notes="根据用户id查询检查项类型")
	@RequestMapping("/getOrgExamItemTypeById")
	@ResponseBody
	public JSONObject getOrgExamItemTypeById(@ApiParam(required=true,name="orgExamItemTypeId",value="orgExamItemTypeId,检查项类型id")String orgExamItemTypeId){
		JSONObject message = new JSONObject();
		try {
			OrganizationExamItemType orgExamItemType = orgExamItemTypeService.getOrgExamItemTypeById(orgExamItemTypeId);
			message.put("data", orgExamItemType);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: updateOrgExamItemType 
	* @Description: TODO(修改检查项类型) 
	* @param orgExamItemType 修改检查项类型对象
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="修改检查项类型",httpMethod="POST",response=JSONObject.class,notes="修改检查项类型")
	@RequestMapping("/updateOrgExamItemType")
	@ResponseBody
	public JSONObject updateOrgExamItemType(@ApiParam(required=true,name="orgExamItemType",value="orgExamItemType,修改类型对象")OrganizationExamItemType orgExamItemType){
		JSONObject message = new JSONObject();
		try {
			int i = orgExamItemTypeService.updateOrgExamItemType(orgExamItemType);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/** 
	* @Title: deleteOrgExamItemType 
	* @Description: TODO(删除检查项类型) 
	* @param orgExamItemTypeId 检查项类型对象
	* @return JSONObject    
	* @throws 
	*/
	
	@ApiOperation(value="删除检查项类型",httpMethod="GET",response=JSONObject.class,notes="删除检查项类型")
	@RequestMapping("/deleteOrgExamItemType")
	@ResponseBody
	public JSONObject deleteOrgExamItemType(@ApiParam(required=true,name="orgExamItemTypeId",value="orgExamItemTypeId,类型id")String orgExamItemTypeId){
		JSONObject message = new JSONObject();
		try {
			OrganizationExamItemType orgExamItemType = orgExamItemTypeService.getOrgExamItemTypeById(orgExamItemTypeId);
			orgExamItemType.setExamItemTypeStatus(1);
			int i = orgExamItemTypeService.updateOrgExamItemType(orgExamItemType);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
}
