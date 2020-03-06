package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.Area;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArea is a Querydsl query type for Area
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QArea extends EntityPathBase<Area> {

    private static final long serialVersionUID = 2111391738L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArea area = new QArea("area");

    public final com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain _super;

    public final StringPath areaCode = createString("areaCode");

    public final StringPath areaDictCode = createString("areaDictCode");

    public final StringPath areaName = createString("areaName");

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> children;

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> childs;

    //inherited
    public final DateTimePath<java.util.Date> createTime;

    //inherited
    public final StringPath createUser;

    //inherited
    public final BooleanPath hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final NumberPath<Integer> lft;

    public final QArea parent;

    public final NumberPath<Integer> parentId = createNumber("parentId", Integer.class);

    public final StringPath remark = createString("remark");

    //inherited
    public final NumberPath<Integer> rgt;

    //inherited
    public final NumberPath<Integer> sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime;

    //inherited
    public final StringPath updateUser;

    public QArea(String variable) {
        this(Area.class, forVariable(variable), INITS);
    }

    public QArea(Path<? extends Area> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArea(PathMetadata metadata, PathInits inits) {
        this(Area.class, metadata, inits);
    }

    public QArea(Class<? extends Area> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain(type, metadata, inits);
        this.children = _super.children;
        this.childs = _super.childs;
        this.createTime = _super.createTime;
        this.createUser = _super.createUser;
        this.hasDeleted = _super.hasDeleted;
        this.lft = _super.lft;
        this.parent = inits.isInitialized("parent") ? new QArea(forProperty("parent")) : null;
        this.rgt = _super.rgt;
        this.sortNum = _super.sortNum;
        this.updateTime = _super.updateTime;
        this.updateUser = _super.updateUser;
    }

}

