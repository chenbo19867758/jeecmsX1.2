package com.jeecms.channel.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.constants.ChannelConstant;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.ChannelTxt;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.member.domain.MemberGroup;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.jeecms.common.constants.WebConstants.INTRANET_PREFIX;
import static com.jeecms.common.constants.WebConstants.SPT;

/**
 * 栏目主体表实体类
 *
 * @author: tom
 * @date: 2019年3月19日 下午6:23:01
 */
@Entity
@Table(name = "jc_channel")
@NamedQuery(name = "Channel.findAll", query = "SELECT c FROM Channel c")
public class Channel extends com.jeecms.common.base.domain.AbstractTreeDomain<Channel, Integer>
                implements Serializable {
        private static final long serialVersionUID = 1L;
        /** 外链打开新窗口名称 */
        public static final String LINK_TARGET_NAME = "linkTarget";

        /** 标识 */
        private Integer id;
        /** 访问路径 */
        private String path;
        /** 栏目描述 */
        private String description;
        /** 是否显示 */
        private Boolean display;
        /** 是否静态化（默认为0） */
        private Boolean staticChannel;
        /** 栏目名称 */
        private String name;
        /** 外链 */
        private String link;
        /** 模型ID */
        private Integer modelId;
        /** 站点ID */
        private Integer siteId;
        /** 栏目页模板 */
        private String tplPc;
        /** 手机栏目页模板 */
        private String tplMobile;
        /** 工作流ID */
        private Integer workflowId;
        /** 外链是否新窗口打开 (0-否 1-是) */
        private Boolean linkTarget;
        /** 是否允许投稿（0-不允许 1-允许） */
        private Boolean contribute;
        /** 回收站标识 */
        private Boolean recycle;
        /** 加入回收站时间 */
        private Date recycleTime;
        /** 是否已生成栏目静态化页面 */
        private Boolean hasStaticChannel;
        /** 是否内容已全部静态化 */
        private Boolean hasStaticContent;

        private int viewSum;

        private Boolean operatingContribute = true;
        
        /** 栏目扩展表对象 */
        private ChannelExt channelExt;
        /** 站点对象 */
        private CmsSite site;
        /** 模型对象 */
        private CmsModel model;
        /** 栏目内容模板List集合 */
        private List<ChannelContentTpl> contentTpls;
        /** 栏目自定义对象 */
        private List<ChannelAttr> channelAttrs;
        /** 富文本对象集合 */
        private List<ChannelTxt> txts;
        /** 浏览会员组 */
        private List<MemberGroup> viewGroups;
        /** 投稿会员组 */
        private List<MemberGroup> contributeGroups;

        private java.util.Set<Channel> child;

        /**
         * 获得URL地址
         *
         * @return
         */
        @Transient
        public String getUrl() {
                if (!StringUtils.isBlank(getLink())) {
                        return getLink();
                }
                if (getOpenStatic() && getHasStaticChannel()) {
                        return getUrlStatic(null, 1);
                } else {
                        return getUrlDynamic(null);
                }
        }

        /**
         * 获取url全路径
         *
         * @Title: getUrlWhole
         * @return: String
         */
        @Transient
        public String getUrlWhole() {
                if (!StringUtils.isBlank(getLink())) {
                        return getLink();
                }
                if (getStaticChannel()) {
                        return getUrlStatic(true, 1);
                } else {
                        return getUrlDynamic(true);
                }
        }

        /**
         * 获得静态URL地址
         *
         * @return
         */
        @Transient
        public String getUrlStatic() {
                return getUrlStatic(null, 1);
        }

        @Transient
        public String getUrlStatic(int pageNo) {
                return getUrlStatic(null, pageNo);
        }

        /**
         * 获取静态URl
         *
         * @Title: getUrlStatic
         * @param whole
         *                是否全路径
         * @param pageNo
         *                页码
         * @return: String
         */
        @Transient
        public String getUrlStatic(Boolean whole, int pageNo) {
                if (!StringUtils.isBlank(getLink())) {
                        return getLink();
                }
                CmsSite site = getSite();
                StringBuilder url = site.getUrlStaticBuff(null, SystemContextUtils.isPc());
                // 默认静态页面访问路径
                url.append(SPT).append(getPath());
                if (pageNo > 1) {
                        url.append("_").append(pageNo);
                }
                url.append(".").append(site.getConfig().getStaticHtmlSuffix());
                return url.toString();
        }

        /**
         * 获取预览 URL
         *
         * @Title: getPreviewUrl
         * @return: String
         */
        @Transient
        public String getPreviewUrl() {
                StringBuilder url = new StringBuilder();
                url.append(getSite().getSitePreviewUrl());
                return url.append("?channelId=").append(this.id).append("&type=")
                                .append(WebConstants.PREVIEW_TYPE_CHANNEL).toString();
        }

        /**
         * 获取动态URL
         *
         * @Title: getUrlDynamic
         * @param whole
         *                是否全路径
         * @return: String
         */
        @Transient
        public String getUrlDynamic(Boolean whole) {
                if (!StringUtils.isBlank(getLink())) {
                        return getLink();
                }
                CmsSite site = getSite();
                StringBuilder url = site.getUrlBuffer(whole);
                if (site.getGlobalConfig().getIsIntranet()) {
                        url.append(INTRANET_PREFIX).append(site.getPath());
                }
                url.append(SPT).append(getPath());
                if(getSite().getGlobalConfig().getConfigAttr().getUrlSuffixJhtml()){
                        url.append(WebConstants.DYNAMIC_CONTENT_SUFFIX);
                }
                return url.toString();
        }

        /**
         * 获得节点列表。从父节点到自身。
         *
         * @return
         */
        @Override
        @Transient
        public List<Channel> getNodeList() {
                LinkedList<Channel> list = new LinkedList<Channel>();
                Channel node = this;
                while (node != null) {
                        list.addFirst(node);
                        node = node.getParent();
                }
                return list;
        }

        /**
         * 获得节点列表ID。从父节点到自身。
         *
         * @return
         */
        @Override
        @Transient
        public Integer[] getNodeIds() {
                List<Channel> channels = getNodeList();
                Integer[] ids = new Integer[channels.size()];
                int i = 0;
                for (Channel c : channels) {
                        ids[i++] = c.getId();
                }
                return ids;
        }

        /**
         * 获得节点列表ID。从父节点到自身。
         */
        @Transient
        public List<Integer> getNodeIdList() {
                List<Channel> channels = getNodeList();
                List<Integer> ids = channels.stream().map(t -> t.getId()).collect(Collectors.toList());
                return ids;
        }

        /**
         * 获取所有子栏目的ID
         * 
         * @Title: getChildAllIdArray
         * @return: Integer[]
         */
        @Transient
        public Integer[] getChildAllIdArray() {
                /** 根据内容数取分页数,有子栏目则取所有子栏目的内容数，否则则只取自身栏目内容数 */
                List<Integer> ids = new ArrayList<Integer>();
                if (getChild() != null && getChild().size() > 0) {
                        ids.addAll(getChildAllIds());
                } else {
                        ids.add(getId());
                }
                Integer[] channelIds = new Integer[ids.size()];
                ids.toArray(channelIds);
                return channelIds;
        }

        /**
         * 获得深度
         *
         * @return 第一层为0，第二层为1，以此类推。
         */
        @Override
        @Transient
        public int getDeep() {
                int deep = 0;
                Channel parent = getParent();
                while (parent != null) {
                        deep++;
                        parent = parent.getParent();
                }
                return deep;
        }

        /**
         * 获取顶层栏目对象
         *
         * @Title: getTopChannel
         * @return Channel
         */
        @Transient
        public Channel getTopChannel() {
                Channel parent = getParent();
                while (parent != null) {
                        if (parent.getParent() != null) {
                                parent = parent.getParent();
                        } else {
                                break;
                        }
                }
                return parent;
        }

        /**
         * 获取栏目PC模板路径
         *
         * @Title: getTplChannelOrDefForPc
         * @return: String
         */
        @Transient
        public String getTplChannelOrDefForPc() {
                /** 栏目自身设置的模板 */
                String tpl = getTplPcPath();
                if (!StringUtils.isBlank(tpl)) {
                        return tpl;
                } else {
                        /** 否则取站点统一配置的模板 */
                        tpl = getTplSiteDef(getModelId(), true);
                        if (!StringUtils.isBlank(tpl)) {
                                return tpl;
                        } else {
                                /** 未取到站点统一配置的模板，新加的模型尚没有配置默认模板的模型 则取模型对应模板第一个模板 */
                                return getTplDefByModel(getModel(), false, true, getSite().getPcSolution());
                        }

                }
        }

        /**
         * 获取栏目手机模板路径
         *
         * @Title: getTplChannelOrDefForMobile
         * @return: String
         */
        @Transient
        public String getTplChannelOrDefForMobile() {
                /** 栏目自身设置的模板 */
                String tpl = getTplMobilePath();
                if (!StringUtils.isBlank(tpl)) {
                        return tpl;
                } else {
                        /** 否则取站点统一配置的模板 */
                        tpl = getTplSiteDef(getModelId(), false);
                        if (!StringUtils.isBlank(tpl)) {
                                return tpl;
                        } else {
                                /** 未取到站点统一配置的模板，新加的模型尚没有配置默认模板的模型 则取模型对应模板第一个模板 */
                                return getTplDefByModel(getModel(), false, false, getSite().getMobileSolution());
                        }

                }
        }

        /**
         * 获取栏目上设置的手机模板路径 (类似/WEB-INF/t/cms/www/mobile/content/news.html)
         *
         * @Title: getTplMobilePath
         * @return: String
         */
        @Transient
        public String getTplMobilePath() {
                /** 栏目自身设置的模板 */
                String tpl = getTplMobile();
                /** 模型字段不存在则读取站点中的统一设置模板 */
                boolean modelExistMobileTplField = getModel().existItem(CmsModelConstant.FIELD_SYS_TPL_MOBILE);
                if (!modelExistMobileTplField) {
                        tpl = null;
                }
                if (StringUtils.isNotBlank(tpl)) {
                        return getSite().getMobileSolutionPath() + SPT + tpl;
                }
                return null;
        }

        /**
         * 获取栏目上设置的手机模板路径 (类似/WEB-INF/t/cms/www/default/content/news.html)
         *
         * @Title: getTplPcPath
         * @return: String
         */
        @Transient
        public String getTplPcPath() {
                /** 栏目自身设置的模板 */
                String tpl = getTplPc();
                /** 模型字段不存在则读取站点中的统一设置模板 */
                boolean modelExistPcTplField = getModel().existItem(CmsModelConstant.FIELD_SYS_TPL_PC);
                if (!modelExistPcTplField) {
                        tpl = null;
                }
                if (StringUtils.isNotBlank(tpl)) {
                        return getSite().getPcSolutionPath() + SPT + tpl;
                }
                return null;
        }

        /**
         * 获取工作流ID （先判断模型字段是否存在，不存在则认为没有工作流）
         * 
         * @Title: getRealWorkflowId
         * @return: Integer
         */
        @Transient
        public Integer getRealWorkflowId() {
        		if (getModel() == null) {
					return null;
				}
                /** 模型字段不存在则读取站点中的统一设置模板 */
                boolean existFlowField = getModel().existItem(CmsModelConstant.FIELD_SYS_WORKFLOW);
                if (!existFlowField) {
                        return null;
                }
                return getWorkflowId();
        }

        @Transient
        public Boolean getWorkflowField() {
        	return getModel().existItem(CmsModelConstant.FIELD_SYS_WORKFLOW);
        }
        
        /**
         * 获取内容页面PC端模板
         *
         * @Title: getTplContentOrDefForPc
         * @param contentModel
         *                内容模型
         * @return: String
         */
        @Transient
        public String getTplContentOrDefForPc(CmsModel contentModel) {
                return getTplContentOrDef(contentModel, true);
        }

        /**
         * 获取内容页面手机端模板
         *
         * @Title: getTplContentOrDefForMobile
         * @param contentModel
         *                内容模型
         * @return: String
         */
        @Transient
        public String getTplContentOrDefForMobile(CmsModel contentModel) {
                return getTplContentOrDef(contentModel, false);
        }

        /**
         * 读取站点的配置
         * 
         * @Title: getOpenStatic
         * @return: boolean true 开启了 false 未开启
         */
        @Transient
        public boolean getOpenStatic() {
                /**
                 * 是否开启静态服务 栏目中有设置且模型字段中存在这个字段则返回栏目中设置的，否则读取站点的配置 站点未配置则false Boolean open = getStaticChannel(); if
                 * (open != null) { if (!getModel().existItem(CmsModelConstant.FIELD_SYS_STATIC_CHANNEL)) { open = null;
                 * } } if (open == null) { open = getSite().getOpenStatic(); }
                 */
                return getSite().getOpenStatic();
        }

        private String getTplContentOrDef(CmsModel contentModel, boolean getPcTpl) {
                /** 栏目设置的内容模板 */
                String tpl = getTplContentDef(contentModel.getId(), getPcTpl);
                /** 内容模型设置字段不存在则读取站点中的统一设置模板 */
                boolean modelExistTplField = getModel().existItem(CmsModelConstant.FIELD_SYS_TPL_CONTENT);
                if (!modelExistTplField) {
                        tpl = null;
                }
                /** 栏目上配置了内容模板并且模板字段存在则取栏目配置的模板 */
                if (!StringUtils.isBlank(tpl)) {
                        return tpl;
                } else {
                        /** 否则取站点统一配置的模板 */
                        tpl = getTplSiteDef(contentModel.getId(), getPcTpl);
                        if (!StringUtils.isBlank(tpl)) {
                                return tpl;
                        } else {
                                /** 未取到站点统一配置的模板，新加的模型尚没有配置默认模板的模型 则取模型对应模板第一个模板 */
                                if (getPcTpl) {
                                        return getTplDefByModel(contentModel, true, getPcTpl,
                                                        getSite().getPcSolution());
                                } else {
                                        return getTplDefByModel(contentModel, true, getPcTpl,
                                                        getSite().getMobileSolution());
                                }
                        }

                }
        }

        /**
         * 获取站点配置默认模板
         *
         * @Title: getTplSiteDef
         * @param modelId
         *                模型ID（内容模型ID或栏目模型ID）
         * @param getPcTpl
         *                是否是PC 模板 true是 false 否
         * @return: String
         */
        private String getTplSiteDef(Integer modelId, boolean getPcTpl) {
                return getSite().getTplByModel(modelId, getPcTpl);
        }

        /**
         * 获取栏目配置的内容模板
         *
         * @Title: getTplDef
         * @param contentModelId
         *                内容模型ID
         * @param getPcTpl
         *                是否取内容模板 true 内容模板 false 栏目模板
         * @return: String
         */
        private String getTplContentDef(Integer contentModelId, boolean getPcTpl) {
                List<ChannelContentTpl> tpls = getContentTpls();
                if (tpls != null && tpls.size() > 0 && contentModelId != null) {
                        for (ChannelContentTpl tpl : tpls) {
                                if (contentModelId.equals(tpl.getModelId())) {
                                        if (getPcTpl && StringUtils.isNotBlank(tpl.getTplPc())) {
                                                return getSite().getPcSolutionPath() + SPT + tpl.getTplPc();
                                        }
                                        if (!getPcTpl && StringUtils.isNotBlank(tpl.getTplMobile())) {
                                                return getSite().getMobileSolutionPath() + SPT + tpl.getTplMobile();
                                        }
                                }
                        }
                }
                return null;
        }

        /**
         * 获取模型默认模板
         *
         * @Title: getTplDefByModel
         * @param model
         *                模型对象（栏目模型或内容模型）
         * @param getContentTpl
         *                是否取内容模板 true 内容模板 false 栏目模板
         * @param getPcTpl
         *                true获取pc模板 false获取手机端模板
         * @param solution
         *                模板方案
         * @return: String
         */
        private String getTplDefByModel(CmsModel model, boolean getContentTpl, boolean getPcTpl, String solution) {
                Set<CmsModelTpl> tpls = model.getTpls();
                if (tpls != null && tpls.size() > 0) {
                        List<CmsModelTpl> modelTpls;
                        modelTpls = tpls.stream()
                                        .filter(tpl -> tpl.getTplType().equals(CmsModelTpl.TPL_TYPE_CHANNEL)
                                                        && solution.equals(tpl.getTplSolution())
                                                        && getSiteId().equals(tpl.getSiteId()))
                                        .collect(Collectors.toList());
                        if (getContentTpl) {
                                modelTpls = tpls.stream()
                                                .filter(tpl -> tpl.getTplType().equals(CmsModelTpl.TPL_TYPE_CONTENT)
                                                                && solution.equals(tpl.getTplSolution())
                                                                && getSiteId().equals(tpl.getSiteId()))
                                                .collect(Collectors.toList());
                        }
                        if (modelTpls != null && modelTpls.size() > 0) {
                                CmsModelTpl targetModelTpl = modelTpls.get(0);
                                if (getPcTpl) {
                                        return getSite().getPcSolutionPath() + SPT + targetModelTpl.getTplPath();
                                } else {
                                        return getSite().getMobileSolutionPath() + SPT + targetModelTpl.getTplPath();
                                }
                        }
                }
                return null;
        }

        /**
         * 获取访问设置 1.都不需要登录2.仅内容页需登录 3.都需要登录
         *
         * @Title: getViewControl
         * @return: Short
         */
        @Transient
        public Short getViewControl() {
                /** 访问设置字段不存在则读取站点的配置 */
                boolean exist = getModel().existItem(CmsModelConstant.FIELD_SYS_VIEW_CONTROL);
                if (!exist) {
                        return getSite().getConfig().getChannelVisitLimitType();
                }
                return getChannelExt().getViewControl();
        }

        /**
         * 获取所有子栏目包含自身
         *
         * @Title: getChildAll
         * @return: List
         */
        @Transient
        public List<Channel> getChildAll() {
                return getListForSelect(null, true);
        }

        /**
         * 获取所有子栏目包含自身的ID
         *
         * @Title: getChildAll
         * @return: List
         */
        @Transient
        public List<Integer> getChildAllIds() {
                return fetchIds(getChildAll());
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param rights
         *                有权限的栏目，为null不控制权限。
         * @param containNotDisplay
         *                false则不包含不显示的栏目 true包含未发布的栏目。
         * @return List
         */
        @Transient
        public List<Channel> getListForSelect(Set<Channel> rights, boolean containNotDisplay) {
                return getListForSelect(rights, null, containNotDisplay);
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param rights
         *                有权限的栏目，为null不控制权限。
         * @param exclude
         *                排除的栏目
         * @param containNotDisplay
         *                false则不包含不显示的栏目 true包含未显示的栏目。
         * @return List
         */
        @Transient
        public List<Channel> getListForSelect(Set<Channel> rights, Channel exclude, boolean containNotDisplay) {
                List<Channel> list = new ArrayList<Channel>((getRgt() - getLft()) / 2);
                addChildToList(list, this, rights, exclude, containNotDisplay);
                return list;
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param topList
         *                顶级栏目
         * @param rights
         *                有权限的栏目，为null不控制权限。
         * @param containNotDisplay
         *                false则不包含不显示的栏目 true包含未发布的栏目。
         * @return List
         */
        public static List<Channel> getListForSelect(List<Channel> topList, Set<Channel> rights,
                        boolean containNotDisplay) {
                return getListForSelect(topList, rights, null, containNotDisplay);
        }

        /**
         * 获得列表用于下拉选择。条件：有内容的栏目。
         *
         * @Title: getListForSelect
         * @param topList
         *                顶级栏目
         * @param rights
         *                有权限的栏目，为null不控制权限。
         * @param exclude
         *                排除的栏目
         * @param containNotDisplay
         *                false则不包含不显示的栏目 true包含未发布的栏目。
         * @return List
         */
        public static List<Channel> getListForSelect(List<Channel> topList, Set<Channel> rights, Channel exclude,
                        boolean containNotDisplay) {
                List<Channel> list = new ArrayList<Channel>();
                for (Channel c : topList) {
                        addChildToList(list, c, rights, exclude, containNotDisplay);
                }
                return list;
        }

        /**
         * 递归将子栏目加入列表。条件：有内容的栏目。
         *
         * @param list
         *                栏目容器
         * @param channel
         *                待添加的栏目，且递归添加子栏目
         * @param rights
         *                有权限的栏目，为null不控制权限。
         * @param containNotDisplay
         *                false则不包含不显示的栏目 true包含未发布的栏目。
         */
        private static void addChildToList(List<Channel> list, Channel channel, Set<Channel> rights, Channel exclude,
                        boolean containNotDisplay) {
                boolean hasNotContaind = (rights != null && !rights.contains(channel))
                                || (exclude != null && exclude.equals(channel));
                if (hasNotContaind) {
                        return;
                }
                addChildToList(list, channel, containNotDisplay);
        }

        private static void addChildToList(List<Channel> list, Channel channel, boolean containNotDisplay) {
                if (!containNotDisplay) {
                        if (!channel.getDisplay()) {
                                return;
                        }
                }
                if (list != null && Channel.fetchIds(list).contains(channel.getId())) {
                        return;
                }
                list.add(channel);
                Set<Channel> child = channel.getChild();
                for (Channel c : child) {
                        addChildToList(list, c, containNotDisplay);
                }
        }

        /**
         * 栏目对象初始化
         *
         * @Title: init
         */
        @Transient
        public void init() {
                if (getDisplay() == null) {
                        setDisplay(true);
                }
        }

        /**
         * 分页大小
         *
         * @Title: getPageSize
         * @return Short
         */
        @Transient
        public Short getPageSize() {
                ChannelExt ext = getChannelExt();
                if (ext != null) {
                        return ext.getRealPageSize();
                } else {
                        return 20;
                }
        }

        /**
         * 获取正文的页数(多个正文则取页数最多的正文分页数)
         *
         * @Title: getTxtPageCount
         * @return: int
         */
        @Transient
        public int getTxtPageCount() {
                List<ChannelTxt> txts = getRealTxts();
                int count = 1;
                if (txts != null && txts.size() > 0) {
                        for (ChannelTxt t : txts) {
                                if (t.getTxtCount() > count) {
                                        count = t.getTxtCount();
                                }
                        }
                }
                return count;
        }

        /**
         * 获取真实有效的富文本对象
         *
         * @Title: getRealTxts
         * @return: List
         */
        @Transient
        public List<ChannelTxt> getRealTxts() {
                CmsModel model = getModel();
                List<ChannelTxt> realTxts = new ArrayList<ChannelTxt>();
                if (txts != null && !txts.isEmpty()) {
                        for (ChannelTxt t : txts) {
                                if (model.existItem(t.getAttrKey())) {
                                        realTxts.add(t);
                                }
                        }
                        return realTxts;
                }
                return null;
        }

        /**
         * 获取正文的页数
         *
         * @Title: getTxtPageCountByField
         * @param field
         *                字段名称
         * @return: int
         */
        @Transient
        public int getTxtPageCountByField(String field) {
                ChannelTxt txt = getTxtByField(field);
                if (txt != null) {
                        return txt.getTxtCount();
                }
                return 1;
        }

        /**
         * 获取内容分页内容，获取第一个正文的分页内容
         *
         * @Title: getTxtByNo
         * @param pageNo
         *                分页数
         * @return: String
         */
        @Transient
        public String getTxtByNo(int pageNo) {
                List<ChannelTxt> txts = getRealTxts();
                if (txts != null && txts.size() > 0) {
                        return txts.get(0).getTxtByNo(pageNo);
                } else {
                        return null;
                }
        }

        /**
         * 获取富文本字段分页内容，根据字段名称获取
         *
         * @Title: getTxtForFieldByNo
         * @param field
         *                字段名称
         * @param pageNo
         *                分页数
         * @return: String
         */
        @Transient
        public String getTxtForFieldByNo(String field, int pageNo) {
                ChannelTxt txt = getTxtByField(field);
                if (txt != null) {
                        return txt.getTxtByNo(pageNo);
                }
                return null;
        }

        /**
         * 获取ChannelTxt 根据字段名称
         *
         * @Title: getTxtByField
         * @param field
         *                字段名称
         * @return: ChannelTxt
         */
        @Transient
        public ChannelTxt getTxtByField(String field) {
                if (StringUtils.isBlank(field)) {
                        return null;
                }
                /** 如果模型不存在字段则为空 */
                if (getModel().existItem(field)) {
                        return null;
                }
                List<ChannelTxt> txts = getTxts();
                for (ChannelTxt txt : txts) {
                        if (field.equals(txt.getAttrKey())) {
                                return txt;
                        }
                }
                return null;
        }

        /**
         * 获取富文本数量
         *
         * @Title: getContentTxtCounts
         * @return: int
         */
        @Transient
        public int getTxtCounts() {
                List<ChannelTxt> txts = getRealTxts();
                if (txts != null) {
                        return txts.size();
                }
                return 0;
        }

        /**
         * 获取栏目是否多富文本
         *
         * @Title: getMutiTxt
         * @return: boolean true是多富文本内容
         */
        @Transient
        public boolean getMutiTxt() {
                if (getTxtCounts() > 1) {
                        return true;
                }
                return false;
        }

        /**
         * 获取是否列表栏目（默认是）
         *
         * @Title: getIsListChannel
         * @return: Boolean
         */
        @Transient
        public Boolean getIsListChannel() {
                if (getChannelExt() != null && getModel().existItem(CmsModelConstant.FIELD_SYS_CHANNEL_LIST)) {
                        return getChannelExt().getIsListChannel();
                }
                return true;
        }

        /**
         * 获取静态化栏目文件的路径 /(p||m+站点ID)/栏目路径.html (栏目目录不能是index)
         *
         * @Title: getStaticFilename
         * @param pc
         *                是否pc页码
         * @param pageNo
         *                页码
         * @return: String
         */
        @Transient
        public String getStaticFilename(boolean pc, int pageNo) {
                StringBuilder url = new StringBuilder();
                // 默认静态路径
                url.append(SPT);
                if (pc) {
                        url.append(WebConstants.STATIC_PC_PATH);
                } else {
                        url.append(WebConstants.STATIC_MOBILE_PATH);
                }
                url.append(getSiteId());
                url.append(SPT).append(getPath());
                if (pageNo > 1) {
                        url.append("_").append(pageNo);
                }
                url.append(".");
                String suffix = getSite().getCmsSiteCfg().getStaticHtmlSuffix();
                url.append(suffix);

                return url.toString();
        }

        /**
         * 每个站点各自维护独立的树结构
         *
         * @see HibernateTree#getTreeCondition()
         */
        @Override
        @Transient
        public String getTreeCondition() {
                return "bean.site.id=" + getSite().getId() + " and bean.hasDeleted=false";
        }

        /**
         * 获得所有的id的List集合
         */
        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<Channel> channels) {
                if (channels == null) {
                        return null;
                }
                // 过滤值为null
                channels = channels.stream().filter(x -> x != null).collect(Collectors.toList());
                List<Integer> ids = new ArrayList<Integer>();
                for (Channel s : channels) {
                        ids.add(s.getId());
                }
                return ids;
        }

        /**
         * 是否可修改
         *
         * @Title: getEditAble
         * @return boolean
         */
        @Transient
        public boolean getEditAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getEditChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可建子栏目
         *
         * @Title: getCreateChildAble
         * @return boolean
         */
        @Transient
        public boolean getCreateChildAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getCreateChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可删除
         *
         * @Title: getDeleteAble
         * @return boolean
         */
        @Transient
        public boolean getDeleteAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                /**来自顶层组织则通过，不进行详细数据检查*/
//                if(user.getOwnerDataPermsByTypeFromSuper(CmsDataPerm.DATA_TYPE_CHANNEL)){
//                        return true;
//                }
                if (user.getDelChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可合并
         *
         * @Title: getMergeAble
         * @return boolean
         */
        @Transient
        public boolean getMergeAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getMergeChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        @Transient
        public int getViewSum() {
                return viewSum;
        }

        public void setViewSum(int viewSum) {
                this.viewSum = viewSum;
        }

        /**
         * 是否可静态化
         *
         * @Title: getStaticAble
         * @return boolean
         */
        @Transient
        public boolean getStaticAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getStaticChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可查看
         *
         * @Title: getViewAble
         * @return boolean
         */
        @Transient
        public boolean getViewAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getViewChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可分配权限
         *
         * @Title: getPermAssignAble
         * @return boolean
         */
        @Transient
        public boolean getPermAssignAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getPermAssignChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可查看内容
         *
         * @Title: getViewContentAble
         * @return boolean
         */
        @Transient
        public boolean getViewContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getViewContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可修改内容
         *
         * @Title: getEditContentAble
         * @return boolean
         */
        @Transient
        public boolean getEditContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getEditContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可删除
         *
         * @Title: getDeleteContentAble
         * @return boolean
         */
        @Transient
        public boolean getDeleteContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getDelContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可归档
         *
         * @Title: getFileContentAble
         * @return boolean
         */
        @Transient
        public boolean getFileContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getFileContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可置顶
         *
         * @Title: getTopContentAble
         * @return boolean
         */
        @Transient
        public boolean getTopContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getTopContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可移动
         *
         * @Title: getMoveContentAble
         * @return boolean
         */
        @Transient
        public boolean getMoveContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getMoveContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可排序
         *
         * @Title: getSortContentAble
         * @return boolean
         */
        @Transient
        public boolean getSortContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getSortContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可复制
         *
         * @Title: getCopyContentAble
         * @return boolean
         */
        @Transient
        public boolean getCopyContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getCopyContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可引用
         *
         * @Title: getQuoteContentAble
         * @return boolean
         */
        @Transient
        public boolean getQuoteContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getQuoteContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可操作内容类型
         *
         * @Title: getTypeContentAble
         * @return boolean
         */
        @Transient
        public boolean getTypeContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getTypeContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可建内容
         *
         * @Title: getCreateContentAble
         * @return boolean
         */
        @Transient
        public boolean getCreateContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getCreateContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可发布内容
         *
         * @Title: getPublishContentAble
         * @return boolean
         */
        @Transient
        public boolean getPublishContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getPublishContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可站群推送
         *
         * @Title: getSitePushContentAble
         * @return boolean
         */
        @Transient
        public boolean getSitePushContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getSitePushContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可微信推送
         *
         * @Title: getWechatPushContentAble
         * @return boolean
         */
        @Transient
        public boolean getWechatPushContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getWechatPushContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可微博推送
         *
         * @Title: getWeiboPushContentAble
         * @return boolean
         */
        @Transient
        public boolean getWeiboPushContentAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getWeiboPushContentChannelIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否开启索引
         */
        @Transient
        public Boolean getIsOpenIndex() {
                /**存在脏数据直接返回true*/
                if(null==getChannelExt()){
                        return true;
                }
                return getChannelExt().getIsOpenIndex();
        }

        /**
         * 以map的方式读取自定义属性，方便单个取字段
         * 
         * @Title: getAttr
         * @return: Map
         */
        @Transient
        public Map<String, ChannelAttr> getAttr() {
                List<ChannelAttr> attrs = getChannelAttrs();
                Map<String, ChannelAttr> map = new HashMap<String, ChannelAttr>();
                if (attrs != null && attrs.size() > 0) {
                        for (ChannelAttr attr : attrs) {
                                map.put(attr.getAttrName(), attr);
                        }
                }
                return map;
        }

        public Channel() {
        }

        @Id
        @TableGenerator(name = "jc_channel", pkColumnValue = "jc_channel", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_channel")
        @Column(name = "channel_id", unique = true, nullable = false)
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "channel_path", nullable = false, length = 50)
        public String getPath() {
                return this.path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        @Column(name = "channel_name", nullable = false, length = 50)
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Column(length = 500)
        public String getDescription() {
                return this.description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        @Column(name = "is_display", nullable = false)
        public Boolean getDisplay() {
                return this.display;
        }

        public void setDisplay(Boolean display) {
                this.display = display;
        }

        @Column(name = "is_static_channel", nullable = false)
        public Boolean getStaticChannel() {
                return this.staticChannel;
        }

        public void setStaticChannel(Boolean staticChannel) {
                this.staticChannel = staticChannel;
        }

        @Column(name = "u_link",length = 255)
        public String getLink() {
                return this.link;
        }

        public void setLink(String link) {
                this.link = link;
        }

        @Column(name = "model_id", nullable = false, insertable = false, updatable = false)
        public Integer getModelId() {
                return this.modelId;
        }

        public void setModelId(Integer modelId) {
                this.modelId = modelId;
        }

        @Column(name = "site_id", nullable = false, insertable = false, updatable = false)
        public Integer getSiteId() {
                return this.siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @Column(name = "tpl_channel", length = 150)
        public String getTplPc() {
                return this.tplPc;
        }

        public void setTplPc(String tplPc) {
                this.tplPc = tplPc;
        }

        @Column(name = "tpl_mobile_channel", length = 150)
        public String getTplMobile() {
                return this.tplMobile;
        }

        public void setTplMobile(String tplMobile) {
                this.tplMobile = tplMobile;
        }

        @Column(name = "workflow_id")
        public Integer getWorkflowId() {
                return this.workflowId;
        }

        public void setWorkflowId(Integer workflowId) {
                this.workflowId = workflowId;
        }

        @Column(name = "link_target")
        public Boolean getLinkTarget() {
                return linkTarget;
        }

        public void setLinkTarget(Boolean linkTarget) {
                this.linkTarget = linkTarget;
        }

        @Column(name = "is_contribute")
        public Boolean getContribute() {
                return contribute;
        }

        public void setContribute(Boolean contribute) {
                this.contribute = contribute;
        }

        @Column(name = "is_recycle")
        public Boolean getRecycle() {
                return recycle;
        }

        public void setRecycle(Boolean recycle) {
                this.recycle = recycle;
        }

        @Column(name = "recycle_time")
        public Date getRecycleTime() {
                return recycleTime;
        }

        public void setRecycleTime(Date recycleTime) {
                this.recycleTime = recycleTime;
        }

        @Column(name = "has_static_channel")
        public Boolean getHasStaticChannel() {
                return hasStaticChannel;
        }

        public void setHasStaticChannel(Boolean hasStatic) {
                this.hasStaticChannel = hasStatic;
        }

        @Column(name = "has_static_content")
        public Boolean getHasStaticContent() {
                return hasStaticContent;
        }

        public void setHasStaticContent(Boolean hasStaticContent) {
                this.hasStaticContent = hasStaticContent;
        }

        @OneToOne(fetch = FetchType.LAZY, mappedBy = "channel", cascade = { CascadeType.PERSIST })
        public ChannelExt getChannelExt() {
                return channelExt;
        }

        public void setChannelExt(ChannelExt channelExt) {
                this.channelExt = channelExt;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id")
        public CmsSite getSite() {
                return site;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "model_id")
        @NotFound(action=NotFoundAction.IGNORE)
        public CmsModel getModel() {
                return model;
        }

        public void setModel(CmsModel model) {
                this.model = model;
        }

        @Override
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public Channel getParent() {
                return parent;
        }

        @Where(clause = "deleted_flag=0")
        @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
        public java.util.Set<Channel> getChild() {
                return child;
        }

        public void setChild(java.util.Set<Channel> child) {
                this.child = child;
        }

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "channel", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.DETACH })
        public List<ChannelContentTpl> getContentTpls() {
                return contentTpls;
        }

        public void setContentTpls(List<ChannelContentTpl> contentTpls) {
                this.contentTpls = contentTpls;
        }

        @OneToMany(mappedBy = "channel")
        public List<ChannelTxt> getTxts() {
                return txts;
        }

        public void setTxts(List<ChannelTxt> txts) {
                this.txts = txts;
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_group_channel_view", joinColumns = @JoinColumn(name = "channel_id") , inverseJoinColumns = @JoinColumn(name = "group_id") )
        public List<MemberGroup> getViewGroups() {
                return viewGroups;
        }

        public void setViewGroups(List<MemberGroup> viewGroups) {
                this.viewGroups = viewGroups;
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_group_channel_contribute", joinColumns = @JoinColumn(name = "channel_id") , inverseJoinColumns = @JoinColumn(name = "group_id") )
        public List<MemberGroup> getContributeGroups() {
                return contributeGroups;
        }

        public void setContributeGroups(List<MemberGroup> contributeGroups) {
                this.contributeGroups = contributeGroups;
        }

        @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
        public List<ChannelAttr> getChannelAttrs() {
                return channelAttrs;
        }

        public void setChannelAttrs(List<ChannelAttr> channelAttrs) {
                this.channelAttrs = channelAttrs;
        }

        /**
         * 重写 hashCode
         *
         * @Title: hashCode
         * @return int
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((contribute == null) ? 0 : contribute.hashCode());
                result = prime * result + ((description == null) ? 0 : description.hashCode());
                result = prime * result + ((display == null) ? 0 : display.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((link == null) ? 0 : link.hashCode());
                result = prime * result + ((linkTarget == null) ? 0 : linkTarget.hashCode());
                result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((path == null) ? 0 : path.hashCode());
                result = prime * result + ((recycle == null) ? 0 : recycle.hashCode());
                result = prime * result + ((recycleTime == null) ? 0 : recycleTime.hashCode());
                result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
                result = prime * result + ((staticChannel == null) ? 0 : staticChannel.hashCode());
                result = prime * result + ((tplMobile == null) ? 0 : tplMobile.hashCode());
                result = prime * result + ((tplPc == null) ? 0 : tplPc.hashCode());
                result = prime * result + ((workflowId == null) ? 0 : workflowId.hashCode());
                return result;
        }

        /**
         * 重写 equals
         *
         * @Title: equals
         * @param obj
         *                Object
         * @return boolean
         */
        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                Channel other = (Channel) obj;
                if (contribute == null) {
                        if (other.contribute != null) {
                                return false;
                        }
                } else if (!contribute.equals(other.contribute)) {
                        return false;
                }
                if (description == null) {
                        if (other.description != null) {
                                return false;
                        }
                } else if (!description.equals(other.description)) {
                        return false;
                }
                if (display == null) {
                        if (other.display != null) {
                                return false;
                        }
                } else if (!display.equals(other.display)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (link == null) {
                        if (other.link != null) {
                                return false;
                        }
                } else if (!link.equals(other.link)) {
                        return false;
                }
                if (linkTarget == null) {
                        if (other.linkTarget != null) {
                                return false;
                        }
                } else if (!linkTarget.equals(other.linkTarget)) {
                        return false;
                }
                if (modelId == null) {
                        if (other.modelId != null) {
                                return false;
                        }
                } else if (!modelId.equals(other.modelId)) {
                        return false;
                }
                if (name == null) {
                        if (other.name != null) {
                                return false;
                        }
                } else if (!name.equals(other.name)) {
                        return false;
                }
                if (path == null) {
                        if (other.path != null) {
                                return false;
                        }
                } else if (!path.equals(other.path)) {
                        return false;
                }
                if (recycle == null) {
                        if (other.recycle != null) {
                                return false;
                        }
                } else if (!recycle.equals(other.recycle)) {
                        return false;
                }
                if (recycleTime == null) {
                        if (other.recycleTime != null) {
                                return false;
                        }
                } else if (!recycleTime.equals(other.recycleTime)) {
                        return false;
                }
                if (siteId == null) {
                        if (other.siteId != null) {
                                return false;
                        }
                } else if (!siteId.equals(other.siteId)) {
                        return false;
                }
                if (staticChannel == null) {
                        if (other.staticChannel != null) {
                                return false;
                        }
                } else if (!staticChannel.equals(other.staticChannel)) {
                        return false;
                }
                if (tplMobile == null) {
                        if (other.tplMobile != null) {
                                return false;
                        }
                } else if (!tplMobile.equals(other.tplMobile)) {
                        return false;
                }
                if (tplPc == null) {
                        if (other.tplPc != null) {
                                return false;
                        }
                } else if (!tplPc.equals(other.tplPc)) {
                        return false;
                }
                if (workflowId == null) {
                        if (other.workflowId != null) {
                                return false;
                        }
                } else if (!workflowId.equals(other.workflowId)) {
                        return false;
                }
                return true;
        }

        /**
         * 判断是否为根节点. true表示是,false表示否 1. 如果子集大小为0则是根节点 2.
         * 如果子集大于0，则如果子集中有不是加入回收站的表示不适合根节点，如果没有说明其子集已经被删除或者加入回收站，则说明其已经是根节点了
         */
        @Transient
        public Boolean getIsBottom() {
                List<Channel> childs = getChilds();
	        	if (childs == null) {
	        		return true;
	        	}
                // 判断是否为根节点
                if (childs.isEmpty()) {
                        return true;
                } else {
                        int i = 0;
                        for (Channel channel : childs) {
                                if (channel.getRecycle()) {
                                        i++;
                                }
                        }
                        // 如果在回收站的个数小于总个数表示不是根节点
                        if (i < childs.size()) {
                                return false;
                        }
                        return true;
                }
        }
        
        

        /**
         * 获取真实，显示在栏目循环列表
         */
        @Transient
        public Boolean getRealDisplay() {
                if (getModel().existItem(CmsModelConstant.FIELD_CHANNEL_DISPLAY)) {
                        return getDisplay();
                }
                return getSite().getCmsSiteCfg().getChannelDisplayList();
        }

        /**
         * 获取真实是否允许投稿
         */
        @Transient
        public Boolean getRealContribute() {
                if (getModel().existItem(CmsModelConstant.FIELD_CHANNEL_CONTRIBUTE)) {
                        return contribute;
                }
                return ChannelConstant.TRUE.equals(getSite().getConfig().getChannelNormalLimitContribute());
        }

        /**
         * 获取真实外链是否允许新窗口打开
         */
        @Transient
        public Boolean getRealLinkTarget() {
                if (getModel().existItem(CmsModelConstant.FIELD_CHANNEL_LINK)) {
                        return linkTarget;
                }
                return false;
        }

        /**
         * 获取真实外链
         */
        @Transient
        public String getRealLink() {
                if (getModel().existItem(CmsModelConstant.FIELD_CHANNEL_LINK)) {
                        return this.link;
                }
                return null;
        }

        /**
         * 获取真实栏目描述
         */
        @Transient
        public String getRealDescription() {
                if (getModel().existItem(CmsModelConstant.FIELD_CHANNEL_TXT)) {
                        return this.description;
                }
                return null;
        }

        /**
         * 获取栏目图片
         * 
         * @Title: getIconUrl
         * @return: String
         */
        @Transient
        public String getIconUrl() {
                ResourcesSpaceData d = getChannelExt().getRealResourcesSpaceData();
                if (d != null) {
                        return d.getUrl();
                }
                return null;
        }

        /**
         * 获取栏目标题
         * 
         * @Title: getIconUrl
         * @return: String
         */
        @Transient
        public String getTitle() {
                return getChannelExt().getRealSeoTitle();
        }

        /**
         * 获取栏目关键词
         * 
         * @Title: getKeywords
         * @return: String
         */
        @Transient
        public String getKeywords() {
                return getChannelExt().getRealSeoKeywork();
        }

        /**
         * 获取栏目描述
         * 
         * @Title: getDescriptions
         * @return: String
         */
        @Transient
        public String getDescriptions() {
                return getChannelExt().getRealSeoDescription();
        }

        @Transient
		public Boolean getOperatingContribute() {
			return operatingContribute;
		}

		public void setOperatingContribute(Boolean operatingContribute) {
			this.operatingContribute = operatingContribute;
		}
}