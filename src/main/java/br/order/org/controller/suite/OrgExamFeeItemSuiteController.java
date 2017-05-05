package br.order.org.controller.suite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import br.crm.common.utils.UUIDUtils;
import br.crm.pojo.examfeeitem.OrganizationExamFeeItem;
import br.crm.pojo.suite.OrganizationExamSuiteFeeItem;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemService;
import br.crm.service.suite.OrgExamFeeItemSuiteService;
import br.crm.vo.suite.OrgExamSuiteVo;

/** 
* (收费项-套餐关联controller)
* @ClassName: OrgExamFeeItemSuiteController 
* @Description: TODO(收费项-套餐关联controller) 
* @author 王文腾
* @date 2016年9月13日 上午10:19:07 
*/
@Controller
@RequestMapping("/orgExamFeeItemSuite")
public class OrgExamFeeItemSuiteController {
    /**
     * {收费项-套餐service}
     */
    @Autowired
    private OrgExamFeeItemSuiteService orgExamFeeItemSuiteService;

    /**
     * {收费项service}
     */
    @Autowired
    private OrgExamFeeItemService orgExamFeeItemService;

    /** 
    * @Title: getOrgExamFeeItemSuiteByPage 
    * @Description: TODO(分页收费体检套餐项列表) 
    * @param page 当前页数
    * @param rows 每页显示行数
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "分页收费体检套餐项列表", httpMethod = "GET", response = JSONObject.class, notes = "分页收费体检套餐项列表")
    @RequestMapping("/getOrgExamFeeItemSuiteByPage")
    @ResponseBody
    public JSONObject getOrgExamFeeItemSuiteByPage(
            @ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
            @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows) {
        JSONObject message = new JSONObject();
        try {
            PageInfo<OrganizationExamSuiteFeeItem> pageInfo = orgExamFeeItemSuiteService
                    .getOrgExamFeeItemSuiteByPage(page, rows);
            message.put("data", pageInfo);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: insertOrgExamFeeItemSuite 
    * @Description: TODO(收费体检套餐项目添加) 
    * @param orgExamFeeItemSuite 收费体检项目对象
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "收费体检套餐项目添加", httpMethod = "GET", response = JSONObject.class, notes = "收费体检套餐项目添加")
    @RequestMapping("/insertOrgExamFeeItemSuite")
    @ResponseBody
    public JSONObject insertOrgExamFeeItemSuite(
            @ApiParam(required = true, name = "orgExamFeeItemSuite", value = "orgExamFeeItemSuite,收费体检套餐项对象") OrganizationExamSuiteFeeItem orgExamFeeItemSuite) {
        JSONObject message = new JSONObject();
        try {
            int i = orgExamFeeItemSuiteService.insertOrgExamFeeItemSuite(orgExamFeeItemSuite);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 
    * @Title: getOrgExamFeeItemSuiteById 
    * @Description: TODO(根据id查找收费体检套餐信息) 
    * @param examFisId 收费体检套餐id
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "根据id查找收费体检套餐信息", httpMethod = "GET", response = JSONObject.class, notes = "根据id查找收费体检套餐信息")
    @RequestMapping("/getOrgExamFeeItemSuiteById")
    @ResponseBody
    public JSONObject getOrgExamFeeItemSuiteById(
            @ApiParam(required = true, name = "examFisId", value = "examFisId,收费套餐项id") String examFisId) {
        JSONObject message = new JSONObject();
        try {
        	OrganizationExamSuiteFeeItem orgExamFeeItemSuite = orgExamFeeItemSuiteService
                    .getOrgExamFeeItemSuiteById(examFisId);
            message.put("data", orgExamFeeItemSuite);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: updateOrgExamFeeItemSuite 
    * @Description: TODO(修改收费体检套餐项目) 
    * @param orgExamFeeItemSuite 收费项-体检套餐对象
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "修改收费体检套餐项目", httpMethod = "POST", response = JSONObject.class, notes = "修改收费体检套餐项目")
    @RequestMapping("/updateOrgExamFeeItemSuite")
    @ResponseBody
    public JSONObject updateOrgExamFeeItemSuite(
            @ApiParam(required = true, name = "orgExamFeeItemSuite", value = "orgExamFeeItemSuite,收费体检套餐项对象") OrganizationExamSuiteFeeItem orgExamFeeItemSuite) {
        JSONObject message = new JSONObject();
        try {
            int i = orgExamFeeItemSuiteService.updateOrgExamFeeItemSuite(orgExamFeeItemSuite);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: deleteOrgExamFeeItemSuite 
    * @Description: TODO(删除收费体检套餐项目) 
    * @param examFisId 主键id
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "删除收费体检套餐项目", httpMethod = "GET", response = JSONObject.class, notes = "删除收费体检套餐项目")
    @RequestMapping("/deleteOrgExamFeeItemSuite")
    @ResponseBody
    public JSONObject deleteOrgExamFeeItemSuite(
            @ApiParam(required = true, name = "examFisId", value = "examFisId,收费体检套餐项目id") String examFisId) {
        JSONObject message = new JSONObject();
        try {
        	OrganizationExamSuiteFeeItem orgExamFeeItemSuite = orgExamFeeItemSuiteService
                    .getOrgExamFeeItemSuiteById(examFisId);
            orgExamFeeItemSuite.setExamFisStatus(1);
            int i = orgExamFeeItemSuiteService.updateOrgExamFeeItemSuite(orgExamFeeItemSuite);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

 
    /** 
    * @Title: batchInsertOrgExamFeeItemSuite 
    * @Description: TODO(批量添加套餐收费项关联信息) 
    * @param examSuiteId 套餐id
    * @param examFeeItemId 收费项id串(多条收费项id逗号隔开)
    * @return JSONObject    
    * @throws 
    */
    
    @ApiOperation(value = "批量添加套餐收费项关联信息", httpMethod = "GET", response = JSONObject.class, notes = "批量添加套餐收费项关联信息")
    @RequestMapping("/batchInsertOrgExamFeeItemSuite")
    @ResponseBody
    public JSONObject batchInsertOrgExamFeeItemSuite(
            @ApiParam(required = true, name = "examSuiteId", value = "examSuiteId,套餐id") String examSuiteId,
            @ApiParam(required = true, name = "examFeeItemDetailId", value = "examFeeItemDetailId,收费项-体检项用户中间表id串") String examFeeItemDetailId) {
        JSONObject message = new JSONObject();
        try {
            if(StringUtils.isNotEmpty(examFeeItemDetailId)){
                int i = orgExamFeeItemSuiteService.insertOrgExamFeeItemSuite(examSuiteId,examFeeItemDetailId);
                message.put("data", i);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
}