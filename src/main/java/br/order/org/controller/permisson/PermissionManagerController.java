package br.order.org.controller.permisson;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.pojo.permission.OrganizationOperation;
import br.crm.pojo.permission.OrganizationPermission;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.permission.PermissionCrmService;
import br.crm.service.permission.RolePermissionCrmService;
import br.crm.vo.permission.PermissionResult;
import br.order.org.controller.common.CommonController;
import br.order.pojo.BrOperation;
import br.order.pojo.BrPermission;

/**
 * 权限管理Controller
 * 
 * @ClassName: PermissionManagerController
 * @Description: TODO(权限管理Controller)
 * @author adminis
 * @date 2016年9月13日 下午3:53:21
 *
 */
@Controller
@RequestMapping("/permissionManager")
public class PermissionManagerController {

	/**
	 * 角色权限Service
	 */
	@Autowired
	private RolePermissionCrmService rolePermissionService;

	@Autowired
	private CommonController commonController;

	/*
	 * @Autowired private PermissionCrmService permissionCrmService;
	 */

	/**
	 * 查询所有权限 @Title: getPermissionList @Description:
	 * TODO(查询所有权限) @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "查询所有权限", httpMethod = "GET", response = JSONObject.class, notes = "查询所有权限")
	@RequestMapping("/getPermissionList")
	@ResponseBody
	public JSONObject getPermissionList() {

		JSONObject message = new JSONObject();
		try {

			List<PermissionResult> allPermission = rolePermissionService.getPermissionList();
			List<OrganizationPermission> topPermission = rolePermissionService.getMenuList();
			JSONObject data = new JSONObject();
			data.put("all", allPermission);
			data.put("top", topPermission);
			message.put("data", data);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * 添加权限 @Title: insertPermission @Description: TODO(添加权限) @param @param
	 * brPermission 权限对象 @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "添加权限", httpMethod = "POST", response = JSONObject.class, notes = "添加权限")
	@RequestMapping("/insertPermission")
	@ResponseBody
	public JSONObject insertPermission(
			@ApiParam(required = true, name = "brPermission", value = "添加的权限") OrganizationPermission brPermission) {

		JSONObject message = new JSONObject();

		try {
			if(null==brPermission.getPermissionParentId()||"".equals(brPermission.getPermissionParentId())){	
				brPermission.setPermissionParentId(0L);
			}
			brPermission.setPermissionCreateTime(new Date());
			brPermission.setPermissionId(null);
			brPermission.setPermissionStatus(0);
			int i = rolePermissionService.insertPermission(brPermission);

			message.put("data", i);

			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("msg", "添加权限失败");
		return InterfaceResultUtil.getReturnMapError(message);

	}

	/**
	 * 根据权限的Id查询功能列表 @Title: getBrOperationListById @Description:
	 * TODO(根据权限的Id查询功能列表) @param @param permissionId 权限ID @param @return
	 * 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "根据权限的Id查询全部功能列表", httpMethod = "GET", response = JSONObject.class, notes = "根据权限的Id查询全部功能列表")
	@RequestMapping("/getBrOperationListById")
	@ResponseBody
	public JSONObject getBrOperationListById(
			@ApiParam(required = true, name = "permissionId", value = "权限id") @RequestParam(value = "permissionId", required = true) Long permissionId) {
		JSONObject message = new JSONObject();
		if (null == permissionId || "".equals(permissionId)) {
			message.put("msg", "参数错误");
			return InterfaceResultUtil.getReturnMapError(message);
		}
		try {
			List<OrganizationOperation> list = rolePermissionService.getBrOperationList(permissionId);
			message.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * 根据权限的Id添加操作功能 @Title: addBrOperationByPermissionId @Description:
	 * TODO(根据权限的Id添加操作功能) @param @param brOperation 操作对象 @param @return
	 * 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "根据权限的Id添加操作功能", httpMethod = "POST", response = JSONObject.class, notes = "根据权限的Id添加操作功能")
	@RequestMapping("/addBrOperationByPermissionId")
	@ResponseBody
	public JSONObject addBrOperationByPermissionId( OrganizationOperation brOperation) {
		JSONObject message = new JSONObject();
		try {
			brOperation.setOperationCreatetime(new Date());
			int i = rolePermissionService.addBrOperationByPermissionId(brOperation);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * 权限删除 @Title: deletePermissionById @Description: TODO(权限删除) @param @param
	 * permissionId 权限ID @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "删除权限", httpMethod = "GET", response = JSONObject.class, notes = "删除权限")
	@RequestMapping("/deletePermissionById")
	@ResponseBody
	public JSONObject deletePermissionById(
			@ApiParam(required = true, name = "permissionId", value = "权限id") @RequestParam(value = "id", required = true) Long permissionId) {
		JSONObject message = new JSONObject();
		if (null == permissionId || "".equals(permissionId)) {
			message.put("msg", "参数错误");
			return InterfaceResultUtil.getReturnMapError(message);
		}
		try {
			int i = rolePermissionService.deletePermissionById(permissionId);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("msg", "删除失败");
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * 权限修改 @Title: updatePermissionById @Description: TODO(权限修改) @param @param
	 * brPermission 权限对象 @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "修改权限", httpMethod = "POST", response = JSONObject.class, notes = "修改权限")
	@RequestMapping("/updatePermissionById")
	@ResponseBody
	public JSONObject updatePermissionById(
			@ApiParam(required = true, name = "brPermission", value = "权限") OrganizationPermission brPermission) {

		JSONObject message = new JSONObject();
		if (null == brPermission) {
			message.put("msg", "修改失败");
			return InterfaceResultUtil.getReturnMapError(message);
		}
		try {
			int i = rolePermissionService.updatePermissionById(brPermission);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		message.put("msg", "修改失败");
		return InterfaceResultUtil.getReturnMapError(message);

	}

	/**
	 * 根据权限的Id查询权限 @Title: getBrPermissionById @Description:
	 * TODO(根据权限的Id查询权限) @param @param id 权限ID @param @return 设定文件 @return
	 * JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "根据权限的Id查询权限", httpMethod = "GET", response = JSONObject.class, notes = "根据权限的Id查询权限")
	@RequestMapping("/getBrPermissionById")
	@ResponseBody
	public JSONObject getBrPermissionById(@ApiParam(required = true, name = "id", value = "权限id") Long id) {
		JSONObject message = new JSONObject();

		if (null == id || "".equals(id)) {
			message.put("msg", "参数错误");
			return InterfaceResultUtil.getReturnMapError(message);
		}
		try {

			OrganizationPermission brPermission = rolePermissionService.getBrPermissionById(id);
			List<OrganizationOperation> list = rolePermissionService.getBrOperationList(brPermission.getPermissionId());
			JSONObject data = new JSONObject();
			data.put("brPermission", brPermission);
			data.put("operationList", list);
			message.put("data", data);
			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * 
	 * @Title: getBrPermissionByUserId @Description:
	 *         TODO(根据用户的Id查询权限列表) @param @return 设定文件 @return JSONObject
	 *         返回类型 @throws
	 */
	/*
	 * @ApiOperation(value = "根据用户的Id查询权限列表", httpMethod = "GET", response =
	 * JSONObject.class, notes = "根据用户的Id查询权限列表")
	 * 
	 * @RequestMapping("/getBrPermissionByUserId")
	 * 
	 * @ResponseBody
	 */
	/*
	 * public JSONObject getBrPermissionByUserId(){
	 * 
	 * JSONObject message = new JSONObject();
	 * 
	 * OrganizationUser user =(OrganizationUser)
	 * commonController.getSessionByKey("currentUser");
	 * 
	 * String userId=user.getUserId();
	 * 
	 * if(StringUtils.isNotEmpty(userId)){
	 * 
	 * //permissionCrmService.getBrPermissionByUserId(userId); } }
	 */
	@ApiOperation(value = "权限管理-删除功能", httpMethod = "GET", response = JSONObject.class, notes = "权限管理-删除功能")
	@RequestMapping("/deletePermissionOrOperation")
	@ResponseBody
	public JSONObject deletePermissionOrOperation(@ApiParam(required = true, name = "id", value = "权限id") String id) {
		JSONObject message = new JSONObject();
		if (null == id || "".equals(id)) {
			message.put("msg", "参数错误");
			return InterfaceResultUtil.getReturnMapError(message);
		}
		try {
			int i = 0;
			if (!id.toString().contains(".")) {
				// 删除一级菜单
				i = rolePermissionService.deletePermissionById(Long.parseLong(id));
			} else {
				// 删除操作菜单
				String[] str = id.toString().split("\\.");
				if (ArrayUtils.isNotEmpty(str)) {
					Long oId = Long.parseLong(str[0].toString());
					i = rolePermissionService.deleteOperationById(oId);
				}
			}
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	@ApiOperation(value = "根据操作Id回显数据", httpMethod = "GET", response = JSONObject.class, notes = "根据操作Id回显数据")
	@RequestMapping("/getBrOperationById")
	@ResponseBody
	public JSONObject getBrOperationById(
			@ApiParam(required = true, name = "operationId", value = "权限Id") Long operationId) {
		JSONObject message = new JSONObject();
		if (null == operationId || "".equals(operationId)) {
			message.put("msg", "参数错误");
			return InterfaceResultUtil.getReturnMapError(message);
		}
		try {
			OrganizationOperation organizationOperation = rolePermissionService.getBrOperationById(operationId);
			message.put("data", organizationOperation);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	@ApiOperation(value = "更新操作数据", httpMethod = "POST", response = JSONObject.class, notes = "更新操作数据")
	@RequestMapping("/updateBrOperation")
	@ResponseBody
	public JSONObject updateBrOperation(
			@ApiParam(required = true, name = "organizationOperation", value = "操作对象") OrganizationOperation organizationOperation) {
		JSONObject message = new JSONObject();
		try {
			organizationOperation.setOperationCreatetime(new Date());
			int i = rolePermissionService.updateBrOperation(organizationOperation);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
}
