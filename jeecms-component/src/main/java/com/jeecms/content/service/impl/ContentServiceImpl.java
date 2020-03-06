/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.collect.domain.CollectContent;
import com.jeecms.collect.service.CollectContentService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.ChannelErrorCodeEnum;
import com.jeecms.common.exception.error.ContentErrorCodeEnum;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.local.ThreadPoolService;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.SnowFlake;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.util.office.Doc2Html;
import com.jeecms.common.util.office.FileUtils;
import com.jeecms.common.util.office.OpenOfficeConverter;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.wechat.bean.request.mp.material.AddMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;
import com.jeecms.common.wechat.bean.request.mp.material.common.SaveArticles;
import com.jeecms.component.listener.*;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.constants.ContentConstant.*;
import com.jeecms.content.dao.ContentDao;
import com.jeecms.content.domain.*;
import com.jeecms.content.domain.dto.*;
import com.jeecms.content.domain.vo.ContentFindVo;
import com.jeecms.content.domain.vo.WechatPushVo;
import com.jeecms.content.service.*;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.dto.UploadResult;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.resource.service.impl.UploadService;
import com.jeecms.system.domain.*;
import com.jeecms.system.domain.dto.BeatchDto;
import com.jeecms.system.service.*;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatSendService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.jeecms.content.constants.ContentConstant.*;

