/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。

 */

package com.jeecms.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreMenuService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.ServerModeEnum;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.PropertiesUtil;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.domain.dto.CmsSiteAgent;
import com.jeecms.system.domain.vo.CmsDataPermVo;
import com.jeecms.system.exception.SiteExceptionInfo;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
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
 * The persistent class for the jc_sys_org database table.
 *
 * @author: tom
 * @date: 2018年11月5日 上午11:13:40
 */
@Entity
@Table(name = "jc_sys_org")
public class CmsOrg extends com.jeecms.common.base.domain.AbstractTreeDomain<CmsOrg, Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private static Logger logger = LoggerFactory.getLogger(CmsOrg.class);
        private Integer id;
        /** 部门代码 **/
        private String code;
        /** 是否虚拟组织 **/
        private Boolean isVirtual;
        /** 组织名称 **/
        private String name;
        /** 组织描述 **/
        private String orgRemark;
        /** 组织编号 **/
        private String orgNum;
        /** 组织负责人 **/
        private String orgLeader;
        /** 电话 **/
        private String phone;
        /** 组织地址 **/
        private String address;
        /** 传真 **/
        private String orgFax;
        /** 排序值权重(排序值相同情况下，权重越大，排序越前) */
        private Integer sortWeight;
        /** 子组织 **/
        private java.util.Set<CmsOrg> child;
        /** 组织所有数据权限 */
        private Set<CmsDataPerm> dataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
        /** 组织管理站点 */
        private Set<CmsSite> sites = new HashSet<CmsSite>(10);
        /** 管理员集合 **/
        private List<CoreUser> users = new ArrayList<CoreUser>(10);
        /** 角色集合 **/
        private List<CoreRole> roles = new ArrayList<CoreRole>(10);
        /** 菜单集合 */
        private List<CoreMenu> menus = new ArrayList<CoreMenu>(100);
        /** 新增权限配置 */
        private List<CmsDataPermCfg> permCfgs;

        private String serverMode;

        public void setServerMode(String serverMode) {
                this.serverMode = serverMode;
        }

        /**
         * 是否不是当前登录用户所属组织
         *
         * @Title: getCurrUserOrg
         * @return: boolean
         */
        @Transient
        public boolean getNotCurrUserOrg() {
                CoreUser user = SystemContextUtils.getCoreUser();
                if (user != null) {
                        if (user.getOrgId().equals(getId())) {
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

        /**
         * 获得节点列表。从父节点到自身。
         */
        @Override
        @Transient
        public List<CmsOrg> getNodeList() {
                LinkedList<CmsOrg> list = new LinkedList<CmsOrg>();
                CmsOrg node = this;
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
                List<CmsOrg> orgs = getNodeList();
                Integer[] ids = new Integer[orgs.size()];
                int i = 0;
                for (CmsOrg c : orgs) {
                        ids[i++] = c.getId();
                }
                return ids;
        }

        /**
         * 获取顶层组织
         *
         * @Title: getTopOrg
         * @return CmsOrg
         */
        @Transient
        public CmsOrg getTopOrg() {
                CmsOrg parent = getParent();
                if (parent == null) {
                        return this;
                }
                while (parent != null) {
                        if (parent.getParent() != null) {
                                parent = parent.getParent();
                        } else {
                                break;
                        }
                }
                return parent;
        }

        @Transient
        public boolean getIsTop() {
                return isTop();
        }

        /**
         * 是否顶层组织
         *
         * @Title: isTop
         * @return: boolean
         */
        @Transient
        public boolean isTop() {
                if (getParentId() == null && getParent() == null) {
                        return true;
                }
                return false;
        }

        /**
         * 获取权限配置(站群、站点类、菜单类)
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
         * 是否有 新增站点的站群权限
         *
         * @Title: getOwnerNewSiteOwner
         * @return Boolean
         */
        @Transient
        public Boolean getOwnerNewSiteOwner() {
                /** 顶层组织超级权限 */
                if (isTop()) {
                        return true;
                }
                Boolean ownerNewSiteOwner = null;
                if (getHasAssignNewSiteOwner() != null) {
                        ownerNewSiteOwner = getHasAssignNewSiteOwner();
                } else {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                if (parent.getOwnerNewSiteOwner()) {
                                        ownerNewSiteOwner = parent.getOwnerNewSiteOwner();
                                        if (ownerNewSiteOwner != null) {
                                                break;
                                        }
                                }
                                parent = parent.getParent();
                        }
                }
                if (ownerNewSiteOwner == null) {
                        ownerNewSiteOwner = false;
                }
                ownerNewSiteOwner = filterOwnerNewSiteOwner(ownerNewSiteOwner);
                return ownerNewSiteOwner;
        }

        private Boolean filterOwnerNewSiteOwner(Boolean ownerNewSiteOwner) {
                /** 上层组织若未配置新站点的站群权限，则过滤为也没有权限 */
                if (getParent() != null) {
                        Boolean parentOwnerNewSiteOwner = getParent().getOwnerNewSiteOwner();
                        if (parentOwnerNewSiteOwner != null && !parentOwnerNewSiteOwner) {
                                return false;
                        }
                }
                return ownerNewSiteOwner;
        }

        /**
         * 新增站点的站点类数据权限操作选项数组
         *
         * @Title: getNewSiteOperators
         * @return Short[]
         */
        @Transient
        public Short[] getNewSiteOperators() {
                if (StringUtils.isNotBlank(getNewSiteOpe())) {
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
         * 组织拥有新增的站点操作选项
         *
         * @Title: getOwnerNewSiteOperators
         * @return Short[]
         */
        @Transient
        public Short[] getOwnerNewSiteOperators() {
                /** 顶层组织超级权限 */
                if (isTop()) {
                        return CmsDataPerm.OPE_SITE_ARRAY;
                }
                Short[] operators = new Short[] {};
                /** 单独指定了站点类权限则以自身为准 */
                if (getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_SITE)) {
                        operators = getNewSiteOperators();
                } else {
                        CmsOrg parent = getParent();
                        Set<Short> ownerOperators = new HashSet<Short>();
                        while (parent != null) {
                                Short[] parentNewSiteOpes = parent.getOwnerNewSiteOperators();
                                if (parentNewSiteOpes != null && parentNewSiteOpes.length > 0) {
                                        for (Short s : parentNewSiteOpes) {
                                                ownerOperators.add(s);
                                        }
                                        break;
                                }
                                parent = parent.getParent();
                        }
                        operators = ownerOperators.toArray(operators);
                }
                operators = filterOwnerNewSiteOperators(operators);
                return operators;
        }

        /**
         * 过滤父组织中没有的权限
         *
         * @Title: filterOwnerNewSiteOperators
         * @param ops
         *                操作
         * @return Short[]
         */
        private Short[] filterOwnerNewSiteOperators(Short[] ops) {
                if (getParent() != null) {
                        Short[] parentOps = getParent().getOwnerNewSiteOperators();
                        Set<Short> filterOps = new HashSet<Short>();
                        Short[] filterOpArray = new Short[] {};
                        for (Short op : ops) {
                                if (parentOps.length > 0 && op != null && Arrays.binarySearch(parentOps, op) != -1) {
                                        filterOps.add(op);
                                }
                        }
                        filterOpArray = filterOps.toArray(filterOpArray);
                        return filterOpArray;
                }
                return ops;
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
         * 是否有 新增菜单的权限
         *
         * @Title: getOwnerNewMenuOwner
         * @return boolean
         */
        @Transient
        public Boolean getOwnerNewMenuOwner() {
                Boolean ownerNewMenuOwner = false;
                /** 顶层组织超级权限 */
                if (isTop()) {
                        return true;
                }
                if (getHasAssignNewMenuOwner() != null) {
                        ownerNewMenuOwner = getHasAssignNewMenuOwner();
                } else {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                if (parent.getHasAssignNewMenuOwner() != null) {
                                        ownerNewMenuOwner = parent.getOwnerNewMenuOwner();
                                        break;
                                }
                                parent = parent.getParent();
                        }
                }
                ownerNewMenuOwner = filterOwnerNewMenuOwner(ownerNewMenuOwner);
                return ownerNewMenuOwner;
        }

        private Boolean filterOwnerNewMenuOwner(Boolean ownerNewMenuOwner) {
                /** 上层组织若未配置新站点的菜单权限，则过滤为也没有权限 */
                if (getParent() != null) {
                        Boolean parentOwnerNewMenuOwner = getParent().getOwnerNewMenuOwner();
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
        private String getAllSiteNewChannelOpe() {
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
         * 获取新增栏目类权限中不为空的 站点
         *
         * @Title: getNewChannelSites
         * @return: Set
         */
        @Transient
        public Set<CmsSite> getNewChannelSites() {
                if (getPermCfgs() != null) {
                        Set<CmsSite> sites = getPermCfgs().stream()
                            .filter(cfg -> StringUtils.isNoneBlank(cfg.getNewChannelOpe()))
                            .filter(cfg -> (cfg.getSiteId() != null)).map(CmsDataPermCfg::getSite)
                            .collect(Collectors.toSet());
                        return sites;
                }
                return new HashSet<CmsSite>();
        }

        /**
         * 获取新增文档类权限中不为空的 站点
         *
         * @Title: getNewContentChannelSites
         * @return: Set
         */
        @Transient
        public Set<CmsSite> getNewContentChannelSites() {
                if (getPermCfgs() != null) {
                        Set<CmsSite> sites = getPermCfgs().stream()
                            .filter(cfg -> StringUtils.isNoneBlank(cfg.getNewChannelOpeContent()))
                            .filter(cfg -> (cfg.getSiteId() != null)).map(CmsDataPermCfg::getSite)
                            .collect(Collectors.toSet());
                        return sites;
                }
                return new HashSet<CmsSite>();
        }

        /**
         * 组织拥有新增的栏目操作选项
         *
         * @Title: getOwnerNewChannelOperators
         * @return Short[]
         */
        @Transient
        public Short[] getOwnerNewChannelOperators(Integer siteId) {
                /** 顶层组织超级权限 */
                if (isTop()) {
                        return CmsDataPerm.OPE_CHANNEL_ARRAY;
                }
                Short[] operators = new Short[] {};
                Set<Short> ownerOperators = new HashSet<Short>();
                if (getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL)) {
                        operators = getNewChannelOperators(siteId);
                } else {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                for (Short s : parent.getOwnerNewChannelOperators(siteId)) {
                                        ownerOperators.add(s);
                                }
                                if (ownerOperators != null && !ownerOperators.isEmpty()) {
                                        break;
                                }
                                parent = parent.getParent();
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
                if (getParent() != null) {
                        Short[] parentOps = getParent().getOwnerNewChannelOperators(siteId);
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
                CmsDataPermCfg cfg = getPermCfgForChannel(siteId);
                if (cfg != null) {
                        return cfg.getNewChannelOpeContent();
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
         * 组织拥有新增的栏目 内容操作选项
         *
         * @Title: getOwnerNewChannelContentOperators
         * @return Short[]
         */
        @Transient
        public Short[] getOwnerNewChannelContentOperators(Integer siteId) {
                /** 顶层组织超级权限 */
                if (isTop()) {
                        return CmsDataPerm.OPE_CONTENT_ARRAY;
                }
                Short[] operators = new Short[] {};
                Set<Short> ownerOperators = new HashSet<Short>();
                if (getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT)) {
                        operators = getNewChannelContentOperators(siteId);
                        ;
                } else {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                for (Short s : parent.getOwnerNewChannelContentOperators(siteId)) {
                                        ownerOperators.add(s);
                                }
                                if (!ownerOperators.isEmpty()) {
                                        break;
                                }
                                parent = parent.getParent();
                        }
                        operators = ownerOperators.toArray(operators);
                }
                operators = filterOwnerNewChannelContentOperators(operators, siteId);
                return operators;
        }

        /**
         * 过滤父组织中不存在的
         *
         * @Title: filterOwnerNewChannelContentOperators
         * @param ops
         *                新栏目的内容操作
         * @param siteId
         *                站点ID
         * @return Short[]
         */
        private Short[] filterOwnerNewChannelContentOperators(Short[] ops, Integer siteId) {
                if (getParent() != null) {
                        Short[] parentOps = getParent().getOwnerNewChannelContentOperators(siteId);
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
         * 获取本机组织以及下级组织
         *
         * @Title: getChildList
         * @return List
         */
        @Transient
        public List<CmsOrg> getChildNodeList() {
                List<CmsOrg> topList = new ArrayList<CmsOrg>();
                topList.add(this);
                return getListForSelect(topList, null, true);
        }

        /**
         * 获取组织以及下级组织所属角色(忽视权限大小)
         *
         * @Title: getChildRoles
         * @return List
         */
        @Transient
        public List<CoreRole> getChildRoles() {
                List<CoreRole> roles = new ArrayList<CoreRole>();
                for (CmsOrg org : getChildNodeList()) {
                        roles.addAll(org.getRoles());
                }
                return roles;
        }

        /**
         * 获取组织以及下级组织用户(忽视权限大小)
         *
         * @Title: getChildUsers
         * @return List
         */
        @Transient
        public List<CoreUser> getChildUsers() {
                List<CoreUser> users = new ArrayList<CoreUser>();
                for (CmsOrg org : getChildNodeList()) {
                        users.addAll(org.getUsers());
                }
                return users;
        }

        /**
         * 获取本机组织以及下级组织ID
         *
         * @Title: getChildNodeIds
         * @return Integer
         */
        @Transient
        public Integer[] getChildNodeIds() {
                List<CmsOrg> orgs = getChildNodeList();
                Integer[] ids = new Integer[orgs.size()];
                int i = 0;
                for (CmsOrg c : orgs) {
                        ids[i++] = c.getId();
                }
                return ids;
        }

        /**
         * 获取本机组织以及下级组织ID
         *
         * @Title: getChildNodeIds
         * @return List
         */
        @Transient
        public List<Integer> getChildOrgIds() {
                List<Integer> orgIds = new ArrayList<Integer>();
                List<CmsOrg> orgs = getChildNodeList();
                for (CmsOrg org : orgs) {
                        orgIds.add(org.getId());
                }
                return orgIds;
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
                CmsOrg parent = getParent();
                while (parent != null) {
                        deep++;
                        parent = parent.getParent();
                }
                return deep;
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param rights
         *                有权限的组织，为null不控制组织。
         * @param containVirtual
         *                false则不包含未启用的组织 true包含未启用的组织。
         * @return List
         */
        @Transient
        public List<CmsOrg> getListForSelect(Set<CmsOrg> rights, boolean containVirtual) {
                return getListForSelect(rights, null, containVirtual);
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param rights
         *                有权限的组织，为null不控制组织。
         * @param exclude
         *                排除的组织
         * @param containVirtual
         *                false则不包含未启用的组织 true包含未启用的组织。
         * @return List
         */
        @Transient
        public List<CmsOrg> getListForSelect(Set<CmsOrg> rights, CmsOrg exclude, boolean containVirtual) {
                List<CmsOrg> list = new ArrayList<CmsOrg>((getRgt() - getLft()) / 2);
                addChildToList(list, this, rights, exclude, containVirtual);
                return list;
        }

        /**
         * 获得列表用于下拉选择。
         *
         * @Title: getListForSelect
         * @param topList
         *                顶级组织
         * @param rights
         *                有权限的组织，为null不控制组织。
         * @param containVirtual
         *                false则不包含未启用的组织 true包含未启用的组织。
         * @return List
         */
        @Transient
        public static List<CmsOrg> getListForSelect(List<CmsOrg> topList, Set<CmsOrg> rights, boolean containVirtual) {
                return getListForSelect(topList, rights, null, containVirtual);
        }

        /**
         * 获得列表用于下拉选择。条件：有内容的栏目。
         *
         * @Title: getListForSelect
         * @param topList
         *                顶级组织
         * @param rights
         *                有权限的组织，为null不控制权限。
         * @param exclude
         *                排除的组织
         * @param containVirtual
         *                false则不包含未启用的组织 true包含未启用的组织。
         * @return List
         */
        @Transient
        public static List<CmsOrg> getListForSelect(List<CmsOrg> topList, Set<CmsOrg> rights, CmsOrg exclude,
                                                    boolean containVirtual) {
                List<CmsOrg> list = new ArrayList<CmsOrg>();
                for (CmsOrg c : topList) {
                        addChildToList(list, c, rights, exclude, containVirtual);
                }
                return list;
        }

        /**
         * 递归将子栏目加入列表。条件：有内容的栏目。
         *
         * @param list
         *                组织容器
         * @param org
         *                待添加的组织，且递归添加子组织
         * @param rights
         *                有权限的组织，为null不控制权限。
         * @param containVirtual
         *                false则不包含未启用的组织 true包含未启用的组织。
         */
        private static void addChildToList(List<CmsOrg> list, CmsOrg org, Set<CmsOrg> rights, CmsOrg exclude,
                                           boolean containVirtual) {
                boolean returnBack = (rights != null && !rights.contains(org))
                    || (exclude != null && exclude.equals(org));
                if (returnBack) {
                        return;
                }
                addChildToList(list, org, containVirtual);
        }

        private static void addChildToList(List<CmsOrg> list, CmsOrg org, boolean containVirtual) {
                if (!containVirtual) {
                        if (org.getIsVirtual()) {
                                return;
                        }
                }
                if (org.getHasDeleted()) {
                        return;
                }
                list.add(org);
                Set<CmsOrg> child = org.getChild();
                if (child != null && child.size() > 0) {
                        for (CmsOrg c : child) {
                                addChildToList(list, c, containVirtual);
                        }
                }
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
         * 获取组织站群权限，若组织站群未配置，则逐层获取上级站群权限
         *
         * @Title: getOwnerSites
         * @return List
         */
        @Transient
        public CopyOnWriteArraySet<CmsSite> getOwnerSites() {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                String cacheKey = CmsDataPerm.getOrgCacheKey(CmsDataPerm.DATA_TYPE_SITE_OWNER);
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
                HashSet<CmsSite> ownerSites = new HashSet<CmsSite>();
                /** 顶层组织有超级权限 */
                if (isTop()) {
                        CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                        CopyOnWriteArraySet<CmsSite> set = new CopyOnWriteArraySet<>(siteService.findAll(false, true));
                        if (StringUtils.isNoneBlank(cacheKey)) {
                                CmsSiteAgent.initSiteChild(set);
                                cache.setCache(cacheKey, getId().toString(), set);
                        }
                        return set;
                }
                Set<CmsSite> sites = getEffectSites();
                if (getHasAssignOwnerSite()) {
                        ownerSites.addAll(sites);
                } else {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                ownerSites.addAll(parent.getOwnerSites());
                                if (ownerSites != null && ownerSites.size() > 0) {
                                        break;
                                }
                                parent = parent.getParent();
                        }
                }
                CopyOnWriteArraySet<CmsSite> filterOwnerSites = filterOwnerSites(ownerSites);
                /** 新增的时候没取到数据是空集合 */
                if (StringUtils.isNoneBlank(cacheKey) && filterOwnerSites != null && filterOwnerSites.size() > 0) {
                        CmsSiteAgent.initSiteChild(filterOwnerSites);
                        cache.setCache(cacheKey, getId().toString(), filterOwnerSites);
                }
                return filterOwnerSites;
        }


        private CopyOnWriteArraySet<CmsSite> filterOwnerSites(Set<CmsSite> sites) {
                CopyOnWriteArraySet<CmsSite> ownerSites = new CopyOnWriteArraySet<CmsSite>();
                ownerSites.addAll(sites);
                if (getParent() != null) {
                        CopyOnWriteArraySet<CmsSite> parentOwnerSites = getParent().getOwnerSites();
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
         * 获取组织菜单权限，若组织菜单未配置，则逐层获取上级菜单权限
         *
         * @Title: getOwnerMenus
         * @return Set
         */
        @Transient
        public HashSet<CoreMenu> getOwnerMenus() {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                String cacheKey = CmsDataPerm.getOrgCacheKey(CmsDataPerm.DATA_TYPE_MENU);
                if (StringUtils.isNoneBlank(cacheKey)) {
                        Object cacheDataPerms = cache.getCache(cacheKey, getId().toString());
                        if (cacheDataPerms != null) {
                                HashSet<CoreMenu> set = (HashSet<CoreMenu>) cacheDataPerms;
                                if (set != null && set.size() > 0) {
                                        return set;
                                }
                        }
                }
                CopyOnWriteArraySet<CoreMenu> ownerMenus = new CopyOnWriteArraySet<CoreMenu>();
                /** 顶层组织有超级权限 */
                if (isTop()) {
                        CoreMenuService menuService = ApplicationContextProvider.getBean(CoreMenuService.class);
                        HashSet<CoreMenu> set = new HashSet<CoreMenu>();
                        set.addAll(menuService.findAll(true));
                        return set;
                }
                List<CoreMenu> menus = getMenus();
                if (getHasAssignOwnerMenu()) {
                        ownerMenus.addAll(menus);
                } else {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                ownerMenus.addAll(parent.getOwnerMenus());
                                if (ownerMenus != null && ownerMenus.size() > 0) {
                                        break;
                                }
                                parent = parent.getParent();
                        }
                }
                if (!isTop()) {
                        ownerMenus = filterOwnerMenus(ownerMenus);
                }
                HashSet<CoreMenu> filterOwnerMenus = new HashSet<CoreMenu>();
                filterOwnerMenus.addAll(ownerMenus);
                if (StringUtils.isNoneBlank(cacheKey) && ownerMenus != null && ownerMenus.size() > 0) {
                        cache.setCache(cacheKey, getId().toString(), filterOwnerMenus);
                }
                return filterOwnerMenus;
        }

        /**
         * 可分配权限的菜单（要少于实际拥有的菜单）
         *
         * @Title: getAuthOwnerMenus
         * @return: HashSet
         */
        @Transient
        public HashSet<CoreMenu> getAuthOwnerMenus() {
                Set<CoreMenu> ownerMenus = getOwnerMenus();
                HashSet<CoreMenu> authMenus= new HashSet<CoreMenu>();
                authMenus.addAll(ownerMenus.stream().filter(m->m.getIsAuth()).collect(Collectors.toSet()));
                return authMenus;
        }

        /**
         * 过滤上级组织没有的菜单
         *
         * @Title: filterOwnerMenus
         * @param ownerMenus
         *                自身菜单
         * @return CopyOnWriteArraySet
         */
        private CopyOnWriteArraySet<CoreMenu> filterOwnerMenus(CopyOnWriteArraySet<CoreMenu> ownerMenus) {
                if (getParent() != null) {
                        HashSet<CoreMenu> parentOwnerMenus = getParent().getOwnerMenus();
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

        /**
         * 获取组织数据权限中站点，若组织数据权限未配置，则逐层获取上级数据权限
         *
         * @Title: getSiteOfOwnerDataPermsByType
         * @param type
         *                数据权限类型
         * @return: Set 数据权限中站点
         */
        @Transient
        public Set<CmsSite> getSiteOfOwnerDataPermsByType(Short type) {
                CopyOnWriteArraySet<CmsDataPerm> ownerPerms = getOwnerDataPermsByType(type, null, null, null, true);
                Set<CmsSite> ownerDataPermsSite = new HashSet<CmsSite>();
                ownerDataPermsSite.addAll(ownerPerms.stream().map(CmsDataPerm::getSite).collect(Collectors.toSet()));
                if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)) {
                        ownerDataPermsSite.addAll(getNewChannelSites());
                } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(type)) {
                        ownerDataPermsSite.addAll(getNewContentChannelSites());
                }
                return ownerDataPermsSite;
        }

        @Transient
        public List<CmsDataPerm> getOwnerDataPermsByType(Short type){
                List<CmsSite> sites = new ArrayList<CmsSite>();
                List<CmsDataPerm> perms = new ArrayList<>();
                CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                sites.addAll(siteService.findAll(false, true));
                for(CmsSite s:sites){
                        perms.addAll(getOwnerDataPermsByType(type,s.getId(),null,null,true));
                }
                return perms;
        }

        /**
         * 获取组织数据权限，若组织数据权限未配置，则逐层获取上级数据权限
         *
         * @Title: getDataPermsByType
         * @param type
         *                类型 1站点数据权限 2栏目 3文档
         * @param siteId
         *                站点ID
         * @param operator
         *                具体操作
         * @param dataId
         *                数据ID(站点ID、栏目ID)
         * @param containEmptyChannelSite
         *                是否包含空栏目的站点数据
         * @return Set
         */
        @Transient
        public CopyOnWriteArraySet<CmsDataPerm> getOwnerDataPermsByType(Short type, Integer siteId, Short operator,
                                                                        Integer dataId, boolean containEmptyChannelSite) {
                CacheProvider cache = ApplicationContextProvider.getBean(CacheProvider.class);
                String cacheKey = CmsDataPerm.getOrgCacheKey(type);
                CopyOnWriteArraySet<CmsDataPerm> perms = new CopyOnWriteArraySet<CmsDataPerm>();
                CopyOnWriteArraySet<CmsDataPerm> set = new CopyOnWriteArraySet<CmsDataPerm>();
                if (StringUtils.isNoneBlank(cacheKey)) {
                        Object cacheDataPerms = cache.getCache(cacheKey,getCacheId(type,siteId));
                        if (cacheDataPerms != null) {
                                perms = (CopyOnWriteArraySet<CmsDataPerm>) cacheDataPerms;
                                /** 站点ID统一后续过滤，缓存中存放权限体的所有站点的数据权限，组织、角色、用户需要获取到上级组织或者所属组织的数据权限设定的站点 */
                                //set = CmsDataPerm.streamFilter(perms, siteId, operator, dataId);
                                return perms;
                        }
                }
                /** 顶层组织有超级权限 */
                if (isTop()) {
                        CmsSiteService siteService = ApplicationContextProvider.getBean(CmsSiteService.class);
                        //List<CmsSite> sites = new ArrayList<CmsSite>();
                        //sites.addAll(siteService.findAll(false, true));
                        // CmsSiteAgent.sortByIdAndChild(sites);
                        CopyOnWriteArraySet<CmsDataPerm> dataPerms = new CopyOnWriteArraySet();
                        if (CmsDataPerm.DATA_TYPE_SITE.equals(type)) {
                                List<CmsSite> sites = new ArrayList<CmsSite>();
                                sites.addAll(siteService.findAll(false, true));
                                CmsSiteAgent.sortByIdAndChild(sites);
                                dataPerms = CmsDataPerm.initOrgDataPermsForSite(CmsDataPerm.OPE_SITE_ARRAY, this,
                                    sites);
                                //dataPerms.addAll(CmsDataPermVo.initOrgDataPermsForSite(CmsDataPerm.OPE_SITE_ARRAY, this,site));
                        } else if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)) {
//                                dataPerms = CmsDataPerm.initOrgDataPermsForChannelBySite(this, sites,
//                                                containEmptyChannelSite);
                                /**栏目类和内容类权限按站点来加载*/
                                if(siteId==null){
                                        siteId = SystemContextUtils.getSiteId(RequestUtils.getHttpServletRequest());
                                }
                                CmsSite site = siteService.findById(siteId);
                                dataPerms.addAll(CmsDataPermVo.initOrgDataPermsForChannelBySite(this, site,containEmptyChannelSite));
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(type)) {
//                                dataPerms = CmsDataPerm.initOrgDataPermsForContentBySite(this, sites,
//                                                containEmptyChannelSite);
                                /**栏目类和内容类权限按站点来加载*/
                                if(siteId==null){
                                        siteId =  SystemContextUtils.getSiteId(RequestUtils.getHttpServletRequest());
                                }
                                CmsSite site = siteService.findById(siteId);
                                dataPerms.addAll(CmsDataPermVo.initOrgDataPermsForContentBySite(this, site,containEmptyChannelSite));
                        }
                        if (dataPerms != null && dataPerms.size() > 0) {
                                if (StringUtils.isNoneBlank(cacheKey)) {
                                        cache.setCache(cacheKey,getCacheId(type,siteId), dataPerms);
                                }
                        }
                        /** 站点ID统一后续过滤，缓存中存放权限体的所有站点的数据权限，组织、角色、用户需要获取到上级组织或者所属组织的数据权限设定的站点 */
                        if(!(CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)||CmsDataPerm.DATA_TYPE_CONTENT.equals(type))){
                                set = CmsDataPerm.streamFilter(dataPerms, siteId, operator, dataId);
                        }else{
                                set = dataPerms;
                        }
                        return set;
                }
                if (!getHasAssignDataPermsByType(type)) {
                        CmsOrg parent = getParent();
                        while (parent != null) {
                                perms = parent.getOwnerDataPermsByType(type, null, null, null, containEmptyChannelSite);
                                if (perms != null && perms.size() > 0) {
                                        break;
                                }
                                parent = parent.getParent();
                        }
                } else {
                        perms = getDataPermsByType(type, null, null, null);
                }
                if (!isTop()) {
                        perms = filterOwnerDataPermsByType(perms, type, null, null, null);
                }
                if (StringUtils.isNoneBlank(cacheKey)) {
                        cache.setCache(cacheKey, getCacheId(type,siteId), perms);
                }
                /** 站点ID统一后续过滤，缓存中存放权限体的所有站点的数据权限，组织、角色、用户需要获取到上级组织或者所属组织的数据权限设定的站点 */
                /**栏目和内容权限已经按站点分开存储*/
                if(!(CmsDataPerm.DATA_TYPE_CHANNEL.equals(type)||CmsDataPerm.DATA_TYPE_CONTENT.equals(type))){
                        set = CmsDataPerm.streamFilter(perms, siteId, operator, dataId);
                }
                return set;
        }

        private CopyOnWriteArraySet<CmsDataPerm> filterOwnerDataPermsByType(CopyOnWriteArraySet<CmsDataPerm> ownerPerms,
                                                                            Short type, Integer siteId, Short operator, Integer dataId) {
                if (getParent() != null) {
                        CopyOnWriteArraySet<CmsDataPerm> parentOwnerPerms = getParent().getOwnerDataPermsByType(type,
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
         * 获取组织单独配置的数据权限
         *
         * @Title: getDataPermsByType
         * @param type
         *                类型 1站点数据权限 2栏目 3文档
         * @param operator
         *                具体操作
         * @param dataId
         *                数据ID
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
                            && StringUtils.isNotBlank(getAllSiteNewChannelOpe())) {
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
                        cache.clearCache(CacheConstants.ORG_OWNER_CHANNEL_CACHE,
                                getCacheId(CmsDataPerm.DATA_TYPE_CHANNEL,site.getId()));
                        cache.clearCache(CacheConstants.ORG_OWNER_CONTENT_CACHE,
                                getCacheId(CmsDataPerm.DATA_TYPE_CONTENT,site.getId()));
                }
//                cache.clearAll(CacheConstants.ORG_OWNER_CHANNEL_CACHE);
//                cache.clearAll(CacheConstants.ORG_OWNER_CONTENT_CACHE);
                cache.clearCache(CacheConstants.ORG_OWNER_MENU_CACHE, getId().toString());
                cache.clearCache(CacheConstants.ORG_OWNER_SITE_CACHE, getId().toString());
                cache.clearCache(CacheConstants.ORG_OWNER_SITE_DATA_CACHE, getId().toString());
        }

        /**
         * 上级组织是否可选择 true不禁用 false禁用 顶层组织或当前用户所属组织 返回false
         *
         * @Title: getParentAble
         * @return: boolean
         */
        @Transient
        public boolean getParentAble() {
                if (isTop()) {
                        return false;
                }
                CoreUser user = SystemContextUtils.getCoreUser();
                if (user != null && getId().equals(user.getOrgId())) {
                        return false;
                }
                return true;
        }

        /**
         * 组织是否可删除 true不禁用 false禁用 顶层组织或当前用户所属组织 返回false 其次 当前登录用户权限>=组织 返回true
         *
         * @Title: getDeleteAble
         * @return: boolean
         */
        @Transient
        public boolean getDeleteAble() {
                if (isTop()) {
                        return false;
                }
                CoreUser user = SystemContextUtils.getCoreUser();
                if (user != null && getId().equals(user.getOrgId())) {
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
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 开发模式下返回true 忽略权限验证 */
                if (ServerModeEnum.dev.toString().equals(getServerMode())) {
                        return true;
                }
                return getManagerAblePerm();
        }

        /**
         * 是否可管理去除了顶层组织和用户所属组织限制
         *
         * @Title: getManagerAbleContainTop
         * @return: boolean
         */
        @Transient
        public boolean getManagerAbleContainTop() {
                /** 开发模式下返回true 忽略权限验证 */
                if (ServerModeEnum.dev.toString().equals(getServerMode())) {
                        return true;
                }
                return getManagerAblePerm();
        }

        @Transient
        private boolean getManagerAblePerm() {
                CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 检查站群是否全包含 */
                if (!CmsSite.fetchIds(user.getOwnerSites()).containsAll(CmsSite.fetchIds(getOwnerSites()))) {
                        return false;
                }
                /** 检查菜单是否全包含 */
                if (!CoreMenu.fetchIds(user.getOwnerMenus()).containsAll(CoreMenu.fetchIds(getOwnerMenus()))) {
                        return false;
                }
                /** 检查数据权限是否全包含 */
//                if (!CmsDataPerm.containsAll(user.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, null),
//                                getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, null, true))) {
//                        return false;
//                }
//                if (!CmsDataPerm.containsAll(
//                                user.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL, null, null, null),
//                                getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL, null, null, null, true))) {
//                        return false;
//                }
//                if (!CmsDataPerm.containsAll(
//                                user.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT, null, null, null),
//                                getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT, null, null, null, true))) {
//                        return false;
//                }
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
                        String msg = MessageResolver.getMessage(SysOtherErrorCodeEnum.NO_ORG_PERMISSION_ERROR.getCode(),
                            SysOtherErrorCodeEnum.NO_ORG_PERMISSION_ERROR.getDefaultMessage());
                        throw new GlobalException(new SiteExceptionInfo(
                            SysOtherErrorCodeEnum.NO_ORG_PERMISSION_ERROR.getCode(), msg));
                }
        }

        /**
         * 是否可管理去除了顶层组织和用户所属组织限制
         *
         * @Title: getManagerAbleContainTop
         * @return: boolean
         */
        @Transient
        public void validManagerGetAble() throws GlobalException {
                if (!getManagerAbleContainTop()) {
                        String msg = MessageResolver.getMessage(SysOtherErrorCodeEnum.NO_ORG_PERMISSION_ERROR.getCode(),
                            SysOtherErrorCodeEnum.NO_ORG_PERMISSION_ERROR.getDefaultMessage());
                        throw new GlobalException(new SiteExceptionInfo(
                            SysOtherErrorCodeEnum.NO_ORG_PERMISSION_ERROR.getCode(), msg));
                }
        }

        /**
         * 获取ID
         *
         * @Title: fetchIds
         * @param orgs
         *                CmsOrg集合
         * @return List
         */
        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<CmsOrg> orgs) {
                if (orgs == null) {
                        return null;
                }
                List<Integer> ids = new ArrayList<Integer>();
                for (CmsOrg s : orgs) {
                        ids.add(s.getId());
                }
                return ids;
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
                return "org" + getId();
        }

        /**
         * 前端 工作流模块读取名称
         *
         * @Title: getLabel
         * @return: String
         */
        @Transient
        public String getLabel() {
                return getName();
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

        public CmsOrg() {
                super.setSortNum(10);
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

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_org", pkColumnValue = "jc_sys_org", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_org")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(length = 150)
        public String getCode() {
                return this.code;
        }

        public void setCode(String code) {
                this.code = code;
        }

        @NotNull
        @Column(name = "is_virtual", nullable = false)
        public Boolean getIsVirtual() {
                return this.isVirtual;
        }

        public void setIsVirtual(Boolean isVirtual) {
                this.isVirtual = isVirtual;
        }

        @NotNull
        @Column(nullable = false, length = 150)
        public String getName() {
                return this.name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Column(name = "org_remark", length = 150)
        public String getOrgRemark() {
                return this.orgRemark;
        }

        public void setOrgRemark(String orgRemark) {
                this.orgRemark = orgRemark;
        }

        @Override
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id", insertable = false, updatable = false)
        public CmsOrg getParent() {
                return parent;
        }

        @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
        public java.util.Set<CmsOrg> getChild() {
                return child;
        }

        public void setChild(java.util.Set<CmsOrg> child) {
                this.child = child;
        }

        @OneToMany(mappedBy = "org")
        public Set<CmsDataPerm> getDataPerms() {
                return dataPerms;
        }

        public void setDataPerms(Set<CmsDataPerm> dataPerms) {
                this.dataPerms = dataPerms;
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_org_site", joinColumns = @JoinColumn(name = "org_id") , inverseJoinColumns = @JoinColumn(name = "site_id") )
        public Set<CmsSite> getSites() {
                return sites;
        }

        public void setSites(Set<CmsSite> sites) {
                this.sites = sites;
        }

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_org_menu", joinColumns = @JoinColumn(name = "org_id") , inverseJoinColumns = @JoinColumn(name = "menu_id") )
        public List<CoreMenu> getMenus() {
                return menus;
        }

        public void setMenus(List<CoreMenu> menus) {
                this.menus = menus;
        }

        @Column(name = "org_num", length = 50)
        public String getOrgNum() {
                return orgNum;
        }

        public void setOrgNum(String orgNum) {
                this.orgNum = orgNum;
        }

        @Column(name = "org_leader", length = 50)
        public String getOrgLeader() {
                return orgLeader;
        }

        public void setOrgLeader(String orgLeader) {
                this.orgLeader = orgLeader;
        }

        @Column(name = "phone", length = 18)
        public String getPhone() {
                // Oracle,达梦数据库 出现空字符的情况
                if (phone != null && phone.length() != 0) {
                        phone = phone.replaceAll("\\s*", "");
                }
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        @Column(name = "address", length = 50)
        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        @Column(name = "org_fax", length = 15)
        public String getOrgFax() {
                return orgFax;
        }

        public void setOrgFax(String orgFax) {
                this.orgFax = orgFax;
        }

        @OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
        public List<CoreUser> getUsers() {
                return users;
        }

        public void setUsers(List<CoreUser> users) {
                this.users = users;
        }

        @OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
        public List<CoreRole> getRoles() {
                return roles;
        }

        public void setRoles(List<CoreRole> roles) {
                this.roles = roles;
        }

        @OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
        public List<CmsDataPermCfg> getPermCfgs() {
                return permCfgs;
        }

        public void setPermCfgs(List<CmsDataPermCfg> permCfgs) {
                this.permCfgs = permCfgs;
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
                result = prime * result + ((address == null) ? 0 : address.hashCode());
                result = prime * result + ((child == null) ? 0 : child.hashCode());
                result = prime * result + ((code == null) ? 0 : code.hashCode());
                result = prime * result + ((dataPerms == null) ? 0 : dataPerms.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((isVirtual == null) ? 0 : isVirtual.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((orgFax == null) ? 0 : orgFax.hashCode());
                result = prime * result + ((orgLeader == null) ? 0 : orgLeader.hashCode());
                result = prime * result + ((orgNum == null) ? 0 : orgNum.hashCode());
                result = prime * result + ((orgRemark == null) ? 0 : orgRemark.hashCode());
                result = prime * result + ((phone == null) ? 0 : phone.hashCode());
                result = prime * result + ((serverMode == null) ? 0 : serverMode.hashCode());
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
                CmsOrg other = (CmsOrg) obj;
                if (address == null) {
                        if (other.address != null) {
                                return false;
                        }
                } else if (!address.equals(other.address)) {
                        return false;
                }
                if (child == null) {
                        if (other.child != null) {
                                return false;
                        }
                } else if (!child.equals(other.child)) {
                        return false;
                }
                if (code == null) {
                        if (other.code != null) {
                                return false;
                        }
                } else if (!code.equals(other.code)) {
                        return false;
                }
                if (dataPerms == null) {
                        if (other.dataPerms != null) {
                                return false;
                        }
                } else if (!dataPerms.equals(other.dataPerms)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (isVirtual == null) {
                        if (other.isVirtual != null) {
                                return false;
                        }
                } else if (!isVirtual.equals(other.isVirtual)) {
                        return false;
                }
                if (name == null) {
                        if (other.name != null) {
                                return false;
                        }
                } else if (!name.equals(other.name)) {
                        return false;
                }
                if (orgFax == null) {
                        if (other.orgFax != null) {
                                return false;
                        }
                } else if (!orgFax.equals(other.orgFax)) {
                        return false;
                }
                if (orgLeader == null) {
                        if (other.orgLeader != null) {
                                return false;
                        }
                } else if (!orgLeader.equals(other.orgLeader)) {
                        return false;
                }
                if (orgNum == null) {
                        if (other.orgNum != null) {
                                return false;
                        }
                } else if (!orgNum.equals(other.orgNum)) {
                        return false;
                }
                if (orgRemark == null) {
                        if (other.orgRemark != null) {
                                return false;
                        }
                } else if (!orgRemark.equals(other.orgRemark)) {
                        return false;
                }
                if (phone == null) {
                        if (other.phone != null) {
                                return false;
                        }
                } else if (!phone.equals(other.phone)) {
                        return false;
                }
                if (serverMode == null) {
                        if (other.serverMode != null) {
                                return false;
                        }
                } else if (!serverMode.equals(other.serverMode)) {
                        return false;
                }
                return true;
        }

        @Column(name = "sort_weight", nullable = false, length = 11)
        public Integer getSortWeight() {
                return sortWeight;
        }

        public void setSortWeight(Integer sortWeight) {
                this.sortWeight = sortWeight;
        }

        /** 适配Oracle的时间 **/
        @Transient
        public String getCreateTimes() {
                return MyDateUtils.formatDate(super.createTime, MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
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