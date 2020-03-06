/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.ChannelErrorCodeEnum;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.dao.ContentFrontDao;
import com.jeecms.content.domain.*;
import com.jeecms.content.domain.dto.ContentContributeDto;
import com.jeecms.content.domain.dto.SpliceCheckUpdateDto;
import com.jeecms.content.domain.vo.ContentContributeVo;
import com.jeecms.content.domain.vo.ContentFrontVo;
import com.jeecms.content.service.*;
import com.jeecms.member.domain.MemberScoreDetails;
import com.jeecms.member.service.MemberScoreDetailsService;
import com.jeecms.member.service.UserCollectionService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.jeecms.content.constants.ContentConstant.CONTENT_RELEASE_TERRACE_PC_NUMBER;
import static com.jeecms.content.constants.ContentConstant.CONTENT_RELEASE_TERRACE_WAP_NUMBER;

/**
 * 前台内容Service实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/19 9:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentFrontServiceImpl implements ContentFrontService {

	private static final String LIKE_COOKIE = "_like_cookie_";

	private static final int INTERVAL = 60 * 1000 * 20;
	/**
	 * 最后刷新时间,线程对volatile变量的修改会立刻被其他线程所感知，即不会出现数据脏读的现象，从而保证数据的“可见性”。
	 */
	private volatile Long refreshTime = System.currentTimeMillis();
	@Autowired
	private ContentFrontDao dao;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private ContentExtService contentExtService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentVersionService contentVersionService;
	@Autowired
	private ContentChannelService contentChannelService;
	@Autowired
	private UserCollectionService userCollectionService;
	@Autowired
	private ContentTxtService contentTxtService;
	@Autowired
	private CmsModelItemService cmsModelItemService;
	@Autowired
	private CoreUserService userService;
	@Resource(name = CacheConstants.CONTENT_NUM)
	private Ehcache cache;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private MemberScoreDetailsService memberScoreDetailsService;

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Page<Content> getPage(Integer[] channelIds, Integer[] tagIds, String[] channelPaths,
								 Integer siteId, Integer[] typeIds, String title,
								 Boolean isNew, Integer releaseTarget, Boolean isTop,
								 Date timeBegin, Date timeEnd, Integer[] excludeId,
								 Integer[] modelId, Integer orderBy,
								 Pageable pageable, CmsSite site) {
		if (releaseTarget == null) {
			releaseTarget = SystemContextUtils.isPc() ? CONTENT_RELEASE_TERRACE_PC_NUMBER : CONTENT_RELEASE_TERRACE_WAP_NUMBER;
		}
		Date date = null;
		if (isNew != null && isNew) {
			date = getNewContentTime(site);
		}
		List<Integer> channels = new ArrayList<Integer>();
		List<Channel> channelArray = new ArrayList<>();
		if (channelIds != null) {
			channelArray = channelService.findByIds(Arrays.asList(channelIds));
		}
		if (channelPaths != null) {
			channelArray.addAll(channelService.findByPath(channelPaths, site.getId()));
		}
		if (channelArray != null) {
			for (Channel channel : channelArray) {
				channels.addAll(channel.getChildAllIds());
			}
		}
		if (channels.size() > 0) {
			channels = channels.parallelStream()
					.filter(Objects::nonNull)
					.distinct()
					.collect(Collectors.toList());
			channelPaths = null;
			channelIds = channels.toArray(new Integer[channels.size()]);
		}
		if (siteId == null && (channelIds == null || channelIds.length == 0) && typeIds == null) {
			siteId = site.getId();
		}
		return dao.getPage(channelIds, tagIds, channelPaths, siteId, typeIds, title, date,
				releaseTarget, isTop, timeBegin, timeEnd, excludeId, modelId, orderBy, pageable);
	}


	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Page<Content> getPage(Integer siteId, Integer userId, Integer status, String title,
								 Date startDate, Date endDate, Pageable pageable) {
		return dao.getPage(siteId, userId, status, title, startDate, endDate, pageable);
	}
	

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<Content> getList(Integer[] channelIds, Integer[] tagIds,
								 String[] channelPaths, Integer siteId,
								 Integer[] typeIds, String title,
								 Boolean isNew, Integer releaseTarget,
								 Boolean isTop, Date timeBegin,
								 Date timeEnd, Integer[] excludeId,
								 Integer[] modelId, Integer orderBy,
								 Integer count, CmsSite site) {
		if (releaseTarget == null) {
			releaseTarget = SystemContextUtils.isPc() ? CONTENT_RELEASE_TERRACE_PC_NUMBER : CONTENT_RELEASE_TERRACE_WAP_NUMBER;
		}
		Date date = null;
		if (isNew != null && isNew) {
			date = getNewContentTime(site);
		}
		List<Integer> channels = new ArrayList<Integer>();
		List<Channel> channelArray = new ArrayList<>();
		if (channelIds != null) {
			channelArray = channelService.findByIds(Arrays.asList(channelIds));
		}
		if (channelPaths != null) {
			channelArray.addAll(channelService.findByPath(channelPaths, site.getId()));
		}
		if (channelArray != null) {
			for (Channel channel : channelArray) {
				channels.addAll(channel.getChildAllIds());
			}
		}
		if (channels.size() > 0) {
			channels = channels.parallelStream()
					.filter(Objects::nonNull)
					.distinct()
					.collect(Collectors.toList());
			channelPaths = null;
			channelIds = channels.toArray(new Integer[channels.size()]);
		}
		if (siteId == null && (channelIds == null || channelIds.length == 0) && typeIds == null) {
			siteId = site.getId();
		}
		return dao.getList(channelIds, tagIds, channelPaths, siteId, typeIds, title, date,
				releaseTarget, isTop, timeBegin, timeEnd, excludeId, modelId, orderBy, count);
	}

	@Override
	public List<Content> getList(Integer[] channelIds, Integer[] tagIds,
								 String[] channelPaths, Integer siteId,
								 Integer[] typeIds, String title,
								 Boolean isNew, Integer releaseTarget,
								 Boolean isTop, Date timeBegin,
								 Date timeEnd, Integer[] excludeId,
								 Integer[] modelId, Integer orderBy,
								 Integer count, CoreUser user, CmsSite site) {
		if (releaseTarget == null) {
			releaseTarget = SystemContextUtils.isPc() ? CONTENT_RELEASE_TERRACE_PC_NUMBER : CONTENT_RELEASE_TERRACE_WAP_NUMBER;
		}
		Date date = null;
		if (isNew != null && isNew) {
			date = getNewContentTime(site);
		}
		List<Integer> channels = new ArrayList<Integer>();
		if (channelIds != null) {
			List<Channel> channelArray = channelService.findAllById(Arrays.asList(channelIds));
			if (channelArray != null) {
				for (Channel channel : channelArray) {
					channels.addAll(channel.getChildAllIds());
				}
			}
		}
		if (channelPaths != null) {
			List<Channel> channelArray = channelService.findByPath(channelPaths, site.getId());
			if (channelArray != null) {
				for (Channel channel : channelArray) {
					channels.addAll(channel.getChildAllIds());
				}
			}
		}
		if (channels.size() > 0) {
			channels = channels.parallelStream()
					.filter(Objects::nonNull)
					.distinct()
					.collect(Collectors.toList());
			channelPaths = null;
			channelIds = channels.toArray(new Integer[channels.size()]);
		}
		if (siteId == null && (channelIds == null || channelIds.length == 0) && typeIds == null) {
			siteId = site.getId();
		}
		List<Content> contents = dao.getList(channelIds, tagIds, channelPaths, siteId, typeIds, title, date,
				releaseTarget, isTop, timeBegin, timeEnd, excludeId, modelId, orderBy, null);
		return excludeSecret(user, contents, count);
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<Content> getList(Integer[] relationIds, Integer orderBy, Integer count) {
		return dao.getList(relationIds, orderBy, count);
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<Content> getList(Integer[] relationIds, Integer orderBy, Integer count, CoreUser user) {
		List<Content> contents = dao.getList(relationIds, orderBy, null);
		return excludeSecret(user, contents, count);
	}

	/**
	 * 筛选符合密级的内容
	 *
	 * @param user     用户
	 * @param contents 内容集合
	 * @param count    数量
	 * @return List
	 */
	private List<Content> excludeSecret(CoreUser user, List<Content> contents, Integer count) {
		List<Content> list = new ArrayList<>();
		int i = 0;
		for (Content content : contents) {
			Integer secretId = content.getRealContentSecretId();
			//密级不存在，或者用户拥有该密级
			boolean flag = secretId == null || (user != null && user.getUserSecret().getContentSecretIds().contains(secretId));
			if (flag) {
				list.add(content);
				i++;
			}
			if (i >= count) {
				break;
			}
		}
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<Content> findAllById(List<Integer> ids, Integer orderBy) {
		if (ids == null) {
			return new ArrayList<>(0);
		}
		return dao.findByIds(ids, orderBy);
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<Content> findAllById(List<Integer> ids, Integer orderBy, CoreUser user) {
		if (ids == null) {
			return new ArrayList<>(0);
		}
		return excludeSecret(user, dao.findByIds(ids, orderBy), ids.size());
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Content findById(Integer id) {
		return contentService.findById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Content getSide(Integer id, Integer siteId, Integer channelId, Boolean next) {
		return dao.getSide(id, siteId, channelId, next, true);
	}

	@Override
	public void contribute(ContentContributeDto dto) throws GlobalException {

		/**
		 * 此处可能会出现一种情况：
		 * 1. 后台查看可能不存在正文(模型不存在正文)
		 * 2. 前台查看可能出现正文(模型字段写死)
		 */
		Channel channel = channelService.findById(dto.getChannnelId());
		if (channel == null) {
			throw new GlobalException(
					new SystemExceptionInfo(
							ChannelErrorCodeEnum.CHANNEL_ID_PASSED_ERROR.getDefaultMessage(),
							ChannelErrorCodeEnum.CHANNEL_ID_PASSED_ERROR.getCode()));
		}
		CmsSite site = cmsSiteService.findById(channel.getSiteId());
		
		Content content = new Content();
		// 前台投稿使用的模型是默认为"排序第一"的模型
		List<CmsModel> cmsModels = cmsModelService.findList(CmsModel.CONTENT_TYPE, site.getId());
		if (cmsModels == null || cmsModels.size() == 0) {
			throw new GlobalException(
					new SystemExceptionInfo(
							SettingErrorCodeEnum.MODEL_NOT_SET.getDefaultMessage(),
							SettingErrorCodeEnum.MODEL_NOT_SET.getCode()));
		}
		Integer modelId = cmsModels.get(0).getId();
		content.setModelId(modelId);
		content.setUserId(SystemContextUtils.getUserId(RequestUtils.getHttpServletRequest()));
		content = dto.initContent(content, dto, channel, site.getCmsSiteCfg(), site.getId(), channel.getRealWorkflowId() != null ? true : false, false);
		ContentExt contentExt = new ContentExt();
		contentExt = contentExtService.initContributeContentExt(contentExt, site.getId(), dto, dto.getChannnelId(), modelId);
		content.setContentExt(contentExt);
		contentExt.setContent(content);
		Content bean = contentService.save(content);
		bean.setSite(site);
		// 如果是提交，获取积分
		contentService.flush();
		if (dto.getIsSubmit()) {
			memberScoreDetailsService.addMemberScore(MemberScoreDetails.CONTRIBUTOR_SCORE_TYPE,bean.getUserId(), bean.getSiteId(),null);
		}
		List<CmsModelItem> items = cmsModelItemService.findByModelId(modelId);
		if (items != null && items.size() > 0) {
			items = items.stream().filter(item -> CmsModelConstant.CONTENT_TXT.equals(item.getDataType())).collect(Collectors.toList());
			if (items != null && items.size() > 0) {
				if (items.size() > 1) {
					items = items.stream()
							.sorted(Comparator.comparing(CmsModelItem::getSortNum)).collect(Collectors.toList());
				}
				String field = items.get(0).getField();
				Map<String, String> txtMap = new HashMap<String, String>(16);
				txtMap.put(field, dto.getContxt());
				if (Content.AUTOMATIC_SAVE_VERSION_TRUE.equals(site.getConfig().getContentSaveVersion())) {
					// 此处Map无需处理为空的情况在其具体方法中已经处理了
					contentVersionService.save(txtMap, bean.getId(), null);
				}
				ContentTxt contentTxt = new ContentTxt(field, dto.getContxt(), bean.getId());
				contentTxt.setContent(bean);
				contentTxtService.save(contentTxt);
			}
		}
		ContentChannel contentChannel = new ContentChannel(bean.getId(), bean.getChannelId(), 2, bean.getStatus(),
				false, bean, false);
		contentChannelService.save(contentChannel);
		List<ContentChannel> contentChannels = new ArrayList<ContentChannel>();
		contentChannels.add(contentChannel);
		bean.setContentChannels(contentChannels);
		contentService.initContentObject(bean);
		contentService.initContentExtObject(bean.getContentExt());
		if (channel.getRealWorkflowId() != null) {
			contentService.submit(null, null, true, bean);
		}
	}

	@Override
	public void updateContribute(ContentContributeDto dto, Channel channel,
								 Content content, HttpServletRequest request) throws GlobalException {
		final SpliceCheckUpdateDto oldDto = contentService.initSpliceCheckUpdateDto(content);
		content.setChannelId(dto.getChannnelId());
		content.setChannel(channel);
		content.setTitle(dto.getTitle());
		ContentExt contentExt = content.getContentExt();
		contentExt.setDescription(dto.getDescription());
		contentExt.setAuthor(dto.getAuthor());
		content.setContentExt(contentExt);
		if (dto.getIsSubmit()) {
			content.setStatus(ContentConstant.STATUS_FIRST_DRAFT);
			memberScoreDetailsService.addMemberScore(MemberScoreDetails.CONTRIBUTOR_SCORE_TYPE,content.getUserId(), content.getSiteId(),null);
		}
		Content bean = contentService.update(content);
		CmsSite site = cmsSiteService.findById(content.getSiteId());
		List<CmsModelItem> items = cmsModelItemService.findByModelId(content.getModelId());
		if (items != null && items.size() > 0) {
			items = items.stream().filter(item -> CmsModelConstant.CONTENT_TXT.equals(item.getDataType()))
					.collect(Collectors.toList());
			if (items != null && items.size() > 0) {
				if (items.size() > 1) {
					items = items.stream().sorted(Comparator.comparing(CmsModelItem::getSortNum))
							.collect(Collectors.toList());
				}
				String field = items.get(0).getField();
				Map<String, String> txtMap = new HashMap<String, String>(16);
				List<ContentTxt> contentTxts = contentTxtService.getTxts(bean.getId());
				for (ContentTxt contentTxt : contentTxts) {
					txtMap.put(contentTxt.getAttrKey(), contentTxt.getAttrTxt());
				}
				if (Content.AUTOMATIC_SAVE_VERSION_TRUE.equals(site.getConfig().getContentSaveVersion())) {
					// 此处Map无需处理为空的情况在其具体方法中已经处理了
					contentVersionService.save(txtMap, bean.getId(), null);
				}
				if (contentTxts != null && contentTxts.size() > 0) {
					for (ContentTxt contentTxt : contentTxts) {
						if (field.equals(contentTxt.getAttrKey())) {
							contentTxt.setAttrTxt(dto.getContxt());
						}
					}
					contentTxtService.batchUpdate(contentTxts);
				} else {
					ContentTxt contentTxt = new ContentTxt(field, dto.getContxt(), bean.getId());
					contentTxt.setContent(bean);
					contentTxtService.save(contentTxt);
				}
			}
		}
		SpliceCheckUpdateDto newDto = contentService.initSpliceCheckUpdateDto(bean);
		GlobalConfig globalConfig = SystemContextUtils.getGlobalConfig(request);
		contentChannelService.update(bean);
		contentService.checkUpdate(oldDto, newDto, globalConfig, bean);
		contentService.initContentObject(bean);
		contentService.initContentExtObject(bean.getContentExt());
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Content initialize(Content content) {
		Integer contentId = content.getId();
		Integer views, comments, downloads, ups, downs;
		if (cache.isKeyInCache(contentId) && cache.getQuiet(contentId) != null) {
			JSONObject json = JSONObject.parseObject(String.valueOf(cache.get(contentId).getObjectValue()));
			views = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_VIEWS);
			comments = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_COMMENTS);
			downloads = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS);
			ups = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_UPS);
			downs = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNS);
		} else {
			views = content.getViews();
			comments = content.getComments();
			downloads = content.getDownloads();
			ups = content.getUps();
			downs = content.getDowns();
		}
		//设置收藏状态
		CoreUser user = SystemContextUtils.getCoreUser();
		if (user != null) {
			boolean f = userCollectionService.isHaveCollection(contentId, user.getId());
			content.setCollection(f);
		}
		content.setViews(views);
		content.setComments(comments);
		content.setDownloads(downloads);
		content.setUps(ups);
		content.setDowns(downs);
		return content;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<Content> initializeList(List<Content> contents) {
		List<Content> list = new ArrayList<Content>(contents.size());
		for (Content content : contents) {
			Integer views;
			Integer comments;
			Integer downloads;
			Integer ups;
			Integer downs;
			if (cache.isKeyInCache(content.getId()) && cache.getQuiet(content.getId()) != null) {
				JSONObject json = JSONObject.parseObject(String.valueOf(cache.get(content.getId()).getObjectValue()));
				views = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_VIEWS);
				comments = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_COMMENTS);
				downloads = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS);
				ups = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_UPS);
				downs = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNS);
			} else {
				views = content.getViews();
				comments = content.getComments();
				downloads = content.getDownloads();
				ups = content.getUps();
				downs = content.getDowns();
			}
			content.setViews(views);
			content.setComments(comments);
			content.setDownloads(downloads);
			content.setUps(ups);
			content.setDowns(downs);
			list.add(content);
		}
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public ContentContributeVo splicContributeVo(Content content)  throws GlobalException{
		ContentContributeVo vo = new ContentContributeVo();
		vo.setChannel(content.getChannel());
		vo.setAuthor(content.getContentExt().getAuthor());
		vo.setContentId(content.getId());
		List<CmsModelItem> items = cmsModelItemService.findByModelId(content.getModelId());
		if (items != null && items.size() > 0) {
			items = items.stream().filter(item -> CmsModelConstant.CONTENT_TXT.equals(item.getDataType())).collect(Collectors.toList());
			if (items != null && items.size() > 0) {
				if (items.size() > 1) {
					items = items.stream()
							.sorted(Comparator.comparing(CmsModelItem::getSortNum)).collect(Collectors.toList());
				}
				String txtKey = items.get(0).getField();
				List<ContentTxt> txts = contentTxtService.getTxts(content.getId());
				if (txts != null && txts.size() > 0) {
					for (ContentTxt contentTxt : txts) {
						if (txtKey.equals(contentTxt.getAttrKey())) {
							vo.setContxt(contentTxt.getAttrTxt());
							break;
						}
					}
				}
			}
		}
		vo.setDescription(content.getContentExt().getDescription());
		vo.setTitle(content.getTitle());
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public boolean isUp(CoreUser user, Integer contentId, HttpServletRequest request) {
		if (user != null) {
			Content content = findById(contentId);
			List<Content> list = user.getLikeContents();
			//如果存在该点赞记录
			if (list.contains(content)) {
				return true;
			}
		} else {
			String cookieName = LIKE_COOKIE + contentId;
			Cookie cookie = CookieUtils.getCookie(request, cookieName);
			String cookieValue;
			if (cookie != null && !StringUtils.isBlank(cookie.getValue())) {
				cookieValue = cookie.getValue();
			} else {
				cookieValue = null;
			}
			//如果cookieValue不为空表示已点赞
			if (cookieValue != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void up(CoreUser user, Integer contentId, HttpServletRequest request,
				   HttpServletResponse response) throws GlobalException {
		if (user != null) {
			Content content = findById(contentId);
			List<Content> list = user.getLikeContents();
			if (!list.contains(content)) {
				list.add(content);
				user.setLikeContents(list);
				userService.update(user);
			}
		} else {
			String cookieName = LIKE_COOKIE + contentId;
			Cookie cookie = CookieUtils.getCookie(request, cookieName);
			String cookieValue;
			if (cookie != null && !StringUtils.isBlank(cookie.getValue())) {
				cookieValue = cookie.getValue();
			} else {
				cookieValue = null;
			}
			//如果cookieValue为空表示可以点赞
			if (cookieValue == null) {
				cookieValue = StringUtils.remove(UUID.randomUUID().toString(), "-");
				CookieUtils.addCookie(request, response, cookieName, cookieValue, Integer.MAX_VALUE, null);
			}
		}
	}

	@Override
	public void cancelUp(CoreUser user, Integer contentId, HttpServletRequest request,
						 HttpServletResponse response) throws GlobalException {
		if (user == null) {
			String cookieName = LIKE_COOKIE + contentId;
			Cookie cookie = CookieUtils.getCookie(request, cookieName);
			if (null != cookie && !StringUtils.isBlank(cookie.getValue())) {
				CookieUtils.cancleCookie(request, response, cookieName, null);
			}
		} else {
			List<Content> list = user.getLikeContents();
			List<Content> contents = new ArrayList<Content>();
			for (Content bean : list) {
				if (!bean.getId().equals(contentId)) {
					contents.add(bean);
				}
			}
			user.setLikeContents(contents);
			userService.update(user);
		}
	}

	@Override
	public JSONObject saveOrUpdateNum(Integer contentId, Integer commentNum, String type, boolean isDeleted)
			throws GlobalException {
		JSONObject returnJson = new JSONObject();
		if (cache.isKeyInCache(contentId) && cache.getQuiet(contentId) != null) {
			JSONObject json = JSONObject.parseObject(String.valueOf(cache.get(contentId).getObjectValue()));
			Integer num = 0;
			Integer dayNum = 0;
			switch (type) {
				case ContentConstant.CONTENT_NUM_TYPE_VIEWS:
					num = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_VIEWS);
					num = this.countNum(num, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_VIEWS, num);
					dayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_VIEWS);
					dayNum = this.countNum(dayNum, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_VIEWS, dayNum);
					break;
				case ContentConstant.CONTENT_NUM_TYPE_COMMENTS:
					num = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_COMMENTS);
					num = this.countNum(num, commentNum, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_COMMENTS, num);
					dayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_COMMENTS);
					dayNum = this.countNum(dayNum, commentNum, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_COMMENTS, dayNum);
					break;
				case ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS:
					num = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS);
					num = this.countNum(num, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS, num);
					dayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNLOADS);
					dayNum = this.countNum(dayNum, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNLOADS, dayNum);
					break;
				case ContentConstant.CONTENT_NUM_TYPE_UPS:
					num = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_UPS);
					num = this.countNum(num, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_UPS, num);
					dayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_UPS);
					dayNum = this.countNum(dayNum, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_UPS, dayNum);
					break;
				case ContentConstant.CONTENT_NUM_TYPE_DOWNS:
					num = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNS);
					num = this.countNum(num, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DOWNS, num);
					dayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNS);
					dayNum = this.countNum(dayNum, null, isDeleted);
					json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNS, dayNum);
					break;
				default:
					break;
			}
			cache.put(new Element(contentId, json));
			returnJson = JSONObject.parseObject(String.valueOf(cache.get(contentId).getObjectValue()));
			this.refreshToDB();
		} else {
			returnJson = this.saveEhcache(contentId);
			if (returnJson == null) {
				return null;
			}
			this.saveOrUpdateNum(contentId, commentNum, type, isDeleted);
		}
		return returnJson;
	}

	/**
	 * 计算数值
	 */
	private int countNum(int num, Integer commentNum, boolean isDeleted) {
		if (isDeleted) {
			if (commentNum != null) {
				if (commentNum > 0) {
					num = num - commentNum;
				}
				if (num < 0) {
					num = 0;
				}
			} else {
				num--;
				if (num<0) {
					num = 0;
				}
			}
		} else {
			num++;
		}
		return num;
	}

	/**
	 * 新增缓存
	 */
	private JSONObject saveEhcache(Integer contentId) {
		Content content = contentService.findById(contentId);
		if (content == null) {
			return null;
		}
		ContentExt contentExt = content.getContentExt();
		JSONObject json = new JSONObject();
		json.put(ContentConstant.CONTENT_NUM_TYPE_VIEWS, content.getViews());
		json.put(ContentConstant.CONTENT_NUM_TYPE_COMMENTS, content.getComments());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS, content.getDownloads());
		json.put(ContentConstant.CONTENT_NUM_TYPE_UPS, content.getUps());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DOWNS, content.getDowns());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_VIEWS, contentExt.getViewsDay());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_COMMENTS, contentExt.getCommentsDay());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNLOADS, contentExt.getDownloadsDay());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_UPS, contentExt.getUpsDay());
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNS, contentExt.getDownsDay());
		cache.put(new Element(contentId, json));
		// 使用refreshTime是为了防止缓存丢失的情况
		if (cacheProvider.exist(CacheConstants.TIME, ContentConstant.CONTENT_NUM_TYPE_END_TIME)) {
			cacheProvider.setCache(CacheConstants.TIME, ContentConstant.CONTENT_NUM_TYPE_END_TIME, System.currentTimeMillis());
		} else {
			cacheProvider.setCache(CacheConstants.TIME, ContentConstant.CONTENT_NUM_TYPE_END_TIME, refreshTime);
		}
		return json;
	}

	/**
	 * 判断持久化到数据库与处理数据
	 * 每周、每月、每日处理缓存与更新数据库
	 */
	private void refreshToDB() throws GlobalException {
		if (cacheProvider.exist(CacheConstants.TIME, ContentConstant.CONTENT_NUM_TYPE_END_TIME)) {
			cacheProvider.setCache(CacheConstants.TIME,
					ContentConstant.CONTENT_NUM_TYPE_END_TIME, refreshTime);
		}
		long newRefreshTime = Long.valueOf(
				String.valueOf(cacheProvider.getCache(CacheConstants.TIME,
						ContentConstant.CONTENT_NUM_TYPE_END_TIME)));
		long time = System.currentTimeMillis();
		if (time > newRefreshTime + INTERVAL) {
			this.saveToDB(cache);
		}
		long dayInitTime = MyDateUtils.getStartDate(new Date()).getTime();
		if (newRefreshTime < dayInitTime) {
			this.processDayNum(cache);
		}
		long weekInitTime = MyDateUtils.getSpecficWeekStart(new Date(), 0).getTime();
		if (newRefreshTime < weekInitTime) {
			this.processWeekNum(cache);
		}
		long monthInitTime = MyDateUtils.getSpecficMonthStart(new Date(), 0).getTime();
		if (newRefreshTime < monthInitTime) {
			this.processMonthNum(cache);
		}
		//直接加重锁
		synchronized (refreshTime) {
			refreshTime = time;
		}
		// 处理结束后更新重置时间
		cacheProvider.setCache(CacheConstants.TIME, ContentConstant.CONTENT_NUM_TYPE_END_TIME, System.currentTimeMillis());
	}

	/**
	 * 每隔一段时间将数据初始化到数据库中
	 */
	@SuppressWarnings("unchecked")
	private void saveToDB(Ehcache cache) throws GlobalException {
		List<Integer> keys = cache.getKeys();
		if (keys.size() == 0) {
			return;
		}
		List<Content> contents = contentService.findAllById(keys);
		Map<Integer, Content> contentMap = new HashMap<Integer, Content>(16);
		if (contents != null && contents.size() > 0) {
			contents = contents.stream().filter(content -> !content.getRecycle() && !content.getHasDeleted()).collect(Collectors.toList());
			if (contents.size() > 0) {
				contentMap = contents.stream().collect(Collectors.toMap(Content::getId, content -> content));
			}
		}
		contents.clear();
		Element element;
		JSONObject json;
		for (Integer it : keys) {
			element = cache.get(it);
			if (element != null) {
				Content content = contentMap.get(it);
				if (content != null) {
					json = JSONObject.parseObject(String.valueOf(element.getObjectValue()));
					content.setViews(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_VIEWS));
					content.setComments(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_COMMENTS));
					content.setUps(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_UPS));
					content.setDowns(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNS));
					content.setDownloads(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS));
					ContentExt contentExt = content.getContentExt();
					contentExt.setViewsDay(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_VIEWS));
					contentExt.setCommentsDay(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_COMMENTS));
					contentExt.setDownloadsDay(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNLOADS));
					contentExt.setUpsDay(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_UPS));
					contentExt.setDownsDay(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNS));
					content.setContentExt(contentExt);
					contents.add(content);
				}
			}
		}
		contentService.batchUpdate(contents);
	}

	/**
	 * 初始化修改本日的缓存和数据
	 */
	private void processDayNum(Ehcache cache) throws GlobalException {
		this.processNum(cache, 1);
	}

	/**
	 * 初始化修改本周的缓存和数据
	 */
	private void processWeekNum(Ehcache cache) throws GlobalException {
		this.processNum(cache, 2);
	}

	/**
	 * 初始化修改本月的缓存和数据
	 */
	private void processMonthNum(Ehcache cache) throws GlobalException {
		this.processNum(cache, 3);
	}

	/**
	 * 处理数据
	 */
	@SuppressWarnings("unchecked")
	private void processNum(Ehcache cache, Integer status) throws GlobalException {
		List<Integer> keys = cache.getKeys();
		if (keys.size() == 0) {
			return;
		}
		List<Content> contents = contentService.findAllById(keys);
		Map<Integer, Content> contentMap = new HashMap<Integer, Content>(16);
		if (contents != null && contents.size() > 0) {
			contentMap = contents.stream().collect(Collectors.toMap(Content::getId, content -> content));
		}
		contents.clear();
		Element element;
		JSONObject json;
		for (Integer it : keys) {
			element = cache.get(it);
			if (element != null) {
				Content content = contentMap.get(it);
				if (content != null) {
					json = JSONObject.parseObject(String.valueOf(element.getObjectValue()));
					content = this.splicContentNum(json, content, status);
					this.initEhcache(it, json);
					contents.add(content);
				}
			}
		}
		contentService.batchUpdate(contents);
		cacheProvider.setCache(CacheConstants.TIME, ContentConstant.CONTENT_NUM_TYPE_END_TIME, System.currentTimeMillis());
	}

	/**
	 * 初始化缓存
	 */
	private void initEhcache(Integer contentId, JSONObject json) {
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_VIEWS, 0);
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_COMMENTS, 0);
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNLOADS, 0);
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_UPS, 0);
		json.put(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNS, 0);
		cache.put(new Element(contentId, json));
	}

	/**
	 * 计算内容中的数值数据
	 */
	private Content splicContentNum(JSONObject json, Content content, Integer status) {
		content.setViews(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_VIEWS));
		content.setComments(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_COMMENTS));
		content.setUps(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_UPS));
		content.setDowns(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNS));
		content.setDownloads(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DOWNLOADS));
		ContentExt contentExt = content.getContentExt();
		Integer viewsDayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_VIEWS);
		contentExt.setViewsDay(0);
		contentExt.setViewsWeek(contentExt.getViewsWeek() + viewsDayNum);
		contentExt.setViewsMonth(contentExt.getViewsMonth() + viewsDayNum);
		Integer commentsDayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_COMMENTS);
		contentExt.setCommentsDay(0);
		contentExt.setCommentsWeek(contentExt.getCommentsWeek() + commentsDayNum);
		contentExt.setCommentsMonth(contentExt.getCommentsMonth() + commentsDayNum);
		Integer downloadsDayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNLOADS);
		contentExt.setDownloadsDay(0);
		contentExt.setDownloadsWeek(contentExt.getCommentsWeek() + downloadsDayNum);
		contentExt.setDownloadsMonth(contentExt.getCommentsMonth() + downloadsDayNum);
		Integer upsDayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_UPS);
		contentExt.setUpsDay(0);
		contentExt.setUpsWeek(contentExt.getUpsWeek() + upsDayNum);
		contentExt.setUpsMonth(contentExt.getCommentsMonth() + upsDayNum);
		Integer downsDayNum = json.getInteger(ContentConstant.CONTENT_NUM_TYPE_DAY_DOWNS);
		contentExt.setDownsDay(0);
		contentExt.setDownsWeek(contentExt.getDownloadsWeek() + downsDayNum);
		contentExt.setDownsMonth(contentExt.getDownloadsMonth() + downsDayNum);

		if (status >= 2) {
			contentExt.setViewsWeek(0);
			contentExt.setCommentsWeek(0);
			contentExt.setDownloadsWeek(0);
			contentExt.setUpsWeek(0);
			contentExt.setDownsWeek(0);
		}
		if (status == 3) {
			contentExt.setViewsMonth(0);
			contentExt.setCommentsMonth(0);
			contentExt.setDownloadsMonth(0);
			contentExt.setUpsMonth(0);
			contentExt.setDownsMonth(0);
		}
		content.setContentExt(contentExt);
		return content;
	}

	/**
	 * 获取新新闻时间
	 *
	 * @param site 站点
	 * @return
	 */
	private Date getNewContentTime(CmsSite site) {
		boolean openContentNewFlag = site.getConfig().getOpenContentNewFlag();
		Integer contentFlagType = site.getConfig().getContentNewFlagType();
		Integer contentNewFlag = site.getConfig().getContentNewFlag();
		if (openContentNewFlag && contentFlagType != null && contentNewFlag != null) {
			Date date = Calendar.getInstance().getTime();
			return contentFlagType == 1
					? MyDateUtils.getSpecficDate(date, -contentNewFlag)
					: MyDateUtils.getHourAfterTime(date, -contentNewFlag);
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Page<ContentFrontVo> getMobilePage(Integer siteId, Integer userId, Integer status, Pageable pageable) throws GlobalException{
		/**
		 * PC端已写死，已发布、待审核、暂存的情况，但是手机端只存在已发布及待审核，所以此次进行此处理
		 */
		if (status == ContentConstant.CONTRIBUTE_TEMPORARY_STORAGE) {
			status = ContentConstant.CONTRIBUTE_RELEASE;
		}
		Page<Content> contents = dao.getPage(siteId, userId, status, null, null, null, pageable);
		List<ContentFrontVo> vos = this.initMobileVos(contents);
		vos = vos.stream()
				.skip(pageable.getPageSize() * (pageable.getPageNumber()))
				.limit(pageable.getPageSize()).collect(Collectors.toList());
		Page<ContentFrontVo> page = new PageImpl<ContentFrontVo>(vos, pageable, vos.size());
		return page;
	}
	
	/**
	 * 初始化"内容手机版显示vo"集合
	 * @Title: initMobileVos  
	 * @param contents	分页内容集合
	 * @throws GlobalException   全局异常
	 * @return: List
	 */
	private List<ContentFrontVo> initMobileVos(Page<Content> contents)  throws GlobalException{
		List<ContentFrontVo> vos = new ArrayList<ContentFrontVo>();
		for (Content content : contents) {
			ContentFrontVo vo = new ContentFrontVo();
			vo = this.initMobileVo(vo, content);
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 初始化"内容手机版显示vo"对象
	 * @Title: initMobileVo  
	 * @param vo		内容手机版显示vo
	 * @param content	内容对象
	 * @throws GlobalException   全局异常   
	 * @return: ContentMobileVo
	 */
	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public ContentFrontVo initMobileVo(ContentFrontVo vo, Content content) throws GlobalException {
		Integer contentId = content.getId();
		vo.setId(contentId);
		vo.setTitle(content.getTitle());
		vo.setTitleIsBold(content.getTitleIsBold());
		vo.setTitleColor(content.getTitleColor());
		vo.setChannelName(content.getChannelName());
		vo.setCreateTime(MyDateUtils.getTime(content.getCreateTime()));
		vo.setUrl(content.getUrlWhole());
		vo.setPublishTime(content.getReleaseTimeString());
		vo.setModelId(content.getModelId());
		JSONObject json = null;
		// 校验是否存在缓存块，如果是则直接处理初始化数值操作，如果不存在，则初始化缓存，再处理初始化数值操作
		if (cache.isKeyInCache(contentId) && cache.getQuiet(contentId) != null) {
			json = JSONObject.parseObject(String.valueOf(cache.get(contentId).getObjectValue()));
			vo = this.initMobileNumVo(vo, json);
		} else {
			json = this.saveEhcache(contentId);
			vo = this.initMobileNumVo(vo, json);
		}
		if (json == null) {
			return null;
		}
		vo = this.initMobileJsonVo(content, vo);
		return vo;
	}
	
	private ContentFrontVo initMobileJsonVo(Content content,ContentFrontVo vo) throws GlobalException {
		List<CmsModelItem> items = cmsModelItemService.findByModelId(content.getModelId());
		if (items == null) {
			items = new ArrayList<CmsModelItem>();
		}
		List<ContentAttr> attrs = content.getContentAttrs();
		if (attrs == null) {
			attrs = new ArrayList<ContentAttr>();
 		}
		// 初始化视频、图片的JSON串，和"多图上传"字段，string集合
		vo.setVideoJson(vo.initVideoJson(items, attrs));
		vo.setImageJson(vo.initImageJson(items, attrs, content));
		vo.setMultiImageUploads(vo.initMultiImageUploads(items));
		return vo;
	}
	
	/**
	 * 初始化"内容手机版显示vo"的数值
	 * @Title: initMobileNumVo  
	 * @param vo	内容手机版显示vo对象
	 * @param json	储存content数值的缓存的json串
	 * @return: ContentMobileVo
	 */
	private ContentFrontVo initMobileNumVo(ContentFrontVo vo, JSONObject json) {
		vo.setViews(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_VIEWS));
		vo.setComments(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_COMMENTS));
		vo.setUps(json.getInteger(ContentConstant.CONTENT_NUM_TYPE_UPS));
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public ContentFrontVo initPartVo(Content content) throws GlobalException {
		ContentFrontVo vo = new ContentFrontVo();
		vo.setModelId(content.getModelId());
		vo = this.initMobileJsonVo(content, vo);
		return vo;
	}
	
}
