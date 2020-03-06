package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.MemberGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberGroup is a Querydsl query type for MemberGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberGroup extends EntityPathBase<MemberGroup> {

    private static final long serialVersionUID = -1640738387L;

    public static final QMemberGroup memberGroup = new QMemberGroup("memberGroup");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final ListPath<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel> contributeChannels = this.<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel>createList("contributeChannels", com.jeecms.channel.domain.Channel.class, com.jeecms.channel.domain.querydsl.QChannel.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath groupName = createString("groupName");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isAllChannelContribute = createBoolean("isAllChannelContribute");

    public final BooleanPath isAllChannelView = createBoolean("isAllChannelView");

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final StringPath remark = createString("remark");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final ListPath<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, com.jeecms.auth.domain.querydsl.QCoreUser.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel> viewChannels = this.<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel>createList("viewChannels", com.jeecms.channel.domain.Channel.class, com.jeecms.channel.domain.querydsl.QChannel.class, PathInits.DIRECT2);

    public QMemberGroup(String variable) {
        super(MemberGroup.class, forVariable(variable));
    }

    public QMemberGroup(Path<? extends MemberGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberGroup(PathMetadata metadata) {
        super(MemberGroup.class, metadata);
    }

}

