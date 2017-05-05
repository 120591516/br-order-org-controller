package br.order.org.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.crm.pojo.permission.OrganizationOperation;
import br.crm.pojo.permission.OrganizationRolePermission;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.permission.OrganizationOperationCrmService;
import br.crm.service.permission.RoleManageCrmService;
import br.crm.service.permission.RolePermissionCrmService;
import br.crm.service.permission.UserManageCrmService;
import br.crm.vo.permission.OrgUserVo;
import br.crm.vo.permission.OrganizationRoleVo;

public class MyRealm extends AuthorizingRealm {


	@Autowired
	private UserManageCrmService userManageService;
	@Autowired
	private RoleManageCrmService roleManageService;

	@Autowired
	private RolePermissionCrmService rolePermissionService;
	
	@Autowired
	private OrganizationOperationCrmService organizationOperationCrmService;

	private static Logger logger = LoggerFactory.getLogger(MyRealm.class);

	/**
	 * 授权 为当前登录的Subject授予角色和权限
	 * 
	 * @see 经测试:本例中该方法的调用时机为需授权资源被访问时
	 * @see 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
	 * @see 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,
	 *      则可灵活决定是否启用AuthorizationCache
	 * @see 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		OrgUserVo user = ((OrgUserVo) principals.getPrimaryPrincipal());

		List<String> roleList = new ArrayList<String>();
		List<String> permissionList = new ArrayList<String>();
		List<Long> roleIdList = new ArrayList<Long>();

		if (null != user) {
			List<OrganizationRoleVo> roleListByUserId = roleManageService.getRoleListByUserId(user.getUserId());
			user.setRoles(roleListByUserId);
			// 实体类User中包含有用户角色的实体类信息
			if (user.getRoles().size() > 0) {
				for (OrganizationRoleVo role : user.getRoles()) { // 获取当前登录用户的角色
					roleList.add(role.getRoleName());
					if (role != null && role.getRoleId() != null) {// 实体类Role中包含有角色权限的实体类信息
						roleIdList.add(role.getRoleId());
					}
				}
				List<OrganizationRolePermission> rolePermissionList = rolePermissionService.getRolePermissionsByRoleId(roleIdList);
				
				for (OrganizationRolePermission rolePermission : rolePermissionList) {
					List<OrganizationOperation> opList = organizationOperationCrmService.getListByRole(rolePermission);
					for (OrganizationOperation organizationOperation : opList) {
						permissionList.add(organizationOperation.getOperationDescribe());
					}
				}
			}
		} else {
			throw new AuthorizationException();
		}
		// 为当前用户设置角色和权限
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
		simpleAuthorInfo.addRoles(new HashSet<String>(roleList));
		simpleAuthorInfo.addStringPermissions(new HashSet<String>(permissionList));
		return simpleAuthorInfo;
	}

	/**
	 * 认证 验证当前登录的Subject
	 * 
	 * @see 经测试:本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		String username = token.getPrincipal().toString();
		String password = new String((char[]) token.getCredentials());

		OrganizationUser brUser = userManageService.getUserByUserName(username);
		if (brUser == null)
			throw new UnknownAccountException("用户名或者密码出错");
		// Md5Hash md5 = new Md5Hash(password, username, 2);
		if (!brUser.getUserPassword().equals(password))
			throw new IncorrectCredentialsException("用户名或者密码出错");
		if (brUser.getUserStatus() != 0)
			throw new LockedAccountException("用户已经被锁定");
		OrgUserVo user = new OrgUserVo();
		user.setUserId(brUser.getUserId());
		user.setUserName(brUser.getUserName());
		user.setUserLoginName(brUser.getUserLoginName());
		user.setUserPassword(brUser.getUserPassword());
		user.setOrgId(brUser.getOrgId());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getUserPassword(), this.getName());
		info.setCredentialsSalt(ByteSource.Util.bytes(user.getUserName()));
		return info;
	}

	/**
	 * 更新授权信息缓存
	 */
	@Override
	protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("清除授权的缓存");
		/*Cache c = this.getAuthorizationCache();
		Set<Object> keys = c.keys();
		for (Object o : keys) {
			System.out.println("授权缓存:" + o + "-----" + c.get(o) + "----------");
		}*/

		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		try {
			System.out.println("清除认证的缓存");
			/*Cache c = this.getAuthenticationCache();
			Set<Object> keys = c.keys();
			for (Object o : keys) {
				System.out.println("认证缓存:" + o + "----------" + c.get(o) + "----------");
			}*/
			OrgUserVo user = ((OrgUserVo) principals.getPrimaryPrincipal());
			SimplePrincipalCollection spc = new SimplePrincipalCollection(user.getUserName(), getName());
			super.clearCachedAuthenticationInfo(spc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
