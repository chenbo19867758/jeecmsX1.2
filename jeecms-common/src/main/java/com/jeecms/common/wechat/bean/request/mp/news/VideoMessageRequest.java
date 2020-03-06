package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 视频消息
 * 
 * @author wulongwei
 * @date 2018年7月30日
 */
@XStreamAlias("xml")
public class VideoMessageRequest extends BaseMessage {

	@XStreamAlias("Video")
	private Video video;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public static class Video {
		/** 通过素材管理中的接口上传多媒体文件，得到的id */
		@XStreamAlias("MediaId")
		private String mediaId;

		/** 视频消息的标题 */
		@XStreamAlias("Title")
		private String title;

		/** 视频消息的描述 */
		@XStreamAlias("Description")
		private String description;

		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}

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

	}
}
