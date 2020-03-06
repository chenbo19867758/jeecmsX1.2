package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.DictData;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDictData is a Querydsl query type for DictData
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDictData extends EntityPathBase<DictData> {

    private static final long serialVersionUID = 955161965L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDictData dictData = new QDictData("dictData");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    public final QDictType coreDictType;

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath dictCode = createString("dictCode");

    public final StringPath dictLabel = createString("dictLabel");

    public final StringPath dictType = createString("dictType");

    public final NumberPath<Integer> dictTypeId = createNumber("dictTypeId", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isSystem = createBoolean("isSystem");

    public final StringPath remark = createString("remark");

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QDictData(String variable) {
        this(DictData.class, forVariable(variable), INITS);
    }

    public QDictData(Path<? extends DictData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDictData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDictData(PathMetadata metadata, PathInits inits) {
        this(DictData.class, metadata, inits);
    }

    public QDictData(Class<? extends DictData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coreDictType = inits.isInitialized("coreDictType") ? new QDictType(forProperty("coreDictType")) : null;
    }

}

