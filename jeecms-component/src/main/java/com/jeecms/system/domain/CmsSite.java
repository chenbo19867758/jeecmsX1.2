/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.common.constants.UploadEnum;
import com.jeecms.common.constants.UploadEnum.UploadLimitType;
import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.jeecms.common.constants.TplConstants.*;
import static com.jeecms.common.constants.WebConstants.*;

/**
 * The persistent class for the jc_site database table.
 *
 * @author: tom
 * @date: 2018年11月5日 上午11:17:05
 */
@Entity
@Table(name = "jc_site")
@NamedQuery(name = "CmsSite.findAll", query = "SELECT c FROM CmsSite c")
public class CmsSite extends com.jeecms.common.base.domain.AbstractTreeDomain<CmsSite, Integer>
                implements IBaseSite, Serializable, Cloneable {

        Logger log = LoggerFactory.getLogger(CmsSite.class);
        private static final long serialVersionUID = -9043814136681766414L;
        /**
         * 唯一标识符
         **/
        private Integer id;
        /**
         * 全局配置id
         **/
        private Integer globalConfigId;
        /**
         * 站点描述
         **/
        private String description;
        /**
         * 站点图片ID
         **/
        private Integer iconId;
        /**
         * seo描述
         **/
        private String seoDescription;
        /**
         * seo关键字
         **/
        private String seoKeyword;
        /**
         * seo标题
         **/
        private String seoTitle;
        /**
         * 域名
         **/
        private String domain;
        /**
         * 域名别名
         **/
        private String domainAlias;
        /**
         * 语言
         **/
        private String siteLanguage;
        /**
         * 站点名称
         **/
        private String name;
        /**
         * 资源目录
         **/
        private String path;
        /**
         * 访问协议'
         **/
        private String protocol;
        /**
         * 站点状态（0未发布1已发布）
         **/
        private Boolean isOpen;
        /**
         * pc首页模板
         **/
        private String pcIndexTpl;
        /**
         * 手机首页模板
         **/
        private String mobileIndexTpl;
        /**
         * 是否删除(0未删除 1删除进入回收站)
         **/
        private Boolean isDelete;
        /**
         * 全局配置对象
         **/
        private GlobalConfig globalConfig;
        /**
         * 站点图片
         **/
        private ResourcesSpaceData iconRes;
        /**
         * 站点扩展
         **/
        private CmsSiteExt cmsSiteExt;
        /**
         * 站点模板
         **/
        private List<SiteModelTpl> modelTpls;

        /** 排序权重 */
        private Integer sortWeight;

        private List<Channel> channels;

        private java.util.Map<java.lang.String, java.lang.String> cfg;

        @Transient
        public String getIconPath() {
                if (getIconRes() != null) {
                        return getIconRes().getUrl();
                } else {
                        return "";
                }
        }

        @Override
        @Transient
        public Integer getSiteId() {
                return getId();
        }

        /**
         * 返回PC首页模板
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getTplIndexOrDefForPc() {
                String siteTpl = getTplIndexConfig(true);
                if (StringUtils.isNoneBlank(siteTpl)) {
                        return siteTpl;
                }
                return getTplIndexDefault(true);
        }

        /**
         * 返回手机端首页模板
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getTplIndexOrDefForMobile() {
                String siteTpl = getTplIndexConfig(false);
                if (StringUtils.isNoneBlank(siteTpl)) {
                        return siteTpl;
                }
                return getTplIndexDefault(false);
        }

        /**
         * 返回站点配置的模板(类似/WEB-INF/t/cms/www/default/index/index.html)
         *
         * @param getPcTpl
         *                是否获取pc模板
         * @Title: getTplIndexConfig
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        private String getTplIndexConfig(boolean getPcTpl) {
                StringBuilder t = new StringBuilder();
                String siteTpl = getConfig().getPcHomePageTemplates();
                if (!getPcTpl) {
                        siteTpl = getConfig().getMobileHomePageTemplates();
                        if (StringUtils.isBlank(siteTpl)) {
                                return null;
                        }
                        t.append(getMobileSolutionPath()).append(SPT);
                } else {
                        if (StringUtils.isBlank(siteTpl)) {
                                return null;
                        }
                        t.append(getPcSolutionPath()).append(SPT);
                }
                t.append(siteTpl);
                return t.toString();
        }

        /**
         * 返回首页默认模板(类似/WEB-INF/t/cms/www/default/index/index.html)
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        private String getTplIndexDefault(boolean getPcTpl) {
                StringBuilder t = new StringBuilder();
                t.append(getTplIndexPrefix(TPLDIR_INDEX, getPcTpl));
                t.append(TPL_SUFFIX);
                return t.toString();
        }

        /**
         * 返回完整前缀(类似/WEB-INF/t/cms/www/default/index/index)
         *
         * @param prefix
         *                前缀
         * @return
         */
        public String getTplIndexPrefix(String prefix, boolean getPcTpl) {
                StringBuilder t = new StringBuilder();
                if (getPcTpl) {
                        t.append(getPcSolutionPath()).append(SPT);
                } else {
                        t.append(getMobileSolutionPath()).append(SPT);
                }
                t.append(TPLDIR_INDEX).append("/");
                if (!StringUtils.isBlank(prefix)) {
                        t.append(prefix);
                }
                return t.toString();
        }

        /**
         * 返回完整前缀(类似/WEB-INF/t/cms/www/default/index/index)
         *
         * @param solutionPath
         *                模板方案名
         * @return 完整前缀
         */
        public String getTplIndexPrefix(String solutionPath) {
                StringBuilder t = new StringBuilder();
                t.append(getTplPath()).append(SPT);
                t.append(solutionPath).append(SPT);
                t.append(TPLDIR_INDEX).append(SPT);
                return t.toString();
        }

        /**
         * 根据站点统一配置获取默认模板
         *
         * @param modelId
         *                模型ID
         * @param getPcTpl
         *                是否获取pc 模板 true 是 false获取手机端模板
         * @Title: getTplByModel
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getTplByModel(Integer modelId, boolean getPcTpl) {
                SiteModelTpl targetSiteModelTpl = null;
                for (SiteModelTpl modelTpl : getModelTpls()) {
                        if (modelTpl.getModelId().equals(modelId)) {
                                targetSiteModelTpl = modelTpl;
                                break;
                        }
                }
                StringBuilder t = new StringBuilder();
                String tpl;
                if (targetSiteModelTpl != null) {
                        if (getPcTpl) {
                                tpl = targetSiteModelTpl.getPcTplPath();
                                if (StringUtils.isBlank(tpl)) {
                                        return null;
                                }
                                t.append(getPcSolutionPath()).append(SPT);
                        } else {
                                tpl = targetSiteModelTpl.getMobileTplPath();
                                if (StringUtils.isBlank(tpl)) {
                                        return null;
                                }
                                t.append(getMobileSolutionPath()).append(SPT);
                        }
                        t.append(tpl);
                        return t.toString();
                } else {
                        return null;
                }
        }

        /**
         * 获取到模板方案这一层 完整路径(类似/WEB-INF/t/cms/www/default/)
         *
         * @Title: getSolutionPath
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getSolutionPath() {
                StringBuilder t = new StringBuilder();
                t.append(getSolutionPathByDevice()).append(SPT);
                return t.toString();
        }

        /**
         * 获得节点列表。从父节点到自身。
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        @Override
        public List<CmsSite> getNodeList() {
                LinkedList<CmsSite> list = new LinkedList<CmsSite>();
                CmsSite node = this;
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
        @Transient
        @Override
        public Integer[] getNodeIds() {
                List<CmsSite> sites = getNodeList();
                // 排除自身的站点
                Integer[] ids = new Integer[sites.size()];
                int i = 0;
                for (CmsSite c : sites) {
                        ids[i++] = c.getId();
                }
                return ids;
        }

        /**
         * 获得节点列表ID。不包含自身。
         *
         * @return
         */
        @Transient
        public Integer[] getNodeSiteIds() {
                List<CmsSite> sites = getNodeList();
                // 排除自身的站点
                sites = sites.stream().filter(x -> !x.getId().equals(this.id)).collect(Collectors.toList());
                Integer[] ids = new Integer[sites.size()];
                int i = 0;
                for (CmsSite c : sites) {
                        ids[i++] = c.getId();
                }
                return ids;
        }

        /**
         * 获得深度
         *
         * @return 第一层为0，第二层为1，以此类推。
         */
        @Transient
        @Override
        public int getDeep() {
                int deep = 0;
                CmsSite parent = getParent();
                while (parent != null) {
                        deep++;
                        parent = parent.getParent();
                }
                return deep;
        }

        /** 站点基础预览地址 **/
        @Transient
        @JSONField(serialize = false)
        public String getSitePreviewUrl() {
                StringBuilder url = new StringBuilder();
                if(getGlobalConfig().getIsIntranet()){
                        url.append(getUrlDynamicForFix(true)).append(WebConstants.PREVIEW_URL);
                }else{
                        url.append(getUrlDynamic(true)).append(WebConstants.PREVIEW_URL);
                }
                return url.toString();
        }

        /** 站点预览地址 **/
        @Transient
        // @JSONField(serialize = false) 序列化时无法获取该信息
        public String getPreviewUrl() {
                StringBuilder url = new StringBuilder();
                url.append(getSitePreviewUrl()).append("?siteId=").append(this.id).append("&type=")
                                .append(WebConstants.PREVIEW_TYPE_SITE);
                return url.toString();
        }

        /**
         * 获得站点url
         *
         * @return
         */
        @Transient
        // @JSONField(serialize = false) 序列化时无法获取该信息
        public String getUrl() {
                StringBuilder url = new StringBuilder();
                /**已开启静态化并已生成静态首页*/
                if (getConfig().getOpenStatic()&&getCmsSiteCfg().getHasStaticIndexPage()) {
                        /** 生成手机静态页需要主动调用 SystemContextUtils.setPc(false); */
                        boolean urlRelative = getConfig().getUrlRelative();
                        url.append(getUrlStatic(!urlRelative, SystemContextUtils.isPc()));
                        url.append(SPT).append(INDEX).append("." + getConfig().getStaticHtmlSuffix());
                } else {
                        url.append(getUrlDynamic(true));
                }
                return url.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getHttpsUrl() {
                StringBuilder url = new StringBuilder();
                if (getConfig().getOpenStatic()) {
                        url.append(getHttpsUrlStatic());
                        url.append(SPT).append(INDEX).append("." + getConfig().getStaticHtmlSuffix());
                } else {
                        url.append(getHttpsUrlDynamic());
                }
                return url.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getAdminUrl() {
                StringBuilder url = new StringBuilder();
                url.append(getUrlDynamic());
                url.append(WebConstants.ADMIN_URL);
                return url.toString();
        }

        /**
         * 获得完整路径。在给其他网站提供客户端包含时也可以使用。
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getUrlWhole() {
                return getUrlBuffer(true).append(SPT).toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getUrlDynamic() {
                return getUrlBuffer(null).append(SPT).toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getUrlDynamic(Boolean whole) {
                StringBuilder builder = getUrlBuffer(whole);
                if (getGlobalConfig().getIsIntranet()) {
                        builder.append(INTRANET_PREFIX).append(getPath());
                }
                builder.append(SPT);
                return builder.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getUrlDynamicForFix(Boolean whole) {
                StringBuilder builder = getUrlBuffer(whole);
                builder.append(SPT);
                return builder.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getUrlStatic(Boolean whole, boolean pc) {
                StringBuilder urlBuf = getUrlStaticBuff(whole, pc);
                return urlBuf.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public StringBuilder getUrlStaticBuff(Boolean whole, boolean pc) {
                StringBuilder urlBuf = getUrlBuffer(whole);
                urlBuf.append(SPT);
                if (pc) {
                        urlBuf.append(WebConstants.STATIC_PC_PATH);
                } else {
                        urlBuf.append(WebConstants.STATIC_MOBILE_PATH);
                }
                urlBuf.append(getId());
                return urlBuf;
        }

        @Transient
        @JSONField(serialize = false)
        public String getHttpsUrlDynamic() {
                return getHttpsUrlBuffer(null).append(SPT).toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getHttpsUrlStatic() {
                return getHttpsUrlBuffer(null).append(SPT).toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getUrlPrefix() {
                StringBuilder url = new StringBuilder();
                url.append(getProtocol()).append(getDomain());
                if (getPort() != null) {
                        url.append(":").append(getPort());
                }
                return url.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getUrlPrefixWithNoDefaultPort() {
                StringBuilder url = new StringBuilder();
                url.append(getProtocol()).append(getDomain());
                int num = 80;
                if (getPort() != null && getPort() != num) {
                        url.append(":").append(getPort());
                }
                return url.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public String getSafeUrlPrefix() {
                StringBuilder url = new StringBuilder();
                url.append("https://").append(getDomain());
                int port1 = 80;
                int port2 = 443;
                if (getPort() != null && getPort() != port1 && getPort() != port2) {
                        url.append(":").append(getPort());
                }
                return url.toString();
        }

        @Transient
        @JSONField(serialize = false)
        public StringBuilder getUrlBuffer(Boolean whole) {
                boolean relative = whole != null ? !whole : !getConfig().getUrlRelative();
                String ctx = getContextPath();
                StringBuilder url = new StringBuilder();
                if (!relative) {
                        url.append(getProtocol()).append(getDomain());
                        int port = 80;
                        if (getPort() != null && getPort() != port) {
                                url.append(":").append(getPort());
                        }
                }
                if (!StringUtils.isBlank(ctx)) {
                        url.append(ctx);
                }
                return url;
        }

        @Transient
        @JSONField(serialize = false)
        public StringBuilder getHttpsUrlBuffer(Boolean whole) {
                boolean relative = whole != null ? !whole : true;
                String ctx = getContextPath();
                StringBuilder url = new StringBuilder();
                if (!relative) {
                        url.append("https://").append(getDomain());
                        int port = 80;
                        if (getPort() != null && getPort() != port) {
                                url.append(":").append(getPort());
                        }
                }
                if (!StringUtils.isBlank(ctx)) {
                        url.append(ctx);
                }
                return url;
        }

        @Transient
        @JSONField(serialize = false)
        public StringBuilder getMobileUrlBuffer(Boolean whole) {
                boolean relative = whole != null ? !whole : true;
                String ctx = getContextPath();
                StringBuilder url = new StringBuilder();
                if (!relative) {
                        url.append(getProtocol()).append(getDomain());
                        if (getPort() != null) {
                                url.append(":").append(getPort());
                        }
                }
                if (!StringUtils.isBlank(ctx)) {
                        url.append(ctx);
                }
                return url;
        }

        /**
         * 获得模板路径。如：/WEB-INF/t/cms/www
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getTplPath() {
                return TPL_BASE + SPT + getPath();
        }

        /**
         * 获得资源路径。如：/r/cms/www
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getResourcePath() {
                return RES_PATH + SPT + getPath();
        }

        /**
         * 获取模板方案路径根据当前访问终端动态识别
         *
         * @Title: getSolutionPath
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getSolutionPathByDevice() {
                if (SystemContextUtils.isPc()) {
                        return getPcSolutionPath();
                } else if (SystemContextUtils.isTablet()) {
                        return getTabletSolutionPath();
                }
                return getMobileSolutionPath();
        }

        /**
         * 获得模板方案路径。如：/WEB-INF/t/cms/www/default
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getPcSolutionPath() {
                return TPL_BASE + SPT + getPath() + SPT + getPcSolution();
        }

        @Transient
        @JSONField(serialize = false)
        public String getMobileSolutionPath() {
                return TPL_BASE + SPT + getPath() + SPT + getMobileSolution();
        }

        @Transient
        @JSONField(serialize = false)
        public String getTabletSolutionPath() {
                return TPL_BASE + SPT + getPath() + SPT + getTabletSolution();
        }

        /**
         * 获得模板资源路径。如：/r/cms/www/default/
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getResPath() {
                return RES_PATH + SPT + getPath() + SPT + getPcSolution();
        }

        /**
         * 获得手机模板资源路径。如：/r/cms/www/mobile/
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getMobileResPath() {
                return RES_PATH + SPT + getPath() + SPT + getMobileSolution();
        }

        /**
         * 获得平板模板资源路径。如：/r/cms/www/tablet/
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getTabletResPath() {
                return RES_PATH + SPT + getPath() + SPT + getTabletSolution() + SPT;
        }

        /**
         * 获得上传路径。如：/u/jeecms/path
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getUploadPath() {
                return UPLOAD_PATH + getPath();
        }

        /**
         * 获得上传访问前缀。
         * <p>
         * 根据配置识别上传至数据、FTP、OSS和本地
         *
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public String getUploadBase() {
                String ctx = getContextPath();
                // if (getUploadFtp() != null) {
                // return getUploadFtp().getUrl();
                // } else if (getUploadOss() != null) {
                // return getUploadOss().getAccessDomain();
                // } else {
                //
                // }
                if (!StringUtils.isBlank(ctx)) {
                        return ctx;
                } else {
                        return "";
                }
        }

        @Transient
        @JSONField(serialize = false)
        public String getContextPath() {
                return getGlobalConfig().getConfigAttr().getDeploymentPath();
                // return RequestUtils.getHttpServletRequest().getContextPath();
        }

        @Transient
        @JSONField(serialize = false)
        public Integer getPort() {
                GlobalConfig config = getGlobalConfig();
                if (config != null) {
                        return config.getConfigAttr().getServerPort();
                } else {
                        return null;
                }
        }

        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<CmsSite> sites) {
                if (sites == null) {
                        return null;
                }
                List<Integer> ids = new ArrayList<Integer>();
                for (CmsSite s : sites) {
                        ids.add(s.getId());
                }
                return ids;
        }

        @Transient
        @JSONField(serialize = false)
        public String getBaseDomain() {
                String domain = getDomain();
                String indexKey = ".";
                if (domain.indexOf(indexKey) > -1) {
                        return domain.substring(domain.indexOf(".") + 1);
                }
                return domain;
        }

        /**
         * 获取所有子栏目包含自身
         *
         * @Title: getChildAll
         * @return: List
         */
        @Transient
        public List<CmsSite> getChildAll() {
                return getListForSelect(null, null);
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param rights
         *                有权限的站点，为null不控制权限。
         * @param exclude
         *                排除的站点
         * @return List
         */
        @Transient
        public List<CmsSite> getListForSelect(Set<CmsSite> rights, CmsSite exclude) {
                List<CmsSite> list = new ArrayList<CmsSite>((getRgt() - getLft()) / 2);
                addChildToList(list, this, rights, exclude);
                return list;
        }

        /**
         * 递归将子站点加入列表。
         *
         * @param list
         *                栏目容器
         * @param site
         *                待添加的站点，且递归添加子站点
         * @param rights
         *                有权限的站点，为null不控制权限。
         */
        private static void addChildToList(List<CmsSite> list, CmsSite site, Set<CmsSite> rights, CmsSite exclude) {
                boolean hasNotContaind = (rights != null && !rights.contains(site))
                                || (exclude != null && exclude.equals(site));
                if (hasNotContaind) {
                        return;
                }
                addChildToList(list, site);
        }

        private static void addChildToList(List<CmsSite> list, CmsSite site) {
                if (list != null && CmsSite.fetchIds(list).contains(site.getId())) {
                        return;
                }
                list.add(site);
                List<CmsSite> child = site.getChilds();
                for (CmsSite c : child) {
                        addChildToList(list, c);
                }
        }

        /**
         * 初始化站点
         **/
        public void init() {
                if (StringUtils.isBlank(getProtocol())) {
                        setProtocol("https://");
                }
                if (StringUtils.isBlank(getSiteLanguage())) {
                        setSiteLanguage(WebConstants.LAN_ZH_CN);
                }
                if (getSortNum() == null) {
                        setSortNum(10);
                }
                if (getIsOpen() == null) {
                        setIsOpen(false);
                }
                if (getIsDelete() == null) {
                        setIsDelete(false);
                }
        }

        /**
         * 获取当前设备访问适用的模板方案
         *
         * @Title: getSolutionByDevice
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getSolutionByDevice() {
                if (SystemContextUtils.isPc()) {
                        return getPcSolution();
                } else if (SystemContextUtils.isTablet()) {
                        return getTabletSolution();
                }
                return getMobileSolution();
        }

        @Transient
        public String getPcHomePageTemplates() {
                return getCmsSiteCfg().getPcHomePageTemplates();
        }

        @Transient
        public String getMobileHomePageTemplates() {
                return getCmsSiteCfg().getMobileHomePageTemplates();
        }

        @Transient
        public String getPcSolution() {
                return getCmsSiteCfg().getPcSolution();
        }

        @Transient
        public String getMobileSolution() {
                return getCmsSiteCfg().getMobileSolution();
        }

        @Transient
        public String getTabletSolution() {
                return getCmsSiteCfg().getTabletSolution();
        }

        @Transient
        @JSONField(serialize = false)
        public CmsSiteConfig getConfig() {
                CmsSiteConfig configAttr = new CmsSiteConfig(getCfg());
                return configAttr;
        }

        /**
         * 是否开启静态化
         *
         * @Title: getOpenStatic
         * @return: boolean
         */
        @Transient
        @JSONField(serialize = false)
        public boolean getOpenStatic() {
                return getConfig().getOpenStatic();
        }

        /**
         * 是否开启PC静态化
         *
         * @Title: getOpenPcStatic
         * @return: boolean
         */
        @Transient
        @JSONField(serialize = false)
        public boolean getOpenPcStatic() {
                return getConfig().getOpenPcStatic();
        }

        /**
         * 是否开启手机静态化
         *
         * @Title: getOpenMobileStatic
         * @return: boolean
         */
        @Transient
        @JSONField(serialize = false)
        public boolean getOpenMobileStatic() {
                return getConfig().getOpenMobileStatic();
        }

        /**
         * PC首页静态页目录
         *
         * @Title: getStaticIndexForPcPagePath
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getStaticIndexForPcPagePath() {
                return WebConstants.STATIC_PC_PATH + getId() + SPT + WebConstants.INDEX + "."
                                + getConfig().getStaticHtmlSuffix();
        }

        /**
         * 手机端首页静态页目录
         *
         * @Title: getStaticIndexForPcPagePath
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getStaticIndexForMobilePagePath() {
                return WebConstants.STATIC_MOBILE_PATH + getId() + SPT + WebConstants.INDEX + "."
                                + getConfig().getStaticHtmlSuffix();
        }

        @Transient
        public UploadOss getUploadOss() {
                return getCmsSiteExt().getUploadOss();
        }

        @Transient
        public UploadFtp getUploadFtp() {
                return getCmsSiteExt().getUploadFtp();
        }

        @Transient
        public UploadFtp getStaticPageFtp() {
                return getCmsSiteExt().getStaticPageFtp();
        }

        @Transient
        public UploadOss getStaticPageOss() {
                return getCmsSiteExt().getStaticPageOss();
        }

        @Transient
        public String getWatermarkRes() {
                if (getCmsSiteExt().getWatermarkResource() != null) {
                        return getCmsSiteExt().getWatermarkResource().getUrl();
                }
                return "";
        }

        @Transient
        public String getNewContentRes() {
                return getCmsSiteExt().getNewContentRes();
        }

        /**
         * 是否登录后才能访问站点
         *
         * @Title: getLoginSuccessVisitSite
         * @return: boolean true是 默认否
         */
        @Transient
        public boolean getLoginSuccessVisitSite() {
                return getCmsSiteCfg().getLoginSuccessVisitSite();
        }

        /**
         * 是否允许上传后缀的图片
         */
        @Transient
        public boolean isAllowPicSuffix(String ext) {
                List<String> suffix = getUploadPicSuffix();
                if (suffix.isEmpty()) {
                        return true;
                }
                if (suffix.contains(ext)) {
                        return true;
                }
                return false;
        }

        /**
         * 是否允许上传的图片，根据文件
         */
        @Transient
        public boolean isAllowPicMaxFile(int size) {
                Integer allowPerFile = getUploadPicSize();
                if (null == allowPerFile || 0 == allowPerFile) {
                        return true;
                } else {
                        return allowPerFile >= size;
                }
        }

        /**
         * 是否允许上传后缀的附件
         */
        @Transient
        public boolean isAllowAttachmentSuffix(String ext) {
        		UploadLimitType limitType = getUploadAttachmentLimitType();
        		if(UploadLimitType.no.equals(limitType)){
        			return true;
        		}
    			List<String> suffix = getUploadAttachmentSuffix();
        		if(UploadLimitType.white.equals(limitType)){
                    if (suffix.isEmpty()) {
                            return false;
                    }
                    if (suffix.contains(ext)) {
                            return true;
                    }
                    return false;
        		}else{
        			if (suffix.isEmpty()) {
                        return true;
	                }
	                if (suffix.contains(ext)) {
	                        return false;
	                }
	                return true;
        		}
        }

        /**
         * 是否允许上传的附件，根据文件大小
         */
        @Transient
        public boolean isAllowAttachmentMaxFile(int size) {
                Integer allowPerFile = getUploadAttachmentSize();
                if (null == allowPerFile || 0 == allowPerFile) {
                        return true;
                } else {
                        return allowPerFile >= size;
                }
        }

        /**
         * 是否允许上传后缀的音频
         */
        @Transient
        public boolean isAllowAudioSuffix(String ext) {
                List<String> suffix = getUploadAudioSuffix();
                if (suffix.isEmpty()) {
                        return true;
                }
                if (suffix.contains(ext)) {
                        return true;
                }
                return false;
        }

        /**
         * 是否允许上传的音频，根据文件大小
         */
        @Transient
        public boolean isAllowAudioMaxFile(int size) {
                Integer allowPerFile = getUploadAudioSize();
                if (null == allowPerFile || 0 == allowPerFile) {
                        return true;
                } else {
                        return allowPerFile >= size;
                }
        }

        /**
         * 是否允许上传后缀的视频
         */
        @Transient
        public boolean isAllowVideoSuffix(String ext) {
                List<String> suffix = getUploadVideoSuffix();
                if (suffix.isEmpty()) {
                        return true;
                }
                if (suffix.contains(ext)) {
                        return true;
                }
                return false;
        }

        /**
         * 是否允许上传的视频，根据文件大小
         */
        @Transient
        public boolean isAllowVideoMaxFile(int size) {
                Integer allowPerFile = getUploadVideoSize();
                if (null == allowPerFile || 0 == allowPerFile) {
                        return true;
                } else {
                        return allowPerFile >= size;
                }
        }

        /**
         * 允许上传图片类型
         */
        @Transient
        public List<String> getUploadPicSuffix() {
                return this.getCmsSiteCfg().getUploadPicSuffix();
        }

        @Transient
        public Integer getUploadPicSize() {
                Integer uploadMaxSize = this.getCmsSiteCfg().getUploadPicSize();
                String uploadSizeUnit = this.getCmsSiteCfg().getUploadPicSizeType();
                if (CmsSiteConfig.UPLOAD_SIZE_UNIT_MB.equals(uploadSizeUnit)) {
                        uploadMaxSize = uploadMaxSize * 1024;
                }
                return uploadMaxSize;
        }

        /**
         * 允许上传视频类型
         */
        @Transient
        public List<String> getUploadVideoSuffix() {
                return this.getCmsSiteCfg().getUploadVideoSuffix();
        }

        @Transient
        public Integer getUploadVideoSize() {
                Integer uploadMaxSize = this.getCmsSiteCfg().getUploadVideoSize();
                String uploadSizeUnit = this.getCmsSiteCfg().getUploadVideoSizeType();
                if (CmsSiteConfig.UPLOAD_SIZE_UNIT_MB.equals(uploadSizeUnit)) {
                        uploadMaxSize = uploadMaxSize * 1024;
                }
                return uploadMaxSize;
        }

        /**
         * 允许上传附件类型
         */
        @Transient
        public List<String> getUploadAttachmentSuffix() {
                return this.getCmsSiteCfg().getUploadAttachmentSuffix();
        }
        
        /**
         * 附件上传限制类型
         * @Title: getUploadAttachmentLimitType
         * @return: UploadLimitType ${@link UploadLimitType}}
         */
        @Transient
        public UploadLimitType getUploadAttachmentLimitType(){
        	return this.getCmsSiteCfg().getUploadAttachmentLimitType();
        }

        @Transient
        public Integer getUploadAttachmentSize() {
                Integer uploadMaxSize = this.getCmsSiteCfg().getUploadAttachmentSize();
                String uploadSizeUnit = this.getCmsSiteCfg().getUploadAttachmentSizeType();
                if (CmsSiteConfig.UPLOAD_SIZE_UNIT_MB.equals(uploadSizeUnit)) {
                        uploadMaxSize = uploadMaxSize * 1024;
                }
                return uploadMaxSize;
        }

        /**
         * 允许上传音频类型
         */
        @Transient
        public List<String> getUploadAudioSuffix() {
                return this.getCmsSiteCfg().getUploadAudioSuffix();
        }

        @Transient
        public Integer getUploadAudioSize() {
                Integer uploadMaxSize = this.getCmsSiteCfg().getUploadAudioSize();
                String uploadSizeUnit = this.getCmsSiteCfg().getUploadAudioSizeType();
                if (CmsSiteConfig.UPLOAD_SIZE_UNIT_MB.equals(uploadSizeUnit)) {
                        uploadMaxSize = uploadMaxSize * 1024;
                }
                return uploadMaxSize;
        }

        /**
         * 允许上传文档类型
         */
        @Transient
        public List<String> getUploadDocumentSuffix() {
                return this.getCmsSiteCfg().getUploadDocumentSuffix();
        }

        @Transient
        public UploadEnum.UploadServerType getUploadServerType() {
                return this.getCmsSiteCfg().getUploadServerType();
        }

        @Transient
        public UploadEnum.UploadServerType getStaticServerType() {
                return this.getCmsSiteCfg().getStaticServerType();
        }

        @Transient
        public Integer getAllowPicFileMaxSize() {
                return getUploadPicSize();
        }

        @Transient
        public void setConfig(CmsSiteConfig configAttr) {
                getCfg().putAll(configAttr.getAttr());
        }

        /**
         * 站点数据是否可修改
         *
         * @return boolean
         * @Title: getEditable
         */
        @Transient
        public boolean getEditAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 用户未登录则不可修改 */
                if (user == null) {
                        return false;
                }
                if (user.getEditSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可删除
         *
         * @return boolean
         * @Title: getDeleteAble
         */
        @Transient
        public boolean getDeleteAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                /**主站1 不能删除*/
                if(getId()==1){
                        return false;
                }
                if (user.getDelSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可开启关闭
         *
         * @Title: getOpenCloseAble
         * @return: boolean
         */
        @Transient
        public boolean getOpenCloseAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getOpenCloseSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可查看
         *
         * @return boolean
         * @Title: getViewAble
         */
        @Transient
        public boolean getViewAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getViewSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可分配权限
         */
        @Transient
        public boolean getPermAssignAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getPermAssignSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可创建子站点
         *
         * @return boolean
         * @Title: getNewChildAble
         */
        @Transient
        public boolean getNewChildAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getNewChildSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否可静态化
         */
        @Transient
        public boolean getStaticAble() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (user == null) {
                        return false;
                }
                if (user.getStaticSiteIds().contains(getId())) {
                        return true;
                }
                return false;
        }

        /**
         * 是否接受网站群推送
         */
        @Transient
        public Boolean getSitePushOpen() {
                return this.getCmsSiteCfg().getSitePushOpen();
        }

        /**
         * 会员登录跳转地址 为null则不设定跳转地址
         * 
         * @Title: getMemberRedirectUrl
         * @return: String
         */
        @Transient
        public String getMemberRedirectUrl() {
                String siteAssignRedirect = getConfig().getMemberRedirectAssign();
                if (StringUtils.isNoneBlank(siteAssignRedirect)
                                && CmsSiteConfig.TRUE_STRING.equals(siteAssignRedirect)) {
                        return getConfig().getMemberRedirectUrl();
                }
                return null;
        }

        /**
         * 获取系统标识ICO url
         * @Title: getSystemIco
         * @return: String
         */
        @Transient
        public String getSystemIco() {
                GlobalConfig config = getGlobalConfig();
                return config.getConfigAttr().getSystemFlagResourceUrl();
        }

        @Transient
        public void uploadFileToRemote(Short distributeType, String path, InputStream in) {
                if (ContentConstant.DISTRIBUTE_TYPE_UPLOAD.equals(distributeType)) {
                        if (UploadServerType.ftp.equals(getUploadServerType()) && getUploadFtp() != null) {
                                try {
                                        getUploadFtp().storeByExt(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                }
                        } else if (UploadServerType.oss.equals(getUploadServerType()) && getUploadOss() != null) {
                                try {
                                        getUploadOss().storeByFilename(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                } catch (GlobalException e) {
                                        log.error(e.getMessage());
                                }
                        }
                } else if (ContentConstant.DISTRIBUTE_TYPE_HTML.equals(distributeType)) {
                        if (UploadServerType.ftp.equals(getStaticServerType()) && getStaticPageFtp() != null) {
                                try {
                                        getStaticPageFtp().storeByExt(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                }
                        } else if (UploadServerType.oss.equals(getStaticServerType()) && getStaticPageOss() != null) {
                                try {
                                        getStaticPageOss().storeByFilename(path, in);
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                } catch (GlobalException e) {
                                        log.error(e.getMessage());
                                } finally {
                                        if (in != null) {
                                                try {
                                                        in.close();
                                                } catch (IOException e) {
                                                        log.error(e.getMessage());
                                                }
                                        }
                                }
                        }
                }
        }

        @Transient
        public void deleteRemoteFile(Short distributeType, String path) {
                if (ContentConstant.DISTRIBUTE_TYPE_UPLOAD.equals(distributeType)) {
                        if (UploadServerType.ftp.equals(getUploadServerType()) && getUploadFtp() != null) {
                                getUploadFtp().deleteFile(path);
                        } else if (UploadServerType.oss.equals(getUploadServerType()) && getUploadOss() != null) {
                                getUploadOss().deleteFile(path);
                        }
                } else if (ContentConstant.DISTRIBUTE_TYPE_HTML.equals(distributeType)) {
                        if (UploadServerType.ftp.equals(getStaticServerType()) && getStaticPageFtp() != null) {
                                getStaticPageFtp().deleteFile(path);
                        } else if (UploadServerType.oss.equals(getStaticServerType()) && getStaticPageOss() != null) {
                                getStaticPageOss().deleteFile(path);
                        }
                }
        }

        public CmsSite() {
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_site", pkColumnValue = "jc_site", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_site")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(length = 500)
        public String getDescription() {
                return this.description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        @Column(name = "icon_id", insertable = false, updatable = false)
        public Integer getIconId() {
                return iconId;
        }

        public void setIconId(Integer iconId) {
                this.iconId = iconId;
        }

        @Column(name = "seo_description", length = 500)
        public String getSeoDescription() {
                return this.seoDescription;
        }

        public void setSeoDescription(String seoDescription) {
                this.seoDescription = seoDescription;
        }

        @Column(name = "seo_keyword", length = 255)
        public String getSeoKeyword() {
                return this.seoKeyword;
        }

        public void setSeoKeyword(String seoKeyword) {
                this.seoKeyword = seoKeyword;
        }

        @Column(name = "seo_title", length = 255)
        public String getSeoTitle() {
                return this.seoTitle;
        }

        public void setSeoTitle(String seoTitle) {
                this.seoTitle = seoTitle;
        }

        @NotNull
        @Column(name = "site_domain", nullable = false, length = 150)
        public String getDomain() {
                return this.domain;
        }

        public void setDomain(String domain) {
                this.domain = domain;
        }

        @Column(name = "site_domain_alias", length = 1000)
        public String getDomainAlias() {
                return this.domainAlias;
        }

        public void setDomainAlias(String domainAlias) {
                this.domainAlias = domainAlias;
        }

        @Column(name = "site_language", length = 50)
        public String getSiteLanguage() {
                return this.siteLanguage;
        }

        public void setSiteLanguage(String siteLanguage) {
                this.siteLanguage = siteLanguage;
        }

        @NotNull
        @Column(name = "site_name", nullable = false, length = 150)
        public String getName() {
                return this.name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @NotNull
        @Column(name = "site_path", nullable = false, length = 50)
        public String getPath() {
                return this.path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        @Column(name = "site_protocol", nullable = false, length = 20)
        public String getProtocol() {
                return this.protocol;
        }

        public void setProtocol(String protocol) {
                this.protocol = protocol;
        }

        @Column(name = "pc_index_tpl")
        public String getPcIndexTpl() {
                return pcIndexTpl;
        }

        @Column(name = "mobile_index_tpl")
        public String getMobileIndexTpl() {
                return mobileIndexTpl;
        }

        @Column(name = "is_delete")
        public Boolean getIsDelete() {
                return isDelete;
        }

        public void setIsDelete(Boolean isDelete) {
                this.isDelete = isDelete;
        }

        public void setPcIndexTpl(String pcIndexTpl) {
                this.pcIndexTpl = pcIndexTpl;
        }

        public void setMobileIndexTpl(String mobileIndexTpl) {
                this.mobileIndexTpl = mobileIndexTpl;
        }

        @Column(nullable = false, name = "is_open")
        public Boolean getIsOpen() {
                return this.isOpen;
        }

        public void setIsOpen(Boolean isOpen) {
                this.isOpen = isOpen;
        }

        @ElementCollection
        @CollectionTable(name = "jc_site_cfg", joinColumns = {
                        @JoinColumn(insertable = false, updatable = false, name = "site_id") })
        @MapKeyColumn(name = "cfg_key", length = 70)
        @Column(name = "cfg_value", length = 120)
        public java.util.Map<java.lang.String, java.lang.String> getCfg() {
                return cfg;
        }

        public void setCfg(java.util.Map<java.lang.String, java.lang.String> cfg) {
                this.cfg = cfg;
        }

        @Transient
        public CmsSiteConfig getCmsSiteCfg() {
                CmsSiteConfig siteCfg = new CmsSiteConfig(getCfg());
                return siteCfg;
        }

        @Transient
        public void setCmsSiteCfg(CmsSiteConfig siteCfg) {
                if (getCfg() != null) {
                        getCfg().putAll(siteCfg.getAttr());
                } else {
                        Map<String, String> map = new HashMap<String, String>(16);
                        map.putAll(siteCfg.getAttr());
                        setCfg(map);
                }
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "icon_id")
        @NotFound(action = NotFoundAction.IGNORE)
        public ResourcesSpaceData getIconRes() {
                return iconRes;
        }

        public void setIconRes(ResourcesSpaceData iconRes) {
                this.iconRes = iconRes;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "global_config_id")
        public GlobalConfig getGlobalConfig() {
                return globalConfig;
        }

        public void setGlobalConfig(GlobalConfig globalConfig) {
                this.globalConfig = globalConfig;
        }

        @Column(nullable = false, name = "global_config_id", insertable = false, updatable = false)
        public Integer getGlobalConfigId() {
                return globalConfigId;
        }

        public void setGlobalConfigId(Integer globalConfigId) {
                this.globalConfigId = globalConfigId;
        }

        @OneToOne(fetch = FetchType.LAZY, mappedBy = "cmsSite", cascade = { CascadeType.REMOVE, CascadeType.PERSIST,
                        CascadeType.MERGE })
        public CmsSiteExt getCmsSiteExt() {
                return cmsSiteExt;
        }

        public void setCmsSiteExt(CmsSiteExt cmsSiteExt) {
                this.cmsSiteExt = cmsSiteExt;
        }

        @OneToMany(mappedBy = "site")
        public List<Channel> getChannels() {
                return channels;
        }

        public void setChannels(List<Channel> channels) {
                this.channels = channels;
        }

        @OneToMany(mappedBy = "site")
        public List<SiteModelTpl> getModelTpls() {
                return modelTpls;
        }

        public void setModelTpls(List<SiteModelTpl> modelTpls) {
                this.modelTpls = modelTpls;
        }

        @Column(name = "sort_weight", length = 11)
        public Integer getSortWeight() {
                return sortWeight;
        }

        public void setSortWeight(Integer sortWeight) {
                this.sortWeight = sortWeight;
        }

        /**
         * 重写 hashCode
         *
         * @return int
         * @Title: hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((description == null) ? 0 : description.hashCode());
                result = prime * result + ((domain == null) ? 0 : domain.hashCode());
                result = prime * result + ((domainAlias == null) ? 0 : domainAlias.hashCode());
                result = prime * result + ((globalConfigId == null) ? 0 : globalConfigId.hashCode());
                result = prime * result + ((iconId == null) ? 0 : iconId.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((isDelete == null) ? 0 : isDelete.hashCode());
                result = prime * result + ((isOpen == null) ? 0 : isOpen.hashCode());
                result = prime * result + ((mobileIndexTpl == null) ? 0 : mobileIndexTpl.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((path == null) ? 0 : path.hashCode());
                result = prime * result + ((pcIndexTpl == null) ? 0 : pcIndexTpl.hashCode());
                result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
                result = prime * result + ((seoDescription == null) ? 0 : seoDescription.hashCode());
                result = prime * result + ((seoKeyword == null) ? 0 : seoKeyword.hashCode());
                result = prime * result + ((seoTitle == null) ? 0 : seoTitle.hashCode());
                result = prime * result + ((siteLanguage == null) ? 0 : siteLanguage.hashCode());
                return result;
        }

        /**
         * 重写 equals
         *
         * @param obj
         *                Object
         * @return boolean
         * @Title: equals
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
                CmsSite other = (CmsSite) obj;
                if (description == null) {
                        if (other.description != null) {
                                return false;
                        }
                } else if (!description.equals(other.description)) {
                        return false;
                }
                if (domain == null) {
                        if (other.domain != null) {
                                return false;
                        }
                } else if (!domain.equals(other.domain)) {
                        return false;
                }
                if (domainAlias == null) {
                        if (other.domainAlias != null) {
                                return false;
                        }
                } else if (!domainAlias.equals(other.domainAlias)) {
                        return false;
                }
                if (globalConfigId == null) {
                        if (other.globalConfigId != null) {
                                return false;
                        }
                } else if (!globalConfigId.equals(other.globalConfigId)) {
                        return false;
                }
                if (iconId == null) {
                        if (other.iconId != null) {
                                return false;
                        }
                } else if (!iconId.equals(other.iconId)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (isDelete == null) {
                        if (other.isDelete != null) {
                                return false;
                        }
                } else if (!isDelete.equals(other.isDelete)) {
                        return false;
                }
                if (isOpen == null) {
                        if (other.isOpen != null) {
                                return false;
                        }
                } else if (!isOpen.equals(other.isOpen)) {
                        return false;
                }
                if (mobileIndexTpl == null) {
                        if (other.mobileIndexTpl != null) {
                                return false;
                        }
                } else if (!mobileIndexTpl.equals(other.mobileIndexTpl)) {
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
                if (pcIndexTpl == null) {
                        if (other.pcIndexTpl != null) {
                                return false;
                        }
                } else if (!pcIndexTpl.equals(other.pcIndexTpl)) {
                        return false;
                }
                if (protocol == null) {
                        if (other.protocol != null) {
                                return false;
                        }
                } else if (!protocol.equals(other.protocol)) {
                        return false;
                }
                if (seoDescription == null) {
                        if (other.seoDescription != null) {
                                return false;
                        }
                } else if (!seoDescription.equals(other.seoDescription)) {
                        return false;
                }
                if (seoKeyword == null) {
                        if (other.seoKeyword != null) {
                                return false;
                        }
                } else if (!seoKeyword.equals(other.seoKeyword)) {
                        return false;
                }
                if (seoTitle == null) {
                        if (other.seoTitle != null) {
                                return false;
                        }
                } else if (!seoTitle.equals(other.seoTitle)) {
                        return false;
                }
                if (siteLanguage == null) {
                        if (other.siteLanguage != null) {
                                return false;
                        }
                } else if (!siteLanguage.equals(other.siteLanguage)) {
                        return false;
                }
                return true;
        }

        @Override
        public CmsSite clone() {
                CmsSite clone = null;
                try {
                        clone = (CmsSite) super.clone();
                } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e); // won't happen
                }
                return clone;
        }

        /**
         * 获取别名集合
         * 
         * @return
         * @Title: getAlias
         */
        @Transient
        public List<JSONObject> getAlias() {
                List<JSONObject> objects = new ArrayList<JSONObject>(10);
                if (StringUtils.isNotBlank(domainAlias)) {
                        String[] s = domainAlias.split(",");
                        for (String string : s) {
                                JSONObject obj = new JSONObject();
                                obj.put("herf", string);
                                objects.add(obj);
                        }
                }
                return objects;
        }

        /**
         * 父结点全部集合(过滤主站点)
         */
        @Transient
        public List<CmsSite> getSiteParents(CmsSite site) {
                List<CmsSite> list = new ArrayList<CmsSite>(10);
                list.add(site);
                parentMethod(site, list);
                list = list.stream().filter(x -> x.getParentId() != null).collect(Collectors.toList());
                return list;
        }

        /**
         * 父结点方法
         */
        @Transient
        public void parentMethod(CmsSite site, List<CmsSite> list) {
                // 只还原到主站点以下的一级
                if (site.getParentId() != null) {
                        list.add(site.getParent());
                        parentMethod(site.getParent(), list);
                }
        }

}