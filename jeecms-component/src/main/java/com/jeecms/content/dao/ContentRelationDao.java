/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.domain.ContentRelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 内容"相关内容"dao接口
 *
 * @author: chenming
 * @date: 2019年6月21日 下午4:29:32
 */
public interface ContentRelationDao extends IBaseDao<ContentRelation, Integer> {

	/**
	 * 通过内容id查找相关内容
	 *
	 * @param contentId 内容id
	 * @return 相关内容列表
	 */
	@Query("select bean from ContentRelation  bean where bean.contentId = ?1")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<ContentRelation> findByContentId(Integer contentId);
}
