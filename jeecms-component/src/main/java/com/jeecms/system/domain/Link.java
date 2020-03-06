/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractSortDomain;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.resource.domain.ResourcesSpaceData;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 友情链接实体类
 *
 * @author wulongwei
 * @version 1.0
 * @date 2019年4月9日 上午10:04:53
 */
@Entity
@Table(name = "jc_sys_link")
public class Link extends AbstractSortDomain<Integer> implements Serializable, IBaseSite {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 名称
	 */
	private String linkName;
	/**
	 * URL
	 */
	private String linkUrl;
	/**
	 * Logo(资源库ID)
	 */
	private Integer linkLogo;
	/**
	 * 是否启用（0-否 1-是）
	 */
	private Boolean isEnable;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 友情链接分类id
	 */
	private Integer linkTypeId;
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 资源数据对象
	 **/
	private ResourcesSpaceData resourcesSpaceData;
	/**
	 * 友情链接分类
	 */
	private SysLinkType linkType;
	/**
	 * 友情链接分类名称
	 */
	private String linkTypeName;


	@Id
	@TableGenerator(name = "jc_sys_link", pkColumnValue = "jc_sys_link", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_link")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "link_name")
	@NotNull
	@Length(max = 150)
	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	@Column(name = "link_url")
	@NotNull
	@Length(max = 150)
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Column(name = "link_logo")
	public Integer getLinkLogo() {
		return linkLogo;
	}

	public void setLinkLogo(Integer linkLogo) {
		this.linkLogo = linkLogo;
	}

	@Column(name = "is_enable")
	@NotNull
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "remark")
	@Length(max = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "link_type_id", nullable = false, length = 11)
	@NotNull
	public Integer getLinkTypeId() {
		return linkTypeId;
	}

	public void setLinkTypeId(Integer linkTypeId) {
		this.linkTypeId = linkTypeId;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	@Override
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_logo", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_type_id", referencedColumnName = "id", insertable = false, updatable = false)
	public SysLinkType getLinkType() {
		return linkType;
	}

	public void setLinkType(SysLinkType linkType) {
		this.linkType = linkType;
	}

	@Transient
	public String getLinkTypeName() {
		if (getLinkType() != null) {
			linkTypeName = getLinkType().getTypeName();
		}
		return linkTypeName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Link)) {
			return false;
		}

		Link link = (Link) o;

		if (getId() != null ? !getId().equals(link.getId()) :
				link.getId() != null) {
			return false;
		}
		if (getLinkName() != null ? !getLinkName().equals(link.getLinkName()) :
				link.getLinkName() != null) {
			return false;
		}
		if (getLinkUrl() != null ? !getLinkUrl().equals(link.getLinkUrl()) :
				link.getLinkUrl() != null) {
			return false;
		}
		if (getLinkLogo() != null ? !getLinkLogo().equals(link.getLinkLogo()) :
				link.getLinkLogo() != null) {
			return false;
		}
		if (getIsEnable() != null ? !getIsEnable().equals(link.getIsEnable()) :
				link.getIsEnable() != null) {
			return false;
		}
		if (getRemark() != null ? !getRemark().equals(link.getRemark()) :
				link.getRemark() != null) {
			return false;
		}
		if (getLinkTypeId() != null ? !getLinkTypeId().equals(link.getLinkTypeId()) :
				link.getLinkTypeId() != null) {
			return false;
		}
		if (getSiteId() != null ? !getSiteId().equals(link.getSiteId()) :
				link.getSiteId() != null) {
			return false;
		}
		if (getResourcesSpaceData() != null ? !getResourcesSpaceData().equals(link.getResourcesSpaceData()) :
				link.getResourcesSpaceData() != null) {
			return false;
		}
		if (getLinkType() != null ? !getLinkType().equals(link.getLinkType()) :
				link.getLinkType() != null) {
			return false;
		}
		return getLinkTypeName() != null ? getLinkTypeName().equals(link.getLinkTypeName()) : link.getLinkTypeName() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getLinkName() != null ? getLinkName().hashCode() : 0);
		result = 31 * result + (getLinkUrl() != null ? getLinkUrl().hashCode() : 0);
		result = 31 * result + (getLinkLogo() != null ? getLinkLogo().hashCode() : 0);
		result = 31 * result + (getIsEnable() != null ? getIsEnable().hashCode() : 0);
		result = 31 * result + (getRemark() != null ? getRemark().hashCode() : 0);
		result = 31 * result + (getLinkTypeId() != null ? getLinkTypeId().hashCode() : 0);
		result = 31 * result + (getSiteId() != null ? getSiteId().hashCode() : 0);
		result = 31 * result + (getResourcesSpaceData() != null ? getResourcesSpaceData().hashCode() : 0);
		result = 31 * result + (getLinkType() != null ? getLinkType().hashCode() : 0);
		result = 31 * result + (getLinkTypeName() != null ? getLinkTypeName().hashCode() : 0);
		return result;
	}
}
