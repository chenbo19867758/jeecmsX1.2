package com.jeecms.auth.domain.dto;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 菜单栏DTO
 *
 * @author: ztx
 * @date: 2018年5月28日 下午3:27:00
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CoreMenuDto {

        public enum RoutingConstant {
                /**
                 * 平台管理
                 */
                PlatFormWeChat("/wechat"),
                /**
                 * 平台管理类型
                 */
                PlatFormWeChatPublic("/wechat/public");

                /**
                 * 路由标识
                 */
                private String routing;

                public String getRouting() {
                        return routing;
                }

                public void setRouting(String routing) {
                        this.routing = routing;
                }

                private RoutingConstant(String routing) {
                        this.routing = routing;
                }
        }

        /**
         * 菜单Id
         */
        private Integer id;
        /**
         * 父级菜单Id
         */
        private Integer parentId;
        /**
         * 菜单名称
         */
        private String menuName;
        /**
         * 路由标识（path）
         */
        private String component;
        /**
         * 页面路径
         */
        private String path;
        /**
         * 权限标识
         */
        private String name;
        /**
         * 菜单图标
         */
        private String icon;
        /**
         * 重定向路由标识
         */
        private String redirect;
        /**
         * 排序
         */
        private Integer sortNum;
        /**
         * 菜单类型 1、菜单 2、权限
         */
        private Short menuType;
        /**
         * 是否可见 true可见 false不可见
         */
        private Boolean hidden;
        /**
         * 是否参与权限分配
         */
        private Boolean isAuth;
        /**
         * 接口id数组
         */
        private Integer[] apiIds;

        @Digits(integer = 11, fraction = 0)
        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        @NotBlank
        @Length(max = 150)
        public String getMenuName() {
                return menuName;
        }

        public void setMenuName(String menuName) {
                this.menuName = menuName;
        }

        @Length(max = 150)
        public String getComponent() {
                return component;
        }

        public void setComponent(String component) {
                this.component = component;
        }

        @Length(max = 150)
        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        @Length(max = 150)
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Length(max = 150)
        public String getIcon() {
                return icon;
        }

        public void setIcon(String icon) {
                this.icon = icon;
        }

        @Length(max = 150)
        public String getRedirect() {
                return redirect;
        }

        public void setRedirect(String redirect) {
                this.redirect = redirect;
        }

        @NotNull
        public Integer getSortNum() {
                return sortNum;
        }

        public void setSortNum(Integer sortNum) {
                this.sortNum = sortNum;
        }

        public Short getMenuType() {
                return menuType;
        }

        public void setMenuType(Short menuType) {
                this.menuType = menuType;
        }

        @NotNull
        public Boolean getHidden() {
                return hidden;
        }

        public void setHidden(Boolean hidden) {
                this.hidden = hidden;
        }
        
        @NotNull
        public Boolean getIsAuth() {
                return isAuth;
        }

        public void setIsAuth(Boolean isAuth) {
                this.isAuth = isAuth;
        }


        public Integer[] getApiIds() {
                return apiIds;
        }

        public void setApiIds(Integer[] apiIds) {
                this.apiIds = apiIds;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof CoreMenuDto)) {
                        return false;
                }

                CoreMenuDto that = (CoreMenuDto) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getParentId() != null ? !getParentId().equals(that.getParentId()) : that.getParentId() != null) {
                        return false;
                }
                if (getMenuName() != null ? !getMenuName().equals(that.getMenuName()) : that.getMenuName() != null) {
                        return false;
                }
                if (getComponent() != null ? !getComponent().equals(that.getComponent()) : that.getComponent() != null) {
                        return false;
                }
                if (getPath() != null ? !getPath().equals(that.getPath()) : that.getPath() != null) {
                        return false;
                }
                if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
                        return false;
                }
                if (getIcon() != null ? !getIcon().equals(that.getIcon()) : that.getIcon() != null) {
                        return false;
                }
                if (getRedirect() != null ? !getRedirect().equals(that.getRedirect()) : that.getRedirect() != null) {
                        return false;
                }
                if (getSortNum() != null ? !getSortNum().equals(that.getSortNum()) : that.getSortNum() != null) {
                        return false;
                }
                if (getMenuType() != null ? !getMenuType().equals(that.getMenuType()) : that.getMenuType() != null) {
                        return false;
                }
                if (getHidden() != null ? !getHidden().equals(that.getHidden()) : that.getHidden() != null) {
                        return false;
                }
                // Probably incorrect - comparing Object[] arrays with Arrays.equals
                return Arrays.equals(getApiIds(), that.getApiIds());
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
                result = 31 * result + (getMenuName() != null ? getMenuName().hashCode() : 0);
                result = 31 * result + (getComponent() != null ? getComponent().hashCode() : 0);
                result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
                result = 31 * result + (getName() != null ? getName().hashCode() : 0);
                result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
                result = 31 * result + (getRedirect() != null ? getRedirect().hashCode() : 0);
                result = 31 * result + (getSortNum() != null ? getSortNum().hashCode() : 0);
                result = 31 * result + (getMenuType() != null ? getMenuType().hashCode() : 0);
                result = 31 * result + (getHidden() != null ? getHidden().hashCode() : 0);
                result = 31 * result + Arrays.hashCode(getApiIds());
                return result;
        }
}
