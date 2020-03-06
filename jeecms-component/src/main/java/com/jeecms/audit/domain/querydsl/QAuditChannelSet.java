package com.jeecms.audit.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.jeecms.audit.domain.AuditChannelSet;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

/**
 * QAuditChannelSet is a Querydsl query type for AuditChannelSet
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuditChannelSet extends EntityPathBase<AuditChannelSet> {

	private static final long serialVersionUID = -147351579L;

	private static final PathInits INITS = PathInits.DIRECT2;

	public static final QAuditChannelSet auditChannelSet = new QAuditChannelSet("auditChannelSet");

	public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = 
			new com.jeecms.common.base.domain.querydsl.QAbstractDomain(
			this);

	public final com.jeecms.channel.domain.querydsl.QChannel channel;

	public final NumberPath<Integer> channelId = createNumber("channelId", Integer.class);

	// inherited
	public final DateTimePath<java.util.Date> createTime = _super.createTime;

	// inherited
	public final StringPath createUser = _super.createUser;

	// inherited
	public final BooleanPath hasDeleted = _super.hasDeleted;

	public final NumberPath<Integer> id = createNumber("id", Integer.class);

	public final BooleanPath isCompel = createBoolean("isCompel");

	public final BooleanPath status = createBoolean("status");

	public final QAuditStrategy strategy;

	public final NumberPath<Integer> strategyId = createNumber("strategyId", Integer.class);

	// inherited
	public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

	// inherited
	public final StringPath updateUser = _super.updateUser;

	public QAuditChannelSet(String variable) {
		this(AuditChannelSet.class, forVariable(variable), INITS);
	}

	public QAuditChannelSet(Path<? extends AuditChannelSet> path) {
		this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
	}

	public QAuditChannelSet(PathMetadata metadata) {
		this(metadata, PathInits.getFor(metadata, INITS));
	}

	public QAuditChannelSet(PathMetadata metadata, PathInits inits) {
		this(AuditChannelSet.class, metadata, inits);
	}

	public QAuditChannelSet(Class<? extends AuditChannelSet> type, PathMetadata metadata, PathInits inits) {
		super(type, metadata, inits);
		this.channel = inits.isInitialized("channel")
				? new com.jeecms.channel.domain.querydsl.QChannel(forProperty("channel"), inits.get("channel"))
				: null;
		this.strategy = inits.isInitialized("strategy")
				? new QAuditStrategy(forProperty("strategy"), inits.get("strategy"))
				: null;
	}

}
