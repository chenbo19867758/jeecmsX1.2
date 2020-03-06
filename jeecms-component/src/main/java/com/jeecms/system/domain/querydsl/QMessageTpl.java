package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.MessageTpl;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageTpl is a Querydsl query type for MessageTpl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMessageTpl extends EntityPathBase<MessageTpl> {

    private static final long serialVersionUID = 2016864662L;

    public static final QMessageTpl messageTpl = new QMessageTpl("messageTpl");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final ListPath<com.jeecms.system.domain.MessageTplDetails, QMessageTplDetails> details = this.<com.jeecms.system.domain.MessageTplDetails, QMessageTplDetails>createList("details", com.jeecms.system.domain.MessageTplDetails.class, QMessageTplDetails.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mesCode = createString("mesCode");

    public final StringPath mesTitle = createString("mesTitle");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> tplType = createNumber("tplType", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QMessageTpl(String variable) {
        super(MessageTpl.class, forVariable(variable));
    }

    public QMessageTpl(Path<? extends MessageTpl> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMessageTpl(PathMetadata metadata) {
        super(MessageTpl.class, metadata);
    }

}

