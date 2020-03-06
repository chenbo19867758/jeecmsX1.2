package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysSearchWord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysSearchWord is a Querydsl query type for SysSearchWord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysSearchWord extends EntityPathBase<SysSearchWord> {

    private static final long serialVersionUID = -795255118L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysSearchWord sysSearchWord = new QSysSearchWord("sysSearchWord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath iniChinese = createString("iniChinese");

    public final BooleanPath isRecommend = createBoolean("isRecommend");

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath word = createString("word");

    public QSysSearchWord(String variable) {
        this(SysSearchWord.class, forVariable(variable), INITS);
    }

    public QSysSearchWord(Path<? extends SysSearchWord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysSearchWord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysSearchWord(PathMetadata metadata, PathInits inits) {
        this(SysSearchWord.class, metadata, inits);
    }

    public QSysSearchWord(Class<? extends SysSearchWord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

