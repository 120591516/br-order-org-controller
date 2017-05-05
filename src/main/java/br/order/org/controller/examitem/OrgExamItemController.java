package br.order.org.controller.examitem;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import br.crm.common.utils.StringTransCodeUtil;
import br.crm.pojo.examitem.OrganizationExamItem;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.examitem.OrgExamItemService;
import br.crm.service.examitem.OrgExamItemUserService;
import br.crm.service.permission.OrganizationUserService;
import br.crm.vo.examitem.OrgExamItemDetailVo;
import br.crm.vo.examitem.OrgExamItemQu;
import br.crm.vo.examitemvalue.OrgExamItemVo;
import br.crm.vo.permission.OrgUserVo;
import br.order.org.controller.common.CommonController;

/**
 * (体检项controller)
 * 
 * @ClassName: OrgExamItemController
 * @Description: TODO(体检项controller)
 * @author 王文腾
 * @date 2016年9月13日 上午10:54:20
 */
@Controller
@RequestMapping("/orgExamItem")
public class OrgExamItemController {

	/**
	 * {体检项service}
	 */
	@Autowired
	private OrgExamItemService orgExamItemService;
	@Autowired
	private OrgExamItemUserService orgExamItemUserService;
	@Autowired
	private OrganizationUserService organizationUserService;
	/**
	 * @Title: getOrgExamItemByPage @Description: TODO(分页获取体检项列表) @param page
	 *         当前页数 @param rows 每页显示行数 @param orgExamItemQu 条件查询对象 @return
	 *         JSONObject @throws
	 */

