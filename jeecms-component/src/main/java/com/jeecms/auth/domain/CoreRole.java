/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.constants.ServerModeEnum;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.util.PropertiesUtil;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsDataPermCfg;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.CmsSiteAgent;
import com.jeecms.system.exception.SiteExceptionInfo;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 用户角色实体类
 * 
 * @author: ljw
 * @date: 2018年3月26日 上午11:10:34
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_sys_role")
public class CoreRole extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;

        private static Logger logger = LoggerFactory.getLogger(CoreUser.class);
        /** 唯一标识符 **/
        private int id;
        /** 角色名称 **/
        private String roleName;
        /** 描述 **/
        private String description;
        /** 菜单ID **/
        private Integer[] menuid;
        /** 组织ID **/
        private Integer orgid;
        /** 菜单集合 **/
        private List<CoreMenu> menus;
        /** 管理员集合 **/
        private List<CoreUser> users = new ArrayList<CoreUser>(10);
        /** 组织集合 **/
        private CmsOrg org;
        /** 角色所有数据权限 */
        private Set<CmsDataPerm> dataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
        /** 角色管理站点 */
        private Set<CmsSite> sites = new HashSet<CmsSite>(10);
        /** 新增权限配置 */
        private List<CmsDataPermCfg> permCfgs;
        private String serverMode;

        public void setServerMode(String serverMode) {
                this.serverMode = serverMode;
        }

        /**
         * 是否不是当前登录用户所属角色
         * 
         * @Title: getNotCurrUserRole
         * @return: boolean
         */
        @Transient
        public boolean getNotCurrUserRole() {
                CoreUser user = SystemContextUtils.getCoreUser();
                if (user != null) {
                        if (user.getRoleIds().contains(getId())) {
                                return false;
                        }
                }
                return true;

        }

        /**
         * 获取配置模式
         * 
         * @Title: getServerMode
         * @return String
         */
        @Transient
        public String getServerMode() {
                if (StringUtils.isBlank(serverMode)) {
                        try {
                                String mode = PropertiesUtil.loadSystemProperties()
                                                .getProperty("spring.profiles.active");
                                setServerMode(mode);
                        } catch (IOException e) {
                                logger.error(e.getMessage());
                        }
                }
                return serverMode;
        }

        @Transient
        public CopyOnWriteArraySet<CmsSite> getCloneOwnerSites() {
                CopyOnWriteArraySet<CmsSite> cloneSites = new CopyOnWriteArraySet<CmsSite>();
                Iterator<CmsSite> iterator = getOwnerSites().iterator();
                while (iterator.hasNext()) {
                        cloneSites.add(iterator.next().clone());
                }
                return cloneSites;
        }

        /**
         * 获取角色站群权限，若角色站群未配置，则获取组织站群权限
         * 
         * @Title: getOwnerSites
         * @return Set
         */
        @Transient
        public CopyOnWriteArraySet<CmsSite> getOwnerSites() {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                String cacheKey = CmsDataPerm.getRoleCacheKey(CmsDataPerm.DATA_TYPE_SITE_OWNER);
                if (StringUtils.isNoneBlank(cacheKey)) {
                        Object cacheDataPerms = cache.getCache(cacheKey, getId().toString());
                        if (cacheDataPerms != null) {
                                CopyOnWriteArraySet<CmsSite> set = (CopyOnWriteArraySet<CmsSite>) cacheDataPerms;
                                if (set != null && set.size() > 0) {
                                        CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                                        List<CmsSite> sites =siteService.findByIds(CmsSite.fetchIds(set));
                                        set.clear();
                                        set.addAll(sites);
                                        return set;
                                }
                        }
                }
                CopyOnWriteArraySet<CmsSite> ownerSites = new CopyOnWriteArraySet<CmsSite>();
                if (getHasAssignOwnerSite()) {
                        Set<CmsSite> sites = getEffectSites();
                        ownerSites.addAll(sites);
                } else {
                        ownerSites.addAll(getOrg().getCloneOwnerSites());
                }
                ownerSites = filterOwnerSites(ownerSites);
                if (StringUtils.isNoneBlank(cacheKey) && ownerSites != null && ownerSites.size() > 0) {
                        CmsSiteAgent.initSiteChild(ownerSites);
                        cache.setCache(cacheKey, getId().toString(), ownerSites);
                }
                return ownerSites;
        }

        private CopyOnWriteArraySet<CmsSite> filterOwnerSites(CopyOnWriteArraySet<CmsSite> ownerSites) {
                if (getOrg() != null) {
                        CopyOnWriteArraySet<CmsSite> parentOwnerSites = getOrg().getOwnerSites();
                        for (CmsSite site : ownerSites) {
                                if (!CmsSite.fetchIds(parentOwnerSites).contains(site.getId())) {
                                        ownerSites.remove(site);
                                }
                        }
                }
                return ownerSites;
        }

        /**
         * 是否单独分配了站群权限
         * 
         * @Title: getHasAssignOwnerSite
         * @return boolean
         */
        @Transient
        public boolean getHasAssignOwnerSite() {
                Set<CmsSite> sites = getEffectSites();
                if (sites != null && !sites.isEmpty()) {
                        return true;
                }
                if (getHasAssignNewSiteOwner() != null && getHasAssignNewSiteOwner()) {
                        return true;
                }
                return false;
        }

        /**
         * 获取角色菜单权限，若角色菜单未配置，则获取组织菜单权限
         * 
         * @Title: getOwnerMenus
         * @return Set
         */
        @Transient
        public Set<CoreMenu> getOwnerMenus() {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                String cacheKey = CmsDataPerm.getRoleCacheKey(CmsDataPerm.DATA_TYPE_MENU);
                if (StringUtils.isNoneBlank(cacheKey)) {
                        Object cacheDataPerms = cache.getCache(cacheKey, getId().toString());
                        if (cacheDataPerms != null) {
                                CopyOnWriteArraySet<CoreMenu> set = (CopyOnWriteArraySet<CoreMenu>) cacheDataPerms;
                                if (set != null && set.size() > 0) {
                                        return set;
                                }
                        }
                }
                List<CoreMenu> sites = getMenus();
                CopyOnWriteArraySet<CoreMenu> ownerMenus = new CopyOnWriteArraySet<CoreMenu>();
                if (getHasAssignOwnerMenu()) {
                        ownerMenus.addAll(sites);
                } else {
                        ownerMenus.addAll(getOrg().getOwnerMenus());
                }
                ownerMenus = filterOwnerMenus(ownerMenus);
                if (StringUtils.isNoneBlank(cacheKey) && ownerMenus != null && ownerMenus.size() > 0) {
                        cache.setCache(cacheKey, getId().toString(), ownerMenus);
                }
                return ownerMenus;
        }

        /**
         * 过滤组织没有的菜单
         * 
         * @Title: filterOwnerMenus
         * @param ownerMenus
         *                自身菜单
         * @return CopyOnWriteArraySet
         */
        private CopyOnWriteArraySet<CoreMenu> filterOwnerMenus(CopyOnWriteArraySet<CoreMenu> ownerMenus) {
                if (getOrg() != null) {
                        HashSet<CoreMenu> parentOwnerMenus = getOrg().getOwnerMenus();
                        for (CoreMenu menu : ownerMenus) {
                                if (!CoreMenu.fetchIds(parentOwnerMenus).contains(menu.getId())) {
                                        ownerMenus.remove(menu);
                                }
                        }
                }
                return ownerMenus;
        }

        /**
         * 是否单独分配菜单权限
         * 
         * @Title: getHasAssignOwnerMenu
         * @return boolean
         */
        @Transient
        public boolean getHasAssignOwnerMenu() {
                List<CoreMenu> menus = getMenus();
                if (menus != null && !menus.isEmpty()) {
                        return true;
                }
                if (getHasAssignNewMenuOwner() != null && getHasAssignNewMenuOwner()) {
                        return true;
                }
                return false;
        }

        @Transient
        public List<CmsDataPerm> getOwnerDataPermsByType(Short type){
                List<CmsSite> sites = new ArrayList<CmsSite>();
                List<CmsDataPerm> perms = new ArrayList<>();
                CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                sites.addAll(siteService.findAll(false, true));
                for(CmsSite s:sites){
                        perms.addAll(getOwnerDataPermsByType(type,s.getId(),null,null));
                }
                return perms;
        }

        /**
         * 获取角色数据权限，若角色数据权限未配置，则获取组织数据权限
         * 
         * @Title: getOwnerDataPermsByType
         * @param type
         *                数据类型
         * @param siteId
         *                站点ID
         * @param operator
         *                操作
         * @param dataId
         *                数据ID（站点ID、栏目ID）
         * @return Set
         */
        @Transient
        public CopyOnWriteArraySet<CmsDataPerm> getOwnerDataPermsByType(Short type, Integer siteId, Short operator,
                        Integer dataId) {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                String cacheKey = CmsDataPerm.getRoleCacheKey(type);
                CopyOnWriteArraySet<CmsDataPerm> perms = new CopyOnWriteArraySet<CmsDataPerm>();
                CopyOnWriteArraySet<CmsDataPerm>set = new CopyOnWriteArraySet<CmsDataPerm>();
                if (StringUtils.isNoneBlank(cacheKey)) {
                        Object cacheDataPerms = cache.getCache(cacheKey, getCacheId(type,siteId));
                        if (cacheDataPerms != null) {
                                perms = (CopyOnWriteArraySet<CmsDataPerm>) cacheDataPerms;
                                /** 站点ID统一后续过滤，缓存中存放权限体的所有站点的数据权限，组织、角色、用户需要获取到上级组织或者所属组织的数据权限设定的站点 */
                                //set = CmsDataPerm.streamFilter(perms, siteId, operator, dataId);
                                return perms;
                        }
                }
                if (!getHasAssignDataPermsByType(type)) {
                        perms = getOrg().getOwnerDataPermsByType(type, siteId, null, null, true);
                } else {
                        perms = getDataPermsByType(type, siteId, null, null);
                }
                perms = filterOwnerDataPermsByType(perms, type, siteId, null);
                if (StringUtils.isNoneBlank(cacheKey)) {
                        cache.setCache(cacheKey, getCacheId(type,siteId), perms);
                }
                /** 站点ID统一后续过滤，缓存中存放权限体的所有站点的数据权限，组织、角色、用户需要获取到上级组织或者所属组织的数据权限设定的站点 */
                /**栏目和内容权限已经按站点分开存储*/
                if(!(CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)||CmsDataPerm.DATA_TYPE_CONTENT.equals(type))){
                        set = CmsDataPerm.streamFilter(perms, siteId, operator, dataId);
                }else{
                        set = perms;
                }
                return set;
        }

        private CopyOnWriteArraySet<CmsDataPerm> filterOwnerDataPermsByType(CopyOnWriteArraySet<CmsDataPerm> ownerPerms,
                        Short type, Integer siteId, Short operator) {
                if (getOrg() != null) {
                        CopyOnWriteArraySet<CmsDataPerm> parentOwnerPerms = getOrg().getOwnerDataPermsByType(type,
                                        siteId, operator, null, true);
                        for (CmsDataPerm p : ownerPerms) {
                                if (!p.beContains(parentOwnerPerms)) {
                                        ownerPerms.remove(p);
                                }
                        }
                }
                return ownerPerms;
        }

        /**
         * 获取角色单独配置的数据权限
         * 
         * @Title: getDataPermsByType
         * @param type
         *                数据类型
         * @param operator
         *                操作
         * @param dataId
         *                数据Id（栏目ID、站点ID）
         * @return Set
         */
        @Transient
        public CopyOnWriteArraySet<CmsDataPerm> getDataPermsByType(Short type, Integer siteId, Short operator,
                        Integer dataId) {
                CopyOnWriteArraySet<CmsDataPerm> dataPerms = new CopyOnWriteArraySet<>();
                dataPerms.addAll(getDataPerms());
                if (type != null) {
                        Set<CmsDataPerm> set = dataPerms.stream().filter(p -> type.equals(p.getDataType()))
                                        .filter(p -> !p.getHasDeleted()).collect(Collectors.toSet());
                        dataPerms.clear();
                        dataPerms.addAll(set);
                }
                CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                ChannelService channelService = ApplicationContextProvider.getBean(ChannelService.class);
                for (CmsDataPerm d : dataPerms) {
                        if (siteId != null) {
                                if (!siteId.equals(d.getSiteId())) {
                                        dataPerms.remove(d);
                                }
                        }
                        if (operator != null) {
                                if (!operator.equals(d.getOperation())) {
                                        dataPerms.remove(d);
                                }
                        }
                        if (dataId != null) {
                                if (!dataId.equals(d.getDataId())) {
                                        dataPerms.remove(d);
                                }
                        }
                        if (d.getDataId() != null) {
                                try {
                                        /** 过滤不存在的数据和逻辑删除的数据 */
                                        if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)
                                                        || CmsDataPerm.DATA_TYPE_CONTENT.equals(type)) {
                                                Channel c = channelService.get(d.getDataId());
                                                if (c == null || c != null && c.getHasDeleted()) {
                                                        dataPerms.remove(d);
                                                }
                                        }
                                        if (CmsDataPerm.DATA_TYPE_SITE.equals(type)
                                                        || CmsDataPerm.DATA_TYPE_SITE_OWNER.equals(type)) {
                                                CmsSite c = siteService.get(d.getDataId());
                                                if (c == null || c != null && c.getHasDeleted()) {
                                                        dataPerms.remove(d);
                                                }
                                        }
                                } catch (Exception e) {
                                        dataPerms.remove(d);
                                }
                        }
                }
                return dataPerms;
        }

        /**
         * 获取组织是否单独分配数据权限
         * 
         * @Title: getHasAssignDataPermsByType
         * @param type
         *                类型 1站点数据权限 2栏目 3文档
         * @return boolean
         */
        @Transient
        public boolean getHasAssignDataPermsByType(Short type) {
                CopyOnWriteArraySet<CmsDataPerm> perms = getDataPermsByType(type, null, null, null);
                if (perms != null && !perms.isEmpty()) {
                        return true;
                }
                if (type != null) {
                        if (type.equals(CmsDataPerm.DATA_TYPE_SITE) && StringUtils.isNotBlank(getNewSiteOpe())) {
                                return true;
                        } else if (type.equals(CmsDataPerm.DATA_TYPE_CHANNEL)
                                        && StringUtils.isNotBlank(getAllNewChannelOpe())) {
                                return true;
                        } else if (type.equals(CmsDataPerm.DATA_TYPE_CONTENT)
                                        && StringUtils.isNotBlank(getAllNewChannelOpeContent())) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 清除权限缓存
         * 
         * @Title: clearPermCache
         * @return: void
         */
        public void clearPermCache() {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                for(CmsSite site:siteService.findAll(false,true)){
                        cache.clearCache(CacheConstants.ROLE_OWNER_CHANNEL_CACHE,
                                getCacheId(CmsDataPerm.DATA_TYPE_CHANNEL,site.getId()));
                        cache.clearCache(CacheConstants.ROLE_OWNER_CONTENT_CACHE,
                                getCacheId(CmsDataPerm.DATA_TYPE_CONTENT,site.getId()));
                }
//                cache.clearAll(CacheConstants.ROLE_OWNER_CHANNEL_CACHE);
//                cache.clearAll(CacheConstants.ROLE_OWNER_CONTENT_CACHE);
                cache.clearCache(CacheConstants.ROLE_OWNER_MENU_CACHE, getId().toString());
                cache.clearCache(CacheConstants.ROLE_OWNER_SITE_CACHE, getId().toString());
                cache.clearCache(CacheConstants.ROLE_OWNER_SITE_DATA_CACHE, getId().toString());
        }

        /**
         * 是否可删除 true不禁用 false禁用 当前用户所属角色 返回false 其次 当前登录用户权限>=角色 返回true
         * 
         * @Title: getDeleteAble
         * @return: boolean
         */
        @Transient
        public boolean getDeleteAble() {
                CoreUser user = SystemContextUtils.getCoreUser();
                if (user != null && user.getRoleIds().contains(getId())) {
                        return false;
                }
                return getManagerAble();
        }

        /**
         * 是否可管理
         * 
         * @Title: getManagerAble 登录用户权限是否大于等于组织权限：当前登录用户5大类目权限范围内是否都包含用户对应的权限
         * @return boolean
         */
        @Transient
        public boolean getManagerAble() {
                /** 开发模式下返回true 忽略权限验证 */
                if (ServerModeEnum.dev.toString().equals(getServerMode())) {
                        return true;
                }
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if(user==null||!user.getAdmin()){
                        return false;
                }
                /** 不可编辑自己所属角色的权限 */
                if (user.getRoleIds().contains(getId())) {
                        return false;
                }
                /** 检查站群是否全包含 */
                List<Integer> ownerSiteIds = CmsSite.fetchIds(user.getOwnerSites());
                List<Integer> roleOwnerSiteIds = CmsSite.fetchIds(getOwnerSites());
                if (!ownerSiteIds.containsAll(roleOwnerSiteIds)) {
                        return false;
                }
                /** 检查菜单是否全包含 */
                if (!CoreMenu.fetchIds(user.getOwnerMenus()).containsAll(CoreMenu.fetchIds(getOwnerMenus()))) {
                        return false;
                }
                /** 检查数据权限是否全包含 */
//                CopyOnWriteArraySet<CmsDataPerm> userSitePerms = user
//                                .getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, null);
//                CopyOnWriteArraySet<CmsDataPerm> roleSitePerms = getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE,
//                                null, null, null);
//                if (!CmsDataPerm.containsAll(userSitePerms, roleSitePerms)) {
//                        return false;
//                }
//                if (!CmsDataPerm.containsAll(
//                                user.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL, null, null, null),
//                                getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL, null, null, null))) {
//                        return false;
//                }
//                if (!CmsDataPerm.containsAll(
//                                user.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT, null, null, null),
//                                getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT, null, null, null))) {
//                        return false;
//                }
                /** 检查用户组织层级是否全包含 */
                if (!user.getChildOrgIds().contains(this.getOrgid())) {
                        return false;
                }
                /** 不可管理自己所属角色 */
                if (user.getRoleIds().contains(getId())) {
                        return false;
                }
                return true;
        }

        /**
         * 验证是否可管理
         * 
         * @Title: validManagerAble
         * @throws GlobalException
         *                 GlobalException
         */
        @Transient
        public void validManagerAble() throws GlobalException {
                if (!getManagerAble()) {
                        String msg = MessageResolver.getMessage(
                                        SysOtherErrorCodeEnum.NO_ROLE_PERMISSION_ERROR.getCode(),
                                        SysOtherErrorCodeEnum.NO_ROLE_PERMISSION_ERROR.getDefaultMessage());
                        throw new GlobalException(new SiteExceptionInfo(
                                        SysOtherErrorCodeEnum.NO_ROLE_PERMISSION_ERROR.getCode(), msg));
                }
        }

        /**
         * 获取权限配置
         * 
         * @Title: getPermCfg
         * @return CmsDataPermCfg
         */
        @Transient
        public CmsDataPermCfg getPermCfg() {
                List<CmsDataPermCfg> cfgs = getPermCfgs();
                if (cfgs != null && !cfgs.isEmpty()) {
                        return cfgs.get(0);
                }
                return null;
        }

        /**
         * 获取栏目类新增权限配置
         * 
         * @Title: getPermCfgForChannel
         * @param siteId
         *                站点ID
         * @return CmsDataPermCfg
         */
        @Transient
        public CmsDataPermCfg getPermCfgForChannel(Integer siteId) {
                if (siteId != null && getPermCfgs() != null) {
                        List<CmsDataPermCfg> cfgs = getPermCfgs().stream().filter(cfg -> siteId.equals(cfg.getSiteId()))
                                        .collect(Collectors.toList());
                        if (cfgs != null && !cfgs.isEmpty()) {
                                return cfgs.get(0);
                        }
                } else {
                        return getPermCfg();
                }
                return null;
        }

        /**
         * 是否有单独分配 新增站点的站群权限
         * 
         * @Title: getNewSiteOwner
         * @return Boolean
         */
        @Transient
        public Boolean getHasAssignNewSiteOwner() {
                CmsDataPermCfg cfg = getPermCfg();
                if (cfg != null) {
                        return cfg.getNewSiteOwner();
                }
                return null;
        }

        /**
         * 是否有新增站点的站群权限
         * 
         * @Title: getOwnerNewSiteOwner
         * @return Boolean
         */
        @Transient
        public Boolean getOwnerNewSiteOwner() {
                Boolean ownerNewSiteOwner = null;
                if (getHasAssignNewSiteOwner() != null) {
                        ownerNewSiteOwner = getHasAssignNewSiteOwner();
                } else {
                        ownerNewSiteOwner = getOrg().getOwnerNewSiteOwner();
                }
                if (ownerNewSiteOwner == null) {
                        ownerNewSiteOwner = false;
                }
                ownerNewSiteOwner = filterOwnerNewSiteOwner(ownerNewSiteOwner);
                return ownerNewSiteOwner;
        }

        private Boolean filterOwnerNewSiteOwner(Boolean ownerNewSiteOwner) {
                /** 组织若未配置新站点的站群权限，则过滤为也没有权限 */
                if (getOrg() != null) {
                        Boolean parentOwnerNewSiteOwner = getOrg().getOwnerNewSiteOwner();
                        if (parentOwnerNewSiteOwner != null && !parentOwnerNewSiteOwner) {
                                return false;
                        }
                }
                return ownerNewSiteOwner;
        }

        /**
         * 是否有单独分配 新增菜单的权限
         * 
         * @Title: getNewMenuOwner
         * @return Boolean
         */
        @Transient
        public Boolean getHasAssignNewMenuOwner() {
                CmsDataPermCfg cfg = getPermCfg();
                if (cfg != null) {
                        return cfg.getNewMenuOwner();
                }
                return null;
        }

        /**
         * 是否有新增菜单的权限
         * 
         * @Title: getOwnerNewMenuOwner
         * @return Boolean
         */
        @Transient
        public Boolean getOwnerNewMenuOwner() {
                Boolean ownerNewMenuOwner = false;
                if (getHasAssignNewMenuOwner() != null) {
                        ownerNewMenuOwner = getHasAssignNewMenuOwner();
                } else {
                        ownerNewMenuOwner = getOrg().getOwnerNewMenuOwner();
                }
                ownerNewMenuOwner = filterOwnerNewMenuOwner(ownerNewMenuOwner);
                return ownerNewMenuOwner;
        }

        private Boolean filterOwnerNewMenuOwner(Boolean ownerNewMenuOwner) {
                /** 组织若未配置新站点的菜单权限，则过滤为也没有权限 */
                if (getOrg() != null) {
                        Boolean parentOwnerNewMenuOwner = getOrg().getOwnerNewMenuOwner();
                        if (parentOwnerNewMenuOwner != null && !parentOwnerNewMenuOwner) {
                                return false;
                        }
                }
                return ownerNewMenuOwner;
        }

        /**
         * 新增站点的站点类数据权限(,逗号分隔操作 为null或者空串则未单独配置)
         * 
         * @Title: getNewSiteOpe
         * @return String
         */
        @Transient
        public String getNewSiteOpe() {
                CmsDataPermCfg cfg = getPermCfg();
                if (cfg != null) {
                        return cfg.getNewSiteOpe();
                }
                return null;
        }

        /**
         * 新增站点的站点类数据权限操作选项数组
         * 
         * @Title: getNewSiteOperators
         * @return Short[]
         */
        @Transient
        public Short[] getNewSiteOperators() {
                if (StringUtils.isNoneBlank(getNewSiteOpe())) {
                        String[] newSiteOperators = getNewSiteOpe().split(",");
                        Short[] operators = new Short[newSiteOperators.length];
                        for (int i = 0; i < newSiteOperators.length; i++) {
                                operators[i] = Short.parseShort(newSiteOperators[i]);
                        }
                        return operators;
                }
                return new Short[] {};
        }

        /**
         * 角色拥有新增的站点操作选项
         * 
         * @Title: getOwnerNewSiteOperators
         * @return Short[]
         */
        @Transient
        public Short[] getOwnerNewSiteOperators() {
                Short[] operators = new Short[] {};
                if (getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_SITE)) {
                        operators = getNewSiteOperators();
                } else {
                        Set<Short> ownerOperators = new HashSet<Short>();
                        for (Short s : getOrg().getOwnerNewSiteOperators()) {
                                ownerOperators.add(s);
                        }
                        operators = ownerOperators.toArray(operators);
                }
                operators = filterOwnerNewSiteOperators(operators);
                return operators;
        }

        /**
         * 过滤组织中没有的权限
         * 
         * @Title: filterOwnerNewSiteOperators
         * @param ops
         *                操作
         * @return Short[]
         */
        private Short[] filterOwnerNewSiteOperators(Short[] ops) {
                if (getOrg() != null) {
                        Short[] parentOps = getOrg().getOwnerNewSiteOperators();
                        Set<Short> filterOps = new HashSet<Short>();
                        Short[] filterOpArray = new Short[] {};
                        for (Short op : ops) {
                                if (Arrays.binarySearch(parentOps, op) != -1) {
                                        filterOps.add(op);
                                }
                        }
                        filterOpArray = filterOps.toArray(filterOpArray);
                        return filterOpArray;
                }
                return ops;
        }

        /**
         * 新增栏目的栏目类数据权限(,逗号分隔操作 为null或者空串则未单独配置)
         * 
         * @Title: getNewChannelOpe
         * @return String
         */
        @Transient
        public String getNewChannelOpe(Integer siteId) {
                CmsDataPermCfg cfg = getPermCfgForChannel(siteId);
                if (cfg != null) {
                        return cfg.getNewChannelOpe();
                }
                return null;
        }

        @Transient
        private String getAllNewChannelOpe() {
                List<CmsDataPermCfg> cfgs = getPermCfgs();
                if (cfgs != null) {
                        StringBuffer buff = new StringBuffer();
                        for (CmsDataPermCfg cfg : cfgs) {
                                if (StringUtils.isNoneBlank(cfg.getNewChannelOpe())) {
                                        buff.append(cfg.getNewChannelOpe()).append(",");
                                }
                        }
                        if (buff.length() > 0) {
                                return buff.toString();
                        }
                }
                return null;
        }

        @Transient
        private String getAllNewChannelOpeContent() {
                List<CmsDataPermCfg> cfgs = getPermCfgs();
                if (cfgs != null) {
                        StringBuffer buff = new StringBuffer();
                        for (CmsDataPermCfg cfg : cfgs) {
                                if (StringUtils.isNoneBlank(cfg.getNewChannelOpeContent())) {
                                        buff.append(cfg.getNewChannelOpeContent()).append(",");
                                }
                        }
                        if (buff.length() > 0) {
                                return buff.toString();
                        }
                }
                return null;
        }

        /**
         * 新增栏目的栏目类数据权限操作选项数组
         * 
         * @Title: getNewChannelOperators
         * @return Short[]
         */
        @Transient
        public Short[] getNewChannelOperators(Integer siteId) {
                if (StringUtils.isNoneBlank(getNewChannelOpe(siteId))) {
                        String[] newOperators = getNewChannelOpe(siteId).split(",");
                        Short[] operators = new Short[newOperators.length];
                        for (int i = 0; i < newOperators.length; i++) {
                                operators[i] = Short.parseShort(newOperators[i]);
                        }
                        return operators;
                }
                return new Short[] {};
        }

        /**
         * 角色拥有新增的栏目操作选项
         * 
         * @Title: getOwnerNewChannelOperators
         * @return Short[]
         */
        @Transient
        public Short[] getOwnerNewChannelOperators(Integer siteId) {
                Short[] operators = new Short[] {};
                if (getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL)) {
                        operators = getNewChannelOperators(siteId);
                } else {
                        Set<Short> ownerOperators = new HashSet<Short>();
                        for (Short s : getOrg().getOwnerNewChannelOperators(siteId)) {
                                ownerOperators.add(s);
                        }
                        operators = ownerOperators.toArray(operators);
                }
                operators = filterOwnerNewChannelOperators(operators, siteId);
                return operators;
        }

        /**
         * 过滤父组织中没有的新栏目操作
         * 
         * @Title: filterOwnerNewChannelOperators
         * @param ops
         *                操作
         * @param siteId
         *                站点Id
         * @return Short[]
         */
        private Short[] filterOwnerNewChannelOperators(Short[] ops, Integer siteId) {
                if (getOrg() != null) {
                        Short[] parentOps = getOrg().getOwnerNewChannelOperators(siteId);
                        Set<Short> filterOps = new HashSet<Short>();
                        Short[] filterOpArray = new Short[] {};
                        for (Short op : ops) {
                                if (Arrays.binarySearch(parentOps, op) != -1) {
                                        filterOps.add(op);
                                }
                        }
                        filterOpArray = filterOps.toArray(filterOpArray);
                        return filterOpArray;
                }
                return ops;
        }

        /**
         * 新增栏目的文档类数据权限(,逗号分隔操作 为null或者空串则未单独配置)
         * 
         * @Title: getNewChannelOpeContent
         * @return String
         */
        @Transient
        public String getNewChannelOpeContent(Integer siteId) {
                CmsDataPermCfg cfg = getPermCfg();
                if (cfg != null) {
                        return cfg.getNewChannelOpeContent();
                }
                return null;
        }

        /**
         * 新增栏目的文档类数据权限操作选项数组
         * 
         * @Title: getNewChannelContentOperators
         * @return Short[]
         */
        @Transient
        public Short[] getNewChannelContentOperators(Integer siteId) {
                if (StringUtils.isNoneBlank(getNewChannelOpeContent(siteId))) {
                        String[] newOperators = getNewChannelOpeContent(siteId).split(",");
                        Short[] operators = new Short[newOperators.length];
                        for (int i = 0; i < newOperators.length; i++) {
                                operators[i] = Short.parseShort(newOperators[i]);
                        }
                        return operators;
                }
                return new Short[] {};
        }

        /**
         * 角色拥有新增的内容操作选项
         * 
         * @Title: getOwnerNewChannelContentOperators
         * @return Short[]
         */
        @Transient
        public Short[] getOwnerNewChannelContentOperators(Integer siteId) {
                Short[] operators = new Short[] {};
                if (getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT)) {
                        operators = getNewChannelContentOperators(siteId);
                } else {
                        Set<Short> ownerOperators = new HashSet<Short>();
                        for (Short s : getOrg().getOwnerNewChannelContentOperators(siteId)) {
                                ownerOperators.add(s);
                        }
                        operators = ownerOperators.toArray(operators);
                }
                operators = filterOwnerNewChannelContentOperators(operators, siteId);
                return operators;
        }

        /**
         * 过滤组织中不存在的
         * 
         * @Title: filterOwnerNewChannelContentOperators
         * @param ops
         *                新栏目的内容操作
         * @param siteId
         *                站点ID
         * @return Short[]
         */
        private Short[] filterOwnerNewChannelContentOperators(Short[] ops, Integer siteId) {
                if (getOrg() != null) {
                        Short[] parentOps = getOrg().getOwnerNewChannelContentOperators(siteId);
                        Set<Short> filterOps = new HashSet<Short>();
                        Short[] filterOpArray = new Short[] {};
                        for (Short op : ops) {
                                if (Arrays.binarySearch(parentOps, op) != -1) {
                                        filterOps.add(op);
                                }
                        }
                        filterOpArray = filterOps.toArray(filterOpArray);
                        return filterOpArray;
                }
                return ops;
        }

        /**
         * 得到角色IDs
         * 
         * @Title: fetchIds
         * @param roles
         *                角色集合
         * @return
         */
        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<CoreRole> roles) {
                if (roles == null) {
                        return null;
                }
                List<Integer> ids = new ArrayList<Integer>();
                for (CoreRole r : roles) {
                        ids.add(r.getId());
                }
                return ids;
        }

        /**
         * 获取可分配权限的菜单
         * 
         * @Title: getAuthMenus
         * @return: List
         */
        @Transient
        public List<CoreMenu> getAuthMenus() {
                if (getMenus() != null) {
                        return getMenus().stream().filter(menu -> menu.getIsAuth()).collect(Collectors.toList());
                }
                return new ArrayList<>();
        }

        /**
         * 获取工作流分组名称
         * 
         * @Title: getCandidateGroupName
         * @return: String
         */
        @Transient
        @JSONField(serialize = false)
        public String getCandidateGroupName() {
                return "role" + getId();
        }

        /**
         * 前端 工作流模块读取名称
         * 
         * @Title: getLabel
         * @return: String
         */
        @Transient
        public String getLabel() {
                return getRoleName();
        }

        /**
         * 前端 工作流模块读取id值
         * 
         * @Title: getValue
         * @return: Integer
         */
        @Transient
        public Integer getValue() {
                return getId();
        }

        /**
         * 过滤回收站和已删除的站点
         * 
         * @Title: getEffectSites
         * @return: Set
         */
        @Transient
        public Set<CmsSite> getEffectSites() {
                return getSites().stream().filter(site -> !site.getIsDelete()).filter(site -> !site.getHasDeleted())
                                .collect(Collectors.toSet());
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_role_menu", joinColumns = @JoinColumn(name = "role_id") , inverseJoinColumns = @JoinColumn(name = "menu_id") )
        public List<CoreMenu> getMenus() {
                return menus;
        }

        public void setMenus(List<CoreMenu> menus) {
                this.menus = menus;
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
        @JoinTable(name = "jc_tr_user_role", joinColumns = @JoinColumn(name = "role_id"),
        	inverseJoinColumns = @JoinColumn(name = "user_id"))
        public List<CoreUser> getUsers() {
                return users;
        }

        public void setUsers(List<CoreUser> users) {
                this.users = users;
        }

        @Override
        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_role", pkColumnValue = "jc_sys_role", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_role")
        public Integer getId() {
                return this.id;
        }

        public void setId(int roleId) {
                this.id = roleId;
        }

        @Column(name = "role_name", nullable = false, length = 60)
        @NotNull
        @Length(min = 0, max = 60)
        public String getRoleName() {
                return this.roleName;
        }

        public void setRoleName(String roleName) {
                this.roleName = roleName;
        }

        @OneToMany(mappedBy = "role")
        public Set<CmsDataPerm> getDataPerms() {
                return dataPerms;
        }

        public void setDataPerms(Set<CmsDataPerm> dataPerms) {
                this.dataPerms = dataPerms;
        }

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinColumn(name = "org_id", insertable = false, updatable = false)
        public CmsOrg getOrg() {
                return org;
        }

        public void setOrg(CmsOrg org) {
                this.org = org;
        }

        @Column(name = "description", nullable = true, length = 150)
        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        @Transient
        public Integer[] getMenuid() {
                return menuid;
        }

        public void setMenuid(Integer[] menuid) {
                this.menuid = menuid;
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_role_site", joinColumns = @JoinColumn(name = "role_id") , inverseJoinColumns = @JoinColumn(name = "site_id") )
        public Set<CmsSite> getSites() {
                return sites;
        }

        public void setSites(Set<CmsSite> sites) {
                this.sites = sites;
        }

        @Column(name = "org_id", nullable = false, length = 6)
        public Integer getOrgid() {
                return orgid;
        }

        public void setOrgid(Integer orgid) {
                this.orgid = orgid;
        }

        @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
        public List<CmsDataPermCfg> getPermCfgs() {
                return permCfgs;
        }

        public void setPermCfgs(List<CmsDataPermCfg> permCfgs) {
                this.permCfgs = permCfgs;
        }

        /**
         * 获取组织名称
         * 
         * @Title: getOrgName
         * @return String
         */
        @Transient
        public String getOrgName() {
                StringBuilder orgName = new StringBuilder();
                if (getOrg() != null) {
                	orgName.append(getOrg().getName());
                }
                return orgName.toString();
        }

        /**
         * 获取组织编号
         * 
         * @Title: getOrgCode
         * @return String
         */
        @Transient
        public String getOrgCode() {
                String orgCode = "";
                if (getOrg() != null) {
                        orgCode = getOrg().getCode();
                }
                return orgCode;
        }

        @Transient
        public String getCacheId(Short dateType, Integer siteId) {
                if(CmsDataPerm.DATA_TYPE_CONTENT.equals(dateType)||CmsDataPerm.DATA_TYPE_CHANNEL.equals(dateType)){
                        return getId()+"-"+siteId;
                }else{
                        return getId().toString();
                }
        }


}