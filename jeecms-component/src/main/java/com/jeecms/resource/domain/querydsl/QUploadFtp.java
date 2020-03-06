package com.jeecms.resource.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.resource.domain.UploadFtp;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUploadFtp is a Querydsl query type for UploadFtp
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUploadFtp extends EntityPathBase<UploadFtp> {

    private static final long serialVersionUID = -2060568363L;

    public static final QUploadFtp uploadFtp = new QUploadFtp("uploadFtp");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath encoding = createString("encoding");

    public final StringPath ftpName = createString("ftpName");

    public final StringPath ftpPath = createString("ftpPath");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final StringPath obfuscationCode = createString("obfuscationCode");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> port = createNumber("port", Integer.class);

    public final NumberPath<Integer> timeout = createNumber("timeout", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath url = createString("url");

    public final StringPath username = createString("username");

    public QUploadFtp(String variable) {
        super(UploadFtp.class, forVariable(variable));
    }

    public QUploadFtp(Path<? extends UploadFtp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUploadFtp(PathMetadata metadata) {
        super(UploadFtp.class, metadata);
    }

}

