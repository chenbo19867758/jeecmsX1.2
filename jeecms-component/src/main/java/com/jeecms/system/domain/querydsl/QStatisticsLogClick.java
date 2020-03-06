package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.StatisticsLogClick;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsLogClick is a Querydsl query type for StatisticsLogClick
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsLogClick extends EntityPathBase<StatisticsLogClick> {

    private static final long serialVersionUID = -435858540L;

    public static final QStatisticsLogClick statisticsLogClick = new QStatisticsLogClick("statisticsLogClick");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> clickType = createNumber("clickType", Integer.class);

    public final NumberPath<Integer> counts = createNumber("counts", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath statisticsDay = createString("statisticsDay");

    public final StringPath statisticsMonth = createString("statisticsMonth");

    public final StringPath statisticsYear = createString("statisticsYear");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QStatisticsLogClick(String variable) {
        super(StatisticsLogClick.class, forVariable(variable));
    }

    public QStatisticsLogClick(Path<? extends StatisticsLogClick> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsLogClick(PathMetadata metadata) {
        super(StatisticsLogClick.class, metadata);
    }

}

