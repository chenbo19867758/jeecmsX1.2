/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.vo.CmsDataPermVo.SiteMap;

/**
 * 组织站群类权限Vo
 * 
 * @author: tom
 * @date: 2019年4月28日 下午4:50:39
 */
public class OrgOwnerSitePermVo implements Serializable {
        private static final long serialVersionUID = 5851126891390130467L;
        /** 组织 */
        CmsOrg org;
        /** 角色 */
        CoreRole role;
        /** 组织和角色操作 */
        SiteMap siteOwner;
        // List<RoleOwnerSitePermVo> roleOwners;
        Integer id;
        Integer parentId;
        /** 子组织权限 */
        List<OrgOwnerSitePermVo> children;

        /**
         * 获取组织名称
         * 
         * @Title: getOrgName
         * @return: String
         */
        public String getOrgName() {
                if (getOrg() != null) {
                        return getOrg().getName();
                }
                return null;
        }

        /**
         * 获取组织ID
         * 
         * @Title: getOrgId
         * @return: Integer
         */
        public Integer getOrgId() {
                if (getOrg() != null) {
                        return getOrg().getId();
                }
                return null;
        }

        /**
         * 获取角色名称
         * 
         * @Title: getRoleName
         * @return: String
         */
        public String getRoleName() {
                if (getRole() != null) {
                        return getRole().getRoleName();
                }
                return null;
        }

        /**
         * 获取角色ID
         * 
         * @Title: getRoleId
         * @return: Integer
         */
        public Integer getRoleId() {
                if (getRole() != null) {
                        return getRole().getId();
                }
                return null;
        }

        public OrgOwnerSitePermVo() {
                super();
        }

        public OrgOwnerSitePermVo(CoreRole role, SiteMap siteOwner) {
                super();
                this.role = role;
                this.siteOwner = siteOwner;
        }

        public OrgOwnerSitePermVo(CmsOrg org, SiteMap siteOwner) {
                super();
                this.org = org;
                this.siteOwner = siteOwner;
        }

        /**
         * 权限集合转换成带树形结构数据
         * 
         * @Title: getChildTree
         * @param perms
         *                菜单权限
         * @return List
         */
        public static List<OrgOwnerSitePermVo> getChildTree(Collection<OrgOwnerSitePermVo> perms) {
                List<OrgOwnerSitePermVo> result = new ArrayList<OrgOwnerSitePermVo>();
                if (null == perms || perms.size() == 0) {
                        return result;
                }
                List<CmsOrg> childs = perms.stream().map(perm -> perm.getOrg()).collect(Collectors.toList());
                Map<Integer, SiteMap> siteOwnerMap = perms.stream()
                                .collect(Collectors.toMap(OrgOwnerSitePermVo::getId, OrgOwnerSitePermVo::getSiteOwner));
                Map<Integer, OrgOwnerSitePermVo> rolePermMap = perms.stream()
                                .collect(Collectors.toMap(OrgOwnerSitePermVo::getId, perm -> perm));
                if (childs != null && !childs.isEmpty()) {
                        CmsOrg org = childs.iterator().next();
                        Integer parentId = null;
                        if (org != null) {
                                parentId = org.getParentId();
                        }
                        List<OrgOwnerSitePermVo> dataSource = new ArrayList<>();
                        Map<Integer, OrgOwnerSitePermVo> hashDatas = new HashMap<>(childs.size());
                        for (CmsOrg t : childs) {
                                OrgOwnerSitePermVo st = new OrgOwnerSitePermVo();
                                st.setId(t.getId());
                                st.setParentId(t.getParentId());
                                st.setOrg(t);
                                st.setSiteOwner(siteOwnerMap.get(t.getId()));
                                /** 子组织 */
                                List<OrgOwnerSitePermVo> children = new ArrayList<OrgOwnerSitePermVo>();
                                /** 所属角色 */
                                List<OrgOwnerSitePermVo> roleChildren = rolePermMap.get(t.getId()).getChildren();
                                if (roleChildren != null && roleChildren.size() > 0) {
                                        children.addAll(roleChildren);
                                }
                                // 没有子节点则过滤childs
                                long count = childs.stream().filter(c -> null != c.getParentId()
                                                && ((Integer) t.getId()).intValue() == c.getParentId().intValue())
                                                .count();
                                if (count > 0 || children.size() > 0) {
                                        st.setChildren(children);
                                }
                                dataSource.add(st);
                                hashDatas.put(t.getId(), st);
                        }
                        childs.clear();

                        // 遍历菜单集合
                        for (int i = 0; i < dataSource.size(); i++) {
                                // 当前节点
                                OrgOwnerSitePermVo json = (OrgOwnerSitePermVo) dataSource.get(i);
                                // 当前的父节点
                                OrgOwnerSitePermVo hashObject = hashDatas.get(json.getParentId());

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

        /**
         * @return the role
         */
        public CoreRole getRole() {
                return role;
        }

        /**
         * @param role
         *                the role to set
         */
        public void setRole(CoreRole role) {
                this.role = role;
        }

        public CmsOrg getOrg() {
                return org;
        }

        public Integer getId() {
                return id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public List<OrgOwnerSitePermVo> getChildren() {
                return children;
        }

        public void setOrg(CmsOrg org) {
                this.org = org;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        public void setChildren(List<OrgOwnerSitePermVo> children) {
                this.children = children;
        }

        public SiteMap getSiteOwner() {
                return siteOwner;
        }

        public void setSiteOwner(SiteMap siteOwner) {
                this.siteOwner = siteOwner;
        }

}
