package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.DictType;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDictType is a Querydsl query type for DictType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDictType extends EntityPathBase<DictType> {

    private static final long serialVersionUID = 955661565L;

    public static final QDictType dictType1 = new QDictType("dictType1");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final ListPath<com.jeecms.system.domain.DictData, QDictData> datas = this.<com.jeecms.system.domain.DictData, QDictData>createList("datas", com.jeecms.system.domain.DictData.class, QDictData.class, PathInits.DIRECT2);

    public final StringPath dictName = createString("dictName");

    public final StringPath dictType = createString("dictType");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isSystem = createBoolean("isSystem");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QDictType(String variable) {
        super(DictType.class, forVariable(variable));
    }

    public QDictType(Path<? extends DictType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDictType(PathMetadata metadata) {
        super(DictType.class, metadata);
    }

}

