package br.order.org.controller.examitem;

import java.util.Date;

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

import br.crm.common.utils.InterfaceResultUtil;
import br.crm.pojo.examitem.OrganizationExamItemUser;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.examitem.OrgExamItemService;
import br.crm.service.examitem.OrgExamItemUserService;
import br.crm.service.permission.OrganizationUserService;
import br.crm.vo.examitem.OrganizationExamItemUserVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.org.controller.common.CommonController;

/**
 * @ClassName: OrgExamItemUserController
 * @Description: 检查项绑定医生
 * @author server
 * @date 2016年11月10日 下午2:50:31
 */
@Controller
@RequestMapping("/orgExamItemUser")
public class OrgExamItemUserController {
	@Autowired
	private OrgExamItemUserService orgExamItemUserService;
	@Autowired
	private OrgExamItemService orgExamItemService;
	@Autowired
    private CommonController commonController;
	@Autowired
	private OrganizationUserService organizationUserService;
	/** 
	* @Title: getOrgExamItemUser 
	* @Description: 检查项绑定医生
	* @param page
	* @param rows
	* @param userId
	* @param examId
	* @param feeItemId
	* @param orgId
	* @param branchId
	* @return    设定文件 
	* @return JSONObject    返回类型 
	*/
	@ApiOperation(value="检查项绑定医生",httpMethod="GET",response=JSONObject.class,notes="检查项绑定医生")
	@RequestMapping("/getOrgExamItemUser")
	@ResponseBody
	public JSONObject getOrgExamItemUser(
			 @ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
	         @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
	         String userId,String examId,String feeItemId,String orgId,String branchId,String deptId){
		JSONObject message = new JSONObject();
		try {
		    OrgUserVo brUser = commonController.getUserBySession();
		    orgId=brUser.getOrgId();
			PageInfo<OrganizationExamItemUserVo> pageInfo = orgExamItemUserService.getOrganizationExamItemUser(page, rows, userId, examId, feeItemId, orgId, branchId,deptId);
			message.put("data", pageInfo);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
	
	 /**
		 * 
		 * @Title: saveUser @Description: TODO(保存修改过的医生) @param @param userId
		 * 医生id @param @param itemId 体检项id @param @return 设定文件 @return JSONObject
		 * 返回类型 @throws
		 */
		@ApiOperation(value = "保存修改过的医生", httpMethod = "GET", response = JSONObject.class, notes = "保存修改过的医生")
		@RequestMapping("/saveOrgExamItemUser")
		@ResponseBody
		public JSONObject saveOrgExamItemUser(@ApiParam(required = true, name = "userId", value = "医生userId,userId") String userId,
				@ApiParam(required = true, name = "itemId", value = "检查项itemId,itemId") String itemId) {
			JSONObject message = new JSONObject();
			try {
			    int i=0;
	            OrganizationUser orgUser = organizationUserService.getOrgUser(userId);
	            if(null!=orgUser&&!orgUser.getOrgBranchId().equals("0")&&!orgUser.getOrgBranchDeptId().equals("0")&&StringUtils.isNotEmpty(itemId)){
	                i = orgExamItemUserService.saveUser(itemId,orgUser);
	                message.put("data", i);
	                return InterfaceResultUtil.getReturnMapSuccess(message);
	            }else{
	                message.put("data", "选择的信息不全，请核对后操作");
	                return InterfaceResultUtil.getReturnMapError(message);
	            }

			} catch (Exception e) {
				e.printStackTrace();

			}
			return InterfaceResultUtil.getReturnMapError(message);
		}
		/** 
		* @Title: deleteOrgExamItemUser 
		* @Description: 删除体检项和医生关联表
		* @param examItemUserId
		* @return    设定文件 
		* @return JSONObject    返回类型 
		*/
		@ApiOperation(value = "删除体检项和医生关联表", httpMethod = "GET", response = JSONObject.class, notes = "删除体检项和医生关联表")
		@RequestMapping("/deleteOrgExamItemUser")
		@ResponseBody
		public JSONObject deleteOrgExamItemUser(
				@ApiParam(required = true, name = "examItemUserId", value = "examItemUserId,主键id") String examItemUserId
				){
			JSONObject message = new JSONObject();
			try {
				OrganizationExamItemUser organizationExamItemUser = orgExamItemUserService.selectOrgExamItemUserById(examItemUserId);
				organizationExamItemUser.setOrganizationExamItemUserId(examItemUserId);
				organizationExamItemUser.setOrganizationExamItemStatus(1);
				organizationExamItemUser.setOrganizationExamItemEditTime(new Date());
				int i = orgExamItemUserService.updateOrgExamItemUser(organizationExamItemUser);
				message.put("data", i);
				return InterfaceResultUtil.getReturnMapSuccess(message);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return InterfaceResultUtil.getReturnMapError(message);
		}
		
}
