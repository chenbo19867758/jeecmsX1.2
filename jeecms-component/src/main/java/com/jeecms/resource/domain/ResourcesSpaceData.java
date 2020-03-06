/*
@Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractSortDomain;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.util.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 资源空间数据表
 *
 * @author: chenming
 * @date: 2018年9月3日 下午5:39:30
 */
@Entity
@Table(name = "jc_sys_resources_space_data")
public class ResourcesSpaceData extends AbstractSortDomain<Integer> implements Serializable {

	private static final long serialVersionUID = 3339974069605000772L;

	/**
	 * 未分享
	 */
	public static final short STATUS_NOT_SHARED = 1;
	/**
	 * 已分享
	 */
	public static final short STATUS_SHARED = 2;

	public void init() {
		setRefCount(0);
		if (getSortNum() == null) {
			setSortNum(10);
		}
		if (getShareStatus() == null) {
			setShareStatus(STATUS_NOT_SHARED);
		}
		if (getDisplay()== null) {
			setDisplay(true);
		}
	}

	/**
	 * 是否本地资源
	 *
	 * @Title: isLocalRes
	 * @return: boolean
	 */
	@Transient
	public boolean isLocalRes() {
		if (getUploadFtp() != null) {
			return false;
		}
		if (getUploadOss() != null) {
			return false;
		}
		return true;
	}

	/**
	 * 主键值
	 */
	private Integer id;
	/**
	 * 文件类型(1图片 2视频 3音频 4附件)
	 */
	private Short resourceType;
	/**
	 * 资源被引用次数
	 */
	private Integer refCount;
	/**
	 * 资源别名
	 */
	private String alias;
	/**
	 * 资源尺寸：如图片100X300
	 */
	private String dimensions;
	/**
	 * 资源大小
	 */
	private Integer size;
	/**
	 * 图片路径
	 */
	private String url;
	/**
	 * 时长,单位：秒（仅限音频及视频）
	 */
	private Integer resourceDate;
	/**
	 * 共享状态（1-未分享 2-已分享）
	 */
	private Short shareStatus;
	/**
	 * 所属用户对象
	 **/
	private CoreUser user;
	/**
	 * 所属用户id
	 */
	private Integer userId;

	/**
	 * 共享时间
	 */
	private Date shareTime;

	/**
	 * 资源空间表
	 */
	private ResourcesSpace space;

	/**
	 * 后缀名（如.js）
	 */
	private String suffix;

	/**
	 * 大小（带单位）
	 */
	private String sizeUnit;

	/**
	 * 宽
	 */
	private String width;

	/**
	 * 高
	 */
	private String height;
	/**
	 * 是否展示资源库中
	 */
	private Boolean display;

	/**
	 * 资源空间表id
	 */
	private Integer storeResourcesSpaceId;

	private UploadFtp uploadFtp;
	private UploadOss uploadOss;
	/**
	 * 页眉图1 背景图2
	 */
	private Integer type;

	private List<CoreUser> users;

	public ResourcesSpaceData() {
	}

