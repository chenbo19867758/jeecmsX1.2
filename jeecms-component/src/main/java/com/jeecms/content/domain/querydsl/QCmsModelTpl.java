package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.CmsModelTpl;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsModelTpl is a Querydsl query type for CmsModelTpl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsModelTpl extends EntityPathBase<CmsModelTpl> {

    private static final long serialVersionUID = 1480205021L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsModelTpl cmsModelTpl = new QCmsModelTpl("cmsModelTpl");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath tplPath = createString("tplPath");

    public final StringPath tplSolution = createString("tplSolution");

    public final NumberPath<Short> tplType = createNumber("tplType", Short.class);

    public QCmsModelTpl(String variable) {
        this(CmsModelTpl.class, forVariable(variable), INITS);
    }

    public QCmsModelTpl(Path<? extends CmsModelTpl> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsModelTpl(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsModelTpl(PathMetadata metadata, PathInits inits) {
        this(CmsModelTpl.class, metadata, inits);
    }

    public QCmsModelTpl(Class<? extends CmsModelTpl> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.model = inits.isInitialized("model") ? new QCmsModel(forProperty("model"), inits.get("model")) : null;
    }

}

