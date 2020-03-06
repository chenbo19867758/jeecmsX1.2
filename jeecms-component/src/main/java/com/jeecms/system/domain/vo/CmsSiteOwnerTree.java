/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.*;
import java.util.stream.Collectors;

import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.CmsSiteAgent;
import com.jeecms.system.domain.vo.CmsDataPermVo.SiteMap;

/**
 * 站群权限树
 * @author: tom
 * @date: 2019年4月27日 下午4:58:44
 */
public class CmsSiteOwnerTree {
        CmsSite site;
        Integer id;
        String name;
        Integer parentId;
        List<CmsSiteOwnerTree> children;
        //Boolean assigned;
        Boolean canAssign;
        Boolean hasPerm;

        /**
         * 站群权限集合转换成带树形结构数据
         * @Title: getChildTree
         * @param sitePerms 站群权限
         * @return List 
         */
        public static List<CmsSiteOwnerTree> getChildTree(Collection<SiteMap> sitePerms) {
                List<CmsSiteOwnerTree> result = new ArrayList<CmsSiteOwnerTree>();
                if (null == sitePerms || sitePerms.size() == 0) {
                        return result;
                }
                List<CmsSite> sites = sitePerms.stream().map(perm -> perm.getSite()).collect(Collectors.toList());
                sites = CmsSiteAgent.sortBySortNumAndChild(sites);
                Map<Integer, SiteMap> sitePermMap = sitePerms.stream().collect(Collectors.toMap(SiteMap::getSiteId,perm -> perm));
                if (sites != null && !sites.isEmpty()) {
                        CmsSite site = sites.iterator().next();
                        Integer parentId = null;
                        if (site != null) {
                                parentId = site.getParentId();
                        }
                        List<CmsSiteOwnerTree> dataSource = new ArrayList<>();
                        Map<Integer, CmsSiteOwnerTree> hashDatas = new HashMap<>(sites.size());
                        for (CmsSite t : sites) {
                                CmsSiteOwnerTree st = new CmsSiteOwnerTree();
                                st.setId(t.getId());
                                st.setParentId(t.getParentId());
                                st.setSite(t);
                                st.setName(t.getName());
                                SiteMap siteMap = sitePermMap.get(t.getId());
                                st.setCanAssign(siteMap.getCanAssign());
                                st.setHasPerm(siteMap.getHasPerm());
                                // 没有子节点则过滤childs
                                long count = sites.stream().filter(c -> null != c.getParentId()
                                                && ((Integer) t.getId()).intValue() == c.getParentId().intValue())
                                                .count();
                                if (count > 0) {
                                        st.setChildren(new ArrayList<CmsSiteOwnerTree>());
                                }
                                dataSource.add(st);
                                hashDatas.put(t.getId(), st);
                        }
                        sites.clear();

                        // 遍历菜单集合
                        for (int i = 0; i < dataSource.size(); i++) {
                                // 当前节点
                                CmsSiteOwnerTree json = (CmsSiteOwnerTree) dataSource.get(i);
                                // 当前的父节点
                                CmsSiteOwnerTree hashObject = hashDatas.get(json.getParentId());

                                if (hashObject != null) {
                                        // 表示当前节点为子节点
                                        hashObject.getChildren().add(json);
                                } else if (null == json.getParentId()
                                                || parentId.intValue() == ((Integer) json.getParentId())) {
                                        // parentId为null和获取匹配parentId的节点(生成某节点的子节点树时需要用到)
                                        result.add(json);
                                }
                        }
                }
                return result;
        }

        /** 按照站点排序值，站点创建时间排序 **/
        public static List<CmsSiteOwnerTree> sortBySortNumAndChild(List<CmsSiteOwnerTree> sites) {
                if (sites != null && sites.size() > 0) {
                        sites = sites.stream()
                                .sorted((Sub1, Sub2) -> Sub1.getSite().getSortNum().compareTo(Sub2.getSite().getSortNum()))
                                .sorted((Sub1, Sub2) -> Sub1.getSite().getCreateTime().compareTo(Sub2.getSite().getCreateTime()))
                                .collect(Collectors.toList());
                        for (CmsSiteOwnerTree s : sites) {
                                if (s.getChildren() != null && s.getChildren().size() > 0) {
                                        List<CmsSiteOwnerTree> childrens = s.getChildren();
                                        childrens = childrens.stream()
                                                .sorted((Sub1, Sub2) -> Sub1.getSite().getSortNum().compareTo(Sub2.getSite().getSortNum()))
                                                .sorted((Sub1, Sub2) -> Sub1.getSite().getCreateTime().compareTo(Sub2.getSite().getCreateTime()))
                                                .collect(Collectors.toList());
                                        s.setChildren(childrens);
                                }
                        }
                }
                return sites;
        }


        public CmsSite getSite() {
                return site;
        }

        public Integer getId() {
                return id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public List<CmsSiteOwnerTree> getChildren() {
                return children;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        public void setChildren(List<CmsSiteOwnerTree> children) {
                this.children = children;
        }

//        public Boolean getAssigned() {
//                return assigned;
//        }

        public Boolean getCanAssign() {
                return canAssign;
        }

        public Boolean getHasPerm() {
                return hasPerm;
        }

//        public void setAssigned(Boolean assigned) {
//                this.assigned = assigned;
//        }

        public void setCanAssign(Boolean canAssign) {
                this.canAssign = canAssign;
        }

        public void setHasPerm(Boolean hasPerm) {
                this.hasPerm = hasPerm;
        }

        /**
         * @return the name
         */
        public String getName() {
                return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
                this.name = name;
        }
        
        

}
