/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractTreeDomain;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 资源空间实体
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/05/23 17:02
 */
@Entity
@Table(name = "jc_sys_resources_space")
public class ResourcesSpace extends AbstractTreeDomain<ResourcesSpace, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 1-未分享
	 */
	public static final Integer NOT_SHARED = 1;

	/**
	 * 2-下属资源被分享
	 */
	public static final Integer CHILD_SHARED = 2;

	/**
	 * 3-已分享
	 */
	public static final Integer SHARED = 3;
	/**
	 * 主键值
	 */
	private Integer id;
	/**
	 * 资源类型名称
	 */
	private String name;
	/**
	 * 共享状态（1-未分享   2-下属资源被分享  3-已分享）
	 */
	private Integer isShare;
	/**
	 * 资源空间所属会员id
	 */
	private Integer userId;
	/**
	 * 资源空间所属会员对象
	 */
	private CoreUser user;

	private List<ResourcesSpaceData> datas;

	private List<CoreUser> users;

	private String userName;

	public ResourcesSpace() {

	}

	@Id
	@TableGenerator(name = "jc_sys_resources_space",
			pkColumnValue = "jc_sys_resources_space", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_resources_space")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "srs_name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "is_share", nullable = false, length = 6)
	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	@Column(name = "user_id", nullable = true, length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "space", orphanRemoval = true)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<ResourcesSpaceData> getDatas() {
		return datas;
	}

	public void setDatas(List<ResourcesSpaceData> datas) {
		this.datas = datas;
	}

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "jc_tr_resource_space_user", joinColumns = @JoinColumn(name = "resource_space_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	public List<CoreUser> getUsers() {
		return users;
	}

	public void setUsers(List<CoreUser> users) {
		this.users = users;
	}

	/**
	 * 判断是否为根节点.  true表示是,false表示否
	 */
	@Transient
	public Boolean getIsChild() {
		//判断是否为根节点
		if (super.getChildren().size() == 0) {
			return true;
		}
		return false;
	}

	@Transient
	public String getUserName() {
		if (getUser() != null) {
			userName = getUser().getUsername();
		}
		return userName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ResourcesSpace)) {
			return false;
		}

		ResourcesSpace that = (ResourcesSpace) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
			return false;
		}
		if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
			return false;
		}
		if (getIsShare() != null ? !getIsShare().equals(that.getIsShare()) : that.getIsShare() != null) {
			return false;
		}
		if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
			return false;
		}
		if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) {
			return false;
		}
		return getDatas() != null ? getDatas().equals(that.getDatas()) : that.getDatas() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		result = 31 * result + (getIsShare() != null ? getIsShare().hashCode() : 0);
		result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
		result = 31 * result + (getDatas() != null ? getDatas().hashCode() : 0);
		return result;
	}
}