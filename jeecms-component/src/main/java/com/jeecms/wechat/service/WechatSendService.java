/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.WechatSend;

/**
 * 微信发送记录Service
* @author ljw
* @version 1.0
* @date 2019-05-28
*/
public interface WechatSendService extends IBaseService<WechatSend, Integer> {

	/**
	 * 保存群发的对象
	 * @Title: saveWechatSend  
	 * @param wechatSend 微信群发对象
	 * @throws Exception 程序异常    
	 * @return: ResponseInfo
	 */
	ResponseInfo saveWechatSend(WechatSend wechatSend) throws Exception;
	
	/**
	 * 立即群发
	 * @Title: saveWechatSend  
	 * @param wechatSend 微信群发对象
	 * @throws Exception 程序异常    
	 * @return: ResponseInfo
	 */
	ResponseInfo send(WechatSend wechatSend) throws Exception;
	
	/**
	 * 修改群发的对象
	 * @Title: updateWechatSend  
	 * @param wechatSend 群发对象
	 * @throws Exception 异常     
	 * @return: ResponseInfo
	 */
	ResponseInfo updateWechatSend(WechatSend wechatSend) throws Exception;
	
	/**
	 * 获取该公众号本月群发列表
	 * @Title: listWechatSend  
	 * @param appids 公众号IDs
	 * @param start 开始时间
	 * @param end 结束时间
	 * @throws GlobalException 全局异常     
	 * @return: ResponseInfo
	 */
	List<WechatSend> listWechatSend(List<String> appids, Date start, Date end) throws GlobalException;
	
	/**
	 * 获取该公众号本月群发列表分页
	 * @Title: listWechatSend  
	 * @param appids 公众号IDs
	 * @param start 分页
	 * @param end 年份月份
	 * @param status 发送状态
	 * @param title 图文标题
	 * @param pageable 分页对象
	 * @throws GlobalException 全局异常     
	 * @return: ResponseInfo
	 */
	Page<WechatSend> pageWechatSend(List<String> appids, Date start, Date end, Integer status, String title, 
			Pageable pageable) throws GlobalException;
	
	/**
	 * 删除群发设置
	 * @Title: deleteWechatSend  
	 * @param ids 群发对象集合IDS
	 * @throws Exception 异常    
	 * @return: ResponseInfo
	 */
	ResponseInfo deleteWechatSend(Integer[] ids) throws Exception;
	
	/**判断服务号本月是否能发送信息**/
	Boolean service(String appId, Date date);
}
