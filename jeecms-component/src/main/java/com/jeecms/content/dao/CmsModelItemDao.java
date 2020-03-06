/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.domain.CmsModelItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 模型字段dao接口
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月19日 上午8:58:34
 */
public interface CmsModelItemDao extends IBaseDao<CmsModelItem, Integer> {

	/**
	 * 根据模型查询List模型字段list集合
	 * 
	 * @Title: findByModelId
	 * @param modelId 模型Id
	 * @return: List
	 */
	@Query("select bean from CmsModelItem bean where 1 = 1 and bean.modelId = ?1 and bean.hasDeleted = false")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModelItem> findByModelId(Integer modelId);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModelItem> findByModelIdAndGroupTypeAndHasDeleted(Integer modelId, String groupType, Boolean hasDeleted);

	/**
	 * 删除对应的模型下面的字段信息
	 * 
	 * @Title: deleteModelItems
	 * @param modelId 模型id
	 * @return: void
	 */
	@Query("delete from CmsModelItem bean where 1 = 1 and bean.modelId=?1")
	@Modifying
	void deleteModelItems(Integer modelId);

	/**
	 * 通过模型id和数据类型查询未被删除的模型子弹
	 * @Title: findByModelIdAndDataTypeAndHasDeleted  
	 * @param modelId	模型id
	 * @param type	数据类型
	 * @param hasDeleted	是否删除
	 * @return: List
	 */
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<CmsModelItem> findByModelIdAndDataTypeAndHasDeleted(Integer modelId,String type,Boolean hasDeleted);
	
}
