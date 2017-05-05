package br.order.org.controller.org;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.crm.service.org.RegisterOrgService;
import br.crm.vo.org.RegistOrgInfoVo;
import br.order.common.utils.InterfaceResultUtil;

@Controller
@RequestMapping("/registerOrg")
public class RegisterOrgController {
	@Autowired
	private RegisterOrgService registerOrgService;
	
	@ApiOperation(value="注册体检机构",httpMethod="POST",response=JSONObject.class,notes="注册体检机构")
	@RequestMapping(value="/insertOrgInfo",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject insertOrgInfo(@ApiParam(required=true,name="registOrgInfoVo",value="registOrgInfoVo,注册机构对象")RegistOrgInfoVo registOrgInfoVo){
		JSONObject message = new JSONObject();
		try {
			int i = registerOrgService.insertOrgInfo(registOrgInfoVo);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	@ApiOperation(value="用户重名校验",httpMethod="GET",response=JSONObject.class,notes="用户重名校验")
	@RequestMapping("/getCountByUserName")
	@ResponseBody
	public JSONObject getCountByUserName(@ApiParam(required=true,name="userName",value="userName,用户名")String userName){
		JSONObject message = new JSONObject();
		try {
			int i = 0;
//			i = registerOrgService.getCountByOrganizationTotalName("");
			i = registerOrgService.getCountByUserName(userName);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
}
