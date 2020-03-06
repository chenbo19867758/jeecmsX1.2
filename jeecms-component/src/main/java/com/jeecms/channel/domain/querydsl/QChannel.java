package com.jeecms.channel.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.channel.domain.Channel;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannel is a Querydsl query type for Channel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChannel extends EntityPathBase<Channel> {

    private static final long serialVersionUID = 1498075814L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannel channel = new QChannel("channel");

    public final com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain _super;

    public final ListPath<com.jeecms.channel.domain.ChannelAttr, QChannelAttr> channelAttrs = this.<com.jeecms.channel.domain.ChannelAttr, QChannelAttr>createList("channelAttrs", com.jeecms.channel.domain.ChannelAttr.class, QChannelAttr.class, PathInits.DIRECT2);

    public final QChannelExt channelExt;

    public final SetPath<Channel, QChannel> child = this.<Channel, QChannel>createSet("child", Channel.class, QChannel.class, PathInits.DIRECT2);

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> children;

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> childs;

    public final ListPath<com.jeecms.channel.domain.ChannelContentTpl, QChannelContentTpl> contentTpls = this.<com.jeecms.channel.domain.ChannelContentTpl, QChannelContentTpl>createList("contentTpls", com.jeecms.channel.domain.ChannelContentTpl.class, QChannelContentTpl.class, PathInits.DIRECT2);

    public final BooleanPath contribute = createBoolean("contribute");

    public final ListPath<com.jeecms.member.domain.MemberGroup, com.jeecms.member.domain.querydsl.QMemberGroup> contributeGroups = this.<com.jeecms.member.domain.MemberGroup, com.jeecms.member.domain.querydsl.QMemberGroup>createList("contributeGroups", com.jeecms.member.domain.MemberGroup.class, com.jeecms.member.domain.querydsl.QMemberGroup.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createTime;

    //inherited
    public final StringPath createUser;

    public final StringPath description = createString("description");

    public final BooleanPath display = createBoolean("display");

    //inherited
    public final BooleanPath hasDeleted;

    public final BooleanPath hasStaticChannel = createBoolean("hasStaticChannel");

    public final BooleanPath hasStaticContent = createBoolean("hasStaticContent");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final NumberPath<Integer> lft;

    public final StringPath link = createString("link");

    public final BooleanPath linkTarget = createBoolean("linkTarget");

    public final com.jeecms.content.domain.querydsl.QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final StringPath name = createString("name");

    public final QChannel parent;

    //inherited
    public final NumberPath<Integer> parentId;

    public final StringPath path = createString("path");

    public final BooleanPath recycle = createBoolean("recycle");

    public final DateTimePath<java.util.Date> recycleTime = createDateTime("recycleTime", java.util.Date.class);

    //inherited
    public final NumberPath<Integer> rgt;

    public final com.jeecms.system.domain.querydsl.QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum;

    public final BooleanPath staticChannel = createBoolean("staticChannel");

    public final StringPath tplMobile = createString("tplMobile");

    public final StringPath tplPc = createString("tplPc");

    public final ListPath<com.jeecms.content.domain.ChannelTxt, com.jeecms.content.domain.querydsl.QChannelTxt> txts = this.<com.jeecms.content.domain.ChannelTxt, com.jeecms.content.domain.querydsl.QChannelTxt>createList("txts", com.jeecms.content.domain.ChannelTxt.class, com.jeecms.content.domain.querydsl.QChannelTxt.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> updateTime;

    //inherited
    public final StringPath updateUser;

    public final ListPath<com.jeecms.member.domain.MemberGroup, com.jeecms.member.domain.querydsl.QMemberGroup> viewGroups = this.<com.jeecms.member.domain.MemberGroup, com.jeecms.member.domain.querydsl.QMemberGroup>createList("viewGroups", com.jeecms.member.domain.MemberGroup.class, com.jeecms.member.domain.querydsl.QMemberGroup.class, PathInits.DIRECT2);

    public final NumberPath<Integer> workflowId = createNumber("workflowId", Integer.class);

    public QChannel(String variable) {
        this(Channel.class, forVariable(variable), INITS);
    }

    public QChannel(Path<? extends Channel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannel(PathMetadata metadata, PathInits inits) {
        this(Channel.class, metadata, inits);
    }

    public QChannel(Class<? extends Channel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain(type, metadata, inits);
        this.channelExt = inits.isInitialized("channelExt") ? new QChannelExt(forProperty("channelExt"), inits.get("channelExt")) : null;
        this.children = _super.children;
        this.childs = _super.childs;
        this.createTime = _super.createTime;
        this.createUser = _super.createUser;
        this.hasDeleted = _super.hasDeleted;
        this.lft = _super.lft;
        this.model = inits.isInitialized("model") ? new com.jeecms.content.domain.querydsl.QCmsModel(forProperty("model"), inits.get("model")) : null;
        this.parent = inits.isInitialized("parent") ? new QChannel(forProperty("parent"), inits.get("parent")) : null;
        this.parentId = _super.parentId;
        this.rgt = _super.rgt;
        this.site = inits.isInitialized("site") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.sortNum = _super.sortNum;
        this.updateTime = _super.updateTime;
        this.updateUser = _super.updateUser;
    }

}

