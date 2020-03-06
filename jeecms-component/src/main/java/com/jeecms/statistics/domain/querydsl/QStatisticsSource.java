package com.jeecms.statistics.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.statistics.domain.StatisticsSource;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsSource is a Querydsl query type for StatisticsSource
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsSource extends EntityPathBase<StatisticsSource> {

    private static final long serialVersionUID = -1780114177L;

    public static final QStatisticsSource statisticsSource = new QStatisticsSource("statisticsSource");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain(this);

    public final NumberPath<Integer> accessHoureLong = createNumber("accessHoureLong", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> ips = createNumber("ips", Integer.class);

    public final BooleanPath isNewVisitor = createBoolean("isNewVisitor");

    public final NumberPath<Integer> onlyOnePv = createNumber("onlyOnePv", Integer.class);

    public final NumberPath<Integer> pvs = createNumber("pvs", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath sorceUrl = createString("sorceUrl");

    public final NumberPath<Integer> sorceUrlType = createNumber("sorceUrlType", Integer.class);

    public final StringPath statisticsDay = createString("statisticsDay");

    public final NumberPath<Integer> statisticsHour = createNumber("statisticsHour", Integer.class);

    public final NumberPath<Integer> uvs = createNumber("uvs", Integer.class);

    public final NumberPath<Integer> visitorDeviceType = createNumber("visitorDeviceType", Integer.class);

    public QStatisticsSource(String variable) {
        super(StatisticsSource.class, forVariable(variable));
    }

    public QStatisticsSource(Path<? extends StatisticsSource> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsSource(PathMetadata metadata) {
        super(StatisticsSource.class, metadata);
    }

}

