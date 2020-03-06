/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysHotWordCategory;


/**
 * 热词分类Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
public interface SysHotWordCategoryDao extends IBaseDao<SysHotWordCategory, Integer> {

        /**
         * 通过热词分类名查询
         *
         * @param cateName 分类名称
         * @return SysHotWordCategory
         */
        SysHotWordCategory findByCateName(String cateName);
}
