/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.api.mp;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.message.MessageRequest;
import com.jeecms.common.wechat.bean.request.mp.user.BatchblackListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.BlackFansListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsAddRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsDeleteRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsUpdateRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserInfoRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserTagsAddRequest;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.user.MessageResponse;
import com.jeecms.common.wechat.bean.response.mp.user.TagsAddResponse;
import com.jeecms.common.wechat.bean.response.mp.user.TagsResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserInfoListResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserListResponse;

/**
 * 微信公众号用户管理接口集
 * @author: ljw
 * @date: 2018年7月30日 下午4:15:46
 */
public interface UserManageService {

	/**
	 * 获取全部用户列表
	 * 
	 * @param userListRequest 请求
	 * @param validToken 令牌
	 * @return List 用户集合
	 * @throws GlobalException 异常
	 */
	List<UserInfoListResponse> getUserList(UserListRequest userListRequest, ValidateToken validToken)
			throws GlobalException;

	/**
	 * 批量获取用户列表
	 * 
	 * @param userInfoRequest 请求
	 * @param validToken 令牌
	 * @return UserInfoListResponse
	 * @throws GlobalException 异常
	 */
	UserInfoListResponse fansMore(UserInfoRequest userInfoRequest, ValidateToken validToken) throws GlobalException;

	/**
	 * 获取公众号已创建的标签
	 * 
	 * @param validToken 令牌
	 * @return TagsResponse
	 * @throws GlobalException 异常
	 */
	TagsResponse tags(ValidateToken validToken) throws GlobalException;

	/**
	 * 添加标签
	 * 
	 * @param tagsAddRequest 请求
	 * @param validToken 令牌
	 * @return TagsAddResponse
	 * @throws GlobalException 异常
	 */
	TagsAddResponse addTags(TagsAddRequest tagsAddRequest, ValidateToken validToken) throws GlobalException;

	/**
	 * 删除标签
	 * 
	 * @param tagsDeleteRequest 请求
	 * @param validToken 令牌
	 * @return BaseResponse 
	 * @throws GlobalException 异常
	 */
	BaseResponse deleteTags(TagsDeleteRequest tagsDeleteRequest, ValidateToken validToken) throws GlobalException;

	/**
	 * 修改标签
	 * 
	 * @param tagsUpdateRequest 请求
	 * @param validToken 令牌
	 * @return BaseResponse
	 * @throws GlobalException 异常
	 */
	BaseResponse updateTags(TagsUpdateRequest tagsUpdateRequest, ValidateToken validToken) throws GlobalException;

	/**
	 * 拉黑用户列表
	 * 
	 * @param blackFansListRequest 请求
	 * @param validToken 令牌
	 * @return UserListResponse  
	 * @throws GlobalException 异常
	 */
	UserListResponse batchBlackList(BlackFansListRequest blackFansListRequest, ValidateToken validToken)
			throws GlobalException;

	/**
	 * 拉黑用户
	 * 
	 * @param batchblackListRequest 请求
	 * @param validToken 令牌
	 * @return BaseResponse
	 * @throws GlobalException 异常
	 */
	BaseResponse batchBlack(BatchblackListRequest batchblackListRequest, ValidateToken validToken)
			throws GlobalException;

	/**
	 * 取消拉黑用户
	 * 
	 * @param batchblackListRequest 请求
	 * @param validToken 令牌
	 * @return BaseResponse
	 * @throws GlobalException 异常
	 */
	BaseResponse cancelbatchBlack(BatchblackListRequest batchblackListRequest, ValidateToken validToken)
			throws GlobalException;

	/**
	 * 用户添加标签
	 * 
	 * @param userTagsAddRequest 请求
	 * @param validToken 令牌
	 * @return BaseResponse
	 * @throws GlobalException 异常
	 */
	BaseResponse addUserTags(UserTagsAddRequest userTagsAddRequest, ValidateToken validToken) 
			throws GlobalException;

	/**
	 * 发送信息
	 * 
	 * @param messageRequest 请求
	 * @param validToken 令牌
	 * @return MessageResponse
	 * @throws GlobalException 异常
	 */
	MessageResponse send(MessageRequest messageRequest, ValidateToken validToken) throws GlobalException;

}
