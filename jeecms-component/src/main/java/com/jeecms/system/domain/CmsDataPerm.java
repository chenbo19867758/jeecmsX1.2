package com.jeecms.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.system.domain.vo.CmsDataPermVo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 数据权限实体类
 * 
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_sys_data_perm")
@NamedQuery(name = "CmsDataPerm.findAll", query = "SELECT c FROM CmsDataPerm c")
public class CmsDataPerm extends com.jeecms.common.base.domain.AbstractDomain<Integer>
                implements Serializable, Comparable<CmsDataPerm> {

        /**
         * 数据模块类型 1站点
         */
        public static final Short DATA_TYPE_SITE = 1;
        /**
         * 数据模块类型 2栏目
         */
        public static final Short DATA_TYPE_CHANNEL = 2;
        /**
         * 数据模块类型 3文档
         */
        public static final Short DATA_TYPE_CONTENT = 3;
        /**
         * 数据模块类型 4菜单
         */
        public static final Short DATA_TYPE_MENU = 4;
        /**
         * 数据模块类型 5可管理站点
         */
        public static final Short DATA_TYPE_SITE_OWNER = 5;

        public static enum DataTypeEnum {
                /**
                 * 站点
                 */
                SITE,
                /**
                 * 栏目
                 */
                CHANNEL,
                /**
                 * 内容
                 */
                CONTENT,
                /**
                 * 组织
                 */
                ORG,
                /**
                 * 可管理站点
                 */
                SITE_OWNER
        }

        /**
         * 站点查看 1
         */
        public static final Short OPE_SITE_VIEW = 1;
        /**
         * 站点新建子站点 2
         */
        public static final Short OPE_SITE_NEW_CHILD = 2;
        /**
         * 站点静态化 3
         */
        public static final Short OPE_SITE_STATIC = 3;
        /**
         * 站点修改 4
         */
        public static final Short OPE_SITE_EDIT = 4;
        /**
         * 站点删除 5
         */
        public static final Short OPE_SITE_DEL = 5;
        /**
         * 站点开启关闭 6
         */
        public static final Short OPE_SITE_OPEN_CLOSE = 6;
        /**
         * 站点分配权限 7
         */
        public static final Short OPE_SITE_PERM_ASSIGN = 7;

        public static final Short[] OPE_SITE_ARRAY = new Short[] { OPE_SITE_VIEW, OPE_SITE_NEW_CHILD, OPE_SITE_STATIC,
                        OPE_SITE_EDIT, OPE_SITE_DEL, OPE_SITE_OPEN_CLOSE, OPE_SITE_PERM_ASSIGN };

        public static enum OpeSiteEnum {
                /**
                 * 站点查看
                 */
                VIEW,
                /**
                 * 站点新建子站点
                 */
                NEWCHILD,
                /**
                 * 站点静态化
                 */
                STATIC,
                /**
                 * 站点修改
                 */
                EDIT,
                /**
                 * 站点删除
                 */
                DEL,
                /**
                 * 站点开启关闭
                 */
                OPENCLOSE,
                /**
                 * 站点分配权限
                 */
                PERM_ASSIGN
        }

        /**
         * 栏目查看 1
         */
        public static final Short OPE_CHANNEL_VIEW = 1;
        /**
         * 栏目创建子栏目 2
         */
        public static final Short OPE_CHANNEL_CREATE = 2;
        /**
         * 栏目修改 3
         */
        public static final Short OPE_CHANNEL_EDIT = 3;
        /**
         * 栏目删除 4
         */
        public static final Short OPE_CHANNEL_DEL = 4;
        /**
         * 栏目静态化 5
         */
        public static final Short OPE_CHANNEL_STATIC = 5;
        /**
         * 栏目合并 6
         */
        public static final Short OPE_CHANNEL_MERGE = 6;
        /**
         * 栏目分配权限 7
         */
        public static final Short OPE_CHANNEL_PERM_ASSIGN = 7;

        public static final Short[] OPE_CHANNEL_ARRAY = new Short[] { OPE_CHANNEL_VIEW, OPE_CHANNEL_CREATE,
                        OPE_CHANNEL_EDIT, OPE_CHANNEL_DEL, OPE_CHANNEL_STATIC, OPE_CHANNEL_MERGE,
                        OPE_CHANNEL_PERM_ASSIGN };

        /**
         * 获取栏目操作数值 字符串，逗号拼接
         * 
         * @Title: getOpeChannels
         * @return: String
         */
        public static final String getOpeChannels() {
                StringBuffer buff = new StringBuffer();
                for (Short o : OPE_CHANNEL_ARRAY) {
                        buff.append(o.toString() + ",");
                }
                return buff.toString();
        }

        /**
         * 获取内容操作数值 字符串，逗号拼接
         * 
         * @Title: getOpeContentChannels
         * @return: String
         */
        public static final String getOpeContentChannels() {
                StringBuffer buff = new StringBuffer();
                for (Short o : OPE_CONTENT_ARRAY) {
                        buff.append(o.toString() + ",");
                }
                return buff.toString();
        }

        /**
         * 栏目操作枚举
         * 
         * @author: tom
         * @date: 2019年4月4日 上午9:34:28
         */
        public static enum OpeChannelEnum {
                /**
                 * 栏目查看
                 */
                VIEW,
                /**
                 * 栏目创建子栏目
                 */
                CREATE,
                /**
                 * 栏目修改
                 */
                EDIT,
                /**
                 * 栏目删除
                 */
                DEL,
                /**
                 * 栏目静态化
                 */
                STATIC,
                /**
                 * 栏目合并
                 */
                MERGE,
                /**
                 * 栏目分配权限
                 */
                PERM_ASSIGN
        }

        /**
         * 内容查看 1
         */
        public static final Short OPE_CONTENT_VIEW = 1;
        /**
         * 内容创建 2
         */
        public static final Short OPE_CONTENT_CREATE = 2;
        /**
         * 内容发布 3
         */
        public static final Short OPE_CONTENT_PUBLISH = 3;
        /**
         * 内容修改 4
         */
        public static final Short OPE_CONTENT_EDIT = 4;
        /**
         * 内容归档 5
         */
        public static final Short OPE_CONTENT_FILE = 5;
        /**
         * 内容删除 6
         */
        public static final Short OPE_CONTENT_DEL = 6;
        /**
         * 置顶7
         */
        public static final Short OPE_CONTENT_TOP = 7;
        /**
         * 移动8
         */
        public static final Short OPE_CONTENT_MOVE = 8;
        /**
         * 排序9
         */
        public static final Short OPE_CONTENT_ORDER = 9;
        /**
         * 复制10
         */
        public static final Short OPE_CONTENT_COPY = 10;
        /**
         * 引用11
         */
        public static final Short OPE_CONTENT_QUOTE = 11;
        /**
         * 内容类型12
         */
        public static final Short OPE_CONTENT_TYPE = 12;
        /**
         * 内容站群推送 13
         */
        public static final Short OPE_CONTENT_SITE_PUSH = 13;
        /**
         * 内容微博推送 14
         */
        public static final Short OPE_CONTENT_WEIBO_PUSH = 14;
        /**
         * 内容微信推送 15
         */
        public static final Short OPE_CONTENT_WECHAT_PUSH = 15;

        public static final Short[] OPE_CONTENT_ARRAY = new Short[] { OPE_CONTENT_VIEW, OPE_CONTENT_CREATE,
                        OPE_CONTENT_EDIT, OPE_CONTENT_PUBLISH, OPE_CONTENT_FILE, OPE_CONTENT_DEL, OPE_CONTENT_TOP,
                        OPE_CONTENT_MOVE, OPE_CONTENT_ORDER, OPE_CONTENT_COPY, OPE_CONTENT_QUOTE, OPE_CONTENT_TYPE,
                        OPE_CONTENT_SITE_PUSH, OPE_CONTENT_WEIBO_PUSH, OPE_CONTENT_WECHAT_PUSH };

        /**
         * 内容操作枚举
         * 
         * @author: tom
         * @date: 2019年4月4日 上午9:35:02
         */
        public static enum OpeContentEnum {
                /**
                 * 内容查看
                 */
                VIEW,
                /**
                 * 内容创建
                 */
                CREATE,
                /**
                 * 内容修改
                 */
                EDIT,
                /**
                 * 内容归档
                 */
                FILE,
                /**
                 * 内容发布
                 */
                PUBLISH,
                /**
                 * 内容删除
                 */
                DEL,
                /**
                 * 置顶
                 */
                TOP,
                /**
                 * 移动
                 */
                MOVE,
                /**
                 * 排序
                 */
                ORDER,
                /**
                 * 复制
                 */
                COPY,
                /**
                 * 引用
                 */
                QUOTE,
                /**
                 * 内容类型
                 */
                TYPE,
                /**
                 * 站群推送
                 */
                SITE_PUSH,
                /**
                 * 微博推送
                 */
                WEIBO_PUSH,
                /**
                 * 微信推送
                 */
                WECHAT_PUSH,
        }

        /**
         * 获取权限的操作
         * 
         * @Title: getOperators
         * @param perms
         *                集合权限
         * @return List
         */
        @Transient
        @JSONField(serialize = false)
        public static Set<Short> getOperators(Collection<CmsDataPerm> perms) {
                Set<Short> operators = new HashSet<Short>();
                if (perms == null) {
                        return operators;
                }
                for (CmsDataPerm s : perms) {
                        operators.add(s.getOperation());
                }
                return operators;
        }

        /**
         * 给组织 初始化站点类数据权限
         * 
         * @Title: initOrgDataPermsForSite
         * @param org
         *                组织
         * @param sites
         *                站点
         * @return Set
         */
        public static CopyOnWriteArraySet<CmsDataPerm> initOrgDataPermsForSite(Short[] operators, CmsOrg org,
                        List<CmsSite> sites) {
                CopyOnWriteArraySet<CmsDataPerm> perms = new CopyOnWriteArraySet<CmsDataPerm>();
                for (CmsSite site : sites) {
                        perms.addAll(CmsDataPermVo.initOrgDataPermsForSite(operators, org, site));
                }
                return perms;
        }

        /**
         * 给组织 初始化栏目类数据权限
         * 
         * @Title: initOrgDataPermsForChannel
         * @param org
         *                组织
         * @param sites
         *                站点
         * @return Set
         */
        public static CopyOnWriteArraySet<CmsDataPerm> initOrgDataPermsForChannelBySite(CmsOrg org, List<CmsSite> sites,
                        boolean containEmptyChannelSite) {
                CopyOnWriteArraySet<CmsDataPerm> perms = new CopyOnWriteArraySet<CmsDataPerm>();
                for (CmsSite site : sites) {
                        perms.addAll(CmsDataPermVo.initOrgDataPermsForChannelBySite(org, site,
                                        containEmptyChannelSite));
                }
                return perms;
        }

        /**
         * 获取数据权限集合的数据ID 集合
         * 
         * @Title: getDataIds
         * @param colections
         *                权限集合
         * @return: Set<Integer>
         */
        public static Set<Integer> getDataIds(Collection<CmsDataPerm> colections) {
                if (colections == null || colections.isEmpty()) {
                        return new HashSet<Integer>();
                }
                return colections.stream().map(perm -> perm.getDataId()).collect(Collectors.toSet());
        }

        /**
         * 给组织 初始化内容类数据权限
         * 
         * @Title: initOrgDataPermsForContent
         * @param org
         *                组织
         * @param sites
         *                站点
         * @param containEmptyChannelSite
         *                是否包含空栏目的站点数据
         * @return Set
         */
        public static CopyOnWriteArraySet<CmsDataPerm> initOrgDataPermsForContentBySite(CmsOrg org, List<CmsSite> sites,
                        boolean containEmptyChannelSite) {
                CopyOnWriteArraySet<CmsDataPerm> perms = new CopyOnWriteArraySet<CmsDataPerm>();
                for (CmsSite site : sites) {
                        perms.addAll(CmsDataPermVo.initOrgDataPermsForContentBySite(org, site,
                                        containEmptyChannelSite));
                }
                return perms;
        }

        public static String getUserCacheKey(Short type) {
                String cacheKey = "";
                if (type != null) {
                        if (CmsDataPerm.DATA_TYPE_SITE.equals(type)) {
                                cacheKey = CacheConstants.USER_OWNER_SITE_DATA_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)) {
                                cacheKey = CacheConstants.USER_OWNER_CHANNEL_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(type)) {
                                cacheKey = CacheConstants.USER_OWNER_CONTENT_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_SITE_OWNER.equals(type)) {
                                cacheKey = CacheConstants.USER_OWNER_SITE_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_MENU.equals(type)) {
                                cacheKey = CacheConstants.USER_OWNER_MENU_CACHE;
                        }
                }
                return cacheKey;
        }

        public static String getOrgCacheKey(Short type) {
                String cacheKey = "";
                if (type != null) {
                        if (CmsDataPerm.DATA_TYPE_SITE.equals(type)) {
                                cacheKey = CacheConstants.ORG_OWNER_SITE_DATA_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)) {
                                cacheKey = CacheConstants.ORG_OWNER_CHANNEL_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(type)) {
                                cacheKey = CacheConstants.ORG_OWNER_CONTENT_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_SITE_OWNER.equals(type)) {
                                cacheKey = CacheConstants.ORG_OWNER_SITE_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_MENU.equals(type)) {
                                cacheKey = CacheConstants.ORG_OWNER_MENU_CACHE;
                        }
                }
                return cacheKey;
        }

        public static String getRoleCacheKey(Short type) {
                String cacheKey = "";
                if (type != null) {
                        if (CmsDataPerm.DATA_TYPE_SITE.equals(type)) {
                                cacheKey = CacheConstants.ROLE_OWNER_SITE_DATA_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)) {
                                cacheKey = CacheConstants.ROLE_OWNER_CHANNEL_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(type)) {
                                cacheKey = CacheConstants.ROLE_OWNER_CONTENT_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_SITE_OWNER.equals(type)) {
                                cacheKey = CacheConstants.ROLE_OWNER_SITE_CACHE;
                        } else if (CmsDataPerm.DATA_TYPE_MENU.equals(type)) {
                                cacheKey = CacheConstants.ROLE_OWNER_MENU_CACHE;
                        }
                }
                return cacheKey;
        }

        public static CopyOnWriteArraySet<CmsDataPerm> streamFilter(CopyOnWriteArraySet<CmsDataPerm> perms,
                        Integer siteId, Short operator, Integer dataId) {
                /** 站点ID统一后续过滤，缓存中存放权限体的所有站点的数据权限，组织、角色、用户需要获取到上级组织或者所属组织的数据权限设定的站点 */
                Set<CmsDataPerm> permSet = new HashSet<CmsDataPerm>(perms);
                if (siteId != null) {
                        permSet = perms.stream().filter(p -> siteId.equals(p.getSiteId())).collect(Collectors.toSet());
                }
                if (operator != null) {
                        permSet = perms.stream().filter(o -> o.getOperation().equals(operator))
                                        .collect(Collectors.toSet());
                }
                if (dataId != null) {
                        permSet = perms.stream().filter(o -> o != null).filter(o -> dataId.equals(o.getDataId()))
                                        .collect(Collectors.toSet());
                }
                CopyOnWriteArraySet<CmsDataPerm> set = new CopyOnWriteArraySet<CmsDataPerm>();
                set.addAll(permSet);
                return set;
        }

        private static final long serialVersionUID = 1L;

        private Integer id;
        private Integer dataId;
        private Short dataType;
        private Short operation;
        private Integer orgId;
        private Integer roleId;
        private Integer siteId;
        private Integer userId;

        private CmsSite site;
        private CmsOrg org;
        private CoreUser user;
        private CoreRole role;

        private CmsOrg dataOrg;
        private Channel dataChannel;

        public CmsDataPerm() {
        }

        /**
         * 根据组织创建数据权限对象
         * 
         * @param dataId
         *                数据Id
         * @param dataType
         *                数据类型
         * @param operation
         *                操作
         * @param orgId
         *                组织ID
         * @param siteId
         *                站点ID
         * @param site
         *                站点
         * @param org
         *                组织
         */
        public CmsDataPerm(Integer dataId, Short dataType, Short operation, Integer orgId, Integer siteId, CmsSite site,
                        CmsOrg org) {
                super();
                this.dataId = dataId;
                this.dataType = dataType;
                this.operation = operation;
                this.orgId = orgId;
                this.siteId = siteId;
                // this.hasPerm = hasPerm;
                this.site = site;
                this.org = org;
        }

        /**
         * 根据角色创建数据权限对象
         * 
         * @param dataId
         *                数据Id
         * @param dataType
         *                数据类型
         * @param operation
         *                操作
         * @param roleId
         *                角色ID
         * @param siteId
         *                站点ID
         * @param site
         *                站点
         * @param role
         *                角色
         */
        public CmsDataPerm(Integer dataId, Short dataType, Short operation, Integer roleId, Integer siteId,
                        CmsSite site, CoreRole role) {
                super();
                this.dataId = dataId;
                this.dataType = dataType;
                this.operation = operation;
                this.roleId = roleId;
                this.siteId = siteId;
                this.site = site;
                this.role = role;
        }

        /**
         * 根据用户创建数据权限对象
         * 
         * @param dataId
         *                数据Id
         * @param dataType
         *                数据类型
         * @param operation
         *                操作
         * @param userId
         *                用户ID
         * @param siteId
         *                站点ID
         * @param site
         *                站点
         * @param user
         *                用户
         */
        public CmsDataPerm(Integer dataId, Short dataType, Short operation, Integer userId, Integer siteId,
                        CmsSite site, CoreUser user) {
                super();
                this.dataId = dataId;
                this.dataType = dataType;
                this.operation = operation;
                this.userId = userId;
                this.siteId = siteId;
                this.site = site;
                this.user = user;
        }

        /**
         * 判断权限是否相同
         * 
         * @Title: getEquals
         * @param perm
         *                CmsDataPerm
         * @return boolean
         */
        @Transient
        public boolean getEquals(CmsDataPerm perm) {
                /**
                 * if (!perm.getHasPerm().equals(this.getHasPerm())) { return false; }
                 */
                if (!perm.getDataType().equals(this.getDataType())) {
                        return false;
                }
                if (!perm.getDataId().equals(this.getDataId())) {
                        return false;
                }
                if (this.getSiteId() != null && !this.getSiteId().equals(perm.getSiteId())) {
                        return false;
                }
                return true;
        }

        /**
         * 是否被包含
         * 
         * @Title: beContains
         * @param perms
         *                CmsDataPerm权限set
         * @return boolean ture被包含
         */
        @Transient
        public boolean beContains(Collection<CmsDataPerm> perms) {
                if (perms != null && !perms.isEmpty()) {
                        for (CmsDataPerm perm : perms) {
                                boolean beContain = perm.getDataType().equals(this.getDataType())
                                                && perm.getOperation().equals(this.getOperation());
                                if (this.getDataId() != null) {
                                        beContain = beContain && this.getDataId().equals(perm.getDataId());
                                }
                                if (this.getSiteId() != null) {
                                        beContain = beContain && this.getSiteId().equals(perm.getSiteId());
                                }
                                if (beContain) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        /**
         * 判断 perms 是否全包含 childPerms权限体
         * 
         * @Title: containsAll
         * @param perms
         *                大权限 集合
         * @param childPerms
         *                子权限集合
         * @return: boolean
         */
        @Transient
        public static boolean containsAll(Collection<CmsDataPerm> perms, Collection<CmsDataPerm> childPerms) {
                if (childPerms != null && !childPerms.isEmpty()) {
                        String SP_BEGIN="-";
                        String SP_END=";";
                        StringBuilder builder = new StringBuilder();
                        for(CmsDataPerm p:perms){
                                builder.append(p.getDataId()).append(p.getOperation());
                        }
                        StringBuilder builder2 = new StringBuilder();
                        for(CmsDataPerm p:childPerms){
                                builder2.append(p.getDataId()).append(p.getOperation());
                        }
                        return builder.toString().equals(builder2.toString());
                        /***
                        for (CmsDataPerm perm : childPerms) {
                                if (!perm.beContains(perms)) {
                                        return false;
                                }
                        }
                         */
                }
                return true;
        }

        /***
         * 先按数据id再按数据权限标识排序
         * @param perms
         * @return
         */
        @Transient
        public static List<CmsDataPerm> sort(List<CmsDataPerm> perms) {
                if (perms != null && perms.size() > 0) {
                        return perms.stream().filter(p->p!=null)
                                .filter(p->p.getDataId()!=null)
                                .filter(p->p.getOperation()!=null)
                                .sorted(Comparator.comparing(CmsDataPerm::getDataId))
                                .sorted(Comparator.comparing(CmsDataPerm::getOperation))
                              .collect(Collectors.toList());
                }
                return perms;
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_data_perm", pkColumnValue = "jc_sys_data_perm", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_data_perm")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "data_id")
        public Integer getDataId() {
                return this.dataId;
        }

        public void setDataId(Integer dataId) {
                this.dataId = dataId;
        }

        @NotNull
        @Column(name = "data_type", nullable = false)
        public Short getDataType() {
                return this.dataType;
        }

        public void setDataType(Short dataType) {
                this.dataType = dataType;
        }

        @Column(nullable = false)
        public Short getOperation() {
                return this.operation;
        }

        public void setOperation(Short operation) {
                this.operation = operation;
        }

        @Column(name = "org_id", insertable = false, updatable = false)
        public Integer getOrgId() {
                return this.orgId;
        }

        public void setOrgId(Integer orgId) {
                this.orgId = orgId;
        }

        @Column(name = "role_id", insertable = false, updatable = false)
        public Integer getRoleId() {
                return this.roleId;
        }

        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        @Column(name = "site_id", insertable = false, updatable = false)
        public Integer getSiteId() {
                return this.siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @Column(name = "user_id", insertable = false, updatable = false)
        public Integer getUserId() {
                return this.userId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
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
        @JoinColumn(name = "org_id")
        public CmsOrg getOrg() {
                return org;
        }

        public void setOrg(CmsOrg org) {
                this.org = org;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        public CoreUser getUser() {
                return user;
        }

        public void setUser(CoreUser user) {
                this.user = user;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "role_id")
        public CoreRole getRole() {
                return role;
        }

        public void setRole(CoreRole role) {
                this.role = role;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "data_id", insertable = false, updatable = false)
        public CmsOrg getDataOrg() {
                return dataOrg;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "data_id", insertable = false, updatable = false)
        public Channel getDataChannel() {
                return dataChannel;
        }

        public void setDataOrg(CmsOrg dataOrg) {
                this.dataOrg = dataOrg;
        }

        public void setDataChannel(Channel dataChannel) {
                this.dataChannel = dataChannel;
        }

        @Override
        public int compareTo(CmsDataPerm o) {
                if (this != null && o != null && this.operation != null && o.getOperation() != null) {
                        return this.operation - o.getOperation();
                }
                return 0;
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
                result = prime * result + ((dataId == null) ? 0 : dataId.hashCode());
                result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((operation == null) ? 0 : operation.hashCode());
                result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
                result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
                result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
                result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
                CmsDataPerm other = (CmsDataPerm) obj;
                if (dataId == null) {
                        if (other.dataId != null) {
                                return false;
                        }
                } else if (!dataId.equals(other.dataId)) {
                        return false;
                }
                if (dataType == null) {
                        if (other.dataType != null) {
                                return false;
                        }
                } else if (!dataType.equals(other.dataType)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (operation == null) {
                        if (other.operation != null) {
                                return false;
                        }
                } else if (!operation.equals(other.operation)) {
                        return false;
                }
                if (orgId == null) {
                        if (other.orgId != null) {
                                return false;
                        }
                } else if (!orgId.equals(other.orgId)) {
                        return false;
                }
                if (roleId == null) {
                        if (other.roleId != null) {
                                return false;
                        }
                } else if (!roleId.equals(other.roleId)) {
                        return false;
                }
                if (siteId == null) {
                        if (other.siteId != null) {
                                return false;
                        }
                } else if (!siteId.equals(other.siteId)) {
                        return false;
                }
                if (userId == null) {
                        if (other.userId != null) {
                                return false;
                        }
                } else if (!userId.equals(other.userId)) {
                        return false;
                }
                return true;
        }

}
