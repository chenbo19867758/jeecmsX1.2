/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.ContentTag;

import java.util.List;

import org.springframework.data.jpa.repository.Query;


/**
 * tag词Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
public interface ContentTagDao extends IBaseDao<ContentTag, Integer> {

        /**
         * 通过tag词查找
         *
         * @param tagName tag词
         * @param siteId  站点id
         * @return ContentTag
         */
        @Query("select bean from ContentTag bean where bean.tagName = ?1 and bean.siteId = ?2")
        ContentTag findByTagName(String tagName, Integer siteId);
        
        List<ContentTag> findBySiteIdAndHasDeleted(Integer siteId,Boolean hasDeledted);
}