/**
 * 内容主体service实现类
 *
 * @author: chenming
 * @date: 2019年5月6日 下午2:35:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "workflow.support", havingValue = "local", matchIfMissing = true)
public class ContentServiceImpl extends BaseServiceImpl<Content, ContentDao, Integer>
		implements ContentService, SiteListener, ChannelListener, WorkflowListener, CmsModelListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

	@Override
	public void afterChannelSave(Channel c) throws GlobalException {
		/** 空实现即可 */
	}

	@Override
	public void beforeChannelDelete(Integer[] ids) throws GlobalException {
		// 栏目加入回收站后对应的所有内容也全部加入回收站
		List<Content> contents = dao.findByChannelIdInAndHasDeleted(ids, false);
		if (contents.size() > 0) {
			super.delete(contents);
		}
		for (ContentListener listener : listenerList) {
			listener.afterDelete(contents);
		}

	}

	@Override
	public void afterSiteSave(CmsSite site) throws GlobalException {
		/** 空实现即可 */
	}

	@Override
	public void afterModelDelete(Collection<CmsModel> models) throws GlobalException {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterModelUpdate(CmsModel model) throws GlobalException {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeSitePhysicDelete(Integer[] ids) throws GlobalException {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeWorkflowDelete(Integer[] flowIds) throws GlobalException {
//		flowService.doInterruptFlow(flowIds);
	}

	/**
	 * 保存之后调用 根据业务生成索引、文库转换后文件
	 *
	 * @param content content
	 * @Title: afterSave
	 * @return: void
	 */
	private void afterSave(Content content) throws GlobalException {
		if (listenerList != null) {
			for (ContentListener listener : listenerList) {
				listener.afterSave(content);
			}
		}
	}

	/**
	 * 更新前调用记录 内容状态等数据
	 *
	 * @param content 内容
	 * @Title: preChange
	 */
	public List<Map<String, Object>> preChange(Content content) {
		if (listenerList != null) {
			int len = listenerList.size();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(len);
			for (ContentListener listener : listenerList) {
				list.add(listener.preChange(content));
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 更新后调用 根据业务更新索引、静态页、文库转换后文件
	 *
	 * @param content 内容
	 * @param mapList List
	 * @Title: afterChange
	 * @return: void
	 */
	private void afterChange(Content content, List<Map<String, Object>> mapList) throws GlobalException {
		if (listenerList != null) {
			Assert.notNull(mapList, "mapList can not null");
			Assert.isTrue(mapList.size() == listenerList.size(), "mapList size not equals listenerList");
			int len = listenerList.size();
			ContentListener listener;
			for (int i = 0; i < len; i++) {
				listener = listenerList.get(i);
				// listener.afterChange(content, mapList.get(i));
				ThreadPoolService.getInstance().execute(new ListenerThread(listener, mapList.get(i), content));
			}
		}
	}

	class ListenerThread implements Runnable {
		ContentListener listener;
		Map<String, Object> map;
		Content content;

		public ListenerThread(ContentListener listener, Map<String, Object> map, Content content) {
			this.listener = listener;
			this.map = map;
			this.content = content;
		}

		@Override
		public void run() {
			try {
				listener.afterChange(content, map);
			} catch (GlobalException e) {

			}

		}
	}

	/**
	 * 刪除时调用 删除索引、静态页、文库转换后文件
	 *
	 * @param content Content
	 * @Title: afterDelete
	 * @return: void
	 */
	private void afterDelete(Content content) throws GlobalException {
		if (listenerList != null) {
			List<Content> contents = new ArrayList<Content>();
			contents.add(content);
			for (ContentListener listener : listenerList) {
				listener.afterDelete(contents);
			}
		}
	}
	
	@Override
	public ResponseInfo changeStatus(BeatchDto dto, CoreUser user) throws GlobalException {
		if (user == null) {
			user = SystemContextUtils.getCoreUser();
		}
		List<Content> list = new ArrayList<Content>(10);
		List<ContentRecord> records = new ArrayList<ContentRecord>(10);
		List<Integer> ids = dto.getIds();
		List<Content> contents = super.findAllById(ids);
		// 智能审核内容集合
		List<Content> checkContents = null;
		boolean isReview = false;
		// 如果状态是发布则判断其内容是否开启是否可以进行智能审核
		if (ContentConstant.STATUS_PUBLISH == dto.getStatus()) {
			if (contentReviewService.checkAppIdOrPhone()) {
				checkContents = new ArrayList<Content>();
				for (Content content : contents) {
					boolean checkReview = false;
					// 此判断是为了如果内容审核成功没有违禁词则直接进行发布操作，而不会再次进行修改操作
					if (ContentConstant.STATUS_SMART_AUDIT != content.getStatus()) {
						/**
						 * TODO 后期优化：checkReview应当修改成返回int类型
						 */
						checkReview = contentReviewService.reviewContentCheck(content,content.getChannelId(), content.getModelId());
					}
					if (checkReview) {
						isReview = true;
						checkContents.add(content);
					}
				}
			}
		}
		if (dto.isCheckPerm()) {
			/** 忽略权限检查也忽略状态判断 */
			// 判断状态是否可以操作
			checkOperate(contents, dto.getStatus(), dto.getSiteId());
			Map<Integer, List<Content>> map = contents.stream().collect(Collectors.groupingBy(Content::getChannelId));
			// 判断是否存在不可操作数据
			// 判断前台传的状态包含的设置初稿，草稿，流转中为修改权限，发布，下线，归档，是否有操作权限，因为发布与下线、为权限一体，归档另当作权限一体；
			for (Entry<Integer, List<Content>> entry : map.entrySet()) {
				List<Short> opration = user.getContentOperatorByChannelId(entry.getKey());
				// 判断发布与下线
				if (dto.getStatus().equals(STATUS_PUBLISH) || dto.getStatus().equals(STATUS_NOSHOWING)) {
					// 如果没有权限，则返回存在不可操作数据
					if (!opration.contains(CmsDataPerm.OPE_CONTENT_PUBLISH)) {
						return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
								UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
					}
				} else if (dto.getStatus().equals(STATUS_DRAFT) || dto.getStatus().equals(STATUS_FIRST_DRAFT)
						|| dto.getStatus().equals(STATUS_FLOWABLE)) {
					// 判断修改
					if (!opration.contains(CmsDataPerm.OPE_CONTENT_EDIT)) {
						return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
								UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
					}
				} else if (dto.getStatus().equals(STATUS_PIGEONHOLE)) {
					// 判断归档
					if (!opration.contains(CmsDataPerm.OPE_CONTENT_FILE)) {
						return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
								UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
					}
				}

			}
		}
		List<Content> newCheckContents = null;
		if (!CollectionUtils.isEmpty(checkContents)) {
			newCheckContents = new ArrayList<Content>();
		}
		// 修改状态的同时，也需要修改contentChannel里面的状态值
		List<ContentChannel> ccs = new ArrayList<ContentChannel>(10);
		for (Content content : contents) {
			boolean isCheck = false;
			Integer status = dto.getStatus();
			// 判断该内容是否在需要智能审核中，如果需要则将传入的状态改成智能审核中的状态
			if (!CollectionUtils.isEmpty(checkContents)) {
				if (checkContents.contains(content)) {
					isCheck = true;
					status = ContentConstant.STATUS_SMART_AUDIT;
					content.setCheckMark(String.valueOf(snowFlake.nextId()));
				}
			}
			List<Map<String, Object>> pre = preChange(content);
			content.setStatus(status);
			content.setEdit(true);
			ContentChannel cc = content.getOriContentChannel();
			if (cc != null) {
				cc.setStatus(status);
				ccs.add(cc);
			}
			list.add(content);
			String userName = null;
			if (user != null) {
				userName = user.getUsername();
			} else {
				userName = content.getUser().getUsername();
			}
			records.add(new ContentRecord(content.getId(), userName,
					"修改状态为" + ContentConstant.status(status)));
			Content bean = super.updateAll(content);
			if (isCheck) {
				newCheckContents.add(bean);
			}
			super.flush();
			/** 变更处理静态文件、索引等 */
			if (bean.getStatus().equals(STATUS_PUBLISH)) {
				List<ContentTxt> contentTxts = bean.getContentTxts();
				hotWordService.totalUserCount(bean.getChannelId(), contentTxts, content.getSiteId());
			}
			afterChange(bean, pre);
		}
		contentRecordService.saveAll(records);
		contentChannelService.batchUpdate(ccs);
		if (dto.getStatus() == STATUS_FLOWABLE) {
			for (Content content : contents) {
				doSubmitFlow(content);
			}
		}
		if (newCheckContents != null) {
			contentReviewService.reviewContents(newCheckContents,user.getId());
		}
		if (isReview) {
			return new ResponseInfo(ContentConstant.STATUS_SMART_AUDIT);
		} else {
			return new ResponseInfo(dto.getStatus());
		}
	}

	/**
	 * 判断该内容是否可以修改状态
	 *
	 * @throws GlobalException 异常
	 * @Title: checkOperate
	 * @param: contents 内容列表
	 * @param: status   修改的内容状态
	 */
	public void checkOperate(List<Content> contents, Integer status, Integer siteId) throws GlobalException {
		// 只处理以下几种状态，初稿，草稿，发布，下线，流转中，归档，如果前台传值不包含，则返回错误信息
		if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_FLOWABLE, STATUS_PUBLISH, STATUS_NOSHOWING,
				STATUS_PIGEONHOLE).contains(status)) {
			throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
		}
		CmsSiteConfig config = cmsSiteService.findById(siteId).getCmsSiteCfg();
		for (Content content : contents) {
			// 判断是否设置工作流
			Integer workflow = content.getChannel().getRealWorkflowId();
			if (workflow != null) {
				// 存在工作流
				switch (content.getStatus()) {
				case STATUS_DRAFT:
				case STATUS_FIRST_DRAFT:
					// 原状态为草稿，初稿状态时，可以设置为初稿，流转中，草稿， 归档
					if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_FLOWABLE, STATUS_PIGEONHOLE)
							.contains(status)) {
						throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
					}
					break;
				case STATUS_FLOWABLE:
					// 原状态为流转中状态时，不处理任何状态
					throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
				case STATUS_WAIT_PUBLISH:
					// 原状态为已审核状态时，可以设置为初稿，发布，草稿，流转， 归档
					if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_PUBLISH, STATUS_FLOWABLE,
							STATUS_PIGEONHOLE).contains(status)) {
						throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
					}
					break;
				case STATUS_PUBLISH:
					// 判断站点设置是否允许内容发布编辑， 归档
					Boolean flag = config.getContentCommitAllowUpdate();
					if (!flag) {
						// 原状态为发布状态时，可以设置为下线， 归档
						if (!Arrays.asList(STATUS_NOSHOWING, STATUS_PIGEONHOLE).contains(status)) {
							throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
						}
					} else {
						// 原状态为发布状态时，可以设置为初稿，发布，草稿，下线， 归档
						if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_PUBLISH, STATUS_NOSHOWING,
								STATUS_FLOWABLE, STATUS_PIGEONHOLE).contains(status)) {
							throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
						}
					}
					break;
				case STATUS_NOSHOWING:
					// 原状态为下线状态时，可以设置为初稿，发布，草稿，流转中， 归档
					if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_FLOWABLE, STATUS_PUBLISH,
							STATUS_PUBLISH, STATUS_PIGEONHOLE).contains(status)) {
						throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
					}
					break;
				case STATUS_BACK:
					// 原状态为驳回状态时，可以设置为初稿，流转中，草稿， 归档
					if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_FLOWABLE, STATUS_PIGEONHOLE)
							.contains(status)) {
						throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
					}
					break;
				default:
					break;
				}
			} else {
				// 不存在工作流
				switch (content.getStatus()) {
				case STATUS_DRAFT:
				case STATUS_FIRST_DRAFT:
					// 原状态为草稿状态时，可以设置为初稿，发布，草稿， 归档
					if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_PUBLISH, STATUS_PIGEONHOLE)
							.contains(status)) {
						throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
					}
					break;
				case STATUS_PUBLISH:
					// 原状态为发布状态时，可以设置下线， 归档
					Boolean flag = config.getContentCommitAllowUpdate();
					// 判断站点设置是否允许内容发布编辑
					if (!flag) {
						if (!Arrays.asList(STATUS_NOSHOWING, STATUS_PIGEONHOLE).contains(status)) {
							throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
						}
					} else {
						// 原状态为发布状态时，可以设置为初稿，发布，草稿，下线， 归档
						if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_PUBLISH, STATUS_NOSHOWING,
								STATUS_PIGEONHOLE).contains(status)) {
							throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
						}
					}
					break;
				case STATUS_NOSHOWING:
					// 原状态为下线状态时，可以设置为初稿，发布，草稿, 归档
					if (!Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_PUBLISH, STATUS_PIGEONHOLE)
							.contains(status)) {
						throw new GlobalException(ContentErrorCodeEnum.CONTENT_STATUS_ERROR);
					}
					break;
				case STATUS_FLOWABLE:
					throw new GlobalException(ContentErrorCodeEnum.CHANNEL_NOT_WORKFLOW_NOT_REVIEW);
				default:
					break;
				}
			}
		}
	}

	@Override
	public void publish(List<Integer> ids) throws GlobalException {
//		List<Content> contents = super.findAllById(ids);
		CoreUser user = SystemContextUtils.getCoreUser();
//		Integer userId = user.getId();
//		List<Content>  needYunCheckContents = new ArrayList<>();
//		/**将需要智能审核的内容送审*/
//		ContentUpdateDto dto = new ContentUpdateDto();
//		dto.setUser(user);
//		dto.setForce(false);
//		for (Content c : contents) {
//			dto.setId(c.getId());
//			/**查询是否需要智能参数*/
//			dto.setType(c.getStatus());
//			dto.setChannelId(c.getChannelId());
//			dto.setModelId(c.getModelId());
//			if(getNeedYunCheck(dto,c)){
//				needYunCheckContents.add(c);
//			}
//		}
//		contents.removeAll(needYunCheckContents);
//		/**其余内容不需要智能审核，走普通发布了*/
//		for (Content c : contents) {
//			List<Map<String, Object>> mapList = preChange(c);
//			c.setStatus(ContentConstant.STATUS_PUBLISH);
//			c.setReleaseTime(new Date());
//			c.getOriContentChannel().setStatus(ContentConstant.STATUS_PUBLISH);
//			afterChange(c, mapList);
//		}
//		contentReviewService.reviewContents(needYunCheckContents,userId);
		BeatchDto dto = new BeatchDto(ids, ContentConstant.STATUS_PUBLISH, false);
		this.changeStatus(dto, user);
	}

	@Override
	public ResponseInfo operation(OperationDto dto) throws GlobalException {
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		// 判断权限
		if (!validType(CmsDataPerm.OPE_CONTENT_TYPE, contents)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		// 内容类型操作
		type(dto, contents);
		return new ResponseInfo();
	}

	/**
	 * 类型操作
	 **/
	protected void type(OperationDto dto, List<Content> contents) throws GlobalException {
		List<Content> list = new ArrayList<Content>(10);
		// 得到内容类型ID
		Integer type = dto.getContentTypeId();
		// 判断是否新增内容类型
		if (dto.getAdd()) {
			ContentType contentType = contentTypeService.findById(type);
			for (Content content : contents) {
				List<Integer> integers = content.getContentTypes().stream().map(ContentType::getId)
						.collect(Collectors.toList());
				// 判断是否包含内容类型
				if (integers.contains(type)) {
					continue;
				}
				content.getContentTypes().add(contentType);
				content.setContentTypes(content.getContentTypes());
				list.add(content);
			}
		} else {
			// 取消内容类型
			for (Content content : contents) {
				// 得到内容类型
				List<ContentType> contentTypes = content.getContentTypes();
				CopyOnWriteArrayList<ContentType> cowList = new CopyOnWriteArrayList<ContentType>(contentTypes);
				for (ContentType contentType : cowList) {
					if (contentType.getId().equals(type)) {
						cowList.remove(contentType);
					}
				}
				content.setContentTypes(cowList);
				list.add(content);
			}
		}
		super.batchUpdate(list);
	}

	protected void order(OperationDto dto, List<Content> contents) throws GlobalException {
		List<Content> list = new ArrayList<Content>(10);
		// 待排序的内容
		Integer contentId = dto.getContentId();
		Content con = super.findById(contentId);
		// 得到排序值
		Integer sortNum = con.getSortNum();
		// 得到权重
		Integer sortWeight = con.getSortWeight();
		List<Boolean> booleans = contents.stream().map(Content::getTop).collect(Collectors.toList());
		// 排序之前
		if (dto.getLocation()) {
			for (Content content : contents) {
				// 如果排序值为0，增加权重
				if (sortNum.equals(0)) {
					content.setSortWeight(sortWeight + 1);
				} else {
					content.setSortNum(sortNum - 1);
				}
				list.add(content);
			}
			// 将非置顶内容放到置顶内容之前，需要加置顶状态
			if (con.getTop() && booleans.contains(false)) {
				List<Content> lists = contents.stream().filter(x -> x.getTop().equals(false))
						.collect(Collectors.toList());
				for (Content content : lists) {
					content.setTop(true);
				}
			}
		} else {
			// 排序之后
			for (Content content : contents) {
				// 如果排序值为0，减少权重
				if (sortNum.equals(0)) {
					if (sortWeight.equals(0)) {
						// 排序，权重都为0，不做操作
						break;
					} else {
						content.setSortWeight(sortWeight - 1);
					}
				} else {
					content.setSortNum(sortNum + 1);
				}
				list.add(content);
			}
			// 将非置顶内容放到置顶内容之后，需要取消置顶状态
			if (!con.getTop() && booleans.contains(true)) {
				List<Content> lists = contents.stream().filter(x -> x.getTop().equals(true))
						.collect(Collectors.toList());
				for (Content content : lists) {
					content.setTop(false);
				}
			}
		}
		super.batchUpdate(list);
	}

	@Override
	public ResponseInfo top(OperationDto dto) throws GlobalException {
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		if (!validType(CmsDataPerm.OPE_CONTENT_TOP, contents)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		for (Content content : contents) {
			content.setTopStartTime(dto.getStartTime());
			content.setTopEndTime(dto.getEndTime());
			content.setTop(true);
		}
		super.batchUpdate(contents);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo notop(OperationDto dto) throws GlobalException {
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		if (!validType(CmsDataPerm.OPE_CONTENT_TOP, contents)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		for (Content content : contents) {
			content.setTopStartTime(null);
			content.setTopEndTime(null);
			content.setTop(false);
		}
		super.batchUpdate(contents);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo move(OperationDto dto) throws GlobalException {
		// 接收操作完成后的集合
		List<Content> list = new ArrayList<Content>(10);
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		// 判断操作类型
		if (!validType(CmsDataPerm.OPE_CONTENT_MOVE, contents)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		// 移动
		Integer channelId = dto.getChannelId();
		Channel channel = channelService.findById(channelId);
		List<ContentChannel> ccs = new ArrayList<ContentChannel>(10);
		// 更改contentChannel的记录
		for (Content content : contents) {
			List<ContentChannel> cc = content.getContentChannels().stream()
					.filter(x -> x.getChannelId().equals(content.getChannelId())).collect(Collectors.toList());
			cc.get(0).setChannelId(channelId);
			cc.get(0).setChannel(channel);
			ccs.add(cc.get(0));
			content.setContentChannels(ccs);
		}
		List<Channel> channelList = new ArrayList<Channel>(10);
		channelList.add(channel);
		for (Content content : contents) {
			content.setChannelId(channelId);
			content.setChannel(channel);
			list.add(content);
		}
		super.batchUpdate(list);
		contentChannelService.batchUpdate(ccs);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo sort(OperationDto dto) throws GlobalException {
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		if (!validType(CmsDataPerm.OPE_CONTENT_ORDER, contents)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		// 排序
		order(dto, contents);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo rubbish(OperationDto dto) throws GlobalException {
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		// 判断操作类型
		if (!validType(CmsDataPerm.OPE_CONTENT_DEL, contents)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		// 删除内容，加入回收站
		for (Content content : contents) {
			content.setRecycle(true);
		}
		super.batchUpdate(contents);
		if (contents.size() > 0) {
			List<Integer> contentIds = contents.stream().filter(content -> content.getStatus().equals(STATUS_FLOWABLE))
					.map(Content::getId).collect(Collectors.toList());
			flowService.doInterruptDataFlow(ContentConstant.WORKFLOW_DATA_TYPE_CONTENT, contentIds,
					SystemContextUtils.getCoreUser());
		}
		if (contents.size() > 0) {
			for (ContentListener listener : listenerList) {
				List<Integer> contentIds = contents.stream().map(Content::getId).collect(Collectors.toList());
				listener.afterContentRecycle(contentIds);
			}
		}
		return new ResponseInfo();
	}

	/**
	 * 检查权限,只要有一个没有权限的，则直接返回false
	 */
	protected Boolean validType(Short opration, List<Content> contents) {
		CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
		Map<Integer, List<Content>> map = contents.stream().collect(Collectors.groupingBy(Content::getChannelId));
		// 判断类型
		for (Entry<Integer, List<Content>> entry : map.entrySet()) {
			List<Short> oprations = user.getContentOperatorByChannelId(entry.getKey());
			// 如果没有权限，直接返回false
			if (!oprations.contains(opration)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ResponseInfo quote(OperationDto dto) throws GlobalException {
		// 查询得到内容列表
		List<Content> contents = super.findAllById(dto.getIds());
		// 判断权限
		for (Content content : contents) {
			List<Short> oprations = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest())
					.getContentOperatorByChannelId(content.getChannelId());
			if (!oprations.contains(CmsDataPerm.OPE_CONTENT_QUOTE)) {
				return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
						UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
			}
		}
		// 得到引用栏目ID集合
		List<Integer> list = dto.getChannelIds();
		// 检测是否引用内容原有的栏目
		for (Content content : contents) {
			if (list.contains(content.getChannelId())) {
				return new ResponseInfo(ContentErrorCodeEnum.NO_QUOTE_CHANNEL_MYSELF_ERROR.getCode(),
						ContentErrorCodeEnum.NO_QUOTE_CHANNEL_MYSELF_ERROR.getDefaultMessage(), false);
			}
		}
		List<ContentChannel> contentChannels = new ArrayList<ContentChannel>(10);
		// 检测栏目是否存在该内容的引用
		List<Integer> create = ImmutableList.<Integer>builder().add(ContentConstant.CONTENT_CREATE_TYPE_URL)
				.add(ContentConstant.CONTENT_CREATE_TYPE_MIRROR).build();
		for (Integer channel : list) {
			List<Integer> ids = new ArrayList<Integer>(10);
			List<ContentChannel> array = contentChannelService.countQuote(channel, create);
			for (ContentChannel contentChannel : array) {
				ids.add(contentChannel.getContentId());
			}
			if (!Collections.disjoint(ids, dto.getIds())) {
				return new ResponseInfo(ContentErrorCodeEnum.ALREADY_QUOTE_CHANNEL_CONTENT.getCode(),
						ContentErrorCodeEnum.ALREADY_QUOTE_CHANNEL_CONTENT.getDefaultMessage(), false);
			}
			for (Content con : contents) {
				con.setContentChannels(array);
				ContentChannel cc = new ContentChannel();
				cc.setIsRef(true);
				cc.setChannelId(channel);
				cc.setChannel(channelService.findById(channel));
				cc.setContentId(con.getId());
				cc.setStatus(con.getStatus());
				if (dto.getCreateType().equals(1)) {
					cc.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_URL);
				} else {
					cc.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_MIRROR);
				}
				cc.setRecycle(false);
				contentChannels.add(cc);
			}
		}
		contentChannelService.saveAll(contentChannels);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo noquote(OperationDto dto) throws GlobalException {
		List<Integer> contents = dto.getIds();
		// 查询得到内容列表
		List<Content> contentList = super.findAllById(dto.getIds());
		// 判断权限
		for (Content content : contentList) {
			List<Short> oprations = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest())
					.getContentOperatorByChannelId(content.getChannelId());
			if (!oprations.contains(CmsDataPerm.OPE_CONTENT_QUOTE)) {
				return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
						UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
			}
		}
		// 得到引用栏目ID集合
		List<Integer> list = dto.getChannelIds();
		// 检测栏目是否存在该内容的引用
		List<Integer> create = ImmutableList.<Integer>builder().add(ContentConstant.CONTENT_CREATE_TYPE_URL)
				.add(ContentConstant.CONTENT_CREATE_TYPE_MIRROR).build();
		// 直接删除引用
		List<ContentChannel> contentChannels = new ArrayList<ContentChannel>(10);
		for (Integer integer : list) {
			List<ContentChannel> array = contentChannelService.countQuote(integer, create);
			for (ContentChannel cc : array) {
				if (contents.contains(cc.getContentId())) {
					contentChannels.add(cc);
				}
			}
		}
		contentChannelService.physicalDeleteInBatch(contentChannels);
		return new ResponseInfo();
	}

	@Override
	public Content save(ContentSaveDto dto, CmsSite site) throws GlobalException {
		
		boolean isCheck = false;
		if (ContentConstant.STATUS_PUBLISH == dto.getType()) {
			if (contentReviewService.checkAppIdOrPhone()) {
				boolean checkReview = contentReviewService.reviewContentCheck(null,dto.getChannelId(), dto.getModelId());
				if (checkReview) {
					dto.setType(STATUS_SMART_AUDIT);
					isCheck = true;
				}
			}
		}
		

		Content content = new Content();
		content.setSite(site);
		CmsModel model = cmsModelService.findById(dto.getModelId());
		content.setModel(model);
		Channel channel = channelService.findById(dto.getChannelId());
		content = dto.initContent(dto, content, site, site.getGlobalConfig(), false, channel);
		if (dto.getCreateType() != null) {
			content.setCreateType(dto.getCreateType());
		}
		// 校验传送过来的状态
		ContentInitUtils.checkStatus(content.getStatus(), dto.getType(),
				channel.getRealWorkflowId() != null ? true : false, false);
		ContentExt contentExt = new ContentExt();
		// 初始化tag将传递过来的string数组进行组装成contentTag集合，而后set到content进行级联新增
		List<ContentTag> tags = contentTagService.initTags(dto.getContentTag(), site.getId());
		content.setContentTags(tags);
		// 初始化contentExt对象
		contentExt = this.spliceContentExt(dto, contentExt, site.getId(), false);
		contentExt.setContent(content);
		content.setContentExt(contentExt);
		content.init();
		content.setChannel(channel);
		
		if (isCheck) {
			content.setCheckMark(String.valueOf(snowFlake.nextId()));
		}
		
		Content bean = super.save(content);
		// super.flush();
		if (bean.getContentExt().getPicResId() != null) {
			bean.getContentExt().setReData(resourcesSpaceDataService.findById(bean.getContentExt().getPicResId()));
		}

		Map<String, String> txtMap = contentTxtService.initContentTxt(dto.getJson(), dto.getModelId(), dto, false);
		if (txtMap != null && txtMap.size() > 0) {
			// 存储内容的文本内容需要进行额外处理作为单独的对象进行处理
			List<ContentTxt> contentTxts = ContentInitUtils.toListTxt(txtMap);
			// 初始化contentTxts并执行新增操作
			bean.setContentTxts(contentTxtService.saveTxts(contentTxts, bean));
			if (bean.getStatus().equals(STATUS_PUBLISH)) {
				hotWordService.totalUserCount(bean.getChannelId(), contentTxts, site.getId());
			}
		}
		if (Content.AUTOMATIC_SAVE_VERSION_TRUE.equals(site.getConfig().getContentSaveVersion())) {
			// 此处Map无需处理为空的情况在其具体方法中已经处理了
			contentVersionService.save(txtMap, bean.getId(), null);
			contentVersionService.flush();
		}
		Integer createType = dto.getCreateType() != null ? dto.getCreateType()
				: ContentConstant.CONTENT_CREATE_TYPE_ADD;
		ContentChannel contentChannel = new ContentChannel(bean.getId(), bean.getChannelId(), createType,
				bean.getStatus(), false, bean, false);
		contentChannel.setChannel(channel);
		contentChannel.setContent(bean);
		contentChannelService.save(contentChannel);
		contentChannelService.flush();
		List<ContentChannel> contentChannels = new ArrayList<ContentChannel>();
		contentChannels.add(contentChannel);
		bean.setContentChannels(contentChannels);
		bean = initAttr(bean, dto.getJson(), dto.getModelId(), bean.getId(), site.getGlobalConfig());
		CoreUser user = coreUserService.findById(bean.getUserId());
		ContentRecord contentRecord = new ContentRecord(bean.getId(), user.getUsername(), "新增", "", null, bean);
		contentRecordService.save(contentRecord);
		this.initContentObject(bean);
		this.initContentExtObject(bean.getContentExt());
		this.afterSave(bean);
		
		if (isCheck) {
			List<Content> contents = new ArrayList<Content>();
			contents.add(bean);
			contentReviewService.reviewContents(contents,dto.getUserId());
		}
		return bean;
	}

	/**
	 * 初始化内容扩展
	 */
	private ContentExt spliceContentExt(ContentSaveDto dto, ContentExt contentExt, Integer siteId, boolean isUpdate)
			throws GlobalException {
		contentExt = dto.initContentExt(dto, contentExt, siteId, isUpdate);
		List<CmsModelItem> cmsModelItem = cmsModelItemService.findByModelId(dto.getModelId());
		if (cmsModelItem != null && cmsModelItem.size() > 0) {
			List<String> modelItems = cmsModelItem.stream().map(CmsModelItem::getField).collect(Collectors.toList());
			if (modelItems.contains(CmsModelConstant.FIELD_SYS_CONTENT_SOURCE)) {
				JSONObject contentSourceJson = dto.getContentSourceId();
				if (contentSourceJson != null) {
					String sourceName = contentSourceJson.getString(ContentExt.SOURCE_NAME);
					String sourceLink = contentSourceJson.getString(ContentExt.SOURCE_LINK);
					ContentSource contentSource = contentSourceService.findBySourceName(sourceName);
					if (contentSource != null) {
						if (StringUtils.isNotBlank(sourceLink)) {
							if (!sourceLink.equals(contentSource.getSourceLink())) {
								contentSource.setSourceLink(sourceLink);
								contentSourceService.update(contentSource);
							}
						} else {
							contentSource.setSourceLink(sourceLink);
							contentSourceService.update(contentSource);
						}
						contentExt.setContentSourceId(contentSource.getId());
					} else {
						if (!StringUtils.isBlank(sourceName)) {
							ContentSource newSource = new ContentSource();
							newSource.setIsDefault(false);
							newSource.setSourceName(sourceName);
							newSource.setSourceLink(sourceLink);
							newSource.setIsOpenTarget(false);
							ContentSource bean = contentSourceService.save(newSource);
							contentExt.setContentSource(bean);
							contentExt.setContentSourceId(bean.getId());
						} else {
							contentExt.setContentSourceId(null);
						}
					}
				}
			} else {
				ContentSource contentSource = contentSourceService.defaultSource();
				contentExt.setContentSourceId(contentSource != null ? contentSource.getId() : null);
			}
		}
		return contentExt;
	}

	@Override
	public ContentFindVo findContent(Integer id, GlobalConfig globalConfig) throws GlobalException {
		Content content = dao.findByIdAndRecycleAndHasDeleted(id,false,false);
		ContentExt contentExt = content.getContentExt();
		ContentFindVo findVo = new ContentFindVo();
		CmsModel model = cmsModelService.getChannelOrContentModel(content.getModelId());
		List<CmsModelItem> modelItems = cmsModelItemService.findByModelId(content.getModelId());
		List<ContentAttr> contentAttrs = content.getContentAttrs();
		List<Integer> cmsOrgIds = new ArrayList<Integer>();
		content.setSite(cmsSiteService.findById(content.getSiteId()));
		for (ContentAttr attr : contentAttrs) {
			if (CmsModelConstant.TISSUE.equals(attr.getAttrType())) {
				cmsOrgIds.add(attr.getOrgId());
			}
		}
		Map<Integer, CmsOrg> cmsOrgMap = null;
		if (cmsOrgIds.size() > 0) {
			List<CmsOrg> cmsOrgs = cmsOrgService.findAllById(cmsOrgIds);
			if (cmsOrgs != null && cmsOrgs.size() > 0) {
				cmsOrgMap = cmsOrgs.stream().collect(Collectors.toMap(CmsOrg::getId, c -> c));
			}
		}
		if (content.getContentExt().getDocResourceId() != null) {
			content.getContentExt()
					.setDocResource(resourcesSpaceDataService.findById(content.getContentExt().getDocResourceId()));
		} else {
			content.getContentExt().setDocResource(null);
		}
		if (content.getContentExt().getContentSourceId() != null) {
			content.getContentExt()
					.setContentSource(contentSourceService.findById(content.getContentExt().getContentSourceId()));
		} else {
			content.getContentExt().setContentSource(null);
		}
		if (contentExt.getPicResId() != null) {
			contentExt.setReData(resourcesSpaceDataService.findById(contentExt.getPicResId()));
		} else {
			contentExt.setReData(null);
		}
		ContentCheckDetail detail = null;
		if (StringUtils.isNotBlank(content.getCheckMark())) {
			detail = checkDetailService.findByCheckMark(content.getCheckMark(),null);
		}
		findVo = findVo.spliceContentFindVo(content, globalConfig, model, modelItems,
				content.getContentExt().getContentSource(), cmsOrgMap,detail,checkDetailService.getCheckBanContent(detail, modelItems));
		/** 组装contentFindVo对象 */
		doFillActions(content, findVo, SystemContextUtils.getCoreUser());
		return findVo;
	}

	/**
	 * 自定义字段及其资源进行初始化和数据组装
	 *
	 * @param content
	 * @param json
	 * @param modelId
	 * @param id
	 * @param globalConfig
	 * @return
	 * @throws GlobalException
	 */
	private Content initAttr(Content content, JSONObject json, Integer modelId, Integer id, GlobalConfig globalConfig)
			throws GlobalException {
		List<ContentAttr> contentAttrs = contentAttrService.initContentAttr(json, modelId);
		if (contentAttrs.size() > 0) {
			// 部分默认字段的初始化
			contentAttrs = contentAttrService.initContentAttr(contentAttrs, id, globalConfig);
			List<ContentAttr> newContentAttrs = new ArrayList<ContentAttr>();
			for (ContentAttr contentAttr : contentAttrs) {
				ContentAttr attrBean = contentAttrService.save(contentAttr);
				contentAttrService.flush();
				List<ContentAttrRes> attrRes = contentAttr.getContentAttrRes();
				if (attrRes != null && attrRes.size() > 0) {
					for (ContentAttrRes attrRe : attrRes) {
						attrRe.setContentAttr(attrBean);
						attrRe.setContentAttrId(attrBean.getId());
					}
					List<ContentAttrRes> newAttrRes = contentAttrResService.saveAll(attrRes);
					contentAttrResService.flush();
					attrBean.setContentAttrRes(newAttrRes);
				}
				newContentAttrs.add(attrBean);
			}
			if (newContentAttrs.size() > 0) {
				content.setContentAttrs(newContentAttrs);
			}
		}
		return content;
	}
	
	private boolean getNeedYunCheck(ContentUpdateDto dto,Content content)throws  GlobalException{
		boolean isCheck = false;
		if (dto.getForce() != null) {
			if (!dto.getForce()) {
				if (ContentConstant.STATUS_PUBLISH == dto.getType()
						|| ContentConstant.STATUS_WAIT_PUBLISH == dto.getType()
						|| ContentConstant.STATUS_FLOWABLE == dto.getType()) {
					if (contentReviewService.checkAppIdOrPhone()) {
						boolean checkReview = false;
						if (ContentConstant.STATUS_SMART_AUDIT != content.getStatus()) {
							checkReview = contentReviewService.reviewContentCheck(content,dto.getChannelId(), content.getModelId());
						}
						if (checkReview) {
							dto.setType(STATUS_SMART_AUDIT);
							isCheck = true;
						}
					}
				}
			}
		} else {
			if (ContentConstant.STATUS_PUBLISH == dto.getType()
					|| ContentConstant.STATUS_WAIT_PUBLISH == dto.getType()
					|| ContentConstant.STATUS_FLOWABLE == dto.getType()) {
				if (contentReviewService.checkAppIdOrPhone()) {
					boolean checkReview = false;
					if (ContentConstant.STATUS_SMART_AUDIT != content.getStatus()) {
						checkReview = contentReviewService.reviewContentCheck(content,dto.getChannelId(), content.getModelId());
					}
					if (checkReview) {
						dto.setType(STATUS_SMART_AUDIT);
						isCheck = true;
					}
				}
			}
		}
		return isCheck;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Content update(ContentUpdateDto dto, HttpServletRequest request) throws GlobalException {
		Content content = super.findById(dto.getId());
		boolean isCheck = getNeedYunCheck(dto,content);
		dto.setModelId(content.getModelId());
		ContentExt contentExt = content.getContentExt();
		List<Map<String, Object>> mapList = this.preChange(content);
		final SpliceCheckUpdateDto oldDto = this.initSpliceCheckUpdateDto(content);
		CmsSite site = SystemContextUtils.getSite(request);
		GlobalConfig globalConfig = SystemContextUtils.getGlobalConfig(request);
		// 如果要修改的内容的状态为已发布，且站点 发布的内容不允许编辑，则抛出异常
		if (content.getStatus() == ContentConstant.STATUS_PUBLISH) {
			if (!site.getCmsSiteCfg().getContentCommitAllowUpdate()) {
				throw new GlobalException(new SystemExceptionInfo(
						ContentErrorCodeEnum.PUBLISHED_CONTENT_CANNOT_BE_EDITED.getDefaultMessage(),
						ContentErrorCodeEnum.PUBLISHED_CONTENT_CANNOT_BE_EDITED.getCode()));
			}
		}
		dto.setModelId(content.getModelId());
		Channel channel = channelService.findById(dto.getChannelId());
		// 校验传送过来的状态
		ContentInitUtils.checkStatus(content.getStatus(), dto.getType(), channel.getRealWorkflowId() != null, true);
		content = dto.initContent(dto, content, site, globalConfig, true, channel);
		contentTagService.deleteTagQuote(content.getContentTags(), site.getId(), null);
		List<ContentTag> tags = contentTagService.initTags(dto.getContentTag(), site.getId());
		content.setContentTags(tags);
		contentExt = this.spliceContentExt(dto, contentExt, site.getId(), true);
		contentExt.setContent(content);
		content.setContentExt(contentExt);
		content.setContentTxts(null);
		contentChannelService.update(content);
		contentAttrService.deleteByContent(content.getId());
		contentAttrService.flush();
		content = initAttr(content, dto.getJson(), dto.getModelId(), dto.getId(), globalConfig);
		
		if (isCheck) {
			content.setCheckMark(String.valueOf(snowFlake.nextId()));
		}
		
		Content bean = super.updateAll(content);
		super.flush();
		bean = super.findById(bean.getId());
		Map<String, String> txtMap = contentTxtService.initContentTxt(dto.getJson(), dto.getModelId(), dto, true);
		if (txtMap != null && txtMap.size() > 0) {
			List<ContentTxt> contentTxts = ContentInitUtils.toListTxt(txtMap);
			contentTxtService.deleteTxts(bean.getId());
			// 初始化contentTxts并执行新增操作
			bean.setContentTxts(contentTxtService.saveTxts(contentTxts, bean));
			if (bean.getStatus().equals(STATUS_PUBLISH)) {
				hotWordService.totalUserCount(bean.getChannelId(), contentTxts, site.getId());
			}
		} else {
			contentTxtService.deleteTxts(bean.getId());
			bean.setContentTxts(null);
		}
		contentTxtService.flush();
		if (Content.AUTOMATIC_SAVE_VERSION_TRUE.equals(site.getConfig().getContentSaveVersion())) {
			contentVersionService.save(txtMap, content.getId(), null);
		}
		contentChannelService.update(bean);
		contentChannelService.flush();
		this.initContentObject(bean);
		this.initContentExtObject(contentExt);
		this.afterChange(bean, mapList);
		SpliceCheckUpdateDto newDto = this.initSpliceCheckUpdateDto(bean);
		this.checkUpdate(oldDto, newDto, globalConfig, bean);
		contentExtService.update(contentExt);
		contentExtService.flush();
		
		if (isCheck) {
			List<Content> contents = new ArrayList<Content>();
			contents.add(bean);
			contentReviewService.reviewContents(contents,dto.getUserId());
		}
		
		return bean;
	}

	/**
	 * 初始化spliceCheckUpdateDto
	 */
	@Override
	public SpliceCheckUpdateDto initSpliceCheckUpdateDto(Content content) {
		SpliceCheckUpdateDto spDto = new SpliceCheckUpdateDto();
		Map<String, String> txtMap = ContentInitUtils.toMapTxt(content.getContentTxts());
		if (content.getChannelId() != null) {
			Channel channel = channelService.findById(content.getChannelId());
			content.setChannel(channel);
		}
		if (content.getContentSecretId() != null) {
			SysSecret secret = secretService.findById(content.getContentSecretId());
			content.setSecret(secret);
		}
		spDto.initSpliceCheckUpdateDto(content, spDto, txtMap);
		return spDto;
	}

	/**
	 * 获取到操作记录中的备注
	 */
	@Override
	public void checkUpdate(SpliceCheckUpdateDto oldUpdateDto, SpliceCheckUpdateDto newUpdateDto,
			GlobalConfig globalConfig, Content bean) throws GlobalException {
		contentRecordService.checkUpdate(oldUpdateDto, newUpdateDto, globalConfig, bean);
	}

	@Override
	public void submit(ContentUpdateDto dto, HttpServletRequest request, boolean contribute, Content content)
			throws GlobalException {
		if (!contribute) {
			Integer userId = SystemContextUtils.getUserId(request);
			dto.setUserId(userId);
			dto.setPublishUserId(userId);
			if (dto.getId() != null) {
				content = update(dto, request);
			} else {
				content = save(dto, SystemContextUtils.getSite(request));
			}
		}
		doSubmitFlow(content);
	}

	@Override
	public void copy(ContentCopyDto dto, HttpServletRequest request) throws GlobalException {
		GlobalConfig globalConfig = SystemContextUtils.getGlobalConfig(request);
		/** 从站点中取出默认设置 */
		CmsSiteConfig cmsSiteConfig = SystemContextUtils.getSite(request).getConfig();
		if (cmsSiteConfig.getTitleRepeat().equals(2)) {
			throw new GlobalException(new SystemExceptionInfo(
					ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT.getDefaultMessage(),
					ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT.getCode()));
		}
		Channel channel = channelService.findById(dto.getChannelId());
		/**
		 * 此处可以使用索引进行判断，但是可能一次性复制的内容过多比如200，那么每次都去查询一次索引不如直接将其全部查询出来，用大量的空间换取下次校验的时间
		 */
		List<String> contentTitles = new ArrayList<String>();
		// 校验栏目内标题是否允许重复
		if (cmsSiteConfig.getTitleRepeat().equals(3)) {
			List<Content> contents = dao.findByChannelIdAndRecycleAndHasDeleted(channel.getId(), false, false);
			if (contents != null && contents.size() > 0) {
				contentTitles = contents.stream().map(Content::getTitle).collect(Collectors.toList());
			}
		}
		for (Integer i = 0; i < dto.getIds().size(); i++) {
			Content content = super.findById(dto.getIds().get(i));
			if (contentTitles.size() > 0) {
				if (contentTitles.contains(content.getTitle())) {
					throw new GlobalException(new SystemExceptionInfo(
							ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT.getDefaultMessage(),
							ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT.getCode()));
				}
				contentTitles.add(content.getTitle());
			}
			SysSecret secret = null;
			if (content.getContentSecretId() != null) {
				secret = secretService.findById(content.getContentSecretId());
			}
			// 撰写管理员为空那么之前所有的逻辑都有问题了
			CoreUser user = coreUserService.findById(content.getUserId());
			Content newContent = ContentCopyDto.initCopyContent(content, globalConfig, channel, secret, user);
			ContentExt contentExt = content.getContentExt();
			ContentExt newContentExt = ContentInitUtils.initCopyContentExt(contentExt, content.getSiteId(),
					contentExt.getSueOrg(), contentExt.getSueYear(), contentExt.getContentSource(),
					contentExt.getReData());
			newContent.setContentExt(newContentExt);
			newContent.setContentTxts(null);
			List<ContentTag> newTags = new ArrayList<ContentTag>();
			// tags无需特意进行修改，因为进行set操作后会自动进行update操作
			List<ContentTag> contentTags = content.getContentTags();
			if (contentTags != null && contentTags.size() > 0) {
				for (ContentTag contentTag : contentTags) {
					contentTag.setRefCounter(contentTag.getRefCounter() + 1);
				}
				newTags = (List<ContentTag>) contentTagService.batchUpdateAll(contentTags);
			}
			super.flush();
			newContentExt.setContent(newContent);
			List<Content> contentCopys = new ArrayList<Content>();
			contentCopys.add(content);
			newContent.setContentCopys(contentCopys);
			Content bean = super.save(newContent);
			super.flush();
			bean.setContentTags(newTags);
			List<ContentTxt> contentTxt = ContentCopyDto.copyInitTxt(content.getContentTxts());
			List<ContentTxt> newContentTxt = contentTxtService.saveTxts(contentTxt, bean);
			bean.setContentTxts(newContentTxt);

			ContentChannel contentChannel = new ContentChannel(bean.getId(), bean.getChannelId(),
					ContentConstant.CONTENT_CREATE_TYPE_COPY, ContentConstant.STATUS_FIRST_DRAFT, false, bean, false);
			contentChannelService.save(contentChannel);
			List<ContentChannel> contentChannels = new ArrayList<ContentChannel>();
			contentChannels.add(contentChannel);
			bean.setContentChannels(contentChannels);
			List<ContentAttr> contentAttrs = contentAttrService.copyInitContentAttr(content.getContentAttrs(),
					bean.getId());
			bean = contentAttrService.copySaveContentAttr(contentAttrs, bean);
			if (Content.AUTOMATIC_SAVE_VERSION_TRUE
					.equals(SystemContextUtils.getSite(request).getConfig().getContentSaveVersion())) {
				contentVersionService.save(ContentInitUtils.toMapTxt(bean.getContentTxts()), bean.getId(), null);
			}
			ContentRecord contentRecord = new ContentRecord(bean.getChannelId(), bean.getUser().getUsername(), "复制",
					null, null, bean);
			contentRecordService.save(contentRecord);
			this.initContentObject(bean);
			this.initContentExtObject(bean.getContentExt());
			this.afterSave(bean);
		}
	}

	@Override
	public boolean checkTitle(String title, Integer channelId) {
		Integer siteId = SystemContextUtils.getSiteId(RequestUtils.getHttpServletRequest());
		Long contentIdConunt = contentLuceneService.searchCount(title, SearchPosition.title, channelId, siteId, true,
				ContentConstant.getNotAllowRepeatStatus());
		if (contentIdConunt > 0) {
			return true;
		}
		return false;
	}

	@Override
	public ResponseInfo recoveryVersion(ContentVersion version, Integer contentId) throws GlobalException {
		Content content = super.findById(contentId);
		if (content == null) {
			return new ResponseInfo();
		}
		if (!this.validType(CmsDataPerm.OPE_CONTENT_EDIT, null, content)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}

		JSONObject jsonTxt = version.getJsonTxt();
		List<ContentTxt> contentTxts = new ArrayList<ContentTxt>();
		List<CmsModelItem> cmsModelItem = cmsModelItemService.findByModelIdAndDataType(content.getModelId(),
				CmsModelConstant.CONTENT_TXT);
		for (CmsModelItem modelItem : cmsModelItem) {
			String field = modelItem.getField();
			String txt = jsonTxt.getString(field);
			if (StringUtils.isNotBlank(txt)) {
				ContentTxt contentTxt = new ContentTxt();
				contentTxt.setAttrKey(field);
				contentTxt.setAttrTxt(txt);
				contentTxts.add(contentTxt);
			}
		}
		if (contentTxts.size() > 0) {
			contentTxtService.deleteTxts(content.getId());
			// 初始化contentTxts并执行新增操作
			content.setContentTxts(contentTxtService.saveTxts(contentTxts, content));
		}
		return new ResponseInfo();
	}

	@Override
	public Boolean validType(Short opration, List<Content> contents, Content content) {
		if (contents == null) {
			contents = new ArrayList<Content>();
		}
		if (content != null) {
			contents.add(content);
		}
		return this.validType(opration, contents);
	}

	@Override
	public Boolean validType(Short opration, Integer channId) {
		CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
		// 判断类型
		List<Short> oprations = user.getContentOperatorByChannelId(channId);
		// 有权限
		if (oprations.contains(opration)) {
			return true;
		}
		// 没有权限
		return false;
	}

	@Override
	public List<Content> findByChannels(Integer[] channelIds) {
		return dao.findByChannelIdInAndHasDeleted(channelIds, false);
	}

	@Override
	public void pushSites(ContentPushSitesDto dto, HttpServletRequest request) throws GlobalException {
		List<Content> contents = dto.getContents();
		Channel channel = dto.getChannel();
		GlobalConfig globalConfig = SystemContextUtils.getGlobalConfig(request);
		List<CmsModel> models = cmsModelService.findList(CmsModel.CONTENT_TYPE, dto.getSiteId());
		for (Content content : contents) {
			Content newContent = ContentPushSitesDto.initContent(content, globalConfig, channel, dto.getSiteId(),
					models, content.getSecret(), content.getUser());
			ContentExt contentExt = content.getContentExt();
			ContentExt newContentExt = ContentInitUtils.initCopyContentExt(contentExt, content.getSiteId(),
					contentExt.getSueOrg(), contentExt.getSueYear(), contentExt.getContentSource(),
					contentExt.getReData());
			newContent.setContentExt(newContentExt);
			List<String> itemFiles = cmsModelItemService.findByModelId(newContent.getModelId()).stream()
					.map(CmsModelItem::getField).collect(Collectors.toList());
			if (itemFiles.contains(CmsModelConstant.FIELD_SYS_CONTENT_CONTENTTAG)) {
				// tags无需特意进行修改，因为进行set操作后会自动进行update操作
				List<ContentTag> newTags = new ArrayList<ContentTag>();
				List<ContentTag> contentTags = content.getContentTags();
				if (contentTags != null && contentTags.size() > 0) {
					for (ContentTag contentTag : contentTags) {
						contentTag.setRefCounter(contentTag.getRefCounter() + 1);
					}
					newTags = (List<ContentTag>) contentTagService.batchUpdateAll(contentTags);
				}
				newContent.setContentTags(newTags);
			}

			newContentExt.setContent(newContent);
			Content bean = super.save(newContent);
			super.flush();
			List<ContentTxt> contentTxt = ContentCopyDto.copyInitTxt(content.getContentTxts());
			List<ContentTxt> newTxt = new ArrayList<ContentTxt>();
			for (ContentTxt txt : contentTxt) {
				if (itemFiles.contains(txt.getAttrKey())) {
					newTxt.add(txt);
				}
			}
			List<ContentTxt> newContentTxt = contentTxtService.saveTxts(newTxt, bean);
			bean.setContentTxts(newContentTxt);

			ContentChannel contentChannel = new ContentChannel(bean.getId(), bean.getChannelId(),
					ContentConstant.CONTENT_CREATE_TYPE_SITE_PUSH, ContentConstant.STATUS_FIRST_DRAFT, false, bean,
					false);
			contentChannelService.save(contentChannel);
			List<ContentChannel> contentChannels = new ArrayList<ContentChannel>();
			contentChannels.add(contentChannel);
			bean.setContentChannels(contentChannels);
			List<ContentAttr> contentAttrs = contentAttrService.pushSiteInitContentAttr(content.getContentAttrs(),
					bean.getId(), itemFiles, globalConfig);
			bean = contentAttrService.copySaveContentAttr(contentAttrs, bean);
			if (Content.AUTOMATIC_SAVE_VERSION_TRUE
					.equals(SystemContextUtils.getSite(request).getConfig().getContentSaveVersion())) {
				contentVersionService.save(ContentInitUtils.toMapTxt(bean.getContentTxts()), bean.getId(), null);
			}
			ContentRecord contentRecord = new ContentRecord(bean.getChannelId(), bean.getUser().getUsername(), "推送",
					null, null, bean);
			contentRecordService.save(contentRecord);
			this.initContentObject(bean);
			this.initContentExtObject(bean.getContentExt());
			this.afterSave(bean);
		}
	}

	@Override
	public void exportContent(Integer channelId, Integer modelId, Integer collectContentId, Integer userId)
			throws GlobalException {
		CollectContent collectContent = collectContentService.findById(collectContentId);
		Channel channel = channelService.findById(channelId);
		if (channel == null || channel.getRecycle()) {
			LOGGER.warn("传入的栏目id错误，传入的栏目id为空或者栏目已经被删除或者加入回收站");
			throw new GlobalException(
					new SystemExceptionInfo(ChannelErrorCodeEnum.CHANNEL_ID_PASSED_ERROR.getDefaultMessage(),
							ChannelErrorCodeEnum.CHANNEL_ID_PASSED_ERROR.getCode()));
		}

		CmsSite site = cmsSiteService.findById(channel.getSiteId());
		Content content = new Content();
		content.setModelId(modelId);
		// 如果userId，没有传送过来则截止任务
		if (userId == null) {
			return;
		}
		content.setUserId(userId);
		JSONObject json = JSONObject.parseObject(collectContent.getContentValue());
		// TODO
		// content = contentDtoService.initContent(content, channel, json,
		// site.getCmsSiteCfg(), site.getId());
		ContentExt contentExt = new ContentExt();
		contentExt = contentExtService.initContentExt(contentExt, site.getId(), json, channelId, modelId);
		content.setContentExt(contentExt);
		contentExt.setContent(content);
		Content bean = super.save(content);
		super.flush();
		String attrTxt = json.getString(ContentConstant.ITEM_CONTXT_NAME);
		Map<String, String> txtMap = new HashMap<String, String>();
		txtMap.put(CmsModelConstant.CONTENT_TXT, attrTxt);
		if (Content.AUTOMATIC_SAVE_VERSION_TRUE.equals(site.getConfig().getContentSaveVersion())) {
			// 此处Map无需处理为空的情况在其具体方法中已经处理了
			contentVersionService.save(txtMap, bean.getId(), null);
		}
		ContentChannel contentChannel = new ContentChannel(bean.getId(), bean.getChannelId(),
				ContentConstant.CONTENT_CREATE_TYPE_SITE_COLLECT, bean.getStatus(), false, bean, false);
		contentChannelService.save(contentChannel);
		List<ContentChannel> contentChannels = new ArrayList<ContentChannel>();
		contentChannels.add(contentChannel);
		bean.setContentChannels(contentChannels);
		if (StringUtils.isNotBlank(CmsModelConstant.CONTENT_TXT)) {
			ContentTxt contentTxt = new ContentTxt(CmsModelConstant.CONTENT_TXT, attrTxt, bean.getId());
			contentTxt.setContent(bean);
			contentTxtService.save(contentTxt);
		}
	}

	@Override
	public ResponseInfo preview(WechatViewDto dto) throws GlobalException {
		Integer number = 1;
		List<WechatPushVo> vos = new ArrayList<WechatPushVo>(8);
		if (!dto.getContentIds().isEmpty()) {
			if (dto.getContentIds().size() > 8) {
				return new ResponseInfo(RPCErrorCodeEnum.NO_MORE_THAN_PUSHED.getDefaultMessage(),
						RPCErrorCodeEnum.NO_MORE_THAN_PUSHED.getCode(), false);
			}
			// 判断今日群发次数
			List<WechatSend> send = sendService.listWechatSend(Arrays.asList(dto.getAppid()),
					MyDateUtils.getStartDate(new Date()), MyDateUtils.getFinallyDate(new Date()));
			// 判断月群发次数
			Boolean flag = sendService.service(dto.getAppid(), new Date());
			if (!send.isEmpty() || !flag) {
				number = 0;
			}
			List<Content> contents = super.findAllById(dto.getContentIds());
			for (Content content : contents) {
				if (!content.getWechatPushContentAble()) {
					return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
							UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
				}
				WechatPushVo vo = new WechatPushVo();
				StringBuilder builder = new StringBuilder();
				List<ContentTxt> txts = content.getContentTxts();
				for (ContentTxt contentTxt : txts) {
					builder.append(contentTxt.getAttrTxt());
				}
				vo.setSengNumber(number);
				vo.setContent(builder.toString());
				// 设置封面
				ResourcesSpaceData data = content.getContentExt().getReData();
				if (data != null) {
					vo.setPicId(data.getId());
					vo.setCover(data.getUrl());
				}
				vo.setTitle(content.getTitle());
				vo.setAuthor(content.getContentExt().getAuthor());
				// 原创链接
				vo.setSourceUrl(content.getUrlWhole());
				vo.setAppId(dto.getAppid());
				vos.add(vo);
			}
		}
		return new ResponseInfo(vos);
	}

	@Override
	public ResponseInfo push(WechatPushDto dto) throws Exception {
		List<WechatPushVo> vos = dto.getVo();
		if (!vos.isEmpty()) {
			AddNewsRequest addNewsRequest = new AddNewsRequest();
			List<SaveArticles> articles = new ArrayList<SaveArticles>(8);
			// 构造新增素材
			for (WechatPushVo vo : vos) {
				SaveArticles art = new SaveArticles();
				// 封面素材ID
				art.setThumbMediaId(material(dto.getAppid(), vo.getPicId()));
				art.setTitle(vo.getTitle());
				art.setAuthor(vo.getAuthor());
				art.setContent(vo.getContent());
				art.setContentSourceUrl(vo.getSourceUrl());
				art.setShowCoverPic(0);
				// 判断留言
				if (dto.getMessage() == WechatPushDto.MESSAGE_1) {
					art.setNeedOpenComment(1);
					art.setOnlyFansCanComment(0);
				} else if (dto.getMessage() == WechatPushDto.MESSAGE_2) {
					art.setNeedOpenComment(1);
					art.setOnlyFansCanComment(1);
				} else {
					art.setNeedOpenComment(0);
				}
				articles.add(art);
			}
			addNewsRequest.setArticles(articles);
			// 上传素材
			WechatMaterial material = wechatMaterialService.saveNews(addNewsRequest, dto.getAppid());
			// 构造群发对象
			WechatSend wechatSend = new WechatSend();
			wechatSend.setAppId(dto.getAppid());
			wechatSend.setMaterialId(material.getId());
			// 发送对象
			if (dto.getTagId() != null) {
				wechatSend.setTagId(dto.getTagId());
			}
			if (dto.getType() == WechatPushDto.TYPE_1) {
				// 立即发送
				wechatSend.setType(WechatConstants.SEND_TYPE_NOW);
				return wechatSendService.send(wechatSend);
			} else {
				// 定时发送
				wechatSend.setSendDate(dto.getSendDate());
				wechatSend.setSendHour(dto.getSendHour());
				wechatSend.setSendMinute(dto.getSendMinute());
				return wechatSendService.saveWechatSend(wechatSend);
			}
		}
		return new ResponseInfo();
	}

	/**
	 * 得到微信素材ID
	 **/
	public String material(String appId, Integer picId) throws Exception {
		if (picId == null) {
			throw new GlobalException(RPCErrorCodeEnum.PUSH_ERROR_IMAGE_NOT_NULL);
		}
		ResourcesSpaceData data = resourcesSpaceDataService.findById(picId);
		if (data == null) {
			throw new GlobalException(RPCErrorCodeEnum.PUSH_ERROR_IMAGE_NOT_NULL);
		}
		File file = resourcesSpaceDataService.getFile(data);
		AddMaterialRequest addMaterialRequest = new AddMaterialRequest();
		addMaterialRequest.setFileName(data.getAlias());
		addMaterialRequest.setType("image");
		WechatMaterial material = wechatMaterialService.saveMaterial(addMaterialRequest, appId, file);
		// 执行业务之后删除临时文件
		file.delete();
		return material.getMediaId();
	}

	@Override
	public void afterChannelChange(Channel c) throws GlobalException {
	}

	@Override
	public void afterChannelRecycle(List<Channel> channels) throws GlobalException {
		// 栏目加入回收站后对应的所有内容也全部加入回收站
		Integer[] cids = new Integer[channels.size()];
		Channel.fetchIds(channels).toArray(cids);
		List<Content> contents = dao.findByChannelIdInAndHasDeleted(cids, false);
		if (contents.size() > 0) {
			for (Content content : contents) {
				content.setRecycle(true);
			}
			super.batchUpdate(contents);
			List<Integer> contentIds = contents.stream().map(Content::getId).collect(Collectors.toList());
			for (ContentListener listener : listenerList) {
				listener.afterContentRecycle(contentIds);
			}
		}
	}

	@Override
	public String docImport(MultipartFile file, Integer type) throws Exception {
		// 得到文件名称
		String fileName = FileUtils.getFileName(file.getOriginalFilename());
		// 得到文件后缀,带.
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		// 创建临时文件，接收上传的文件，后缀写死
		File changeFile = File.createTempFile(fileName + StrUtils.getRandStr(6), ext);
		file.transferTo(changeFile);
		// 后缀写死
		File tempFile = File.createTempFile(fileName + StrUtils.getRandStr(6), ".html");
		String html = openOfficeConverter.convertToHtmlString(changeFile, tempFile);
		// 替换
		html = replaceImg(html, tempFile.getParent());
		// 删除临时文件
		changeFile.delete();
		tempFile.delete();
		if (type.equals(ContentConstant.IMPORT_TYPE_2)) {
			// 清除格式
			return Doc2Html.clearStyle(html);
		} else if (type.equals(ContentConstant.IMPORT_TYPE_3)) {
			// 仅导入文字
			return Doc2Html.onlyWords(html);
		} else {
			// 导入方式，直接导入（不做任何处理）
			return html;
		}
	}

	/**
	 * 替换文件路径以及上传
	 *
	 * @param html html片段
	 * @throws IOException     异常
	 * @throws GlobalException 异常
	 * @Title: replaceImg
	 */
	public String replaceImg(String html, String path) throws IOException, GlobalException {
		List<ResourcesSpaceData> datas = new ArrayList<ResourcesSpaceData>(10);
		CmsSite site = SystemContextUtils.getSite(RequestUtils.getHttpServletRequest());
		Document doc = Jsoup.parse(html);
		// 获取 带有src属性的img元素
		Elements imgTags = doc.select("img[src]");
		for (org.jsoup.nodes.Element element : imgTags) {
			// 获取src的绝对路径
			String src = path + File.separatorChar + URLDecoder.decode(element.attr("src"), "UTF-8");
			File file = new File(src);
			// 上传文件
			UploadResult result = uploadService.doUpload(file, null, null, ResourceType.IMAGE, site);
			// 应前端需求图片地址给绝对地址
			if (result.getFileUrl().contains("http")) {
				element.attr("src", result.getFileUrl());
			} else {
				element.attr("src", site.getUrl() + result.getFileUrl().substring(1));
			}
			ResourcesSpaceData source = new ResourcesSpaceData();
			source.init();
			source.setUrl(result.getFileUrl());
			source.setAlias(file.getName());
			source.setResourceType(ResourceType.RESOURCE_TYPE_IMAGE);
			datas.add(source);
			// 删除本地的图片
			file.delete();
		}
		resourcesSpaceDataService.saveAll(datas);
		return doc.html().toString();
	}

	@Override
	public void restore(List<Integer> contentIds, Integer siteId, List<Integer> channelIds) throws GlobalException {
		// 判断系统设置是否允许标题重复
		Integer type = cmsSiteService.findById(siteId).getCmsSiteCfg().getTitleRepeat();
		List<Content> contents = new ArrayList<Content>(10);
		List<ContentChannel> contentChannelList = new ArrayList<ContentChannel>(10);
		List<Content> list = super.findAllById(contentIds);
		// 判断内容所属的栏目是不是底层栏目，不是的话直接报错,只有是底层栏目，都还原
		for (Content content : list) {
			List<Map<String, Object>> mapList = this.preChange(content);
			// 得到栏目内容集合（包含原始栏目和引用栏目）
			List<ContentChannel> contentChannels = content.getContentChannels();
			// 判断是否有引用，根据ContentChannel的数量判断
			if (contentChannels.size() > 1) {
				// 判断原来的栏目和引用的栏目是否底层栏目,是的话就还原
				for (ContentChannel contentChannel : contentChannels) {
					// 如果没有子栏目，就是底层栏目,需要还原引用内容
					// 以及该栏目不是回收站，已删除状态
					Channel channel = contentChannel.getChannel();
					// 如果映射到的那个栏目也即将被还原，则只需要判断其是否是底层栏目即可
					if (!channelIds.contains(channel.getId())) {
						if (channel.getRecycle()) {
							continue;
						}
					}
					Boolean flag = channel.getIsBottom();
					if (flag) {
						contentChannel.setRecycle(false);
						contentChannelList.add(contentChannel);
					}
				}
			} else {
				// 没有引用，需要判断所属栏目是否是底层栏目
				if (!content.getChannel().getIsBottom()) {
					throw new GlobalException(ContentErrorCodeEnum.CHANNEL_HAS_CHILD_ERROR);
				}
				if (contentChannels != null && contentChannels.size() > 0) {
					ContentChannel c = contentChannels.get(0);
					c.setRecycle(false);
					contentChannelList.add(c);
				}
			}
			check(type, siteId, content.getTitle(), content.getChannelId());
			content.setRecycle(false);
			if (STATUS_FLOWABLE == content.getStatus()) {
				content.setStatus(ContentConstant.STATUS_FIRST_DRAFT);
			}
			if (STATUS_WAIT_PUBLISH == content.getStatus()) {
				content.setStatus(ContentConstant.STATUS_FIRST_DRAFT);
			}
			contents.add(content);
			update(content);
			afterChange(content, mapList);
		}
		contentChannelService.batchUpdate(contentChannelList);
		super.batchUpdate(contents);
	}

	/**
	 * 检验数据
	 **/
	public void check(Integer type, Integer siteId, String title, Integer channelId) throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		// 2站点内不允许重复
		if (type == 2) {
			params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			params.put("EQ_title_String", new String[] { title });
			params.put("EQ_recycle_Boolean", new String[] { "false" });
			Long contentList = super.count(params);
			;
			if (contentList > 0) {
				throw new GlobalException(ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT);
			}
		} else if (type == 3) {
			// 3同一栏目下不允许重复
			params.put("EQ_channelId_Integer", new String[] { channelId.toString() });
			params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			params.put("EQ_title_String", new String[] { title });
			params.put("EQ_recycle_Boolean", new String[] { "false" });
			Long contentList = super.count(params);
			;
			if (contentList > 0) {
				throw new GlobalException(ContentErrorCodeEnum.CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT);
			}
		}
	}

	@Override
	public void deleteContent(List<Integer> contentIds) throws GlobalException {
		List<Content> contents = super.findAllById(contentIds);
		for (Content content : contents) {
			afterDelete(content);
			contentTagService.deleteTagQuote(null, content.getSiteId(), content);
		}
		super.delete(contentIds.toArray(new Integer[contentIds.size()]));
	}

	/**
	 * 提交工作流模板方法
	 *
	 * @param content 内容对象
	 * @Title: doSubmitFlow
	 * @return: void
	 */
	protected void doSubmitFlow(Content content) throws GlobalException {

	}

	/**
	 * 填充内容当前支持的动作 模板方法
	 *
	 * @param content 内容对象
	 * @param vo      详情视图vo对象
	 * @Title: doFillActions
	 * @return: void
	 */
	protected void doFillActions(Content content, ContentFindVo vo, CoreUser user) {

	}

	@Override
	public Content initContentObject(Content content) throws GlobalException {
		content.setSite(cmsSiteService.findById(content.getSiteId()));
		if (content.getChannelId() != null) {
			content.setChannel(channelService.findById(content.getChannelId()));
		}
		if (content.getUserId() != null) {
			content.setUser(coreUserService.findById(content.getUserId()));
		}
		if (content.getPublishUserId() != null) {
			content.setPublishUser(coreUserService.findById(content.getPublishUserId()));
		} else {
			content.setPublishUser(null);
		}
		return content;
	}

	@Override
	public ContentExt initContentExtObject(ContentExt contentExt) throws GlobalException {
		if (contentExt.getDocResourceId() != null) {
			contentExt.setDocResource(resourcesSpaceDataService.findById(contentExt.getDocResourceId()));
		} else {
			contentExt.setDocResource(null);
		}
		if (contentExt.getContentSourceId() != null) {
			contentExt.setContentSource(contentSourceService.findById(contentExt.getContentSourceId()));
		} else {
			contentExt.setContentSource(null);
		}
		if (contentExt.getPicResId() != null) {
			contentExt.setReData(resourcesSpaceDataService.findById(contentExt.getPicResId()));
		} else {
			contentExt.setReData(null);
		}
		if (contentExt.getIssueOrg() != null) {
			contentExt.setSueOrg(contentMarkService.findById(contentExt.getIssueOrg()));
		} else {
			contentExt.setSueOrg(null);
		}
		if (contentExt.getIssueYear() != null) {
			contentExt.setSueYear(contentMarkService.findById(contentExt.getIssueYear()));
		} else {
			contentExt.setSueYear(null);
		}
		return contentExt;
	}
	
	// 获取内容标识
	private SnowFlake snowFlake = new SnowFlake(SnowFlake.LONG_STR_CODE);
	
	@Autowired
	private CmsModelItemService cmsModelItemService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ContentVersionService contentVersionService;
	@Autowired
	private ContentChannelService contentChannelService;
	@Autowired
	private ContentAttrService contentAttrService;
	@Autowired
	@Lazy
	private ContentRecordService contentRecordService;
	@Autowired
	private ContentTagService contentTagService;
	@Autowired
	private ContentTypeService contentTypeService;
	@Autowired
	private List<ContentListener> listenerList;
	@Autowired
	private ContentTxtService contentTxtService;
	@Autowired
	private ContentExtService contentExtService;
	@Autowired
	private CollectContentService collectContentService;
	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private WechatMaterialService wechatMaterialService;
	@Autowired
	private WechatSendService wechatSendService;
	@Autowired
	private ResourcesSpaceDataService resourcesSpaceDataService;
	@Autowired
	private OpenOfficeConverter openOfficeConverter;
	@Autowired
	private ContentSourceService contentSourceService;
	@Autowired
	private ContentMarkService contentMarkService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private CmsOrgService cmsOrgService;
	@Autowired
	private SysSecretService secretService;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private FlowService flowService;
	@Autowired
	private ContentLuceneService contentLuceneService;
	@Autowired
	private ContentAttrResService contentAttrResService;
	@Autowired
	private WechatSendService sendService;
	@Autowired
	private UploadService uploadService;// doUpload
	@Autowired
	private SysHotWordService hotWordService;
	@Autowired
	@Lazy
	private ContentReviewService contentReviewService;
	@Autowired
	private ContentCheckDetailService checkDetailService;
}