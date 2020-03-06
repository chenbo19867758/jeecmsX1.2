package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.MemberAttr;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAttr is a Querydsl query type for MemberAttr
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberAttr extends EntityPathBase<MemberAttr> {

    private static final long serialVersionUID = -745840381L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAttr memberAttr = new QMemberAttr("memberAttr");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath attrName = createString("attrName");

    public final StringPath attrType = createString("attrType");

    public final StringPath attrValue = createString("attrValue");

    public final StringPath cityCode = createString("cityCode");

    public final com.jeecms.auth.domain.querydsl.QCoreUser member;

    public final NumberPath<Integer> memberAttrId = createNumber("memberAttrId", Integer.class);

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final StringPath provinceCode = createString("provinceCode");

    public final NumberPath<Integer> resId = createNumber("resId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public QMemberAttr(String variable) {
        this(MemberAttr.class, forVariable(variable), INITS);
    }

    public QMemberAttr(Path<? extends MemberAttr> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAttr(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAttr(PathMetadata metadata, PathInits inits) {
        this(MemberAttr.class, metadata, inits);
    }

    public QMemberAttr(Class<? extends MemberAttr> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("member"), inits.get("member")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
    }

}

