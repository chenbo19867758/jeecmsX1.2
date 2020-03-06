package com.jeecms.collect.domain.querydsl;

import com.jeecms.collect.domain.CollectContent;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QCollectContent is a Querydsl query type for CollectContent
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCollectContent extends EntityPathBase<CollectContent> {

    private static final long serialVersionUID = 451445203L;

    public static final QCollectContent collectContent = new QCollectContent("collectContent");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath contentValue = createString("contentValue");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> taskId = createNumber("taskId", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath url = createString("url");

    public QCollectContent(String variable) {
        super(CollectContent.class, forVariable(variable));
    }

    public QCollectContent(Path<? extends CollectContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCollectContent(PathMetadata metadata) {
        super(CollectContent.class, metadata);
    }

}

