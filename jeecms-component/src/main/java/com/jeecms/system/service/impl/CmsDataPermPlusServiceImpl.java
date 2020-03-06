/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.dto.CmsOrgAgent;
import com.jeecms.auth.service.CoreMenuService;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.util.ChannelAgent;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.ServerModeEnum;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.IllegalParamExceptionInfo;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.CmsSiteAgent;
import com.jeecms.system.domain.vo.ChannelDataPermTree;
import com.jeecms.system.domain.vo.CmsDataPermVo;
import com.jeecms.system.domain.vo.CmsDataPermVo.ChannelRow;
import com.jeecms.system.domain.vo.CmsDataPermVo.MiniDataUnit;
import com.jeecms.system.domain.vo.CmsDataPermVo.SiteMap;
import com.jeecms.system.domain.vo.CmsSiteOwnerTree;
import com.jeecms.system.domain.vo.CoreMenuOwnerTree;
import com.jeecms.system.domain.vo.OrgOwnerSitePermVo;
import com.jeecms.system.domain.vo.OrgPermVo;
import com.jeecms.system.domain.vo.SiteDataPermTree;
import com.jeecms.system.domain.vo.UserOwnerSitePermVo;
import com.jeecms.system.domain.vo.UserPermVo;
import com.jeecms.system.service.CmsDataPermPlusService;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;

/**
 * 权限扩展service
 * 
 * @author: tom
 * @date: 2019年4月28日 下午5:16:30
 */
@Service
@Transactional(rollbackFor = { Exception.class })
public class CmsDataPermPlusServiceImpl implements CmsDataPermPlusService {