	@Id
	@TableGenerator(name = "jc_sys_resources_space_data", pkColumnValue = "jc_sys_resources_space_data", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_resources_space_data")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	@JSONField(serialize = false)
	public static Short getResourceType(String ext) {
		if (ImageUtils.isValidImageExt(ext)) {
			return ResourceType.RESOURCE_TYPE_IMAGE;
		} else if (FileUtils.isValidVideoExt(ext)) {
			return ResourceType.RESOURCE_TYPE_VIDEO;
		} else if (FileUtils.isValidAudioExt(ext)) {
			return ResourceType.RESOURCE_TYPE_AUDIO;
		} else if (FileUtils.isValidZipExt(ext)) {
			return ResourceType.RESOURCE_TYPE_ANNEX;
		} else {
			return ResourceType.RESOURCE_TYPE_ANNEX;
		}
	}

	@NotNull
	@Column(name = "resource_type", nullable = false)
	public Short getResourceType() {
		return resourceType;
	}

	public void setResourceType(Short resourceType) {
		this.resourceType = resourceType;
	}

	@NotNull
	@Column(name = "ref_count", nullable = false)
	public Integer getRefCount() {
		return this.refCount;
	}

	public void setRefCount(Integer refCount) {
		this.refCount = refCount;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "resource_alias", nullable = false, length = 150)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Length(max = 150)
	@Column(name = "resource_dimensions", length = 150)
	public String getDimensions() {
		return this.dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	@Column(name = "resource_size")
	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Column(name = "resource_date", nullable = true, length = 11)
	public Integer getResourceDate() {
		return resourceDate;
	}

	public void setResourceDate(Integer resourceDate) {
		this.resourceDate = resourceDate;
	}

	@Column(name = "share_status", nullable = false, length = 6)
	public Short getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(Short shareStatus) {
		this.shareStatus = shareStatus;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "resource_url", nullable = false, length = 150)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "share_time", nullable = true)
	public Date getShareTime() {
		return shareTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}

	@Column(name = "store_resources_space_id", nullable = true, length = 11)
	public Integer getStoreResourcesSpaceId() {
		return storeResourcesSpaceId;
	}

	public void setStoreResourcesSpaceId(Integer storeResourcesSpaceId) {
		this.storeResourcesSpaceId = storeResourcesSpaceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_resources_space_id", updatable = false, insertable = false)
	public ResourcesSpace getSpace() {
		return space;
	}

	public void setSpace(ResourcesSpace space) {
		this.space = space;
	}

	@Column(name = "user_id")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "jc_tr_resource_user", joinColumns = @JoinColumn(name = "resource_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@NotFound(action = NotFoundAction.IGNORE)
	public List<CoreUser> getUsers() {
		return users;
	}

	public void setUsers(List<CoreUser> users) {
		this.users = users;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_ftp_id")
	public UploadFtp getUploadFtp() {
		return uploadFtp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_oss_id")
	public UploadOss getUploadOss() {
		return uploadOss;
	}

	@Column(name = "type", nullable = true, length = 6)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setUploadFtp(UploadFtp uploadFtp) {
		this.uploadFtp = uploadFtp;
	}

	public void setUploadOss(UploadOss uploadOss) {
		this.uploadOss = uploadOss;
	}

	@Column(name = "suffix")
	public String getSuffix() {
		if (this.suffix == null && StringUtils.isNotBlank(getUrl())) {
			return url.substring(getUrl().lastIndexOf("."));
		}
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	

	/**
         * @return the display
         */
	@Column(name = "is_display")
        public Boolean getDisplay() {
                return display;
        }

        /**
         * @param display the display to set
         */
        public void setDisplay(Boolean display) {
                this.display = display;
        }

        @Transient
	public String getWidth() {
		if (StringUtils.isNotBlank(getDimensions()) && getDimensions().contains("*")) {
			width = getDimensions().split("\\*")[0];
		}
		return width;
	}

	@Transient
	public String getHeight() {
		if (StringUtils.isNotBlank(getDimensions()) && getDimensions().contains("*")) {
			height = getDimensions().split("\\*")[1];
		}
		return height;
	}

	/**
	 * 时长
	 * @return String
	 */
	@Transient
	public String getDuration() {
		if (getResourceDate() != null) {
			long diff = Math.abs(getResourceDate());
			long nd = 24 * 60 * 60;
			long nh = 60 * 60;
			long nm = 60;
			//获取相差的小时数
			long hour = diff % nd / nh;
			hour = Math.abs(hour);
			//获取相差的分钟数
			long min = diff % nd % nh / nm;
			min = Math.abs(min);
			//计算相差的秒数
			long sec = diff % nd % nh % nm;
			sec = Math.abs(sec);
			return (hour < 10 ? "0" + hour : hour) + ":"
					+ (min < 10 ? "0" + min : min) + ":"
					+ (sec < 10 ? "0" + sec : sec);
		} else {
			return "00:00:00";
		}
	}

	@Transient
	public String getSizeUnit() {
		// 定义GB的计算常量
		int gb = 1024 * 1024;
		// 定义MB的计算常量
		int mb = 1024;
		// 格式化小数
		DecimalFormat df = new DecimalFormat("0.00");
		String resultSize;
		if (size == null) {
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
		sizeUnit = resultSize;
		return sizeUnit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ResourcesSpaceData)) {
			return false;
		}

		ResourcesSpaceData spaceData = (ResourcesSpaceData) o;

		if (getId() != null ? !getId().equals(spaceData.getId()) : spaceData.getId() != null) {
			return false;
		}
		if (getResourceType() != null ? !getResourceType().equals(spaceData.getResourceType())
				: spaceData.getResourceType() != null) {
			return false;
		}
		if (getRefCount() != null ? !getRefCount().equals(spaceData.getRefCount())
				: spaceData.getRefCount() != null) {
			return false;
		}
		if (getAlias() != null ? !getAlias().equals(spaceData.getAlias()) : spaceData.getAlias() != null) {
			return false;
		}
		if (getDimensions() != null ? !getDimensions().equals(spaceData.getDimensions())
				: spaceData.getDimensions() != null) {
			return false;
		}
		if (getSize() != null ? !getSize().equals(spaceData.getSize()) : spaceData.getSize() != null) {
			return false;
		}
		if (getUrl() != null ? !getUrl().equals(spaceData.getUrl()) : spaceData.getUrl() != null) {
			return false;
		}
		if (getResourceDate() != null ? !getResourceDate().equals(spaceData.getResourceDate())
				: spaceData.getResourceDate() != null) {
			return false;
		}
		if (getShareStatus() != null ? !getShareStatus().equals(spaceData.getShareStatus())
				: spaceData.getShareStatus() != null) {
			return false;
		}
		if (getUser() != null ? !getUser().equals(spaceData.getUser()) : spaceData.getUser() != null) {
			return false;
		}
		if (getUserId() != null ? !getUserId().equals(spaceData.getUserId()) : spaceData.getUserId() != null) {
			return false;
		}
		if (getShareTime() != null ? !getShareTime().equals(spaceData.getShareTime())
				: spaceData.getShareTime() != null) {
			return false;
		}
		if (getSpace() != null ? !getSpace().equals(spaceData.getSpace()) : spaceData.getSpace() != null) {
			return false;
		}
		if (getSuffix() != null ? !getSuffix().equals(spaceData.getSuffix()) : spaceData.getSuffix() != null) {
			return false;
		}
		if (getSizeUnit() != null ? !getSizeUnit().equals(spaceData.getSizeUnit())
				: spaceData.getSizeUnit() != null) {
			return false;
		}
		if (getStoreResourcesSpaceId() != null
				? !getStoreResourcesSpaceId().equals(spaceData.getStoreResourcesSpaceId())
				: spaceData.getStoreResourcesSpaceId() != null) {
			return false;
		}
		return getUsers() != null ? getUsers().equals(spaceData.getUsers()) : spaceData.getUsers() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getResourceType() != null ? getResourceType().hashCode() : 0);
		result = 31 * result + (getRefCount() != null ? getRefCount().hashCode() : 0);
		result = 31 * result + (getAlias() != null ? getAlias().hashCode() : 0);
		result = 31 * result + (getDimensions() != null ? getDimensions().hashCode() : 0);
		result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
		result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
		result = 31 * result + (getResourceDate() != null ? getResourceDate().hashCode() : 0);
		result = 31 * result + (getShareStatus() != null ? getShareStatus().hashCode() : 0);
		result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
		result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
		result = 31 * result + (getShareTime() != null ? getShareTime().hashCode() : 0);
		result = 31 * result + (getSpace() != null ? getSpace().hashCode() : 0);
		result = 31 * result + (getSuffix() != null ? getSuffix().hashCode() : 0);
		result = 31 * result + (getSizeUnit() != null ? getSizeUnit().hashCode() : 0);
		result = 31 * result + (getStoreResourcesSpaceId() != null ? getStoreResourcesSpaceId().hashCode() : 0);
		result = 31 * result + (getUsers() != null ? getUsers().hashCode() : 0);
		return result;
	}
}