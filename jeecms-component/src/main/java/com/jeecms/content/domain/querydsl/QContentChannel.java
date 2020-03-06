package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentChannel;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentChannel is a Querydsl query type for ContentChannel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentChannel extends EntityPathBase<ContentChannel> {

    private static final long serialVersionUID = 2033471037L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentChannel contentChannel = new QContentChannel("contentChannel");

    public final com.jeecms.channel.domain.querydsl.QChannel channel;

    public final NumberPath<Integer> channelId = createNumber("channelId", Integer.class);

    public final QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final NumberPath<Integer> createType = createNumber("createType", Integer.class);

    public final BooleanPath recycle = createBoolean("recycle");
    
    public final BooleanPath isRef = createBoolean("isRef");

    public final NumberPath<Integer> refId = createNumber("refId", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QContentChannel(String variable) {
        this(ContentChannel.class, forVariable(variable), INITS);
    }

    public QContentChannel(Path<? extends ContentChannel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentChannel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentChannel(PathMetadata metadata, PathInits inits) {
        this(ContentChannel.class, metadata, inits);
    }

    public QContentChannel(Class<? extends ContentChannel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new com.jeecms.channel.domain.querydsl.QChannel(forProperty("channel"), inits.get("channel")) : null;
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
    }

}

