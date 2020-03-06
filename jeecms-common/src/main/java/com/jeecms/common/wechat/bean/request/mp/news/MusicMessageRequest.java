package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 音乐消息
 * <p>
 * Title:MusicMessage
 * </p>
 * 
 * @author wulongwei
 * @date 2018年7月30日
 */
@XStreamAlias("xml") 
public class MusicMessageRequest extends BaseMessage{

	@XStreamAlias("Music")
	private Music music;

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	public static class Music {

		/** 音乐标题 */
		@XStreamAlias("Title")
		private String title;

		/** 音乐描述 */
		@XStreamAlias("Description")
		private String description;

		/** 音乐链接 */
		@XStreamAlias("MusicUrl")
		private String musicUrl;

		/** 高质量音乐链接，WIFI环境优先使用该链接播放音乐 */
		@XStreamAlias("HQMusicUrl")
		private String hQMusicUrl;

		/** 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id */
		@XStreamAlias("ThumbMediaId")
		private String thumbMediaId;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getMusicUrl() {
			return musicUrl;
		}

		public void setMusicUrl(String musicUrl) {
			this.musicUrl = musicUrl;
		}

		public String gethQMusicUrl() {
			return hQMusicUrl;
		}

		public void sethQMusicUrl(String hQMusicUrl) {
			this.hQMusicUrl = hQMusicUrl;
		}

		public String getThumbMediaId() {
			return thumbMediaId;
		}

		public void setThumbMediaId(String thumbMediaId) {
			this.thumbMediaId = thumbMediaId;
		}

	}
}
