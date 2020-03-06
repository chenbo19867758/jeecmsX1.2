package com.jeecms.content.service.impl;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.constants.ContentConstant.ContentOperation;
import com.jeecms.content.dao.ContentDao;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentSearchDto;
import com.jeecms.content.domain.vo.ContentVo;
import com.jeecms.content.domain.vo.ContentVo.TypeOperations;
import com.jeecms.content.service.ContentGetService;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.ContentType;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.ContentTypeService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 内容查询service实现类
 * 
 * @author: chenming
 * @date: 2019年8月5日 上午10:39:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "workflow.support", havingValue = "local", matchIfMissing = true)
public class ContentGetServiceImpl implements ContentGetService {

        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public Page<ContentVo> getPage(ContentSearchDto dto, Pageable pageable) throws GlobalException {
        	CmsSiteConfig config = cmsSiteService.findById(dto.getSiteId()).getCmsSiteCfg();
                // 查询是否开启内容密级
                GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
                if (!attr.getOpenContentSecurity()) {
                        dto.setContentSecretIds(null);
                }
                // 查询发文字号是否开启
                if (!attr.getOpenContentIssue()) {
                        dto.setIssueNum(null);
                        dto.setIssueOrg(null);
                        dto.setIssueYear(null);
                }
                Page<ContentVo> contentList = dao.getPages(dto, pageable);
                List<ContentVo> contents = new ArrayList<ContentVo>(10);
                List<ContentVo> vos = contentList.getContent();
                if (!vos.isEmpty()) {
                        for (ContentVo contentVo : vos) {
                                // 判断是否引用内容
                                if (contentVo.getQuote()) {
                                        contents.add(getRef(contentVo));
                                } else {
                                        contents.add(getVo(contentVo, config));
                                }
                        }
                }
                /** 在工作流中真实填充，此处只是模板方法占用，误删 */
                warpData(vos);
                return new PageImpl<ContentVo>(contents, pageable, contentList.getTotalElements());
        }

        /**
         * 增强包装模板方法，给子类重写
         * 
         * @Title: warpData
         * @return: void
         */
        protected void warpData(List<ContentVo> vos) throws GlobalException {

        }

        /** 给引用内容赋值 **/
        protected ContentVo getRef(ContentVo vo) {
                // 分配权限
                Map<String, Boolean> mapAll = new HashMap<String, Boolean>(16);
                for (ContentOperation string : ContentOperation.values()) {
                        // 只给浏览预览权限,引用权限
                        Boolean flag = string.name().equals(ContentOperation.PREVIEW.name())
                                        || string.name().equals(ContentOperation.QUOTE.name());
                        if (flag) {
                                mapAll.put(string.name(), true);
                        } else {
                                mapAll.put(string.name(), false);
                        }
                }
                // 只有发布的内容才可以浏览
                if (vo.getCmsContent().getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
                        mapAll.put(ContentOperation.VIEW.name(), true);
                }
                Channel channel = channelService.findById(vo.getChannelId());
                vo.setQuoteChannelName(channel != null ? channel.getName() : "");
                vo.setOperations(mapAll);
                vo.setTypes(null);
                return vo;
        }

