/*
@Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.vo.SortMenuVO;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.AbstractTreeDomain;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单管理
 *
 * @author: ztx
 * @date: 2018年6月11日 上午10:26:49
 */
@Entity
@Table(name = "jc_sys_menu")
public class CoreMenu extends AbstractTreeDomain<CoreMenu, Integer> implements Serializable, Cloneable {

        private static final long serialVersionUID = 1L;

        /**
         * 菜单类型--菜单
         */
        public static final short MENUTYPE_MENU = 1;
        /**
         * 菜单类型--权限
         */
        public static final short MENUTYPE_AUTH = 2;

        /**
         * 管理后台系统类型 --平台管理
         */
        public static final short TYPE_ADMIN = 1;

        /**
         * 主键
         */
        private Integer id;
        /**
         * 菜单标识
         */
        private String name;
        /**
         * 是否可见 true可见 false不可见
         */
        private Boolean hidden;
        /**
         * 菜单图标
         */
        private String icon;
        /**
         * 菜单名称
         */
        private String menuName;
        /**
         * 菜单类型 1、菜单 2、权限
         */
        private Short menuType;
        /**
         * 页面路径(路由地址)
         */
        private String path;
        /**
         * 重定向路由标识
         */
        private String redirect;
        /**
         * 路由标识（页面组件）
         */
        private String component;
        /**
         * 是否参与权限分配
         */
        private Boolean isAuth;
        /**
         * api集合
         */
        private List<CoreApi> apis = new ArrayList<>(0);
        /**
         * 菜单角色
         */
        private List<CoreRole> roles;

        private List<CoreUser> users;

        private List<CmsOrg> orgs;

        private Integer[] parentIds;

        @Transient
        public List<CoreMenu> getNodeList() {
                LinkedList<CoreMenu> list = new LinkedList<CoreMenu>();
                CoreMenu node = this;
                while (node != null) {
                        list.addFirst(node);
                        node = node.getParent();
                }
                return list;
        }

        @Transient
        public String getMenuStr() {
                List<CoreMenu> menus = getNodeList();
                StringBuffer buff = new StringBuffer();
                for (CoreMenu m : menus) {
                        buff.append(m.getMenuName() + "->");
                }
                return buff.toString();
        }

