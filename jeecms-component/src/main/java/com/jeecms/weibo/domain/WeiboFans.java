/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.domain;

import java.util.Date;
import java.io.Serializable;
import com.jeecms.common.base.domain.AbstractDomain;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 微博粉丝
 * @author ljw
 * @version 1.0
 * @date 2019-06-18
 * 
 */
@Entity
@Table(name = "jc_weibo_fans")
public class WeiboFans extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	/** 粉丝所属账户uid */
	private Long uid;
	/** 用户uid */
	private String idstr;
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
	

	public WeiboFans() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_weibo_fans", pkColumnValue = "jc_weibo_fans", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_weibo_fans")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "u_uid", nullable = false, length = 20)
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	@Column(name = "fan_uid", nullable = false, length = 50)
	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
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

	@Column(name = "lang", nullable = true, length = 50)
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}