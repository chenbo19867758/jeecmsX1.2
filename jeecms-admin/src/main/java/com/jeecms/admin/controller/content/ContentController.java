/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.content;

import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.audit.service.AuditChannelSetService;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.UserModelRecord;
import com.jeecms.auth.service.UserModelRecordService;
import com.jeecms.auth.service.UserModelSortService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.*;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.local.ThreadPoolService;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.content.domain.dto.ContentCopyDto;
import com.jeecms.content.domain.dto.ContentPushSitesDto;
import com.jeecms.content.domain.dto.ContentSaveDto;
import com.jeecms.content.domain.dto.ContentUpdateDto;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.content.service.CmsModelTplService;
import com.jeecms.content.service.ContentLuceneService;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.impl.ContentDocServiceImpl;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.*;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.ContentMarkService;
import com.jeecms.system.service.ContentSourceService;
import com.jeecms.system.service.SysSecretService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 内容controller层
 * 
 * @author: chenming
 * @date: 2019年5月16日 上午8:49:33
 */
@RequestMapping("/content")
@RestController
@Validated
public class ContentController extends BaseController<Content, Integer> {
	static Logger logger = LoggerFactory.getLogger(ContentController.class);

	@Autowired
	private ContentService service;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private CmsModelTplService cmsModelTplService;
	@Autowired
	private SysSecretService secretService;
	@Autowired
	private ContentSourceService contentSourceService;
	@Autowired
	private ContentMarkService contentMarkService;
	@Autowired
	private ContentLuceneService luceneService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private UserModelRecordService userModelRecordService;
	@Autowired
	private ContentDocServiceImpl docServiceImpl;
	@Autowired
	private ResourcesSpaceDataService resourcesSpaceDataService;
	@Autowired
	private UserModelSortService userModelSortService;
	@Autowired
	private AuditChannelSetService auditChannelSetService;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 模型字段中有一个附件上传，附件上传的密级是否显示应当通过全局配置获取
	 */

	/**
	 * 通过模型内容id获取到其相应的字段
	 */
	@MoreSerializeField({ @SerializeField(clazz = CmsModel.class, includes = { "enableJson" }) })
	@RequestMapping(value = "/plus/{modelId}", method = RequestMethod.GET)
	public ResponseInfo getSave(@PathVariable(name = "modelId") Integer modelId, HttpServletRequest request)
			throws GlobalException {
		CmsModel model = cmsModelService.getChannelOrContentModel(modelId);
		if (CmsModel.CONTENT_TYPE.equals(model.getTplType())) {
			return new ResponseInfo(model);
		}
		return new ResponseInfo();
	}

	/**
	 * 通用的校验栏目的方法
	 */
	private ResponseInfo checkChannel(Channel channel) {
		if (channel == null) {
			return new ResponseInfo(SettingErrorCodeEnum.CHANNEL_IS_NOT_NULL.getCode(),
					SettingErrorCodeEnum.CHANNEL_IS_NOT_NULL.getDefaultMessage(), false);
		}
		if (!channel.getIsBottom()) {
			return new ResponseInfo(SettingErrorCodeEnum.NOT_THE_BOTTOM_CHANNEL.getCode(),
					SettingErrorCodeEnum.NOT_THE_BOTTOM_CHANNEL.getDefaultMessage(), false);
		}
		return new ResponseInfo();
	}

	/**
	 * 通用的校验内容标题的方法
	 */
	private boolean checkTitle(HttpServletRequest request, String title, Integer channelId, boolean isReturn)
			throws GlobalException {
		Integer titleRepeat = SystemContextUtils.getSite(request).getConfig().getTitleRepeat();
		boolean titleStatus = true;
		// 站点内不允许重复
		if (titleRepeat == 2) {
			if (service.checkTitle(title, null)) {
				if (isReturn) {
					titleStatus = false;
				} else {
					throw new GlobalException(
							ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT);
				}
			}
		}
		// 同一栏目内不允许重复
		if (titleRepeat == 3) {
			if (service.checkTitle(title, channelId)) {
				if (isReturn) {
					titleStatus = false;
				} else {
					throw new GlobalException(
							ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT);
				}
			}
		}
		return titleStatus;
	}

