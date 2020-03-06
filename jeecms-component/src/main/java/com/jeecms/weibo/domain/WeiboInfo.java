/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 微博授权账户
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-06-17
 */
@Entity
@Table(name = "jc_weibo_info")
public class WeiboInfo extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 所属站点 */
	private Integer siteId;
	/** 用户uid */
	private String uid;
	/** 用户昵称 */
	private String screenName;
	/** 友好显示名称 */
	private String name;
	/** 用户所在地 */
	private String location;
	/** 用户个人描述 */
	private String description;
	/** 用户博客地址 */
	private String blogUrl;
	/** 用户头像地址(中图，50*50) */
	private String profileImageUrl;
	/** 用户头像地址(大图，180*180) */
	private String avatarLarge;
	/** 用户头像地址(高清图) */
	private String avatarHd;
	/** 用户微博统一url地址 */
	private String profileUrl;
	/** 用户的个性化域名 */
	private String domain;
	/** 用户的微号 */
	private String weihao;
	/** 性别,m-男 f-女 n-位置 */
	private String gender;
	/** 粉丝数 */
	private Integer followersCount;
	/** 关注数 */
	private Integer friendsCount;
	/** 微博数 */
	private Integer statusesCount;
	/** 收藏数 */
	private Integer favouritesCount;
	/** 用户注册时间 */
	private Date createdAt;
	/** 是否允许所有人发给我私信,true:是 ，false:否 */
	private Boolean allowAllActMsg;
	/** 是否允许标识用户的地理位置 ,true:是 ，false:否 */
	private Boolean geoEnabled;
	/** 是否是微博认证用户，即加V用户 ,true:是 ，false:否 */
	private Boolean verified;
	/** 是否允许所有人对我的微博进行评论 ,true:是 ，false:否 */
	private Boolean allowAllComment;
	/** 认证原因 */
	private String verifiedReason;
	/** 用户当前的语言版本 ,zh-cn:简体中文 zh-tw:繁体中文, en:英语 */
	private String lang;
	/** 授权token */
	private String accessToken;
	/** 授权到期时间 */
	private Date authExpireTime;
	/** 是否设置管理员*/
	private Boolean isSetAdmin;
	/** 是否标红*/
	private Boolean red;
	/**关联管理员**/
	private List<CoreUser> coreUsers = new ArrayList<CoreUser>(10);
	
	public WeiboInfo() {}

	@Override
	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_weibo_info", pkColumnValue = "jc_weibo_info", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_weibo_info")
	public Integer getId() {
		return this.id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@Column(name = "u_uid", nullable = false, length = 50)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "screen_name", nullable = true, length = 150)
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@Column(name = "u_name", nullable = true, length = 150)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "u_location", nullable = true, length = 150)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "blog_url", nullable = true, length = 255)
	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	@Column(name = "profile_image_url", nullable = true, length = 500)
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Column(name = "avatar_large", nullable = true, length = 500)
	public String getAvatarLarge() {
		return avatarLarge;
	}

	public void setAvatarLarge(String avatarLarge) {
		this.avatarLarge = avatarLarge;
	}

	@Column(name = "avatar_hd", nullable = true, length = 500)
	public String getAvatarHd() {
		return avatarHd;
	}

	public void setAvatarHd(String avatarHd) {
		this.avatarHd = avatarHd;
	}

	@Column(name = "profile_url", nullable = true, length = 255)
	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	@Column(name = "u_domain", nullable = true, length = 255)
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "weihao", nullable = true, length = 150)
	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	@Column(name = "gender", nullable = true, length = 50)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "followers_count", nullable = true, length = 11)
	public Integer getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}

	@Column(name = "friends_count", nullable = true, length = 11)
	public Integer getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(Integer friendsCount) {
		this.friendsCount = friendsCount;
	}

	@Column(name = "statuses_count", nullable = true, length = 11)
	public Integer getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(Integer statusesCount) {
		this.statusesCount = statusesCount;
	}

	@Column(name = "favourites_count", nullable = true, length = 11)
	public Integer getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(Integer favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	@Column(name = "created_at", nullable = true, length = 19)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "allow_all_act_msg", nullable = true, length = 13)
	public Boolean getAllowAllActMsg() {
		return allowAllActMsg;
	}

	public void setAllowAllActMsg(Boolean allowAllActMsg) {
		this.allowAllActMsg = allowAllActMsg;
	}

	@Column(name = "geo_enabled", nullable = true, length = 13)
	public Boolean getGeoEnabled() {
		return geoEnabled;
	}

	public void setGeoEnabled(Boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}

	@Column(name = "verified", nullable = true, length = 13)
	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	@Column(name = "allow_all_comment", nullable = true, length = 13)
	public Boolean getAllowAllComment() {
		return allowAllComment;
	}

	public void setAllowAllComment(Boolean allowAllComment) {
		this.allowAllComment = allowAllComment;
	}

	@Column(name = "verified_reason", nullable = true, length = 255)
	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	@Column(name = "u_lang", nullable = true, length = 50)
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Column(name = "access_token", nullable = false, length = 255)
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name = "auth_expire_time", nullable = false, length = 19)
	public Date getAuthExpireTime() {
		return authExpireTime;
	}

	public void setAuthExpireTime(Date authExpireTime) {
		this.authExpireTime = authExpireTime;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "jc_tr_weibo_user", joinColumns = @JoinColumn(name = "weibo_info_id"), 
	        inverseJoinColumns = @JoinColumn(name = "user_id"))
	public List<CoreUser> getCoreUsers() {
		return coreUsers;
	}

	public void setCoreUsers(List<CoreUser> coreUsers) {
		this.coreUsers = coreUsers;
	}
	
	@Column(name = "is_set_admin", nullable = false)
	public Boolean getIsSetAdmin() {
		return isSetAdmin;
	}

	public void setIsSetAdmin(Boolean isSetAdmin) {
		this.isSetAdmin = isSetAdmin;
	}
	
	/**
	 * 获取管理员名称
	* @Title: getAdmin 
	* @return
	 */
	@Transient
	public String getAdminNames() {
		StringBuilder builder = new StringBuilder();
		if (coreUsers.isEmpty()) {
			return "";
		}
		for (CoreUser coreUser : coreUsers) {
			builder.append(coreUser.getUsername()).append(",");
		}
		return builder.toString().substring(0,builder.toString().length() - 1);
	}
	
	/**
	 * 是否标红
	* @Title: getRed 
	* @return
	 */
	@Transient
	public Boolean getRed() {
		return red;
	}
	
	public void setRed(Boolean red) {
		this.red = red;
	}
	
	/**
	 * 剩余时间
	* @Title: getRed 
	* @return
	 */
	@Transient
	public String getResidueTime() {
		StringBuilder builder = new StringBuilder();
		// 剩余3天内标红提醒
		Long result = getAuthExpireTime().getTime() - new Date().getTime();
		if (result < 0) {
			builder.append("已到期");
			this.setRed(false);
			return builder.toString();
		}
		Long sum = result / (1000 * 60 * 60 * 24L);
		if (sum > 3) {
			builder.append("剩余" + sum + "天");
			this.setRed(false);
			return builder.toString();
		} else if (sum < 1) {
			// 如果小于一分钟
			if (result < (1000 * 60)) {
				builder.append("即将到期");
				return builder.toString();
			}
			// 如果小于一小时
			if (result < (1000 * 60 * 60)) {
				Long minute = result / (1000 * 60);
				builder.append("剩余" + minute + "分钟");
				return builder.toString();
			}
			// 如果小于一天
			if (result < (1000 * 60 * 60 * 24L)) {
				Long hour = result / (1000 * 60 * 60);
				builder.append("剩余" + hour + "小时");
				return builder.toString();
			}
			builder.append("剩余" + sum + "天");
			this.setRed(true);
			return builder.toString();
		} else {
			this.setRed(true);
			builder.append("剩余" + sum + "天");
			return builder.toString();
		}
	}

	
	
}