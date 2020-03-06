package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:41:21
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
	 public class Voice{
			/**通过素材管理中的接口上传多媒体文件，得到的id*/
			@XStreamAlias("MediaId") 
			private String mediaId;

			public String getMediaId() {
				return mediaId;
			}

			public void setMediaId(String mediaId) {
				this.mediaId = mediaId;
			}
		}
