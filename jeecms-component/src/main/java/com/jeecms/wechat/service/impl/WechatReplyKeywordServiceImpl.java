package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.mp.GetMaterialApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.news.ImageMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.news.MusicMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.news.NewsMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.news.ReqMessage;
import com.jeecms.common.wechat.bean.request.mp.news.TextMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.news.VideoMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.news.Voice;
import com.jeecms.common.wechat.bean.request.mp.news.VoiceMessageRequest;
import com.jeecms.common.wechat.bean.response.mp.material.GetVideoResponse;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.dao.WechatReplyKeywordDao;
import com.jeecms.wechat.domain.WechatFansSendLog;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatReplyClick;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.service.WechatFansSendLogService;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatReplyClickService;
import com.jeecms.wechat.service.WechatReplyContentService;
import com.jeecms.wechat.service.WechatReplyKeywordService;

/**
 * 关键字service实现类
 * @author: chenming
 * @date:   2019年6月3日 上午9:44:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatReplyKeywordServiceImpl extends BaseServiceImpl<WechatReplyKeyword, WechatReplyKeywordDao, Integer>
		implements WechatReplyKeywordService {

	@Override
	public String getMessageReply(ReqMessage reqMessage, String appId) throws GlobalException {

		/*
		 * //消息类型用msgId排重，事件类型用createTime和fromUserName排重 if(reqMessage.getMsgId()!=null)
		 * { message.setMsgId(reqMessage.getMsgId()); }else {
		 * message.setCreateTime(reqMessage.getCreateTime());
		 * message.setFromUserName(reqMessage.getFromUserName()); }
		 * if(MESSAGE_CACHE.contains(message)) { //重复时 return ""; }else {
		 * setMessageToCache(message); }
		 */
		WechatReplyContent wechatReplyContent = null;
		WechatFansSendLog fansSendLog = null;
		WechatFansSendLog bean = null;
		fansSendLog = new WechatFansSendLog(
					1, appId, reqMessage.getFromUserName(), 1, reqMessage.getMsgType(), 
					this.initMediaJson(reqMessage,appId), false, false);
		bean = fansSendLogService.save(fansSendLog);
		super.flush();
		try {
			switch (reqMessage.getMsgType()) {
				// 关键词回复（即文本）
				case Const.Mssage.REQ_MESSAGE_TYPE_TEXT:
					WechatReplyKeyword keywords = dao.getKeyword(appId, reqMessage.getContent());
					if (keywords != null) {
						// 关键词回复
						wechatReplyContent = wechatReplyContentService.findById(
								keywords.getReplyContentId());
					} else {
						// 默认回复，未设置默认回复，统一使用系统回复
						WechatReplyClick click = wechatReplyClickService
								.findByReplyTypeAndAppId(2, appId);
						if (click != null) {
							WechatReplyContent content = click.getWechatReplyContent();
							if (content != null) {
								wechatReplyContent = content;
							}
						}
					}
					break; 
				// 事件回复 （含取消关注 、关注、扫描二维码、自定义菜单、上报地理位置；目前只处理关注 、自定义菜单之点击菜单拉取时的事件推送）
				case Const.Mssage.REQ_MESSAGE_TYPE_EVENT:
					String event = reqMessage.getEvent();
					if (Const.Mssage.EVENT_TYPE_SUBSCRIBE.equals(event)) {
						// 关注公众号事件回复
						WechatReplyClick replyClick = wechatReplyClickService
								.findByReplyTypeAndAppId(1, appId);
						if (replyClick != null) {
							wechatReplyContent = replyClick.getWechatReplyContent();
						}
					} else if (Const.Mssage.EVENT_TYPE_CLICK.equals(event)) {
						// 自定义菜单之点击菜单拉取时的事件推送
						String eventKey = reqMessage.getEventKey();
						if (wechatReplyContentService
								.findById(Integer.valueOf(eventKey)) != null) {
							// WechatReplyClick click =
							// wechatReplyClickService.findById(Integer.valueOf(eventKey));
							wechatReplyContent = wechatReplyContentService.findById(
									Integer.valueOf(eventKey));
						}
					}
					break;
				default:
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 文本消息时返回指定字符串(全网发布时用)
			if (Const.Mssage.TESTCOMPONENT_MSG_TYPE_TEXT.equals(reqMessage.getContent())) {
				wechatReplyContent = new WechatReplyContent();
				wechatReplyContent.setContent("TESTCOMPONENT_MSG_TYPE_TEXT_callback");
				wechatReplyContent.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
			}

			// 返回Api文本消息(全网发布时用)
			if (reqMessage.getContent() != null 
					&& reqMessage.getContent().startsWith(Const.Mssage.QUERY_AUTH_CODE)) {
				wechatReplyContent = new WechatReplyContent();
				wechatReplyContent.setContent(reqMessage.getContent().split(":")[1] + "_from_api");
				wechatReplyContent.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
			}

			if (wechatReplyContent == null) {
				WechatReplyClick replyClick = wechatReplyClickService
						.findByReplyTypeAndAppId(2, appId);
				if (replyClick != null && replyClick.getReplyContentId() != null) {
					wechatReplyContent = replyClick.getWechatReplyContent();
				} else {
					ServletRequestAttributes requestAttributes = (ServletRequestAttributes) 
							RequestContextHolder.getRequestAttributes();
					GlobalConfig config = SystemContextUtils.getGlobalConfig(
							requestAttributes.getRequest());
					wechatReplyContent = new WechatReplyContent();
					wechatReplyContent.setContent(config.getConfigAttr().getWechatReplyContent());
					wechatReplyContent.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
				}
			}
		}
		String messageReply = null;
		messageReply = message(wechatReplyContent.getMsgType(), reqMessage.getToUserName(),
				reqMessage.getFromUserName(), wechatReplyContent,bean);
 		return messageReply;
	}

	/**
	 * 文字消息组装
	 */
	private List<String> initText(String toUserName, String fromUserName, String content) {
		TextMessageRequest text = new TextMessageRequest();
		// 设置从哪发出(发给谁)
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
		text.setCreateTime(System.currentTimeMillis());
		text.setContent(content);
		// 创建对象接受，将文本对象转换为XML对象
		String message = null;
		message = SerializeUtil.beanToXml(text);
		JSONObject ject = new JSONObject();
		ject.put("msgType", "text");
		ject.put("content", content);
		List<String> sList = new ArrayList<String>();
		sList.add(message);
		sList.add(ject.toJSONString());
		return sList;
	}

	/**
	 * 音乐消息的组装
	 */
	private List<String> initMusicMessage(String toUserName, String fromUserName, WechatReplyContent content) {
		MusicMessageRequest.Music music = new MusicMessageRequest.Music();
		music.setThumbMediaId(content.getWechatMaterial().getMediaId());
		music.setTitle(content.getTitle());
		music.setDescription(content.getDescription());
		music.setMusicUrl(content.getMusicUrl());
		music.sethQMusicUrl(content.getHqMusicUrll());
		MusicMessageRequest musicMessage = new MusicMessageRequest();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(content.getMsgType());
		musicMessage.setCreateTime(System.currentTimeMillis());
		musicMessage.setMusic(music);
		String message = null;
		message = SerializeUtil.beanToXml(musicMessage);
		JSONObject ject = new JSONObject();
		ject.put("msgType", "music");
		ject.put("title", content.getTitle());
		ject.put("description", content.getDescription());
		ject.put("musicUrl", content.getMusicUrl());
		ject.put("hqMusicUrll", content.getHqMusicUrll());
		ject.put("thumbUrl", this.getImgUrl(content.getThumbMediaId()));
		List<String> sList = new ArrayList<String>();
		sList.add(message);
		sList.add(ject.toJSONString());
		return sList;
	}

	/**
	 * 图片消息组装
	 */
	private List<String> initImage(String toUserName, String fromUserName, WechatReplyContent content) {
		ImageMessageRequest.Image image = new ImageMessageRequest.Image();
		image.setMediaId(content.getWechatMaterial().getMediaId());
		ImageMessageRequest imageMessage = new ImageMessageRequest();
		imageMessage.setCreateTime(System.currentTimeMillis());
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(content.getMsgType());
		imageMessage.setImage(image);
		String message = null;
		message = SerializeUtil.beanToXml(imageMessage);
		JSONObject json = new JSONObject();
		json.put("msgType", "image");
		json.put("picUrl", this.getImgUrl(content.getMediaId()));
		List<String> sList = new ArrayList<String>();
		sList.add(message);
		sList.add(json.toJSONString());
		return sList;
	}

	/**
	 * 语音消息组装
	 */
	private List<String> initVoice(String toUserName, String fromUserName, WechatReplyContent content) {
		Voice voice = new Voice();
		voice.setMediaId(content.getWechatMaterial().getMediaId());
		VoiceMessageRequest voiceMessage = new VoiceMessageRequest();
		voiceMessage.setCreateTime(System.currentTimeMillis());
		voiceMessage.setFromUserName(toUserName);
		voiceMessage.setToUserName(fromUserName);
		voiceMessage.setMsgType(content.getMsgType());
		voiceMessage.setVoice(voice);
		String message = null;
		message = SerializeUtil.beanToXml(voiceMessage);
		JSONObject json = new JSONObject();
		json.put("msgType", "voice");
		json.put("mediaId", content.getWechatMaterial().getMediaId());
		List<String> sList = new ArrayList<String>();
		sList.add(message);
		sList.add(json.toJSONString());
		return sList;
	}
	
	/**
	 * 视频消息组装
	 */
	private List<String> initVideo(String toUserName, String fromUserName, WechatReplyContent content) 
			throws GlobalException {
		VideoMessageRequest.Video video = new VideoMessageRequest.Video();
		video.setDescription(content.getDescription());
		video.setMediaId(content.getWechatMaterial().getMediaId());
		video.setTitle(content.getTitle());
		VideoMessageRequest videoMessage = new VideoMessageRequest();
		videoMessage.setCreateTime(System.currentTimeMillis());
		videoMessage.setFromUserName(toUserName);
		videoMessage.setToUserName(fromUserName);
		videoMessage.setMsgType(content.getMsgType());
		videoMessage.setVideo(video);
		String message = null;
		message = SerializeUtil.beanToXml(videoMessage);
		JSONObject json = new JSONObject();
		json.put("msgType", "video");
		json.put("mediaId", content.getWechatMaterial().getMediaId());
		List<String> sList = new ArrayList<String>();
		sList.add(message);
		sList.add(json.toJSONString());
		return sList;
	}

	/**
	 * 图文消息组装
	 */
	private List<String> initNew(String toUserName, String fromUserName, WechatReplyContent content) {
		NewsMessageRequest newsMessage = new NewsMessageRequest();
		newsMessage.setCreateTime(System.currentTimeMillis());
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setMsgType(content.getMsgType());
		List<NewsMessageRequest.News> newsList = new ArrayList<NewsMessageRequest.News>();
		// 获取到数据库的json串
		String sJSON = content.getWechatMaterial().getMaterialJson();
		JSONArray array = JSONArray.parseArray(sJSON);
		// 遍历图文内的消息
		for (int i = 0; i < array.size(); i++) {
			NewsMessageRequest.News news = new NewsMessageRequest.News();
			JSONObject iObj = array.getJSONObject(i);
			news.setDescription(iObj.get(WechatReplyContent.DIGEST).toString());
			/**
			 * thumbMediaUrl可能为空：此处为空是因为获取素材无法获取到thumb的素材列表
			 * 此处如此写的原因：1. 文档thumb出必须要填。 2. 文档他妈的是错的(主要)
			 */
			if (iObj.get(WechatReplyContent.THUMB_MEDIA_URL) != null) {
				news.setPicUrl(iObj.get(WechatReplyContent.THUMB_MEDIA_URL).toString());
			}
			news.setTitle(iObj.get(WechatReplyContent.TITLE).toString());
			news.setUrl(iObj.get(WechatReplyContent.URL).toString());
			newsList.add(news);
		}
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		String message = null;
		message = SerializeUtil.beanToXml(newsMessage);
		JSONObject json = new JSONObject();
		json.put("msgType", "news");
		json.put("news", sJSON);
		List<String> sList = new ArrayList<String>();
		sList.add(message);
		sList.add(json.toJSONString());
		return sList;
	}

	/**
	 * 根据不同类型组装不同数据
	 */
	public String message(String type, String toUserName, String fromUserName, 
			WechatReplyContent wechatReplyContent, WechatFansSendLog bean) throws GlobalException {
		List<String> sList = new ArrayList<String>();
		String message = null;
		String json = null;
		switch (type) {
			case Const.Mssage.REQ_MESSAGE_TYPE_TEXT:
				sList = initText(toUserName, fromUserName, wechatReplyContent.getContent());
				message = sList.get(0);
				json = sList.get(1);
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_MUSIC:
				sList = initMusicMessage(toUserName, fromUserName, wechatReplyContent);
				message = sList.get(0);
				json = sList.get(1);
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_IMAGE:
				sList = initImage(toUserName, fromUserName, wechatReplyContent);
				message = sList.get(0);
				json = sList.get(1);
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_VOICE:
				sList = initVoice(toUserName, fromUserName, wechatReplyContent);
				message = sList.get(0);
				json = sList.get(1);
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_VIDEO:
				sList = initVideo(toUserName, fromUserName, wechatReplyContent);
				message = sList.get(0);
				json = sList.get(1);
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_NEWS:
				sList = initNew(toUserName, fromUserName, wechatReplyContent);
				message = sList.get(0);
				json = sList.get(1);
				break;
			default:
				break;
		}
		if (json != null) {
			WechatFansSendLog fansSendLog =  new WechatFansSendLog(
					2, bean.getAppId(), fromUserName, 1, 
					wechatReplyContent.getMsgType(), 
					json, false, false);
			fansSendLogService.save(fansSendLog);
			bean.setReply(true);
			fansSendLogService.update(bean);
		}
		return message;
	}

	/**
	 * 添加关键字
	 */
	@Override
	public ResponseInfo saveKeyWork(WechatReplyKeyword keyword) throws GlobalException {

		WechatReplyContent replyContent = new WechatReplyContent();
		replyContent = keyword.getWechatReplyContent();
		replyContent.setIsEnable(replyContent.getIsEnable());
		// 判断回复类型
		if (StringUtils.isNotBlank(replyContent.getContent())) {
			// 文本类型
			replyContent.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_TEXT);
		} else if (replyContent.getThumbMediaId() != null) {
			// 音乐类型
			replyContent.setMsgType(Const.Mssage.REQ_MESSAGE_TYPE_MUSIC);
			replyContent.setMediaId(replyContent.getThumbMediaId());
		} else {
			if (replyContent.getMediaId() == null) {
				// 回复内容不能为空
				return new ResponseInfo(SettingErrorCodeEnum.CONTENT_NOTNULL.getCode(),
						SettingErrorCodeEnum.CONTENT_NOTNULL.getDefaultMessage());
			} else {
				// 媒体类型
				WechatMaterial wechatMaterial = materialService.findById(replyContent.getMediaId());
				keyword.getWechatReplyContent().setMsgType(wechatMaterial.getMediaType());
			}

		}
		WechatReplyContent content = null;
		content = wechatReplyContentService.save(keyword.getWechatReplyContent());
		keyword.setReplyContentId(content.getId());
		String[] wordkeyEq = keyword.getKeyEq();
		String[] wordkeyLike = keyword.getKeyLike();
		List<WechatReplyKeyword> list = new ArrayList<>();
		if (wordkeyEq != null) {
			for (int i = 0; i < wordkeyEq.length; i++) {
				WechatReplyKeyword replyKeyword = new WechatReplyKeyword();
				replyKeyword.setWordkeyEq(wordkeyEq[i]);
				replyKeyword.setAppId(keyword.getAppId());
				replyKeyword.setReplyContentId(keyword.getReplyContentId());
				replyKeyword.setWechatReplyContent(keyword.getWechatReplyContent());
				list.add(replyKeyword);
			}
		}
		if (wordkeyLike != null) {
			for (int i = 0; i < wordkeyLike.length; i++) {
				WechatReplyKeyword replyKeyword = new WechatReplyKeyword();
				replyKeyword.setWordkeyLike(wordkeyLike[i]);
				replyKeyword.setAppId(keyword.getAppId());
				replyKeyword.setReplyContentId(keyword.getReplyContentId());
				replyKeyword.setWechatReplyContent(keyword.getWechatReplyContent());
				list.add(replyKeyword);
			}
		}

		super.saveAll(list);
		return new ResponseInfo();
	}

	/**
	 * 修改关键词
	 */
	@Override
	public void updateKeyWork(WechatReplyKeyword keyword) throws GlobalException {
		WechatReplyContent content = keyword.getWechatReplyContent();
		wechatReplyContentService.update(content);
		super.update(keyword);
	}

	/**
	 * 根据AppId查询数据
	 */
	@Override
	public List<WechatReplyKeyword> findByAppId(String appId) throws GlobalException {
		return dao.findByAppIdAndHasDeleted(appId, false);
	}

	/**
	 * 初始化接受的数据的JSON数据
	 */
	private String initMediaJson(ReqMessage reqMessage,String appId) throws GlobalException {
		JSONObject json = new JSONObject();
		switch (reqMessage.getMsgType()) {
			case Const.Mssage.REQ_MESSAGE_TYPE_TEXT:
				json.put("msgType", "text");
				json.put("content", reqMessage.getContent());
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_IMAGE:
				json.put("msgType", "image");
				json.put("picUrl", reqMessage.getPicUrl());
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_VOICE:
				json.put("msgType", "voice");
				json.put("mediaId", reqMessage.getMediaId());
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_VIDEO:
				json.put("msgType", "video");
				json.put("mediaId", reqMessage.getMediaId());
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_SHORTVIDEO:
				json.put("msgType", "shortvideo");
				json.put("mediaId",reqMessage.getMediaId());
				break;
			case Const.Mssage.EVENT_TYPE_LOCATION:
				json.put("msgType", "location");
				json.put("label", reqMessage.getLabel());
				break;
			case Const.Mssage.REQ_MESSAGE_TYPE_LINK:
				json.put("msgType", "link");
				json.put("title", reqMessage.getTitle());
				json.put("description", reqMessage.getDescription());
				json.put("url", reqMessage.getUrl());
				break;
			default:
				break;
		}
		return json.toJSONString();
	}
	
	private ValidateToken getToken(String appId) {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(appId);
		return validateToken;
	}
	
	private String getImgUrl(Integer id) {
		WechatMaterial wMaterial = materialService.findById(id);
		String url = null;
		if (wMaterial != null) {
			wMaterial.setRequest(JSONObject.parseObject(wMaterial.getMaterialJson()));
			url = String.valueOf(wMaterial.getRequest().get("url"));
		}
		return url;
	}
	
	private String getVideoUrl(Integer id) throws GlobalException {
		WechatMaterial wMaterial = materialService.findById(id);
		String videoUrl = null;
		if (wMaterial != null) {
			GetMaterialRequest request = new GetMaterialRequest(wMaterial.getMediaId());
			GetVideoResponse response = materialApiService.getVideo(
					request, this.getToken(wMaterial.getAppId()));
			if (response != null) {
				videoUrl = response.getDownUrl();
			}
		}
		return videoUrl;
	}
	
	@Autowired
	private WechatReplyContentService wechatReplyContentService;
	@Autowired
	private WechatReplyClickService wechatReplyClickService;
	@Autowired
	private WechatMaterialService materialService;
	@Autowired
	private WechatFansSendLogService fansSendLogService;
	@Autowired
	private GetMaterialApiService materialApiService;
}