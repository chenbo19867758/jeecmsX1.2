package com.jeecms.common.wechat.bean.request.mp.news;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 语音消息
 * @author wulongwei
 * @date 2018年7月30日
 */
@XStreamAlias("xml") 
public class VoiceMessageRequest extends BaseMessage{
	@XStreamAlias("Voice") 
	private Voice voice;
	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}
	
	
	

}
