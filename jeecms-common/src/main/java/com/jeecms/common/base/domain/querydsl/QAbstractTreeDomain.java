package com.jeecms.common.base.domain.querydsl;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import com.jeecms.common.base.domain.AbstractTreeDomain;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

/**
 * QAbstractTreeDomain is a Querydsl query type for AbstractTreeDomain
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractTreeDomain extends EntityPathBase<AbstractTreeDomain<?, ? extends java.io.Serializable>> {

	private static final long serialVersionUID = 1031639833L;

	private static final PathInits INITS = PathInits.DIRECT2;

	public static final QAbstractTreeDomain abstractTreeDomain = new QAbstractTreeDomain("abstractTreeDomain");

	public final QAbstractSortDomain _super = new QAbstractSortDomain(this);

	public final ListPath<AbstractTreeDomain<?, ?>, QAbstractTreeDomain> childs =  this.<AbstractTreeDomain<?, ?>, 
			QAbstractTreeDomain>createList("childs", 
				AbstractTreeDomain.class,QAbstractTreeDomain.class, PathInits.DIRECT2);
	
	public final ListPath<AbstractTreeDomain<?, ?>, QAbstractTreeDomain> children =  
			this.<AbstractTreeDomain<?, ?>, 
			QAbstractTreeDomain>createList("children", 
				AbstractTreeDomain.class,QAbstractTreeDomain.class, PathInits.DIRECT2);

	// inherited
	public final DateTimePath<java.util.Date> createTime = _super.createTime;

	// inherited
	public final StringPath createUser = _super.createUser;

	// inherited
	public final BooleanPath hasDeleted = _super.hasDeleted;

	public final NumberPath<Integer> lft = createNumber("lft", Integer.class);

	public final QAbstractTreeDomain parent;

	public final NumberPath<Integer> parentId = createNumber("parentId", Integer.class);

	public final NumberPath<Integer> rgt = createNumber("rgt", Integer.class);

	// inherited
	public final NumberPath<Integer> sortNum = _super.sortNum;

	// inherited
	public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

	// inherited
	public final StringPath updateUser = _super.updateUser;

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractTreeDomain(String variable) {
		this((Class) AbstractTreeDomain.class, PathMetadataFactory.forVariable(variable), INITS);
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractTreeDomain(Path<? extends AbstractTreeDomain> path) {
		this((Class) path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
	}

	public QAbstractTreeDomain(PathMetadata metadata) {
		this(metadata, PathInits.getFor(metadata, INITS));
	}

	@SuppressWarnings({ "all", "rawtypes", "unchecked" })
	public QAbstractTreeDomain(PathMetadata metadata, PathInits inits) {
		this((Class) AbstractTreeDomain.class, metadata, inits);
	}

	/**
	 * 获取QAbstractTreeDomain
	 * @param type class
	 * @param metadata PathMetadata
	 * @param inits PathInits
	 */
	public QAbstractTreeDomain(Class<? extends AbstractTreeDomain<?, ? extends java.io.Serializable>> type,
			PathMetadata metadata, PathInits inits) {
		super(type, metadata, inits);
		this.parent = inits.isInitialized("parent")
				? new QAbstractTreeDomain(forProperty("parent"), inits.get("parent")) : null;
	}

}
