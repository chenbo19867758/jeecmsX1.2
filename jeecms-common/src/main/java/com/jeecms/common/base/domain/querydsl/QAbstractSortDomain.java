package com.jeecms.common.base.domain.querydsl;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import com.jeecms.common.base.domain.AbstractSortDomain;
import com.querydsl.core.types.Path;

/**
 * QAbstractSortDomain is a Querydsl query type for AbstractSortDomain
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractSortDomain extends EntityPathBase<AbstractSortDomain<? extends java.io.Serializable>> {

	private static final long serialVersionUID = -330066663L;

	public static final QAbstractSortDomain abstractSortDomain = new QAbstractSortDomain("abstractSortDomain");

	public final QAbstractDomain _super = new QAbstractDomain(this);

	// inherited
	public final DateTimePath<java.util.Date> createTime = _super.createTime;

	// inherited
	public final StringPath createUser = _super.createUser;

	// inherited
	public final BooleanPath hasDeleted = _super.hasDeleted;

	public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

	// inherited
	public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

	// inherited
	public final StringPath updateUser = _super.updateUser;

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractSortDomain(String variable) {
		super((Class) AbstractSortDomain.class, PathMetadataFactory.forVariable(variable));
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractSortDomain(Path<? extends AbstractSortDomain> path) {
		super((Class) path.getType(), path.getMetadata());
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractSortDomain(PathMetadata metadata) {
		super((Class) AbstractSortDomain.class, metadata);
	}

}
