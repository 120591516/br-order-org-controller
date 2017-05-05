/**   
* @Title: LiminalValueController.java 
* @Package br.order.org.controller.liminalvalue 阈值设置(套餐与门店)
* @Description: TODO
* @author kangting   
* @date 2016年10月24日 下午4:17:12 
* @version V1.0   
*/
package br.order.org.controller.liminalvalue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.crm.pojo.branch.OrganizationBranch;
import br.crm.pojo.suite.OrganizationExamSuite;
import br.crm.service.branch.OrgBranchService;
import br.crm.service.suite.OrgExamSuiteService;
import br.crm.vo.branch.OrganizationBranchVo;
import br.crm.vo.permission.OrgUserVo;
import br.crm.vo.suite.OrgExamSuiteQu;
import br.order.common.utils.InterfaceResultUtil;
import br.order.common.utils.TransCoding;
import br.order.org.controller.common.CommonController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/** 
 * @ClassName: LiminalValueController  阈值设置(套餐与门店)
 * @Description: TODO
 * @author kangting
 * @date 2016年10月24日 下午4:17:12 
 *  
 */
@RequestMapping("/liminalValue")
@Controller
public class LiminalValueController {
	/**
	 * 机构Service
	 */
	@Autowired
	private OrgBranchService orgBranchService;
	@Autowired
	private OrgExamSuiteService orgExamSuiteService;
	/**
	 * Session
	 */
	@Autowired
	private CommonController commonController;
	/**
	 * 阀值查看页面
	* @Title: getOrganizationBranchLimitPeople 
	* @Description: TODO
	* @param @param page
	* @param @param rows
	* @param @param organizationBranchvo
	* @param @return    
	* @return JSONObject    
	* @throws
	 */
	@ApiOperation(value = "获取门店阀值", httpMethod = "GET", response = JSONObject.class, notes = "获取门店阀值")
	@RequestMapping("/getOrganizationBranchLimitPeople")
	@ResponseBody
	public JSONObject getOrganizationBranchLimitPeople(
			@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
			@ApiParam(name = "organizationBranch", value = "organizationBranch,搜索对象") OrganizationBranchVo organizationBranchvo) {
		JSONObject message = new JSONObject();
		try {
			OrgUserVo brUser = commonController.getUserBySession();
			organizationBranchvo.setOrgId(brUser.getOrgId());
			if (organizationBranchvo != null
					&& organizationBranchvo.getBranchName() != null) {
				organizationBranchvo.setBranchName((TransCoding
						.encodeStr(organizationBranchvo.getBranchName())));
			}
			PageInfo<OrganizationBranch> list = orgBranchService.getOrganizationBranchLimitPeople(page,rows,organizationBranchvo);
			List<OrganizationBranch> branchArray = new ArrayList<OrganizationBranch>();
			for (OrganizationBranch orgBranch : list.getList() ) {
				OrganizationBranch orgBranchJson = new OrganizationBranch();
				orgBranchJson.setBranchId(orgBranch.getBranchId());
				orgBranchJson.setBranchName(orgBranch.getBranchName());
				orgBranchJson.setBranchCode(orgBranch.getBranchCode());
				orgBranchJson.setLimitPeople(orgBranch.getLimitPeople()); 
				orgBranchJson.setWarnPeople(orgBranch.getWarnPeople());
				branchArray.add(orgBranchJson);
			}
			list.setList(branchArray);
			message.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/**
	 * 
	* @Title: updateOrgBranchLimitPeople 
	* @Description: TODO
	* @param @param branchId
	* @param @param limitPeople
	* @param @return    
	* @return JSONObject    
	* @throws
	 */
	@ApiOperation(value = "修改门店阀值", notes = "OrgExamSuite", httpMethod = "POST")
	@RequestMapping(value = "/updateOrgBranchLimitPeople", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateOrgBranchLimitPeople(
			@ApiParam(name = "branchId", value = "门店id", required = true)String branchId,
			@ApiParam(name = "limitPeople", value = "限制人数", required = true)Integer limitPeople,
			@ApiParam(name = "warnPeople", value = "警告人数", required = true)Integer warnPeople ) {
		JSONObject message = new JSONObject();
		try {
			int r = orgBranchService.updateOrgBranchLimitPeople(branchId,limitPeople,warnPeople);
			message.put("data", r);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/**
	 * 
	* @Title: getOrgExamSuiteLimitPeople 
	* @Description: TODO 获取套餐阀值
	* @param @param page
	* @param @param rows
	* @param @param orgExamSuiteQu
	* @param @return    
	* @return JSONObject    
	* @throws
	 */
	@ApiOperation(value = "获取套餐阀值", httpMethod = "GET", response = JSONObject.class, notes = "获取门店阀值")
	@RequestMapping("/getOrgExamSuiteLimitPeople")
	@ResponseBody
	public JSONObject getOrgExamSuiteLimitPeople(
			@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
			@ApiParam(name = "orgExamSuiteQu", value = "orgExamSuiteQu,搜索对象") OrgExamSuiteQu orgExamSuiteQu) {
		JSONObject message = new JSONObject();
		try {
			OrgUserVo brUser = commonController.getUserBySession();
			orgExamSuiteQu.setOrgId(brUser.getOrgId());
			if (orgExamSuiteQu != null
					&& orgExamSuiteQu.getExamSuiteName() != null) {
				orgExamSuiteQu.setExamSuiteName(TransCoding
						.encodeStr(orgExamSuiteQu.getExamSuiteName()));
			}
			PageInfo<OrganizationExamSuite> list = orgExamSuiteService.getOrgExamSuiteLimitPeople(page,rows,orgExamSuiteQu);
			List<OrganizationExamSuite> suiteArray =new ArrayList<OrganizationExamSuite>() ;
			if( !list.getList().isEmpty()){
				for (OrganizationExamSuite orgSuite : list.getList() ) {
					OrganizationExamSuite orgSuiteJson = new OrganizationExamSuite();
					orgSuiteJson.setExamSuiteId(orgSuite.getExamSuiteId());
					orgSuiteJson.setExamSuiteName(orgSuite.getExamSuiteName());
					orgSuiteJson.setExamSuiteCode(orgSuite.getExamSuiteCode());
					orgSuiteJson.setExamSuiteLimitPeople(orgSuite.getExamSuiteLimitPeople());
					orgSuiteJson.setExamSuiteWarnPeople(orgSuite.getExamSuiteWarnPeople());
					suiteArray.add(orgSuiteJson);
				}
				list.setList(suiteArray);
				message.put("data", list);
			} 
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	/**
	 * 
	* @Title: updateOrgExamSuiteLimitPeople 
	* @Description: TODO
	* @param @param suiteId
	* @param @param limitPeople
	* @param @return    
	* @return JSONObject    
	* @throws
	 */
	@ApiOperation(value = "修改套餐阀值", notes = "OrgExamSuite", httpMethod = "POST")
	@RequestMapping(value = "/updateOrgExamSuiteLimitPeople", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateOrgExamSuiteLimitPeople(
			@ApiParam(name = "suiteId", value = "套餐id", required = true)String suiteId,
			@ApiParam(name = "limitPeople", value = "限制人数", required = true)Integer limitPeople,
			@ApiParam(name = "warnPeople", value = "警告人数", required = true)Integer warnPeople ) {
		JSONObject message = new JSONObject();
		try {
			int r = orgExamSuiteService.updateOrgExamSuiteLimitPeople(suiteId,limitPeople,warnPeople);
			message.put("data", r);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
}
