package com.jeecms.common.wechat.api.mp.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.mp.NewsApiService;
import com.jeecms.common.wechat.bean.request.mp.news.MusicMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.news.ReceiveMessage;
import com.jeecms.common.wechat.bean.request.mp.news.TextMessageRequest;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 自动回复
 * <p>
 * Title:NewsApiServiceImpl
 * </p>
 * 
 * @author wulongwei
 * @date 2018年7月31日
 */
@Service
public class NewsApiServiceImpl implements NewsApiService {

	@Override
	public ResponseInfo messageReply(HttpServletRequest req) {
		ResponseInfo message = null;
		try {
			// 模仿数据库的数据(完全匹配)
			String[] array = new String[] { "你好", "可以", "活动", "优惠" }; 
			// 模仿数据库的数据(模糊匹配)
			String[] arrayData = new String[] { "我", "嗯", "同意", "没问题" }; 
			ReceiveMessage receiveMessage = new ReceiveMessage();
			SerializeUtil.xmlToBean(req.toString(), receiveMessage.getClass());
			String fromUserName = receiveMessage.getFromUserName();
			String toUserName = receiveMessage.getToUserName();
			String msgType = receiveMessage.getMsgType();
			String content = receiveMessage.getContent();
			// 判断是否微信传过来的是否属于文本消息
			if (Const.Mssage.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
				// 遍历存储的数据(完全匹配)
				for (String arr : array) {
					// 关键字回复判断 (完全匹配)
					if (arr.equals(content)) {
						// 多组都匹配成功的时候，比较优先级

						message = initText(toUserName, fromUserName, "关键字回复消息");
					} else {
						// 默认回复
						message = initText(toUserName, fromUserName, "不知道回复啥，我能说你真帅吗?");
					}
				}

				// 遍历存储的数据 (模糊匹配)
				for (String arrData : arrayData) {
					// 关键字回复判断 (模糊匹配)
					if (arrData.contains(content)) {
						// 多组都匹配成功的时候，比较优先级

						message = initText(toUserName, fromUserName, "关键字回复消息");
					} else {
						// 默认回复
						message = initText(toUserName, fromUserName, "不知道回复啥，我能说你真帅吗?");
					}
				}

			} else if (Const.Mssage.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
				// 关注时回复
				String eventType = receiveMessage.getEvent(); 
				if (Const.Mssage.EVENT_TYPE_SUBSCRIBE.equals(eventType)) {
					message = initText(toUserName, fromUserName, "用户关注时回复的消息");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseInfo(message);

	}

	/**
	 * 文字消息组装
	 */
	@Override
	public ResponseInfo initText(String toUserName, String fromUserName, String content) {
		String message = null;
		TextMessageRequest text = new TextMessageRequest();
		// 设置从哪发出(发给谁)
		text.setFromUserName(toUserName); 
		text.setToUserName(fromUserName);
		text.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
		text.setCreateTime(System.currentTimeMillis());
		text.setContent(content);
		// 创建对象接受，将文本对象转换为XML对象
		message = SerializeUtil.beanToXml(text);
		return new ResponseInfo(message);
	}

	/**
	 * 音乐消息的组装
	 */
	@Override
	public ResponseInfo initMusicMessage(String toUserName, String fromUserName) {
		String message = null;
		MusicMessageRequest.Music music = new MusicMessageRequest.Music();
		music.setThumbMediaId("oPSaANAdutQ3ALNjJsqIBYeAN2QrOSylSCe_DagftdnuTRe1ymUM51S-7IUT78Rt");
		music.setTitle("谁来剪月光.mp3");
		music.setDescription("陈奕迅");
		music.setMusicUrl("/we/resource/SeeYouAgain.mp3");
		music.sethQMusicUrl("/we/resource/SeeYouAgain.mp3");
		MusicMessageRequest musicMessage = new MusicMessageRequest();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_MUSIC);
		musicMessage.setCreateTime(System.currentTimeMillis());
		musicMessage.setMusic(music);
		message = SerializeUtil.beanToXml(musicMessage);
		return new ResponseInfo(message);
	}




}
