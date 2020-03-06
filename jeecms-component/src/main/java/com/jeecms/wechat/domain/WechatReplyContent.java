package com.jeecms.wechat.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 关键词回复内容
 * 
 * @author wulongwei
 * @version 1.0
 * @date 2018-08-08
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_wechat_reply_content")
public class WechatReplyContent extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 所属公众号appId */
	private String appId;
	/** 主键 */
	private Integer id;
	/** 消息类型（text-文本，news-图文 ，image-图片，voice-音乐，video-视频，music-音乐） */
	private String msgType;
	/** 规则名称 */
	private String ruleName;
	/** 排序 */
	private Integer sortNum;
	/** 素材ID */
	private Integer mediaId;
	/** 回复的消息内容 */
	private String content;
	/** 音乐标题 */
	private String title;
	/** 音乐描述 */
	private String description;
	/** 音乐链接 */
	private String musicUrl;
	/** 高质量音乐链接 */
	private String hqMusicUrll;
	/** 缩略图的媒体id */
	private Integer thumbMediaId;
	/** 是否启用(0-否 1-是) */
	private Boolean isEnable;
	/** 关键字 */
	private List<WechatReplyKeyword> wechatReplyKeywordList;
	/** 事件促发 */
	private WechatReplyClick wechatReplyClick;
	/** 素材 */
	private WechatMaterial wechatMaterial;

	public static final String DIGEST = "digest";
	public static final String THUMB_MEDIA_URL = "thumbMediaUrl";
	public static final String TITLE = "title";
	public static final String URL = "url";
	public static final String MEDIA_ID = "media_id";

	public WechatReplyContent() {
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_reply_content", pkColumnValue = "jc_wechat_reply_content", 
					initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_reply_content")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "msg_type", nullable = false, length = 50)
	@NotBlank
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "rule_name", nullable = false, length = 150)
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@Column(name = "sort_num", nullable = false, length = 11)
	@NotNull
	@Min(1)
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	@Column(name = "material_id", nullable = true, length = 11)
	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "content", nullable = true, length = 500)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "title", nullable = true, length = 150)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "music_url", nullable = true, length = 255)
	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	@Column(name = "hq_music_urll", nullable = true, length = 255)
	public String getHqMusicUrll() {
		return hqMusicUrll;
	}

	public void setHqMusicUrll(String hqMusicUrll) {
		this.hqMusicUrll = hqMusicUrll;
	}

	@Column(name = "thumb_media_id", nullable = true, length = 11)
	public Integer getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(Integer thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Column(name = "is_enable", nullable = true)
	@NotNull
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wechatReplyContent", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	public List<WechatReplyKeyword> getWechatReplyKeywordList() {
		return wechatReplyKeywordList;
	}

	public void setWechatReplyKeywordList(List<WechatReplyKeyword> wechatReplyKeywordList) {
		this.wechatReplyKeywordList = wechatReplyKeywordList;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "wechatReplyContent",cascade = CascadeType.ALL)
	public WechatReplyClick getWechatReplyClick() {
		return wechatReplyClick;
	}

	public void setWechatReplyClick(WechatReplyClick wechatReplyClick) {
		this.wechatReplyClick = wechatReplyClick;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "material_id", insertable = false, updatable = false)
	public WechatMaterial getWechatMaterial() {
		return wechatMaterial;
	}

	public void setWechatMaterial(WechatMaterial wechatMaterial) {
		this.wechatMaterial = wechatMaterial;
	}

	@Transient
	public String getKeyWord() {
		StringBuffer word = new StringBuffer();
		if (getWechatReplyKeywordList() != null && getWechatReplyKeywordList().size() > 0) {
			for (WechatReplyKeyword wechatReplyKeyword : wechatReplyKeywordList) {
				if (StringUtils.isNotBlank(wechatReplyKeyword.getWordkeyEq())) {
					word.append(wechatReplyKeyword.getWordkeyEq()+",");
				}
				if (StringUtils.isNotBlank(wechatReplyKeyword.getWordkeyLike())) {
					word.append(wechatReplyKeyword.getWordkeyLike()+",");
				}
			}
		}
		String wordStr = word.toString();
		if (StringUtils.isNotBlank(wordStr)) {
			return wordStr.substring(0, wordStr.length()-1);
		}
		return wordStr;
	}
}
