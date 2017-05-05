package br.order.org.controller.branchdept;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

import br.crm.common.utils.CommonUtils;
import br.crm.common.utils.InterfaceResultUtil;
import br.crm.common.utils.UUIDUtils;
import br.crm.pojo.dept.OrganizationDept;
import br.crm.service.dept.OrgDeptService;
import br.crm.vo.dept.OrganizationDeptVo;
import br.crm.vo.examfeeitem.OrgExamFeeItemVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.org.controller.common.CommonController;

/**
 * 
 * @ClassName: OrgDeptController
 * @Description: 科室的相关接口实现
 * @author zxy
 * @date 2016年9月12日 上午11:03:18
 *
 */
@Controller
@RequestMapping("/OrgDept")
public class OrgDeptController {

	@Autowired
	private OrgDeptService orgDeptService;

	/**
	 * Session
	 */
	@Autowired
	private CommonController commonController;

	/**
	 * 
	 * @Title: getOrgDeptByPage @Description: 分页查询部门科室 @param @param page
	 *         当前页 @param @param rows 每页显示条数 @param @param organizationDeptQu
	 *         查询条件 @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "分页查询部门科室", httpMethod = "POST", response = JSONObject.class, notes = "分页查询部门科室列表")
	@RequestMapping("/getOrgDeptList")
	@ResponseBody
	public JSONObject getOrgDeptByPage(@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page, @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows, OrganizationDeptVo organizationDeptVo) {
		JSONObject jsonObject = new JSONObject();
		try {
			OrgUserVo userVo = commonController.getUserBySession();
			if (null != userVo) {
				organizationDeptVo.setOrgId(userVo.getOrgId());
				//截取branchId中的值
				if(organizationDeptVo.getBranchId()!=null){
				    if(organizationDeptVo.getBranchId().contains("'")){
	                    String branchId=organizationDeptVo.getBranchId().trim();
	                    organizationDeptVo.setBranchId(branchId.substring(1, branchId.length()-1));
	                }
				}
				PageInfo<OrganizationDeptVo> pageInfo = orgDeptService.getOrgDeptByCondition(page, rows, organizationDeptVo);
				jsonObject.put("data", pageInfo);
				return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 
	 * @Title: getOrgDeptAll @Description: 部门科室列表 @param @return 设定文件 @return
	 *         JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "部门科室列表", httpMethod = "GET", response = JSONObject.class, notes = "部门科室列表")
	@RequestMapping("/getOrgDeptAll")
	@ResponseBody
	public JSONObject getOrgDeptAll() {
		JSONObject jsonObject = new JSONObject();
		try {
			List<OrganizationDept> list = orgDeptService.getOrgDeptAll();
			jsonObject.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 
	 * @Title: addOrgDept @Description: 添加部门科室 @param @param dept
	 *         部门科室对象 @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "添加部门科室", httpMethod = "POST", response = JSONObject.class, notes = "添加部门科室")
	@RequestMapping("/addOrgDept")
	@ResponseBody
	public JSONObject addOrgDept(@ApiParam(required = true, name = "dept", value = "dept,部门科室对象") OrganizationDept dept) {
		JSONObject jsonObject = new JSONObject();
		OrgUserVo userVo = commonController.getUserBySession();
		if (null != userVo) {
			dept.setCreatetime(new Date());
			dept.setEdittime(new Date());
			dept.setStatus(0);
			dept.setOrgId(userVo.getOrgId());
			dept.setOrgDeptId(UUIDUtils.getId());
		}
		try {
			int i = orgDeptService.addOrgDept(dept);
			jsonObject.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 数据反填
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据id获取对象信息", httpMethod = "GET", response = JSONObject.class, notes = "根据id获取对象信息")
	@RequestMapping("/getOrgDeptById")
	@ResponseBody
	public JSONObject getOrgDeptById(@ApiParam(required = true, name = "pid", value = "pid,主键") String pid) {
		JSONObject jsonObject = new JSONObject();
		if (pid == null) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		if(pid.equals("0")){
		    jsonObject.put("data", "");
            return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
		}
		try {
			OrganizationDeptVo dept = orgDeptService.getOrgDeptByPid(pid);
			jsonObject.put("data", dept);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 
	 * @Title: updateOrgDept @Description: 修改部门科室 @param @param dept
	 *         部门科室对象 @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "修改部门科室", httpMethod = "POST", response = JSONObject.class, notes = "修改部门科室")
	@RequestMapping("/updateOrgDept")
	@ResponseBody
	public JSONObject updateOrgDept(OrganizationDeptVo dept) {
		JSONObject jsonObject = new JSONObject();
		if (CommonUtils.isEmpty(dept.getOrgDeptId())) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		OrgUserVo userVo = commonController.getUserBySession();
		if (null != userVo) {

			try {
				dept.setOrgId(userVo.getOrgId());
				int i = orgDeptService.updateOrgDept(dept);
				jsonObject.put("data", i);
				return InterfaceResultUtil.getReturnMapSuccess(jsonObject);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 
	 * @Title: deleteOrgDept @Description: 逻辑删除部门科室 @param @param pid
	 *         修改主键 @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "逻辑删除部门科室", httpMethod = "GET", response = JSONObject.class, notes = "逻辑删除部门科室")
	@RequestMapping("/deleteOrgDept")
	@ResponseBody
	public JSONObject deleteOrgDept(@ApiParam(required = true, name = "pid", value = "pid,修改主键") String pid) {
		JSONObject jsonObject = new JSONObject();
		if (CommonUtils.isEmpty(pid)) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		try {
			OrganizationDeptVo dept = orgDeptService.getOrgDeptByPid(pid);
			dept.setEdittime(new Date());
			dept.setStatus(1);
			int i = orgDeptService.updateOrgDept(dept);
			jsonObject.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 
	 * @Title: getOrgDeptByOrgid @Description: 根据机构id获取对象列表 @param @param orgid
	 *         机构id @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "根据机构id获取对象列表", httpMethod = "GET", response = JSONObject.class, notes = "根据机构id获取对象列表")
	@RequestMapping("/getOrgDeptByOrgid")
	@ResponseBody
	public JSONObject getOrgDeptByOrgid(@ApiParam(required = true, name = "orgid", value = "orgid,机构id") String orgid) {
		JSONObject jsonObject = new JSONObject();
		if (CommonUtils.isEmpty(orgid)) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		try {
			List<OrganizationDept> dept = orgDeptService.getOrgDeptByOrgid(orgid);
			jsonObject.put("data", dept);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	/**
	 * 
	 * @Title: getOrgDeptByBranid @Description: 根据门店id获取对象列表 @param @param
	 *         branid 门店id @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "根据门店id获取对象列表", httpMethod = "GET", response = JSONObject.class, notes = "根据门店id获取对象列表")
	@RequestMapping("/getOrgDeptByBranid")
	@ResponseBody
	public JSONObject getOrgDeptByBranid(@ApiParam(required = true, name = "branid", value = "branid,门店id") String branid) {
		JSONObject jsonObject = new JSONObject();
		if (CommonUtils.isEmpty(branid)) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		try {
			List<OrganizationDept> dept = orgDeptService.getOrgDeptByBranid(branid);
			jsonObject.put("data", dept);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	@ApiOperation(value = "根据科室id获取收费项", httpMethod = "GET", response = JSONObject.class, notes = "根据科室id获取收费项")
	@RequestMapping("/getItemByDeptid")
	@ResponseBody
	public JSONObject getItemByDeptid(@ApiParam(required = true, name = "deptId", value = "deptId,科室id") String deptId) {

		JSONObject jsonObject = new JSONObject();
		if (CommonUtils.isEmpty(deptId)) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		try {
			List<OrgExamFeeItemVo> list = orgDeptService.getItemByDeptid(deptId);
			jsonObject.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

	@ApiOperation(value = "查询当前门店科室名称", httpMethod = "GET", response = JSONObject.class, notes = "查询当前门店科室名称")
	@RequestMapping("/getDeptNameByBranchId")
	@ResponseBody
	public JSONObject getDeptNameByBranchId(String branchId) {
		JSONObject jsonObject = new JSONObject();
		if (StringUtils.isEmpty(branchId)) {
			return InterfaceResultUtil.getReturnMapValidValue(jsonObject);
		}
		try {
			List<Map<String, String>> deptList = orgDeptService.getDeptNameByBranchId(branchId);
			jsonObject.put("data", deptList);
			return InterfaceResultUtil.getReturnMapSuccess(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return InterfaceResultUtil.getReturnMapError(jsonObject);
	}

}
