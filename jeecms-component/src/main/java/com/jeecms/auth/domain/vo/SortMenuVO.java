/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain.vo;

import com.jeecms.auth.domain.CoreMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.aspectj.weaver.ast.HasAnnotation;

/**
 * 用户菜单路由权限ViewMode
 *
 * @author: ztx
 * @date: 2018年7月11日 下午3:56:42
 */
public class SortMenuVO implements Comparable<SortMenuVO> {

        /**
         * 标识id
         */
        private Integer id;
        /**
         * 菜单名称
         */
        private String menuName;
        /**
         * 路由标识（组件）
         */
        private String component;
        /**
         * 权限标识
         */
        private String name;
        /**
         * 页面路径
         */
        private String path;
        /**
         * 菜单图标
         */
        private String icon;
        /**
         * 重定向路由标识
         */
        private String redirect;
        /**
         * 是否可见
         */
        private Boolean hidden;
        /**
         * 父节点id
         */
        private Integer parentId;
        /**
         * 排序值
         */
        private Integer sortNum;

        /**
         * 子节点数据集(这里可以不用Set装的,如果在Service做了处理的话)
         */
        private List<SortMenuVO> children;

        public SortMenuVO() {
                super();
        }

        // 手动给list集合排序,由于VO里面还有子节点VO,所以它所对应的排序规则是不能更改的,而且不能返回0(当子节点不允许被覆盖时).
        // 并且还有两个问题 :
        // 1.JDK1.8已经不支持System.setProperties("java.util.Arrays.useLegacyMergeSort","true");
        // 来设置排序方法为普通归并排序.得从程序启动参数中设置.
        // 2.由于问题{1},所以现在只能用TimSort(加强归并排序,但是它对排序规则非常严格)算法来排序,
        // 这里排序时不能用VO的那套排序规则,原因是VO中的排序规则缺少传递性,用了会异常.解决办法可以在排序的时候指定一个自定义的排序规则覆盖T中的.
        /**
         * 菜单排序
         * 
         * @Title: sortBySortNum
         * @param menus
         *                菜单
         * @return: void
         */
        public static void sortBySortNum(List<SortMenuVO> menus) {
                Collections.sort(menus, new SortMenuComparator());
        }

        public static List<SortMenuVO> sortListBySortAndChild(List<SortMenuVO> menus) {
                sortBySortAndChild(menus);
                return menus;
        }

        public static List<SortMenuVO> sortBySortAndChild(List<SortMenuVO> menus) {
                if (menus != null && menus.size() > 0) {
                        menus = menus.stream().sorted(new SortMenuVO.SortMenuComparator()).collect(Collectors.toList());
                        for (SortMenuVO vo : menus) {
                                if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                                        vo.setChildren(sortBySortAndChild(vo.getChildren()));
                                }
                        }
                }
                return menus;
        }

        public static class SortMenuComparator implements Comparator<SortMenuVO> {
                @Override
                public int compare(SortMenuVO o1, SortMenuVO o2) {
                        Integer sourceSortNum = o1.getSortNum();
                        Integer targetSortNum = o2.getSortNum();
                        if (sourceSortNum.equals(targetSortNum)) {
                                return 0;
                        }
                        return sourceSortNum < targetSortNum ? -1 : 1;
                }
        }

        /**
         * 构造函数
         *
         * @param menu
         *                菜单对象
         */
        public SortMenuVO(CoreMenu menu) {
                super();
                this.id = menu.getId();
                this.menuName = menu.getMenuName();
                this.path = menu.getPath();
                this.name = menu.getName();
                this.component = menu.getComponent();
                this.icon = menu.getIcon();
                this.redirect = menu.getRedirect();
                this.hidden = menu.getHidden();
                this.parentId = menu.getParentId();
                this.sortNum = menu.getSortNum();
        }

        public SortMenuVO(Integer id, String menuName, Integer sortNum) {
                super();
                this.id = id;
                this.menuName = menuName;
                this.sortNum = sortNum;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getMenuName() {
                return menuName;
        }

        public void setMenuName(String menuName) {
                this.menuName = menuName;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        public String getComponent() {
                return component;
        }

        public void setComponent(String component) {
                this.component = component;
        }

        public String getIcon() {
                return icon;
        }

        public void setIcon(String icon) {
                this.icon = icon;
        }

        public String getRedirect() {
                return redirect;
        }

        public void setRedirect(String redirect) {
                this.redirect = redirect;
        }

        public Boolean getHidden() {
                return hidden;
        }

        public void setHidden(Boolean hidden) {
                this.hidden = hidden;
        }

        public List<SortMenuVO> getChildren() {
                return children;
        }

        public void setChildren(List<SortMenuVO> children) {
                this.children = children;
        }

        public Integer getParentId() {
                return parentId;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        public Integer getSortNum() {
                return sortNum;
        }

        public void setSortNum(Integer sortNum) {
                this.sortNum = sortNum;
        }

        @Override
        public int compareTo(SortMenuVO o) {
                Integer sourceSortNum = Integer.valueOf(this.getSortNum());
                Integer targetSortNum = Integer.valueOf(o.getSortNum());
                return (sourceSortNum < targetSortNum || sourceSortNum.equals(targetSortNum)) ? -1 : 1;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof SortMenuVO)) {
                        return false;
                }

                SortMenuVO that = (SortMenuVO) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getMenuName() != null ? !getMenuName().equals(that.getMenuName()) : that.getMenuName() != null) {
                        return false;
                }
                if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
                        return false;
                }
                if (getPath() != null ? !getPath().equals(that.getPath()) : that.getPath() != null) {
                        return false;
                }
                if (getComponent() != null ? !getComponent().equals(that.getComponent())
                                : that.getComponent() != null) {
                        return false;
                }
                if (getIcon() != null ? !getIcon().equals(that.getIcon()) : that.getIcon() != null) {
                        return false;
                }
                if (getRedirect() != null ? !getRedirect().equals(that.getRedirect()) : that.getRedirect() != null) {
                        return false;
                }
                if (getHidden() != null ? !getHidden().equals(that.getHidden()) : that.getHidden() != null) {
                        return false;
                }
                if (getParentId() != null ? !getParentId().equals(that.getParentId()) : that.getParentId() != null) {
                        return false;
                }
                if (getSortNum() != null ? !getSortNum().equals(that.getSortNum()) : that.getSortNum() != null) {
                        return false;
                }
                return getChildren() != null ? getChildren().equals(that.getChildren()) : that.getChildren() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getMenuName() != null ? getMenuName().hashCode() : 0);
                result = 31 * result + (getName() != null ? getName().hashCode() : 0);
                result = 31 * result + (getPath() != null ? getPath().hashCode() : 0);
                result = 31 * result + (getComponent() != null ? getComponent().hashCode() : 0);
                result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
                result = 31 * result + (getRedirect() != null ? getRedirect().hashCode() : 0);
                result = 31 * result + (getHidden() != null ? getHidden().hashCode() : 0);
                result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
                result = 31 * result + (getSortNum() != null ? getSortNum().hashCode() : 0);
                result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
                return result;
        }

        @Override
        public String toString() {
                return "SortMenuVO{" + "id=" + id + ", name='" + name + '\'' + ", path='" + path + '\''
                                + ", component='" + component + '\'' + ", icon='" + icon + '\'' + ", redirect='"
                                + redirect + '\'' + ", hidden=" + hidden + ", parentId=" + parentId + ", sortNum="
                                + sortNum + ", children=" + children + '}';
        }
}
