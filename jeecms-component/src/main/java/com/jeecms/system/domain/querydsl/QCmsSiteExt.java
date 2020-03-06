package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.CmsSiteExt;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsSiteExt is a Querydsl query type for CmsSiteExt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsSiteExt extends EntityPathBase<CmsSiteExt> {

    private static final long serialVersionUID = 17976702L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsSiteExt cmsSiteExt = new QCmsSiteExt("cmsSiteExt");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final QCmsSite cmsSite;

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> newContentResourceId = createNumber("newContentResourceId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QUploadFtp staticPageFtp;

    public final NumberPath<Integer> staticPageFtpId = createNumber("staticPageFtpId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QUploadOss staticPageOss;

    public final NumberPath<Integer> staticPageOssId = createNumber("staticPageOssId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.resource.domain.querydsl.QUploadFtp uploadFtp;

    public final NumberPath<Integer> uploadFtpId = createNumber("uploadFtpId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QUploadOss uploadOss;

    public final NumberPath<Integer> uploadOssId = createNumber("uploadOssId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData watermarkResource;

    public final NumberPath<Integer> watermarkResourceId = createNumber("watermarkResourceId", Integer.class);

    public QCmsSiteExt(String variable) {
        this(CmsSiteExt.class, forVariable(variable), INITS);
    }

    public QCmsSiteExt(Path<? extends CmsSiteExt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsSiteExt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsSiteExt(PathMetadata metadata, PathInits inits) {
        this(CmsSiteExt.class, metadata, inits);
    }

    public QCmsSiteExt(Class<? extends CmsSiteExt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cmsSite = inits.isInitialized("cmsSite") ? new QCmsSite(forProperty("cmsSite"), inits.get("cmsSite")) : null;
        this.staticPageFtp = inits.isInitialized("staticPageFtp") ? new com.jeecms.resource.domain.querydsl.QUploadFtp(forProperty("staticPageFtp")) : null;
        this.staticPageOss = inits.isInitialized("staticPageOss") ? new com.jeecms.resource.domain.querydsl.QUploadOss(forProperty("staticPageOss")) : null;
        this.uploadFtp = inits.isInitialized("uploadFtp") ? new com.jeecms.resource.domain.querydsl.QUploadFtp(forProperty("uploadFtp")) : null;
        this.uploadOss = inits.isInitialized("uploadOss") ? new com.jeecms.resource.domain.querydsl.QUploadOss(forProperty("uploadOss")) : null;
        this.watermarkResource = inits.isInitialized("watermarkResource") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("watermarkResource"), inits.get("watermarkResource")) : null;
    }

}

