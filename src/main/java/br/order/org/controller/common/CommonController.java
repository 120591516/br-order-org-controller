package br.order.org.controller.common;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Repository;

import br.crm.pojo.permission.OrganizationUser;
import br.crm.vo.permission.OrgUserVo;
/**
 * CommonController
 * @ClassName: CommonController
 * @Description: TODO(CommonController)
 * @author adminis
 * @date 2016年9月13日 下午3:40:10
 *
 */
@Repository
public class CommonController {

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * @Title: setSession
	 * @Description: TODO(将一些数据放到ShiroSession中,以便于其它地方使用)
	 * @param @param key
	 * @param @param value    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public void setSession(Object key, Object value) {
		Subject subject = SecurityUtils.getSubject();
		if (null != subject) {
			Session session = subject.getSession();
			System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

	/**
	 * 获取session中用户信息
	 * @Title: getUserBySession
	 * @Description: TODO(获取session中用户信息)
	 * @param @return    设定文件
	 * @return OrganizationUser    返回类型
	 * @throws
	 */
	public OrgUserVo getUserBySession() {
		OrgUserVo user = null;
		Subject subject = SecurityUtils.getSubject();
		if (null != subject) {
			user = (OrgUserVo) subject.getPrincipals().asList().get(0);
		}
		return user;
	}

	/**
	 * 获取session中用户信息
	 * @Title: getSessionByKey
	 * @Description: TODO(获取session中用户信息)
	 * @param @param key
	 * @param @return    设定文件
	 * @return Object    返回类型
	 * @throws
	 */
	public Object getSessionByKey(Object key) {
		Subject subject = SecurityUtils.getSubject();
		Object result = null;
		if (null != subject) {
			Session session = subject.getSession();
			result = session.getAttribute(key);
		}
		return result;
	}
	
}
