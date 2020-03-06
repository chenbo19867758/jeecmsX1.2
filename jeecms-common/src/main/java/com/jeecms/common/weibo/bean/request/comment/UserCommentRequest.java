/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.request.comment;

import com.jeecms.common.weibo.bean.request.BaseRequest;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 获取当前登录用户所发出的评论列表 && 收到的评论 && @我的评论
 * 
 * @url https://api.weibo.com/2/comments/by_me.json
 * @url https://api.weibo.com/2/comments/to_me.json
 * @url https://api.weibo.com/2/comments/mentions.json
 * @author: ljw
 * @date: 2019年6月18日 下午2:49:23
 */
public class UserCommentRequest extends BaseRequest {

	/** 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。 **/
	@XStreamAlias("since_id")
	private Long sinceId;
	/** 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。 **/
	@XStreamAlias("max_id")
	private Long maxId;
	/** 单页返回的记录条数，默认为50。 **/
	private Integer count;
	/** 返回结果的页码，默认为1。。 **/
	private Integer page;
	/** 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。 **/
	@XStreamAlias("filter_by_author")
	private Integer filterByAuthor;	
	/** 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。。 **/
	@XStreamAlias("filter_by_source")
	private Integer filterBySource;

	public UserCommentRequest() {
		super();
	}
	
	public Long getSinceId() {
		return sinceId;
	}

	public void setSinceId(Long sinceId) {
		this.sinceId = sinceId;
	}

	public Long getMaxId() {
		return maxId;
	}

	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getFilterBySource() {
		return filterBySource;
	}

	public void setFilterBySource(Integer filterBySource) {
		this.filterBySource = filterBySource;
	}

	public Integer getFilterByAuthor() {
		return filterByAuthor;
	}

	public void setFilterByAuthor(Integer filterByAuthor) {
		this.filterByAuthor = filterByAuthor;
	}

}
