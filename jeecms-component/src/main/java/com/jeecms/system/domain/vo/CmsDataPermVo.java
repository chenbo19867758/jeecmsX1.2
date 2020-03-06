/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.util.ChannelAgent;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限Vo
 *
 * @author: tom
 * @date: 2019年3月5日 下午4:25:35
 */
public class CmsDataPermVo implements Serializable {
        private static final long serialVersionUID = -8949502241253652368L;

        /** 站群权限数据 */
        List<CmsSiteOwnerTree> sites;
        /** 菜单数据 */
        List<CoreMenuOwnerTree> menus;
        /** 二级结构 站点数据权限 */
        List<SiteDataPermTree> dataIds;
        /** 三级结构 栏目数据权限 */
        ChannelRow moreDataIds;

        /**
         * 权限体（组织、角色、用户）是否单独分配权限
         */
        Boolean assigned;
        /**
         * 是否可管理
         */
        Boolean managerAble;

        public List<CmsSiteOwnerTree> getSites() {
                return sites;
        }

        public void setSites(List<CmsSiteOwnerTree> sites) {
                this.sites = sites;
        }

        /**
         * 指定站点的数据权限 所选择的站点操作类型ID
         *
         * @Title: getDataIds
         * @return: SiteRow[]
         */
        public List<SiteDataPermTree> getDataIds() {
                return dataIds;
        }

        /**
         * 栏目、内容三层结构 对象数组
         *
         * @Title: getMoreDataIds
         * @return ChannelOrgRow[]
         */
        public ChannelRow getMoreDataIds() {
                return moreDataIds;
        }

        public void setMoreDataIds(ChannelRow moreDataIds) {
                this.moreDataIds = moreDataIds;
        }

        public void setDataIds(List<SiteDataPermTree> dataIds) {
                this.dataIds = dataIds;
        }

        /**
         * 菜单权限
         *
         * @Title: getMenus
         * @return List
         */
        public List<CoreMenuOwnerTree> getMenus() {
                return menus;
        }

        public void setMenus(List<CoreMenuOwnerTree> menus) {
                this.menus = menus;
        }

        public Boolean getAssigned() {
                return assigned;
        }

        public void setAssigned(Boolean assigned) {
                this.assigned = assigned;
        }

        public Boolean getManagerAble() {
                return managerAble;
        }

        public void setManagerAble(Boolean managerAble) {
                this.managerAble = managerAble;
        }

        /**
         * 站点数据结构
         *
         * @author: tom
         * @date: 2019年3月5日 下午5:02:29
         */
        public static class SiteRow {
                CmsSite site;
                List<MiniDataUnit> rowDatas;

                public Integer getSiteId() {
                        if (site != null) {
                                return site.getId();
                        }
                        return null;
                }

                /**
                 * 创建初始化站点数据权限的一行 依 operation值顺序添加的集合
                 *
                 * @Title: initRowDatasForSite
                 */
                public void initRowDatasForSite(boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initRowDatasForSite(selected));
                }

