package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.StatisticsLogResult;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsLogResult is a Querydsl query type for StatisticsLogResult
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsLogResult extends EntityPathBase<StatisticsLogResult> {

    private static final long serialVersionUID = -203424879L;

    public static final QStatisticsLogResult statisticsLogResult = new QStatisticsLogResult("statisticsLogResult");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> counts = createNumber("counts", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> resultType = createNumber("resultType", Integer.class);

    public final StringPath statisticsDay = createString("statisticsDay");

    public final StringPath statisticsMonth = createString("statisticsMonth");

    public final StringPath statisticsYear = createString("statisticsYear");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QStatisticsLogResult(String variable) {
        super(StatisticsLogResult.class, forVariable(variable));
    }

    public QStatisticsLogResult(Path<? extends StatisticsLogResult> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsLogResult(PathMetadata metadata) {
        super(StatisticsLogResult.class, metadata);
    }

}

