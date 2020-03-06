package com.jeecms.channel.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.channel.domain.ChannelAttrRes;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannelAttrRes is a Querydsl query type for ChannelAttrRes
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChannelAttrRes extends EntityPathBase<ChannelAttrRes> {

    private static final long serialVersionUID = -850988183L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannelAttrRes channelAttrRes = new QChannelAttrRes("channelAttrRes");

    public final QChannelAttr channelAttr;

    public final NumberPath<Integer> channelAttrId = createNumber("channelAttrId", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> resId = createNumber("resId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public final com.jeecms.system.domain.querydsl.QSysSecret secret;

    public final NumberPath<Integer> secretId = createNumber("secretId", Integer.class);

    public QChannelAttrRes(String variable) {
        this(ChannelAttrRes.class, forVariable(variable), INITS);
    }

    public QChannelAttrRes(Path<? extends ChannelAttrRes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannelAttrRes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannelAttrRes(PathMetadata metadata, PathInits inits) {
        this(ChannelAttrRes.class, metadata, inits);
    }

    public QChannelAttrRes(Class<? extends ChannelAttrRes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channelAttr = inits.isInitialized("channelAttr") ? new QChannelAttr(forProperty("channelAttr"), inits.get("channelAttr")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
        this.secret = inits.isInitialized("secret") ? new com.jeecms.system.domain.querydsl.QSysSecret(forProperty("secret")) : null;
    }

}

