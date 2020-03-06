/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.WechatFansTag;
import com.jeecms.wechat.domain.dto.WechatTagDto;

/**
 * 微信粉丝标签Service
* @author ljw
* @version 1.0
* @date 2019-05-28
*/
public interface WechatFansTagService extends IBaseService<WechatFansTag, Integer> {
	
	/**
	 *  新增标签
	* @Title: addTags 
	* @param dto 传输对象 
	* @return 响应对象
	* @throws GlobalException 异常
	 */
	ResponseInfo addTags(WechatTagDto dto) throws GlobalException;
	
	/**
	 *  删除标签
	* @Title: deleteTags 
	* @param appid 公众号ID
	* @param tagid 标签ID
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo deleteTags(String appid, Integer tagid) throws GlobalException;
	
	/**
	 * 根据appid删除标签的信息
	* @Title: deleteTags 
	* @param appid 公众号ID
	 */
	void deleteTags(String appid);
	
	/**
	 * 编辑标签
	* @Title: updateTags 
	* @param dto 传输对象 
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo updateTags(WechatTagDto dto) throws GlobalException;
	
	/**
	 * 设置用户标签
	 * @param dto 传输Dto
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo setTags(WechatTagDto dto) throws GlobalException;

	/**
	 * 同步标签
	 * @param appid 公众号ID
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo syncTags(String appid) throws GlobalException;

	/**
	 * 获取该公众号下的全部标签
	 * @param appid appid 公众号ID
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo tags(String appid) throws GlobalException;

	/**
	 * 根据appid查询标签列表
	 * @param appid 公众号ID
	 * @return
	 */
	List<WechatFansTag> selectTags(String appid);
		
	/**
	 * 根据appid和id查询标签的信息
	* @Title: selectTags 
	* @param appid 公众号ID
	* @param id 标签ID
	* @return
	 */
	WechatFansTag selectTags(String appid, Integer id);
	
	/**
	 * 根据appid和tagName检查标签
	* @Title: selectTags 
	* @param appid 公众号ID
	* @param tagName 标签名称
	* @return
	 */
	Boolean check(String appid, String tagName);
}
