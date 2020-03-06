package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysSensitiveWord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysSensitiveWord is a Querydsl query type for SysSensitiveWord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysSensitiveWord extends EntityPathBase<SysSensitiveWord> {

    private static final long serialVersionUID = -2025279264L;

    public static final QSysSensitiveWord sysSensitiveWord = new QSysSensitiveWord("sysSensitiveWord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath replaceWord = createString("replaceWord");

    public final StringPath sensitiveWord = createString("sensitiveWord");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysSensitiveWord(String variable) {
        super(SysSensitiveWord.class, forVariable(variable));
    }

    public QSysSensitiveWord(Path<? extends SysSensitiveWord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysSensitiveWord(PathMetadata metadata) {
        super(SysSensitiveWord.class, metadata);
    }

}

