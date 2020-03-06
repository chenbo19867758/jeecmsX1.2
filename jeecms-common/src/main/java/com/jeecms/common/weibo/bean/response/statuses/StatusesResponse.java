/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response.statuses;

import java.util.List;

import com.jeecms.common.weibo.bean.response.BaseResponse;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微博响应
 * 
 * @author: ljw
 * @date: 2019年6月19日 下午2:47:36
 */
public class StatusesResponse extends BaseResponse {

	@XStreamAlias("previous_cursor")
	private Long previousCursor;
	@XStreamAlias("total_number")
	private Long totalNumber;
	@XStreamAlias("next_cursor")
	private Long nextCursor;
	private List<Statuses> statuses;

	public StatusesResponse() {
		super();
	}
	
	public class Statuses {
		/** 微博创建时间 **/
		@XStreamAlias("created_at")
		private String createdAt;
		/** 微博ID **/
		private Long id;
		/** 微博MID **/
		private Long mid;
		/** 字符串型的微博ID **/
		private String idstr;
		/** 微博信息内容 **/
		private String text;
		/** 微博来源 **/
		private String source;
		/** 是否已收藏，true：是，false：否 **/
		private Boolean favorited;
		/** 是否被截断，true：是，false：否 **/
		private Boolean truncated;
		/** （暂未支持）回复ID **/
		@XStreamAlias("in_reply_to_status_id")
		private String inReplyToStatusId;
		/** （暂未支持）回复人UID **/
		@XStreamAlias("in_reply_to_user_id")
		private String inReplyToUserId;
		/** （暂未支持）回复人昵称 **/
		@XStreamAlias("in_reply_to_screen_name")
		private String inReplyToScreenName;
		/** 缩略图片地址，没有时不返回此字段 **/
		@XStreamAlias("thumbnail_pic")
		private String thumbnailPic;
		/** 中等尺寸图片地址，没有时不返回此字段 **/
		@XStreamAlias("bmiddle_pic")
		private String bmiddlePic;
		/** 原始图片地址，没有时不返回此字段 **/
		@XStreamAlias("original_pic")
		private String originalPic;
		/** 地理信息字段 **/
		private Object geo;
		/** 微博作者的用户信息字段 **/
		@XStreamAlias("user")
		private WeiboUserResponse user;
		/** 被转发的原微博信息字段，当该微博为转发微博时返回 **/
		@XStreamAlias("retweeted_status")
		private Object retweetedStatus;
		/** 转发数 **/
		@XStreamAlias("reposts_count")
		private Integer repostsCount;
		/** 评论数 **/
		@XStreamAlias("comments_count")
		private Integer commentsCount;
		/** 表态数 **/
		@XStreamAlias("attitudes_count")
		private Integer attitudesCount;
		/** 暂未支持 **/
		private Integer mlevel;
		/**
		 * 微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
		 **/
		private Object visible;
		/**
		 * 微博配图ID。多图时返回多图ID，用来拼接图片url。用返回字段thumbnail_pic的地址配上该返回字段的图片ID，即可得到多个图片url。
		 **/
		@XStreamAlias("pic_ids")
		private Object picIds;
		/** 微博流内的推广微博ID **/
		private Object ad;

		public Statuses() {
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getMid() {
			return mid;
		}

		public void setMid(Long mid) {
			this.mid = mid;
		}

		public String getIdstr() {
			return idstr;
		}

		public void setIdstr(String idstr) {
			this.idstr = idstr;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public Boolean getFavorited() {
			return favorited;
		}

		public void setFavorited(Boolean favorited) {
			this.favorited = favorited;
		}

		public Boolean getTruncated() {
			return truncated;
		}

		public void setTruncated(Boolean truncated) {
			this.truncated = truncated;
		}

		public String getInReplyToStatusId() {
			return inReplyToStatusId;
		}

		public void setInReplyToStatusId(String inReplyToStatusId) {
			this.inReplyToStatusId = inReplyToStatusId;
		}

		public String getInReplyToUserId() {
			return inReplyToUserId;
		}

		public void setInReplyToUserId(String inReplyToUserId) {
			this.inReplyToUserId = inReplyToUserId;
		}

		public String getInReplyToScreenName() {
			return inReplyToScreenName;
		}

		public void setInReplyToScreenName(String inReplyToScreenName) {
			this.inReplyToScreenName = inReplyToScreenName;
		}

		public String getThumbnailPic() {
			return thumbnailPic;
		}

		public void setThumbnailPic(String thumbnailPic) {
			this.thumbnailPic = thumbnailPic;
		}

		public String getBmiddlePic() {
			return bmiddlePic;
		}

		public void setBmiddlePic(String bmiddlePic) {
			this.bmiddlePic = bmiddlePic;
		}

		public String getOriginalPic() {
			return originalPic;
		}

		public void setOriginalPic(String originalPic) {
			this.originalPic = originalPic;
		}

		public Object getGeo() {
			return geo;
		}

		public void setGeo(Object geo) {
			this.geo = geo;
		}

		public WeiboUserResponse getUser() {
			return user;
		}

		public void setUser(WeiboUserResponse user) {
			this.user = user;
		}

		public Object getRetweetedStatus() {
			return retweetedStatus;
		}

		public void setRetweetedStatus(Object retweetedStatus) {
			this.retweetedStatus = retweetedStatus;
		}

		public Integer getRepostsCount() {
			return repostsCount;
		}

		public void setRepostsCount(Integer repostsCount) {
			this.repostsCount = repostsCount;
		}

		public Integer getCommentsCount() {
			return commentsCount;
		}

		public void setCommentsCount(Integer commentsCount) {
			this.commentsCount = commentsCount;
		}

		public Integer getAttitudesCount() {
			return attitudesCount;
		}

		public void setAttitudesCount(Integer attitudesCount) {
			this.attitudesCount = attitudesCount;
		}

		public Integer getMlevel() {
			return mlevel;
		}

		public void setMlevel(Integer mlevel) {
			this.mlevel = mlevel;
		}

		public Object getVisible() {
			return visible;
		}

		public void setVisible(Object visible) {
			this.visible = visible;
		}

		public Object getPicIds() {
			return picIds;
		}

		public void setPicIds(Object picIds) {
			this.picIds = picIds;
		}

		public Object getAd() {
			return ad;
		}

		public void setAd(Object ad) {
			this.ad = ad;
		}

	}

	public Long getPreviousCursor() {
		return previousCursor;
	}

	public void setPreviousCursor(Long previousCursor) {
		this.previousCursor = previousCursor;
	}

	public Long getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Long totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Long getNextCursor() {
		return nextCursor;
	}

	public void setNextCursor(Long nextCursor) {
		this.nextCursor = nextCursor;
	}

	public List<Statuses> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<Statuses> statuses) {
		this.statuses = statuses;
	}

}
