package com.jeecms.channel.dao;

import com.jeecms.channel.domain.ChannelContentTpl;
import com.jeecms.common.base.dao.IBaseDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 栏目内容目标dao接口
 * 
 * @author: chenming
 * @date: 2019年4月18日 上午9:40:51
 */
public interface ChannelContentTplDao
		extends JpaRepository<ChannelContentTpl, Integer>, IBaseDao<ChannelContentTpl, Integer> {

	/**
	 * 根据栏目Id进行检索
	 * 
	 * @Title: findByChannelId
	 * @param channelId 栏目Id
	 * @return: Set
	 */
	@Query("select bean from ChannelContentTpl bean where 1 = 1 and bean.channelId = ?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<ChannelContentTpl> findByChannelId(Integer channelId);

	/**
	 * 通过栏目Id和模型id进行检索
	 * @Title: findByChannelIdAndModelId  
	 * @param channelId	栏目id
	 * @param modelId	模型id
	 * @return: ChannelContentTpl
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	ChannelContentTpl findByChannelIdAndModelId(Integer channelId, Integer modelId);

	
	/**
	 * 通过栏目Id和是否勾选进行检索
	 * @Title: findByChannelIdAndModelId  
	 * @param channelId	栏目id
	 * @param select true勾选，false不勾选
	 * @return: ChannelContentTpl
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<ChannelContentTpl> findByChannelIdAndSelect(Integer channelId, Boolean select);
}
