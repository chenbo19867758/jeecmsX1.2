package com.jeecms.statistics.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.statistics.domain.StatisticsFlow;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsFlow is a Querydsl query type for StatisticsFlow
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsFlow extends EntityPathBase<StatisticsFlow> {

    private static final long serialVersionUID = 1079320338L;

    public static final QStatisticsFlow statisticsFlow = new QStatisticsFlow("statisticsFlow");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain(this);

    public final NumberPath<Integer> accessHoureLong = createNumber("accessHoureLong", Integer.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath engineName = createString("engineName");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> ips = createNumber("ips", Integer.class);

    public final BooleanPath isNewVisitor = createBoolean("isNewVisitor");

    public final NumberPath<Integer> onlyOnePv = createNumber("onlyOnePv", Integer.class);

    public final NumberPath<Integer> pvs = createNumber("pvs", Integer.class);

    public final NumberPath<Integer> session = createNumber("session", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> sorceUrlType = createNumber("sorceUrlType", Integer.class);

    public final StringPath statisticsDay = createString("statisticsDay");

    public final NumberPath<Integer> statisticsHour = createNumber("statisticsHour", Integer.class);

    public final NumberPath<Integer> uvs = createNumber("uvs", Integer.class);

    public final StringPath visitorCity = createString("visitorCity");

    public final StringPath visitorDeviceOs = createString("visitorDeviceOs");

    public final NumberPath<Integer> visitorDeviceType = createNumber("visitorDeviceType", Integer.class);

    public final StringPath visitorProvince = createString("visitorProvince");

    public QStatisticsFlow(String variable) {
        super(StatisticsFlow.class, forVariable(variable));
    }

    public QStatisticsFlow(Path<? extends StatisticsFlow> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsFlow(PathMetadata metadata) {
        super(StatisticsFlow.class, metadata);
    }

}

