package br.order.org.controller.examfeeitem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.common.utils.StringTransCodeUtil;
import br.crm.pojo.examfeeitem.OrganizationExamFeeItem;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemService;
import br.crm.service.suite.OrgExamFeeItemSuiteService;
import br.crm.vo.examfeeitem.OrgExamFeeItemQu;
import br.crm.vo.examfeeitem.OrgExamFeeItemVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.common.utils.TransCoding;
import br.order.org.controller.common.CommonController;

/**
 * (收费项目controller)
 * 
 * @ClassName: OrgExamFeeItemController
 * @Description: TODO(收费项目controller)
 * @author 王文腾
 * @date 2016年9月12日 下午1:54:05
 */
@Controller
@RequestMapping("/OrgExamFeeItem")
public class OrgExamFeeItemController {
    /**
     * {收费项目service}
     */
    @Autowired
    private OrgExamFeeItemService orgExamFeeItemService;
    
    @Autowired
    private OrgExamFeeItemSuiteService orgExamFeeItemSuiteService;
    @Autowired
    private CommonController commonController;
    /**
     * @Title: getOrgExamFeeItemByPage @Description: TODO(分页获取收费项列表) @param page
     * 页数 @param rows 每页显示行数 @param orgExamFeeItemQu 分页条件查询对象 @return
     * JSONObject @throws
     */

    @ApiOperation(value = "分页收费项列表", httpMethod = "POST", response = JSONObject.class, notes = "分页获取体检列表")
    @RequestMapping("/getOrgExamFeeItemByPage")
    @ResponseBody
    public JSONObject getOrgExamFeeItemByPage(
            @ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
            @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
            @ApiParam(required = true, name = "orgExamFeeItem", value = "orgExamFeeItem,条件查询对象") OrgExamFeeItemQu orgExamFeeItemQu) {
        JSONObject message = new JSONObject();
        try { 
        	OrgUserVo organizationUser = commonController.getUserBySession();
			String orgId = organizationUser.getOrgId();
			orgExamFeeItemQu.setOrgId(orgId);
			orgExamFeeItemQu = (OrgExamFeeItemQu) StringTransCodeUtil.transCode(orgExamFeeItemQu);
            PageInfo<OrganizationExamFeeItem> pageInfo = orgExamFeeItemService.getOrgExamItemByPage(orgExamFeeItemQu,
                    page, rows);
            message.put("data", pageInfo);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * @Title: insertOrgExamFeeItem @Description: TODO(新增收费项) @param
     * organizationExamFeeItem 收费项对象 @return JSONObject @throws
     */

    @ApiOperation(value = "新增收费项", httpMethod = "POST", response = JSONObject.class, notes = "新增收费项")
    @RequestMapping(value = "/insertOrgExamFeeItem", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertOrgExamFeeItem(
            @ApiParam(required = true, name = "insertOrgExamFeeItem", value = "insertOrgExamFeeItem,新增收费项对象") OrganizationExamFeeItem organizationExamFeeItem) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            organizationExamFeeItem.setOrgId(brUser.getOrgId());
            int i = orgExamFeeItemService.insertOrganizationExamFeeItem(organizationExamFeeItem);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * @Title: getOrgExamFeeItemById @Description: TODO(根据收费项id查询收费项所有信息) @param
     * id 收费项id @return JSONObject @throws
     */

    @ApiOperation(value = "根据收费项id查询收费项所有信息", httpMethod = "GET", response = JSONObject.class, notes = "根据收费项id查询收费项所有信息")
    @RequestMapping("/getOrgExamFeeItemById")
    @ResponseBody
    public JSONObject getOrgExamFeeItemById(@ApiParam(required = true, name = "id", value = "id,收费项id") String id) {
        JSONObject message = new JSONObject();
        try {
            OrganizationExamFeeItem organizationExamFeeItem = orgExamFeeItemService.getOrganizationExamFeeItemById(id);
            message.put("data", organizationExamFeeItem);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * @Title: deleteOrgExamFeeItemById @Description: TODO(删除收费项信息) @param id
     * 收费项id @return JSONObject @throws
     */

    @ApiOperation(value = "删除收费项信息", httpMethod = "GET", response = JSONObject.class, notes = "删除收费项信息")
    @RequestMapping("/deleteOrgExamFeeItemById")
    @ResponseBody
    public JSONObject deleteOrgExamFeeItemById(@ApiParam(required = true, name = "id", value = "id,收费项id") String id) {
        JSONObject message = new JSONObject();
        try {
            OrganizationExamFeeItem organizationExamFeeItem = orgExamFeeItemService.getOrganizationExamFeeItemById(id);
            organizationExamFeeItem.setStatus(1);
            int i = orgExamFeeItemSuiteService.countExamFeeItemRelation(id);
            if (i < 0) {
                message.put("data", "该项存在关联关系，尚不能删除");
                return InterfaceResultUtil.getReturnMapError(message);
            }
            else {
                i = orgExamFeeItemService.updateOrganizationExamFeeItemById(organizationExamFeeItem);
                message.put("data", i);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * @Title: updateOrgExamFeeItem @Description: TODO(修改收费项信息) @param
     * organizationExamFeeItem 收费项对象 @return JSONObject @throws
     */

    @ApiOperation(value = "修改收费项信息", httpMethod = "POST", response = JSONObject.class, notes = "修改收费项信息")
    @RequestMapping("/updateOrgExamFeeItem")
    @ResponseBody
    public JSONObject updateOrgExamFeeItem(
            @ApiParam(required = true, name = "OrganizationExamFeeItem", value = "OrganizationExamFeeItem,修改收费项对象") OrganizationExamFeeItem organizationExamFeeItem) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            organizationExamFeeItem.setOrgId(brUser.getOrgId());
            int i = orgExamFeeItemService.updateOrganizationExamFeeItemById(organizationExamFeeItem);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * @Title: getOrgExamFeeItemBySId @Description: TODO(根据套餐id查询收费项信息) @param
     * orgExamSuiteId 套餐对象 @return JSONObject @throws
     */

    @ApiOperation(value = "根据套餐id查询收费项信息", httpMethod = "GET", response = JSONObject.class, notes = "根据套餐id查询收费项信息")
    @RequestMapping("/getOrgExamFeeItemBySId")
    @ResponseBody
    public JSONObject getOrgExamFeeItemBySId(
            @ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
            @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
            @ApiParam(required = true, name = "orgExamSuiteId", value = "orgExamSuiteId,套餐id") String orgExamSuiteId,
            @ApiParam(required = true, name = "orgId", value = "orgId,机构id") String orgId) {
        JSONObject message = new JSONObject();
        try {
            if (orgExamSuiteId != null) {
                PageInfo<OrgExamFeeItemVo> pageInfo = orgExamFeeItemSuiteService.getOrgExamFeeItemBySId(orgId,orgExamSuiteId,
                        page, rows);
                message.put("data", pageInfo);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
}
