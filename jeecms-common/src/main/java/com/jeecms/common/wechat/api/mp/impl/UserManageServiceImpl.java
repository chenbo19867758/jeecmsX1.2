package com.jeecms.common.wechat.api.mp.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.UserManageService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.message.ImageMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.message.MessageRequest;
import com.jeecms.common.wechat.bean.request.mp.message.MpnewsMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.message.MpvideoMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.message.TextMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.message.VoiceMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.message.WxcardMessageRequest;
import com.jeecms.common.wechat.bean.request.mp.user.BatchblackListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.BlackFansListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsAddRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsDeleteRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsUpdateRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserInfoRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserTagsAddRequest;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.user.MessageResponse;
import com.jeecms.common.wechat.bean.response.mp.user.TagsAddResponse;
import com.jeecms.common.wechat.bean.response.mp.user.TagsResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserInfoListResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserListResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 粉丝列表接口实现类
 * @author: ljw
 * @date: 2018年8月2日 下午1:51:17
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class UserManageServiceImpl implements UserManageService {

	/** 获取公众号用户列表 **/
	public final String USER_LIST_URL = Const.DoMain.API_URI.concat("/cgi-bin/user/get?access_token");

	/** 获取公众号用户详情列表 **/
	public final String LIST_URL = Const.DoMain.API_URI.concat("/cgi-bin/user/info/batchget?access_token");

	/** 新增标签 **/
	public final String ADD_TAGS_URL = Const.DoMain.API_URI.concat("/cgi-bin/tags/create?access_token");

	/** 删除标签 **/
	public final String DELETE_TAGS_URL = Const.DoMain.API_URI.concat("/cgi-bin/tags/delete?access_token");

	/** 修改标签 **/
	public final String UPDATE_TAGS_URL = Const.DoMain.API_URI.concat("/cgi-bin/tags/update?access_token");

	/** 拉黑用户列表 **/
	public final String Batch_Black_List_URL = Const.DoMain.API_URI
			.concat("/cgi-bin/tags/members/getblacklist?access_token");

	/** 拉黑用户 **/
	public final String Batch_Black_URL = Const.DoMain.API_URI
			.concat("/cgi-bin/tags/members/batchblacklist?access_token");

	/** 取消拉黑用户 **/
	public final String CancelBatch_Black_URL = Const.DoMain.API_URI
			.concat("/cgi-bin/tags/members/batchunblacklist?access_token");

	/** 新增标签 **/
	public final String TAGS_URL = Const.DoMain.API_URI.concat("/cgi-bin/tags/get?access_token");

	/** 设置用户标签 **/
	public final String ADD_USER_TAGS_URL = Const.DoMain.API_URI.concat("/cgi-bin/tags/members/batchtagging");

	/** 发送消息 **/
	public final String MESSAGE_URL = Const.DoMain.API_URI.concat("/cgi-bin/message/mass/preview?access_token");

	public final String ACCESS_TOKEN = "access_token";

	public final String NEXT_OPENID = "next_openid";

	public final String OPENID_LIST = "openid_list";

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public List<UserInfoListResponse> getUserList(UserListRequest userListRequest, ValidateToken validToken)
			throws GlobalException {

		List<UserInfoListResponse> listResponses = new ArrayList<UserInfoListResponse>();

		List<UserListResponse> userListResponse = getOpenid(userListRequest, validToken);

		for (UserListResponse fans : userListResponse) {
			List<String> list = fans.getData().getOpenid();
			Map<String, String> params = new HashMap<String, String>(16);
			params.put(ACCESS_TOKEN, validToken.getAccessToken());
			// 开发者可通过该接口来批量获取用户基本信息。最多支持一次拉取100条。
			for (int i = 0; i < list.size();) {
				int end = i + 100;
				if (end < list.size()) {
					UserInfoListResponse response = HttpUtil.postJsonBean(LIST_URL, params,
							SerializeUtil.beanToJson(new UserInfoRequest(list.subList(i, end))),
							UserInfoListResponse.class);
					if (response.SUCCESS_CODE.equals(response.getErrcode())) {
						listResponses.add(response);
					} else {
						throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
					}
				} else {
					UserInfoListResponse response = HttpUtil.postJsonBean(LIST_URL, params,
							SerializeUtil.beanToJson(new UserInfoRequest(list.subList(i, list.size()))),
							UserInfoListResponse.class);
					if (response.SUCCESS_CODE.equals(response.getErrcode())) {
						listResponses.add(response);
					} else {
						throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
					}
				}
				i += 100;
			}
		}
		return listResponses;
	}

	/**
	 * 新增标签
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public TagsAddResponse addTags(TagsAddRequest tagsAddRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		TagsAddResponse tagsAddResponse = HttpUtil.postJsonBean(ADD_TAGS_URL, params,
				SerializeUtil.beanToJson(tagsAddRequest), TagsAddResponse.class);

		if (tagsAddResponse.SUCCESS_CODE.equals(tagsAddResponse.getErrcode())) {
			return tagsAddResponse;
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(tagsAddResponse.getErrcode(), tagsAddResponse.getErrmsg()));
		}

	}

	/**
	 * 拉黑粉丝列表
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public UserListResponse batchBlackList(BlackFansListRequest blackFansListRequest, ValidateToken validToken)
			throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		UserListResponse response = HttpUtil.postJsonBean(Batch_Black_List_URL, params,
				SerializeUtil.beanToJson(blackFansListRequest), UserListResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 拉黑粉丝
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse batchBlack(BatchblackListRequest batchblackListRequest, ValidateToken validToken)
			throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		BaseResponse response = HttpUtil.postJsonBean(Batch_Black_URL, params,
				SerializeUtil.beanToJson(batchblackListRequest), BaseResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 取消拉黑粉丝
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse cancelbatchBlack(BatchblackListRequest batchblackListRequest, ValidateToken validToken)
			throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		BaseResponse response = HttpUtil.postJsonBean(CancelBatch_Black_URL, params,
				SerializeUtil.beanToJson(batchblackListRequest), BaseResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 获取粉丝openid数量
	 * 
	 * @param userListRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	List<UserListResponse> getOpenid(UserListRequest userListRequest, ValidateToken validToken) throws GlobalException {

		List<UserListResponse> list = new ArrayList<UserListResponse>(10);

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		params.put(NEXT_OPENID, userListRequest.getNextOpenid());
		// 得到返回的数据
		UserListResponse userListResponse = HttpUtil.getJsonBean(USER_LIST_URL, params, UserListResponse.class);

		list.add(userListResponse);

		String nextOpenid = userListResponse.getNextOpenid();

		int count = userListResponse.getCount();

		int number = 10000;
		// 如果粉丝数量超过100000+，需要重复调用
		if (count > number) {
			// 得到遍历次数
			int sum = (count - 10000) % 10000 == 0 ? (count - 10000) / 10000 : (count - 10000) / 10000 + 1;

			for (int i = 0; i < sum; i++) {
				UserListResponse response = getUserSize(validToken, nextOpenid);
				list.add(response);
				nextOpenid = response.getNextOpenid();
			}
		}

		if (userListResponse.SUCCESS_CODE.equals(userListResponse.getErrcode())) {
			return list;
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(userListResponse.getErrcode(), userListResponse.getErrmsg()));
		}
	}

	/**
	 * 超过10000+时获取粉丝数量
	 * 
	 * @param validToken
	 * @param nextOpenid
	 * @return
	 * @throws GlobalException
	 */
	public UserListResponse getUserSize(ValidateToken validToken, String nextOpenid) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		params.put(NEXT_OPENID, nextOpenid);
		UserListResponse response = HttpUtil.getJsonBean(USER_LIST_URL, params, UserListResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse addUserTags(UserTagsAddRequest userTagsAddRequest, ValidateToken validToken)
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		BaseResponse response = HttpUtil.postJsonBean(ADD_USER_TAGS_URL, params,
				SerializeUtil.beanToJson(userTagsAddRequest), BaseResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public TagsResponse tags(ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		// 得到返回的数据
		TagsResponse tagsResponse = HttpUtil.getJsonBean(TAGS_URL, params, TagsResponse.class);

		if (tagsResponse.SUCCESS_CODE.equals(tagsResponse.getErrcode())) {
			return tagsResponse;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(tagsResponse.getErrcode(), tagsResponse.getErrmsg()));
		}

	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public MessageResponse send(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		String messageType = messageRequest.getMsgtype();

		switch (messageType) {
		// 发送文本信息
		case "text":
			MessageResponse text = text(messageRequest, validToken);
			return text;
		// 发送语音信息
		case "voice":
			MessageResponse voice = voice(messageRequest, validToken);
			return voice;
		// 发送图片信息
		case "image":
			MessageResponse image = image(messageRequest, validToken);
			return image;
		// 发送图文信息
		case "mpnews":
			MessageResponse mpnews = mpnews(messageRequest, validToken);
			return mpnews;
		// 发送视频信息
		case "video":
			MessageResponse mpvideo = mpvideo(messageRequest, validToken);
			return mpvideo;
//		case "wxcard"://发送卡卷信息
//			MessageResponse wxcard=wxcard(messageRequest, validToken);
//			return wxcard;
		default:
			break;
		}
		return null;
	}

	/**
	 * 发送文本消息
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	public MessageResponse text(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		TextMessageRequest textMessageRequest = new TextMessageRequest();
		textMessageRequest.setTouser(messageRequest.getTouser());
		textMessageRequest.setMsgtype(messageRequest.getMsgtype());
		TextMessageRequest.Text text = textMessageRequest.new Text();
		text.setContent(messageRequest.getContent());
		textMessageRequest.setText(text);

		MessageResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params,
				SerializeUtil.beanToJson(textMessageRequest), MessageResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 发送语音消息
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	public MessageResponse voice(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		VoiceMessageRequest voiceMessageRequest = new VoiceMessageRequest();
		voiceMessageRequest.setTouser(messageRequest.getTouser());
		voiceMessageRequest.setMsgtype(messageRequest.getMsgtype());
		VoiceMessageRequest.Voice voice = voiceMessageRequest.new Voice();
		voice.setMediaId(messageRequest.getMediaId());
		voiceMessageRequest.setVoice(voice);

		MessageResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params,
				SerializeUtil.beanToJson(voiceMessageRequest), MessageResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 发送图片消息
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	public MessageResponse image(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		ImageMessageRequest imageMessageRequest = new ImageMessageRequest();
		imageMessageRequest.setTouser(messageRequest.getTouser());
		imageMessageRequest.setMsgtype(messageRequest.getMsgtype());
		ImageMessageRequest.Image image = imageMessageRequest.new Image();
		image.setMediaId(messageRequest.getMediaId());
		imageMessageRequest.setImage(image);

		MessageResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params,
				SerializeUtil.beanToJson(imageMessageRequest), MessageResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 发送图文消息
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	public MessageResponse mpnews(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		MpnewsMessageRequest mpnewsMessageRequest = new MpnewsMessageRequest();
		mpnewsMessageRequest.setTouser(messageRequest.getTouser());
		mpnewsMessageRequest.setMsgtype(messageRequest.getMsgtype());
		MpnewsMessageRequest.Mpnews mpnews = mpnewsMessageRequest.new Mpnews();
		mpnews.setMediaId(messageRequest.getMediaId());
		mpnewsMessageRequest.setMpnews(mpnews);

		MessageResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params,
				SerializeUtil.beanToJson(mpnewsMessageRequest), MessageResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 发送视频消息
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	public MessageResponse mpvideo(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		MpvideoMessageRequest mpvideoMessageRequest = new MpvideoMessageRequest();
		mpvideoMessageRequest.setTouser(messageRequest.getTouser());
		mpvideoMessageRequest.setMsgtype("mp" + messageRequest.getMsgtype());
		MpvideoMessageRequest.Mpvideo mpvideo = mpvideoMessageRequest.new Mpvideo();
		mpvideo.setMediaId(messageRequest.getMediaId());
		mpvideoMessageRequest.setMpvideo(mpvideo);

		MessageResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params,
				SerializeUtil.beanToJson(mpvideoMessageRequest), MessageResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 发送卡卷消息 (可做扩展用)
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	public MessageResponse wxcard(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		WxcardMessageRequest wxcardMessageRequest = new WxcardMessageRequest();
		wxcardMessageRequest.setTouser(messageRequest.getTouser());
		wxcardMessageRequest.setMsgtype(messageRequest.getMsgtype());
//		WxcardMessageRequest.Wxcard wxcard=wxcardMessageRequest.new Wxcard();

		MessageResponse response = HttpUtil.postJsonBean(MESSAGE_URL, params,
				SerializeUtil.beanToJson(wxcardMessageRequest), MessageResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	/**
	 * 批量获取粉丝详情
	 * 
	 * @param messageRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public UserInfoListResponse fansMore(UserInfoRequest userInfoRequest, ValidateToken validToken)
			throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		UserInfoListResponse response = HttpUtil.postJsonBean(LIST_URL, params,
				SerializeUtil.beanToJson(userInfoRequest), UserInfoListResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse deleteTags(TagsDeleteRequest tagsDeleteRequest, ValidateToken validToken)
			throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		BaseResponse response = HttpUtil.postJsonBean(DELETE_TAGS_URL, params,
				SerializeUtil.beanToJson(tagsDeleteRequest), BaseResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse updateTags(TagsUpdateRequest tagsUpdateRequest, ValidateToken validToken)
			throws GlobalException {

		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());

		BaseResponse response = HttpUtil.postJsonBean(UPDATE_TAGS_URL, params,
				SerializeUtil.beanToJson(tagsUpdateRequest), BaseResponse.class);

		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

}
