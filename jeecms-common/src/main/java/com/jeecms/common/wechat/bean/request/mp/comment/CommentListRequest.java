/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.comment;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 查看指定文章的评论数据
 * 
 * @link https://api.weixin.qq.com/cgi-bin/comment/list?access_token=ACCESS_TOKEN
 *       http请求方式: POST（请使用https协议）
 * @author: ljw
 * @date: 2019年5月29日 上午11:19:24
 */
public class CommentListRequest {

	/** 群发返回的msg_data_id,必填 **/
	@XStreamAlias("msg_data_id")
	private Long msgDataId;
	/** 多图文时，用来指定第几篇图文，从0开始，不带默认返回该msg_data_id的第一篇图文,非必填 **/
	private Long index;
	/** 起始位置,必填 **/
	private Long begin;
	/** 获取数目（>=50会被拒绝）,必填 **/
	private Long count;
	/** type=0 普通评论&精选评论 type=1 普通评论 type=2 精选评论，必填 **/
	private Long type;

	public CommentListRequest() {
	}

	public Long getMsgDataId() {
		return msgDataId;
	}

	public void setMsgDataId(Long msgDataId) {
		this.msgDataId = msgDataId;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Long getBegin() {
		return begin;
	}

	public void setBegin(Long begin) {
		this.begin = begin;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

}
