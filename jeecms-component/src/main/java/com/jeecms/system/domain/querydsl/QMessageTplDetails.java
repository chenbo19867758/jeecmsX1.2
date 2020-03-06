package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.MessageTplDetails;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageTplDetails is a Querydsl query type for MessageTplDetails
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMessageTplDetails extends EntityPathBase<MessageTplDetails> {

    private static final long serialVersionUID = -1897739316L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageTplDetails messageTplDetails = new QMessageTplDetails("messageTplDetails");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath extendedField = createString("extendedField");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isOpen = createBoolean("isOpen");

    public final StringPath mesContent = createString("mesContent");

    public final QMessageTpl messageTpl;

    public final StringPath mesTitle = createString("mesTitle");

    public final NumberPath<Integer> mesTplId = createNumber("mesTplId", Integer.class);

    public final NumberPath<Short> mesType = createNumber("mesType", Short.class);

    public final StringPath tplId = createString("tplId");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QMessageTplDetails(String variable) {
        this(MessageTplDetails.class, forVariable(variable), INITS);
    }

    public QMessageTplDetails(Path<? extends MessageTplDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageTplDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageTplDetails(PathMetadata metadata, PathInits inits) {
        this(MessageTplDetails.class, metadata, inits);
    }

    public QMessageTplDetails(Class<? extends MessageTplDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.messageTpl = inits.isInitialized("messageTpl") ? new QMessageTpl(forProperty("messageTpl")) : null;
    }

}

