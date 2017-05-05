package br.order.org.controller.examfeeitem;

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
import br.crm.pojo.examfeeitem.OrganizationExamFeeItemClass;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemClassService;


/** 
* (收费项目分类controller)
* @ClassName: OrgExamFeeItemClassController 
* @Description: TODO(收费项目分类controller) 
* @author 王文腾
* @date 2016年9月12日 上午11:56:28 
*  
*/
@Controller
@RequestMapping("/OrgExamFeeItemClass")
public class OrgExamFeeItemClassController {
    /**
     * {收费项目分类service}
     */
    @Autowired
    private OrgExamFeeItemClassService orgExamFeeItemClassService;

    /** 
    * @Title: getAllOrgExamFeeItemClass 
    * @Description: TODO(获取所有收费项目分类列表) 
    * @param  
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "获取所有收费项目分类列表", httpMethod = "GET", response = JSONObject.class, notes = "获取所有收费项目分类列表")
    @RequestMapping("/getAllOrgExamFeeItemClass")
    @ResponseBody
    public JSONObject getAllOrgExamFeeItemClass() {
        JSONObject message = new JSONObject();
        try {
            List<OrganizationExamFeeItemClass> list = orgExamFeeItemClassService.getAllOrgExamItemClass();
            message.put("data", list);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
   
    /** 
    * @Title: insertOrgExamFeeItemClass 
    * @Description: TODO(新增收费项目分类项) 
    * @param orgExamFeeItemClass 收费项对象
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "新增收费项目分类项", httpMethod = "POST", response = JSONObject.class, notes = "新增收费项目分类项")
    @RequestMapping(value = "/insertOrgExamFeeItemClass", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertOrgExamFeeItemClass(
            @ApiParam(required = true, name = "organizationExamFeeItemClass", value = "orgExamFeeItemClass,新增收费项目分类项") OrganizationExamFeeItemClass orgExamFeeItemClass) {
        JSONObject message = new JSONObject();
        try {
            int i = orgExamFeeItemClassService.insertOrgExamFeeItemClass(orgExamFeeItemClass);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: getOrgExamFeeItemClassById 
    * @Description: TODO(根据用户id查询收费项分类信息) 
    * @param id 收费项id
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "根据用户id查询收费项分类信息", httpMethod = "GET", response = JSONObject.class, notes = "根据用户id查询收费项分类信息")
    @RequestMapping("/getOrgExamFeeItemClassById")
    @ResponseBody
    public JSONObject getOrgExamFeeItemClassById(
            @ApiParam(required = true, name = "id", value = "id,收费项分类信息id") String id) {
        JSONObject message = new JSONObject();
        try {
            OrganizationExamFeeItemClass orgExamFeeItemClass = orgExamFeeItemClassService
                    .getOrgExamFeeItemClassById(id);
            message.put("data", orgExamFeeItemClass);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: updateOrgExamFeeItemClass 
    * @Description: TODO(修改收费项分类信息) 
    * @param orgExamFeeItemClass 收费项信息对象
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "修改收费项分类信息", httpMethod = "POST", response = JSONObject.class, notes = "修改收费项分类信息")
    @RequestMapping("/updateOrgExamFeeItemClass")
    @ResponseBody
    public JSONObject updateOrgExamFeeItemClass(
            @ApiParam(required = true, name = "orgExamFeeItemClass", value = "orgExamFeeItemClass,收费项目分类对象") OrganizationExamFeeItemClass orgExamFeeItemClass) {
        JSONObject message = new JSONObject();
        try {
            int i = orgExamFeeItemClassService.updateOrgExamFeeItemClass(orgExamFeeItemClass);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: deleteOrgExamFeeItemClass 
    * @Description: TODO(删除收费项分类信息) 
    * @param id 收费项id
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "删除收费项分类信息", httpMethod = "GET", response = JSONObject.class, notes = "删除收费项分类信息")
    @RequestMapping("/deleteOrgExamFeeItemClass")
    @ResponseBody
    public JSONObject deleteOrgExamFeeItemClass(
            @ApiParam(required = true, name = "id", value = "id,收费项目分类id") String id) {
        JSONObject message = new JSONObject();
        try {
            OrganizationExamFeeItemClass orgExamFeeItemClass = orgExamFeeItemClassService
                    .getOrgExamFeeItemClassById(id);
            orgExamFeeItemClass.setStatus(1);
            int i = orgExamFeeItemClassService.updateOrgExamFeeItemClass(orgExamFeeItemClass);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
}
