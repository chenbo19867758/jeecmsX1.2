/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.request.user;

import com.jeecms.common.weibo.bean.request.BaseRequest;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 根据用户ID获取用户信息
 * @url https://api.weibo.com/2/friendships/followers.json
 * @author: ljw
 * @date: 2019年6月17日 上午11:28:24
 */
public class WeiboFansRequest extends BaseRequest {

	// 参数uid与screen_name二者必选其一，且只能选其一；
	/** 需要查询的用户ID。 **/
	private Long uid;
	/** 需要查询的用户昵称。 **/
	@XStreamAlias("screen_name")
	private String screenName;
	/**单页返回的记录条数，默认为5，最大不超过5。**/
	private Integer count;
	/**返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0**/
	private Integer cursor;
	/**返回值中user字段中的status字段开关，0：返回完整status字段、1：status字段仅返回status_id，默认为1。**/
	@XStreamAlias("trim_status")
	private Integer trimStatus;
	
	public WeiboFansRequest() {
		super();
	}

	public WeiboFansRequest(String accessToken, Long uid) {
		super(accessToken);
		this.uid = uid;
	}
	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCursor() {
		return cursor;
	}

	public void setCursor(Integer cursor) {
		this.cursor = cursor;
	}

	public Integer getTrimStatus() {
		return trimStatus;
	}

	public void setTrimStatus(Integer trimStatus) {
		this.trimStatus = trimStatus;
	}

}
