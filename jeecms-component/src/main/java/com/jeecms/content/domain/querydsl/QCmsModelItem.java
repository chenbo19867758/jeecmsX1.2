package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.CmsModelItem;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsModelItem is a Querydsl query type for CmsModelItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsModelItem extends EntityPathBase<CmsModelItem> {

    private static final long serialVersionUID = -1358608570L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsModelItem cmsModelItem = new QCmsModelItem("cmsModelItem");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath dataType = createString("dataType");

    public final StringPath defValue = createString("defValue");

    public final StringPath field = createString("field");

    public final StringPath groupType = createString("groupType");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isCustom = createBoolean("isCustom");

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final StringPath itemLabel = createString("itemLabel");

    public final QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final StringPath placeholder = createString("placeholder");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final StringPath tipText = createString("tipText");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QCmsModelItem(String variable) {
        this(CmsModelItem.class, forVariable(variable), INITS);
    }

    public QCmsModelItem(Path<? extends CmsModelItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsModelItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsModelItem(PathMetadata metadata, PathInits inits) {
        this(CmsModelItem.class, metadata, inits);
    }

    public QCmsModelItem(Class<? extends CmsModelItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.model = inits.isInitialized("model") ? new QCmsModel(forProperty("model"), inits.get("model")) : null;
    }

}

