package com.jeecms.content.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.audit.domain.AuditModelItem;
import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.audit.service.AuditChannelSetService;
import com.jeecms.audit.service.AuditModelSetService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.ChastityUtil;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.util.HttpClientUtil;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.constants.ContentReviewConstant;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentChannel;
import com.jeecms.content.domain.ContentCheckDetail;
import com.jeecms.content.domain.dto.ContentReviewProcessDataDto;
import com.jeecms.content.service.*;
import com.jeecms.content.util.CmsModelUtil;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.message.MqConstants;
import com.jeecms.message.MqSendMessageService;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.sso.dto.response.SyncResponseBaseVo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.BeatchDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 内容审核service实现类
 * 
 * @author: tom
 * @date: 2020年1月7日 下午8:47:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentReviewServiceImpl implements ContentReviewService {
	static Logger logger = LoggerFactory.getLogger(ContentReviewServiceImpl.class);
//	TODO
//	1. 发送错误站内信
//	2. 保存审核错误信息(contentCheckError-这个数据表名需要修改)

//	3. 轮训查询数据，将数据库保存到数据库中
// 	4. 从数据库中然后显示到前台
//  5. 逻辑删除时，无论是审核中还是审核失败都是逻辑删除不加入回收站，在删除之前干掉contentCheckDetail

	// TODO 校验
	// 审核中的内容不能继续提交审核
	// 无论是改变状态还是其它
	// 审核错误信息添加换行符

	@Override
	@Async("asyncServiceExecutor")
	public void reviewContents(List<Content> contents, Integer userId) throws GlobalException {
		try {
			// 此是子线程操作的形式，所以要等待主线程完成才可以进行子线程操作
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// 校验参数失败内容集合
		List<Content> checkErrorContents = new ArrayList<Content>();
		// 平台请求错误内容集合
		List<Content> errorContents = new ArrayList<Content>();
		// 缺少余量内容集合
		List<Content> notMoneyContents = new ArrayList<Content>();
		// 图片格式错误内容集合
		List<Content> notImgFormatContents = new ArrayList<Content>();
		boolean noMoney = false;
		for (Content content : contents) {
			ContentReviewProcessDataDto dto = this.reviewContentProcessData(content,
					ContentInitUtils.checkContentToJson(content, content.getContentExt()));
			if (dto == null) {
				checkErrorContents.add(content);
				continue;
			}
			// 判断如果获取的该
			if (StringUtils.isNotBlank(dto.getError())) {
				notImgFormatContents.add(content);
				continue;
			}
			List<CmsModelItem> items = dto.getItems();
			List<CmsModelItem> auditItems = dto.getAuditItems();
			JSONArray array = ContentInitUtils.initReviewContent(dto.getDtoJson(), items, auditItems,
					CmsModelUtil.getContentCheckFieldAndDataType(dto.getAuditStrategy()));
			content.setAuditStrategy(dto.getAuditStrategy());
			if (!CollectionUtils.isEmpty(array)) {
				JSONArray array2 = new JSONArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject json = array.getJSONObject(i);
					if (StringUtils.isBlank(dto.getAuditStrategy().getTextScene())) {
						json.put("txt", new ArrayList<String>());
					}
					if (StringUtils.isBlank(dto.getAuditStrategy().getPictureScene())) {
						json.put("imgValue",new ArrayList<String>());
						json.put("img",new ArrayList<String>());
					}
					
					array2.add(json);
				}
				array = array2;
			}
			if (CollectionUtils.isEmpty(array)) {
				checkErrorContents.add(content);
				continue;
			}
			
			// 如果上一次for循环的请求已经存在缺少余量的情况，那么这次，包括以后的循环只需要将参数全部丢入到缺乏余量的内容集合中即可
			if (noMoney) {
				notMoneyContents.add(content);
				continue;
			}
			// 发送审核请求
			Integer status = this.sendPlatform(array, content.getCheckMark());
			switch (status) {
			case ContentReviewServiceImpl.REQUEST_ERROR:
				errorContents.add(content);
				break;
			case ContentReviewServiceImpl.REQUEST_SUCCESS:
				// 这一次发送成功，则说明平台请求成功，这时候直接发送请求告诉平台这次连接没有其它情况，可以审核了
				this.sendPlatform(content.getCheckMark());
				break;
			case ContentReviewServiceImpl.LACK_MONEY:
				noMoney = true;
				notMoneyContents.add(content);
				break;
			default:
				break;
			}
		}
		this.reviewContents(contents, errorContents, notMoneyContents, notImgFormatContents, checkErrorContents,
				userId);
	}

	/**
	 * 处理具体的此处请求的SQL请求，即：添加内容审核详情对象、修改内容的状态
	 * 
	 * @Title: reviewContents
	 * @param contents             总内容集合
	 * @param errorContents        平台请求错误内容集合
	 * @param notMoneyContents     缺少余量内容集合
	 * @param notImgFormatContents 图片格式不正确内容集合
	 * @param checkErrorContents   审核参数校验失败内容集合
	 * @param userId               审核用户ID
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	private void reviewContents(List<Content> contents, List<Content> errorContents, List<Content> notMoneyContents,
			List<Content> notImgFormatContents, List<Content> checkErrorContents, Integer userId)
			throws GlobalException {
		List<ContentChannel> updateContentChannels = new ArrayList<ContentChannel>();
		if (!CollectionUtils.isEmpty(errorContents)) {
			updateContentChannels = this.updateErrorContent(errorContents, updateContentChannels, userId,
					contents.size(), 1);
			contents.removeAll(errorContents);
		}
		if (!CollectionUtils.isEmpty(notMoneyContents)) {
			updateContentChannels = this.updateErrorContent(notMoneyContents, updateContentChannels, userId,
					contents.size(), 2);
			contents.removeAll(notMoneyContents);
		}
		if (!CollectionUtils.isEmpty(notImgFormatContents)) {
			updateContentChannels = this.updateErrorContent(notImgFormatContents, updateContentChannels, userId,
					contents.size(), 3);
			contents.removeAll(notImgFormatContents);
		}
		if (!CollectionUtils.isEmpty(checkErrorContents)) {
			updateContentChannels = this.updateErrorContent(checkErrorContents, updateContentChannels, userId,
					contents.size(), 4);
			contents.removeAll(checkErrorContents);
		}
		if (updateContentChannels.size() > 0) {
			contentChannelService.batchUpdateAll(updateContentChannels);
			contentChannelService.flush();
		}
		if (contents.size() > 0) {
			this.saveContentCheckDetail(contents, 0, null, userId);
		}
	}

	/**
	 * 新增 内容审核详情对象
	 * 
	 * @Title: saveContentCheckDetail
	 * @param contents     内容集合
	 * @param status       0-审核中，1-审核失败、2-审核失败、3-内容检验失败、4-内容校验失败
	 * @param errorContent 内容的错误详情
	 * @param userId       审核用户ID
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	private void saveContentCheckDetail(List<Content> contents, Integer status, String errorContent, Integer userId)
			throws GlobalException {
		// 查询出内容集合中存在的 内容审核详情对象，然后将该对象集合物理删除，然后重新生成一个内容审核详情对象
		List<Integer> contentIds = contents.stream().map(Content::getId).collect(Collectors.toList());
		List<ContentCheckDetail> details = contentCheckDetailService.findByContentIds(contentIds);
		if (!CollectionUtils.isEmpty(details)) {
			contentCheckDetailService.physicalDeleteInBatch(details);
		}
		details = new ArrayList<ContentCheckDetail>(contents.size());
		for (Content content : contents) {
			ContentCheckDetail detail = null;
			if (status == 3 || status == 4) {
				detail = new ContentCheckDetail(content.getId(),
						ContentReviewConstant.STATUS_CHANGE_STATUS.get(status), errorContent, content.getCheckMark(),
						null, null, userId);
			} else {
				detail = new ContentCheckDetail(content.getId(),
						ContentReviewConstant.STATUS_CHANGE_STATUS.get(status), errorContent, content.getCheckMark(),
						content.getAuditStrategy().getTextScene(), content.getAuditStrategy().getPictureScene(), userId);
			}
			details.add(detail);
		}
		contentCheckDetailService.saveAll(details);
		contentCheckDetailService.flush();
	}

	/**
	 * 错误提示
	 */
	private static final String ERROR_PROMPT = "本次共num篇内容提交智能审核，其中errorNum篇因errorContent原因审核失败，请前往智能审核列表重新提交";

	/**
	 * 发送错误信息(发送站内容+保存错误的内容审核对象)
	 * 
	 * @Title: senMemberMsg
	 * @param contents     内容对象集合
	 * @param userIds      请求审核用户id(虽然此值是id集合但是本身而言此集合中只存在一个审核用户id)
	 * @param num          此处请求的总内容集合大小
	 * @param errorNum     请求错误的内容集合大小
	 * @param errorContent 该内容集合审核失败详情
	 * @param status       审核失败状态
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	private void senMemberMsg(List<Content> contents, List<Integer> userIds, Integer num, Integer errorNum,
			String errorContent, Integer status) throws GlobalException {
		String errorPromptContent = ERROR_PROMPT;
		// 底下num等不使用常量是因为下方的值与ERROR_PROMPT字符串中的参数有关，而不是固定的，如果需要修改则直接在下方修改即可
		errorPromptContent = errorPromptContent.replaceAll("num", num + "").replaceAll("errorNum", errorNum + "")
				.replaceAll("errorContent", errorContent);
		mqSendMessageService.sendMemberMsg(MqConstants.ASSIGN_MANAGER, null, null, null, userIds, null,
				MessageSceneEnum.USER_MESSAGE, "内容审核失败", errorPromptContent, null, null, null,
				MqConstants.SEND_SYSTEM_STATION, null);
		this.saveContentCheckDetail(contents, status, errorContent, userIds.get(0));
	}

	/**
	 * 修改请求错误的内容
	 * 
	 * @Title: updateErrorContent
	 * @param contents              请求错误的内容对象集合
	 * @param updateContentChannels 错误的contentChannel对象集合
	 * @param status                1-云平台请求错误、2-缺少余量、3-图片格式不正确、4-审核错误，或者缺少审核字段(此处出现可能极小，出现的可能就是审核通过，然后在审核通过、正式审核的这个时间内模型字段有被修，这种极端情况当做审核错误处理)
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	private List<ContentChannel> updateErrorContent(List<Content> contents, List<ContentChannel> updateContentChannels,
			Integer userId, Integer num, Integer status) throws GlobalException {
		// 该内容是请求错误的内容对象集合，如果该不为空：说明该内容集合状态需要回滚成审核失败
		if (!CollectionUtils.isEmpty(contents)) {
			for (Content content : contents) {
				ContentChannel contentChannel = content.getOriContentChannel();
				contentChannel.setStatus(ContentConstant.STATUS_SMART_AUDIT_FAILURE);
				updateContentChannels.add(contentChannel);
				content.setStatus(ContentConstant.STATUS_SMART_AUDIT_FAILURE);
			}
			contentService.batchUpdateAll(contents);
			contentService.flush();
		}
		this.senMemberMsg(contents, Arrays.asList(userId), num, contents.size(),
				ContentReviewConstant.STATUS_ERROR_DATA.get(status), status);
		return updateContentChannels;
	}

	/**
	 * 将内容处理成给平台审核的对象(主要处理内容，及将资源id转换成内容对象)
	 * 
	 * @Title: reviewContentProcessData
	 * @param content 内容对象
	 * @param dtoJson 内容转换成的JSON集合(推送给平平台审核的对象)
	 * @throws GlobalException 全局异常
	 * @return: ContentReviewProcessDataDto
	 */
	private ContentReviewProcessDataDto reviewContentProcessData(Content content, JSONObject dtoJson)
			throws GlobalException {
		AuditChannelSet auditChannelSet = auditChannelSetService.findByChannelId(content.getChannelId(),true);
		AuditStrategy auditStrategy = auditChannelSet.getStrategy();
		System.out.println(auditStrategy.getId());
		List<AuditModelItem> auditModelItems = auditModelSetService.findByModelId(content.getModelId());
		if (CollectionUtils.isEmpty(auditModelItems)) {
			return null;
		}
		List<CmsModelItem> auditItems = new ArrayList<CmsModelItem>();
		for (AuditModelItem auditModelItem : auditModelItems) {
			if (auditModelItem.getModelItem() != null) {
				auditItems.add(auditModelItem.getModelItem());
			}
		}
//		List<Integer> auditModelItemsIds = auditModelItems.stream().map(AuditModelItem::getModelItemId)
//				.collect(Collectors.toList());
		
//		List<CmsModelItem> auditItems = cmsModelItemService.findAllById(auditModelItemsIds);
		List<CmsModelItem> items = cmsModelItemService.findByModelId(content.getModelId());
		if (!this.reviewContentCheck(auditChannelSet, auditModelItems, items)) {
			return null;
		}
		// 特殊处理的模型字段类型集合：正文、单图、多图、默认字段单图
		List<String> specialTypes = Arrays.asList(CmsModelConstant.CONTENT_TXT, CmsModelConstant.SINGLE_CHART_UPLOAD,
				CmsModelConstant.MANY_CHART_UPLOAD, CmsModelConstant.TYPE_SYS_CONTENT_RESOURCE);
		Map<String, String> typeMap = items.stream().filter(item -> specialTypes.contains(item.getDataType()))
				.collect(Collectors.toMap(CmsModelItem::getField, CmsModelItem::getDataType));
		CmsSite site = content.getSite();
		String webUrl = site.getUrlWhole();
		ContentReviewProcessDataDto dto = null;
		dto = this.processDtoJsonData(dtoJson, typeMap, auditStrategy, webUrl, dto, auditItems, items);
		return dto;
	}

	/**
	 * 具体处理资源对象操作
	 * 
	 * @Title: processDtoJsonData
	 * @param dtoJson       内容转换的json
	 * @param typeMap       模型字段Field->模型字段类型 的map
	 * @param auditStrategy 审核策略
	 * @param webUrl        站点url
	 * @param dto           内容审核转换资源dto
	 * @param auditItems    审核策略配置的 模型字段
	 * @param items         内容模型字段
	 * @return: ContentReviewProcessDataDto
	 */
	private ContentReviewProcessDataDto processDtoJsonData(JSONObject dtoJson, Map<String, String> typeMap,
			AuditStrategy auditStrategy, String webUrl, ContentReviewProcessDataDto dto, List<CmsModelItem> auditItems,
			List<CmsModelItem> items) {
		
		for (String key : dtoJson.keySet()) {
			String dataType = typeMap.get(key);
			if (dataType != null) {
				switch (dataType) {
				case CmsModelConstant.CONTENT_TXT:
					String txtValue = dtoJson.getString(key);
					JSONObject contentJson = new JSONObject();
					// 此处仅"内容"类型字段需要如此处理，因为会有这种情况就是开启文本不开启图片
					if (auditStrategy.getIsText()) {
						contentJson.put(ContentReviewConstant.CONTENT_TXT, ContentInitUtils.removeHtmlTag(txtValue));
					}
					if (auditStrategy.getIsPicture()) {
						contentJson.put(ContentReviewConstant.CONTENT_IMG,
								ContentInitUtils.obtainImgUrl(txtValue, webUrl));
					}
					dtoJson.put(key, contentJson);
					break;
				case CmsModelConstant.SINGLE_CHART_UPLOAD:
					Integer dataId = dtoJson.getInteger(key);
					ResourcesSpaceData data = resourcesSpaceDataService.findById(dataId);
					if (data != null) {
						if (!this.checkImg(data)) {
							dto = new ContentReviewProcessDataDto("error");
							return dto;
						}
						String dataUrl = data.getUrl();
						if (StringUtils.isNotBlank(dataUrl)) {
							dataUrl = ContentInitUtils.processImgUrlFullPath(dataUrl, webUrl);
							dtoJson.put(key, dataUrl);
						}
					}
					break;
				case CmsModelConstant.TYPE_SYS_CONTENT_RESOURCE:
					Integer dataId2 = dtoJson.getInteger(key);
					ResourcesSpaceData data2 = resourcesSpaceDataService.findById(dataId2);
					if (data2 != null) {
						if (!this.checkImg(data2)) {
							dto = new ContentReviewProcessDataDto("error");
							return dto;
						}
						String dataUrl = data2.getUrl();
						if (StringUtils.isNotBlank(dataUrl)) {
							dataUrl = ContentInitUtils.processImgUrlFullPath(dataUrl, webUrl);
							dtoJson.put(key, dataUrl);
						}
					}
					break;
				case CmsModelConstant.MANY_CHART_UPLOAD:
					JSONArray imgs = dtoJson.getJSONArray(key);
					JSONArray newImags = new JSONArray();
					for (int i = 0; i < imgs.size(); i++) {
						JSONObject json = imgs.getJSONObject(i);
						Integer imgId = json.getInteger(ContentReviewConstant.MANY_IMAGE_RESOURCE_ID);
						ResourcesSpaceData img = resourcesSpaceDataService.findById(imgId);
						if (img != null) {
							if (!this.checkImg(img)) {
								dto = new ContentReviewProcessDataDto("error");
								return dto;
							}
							String imgUrl = img.getUrl();
							if (StringUtils.isNotBlank(imgUrl)) {
								imgUrl = ContentInitUtils.processImgUrlFullPath(imgUrl, webUrl);
								json.put(ContentReviewConstant.MANY_IMAGE_URL, imgUrl);
								newImags.add(json);
							}
						}
					}
					dtoJson.put(key, newImags);
					break;
				default:
					break;
				}
			}
		}
		dto = new ContentReviewProcessDataDto(dtoJson, items, auditItems, auditStrategy);
		return dto;
	}

	/**
	 * 校验送审内容中图片是否合格
	 * 
	 * @Title: checkImg
	 * @param data 资源对象
	 * @return: boolean
	 */
	private boolean checkImg(ResourcesSpaceData data) {
		// 校验后缀
		if (StringUtils.isNotBlank(data.getSuffix())) {
			if (!ContentReviewConstant.CheckImg.IMG_FORMAT.contains(data.getSuffix())) {
				return false;
			}
		}
		// 校验图片边长，最长边<4096像素，最短边>128像素
		if (StringUtils.isNotBlank(data.getDimensions())) {
			Integer weight = Integer.valueOf(data.getWidth());
			if (weight < ContentReviewConstant.CheckImg.MIN_LONG || weight > ContentReviewConstant.CheckImg.MAX_LONG) {
				return false;
			}
			Integer height = Integer.valueOf(data.getHeight());
			if (height < ContentReviewConstant.CheckImg.MIN_LONG || height > ContentReviewConstant.CheckImg.MAX_LONG) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean reviewContentCheck(Content content, Integer channelId, Integer modelId) throws GlobalException {
		// 如果传入了内容对象，并且通过该内容对象中查询出了一个在审核中的内容审核详情，这时候就不能继续提交审核了
		if (content != null) {
			if (StringUtils.isNotBlank(content.getCheckMark())) {
				ContentCheckDetail detail = contentCheckDetailService.findByCheckMark(content.getCheckMark(),null);
				if (detail != null) {
					if (detail.getStatus() == 1) {
						return false;
					}
				}
			}
		}
		
		AuditChannelSet auditChannelSet = auditChannelSetService.findByChannelId(channelId,true);
		List<AuditModelItem> auditModelItems = auditModelSetService.findByModelId(modelId);
		List<CmsModelItem> items = cmsModelItemService.findByModelId(modelId);
		return this.reviewContentCheck(auditChannelSet, auditModelItems, items);
	}

	/**
	 * 校验该内容是否可以进行审核
	 * 
	 * @Title: reviewContentCheck
	 * @param auditChannelSet 审核栏目设置对象
	 * @param auditModelItems 该内容模型设置的审核字段集合
	 * @param items           该内容模型字段集合
	 * @return: boolean
	 */
	private boolean reviewContentCheck(AuditChannelSet auditChannelSet, List<AuditModelItem> auditModelItems,
			List<CmsModelItem> items) {
		// 如果该栏目ID没有绑定审核策略直接退出
		if (auditChannelSet == null) {
			return false;
		}
		// 判断该策略是否开启
		if (!auditChannelSet.getStatus()) {
			return false;
		}
		AuditStrategy auditStrategy = auditChannelSet.getStrategy();
		// 如果审核策略不开启文本和图片审核(那次审核策略无用直接退出)
		if (!auditStrategy.getIsText() && !auditStrategy.getIsPicture()) {
			return false;
		}
		// 获得配置的审核模型字段(正常逻辑此处不可能出现)
		if (CollectionUtils.isEmpty(auditModelItems)) {
			return false;
		}
		// 判断该内容模型是否存在字段(正常逻辑此处不可能出现)(出现的可能-用户用该模型新增了内容之后，将模型干掉)
		if (CollectionUtils.isEmpty(items)) {
			return false;
		}
		List<String> auditItemFilds = auditModelItems.stream().map(AuditModelItem::getModelItemField)
				.collect(Collectors.toList());
		// 将查询出的模型的字段集合，去删除审核策略配置的字段集合，如果字段集合相等(策略配置的字段不符合当前模型的任何一个字段)，这样判断未设置字段，未设置字段无法审核
		int realSize = items.size();
		List<String> itemFilds = items.stream().map(CmsModelItem::getField).collect(Collectors.toList());
		itemFilds.removeAll(auditItemFilds);
		int processSize = itemFilds.size();
		if (processSize == realSize) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkAppIdOrPhone() throws GlobalException {
		String appId = this.getUserParameter(true);
		if (StringUtils.isBlank(appId)) {
			return false;
		}
		String phone = this.getUserParameter(false);
		if (StringUtils.isBlank(phone)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取appId或者手机号
	 * 
	 * @Title: getUserParameter
	 * @param isAppId true->appId、phone->手机号
	 * @throws GlobalException 全局异常
	 * @return: String
	 */
	private String getUserParameter(boolean isAppId) throws GlobalException {
		String appId = null;
		// 判断缓存中是否存在appId
		if (cacheProvider.exist(ContentReviewConstant.CONTENT_REVIEW_CACHE, ContentReviewConstant.REVIEW_APPID)) {
			appId = cacheProvider
					.getCache(ContentReviewConstant.CONTENT_REVIEW_CACHE, ContentReviewConstant.REVIEW_APPID)
					.toString();
		}
		// 如果appId不存在，则从util中取appId，并将其放入缓存中
		if (StringUtils.isBlank(appId)) {
			appId = chastityUtil.getId();
			if (StringUtils.isNotBlank(appId)) {
				cacheProvider.setCache(ContentReviewConstant.CONTENT_REVIEW_CACHE, ContentReviewConstant.REVIEW_APPID,
						appId);
			} else {
				throw new GlobalException(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR);
			}
		}
		if (isAppId) {
			return appId;
		}
		String phone = null;
		// 判断缓存中是否存在phone
		if (cacheProvider.exist(ContentReviewConstant.CONTENT_REVIEW_CACHE, ContentReviewConstant.REVIEW_PHONE)) {
			phone = cacheProvider
					.getCache(ContentReviewConstant.CONTENT_REVIEW_CACHE, ContentReviewConstant.REVIEW_PHONE)
					.toString();
		}
		// 如果phone不存在则从云平台中取phone中，并将其放入到缓存中
		if (StringUtils.isBlank(phone)) {
			Map<String, String> params = new HashMap<String, String>(1);
			params.put(ContentReviewConstant.GET_PHONE_REQUEST_KEY, appId);
			ResponseInfo response = HttpUtil.getJsonBean(IF_AUTH_SERVER_URL, params, ResponseInfo.class);
			if (response != null && SyncResponseBaseVo.SUCCESS_CODE.equals(response.getCode())) {
				JSONObject json = JSONObject.parseObject(String.valueOf(response.getData()));
				if (json != null) {
					phone = json.getString(ContentReviewConstant.GET_PHONE_OBTAIN_KEY);
					if (StringUtils.isNotBlank(phone)) {
						cacheProvider.setCache(ContentReviewConstant.CONTENT_REVIEW_CACHE,
								ContentReviewConstant.REVIEW_PHONE, phone);
					}
					return phone;
				}
				return null;
			} else {
				throw new GlobalException(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR);
			}
		}
		return phone;
	}

	/** 云平台请求错误 */
	public static final int REQUEST_ERROR = 1;
	/** 云平台请求成功 */
	public static final int REQUEST_SUCCESS = 2;
	/** 缺乏余量 */
	public static final int LACK_MONEY = 3;

	/**
	 * 发送内容审核请求
	 * 
	 * @Title: sendPlatform
	 * @param array       审核内容JSONArray对象
	 * @param contentMark 内容审核标识
	 * @throws GlobalException 全局异常
	 * @return: Integer 1-平台请求错误 2-云平台成功 3-缺少余量
	 */
	private Integer sendPlatform(JSONArray array, String contentMark) throws GlobalException {

//		System.out.println(array.toJSONString());

		
		JSONObject json = new JSONObject();
		json.put(ContentReviewConstant.SendPlatformRequest.CONTENT_MARK, contentMark);
		json.put(ContentReviewConstant.SendPlatformRequest.PHONE, this.getUserParameter(false));
		json.put(ContentReviewConstant.SendPlatformRequest.CONTENT_VALUE, array);
		String value = null;
		try {
			value = HttpClientUtil.timeLimitPostJson(CHECK_CONTENT_URL, json, this.getHeader(), 80000);
		} catch (ParseException | IOException e) {
			// 连接超时
			return 1;
		}
		if (StringUtils.isNotBlank(value)) {
			JSONObject returnJson = JSONObject.parseObject(value);
			if (returnJson != null) {
				if (ContentReviewConstant.SendPlatformResponse.CODE_VALUE_SUCCESS
						.equals(returnJson.get(ContentReviewConstant.SendPlatformResponse.CODE))) {
					return 2;
				}
				// 缺少余量
				if (ContentReviewConstant.SendPlatformResponse.CODE_VALUE_ERROR
						.equals(returnJson.get(ContentReviewConstant.SendPlatformResponse.CODE))) {
					return 3;
				}
				return 1;
			}
			return 1;
		} else {
			return 1;
		}
	}

	/**
	 * 发送内容审核请求，确定该内容开始审核
	 * 
	 * @Title: sendPlatform
	 * @param contentMark 内容审核标识
	 * @throws GlobalException
	 * @return: void
	 */
	private void sendPlatform(String contentMark) throws GlobalException {
		JSONObject json = new JSONObject();
		json.put(ContentReviewConstant.SendPlatformRequest.CONTENT_MARKS, Arrays.asList(contentMark));
		try {
			HttpClientUtil.timeLimitPostJson(DETERMINE_CHECK_URL, json, this.getHeader(), 10000);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * 获得请求header头
	 * 
	 * @Title: getHeader
	 * @throws GlobalException
	 * @return: Map<String,String>
	 */
	private Map<String, String> getHeader() throws GlobalException {
		HEADER_MAP.put(ContentReviewConstant.SEND_REQUEST_HEADER, this.getUserParameter(true));
		return HEADER_MAP;
	}

	/** 请求header头map */
	private static Map<String, String> HEADER_MAP = new HashMap<String, String>();
	/** 请求云平台是否授权 **/
	private final String IF_AUTH_SERVER_URL = ContentReviewConstant.DoMain.PLATFORM_URL
			.concat("/MODULE-APP/client/v1/userClient");
	/** 提交内容审核 */
	private final String CHECK_CONTENT_URL = ContentReviewConstant.DoMain.PLATFORM_URL
			.concat("/MODULE-AUDIT-CONTENT/client/v1/audit/content/temporary");
	/** 通知确定开始审核 */
	private final String DETERMINE_CHECK_URL = ContentReviewConstant.DoMain.PLATFORM_URL
			.concat("/MODULE-AUDIT-CONTENT/client/v1/audit/content/confirm");
	/** 获取审核结果 */
	private final String GET_CHECK_DETAIL = ContentReviewConstant.DoMain.PLATFORM_URL
			.concat("/MODULE-AUDIT-CONTENT/client/v1/audit/content/list");

	@PostConstruct
	private void checkProcess() {
		startEncodeTask();
	}

	private void startEncodeTask() {
		timer = Executors.newScheduledThreadPool(3);
		timer.scheduleAtFixedRate(() -> {
			try {
				startCheck();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 60, 300, TimeUnit.SECONDS);
	}

	private ScheduledExecutorService timer;

	/**
	 * 查询内容审核结果
	 *
	 * @Title: startCheck
	 * @return: void
	 * @throws Exception
	 */
	private void startCheck() {
		List<ContentCheckDetail> details = contentCheckDetailService.findUnderReviews();
		if (CollectionUtils.isEmpty(details)) {
			return;
		}
		JSONObject json = new JSONObject();
		List<String> contentMarks = details.stream().map(ContentCheckDetail::getCheckMark).collect(Collectors.toList());
		json.put(ContentReviewConstant.SendPlatformRequest.CONTENT_MARKS, contentMarks);
		String value = null;
		try {
			value = HttpClientUtil.postJson(GET_CHECK_DETAIL, json, this.getHeader());
		} catch (Exception e) {
			// 抛出异常
		}
		if (StringUtils.isNotBlank(value)) {
			JSONObject returnJson = JSONObject.parseObject(value);
			String code = returnJson.getString(ContentReviewConstant.GetRecodeResponse.CODE);
			if (StringUtils.isBlank(code)) {
				return;
			}
			if (ContentReviewConstant.GetRecodeResponse.CODE_VALUE_SUCCESS.equals(code)) {
				JSONObject dataJson = returnJson.getJSONObject(ContentReviewConstant.GetRecodeResponse.DATA);
				@SuppressWarnings("unchecked")
				List<String> unprocesseds = (List<String>) dataJson
						.get(ContentReviewConstant.GetRecodeResponse.PROCESS_CONTENT_MARKS);
				Map<String, ContentCheckDetail> cMap = details.stream()
						.collect(Collectors.toMap(ContentCheckDetail::getCheckMark, c -> c));
				if (!CollectionUtils.isEmpty(unprocesseds)) {
					for (ContentCheckDetail detail : details) {
						if (unprocesseds.contains(detail.getCheckMark())) {
							cMap.remove(detail.getCheckMark());
						}
					}
				}
				try {
					JSONArray array = dataJson.getJSONArray(ContentReviewConstant.GetRecodeResponse.RESULTS);
					if (array.size() != cMap.size()) {
						List<String> checkBan = new ArrayList<String>();
						for (int i = 0; i < array.size(); i++) {
							JSONObject banJson = array.getJSONObject(i);
							checkBan.add(banJson.getString(ContentReviewConstant.GetRecodeResponse.CONTENT_MARK));
						}

						List<Content> releaseContents = new ArrayList<Content>();
						List<ContentCheckDetail> removeDetail = new ArrayList<ContentCheckDetail>();
						for (String contentMark : cMap.keySet()) {
							if (!checkBan.contains(contentMark)) {
								removeDetail.add(cMap.get(contentMark));
								releaseContents.add(cMap.get(contentMark).getContent());
							}
						}

						for (Content content : releaseContents) {
							BeatchDto dto = new BeatchDto();
							dto.setCheckPerm(false);
							dto.setIds(Arrays.asList(content.getId()));
							dto.setSiteId(content.getSiteId());
							dto.setStatus(ContentConstant.STATUS_PUBLISH);
							contentService.changeStatus(dto, null);
							contentService.flush();
						}
						if (!CollectionUtils.isEmpty(removeDetail)) {
							contentCheckDetailService.physicalDeleteInBatch(removeDetail);
						}
					}
					this.updateDetail(array, cMap);
				} catch (GlobalException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 内容审核操作逻辑
	 * 
	 * @Title: updateDetail
	 * @param array 查询审核返回的数据
	 * @param cMap  内容审核标识、内容审核详情对象的Map
	 * @throws GlobalException
	 * @return: void
	 */
	private void updateDetail(JSONArray array, Map<String, ContentCheckDetail> cMap) throws GlobalException {
		if (!CollectionUtils.isEmpty(array)) {
			
			List<ContentCheckDetail> removeContentCheckDetails = new ArrayList<ContentCheckDetail>();
			
			List<ContentCheckDetail> contentCheckDetails = new ArrayList<ContentCheckDetail>();
//			List<Content> contents = new ArrayList<Content>();
            List<Content> banContents = new ArrayList<Content>();
            List<Content> errorContents = new ArrayList<Content>();
			List<ContentChannel> contentChannels = new ArrayList<ContentChannel>();
			List<Content> releaseContents = new ArrayList<Content>();
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				String contentMark = json.getString(ContentReviewConstant.GetRecodeResponse.CONTENT_MARK);
				JSONArray listArrays = json.getJSONArray(ContentReviewConstant.GetRecodeResponse.LIST);
				ContentCheckDetail checkDetail = cMap.get(contentMark);
				Content content = checkDetail.getContent();
				List<Integer> textScenes = checkDetail.getTextAuditScenes();
				List<Integer> pictureScenes = checkDetail.getPictureAuditScenes();
				List<CmsModelItem> items = cmsModelItemService.findByModelId(checkDetail.getContent().getModelId());
				if (CollectionUtils.isEmpty(items)) {
					continue;
				}
				Map<String, String> fildLabelMap = items.stream()
						.collect(Collectors.toMap(CmsModelItem::getField, CmsModelItem::getItemLabel));
				JSONObject detailJson = new JSONObject();
				JSONObject detailErrorJson = new JSONObject();
				for (int j = 0; j < listArrays.size(); j++) {
					JSONObject listJson = listArrays.getJSONObject(j);
					String name = listJson.getString(ContentReviewConstant.GetRecodeResponse.FAIL_NAME);

					String failText = listJson.getString(ContentReviewConstant.GetRecodeResponse.FAIL_TEXT);
					if (!fildLabelMap.keySet().contains(name)) {
						continue;
					}
					JSONArray imgs = listJson.getJSONArray(ContentReviewConstant.GetRecodeResponse.IMGS);
					JSONObject errorJson = detailErrorJson.getJSONObject(name);
					if (errorJson == null) {
						errorJson = new JSONObject();
					}
					JSONObject filedJson = detailJson.getJSONObject(name);
					if (filedJson == null) {
						filedJson = new JSONObject();
					}
					boolean errorMark = false;
					if (!CollectionUtils.isEmpty(imgs)) {
						JSONObject errorImgJson = errorJson.getJSONObject("img");
						if (errorImgJson == null) {
							errorImgJson = new JSONObject();
						}
						@SuppressWarnings("unchecked")
						List<String> imgList = (List<String>) filedJson.get("img");
						if (CollectionUtils.isEmpty(imgList)) {
							imgList = new ArrayList<String>();
						}
						for (int k = 0; k < imgs.size(); k++) {
							JSONObject imgJson = imgs.getJSONObject(k);
							Integer auditType = imgJson.getInteger("auditType");
							Integer conclusionType = imgJson.getInteger("conclusionType");
//							failText = imgJson.getString("failText");
							if (conclusionType == 2 || conclusionType == 3) {
								if (pictureScenes.contains(auditType)) {
									imgList.add(failText);
								}
							}
							if (conclusionType == 4) {
								String failOrigin = imgJson.getString("failOrigin");
								if (StringUtils.isNotBlank(failOrigin)) {

									detailJson.remove(name);

									Integer num = errorImgJson.getInteger(failOrigin);
									if (errorImgJson.get(failOrigin) != null) {
										num++;
										errorImgJson.put(failOrigin, num);
									} else {
										errorImgJson.put(failOrigin, 1);
									}
								}
								errorMark = true;
							}
						}
						if (errorMark) {
							errorJson.put("img", errorImgJson);
						}
						if (!errorMark) {
							if (!CollectionUtils.isEmpty(imgList)) {
								filedJson.put("img", imgList);
							}
						}
					}
                    boolean isAllTxt = false;
					JSONArray txts = listJson.getJSONArray("txt");
					if (!CollectionUtils.isEmpty(txts)) {
						JSONObject errorTxtJson = errorJson.getJSONObject("txt");
						if (errorTxtJson == null) {
							errorTxtJson = new JSONObject();
						}
						@SuppressWarnings("unchecked")
						List<String> txtList = (List<String>) filedJson.get("txt");
						if (CollectionUtils.isEmpty(txtList)) {
							txtList = new ArrayList<String>();
						}
						for (int k = 0; k < txts.size(); k++) {
							JSONObject txtJson = txts.getJSONObject(k);
							Integer conclusionType = txtJson.getInteger("conclusionType");
							Integer auditType = txtJson.getInteger("auditType");
							if (conclusionType == 2 || conclusionType == 3) {
								if (textScenes.contains(auditType)) {
									String wordStr = txtJson.getString("words");
									if (StringUtils.isNoneBlank(wordStr)) {
										List<String> words = JSONArray.parseArray(wordStr, String.class);
										if (CollectionUtils.isEmpty(words)) {
											txtList.add(failText);
										} else {
											txtList.addAll(words);
										}
									} else {
                                        isAllTxt = true;
										txtList.add(failText);
									}
								}
							}
							if (conclusionType == 4) {
								String failOrigin = txtJson.getString("failOrigin");
								if (StringUtils.isNotBlank(failOrigin)) {

									detailJson.remove(name);

									Integer num = errorTxtJson.getInteger(failOrigin);
									if (errorTxtJson.get(failOrigin) != null) {
										num++;
										errorTxtJson.put(failOrigin, num);
									} else {
										errorTxtJson.put(failOrigin, 1);
									}
								}
								errorMark = true;
							}
						}
						if (errorMark) {
							errorJson.put("txt", errorTxtJson);
						}
						if (!errorMark) {
							if (!CollectionUtils.isEmpty(txtList)) {
								filedJson.put("txt", txtList);
								if (isAllTxt) {
								    filedJson.put("isAllTxt",true);
                                } else {
								    filedJson.put("isAllTxt",false);
                                }
							}
						}
					}
					if (errorMark) {
						detailErrorJson.put(name, errorJson);
					} else {
						detailJson.put(name, filedJson);
					}
				}
				boolean isRemove = true;
				if (!CollectionUtils.isEmpty(detailErrorJson.keySet())) {
					checkDetail.setStatus(3);
					StringBuffer buffer = new StringBuffer();
					int num = 1;
					for (String key : detailErrorJson.keySet()) {
						JSONObject detailFiledJson = detailErrorJson.getJSONObject(key);
						JSONObject imgErrorJson = detailFiledJson.getJSONObject("img");
						StringBuffer fildBuffer = new StringBuffer();
						fildBuffer.append(num + "、" + fildLabelMap.get(key) + "字段错误，错误原因：");
						StringBuffer imgBuffer = null;
						if (imgErrorJson != null) {
							imgBuffer = new StringBuffer();
							for (String string : imgErrorJson.keySet()) {
								imgBuffer.append("有" + imgErrorJson.get(string) + "张图片错误，错误原因：").append(string + "，");
							}
						}
						JSONObject txtErrorJson = detailFiledJson.getJSONObject("txt");
						StringBuffer txtBuffer = null;
						if (txtErrorJson != null) {
							txtBuffer = new StringBuffer();
							for (String string : txtErrorJson.keySet()) {
								txtBuffer.append("有" + txtErrorJson.get(string) + "段文本错误，错误原因：").append(string + "，");
							}
						}
						if (imgBuffer != null) {
							fildBuffer.append(imgBuffer);
						}
						if (txtBuffer != null) {
							fildBuffer.append(txtBuffer);
						}
						String a = fildBuffer.toString();
						buffer.append(a.subSequence(0, a.length() - 1)).append("；");
						num++;
					}
					String checkErrorContent = "内容：" + checkDetail.getContent().getTitle() + " 审核失败，错误原因："
							+ buffer.toString();
					checkDetail.setCheckErrorContent(checkErrorContent);

					mqSendMessageService.sendMemberMsg(MqConstants.ASSIGN_MANAGER, null, null, null,
							Arrays.asList(checkDetail.getCheckUserId()), null, MessageSceneEnum.USER_MESSAGE,
							"内容：" + checkDetail.getContent().getTitle() + "审核失败", checkErrorContent, null, null, null,
							MqConstants.SEND_SYSTEM_STATION, null);

					if (detailJson != null) {
						checkDetail.setCheckBanContent(JSONObject.toJSONString(detailJson));
					}

					contentCheckDetails.add(checkDetail);
					
//					content.setStatus(ContentConstant.STATUS_SMART_AUDIT_FAILURE);
//					ContentChannel contentChannel = content.getOriContentChannel();
//					contentChannel.setStatus(ContentConstant.STATUS_SMART_AUDIT_FAILURE);
					errorContents.add(content);
//					contentChannels.add(contentChannel);
				} else {
					
					if (CollectionUtils.isEmpty(detailJson.keySet())) {
						isRemove = false;
//						releaseContents.add(content);
					} else {
						boolean isPublish = false;
						for (String filed : detailJson.keySet()) {
							JSONObject filedJson = detailJson.getJSONObject(filed);
							if (!CollectionUtils.isEmpty(filedJson.keySet())) {
								isPublish = true;
								if (isPublish) {
									break;
								}
							}
						}
						if (!isPublish) {
//							releaseContents.add(content);
							isRemove = false;
						} else {
							checkDetail.setStatus(2);
							checkDetail.setCheckBanContent(JSONObject.toJSONString(detailJson));
							contentCheckDetails.add(checkDetail);
//							content.setStatus(ContentConstant.STATUS_SMART_AUDIT_SUCCESS);
//							ContentChannel contentChannel = content.getOriContentChannel();
//							contentChannel.setStatus(ContentConstant.STATUS_SMART_AUDIT_SUCCESS);
							banContents.add(content);
//							contentChannels.add(contentChannel);
						}
					}
				}
				if (isRemove) {
					cMap.remove(contentMark);
				} else {
					removeContentCheckDetails.add(checkDetail);
				}
			}
//			contentService.batchUpdateAll(contents);
//			contentService.flush();
//			contentChannelService.batchUpdateAll(contentChannels);
//			contentChannelService.flush();
//			contentCheckDetailService.batchUpdate(contentCheckDetails);
            if (!CollectionUtils.isEmpty(banContents)) {
                for (Content content : banContents) {
                    BeatchDto dto = new BeatchDto();
                    dto.setCheckPerm(false);
                    dto.setIds(Arrays.asList(content.getId()));
                    dto.setSiteId(content.getSiteId());
                    dto.setStatus(ContentConstant.STATUS_SMART_AUDIT_SUCCESS);
                    contentService.changeStatus(dto, null);
                }
            }
            if (!CollectionUtils.isEmpty(errorContents)) {
                for (Content content : errorContents) {
                    BeatchDto dto = new BeatchDto();
                    dto.setCheckPerm(false);
                    dto.setIds(Arrays.asList(content.getId()));
                    dto.setSiteId(content.getSiteId());
                    dto.setStatus(ContentConstant.STATUS_SMART_AUDIT_FAILURE);
                    contentService.changeStatus(dto, null);
                }
            }
			contentCheckDetailService.batchUpdate(contentCheckDetails);
			if (cMap.size() > 0) {
				for (String contentMark : cMap.keySet()) {
					releaseContents.add(cMap.get(contentMark).getContent());
				}
				for (Content content : releaseContents) {
					BeatchDto dto = new BeatchDto();
					dto.setCheckPerm(false);
					dto.setIds(Arrays.asList(content.getId()));
					dto.setSiteId(content.getSiteId());
					dto.setStatus(ContentConstant.STATUS_PUBLISH);
					contentService.changeStatus(dto, null);
				}
			}
			contentCheckDetailService.physicalDeleteInBatch(removeContentCheckDetails);
		} else {

		}
	}

	@Autowired
	private ResourcesSpaceDataService resourcesSpaceDataService;
	@Autowired
	private CmsModelItemService cmsModelItemService;
	@Autowired
	private ContentChannelService contentChannelService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private AuditChannelSetService auditChannelSetService;
	@Autowired
	private AuditModelSetService auditModelSetService;
	@Autowired
	private ChastityUtil chastityUtil;
	@Autowired
	private MqSendMessageService mqSendMessageService;
	@Autowired
	private ContentCheckDetailService contentCheckDetailService;
	@Autowired
	private CacheProvider cacheProvider;
}
