package com.jeecms.wechat.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.AbstractWeChatInfoDaoExt;
import com.jeecms.wechat.domain.AbstractWeChatInfo;

/**
 * 微信公众号信息dao接口
 * @Description:AbstractWeChatToken
 * @author: qqwang
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface AbstractWeChatInfoDao extends IBaseDao<AbstractWeChatInfo, Integer>, AbstractWeChatInfoDaoExt {
	
	/**
	 * 根据appid获取AbstractWeChatInfo
	 * @Title: findByAppIdAndHasDeleted  
	 * @param appId 公众号、小程序的唯一标识
	 * @param hasDeleted 是否删除
	 * @return: AbstractWeChatInfo
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	AbstractWeChatInfo findByAppIdAndHasDeleted(String appId,Boolean hasDeleted);

	/**
	 * 获取站点下所有的未删除的公众号或小程序
	 * @Title: findByTypeAndSiteIdAndHasDeleted  
	 * @param type	类型：1-公众号，2-小程序
	 * @param siteId	站点id
	 * @return: List
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<AbstractWeChatInfo> findByTypeAndSiteIdAndHasDeleted(Short type, Integer siteId,Boolean hasDeleted);
	
	/**
	 * 查询站点下的默认快捷登录的公众号或小程序
	 * @Title: findByTypeAndSiteIdAndIsDefaultAuthAndHasDeleted  
	 * @param type	类型：1-公众号，2-小程序
	 * @param siteId	站点
	 * @param isDefaultAuth		是否默认
	 * @param hasDeleted	是否删除
	 * @return: AbstractWeChatInfo
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	AbstractWeChatInfo findByTypeAndSiteIdAndIsDefaultAuthAndHasDeleted(
			Short type,Integer siteId,Boolean isDefaultAuth,Boolean hasDeleted);
	
}
