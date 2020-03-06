package com.jeecms.channel.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.channel.domain.ChannelContentTpl;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QChannelContentTpl is a Querydsl query type for ChannelContentTpl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChannelContentTpl extends EntityPathBase<ChannelContentTpl> {

    private static final long serialVersionUID = 1917758845L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannelContentTpl channelContentTpl = new QChannelContentTpl("channelContentTpl");

    public final QChannel channel;

    public final NumberPath<Integer> channelId = createNumber("channelId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.jeecms.content.domain.querydsl.QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final StringPath tplMobile = createString("tplMobile");

    public final StringPath tplPc = createString("tplPc");
    
    public final BooleanPath select = createBoolean("select");

    public QChannelContentTpl(String variable) {
        this(ChannelContentTpl.class, forVariable(variable), INITS);
    }

    public QChannelContentTpl(Path<? extends ChannelContentTpl> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannelContentTpl(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannelContentTpl(PathMetadata metadata, PathInits inits) {
        this(ChannelContentTpl.class, metadata, inits);
    }

    public QChannelContentTpl(Class<? extends ChannelContentTpl> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new QChannel(forProperty("channel"), inits.get("channel")) : null;
        this.model = inits.isInitialized("model") ? new com.jeecms.content.domain.querydsl.QCmsModel(forProperty("model"), inits.get("model")) : null;
    }

}

