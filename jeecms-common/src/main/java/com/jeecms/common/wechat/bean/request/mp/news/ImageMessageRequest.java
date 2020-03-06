package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 图片消息
 * <p>Title:ReplyImage</p>
 * @author wulongwei
 * @date 2018年7月30日
 */
@XStreamAlias("xml") 
public class ImageMessageRequest extends BaseMessage{

	@XStreamAlias("Image")
	private Image image;
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public static class Image{
		
	
	/**通过素材管理中的接口上传多媒体文件，得到的id。*/
	@XStreamAlias("MediaId")
	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	}
	

}
