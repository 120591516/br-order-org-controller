package br.order.org.controller.examitem;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import br.crm.pojo.examfeeitem.OrganizationExamFeeItem;
import br.crm.pojo.examitem.OrganizationExamFeeItemDetail;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemDetailService;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemService;
import br.crm.vo.examitem.OrganizationExamFeeItemDetailVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.org.controller.common.CommonController;

/** 
* (收费项-检查项controller)
* @ClassName: OrgExamFeeItemDetailController 
* @Description: TODO(收费项-检查项controller) 
* @author 王文腾
* @date 2016年9月13日 上午9:09:33 
*/
@Controller
@RequestMapping("/orgExamFeeItemDetail")
public class OrgExamFeeItemDetailController {
    /**
     * {收费项-检查项service}
     */
    @Autowired
    private OrgExamFeeItemDetailService orgExamFeeItemDetailService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrgExamFeeItemService orgExamFeeItemService;
    /**
     * Session
     */
    @Autowired
    private CommonController commonController;
    /** 
    * @Title: insertOrgExamFeeItemDetail 
    * @Description: TODO(批量添加收费项检查项关联信息) 
    * @param examFeeItemId 收费项目id
    * @param examItemId 体检项目id串(多条id按逗号隔开)
    * @return JSONObject    
    * @throws 
    */
    @ApiOperation(value="收费项目与检查项目关联表",httpMethod="GET",response=JSONObject.class,notes="收费项目与检查项目关联表")
    @RequestMapping("/getOrgExamFeeItemDetailByPage")
    @ResponseBody 
   public JSONObject getOrgExamFeeItemDetailByPage(
           @ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
             @ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
           String feeItemId,String orgId,String branchId){
        JSONObject message = new JSONObject();
        try {
            OrgUserVo brUser = commonController.getUserBySession();
            orgId=brUser.getOrgId();
            PageInfo<OrganizationExamFeeItemDetailVo> pageInfo = orgExamFeeItemDetailService.getOrgExamFeeItemDetailByPage(page, rows,feeItemId, orgId, branchId);
            message.put("data", pageInfo);
            return InterfaceResultUtil.getReturnMapSuccess(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
       
   }
    @ApiOperation(value = "添加收费项体检项关联信息", httpMethod = "GET", response = JSONObject.class, notes = "添加收费项体检项关联信息")
    @RequestMapping("/insertOrgExamFeeItemDetail")
    @ResponseBody
    public JSONObject insertOrgExamFeeItemDetail(
            @ApiParam(required = true, name = "examFeeItemId", value = "examFeeItemId,收费项id") String examFeeItemId,
            @ApiParam(required = true, name = "examItemId", value = "examItemId,体检项id串") String examItemId) {
        JSONObject message = new JSONObject();
        try {
        	//int i = orgExamFeeItemDetailService.deleteOrgExamFeeItemDetail(examFeeItemId);
            if (StringUtils.isNotEmpty(examItemId)&&StringUtils.isNotEmpty(examFeeItemId)) {
                OrganizationExamFeeItem  orgExamFeeItem =orgExamFeeItemService.getOrganizationExamFeeItemById(examFeeItemId);
                int i = orgExamFeeItemDetailService.batchInsertOrgExamFeeItemDetail(orgExamFeeItem,examItemId);
                message.put("data", i);
                return InterfaceResultUtil.getReturnMapSuccess(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return InterfaceResultUtil.getReturnMapError(message);
    }

    /** 
    * @Title: deleteOrgExamFeeItemDetail 
    * @Description:逻辑删除 收费项体检项关联信息
    * @param examFeeItemDetailId
    * @return    设定文件 
    * @return JSONObject    返回类型 
    */
    @ApiOperation(value = "逻辑删除收费项体检项关联信息", httpMethod = "GET", response = JSONObject.class, notes = "逻辑删除收费项体检项关联信息")
    @RequestMapping("/deleteOrgExamFeeItemDetail")
    @ResponseBody
    public JSONObject deleteOrgExamFeeItemDetail( @ApiParam(required = true, name = "examFeeItemDetailId", value = "examFeeItemDetailId,主键id")String examFeeItemDetailId){
    	JSONObject message = new JSONObject();
    	try {
    		OrganizationExamFeeItemDetail organizationExamFeeItemDetail = orgExamFeeItemDetailService.SelectOrgExamFeeItemDetailByPrimaryKey(examFeeItemDetailId);
    		organizationExamFeeItemDetail.setExamFeeItemDetailId(examFeeItemDetailId);
    		organizationExamFeeItemDetail.setStatus(1);
    		organizationExamFeeItemDetail.setEditTime(new Date());
    		int i = orgExamFeeItemDetailService.updateOrgExamFeeItemDetail(organizationExamFeeItemDetail);
    		if(i>0){
    			message.put("data", i);
    			return InterfaceResultUtil.getReturnMapSuccess(message);
    		}    				
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return InterfaceResultUtil.getReturnMapError(message);
    }
}
