/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.bean.response.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.weibo.bean.response.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**   
 * 微博粉丝返回的数据
 * @author: ljw
 * @date:   2019年6月18日 上午10:04:23     
 */
public class WeiboFansResponse extends BaseResponse {

	/**微博粉丝集合**/
	private List<WeiboFansCommon> users = new ArrayList<WeiboFansCommon>(16);
	/**下一页游标**/
	@XStreamAlias("next_cursor")
	private Integer nextCursor;
	/**上一页游标**/
	@XStreamAlias("previous_cursor")
	private Integer previousCursor;
	/**总数**/
	@XStreamAlias("total_number")
	private Integer totalNumber;
	/**展示总数**/
	@XStreamAlias("display_total_number")
	private Integer displayTotalNumber;
	
	public WeiboFansResponse() {
		super();
	}
	
	public class WeiboFansCommon {
		
		/**用户UID**/
		@XStreamAlias("id")
		private Long weiboId;
		/**字符串型的用户UID**/
		private String idstr;
		/**	用户昵称**/	
		@XStreamAlias("screen_name")
		private String screenName;
		/**友好显示名称**/	
		private String name;
		/**用户所在省级ID**/	
		private String province;
		/**	用户所在城市ID**/
		private String city;
		/**	用户所在地**/
		private String location;
		/**	用户个人描述**/
		private String description;
		/**	用户博客地址**/
		@XStreamAlias("url")
		private String blogUrl;
		/**用户头像地址（中图），50×50像素**/
		@XStreamAlias("profile_image_url")
		private String profileImageUrl;
		/**用户的微博统一URL地址**/
		@XStreamAlias("profile_url")
		private String profileUrl;
		/**用户的个性化域名**/
		private String domain;
		/**	用户的微号**/
		private String weihao;
		/**性别，m：男、f：女、n：未知**/
		private String gender;
		/**粉丝数**/
		@XStreamAlias("followers_count")
		private Integer followersCount;
		/**关注数**/
		@XStreamAlias("friends_count")
		private Integer friendsCount;
		/**微博数**/
		@XStreamAlias("statuses_count")
		private Integer statusesCount;
		/**收藏数**/
		@XStreamAlias("favourites_count")
		private Integer favouritesCount;
		/**用户创建（注册）时间**/
		@XStreamAlias("created_at")
		private Date createdAt;
		/**暂未支持**/
		private Boolean following;
		/**是否允许所有人给我发私信，true：是，false：否**/
		@XStreamAlias("allow_all_act_msg")
		private Boolean allowAllActMsg;	
		/**是否允许标识用户的地理位置，true：是，false：否**/
		@XStreamAlias("geo_enabled")
		private Boolean geoEnabled;	
		/**是否是微博认证用户，即加V用户，true：是，false：否**/
		@XStreamAlias("verified")
		private Boolean verified;	
		/**暂未支持**/
		@XStreamAlias("verified_type")
		private Integer verifiedType;
		/**用户备注信息，只有在查询用户关系时才返回此字段**/
		private String remark;	
		/**用户的最近一条微博信息字段**/
		private Object status;	
		/**是否允许所有人对我的微博进行评论，true：是，false：否**/
		@XStreamAlias("allow_all_comment")
		private Boolean allowAllComment;
		/**用户头像地址（大图），180×180像素**/
		@XStreamAlias("avatar_large")
		private String avatarLarge;
		/**用户头像地址（高清），高清头像原图**/
		@XStreamAlias("avatar_hd")
		private String avatarHd;
		/**认证原因**/
		@XStreamAlias("verified_reason")
		private String verifiedReason;
		/**该用户是否关注当前登录用户，true：是，false：否**/
		@XStreamAlias("follow_me")
		private Boolean followMe;
		/**用户的在线状态，0：不在线、1：在线**/
		@XStreamAlias("online_status")
		private Integer onlineStatus;
		/**用户的互粉数**/
		@XStreamAlias("bi_followers_count")
		private Integer biFollowersCount;
		/**用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语**/
		private String lang;
		
		public WeiboFansCommon() {}

		public Long getWeiboId() {
			return weiboId;
		}

		public void setWeiboId(Long weiboId) {
			this.weiboId = weiboId;
		}

		public String getIdstr() {
			return idstr;
		}

		public void setIdstr(String idstr) {
			this.idstr = idstr;
		}

		public String getScreenName() {
			return screenName;
		}