                /**
                 * 创建初始化栏目数据权限的一行
                 *
                 * @Title: initRowDatasForChannel
                 */
                public void initRowDatasForChannel(Short[] opes, boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initRowDatasForChannel(opes, selected));
                }

                /**
                 * 创建初始化内容数据权限的一行
                 *
                 * @Title: initRowDatasForContent
                 */
                public void initRowDatasForContent(Short[] opes, boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initOrgRowDatasForContent(opes, selected));
                }

                /**
                 * 获取操作选项
                 *
                 * @Title: getRowOperates
                 * @return List
                 */
                @JSONField(serialize = false)
                public List<Short> getRowOperates() {
                        List<Short> operates = new ArrayList<Short>();
                        for (MiniDataUnit u : getRowDatas()) {
                                operates.add(u.getOperation());
                        }
                        return operates;
                }

                public List<MiniDataUnit> getRowDatas() {
                        return rowDatas;
                }

                public void setRowDatas(List<MiniDataUnit> rowDatas) {
                        this.rowDatas = rowDatas;
                }

                public CmsSite getSite() {
                        return site;
                }

                public void setSite(CmsSite site) {
                        this.site = site;
                }

        }

        public static class SiteMap {
                // Boolean assigned;
                Boolean canAssign;
                Boolean hasPerm;
                String name;
                CmsSite site;

                /**
                 * 获取站点
                 *
                 * @Title: getSite
                 * @return CmsSite
                 */
                public CmsSite getSite() {
                        return site;
                }

                @JSONField(serialize = false)
                public Integer getSiteId() {
                        if (getSite() != null) {
                                return getSite().getId();
                        }
                        return 0;
                }

                public void setSite(CmsSite site) {
                        this.site = site;
                }

                /**
                 * 是否单独分配的权限
                 *
                 * @Title: getSelected
                 * @return Boolean
                 */
                // public Boolean getAssigned() {
                // return assigned;
                // }
                //
                // public void setAssigned(Boolean assigned) {
                // this.assigned = assigned;
                // }

                /**
                 * @return the name
                 */
                public String getName() {
                        return name;
                }

                /**
                 * @param name
                 *                the name to set
                 */
                public void setName(String name) {
                        this.name = name;
                }

                /**
                 * 是否可分配
                 *
                 * @Title: getCanAssign
                 * @return Boolean
                 */
                public Boolean getCanAssign() {
                        return canAssign;
                }

                public void setCanAssign(Boolean canAssign) {
                        this.canAssign = canAssign;
                }

                /**
                 * 是否有权限
                 *
                 * @Title: getHasPerm
                 * @return Boolean
                 */
                public Boolean getHasPerm() {
                        return hasPerm;
                }

                public void setHasPerm(Boolean hasPerm) {
                        this.hasPerm = hasPerm;
                }

        }

        public static class MenuMap {
                CoreMenu menu;
                // Boolean assigned;
                Boolean canAssign;
                Boolean hasPerm;

                /**
                 * 菜单ID
                 *
                 * @Title: getMenuId
                 * @return Integer
                 */
                public Integer getMenuId() {
                        if (getMenu() != null) {
                                return getMenu().getId();
                        }
                        return 0;
                }

                public CoreMenu getMenu() {
                        return menu;
                }

                public void setMenu(CoreMenu menu) {
                        this.menu = menu;
                }

                /**
                 * 是否单独分配权限
                 *
                 * @Title: getAssigned
                 * @return Boolean
                 */
                // public Boolean getAssigned() {
                // return assigned;
                // }
                //
                // public void setAssigned(Boolean assigned) {
                // this.assigned = assigned;
                // }

                /**
                 * 是否可分配
                 *
                 * @Title: getCanAssign
                 * @return Boolean
                 */
                public Boolean getCanAssign() {
                        return canAssign;
                }

                public void setCanAssign(Boolean canAssign) {
                        this.canAssign = canAssign;
                }

                /**
                 * 是否有权限
                 *
                 * @Title: getHasPerm
                 * @return Boolean
                 */
                public Boolean getHasPerm() {
                        return hasPerm;
                }

                public void setHasPerm(Boolean hasPerm) {
                        this.hasPerm = hasPerm;
                }
        }

        /**
         * 栏目、组织、内容三层结构
         *
         * @author: tom
         * @date: 2019年3月5日 下午5:13:06
         */
        public static class ChannelRow {
                CmsSite site;

                List<ChannelDataPermTree> units;

                public CmsSite getSite() {
                        return site;
                }

                public void setSite(CmsSite site) {
                        this.site = site;
                }

                /**
                 * 获取站点名称
                 *
                 * @Title: getName
                 * @return: String
                 */
                public String getName() {
                        if (getSite() != null) {
                                return getSite().getName();
                        }
                        return MessageResolver.getMessage("tip.site.new");
                }

                /**
                 * 站点ID
                 *
                 * @Title: getSiteId
                 * @return: Integer
                 */
                public Integer getSiteId() {
                        if (getSite() != null) {
                                return getSite().getId();
                        }
                        return 0;
                }

                /**
                 * 一个站点内的栏目单元数据
                 *
                 * @Title: getUnits
                 * @return: List
                 */
                public List<ChannelDataPermTree> getUnits() {
                        return units;
                }

                public void setUnits(List<ChannelDataPermTree> units) {
                        this.units = units;
                }

        }

        public static class ChannelUnit {
                Channel channel;
                String name;
                List<MiniDataUnit> rowDatas;

                /**
                 * 创建初始化站点数据权限的一行 依 operation值顺序添加的集合
                 *
                 * @Title: initRowDatasForSite
                 */
                public void initRowDatasForSite(boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initRowDatasForSite(selected));
                }

                /**
                 * 创建初始化栏目数据权限的一行
                 *
                 * @Title: initRowDatasForChannel
                 */
                public void initRowDatasForChannel(Short[] opes, boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initRowDatasForChannel(opes, selected));
                }

                public void initRowDatasForChannel(Collection<Short> opes, boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initRowDatasForChannel(opes, selected));
                }

                /**
                 * 创建初始化内容数据权限的一行
                 *
                 * @Title: initRowDatasForContent
                 */
                public void initRowDatasForContent(Short[] opes, boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initOrgRowDatasForContent(opes, selected));
                }

                public void initRowDatasForContent(Collection<Short> opes, boolean selected) {
                        this.setRowDatas(CmsDataPermVo.initOrgRowDatasForContent(opes, selected));
                }

                /**
                 * 获取操作选项
                 *
                 * @Title: getRowOperates
                 * @return List
                 */
                @JSONField(serialize = false)
                public List<Short> getRowOperates() {
                        List<Short> operates = new ArrayList<Short>();
                        for (MiniDataUnit u : getRowDatas()) {
                                operates.add(u.getOperation());
                        }
                        return operates;
                }

                /**
                 * @return the name
                 */
                public String getName() {
                        return name;
                }

                /**
                 * @param name
                 *                the name to set
                 */
                public void setName(String name) {
                        this.name = name;
                }

                /**
                 * 栏目ID
                 *
                 * @Title: getChannelId
                 * @return: Integer
                 */
                public Integer getChannelId() {
                        if (getChannel() != null) {
                                return getChannel().getId();
                        }
                        return null;
                }

                /**
                 * 组织数据对应的数据操作
                 *
                 * @Title: getRowDatas
                 * @return: Integer[]
                 */
                public List<MiniDataUnit> getRowDatas() {
                        return rowDatas;
                }

                public void setRowDatas(List<MiniDataUnit> rowDatas) {
                        this.rowDatas = rowDatas;
                }

                public Channel getChannel() {
                        return channel;
                }

                public void setChannel(Channel channel) {
                        this.channel = channel;
                }

        }

        public static class MiniDataUnit implements Comparable<MiniDataUnit> {
                Short operation;
                // Boolean assigned;
                Boolean canAssign;
                Boolean hasPerm;

                /**
                 * 获取操作类型
                 *
                 * @Title: getOperation
                 * @return Short
                 */
                public Short getOperation() {
                        return operation;
                }

                public void setOperation(Short operation) {
                        this.operation = operation;
                }

                /**
                 * 是否单独指定
                 *
                 * @Title: getAssigned
                 * @return Boolean
                 */
                // public Boolean getAssigned() {
                // return assigned;
                // }
                //
                // public void setAssigned(Boolean assigned) {
                // this.assigned = assigned;
                // }

                /**
                 * 是否可分配
                 *
                 * @Title: getCanAssign
                 * @return Boolean
                 */
                public Boolean getCanAssign() {
                        return canAssign;
                }

                /**
                 * 是否有权限
                 *
                 * @Title: getHasPerm
                 * @return Boolean
                 */
                public Boolean getHasPerm() {
                        return hasPerm;
                }

                public void setCanAssign(Boolean canAssign) {
                        this.canAssign = canAssign;
                }

                public void setHasPerm(Boolean hasPerm) {
                        this.hasPerm = hasPerm;
                }

                /**
                 * 构造器
                 *
                 * @param operation
                 *                操作
                 * @param assigned
                 *                是否单独指定
                 * @param canAssign
                 *                是否可分配
                 * @param hasPerm
                 *                是否有权限
                 */
                public MiniDataUnit(Short operation,
                                    // Boolean assigned,
                                    Boolean canAssign, Boolean hasPerm) {
                        super();
                        this.operation = operation;
                        // this.assigned = assigned;
                        this.canAssign = canAssign;
                        this.hasPerm = hasPerm;
                }

                @Override
                public int compareTo(MiniDataUnit o) {
                        if (this != null && o != null && this.operation != null && o.getOperation() != null) {
                                return this.operation - o.getOperation();
                        }
                        return 0;
                }

        }

        /**
         * 创建初始化站点数据权限的一行 依 operation值顺序添加的集合
         *
         * @Title: initRowDatasForSite
         */
        public static List<MiniDataUnit> initRowDatasForSite(boolean selected) {
                List<MiniDataUnit> rowDatas = new ArrayList<MiniDataUnit>();
                MiniDataUnit row;
                for (Short ope : CmsDataPerm.OPE_SITE_ARRAY) {
                        row = new MiniDataUnit(ope,
                            // selected,
                            selected, selected);
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        /**
         * 创建组织 初始化站点数据权限
         *
         * @Title: initDataPermsForSite
         * @param operators
         *                操作选项
         * @param org
         *                组织
         * @param site
         *                站点
         * @return List
         */
        public static List<CmsDataPerm> initOrgDataPermsForSite(Short[] operators, CmsOrg org, CmsSite site) {
                return initDataPermsForSite(operators, org, null, null, site);
        }

        /**
         * 创建角色 初始化站点数据权限
         *
         * @Title: initRoleDataPermsForSite
         * @param operators
         *                操作选项
         * @param role
         *                角色
         * @param site
         *                站点
         * @return List
         */
        public static List<CmsDataPerm> initRoleDataPermsForSite(Short[] operators, CoreRole role, CmsSite site) {
                return initDataPermsForSite(operators, null, role, null, site);
        }

        /**
         * 创建用户 初始化站点数据权限
         *
         * @Title: initUserDataPermsForSite
         * @param operators
         *                操作选项
         * @param user
         *                用户
         * @param site
         *                站点
         * @return List
         */
        public static List<CmsDataPerm> initUserDataPermsForSite(Short[] operators, CoreUser user, CmsSite site) {
                return initDataPermsForSite(operators, null, null, user, site);
        }

        private static List<CmsDataPerm> initDataPermsForSite(Short[] operators, CmsOrg org, CoreRole role,
                                                              CoreUser user, CmsSite site) {
                List<CmsDataPerm> rowDatas = new ArrayList<CmsDataPerm>();
                CmsDataPerm row;
                for (Short ope : operators) {
                        if (org != null) {
                                row = new CmsDataPerm(site.getId(), CmsDataPerm.DATA_TYPE_SITE, ope, org.getId(),
                                    site.getId(), site, org);
                        } else if (role != null) {
                                row = new CmsDataPerm(site.getId(), CmsDataPerm.DATA_TYPE_SITE, ope, role.getId(),
                                    site.getId(), site, role);
                        } else {
                                row = new CmsDataPerm(site.getId(), CmsDataPerm.DATA_TYPE_SITE, ope, user.getId(),
                                    site.getId(), site, user);
                        }
                        if(site!=null){
                                row.setDataId(site.getId());
                        }
                        row.setSite(site);
                        row.setSiteId(site.getId());
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        /**
         * 创建初始化栏目数据权限的一行
         *
         * @Title: initRowDatasForChannel
         */
        public static List<MiniDataUnit> initRowDatasForChannel(Short[] opes, boolean selected) {
                List<MiniDataUnit> rowDatas = new ArrayList<MiniDataUnit>();
                MiniDataUnit row;
                for (Short ope : opes) {
                        row = new MiniDataUnit(ope,
                            // selected,
                            selected, selected);
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        public static List<MiniDataUnit> initRowDatasForChannel(Collection<Short> opes, boolean selected) {
                List<MiniDataUnit> rowDatas = new ArrayList<MiniDataUnit>();
                MiniDataUnit row;
                for (Short ope : opes) {
                        row = new MiniDataUnit(ope,
                            // selected,
                            selected, selected);
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        /**
         * 创建组织 初始化栏目类数据权限
         *
         * @Title: initDataPermsForChannel
         * @param org
         *                组织
         * @param site
         *                站点
         * @param containEmptyChannelSite
         *                是否包含空栏目的站点
         * @return List
         */
        public static List<CmsDataPerm> initOrgDataPermsForChannelBySite(CmsOrg org, CmsSite site,
                                                                         boolean containEmptyChannelSite) {
                return initDataPermsForChannelBySite(org, null, null, site, containEmptyChannelSite);
        }

        /**
         * 创建角色 初始化栏目类数据权限
         *
         * @Title: initRoleDataPermsForChannelBySite
         * @param role
         *                角色
         * @param site
         *                站点
         * @return List
         */
        public static List<CmsDataPerm> initRoleDataPermsForChannelBySite(CoreRole role, CmsSite site) {
                return initDataPermsForChannelBySite(null, role, null, site, false);
        }

        /**
         * 创建组织 初始化栏目类数据权限
         *
         * @Title: initUserDataPermsForChannel
         * @param user
         *                用户
         * @param site
         *                站点
         * @param containEmptyChannelSite
         *                返回数据是否包括空栏目的站点数据
         * @return List
         */
        public static List<CmsDataPerm> initUserDataPermsForChannelBySite(CoreUser user, CmsSite site,
                                                                          boolean containEmptyChannelSite) {
                return initDataPermsForChannelBySite(null, null, user, site, containEmptyChannelSite);
        }

        private static List<CmsDataPerm> initDataPermsForChannelBySite(CmsOrg org, CoreRole role, CoreUser user,
                                                                       CmsSite site, boolean containEmptyChannelSite) {
                List<CmsDataPerm> rowDatas = new ArrayList<CmsDataPerm>();
                if (site.getChannels() != null) {
                        ChannelAgent agent = new ChannelAgent();
                        List<Channel> channels = agent.sort(site.getChannels().stream().filter(c -> !c.getHasDeleted())
                            .collect(Collectors.toList()));
                        for (Channel c : channels) {
                                rowDatas.addAll(initDataPermsForChannelByChannel(CmsDataPerm.OPE_CHANNEL_ARRAY, c, org,
                                    role, user));
                        }
                }
                /**虚拟空栏目数据，将删除状态设置为true*/
                if (containEmptyChannelSite) {
                        Channel c = new Channel();
                        c.setSite(site);
                        c.setSiteId(site.getId());
                        c.setHasDeleted(true);
                        rowDatas.addAll(initDataPermsForChannelByChannel(CmsDataPerm.OPE_CHANNEL_ARRAY, c, org, role,
                            user));
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        /**
         * 创建组织 初始化栏目类数据权限
         *
         * @Title: initDataPermsForChannel
         * @param org
         *                组织
         * @param channel
         *                栏目
         * @return List
         */
        public static List<CmsDataPerm> initOrgDataPermsForChannelByChannel(Short[] operators, CmsOrg org,
                                                                            Channel channel) {
                return initDataPermsForChannelByChannel(operators, channel, org, null, null);
        }

        /**
         * 创建角色 初始化栏目类数据权限
         *
         * @Title: initRoleDataPermsForChannel
         * @param role
         *                角色
         * @param channel
         *                栏目
         * @return List
         */
        public static List<CmsDataPerm> initRoleDataPermsForChannelByChannel(Short[] operators, CoreRole role,
                                                                             Channel channel) {
                return initDataPermsForChannelByChannel(operators, channel, null, role, null);
        }

        /**
         * 创建组织 初始化栏目类数据权限
         *
         * @Title: initUserDataPermsForChannel
         * @param user
         *                用户
         * @param channel
         *                栏目
         * @return List
         */
        public static List<CmsDataPerm> initUserDataPermsForChannelByChannel(Short[] operators, CoreUser user,
                                                                             Channel channel) {
                return initDataPermsForChannelByChannel(operators, channel, null, null, user);
        }

        private static List<CmsDataPerm> initDataPermsForChannelByChannel(Short[] operators, Channel c, CmsOrg org,
                                                                          CoreRole role, CoreUser user) {
                List<CmsDataPerm> rowDatas = new ArrayList<CmsDataPerm>();
                CmsDataPerm row;
                for (Short ope : operators) {
                        if (org != null) {
                                row = new CmsDataPerm(c.getId(), CmsDataPerm.DATA_TYPE_CHANNEL, ope, org.getId(),
                                    c.getSiteId(), c.getSite(), org);
                        } else if (role != null) {
                                row = new CmsDataPerm(c.getId(), CmsDataPerm.DATA_TYPE_CHANNEL, ope, role.getId(),
                                    c.getSiteId(), c.getSite(), role);
                        } else {
                                row = new CmsDataPerm(c.getId(), CmsDataPerm.DATA_TYPE_CHANNEL, ope, user.getId(),
                                    c.getSiteId(), c.getSite(), user);
                        }
                        if(c!=null){
                                row.setDataId(c.getId());
                        }
                        row.setDataChannel(c);
                        row.setSite(c.getSite());
                        row.setSiteId(c.getSiteId());
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        /**
         * 创建初始化内容数据权限的一行
         *
         * @Title: initRowDatasForContent
         */
        public static List<MiniDataUnit> initOrgRowDatasForContent(Short[] opes, boolean selected) {
                List<MiniDataUnit> rowDatas = new ArrayList<MiniDataUnit>();
                MiniDataUnit row;
                for (Short ope : opes) {
                        row = new MiniDataUnit(ope,
                            // selected,
                            selected, selected);
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        public static List<MiniDataUnit> initOrgRowDatasForContent(Collection<Short> opes, boolean selected) {
                List<MiniDataUnit> rowDatas = new ArrayList<MiniDataUnit>();
                MiniDataUnit row;
                for (Short ope : opes) {
                        row = new MiniDataUnit(ope,
                            // selected,
                            selected, selected);
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        /**
         * 创建组织 初始化文档类数据权限
         *
         * @Title: initDataPermsForContent
         * @param org
         *                组织
         * @param site
         *                站点
         * @param containEmptyChannelSite
         *                是否包含空栏目的站点数据
         * @return List
         */
        public static List<CmsDataPerm> initOrgDataPermsForContentBySite(CmsOrg org, CmsSite site,
                                                                         boolean containEmptyChannelSite) {
                return initDataPermsForContentBySite(org, null, null, site, containEmptyChannelSite);
        }

        /**
         * 创建角色 初始化文档类数据权限
         *
         * @Title: initDataPermsForContent
         * @param role
         *                角色
         * @param site
         *                站点
         * @param containEmptyChannelSite
         *                是否包含空栏目的站点数据
         * @return List
         */
        public static List<CmsDataPerm> initRoleDataPermsForContentBySite(CoreRole role, CmsSite site,
                                                                          boolean containEmptyChannelSite) {
                return initDataPermsForContentBySite(null, role, null, site, containEmptyChannelSite);
        }

        /**
         * 创建用户 初始化文档类数据权限
         *
         * @Title: initUserDataPermsForContent
         * @param user
         *                用户
         * @param site
         *                站点
         * @param containEmptyChannelSite
         *                是否包含空栏目的站点数据
         * @return List
         */
        public static List<CmsDataPerm> initUserDataPermsForContentBySite(CoreUser user, CmsSite site,
                                                                          boolean containEmptyChannelSite) {
                return initDataPermsForContentBySite(null, null, user, site, containEmptyChannelSite);
        }

        /**
         * 创建组织 初始化文档类数据权限
         *
         * @Title: initOrgDataPermsForContentByChannel
         * @param org
         *                组织
         * @param channel
         *                栏目
         * @return List
         */
        public static List<CmsDataPerm> initOrgDataPermsForContentByChannel(Short[] operators, CmsOrg org,
                                                                            Channel channel) {
                return initDataPermsForContentByChannel(operators, channel, org, null, null);
        }

        /**
         * 创建角色 初始化文档类数据权限
         *
         * @Title: initDataPermsForContent
         * @param role
         *                角色
         * @param channel
         *                栏目
         * @return List
         */
        public static List<CmsDataPerm> initRoleDataPermsForContentByChannel(Short[] operators, CoreRole role,
                                                                             Channel channel) {
                return initDataPermsForContentByChannel(operators, channel, null, role, null);
        }

        /**
         * 创建用户 初始化文档类数据权限
         *
         * @Title: initUserDataPermsForContent
         * @param user
         *                用户
         * @param channel
         *                栏目
         * @return List
         */
        public static List<CmsDataPerm> initUserDataPermsForContentByChannel(Short[] operators, CoreUser user,
                                                                             Channel channel) {
                return initDataPermsForContentByChannel(operators, channel, null, null, user);
        }

        private static List<CmsDataPerm> initDataPermsForContentBySite(CmsOrg org, CoreRole role, CoreUser user,
                                                                       CmsSite site, boolean containEmptyChannelSite) {
                List<CmsDataPerm> rowDatas = new ArrayList<CmsDataPerm>();
                if (site.getChannels() != null) {
                        ChannelAgent agent = new ChannelAgent();
                        /** 回收站栏目还不能过滤，数据权限需要对回收站的栏目有效 */
                        List<Channel> channels = agent.sort(site.getChannels().stream().filter(c -> !c.getHasDeleted())
                            .collect(Collectors.toList()));
                        for (Channel c : channels) {
                                rowDatas.addAll(initDataPermsForContentByChannel(CmsDataPerm.OPE_CONTENT_ARRAY, c, org,
                                    role, user));
                        }
                }
                /**虚拟空栏目数据，将删除状态设置为true*/
                if (containEmptyChannelSite) {
                        /** 方便后续处理站点 CmsOrg.getSiteOfOwnerDataPermsByType */
                        Channel c = new Channel();
                        c.setSite(site);
                        c.setSiteId(site.getId());
                        c.setHasDeleted(true);
                        rowDatas.addAll(initDataPermsForContentByChannel(CmsDataPerm.OPE_CONTENT_ARRAY, c, org, role,
                            user));
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

        private static List<CmsDataPerm> initDataPermsForContentByChannel(Short[] operators, Channel c, CmsOrg org,
                                                                          CoreRole role, CoreUser user) {
                List<CmsDataPerm> rowDatas = new ArrayList<CmsDataPerm>();
                CmsDataPerm row;
                for (Short ope : operators) {
                        if (org != null) {
                                row = new CmsDataPerm(c.getId(), CmsDataPerm.DATA_TYPE_CONTENT, ope, org.getId(),
                                    c.getSiteId(), c.getSite(), org);
                        } else if (role != null) {
                                row = new CmsDataPerm(c.getId(), CmsDataPerm.DATA_TYPE_CONTENT, ope, role.getId(),
                                    c.getSiteId(), c.getSite(), role);
                        } else {
                                row = new CmsDataPerm(c.getId(), CmsDataPerm.DATA_TYPE_CONTENT, ope, user.getId(),
                                    c.getSiteId(), c.getSite(), user);
                        }
                        if(c!=null){
                                row.setDataId(c.getId());
                        }
                        row.setDataChannel(c);
                        row.setSite(c.getSite());
                        row.setSiteId(c.getSiteId());
                        rowDatas.add(row);
                }
                Collections.sort(rowDatas);
                return rowDatas;
        }

}
