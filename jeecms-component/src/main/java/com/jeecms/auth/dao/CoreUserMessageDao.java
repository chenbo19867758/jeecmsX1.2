/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.auth.dao.ext.CoreUserMessageDaoExt;
import com.jeecms.auth.domain.CoreUserMessage;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * 用户接收信息Dao
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-01-23
 */
public interface CoreUserMessageDao extends IBaseDao<CoreUserMessage, Integer>, CoreUserMessageDaoExt {

        /**
         * 根据messageId查询平台信息
         * 
         * @param messageId
         * @return
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<CoreUserMessage> findByMessageId(Integer messageId);

}
