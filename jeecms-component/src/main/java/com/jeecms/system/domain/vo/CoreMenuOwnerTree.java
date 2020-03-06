/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.vo.SortMenuVO;
import com.jeecms.system.domain.vo.CmsDataPermVo.MenuMap;
import com.jeecms.system.domain.vo.CmsDataPermVo.SiteMap;

/**
 * 菜单权限树
 * 
 * @author: tom
 * @date: 2019年4月27日 下午4:58:44
 */
public class CoreMenuOwnerTree {
        CoreMenu menu;
        Integer id;
        String name;
        Integer parentId;
        List<CoreMenuOwnerTree> children;
        // Boolean assigned;
        Boolean canAssign;
        Boolean hasPerm;

        /**
         * 菜单权限集合转换成带树形结构数据
         * 
         * @Title: getChildTree
         * @param perms
         *                菜单权限
         * @return List
         */
        public static List<CoreMenuOwnerTree> getChildTree(Collection<MenuMap> perms) {
                List<CoreMenuOwnerTree> result = new ArrayList<CoreMenuOwnerTree>();
                if (null == perms || perms.size() == 0) {
                        return result;
                }
                List<CoreMenu> childs = perms.stream().map(perm -> perm.getMenu()).sorted(new CoreMenu.CoreMenuComparator()).collect(Collectors.toList());
                Map<Integer, MenuMap> menuMapGroup = perms.stream().collect(Collectors.toMap(MenuMap::getMenuId,perm -> perm));
                if (childs != null && !childs.isEmpty()) {
                        CoreMenu menu = childs.iterator().next();
                        Integer parentId = null;
                        if (menu != null) {
                                parentId = menu.getParentId();
                        }
                        List<CoreMenuOwnerTree> dataSource = new ArrayList<>();
                        Map<Integer, CoreMenuOwnerTree> hashDatas = new HashMap<>(childs.size());
                        for (CoreMenu t : childs) {
                                CoreMenuOwnerTree st = new CoreMenuOwnerTree();
                                st.setId(t.getId());
                                st.setParentId(t.getParentId());
                                st.setMenu(t);
                                st.setName(t.getName());
                                MenuMap menuMap = menuMapGroup.get(t.getId());
                                st.setCanAssign(menuMap.getCanAssign());
                                st.setHasPerm(menuMap.getHasPerm());
                                // 没有子节点则过滤childs
                                long count = childs.stream().filter(c -> null != c.getParentId()
                                                && ((Integer) t.getId()).intValue() == c.getParentId().intValue())
                                                .count();
                                if (count > 0) {
                                        st.setChildren(new ArrayList<CoreMenuOwnerTree>());
                                }
                                dataSource.add(st);
                                hashDatas.put(t.getId(), st);
                        }
                        childs.clear();

                        // 遍历菜单集合
                        for (int i = 0; i < dataSource.size(); i++) {
                                // 当前节点
                                CoreMenuOwnerTree json = (CoreMenuOwnerTree) dataSource.get(i);
                                // 当前的父节点
                                CoreMenuOwnerTree hashObject = hashDatas.get(json.getParentId());

                                if (hashObject != null) {
                                        // 表示当前节点为子节点
                                        hashObject.getChildren().add(json);
                                } else if (null == json.getParentId()
                                                || parentId.intValue() == ((Integer) json.getParentId())) {
                                        // parentId为null和获取匹配parentId的节点(生成某节点的子节点树时需要用到)
                                        result.add(json);
                                }
                        }
                        sortBySortAndChild(result);
                }
                return result;
        }
        
        public static  List<CoreMenuOwnerTree> sortBySortAndChild(List<CoreMenuOwnerTree> menus) {
                if (menus != null && menus.size() > 0) {
                        menus = menus.stream().sorted(new CoreMenuOwnerTreeComparator()).collect(Collectors.toList());
                        for (CoreMenuOwnerTree vo : menus) {
                                if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                                        vo.setChildren(sortBySortAndChild(vo.getChildren()));
                                }
                        }
                }
                return menus;
        } 
        
        public static  class CoreMenuOwnerTreeComparator implements Comparator<CoreMenuOwnerTree> {
                @Override
                public int compare(CoreMenuOwnerTree o1, CoreMenuOwnerTree o2) {
                        Integer sourceSortNum = o1.getMenu().getSortNum();
                        Integer targetSortNum = o2.getMenu().getSortNum();
                        if (sourceSortNum.equals(targetSortNum)) {
                                return 0;
                        }
                        return sourceSortNum < targetSortNum ? -1 : 1;
                }
        }
        

        /**
         * @return the name
         */
        public String getName() {
                if (menu != null) {
                        return menu.getMenuName();
                }
                return name;
        }

        public Boolean getHidden() {
                if (menu != null) {
                        return menu.getHidden();
                }
                return false;
        }

        /**
         * @param name
         *                the name to set
         */
        public void setName(String name) {
                this.name = name;
        }

        public Integer getId() {
                return id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public List<CoreMenuOwnerTree> getChildren() {
                return children;
        }

        public CoreMenu getMenu() {
                return menu;
        }

        public void setMenu(CoreMenu menu) {
                this.menu = menu;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        public void setChildren(List<CoreMenuOwnerTree> children) {
                this.children = children;
        }

        // public Boolean getAssigned() {
        // return assigned;
        // }

        public Boolean getCanAssign() {
                return canAssign;
        }

        public Boolean getHasPerm() {
                return hasPerm;
        }

        // public void setAssigned(Boolean assigned) {
        // this.assigned = assigned;
        // }

        public void setCanAssign(Boolean canAssign) {
                this.canAssign = canAssign;
        }

        public void setHasPerm(Boolean hasPerm) {
                this.hasPerm = hasPerm;
        }

}