	/**
	 * 校验栏目是否重复
	 */
	@RequestMapping(value = "/title/unique", method = RequestMethod.GET)
	public ResponseInfo checkTitle(HttpServletRequest request, @RequestParam String title,
			@RequestParam Integer channelId,@RequestParam(required = false) Integer contentId) 
					throws GlobalException {
		if (contentId != null) {
			Content content = service.findById(contentId);
			if (title.equals(content.getTitle())) {
				return new ResponseInfo(true);
			}
		}
		return new ResponseInfo(this.checkTitle(request, title, channelId, true));
	}

	/**
	 * 新增
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Validated ContentSaveDto dto, HttpServletRequest request,
			BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		String title = dto.getTitle().getString(Content.TITLE_NAME);
		this.checkTitle(request, title, dto.getChannelId(), false);
		Integer userId = SystemContextUtils.getUserId(request);
		dto.setUserId(userId);
		dto.setPublishUserId(userId);
		Channel channel = channelService.findById(dto.getChannelId());
		this.checkChannel(channel);
		if (!channel.getCreateContentAble()) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		if (dto.getType() == ContentConstant.STATUS_PUBLISH) {
			if (!channel.getPublishContentAble()) {
				return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
			}
			// 当前时间
			Long currentTime = System.currentTimeMillis();
			// 发布时间
			Long releaseTime = MyDateUtils.parseDate(
					dto.getReleaseTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN).getTime();
			if (StringUtils.isNotBlank(dto.getOfflineTime())) {
				// 下线时间
				Long offlineTime = MyDateUtils.parseDate(
						dto.getOfflineTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN)
						.getTime();
				// 如果下线时间小于当前时间并且下线时间小于发布时间抛出异常
				if (offlineTime < currentTime && offlineTime < releaseTime) {
					return new ResponseInfo(
							ContentErrorCodeEnum.CONTENT_CANNOT_BE_PUBLISHED.getCode(),
							ContentErrorCodeEnum.CONTENT_CANNOT_BE_PUBLISHED
							.getDefaultMessage());
				}
			}
		}
		Content content = service.save(dto, SystemContextUtils.getSite(request));
		return new ResponseInfo(content.getStatus());
	}

	/**
	 * 提交审核
	 */
	@PostMapping("/submit")
	public ResponseInfo submit(@RequestBody @Valid ContentUpdateDto dto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		Channel channel = channelService.findById(dto.getChannelId());
		if (dto.getId() != null) {
			Content content = service.findById(dto.getId());
			if (!dto.getTitle().getString(Content.TITLE_NAME).equals(content.getTitle())) {
				this.checkTitle(request, dto.getTitle().getString(Content.TITLE_NAME), 
						dto.getChannelId(), false);
			}
		} else {
			this.checkTitle(request, dto.getTitle().getString(Content.TITLE_NAME), 
					dto.getChannelId(), false);
		}
		this.checkChannel(channel);
		// TODO 校验栏目是否包含工作流，
		// TODO 校验内容的状态只能是流转中
		if (!channel.getCreateContentAble()) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		service.submit(dto, request, false, null);
		return new ResponseInfo();
	}

