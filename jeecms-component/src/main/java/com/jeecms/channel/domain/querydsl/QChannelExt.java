package com.jeecms.channel.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.channel.domain.ChannelExt;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannelExt is a Querydsl query type for ChannelExt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChannelExt extends EntityPathBase<ChannelExt> {

    private static final long serialVersionUID = 171472283L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannelExt channelExt = new QChannelExt("channelExt");

    public final QChannel channel;

    public final NumberPath<Short> commentControl = createNumber("commentControl", Short.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> intervalTime = createNumber("intervalTime", Integer.class);

    public final BooleanPath isListChannel = createBoolean("isListChannel");

    public final BooleanPath isOpenIndex = createBoolean("isOpenIndex");

    public final NumberPath<Short> pageSize = createNumber("pageSize", Short.class);

    public final NumberPath<Integer> resourceId = createNumber("resourceId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public final StringPath seoDescription = createString("seoDescription");

    public final StringPath seoKeywork = createString("seoKeywork");

    public final StringPath seoTitle = createString("seoTitle");

    public final BooleanPath taskControl = createBoolean("taskControl");

    public final DateTimePath<java.util.Date> taskTime = createDateTime("taskTime", java.util.Date.class);

    public final NumberPath<Short> taskType = createNumber("taskType", Short.class);

    public final StringPath txt = createString("txt");

    public final NumberPath<Short> viewControl = createNumber("viewControl", Short.class);

    public QChannelExt(String variable) {
        this(ChannelExt.class, forVariable(variable), INITS);
    }

    public QChannelExt(Path<? extends ChannelExt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannelExt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannelExt(PathMetadata metadata, PathInits inits) {
        this(ChannelExt.class, metadata, inits);
    }

    public QChannelExt(Class<? extends ChannelExt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new QChannel(forProperty("channel"), inits.get("channel")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
    }

}

