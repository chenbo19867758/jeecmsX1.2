/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 数据权限Dto
 * 
 * @author: tom
 * @date: 2019年3月5日 下午4:25:35
 */
public class CmsDatePermDto implements Serializable {
        private static final long serialVersionUID = -2448269659252417398L;
        Short dataType;
        /** 站点数据 */
        List<SiteMap> siteDatas;

        /** 菜单数据 */
        List<MenuMap> menus;
        /** 二级结构 */
        List<SiteRow> dataIds;
        /** 三级结构 */
        ChannelRow moreDataIds;
        Integer roleId;
        Integer orgId;
        Integer userId;

        /**
         * 站点数据 是否选中一个
         * 
         * @Title: getSiteDataOneSelect
         * @return: boolean
         */
        public boolean getSiteDataOneSelect() {
                if (getSiteDatas() == null || getSiteDatas().isEmpty()) {
                        return false;
                }
                for (SiteMap siteMap : getSiteDatas()) {
                        if (siteMap.getSelected() != null && siteMap.getSelected()) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 菜单数据 是否选中一个
         * 
         * @Title: getMenuOneSelect
         * @return: boolean
         */
        public boolean getMenuOneSelect() {
                if (getMenus() == null || getMenus().isEmpty()) {
                        return false;
                }
                for (MenuMap menuMap : getMenus()) {
                        if (menuMap.getSelected() != null && menuMap.getSelected()) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 站点数据 是否选中一个
         * 
         * @Title: getSiteRowDateOneSelect
         * @return: boolean
         */
        public boolean getSiteRowDateOneSelect() {
                if (getDataIds() == null || getDataIds().isEmpty()) {
                        return false;
                }
                for (SiteRow siteRow : getDataIds()) {
                        for (MiniDataUnit unit : siteRow.getRowDatas()) {
                                if (unit.getSelected() != null && unit.getSelected()) {
                                        return true;
                                }
                        }
                }
                return false;
        }
        
        /**
         * 栏目数据 是否选中一个
         * 
         * @Title: getChannelRowDateOneSelect
         * @return: boolean
         */
        public boolean getChannelRowDateOneSelect() {
                if (getMoreDataIds() == null) {
                        return false;
                }
                for (ChannelUnit channelUnit : getMoreDataIds().getUnits()) {
                        for (MiniDataUnit unit : channelUnit.getRowDatas()) {
                                if (unit.getSelected() != null && unit.getSelected()) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        /**
         * 数据模块类型（1站点 2栏目 3文档 4组织 5可管理站点）
         * 
         * @Title: getDataType
         * @return Short
         */
        @NotNull
        public Short getDataType() {
                return dataType;
        }

        /**
         * 获取站点数据
         * 
         * @Title: getSiteDatas
         * @return SiteMap[]
         */
        public List<SiteMap> getSiteDatas() {
                return siteDatas;
        }

        public void setSiteDatas(List<SiteMap> siteDatas) {
                this.siteDatas = siteDatas;
        }

        /**
         * 获取菜单数据
         * 
         * @Title: getMenus
         * @return List
         */
        public List<MenuMap> getMenus() {
                return menus;
        }

        public void setMenus(List<MenuMap> menus) {
                this.menus = menus;
        }

        /**
         * 指定站点的数据权限 所选择的站点操作类型ID
         * 
         * @Title: getDataIds
         * @return: SiteRow[]
         */
        public List<SiteRow> getDataIds() {
                return dataIds;
        }

        /**
         * 角色ID
         * 
         * @Title: getRoleId
         * @return: Integer
         */
        public Integer getRoleId() {
                return roleId;
        }

        /**
         * 组织ID
         * 
         * @Title: getOrgId
         * @return: Integer
         */
        public Integer getOrgId() {
                return orgId;
        }

        /**
         * 用户ID
         * 
         * @Title: getUserId
         * @return: Integer
         */
        public Integer getUserId() {
                return userId;
        }

        /**
         * 栏目、组织、内容三层结构 对象数组
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

        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        public void setOrgId(Integer orgId) {
                this.orgId = orgId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        public void setDataIds(List<SiteRow> dataIds) {
                this.dataIds = dataIds;
        }

        public void setDataType(Short dataType) {
                this.dataType = dataType;
        }

        /**
         * 站点数据结构
         * 
         * @author: tom
         * @date: 2019年3月5日 下午5:02:29
         */
        public static class SiteRow {
                Integer siteId;
                List<MiniDataUnit> rowDatas;

                /**
                 * 获取站点ID
                 * 
                 * @Title: getSiteId 0 代表增量站点
                 * @return Integer
                 */
                public Integer getSiteId() {
                        return siteId;
                }

                public List<MiniDataUnit> getRowDatas() {
                        return rowDatas;
                }

                public void setSiteId(Integer siteId) {
                        this.siteId = siteId;
                }

                public void setRowDatas(List<MiniDataUnit> rowDatas) {
                        this.rowDatas = rowDatas;
                }
        }

        public static class SiteMap {
                Integer siteId;
                Boolean selected;

                /**
                 * 站点ID
                 * 
                 * @Title: getSiteId 0 代表增量站点
                 * @return Integer
                 */
                public Integer getSiteId() {
                        return siteId;
                }

                public void setSiteId(Integer siteId) {
                        this.siteId = siteId;
                }

                /**
                 * 是否选中
                 * 
                 * @Title: getSelected
                 * @return Boolean
                 */
                public Boolean getSelected() {
                        return selected;
                }

                public void setSelected(Boolean selected) {
                        this.selected = selected;
                }

        }

        public static class MenuMap {
                Integer menuId;
                Boolean selected;

                /**
                 * 菜单ID
                 * 
                 * @Title: getMenuId 0 代表增量菜单
                 * @return Integer
                 */
                public Integer getMenuId() {
                        return menuId;
                }

                public void setMenuId(Integer menuId) {
                        this.menuId = menuId;
                }

                /**
                 * 是否选中
                 * 
                 * @Title: getSelected
                 * @return Boolean
                 */
                public Boolean getSelected() {
                        return selected;
                }

                public void setSelected(Boolean selected) {
                        this.selected = selected;
                }

                public MenuMap(Integer menuId, Boolean selected) {
                        super();
                        this.menuId = menuId;
                        this.selected = selected;
                }

        }

        /**
         * 栏目、内容三层结构
         * 
         * @author: tom
         * @date: 2019年3月5日 下午5:13:06
         */
        public static class ChannelRow {
                Integer siteId;
                ChannelUnit[] units;

                /**
                 * 站点ID
                 * 
                 * @Title: getSiteId
                 * @return: Integer
                 */
                public Integer getSiteId() {
                        return siteId;
                }

                /**
                 * 一个站点内的栏目、组织单元数据
                 * 
                 * @Title: getUnits
                 * @return: ChannelOrgUnit[]
                 */
                public ChannelUnit[] getUnits() {
                        return units;
                }

                public void setSiteId(Integer siteId) {
                        this.siteId = siteId;
                }

                public void setUnits(ChannelUnit[] units) {
                        this.units = units;
                }

        }

        public static class ChannelUnit {
                Integer keyId;
                MiniDataUnit[] rowDatas;

                /**
                 * 栏目ID或者组织ID
                 * 
                 * @Title: getKeyId
                 * @return: Integer
                 */
                public Integer getKeyId() {
                        return keyId;
                }

                /**
                 * 组织数据对应的数据操作
                 * 
                 * @Title: getRowDatas
                 * @return: Integer[]
                 */
                public MiniDataUnit[] getRowDatas() {
                        return rowDatas;
                }

                public void setKeyId(Integer keyId) {
                        this.keyId = keyId;
                }

                public void setRowDatas(MiniDataUnit[] rowDatas) {
                        this.rowDatas = rowDatas;
                }

        }

        public static class MiniDataUnit {
                Short operation;
                Boolean selected;

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
                 * 是否选中
                 * 
                 * @Title: getSelected
                 * @return Boolean
                 */
                public Boolean getSelected() {
                        return selected;
                }

                public void setSelected(Boolean selected) {
                        this.selected = selected;
                }
        }

}
