package com.jeecms.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.MiniprogramMember;

/**
 * 小程序成员管理dao层接口
 * @author: chenming
 * @date:   2019年6月13日 上午11:25:51
 */
public interface MiniprogramMemberDao extends IBaseDao<MiniprogramMember, Integer> {
	
	/**
	 * 根据appId获取到一个小程序所有的体验者列表
	 * @Title: getMember  
	 * @param appId	小程序appId
	 * @return: List
	 */
	@Query("select bean from MiniprogramMember bean where 1 = 1 and bean.hasDeleted = 0 and bean.appId=?1")
	List<MiniprogramMember> getMember(String appId);
	
	/**
     * 查看该微信号是否已经绑定了此小程序
	 * @Title: isExist  
	 * @param appId	小程序appId
	 * @param wechatId	微信号
	 * @return: List
	 */
	@Query("select bean from MiniprogramMember bean where 1 = 1 and bean.hasDeleted = 0 and bean.appId=?1 and bean.wechatId=?2")
	List<MiniprogramMember> isExist(String appId,String wechatId);
}