        public CoreMenu() {
        }

        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<CoreMenu> menus) {
                if (menus == null) {
                        return null;
                }
                List<Integer> ids = new ArrayList<Integer>();
                for (CoreMenu s : menus) {
                        ids.add(s.getId());
                }
                return ids;
        }

        /**
         * 递归获取集合内菜单的所有子菜单
         * 
         * @Title: getAllChildAndSort
         * @param menus
         * @param filterAuthMenu 是否过滤 是否权限分配控制 的菜单 true 则只包含未开启权限控制的默认赋予权限的菜单
         * @return: List<CoreMenu>
         */
        @Transient
        @JSONField(serialize = false)
        public static List<CoreMenu> getAllChildAndSort(List<CoreMenu> menus, boolean filterAuthMenu) {
                Set<CoreMenu> menusSet = new HashSet<CoreMenu>();
                if (menus != null && menus.size() > 0) {
                        for (CoreMenu vo : menus) {
                                if(!filterAuthMenu||filterAuthMenu&&!vo.getIsAuth()){
                                        menusSet.add(vo);
                                }
                                if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                                        menusSet.addAll(getAllChildAndSort(vo.getChildren(), filterAuthMenu));
                                }
                        }
                }
                menus = menusSet.stream().sorted(new CoreMenuComparator()).collect(Collectors.toList());
                return menus;
        }

        public static class CoreMenuComparator implements Comparator<CoreMenu> {
                @Override
                public int compare(CoreMenu o1, CoreMenu o2) {
                        Integer sourceSortNum = o1.getSortNum();
                        Integer targetSortNum = o2.getSortNum();
                        if (sourceSortNum.equals(targetSortNum)) {
                                return 0;
                        }
                        return sourceSortNum < targetSortNum ? -1 : 1;
                }
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_menu", pkColumnValue = "jc_sys_menu", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_menu")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Length(max = 150)
        @Column(name = "perm_identity", length = 150)
        public String getName() {
                return this.name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @NotNull
        @Column(name = "is_show", nullable = false)
        public Boolean getHidden() {
                return this.hidden;
        }

        public void setHidden(Boolean show) {
                this.hidden = show;
        }

        @Length(max = 150)
        @Column(name = "icon_class", length = 150)
        public String getIcon() {
                return this.icon;
        }

        public void setIcon(String icon) {
                this.icon = icon;
        }

        @NotBlank
        @Length(max = 100)
        @Column(name = "menu_name", nullable = false, length = 150)
        public String getMenuName() {
                return this.menuName;
        }

        public void setMenuName(String menuName) {
                this.menuName = menuName;
        }

        @NotNull
        @Digits(integer = 6, fraction = 0)
        @Column(name = "menu_type", nullable = false)
        public Short getMenuType() {
                return this.menuType;
        }

        public void setMenuType(Short menuType) {
                this.menuType = menuType;
        }

        @Length(max = 150)
        @Column(name = "route_path", length = 150)
        public String getComponent() {
                return this.component;
        }

        public void setComponent(String component) {
                this.component = component;
        }

        @Column(name = "is_auth")
        public Boolean getIsAuth() {
                return isAuth;
        }

        public void setIsAuth(Boolean isAuth) {
                this.isAuth = isAuth;
        }

        @Length(max = 150)
        @Column(name = "redirect_route", length = 150)
        public String getRedirect() {
                return this.redirect;
        }

        public void setRedirect(String redirect) {
                this.redirect = redirect;
        }

        @Length(max = 150)
        @Column(name = "page_path", length = 150)
        public String getPath() {
                return this.path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_menu_api", joinColumns = @JoinColumn(name = "menu_management_id") , inverseJoinColumns = @JoinColumn(name = "api_body_information_id") )
        public List<CoreApi> getApis() {
                return apis;
        }

        public void setApis(List<CoreApi> apis) {
                this.apis = apis;
        }

        @ManyToMany(cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_role_menu", joinColumns = @JoinColumn(name = "menu_id") , inverseJoinColumns = @JoinColumn(name = "role_id") )
        public List<CoreRole> getRoles() {
                return roles;
        }

        public void setRoles(List<CoreRole> roles) {
                this.roles = roles;
        }

        @ManyToMany(cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_user_menu", joinColumns = @JoinColumn(name = "menu_id") , inverseJoinColumns = @JoinColumn(name = "user_id") )
        public List<CoreUser> getUsers() {
                return users;
        }

        @ManyToMany(cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_org_menu", joinColumns = @JoinColumn(name = "menu_id") , inverseJoinColumns = @JoinColumn(name = "org_id") )
        public List<CmsOrg> getOrgs() {
                return orgs;
        }

        public void setUsers(List<CoreUser> users) {
                this.users = users;
        }

        public void setOrgs(List<CmsOrg> orgs) {
                this.orgs = orgs;
        }

        @Transient
        public Integer[] getParentIds() {
                return parentIds;
        }

        public void setParentIds(Integer[] parentIds) {
                this.parentIds = parentIds;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof CoreMenu)) {
                        return false;
                }

                CoreMenu menu = (CoreMenu) o;

                if (getId() != null ? !getId().equals(menu.getId()) : menu.getId() != null) {
                        return false;
                }
                if (getName() != null ? !getName().equals(menu.getName()) : menu.getName() != null) {
                        return false;
                }
                if (getHidden() != null ? !getHidden().equals(menu.getHidden()) : menu.getHidden() != null) {
                        return false;
                }
                if (getIcon() != null ? !getIcon().equals(menu.getIcon()) : menu.getIcon() != null) {
                        return false;
                }
                if (getMenuName() != null ? !getMenuName().equals(menu.getMenuName()) : menu.getMenuName() != null) {
                        return false;
                }
                if (getMenuType() != null ? !getMenuType().equals(menu.getMenuType()) : menu.getMenuType() != null) {
                        return false;
                }
                if (getComponent() != null ? !getComponent().equals(menu.getComponent())
                                : menu.getComponent() != null) {
                        return false;
                }
                if (getRedirect() != null ? !getRedirect().equals(menu.getRedirect()) : menu.getRedirect() != null) {
                        return false;
                }
                if (getPath() != null ? !getPath().equals(menu.getPath()) : menu.getPath() != null) {
                        return false;
                }
                if (getApis() != null ? !getApis().equals(menu.getApis()) : menu.getApis() != null) {
                        return false;
                }
                if (getRoles() != null ? !getRoles().equals(menu.getRoles()) : menu.getRoles() != null) {
                        return false;
                }
                return getChildren() != null ? getChildren().equals(menu.getChildren()) : menu.getChildren() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getName() != null ? getName().hashCode() : 0);
                result = 31 * result + (getHidden() != null ? getHidden().hashCode() : 0);
                result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
                result = 31 * result + (getMenuName() != null ? getMenuName().hashCode() : 0);
                result = 31 * result + (getMenuType() != null ? getMenuType().hashCode() : 0);
                result = 31 * result + (getComponent() != null ? getComponent().hashCode() : 0);
                result = 31 * result + (getRedirect() != null ? getRedirect().hashCode() : 0);
                result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
                result = 31 * result + (getApis() != null ? getApis().hashCode() : 0);
                result = 31 * result + (getRoles() != null ? getRoles().hashCode() : 0);
                result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
                return result;
        }

        /**
         * 获取当前菜单对应api权限标识数据，多个以逗号分隔
         */
        @Transient
        public String getApiPerms() {
                List<CoreApi> apis = getApis();
                StringBuffer buff = new StringBuffer();
                for (CoreApi api : apis) {
                        buff.append(api.getPerms()).append(",");
                }
                return buff.toString();
        }

        /**
         * 获取当前菜单所有api地址数据，多个以逗号分隔
         */
        @Transient
        public String getApiUrls() {
                List<CoreApi> apis = getApis();
                StringBuffer buff = new StringBuffer();
                if (apis != null && apis.size() > 0) {
                        for (CoreApi api : apis) {
                                buff.append(api.getApiUrl()).append(",");
                        }
                }
                return buff.toString();
        }

        /**
         * 获得附加条件 通过附加条件可以维护多棵树相互独立的树，附加条件使用hql语句，实体别名为bean。例如：bean.website.id=5
         *
         * @return 为null则不添加任何附加条件
         */
        @Transient
        @Override
        public String getTreeCondition() {
                return "bean.hasDeleted=false ";
        }

        /**
         * 判断是否为根节点. true表示是,false表示否
         */
        @Transient
        public Boolean getIsChild() {
                // 判断是否为根节点
                if (super.getChildren().size() == 0) {
                        return true;
                }
                return false;
        }

        @Override
        public CoreMenu clone() {
                CoreMenu clone = null;
                try {
                        clone = (CoreMenu) super.clone();
                } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e); // won't happen
                }
                return clone;
        }

}