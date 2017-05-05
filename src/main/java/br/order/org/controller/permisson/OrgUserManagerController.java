package br.order.org.controller.permisson;

import java.util.Date;
import java.util.List;

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
import br.crm.pojo.dept.OrganizationDept;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.pojo.permission.OrganizationUserManager;
import br.crm.service.branch.OrgBranchService;
import br.crm.service.dept.OrgDeptService;
import br.crm.service.permission.OrgUserManagerService;
import br.crm.service.permission.OrganizationUserService;
import br.crm.vo.branch.OrganizationBranchVo;
import br.crm.vo.permission.OrgUserVo;
import br.crm.vo.permission.OrganizationUserManagerVo;
import br.order.common.utils.InterfaceResultUtil;
import br.order.org.controller.common.CommonController;

/**
 * 
 * @ClassName: OrgUserManagerController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王文腾
 * @date 2016年11月17日 上午10:05:15
 */
@Controller
@RequestMapping("/orgUserManager")
public class OrgUserManagerController {
    @Autowired
    private OrgUserManagerService orgUserManagerService;

    @Autowired
    private OrgDeptService orgDeptService;

    @Autowired
    private OrganizationUserService organizationUserService;

    @Autowired
    private OrgBranchService orgBranchService;

    @Autowired
    private CommonController commonController;

    @ApiOperation(value = "新增管理用户", httpMethod = "POST", response = JSONObject.class, notes = "新增管理用户")
    @RequestMapping(value = "/insertOrgUserManager", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertOrgUserManager(
            @ApiParam(required = true, name = "orgUserManager", value = "orgUserManager,新增管理用户对象") OrganizationUserManager orgUserManager) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            OrganizationUser orgUser = organizationUserService.getOrgUser(orgUserManager.getUserId());
            orgUserManager.setOrgId(orgUser.getOrgId());
            orgUserManager.setBranchId(orgUser.getOrgBranchId());
            orgUserManager.setUserManagerEditPersonId(brUser.getUserId());
            orgUserManager.setUserManagerEditPlat(2);
            if (orgUserManager.getUserManagerType().intValue()==1) {
                OrganizationBranchVo organizationBranchVo = orgBranchService.getOrganizationBranchById(orgUserManager.getUserManagerObjId());
                orgUserManager.setUserManagerObjName(organizationBranchVo.getBranchName());
            }else {
                OrganizationDept orgDept = orgDeptService.getOrganizationDeptByDeptId(orgUserManager.getUserManagerObjId());
                orgUserManager.setUserManagerObjName(orgDept.getDepartName());
            }
            int i = orgUserManagerService.insertOrgUserManager(orgUserManager);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "分页获取管理员信息", httpMethod = "GET", response = JSONObject.class, notes = "分页获取管理员信息")
    @RequestMapping(value = "/getOrgUserManagerByPage")
    @ResponseBody
    public JSONObject getOrgUserManagerByPage(
            @ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
            @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            String orgId = brUser.getOrgId();
            PageInfo<OrganizationUserManagerVo> pageInfo = orgUserManagerService.getOrgUserManagerByPage(page, rows,
                    orgId);
            message.put("data", pageInfo);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "根据用户id查询", httpMethod = "GET", response = JSONObject.class, notes = "根据用户id查询")
    @RequestMapping(value = "/getOrgUserManagerByUserId")
    @ResponseBody
    public JSONObject getOrgUserManagerByUserId(
            @ApiParam(required = true, name = "userId", value = "userId,管理用户id") String userId) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            String orgId = brUser.getOrgId();
            List<OrganizationUserManagerVo> orgUserManager = orgUserManagerService.getOrgUserManagerByUserId(userId,
                    orgId);
            
            message.put("data", orgUserManager);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "修改管理用户id查询用户对象", httpMethod = "POST", response = JSONObject.class, notes = "修改管理用户id查询用户对象")
    @RequestMapping(value = "/updateOrgUserManager", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateOrgUserManager(
            @ApiParam(required = true, name = "orgUserManager", value = "orgUserManager,新增管理用户对象") OrganizationUserManager orgUserManager) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            if (orgUserManager.getUserManagerType().intValue()==1) {
                OrganizationBranchVo organizationBranchVo = orgBranchService.getOrganizationBranchById(orgUserManager.getUserManagerObjId());
                orgUserManager.setUserManagerObjName(organizationBranchVo.getBranchName());
            }else {
                OrganizationDept orgDept = orgDeptService.getOrganizationDeptByDeptId(orgUserManager.getUserManagerObjId());
                orgUserManager.setUserManagerObjName(orgDept.getDepartName());
            }
            orgUserManager.setUserManagerEditPersonId(brUser.getUserId());
            orgUserManager.setUserManagerEditPlat(2);
            int i = orgUserManagerService.updateOrgUserManager(orgUserManager);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "删除管理用户id查询用户对象", httpMethod = "GET", response = JSONObject.class, notes = "删除管理用户id查询用户对象")
    @RequestMapping(value = "/deleteOrgUserManager")
    @ResponseBody
    public JSONObject deleteOrgUserManager(
            @ApiParam(required = true, name = "userManagerId", value = "orgUserManager,管理用户id") String userManagerId) {
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            OrganizationUserManager orgUserManager = new OrganizationUserManager();
            orgUserManager.setOrgId(brUser.getOrgId());
            orgUserManager.setUserManagerEditPersonId(brUser.getUserId());
            orgUserManager.setUserManagerId(userManagerId);
            orgUserManager.setUserManagerEditTime(new Date());
            orgUserManager.setUserManagerStatus(1);
            orgUserManager.setUserManagerEditPlat(2);
            int i = orgUserManagerService.updateOrgUserManager(orgUserManager);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "根据管理用户主键id查询", httpMethod = "GET", response = JSONObject.class, notes = "根据管理用户主键id查询")
    @RequestMapping(value = "/getOrgUserManager")
    @ResponseBody
    public JSONObject getOrgUserManager(
            @ApiParam(required = true, name = "userManagerId", value = "userManagerId,管理用户id") String userManagerId) {
        JSONObject message = new JSONObject();
        try {
            OrganizationUserManagerVo orgUserManager = orgUserManagerService.getOrgUserManager(userManagerId);
            message.put("data", orgUserManager);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    @ApiOperation(value = "获取用户下的门店科室列表", httpMethod = "GET", response = JSONObject.class, notes = "获取用户下的门店科室列表")
    @RequestMapping(value = "/getNameListByUserId")
    @ResponseBody
    public JSONObject getNameListByUserId(
            @ApiParam(required = true, name = "userId", value = "userId,管理用户id") String userId,
            @ApiParam(required = true, name = "type", value = "type,列表类型") String type) {
        JSONObject message = new JSONObject();
        try {
            //查询门店类列表
            OrganizationUser orgUser = organizationUserService.getOrgUser(userId);
            if (type.equals("1")) {
                List<OrganizationBranch> list = orgBranchService.getOrganizationBranchByUser(orgUser.getOrgId());
                message.put("data", list);
            }
            else {
                List<OrganizationDept> list = orgDeptService.getOrgDeptByBranid(orgUser.getOrgBranchId());
                message.put("data", list);
            }
            return InterfaceResultUtil.getReturnMapSuccess(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
}
