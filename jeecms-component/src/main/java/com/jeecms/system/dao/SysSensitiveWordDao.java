/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.SysSensitiveWord;


/**
 * 敏感词Dao类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-29
 */
public interface SysSensitiveWordDao extends IBaseDao<SysSensitiveWord, Integer> {

        /**
         * 通过敏感词查询
         *
         * @param sensitiveWord 敏感词
         * @return SysSensitiveWord
         */
        SysSensitiveWord findBySensitiveWord(String sensitiveWord);
}
