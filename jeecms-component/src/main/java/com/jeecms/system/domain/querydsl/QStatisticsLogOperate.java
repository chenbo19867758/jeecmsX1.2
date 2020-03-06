package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.StatisticsLogOperate;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsLogOperate is a Querydsl query type for StatisticsLogOperate
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsLogOperate extends EntityPathBase<StatisticsLogOperate> {

    private static final long serialVersionUID = -76856176L;

    public static final QStatisticsLogOperate statisticsLogOperate = new QStatisticsLogOperate("statisticsLogOperate");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> counts = createNumber("counts", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath operateType = createString("operateType");

    public final StringPath statisticsDay = createString("statisticsDay");

    public final StringPath statisticsMonth = createString("statisticsMonth");

    public final StringPath statisticsYear = createString("statisticsYear");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QStatisticsLogOperate(String variable) {
        super(StatisticsLogOperate.class, forVariable(variable));
    }

    public QStatisticsLogOperate(Path<? extends StatisticsLogOperate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsLogOperate(PathMetadata metadata) {
        super(StatisticsLogOperate.class, metadata);
    }

}

