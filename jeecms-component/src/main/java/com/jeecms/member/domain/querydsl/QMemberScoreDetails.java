package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.MemberScoreDetails;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberScoreDetails is a Querydsl query type for MemberScoreDetails
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberScoreDetails extends EntityPathBase<MemberScoreDetails> {

    private static final long serialVersionUID = 1630435426L;

    public static final QMemberScoreDetails memberScoreDetails = new QMemberScoreDetails("memberScoreDetails");

    public final NumberPath<Integer> businessType = createNumber("businessType", Integer.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QMemberScoreDetails(String variable) {
        super(MemberScoreDetails.class, forVariable(variable));
    }

    public QMemberScoreDetails(Path<? extends MemberScoreDetails> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberScoreDetails(PathMetadata metadata) {
        super(MemberScoreDetails.class, metadata);
    }

}

