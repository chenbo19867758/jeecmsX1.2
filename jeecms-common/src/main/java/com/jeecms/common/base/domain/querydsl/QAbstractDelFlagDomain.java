package com.jeecms.common.base.domain.querydsl;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;

import javax.annotation.Generated;

import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.querydsl.core.types.Path;

/**
 * QAbstractDelFlagDomain is a Querydsl query type for AbstractDelFlagDomain
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractDelFlagDomain extends EntityPathBase<AbstractDelFlagDomain<? extends java.io.Serializable>> {

	private static final long serialVersionUID = -147073724L;

	public static final QAbstractDelFlagDomain abstractDelFlagDomain = new QAbstractDelFlagDomain(
			"abstractDelFlagDomain");

	public final BooleanPath hasDeleted = createBoolean("hasDeleted");

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractDelFlagDomain(String variable) {
		super((Class) AbstractDelFlagDomain.class, PathMetadataFactory.forVariable(variable));
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractDelFlagDomain(Path<? extends AbstractDelFlagDomain> path) {
		super((Class) path.getType(), path.getMetadata());
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractDelFlagDomain(PathMetadata metadata) {
		super((Class) AbstractDelFlagDomain.class, metadata);
	}

}
