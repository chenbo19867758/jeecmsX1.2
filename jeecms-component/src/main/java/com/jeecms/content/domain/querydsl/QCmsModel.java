package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.CmsModel;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsModel is a Querydsl query type for CmsModel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsModel extends EntityPathBase<CmsModel> {

    private static final long serialVersionUID = -400598637L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsModel cmsModel = new QCmsModel("cmsModel");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final NumberPath<Short> isGlobal = createNumber("isGlobal", Short.class);

    public final SetPath<com.jeecms.content.domain.CmsModelItem, QCmsModelItem> items = this.<com.jeecms.content.domain.CmsModelItem, QCmsModelItem>createSet("items", com.jeecms.content.domain.CmsModelItem.class, QCmsModelItem.class, PathInits.DIRECT2);

    public final StringPath modelName = createString("modelName");

    public final com.jeecms.system.domain.querydsl.QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    public final NumberPath<Integer> sortWeight = createNumber("sortWeight", Integer.class);

    public final SetPath<com.jeecms.content.domain.CmsModelTpl, QCmsModelTpl> tpls = this.<com.jeecms.content.domain.CmsModelTpl, QCmsModelTpl>createSet("tpls", com.jeecms.content.domain.CmsModelTpl.class, QCmsModelTpl.class, PathInits.DIRECT2);

    public final NumberPath<Short> tplType = createNumber("tplType", Short.class);

    public final StringPath unEnableJsonStr = createString("unEnableJsonStr");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QCmsModel(String variable) {
        this(CmsModel.class, forVariable(variable), INITS);
    }

    public QCmsModel(Path<? extends CmsModel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsModel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsModel(PathMetadata metadata, PathInits inits) {
        this(CmsModel.class, metadata, inits);
    }

    public QCmsModel(Class<? extends CmsModel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

