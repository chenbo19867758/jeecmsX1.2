/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.SearchWordDaoExt;
import com.jeecms.system.domain.SysSearchWord;
import org.springframework.data.jpa.repository.Query;


/**
 * 搜索词Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
public interface SearchWordDao extends IBaseDao<SysSearchWord, Integer>, SearchWordDaoExt {


        /**
         * 通过搜索词和站点id查找
         *
         * @param word   搜索词
         * @param siteId 站点id
         * @return SysSearchWord
         */
        @Query("select bean from SysSearchWord bean where bean.word = ?1 and bean.siteId = ?2")
        SysSearchWord findByWord(String word, Integer siteId);
}
