package com.jeecms.admin.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.admin.controller.BaseTreeAdminController;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.ChannelAttrRes;
import com.jeecms.channel.domain.ChannelContentTpl;
import com.jeecms.channel.domain.dto.*;
import com.jeecms.channel.service.ChannelDtoService;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.ChannelErrorCodeEnum;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.domain.ContentChannel;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.content.service.CmsModelTplService;
import com.jeecms.content.service.ContentChannelService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.CmsDataPerm.OpeChannelEnum;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.SiteModelTpl;
import com.jeecms.system.domain.SysSecret;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 栏目管理控制controller
 *
 * @author: chenming
 * @date: 2019年4月29日 下午4:01:46
 */
@RestController
@RequestMapping("/channel")
public class ChannelController extends BaseTreeAdminController<Channel, Integer> {

	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private ChannelService service;
	@Autowired
	private CmsModelTplService cmsModelTplService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private ChannelDtoService channelDtoService;
	@Autowired
	private ContentChannelService contentChannelService;

	/**
	 * 1. 加载出来的列表必须是树形的列表，且是该用户可管理的列表 2.
	 * 选择parentId时是否可以选择"无"必须根据channelParentId作为路由标识去用户路由列表中查询如果有就显示否则不显示 3.
	 * 在controller层中必须校验是否有拥有该栏目的添加子栏目的权限，该用户是否拥有channelParentId作为路由标识
	 *
	 * 4. 可以通过
	 * SystemContextUtils.getUser(request).getViewChannels();查询出该用户可以加载删除的各种列表，
	 * 必须修改
	 */

