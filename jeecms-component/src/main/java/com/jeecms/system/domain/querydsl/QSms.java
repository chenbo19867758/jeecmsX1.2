package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.Sms;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSms is a Querydsl query type for Sms
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSms extends EntityPathBase<Sms> {

    private static final long serialVersionUID = -763157428L;

    public static final QSms sms = new QSms("sms");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath accessKey = createString("accessKey");

    public final StringPath accesskeySecret = createString("accesskeySecret");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final BooleanPath isGloable = createBoolean("isGloable");

    public final NumberPath<Short> serviceProvider = createNumber("serviceProvider", Short.class);

    public final StringPath smsSign = createString("smsSign");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSms(String variable) {
        super(Sms.class, forVariable(variable));
    }

    public QSms(Path<? extends Sms> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSms(PathMetadata metadata) {
        super(Sms.class, metadata);
    }

}

