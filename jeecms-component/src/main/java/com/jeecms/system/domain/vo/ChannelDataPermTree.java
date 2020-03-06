/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeecms.channel.domain.Channel;
import com.jeecms.system.domain.vo.CmsDataPermVo.ChannelUnit;
import com.jeecms.system.domain.vo.CmsDataPermVo.MiniDataUnit;

/**
 * 栏目数据权限树
 * 
 * @author: tom
 * @date: 2019年4月27日 下午4:58:44
 */
public class ChannelDataPermTree {
        Channel channel;
        Integer id;
        String name;
        Integer parentId;
        List<ChannelDataPermTree> children;
        List<MiniDataUnit> rowDatas;

        /**
         * 栏目数据权限集合转换成带树形结构数据
         * 
         * @Title: getChildTree
         * @param perms
         *                栏目数据权限
         * @return List
         */
        public static List<ChannelDataPermTree> getChildTree(Collection<ChannelUnit> perms) {
                List<ChannelDataPermTree> result = new ArrayList<ChannelDataPermTree>();
                if (null == perms || perms.size() == 0) {
                        return result;
                }
                List<Channel> childs = perms.stream().map(perm -> perm.getChannel()).collect(Collectors.toList());
                Map<Integer, List<MiniDataUnit>> map = perms.stream()
                                .collect(Collectors.toMap(ChannelUnit::getChannelId, ChannelUnit::getRowDatas));
                if (childs != null && !childs.isEmpty()) {
                        Channel site = childs.iterator().next();
                        Integer parentId = null;
                        if (site != null) {
                                parentId = site.getParentId();
                        }
                        List<ChannelDataPermTree> dataSource = new ArrayList<>();
                        Map<Integer, ChannelDataPermTree> hashDatas = new HashMap<>(childs.size());
                        for (Channel t : childs) {
                                ChannelDataPermTree st = new ChannelDataPermTree();
                                st.setId(t.getId());
                                st.setParentId(t.getParentId());
                                st.setChannel(t);
                                st.setName(t.getName());
                                st.setRowDatas(map.get(t.getId()));
                                // 没有子节点则过滤childs
                                long count = childs.stream().filter(c -> null != c.getParentId()
                                                && ((Integer) t.getId()).intValue() == c.getParentId().intValue())
                                                .count();
                                if (count > 0) {
                                        st.setChildren(new ArrayList<ChannelDataPermTree>());
                                }
                                dataSource.add(st);
                                hashDatas.put(t.getId(), st);
                        }
                        childs.clear();

                        // 遍历菜单集合
                        for (int i = 0; i < dataSource.size(); i++) {
                                // 当前节点
                                ChannelDataPermTree json = (ChannelDataPermTree) dataSource.get(i);
                                // 当前的父节点
                                ChannelDataPermTree hashObject = hashDatas.get(json.getParentId());

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

        public Integer getId() {
                return id;
        }

        public Integer getParentId() {
                return parentId;
        }

        public List<ChannelDataPermTree> getChildren() {
                return children;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public void setParentId(Integer parentId) {
                this.parentId = parentId;
        }

        public void setChildren(List<ChannelDataPermTree> children) {
                this.children = children;
        }

        public Channel getChannel() {
                return channel;
        }

        public List<MiniDataUnit> getRowDatas() {
                return rowDatas;
        }

        public void setChannel(Channel channel) {
                this.channel = channel;
        }

        public void setRowDatas(List<MiniDataUnit> rowDatas) {
                this.rowDatas = rowDatas;
        }

}
