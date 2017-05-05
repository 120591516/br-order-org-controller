package br.order.org.controller.branch;

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

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.common.utils.UUIDUtils;
import br.crm.pojo.branch.OrganizationBranchRest;
import br.crm.service.branch.OrgRestService;
/**
 * 休息日Controller
 * @ClassName: OrgRestController
 * @Description: TODO(休息日Controller)
 * @author adminis
 * @date 2016年9月12日 下午1:54:57
 *
 */
@Controller
@RequestMapping("/orgRest")
public class OrgRestController {
	
	/**
	 * 节假日接口
	 */
    @Autowired
    private OrgRestService orgRestService;
    
    /**
     * 根据门店id查询节假日所有信息
     * @Title: getOrganizationRestById
     * @Description: TODO(根据门店id查询节假日分页信息)
     * @param @param id  门店ID
     * @param @return    设定文件
     * @return JSONObject    返回类型
     * @throws
     */
    @ApiOperation(value = "根据门店id查询节假日所有信息", httpMethod = "GET", response = JSONObject.class, notes = "根据门店id查询节假日所有信息")
    @RequestMapping("/getOrganizationRestById")
    @ResponseBody
    public JSONObject getOrganizationRestById(
    		@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
    		@ApiParam(required = true, name = "id", value = "id,门店id") String id) {
        JSONObject message = new JSONObject();
        if (null == id || "".equals(id)) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        try {
            PageInfo<OrganizationBranchRest> data = orgRestService.getOrganizationRestDayById(id,page,rows);
            List<OrganizationBranchRest> weeks= orgRestService.getOrganizationRestById(id);
            message.put("data", data);
            message.put("weeks", weeks!=null&&weeks.size()>0?weeks.get(0):null);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /**
     * 根据门店id查询节假日所有信息
     * @Title: getOrganizationRestByIdAll
     * @Description: TODO(根据门店id查询节假日所有信息)
     * @param @param id  门店ID
     * @param @return    设定文件
     * @return JSONObject    返回类型
     * @throws
     */
    
    @ApiOperation(value = "根据门店id查询节假日所有信息", httpMethod = "GET", response = JSONObject.class, notes = "根据门店id查询节假日所有信息")
    @RequestMapping("/getOrganizationRestByIdAll")
    @ResponseBody
    public JSONObject getOrganizationRestById(@ApiParam(required = true, name = "id", value = "id,门店id") String id) {
        JSONObject message = new JSONObject();
        if (null == id || "".equals(id)) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        try {
            List<OrganizationBranchRest> data = orgRestService.getOrganizationRestDayByIdAll(id);
            List<OrganizationBranchRest> weeks= orgRestService.getOrganizationRestById(id);
            message.put("data", data);
            message.put("weeks", weeks.get(0));
            return InterfaceResultUtil.getReturnMapSuccess(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
    /**
     * 新增节假日接口
     * @Title: insertOrganizationRest
     * @Description: TODO(新增节假日接口)
     * @param @param organizationBranchRest 节假日对象
     * @param @return    设定文件
     * @return JSONObject    返回类型
     * @throws
     */
    @ApiOperation(value = "新增节假日", httpMethod = "POST", response = JSONObject.class, notes = "新增节假日")
    @RequestMapping(value = "/insertOrganizationRest", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insertOrganizationRest(
            @ApiParam(required = true, name = "organizationBranchRest", value = "organizationBranchRest,新增节假日对象") OrganizationBranchRest organizationBranchRest) {
        JSONObject message = new JSONObject();
        if (null == organizationBranchRest.getBranchId() || "".equals(organizationBranchRest.getBranchId())) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        try {
        	organizationBranchRest.setBranchRestId(UUIDUtils.getId());
            organizationBranchRest.setBranchRestStatus(0);
            organizationBranchRest.setBranchRestCreateTime(new Date());
            organizationBranchRest.setBranchRestEditTime(organizationBranchRest.getBranchRestCreateTime());
            int i = orgRestService.insertOrganizationRest(organizationBranchRest);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }
    
    /**
     * 更新节假日信息
     * @Title: updateOrganizationRest
     * @Description: TODO(更新节假日信息)
     * @param @param organizationBranchRest  节假日对象
     * @param @return    设定文件
     * @return JSONObject    返回类型
     * @throws
     */
    @ApiOperation(value = "修改节假日信息", httpMethod = "POST", response = JSONObject.class, notes = "修改节假日信息")
    @RequestMapping("/updateOrganizationRest")
    @ResponseBody
    public JSONObject updateOrganizationRest(
            @ApiParam(required = true, name = "organizationBranchRest", value = "organizationBranchRest,修改节假日对象") OrganizationBranchRest organizationBranchRest) {
        JSONObject message = new JSONObject();
        try {
            organizationBranchRest.setBranchRestEditTime(new Date());
            int i = orgRestService.updateOrganizationRest(organizationBranchRest);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    

    /**
	 * 根据节假日id删除节假日所有信息
	 * @Title: deleteOrganizationRest
	 * @Description: TODO(根据id删除节假日所有信息)
	 * @param @param id ID
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
    @ApiOperation(value = "根据id删除节假日所有信息", httpMethod = "GET", response = JSONObject.class, notes = "根据id删除节假日所有信息")
    @RequestMapping("/deleteOrganizationRestById")
    @ResponseBody
    public JSONObject deleteOrganizationRestById(@ApiParam(required = true, name = "id", value = "id,id") String id) {
        JSONObject message = new JSONObject();
        if (null == id || "".equals(id)) {
            return InterfaceResultUtil.getReturnMapValidValue(message);
        }
        try {
            OrganizationBranchRest branchRest = new OrganizationBranchRest();
            branchRest.setBranchRestId(id);
            branchRest.setBranchRestStatus(1); 
            branchRest.setBranchRestEditTime(new Date());
            int i = orgRestService.updateOrganizationRest(branchRest);
            message.put("data", i);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

   
}