package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentAttr;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentAttr is a Querydsl query type for ContentAttr
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentAttr extends EntityPathBase<ContentAttr> {

    private static final long serialVersionUID = 798434039L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentAttr contentAttr = new QContentAttr("contentAttr");

    public final com.jeecms.system.domain.querydsl.QArea area;

    public final StringPath areaCode = createString("areaCode");

    public final StringPath attrName = createString("attrName");

    public final StringPath attrType = createString("attrType");

    public final StringPath attrValue = createString("attrValue");

    public final com.jeecms.system.domain.querydsl.QArea city;

    public final StringPath cityCode = createString("cityCode");

    public final com.jeecms.system.domain.querydsl.QCmsOrg cmsOrg;

    public final QContent content;

    public final ListPath<com.jeecms.content.domain.ContentAttrRes, QContentAttrRes> contentAttrRes = this.<com.jeecms.content.domain.ContentAttrRes, QContentAttrRes>createList("contentAttrRes", com.jeecms.content.domain.ContentAttrRes.class, QContentAttrRes.class, PathInits.DIRECT2);

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> orgId = createNumber("orgId", Integer.class);

    public final com.jeecms.system.domain.querydsl.QArea province;

    public final StringPath provinceCode = createString("provinceCode");

    public final NumberPath<Integer> resId = createNumber("resId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public QContentAttr(String variable) {
        this(ContentAttr.class, forVariable(variable), INITS);
    }

    public QContentAttr(Path<? extends ContentAttr> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentAttr(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentAttr(PathMetadata metadata, PathInits inits) {
        this(ContentAttr.class, metadata, inits);
    }

    public QContentAttr(Class<? extends ContentAttr> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.area = inits.isInitialized("area") ? new com.jeecms.system.domain.querydsl.QArea(forProperty("area"), inits.get("area")) : null;
        this.city = inits.isInitialized("city") ? new com.jeecms.system.domain.querydsl.QArea(forProperty("city"), inits.get("city")) : null;
        this.cmsOrg = inits.isInitialized("cmsOrg") ? new com.jeecms.system.domain.querydsl.QCmsOrg(forProperty("cmsOrg"), inits.get("cmsOrg")) : null;
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
        this.province = inits.isInitialized("province") ? new com.jeecms.system.domain.querydsl.QArea(forProperty("province"), inits.get("province")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
    }

}

