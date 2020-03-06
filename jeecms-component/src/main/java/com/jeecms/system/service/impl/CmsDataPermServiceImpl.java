package com.jeecms.system.service.impl;

import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreMenuService;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.component.listener.ChannelListener;
import com.jeecms.component.listener.MenuListener;
import com.jeecms.component.listener.SiteListener;
import com.jeecms.system.dao.CmsDataPermDao;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsDataPermCfg;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.*;
import com.jeecms.system.domain.dto.CmsDatePermDto.*;
import com.jeecms.system.domain.vo.CmsDataPermVo;
import com.jeecms.system.service.CmsDataPermCfgService;
import com.jeecms.system.service.CmsDataPermService;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 数据权限service实现类
 * 
 * @author: tom
 * @date: 2018年11月5日 下午2:04:49
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = { Exception.class })
public class CmsDataPermServiceImpl extends BaseServiceImpl<CmsDataPerm, CmsDataPermDao, Integer>
                implements CmsDataPermService, SiteListener, ChannelListener, MenuListener {

        Paginable maxPaginable = new PaginableRequest(0, Integer.MAX_VALUE);

        @Override
        public void updateByDto(CmsDatePermDto dto) throws GlobalException {
                Short dataType = dto.getDataType();
                CmsDataPermCfg cfg = null;
                List<CmsDataPerm> toRemoveDataPerms = new ArrayList<CmsDataPerm>();
                CmsOrg org = null;
                CoreRole role = null;
                CoreUser user = null;
                Integer siteId = null;
                if (dto != null && dto.getMoreDataIds() != null) {
                        siteId = dto.getMoreDataIds().getSiteId();
                }
                Set<CmsDataPerm> dataPerms = new HashSet<>();
                if (dto.getOrgId() != null) {
                        org = orgService.findById(dto.getOrgId());
                        org.validManagerAble();
                        cfg = org.getPermCfg();
                        /** 先删除对应数据权限 */
                        toRemoveDataPerms = findList(org.getId(), null, null, siteId, dto.getDataType(), null, null,
                                        maxPaginable);
                        dataPerms = org.getDataPerms();
                } else if (dto.getRoleId() != null) {
                        role = roleService.findById(dto.getRoleId());
                        role.validManagerAble();
                        cfg = role.getPermCfg();
                        toRemoveDataPerms = findList(null, dto.getRoleId(), null, siteId, dto.getDataType(), null, null,
                                        maxPaginable);
                        dataPerms = role.getDataPerms();
                } else if (dto.getUserId() != null) {
                        user = userService.findById(dto.getUserId());
                        user.validAssignAble();
                        cfg = user.getPermCfg();
                        toRemoveDataPerms = findList(null, null, dto.getUserId(), siteId, dto.getDataType(), null, null,
                                        maxPaginable);
                        dataPerms = user.getDataPerms();
                }
                physicalDeleteInBatch(toRemoveDataPerms);
                dataPerms.removeAll(toRemoveDataPerms);
                /**
                 * 站群权限
                 */
                if (CmsDataPerm.DATA_TYPE_SITE_OWNER.equals(dataType)) {
                        List<SiteMap> rows = dto.getSiteDatas();
                        Set<CmsSite> sites = new HashSet<CmsSite>();
                        for (SiteMap row : rows) {
                                if (row.getSiteId() != null) {
                                        if (row.getSiteId() != 0) {
                                                if (row.getSelected() != null && row.getSelected()) {
                                                        CmsSite s = siteService.findById(row.getSiteId());
                                                        sites.add(s);
                                                }
                                        } else {
                                                /** 新增站点配置 */
                                                if (cfg != null) {
                                                        if (row.getSelected() != null && row.getSelected()) {
                                                                cfg.setNewSiteOwner(true);
                                                        } else {
                                                                if (dto.getSiteDataOneSelect()) {
                                                                        cfg.setNewSiteOwner(false);
                                                                } else {
                                                                        cfg.setNewSiteOwner(null);
                                                                }
                                                        }
                                                        permCfgService.updateAll(cfg);
                                                } else {
                                                        if (org != null) {
                                                                permCfgService.saveByOrg(org, null);
                                                        } else if (role != null) {
                                                                permCfgService.saveByRole(role, null);
                                                        } else if (user != null) {
                                                                permCfgService.saveByUser(user, null);
                                                        }
                                                }
                                        }
                                }
                        }
                        if (dto.getOrgId() != null) {
                                org.getSites().clear();
                                org.setSites(sites);
                                orgService.update(org);
                        } else if (dto.getRoleId() != null) {
                                role.getSites().clear();
                                role.setSites(sites);
                                roleService.update(role);
                        } else if (dto.getUserId() != null) {
                                user.getSites().clear();
                                user.setSites(sites);
                                userService.update(user);
                        }
                } else if (CmsDataPerm.DATA_TYPE_SITE.equals(dataType)) {
                        /**
                         * 站点数据权限
                         */
                        List<SiteRow> rows = dto.getDataIds();
                        for (SiteRow row : rows) {
                                if (row.getSiteId() != null) {
                                        if (row.getSiteId() != 0) {
                                                for (MiniDataUnit rowOpe : row.getRowDatas()) {
                                                        CmsDataPerm data = findOne(dto.getOrgId(), dto.getRoleId(),
                                                                        dto.getUserId(), row.getSiteId(), dataType,
                                                                        rowOpe.getOperation(), row.getSiteId());
                                                        saveOneDataPerm(data, dto, dataType, row.getSiteId(),
                                                                        rowOpe.getOperation(), row.getSiteId(),
                                                                        rowOpe.getSelected());
                                                        dataPerms.add(data);
                                                }
                                        } else {
                                                /** 新增站点配置 */
                                                StringBuffer newSiteOpe = new StringBuffer();
                                                for (MiniDataUnit rowOpe : row.getRowDatas()) {
                                                        if (cfg != null) {
                                                                if (rowOpe.getSelected() != null
                                                                                && rowOpe.getSelected()) {
                                                                        newSiteOpe.append(rowOpe.getOperation())
                                                                                        .append(",");
                                                                }
                                                        }
                                                }
                                                if (cfg != null) {
                                                        cfg.setNewSiteOpe(newSiteOpe.toString());
                                                        permCfgService.updateAll(cfg);
                                                } else {
                                                        if (org != null) {
                                                                permCfgService.saveByOrg(org, null);
                                                        } else if (role != null) {
                                                                permCfgService.saveByRole(role, null);
                                                        } else if (user != null) {
                                                                permCfgService.saveByUser(user, null);
                                                        }
                                                }
                                        }
                                }
                        }
                } else if (CmsDataPerm.DATA_TYPE_MENU.equals(dataType)) {
                        List<MenuMap> rows = dto.getMenus();
                        /**
                         * 不参与权限分配的子菜单默认全部给与，一级菜单若是不参与分配 则全部给与
                         */
                        List<CoreMenu> topAutoAuthMenus = menuService.findByParams(0).stream()
                                        .filter(menu -> !menu.getIsAuth()).collect(Collectors.toList());
                        List<CoreMenu> autoAuthMenus = new ArrayList<CoreMenu>();
                        /** 用户所选择的菜单处理不参与权限分配的子菜单 */
                        List<CoreMenu> toAddAutoAuthMenus = new ArrayList<CoreMenu>();
                        toAddAutoAuthMenus.addAll(topAutoAuthMenus);
                        for (MenuMap row : rows) {
                                if (row.getMenuId() != 0) {
                                        if (row.getSelected() != null && row.getSelected()) {
                                                CoreMenu menu = menuService.findById(row.getMenuId());
                                                toAddAutoAuthMenus.add(menu);
                                        }
                                }
                        }
                        autoAuthMenus.addAll(CoreMenu.getAllChildAndSort(toAddAutoAuthMenus, true));
                        Set<MenuMap> menuMap = new HashSet<MenuMap>();
                        menuMap.addAll(rows);
                        for (CoreMenu menu : autoAuthMenus) {
                                menuMap.add(new MenuMap(menu.getId(), true));
                        }
                        List<CoreMenu> menus = new ArrayList<CoreMenu>();
                        for (MenuMap row : menuMap) {
                                if (row.getMenuId() != null) {
                                        if (row.getMenuId() != 0) {
                                                if (row.getSelected() != null && row.getSelected()) {
                                                        menus.add(menuService.findById(row.getMenuId()));
                                                }
                                        } else {
                                                /** 新增菜单配置 */
                                                if (cfg != null) {
                                                        if (row.getSelected() != null && row.getSelected()) {
                                                                cfg.setNewMenuOwner(true);
                                                        } else {
                                                                if (dto.getMenuOneSelect()) {
                                                                        cfg.setNewMenuOwner(false);
                                                                } else {
                                                                        cfg.setNewMenuOwner(null);
                                                                }
                                                        }
                                                        permCfgService.updateAll(cfg);
                                                } else {
                                                        if (org != null) {
                                                                permCfgService.saveByOrg(org, null);
                                                        } else if (role != null) {
                                                                permCfgService.saveByRole(role, null);
                                                        } else if (user != null) {
                                                                permCfgService.saveByUser(user, null);
                                                        }
                                                }
                                        }
                                }
                        }
                        if (dto.getOrgId() != null) {
                                org.getMenus().clear();
                                org.setMenus(menus);
                                orgService.update(org);
                        } else if (dto.getRoleId() != null) {
                                role.getMenus().clear();
                                role.setMenus(menus);
                                roleService.update(role);
                        } else if (dto.getUserId() != null) {
                                user.getMenus().clear();
                                user.setMenus(menus);
                                userService.update(user);
                        }
                } else {
                        /**
                         * 栏目、文档类数据权限
                         */
                        ChannelRow row = dto.getMoreDataIds();
                        /** 栏目的权限的新配置 多个站点是分开的 */
                        if (dto.getOrgId() != null) {
                                cfg = org.getPermCfgForChannel(row.getSiteId());
                        } else if (dto.getRoleId() != null) {
                                cfg = role.getPermCfgForChannel(row.getSiteId());
                        } else if (dto.getUserId() != null) {
                                cfg = user.getPermCfgForChannel(row.getSiteId());
                        }
                        /** 新增栏目配置 */
                        StringBuffer newSiteOpe = new StringBuffer();
                        CmsSite site = siteService.findById(row.getSiteId());
                        /** 循环栏目 */
                        for (ChannelUnit oneSite : row.getUnits()) {
                                if (oneSite.getKeyId() != 0) {
                                        /** 具体操作权限 */
                                        for (MiniDataUnit rowOpe : oneSite.getRowDatas()) {
                                                CmsDataPerm data = findOne(dto.getOrgId(), dto.getRoleId(),
                                                                dto.getUserId(), row.getSiteId(), dataType,
                                                                rowOpe.getOperation(), oneSite.getKeyId());
                                                saveOneDataPerm(data, dto, dataType, row.getSiteId(),
                                                                rowOpe.getOperation(), oneSite.getKeyId(),
                                                                rowOpe.getSelected());
                                                dataPerms.add(data);
                                        }
                                } else {
                                        /** 0为新增栏目配置 */
                                        for (MiniDataUnit rowOpe : oneSite.getRowDatas()) {
                                                if (rowOpe.getSelected() != null && rowOpe.getSelected()) {
                                                        newSiteOpe.append(rowOpe.getOperation()).append(",");
                                                }
                                        }
                                }
                        }
                        if (cfg != null) {
                                if (CmsDataPerm.DATA_TYPE_CHANNEL.equals(dataType)) {
                                        cfg.setNewChannelOpe(newSiteOpe.toString());
                                } else if (CmsDataPerm.DATA_TYPE_CONTENT.equals(dataType)) {
                                        cfg.setNewChannelOpeContent(newSiteOpe.toString());
                                }
                                permCfgService.updateAll(cfg);
                        } else {
                                if (org != null) {
                                        permCfgService.saveByOrg(org, site);
                                } else if (role != null) {
                                        permCfgService.saveByRole(role, site);
                                } else if (user != null) {
                                        permCfgService.saveByUser(user, site);
                                }
                        }
                }
                if (dto.getOrgId() != null) {
                        /** 需要验证更新后的权限 */
                        org.validManagerAble();
                        /**主动维护集合缓存*/
                        org.setDataPerms(dataPerms);
                        /** 从新分配权限，需要清空权限缓存 */
                        org.clearPermCache();
                } else if (dto.getRoleId() != null) {
                        /** 需要验证更新后的权限 */
                        role.validManagerAble();
                        /**主动维护集合缓存*/
                        role.setDataPerms(dataPerms);
                        /** 从新分配权限，需要清空权限缓存 */
                        role.clearPermCache();
                } else if (dto.getUserId() != null) {
                        /** 需要验证更新后的权限 */
                        user.validManagerAble();
                        /**主动维护集合缓存*/
                        user.setDataPerms(dataPerms);
                        /** 从新分配权限，需要清空权限缓存 */
                        user.clearPermCache();
                }
        }

        @Override
        public void updateDataPermByOrg(OrgPermDto dto) throws GlobalException {
                if (dto.getDataId() != null) {
                        HashSet<OrgPermUnitDto> units = dto.getPerms();
                        Set<Integer> orgIds = new HashSet<Integer>();
                        Set<Integer> roleIds = new HashSet<Integer>();
                        Map<Integer, Set<MiniDataUnit>> rolePermMap = new HashMap<Integer, Set<MiniDataUnit>>(16);
                        Map<Integer, Set<MiniDataUnit>> orgPermMap = new HashMap<Integer, Set<MiniDataUnit>>(16);
                        for (OrgPermUnitDto unit : units) {
                                if (unit.getOrgId() != null&& unit.getOps()!=null) {
                                        orgIds.add(unit.getOrgId());
                                        orgPermMap.put(unit.getOrgId(), unit.getOps());
                                } else if (unit.getRoleId() != null&& unit.getOps()!=null){
                                        roleIds.add(unit.getRoleId());
                                        rolePermMap.put(unit.getRoleId(), unit.getOps());
                                }
                        }
                        Channel channel = channelService.findById(dto.getDataId());
                        HashSet<CmsDataPerm> toRemovePerms = new HashSet<CmsDataPerm>();
                        HashSet<CmsDataPerm> toAddPerms = new HashSet<CmsDataPerm>();
                        CoreUser currUser = SystemContextUtils.getCoreUser();
                        for (Integer orgId : orgIds) {
                                CmsOrg org = orgService.findById(orgId);
                                /** 非顶层组织和非当前登录用户所属组织才可变更权限 */
                                if (!org.isTop() && !currUser.getOrgId().equals(orgId)) {
                                        CopyOnWriteArraySet<CmsDataPerm> orgOwnerByChannelPerms = org
                                                        .getOwnerDataPermsByType(dto.getDataType(), channel.getSiteId(),
                                                                        null, dto.getDataId(), false);
                                        Set<Short> orgOwnerByChannelOpes = CmsDataPerm
                                                        .getOperators(orgOwnerByChannelPerms);
                                        Set<Short> toRemoveOpes = new HashSet<Short>();
                                        Set<Short> toAddOpes = new HashSet<Short>();
                                        boolean needUpdateOrgPerm = false;
                                        /** 前端需要传递所有操作选项 */
                                        if(orgPermMap!=null&&orgPermMap.size()>0){
                                                for (MiniDataUnit mu : orgPermMap.get(orgId)) {
                                                        /** 未选中的则需要移除 */
                                                        if (mu.getSelected() != null && !mu.getSelected()) {
                                                                /** 未选中的被现有权限包含，则需要添加到待删除中 */
                                                                if (orgOwnerByChannelOpes.contains(mu.getOperation())) {
                                                                        toRemoveOpes.add(mu.getOperation());
                                                                        needUpdateOrgPerm = true;
                                                                }
                                                        } else {
                                                                /** 选中的没有被现有权限包含，则需要添加到待添加中 */
                                                                if (!orgOwnerByChannelOpes.contains(mu.getOperation())) {
                                                                        toAddOpes.add(mu.getOperation());
                                                                        needUpdateOrgPerm = true;
                                                                }
                                                        }
                                                }     
                                        }
                                        /** 则检查是否需要 待更新 */
                                        if (needUpdateOrgPerm) {
                                                /** 判断是否单独分配了权限,单独分配了则直接从单独分配的权限去除或者新增，否则需要从继承的权限中去除或者新增 */
                                                CopyOnWriteArraySet<CmsDataPerm> orgOriginPerms;
                                                if (org.getHasAssignDataPermsByType(dto.getDataType())) {
                                                        orgOriginPerms = org.getDataPermsByType(dto.getDataType(),
                                                                        channel.getSiteId(), null, null);
                                                } else {
                                                        orgOriginPerms = org.getOwnerDataPermsByType(dto.getDataType(),
                                                                        channel.getSiteId(), null, null, false);
                                                }
                                                for (CmsDataPerm p : orgOriginPerms) {
                                                        if (toRemoveOpes.contains(p.getOperation())) {
                                                                toRemovePerms.add(p);
                                                        }
                                                }
                                                for (Short op : toAddOpes) {
                                                        CmsDataPerm dp = new CmsDataPerm(dto.getDataId(),
                                                                        dto.getDataType(), op, orgId,
                                                                        channel.getSiteId(), channel.getSite(), org);
                                                        toAddPerms.add(dp);
                                                }
                                                /** 从新分配权限，需要清空权限缓存 */
                                                org.clearPermCache();
                                        }
                                }
                        }
                        for (Integer roleId : roleIds) {
                                /** 非当前登录用户所在角色 */
                                if (!currUser.getRoleIds().contains(roleId)) {
                                        CoreRole role = roleService.findById(roleId);
                                        CopyOnWriteArraySet<CmsDataPerm> roleOwnerByChannelPerms = role
                                                        .getOwnerDataPermsByType(dto.getDataType(), channel.getSiteId(),
                                                                        null, dto.getDataId());
                                        Set<Short> roleOwnerByChannelOpes = CmsDataPerm
                                                        .getOperators(roleOwnerByChannelPerms);
                                        Set<Short> toRemoveOpes = new HashSet<Short>();
                                        Set<Short> toAddOpes = new HashSet<Short>();
                                        boolean needUpdateOrgPerm = false;
                                        /** 前端需要传递所有操作选项 */
                                        if(rolePermMap!=null&&rolePermMap.size()>0){
                                                for (MiniDataUnit mu : rolePermMap.get(roleId)) {
                                                        /** 未选中的则需要移除 */
                                                        if (mu.getSelected() != null && !mu.getSelected()) {
                                                                /** 未选中的被现有权限包含，则需要添加到待删除中 */
                                                                if (roleOwnerByChannelOpes.contains(mu.getOperation())) {
                                                                        toRemoveOpes.add(mu.getOperation());
                                                                        needUpdateOrgPerm = true;
                                                                }
                                                        } else {
                                                                /** 选中的没有被现有权限包含，则需要添加到待添加中 */
                                                                if (!roleOwnerByChannelOpes.contains(mu.getOperation())) {
                                                                        toAddOpes.add(mu.getOperation());
                                                                        needUpdateOrgPerm = true;
                                                                }
                                                        }
                                                }    
                                        }
                                        /** 则检查是否需要 待更新 */
                                        if (needUpdateOrgPerm) {
                                                /** 判断是否单独分配了权限,单独分配了则直接从单独分配的权限去除或者新增，否则需要从继承的权限中去除或者新增 */
                                                CopyOnWriteArraySet<CmsDataPerm> roleOriginPerms;
                                                if (role.getHasAssignDataPermsByType(dto.getDataType())) {
                                                        roleOriginPerms = role.getDataPermsByType(dto.getDataType(),
                                                                        channel.getSiteId(), null, null);
                                                } else {
                                                        roleOriginPerms = role.getOwnerDataPermsByType(
                                                                        dto.getDataType(), channel.getSiteId(), null,
                                                                        null);
                                                }
                                                for (CmsDataPerm p : roleOriginPerms) {
                                                        if (toRemoveOpes.contains(p.getOperation())) {
                                                                toRemovePerms.add(p);
                                                        }
                                                }
                                                for (Short op : toAddOpes) {
                                                        CmsDataPerm dp = new CmsDataPerm(dto.getDataId(),
                                                                        dto.getDataType(), op, roleId,
                                                                        channel.getSiteId(), channel.getSite(), role);
                                                        toAddPerms.add(dp);
                                                }
                                                /** 从新分配权限，需要清空权限缓存 */
                                                role.clearPermCache();
                                        }
                                }
                        }
                        saveAll(toAddPerms);
                        physicalDeleteInBatch(toRemovePerms);
                }
        }

        @Override
        public void updateDataPermByUser(UserPermDto dto) throws GlobalException {
                if (dto.getDataId() != null) {
                        HashSet<UserPermUnitDto> units = dto.getPerms();
                        Set<Integer> userIds = new HashSet<Integer>();
                        Map<Integer, Set<MiniDataUnit>> userPermMap = new HashMap<Integer, Set<MiniDataUnit>>(16);
                        for (UserPermUnitDto unit : units) {
                                userIds.add(unit.getUserId());
                                userPermMap.put(unit.getUserId(), unit.getOps());
                        }
                        Channel channel = channelService.findById(dto.getDataId());
                        HashSet<CmsDataPerm> toRemovePerms = new HashSet<CmsDataPerm>();
                        HashSet<CmsDataPerm> toAddPerms = new HashSet<CmsDataPerm>();
                        Integer currUserId = SystemContextUtils.getCoreUser().getId();
                        /** 不可变更当前登录用户的权限 */
                        for (Integer userId : userIds) {
                                if (!userId.equals(currUserId)) {
                                        CoreUser user = userService.findById(userId);
                                        CopyOnWriteArraySet<CmsDataPerm> userOwnerByChannelPerms = user
                                                        .getOwnerDataPermsByType(dto.getDataType(), channel.getSiteId(),
                                                                        null, dto.getDataId());
                                        Set<Short> userOwnerByChannelOpes = CmsDataPerm
                                                        .getOperators(userOwnerByChannelPerms);
                                        Set<Short> toRemoveOpes = new HashSet<Short>();
                                        Set<Short> toAddOpes = new HashSet<Short>();
                                        boolean needUpdateOrgPerm = false;
                                        /** 前端需要传递所有操作选项 */
                                        for (MiniDataUnit mu : userPermMap.get(userId)) {
                                                /** 未选中的则需要移除 */
                                                if (mu.getSelected() != null && !mu.getSelected()) {
                                                        /** 未选中的被现有权限包含，则需要添加到待删除中 */
                                                        if (userOwnerByChannelOpes.contains(mu.getOperation())) {
                                                                toRemoveOpes.add(mu.getOperation());
                                                                needUpdateOrgPerm = true;
                                                        }
                                                } else {
                                                        /** 选中的没有被现有权限包含，则需要添加到待添加中 */
                                                        if (!userOwnerByChannelOpes.contains(mu.getOperation())) {
                                                                toAddOpes.add(mu.getOperation());
                                                                needUpdateOrgPerm = true;
                                                        }
                                                }
                                        }
                                        /** 则检查是否需要 待更新 */
                                        if (needUpdateOrgPerm) {
                                                /** 判断是否单独分配了权限,单独分配了则直接从单独分配的权限去除或者新增，否则需要从继承的权限中去除或者新增 */
                                                CopyOnWriteArraySet<CmsDataPerm> orgOriginPerms;
                                                if (user.getHasAssignDataPermsByType(dto.getDataType())) {
                                                        orgOriginPerms = user.getDataPermsByType(dto.getDataType(),
                                                                        channel.getSiteId(), null, null);
                                                } else {
                                                        orgOriginPerms = user.getOwnerDataPermsByType(dto.getDataType(),
                                                                        channel.getSiteId(), null, null);
                                                }
                                                for (CmsDataPerm p : orgOriginPerms) {
                                                        if (toRemoveOpes.contains(p.getOperation())) {
                                                                toRemovePerms.add(p);
                                                        }
                                                }
                                                for (Short op : toAddOpes) {
                                                        CmsDataPerm dp = new CmsDataPerm(dto.getDataId(),
                                                                        dto.getDataType(), op, userId,
                                                                        channel.getSiteId(), channel.getSite(), user);
                                                        toAddPerms.add(dp);
                                                }
                                                /** 从新分配权限，需要清空权限缓存 */
                                                user.clearPermCache();
                                        }
                                }
                        }
                        saveAll(toAddPerms);
                        physicalDeleteInBatch(toRemovePerms);
                }
        }

        @Override
        public void updateSiteOwner(OwnerSitePermDto dto) throws GlobalException {
                Integer siteId = dto.getSiteId();
                HashSet<OwnerSitePermUnitDto> perms = dto.getPerms();
                CmsSite site = siteService.findById(siteId);
                Set<Integer> toAddOwnerSiteOrgIds = new HashSet<Integer>();
                Set<Integer> toRemoveOwnerSiteOrgIds = new HashSet<Integer>();
                HashSet<CmsOrg> toAddOwnerSiteOrgs = new HashSet<CmsOrg>();
                HashSet<CmsOrg> toRemoveOwnerSiteOrgs = new HashSet<CmsOrg>();
                Set<Integer> toAddOwnerSiteRoleIds = new HashSet<Integer>();
                Set<Integer> toRemoveOwnerSiteRoleIds = new HashSet<Integer>();
                Set<Integer> toAddOwnerSiteUserIds = new HashSet<Integer>();
                Set<Integer> toRemoveOwnerSiteUserIds = new HashSet<Integer>();
                CoreUser currUser = SystemContextUtils.getCoreUser();
                for (OwnerSitePermUnitDto p : perms) {
                        if (p.getOrgId() != null) {
                                if (p.getSelected()) {
                                        toAddOwnerSiteOrgIds.add(p.getOrgId());
                                } else {
                                        toRemoveOwnerSiteOrgIds.add(p.getOrgId());
                                }
                        }
                        if (p.getRoleId() != null) {
                                if (p.getSelected()) {
                                        toAddOwnerSiteRoleIds.add(p.getRoleId());
                                } else {
                                        toRemoveOwnerSiteRoleIds.add(p.getRoleId());
                                }
                        }
                        if (p.getUserId() != null) {
                                if (p.getSelected()) {
                                        toAddOwnerSiteUserIds.add(p.getUserId());
                                } else {
                                        toRemoveOwnerSiteUserIds.add(p.getUserId());
                                }
                        }

                }
                toAddOwnerSiteOrgs.addAll(orgService.findAllById(toAddOwnerSiteOrgIds));
                toRemoveOwnerSiteOrgs.addAll(orgService.findAllById(toRemoveOwnerSiteOrgIds));
                /** 判断是否单独分配了权限,单独分配了则直接从单独分配的权限去除或者新增，否则需要从继承的权限中去除或者新增 */
                HashSet<CmsSite> orgOriginPerms;
                for (CmsOrg org : toRemoveOwnerSiteOrgs) {
                        /** 非顶层组织且非当前用户所在组织 */
                        if (!org.isTop() && !org.getId().equals(currUser.getOrgId())) {
                                if (org.getHasAssignOwnerSite()) {
                                        org.getSites().remove(site);
                                } else {
                                        orgOriginPerms = new HashSet<>();
                                        orgOriginPerms.addAll(org.getCloneOwnerSites());
                                        orgOriginPerms.remove(site);
                                        org.setSites(orgOriginPerms);
                                }
                                /** 从新分配权限，需要清空权限缓存 */
                                org.clearPermCache();
                        }
                }
                for (CmsOrg org : toAddOwnerSiteOrgs) {
                        /** 非顶层组织且非当前用户所在组织 */
                        if (!org.isTop() && !org.getId().equals(currUser.getOrgId())) {
                                if (org.getHasAssignOwnerSite()) {
                                        org.getSites().add(site);
                                } else {
                                        /** 非顶层组织才可变更 */
                                        orgOriginPerms = new HashSet<>();
                                        orgOriginPerms.addAll(org.getCloneOwnerSites());
                                        orgOriginPerms.add(site);
                                        org.setSites(orgOriginPerms);
                                }
                                /** 从新分配权限，需要清空权限缓存 */
                                org.clearPermCache();
                        }
                }
                /** 处理角色权限 */
                HashSet<CoreRole> toAddOwnerSiteRoles = new HashSet<CoreRole>();
                HashSet<CoreRole> toRemoveOwnerSiteRoles = new HashSet<CoreRole>();
                toAddOwnerSiteRoles.addAll(roleService.findAllById(toAddOwnerSiteRoleIds));
                toRemoveOwnerSiteRoles.addAll(roleService.findAllById(toRemoveOwnerSiteRoleIds));
                /** 判断是否单独分配了权限,单独分配了则直接从单独分配的权限去除或者新增，否则需要从继承的权限中去除或者新增 */
                for (CoreRole role : toRemoveOwnerSiteRoles) {
                        if (!currUser.getRoleIds().contains(role.getId())) {
                                if (role.getHasAssignOwnerSite()) {
                                        role.getSites().remove(site);
                                } else {
                                        orgOriginPerms = new HashSet<>();
                                        orgOriginPerms.addAll(role.getCloneOwnerSites());
                                        orgOriginPerms.remove(site);
                                        role.setSites(orgOriginPerms);
                                }
                                /** 从新分配权限，需要清空权限缓存 */
                                role.clearPermCache();
                        }
                }
                for (CoreRole role : toAddOwnerSiteRoles) {
                        if (!currUser.getRoleIds().contains(role.getId())) {
                                if (role.getHasAssignOwnerSite()) {
                                        role.getSites().add(site);
                                } else {
                                        orgOriginPerms = new HashSet<>();
                                        orgOriginPerms.addAll(role.getCloneOwnerSites());
                                        orgOriginPerms.add(site);
                                        role.setSites(orgOriginPerms);
                                }
                                /** 从新分配权限，需要清空权限缓存 */
                                role.clearPermCache();
                        }
                }
                /** 处理用户权限 */
                HashSet<CoreUser> toAddOwnerSiteUsers = new HashSet<CoreUser>();
                HashSet<CoreUser> toRemoveOwnerSiteUsers = new HashSet<CoreUser>();
                toAddOwnerSiteUsers.addAll(userService.findAllById(toAddOwnerSiteUserIds));
                toRemoveOwnerSiteUsers.addAll(userService.findAllById(toRemoveOwnerSiteUserIds));
                /** 需要从继承的权限中去除或者新增 */
                Set<CmsSite> userOwnerSites;
                for (CoreUser user : toAddOwnerSiteUsers) {
                        if (!user.getId().equals(currUser.getId())) {
                                userOwnerSites = new HashSet<CmsSite>();
                                userOwnerSites.addAll(user.getCloneOwnerSites());
                                userOwnerSites.add(site);
                                user.getSites().clear();
                                user.setSites(userOwnerSites);
                                /** 从新分配权限，需要清空权限缓存 */
                                user.clearPermCache();
                        }
                }

                for (CoreUser user : toRemoveOwnerSiteUsers) {
                        if (!user.getId().equals(currUser.getId())) {
                                userOwnerSites = new HashSet<CmsSite>();
                                userOwnerSites.addAll(user.getCloneOwnerSites());
                                userOwnerSites.remove(site);
                                user.getSites().clear();
                                user.setSites(userOwnerSites);
                                /** 从新分配权限，需要清空权限缓存 */
                                user.clearPermCache();
                        }
                }
        }

        @Override
        public List<CmsDataPerm> findList(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType,
                        Short operation, Integer dataId, Paginable paginable) {
                List<CmsDataPerm> list = dao.findList(orgId, roleId, userId, siteId, dataType, operation, dataId,
                                paginable);
                return list;
        }

        @Override
        public CmsDataPerm findOne(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType,
                        Short operation, Integer dataId) {
                List<CmsDataPerm> list = findList(orgId, roleId, userId, siteId, dataType, operation, dataId,
                                new PaginableRequest(0, 1));
                if (list != null && list.size() > 0) {
                        return list.get(0);
                }
                return null;
        }

        private CmsDataPerm saveOneDataPerm(CmsDataPerm data, CmsDatePermDto dto, Short dataType, Integer siteId,
                        Short operate, Integer dataId, Boolean selected) throws GlobalException {
                if (data == null) {
                        if (selected != null && selected) {
                                data = getDataPerm(dto, dataType, siteId, operate, dataId);
                                super.save(data);
                        }
                } else {
                        super.update(data);
                }
                /*
                 * if (selected != null && selected) { data.setHasPerm(true); } else { data.setHasPerm(false); }
                 */
                return data;
        }

        private CmsDataPerm getDataPerm(CmsDatePermDto dto, Short dataType, Integer siteId, Short operate,
                        Integer dataId) throws GlobalException {
                CmsDataPerm data = new CmsDataPerm();
                data.setDataType(dataType);
                data.setOrgId(dto.getOrgId());
                data.setRoleId(dto.getRoleId());
                data.setUserId(dto.getUserId());
                data.setSiteId(siteId);
                data.setDataId(dataId);
                data.setOperation(operate);
                if (dto.getOrgId() != null) {
                        data.setOrg(orgService.findById(dto.getOrgId()));
                }
                if (dto.getRoleId() != null) {
                        data.setRole(roleService.findById(dto.getRoleId()));
                }
                if (dto.getUserId() != null) {
                        data.setUser(userService.findById(dto.getUserId()));
                }
                if (siteId != null) {
                        data.setSite(siteService.findById(siteId));
                }
                return data;
        }

        /**
         * 分配新栏目的权限
         * 
         * @Title: afterChannelSave
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        @Override
        public void afterChannelSave(Channel c) throws GlobalException {
                List<CoreUser> users = userService.findList(true, null, null, null, true, CoreUser.AUDIT_USER_STATUS_PASS, null, null, null, null,
                                maxPaginable);
                for (CoreUser user : users) {
                        updateUserChannel(user, c);
                }
                List<CoreRole> roles = roleService.findAll(true);
                for (CoreRole role : roles) {
                        updateRoleChannel(role, c);
                }
                List<CmsOrg> orgs = orgService.findAll(true);
                for (CmsOrg org : orgs) {
                        updateOrgChannel(org, c);
                }
                /** 主动刷新权限缓存 */
                userService.clearAllUserCache();
                orgService.clearAllOrgCache();
                roleService.clearAllRoleCache();
        }

        @Override
        public void beforeChannelDelete(Integer[] ids) {
                for (Integer id : ids) {
                        deleteByChannelId(id);
                }
        }

        @Override
        public void afterChannelRecycle(List<Channel> channels) throws GlobalException {
                /** 主动刷新权限缓存 */
                userService.clearAllUserCache();
                orgService.clearAllOrgCache();
                roleService.clearAllRoleCache();
        }

        @Override
        public void afterChannelChange(Channel c) throws GlobalException {
                /** 主动刷新权限缓存 */
                userService.clearAllUserCache();
                orgService.clearAllOrgCache();
                roleService.clearAllRoleCache();
        }

        /**
         * 新建菜单调用更新用户、角色、组织的菜单权限
         * 
         * @Title: afterMenuSave
         * @param menu
         *                CoreMenu
         * @throws GlobalException
         *                 GlobalException
         */
        @Override
        public void afterMenuSave(CoreMenu menu) throws GlobalException {
                List<CoreUser> users = userService.findList(true, null, null, null, true, null, null, null, null, null,
                                maxPaginable);
                /** 只更新用户栏目数据会导致菜单不会保存入库 */
                for (CoreUser user : users) {
                        /** 用户是否有新增菜单权限 */
                        if (user.getHasAssignOwnerMenu() && user.getOwnerNewMenuOwner()) {
                                user.getMenus().add(menu);
                        }
                }
                List<CoreRole> roles = roleService.findAll(true);
                for (CoreRole role : roles) {
                        if (role.getHasAssignOwnerMenu() && role.getOwnerNewMenuOwner()) {
                                role.getMenus().add(menu);
                        }
                }
                List<CmsOrg> orgs = orgService.findAll(true);
                for (CmsOrg org : orgs) {
                        if (org.getHasAssignOwnerMenu() && org.getOwnerNewMenuOwner()) {
                                org.getMenus().add(menu);
                        }
                }
                /** 主动刷新权限缓存 */
                userService.clearAllUserCache();
                orgService.clearAllOrgCache();
                roleService.clearAllRoleCache();
        }

        /**
         * 分配新站点权限给用户、角色、组织
         * 
         * @Title: afterSiteSave
         * @param site
         *                站点
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        @Override
        public void afterSiteSave(CmsSite site) throws GlobalException {
                List<CoreUser> users = userService.findList(true, null, null, null, true,
                                CoreUser.AUDIT_USER_STATUS_PASS, null, null, null, null, maxPaginable);
                for (CoreUser user : users) {
                        updateUserSite(user, site);
                }
                List<CoreRole> roles = roleService.findAll(true);
                for (CoreRole role : roles) {
                        updateRoleSite(role, site);
                }
                List<CmsOrg> orgs = orgService.findAll(true);
                for (CmsOrg org : orgs) {
                        updateOrgSite(org, site);
                }
                /** 主动刷新权限缓存 */
                userService.clearAllUserCache();
                orgService.clearAllOrgCache();
                roleService.clearAllRoleCache();
        }

        @Override
        public void beforeSitePhysicDelete(Integer[] ids) {
                for (Integer id : ids) {
                        deleteBySiteId(id);
                }
        }

        private void updateUserSite(CoreUser user, CmsSite site) throws GlobalException {
                /** 用户是否有新增站点站群权限 */
                if (user.getHasAssignOwnerSite() && user.getOwnerNewSiteOwner()) {
                        /** 给用户分配该新建站点的站群权限 */
                        Set<CmsSite> sites = user.getOwnerSites();
                        sites.add(site);
                        user.setSites(sites);
                        userService.flush();
                }
                if (user.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_SITE)) {
                        if (StringUtils.isNoneBlank(user.getNewSiteOpe())) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo
                                                .initUserDataPermsForSite(user.getOwnerNewSiteOperators(), user, site);
                                initPerms = saveAll(initPerms);
                                user.getDataPerms().addAll(initPerms);
                                userService.flush();
                        }
                }
        }

        private void updateRoleSite(CoreRole role, CmsSite site) throws GlobalException {
                if (role.getHasAssignOwnerSite() && role.getOwnerNewSiteOwner()) {
                        Set<CmsSite> sites = role.getOwnerSites();
                        sites.add(site);
                        role.setSites(sites);
                        roleService.flush();
                }
                if (role.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_SITE)) {
                        if (StringUtils.isNoneBlank(role.getNewSiteOpe())) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo
                                                .initRoleDataPermsForSite(role.getOwnerNewSiteOperators(), role, site);
                                initPerms = saveAll(initPerms);
                                role.getDataPerms().addAll(initPerms);
                                roleService.flush();
                        }
                }
        }

        private void updateOrgSite(CmsOrg org, CmsSite site) throws GlobalException {
                /** 是否单独分配了站群权限，且有新建站点的站群权限 */
                if (org.getHasAssignOwnerSite() && org.getOwnerNewSiteOwner()) {
                        Set<CmsSite> sites = org.getOwnerSites();
                        sites.add(site);
                        org.setSites(sites);
                        orgService.flush();
                }
                /** 是否单独分配了站点数据权限，且有新建站点的站点操作数据权限 */
                if (org.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_SITE)) {
                        if (StringUtils.isNoneBlank(org.getNewSiteOpe())) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo
                                                .initOrgDataPermsForSite(org.getOwnerNewSiteOperators(), org, site);
                                initPerms = saveAll(initPerms);
                                org.getDataPerms().addAll(initPerms);
                                orgService.flush();
                        }
                }
        }

        private void updateUserChannel(CoreUser user, Channel channel) throws GlobalException {
                /** 栏目类数据权限 */
                /** 是否单独分配了栏目类权限，且有新增栏目类权限 */
                if (user.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL)) {
                        if (StringUtils.isNoneBlank(user.getNewChannelOpe(channel.getSiteId()))) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo.initUserDataPermsForChannelByChannel(
                                                user.getOwnerNewChannelOperators(channel.getSiteId()), user, channel);
                                initPerms = saveAll(initPerms);
                                user.getDataPerms().addAll(initPerms);
                                userService.flush();
                        }
                }

                /** 文档类数据权限 */
                /** 是否单独分配了文档类权限，且有新增文档类权限 */
                if (user.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT)) {
                        if (StringUtils.isNoneBlank(user.getNewChannelOpeContent(channel.getSiteId()))) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo.initUserDataPermsForContentByChannel(
                                                user.getOwnerNewChannelContentOperators(channel.getSiteId()), user,
                                                channel);
                                initPerms = saveAll(initPerms);
                                user.getDataPerms().addAll(initPerms);
                                userService.flush();
                        }
                }
        }

        private void updateRoleChannel(CoreRole role, Channel channel) throws GlobalException {
                /** 栏目类数据权限 */
                if (role.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL)) {
                        if (StringUtils.isNoneBlank(role.getNewChannelOpe(channel.getSiteId()))) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo.initRoleDataPermsForChannelByChannel(
                                                role.getOwnerNewChannelOperators(channel.getSiteId()), role, channel);
                                initPerms = saveAll(initPerms);
                                role.getDataPerms().addAll(initPerms);
                                roleService.flush();
                        }
                }

                /** 文档类数据权限 */
                if (role.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT)) {
                        if (StringUtils.isNoneBlank(role.getNewChannelOpeContent(channel.getSiteId()))) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo.initRoleDataPermsForContentByChannel(
                                                role.getOwnerNewChannelContentOperators(channel.getSiteId()), role,
                                                channel);
                                initPerms = saveAll(initPerms);
                                role.getDataPerms().addAll(initPerms);
                                roleService.flush();
                        }
                }
        }

        private void updateOrgChannel(CmsOrg org, Channel channel) throws GlobalException {
                /** 栏目类数据权限 */
                if (org.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL)) {
                        if (StringUtils.isNoneBlank(org.getNewChannelOpe(channel.getSiteId()))) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo.initOrgDataPermsForChannelByChannel(
                                                org.getOwnerNewChannelOperators(channel.getSiteId()), org, channel);
                                initPerms = saveAll(initPerms);
                                org.getDataPerms().addAll(initPerms);
                                orgService.flush();
                        }
                }
                /** 文档类数据权限 */
                if (org.getHasAssignDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT)) {
                        if (StringUtils.isNoneBlank(org.getNewChannelOpeContent(channel.getSiteId()))) {
                                List<CmsDataPerm> initPerms = CmsDataPermVo.initOrgDataPermsForContentByChannel(
                                                org.getOwnerNewChannelContentOperators(channel.getSiteId()), org,
                                                channel);
                                initPerms = saveAll(initPerms);
                                org.getDataPerms().addAll(initPerms);
                                orgService.flush();
                        }
                }
        }

        public void deleteBySiteId(Integer siteId) {
                dao.deleteBySiteId(siteId);
                permCfgService.deleteBySiteId(siteId);
        }

        public void deleteByChannelId(Integer channelId) {
                dao.deleteByChannelId(channelId);
        }

        @Autowired
        private CmsOrgService orgService;
        @Autowired
        private CoreRoleService roleService;
        @Autowired
        private CoreUserService userService;
        @Autowired
        private CmsSiteService siteService;
        @Autowired
        private CoreMenuService menuService;
        @Autowired
        private CmsDataPermCfgService permCfgService;
        @Autowired
        private ChannelService channelService;

}