		public void setScreenName(String screenName) {
			this.screenName = screenName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getBlogUrl() {
			return blogUrl;
		}

		public void setBlogUrl(String blogUrl) {
			this.blogUrl = blogUrl;
		}

		public String getProfileImageUrl() {
			return profileImageUrl;
		}

		public void setProfileImageUrl(String profileImageUrl) {
			this.profileImageUrl = profileImageUrl;
		}

		public String getProfileUrl() {
			return profileUrl;
		}

		public void setProfileUrl(String profileUrl) {
			this.profileUrl = profileUrl;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getWeihao() {
			return weihao;
		}

		public void setWeihao(String weihao) {
			this.weihao = weihao;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public Integer getFollowersCount() {
			return followersCount;
		}

		public void setFollowersCount(Integer followersCount) {
			this.followersCount = followersCount;
		}

		public Integer getFriendsCount() {
			return friendsCount;
		}

		public void setFriendsCount(Integer friendsCount) {
			this.friendsCount = friendsCount;
		}

		public Integer getStatusesCount() {
			return statusesCount;
		}

		public void setStatusesCount(Integer statusesCount) {
			this.statusesCount = statusesCount;
		}

		public Integer getFavouritesCount() {
			return favouritesCount;
		}

		public void setFavouritesCount(Integer favouritesCount) {
			this.favouritesCount = favouritesCount;
		}

		public Date getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = MyDateUtils.parseTimeDate(createdAt);
		}

		public Boolean getFollowing() {
			return following;
		}

		public void setFollowing(Boolean following) {
			this.following = following;
		}

		public Boolean getAllowAllActMsg() {
			return allowAllActMsg;
		}

		public void setAllowAllActMsg(Boolean allowAllActMsg) {
			this.allowAllActMsg = allowAllActMsg;
		}

		public Boolean getGeoEnabled() {
			return geoEnabled;
		}

		public void setGeoEnabled(Boolean geoEnabled) {
			this.geoEnabled = geoEnabled;
		}

		public Boolean getVerified() {
			return verified;
		}

		public void setVerified(Boolean verified) {
			this.verified = verified;
		}

		public Integer getVerifiedType() {
			return verifiedType;
		}

		public void setVerifiedType(Integer verifiedType) {
			this.verifiedType = verifiedType;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public Object getStatus() {
			return status;
		}

		public void setStatus(Object status) {
			this.status = status;
		}

		public Boolean getAllowAllComment() {
			return allowAllComment;
		}

		public void setAllowAllComment(Boolean allowAllComment) {
			this.allowAllComment = allowAllComment;
		}

		public String getAvatarLarge() {
			return avatarLarge;
		}

		public void setAvatarLarge(String avatarLarge) {
			this.avatarLarge = avatarLarge;
		}

		public String getAvatarHd() {
			return avatarHd;
		}

		public void setAvatarHd(String avatarHd) {
			this.avatarHd = avatarHd;
		}

		public String getVerifiedReason() {
			return verifiedReason;
		}

		public void setVerifiedReason(String verifiedReason) {
			this.verifiedReason = verifiedReason;
		}

		public Boolean getFollowMe() {
			return followMe;
		}

		public void setFollowMe(Boolean followMe) {
			this.followMe = followMe;
		}

		public Integer getOnlineStatus() {
			return onlineStatus;
		}

		public void setOnlineStatus(Integer onlineStatus) {
			this.onlineStatus = onlineStatus;
		}

		public Integer getBiFollowersCount() {
			return biFollowersCount;
		}

		public void setBiFollowersCount(Integer biFollowersCount) {
			this.biFollowersCount = biFollowersCount;
		}

		public String getLang() {
			return lang;
		}

		public void setLang(String lang) {
			this.lang = lang;
		}
	}

	public List<WeiboFansCommon> getUsers() {
		return users;
	}

	public void setUsers(List<WeiboFansCommon> users) {
		this.users = users;
	}

	public Integer getNextCursor() {
		return nextCursor;
	}

	public void setNextCursor(Integer nextCursor) {
		this.nextCursor = nextCursor;
	}

	public Integer getPreviousCursor() {
		return previousCursor;
	}

	public void setPreviousCursor(Integer previousCursor) {
		this.previousCursor = previousCursor;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getDisplayTotalNumber() {
		return displayTotalNumber;
	}

	public void setDisplayTotalNumber(Integer displayTotalNumber) {
		this.displayTotalNumber = displayTotalNumber;
	}
	
}