        @Override
        public CmsDataPermVo assembleData(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType)
                        throws GlobalException {
                CmsDataPermVo vo = new CmsDataPermVo();
                HttpServletRequest request = RequestUtils.getHttpServletRequest();
                CoreUser currUser = SystemContextUtils.getUser(request);
                /** 权限来源组织 */
                CmsOrg sourceOrg = null;
                if (CmsDataPerm.DATA_TYPE_SITE_OWNER.equals(dataType)) {
                        List<CmsDataPermVo.SiteMap> siteDatas = new ArrayList<CmsDataPermVo.SiteMap>();
                        /** 展示站点 */
                        CopyOnWriteArraySet<CmsSite> viewSites = new CopyOnWriteArraySet<CmsSite>();
                        /** 组织/角色/用户拥有的站群站点 */
                        HashSet<CmsSite> ownerSites = new HashSet<CmsSite>();
                        /** 当前登录用户可分配的站点 */
                        HashSet<CmsSite> canAssignSites = new HashSet<CmsSite>();
                        canAssignSites.addAll(currUser.getOwnerSites());
                        Boolean newSiteAssigned = null;
                        Boolean newSiteOwner = null;
                        CmsOrg org = null;
                        if (orgId != null) {
                                /** 读取上级组织的权限、一级组织则读取所有站点 */
                                org = orgService.findById(orgId);
                                if (org != null) {
                                        if (org.getParent() != null) {
                                                viewSites = org.getParent().getOwnerSites();
                                                sourceOrg = org.getParent();
                                                vo.setAssigned(org.getHasAssignOwnerSite());
                                                /** 不能修改自己的组织权限 */
                                                vo.setManagerAble(org.getDeleteAble());
                                        } else {
                                                viewSites.addAll(siteService.findAll(false, true));
                                                vo.setAssigned(true);
                                                vo.setManagerAble(false);
                                        }
                                        newSiteAssigned = org.getHasAssignNewSiteOwner();
                                        newSiteOwner = org.getOwnerNewSiteOwner();
                                        ownerSites.addAll(org.getOwnerSites());
                                }
                        } else if (roleId != null) {
                                /** 读取角色所属组织站群权限 */
                                CoreRole role = roleService.findById(roleId);
                                if (role != null) {
                                        viewSites = role.getOrg().getOwnerSites();
                                        ownerSites.addAll(role.getOwnerSites());
                                        newSiteAssigned = role.getOwnerNewSiteOwner();
                                        newSiteOwner = role.getOwnerNewSiteOwner();
                                        vo.setAssigned(role.getHasAssignOwnerSite());
                                        sourceOrg = role.getOrg();
                                        vo.setManagerAble(role.getDeleteAble());
                                }
                        } else if (userId != null) {
                                /** 读取用户所属组织站群权限 */
                                CoreUser user = userService.findById(userId);
                                if (user != null) {
                                        viewSites = user.getOrg().getOwnerSites();
                                        ownerSites.addAll(user.getOwnerSites());
                                        newSiteAssigned = user.getHasAssignNewSiteOwner();
                                        newSiteOwner = user.getOwnerNewSiteOwner();
                                        vo.setAssigned(user.getHasAssignOwnerSite());
                                        /** 不能修改自己的权限 */
                                        vo.setManagerAble(user.getDeleteAble());
                                        sourceOrg = user.getOrg();
                                }
                        }
                        List<CmsSite>sites = new ArrayList<>(viewSites);
                        CmsSiteAgent.sortBySortNumAndChild(sites);
                        for (CmsSite site : sites) {
                                siteDatas.add(getSiteMap(org, null, null, currUser, site, ownerSites, canAssignSites));
                        }
                        Boolean newSiteCanAssign = currUser.getOwnerNewSiteOwner();
                        /** 是否可分配新站点权限需要综合上级设置 */
                        if (sourceOrg != null) {
                                Boolean sourceOwnerNew = sourceOrg.getOwnerNewSiteOwner();
                                if (sourceOwnerNew != null && !sourceOwnerNew) {
                                        newSiteCanAssign = false;
                                }
                        }
                        List<CmsSiteOwnerTree> sitePerms = CmsSiteOwnerTree.getChildTree(siteDatas);
                        /** 添加新建站点配置 */
                        sitePerms.add(getNewSiteOwnerMap(org, newSiteAssigned, newSiteOwner, newSiteCanAssign, null));
                        vo.setSites(sitePerms);
                } else if (CmsDataPerm.DATA_TYPE_MENU.equals(dataType)) {
                        List<CmsDataPermVo.MenuMap> menus = new ArrayList<CmsDataPermVo.MenuMap>();
                        /** 展示菜单 */
                        HashSet<CoreMenu> viewMenus = new HashSet<CoreMenu>();
                        /** 组织/角色/用户拥有的菜单 */
                        HashSet<CoreMenu> ownerMenus = new HashSet<CoreMenu>();
                        /** 当前登录用户可分配的菜单 */
                        HashSet<CoreMenu> canAssignMenus = new HashSet<CoreMenu>();
                        canAssignMenus.addAll(currUser.getOwnerMenus());
                        Boolean newMenuOwner = null;
                        Boolean newMenuCanAssign = currUser.getOwnerNewMenuOwner();
                        CmsOrg org = null;
                        if (orgId != null) {
                                /** 读取上级组织的菜单、一级组织则读取所有菜单 */
                                org = orgService.findById(orgId);
                                if (org.getParent() != null) {
                                        viewMenus = org.getParent().getAuthOwnerMenus();
                                        sourceOrg = org.getParent();
                                        vo.setAssigned(org.getHasAssignOwnerMenu());
                                        vo.setManagerAble(org.getDeleteAble());
                                } else {
                                        viewMenus.addAll(menuService.findAllWithAuth());
                                        vo.setAssigned(true);
                                        vo.setManagerAble(false);
                                }
                                newMenuOwner = org.getOwnerNewMenuOwner();
                                ownerMenus.addAll(org.getAuthOwnerMenus());
                        } else if (roleId != null) {
                                /** 读取角色所属组织菜单 */
                                CoreRole role = roleService.findById(roleId);
                                viewMenus = role.getOrg().getAuthOwnerMenus();
                                ownerMenus.addAll(role.getOwnerMenus());
                                newMenuOwner = role.getOwnerNewMenuOwner();
                                vo.setAssigned(role.getHasAssignOwnerMenu());
                                vo.setManagerAble(role.getDeleteAble());
                                sourceOrg = role.getOrg();
                        } else if (userId != null) {
                                /** 读取用户所属组织菜单 */
                                CoreUser user = userService.findById(userId);
                                viewMenus = user.getOrg().getAuthOwnerMenus();
                                ownerMenus.addAll(user.getOwnerMenus());
                                newMenuOwner = user.getOwnerNewMenuOwner();
                                vo.setAssigned(user.getHasAssignOwnerMenu());
                                vo.setManagerAble(user.getDeleteAble());
                                sourceOrg = user.getOrg();
                        }
                        for (CoreMenu menu : viewMenus) {
                                menus.add(getMenuMap(org, menu, ownerMenus, canAssignMenus));
                        }
                        /** 是否可分配新站点权限需要综合上级设置 */
                        if (sourceOrg != null) {
                                Boolean sourceOwnerNew = sourceOrg.getOwnerNewMenuOwner();
                                if (sourceOwnerNew != null && !sourceOwnerNew) {
                                        newMenuCanAssign = false;
                                }
                        }
                        List<CoreMenuOwnerTree> menuTree = CoreMenuOwnerTree.getChildTree(menus);
                        /** 添加新建菜单配置 */
                        menuTree.add(getNewMenuOwnerMap(org, newMenuOwner, newMenuCanAssign));
                        vo.setMenus(menuTree);
                } else if (CmsDataPerm.DATA_TYPE_SITE.equals(dataType)) {
                        /** 展示站点权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> viewSiteDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                        /** 组织/角色/用户拥有的站点权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> ownerSiteDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                        List<Short> ownerMewSiteOpes = new ArrayList<Short>();
                        /** 当前登录用户可分配的站点权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> canAssignDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                        List<Short> canAssignNewSiteOpes = new ArrayList<Short>();
                        canAssignDataPerms.addAll(
                                        currUser.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, null));
                        canAssignNewSiteOpes = Arrays.asList(currUser.getOwnerNewSiteOperators());
                        CmsOrg org = null;
                        if (orgId != null) {
                                /** 读取上级组织的权限、一级组织则读取所有站点 */
                                org = orgService.findById(orgId);
                                if (org.getParent() != null) {
                                        viewSiteDataPerms = org.getParent().getOwnerDataPermsByType(
                                                        CmsDataPerm.DATA_TYPE_SITE, null, null, null, false);
                                        sourceOrg = org.getParent();
                                        vo.setAssigned(org.getHasAssignDataPermsByType(dataType));
                                        vo.setManagerAble(org.getDeleteAble());
                                } else {
                                        /** 构造系统所有站点的站点操作数据 */
                                        List<CmsSite> sites = new ArrayList<CmsSite>();
                                        sites.addAll(siteService.findAll(false, true));
                                        viewSiteDataPerms = CmsDataPerm.initOrgDataPermsForSite(
                                                        CmsDataPerm.OPE_SITE_ARRAY, org, sites);
                                        vo.setAssigned(true);
                                        vo.setManagerAble(false);
                                }
                                ownerSiteDataPerms.addAll(org.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null,
                                                null, null, false));
                                ownerMewSiteOpes = Arrays.asList(org.getOwnerNewSiteOperators());
                        } else if (roleId != null) {
                                /** 读取角色所属组织权限 */
                                CoreRole role = roleService.findById(roleId);
                                viewSiteDataPerms = role.getOrg().getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE,
                                                null, null, null, false);
                                ownerSiteDataPerms.addAll(role.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null,
                                                null, null));
                                ownerMewSiteOpes = Arrays.asList(role.getOwnerNewSiteOperators());
                                vo.setAssigned(role.getHasAssignDataPermsByType(dataType));
                                vo.setManagerAble(role.getDeleteAble());
                                sourceOrg = role.getOrg();
                        } else if (userId != null) {
                                /** 读取用户所属组织权限 */
                                CoreUser user = userService.findById(userId);
                                viewSiteDataPerms = user.getOrg().getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE,
                                                null, null, null, false);
                                ownerSiteDataPerms.addAll(user.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null,
                                                null, null));
                                ownerMewSiteOpes = Arrays.asList(user.getOwnerNewSiteOperators());
                                vo.setAssigned(user.getHasAssignDataPermsByType(dataType));
                                vo.setManagerAble(user.getDeleteAble());
                                sourceOrg = user.getOrg();
                        }
                        /** 是否可分配新站点数据权限需要综合上级设置 */
                        if (sourceOrg != null) {
                                Short[] sourceNewSiteOpes = sourceOrg.getOwnerNewSiteOperators();
                                CopyOnWriteArrayList<Short> filterNewSiteOpes = new CopyOnWriteArrayList<Short>();
                                if (sourceNewSiteOpes != null) {
                                        for (Short op : sourceNewSiteOpes) {
                                                if (canAssignNewSiteOpes.contains(op)) {
                                                        filterNewSiteOpes.add(op);
                                                }
                                        }
                                }
                                canAssignNewSiteOpes = filterNewSiteOpes;
                        }
                        List<CmsDataPermVo.SiteRow> siteRows = new ArrayList<CmsDataPermVo.SiteRow>();
                        siteRows.addAll(getSiteRow(org, viewSiteDataPerms, ownerSiteDataPerms, canAssignDataPerms));
                        List<SiteDataPermTree> permTree = SiteDataPermTree.getChildTree(siteRows);
                        permTree = SiteDataPermTree.sortBySortNumAndChild(permTree);
                        vo.setDataIds(permTree);
                } else {
                        /** 栏目、内容、数据权限 */
                        /** 展示栏目类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> viewDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                        /** 组织/角色/用户拥有的栏目类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> ownerDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                        /** 当前登录用户可分配的栏目类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> canAssignDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                        canAssignDataPerms.addAll(currUser.getOwnerDataPermsByType(dataType, siteId, null, null));
                        CmsOrg org = null;
                        CoreRole role = null;
                        CoreUser user = null;
                        if (orgId != null) {
                                /** 读取上级组织的权限、一级组织则读取所有站点 */
                                org = orgService.findById(orgId);
                                if (org.getParent() != null) {
                                        viewDataPerms = org.getParent().getOwnerDataPermsByType(dataType, siteId, null,
                                                        null, false);
                                        sourceOrg = org.getParent();
                                        vo.setAssigned(org.getHasAssignDataPermsByType(dataType));
                                        vo.setManagerAble(org.getDeleteAble());
                                } else {
                                        /** 构造系统所有站点的栏目类操作数据 */
                                        List<CmsSite> sites = new ArrayList<CmsSite>();
                                        sites.addAll(siteService.findAll(false, true));
                                        CmsSiteAgent.sortByIdAndChild(sites);
                                        if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                                                viewDataPerms = CmsDataPerm.initOrgDataPermsForChannelBySite(org, sites,
                                                                false);
                                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                                                viewDataPerms = CmsDataPerm.initOrgDataPermsForContentBySite(org, sites,
                                                                false);
                                        }
                                        vo.setAssigned(true);
                                        vo.setManagerAble(false);
                                }
                                ownerDataPerms.addAll(org.getOwnerDataPermsByType(dataType, siteId, null, null, true));
                        } else if (roleId != null) {
                                /** 读取角色所属组织权限 */
                                role = roleService.findById(roleId);
                                viewDataPerms = role.getOrg().getOwnerDataPermsByType(dataType, siteId, null, null,
                                                false);
                                ownerDataPerms.addAll(role.getOwnerDataPermsByType(dataType, siteId, null, null));
                                vo.setAssigned(role.getHasAssignDataPermsByType(dataType));
                                vo.setManagerAble(role.getDeleteAble());
                                sourceOrg = role.getOrg();
                        } else if (userId != null) {
                                /** 读取用户所属组织权限 */
                                user = userService.findById(userId);
                                viewDataPerms = user.getOrg().getOwnerDataPermsByType(dataType, siteId, null, null,
                                                false);
                                ownerDataPerms.addAll(user.getOwnerDataPermsByType(dataType, siteId, null, null));
                                vo.setAssigned(user.getHasAssignDataPermsByType(dataType));
                                vo.setManagerAble(user.getDeleteAble());
                                sourceOrg = user.getOrg();
                        }
                        /** 文档、栏目类权限站点ID必填 */
                        if (siteId == null) {
                                throw new GlobalException(new IllegalParamExceptionInfo());
                        }
                        CmsSite site = siteService.findById(siteId);
                        /** 是否可分配数据权限需要综合上级设置 */
                        if (sourceOrg != null) {
                                CopyOnWriteArraySet<CmsDataPerm> sourceDataPerms = sourceOrg
                                                .getOwnerDataPermsByType(dataType, siteId, null, null, true);
                                CopyOnWriteArraySet<CmsDataPerm> filterAssignDataPerms = new CopyOnWriteArraySet<CmsDataPerm>();
                                if (sourceDataPerms != null) {
                                        for (CmsDataPerm p : sourceDataPerms) {
                                                if (p.beContains(canAssignDataPerms)) {
                                                        filterAssignDataPerms.add(p);
                                                }
                                        }
                                }
                                canAssignDataPerms = filterAssignDataPerms;
                        }
                        ChannelRow channelRow = createOneSiteChannelRows(site, currUser, dataType, org, role, user,
                                        sourceOrg, viewDataPerms, ownerDataPerms, canAssignDataPerms);
                        vo.setMoreDataIds(channelRow);
                }
                return vo;
        }

        @Override
        public Set<CmsSite> getSites(Integer orgId, Integer roleId, Integer userId, Short dataType) {
                CmsOrg org = null;
                CoreUser user;
                CoreRole role;
                /** 上级组织站群类权限站点 */
                Set<CmsSite> ownerSites = new CopyOnWriteArraySet<CmsSite>();
                /** 上级组织、所属组织 文档或栏目权限站点 */
                Set<CmsSite> orgDataSites = new HashSet<CmsSite>();
                CmsOrg originOrg = null;
                if (orgId != null) {
                        /** 读取上级组织的权限、一级组织则读取所有站点 */
                        org = orgService.findById(orgId);
                        if (org.getParent() != null) {
                                originOrg = org.getParent();
                        }
                        ownerSites = org.getCloneOwnerSites();
                } else if (roleId != null) {
                        /** 读取角色所属组织权限 */
                        role = roleService.findById(roleId);
                        originOrg = role.getOrg();
                        ownerSites = role.getCloneOwnerSites();
                } else if (userId != null) {
                        /** 读取用户所属组织权限 */
                        user = userService.findById(userId);
                        originOrg = user.getOrg();
                        ownerSites = user.getCloneOwnerSites();
                }
                if (originOrg != null) {
                        orgDataSites = originOrg.getSiteOfOwnerDataPermsByType(dataType);
                        List<Integer> orgDateSiteIds = CmsSite.fetchIds(orgDataSites);
                        /** 只保留上级组织、所属组织 栏目类(或者文档类)权限中所选站点 */
                        for (CmsSite site : ownerSites) {
                                if (!orgDateSiteIds.contains(site.getId())) {
                                        ownerSites.remove(site);
                                }
                        }
                } else {
                        /** 顶层组织则构造系统所有站点的栏目类操作数据 */
                        if (org != null) {
                                ownerSites.addAll(org.getOwnerSites());
                                orgDataSites = ownerSites;
                        }
                }
                return ownerSites;
        }

        @Override
        public List<OrgPermVo> assembleDataByChannelForOrg(Channel channel, CmsOrg org, Short dateType) {
                List<CmsOrg> orgs = org.getChildNodeList();
                orgs = CmsOrgAgent.sortBySortAndChild(orgs);
                List<OrgPermVo> orgPermVos = new ArrayList<OrgPermVo>();
                CoreUser currUser = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (channel != null) {
                        Integer siteId = channel.getSiteId();
                        /** 当前登录用户可分配的栏目类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> canAssignPerms = currUser.getOwnerDataPermsByType(dateType,
                                        siteId, null, channel.getId());
                        for (CmsOrg or : orgs) {
                                OrgPermVo orgPermVo = new OrgPermVo();
                                orgPermVo.setId(or.getId());
                                orgPermVo.setOrg(or);
                                /** 组织/角色/用户拥有的栏目类权限数据 */
                                CopyOnWriteArraySet<CmsDataPerm> orgOwnerPerms = or.getOwnerDataPermsByType(dateType,
                                                siteId, null, channel.getId(), false);
                                CmsDataPermVo.ChannelUnit channelUnit = getChannelUnit(or, null, null, channel,
                                                currUser, dateType, orgOwnerPerms, canAssignPerms);
                                /** 先添加组织的权限信息 */
                                orgPermVo.setOps(channelUnit.getRowDatas());
                                orgPermVo.setChildren(new ArrayList<OrgPermVo>());
                                /** 后添加角色的权限信息 */
                                for (CoreRole role : or.getRoles()) {
                                        OrgPermVo rolePermVo = new OrgPermVo();
                                        /** 组织/角色/用户拥有的栏目类权限数据 */
                                        CopyOnWriteArraySet<CmsDataPerm> roleOwnerPerms = role.getOwnerDataPermsByType(
                                                        dateType, siteId, null, channel.getId());
                                        CmsDataPermVo.ChannelUnit roleChannelUnit = getChannelUnit(null, role, null,
                                                        channel, currUser, dateType, roleOwnerPerms, canAssignPerms);
                                        rolePermVo.setRole(role);
                                        rolePermVo.setOps(roleChannelUnit.getRowDatas());
                                        orgPermVo.getChildren().add(rolePermVo);
                                }
                                orgPermVos.add(orgPermVo);
                        }
                }
                return orgPermVos;
        }

        @Override
        public Page<UserPermVo> assembleDataByChannelForUser(Channel channel, CmsOrg org, Short dateType, Integer orgid,
                        Integer roleid, String key, Pageable pageable) {
                List<Integer> roleIds = null;
                if (roleid != null) {
                        roleIds = ImmutableList.of(roleid);
                }
                List<Integer> orgIds = org.getChildOrgIds();
                if (orgid != null) {
                        orgIds = ImmutableList.of(orgid);
                }
                Page<CoreUser> users = coreUserService.pageUser(true, orgIds, roleIds, key, true,
                                CoreUser.AUDIT_USER_STATUS_PASS, null, null, null, null, pageable);
                List<UserPermVo> userPermVos = new ArrayList<UserPermVo>();
                CoreUser currUser = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                if (channel != null) {
                        Integer siteId = channel.getSiteId();
                        /** 当前登录用户可分配的栏目类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> canAssignPerms = currUser.getOwnerDataPermsByType(dateType,
                                        siteId, null, channel.getId());
                        for (CoreUser user : users.getContent()) {
                                UserPermVo userPermVo = new UserPermVo();
                                userPermVo.setUser(user);
                                /** 组织/角色/用户拥有的栏目类权限数据 */
                                CopyOnWriteArraySet<CmsDataPerm> orgOwnerPerms = user.getOwnerDataPermsByType(dateType,
                                                siteId, null, channel.getId());
                                CmsDataPermVo.ChannelUnit channelUnit = getChannelUnit(null, null, user, channel,
                                                currUser, dateType, orgOwnerPerms, canAssignPerms);
                                userPermVo.setOps(channelUnit.getRowDatas());
                                userPermVos.add(userPermVo);
                        }
                }
                Page<UserPermVo> page = new PageImpl<>(userPermVos, pageable, users.getTotalElements());
                return page;
        }

        @Override
        public List<OrgOwnerSitePermVo> assembleDataByOwnerSiteForOrg(CmsSite site, CmsOrg org) {
                List<CmsOrg> orgs = org.getChildNodeList();
                orgs = CmsOrgAgent.sortListBySortAndChild(orgs);
                List<OrgOwnerSitePermVo> orgPermVos = new ArrayList<OrgOwnerSitePermVo>();
                CoreUser currUser = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 当前登录用户可分配的栏目类权限数据 */
                CopyOnWriteArraySet<CmsSite> canAssignSites = currUser.getOwnerSites();
                for (CmsOrg or : orgs) {
                        OrgOwnerSitePermVo orgPermVo = new OrgOwnerSitePermVo();
                        orgPermVo.setId(or.getId());
                        orgPermVo.setOrg(or);
                        /** 组织/角色/用户拥有的站群权限数据 */
                        CopyOnWriteArraySet<CmsSite> ownerSites = or.getOwnerSites();
                        /** 原分配站群权限数据 */
                        HashSet<CmsSite> assignSites = new HashSet<>();
                        assignSites.addAll(or.getEffectSites());
                        SiteMap siteOwner = getSiteMap(or, null, null, currUser, site, ownerSites, canAssignSites);
                        orgPermVo.setSiteOwner(siteOwner);
                        orgPermVo.setChildren(new ArrayList<OrgOwnerSitePermVo>());
                        List<OrgOwnerSitePermVo> roleOwners = new ArrayList<OrgOwnerSitePermVo>();
                        for (CoreRole role : or.getRoles()) {
                                OrgOwnerSitePermVo rolePermVo = new OrgOwnerSitePermVo();
                                /** 组织/角色/用户拥有的站群权限数据 */
                                CopyOnWriteArraySet<CmsSite> roleOwnerSites = role.getOwnerSites();
                                /** 原分配站群权限数据 */
                                CopyOnWriteArraySet<CmsSite> roleAssignSites = new CopyOnWriteArraySet<CmsSite>();
                                roleAssignSites.addAll(role.getEffectSites());
                                SiteMap roleSiteOwner = getSiteMap(null, role, null, currUser, site, roleOwnerSites,
                                                canAssignSites);
                                rolePermVo.setSiteOwner(roleSiteOwner);
                                rolePermVo.setRole(role);
                                roleOwners.add(rolePermVo);
                        }
                        /** 将角色权限放入到组织权限的子节点下 */
                        orgPermVo.getChildren().addAll(roleOwners);
                        orgPermVos.add(orgPermVo);
                }
                return orgPermVos;
        }

        @Override
        public Page<UserOwnerSitePermVo> assembleDataByOwnerSiteForUser(CmsSite site, CmsOrg org, Integer orgid,
                        Integer roleid, String key, Pageable pageable) {
                List<Integer> roleIds = null;
                List<Integer> orgIds = org.getChildOrgIds();
                if (roleid != null) {
                        roleIds = ImmutableList.of(roleid);
                }
                if (orgid != null) {
                        orgIds = ImmutableList.of(orgid);
                }
                Page<CoreUser> users = coreUserService.pageUser(true, orgIds, roleIds, key, true,
                                CoreUser.AUDIT_USER_STATUS_PASS, null, null, null, null, pageable);
                List<UserOwnerSitePermVo> ownerSitePerms = new ArrayList<UserOwnerSitePermVo>();
                CoreUser currUser = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 当前登录用户可分配的站群权限数据 */
                CopyOnWriteArraySet<CmsSite> canAssignSites = currUser.getOwnerSites();
                for (CoreUser user : users.getContent()) {
                        UserOwnerSitePermVo userPermVo = new UserOwnerSitePermVo();
                        userPermVo.setUser(user);
                        /** 组织/角色/用户拥有的站群数据 */
                        CopyOnWriteArraySet<CmsSite> ownerSites = user.getOwnerSites();
                        SiteMap siteOwner = getSiteMap(null, null, user, currUser, site, ownerSites, canAssignSites);
                        userPermVo.setSiteOwner(siteOwner);
                        ownerSitePerms.add(userPermVo);
                }
                Page<UserOwnerSitePermVo> page = new PageImpl<>(ownerSitePerms, pageable, users.getTotalElements());
                return page;
        }

        @Override
        public List<OrgPermVo> assembleDataBySiteForOrg(CmsSite site, CmsOrg org) {
                List<CmsOrg> orgs = org.getChildNodeList();
                orgs = CmsOrgAgent.sortListBySortAndChild(orgs);
                List<OrgPermVo> orgPermVos = new ArrayList<OrgPermVo>();
                CoreUser currUser = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 当前登录用户可分配的栏目类权限数据 */
                CopyOnWriteArraySet<CmsDataPerm> canAssignPerms = currUser
                                .getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, site.getId());
                for (CmsOrg or : orgs) {
                        OrgPermVo orgPermVo = new OrgPermVo();
                        orgPermVo.setId(or.getId());
                        orgPermVo.setOrg(or);
                        /** 组织/角色/用户拥有的栏目类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> orgOwnerPerms = or.getOwnerDataPermsByType(
                                        CmsDataPerm.DATA_TYPE_SITE, null, null, site.getId(), false);
                        List<MiniDataUnit> siteOps = getInitSiteOps(or, null, null, currUser, orgOwnerPerms,
                                        canAssignPerms);
                        /** 先添加组织的权限信息 */
                        orgPermVo.setOps(siteOps);
                        orgPermVo.setChildren(new ArrayList<OrgPermVo>());
                        /** 后添加角色的权限信息 */
                        for (CoreRole role : or.getRoles()) {
                                OrgPermVo rolePermVo = new OrgPermVo();
                                /** 组织/角色/用户拥有的栏目类权限数据 */
                                CopyOnWriteArraySet<CmsDataPerm> roleOwnerPerms = role.getOwnerDataPermsByType(
                                                CmsDataPerm.DATA_TYPE_SITE, null, null, site.getId());
                                rolePermVo.setRole(role);
                                rolePermVo.setOps((getInitSiteOps(null, role, null, currUser, roleOwnerPerms,
                                                canAssignPerms)));
                                orgPermVo.getChildren().add(rolePermVo);
                        }
                        orgPermVos.add(orgPermVo);
                }
                return orgPermVos;
        }

        @Override
        public Page<UserPermVo> assembleDataBySiteForUser(CmsSite site, CmsOrg org, Integer orgid, Integer roleid,
                        String key, Pageable pageable) {
                List<Integer> roleIds = null;
                List<Integer> orgIds = org.getChildOrgIds();
                if (roleid != null) {
                        roleIds = ImmutableList.of(roleid);
                }
                if (orgid != null) {
                        orgIds = ImmutableList.of(orgid);
                }
                Page<CoreUser> users = coreUserService.pageUser(true, orgIds, roleIds, key, true,
                                CoreUser.AUDIT_USER_STATUS_PASS, null, null, null, null, pageable);
                List<UserPermVo> userPermVos = new ArrayList<UserPermVo>();
                CoreUser currUser = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
                /** 当前登录用户可分配的站点类权限数据 */
                CopyOnWriteArraySet<CmsDataPerm> canAssignPerms = currUser
                                .getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, site.getId());
                for (CoreUser user : users.getContent()) {
                        UserPermVo userPermVo = new UserPermVo();
                        userPermVo.setUser(user);
                        /** 组织/角色/用户拥有的站点类权限数据 */
                        CopyOnWriteArraySet<CmsDataPerm> ownerPerms = user
                                        .getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, site.getId());
                        userPermVo.setOps(getInitSiteOps(null, null, user, currUser, ownerPerms, canAssignPerms));
                        userPermVos.add(userPermVo);
                }
                Page<UserPermVo> page = new PageImpl<>(userPermVos, pageable, users.getTotalElements());
                return page;
        }

        private CmsDataPermVo.ChannelRow createOneSiteChannelRows(CmsSite site, CoreUser currUser, Short dataType,
                        CmsOrg org, CoreRole role, CoreUser user, CmsOrg sourceOrg, Set<CmsDataPerm> viewDataPerms,
                        Set<CmsDataPerm> ownerDataPerms, Set<CmsDataPerm> canAssignDataPerms) {
                CmsDataPermVo.ChannelRow oneSiteData = new CmsDataPermVo.ChannelRow();
                List<CmsDataPermVo.ChannelUnit> units = new ArrayList<CmsDataPermVo.ChannelUnit>();
                oneSiteData.setSite(site);
                List<Channel> channels = new ArrayList<Channel>();
                if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType) || CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                        channels = channelService.findList(site.getId(), true);
                }
                ChannelAgent agent = new ChannelAgent();
                List<Channel> toViewChannels = agent
                                .sort(channels.stream().filter(c -> !c.getRecycle()).collect(Collectors.toList()));
                Set<Integer> channelIds = CmsDataPerm.getDataIds(viewDataPerms);
                for (Channel c : toViewChannels) {
                        /** 在可读权限范围内才需要组装栏目权限数据，否则不需要展示该栏目 */
                        if (channelIds.contains(c.getId())) {
                                HashSet<CmsDataPerm> ownerSiteDataPerms = new HashSet<CmsDataPerm>();
                                ownerSiteDataPerms.addAll(ownerDataPerms.stream()
                                                .filter(perm -> c.getId().equals(perm.getDataId()))
                                                .collect(Collectors.toSet()));

                                HashSet<CmsDataPerm> canAssignSiteDataPerms = new HashSet<CmsDataPerm>();
                                canAssignSiteDataPerms.addAll(canAssignDataPerms.stream()
                                                .filter(perm -> c.getId().equals(perm.getDataId()))
                                                .collect(Collectors.toSet()));
                                units.add(getChannelUnit(org, role, user, c, currUser, dataType, ownerSiteDataPerms,
                                                canAssignSiteDataPerms));
                        }
                }
                /** 新栏目配置 */
                List<Short> viewChannelOpes = new ArrayList<Short>();
                List<Short> ownerChannelOpes = new ArrayList<Short>();
                List<Short> canAssignChannelOpes = new ArrayList<Short>();
                Short[] sourceNewChannelOpes = null;
                if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                        canAssignChannelOpes = Arrays.asList(currUser.getOwnerNewChannelOperators(site.getId()));
                        if (sourceOrg != null) {
                                sourceNewChannelOpes = sourceOrg.getOwnerNewChannelOperators(site.getId());
                        }
                } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                        canAssignChannelOpes = Arrays.asList(currUser.getOwnerNewChannelContentOperators(site.getId()));
                        if (sourceOrg != null) {
                                sourceNewChannelOpes = sourceOrg.getOwnerNewChannelContentOperators(site.getId());
                        }
                }
                /** 是否可分配新栏目数据权限需要综合上级设置 */
                CopyOnWriteArrayList<Short> filterNewChannelOpes = new CopyOnWriteArrayList<Short>();
                if (sourceOrg != null) {
                        if (sourceNewChannelOpes != null) {
                                for (Short op : sourceNewChannelOpes) {
                                        if (canAssignChannelOpes.contains(op)) {
                                                filterNewChannelOpes.add(op);
                                        }
                                }
                        }
                }
                if (org != null) {
                        if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                                viewChannelOpes = Arrays.asList(org.getOwnerNewChannelOperators(site.getId()));
                                ownerChannelOpes = Arrays.asList(org.getOwnerNewChannelOperators(site.getId()));
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                                viewChannelOpes = Arrays.asList(org.getOwnerNewChannelContentOperators(site.getId()));
                                ownerChannelOpes = Arrays.asList(org.getOwnerNewChannelContentOperators(site.getId()));
                        }
                } else if (role != null) {
                        if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                                viewChannelOpes = Arrays
                                                .asList(role.getOrg().getOwnerNewChannelOperators(site.getId()));
                                ownerChannelOpes = Arrays.asList(role.getOwnerNewChannelOperators(site.getId()));
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                                viewChannelOpes = Arrays
                                                .asList(role.getOrg().getOwnerNewChannelContentOperators(site.getId()));
                                ownerChannelOpes = Arrays.asList(role.getOwnerNewChannelContentOperators(site.getId()));
                        }
                } else if (user != null) {
                        if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                                viewChannelOpes = Arrays
                                                .asList(user.getOrg().getOwnerNewChannelOperators(site.getId()));
                                ownerChannelOpes = Arrays.asList(user.getOwnerNewChannelOperators(site.getId()));
                        } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                                viewChannelOpes = Arrays
                                                .asList(user.getOrg().getOwnerNewChannelContentOperators(site.getId()));
                                ownerChannelOpes = Arrays.asList(user.getOwnerNewChannelContentOperators(site.getId()));
                        }
                }
                List<ChannelDataPermTree> unitTree = ChannelDataPermTree.getChildTree(units);
                unitTree.add(getNewSiteChannelRows(org, dataType, viewChannelOpes, ownerChannelOpes,
                                filterNewChannelOpes));
                oneSiteData.setUnits(unitTree);
                return oneSiteData;
        }

        private ChannelDataPermTree getNewSiteChannelRows(CmsOrg org, Short dataType, Collection<Short> viewChannelOpes,
                        Collection<Short> ownerChannelOpes, Collection<Short> canAssignChannelOpes) {
                ChannelDataPermTree unit = new ChannelDataPermTree();
                List<CmsDataPermVo.MiniDataUnit> rowOpes = new ArrayList<CmsDataPermVo.MiniDataUnit>();
                unit.setRowDatas(rowOpes);
                Short[] opes = new Short[] {};
                if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                        opes = CmsDataPerm.OPE_CHANNEL_ARRAY;
                } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                        opes = CmsDataPerm.OPE_CONTENT_ARRAY;
                }
                boolean isTopOrg = org != null ? org.isTop() : false;
                for (Short p : opes) {
                        boolean canAssign = getCanAssignInitValue();
                        boolean hasPerm = false;
                        /** 顶层组织有所有权限且不可分配权限 */
                        if (!isTopOrg && canAssignChannelOpes.contains(p)) {
                                canAssign = true;
                        }
                        if (isTopOrg || ownerChannelOpes.contains(p)) {
                                hasPerm = true;
                        }
                        CmsDataPermVo.MiniDataUnit miniUnit = new CmsDataPermVo.MiniDataUnit(p, canAssign, hasPerm);
                        unit.getRowDatas().add(miniUnit);
                }
                if (unit.getRowDatas() != null) {
                        Collections.sort(unit.getRowDatas());
                }
                unit.setId(0);
                unit.setName(MessageResolver.getMessage("tip.channel.new"));
                return unit;
        }

        private CmsDataPermVo.ChannelUnit getChannelUnit(CmsOrg org, CoreRole role, CoreUser user, Channel channel,
                        CoreUser currUser, Short dataType, Set<CmsDataPerm> ownerDataPerms,
                        Set<CmsDataPerm> canAssignDataPerms) {
                CmsDataPermVo.ChannelUnit unit = new CmsDataPermVo.ChannelUnit();
                List<CmsDataPermVo.MiniDataUnit> rowDatas = new ArrayList<CmsDataPermVo.MiniDataUnit>();
                unit.setRowDatas(rowDatas);
                Short[] ops = new Short[] {};
                if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                        ops = CmsDataPerm.OPE_CHANNEL_ARRAY;
                } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                        ops = CmsDataPerm.OPE_CONTENT_ARRAY;
                }
                boolean isTopOrg = org != null ? org.isTop() : false;
                for (Short p : ops) {
                        boolean canAssign = getCanAssignInitValue();
                        boolean hasPerm = false;
                        List<Short> ownerOps = ownerDataPerms.stream().map(perm -> perm.getOperation())
                                        .collect(Collectors.toList());
                        List<Short> canAssignOps = canAssignDataPerms.stream().map(perm -> perm.getOperation())
                                        .collect(Collectors.toList());
                        /** 顶层组织不可分配权限，且有权限 */
                        if (!isTopOrg && canAssignOps.contains(p)) {
                                canAssign = true;
                        }
                        if (isTopOrg || ownerOps.contains(p)) {
                                hasPerm = true;
                        }
                        /** 当前用户所属组织不可分配 */
                        if (currUser != null && org != null && org.getId().equals(currUser.getOrgId())) {
                                canAssign = false;
                        }
                        /** 当前用户所属角色不可分配 */
                        if (currUser != null && role != null && currUser.getRoleIds().contains(role.getId())) {
                                canAssign = false;
                        }
                        /** 当前用户不可分配 */
                        if (currUser != null && user != null && user.getId().equals(currUser.getId())) {
                                canAssign = false;
                        }
                        CmsDataPermVo.MiniDataUnit miniUnit = new CmsDataPermVo.MiniDataUnit(p, canAssign, hasPerm);
                        unit.getRowDatas().add(miniUnit);
                }
                unit.setChannel(channel);
                unit.setName(channel.getName());
                if (unit.getRowDatas() != null) {
                        Collections.sort(unit.getRowDatas());
                }
                return unit;
        }

        private List<CmsDataPermVo.SiteRow> getSiteRow(CmsOrg org, Set<CmsDataPerm> viewDataPerms,
                        Set<CmsDataPerm> ownerDataPerms, Set<CmsDataPerm> canAssignDataPerms) {
                // 按站点ID进行分组
                Map<Integer, List<CmsDataPerm>> viewDataPermMap = viewDataPerms.stream()
                                .collect(Collectors.groupingBy(CmsDataPerm::getSiteId));
                Map<Integer, List<CmsDataPerm>> ownerDataPermMap = ownerDataPerms.stream()
                                .collect(Collectors.groupingBy(CmsDataPerm::getSiteId));
                Map<Integer, List<CmsDataPerm>> canAssignDataPermMap = canAssignDataPerms.stream()
                                .collect(Collectors.groupingBy(CmsDataPerm::getSiteId));
                List<CmsDataPermVo.SiteRow> result = new ArrayList<CmsDataPermVo.SiteRow>();
                boolean isTopOrg = org != null && org.isTop() ? org.isTop() : false;
                Set<Integer> siteIds = viewDataPermMap.keySet();
                List<Integer> siteIdList = CmsSiteAgent.sortIds(siteIds);
                for (Integer siteId : siteIdList) {
                        CmsDataPermVo.SiteRow siteRow = new CmsDataPermVo.SiteRow();
                        List<CmsDataPermVo.MiniDataUnit> units = new ArrayList<CmsDataPermVo.MiniDataUnit>();
                        siteRow.setRowDatas(units);
                        Set<Short> hasViewOpes = new HashSet<Short>();
                        for (CmsDataPerm p : viewDataPermMap.get(siteId)) {
                                boolean canAssign = getCanAssignInitValue();
                                boolean hasPerm = false;
                                if (!isTopOrg && p.beContains(canAssignDataPermMap.get(siteId))) {
                                        canAssign = true;
                                }
                                if (isTopOrg || p.beContains(ownerDataPermMap.get(siteId))) {
                                        hasPerm = true;
                                }
                                CmsDataPermVo.MiniDataUnit unit = new CmsDataPermVo.MiniDataUnit(p.getOperation(),
                                                canAssign, hasPerm);
                                siteRow.getRowDatas().add(unit);
                                hasViewOpes.add(p.getOperation());
                        }
                        /** 未显示的操作全部显示没有权限,添加进入结果集中方便前端处理 */
                        for (Short op : CmsDataPerm.OPE_SITE_ARRAY) {
                                if (!hasViewOpes.contains(op)) {
                                        CmsDataPermVo.MiniDataUnit unit = new CmsDataPermVo.MiniDataUnit(op, false,
                                                        false);
                                        siteRow.getRowDatas().add(unit);
                                }
                        }
                        CmsSite site = siteService.findById(siteId);
                        siteRow.setSite(site);
                        if (siteRow.getRowDatas() != null) {
                                Collections.sort(siteRow.getRowDatas());
                        }
                        result.add(siteRow);
                }

                return result;
        }

        private List<MiniDataUnit> getInitSiteOps(CmsOrg org, CoreRole role, CoreUser user, CoreUser currUser,
                        Set<CmsDataPerm> ownerDataPerms, Set<CmsDataPerm> canAssignDataPerms) {
                List<CmsDataPermVo.MiniDataUnit> units = new ArrayList<CmsDataPermVo.MiniDataUnit>();
                for (Short p : CmsDataPerm.OPE_SITE_ARRAY) {
                        boolean canAssign = getCanAssignInitValue();
                        boolean hasPerm = false;
                        if (CmsDataPerm.getOperators(canAssignDataPerms).contains(p)) {
                                canAssign = true;
                        }
                        /** 顶层组织不可分配权限 */
                        if (org != null && org.isTop()) {
                                canAssign = false;
                        }
                        /** 当前用户所属组织不可分配 */
                        if (currUser != null && org != null && org.getId().equals(currUser.getOrgId())) {
                                canAssign = false;
                        }
                        /** 当前用户所属角色不可分配 */
                        if (currUser != null && role != null && currUser.getRoleIds().contains(role.getId())) {
                                canAssign = false;
                        }
                        /** 当前用户不可分配 */
                        if (currUser != null && user != null && user.getId().equals(currUser.getId())) {
                                canAssign = false;
                        }
                        if (CmsDataPerm.getOperators(ownerDataPerms).contains(p)) {
                                hasPerm = true;
                        }
                        CmsDataPermVo.MiniDataUnit unit = new CmsDataPermVo.MiniDataUnit(p, canAssign, hasPerm);
                        units.add(unit);
                }
                return units;
        }

        private SiteDataPermTree getNewSiteRow(CmsOrg org, Collection<Short> ownerMewSiteOpes,
                        Collection<Short> canAssignMewSiteOpes) {
                SiteDataPermTree siteRow = new SiteDataPermTree();
                List<CmsDataPermVo.MiniDataUnit> units = new ArrayList<CmsDataPermVo.MiniDataUnit>();
                siteRow.setRowDatas(units);
                boolean isTopOrg = org != null && org.isTop() ? org.isTop() : false;
                for (Short p : CmsDataPerm.OPE_SITE_ARRAY) {
                        // boolean assigned = false;
                        boolean canAssign = getCanAssignInitValue();
                        boolean hasPerm = false;
                        /** 顶层组织不可分配权限且有所有权限 */
                        if (!isTopOrg && canAssignMewSiteOpes.contains(p)) {
                                canAssign = true;
                        }
                        if (isTopOrg || ownerMewSiteOpes.contains(p)) {
                                hasPerm = true;
                        }
                        CmsDataPermVo.MiniDataUnit unit = new CmsDataPermVo.MiniDataUnit(p, canAssign, hasPerm);
                        siteRow.getRowDatas().add(unit);
                        siteRow.setId(0);
                        siteRow.setName(MessageResolver.getMessage("tip.site.new"));
                }
                if (siteRow.getRowDatas() != null) {
                        Collections.sort(siteRow.getRowDatas());
                }
                return siteRow;
        }

        private CmsDataPermVo.SiteMap getSiteMap(CmsOrg org, CoreRole role, CoreUser user, CoreUser currUser,
                        CmsSite site, Set<CmsSite> ownerSites, Set<CmsSite> canAssignSites) {
                CmsDataPermVo.SiteMap siteRow = new CmsDataPermVo.SiteMap();
                siteRow.setCanAssign(getCanAssignInitValue());
                siteRow.setHasPerm(false);
                siteRow.setName(site.getName());
                boolean isTopOrg = org != null && org.isTop();
                /** org正在构建的权限集合所属组织， 顶层组织不可更改可分配权限 */
                if (isTopOrg || CmsSite.fetchIds(ownerSites).contains(site.getId())) {
                        siteRow.setHasPerm(true);
                }
                if (!isTopOrg && CmsSite.fetchIds(canAssignSites).contains(site.getId())) {
                        siteRow.setCanAssign(true);
                }
                /** 当前用户所属组织不可分配 */
                if (currUser != null && org != null && org.getId().equals(currUser.getOrgId())) {
                        siteRow.setCanAssign(false);
                }
                /** 当前用户所属角色不可分配 */
                if (currUser != null && role != null && currUser.getRoleIds().contains(role.getId())) {
                        siteRow.setCanAssign(false);
                }
                /** 当前用户不可分配 */
                if (currUser != null && user != null && user.getId().equals(currUser.getId())) {
                        siteRow.setCanAssign(false);
                }
                siteRow.setSite(site);
                return siteRow;
        }

        /**
         * 创建是否有新站点站群权限对象
         * @return CmsDataPermVo.SiteMap
         */
        private CmsSiteOwnerTree getNewSiteOwnerMap(CmsOrg org, Boolean assignNewSiteOwner, Boolean ownerNewSiteOwner,
                        Boolean canAssignNewSiteOwner, CmsSite site) {
                CmsSiteOwnerTree siteRow = new CmsSiteOwnerTree();
                siteRow.setCanAssign(getCanAssignInitValue());
                /** org正在构建的权限集合所属组织， 顶层组织不可更改可分配权限 */
                boolean isTopOrg = org != null && org.isTop();
                siteRow.setHasPerm(false);
                siteRow.setId(0);
                siteRow.setName(MessageResolver.getMessage("tip.site.new"));
                if (isTopOrg || Boolean.TRUE.equals(ownerNewSiteOwner)) {
                        siteRow.setHasPerm(true);
                }
                if (!isTopOrg && Boolean.TRUE.equals(canAssignNewSiteOwner)) {
                        siteRow.setCanAssign(true);
                }
                return siteRow;
        }

        private CmsDataPermVo.MenuMap getMenuMap(CmsOrg org, CoreMenu menu, Set<CoreMenu> ownerMenus,
                        Set<CoreMenu> canAssignMenus) {
                CmsDataPermVo.MenuMap menuRow = new CmsDataPermVo.MenuMap();
                menuRow.setCanAssign(getCanAssignInitValue());
                menuRow.setHasPerm(false);
                menuRow.setMenu(menu);
                /** org正在构建的权限集合所属组织， 顶层组织不可更改可分配权限 */
                boolean isTopOrg = org != null && org.isTop();
                if (isTopOrg || CoreMenu.fetchIds(ownerMenus).contains(menu.getId())) {
                        menuRow.setHasPerm(true);
                }
                if (!isTopOrg && CoreMenu.fetchIds(canAssignMenus).contains(menu.getId())) {
                        menuRow.setCanAssign(true);
                }
                return menuRow;
        }

        private CoreMenuOwnerTree getNewMenuOwnerMap(CmsOrg org, Boolean owner, Boolean canAssign) {
                CoreMenuOwnerTree menuRow = new CoreMenuOwnerTree();
                // menuRow.setAssigned(false);
                menuRow.setCanAssign(getCanAssignInitValue());
                boolean isTopOrg = org != null && org.isTop();
                menuRow.setHasPerm(false);
                if (isTopOrg || Boolean.TRUE.equals(owner)) {
                        menuRow.setHasPerm(true);
                }
                if (!isTopOrg && Boolean.TRUE.equals(canAssign)) {
                        menuRow.setCanAssign(true);
                }
                menuRow.setId(0);
                menuRow.setName(MessageResolver.getMessage("tip.menu.new"));
                return menuRow;
        }

        /**
         * 获取可分配权限初始值，开放模式true
         * 
         * @Title: getCanAssignInitValue
         * @return: boolean
         */
        private boolean getCanAssignInitValue() {
                if (isDevelopMode()) {
                        return true;
                }
                return false;
        }

        /**
         * 是否开放模式
         * 
         * @Title: isDevelopMode
         * @return: boolean
         */
        private boolean isDevelopMode() {
                if (ServerModeEnum.dev.toString().equals(serverMode)) {
                        return true;
                }
                return false;
        }

        @Value("${spring.profiles.active}")
        private String serverMode;
        @Autowired
        private CmsOrgService orgService;
        @Autowired
        private CoreRoleService roleService;
        @Autowired
        private CoreUserService userService;
        @Autowired
        private CmsSiteService siteService;
        @Autowired
        private ChannelService channelService;
        @Autowired
        private CoreMenuService menuService;
        @Autowired
        private CoreUserService coreUserService;

}
