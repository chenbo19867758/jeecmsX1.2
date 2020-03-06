/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.dao.ext.ContentDaoExt;
import com.jeecms.content.domain.Content;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 内容主体dao接口
 * @author: chenming
 * @date:   2019年5月6日 下午2:32:49
 */
public interface ContentDao extends IBaseDao<Content, Integer>, ContentDaoExt {
	
	@Query("select bean from Content bean where 1 = 1 and bean.id = ?1 and bean.hasDeleted = false")
	Content findContent(Integer id);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Content> findByChannelIdInAndHasDeleted(Integer[] channelIds,Boolean hasDeleted);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Content> findByChannelIdAndRecycleAndHasDeleted(Integer chanenlId,Boolean recycle,Boolean hasDeleted);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Content findByIdAndRecycleAndHasDeleted(Integer contentId,Boolean recycle,Boolean hasDeleted);
	
}
