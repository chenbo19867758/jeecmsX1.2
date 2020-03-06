package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ChannelTxt;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannelTxt is a Querydsl query type for ChannelTxt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChannelTxt extends EntityPathBase<ChannelTxt> {

    private static final long serialVersionUID = -91376672L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannelTxt channelTxt = new QChannelTxt("channelTxt");

    public final StringPath attrKey = createString("attrKey");

    public final StringPath attrTxt = createString("attrTxt");

    public final com.jeecms.channel.domain.querydsl.QChannel channel;

    public final NumberPath<Integer> channelId = createNumber("channelId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QChannelTxt(String variable) {
        this(ChannelTxt.class, forVariable(variable), INITS);
    }

    public QChannelTxt(Path<? extends ChannelTxt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannelTxt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannelTxt(PathMetadata metadata, PathInits inits) {
        this(ChannelTxt.class, metadata, inits);
    }

    public QChannelTxt(Class<? extends ChannelTxt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new com.jeecms.channel.domain.querydsl.QChannel(forProperty("channel"), inits.get("channel")) : null;
    }

}