        /**
         * 分配权限 @Title: getVo @param channelId 栏目ID @param contents 内容列表 @return
         */
        protected ContentVo getVo(ContentVo vo, CmsSiteConfig config) {
                Content content = vo.getCmsContent();
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                // 不为空则根据栏目ID取得该栏目的内容权限，做对比；
                List<Short> opration = user.getContentOperatorByChannelId(vo.getChannelId());
                // 分配权限
                Map<String, Boolean> mapAll = new HashMap<String, Boolean>(16);
                for (ContentOperation string : ContentOperation.values()) {
                        mapAll.put(string.name(), false);
                }
                // 如果不是流转中
                if (!content.getStatus().equals(ContentConstant.STATUS_FLOWABLE)) {
                        // 如果拥有查看权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_VIEW)) {
                                // 只有发布的内容才可以浏览
                                if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
                                        mapAll.put(ContentOperation.VIEW.name(), true);
                                }
                                mapAll.put(ContentOperation.PREVIEW.name(), true);
                        }
                        // 如果拥有内容创建权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_CREATE)) {
                                mapAll.put(ContentOperation.ADD.name(), true);
                        }
                        // 如果拥有内容修改权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_EDIT)) {
                                // 只有发布的内容才可以浏览
                                if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
                                        mapAll.put(ContentOperation.VIEW.name(), true);
                                }
                                mapAll.put(ContentOperation.UPDATE.name(), true);
                        }
                        // 拥有删除权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_DEL)) {
                                mapAll.put(ContentOperation.DELETE.name(), true);
                        }
                        // 拥有引用权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_QUOTE)) {
                                mapAll.put(ContentOperation.QUOTE.name(), true);
                        }
			// 拥有发布权限
			if (opration.contains(CmsDataPerm.OPE_CONTENT_PUBLISH)) {
				// 判断站点设置是否允许内容发布编辑， 归档
				Boolean flag = config.getContentCommitAllowUpdate();
				// 判断是否开启工作流
				Integer work = content.getChannel().getRealWorkflowId();
				// 如果工作流不存在，则根据内容状态来给发布按钮
				if (work != null) {
					// 审核，下线状态可以发布
					if (content.getStatus().equals(ContentConstant.STATUS_WAIT_PUBLISH)
						|| content.getStatus().equals(ContentConstant.STATUS_NOSHOWING)) {
						mapAll.put(ContentOperation.PUBLISH.name(), true);
					} else if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
						// 发布状态需要判断站点配置，已发布的内容是否允许编辑
						if (flag) {
							mapAll.put(ContentOperation.PUBLISH.name(), true);
						}
					}
				} else {
					// 草稿，初稿，已审核，下线状态可以发布
					if (content.getStatus().equals(ContentConstant.STATUS_DRAFT)
						|| content.getStatus().equals(ContentConstant.STATUS_FIRST_DRAFT)
						|| content.getStatus().equals(ContentConstant.STATUS_NOSHOWING)
						|| content.getStatus().equals(ContentConstant.STATUS_WAIT_PUBLISH)) {
						mapAll.put(ContentOperation.PUBLISH.name(), true);
					} else if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
						// 发布状态需要判断站点配置，已发布的内容是否允许编辑
						if (flag) {
							mapAll.put(ContentOperation.PUBLISH.name(), true);
						}
					}
				}
				// 只有已发布的内容才可以下线
				if (content.getStatus().equals(ContentConstant.STATUS_PUBLISH)) {
					mapAll.put(ContentOperation.OFFLINE.name(), true);
				}
				
			}
			//拥有归档权限
			if (opration.contains(CmsDataPerm.OPE_CONTENT_FILE)) {
				mapAll.put(ContentOperation.PIGEONHOLE.name(), true);
			}
			//排序权限
			if (opration.contains(CmsDataPerm.OPE_CONTENT_ORDER)) {
				mapAll.put(ContentOperation.SORT.name(), true);
			}
			
                        // 拥有站群推送权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_SITE_PUSH)) {
                                mapAll.put(ContentOperation.SITE.name(), true);
                        }
                        // 拥有微博推送权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_WEIBO_PUSH)) {
                                mapAll.put(ContentOperation.SINA.name(), true);
                        }
                        // 拥有微信推送权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_WECHAT_PUSH)) {
                                mapAll.put(ContentOperation.WECHAT.name(), true);
                        }
                        // 拥有复制权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_COPY)) {
                                mapAll.put(ContentOperation.COPY.name(), true);
                        }
                        // 拥有移动权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_MOVE)) {
                                mapAll.put(ContentOperation.MOVE.name(), true);
                        }
                        // 拥有置顶权限
                        if (opration.contains(CmsDataPerm.OPE_CONTENT_TOP)) {
                                mapAll.put(ContentOperation.STICK.name(), true);
                        }
		} else {
			// 如果拥有查看权限
			if (opration.contains(CmsDataPerm.OPE_CONTENT_VIEW)) {
				mapAll.put(ContentOperation.PREVIEW.name(), true);
			}
			// 拥有删除权限
			if (opration.contains(CmsDataPerm.OPE_CONTENT_DEL)) {
				mapAll.put(ContentOperation.DELETE.name(), true);
			}
		}
                // 拥有内容类型权限
                List<ContentType> types = contentTypeService.getList(null, null, true);
                List<TypeOperations> operations = new ArrayList<TypeOperations>(10);
                ContentVo v = new ContentVo();
                if (opration.contains(CmsDataPerm.OPE_CONTENT_TYPE)) {
                        for (ContentType contentType : types) {
                                TypeOperations op = v.new TypeOperations();
                                op.setId(contentType.getId());
                                op.setTypeName(contentType.getTypeName());
                                op.setOperation(true);
                                operations.add(op);
                        }
                        mapAll.put(ContentOperation.CONTENTTYPE.name(), true); 
                } else {
                        for (ContentType contentType : types) {
                                TypeOperations op = v.new TypeOperations();
                                op.setId(contentType.getId());
                                op.setTypeName(contentType.getTypeName());
                                op.setOperation(false);
                                operations.add(op);
                        }
                }
                // 给原生的内容添加权限
                vo.setOperations(mapAll);
                vo.setTypes(operations);
                return vo;
        }

        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public Page<Content> getPages(ContentSearchDto dto, Pageable pageable) {
                return dao.getPage(dto, pageable);
        }

        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public List<Content> getList(ContentSearchDto dto, Paginable paginable) {
                return dao.getList(dto, paginable);
        }

        /**
         * 获取栏目下内容数
         * 
         * @Title: getContentCountByChannel
         * @param channel
         *                栏目对象
         * @param containChild
         *                是否包含子栏目
         * @return: Integer
         */
        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public Integer getCountByChannel(Channel channel, boolean containChild) {
                Integer[] channelIds = null;
                if (containChild) {
                        // channelIds = channel.getChildAllIdArray();
                } else {
                        channelIds = new Integer[] { channel.getId() };
                }
                return (int) getCount(new ContentSearchDto(channel.getSiteId(), channelIds,
                                ContentConstant.ORDER_TYPE_RELEASE_TIME_DESC,
                                Arrays.asList(ContentConstant.STATUS_PUBLISH), false));
        }

        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public long getCount(ContentSearchDto dto) {
                return dao.getCount(dto);
        }

        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public long getReleaseSum(Date beginTime, Date endTime, Integer siteId) {
                return dao.getSum(beginTime, endTime, siteId, ContentConstant.STATUS_PUBLISH, null);
        }

        @Override
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public long getSubmissionSum(Date beginTime, Date endTime, Integer siteId) {
                return dao.getSum(beginTime, endTime, siteId, null, ContentConstant.CONTENT_CREATE_TYPE_CONTRIBUTE);
        }

        @Autowired
        private GlobalConfigService globalConfigService;
        @Autowired
        private ContentTypeService contentTypeService;
        @Autowired
        private ChannelService channelService;
        @Autowired
        private ContentDao dao;
    	@Autowired
    	private CmsSiteService cmsSiteService;
}
