/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import com.jeecms.auth.domain.CoreApi;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.CoreUser;

/**
 * 用户增强类
 * 
 * @author: tom
 * @date: 2019年8月14日 下午6:30:32
 */
public class CoreUserAgent implements Serializable {

        private static final long serialVersionUID = 3276705888832133327L;

        CoreUser user;

        public CoreUserAgent(CoreUser user) {
                super();
                this.user = user;
        }

        /**
         * 获取用户菜单（menuType不为null 只查询特定类型菜单）
         * 
         * @Title: getUserMenuVos
         * @param menuType
         * @return: List<SortMenuVO>
         */
        public List<SortMenuVO> getUserMenuVos(Short menuType) {
                List<SortMenuVO> sortMenus = new ArrayList<>();
                Map<Integer, SortMenuVO> hashDatas = new HashMap<>(500);
                // 这里没用TreeSet是因为不能解决删除节点问题.
                List<SortMenuVO> dataSource = new ArrayList<>();
                SortMenuVO menuVO = null;
                Set<CoreMenu> ownerMenus = user.getCloneOwnerMenus();
                Set<CoreMenu> filterOwnerMenus = new HashSet<CoreMenu>();
                if (menuType != null) {
                        filterOwnerMenus = ownerMenus.stream().filter(m -> m.getMenuType().equals(menuType))
                                        .collect(Collectors.toSet());
                } else {
                        filterOwnerMenus.addAll(ownerMenus);
                }
                for (CoreMenu menu : filterOwnerMenus) {
                        if (!hashDatas.containsKey(menu.getId())) {
                                menuVO = new SortMenuVO(menu);
                                menuVO.setChildren(new ArrayList<SortMenuVO>());
                                hashDatas.put(menu.getId(), menuVO);
                                dataSource.add(menuVO);
                        }
                }
                SortMenuVO.sortBySortNum(dataSource);
                for (SortMenuVO it : dataSource) {
                        // 当前的父节点
                        SortMenuVO hashNode = hashDatas.get(it.getParentId());

                        // 表示当前节点为子节点
                        if (hashNode != null) {
                                hashNode.getChildren().add(it);
                        } else { // 为null表示当前节点为根节点(父节点)
                                sortMenus.add(it);
                        }
                }
                SortMenuVO.sortListBySortAndChild(sortMenus);
                return sortMenus;
        }

        public Set<CoreApiVo> getUserApis() {
                Set<CoreApiVo> apis = new HashSet<CoreApiVo>();
                Map<Integer, SortMenuVO> hashDatas = new HashMap<>(500);
                // 这里没用TreeSet是因为不能解决删除节点问题.
                List<SortMenuVO> dataSource = new ArrayList<>();
                SortMenuVO menuVO = null;
                for (CoreMenu menu : user.getCloneOwnerMenus()) {
                        if (!hashDatas.containsKey(menu.getId())
                                        && CoreMenu.MENUTYPE_MENU == menu.getMenuType().shortValue()) {
                                menuVO = new SortMenuVO(menu);
                                menuVO.setChildren(new ArrayList<SortMenuVO>());
                                hashDatas.put(menu.getId(), menuVO);
                                dataSource.add(menuVO);
                        }
                        for (CoreApi api : menu.getApis()) {
                                apis.add(CoreApiVo.buildByApi(api));
                        }
                }
                return apis;
        }

}
