/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsDataPerm.OpeChannelEnum;
import com.jeecms.system.domain.CmsDataPerm.OpeSiteEnum;
import com.jeecms.system.domain.dto.CmsDatePermDto;
import com.jeecms.system.domain.dto.OrgPermDto;
import com.jeecms.system.domain.dto.OwnerSitePermDto;
import com.jeecms.system.domain.dto.UserPermDto;
import com.jeecms.system.domain.vo.ChannelDataPermTree;
import com.jeecms.system.domain.vo.CmsDataPermVo.ChannelRow;
import com.jeecms.system.domain.vo.CmsSiteOwnerTree;
import com.jeecms.system.domain.vo.CoreMenuOwnerTree;
import com.jeecms.system.domain.vo.OrgOwnerSitePermVo;
import com.jeecms.system.domain.vo.OrgPermVo;
import com.jeecms.system.domain.vo.SiteDataPermTree;
import com.jeecms.system.domain.vo.UserOwnerSitePermVo;
import com.jeecms.system.domain.vo.UserPermVo;
import com.jeecms.system.service.CmsDataPermPlusService;
import com.jeecms.system.service.CmsDataPermService;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;

/**
 * 数据权限管理
 * 
 * @author: tom
 * @date: 2019年3月5日 下午3:50:54
 */
@RequestMapping("/dataPerm")
@RestController
public class CmsDatePermController extends BaseAdminController<CmsDataPerm, Integer> {

