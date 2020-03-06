/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.api.mp.impl;

import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.MassManageService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.mass.ImageMassRequest;
import com.jeecms.common.wechat.bean.request.mp.mass.MassRequest;
import com.jeecms.common.wechat.bean.request.mp.mass.MpnewsMassRequest;
import com.jeecms.common.wechat.bean.request.mp.mass.MpvideoMassRequest;
import com.jeecms.common.wechat.bean.request.mp.mass.TextMassRequest;
import com.jeecms.common.wechat.bean.request.mp.mass.VoiceMassRequest;
import com.jeecms.common.wechat.bean.request.mp.mass.WxcardMassRequest;
import com.jeecms.common.wechat.bean.response.mp.user.MassResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;


/**
 * 群发接口实现类
 * @author: ljw
 * @date: 2018年8月2日 下午1:51:17
 */
@Service
public class MassManageServiceImpl implements MassManageService {

	/** 群发消息 **/
	public final String MESSAGE_URL = Const.DoMain.API_URI.concat("/cgi-bin/message/mass/sendall?access_token");

	public final String ACCESS_TOKEN = "access_token";

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public MassResponse sendMass(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		String messageType = massRequest.getMsgtype();
		switch (messageType) {
			// 群发文本信息
			case "text":
				MassResponse text = text(massRequest, validToken);
				return text;
			// 群发语音信息
			case "voice":
				MassResponse voice = voice(massRequest, validToken);
				return voice;
			// 群发图片信息
			case "image":
				MassResponse image = image(massRequest, validToken);
				return image;
			// 群发图文信息
			case "news":
				MassResponse mpnews = mpnews(massRequest, validToken);
				return mpnews;
			// 群发视频信息
			case "mpvideo":
				MassResponse mpvideo = mpvideo(massRequest, validToken);
				return mpvideo;
			default:
				break;
		}
		return null;
	}

	/**
	 * 群发文本消息
	 * 
	 * @param massRequest 请求
	 * @param validToken  令牌
	 * @return MassResponse 返回
	 * @throws GlobalException 异常
	 */
	public MassResponse text(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		TextMassRequest textMassRequest = new TextMassRequest();
		TextMassRequest.Filter filter = textMassRequest.new Filter();
		filter.setIsToAll(massRequest.getFilter().getIsToAll());
		filter.setTagId(massRequest.getFilter().getTagId());
		textMassRequest.setFilter(filter);
		TextMassRequest.Text text = textMassRequest.new Text();
		text.setContent(massRequest.getContent());
		textMassRequest.setText(text);
		textMassRequest.setMsgtype(massRequest.getMsgtype());
		MassResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params, 
				SerializeUtil.beanToJson(textMassRequest),
				MassResponse.class);
		return response;
	}

	/**
	 * 群发语音消息
	 * 
	 * @param massRequest 请求
	 * @param validToken  令牌
	 * @return MassResponse 返回
	 * @throws GlobalException 异常
	 */
	public MassResponse voice(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		VoiceMassRequest voiceMassRequest = new VoiceMassRequest();
		VoiceMassRequest.Filter filter = voiceMassRequest.new Filter();
		filter.setIsToAll(massRequest.getFilter().getIsToAll());
		filter.setTagId(massRequest.getFilter().getTagId());
		voiceMassRequest.setFilter(filter);
		voiceMassRequest.setMsgtype(massRequest.getMsgtype());
		VoiceMassRequest.Voice voice = voiceMassRequest.new Voice();
		voice.setMediaId(massRequest.getMediaId());
		voiceMassRequest.setVoice(voice);
		MassResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params, 
				SerializeUtil.beanToJson(voiceMassRequest),
				MassResponse.class);
		return response;
	}

	/**
	 * 群发图片消息
	 * 
	 * @param massRequest 请求
	 * @param validToken  令牌
	 * @return MassResponse 返回
	 * @throws GlobalException 异常
	 */
	public MassResponse image(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		ImageMassRequest imageMassRequest = new ImageMassRequest();
		ImageMassRequest.Filter filter = imageMassRequest.new Filter();
		filter.setIsToAll(massRequest.getFilter().getIsToAll());
		filter.setTagId(massRequest.getFilter().getTagId());
		imageMassRequest.setFilter(filter);
		imageMassRequest.setMsgtype(massRequest.getMsgtype());
		ImageMassRequest.Image image = imageMassRequest.new Image();
		image.setMediaId(massRequest.getMediaId());
		imageMassRequest.setImage(image);
		MassResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params, 
				SerializeUtil.beanToJson(imageMassRequest),
				MassResponse.class);
		return response;
	}

	/**
	 * 群发图文消息
	 * 
	 * @param massRequest 请求
	 * @param validToken  令牌
	 * @return MassResponse 返回
	 * @throws GlobalException 异常
	 */
	public MassResponse mpnews(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		MpnewsMassRequest mpnewsMassRequest = new MpnewsMassRequest();
		mpnewsMassRequest.setClientmsgid(new Date().getTime());
		MpnewsMassRequest.Filter filter = mpnewsMassRequest.new Filter();
		filter.setIsToAll(massRequest.getFilter().getIsToAll());
		filter.setTagId(massRequest.getFilter().getTagId());
		mpnewsMassRequest.setFilter(filter);
		mpnewsMassRequest.setMsgtype("mpnews");
		MpnewsMassRequest.Mpnews mpnews = mpnewsMassRequest.new Mpnews();
		mpnews.setMediaId(massRequest.getMediaId());
		mpnewsMassRequest.setMpnews(mpnews);
		MassResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params, 
				SerializeUtil.beanToJson(mpnewsMassRequest),
				MassResponse.class);
		return response;
	}

	/**
	 * 群发视频消息
	 * 
	 * @param massRequest 请求
	 * @param validToken  令牌
	 * @return MassResponse 返回
	 * @throws GlobalException 异常
	 */
	public MassResponse mpvideo(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		MpvideoMassRequest mpvideoMassRequest = new MpvideoMassRequest();
		MpvideoMassRequest.Filter filter = mpvideoMassRequest.new Filter();
		filter.setIsToAll(massRequest.getFilter().getIsToAll());
		filter.setTagId(massRequest.getFilter().getTagId());
		mpvideoMassRequest.setFilter(filter);
		mpvideoMassRequest.setMsgtype(massRequest.getMsgtype());
		MpvideoMassRequest.Mpvideo mpvideo = mpvideoMassRequest.new Mpvideo();
		mpvideo.setMediaId(massRequest.getMediaId());
		mpvideoMassRequest.setMpvideo(mpvideo);
		MassResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params, 
				SerializeUtil.beanToJson(mpvideoMassRequest),
				MassResponse.class);
		return response;
	}

	/**
	 * 群发卡卷消息 （可做扩展）
	 * 
	 * @param massRequest 请求
	 * @param validToken  令牌
	 * @return MassResponse 返回
	 * @throws GlobalException 异常
	 */
	public MassResponse wxcard(MassRequest massRequest, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		WxcardMassRequest wxcardMassRequest = new WxcardMassRequest();
		WxcardMassRequest.Filter filter = wxcardMassRequest.new Filter();
		filter.setIsToAll(massRequest.getFilter().getIsToAll());
		filter.setTagId(massRequest.getFilter().getTagId());
		wxcardMassRequest.setMsgtype(massRequest.getMsgtype());
		// WxcardMassRequest.Wxcard wxcard=wxcardMassRequest.new Wxcard();
		MassResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params, 
				SerializeUtil.beanToJson(wxcardMassRequest),
				MassResponse.class);
		return response;
	}
}
