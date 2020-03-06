/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.ext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.vo.MessageVo;

/**   
 * @Description:会员消息扩展dao接口
 * @author: ljw
 * @date:   2018年6月13日 上午9:57:51     
 */
public interface MemberMessageDaoExt {

    /**
     * 获取系统公告
     * @param groupId 会员组ID
     * @param levelId 等级ID
     * @param pageable
     * @return
     */
    Page<MessageVo> getSysMessagePage(Integer groupId, Integer levelId, Pageable pageable);
    
    /**
     * 获取私信
     * @param memberId
     * @param pageable
     * @return
     */
    Page<MessageVo> getPriMessagePage(Integer memberId, Pageable pageable);
    
    /**
     * 系统公告未读的信息数量
     * @param groupId 会员组ID
     * @param levelId 等级ID
     * @return
     * @throws GlobalException
     */
    Long unreadNumSys(Integer groupId, Integer levelId) throws GlobalException;
    
    /**
     * 私人未读的信息数量
     * @param memberId 会员ID
     * @return
     * @throws GlobalException
     */
    Long unreadNumPri(Integer memberId) throws GlobalException;
}
