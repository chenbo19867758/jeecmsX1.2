package com.jeecms.wechat.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.MiniprogramMember;

/**
 * 小程序成员管理service层接口
 * @author: chenming
 * @date:   2019年6月13日 上午11:22:45
 */
public interface MiniprogramMemberService extends IBaseService<MiniprogramMember, Integer> {
	
	/**
	 * 新增一个小程序体验者
	 * @Title: addMember  
	 * @param appId	小程序appId
	 * @param wechatId	微信号
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void addMember(String appId,String wechatId) throws GlobalException;
	
	/**
	 * 删除一个小程序体验者
	 * @Title: deleteMember  
	 * @param miniprogramMember	小程序成员管理实体类
	 * @throws GlobalException  全局异常
	 * @return: void
	 */
	void deleteMember(MiniprogramMember miniprogramMember) throws GlobalException;
	
	/**
	 * 根据appId获取到一个小程序所有的体验者列表
	 * @Title: getMember  
	 * @param appId	小程序appId
	 * @throws GlobalException   全局异常  
	 * @return: List
	 */
	List<MiniprogramMember> getMember(String appId) throws GlobalException;
	
	/**
	 * 查看该微信号是否已经绑定了此小程序
	 * @Title: isExist  
	 * @param appId	小程序appId
	 * @param wechatId	微信号
	 * @throws GlobalException  全局异常    
	 * @return: MiniprogramMember
	 */
	Boolean isExist(String appId,String wechatId) throws GlobalException;
	
}
