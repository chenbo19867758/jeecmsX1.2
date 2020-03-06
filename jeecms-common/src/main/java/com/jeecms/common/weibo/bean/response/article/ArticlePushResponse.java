/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response.article;

import com.jeecms.common.weibo.bean.response.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 推送头条文章响应
 * 
 * @author: ljw
 * @date: 2019年6月18日 下午4:38:25
 */
public class ArticlePushResponse extends BaseResponse {

	/** 返回码，成功返回100000，失败返回其他 **/
	private Integer code;
	/** 默认为空，code不为100000返回错误信息，错误描述详见错误对照表 **/
	private String msg;
	/** 结果数据，成功时返回，失败返回空 **/
	private Data data;
	
	public ArticlePushResponse() {
		super();
	}

	public class Data {

		/** 对象id **/
		@XStreamAlias("object_id")
		private String objectId;
		/** 文章url **/
		private String url;
		/** 短微博对象id **/
		private Long mid;

		public Data() {
		}

		public String getObjectId() {
			return objectId;
		}

		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Long getMid() {
			return mid;
		}

		public void setMid(Long mid) {
			this.mid = mid;
		}

	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
