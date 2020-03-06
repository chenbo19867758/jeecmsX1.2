/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.api.mp.CommentService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.comment.CommentListRequest;
import com.jeecms.common.wechat.bean.request.mp.comment.CommentRequest;
import com.jeecms.common.wechat.bean.request.mp.comment.MarkelectCommentRequest;
import com.jeecms.common.wechat.bean.response.mp.comment.CommentResponse;
import com.jeecms.common.wechat.bean.response.mp.comment.CommentResponse.Comment;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.dao.WechatCommentDao;
import com.jeecms.wechat.domain.WechatComment;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.domain.WechatSendArticle;
import com.jeecms.wechat.domain.dto.WechatCommentDto;
import com.jeecms.wechat.service.WechatCommentService;
import com.jeecms.wechat.service.WechatFansService;
import com.jeecms.wechat.service.WechatSendArticleService;
import com.jeecms.wechat.service.WechatSendService;

/**
 * 微信留言Service实现类
* @author ljw
* @version 1.0
* @date 2019-05-31
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatCommentServiceImpl extends BaseServiceImpl<WechatComment,WechatCommentDao, Integer>  
			implements WechatCommentService {
	
	@Autowired
	private CommentService commentService;
	@Autowired
	private WechatSendArticleService wechatSendArticleService;
	@Autowired
	private WechatSendService wechatSendService;
	@Autowired
	private WechatFansService wechatFansService;
	
	@Override
	public ResponseInfo on(String appId, Long msgDataId, Long msgDataIndex) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appId);
		CommentRequest on = new CommentRequest();
		on.setMsgDataId(msgDataId);
		on.setIndex(msgDataIndex);
		commentService.openComment(on, validToken);
		WechatSendArticle article = 
				wechatSendArticleService.findArticle(msgDataId.toString(), msgDataIndex.intValue());
		article.setOpen(1);
		wechatSendArticleService.update(article);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo off(String appId, Long msgDataId, Long msgDataIndex) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appId);
		CommentRequest off = new CommentRequest();
		off.setMsgDataId(msgDataId);
		off.setIndex(msgDataIndex);
		commentService.closeComment(off, validToken);
		WechatSendArticle article = 
				wechatSendArticleService.findArticle(msgDataId.toString(), msgDataIndex.intValue());
		article.setOpen(0);
		wechatSendArticleService.update(article);
		return new ResponseInfo();
	}

	@Override
	public Page<WechatComment> getPages(List<String> appids, Boolean commentType, Date startTime, Date endTime, 
			Integer orderType, String comment,String msgDataId, Integer msgDataIndex, Pageable pageable) 
					throws GlobalException {
		return dao.getPages(appids, commentType, startTime, endTime, orderType, comment, msgDataId, 
				msgDataIndex, pageable);
	}

	@Override
	public ResponseInfo markelect(Integer id, Integer fansId) throws GlobalException {
		WechatComment comment = super.findById(id);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(comment.getAppId());
		MarkelectCommentRequest markelect = new MarkelectCommentRequest();
		markelect.setMsgDataId(Long.valueOf(comment.getMsgDataId()));
		markelect.setIndex(Long.valueOf(comment.getMsgDataIndex()));
		markelect.setUserCommentId(Long.valueOf(comment.getUserCommentId()));
		commentService.markelect(markelect, validToken);
		comment.setCommentType(true);
		//同步精选留言数
		WechatFans fans = wechatFansService.findById(fansId);
		Integer top = fans.getFansExt().getTopCommentCount();
		fans.getFansExt().setTopCommentCount(top + 1);
		wechatFansService.update(fans);
		super.update(comment);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo unmarkelect(Integer id, Integer fansId) throws GlobalException {
		WechatComment comment = super.findById(id);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(comment.getAppId());
		MarkelectCommentRequest markelect = new MarkelectCommentRequest();
		markelect.setMsgDataId(Long.valueOf(comment.getMsgDataId()));
		markelect.setIndex(Long.valueOf(comment.getMsgDataIndex()));
		markelect.setUserCommentId(Long.valueOf(comment.getUserCommentId()));
		commentService.markelect(markelect, validToken);
		comment.setCommentType(false);
		super.update(comment);
		//同步精选留言数
		WechatFans fans = wechatFansService.findById(fansId);
		Integer top = fans.getFansExt().getTopCommentCount();
		fans.getFansExt().setTopCommentCount(top - 1);
		wechatFansService.update(fans);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo reply(WechatCommentDto dto) throws GlobalException {
		WechatComment comment = super.findById(dto.getId());
		//已回复的留言不能回复
		if (StringUtils.isNotBlank(comment.getReplyContent())) {
			return new ResponseInfo(RPCErrorCodeEnum.WECHAT_REPLY_ALREADY_EXIST.getCode(),
					RPCErrorCodeEnum.WECHAT_REPLY_ALREADY_EXIST.getDefaultMessage(),false);
		}
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(comment.getAppId());
		MarkelectCommentRequest reply = new MarkelectCommentRequest();
		reply.setMsgDataId(Long.valueOf(comment.getMsgDataId()));
		reply.setIndex(Long.valueOf(comment.getMsgDataIndex()));
		reply.setUserCommentId(Long.valueOf(comment.getUserCommentId()));
		reply.setContent(dto.getReply());
		commentService.reply(reply, validToken);
		comment.setReplyContent(dto.getReply());
		comment.setReplyTime(new Date());
		super.update(comment);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo deleteReply(Integer id) throws GlobalException {
		WechatComment comment = super.findById(id);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(comment.getAppId());
		MarkelectCommentRequest reply = new MarkelectCommentRequest();
		reply.setMsgDataId(Long.valueOf(comment.getMsgDataId()));
		reply.setIndex(Long.valueOf(comment.getMsgDataIndex()));
		reply.setUserCommentId(Long.valueOf(comment.getUserCommentId()));
		commentService.deleteReply(reply, validToken);
		comment.setReplyContent(null);
		super.update(comment);
		return new ResponseInfo();
	}
	
	@Override
	public ResponseInfo deleteComment(Integer id) throws GlobalException {
		WechatComment comment = super.findById(id);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(comment.getAppId());
		MarkelectCommentRequest delete = new MarkelectCommentRequest();
		delete.setMsgDataId(Long.valueOf(comment.getMsgDataId()));
		delete.setIndex(Long.valueOf(comment.getMsgDataIndex()));
		delete.setUserCommentId(Long.valueOf(comment.getUserCommentId()));
		commentService.delete(delete, validToken);
		super.delete(comment);
		return new ResponseInfo();
	}

	@Override
	public void hand(List<String> appids, Date start, Date end) throws GlobalException {
		List<String> msgDatas = new ArrayList<String>(10);
		List<WechatSend> sends = wechatSendService.listWechatSend(appids, start, end);
		//过滤出只发送成功的数据
		sends = sends.stream().filter(x -> x.getStatus().equals(WechatConstants.SEND_STATUS_SUCCESS))
		.collect(Collectors.toList());
		for (WechatSend wechatSend : sends) {
			msgDatas.add(wechatSend.getMsgDataId());
		}
		//存在评论数据则全部物理删除
		dao.deleteComments(msgDatas);
		//判断评论数累加器
		LongAdder newAdder = new LongAdder();
		List<WechatComment> wechatComments = new ArrayList<WechatComment>(10);
		//得到当前月份群发成功的全部的文章
		List<WechatSendArticle> list = wechatSendArticleService.getArticlesBySend(msgDatas, start, end);
		//遍历文章, 取得全部评论
		for (WechatSendArticle wechatSendArticle : list) {
			//得到微信每篇文章的评论
			List<Comment> comments = new ArrayList<Comment>(10);
			ValidateToken validToken = new ValidateToken();
			validToken.setAppId(wechatSendArticle.getAppId());
			CommentListRequest commentListRequest = new CommentListRequest();
			commentListRequest.setMsgDataId(Long.valueOf(wechatSendArticle.getMsgDataId()));
			commentListRequest.setIndex(Long.valueOf(wechatSendArticle.getMsgDataIndex()));
			commentListRequest.setType(Long.valueOf(0));
			commentListRequest.setBegin(Long.valueOf(0));
			commentListRequest.setCount(Long.valueOf(39));
			newAdder.add(40);
			CommentResponse reponse = commentService.commentList(commentListRequest, validToken);
			//得到当前文章的评论
			List<Comment> commentList = reponse.getComment();
			if (!commentList.isEmpty()) {
				comments.addAll(commentList);
				//判断是否需要再次请求接口获取剩余评论数
				Long commentId = commentList.get(0).getUserCommentId();
				//如果评论数大于累加，则还需请求
				while (commentId > newAdder.longValue()) {
					commentListRequest.setType(newAdder.longValue());
					commentListRequest.setBegin(newAdder.longValue() + 39);
					CommentResponse reponses = commentService
							.commentList(commentListRequest, validToken);
					comments.addAll(reponses.getComment());
					newAdder.add(40);
				}
			}
			newAdder.reset();
			//遍历评论
			for (Comment comment : comments) {
				WechatComment wechatComment = new WechatComment();
				wechatComment.setAppId(wechatSendArticle.getAppId());
				wechatComment.setMsgDataId(wechatSendArticle.getMsgDataId());
				wechatComment.setMsgDataIndex(wechatSendArticle.getMsgDataIndex());
				wechatComment.setUserCommentId(comment.getUserCommentId().toString());
				if (comment.getReply() != null) {
					Date replyDate = new Date(comment.getReply().getCreateTime() * 1000);
					wechatComment.setReplyContent(comment.getReply().getContent());
					wechatComment.setReplyTime(replyDate);
				}
				Date commentDate = new Date(comment.getCreateTime() * 1000);
				wechatComment.setContent(comment.getContent());
				wechatComment.setCommentTime(commentDate);
				wechatComment.setCommentType(comment.getCommentType() == 1);
				wechatComment.setOpenid(comment.getOpenId());
				wechatComments.add(wechatComment);
			}
		}
		super.saveAll(wechatComments);
	}

	@Override
	public Long getUserCommentId(String msgDataId, Integer msgDataIndex) {
		return dao.getUserCommentId(msgDataId, msgDataIndex);
	}

	@Override
	public List<WechatComment> findByOpenId(String openId) {
		return dao.findByOpenidAndHasDeletedFalse(openId);
	}

}