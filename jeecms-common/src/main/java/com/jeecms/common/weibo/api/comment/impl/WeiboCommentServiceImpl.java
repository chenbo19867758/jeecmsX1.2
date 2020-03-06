/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.api.comment.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.Constants;
import com.jeecms.common.weibo.api.comment.WeiboCommentService;
import com.jeecms.common.weibo.bean.request.comment.CommentCreateRequest;
import com.jeecms.common.weibo.bean.request.comment.DeleteCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.ReplyCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.UserCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.WeiboCommentRequest;
import com.jeecms.common.weibo.bean.response.comment.UserCommentResponse;
import com.jeecms.common.weibo.bean.response.comment.WeiboCommentResponse;
import com.jeecms.common.weibo.bean.response.comment.vo.CommentVO;

/**
 * 微博评论实现类
 * 
 * @author: ljw
 * @date: 2019年6月18日 下午2:46:19
 */
@Service
public class WeiboCommentServiceImpl implements WeiboCommentService {

	/** 获取最新的提到当前登录用户的评论，即@我的评论 **/
	public static final String COMMENT_URL = Constants.BASE_URL.concat("comments/mentions.json");
	/** 发出的评论**/
	public static final String SEND_URL = Constants.BASE_URL.concat("comments/by_me.json");
	/** 收到的评论 **/
	public static final String RECEIVE_URL = Constants.BASE_URL.concat("comments/to_me.json");
	/** 根据微博ID返回某条微博的评论列表**/
	public static final String WEIBO_COMMENT_URL = Constants.BASE_URL.concat("comments/show.json");
	/** 根据微博ID发表评论**/
	public static final String CREATE_COMMENT_URL = Constants.BASE_URL.concat("comments/create.json");
	/** 根据评论ID发表评论**/
	public static final String DELETE_COMMENT_URL = Constants.BASE_URL.concat("comments/destroy.json");
	/** 回复发表评论**/
	public static final String REPLY_COMMENT_URL = Constants.BASE_URL.concat("comments/reply.json");
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String COUNT = "count";
	public static final String PAGE = "page";
	public static final String ID = "id";
	public static final String COMMENT = "comment";
	public static final String CID = "cid";
	
	@Override
	public List<CommentVO> getInfo(UserCommentRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(COUNT, request.getCount().toString());
		params.put(PAGE, request.getPage().toString());
		WeiboCommentResponse response = HttpUtil.getJsonBean(COMMENT_URL, params, 
				WeiboCommentResponse.class);
		if (response.getErrorCode() == null) {
			//组装数据
			return dispose(response.getComments(), response.getTotalNumber());
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}

	@Override
	public List<CommentVO> sendInfo(UserCommentRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(COUNT, request.getCount().toString());
		params.put(PAGE, request.getPage().toString());
		WeiboCommentResponse response = HttpUtil.getJsonBean(SEND_URL, params, 
				WeiboCommentResponse.class);
		if (response.getErrorCode() == null) {
			//组装数据
			return dispose(response.getComments(), response.getTotalNumber());
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}

	@Override
	public List<CommentVO> receiveInfo(UserCommentRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(COUNT, request.getCount().toString());
		params.put(PAGE, request.getPage().toString());
		WeiboCommentResponse response = HttpUtil.getJsonBean(RECEIVE_URL, params, 
				WeiboCommentResponse.class);
		if (response.getErrorCode() == null) {
			//组装数据
			return dispose(response.getComments(), response.getTotalNumber());
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}

	@Override
	public List<CommentVO> findByWeiboID(WeiboCommentRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(ID, request.getId().toString());
		params.put(COUNT, request.getCount().toString());
		params.put(PAGE, request.getPage().toString());
		WeiboCommentResponse response = HttpUtil.getJsonBean(WEIBO_COMMENT_URL, params, 
				WeiboCommentResponse.class);
		if (response.getErrorCode() == null) {
			//组装数据
			return dispose(response.getComments(), response.getTotalNumber());
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}
	
	/**
	 * 处理微博返回的数据
	* @Title: dispose 
	* @param commentResponses 单条微博评论集合
	* @param weiboId 微博ID
	* @return
	 */
	protected List<CommentVO> dispose(List<UserCommentResponse> commentResponses, Integer totalNumber) {
		List<CommentVO> vos = new ArrayList<CommentVO>();
		//遍历评论
		for (UserCommentResponse bean : commentResponses) {
			CommentVO vo = new CommentVO();
			vo.setWeiboId(bean.getWeiboId());
			vo.setWeiboContent(bean.getWeiboContent());
			vo.setCommentId(bean.getId());
			vo.setCommentText(bean.getText());
			vo.setUser(bean.getUser());
			vo.setCreateTime(MyDateUtils.parseTimeDate(bean.getCreatedAt()));
			vo.setReplyCommentId(bean.getReply());
			vo.setTotalNumber(totalNumber);
			vos.add(vo);
		}
		return vos;
	}

	@Override
	public CommentVO createComment(CommentCreateRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(ID, request.getId().toString());
		params.put(COMMENT, request.getComment());
		UserCommentResponse response = HttpUtil.postJsonBean(CREATE_COMMENT_URL, params, null, 
				UserCommentResponse.class);
		if (response.getErrorCode() == null) {
			CommentVO vo = new CommentVO();
			vo.setCommentId(response.getId());
			vo.setCommentText(response.getText());
			vo.setCreateTime(new Date());
			vo.setUser(response.getUser());
			return vo;
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}

	@Override
	public void deleteComment(DeleteCommentRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(CID, request.getCid().toString());
		UserCommentResponse response = HttpUtil.postJsonBean(DELETE_COMMENT_URL, params, null, 
				UserCommentResponse.class);
		if (response.getErrorCode() == null) {
			return;
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}

	@Override
	public CommentVO replyComment(ReplyCommentRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(CID, request.getCid().toString());
		params.put(ID, request.getId().toString());
		params.put(COMMENT, request.getComment());
		UserCommentResponse response = HttpUtil.postJsonBean(REPLY_COMMENT_URL, params, null, 
				UserCommentResponse.class);
		if (response.getErrorCode() == null) {
			CommentVO vo = new CommentVO();
			vo.setCommentId(response.getId());
			vo.setCommentText(response.getText());
			vo.setCreateTime(new Date());
			vo.setUser(response.getUser());
			return vo;
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}	
	}

}
