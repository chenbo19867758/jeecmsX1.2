package com.jeecms.common.base.domain.querydsl;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import com.jeecms.common.base.domain.AbstractDomain;
import com.querydsl.core.types.Path;

/**
 * QAbstractDomain is a Querydsl query type for AbstractDomain
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractDomain extends EntityPathBase<AbstractDomain<? extends java.io.Serializable>> {

	private static final long serialVersionUID = 888407387L;

	public static final QAbstractDomain abstractDomain = new QAbstractDomain("abstractDomain");

	public final QAbstractDelFlagDomain _super = new QAbstractDelFlagDomain(this);

	public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

	public final StringPath createUser = createString("createUser");

	// inherited
	public final BooleanPath hasDeleted = _super.hasDeleted;

	public final DateTimePath<java.util.Date> updateTime = createDateTime("updateTime", java.util.Date.class);

	public final StringPath updateUser = createString("updateUser");

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractDomain(String variable) {
		super((Class) AbstractDomain.class, PathMetadataFactory.forVariable(variable));
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractDomain(Path<? extends AbstractDomain> path) {
		super((Class) path.getType(), path.getMetadata());
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractDomain(PathMetadata metadata) {
		super((Class) AbstractDomain.class, metadata);
	}

}