	/**
	 * 查询所有的内容模型(全局+站点)
	 * ps:升级接口，将接口改成勾选了的才显示；兼容之前的接口
	* @Title: findModelList 
	* @param request 请求
	* @param channelId 栏目ID
	* @throws GlobalException 异常
	* @since 1.2
	 */
	@RequestMapping(value = "/model/list", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsModel.class, includes = { "id", "modelName" }) })
	public ResponseInfo findModelList(HttpServletRequest request,
			 Integer channelId) throws GlobalException {
		if (channelId != null) {
			CoreUser user = SystemContextUtils.getCoreUser();
			Integer siteId = SystemContextUtils.getSiteId(request);
			List<CmsModel> models = cmsModelService.findList(CmsModel.CONTENT_TYPE, siteId);
			return new ResponseInfo(userModelSortService.sort(models, channelId, user.getId()));
		} else {
			Integer siteId = SystemContextUtils.getSiteId(request);
			return new ResponseInfo(cmsModelService.findList(CmsModel.CONTENT_TYPE, siteId));
		}

	}

	/**
	 * 查询所有的PC端模板、手机模板(模型之下)
	 */
	@RequestMapping(value = "/template/list", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = CmsModelTpl.class, includes = { "tplSolution" }) })
	public ResponseInfo findByModel(@RequestParam Integer modelId, @RequestParam Integer type,
			HttpServletRequest request) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		CmsSiteConfig siteConfig = site.getCmsSiteCfg();
		List<CmsModelTpl> cmtList = null;
		// 通过站点配置的模板方案名查询出该站点下该模型配置的PC模板路径
		if (type == 1) {
			cmtList = cmsModelTplService.models(site.getId(), modelId, siteConfig.getPcSolution());
		}
		// 通过站点配置的模板方案名查询出该站点下该模型配置的手机模板路径
		if (type == 2) {
			cmtList = cmsModelTplService.models(site.getId(), modelId, siteConfig.getMobileSolution());
		}
		return new ResponseInfo(cmtList);
	}

	/**
	 * 内容、附件密级列表
	 */
	@RequestMapping(value = "/secret/list", method = RequestMethod.GET)
	@MoreSerializeField({ 
		@SerializeField(clazz = SysSecret.class, includes = { "id", "name" }) 
	})
	public ResponseInfo secretList(
			@Range(min = 1, max = 2, message = "类型只有1或者2") @RequestParam Integer secretType) {
		return new ResponseInfo(secretService.findByType(secretType));
	}

	/**
	 * 来源列表(可根据来源名称进行模糊检索)
	 */
	@RequestMapping(value = "/source/list", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = ContentSource.class, 
			includes = { "sourceName", "sourceLink", "isDefault" }) })
	public ResponseInfo sourceList(@RequestParam(required = false) String sourceName) {
		HashMap<String, String[]> params = new HashMap<String, String[]>(1);
		if (!StringUtils.isBlank(sourceName)) {
			params.put("LIKE_sourceName_String", new String[] { sourceName });
		}
		return new ResponseInfo(contentSourceService.getList(params, null, false));
	}

	/**
	 * 发文字号列表
	 */
	@RequestMapping(value = "/mark/list", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = ContentMark.class, includes = { "markName", "id" }) })
	public ResponseInfo markList(@Range(min = 1, max = 2, message = "类型只有1或者2") @RequestParam Integer markType) {
		return new ResponseInfo(contentMarkService.getList(markType));
	}

	/**
	 * 通过模型内容id获取到其相应的字段
	 */
	@MoreSerializeField({
			@SerializeField(clazz = ContentAttrRes.class, includes = { "description", "secret", 
					"resourcesSpaceData" }),
			@SerializeField(clazz = ContentSource.class, includes = { "id", "sourceName", "sourceLink" }),
			@SerializeField(clazz = SysSecret.class, includes = { "id", "name" }),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "resourceType", 
					"alias", "suffix", "url" }),
			@SerializeField(clazz = ContentType.class, includes = { "id", "typeName", "logoResource" }) })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseInfo get(@PathVariable(name = "id") Integer id, HttpServletRequest request) 
			throws GlobalException {
		Content content = service.findById(id);
		if (content == null) {
			return new ResponseInfo(null);
		}
		if (!content.getViewContentAble()) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		GlobalConfig globalConfig = SystemContextUtils.getGlobalConfig(request);
		return new ResponseInfo(service.findContent(id, globalConfig));
	}

	/**
	 * 修改
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo update(@RequestBody @Valid  ContentUpdateDto dto, HttpServletRequest request,
			BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		Content content = service.findById(dto.getId());
		if (!content.getEditContentAble()) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		if (dto.getType() == ContentConstant.STATUS_PUBLISH) {
			if (!content.getPublishContentAble()) {
				return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
						UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), 
						false);
			}
		}
		Integer userId = SystemContextUtils.getUserId(request);
		dto.setModelId(null);
		dto.setUserId(userId);
		dto.setPublishUserId(userId);
		dto.setUser(SystemContextUtils.getUser(request));
		Content content1 = service.update(dto, request);
		return new ResponseInfo(content1.getStatus());
	}

	/**
	 * 复制内容
	 */
	@RequestMapping(value = "/duplication", method = RequestMethod.POST)
	public ResponseInfo copy(@RequestBody @Validated ContentCopyDto dto, HttpServletRequest request)
			throws GlobalException {
		Channel channel = channelService.findById(dto.getChannelId());
		this.checkChannel(channel);
		if (!channel.getCreateContentAble()) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		/**
		 * 前台选择传入的内容一定是在同一个栏目之下的，本质而言权限是由栏目控制的，既然栏目一样，那么保证一个内容(栏目)有权限那么其它内容一定有权限
		 */
		Content content = service.findById(dto.getIds().get(0));
		if (!content.getCopyContentAble()) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		service.copy(dto, request);
		return new ResponseInfo();
	}

	/**
	 * 重置索引
	 * 
	 * @Title: resetIndex
	 * @param channelId
	 *            栏目Id
	 * @param siteId
	 *            站点Id
	 * @param releaseTimeStart
	 *            发布时间开始
	 * @param releaseTimeEnd
	 *            发布时间结束
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @throws InterruptedException
	 *             InterruptedException
	 * @throws ExecutionException
	 *             ExecutionException
	 * @return: ResponseInfo
	 */
	@PutMapping(value = "/resetIndex")
	public ResponseInfo resetIndex(Integer channelId, Integer siteId, Date releaseTimeStart, Date releaseTimeEnd,
			HttpServletRequest request) 
					throws GlobalException, IOException, InterruptedException, ExecutionException {
		if (siteId != null) {
			siteId = SystemContextUtils.getSiteId(request);
		}
		Integer sid = siteId;
		
		File file = new File(this.getClass().getClassLoader().getResource("").getPath().concat("hanlp"));
		if (!file.exists() || file.list().length <= 0) {
			return new ResponseInfo(SettingErrorCodeEnum.THE_SYSTEM_IS_INITIALIZING.getCode(),
					SettingErrorCodeEnum.THE_SYSTEM_IS_INITIALIZING.getDefaultMessage());
		}
		
		/**
		 * 没有返回结果的异步任务，先返回消息，后台运行生成索引
		 */
		ThreadPoolService.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					luceneService.resetIndex(channelId, sid, releaseTimeStart, releaseTimeEnd);
				} catch (GlobalException e) {
					logger.error(e.getMessage());
				}
			}
		});
		return new ResponseInfo(true);
	}

	/**
	 * 推送到站群
	 */
	@PutMapping(value = "/push/sites")
	public ResponseInfo pushSites(@RequestBody @Valid ContentPushSitesDto dto, HttpServletRequest request,
			BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		List<Content> contents = service.findAllById(dto.getContentIds());
		if (contents == null || contents.size() == 0) {
			return new ResponseInfo(false);
		}
		Map<Integer, Integer> channelIdMap = new HashMap<Integer, Integer>();
		for (Content content : contents) {
			channelIdMap.put(content.getChannelId(), 0);
		}
		if (channelIdMap.keySet().size() > 1) {
			return new ResponseInfo(ContentErrorCodeEnum.QUOTED_CONTENT_CANNOT_BE_PUSHED.getCode(),
					ContentErrorCodeEnum.QUOTED_CONTENT_CANNOT_BE_PUSHED.getDefaultMessage());
		}
		if (!service.validType(CmsDataPerm.OPE_CONTENT_SITE_PUSH, contents.get(0).getChannelId())) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		Channel channel = channelService.findById(dto.getChannelId());
		if (!service.validType(CmsDataPerm.OPE_CONTENT_CREATE, channel.getId())) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		CmsSiteConfig config = cmsSiteService.findById(dto.getSiteId()).getCmsSiteCfg();
		if (!config.getSitePushOpen()) {
			return new ResponseInfo(SiteErrorCodeEnum.SITE_CANNOT_PUSH_CONTENT.getCode(),
					SiteErrorCodeEnum.SITE_CANNOT_PUSH_CONTENT.getDefaultMessage(), false);
		}
		if (!channel.getSiteId().equals(dto.getSiteId())) {
			return new ResponseInfo(ChannelErrorCodeEnum.CHANNEL_IS_NOT_UNDER_THE_SITE.getCode(),
					ChannelErrorCodeEnum.CHANNEL_IS_NOT_UNDER_THE_SITE.getDefaultMessage(), false);
		}
		boolean isPushSecret = false;
		String pushSecret = config.getSitePushSecret();
		if (StringUtils.isNotBlank(pushSecret)) {
			if (pushSecret.equals(dto.getPushSecret())) {
				isPushSecret = true;
			}
		} else {
			isPushSecret = true;
		}
		if (isPushSecret) {
			dto.setContents(contents);
			dto.setChannel(channel);
			service.pushSites(dto, request);
		} else {
			return new ResponseInfo(SiteErrorCodeEnum.PUSH_SECRET_ERROR.getCode(),
					SiteErrorCodeEnum.PUSH_SECRET_ERROR.getDefaultMessage(), false);
		}
		return new ResponseInfo();
	}

	/**
	 * 用户使用内容模型记录
	* @Title: saveModelRecord  
	* @param request 请求
	* @param modelId 模型ID
	* @throws GlobalException 异常
	 */
	@RequestMapping(value = "/model/record", method = RequestMethod.GET)
	public ResponseInfo saveModelRecord(HttpServletRequest request, 
			@NotNull Integer modelId) throws GlobalException {
		//得到当前用户
		CoreUser user = SystemContextUtils.getCoreUser();
		UserModelRecord bean = new UserModelRecord(user.getId(),modelId);
		userModelRecordService.save(bean);
		return new ResponseInfo();
	}
	
	/**
	 * 处理文档(文库文档转换)
	 * @Title: conversionDoc  
	 * @param id		资源id
	 * @param request	request请求
	 * @throws GlobalException    全局异常  
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/conversion/doc",method = RequestMethod.GET)
	public ResponseInfo conversionDoc(@RequestParam Integer id,HttpServletRequest request) 
			throws GlobalException {
		ResourcesSpaceData space = resourcesSpaceDataService.findById(id);
		String docUrl = docServiceImpl.conversionDoc(space, SystemContextUtils.getSite(request));
		return new ResponseInfo(docUrl);
	}
	
	/**
	  * 查询是否存在强制通过按钮
	  * @Title: forceReleaseButton
	  * @param contentId	栏目id
	  * @return: ResponseInfo
	  */
	@RequestMapping(value = "/force/button",method =  RequestMethod.GET)
	public ResponseInfo forceReleaseButton(@RequestParam Integer contentId) {
		Content content = service.findById(contentId);
		if (!Arrays.asList(ContentConstant.STATUS_SMART_AUDIT_SUCCESS, ContentConstant.STATUS_SMART_AUDIT_FAILURE)
				.contains(content.getStatus())) {
			return new ResponseInfo(false);
		}
		// 查询该内容所在的栏目是否存在策略
		AuditChannelSet auditChannelSet = auditChannelSetService.findByChannelId(content.getChannelId(), false);
		if (auditChannelSet != null) {
			// 如果存在策略则直接查看按钮
			return new ResponseInfo(auditChannelSet.getIsCompel());
		}
		return new ResponseInfo(false);
	}
}
