/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.bean.request.mp.message.MessageRequest;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.vo.WechatFansVO;

/**
 * 微信粉丝Service
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-28
 * 
 */
public interface WechatFansService extends IBaseService<WechatFans, Integer> {

	/**
	 * 同步粉丝
	 * 
	 * @param appid 公众号ID
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo sync(String appid) throws GlobalException;

	/**
	 * 同步多个粉丝
	 * 
	 * @param appid 公众号ID
	 * @param ids   粉丝ID集合
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo syncFans(String appid, Integer[] ids) throws GlobalException;

	/**
	 * 同步黑名单
	 * 
	 * @param appid 公众号ID
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo syncblack(String appid) throws GlobalException;
	
	/**
	 * 黑名单
	 * 
	 * @param appid 公众号ID
	 * @param nickname 昵称
	 * @param pageable 分页
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo blackList(String appid,String nickname, Pageable pageable) throws GlobalException;
	
	/**
	 * 拉黑粉丝
	 * 
	 * @param appid 公众号ID
	 * @param ids   粉丝ID集合
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo black(String appid, Integer[] ids) throws GlobalException;

	/**
	 * 取消拉黑粉丝
	 * 
	 * @param appid 公众号ID
	 * @param ids   粉丝ID集合
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo cancelblack(String appid, Integer[] ids) throws GlobalException;

	/**
	 * 发送消息
	 * @param appid 公众号ID
	 * @param messageRequest 信息对象
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	ResponseInfo send(String appid, MessageRequest messageRequest) throws GlobalException;

	/**
	 * 查询粉丝关注的数量
	 * 
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param appId 公众号ID
	 * @return Integer 数量
	 * @throws GlobalException 异常
	 */
	Integer timeCount(Long startTime, Long endTime, String appId) throws GlobalException;

	/**
	 * 查询粉丝总数
	 * 
	 * @param appId 公众号ID
	 * @return int 总数
	 * @throws GlobalException 异常
	 */
	Long fansCount(String appId) throws GlobalException;

	/**
	 * 根据nickname, tagid查询粉丝列表分页
	 * 
	 * @Title: getFansPage
	 * @param appids    公众号IDs
	 * @param pageable 分页参数
	 * @param nickname 昵称
	 * @param tagid    标签ID
	 * @param black    是否黑名单
	 * @throws GlobalException 全局异常
	 * @return: ResponseInfo
	 */
	ResponseInfo getFansPage(List<String> appids, Pageable pageable, String nickname, String tagid, Boolean black)
			throws GlobalException;

	/**
	 * 查询粉丝的男女数量
	 * 
	 * @Title: selectFansSex
	 * @param appId 公众号ID
	 * @throws GlobalException 全局异常
	 * @return: List 
	 */
	List<String> selectFansSex(String appId) throws GlobalException;

	/**
	 * 查询粉丝的关注渠道的数量
	 * 
	 * @Title: selectFansSubscribe
	 * @param appId 公众号ID
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<String> selectFansSubscribe(String appId) throws GlobalException;

	/**
	 * 查询世界各个 国家 的用户的数量
	 * 
	 * @Title: selectFansCountry
	 * @param appId 公众号ID
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<String> selectFansCountry(String appId) throws GlobalException;

	/**
	 * 查询中国地区各个 省区 的用户的数量
	 * 
	 * @Title: selectFansProvince
	 * @param appId 公众号ID
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<String> selectFansProvince(String appId) throws GlobalException;

	/**
	 * 查询开通了会员的用户的数量
	 * 
	 * @Title: selectFansCount
	 * @param appId 公众号ID
	 * @return: int 数量
	 */
	Integer selectFansCount(String appId);
	
	/**
	 * 根据多个APPID查询粉丝列表
	* @Title: getListByAppid 
	* @param appids 公众号IDs
	* @return List
	 */
	List<WechatFans> getListByAppid(List<String> appids);
	
	/**
	 * 获取粉丝详情
	* @Title: getFans 
	* @param openId 粉丝开放ID
	* @return
	 */
	ResponseInfo getFans(String openId) throws GlobalException;
	
	/**
	 * 根据分组查询
	* @Title: getListByAppid 
	* @param appids 公众号IDs
	* @param type 分组类型
	* @param provinceName 省级名称
	* @return List
	 */
	List<WechatFansVO> fansVOs(List<String> appids, Integer type, String provinceName);
}
