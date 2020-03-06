package com.jeecms.common.wechat.bean.request.mp.message;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN
 * http请求方式: POST（请使用https协议）
 * @Description:卡卷消息发送对象；
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class WxcardMessageRequest {

	/**发送对象**/
	private String touser;
	/**卡卷对象**/
	private Wxcard wxcard;
	/**消息类型**/
	private String msgtype;
	
	public class Wxcard {
		
		private String cardId;

		private CardExt cardExt;
		
		public class CardExt {
			private String code;
			private String openid;
			private String timestamp;
			private String signature;
			public String getCode() {
				return code;
			}
			public void setCode(String code) {
				this.code = code;
			}
			public String getOpenid() {
				return openid;
			}
			public void setOpenid(String openid) {
				this.openid = openid;
			}
			public String getTimestamp() {
				return timestamp;
			}
			public void setTimestamp(String timestamp) {
				this.timestamp = timestamp;
			}
			public String getSignature() {
				return signature;
			}
			public void setSignature(String signature) {
				this.signature = signature;
			}			
		}

		public String getCardId() {
			return cardId;
		}

		public void setCardId(String cardId) {
			this.cardId = cardId;
		}

		public CardExt getCardExt() {
			return cardExt;
		}

		public void setCardExt(CardExt cardExt) {
			this.cardExt = cardExt;
		}
		
		
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public Wxcard getWxcard() {
		return wxcard;
	}

	public void setWxcard(Wxcard wxcard) {
		this.wxcard = wxcard;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
}
