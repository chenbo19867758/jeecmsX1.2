package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.StatisticsLogUser;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsLogUser is a Querydsl query type for StatisticsLogUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStatisticsLogUser extends EntityPathBase<StatisticsLogUser> {

    private static final long serialVersionUID = -706253761L;

    public static final QStatisticsLogUser statisticsLogUser = new QStatisticsLogUser("statisticsLogUser");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> counts = createNumber("counts", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath operateUser = createString("operateUser");

    public final StringPath statisticsDay = createString("statisticsDay");

    public final StringPath statisticsMonth = createString("statisticsMonth");

    public final StringPath statisticsYear = createString("statisticsYear");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QStatisticsLogUser(String variable) {
        super(StatisticsLogUser.class, forVariable(variable));
    }

    public QStatisticsLogUser(Path<? extends StatisticsLogUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsLogUser(PathMetadata metadata) {
        super(StatisticsLogUser.class, metadata);
    }

}

