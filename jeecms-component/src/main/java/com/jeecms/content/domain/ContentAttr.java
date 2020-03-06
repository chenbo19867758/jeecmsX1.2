/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.CmsOrg;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 内容自定义属性表
 *
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Entity
@Table(name = "jc_content_attr")
public class ContentAttr extends AbstractIdDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 省份编码名称
	 */
	public static final String PROVINCE_CODE_NAME = "provinceCode";
	/**
	 * 城市编码名称
	 */
	public static final String CITY_CODE_NAME = "cityCode";
	/**
	 * 区县编码名称
	 */
	public static final String AREA_CODE_NAME = "areaCode";
	/**
	 * 详细地址名称
	 */
	public static final String ADDRESS_NAME = "address";

	private Integer id;
	/**
	 * 内容ID
	 */
	private Integer contentId;
	/**
	 * 扩展字段名
	 */
	private String attrName;
	/**
	 * 扩展字段值
	 */
	private String attrValue;

	private String attrType;
	/**
	 * 资源ID(单资源)
	 */
	private Integer resId;
	/**
	 * 组织
	 */
	private Integer orgId;
	/**
	 * 省份编码
	 */
	private String provinceCode;
	/**
	 * 城市编码
	 */
	private String cityCode;
	/**
	 * 区县编码
	 */
	private String areaCode;

	/**
	 * 内容对象
	 */
	private Content content;
	/**
	 * 多资源对象集合
	 */
	private List<ContentAttrRes> contentAttrRes;
	/**
	 * 资源对象
	 */
	private ResourcesSpaceData resourcesSpaceData;
	/**
	 * 组织对象
	 */
	private CmsOrg cmsOrg;
	/**
	 * 省级对象
	 */
	private Area province;
	/**
	 * 城市对象
	 */
	private Area city;
	/**
	 * 区级对象
	 */
	private Area area;

	private String value;

	@Transient
	public String getOrgName() {
		if (getCmsOrg() != null) {
			return getCmsOrg().getName();
		}
		return null;
	}

	@Transient
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/***
	 * 获取地址拼接(省市县详细地址)
	 *
	 * @Title: getAddress
	 * @return: String
	 */
	@Transient
	public String getAddress() {
		StringBuffer address = new StringBuffer("");
		if (getProvince() != null) {
			address.append(getProvince().getAreaName());
		}
		if (getCity() != null) {
			address.append(getCity().getAreaName());
		}
		if (getArea() != null) {
			address.append(getArea().getAreaName());
		}
		address.append(getAttrValue());
		return address.toString();
	}

	/**
	 * 获取省市拼接(省市)
	 *
	 * @Title: getCityArea
	 * @return: String
	 */
	@Transient
	public String getCityArea() {
		StringBuffer address = new StringBuffer("");
		if (getProvince() != null) {
			address.append(getProvince().getAreaName());
		}
		if (getCity() != null) {
			address.append(getCity().getAreaName());
		}
		return address.toString();
	}

	/**
	 * 获取资源地址
	 *
	 * @Title: getResUrl
	 * @return: String
	 */
	@Transient
	public String getResUrl() {
		if (getResourcesSpaceData() != null) {
			return getResourcesSpaceData().getUrl();
		}
		return null;
	}

	/**兼容之前v9调用方式*/
	@Transient
	public String getPath() {
		return getResUrl();
	}

	/**兼容之前v9调用方式*/
	@Transient
	public String getName() {
		return getResAlias();
	}
	/**
	 * 获取资源名称
	 *
	 * @Title: getResAlias
	 * @return: String
	 */
	@Transient
	public String getResAlias() {
		if (getResourcesSpaceData() != null) {
			return getResourcesSpaceData().getAlias();
		}
		return null;
	}

	/**
	 * 获取资源时长(时间秒)
	 *
	 * @Title: getResDuration
	 * @return: String
	 */
	@Transient
	public String getResDuration() {
		if (getResourcesSpaceData() != null) {
			return getResourcesSpaceData().getDuration();
		}
		return "00:00:00";
	}

	@Transient
	public String getFileSize() {
		Integer size = 0;
		if(getContentAttrRes() != null && getContentAttrRes().size() > 0) {
			for (ContentAttrRes contentAttrRe : getContentAttrRes()) {
				size += contentAttrRe.getFileSize();
			}
		}
		// 定义GB的计算常量
		int gb = 1024 * 1024;
		// 定义MB的计算常量
		int mb = 1024;
		// 格式化小数
		DecimalFormat df = new DecimalFormat("0.00");
		String resultSize;
		if (0 == size) {
			resultSize = "1KB";
		} else if (size / gb >= 1) {
			// 如果当前Byte的值大于等于1GB
			resultSize = df.format(size / (float) gb) + "GB";
		} else if (size / mb >= 1) {
			// 如果当前Byte的值大于等于1MB
			resultSize = df.format(size / (float) mb) + "MB";
		} else {
			resultSize = size + "KB";
		}
		return resultSize;
	}

	/**
	 * 获取省级名称
	 *
	 * @return 省级名称
	 */
	@Transient
	public String getProvinceName() {
		return getProvince() != null ? getProvince().getAreaName() : "";
	}

	/**
	 * 获取城市名称
	 *
	 * @return 城市名称
	 */
	@Transient
	public String getCityName() {
		return getCity() != null ? getCity().getAreaName() : "";
	}

	/**
	 * 获取区级名称
	 *
	 * @return 区级名称
	 */
	@Transient
	public String getAreaName() {
		return getArea() != null ? getArea().getAreaName() : "";
	}

	/**
	 * 获取资源对象别名
	 *
	 * @return 资源对象别名
	 */
	@Transient
	public String getAlias() {
		return getResourcesSpaceData() != null ? getResourcesSpaceData().getAlias() : "";
	}

	/**
	 * 获取资源对象类型（1图片 2视频 3音频 4附件)）
	 *
	 * @return 资源对象类型
	 */
	@Transient
	public Short getResourceType() {
		return getResourcesSpaceData() != null ? getResourcesSpaceData().getResourceType() : null;
	}

	/**
	 * 获取资源对象大小
	 *
	 * @return 资源对象大小
	 */
	@Transient
	public String getSizeUnit() {
		return getResourcesSpaceData() != null ? getResourcesSpaceData().getSizeUnit() : "";
	}

	/**
	 * 获取资源对象地址
	 *
	 * @return 资源对象地址
	 */
	@Transient
	public String getUrl() {
		return getResourcesSpaceData() != null ? getResourcesSpaceData().getUrl() : "";
	}

	/**
	 * 获得所有的资源id的List集合
	 */
	@Transient
	@JSONField(serialize = false)
	public static List<Integer> fetchIds(Collection<ContentAttr> attrs) {
		if (attrs == null) {
			return null;
		}
		List<Integer> ids = new ArrayList<Integer>();
		for (ContentAttr s : attrs) {
			for (ContentAttrRes res : s.getContentAttrRes()) {
				ids.add(res.getResId());
			}
		}
		return ids;
	}
	
	public ContentAttr() {
	}

	/**
	 * 用于初始化contentAttr参数时使用
	 */
	public ContentAttr(String attrName, String attrType) {
		super();
		this.attrName = attrName;
		this.attrType = attrType;
	}

	@Id
	@Column(name = "content_attr_id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_attr", pkColumnValue = "jc_content_attr",
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_attr")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "attr_name", nullable = false, length = 150)
	@NotBlank
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@Column(name = "attr_value", nullable = true, length = 3000)
	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	@Column(name = "attr_type", nullable = false, length = 50)
	@NotBlank
	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	@Column(name = "res_id", nullable = true, length = 11)
	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	@Column(name = "org_id", nullable = true, length = 11)
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Column(name = "province_code", nullable = true, length = 50)
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	@Column(name = "city_code", nullable = true, length = 50)
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Column(name = "area_code", nullable = true, length = 50)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	@OneToMany(mappedBy = "contentAttr")
	public List<ContentAttrRes> getContentAttrRes() {
		return contentAttrRes;
	}

	public void setContentAttrRes(List<ContentAttrRes> contentAttrRes) {
		this.contentAttrRes = contentAttrRes;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "res_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public ResourcesSpaceData getResourcesSpaceData() {
		return resourcesSpaceData;
	}

	public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
		this.resourcesSpaceData = resourcesSpaceData;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public CmsOrg getCmsOrg() {
		return cmsOrg;
	}

	public void setCmsOrg(CmsOrg cmsOrg) {
		this.cmsOrg = cmsOrg;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_code", referencedColumnName = "area_code", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public Area getProvince() {
		return province;
	}

	public void setProvince(Area province) {
		this.province = province;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_code", referencedColumnName = "area_code", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public Area getCity() {
		return city;
	}

	public void setCity(Area city) {
		this.city = city;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_code", referencedColumnName = "area_code", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

}