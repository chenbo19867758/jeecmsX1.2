package com.jeecms.statistics.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.statistics.domain.StatisticsAccess;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsAccess is a Querydsl query type for StatisticsAccess
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsAccess extends EntityPathBase<StatisticsAccess> {

    private static final long serialVersionUID = 1987897928L;

    public static final QStatisticsAccess statisticsAccess = new QStatisticsAccess("statisticsAccess");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain(this);

    public final NumberPath<Integer> accessCount = createNumber("accessCount", Integer.class);

    public final NumberPath<Integer> accessPage = createNumber("accessPage", Integer.class);

    public final NumberPath<Integer> accessTime = createNumber("accessTime", Integer.class);

    public final NumberPath<Integer> depthPage = createNumber("depthPage", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isNewVisitor = createBoolean("isNewVisitor");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> sorceUrlType = createNumber("sorceUrlType", Integer.class);

    public final StringPath statisticsDay = createString("statisticsDay");

    public final StringPath visitorArea = createString("visitorArea");

    public QStatisticsAccess(String variable) {
        super(StatisticsAccess.class, forVariable(variable));
    }

    public QStatisticsAccess(Path<? extends StatisticsAccess> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsAccess(PathMetadata metadata) {
        super(StatisticsAccess.class, metadata);
    }

}

