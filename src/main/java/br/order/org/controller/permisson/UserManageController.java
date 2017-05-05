package br.order.org.controller.permisson;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.crypto.hash.Md5Hash;
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
import br.crm.common.utils.UUIDUtils;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.permission.UserManageCrmService;
import br.crm.service.permission.UserPermissionCrmService;
import br.crm.vo.permission.OrgUserVo;
import br.crm.vo.permission.OrganizationRoleVo;
import br.crm.vo.permission.RoleModel;
import br.crm.vo.permission.UserPermissionVo;
import br.order.org.controller.common.CommonController;
/**
 * 用户管理Controller
 * @ClassName: UserManageController
 * @Description: TODO(用户管理Controller)
 * @author adminis
 * @date 2016年9月13日 下午3:59:42
 *
 */
@Controller
@RequestMapping("/userManage")
public class UserManageController {
		
	/**
	 * 用户Service
	 */
	@Autowired
	private UserManageCrmService userManageService;
	
	/**
	 * Session
	 */
	@Autowired
	private CommonController commonController;
	
	/**
	 * 用户权限Service
	 */
	@Autowired
	private UserPermissionCrmService userPermissionService;
	/*其中@ApiOperation和@ApiParam为添加的API相关注解，个参数说明如下：
	@ApiOperation(value = “接口说明”, httpMethod = “接口请求方式”, response = “接口返回参数类型”, notes = “接口发布说明”；其他参数可参考源码；
	@ApiParam(required = “是否必须参数”, name = “参数名称”, value = “参数具体描述”*/
	
