/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */ 

package com.jeecms.common.weibo.api.statuses.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.Constants;
import com.jeecms.common.weibo.api.comment.WeiboCommentService;
import com.jeecms.common.weibo.api.statuses.WeiboStatusesService;
import com.jeecms.common.weibo.bean.request.comment.WeiboCommentRequest;
import com.jeecms.common.weibo.bean.request.statuses.StatusesRequest;
import com.jeecms.common.weibo.bean.response.comment.vo.CommentVO;
import com.jeecms.common.weibo.bean.response.comment.vo.ReplyVO;
import com.jeecms.common.weibo.bean.response.statuses.StatusesResponse;
import com.jeecms.common.weibo.bean.response.statuses.StatusesResponse.Statuses;
import com.jeecms.common.weibo.bean.response.statuses.vo.StatusesVO;

/**   
 * 微博请求接口
 * @author: ljw
 * @date:   2019年6月19日 下午3:49:41     
 */
@Service
public class WeiboStatusesServiceImpl implements WeiboStatusesService {
	
	@Autowired
	private WeiboCommentService weiboCommentService;

	/** 获取最新的提到登录用户的微博列表，即@我的微博 **/
	public static final String STATUSES_URL = Constants.BASE_URL.concat("statuses/mentions.json");
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String COUNT = "count";
	public static final String PAGE = "page";
	
	@Override
	public List<StatusesVO> getInfo(StatusesRequest request) throws GlobalException {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put(ACCESS_TOKEN, request.getAccessToken());
		params.put(COUNT, request.getCount().toString());
		params.put(PAGE, request.getPage().toString());
		StatusesResponse response = HttpUtil.getJsonBean(STATUSES_URL, params, 
				StatusesResponse.class);
		if (response.getErrorCode() == null) {
			//处理数据
			return dispose(response.getStatuses(), request.getAccessToken());
		} else {
			throw new GlobalException(
					new WeChatExceptionInfo(response.getErrorCode().toString(),
							response.getError(),false));
		}
	}
	
	/**
	 * 处理微博返回的数据
	* @Title: dispose 
	* @param statuses 微博集合
	*@throws GlobalException 异常
	 */
	protected List<StatusesVO> dispose(List<Statuses> statuses, String accessToken) throws GlobalException {
		List<StatusesVO> vos = new ArrayList<StatusesVO>();
		//遍历微博
		for (Statuses status : statuses) {
			StatusesVO vo = new StatusesVO();
			vo.setCreateTime(MyDateUtils.formatDate(MyDateUtils.parseTimeDate(status.getCreatedAt()), 
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
			vo.setId(status.getId());
			vo.setUser(status.getUser());
			vo.setText(status.getText());
			vo.setTotalNumber(status.getCommentsCount());
			//得到评论内容
			List<CommentVO> comments = weiboCommentService
					.findByWeiboID(new WeiboCommentRequest(accessToken,status.getId(), 50, 1));
			List<CommentVO> commentVOs = dealComment(comments);
			//按照评论ID排序
			commentVOs = commentVOs.stream()
					.sorted(Comparator.comparing(CommentVO::getCommentId).reversed())
			.collect(Collectors.toList());
			vo.setComments(commentVOs);
			vos.add(vo);
		}
		return vos;
	}
	
	/**处理评论回复**/
	public List<CommentVO> dealComment(List<CommentVO> comments) {
		List<CommentVO> reply = new ArrayList<CommentVO>(10);
		List<CommentVO> commentList = new ArrayList<CommentVO>(10);
		//得到评论的数据
		commentList = comments.stream().filter(x -> x.getReplyCommentId() == null)
		.collect(Collectors.toList());
		//先分离评论和回复的数据
		if (!comments.isEmpty()) {
			//得到回复的数据
			reply = comments.stream().filter(x -> x.getReplyCommentId() != null)
			.collect(Collectors.toList());
			//根据回复里的ID分组
			Map<Long, List<CommentVO>> list = reply.stream()
					.collect(Collectors
							.groupingBy(CommentVO::getReplyCommentId));
			//得到来源评论的ID
			for (Long key : list.keySet()) {
				Optional<CommentVO> vo = comments.stream().filter(x -> x.getCommentId().equals(key))
						.findFirst();
				if (vo.isPresent()) {
					List<CommentVO> replyList = list.get(key);
					vo.get().setReplys(transform(replyList));
				}
				
			}
		}
		return commentList;
	}
	
	/**将commentVo转ReplyVo**/
	public List<ReplyVO> transform(List<CommentVO> replyList) {
		List<ReplyVO> vos = new ArrayList<ReplyVO>();
		for (CommentVO commentVO : replyList) {
			ReplyVO vo = new ReplyVO();
			vo.setCommentId(commentVO.getCommentId());
			vo.setCommentText(commentVO.getCommentText());
			vo.setCreateTime(commentVO.getCreateTime());
			vo.setScreenName(commentVO.getUser().getScreenName());;
			vos.add(vo);
		}
		return vos;
	}
}