/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.member.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.member.dao.ext.MemberMessageDaoExt;
import com.jeecms.member.domain.MemberMessage;
import com.jeecms.member.domain.querydsl.QMemberMessage;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.system.domain.querydsl.QSysMessage;
import com.jeecms.system.domain.vo.MessageVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * @Description:会员消息接口实现类
 * @author: ljw
 * @date:   2019年5月13日 上午9:36:19     
 */
public class MemberMessageDaoImpl extends BaseDao<MemberMessage> implements MemberMessageDaoExt{

    @Override
    public Page<MessageVo> getSysMessagePage(Integer groupId,Integer levelId, Pageable pageable) {
        
        QSysMessage qSysMessage = QSysMessage.sysMessage;
        QMemberMessage qMemberMessage = QMemberMessage.memberMessage;
        BooleanBuilder boolbuild = new BooleanBuilder();
        // 排除删除的信息
        boolbuild.and(qMemberMessage.status.isNull().or(qMemberMessage.status.eq(SysMessage.MESSAGE_STATUS_NORMAL)))                       
                        .and(
                            //接收对象为全部        
                            qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL)
                            //全部会员
                            .or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL_MEMBER))
                            //指定会员(暂时不需要)
                            //.or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_APPOINT_ADMIN).and(qSysMessage.userId.eq(userId)))
                            //会员组
                            .or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_MEMBER_GROUP).and(qSysMessage.memeberGroupId.eq(groupId)))
                            //会员等级
                            .or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_MEMBER_LELVEL).and(qSysMessage.memeberLevelId.eq(levelId)))
                            );
        JPAQuery<MessageVo> query = super.getJpaQueryFactory()
                .select(
                        Projections.bean(
                                MessageVo.class, 
                                qSysMessage.id.as("messageId"), 
                                qMemberMessage.id.as("memberMessageId"), 
                                qSysMessage.title.as("title"),
                                qSysMessage.content.as("content"),                                 
                                qSysMessage.createTime.as("createTime"),
                                qMemberMessage.status.as("status")                           
                                ))
                .from(qSysMessage)
                .leftJoin(qMemberMessage)
                .on(qSysMessage.id.eq(qMemberMessage.messageId))           
                .where(boolbuild)
                .orderBy(qSysMessage.createTime.desc());
        return QuerydslUtils.page(query, pageable, null);
    }

    @Override
    public Page<MessageVo> getPriMessagePage(Integer memberId, Pageable pageable) {
                
        QSysMessage qSysMessage = QSysMessage.sysMessage;
        QMemberMessage qMemberMessage = QMemberMessage.memberMessage;
        BooleanBuilder boolbuild = new BooleanBuilder();
        // 排除删除的信息
        boolbuild.and(qMemberMessage.status.isNull().or(qMemberMessage.status.eq(1)))                       
                        .and(                        
                            //用户ID
                            qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_APPOINT_MEMBER)
                            .and(qSysMessage.memeberId.eq(memberId))
                            );
        JPAQuery<MessageVo> query = super.getJpaQueryFactory()
                .select(
                        Projections.bean(
                                MessageVo.class, 
                                qSysMessage.id.as("messageId"), 
                                qMemberMessage.id.as("memberMessageId"), 
                                qSysMessage.title.as("title"),
                                qSysMessage.content.as("content"),                                 
                                qSysMessage.createTime.as("createTime"),
                                qMemberMessage.status.as("status")                   
                            ))
                .from(qSysMessage)
                .leftJoin(qMemberMessage)
                .on(qSysMessage.id.eq(qMemberMessage.messageId))           
                .where(boolbuild)
                .orderBy(qSysMessage.createTime.desc());
        return QuerydslUtils.page(query, pageable, null);
    }

    @Override
    public Long unreadNumSys(Integer groupId, Integer levelId) throws GlobalException {
        QSysMessage qSysMessage = QSysMessage.sysMessage;
        QMemberMessage qMemberMessage = QMemberMessage.memberMessage;
        BooleanBuilder boolbuild = new BooleanBuilder();
        // 得到未读的数量
        boolbuild.and(qMemberMessage.status.isNull())                       
                        .and(
                                //接收对象为全部        
                                qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL)
                                //全部会员
                                .or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL_MEMBER))
                                //指定会员(暂时不需要)
                                //.or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_APPOINT_ADMIN).and(qSysMessage.userId.eq(userId)))
                                //会员组
                                .or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_MEMBER_GROUP).and(qSysMessage.memeberGroupId.eq(groupId)))
                                //会员等级
                                .or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_MEMBER_LELVEL).and(qSysMessage.memeberLevelId.eq(levelId)))
                            );
        JPAQuery<Long> query = super.getJpaQueryFactory()
                .select(qMemberMessage.status.count())
                .from(qSysMessage)
                .leftJoin(qMemberMessage)
                .on(qSysMessage.id.eq(qMemberMessage.messageId))           
                .where(boolbuild);               
        return query.fetchCount();
    }

    @Override
    public Long unreadNumPri(Integer memberId) throws GlobalException {
        QSysMessage qSysMessage = QSysMessage.sysMessage;
        QMemberMessage qMemberMessage = QMemberMessage.memberMessage;
        BooleanBuilder boolbuild = new BooleanBuilder();
        // 排除删除的信息
        boolbuild.and(qMemberMessage.status.isNull())                       
                        .and(                        
                              //用户ID
                              qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_APPOINT_MEMBER)
                              .and(qSysMessage.memeberId.eq(memberId))
                            );
        JPAQuery<Long> query = super.getJpaQueryFactory()
                .select(qMemberMessage.status.count())
                .from(qSysMessage)
                .leftJoin(qMemberMessage)
                .on(qSysMessage.id.eq(qMemberMessage.messageId))           
                .where(boolbuild);
        return query.fetchCount();
    }  
  
}
