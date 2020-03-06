package com.jeecms.channel.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.channel.domain.ChannelAttr;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannelAttr is a Querydsl query type for ChannelAttr
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChannelAttr extends EntityPathBase<ChannelAttr> {

    private static final long serialVersionUID = 1020550583L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannelAttr channelAttr = new QChannelAttr("channelAttr");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath attrName = createString("attrName");

    public final StringPath attrType = createString("attrType");

    public final StringPath attrValue = createString("attrValue");

    public final QChannel channel;

    public final NumberPath<Integer> channelAttrId = createNumber("channelAttrId", Integer.class);

    public final ListPath<com.jeecms.channel.domain.ChannelAttrRes, QChannelAttrRes> channelAttrRes = this.<com.jeecms.channel.domain.ChannelAttrRes, QChannelAttrRes>createList("channelAttrRes", com.jeecms.channel.domain.ChannelAttrRes.class, QChannelAttrRes.class, PathInits.DIRECT2);

    public final NumberPath<Integer> channelId = createNumber("channelId", Integer.class);

    public final StringPath cityCode = createString("cityCode");

    public final com.jeecms.system.domain.querydsl.QCmsOrg cmsOrg;

    public final NumberPath<Integer> orgId = createNumber("orgId", Integer.class);

    public final StringPath provinceCode = createString("provinceCode");

    public final NumberPath<Integer> resId = createNumber("resId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public QChannelAttr(String variable) {
        this(ChannelAttr.class, forVariable(variable), INITS);
    }

    public QChannelAttr(Path<? extends ChannelAttr> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannelAttr(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannelAttr(PathMetadata metadata, PathInits inits) {
        this(ChannelAttr.class, metadata, inits);
    }

    public QChannelAttr(Class<? extends ChannelAttr> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new QChannel(forProperty("channel"), inits.get("channel")) : null;
        this.cmsOrg = inits.isInitialized("cmsOrg") ? new com.jeecms.system.domain.querydsl.QCmsOrg(forProperty("cmsOrg"), inits.get("cmsOrg")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
    }

}

