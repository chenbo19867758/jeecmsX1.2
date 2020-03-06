package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatFansStatistics;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWechatFansStatistics is a Querydsl query type for WechatFansStatistics
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatFansStatistics extends EntityPathBase<WechatFansStatistics> {

    private static final long serialVersionUID = 1686118317L;

    public static final QWechatFansStatistics wechatFansStatistics = new QWechatFansStatistics("wechatFansStatistics");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final NumberPath<Integer> cancelUser = createNumber("cancelUser", Integer.class);

    public final NumberPath<Integer> countUser = createNumber("countUser", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> netGrowthUser = createNumber("netGrowthUser", Integer.class);

    public final NumberPath<Integer> newUser = createNumber("newUser", Integer.class);
    
    public final NumberPath<Integer> userSource = createNumber("userSource", Integer.class);

    public final DateTimePath<java.util.Date> statisticsDate = createDateTime("statisticsDate", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QWechatFansStatistics(String variable) {
        super(WechatFansStatistics.class, forVariable(variable));
    }

    public QWechatFansStatistics(Path<? extends WechatFansStatistics> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWechatFansStatistics(PathMetadata metadata) {
        super(WechatFansStatistics.class, metadata);
    }

}

