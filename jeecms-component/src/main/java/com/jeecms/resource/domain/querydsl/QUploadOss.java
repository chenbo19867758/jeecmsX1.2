package com.jeecms.resource.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.resource.domain.UploadOss;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUploadOss is a Querydsl query type for UploadOss
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUploadOss extends EntityPathBase<UploadOss> {

    private static final long serialVersionUID = -2060559742L;

    public static final QUploadOss uploadOss = new QUploadOss("uploadOss");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath accessDomain = createString("accessDomain");

    public final StringPath appId = createString("appId");

    public final StringPath appKey = createString("appKey");

    public final StringPath bucketArea = createString("bucketArea");

    public final StringPath bucketName = createString("bucketName");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath endPoint = createString("endPoint");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ossName = createString("ossName");

    public final NumberPath<Short> ossType = createNumber("ossType", Short.class);

    public final StringPath secretId = createString("secretId");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QUploadOss(String variable) {
        super(UploadOss.class, forVariable(variable));
    }

    public QUploadOss(Path<? extends UploadOss> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUploadOss(PathMetadata metadata) {
        super(UploadOss.class, metadata);
    }

}