	@ApiOperation(value = "分页获取体检项列表", httpMethod = "POST", response = JSONObject.class, notes = "分页获取体检项列表")
	@RequestMapping("/getOrgExamItemByPage")
	@ResponseBody
	public JSONObject getOrgExamItemByPage(
			@ApiParam(required = true, name = "page", value = "page,当前页") @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "rows,每页显示条数") @RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
			@ApiParam(required = true, name = "orgExamFeeItem", value = "orgExamFeeItem,条件查询对象") OrgExamItemQu orgExamItemQu) {
		JSONObject message = new JSONObject();
		try {
			PageInfo<OrgExamItemVo> pageInfo = orgExamItemService.getOrgExamItemByPage(orgExamItemQu, page, rows);
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				for (OrgExamItemVo orgExamItemVo : pageInfo.getList()) {
					// 根据体检项id查找医生
					OrganizationUser user = orgExamItemService.getUserByItemId(orgExamItemVo.getExamItemId());
					if (null != user) {
						orgExamItemVo.setUserId(user.getUserId());
						orgExamItemVo.setUserName(user.getUserName());
					} else {
						orgExamItemVo.setUserId("");
						orgExamItemVo.setUserName("");
					}
				}
			}
			message.put("data", pageInfo);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: getAllOrgExamItem @Description: TODO(获取体检项列表) @return
	 *         JSONObject @throws
	 */

	@ApiOperation(value = "获取体检项列表", httpMethod = "GET", response = JSONObject.class, notes = "获取体检项列表")
	@RequestMapping("/getAllOrgExamItem")
	@ResponseBody
	public JSONObject getAllOrgExamItem() {
		JSONObject message = new JSONObject();
		try {
			List<OrganizationExamItem> list = orgExamItemService.getAllOrgExamItem();
			message.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: insertOrgExamItem @Description: TODO(新增体检项) @param orgExamItem
	 *         体检项对象 @return JSONObject @throws
	 */

	@ApiOperation(value = "新增体检项", httpMethod = "POST", response = JSONObject.class, notes = "新增体检项")
	@RequestMapping(value = "/insertOrgExamItem", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject insertOrgExamItem(
			@ApiParam(required = true, name = "insertOrgExamItem", value = "insertOrgExamItem,新增体检项对象")  OrganizationExamItem orgExamItem) {
		JSONObject message = new JSONObject();
		try {
			// 插入数据
			int i = orgExamItemService.insertOrgExamItem(orgExamItem);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: getOrgExamItemById @Description: TODO(根据用户id查询体检项信息) @param
	 *         orgExamItemId 体检项id @return JSONObject @throws
	 */

	@ApiOperation(value = "根据用户id查询体检项信息", httpMethod = "GET", response = JSONObject.class, notes = "根据用户id查询体检项信息")
	@RequestMapping("/getOrgExamItemById")
	@ResponseBody
	public JSONObject getOrgExamItemById(
			@ApiParam(required = true, name = "orgExamItemId", value = "orgExamItemId,体检项id") String orgExamItemId) {
		JSONObject message = new JSONObject();
		try {
			OrganizationExamItem orgExamItem = orgExamItemService.getOrgExamItemById(orgExamItemId);
			message.put("data", orgExamItem);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: updateOrgExamItem @Description: TODO(修改体检项) @param orgExamItem
	 *         体检项对象 @return JSONObject @throws
	 */

	@ApiOperation(value = "修改体检项", httpMethod = "POST", response = JSONObject.class, notes = "修改体检项")
	@RequestMapping("/updateOrgExamItem")
	@ResponseBody
	public JSONObject updateOrgExamItem(
			@ApiParam(required = true, name = "brUser", value = "brUser,修改用户对象")OrganizationExamItem orgExamItem) {
		JSONObject message = new JSONObject();
		try {
			int i = orgExamItemService.updateOrgExamItem(orgExamItem);
			message.put("data", i);
			return InterfaceResultUtil.getReturnMapSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: deleteOrgExamItem @Description: TODO(deleteOrgExamItem) @param
	 *         orgExamItemId 体检项id @return JSONObject @throws
	 */

	@ApiOperation(value = "删除体检项", httpMethod = "GET", response = JSONObject.class, notes = "删除体检项")
	@RequestMapping("/deleteOrgExamItem")
	@ResponseBody
	public JSONObject deleteOrgExamItem(
			@ApiParam(required = true, name = "orgExamItemId", value = "orgExamItemId,体检项id") String orgExamItemId) {
		JSONObject message = new JSONObject();
		try {
			OrganizationExamItem orgExamItem = new OrganizationExamItem();
			orgExamItem.setExamItemId(orgExamItemId);
			orgExamItem.setExamItemStatus(1);
			int i = orgExamItemService.countExamItemRelation(orgExamItemId);
			if (i < 0) {
				message.put("data", "该项存在关联关系，尚不能删除");
				return InterfaceResultUtil.getReturnMapError(message);
			} else {
				i = orgExamItemService.updateOrgExamItem(orgExamItem);
				message.put("data", i);
				return InterfaceResultUtil.getReturnMapSuccess(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: getAllOrgExamItemByFeeItem @Description:
	 *         TODO(根据收费项获取体检项列表) @param id 收费项id @return JSONObject @throws
	 */

	@ApiOperation(value = "根据收费项获取体检项列表", httpMethod = "GET", response = JSONObject.class, notes = "根据收费项获取体检项列表")
	@RequestMapping("/getAllOrgExamItemByFeeItem")
	@ResponseBody
	public JSONObject getAllOrgExamItemByFeeItem(
			@ApiParam(required = true, name = "page", value = "page,当前页")@RequestParam(value = "page", defaultValue = "1", required = true)Integer page,
            @ApiParam(required = true, name = "rows", value = "rows,每页显示条数")@RequestParam(value = "rows", defaultValue = "10", required = true)Integer rows,
			@ApiParam(required = true, name = "id", value = "收费项id,id") String id) {
		JSONObject message = new JSONObject();
		try {
			if (id != null) {
			    PageInfo<OrgExamItemDetailVo> pageInfo = orgExamItemService.getAllExamItemByFeeItem(id,page,rows);
				message.put("data", pageInfo);
				return InterfaceResultUtil.getReturnMapSuccess(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * @Title: getOrgExamItemByFeeItem @Description: TODO(查询指定收费项下的体检项) @param
	 *         id 收费项id @return JSONObject @throws
	 */

	@ApiOperation(value = "查询指定收费项下的体检项", httpMethod = "GET", response = JSONObject.class, notes = "查询指定收费项下的体检项")
	@RequestMapping("/getOrgExamItemByFeeItem")
	@ResponseBody
	public JSONObject getOrgExamItemByFeeItem(
			@ApiParam(required = true, name = "page", value = "page,当前页")@RequestParam(value = "page", defaultValue = "1", required = true)Integer page,
            @ApiParam(required = true, name = "rows", value = "rows,每页显示条数")@RequestParam(value = "rows", defaultValue = "10", required = true) Integer rows,
			@ApiParam(required = true, name = "id", value = "收费项id,id") String id) {
		JSONObject message = new JSONObject();
		try {
			if (id != null) {
				PageInfo<OrganizationExamItem> pageInfo = orgExamItemService.getExamItemByFeeItem(id,page,rows);
				message.put("data", pageInfo);
				return InterfaceResultUtil.getReturnMapSuccess(message);
			}
		} catch (Exception e) {
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	/**
	 * 
	 * @Title: getUserVoByItemId @Description: TODO(根据检查项ID查询医生) @param @param
	 *         id 检查项id @param @return 设定文件 @return JSONObject 返回类型 @throws
	 */
	@ApiOperation(value = "根据检查项ID查询医生", httpMethod = "GET", response = JSONObject.class, notes = "根据检查项ID查询医生")
	@RequestMapping("/getUserByItemId")
	@ResponseBody
	public JSONObject getUserVoByItemId(@ApiParam(required = true, name = "id", value = "检查项id,id") String id) {
		JSONObject message = new JSONObject();
		if (StringUtils.isEmpty(id)) {
			message.put("data", "参数有误");
			return InterfaceResultUtil.getReturnMapSuccess(message);
		}
		try {

			List<OrgUserVo> list = orgExamItemService.getUserVoByItemId(id);
			message.put("data", list);
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
	@RequestMapping("/saveUser")
	@ResponseBody
	public JSONObject saveUser(@ApiParam(required = true, name = "userId", value = "医生userId,userId") String userId,
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
		        message.put("data", "选择信息不全，请核对后操作");
                return InterfaceResultUtil.getReturnMapError(message);
		    }

		} catch (Exception e) {
			e.printStackTrace();

		}
		return InterfaceResultUtil.getReturnMapError(message);
	}

	@ApiOperation(value = "根据医生id查询体检项", httpMethod = "GET", response = JSONObject.class, notes = "根据医生id查询体检项")
	@RequestMapping("/getOrgExamItemByUserId")
	@ResponseBody
	public JSONObject getOrgExamItemByUserId(
			@ApiParam(required = true, name = "userId", value = "医生userId,userId") String userId) {
		JSONObject message = new JSONObject();
		if (StringUtils.isEmpty(userId)) {
			message.put("data", "参数有误");
			return InterfaceResultUtil.getReturnMapSuccess(message);
		}
		try {
			List<OrgExamItemVo> list = orgExamItemService.getOrgExamItemByUserId(userId);
			message.put("data", list);
			return InterfaceResultUtil.getReturnMapSuccess(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return InterfaceResultUtil.getReturnMapError(message);
	}
}
