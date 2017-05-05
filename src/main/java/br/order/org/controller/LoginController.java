package br.order.org.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.vo.permission.OrgUserVo;
import br.order.org.controller.common.CommonController;
import br.order.org.realm.MyRealm;
/**
 * 登录退出Controller
 * @ClassName: LoginController
 * @Description: TODO(登录退出Controller)
 * @author adminis
 * @date 2016年9月13日 下午3:38:40
 *
 */
@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(MyRealm.class);
	
	/**
	 * request
	 */
	@Autowired
	private HttpServletRequest request;
	/**
	 * Session
	 */
	@Autowired
	private CommonController commonController;

	/**
	 * 用户登录方法
	 * @param username		用户名
	 * @param password		密码
	 * @param validCode		验证码
	 * @return
	 */
	@ApiOperation(value="用户登录",httpMethod="GET",response=JSONObject.class,notes="用户登录接口")
	@RequestMapping(value="/login",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject login(@ApiParam(required=true,name="username",value="用户名")@RequestParam(value = "username", defaultValue = "", required = true)String username, 
			@ApiParam(required=true,name="password",value="密码")@RequestParam(value = "password", defaultValue = "", required = true)String password,
			@ApiParam(required=true,name="validCode",value="验证码")@RequestParam(value = "validCode", defaultValue = "", required = true)String validCode) {
		JSONObject resultJson = new JSONObject();
		if (StringUtils.isEmpty(validCode)) {
			resultJson.put("message", "验证码为空");
			return InterfaceResultUtil.getReturnMapValidValue(resultJson);
		}
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return InterfaceResultUtil.getReturnMapValidUser();
		}
		
		if (null == request.getSession().getAttribute("validateCode") || StringUtils.isEmpty(request.getSession().getAttribute("validateCode").toString())){
			resultJson.put("message", "非法请求");
			return InterfaceResultUtil.getReturnMapValidValue(resultJson);
		}
		if(!validCode.equalsIgnoreCase(request.getSession().getAttribute("validateCode").toString())) {
			resultJson.put("message", "验证码错误");
			return InterfaceResultUtil.getReturnMapValidValue(resultJson);
		}
		Md5Hash md5 = new Md5Hash(password, username, 2);
		UsernamePasswordToken token = new UsernamePasswordToken(username, md5.toString());
		System.out.println("为了验证登录用户而封装的token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
		token.setRememberMe(true);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			logger.info("用户名 【" + subject.getPrincipal() + "】密码：【" + subject.getPrincipal() + "】登录成功.");
			
			//1.获取用户输入的权限
			//String uri = this.request.getRequestURI();
			//2.根据权限的URI查找权限ID
			
			
			/*if(subject.){
				
				//3.根据权限ID查找中间表roleID
				//4.
				logger.info("拥有【user】角色。");
			} else {
				
				return InterfaceResultUtil.getReturnMapValidUser();
			}*/
			
			OrgUserVo brUser = commonController.getUserBySession();
//			RedisService.set(brUser.getUserId(), JSON.toJSONString(brUser), 1800);
			logger.info("brUser"+JSON.toJSONString(brUser));
			return InterfaceResultUtil.getReturnMapSuccess(null);
		} catch (UnknownAccountException ex) {
			// 用户名没有找到。
			logger.info("用户名为【" + token.getPrincipal() + "】不存在");
		} catch (IncorrectCredentialsException ex) {
			// 用户名密码不匹配。
			logger.info("用户名为【 " + token.getPrincipal() + " 】密码错误！");
		} catch (LockedAccountException lae) {
			logger.info("用户名为【" + token.getPrincipal() + " 】的账户锁定，请联系管理员。");
		} catch (DisabledAccountException dax) {
			logger.info("用户名为:【" + token.getHost() + "】用户已经被禁用.");
		} catch (ExcessiveAttemptsException eae) {
			logger.info("用户名为:【" + token.getHost() + "】的用户登录次数过多，有暴力破解的嫌疑.");
		} catch (ExpiredCredentialsException eca) {
			logger.info("用户名为:【" + token.getHost() + "】用户凭证过期.");
		} catch (AuthenticationException e) {
			// 其他的登录错误
			e.printStackTrace();
		} catch (Exception e) {
			logger.info("别的异常信息。。。。具体查看继承关系");
		}
		return InterfaceResultUtil.getReturnMapValidUser();
	}
	
	
	/**
	 * 用户退出
	 * @Title: logout
	 * @Description: TODO(用户退出)
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="用户退出",httpMethod="GET",response=JSONObject.class,notes="用户退出接口")
	@RequestMapping("/logout")
	@ResponseBody
	public JSONObject logout(){
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.logout();
			return InterfaceResultUtil.getReturnMapSuccess(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapValidUser();
	}

	/**
	 * 拒绝访问
	 * @Title: refuse
	 * @Description: TODO(拒绝访问)
	 * @param @return    设定文件
	 * @return JSONObject    返回类型
	 * @throws
	 */
	@ApiOperation(value="拒绝访问",httpMethod="GET",response=JSONObject.class,notes="用户拒绝访问")
	@RequestMapping("/refuse")
	@ResponseBody
	public JSONObject refuse(){
		return InterfaceResultUtil.getReturnMapRefuseRequest();
	}
	
}
