/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 会员组信息
 * 
 * @author: wulongwei
 * @date: 2019年4月15日 上午9:51:26
 */
@Entity
@Table(name = "jc_sys_user_group")
public class MemberGroup extends AbstractDomain<Integer> implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 主键 */
        private Integer id;
        /** 用户组名称 */
        private String groupName;
        /** 备注 */
        private String remark;
        /** 是否默认组 */
        private Boolean isDefault;
        /** 是否所有栏目浏览权限 */
        private Boolean isAllChannelView;
        /** 是否所有栏目投稿权限 */
        private Boolean isAllChannelContribute;
        /**栏目浏览权限ID集合**/
        private List<Integer> views;
        /**栏目投稿权限ID集合**/
        private List<Integer> contributes;
        
        /** 会员集合 **/
        private List<CoreUser> users;
        /**允许浏览的栏目*/
        private List<Channel> viewChannels;
        /**允许投稿的栏目*/
        private List<Channel> contributeChannels;

        @Override
        @Id
        @TableGenerator(name = "jc_sys_user_group", pkColumnValue = "jc_sys_user_group", 
        	initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_user_group")
        public Integer getId() {
                return id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "group_name")
        @NotNull
        @Length(max = 150)
        public String getGroupName() {
                return groupName;
        }

        public void setGroupName(String groupName) {
                this.groupName = groupName;
        }

        /** 备注信息，分页查询，超过20字时，用...代替显示 */
        @Transient
        public String getRemarkInfo() {
                String str = remark;
                final Integer length = 20;
                if (StringUtils.isNotBlank(str) && str.length() > length) {
                        str = str.substring(0, length) + "...";
                }
                return str;
        }

        @Column(name = "remark")
        @Length(max = 150)
        public String getRemark() {
                return remark;
        }

        public void setRemark(String remark) {
                this.remark = remark;
        }

        @Column(name = "is_default")
        @NotNull
        public Boolean getIsDefault() {
                return isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
                this.isDefault = isDefault;
        }

        @Column(name = "is_all_channel_view", nullable = false)
        public Boolean getIsAllChannelView() {
                return isAllChannelView;
        }

        @Column(name = "is_all_channel_contribute", nullable = false)
        public Boolean getIsAllChannelContribute() {
                return isAllChannelContribute;
        }

        public void setIsAllChannelView(Boolean isAllChannelView) {
                this.isAllChannelView = isAllChannelView;
        }

        public void setIsAllChannelContribute(Boolean isAllChannelContribute) {
                this.isAllChannelContribute = isAllChannelContribute;
        }

        @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY)
        @Where(clause = " deleted_flag=0 ")
        public List<CoreUser> getUsers() {
                return users;
        }

        public void setUsers(List<CoreUser> users) {
                this.users = users;
        }
        
        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_group_channel_view", joinColumns = @JoinColumn(name = "group_id"), 
                inverseJoinColumns = @JoinColumn(name = "channel_id"))
        public List<Channel> getViewChannels() {
                return viewChannels;
        }
        
        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_group_channel_contribute", joinColumns = @JoinColumn(name = "group_id"), 
                inverseJoinColumns = @JoinColumn(name = "channel_id"))
        public List<Channel> getContributeChannels() {
                return contributeChannels;
        }

        public void setViewChannels(List<Channel> viewChannels) {
                this.viewChannels = viewChannels;
        }
        
        public void setContributeChannels(List<Channel> contributeChannels) {
                this.contributeChannels = contributeChannels;
        }
      
	/** 查看栏目IDs **/    
	@Transient
	public List<Integer> getViews() {
		if (views == null) {
			views = new ArrayList<Integer>(10);
			
		}
		if (viewChannels != null) {
			List<Integer> sd = viewChannels.stream().map(Channel::getId).collect(Collectors.toList());
			views.addAll(sd);
		}
		return views;
	}

	public void setViews(List<Integer> views) {
		this.views = views;
	}

	/**投稿栏目IDs**/
	@Transient
	public List<Integer> getContributes() {
		if (contributes == null) {
			contributes = new ArrayList<Integer>(10);
		}
		if (contributeChannels != null) {
			List<Integer> sd = contributeChannels.stream().map(Channel::getId).collect(Collectors.toList());
			contributes.addAll(sd);
		}
		return contributes;
	}

	public void setContributes(List<Integer> contributes) {
		this.contributes = contributes;
	}
        
}
