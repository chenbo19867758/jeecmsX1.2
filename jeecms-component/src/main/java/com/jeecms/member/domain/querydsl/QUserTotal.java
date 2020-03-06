package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.UserTotal;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserTotal is a Querydsl query type for UserTotal
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserTotal extends EntityPathBase<UserTotal> {

    private static final long serialVersionUID = 902641121L;

    public static final QUserTotal userTotal = new QUserTotal("userTotal");

    public final NumberPath<Integer> commentNum = createNumber("commentNum", Integer.class);

    public final NumberPath<Integer> contributorNum = createNumber("contributorNum", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);
    
    public final BooleanPath userPerfect = createBoolean("userPerfect");

    public QUserTotal(String variable) {
        super(UserTotal.class, forVariable(variable));
    }

    public QUserTotal(Path<? extends UserTotal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserTotal(PathMetadata metadata) {
        super(UserTotal.class, metadata);
    }

}

