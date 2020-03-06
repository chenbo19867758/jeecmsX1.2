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

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.vo.CmsDataPermVo.MiniDataUnit;

/**
 * 组织栏目类、站点类权限Vo
 * 
 * @author: tom
 * @date: 2019年4月28日 下午4:50:39
 */
public class OrgPermVo implements Serializable {
        private static final long serialVersionUID = 5851126891390130467L;
        /** 组织 */
        CmsOrg org;
        /** 角色 */
        CoreRole role;
        /** 组织操作、角色操作汇总 */
        //List<OrgRolePermVo> ops;
        List<MiniDataUnit> ops;
        Integer id;
        Integer parentId;
        /** 子组织权限 */
        List<OrgPermVo> children;
        
        

        public OrgPermVo() {
                super();
        }

        public OrgPermVo(CoreRole role, List<MiniDataUnit> ops) {
                super();
                this.role = role;
                this.ops = ops;
        }

        public OrgPermVo(CmsOrg org, List<MiniDataUnit> ops) {
                super();
                this.org = org;
                this.ops = ops;
        }

        /**
         * 权限集合转换成带树形结构数据
         * 
         * @Title: getChildTree
         * @param perms
         *                菜单权限
         * @return List
         */
        public static List<OrgPermVo> getChildTree(Collection<OrgPermVo> perms) {
                List<OrgPermVo> result = new ArrayList<OrgPermVo>();
                if (null == perms || perms.size() == 0) {
                        return result;
                }
                List<CmsOrg> childs = perms.stream().map(perm -> perm.getOrg()).collect(Collectors.toList());
                Map<Integer, List<MiniDataUnit>> orgOpsMap = perms.stream()
                                .collect(Collectors.toMap(OrgPermVo::getOrgId, OrgPermVo::getOps));
                Map<Integer, OrgPermVo> orgPermMap = perms.stream()
                                .collect(Collectors.toMap(OrgPermVo::getOrgId, perm -> perm));
                if (childs != null && !childs.isEmpty()) {
                        CmsOrg org = childs.iterator().next();
                        Integer parentId = null;
                        if (org != null) {
                                parentId = org.getParentId();
                        }
                        List<OrgPermVo> dataSource = new ArrayList<>();
                        Map<Integer, OrgPermVo> hashDatas = new HashMap<>(childs.size());
                        for (CmsOrg t : childs) {
                                OrgPermVo st = new OrgPermVo();
                                st.setId(t.getId());
                                st.setParentId(t.getParentId());
                                st.setOrg(t);
                                st.setOps(orgOpsMap.get(t.getId()));
                                /**子组织*/
                                List<OrgPermVo> children = new ArrayList<OrgPermVo>();
                                /**所属角色*/
                                List<OrgPermVo> roleChildren = orgPermMap.get(t.getId()).getChildren();
                                if(roleChildren!=null&&roleChildren.size()>0){
                                        children.addAll(roleChildren);
                                }
                                // 没有子节点则过滤childs
                                long count = childs.stream().filter(c -> null != c.getParentId()
                                                && ((Integer) t.getId()).intValue() == c.getParentId().intValue())
                                                .count();
                                if (count > 0||children.size()>0 ) {
                                        st.setChildren(children);
                                }
                                dataSource.add(st);
                                hashDatas.put(t.getId(), st);
                        }
                        childs.clear();

                        // 遍历菜单集合
                        for (int i = 0; i < dataSource.size(); i++) {
                                // 当前节点
                                OrgPermVo json = (OrgPermVo) dataSource.get(i);
                                // 当前的父节点
                                OrgPermVo hashObject = hashDatas.get(json.getParentId());

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

        @JSONField(serialize = false)
        public CmsOrg getOrg() {
                return org;
        }

        /**
         * 获取组织名称
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
         * @Title: getRoleId
         * @return: Integer
         */
        public Integer getRoleId() {
                if (getRole() != null) {
                        return getRole().getId();
                }
                return null;
        }
        
        public Integer getId() {
                return id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public List<OrgPermVo> getChildren() {
                return children;
        }
        
        

        /**
         * @return the role
         */
        @JSONField(serialize = false)
        public CoreRole getRole() {
                return role;
        }

        /**
         * @param role the role to set
         */
        public void setRole(CoreRole role) {
                this.role = role;
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

        public void setChildren(List<OrgPermVo> children) {
                this.children = children;
        }

        /**
         * @return the ops
         */
        public List<MiniDataUnit> getOps() {
                return ops;
        }

        /**
         * @param ops
         *                the ops to set
         */
        public void setOps(List<MiniDataUnit> ops) {
                this.ops = ops;
        }

}