	/**
	 * 分页获取用户列表
	 * @Title: getUserByPage
	 * @Description: TODO(分页获取用户列表)
	 * @param @param page  当前页码
	 * @param @param rows  每页显示的条数
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="分页获取用户列表",httpMethod="GET",response=JSONObject.class,notes="分页获取用户列表")
	@RequestMapping("/getUserByPage")
	@ResponseBody
	public JSONObject getUserByPage(@ApiParam(required=true,name="page",value="page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required=true,name="rows",value="rows,每页显示条数")@RequestParam(value = "rows", defaultValue = "10", required = true)Integer rows){
		JSONObject message = new JSONObject();
		try {
			
			OrgUserVo user = commonController.getUserBySession();
			if(null!=user){
				PageInfo<OrgUserVo> pageInfo = userManageService.getUserByPage(page,rows,user);	
				message.put("data", pageInfo);
				return InterfaceResultUtil.getReturnMapSuccess(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/**
	 * 新增用户
	 * @Title: insertBrUser
	 * @Description: TODO(新增用户)
	 * @param @param brUser  用户对象
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="新增用户",httpMethod="POST",response=JSONObject.class,notes="新增用户")
	@RequestMapping(value="/insertBrUser",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject insertBrUser(@ApiParam(required=true,name="brUser",value="brUser,新增用户对象") OrganizationUser brUser){
		JSONObject message = new JSONObject();
		try {
			OrgUserVo user = commonController.getUserBySession();
			
			brUser.setUserEditId(user.getUserId());
			brUser.setUserId(UUIDUtils.getId());
			brUser.setUserEditName(user.getUserName());
			brUser.setUserStatus(0);
			brUser.setUserCreateTime(new Date());
			brUser.setUserEditTime(brUser.getUserCreateTime());
			//密码加密：1.加盐，2:两次散列算法
			String salt = brUser.getUserLoginName();
			Md5Hash md5 = new Md5Hash("88888888", salt, 2);
			brUser.setUserPassword(md5.toString());
			brUser.setOrgId(user.getOrgId());	
			//插入数据
			int i = userManageService.insertBrUser(brUser);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/**
	 * 根据用户id查询用户信息
	 * @Title: getBrUserById
	 * @Description: TODO(根据用户id查询用户信息)
	 * @param @param userId  用户ID
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="根据用户id查询用户信息",httpMethod="GET",response=JSONObject.class,notes="根据用户id查询用户信息")
	@RequestMapping("/getBrUserById")
	@ResponseBody
	public JSONObject getBrUserById(@ApiParam(required=true,name="userId",value="userId,用户id")String userId){
		JSONObject message = new JSONObject();
		try {
			OrganizationUser brUser = userManageService.getBrUserById(userId);
			message.put("data", brUser);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/**
	 * 用户重名校验
	 * @Title: getCountByUserName
	 * @Description: TODO(用户重名校验)
	 * @param @param userName 用户名
	 * @param @param userId  用户ID
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="用户重名校验",httpMethod="GET",response=JSONObject.class,notes="用户重名校验")
	@RequestMapping("/getCountByUserName")
	@ResponseBody
	public JSONObject getCountByUserName(@ApiParam(required=true,name="userName",value="userName,用户名")String userName,
			@ApiParam(required=false,name="userId",value="userId,用户id")String userId){
		JSONObject message = new JSONObject();
		try {
			int i = userManageService.getCountByUserName(userName,userId);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/**
	 * 修改用户
	 * @Title: updateBrUser
	 * @Description: TODO(修改用户)
	 * @param @param brUser 用户对象
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="修改用户",httpMethod="POST",response=JSONObject.class,notes="修改用户")
	@RequestMapping(value ="/updateBrUser",method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateBrUser(@ApiParam(required=true,name="brUser",value="brUser,修改用户对象") OrganizationUser brUser){
		JSONObject message = new JSONObject();
		try {
			OrgUserVo user = commonController.getUserBySession();
			brUser.setUserEditId(user.getUserId());
			brUser.setUserEditName(user.getUserLoginName());
			brUser.setUserEditTime(new Date());
			brUser.setUserLoginName(null);
			int i = userManageService.updateBrUser(brUser);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	@ApiOperation(value="删除用户",httpMethod="GET",response=JSONObject.class,notes="删除用户")
	@RequestMapping("/deleteBrUser")
	@ResponseBody
	public JSONObject deleteBrUser(@ApiParam(required=true,name="userId",value="userId,用户id")String userId){
		JSONObject message = new JSONObject();
		try {
			OrgUserVo user = commonController.getUserBySession();
			OrganizationUser brUser = new OrganizationUser();
			brUser.setUserId(userId);
			brUser.setUserStatus(1);
			brUser.setUserEditId(user.getUserId());
			brUser.setUserEditName(user.getUserLoginName());
			brUser.setUserEditTime(new Date());
			int i = userManageService.updateBrUser(brUser);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/**
	 * 根据用户id获取角色列表
	 */
	@ApiOperation(value="根据用户id获取角色列表",httpMethod="GET",response=JSONObject.class,notes="根据用户id获取角色列表")
	@RequestMapping("/getRolesByUserId")
	@ResponseBody
	public JSONObject getRolesByUserId(@ApiParam(required = true, name = "id", value = "userId,所选用户Id")String userId){
		JSONObject message = new JSONObject();
		try {
			if(userId!=null && !userId.equals("")){
				OrgUserVo user = commonController.getUserBySession();
				List<RoleModel> list = userManageService.getRolesByUserId(user.getOrgId(),userId);
				message.put("data", list);
				return InterfaceResultUtil.getReturnMapSuccess(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/**
	 * 新增用户角色
	 * @Title: insertUserRole
	 * @Description: TODO(新增用户角色)
	 * @param @param strRolesId  角色ID
	 * @param @param userId      用户ID
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="新增用户角色",httpMethod="POST",response=JSONObject.class,notes="新增用户角色")
	@ResponseBody
	@RequestMapping("/insertUserRole")
	public JSONObject insertUserRole(@ApiParam(required=true,name="strRolesId",value="strRolesId,新增用户的角色id字符串")String strRolesId,
			@ApiParam(required=true,name="userId",value="userId,需要新增角色的用户id")String userId){
		JSONObject message = new JSONObject();
		try {
			OrgUserVo user = commonController.getUserBySession();
			int i = userManageService.insertUserRole(strRolesId,userId, user);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	@ApiOperation(value="修改用户角色",httpMethod="POST",response=JSONObject.class,notes="修改用户角色")
	@ResponseBody
	@RequestMapping("/updateUserRole")
	public JSONObject updateUserRole(@ApiParam(required=true,name="strRolesId",value="strRolesId,修改用户的角色id字符串")String strRolesId,
			@ApiParam(required=true,name="userId",value="userId,需要修改角色的用户id")String userId){
		JSONObject message = new JSONObject();
		try {
			OrgUserVo user = commonController.getUserBySession();
			int i = userManageService.updateUserRole(strRolesId,userId,user);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	/**
	 * 获取用户权限列表
	 * @Title: getUserPermission
	 * @Description: TODO(获取用户权限列表)
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value = "获取用户权限列表", httpMethod = "GET", response = JSONObject.class, notes = "获取用户权限列表")
	@ResponseBody
	@RequestMapping("/getUserPermission")
	public JSONObject getUserPermission() {
		JSONObject message = new JSONObject();
		try {
			String userId = commonController.getUserBySession().getUserId();
			System.out.println("userID:" + userId);
//			userId=(long) 1;
			List<UserPermissionVo> userPermissions = userPermissionService.getUserPermission(userId);
			OrganizationRoleVo userRoleVo= userManageService.getUserRoleById(userId);
			JSONObject permissions = new JSONObject();
			permissions.put("permissions", userPermissions);
			message.put("userRole", userRoleVo);
			message.put("data", permissions);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);

	}
	
	/**
	 * 重置用户密码
	 * @Title: resetPassWord
	 * @Description: TODO(重置用户密码)
	 * @param @param userId  用户ID
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value = "重置用户密码", httpMethod = "GET", response = JSONObject.class, notes = "重置用户密码")
	@ResponseBody
	@RequestMapping("/resetPassWord")
	public JSONObject resetPassWord(@ApiParam(required=true,name="userId",value="userId,修改用户的角色id字符串") String userId) {
		JSONObject message = new JSONObject();
		try {
			OrgUserVo user = commonController.getUserBySession();
			OrganizationUser brUser = userManageService.getBrUserById(userId);
			//密码加密：1.加盐，2:两次散列算法
			if(null!=brUser&&null!=user){
				String salt = brUser.getUserLoginName();
				Md5Hash md5 = new Md5Hash("88888888", salt, 2);
				brUser.setUserPassword(md5.toString());
				brUser.setUserEditId(user.getUserId());
				brUser.setUserEditName(user.getUserLoginName());
				brUser.setUserEditTime(new Date());
				int i = userManageService.updateBrUser(brUser);
				message.put("data", i);
				return InterfaceResultUtil.getReturnMapSuccess(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);

	}
	
	
}