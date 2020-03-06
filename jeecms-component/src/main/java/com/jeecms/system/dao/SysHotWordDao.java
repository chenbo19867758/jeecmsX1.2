/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.SysHotWordDaoExt;
import com.jeecms.system.domain.SysHotWord;


/**
 * 热词Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
public interface SysHotWordDao extends IBaseDao<SysHotWord, Integer>,SysHotWordDaoExt {

        /**
         * 通过热词名称查找热词
         *
         * @param hotWord 热词
         * @return
         */
        SysHotWord findByHotWord(String hotWord);
}
