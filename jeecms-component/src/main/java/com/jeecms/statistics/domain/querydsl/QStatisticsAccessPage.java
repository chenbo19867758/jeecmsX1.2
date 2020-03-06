package com.jeecms.statistics.domain.querydsl;

import com.jeecms.statistics.domain.StatisticsAccessPage;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QStatisticsAccessPage is a Querydsl query type for StatisticsAccessPage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsAccessPage extends EntityPathBase<StatisticsAccessPage> {

    private static final long serialVersionUID = -1105961737L;

    public static final QStatisticsAccessPage statisticsAccessPage = new QStatisticsAccessPage("statisticsAccessPage");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDelFlagDomain(this);

    public final NumberPath<Integer> accessHoureLong = createNumber("accessHoureLong", Integer.class);

    public final NumberPath<Integer> flows = createNumber("flows", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath newVisitor = createBoolean("newVisitor");

    public final NumberPath<Integer> onlyOnePv = createNumber("onlyOnePv", Integer.class);

    public final NumberPath<Integer> pvs = createNumber("pvs", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> sourceType = createNumber("sourceType", Integer.class);

    public final StringPath statisticsDay = createString("statisticsDay");

    public final StringPath url = createString("url");

    public final NumberPath<Integer> urlType = createNumber("urlType", Integer.class);

    public final NumberPath<Integer> uvs = createNumber("uvs", Integer.class);

    public QStatisticsAccessPage(String variable) {
        super(StatisticsAccessPage.class, forVariable(variable));
    }

    public QStatisticsAccessPage(Path<? extends StatisticsAccessPage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsAccessPage(PathMetadata metadata) {
        super(StatisticsAccessPage.class, metadata);
    }

}

