/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.bean.response.comment;

import java.util.ArrayList;
import java.util.List;

import com.jeecms.common.weibo.bean.response.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**   
 * 根据微博ID查询评论列表返回的数据
 * @author: ljw
 * @date:   2019年6月20日 上午11:16:33     
 */
public class WeiboCommentResponse extends BaseResponse {

	/**评论总数**/
	@XStreamAlias("total_number")
	private Integer totalNumber;
	/**评论内容列表**/
	private List<UserCommentResponse> comments = new ArrayList<UserCommentResponse>(16);

	public WeiboCommentResponse() {
		super();
	}
	
	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public List<UserCommentResponse> getComments() {
		return comments;
	}

	public void setComments(List<UserCommentResponse> comments) {
		this.comments = comments;
	} 
	
}
