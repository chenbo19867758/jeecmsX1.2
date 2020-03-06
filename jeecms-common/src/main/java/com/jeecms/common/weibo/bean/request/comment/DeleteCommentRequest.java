/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.bean.request.comment;

import com.jeecms.common.weibo.bean.request.BaseRequest;

/**   
 * 删除评论
 * @url https://api.weibo.com/2/comments/destroy.json
 * @author: ljw
 * @date:   2019年6月20日 下午2:05:59     
 */
public class DeleteCommentRequest extends BaseRequest {

	/**要删除的评论ID，只能删除登录用户自己发布的评论。**/
	private Long cid;

	public DeleteCommentRequest(){}
	
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
}
