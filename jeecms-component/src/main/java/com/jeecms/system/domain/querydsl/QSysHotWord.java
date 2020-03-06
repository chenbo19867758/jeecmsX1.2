package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysHotWord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysHotWord is a Querydsl query type for SysHotWord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysHotWord extends EntityPathBase<SysHotWord> {

    private static final long serialVersionUID = -47097065L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysHotWord sysHotWord = new QSysHotWord("sysHotWord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> clickCount = createNumber("clickCount", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath hotWord = createString("hotWord");

    public final NumberPath<Integer> hotWordCategoryId = createNumber("hotWordCategoryId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isTargetBlank = createBoolean("isTargetBlank");

    public final StringPath linkUrl = createString("linkUrl");

    public final StringPath remark = createString("remark");

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final QSysHotWordCategory sysHotWordCategory;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> useCount = createNumber("useCount", Integer.class);

    public QSysHotWord(String variable) {
        this(SysHotWord.class, forVariable(variable), INITS);
    }

    public QSysHotWord(Path<? extends SysHotWord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysHotWord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysHotWord(PathMetadata metadata, PathInits inits) {
        this(SysHotWord.class, metadata, inits);
    }

    public QSysHotWord(Class<? extends SysHotWord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.sysHotWordCategory = inits.isInitialized("sysHotWordCategory") ? new QSysHotWordCategory(forProperty("sysHotWordCategory"), inits.get("sysHotWordCategory")) : null;
    }

}

