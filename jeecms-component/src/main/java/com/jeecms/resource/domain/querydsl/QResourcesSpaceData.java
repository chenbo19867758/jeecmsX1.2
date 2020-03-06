package com.jeecms.resource.domain.querydsl;

import com.jeecms.resource.domain.ResourcesSpaceData;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QResourcesSpaceData is a Querydsl query type for ResourcesSpaceData
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QResourcesSpaceData extends EntityPathBase<ResourcesSpaceData> {

    private static final long serialVersionUID = 403307127L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResourcesSpaceData resourcesSpaceData = new QResourcesSpaceData("resourcesSpaceData");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    public final StringPath alias = createString("alias");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath dimensions = createString("dimensions");

    public final BooleanPath display = createBoolean("display");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> refCount = createNumber("refCount", Integer.class);

    public final NumberPath<Integer> resourceDate = createNumber("resourceDate", Integer.class);

    public final NumberPath<Short> resourceType = createNumber("resourceType", Short.class);

    public final NumberPath<Short> shareStatus = createNumber("shareStatus", Short.class);

    public final DateTimePath<java.util.Date> shareTime = createDateTime("shareTime", java.util.Date.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    public final QResourcesSpace space;

    public final NumberPath<Integer> storeResourcesSpaceId = createNumber("storeResourcesSpaceId", Integer.class);

    public final StringPath suffix = createString("suffix");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QUploadFtp uploadFtp;

    public final QUploadOss uploadOss;

    public final StringPath url = createString("url");

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final ListPath<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, com.jeecms.auth.domain.querydsl.QCoreUser.class, PathInits.DIRECT2);

    public QResourcesSpaceData(String variable) {
        this(ResourcesSpaceData.class, forVariable(variable), INITS);
    }

    public QResourcesSpaceData(Path<? extends ResourcesSpaceData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResourcesSpaceData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResourcesSpaceData(PathMetadata metadata, PathInits inits) {
        this(ResourcesSpaceData.class, metadata, inits);
    }

    public QResourcesSpaceData(Class<? extends ResourcesSpaceData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.space = inits.isInitialized("space") ? new QResourcesSpace(forProperty("space"), inits.get("space")) : null;
        this.uploadFtp = inits.isInitialized("uploadFtp") ? new QUploadFtp(forProperty("uploadFtp")) : null;
        this.uploadOss = inits.isInitialized("uploadOss") ? new QUploadOss(forProperty("uploadOss")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

