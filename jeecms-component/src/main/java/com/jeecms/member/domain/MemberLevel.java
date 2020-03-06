/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;

/**
 * 会员等级
 * @author: wulongwei
 * @date: 2019年4月15日 上午10:49:01
 */
@Entity
@Table(name = "jc_sys_user_level")
public class MemberLevel extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 全局唯一标识符 */
	private Integer id;
	/** 会员等级名称 */
	private String levelName;
	/** 等级积分范围最小值 */
	private Integer integralMin;
	/** 等级积分范围最大值 */
	private Integer integralMax;
	/** logo资源id */
	private Integer levelIcon;
	/** 备注 */
	private String remark;
	/** logo资源 */
	private ResourcesSpaceData logoResource;
	/** 会员集合 **/
	private List<CoreUser> users;

	public MemberLevel() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_user_level", pkColumnValue = "jc_sys_user_level", 
			initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_user_level")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "level_name", nullable = false, length = 100)
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@NotNull
	@Column(name = "integral_min", nullable = false, length = 6)
	public Integer getIntegralMin() {
		return integralMin;
	}

	public void setIntegralMin(Integer integralMin) {
		this.integralMin = integralMin;
	}

	@NotNull
	@Column(name = "integral_max", nullable = false, length = 6)
	public Integer getIntegralMax() {
		return integralMax;
	}

	public void setIntegralMax(Integer integralMax) {
		this.integralMax = integralMax;
	}

	@Column(name = "level_icon", nullable = false)
	public Integer getLevelIcon() {
		return levelIcon;
	}

	public void setLevelIcon(Integer levelIcon) {
		this.levelIcon = levelIcon;
	}

	/** 备注信息，分页查询，超过20字时，用...代替显示 */
	@Transient
	public String getRemarkInfo() {
		String str = remark;
		Integer length = 20;
		if (StringUtils.isNotBlank(str) && str.length() > length) {
			str = str.substring(0, length) + "...";
		}
		return str;
	}

	@Length(max = 150)
	@Column(name = "remark", nullable = false, length = 150)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_icon", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getLogoResource() {
		return logoResource;
	}

	public void setLogoResource(ResourcesSpaceData logoResource) {
		this.logoResource = logoResource;
	}

	@OneToMany(mappedBy = "userLevel", fetch = FetchType.LAZY)
	@Where(clause = " deleted_flag=0 ")
	public List<CoreUser> getUsers() {
		return users;
	}

	public void setUsers(List<CoreUser> users) {
		this.users = users;
	}

}