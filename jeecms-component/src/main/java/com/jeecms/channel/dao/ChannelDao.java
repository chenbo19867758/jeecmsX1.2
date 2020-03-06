/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.dao;

import com.jeecms.channel.dao.ext.ChannelDaoExt;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.dao.IBaseDao;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 栏目dao
 * 
 * @author: tom
 * 
 * @date: 2019年3月19日 下午6:29:42
 */
public interface ChannelDao extends IBaseDao<Channel, Integer>, ChannelDaoExt {
	
	/**
	 * 通过模型id查询处没有加入回收站，没有删除的栏目集合
	 * @Title: findByModelIdAndRecycleAndHasDeleted  
	 * @param modelId		模型id
	 * @param recycle
	 * @param hasDeleted
	 * @return: List
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Channel> findByModelIdAndHasDeleted(Integer modelId,Boolean hasDeleted);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Channel> findBySiteIdAndHasDeleted(Integer siteId,Boolean hasDeleted);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Channel> findByWorkflowIdInAndHasDeleted(Integer[] workflowIds,Boolean hasDeleted);
}
