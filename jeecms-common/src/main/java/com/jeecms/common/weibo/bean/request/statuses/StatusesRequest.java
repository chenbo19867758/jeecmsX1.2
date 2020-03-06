/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.request.statuses;

import com.jeecms.common.weibo.bean.request.BaseRequest;

/**
 * 获取最新的提到登录用户的微博列表，即@我的微博
 * 
 * @URL https://api.weibo.com/2/statuses/mentions.json
 * @author: ljw
 * @date: 2019年6月19日 下午2:32:53
 */
public class StatusesRequest extends BaseRequest {

	/** 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。 **/
	private Long sinceId;
	/** 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。 **/
	private Long maxId;
	/** 单页返回的记录条数，最大不超过200，默认为20。 **/
	private Integer count;
	/** 返回结果的页码，默认为1。 **/
	private Integer page;
	/** 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。 **/
	private Integer filterByAuthor;
	/** 来源筛选类型，0：全部、1：来自微博、2：来自微群，默认为0。 **/
	private Integer filterBySource;
	/** 原创筛选类型，0：全部微博、1：原创的微博，默认为0。 **/
	private Integer filterByType;

	public StatusesRequest() {
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

	public Integer getFilterByAuthor() {
		return filterByAuthor;
	}

	public void setFilterByAuthor(Integer filterByAuthor) {
		this.filterByAuthor = filterByAuthor;
	}

	public Integer getFilterBySource() {
		return filterBySource;
	}

	public void setFilterBySource(Integer filterBySource) {
		this.filterBySource = filterBySource;
	}

	public Integer getFilterByType() {
		return filterByType;
	}

	public void setFilterByType(Integer filterByType) {
		this.filterByType = filterByType;
	}

}
