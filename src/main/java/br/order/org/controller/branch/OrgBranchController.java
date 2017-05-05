package br.order.org.controller.branch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import br.crm.pojo.branch.OrganizationBranch;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.branch.OrgBranchService;
import br.crm.vo.branch.OrganizationBranchVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.common.utils.InterfaceResultUtil;
import br.order.org.controller.common.CommonController;

/**
 * 机构门店Controller
 * 
 * @ClassName: OrgBranchController
 * @Description: TODO(机构门店Controller)
 * @author adminis
 * @date 2016年9月13日 下午3:48:47
 *
 */
@Controller
@RequestMapping("/orgBranch")
public class OrgBranchController {

    /**
     * 机构Service
     */
    @Autowired
    private OrgBranchService orgBranchService;

    /**
     * Session
     */
    @Autowired
    private CommonController commonController;

    /**
     * 门店搜索 @Title: searchOrgBranch @Description: TODO(门店搜索) @param @param page
     * 当前页码 @param @param rows 每页显示的条数 @param @param organizationBranchvo
     * 门店VO对象 @param @return 设定文件 @return JSONObject 返回类型 @throws
     */
    @ApiOperation(value = "门店搜索", httpMethod = "POST", response = JSONObject.class, notes = "门店搜索")
    @RequestMapping("/searchOrgBranch")
    @ResponseBody
    public JSONObject searchOrgBranch(@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page, @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows, @ApiParam(name = "organizationBranch", value = "organizationBranch,搜索对象") OrganizationBranchVo organizationBranchvo) {
        JSONObject message = new JSONObject();
        try {
            OrganizationUser orgUser = commonController.getUserBySession();
            String orgId = orgUser.getOrgId();
            if (StringUtils.isNotEmpty(orgId)) {
                PageInfo<OrganizationBranchVo> pageInfo = orgBranchService.getOrganizationBranchListByOrgId(orgId, page, rows, organizationBranchvo);
                message.put("data", pageInfo);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 添加门店 @Title: addOrganizationBranch @Description: TODO(添加门店) @param @param
     * organizationBranchVo 门店VO对象 @param @return 设定文件 @return JSONObject
     * 返回类型 @throws
     */
    @ApiOperation(value = "新增门店", httpMethod = "POST", response = JSONObject.class, notes = "新增门店")
    @RequestMapping(value = "/addOrganizationBranch", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addOrganizationBranch(@ApiParam(required = true, name = "organizationBranchVo", value = "organizationBranchVo,新增门店对象") OrganizationBranchVo organizationBranchVo) {
        JSONObject message = new JSONObject();
        try {
            Long imgId = null;
            List<Long> list = new ArrayList<Long>();
            if (StringUtils.isNotEmpty(organizationBranchVo.getImgId())) {
                String imgAll = organizationBranchVo.getImgId();// 1A,2,3
                String[] str = imgAll.split(",");
                for (String s : str) {
                    if (!s.contains("A")) {
                        list.add(Long.parseLong(s));
                    }
                    else {
                        String ss = s.substring(0, s.length() - 1);
                        imgId = Long.parseLong(ss);
                    }
                }
            }
            // 添加门店的基本信息
            organizationBranchVo.setOrgId(commonController.getUserBySession().getOrgId());
            String branchId = orgBranchService.insertOrganizationBranch(organizationBranchVo);
            // 添加中间表信息
            if (StringUtils.isNotEmpty(branchId)) {
                // 添加主信息
                if (null != imgId) {
                    orgBranchService.insertMasterImgAndBranch(organizationBranchVo, branchId, imgId);
                }
                // 添加从 信息
                int i = orgBranchService.insertImgAndBranch(organizationBranchVo, branchId, list);
                message.put("data", i);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
            else {
                message.put("data", branchId);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 根据门店id查询门店所有信息 @Title: getOrgBranchById @Description:
     * TODO(根据门店id查询门店所有信息) @param @param id 门店ID @param @return 设定文件 @return
     * JSONObject 返回类型 @throws
     */
    @ApiOperation(value = "根据门店id查询门店所有信息", httpMethod = "GET", response = JSONObject.class, notes = "根据门店id查询门店所有信息")
    @RequestMapping("/getOrgBranchById")
    @ResponseBody
    public JSONObject getOrgBranchById(@ApiParam(required = true, name = "id", value = "id,门店id") String id) {
        JSONObject message = new JSONObject();
        if (null == id) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        if (id.equals("0")) {
            message.put("data", "");
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        try {
            OrganizationBranchVo organizationBranchVo = orgBranchService.getOrganizationBranchById(id);
            message.put("data", organizationBranchVo);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 修改门店信息 @Title: updateOrgBranch @Description: TODO(修改门店信息) @param @param
     * organizationBranch 门店对象 @param @return 设定文件 @return JSONObject
     * 返回类型 @throws
     */
    @ApiOperation(value = "修改门店信息", httpMethod = "POST", response = JSONObject.class, notes = "修改门店信息")
    @RequestMapping("/updateOrgBranch")
    @ResponseBody
    public JSONObject updateOrgBranch(@ApiParam(required = true, name = "organizationBranch", value = "organizationBranch,修改门店对象") OrganizationBranchVo organizationBranch) {
        JSONObject message = new JSONObject();
        if (StringUtils.isEmpty(organizationBranch.getBranchName())) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        try {
            organizationBranch.setEdittime(new Date());
            int i = orgBranchService.updateOrganizationBranch(organizationBranch);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 删除门店信息 @Title: deleteOrgBranch @Description: TODO(删除门店信息) @param @param
     * id 门店ID @param @return 设定文件 @return JSONObject 返回类型 @throws
     */
    @ApiOperation(value = "删除门店信息", httpMethod = "GET", response = JSONObject.class, notes = "删除门店信息")
    @RequestMapping("/deleteOrgBranch")
    @ResponseBody
    public JSONObject deleteOrgBranch(@ApiParam(required = true, name = "id", value = "id,门店id") String id) {
        JSONObject message = new JSONObject();
        if (null == id || id.equals(0)) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        try {
            OrganizationBranchVo organizationBranchVo = orgBranchService.getOrganizationBranchById(id);
            organizationBranchVo.setStatus(1);
            int i = orgBranchService.updateOrganizationBranch(organizationBranchVo);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 门店列表 @Title: getOrganizationBranchAll @Description:
     * TODO(门店列表) @param @return 设定文件 @return JSONObject 返回类型 @throws
     */
    @ApiOperation(value = "门店列表", httpMethod = "GET", response = JSONObject.class, notes = "门店列表")
    @RequestMapping("/getOrganizationBranchAll")
    @ResponseBody
    public JSONObject getOrganizationBranchAll() {
        JSONObject message = new JSONObject();
        try {
            List<OrganizationBranch> list = orgBranchService.getOrganizationBranchAll();
            message.put("data", list);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 根据机构id查可用门店列表 @Title: getOrganizationBranchOpenByOrgId @Description:
     * TODO(根据机构id查可用门店列表) @param @param page 当前页码 @param @param rows
     * 每页显示的条数 @param @param orgid 机构ID @param @return 设定文件 @return JSONObject
     * 返回类型 @throws
     */
    @ApiOperation(value = "根据机构id查可用门店列表", httpMethod = "GET", response = JSONObject.class, notes = "根据机构id查可用门店列表")
    @RequestMapping("/getOrganizationBranchOpenByOrgId")
    @ResponseBody
    public JSONObject getOrganizationBranchOpenByOrgId(@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page, @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo user = commonController.getUserBySession();
            if (null != user) {

                List<OrganizationBranch> list = orgBranchService.getOrganizationBranchAllByOrgIdOpen(user.getOrgId(), page, rows);
                message.put("data", list);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 
     * @Title: getOrganizationBranchByUser
     * @Description: TODO(添加用户时,查询可用门店)
     * @param @return    设定文件
     * @return JSONObject    返回类型
     * @throws
     */
    @ApiOperation(value = "添加用户时,查询可用门店", httpMethod = "GET", response = JSONObject.class, notes = "添加用户时,查询可用门店")
    @RequestMapping("/getOrganizationBranch")
    @ResponseBody
    public JSONObject getOrganizationBranchByUser() {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo user = commonController.getUserBySession();
            if (null != user) {
                List<OrganizationBranch> list = orgBranchService.getOrganizationBranchByUser(user.getOrgId());
                message.put("data", list);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "查询当前机构下所有门店", httpMethod = "GET", response = JSONObject.class, notes = "查询当前机构下所有门店")
    @RequestMapping("/getBranchNameByOrgId")
    @ResponseBody
    public JSONObject getBranchNameByOrgId() {
        JSONObject jsonObject = new JSONObject();
        try {
            OrgUserVo userVo = commonController.getUserBySession();
            if (null != userVo) {
                List<Map<String, String>> map = orgBranchService.getBranchNameByOrgId(userVo.getOrgId());
                jsonObject.put("data", map);
                return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return InterfaceResultUtil.getReturnMapError(jsonObject);
    }

}
