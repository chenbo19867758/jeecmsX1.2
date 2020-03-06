/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.dao.ext.MemberMessageDaoExt;
import com.jeecms.member.domain.MemberMessage;

/**
 * 用户接收信息状态
* @author ljw
* @version 1.0
* @date 2018-09-25
*/
public interface MemberMessageDao extends IBaseDao<MemberMessage, Integer>, MemberMessageDaoExt {

    /**
     * 根据messageId查询会员信息
     * @param messageId 消息ID
     * @return List<MemberMessage>
     */
    List<MemberMessage> findByMessageId(Integer messageId);
}
