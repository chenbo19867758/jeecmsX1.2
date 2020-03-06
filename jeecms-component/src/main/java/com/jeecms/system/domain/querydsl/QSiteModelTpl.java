package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SiteModelTpl;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSiteModelTpl is a Querydsl query type for SiteModelTpl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSiteModelTpl extends EntityPathBase<SiteModelTpl> {

    private static final long serialVersionUID = 691918939L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSiteModelTpl siteModelTpl = new QSiteModelTpl("siteModelTpl");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mobileTplPath = createString("mobileTplPath");

    public final com.jeecms.content.domain.querydsl.QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final StringPath pcTplPath = createString("pcTplPath");

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSiteModelTpl(String variable) {
        this(SiteModelTpl.class, forVariable(variable), INITS);
    }

    public QSiteModelTpl(Path<? extends SiteModelTpl> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSiteModelTpl(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSiteModelTpl(PathMetadata metadata, PathInits inits) {
        this(SiteModelTpl.class, metadata, inits);
    }

    public QSiteModelTpl(Class<? extends SiteModelTpl> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.model = inits.isInitialized("model") ? new com.jeecms.content.domain.querydsl.QCmsModel(forProperty("model"), inits.get("model")) : null;
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

