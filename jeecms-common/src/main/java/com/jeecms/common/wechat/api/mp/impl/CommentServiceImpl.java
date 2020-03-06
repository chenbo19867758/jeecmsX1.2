/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.CommentService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.comment.CommentListRequest;
import com.jeecms.common.wechat.bean.request.mp.comment.CommentRequest;
import com.jeecms.common.wechat.bean.request.mp.comment.MarkelectCommentRequest;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.comment.CommentResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 留言评论实现类
 * 
 * @author: ljw
 * @date: 2019年5月29日 下午3:45:06
 */
@Service
public class CommentServiceImpl implements CommentService {

	/** 打开已群发文章评论 **/
	public final String OPEN_COMMENT_URL = Const.DoMain.API_URI
						.concat("/cgi-bin/comment/open?access_token");
	/** 关闭已群发文章评论 **/
	public final String CLOSE_COMMENT_URL = Const.DoMain.API_URI
						.concat("/cgi-bin/comment/close?access_token");
	/** 查看指定文章的评论数据 **/
	public final String LIST_COMMENT_URL = Const.DoMain.API_URI
						.concat("/cgi-bin/comment/list?access_token");
	/** 将评论标记精选 **/
	public final String MARKELECT_COMMENT_URL = Const.DoMain.API_URI
						.concat("/cgi-bin/comment/markelect?access_token");
	/** 将评论取消精选 **/
	public final String UNMARKELECT_COMMENT_URL = Const.DoMain.API_URI
					.concat("/cgi-bin/comment/unmarkelect?access_token");
	/** 删除评论 **/
	public final String DELETE_COMMENT_URL = Const.DoMain.API_URI
					.concat("/cgi-bin/comment/delete?access_token");
	/** 回复评论 **/
	public final String REPLY_ADD_COMMENT_URL = Const.DoMain.API_URI
					.concat("/cgi-bin/comment/reply/add?access_token");
	/** 删除回复 **/
	public final String REPLY_DELETE_COMMENT_URL = Const.DoMain.API_URI
			.concat("/cgi-bin/comment/reply/delete?access_token");

	public final String ACCESS_TOKEN = "access_token";

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse openComment(CommentRequest commentRequest, ValidateToken validToken) 
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(OPEN_COMMENT_URL, params,
				SerializeUtil.beanToJson(commentRequest), BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse closeComment(CommentRequest commentRequest, ValidateToken validToken) 
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(CLOSE_COMMENT_URL, params,
				SerializeUtil.beanToJson(commentRequest), BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public CommentResponse commentList(CommentListRequest commentListRequest, ValidateToken validToken)
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		CommentResponse response = HttpUtil.postJsonBean(LIST_COMMENT_URL, params,
				SerializeUtil.beanToJson(commentListRequest), CommentResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse markelect(MarkelectCommentRequest markelect, ValidateToken validToken) 
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(MARKELECT_COMMENT_URL, params,
				SerializeUtil.beanToJson(markelect), BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse unmarkelect(MarkelectCommentRequest unmarkelect, ValidateToken validToken)
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(UNMARKELECT_COMMENT_URL, params,
				SerializeUtil.beanToJson(unmarkelect), BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse delete(MarkelectCommentRequest delete, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(DELETE_COMMENT_URL, params, 
				SerializeUtil.beanToJson(delete),
				BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse reply(MarkelectCommentRequest reply, ValidateToken validToken) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(REPLY_ADD_COMMENT_URL, params, 
				SerializeUtil.beanToJson(reply),
				BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public BaseResponse deleteReply(MarkelectCommentRequest deleteReply, ValidateToken validToken)
			throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, validToken.getAccessToken());
		BaseResponse response = HttpUtil.postJsonBean(REPLY_DELETE_COMMENT_URL, params,
				SerializeUtil.beanToJson(deleteReply), BaseResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(), response.getErrmsg()));
		}
	}

}