	/**
	 * 新增栏目
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Valid ChannelSaveDto dto, BindingResult result, 
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		if (dto.getChannelParentId() == null || dto.getChannelParentId() == 0) {
			return new ResponseInfo(
					SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
					SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
		}
		return this.save(dto, request);
	}

	private ResponseInfo save(ChannelSaveDto dto, HttpServletRequest request) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		dto.setSiteId(site.getId());
		// 前端已经校验过了，但此处必须进行强校验，防止脏数据
		boolean status = service.checkElement(null, dto.getChannelName(), null, site.getId());
		CmsModel model = cmsModelService.findById(dto.getModelId());
		if (model == null || !model.getIsEnable() || !CmsModel.CHANNEL_TYPE.equals(model.getTplType())) {
			return new ResponseInfo(SettingErrorCodeEnum.INCOMING_MODEL_ERROR.getCode(),
					SettingErrorCodeEnum.INCOMING_MODEL_ERROR.getDefaultMessage());
		}
		if (status) {
			return new ResponseInfo(SettingErrorCodeEnum.CHANNEL_NAME_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.CHANNEL_NAME_ALREADY_EXIST.getDefaultMessage());
		}
		status = service.checkElement(null, null, dto.getChannelPath(), site.getId());
		if (status) {
			return new ResponseInfo(SettingErrorCodeEnum.CHANNEL_PATH_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.CHANNEL_PATH_ALREADY_EXIST.getDefaultMessage());
		}
		if (dto.getChannelParentId() != null) {
			List<ContentChannel> contentChannels = contentChannelService
					.findByChannelIds(new Integer[] { dto.getChannelParentId() });
			if (contentChannels != null && contentChannels.size() > 0) {
				contentChannels = contentChannels.stream().filter(contentChannel -> !contentChannel.getRecycle())
						.collect(Collectors.toList());
				if (contentChannels.size() > 0) {
					return new ResponseInfo(ChannelErrorCodeEnum.PARENT_CLASS_HAS_CONTENT_NOT_SAVE.getCode(),
							ChannelErrorCodeEnum.PARENT_CLASS_HAS_CONTENT_NOT_SAVE.getDefaultMessage());
				}
			}
			// 栏目创建子集栏目校验
			super.checkChannelDataPerm(dto.getChannelParentId(), OpeChannelEnum.CREATE);
		}
		Channel channel = service.save(dto);
		/** 主动维护站点栏目集合，方便权限根据站点取栏目数据 */
		CmsSite channelSite = channel.getSite();
		channelSite.getChannels().add(channel);
		if(channel.getParent()!=null){
			channel.getParent().getChild().add(channel);
		}
		cmsSiteService.update(channelSite);
		cmsSiteService.flush();
		return new ResponseInfo(true);
	}
	
	@RequestMapping(value = "/top", method = RequestMethod.POST)
	public ResponseInfo saveTop(@RequestBody @Valid ChannelSaveDto dto, BindingResult result, 
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		if (dto.getChannelParentId() != null && dto.getChannelParentId() != 0) {
			return new ResponseInfo(
					SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
					SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
		}
		return this.save(dto, request);
	}
	
	/**
	 * 校验栏目名称或者路径是否重复，true表示不可用，false表示可用
	 */
	@RequestMapping(value = "/element/unique", method = RequestMethod.GET)
	public ResponseInfo checkNameOrPath(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "path", required = false) String path,
			@RequestParam(name = "id", required = false) Integer id, HttpServletRequest request)
					throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Channel channel = null;
		if (id != null) {
			channel = service.findById(id);
			if (channel == null) {
				return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
						RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
			}
		}
		return new ResponseInfo(!service.checkElement(channel, name, path, siteId));
	}

	/**
	 * 新增时的字段
	 */
	@MoreSerializeField({ @SerializeField(clazz = CmsModel.class, includes = { "enableJson" }) })
	@RequestMapping(value = "/plus/{modelId}", method = RequestMethod.GET)
	public ResponseInfo getSave(@PathVariable(name = "modelId") Integer modelId) throws GlobalException {
		CmsModel model = cmsModelService.getChannelOrContentModel(modelId);
		if (CmsModel.CHANNEL_TYPE.equals(model.getTplType())) {
			return new ResponseInfo(model);
		}
		return new ResponseInfo();
	}

	/**
	 * 批量新增栏目
	 */
	@RequestMapping(value = "/multiple", method = RequestMethod.POST)
	public ResponseInfo saveAll(@RequestBody @Valid ChannelSaveMultipleDto dto, HttpServletRequest request,
			BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		dto.setSiteId(site.getId());
		if (dto.getChannelParentId() != null && dto.getChannelParentId() != 0) {
			List<ContentChannel> contentChannels = contentChannelService
					.findByChannelIds(new Integer[] { dto.getChannelParentId() });
			if (contentChannels != null && contentChannels.size() > 0) {
				contentChannels = contentChannels.stream()
						.filter(contentChannel -> !contentChannel.getRecycle())
						.collect(Collectors.toList());
				if (contentChannels.size() > 0) {
					return new ResponseInfo(
							ChannelErrorCodeEnum.PARENT_CLASS_HAS_CONTENT_NOT_SAVE
							.getCode(),
							ChannelErrorCodeEnum.PARENT_CLASS_HAS_CONTENT_NOT_SAVE
							.getDefaultMessage());
				}
			}
		} else {
			return new ResponseInfo(ChannelErrorCodeEnum.CHANNEL_PARENT_CLASS_ID_PASSED_ERROR.getCode(),
					ChannelErrorCodeEnum.CHANNEL_PARENT_CLASS_ID_PASSED_ERROR.getDefaultMessage());
		}
		List<String> channelNames = dto.getChannelNames();
		Integer spaceNum = null;
		Integer parentSpaceNum = 0;
		for (int i = 0; i < channelNames.size(); i++) {
			spaceNum = channelNames.get(i).replaceAll("([ ]*).*", "$1").length();
			if (i == 0 && spaceNum > 0) {
				return new ResponseInfo(
						ChannelErrorCodeEnum.CHANNEL_NAME_FORMAT_PASSED_ERROR.getCode(),
						ChannelErrorCodeEnum.CHANNEL_NAME_FORMAT_PASSED_ERROR
						.getDefaultMessage());
			}
			if ((spaceNum - parentSpaceNum) > 2 || (spaceNum % 2) != 0) {
				return new ResponseInfo(
						ChannelErrorCodeEnum.CHANNEL_NAME_FORMAT_PASSED_ERROR.getCode(),
						ChannelErrorCodeEnum.CHANNEL_NAME_FORMAT_PASSED_ERROR
						.getDefaultMessage());
			}
			parentSpaceNum = spaceNum;
		}
		List<ChannelSaveAllDto> dtos = channelDtoService.initChannelSaveAllDto(dto);
		super.checkChannelDataPerm(dto.getChannelParentId(), OpeChannelEnum.CREATE);
		List<Channel> channels = service.saveAll(dtos, site);
		/** 主动维护站点栏目集合，方便权限根据站点取栏目数据 */
		CmsSite channelSite = channels.get(0).getSite();
		for (Channel channel : channels) {
			channelSite.getChannels().add(channel);
		}
		cmsSiteService.update(channelSite);
		cmsSiteService.flush();
		return new ResponseInfo(true);
	}

	
	
	/**
	 * 修改主体
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo updateChannel(@RequestBody @Valid ChannelDto dto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		if (dto.getChannelParentId() == null || dto.getChannelParentId() == 0) {
			return new ResponseInfo(
					SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
					SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
		}
		return this.updateChannel(dto, request);
	}

	
	private ResponseInfo updateChannel(ChannelDto dto, HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Channel channel = service.findById(dto.getId());
		if (channel == null) {
			return new ResponseInfo(
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		// 前端已经校验过了，但此处必须进行强校验，防止脏数据
		boolean status = service.checkElement(channel, dto.getChannelName(), null, siteId);
		if (status) {
			return new ResponseInfo(SettingErrorCodeEnum.CHANNEL_NAME_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.CHANNEL_NAME_ALREADY_EXIST.getDefaultMessage());
		}
		status = service.checkElement(channel, null, dto.getChannelPath(), siteId);
		if (status) {
			return new ResponseInfo(SettingErrorCodeEnum.CHANNEL_PATH_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.CHANNEL_PATH_ALREADY_EXIST.getDefaultMessage());
		}
		if (dto.getChannelParentId() != null) {
			if (dto.getChannelParentId() == 0) {
				
				dto.setChannelParentId(null);
			} else {
				List<ContentChannel> contentChannels = contentChannelService
						.findByChannelIds(new Integer[] { dto.getChannelParentId() });
				if (contentChannels != null && contentChannels.size() > 0) {
					contentChannels = contentChannels.stream()
							.filter(contentChannel -> !contentChannel.getRecycle())
							.collect(Collectors.toList());
					if (contentChannels.size() > 0) {
						return new ResponseInfo(
								ChannelErrorCodeEnum.PARENT_CLASS_HAS_CONTENT_NOT_SAVE
								.getCode(),
								ChannelErrorCodeEnum.PARENT_CLASS_HAS_CONTENT_NOT_SAVE
								.getDefaultMessage());
					}
				}
			}
		}
		super.checkChannelDataPerm(dto.getId(), OpeChannelEnum.EDIT);
		service.updateChannel(dto);
		return new ResponseInfo(true);
	}
	
	/**
	 * 修改主体
	 */
	@RequestMapping(value = "/top",method = RequestMethod.PUT)
	public ResponseInfo updateChannelTop(@RequestBody @Valid ChannelDto dto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		if (dto.getChannelParentId() != null && dto.getChannelParentId() != 0) {
			return new ResponseInfo(
					SystemExceptionEnum.ILLEGAL_PARAM.getCode(),
					SystemExceptionEnum.ILLEGAL_PARAM.getDefaultMessage());
		}
		return this.updateChannel(dto, request);
	}
	
	
	/**
	 * 所有查询必须注意条件：站点
	 */
	@MoreSerializeField({
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "resourceType", 
					"alias", "url", "suffix" }),
			@SerializeField(clazz = ChannelAttrRes.class, includes = { "resourcesSpaceData", 
					"description", "secret" }),
			@SerializeField(clazz = SysSecret.class, includes = { "id", "name", "secretType" }),
			@SerializeField(clazz = ChannelContentTpl.class, includes = { "modelId", "tplMobile",
					"tplPc", "select" }) })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseInfo get(@PathVariable(name = "id") Integer id, HttpServletRequest request) 
			throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		return new ResponseInfo(service.findById(id, site));
	}

	@MoreSerializeField({ @SerializeField(clazz = Channel.class, includes = { 
			"viewContentAble", "editContentAble","mergeAble", "staticAble",
			"deleteContentAble", "fileContentAble", "topContentAble", "moveContentAble", "sortContentAble",
			"copyContentAble", "quoteContentAble", "typeContentAble", "createContentAble", 
			"publishContentAble","sitePushContentAble", "wechatPushContentAble", "weiboPushContentAble", 
			"editAble", "createChildAble","permAssignAble", "deleteAble", "viewAble" }) 
	})
	@RequestMapping(value = "/getPerm", method = RequestMethod.GET)
	public ResponseInfo getForPerm(Integer id, HttpServletRequest request) throws GlobalException {
		return new ResponseInfo(service.get(id));
	}

	/**
	 * 查询内容模型模板
	 * 
	 */
	@RequestMapping(value = "/content/model", method = RequestMethod.GET)
	@MoreSerializeField({ 
		@SerializeField(clazz = SiteModelTpl.class, includes = { "modelId", "pcTplPath",
			"mobileTplPath", "model" }), 
		@SerializeField(clazz = CmsModel.class, includes = { "modelName" }) 
	})
	public ResponseInfo findByModelId(@RequestParam Integer channelId, HttpServletRequest request)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSite(request).getId();
		return new ResponseInfo(service.findModelTplVo(siteId, channelId));
	}
	

	/**
	 * 根据条件查询list集合
	 *
	 * @param name
	 *            栏目名称 是否栏目列表，true是栏目列表的树 否则是内容列表的树
	 * @param permOut 权限是否输出 
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @Title: getList
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/term/tree", method = RequestMethod.GET)
	public ResponseInfo getList(@RequestParam(required = false) String name,
			Boolean permOut, HttpServletRequest request)
			throws GlobalException {
		CoreUser user = SystemContextUtils.getUser(request);
		List<Channel> channelList = user.getViewNoCycleChannels();
		Integer siteId = SystemContextUtils.getSiteId(request);
		if (channelList.size() > 0) {
			channelList = channelList.stream().filter(
					channel -> !channel.getHasDeleted() 
					&& 
					!channel.getRecycle() 
					&& 
					siteId.equals(channel.getSiteId()))
					.collect(Collectors.toList());
		}
		List<Channel> channels = channelList;
		/**
		 * 此处使用strem进行检索
		 */
		if (name != null) {
			channels = channelList.stream().filter(channel -> channel.getName().indexOf(name) != -1)
					.collect(Collectors.toList());
		}
		List<Channel> newChannnels = new ArrayList<Channel>();
		for (Channel channel : channels) {
			List<Channel> newChannels = channelList.stream()
					.filter(c -> c.getLft() < channel.getLft() && c.getRgt() > channel.getRgt())
					.collect(Collectors.toList());
			if (newChannels != null && newChannels.size() > 0) {
				newChannnels.addAll(newChannels);
			}
		}
		channels.addAll(newChannnels);
		List<Integer> ids = new ArrayList<Integer>();
		Iterator<Channel> iterator = channels.iterator();
		while (iterator.hasNext()) {
			Integer id = iterator.next().getId();
			if (ids.contains(id)) {
				iterator.remove();
			} else {
				ids.add(id);
			}
		}
		channels = channels.stream()
				.sorted(Comparator.comparing(Channel::getSortNum)
						.thenComparing(Comparator.comparing(Channel::getCreateTime)))
				.collect(Collectors.toList());
		if (permOut == null) {
			permOut = true;
		}
		long t3= System.currentTimeMillis();
		if (permOut) {
			return new ResponseInfo(super.getChildTree(channels, false, "id", "name",
					"editAble", "createChildAble","permAssignAble",
					"mergeAble", "staticAble", "viewAble", "deleteAble", "sortNum"));
		} else {
			return new ResponseInfo(super.getChildTree(channels, false, "id", "name","sortNum"));
		}
	}

	/**
	 * 根据条件查询list集合
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public ResponseInfo getTree(HttpServletRequest request, Boolean recycle) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		if (recycle == null) {
			recycle = false;
		}
		List<Channel> channelList = service.getChanelByRecycle(siteId, recycle);
		if (channelList != null && channelList.size() > 0) {
			channelList = channelList.stream()
					.sorted(Comparator.comparing(Channel::getSortNum)
							.thenComparing(
									Comparator.comparing(Channel::getCreateTime)))
					.collect(Collectors.toList());
		} else {
			channelList = new ArrayList<Channel>();
		}
		// return new ResponseInfo(super.getChildTree(channelList, false, "id",
		// "name"));
		JSONArray jsonArray = super.getChildTree(channelList, false, "id", "name", "editAble",
				"createChildAble","deleteAble", "mergeAble", "staticAble", "permAssignAble",
				"viewAble");
		return new ResponseInfo(jsonArray);
	}

	/**
	 * 忽略权限的栏目树（目前用于索引设置开关）
	 */
	@RequestMapping(value = "/getAllTree", method = RequestMethod.GET)
	public ResponseInfo getAllTree(Integer siteId, HttpServletRequest request) throws GlobalException {
		if (siteId == null) {
			siteId = SystemContextUtils.getSiteId(request);
		}
		List<Channel> channelList = service.findList(siteId, true);
		channelList = channelList.stream().filter(channel -> channel.getRecycle().equals(false))
				.sorted(Comparator.comparing(Channel::getSortNum)
						.thenComparing(Comparator.comparing(Channel::getCreateTime)))
				.collect(Collectors.toList());
		return new ResponseInfo(super.getChildTree(channelList, false, "name", "id", 
				"isOpenIndex", "urlWhole"));
	}

	/**
	 * 获取可生成栏目静态页的栏目树
	 */
	@RequestMapping(value = "/staticChannelTree", method = RequestMethod.GET)
	public ResponseInfo getCreateChannelPageTree(Integer siteId, HttpServletRequest request) 
			throws GlobalException {
		if (siteId == null) {
			siteId = SystemContextUtils.getSiteId(request);
		}
		final Integer sid = siteId;
		List<Channel> channelList = SystemContextUtils.getUser(request).getStaticChannels();
		channelList = channelList.stream().filter(channel -> channel.getRecycle().equals(false))
				.filter(channel -> channel.getSiteId().equals(sid))
				.sorted(Comparator.comparing(Channel::getSortNum)
						.thenComparing(Comparator.comparing(Channel::getCreateTime)))
				.collect(Collectors.toList());
		return new ResponseInfo(super.getChildTree(channelList, false, "name", "id", "hasStaticChannel"));
	}

	/**
	 * 获取可发布内容的栏目树
	 */
	@RequestMapping(value = "/publicContentChannelTree", method = RequestMethod.GET)
	public ResponseInfo getPublishContentPageTree(Integer siteId, HttpServletRequest request) 
			throws GlobalException {
		if (siteId == null) {
			siteId = SystemContextUtils.getSiteId(request);
		}
		final Integer sid = siteId;
		List<Channel> channelList = SystemContextUtils.getUser(request).getPublishContentChannels();
		channelList = channelList.stream().filter(channel -> channel.getRecycle().equals(false))
				.filter(channel -> channel.getSiteId().equals(sid))
				.sorted(Comparator.comparing(Channel::getSortNum)
						.thenComparing(Comparator.comparing(Channel::getCreateTime)))
				.collect(Collectors.toList());
		return new ResponseInfo(super.getChildTree(channelList, false, "name", "id", "hasStaticContent"));
	}

	/**
	 * 获取内容的通用栏目树
	 */
	@RequestMapping(value = "/content/common/tree", method = RequestMethod.GET)
	public ResponseInfo getContentCommentTree(@RequestParam(required = false) String name, Integer siteId,
		@RequestParam Short operator,Boolean permOut, HttpServletRequest request) throws GlobalException {
		if (siteId == null) {
			siteId = SystemContextUtils.getSiteId(request);
		}
		final Integer sid = siteId;
		Long t3 = System.currentTimeMillis();
		List<Channel> channels = SystemContextUtils.getUser(request)
				.getContentChannelsByOperator(sid, operator);
		Long t4 = System.currentTimeMillis();
		if (channels.size() > 0) {
			/**
			 * 此处使用strem进行检索
			 */
			if (name != null) {
				channels = channels.stream().filter(channel -> channel.getName().contains(name))
						.collect(Collectors.toList());
			}
			channels = channels.stream()
					.filter(channel -> !channel.getRecycle() && !channel.getHasDeleted()
							&& channel.getSiteId().equals(sid))
					.sorted(Comparator.comparing(Channel::getSortNum)
							.thenComparing(
									Comparator.comparing(Channel::getCreateTime)))
					.collect(Collectors.toList());
		}
		// return new ResponseInfo(super.getChildTree(channels, false, "name",
		// "id"));
		if(permOut == null){
			permOut = true;
		}
		/***/
		//List<Channel> channelList = new ArrayList<Channel>();
		//暂时取消
		//channelList.addAll(service.findAllById(Channel.fetchIds(channels)));
		//改为直接使用缓存的集合
		//channelList = channels;
		if(permOut) {
			return new ResponseInfo(super.getChildTree(channels, false, "name", "id", "viewContentAble",
					"editContentAble", "deleteContentAble", "fileContentAble", "topContentAble", 
					"moveContentAble", "sortContentAble","copyContentAble", "quoteContentAble", 
					"typeContentAble", "createContentAble", "publishContentAble","sitePushContentAble", 
					"wechatPushContentAble", "weiboPushContentAble","isBottom"));
		} else {
			return new ResponseInfo(super.getChildTree(channels, false, "name", "id","isBottom"));
		}
	}

	/**
	 * 获取栏目的通用栏目树
	 */
	@RequestMapping(value = "/common/tree", method = RequestMethod.GET)
	public ResponseInfo getCommentTree(Integer siteId, @RequestParam Short operator, HttpServletRequest request)
			throws GlobalException {
		if (siteId == null) {
			siteId = SystemContextUtils.getSiteId(request);
		}
		final Integer sid = siteId;
		List<Channel> channels = SystemContextUtils.getUser(request).getChannelsByOperator(siteId, operator);
		if (channels.size() > 0) {
			channels = channels.stream()
					.filter(channel -> !channel.getRecycle() && !channel.getHasDeleted()
							&& channel.getSiteId().equals(sid))
					.sorted(Comparator.comparing(Channel::getSortNum)
							.thenComparing(
									Comparator.comparing(Channel::getCreateTime)))
					.collect(Collectors.toList());
		}
		return new ResponseInfo(super.getChildTree(channels, false, "name", "id", "editAble", "createChildAble",
				"deleteAble", "mergeAble", "staticAble", "permAssignAble", "viewAble"));
	}

	/**
	 * 查询所有的PC端模板、手机模板(模型之下)
	 */
	@RequestMapping(value = "/template/list", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsModelTpl.class, includes = { "tplPath" }) })
	public ResponseInfo findByModel(@RequestParam Integer modelId, @RequestParam Short type,
			HttpServletRequest request) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		CmsSiteConfig siteConfig = site.getCmsSiteCfg();
		List<CmsModelTpl> cmtList = null;
		// 通过站点配置的模板方案名查询出该站点下该模型配置的PC模板路径
		if (type == CmsModelTpl.TPL_TYPE_CHANNEL) {
			cmtList = cmsModelTplService.models(site.getId(), modelId, siteConfig.getPcSolution());
		}
		// 通过站点配置的模板方案名查询出该站点下该模型配置的手机模板路径
		if (type == CmsModelTpl.TPL_TYPE_CONTENT) {
			cmtList = cmsModelTplService.models(site.getId(), modelId, siteConfig.getMobileSolution());
		}
		return new ResponseInfo(cmtList);
	}

	/**
	 * 用于回收站对栏目操作(未过滤加入回收站的栏目)
	 */
	@RequestMapping(value = "/recycle/tree", method = RequestMethod.GET)
	public ResponseInfo getRecycleTree(HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 此处忽略掉权限，因为此处有两种甚至更多种权限：1. 查看栏目、2.修改栏目、3.修改内容、4.查看内容
		List<Channel> channelList = service.findListBySiteId(siteId);
		channelList = channelList.stream().sorted(Comparator.comparing(Channel::getSortNum)
						.thenComparing(Comparator.comparing(Channel::getCreateTime).reversed()))
				.collect(Collectors.toList());
		return new ResponseInfo(super.getChildTree(channelList, false, "name", "id", "recycle"));
	}

	/**
	 * 查询所有的栏目模型(全局+站点)
	 */
	@RequestMapping(value = "/model/list", method = RequestMethod.GET)
	@MoreSerializeField({ 
		@SerializeField(clazz = CmsModel.class, includes = { "id", "modelName" }) 
	})
	public ResponseInfo findModelList(
			@RequestParam Short tplType, HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(cmsModelService.findList(tplType, siteId));
	}

	/**
	 * 加入回收站
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		for (int i = 0; i < ids.getIds().length; i++) {
			super.checkChannelDataPerm(ids.getIds()[i], OpeChannelEnum.DEL);
		}
		service.delete(ids.getIds(), false);
		return new ResponseInfo(true);
	}

	/**
	 * 彻底删除(逻辑删除)
	 */
	@RequestMapping(value = "/thorough", method = RequestMethod.DELETE)
	public ResponseInfo thoroughDelete(@RequestBody @Valid DeleteDto ids, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		for (int i = 0; i < ids.getIds().length; i++) {
			super.checkChannelDataPerm(ids.getIds()[i], OpeChannelEnum.DEL);
		}
		service.delete(ids.getIds(), true);
		return new ResponseInfo(true);
	}

	/**
	 * 还原栏目
	 */
	@RequestMapping(value = "/reduction", method = RequestMethod.PUT)
	public ResponseInfo reduction(@RequestBody @Valid ChannelReductionDto dto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 此处忽略掉权限，因为此处有两种甚至更多种权限：1. 查看栏目、2.修改栏目、3.修改内容、4.查看内容
		dto.setSiteId(siteId);
		service.reduction(dto);
		return new ResponseInfo();
	}

	/**
	 * 设置栏目静态化开关
	 */
	@RequestMapping(value = "/setOpenIndex", method = RequestMethod.PUT)
	public ResponseInfo setOpenIndex(@RequestBody @Valid ChannelSetIndexDto channelOpens, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		service.setOpenIndex(channelOpens);
		return new ResponseInfo();
	}

	/**
	 * 应用工作流
	 */
	@RequestMapping(value = "/claim/workflow", method = RequestMethod.POST)
	public ResponseInfo claimWorkflow(@RequestBody @Valid ChannelWorkflowDto dto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		// 测试
		for (int i = 1; i < dto.getIds().size(); i++) {
			super.checkChannelDataPerm(dto.getIds().get(i), OpeChannelEnum.EDIT);
		}

		if (dto.getAll()) {
			List<Channel> channels = SystemContextUtils.getCoreUser().getEditChannels();
			if (channels.size() > 0) {
				Integer siteId = SystemContextUtils.getSiteId(request);
				channels = channels.stream().filter(
						channel -> channel.getSiteId().equals(siteId) 
						&& 
						!channel.getId().equals(dto.getChannelId()))
						.collect(Collectors.toList());
			} else {
				channels = new ArrayList<Channel>();
			}
			dto.setChannels(channels);
		}
		service.claimWorkflow(dto);
		return new ResponseInfo(true);
	}

	/**
	 * 查询该站点已经删除的栏目ID集合
	 */
	@RequestMapping(value = "/recycle/ids", method = RequestMethod.GET)
	public ResponseInfo getrecycleIds(HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<Channel> channelList = service.findAll(true);
		List<Integer> ids = new ArrayList<Integer>();
		if (channelList != null && channelList.size() > 0) {
			channelList = channelList.stream().filter(channel -> channel.getSiteId().equals(siteId))
					.filter(channel -> channel.getRecycle()).collect(Collectors.toList());
			ids = channelList.stream().map(channel -> channel.getId()).collect(Collectors.toList());
		}
		return new ResponseInfo(ids);

	}

	/**
	 * 移动排序
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public ResponseInfo channelSort(@RequestBody @Validated ChannelSortDto dto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		super.checkChannelDataPerm(dto.getChannelId(), OpeChannelEnum.EDIT);
		List<Channel> channelList = SystemContextUtils.getUser(request).getViewChannels();
		service.channelSort(dto, channelList);
		return new ResponseInfo();
	}

	/**
	 * 合并栏目
	 */
	@RequestMapping(value = "/merge", method = RequestMethod.PUT)
	public ResponseInfo merge(@RequestBody @Valid ChannelMergeDto dto, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		Integer id = dto.getId();
		Channel channel = service.findById(id);
		if (channel == null) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		if (!channel.getIsBottom()) {
			return new ResponseInfo(ChannelErrorCodeEnum.AIMS_CHANNEL_IS_NOT_BOTTOM_NOT_MERGE.getCode(),
					ChannelErrorCodeEnum.AIMS_CHANNEL_IS_NOT_BOTTOM_NOT_MERGE.getDefaultMessage());
		}
		Integer[] ids = dto.getIds();
		for (Integer i : ids) {
			if (i.equals(id)) {
				return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
						RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
			}
			super.checkChannelDataPerm(i, OpeChannelEnum.MERGE);
		}
		super.checkChannelDataPerm(id, OpeChannelEnum.MERGE);
		service.mergeChannel(channel, ids);
		return new ResponseInfo(true);
	}

	/**
	 * 校验该栏目是否拥有工作流
	 */
	@RequestMapping(value = "/workflow/being/{id}", method = RequestMethod.GET)
	public ResponseInfo uniqueWorkflow(@PathVariable(name = "id") Integer id) {
		if (id == null) {
			// 如果ID为空，则说明前端处理或者是数据有问题，此处不认为是BUG不抛出异常
			return new ResponseInfo(false);
		}
		Channel channel = service.findById(id);
		if (channel == null || channel.getHasDeleted() || channel.getRecycle()) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		if (channel.getRealWorkflowId() != null) {
			return new ResponseInfo(true);
		}
		return new ResponseInfo(false);
	}

	/**
	 * 新增内容查看部分栏目默认值
	 */
	@RequestMapping(value = "/defaults/{id}", method = RequestMethod.GET)
	public ResponseInfo defaults(@PathVariable(name = "id") Integer id) {
		// 此处不校验权限，因为如果校验的是查看权限可能满足不了，如果校验新增权限，又不符合使用场景
		Channel channel = service.findById(id);
		if (channel == null || channel.getHasDeleted() || channel.getRecycle()) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		// 此处不用对象返回时因为返回VO对象，里面的对象也是JSON，因为里面对象的数据量的大小和对象的名称无法确定
		JSONObject returnJson = new JSONObject();
		returnJson.put(CmsModelConstant.FIELD_SYS_VIEW_CONTROL, channel.getChannelExt().getRealViewControl());
		returnJson.put(
				CmsModelConstant.FIELD_SYS_COMMENT_CONTROL, channel.getChannelExt()
				.getRealCommentControl());
		return new ResponseInfo(returnJson);
	}
}