	/**
	 * 组装组织、角色、用户权限数据
	 * 
	 * @Title: assembleData
	 * @param orgId
	 *            组织ID
	 * @param roleId
	 *            角色ID
	 * @param userId
	 *            用户ID
	 * @param siteId
	 *            站点ID
	 * @param dataType
	 *            权限类型 1站点 2栏目 3文档 4菜单 5可管理站点（站群）
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo ResponseInfo
	 */
	@RequestMapping(method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSiteOwnerTree.class, excludes = { "site" }),
			@SerializeField(clazz = CoreMenuOwnerTree.class, excludes = { "menu" }),
			@SerializeField(clazz = SiteDataPermTree.class, excludes = { "site" }),
			@SerializeField(clazz = ChannelDataPermTree.class, excludes = { "channel" }),
			@SerializeField(clazz = ChannelRow.class, excludes = { "site" }),
			@SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name","notCurrUserOrg"}),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username","realname","orgName","roleNames" ,"notCurrUser"}),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName" ,"notCurrUserRole" }) ,
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName", "hidden" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }), })
	public ResponseInfo assembleData(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType)
			throws GlobalException {
		return new ResponseInfo(dataPermPlusService.assembleData(orgId, roleId, userId, siteId, dataType));
	}

	/**
	 * 获取栏目 文档类权限 站点列表
	 * 
	 * @Title: getSites
	 * @param orgId
	 *            组织
	 * @param roleId
	 *            角色
	 * @param userId
	 *            用户
	 * @param dataType
	 *            数据类型
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/site", method = RequestMethod.GET)
	@SerializeField(clazz = CmsSite.class, includes = { "id", "name" })
	public ResponseInfo getSites(Integer orgId, Integer roleId, Integer userId, Short dataType) throws GlobalException {
		return new ResponseInfo(dataPermPlusService.getSites(orgId, roleId, userId, dataType));
	}

	/**
	 * 组装 栏目的权限 组织、角色、权限数据
	 */
	@RequestMapping(value = "/channel/org", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "notCurrUserOrg","deleteAble" }),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "realname", "orgName", "roleNames",
					"notCurrUser","deleteAble"  }),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "notCurrUserRole","deleteAble"  }),
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }), })
	public ResponseInfo assembleDataForChannelOrg(Integer channelId, Short dataType, HttpServletRequest request)
			throws GlobalException {
		Channel channel = channelService.findById(channelId);
		CoreUser user = SystemContextUtils.getUser(request);
		List<OrgPermVo> vos = dataPermPlusService.assembleDataByChannelForOrg(channel, user.getOrg(), dataType);
		return new ResponseInfo(OrgPermVo.getChildTree(vos));
	}

	/**
	 * 组装 栏目的权限 用户数据
	 */
	@RequestMapping(value = "/channel/user", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "notCurrUserOrg" }),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "realname", "orgName", "roleNames",
					"notCurrUser" ,"deleteAble"}),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "notCurrUserRole" }),
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }), })
	public ResponseInfo assembleDataForChannelUser(Integer channelId, Short dataType, Pageable pageable, Integer orgid,
			Integer roleid, String key, HttpServletRequest request) throws GlobalException {
		Channel channel = channelService.findById(channelId);
		CoreUser user = SystemContextUtils.getUser(request);
		Page<UserPermVo> page = dataPermPlusService.assembleDataByChannelForUser(channel, user.getOrg(), dataType,
				orgid, roleid, key, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 组装 站点的权限 组织、角色、站点权限数据
	 */
	@RequestMapping(value = "/site/org", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "notCurrUserOrg","deleteAble"}),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "realname", "orgName", "roleNames",
					"notCurrUser","deleteAble" }),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "notCurrUserRole","deleteAble" }),
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }), })
	public ResponseInfo assembleDataBySiteForOrg(Integer siteId, HttpServletRequest request) throws GlobalException {
		CmsSite site = siteService.findById(siteId);
		CoreUser user = SystemContextUtils.getUser(request);
		List<OrgPermVo> vos = dataPermPlusService.assembleDataBySiteForOrg(site, user.getOrg());
		return new ResponseInfo(OrgPermVo.getChildTree(vos));
	}

	/**
	 * 组装 站点数据的权限 用户数据
	 */
	@RequestMapping(value = "/site/user", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "notCurrUserOrg","deleteAble" }),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "realname", "orgName", "roleNames",
					"notCurrUser","deleteAble" }),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "notCurrUserRole","deleteAble" }),
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }), })
	public ResponseInfo assembleDataBySiteForUser(Integer siteId, Pageable pageable, Integer orgid, Integer roleid,
			String key, HttpServletRequest request) throws GlobalException {
		CmsSite site = siteService.findById(siteId);
		CoreUser user = SystemContextUtils.getUser(request);
		Page<UserPermVo> page = dataPermPlusService.assembleDataBySiteForUser(site, user.getOrg(), orgid, roleid, key,
				pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 组装 站点的权限 组织、角色、站点权限数据
	 */
	@RequestMapping(value = "/ownerSite/org", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "notCurrUserOrg","deleteAble" }),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "realname", "orgName", "roleNames",
					"notCurrUser","deleteAble" }),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "notCurrUserRole","deleteAble" }),
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }) })
	public ResponseInfo assembleDataByOwnerSiteForOrg(Integer siteId, HttpServletRequest request)
			throws GlobalException {
		CmsSite site = siteService.findById(siteId);
		CoreUser user = SystemContextUtils.getUser(request);
		List<OrgOwnerSitePermVo> vos = dataPermPlusService.assembleDataByOwnerSiteForOrg(site, user.getOrg());
		return new ResponseInfo(OrgOwnerSitePermVo.getChildTree(vos));
	}

	/**
	 * 组装 站点数据的权限 用户数据
	 */
	@RequestMapping(value = "/ownerSite/user", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = CmsOrg.class, includes = { "id", "name", "notCurrUserOrg","deleteAble" }),
			@SerializeField(clazz = CoreUser.class, includes = { "id", "username", "realname", "orgName", "roleNames",
					"notCurrUser","deleteAble" }),
			@SerializeField(clazz = CoreRole.class, includes = { "id", "roleName", "notCurrUserRole","deleteAble" }),
			@SerializeField(clazz = CoreMenu.class, includes = { "id", "menuName" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }), })
	public ResponseInfo assembleDataByOwnerSiteForUser(Integer siteId, Pageable pageable, Integer orgid, Integer roleid,
			String key, HttpServletRequest request) throws GlobalException {
		CmsSite site = siteService.findById(siteId);
		CoreUser user = SystemContextUtils.getUser(request);
		Page<UserOwnerSitePermVo> page = dataPermPlusService.assembleDataByOwnerSiteForUser(site, user.getOrg(), orgid,
				roleid, key, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 组织、角色、用户分配权限
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo update(HttpServletRequest request, @RequestBody @Valid CmsDatePermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		dataPermService.updateByDto(dto);
		return new ResponseInfo();
	}

	/**
	 * 从栏目维度 分配组织、角色权限
	 * 
	 * @Title: updateChannelByOrg
	 * @param dto
	 *            OrgPermDto
	 * @param result
	 *            BindingResult
	 * @throws GlobalException
	 *             GlobalException
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/channel/org", method = RequestMethod.PUT)
	public ResponseInfo updateChannelByOrg(@RequestBody @Valid OrgPermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		/**检查栏目的权限分配 数据权限*/
		super.checkChannelDataPerm(dto.getDataId(), OpeChannelEnum.PERM_ASSIGN);
		dataPermService.updateDataPermByOrg(dto);
		return new ResponseInfo();
	}

	/**
	 * 从栏目维度 分配用戶权限
	 * 
	 * @Title: updateChannelByUser
	 * @param dto
	 *            UserPermDto
	 * @param result
	 *            BindingResult
	 * @throws GlobalException
	 *             GlobalException
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/channel/user", method = RequestMethod.PUT)
	public ResponseInfo updateChannelByUser(@RequestBody @Valid UserPermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		/**检查栏目的权限分配 数据权限*/
		super.checkChannelDataPerm(dto.getDataId(), OpeChannelEnum.PERM_ASSIGN);
		dataPermService.updateDataPermByUser(dto);
		return new ResponseInfo();
	}

	/**
	 * 从站点维度 分配组织、角色站点类数据权限
	 * 
	 * @Title: updateSiteByOrg
	 * @param dto
	 *            OrgPermDto
	 * @param result
	 *            BindingResult
	 * @throws GlobalException
	 *             GlobalException
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/site/org", method = RequestMethod.PUT)
	public ResponseInfo updateSiteByOrg(@RequestBody @Valid OrgPermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		/**检查站点的权限分配 数据权限*/
		super.checkSiteDataPerm(dto.getDataId(), OpeSiteEnum.PERM_ASSIGN);
		dataPermService.updateDataPermByOrg(dto);
		return new ResponseInfo();
	}

	/**
	 * 从站点维度 分配用戶站点类数据权限
	 * 
	 * @Title: updateSiteByUser
	 * @param dto
	 *            UserPermDto
	 * @param result
	 *            BindingResult
	 * @throws GlobalException
	 *             GlobalException
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/site/user", method = RequestMethod.PUT)
	public ResponseInfo updateSiteByUser(@RequestBody @Valid UserPermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		/**检查站点的权限分配 数据权限*/
		super.checkSiteDataPerm(dto.getDataId(), OpeSiteEnum.PERM_ASSIGN);
		dataPermService.updateDataPermByUser(dto);
		return new ResponseInfo();
	}

	/**
	 * 从站点维度 分配组织、角色 站群类权限
	 * 
	 * @Title: updateSiteByOrg
	 * @param dto
	 *            OrgPermDto
	 * @param result
	 *            BindingResult
	 * @throws GlobalException
	 *             GlobalException
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/ownerSite/org", method = RequestMethod.PUT)
	public ResponseInfo updateOwnerSiteByOrg(@RequestBody @Valid OwnerSitePermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		/**检查站点的权限分配 数据权限*/
		super.checkSiteDataPerm(dto.getSiteId(), OpeSiteEnum.PERM_ASSIGN);
		dataPermService.updateSiteOwner(dto);
		return new ResponseInfo();
	}

	/**
	 * 从站点维度 分配用戶 站群类权限
	 * 
	 * @Title: updateSiteByUser
	 * @param dto
	 *            UserPermDto
	 * @param result
	 *            BindingResult
	 * @throws GlobalException
	 *             GlobalException
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/ownerSite/user", method = RequestMethod.PUT)
	public ResponseInfo updateOwnerSiteByUser(@RequestBody @Valid OwnerSitePermDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		/**检查站点的权限分配 数据权限*/
		super.checkSiteDataPerm(dto.getSiteId(), OpeSiteEnum.PERM_ASSIGN);
		dataPermService.updateSiteOwner(dto);
		return new ResponseInfo();
	}

	@Autowired
	private CmsDataPermService dataPermService;
	@Autowired
	private CmsDataPermPlusService dataPermPlusService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private CmsSiteService siteService;

}
