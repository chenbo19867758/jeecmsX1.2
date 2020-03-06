package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.MiniprogramCode;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMiniprogramCode is a Querydsl query type for MiniprogramCode
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMiniprogramCode extends EntityPathBase<MiniprogramCode> {

    private static final long serialVersionUID = 2007843190L;

    public static final QMiniprogramCode miniprogramCode = new QMiniprogramCode("miniprogramCode");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath codeDesc = createString("codeDesc");

    public final NumberPath<Short> codeType = createNumber("codeType", Short.class);

    public final StringPath codeVersion = createString("codeVersion");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> draftId = createNumber("draftId", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isNew = createBoolean("isNew");

    public final NumberPath<Long> submitTime = createNumber("submitTime", Long.class);

    public final NumberPath<Integer> templateId = createNumber("templateId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QMiniprogramCode(String variable) {
        super(MiniprogramCode.class, forVariable(variable));
    }

    public QMiniprogramCode(Path<? extends MiniprogramCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMiniprogramCode(PathMetadata metadata) {
        super(MiniprogramCode.class, metadata);
    }

}

