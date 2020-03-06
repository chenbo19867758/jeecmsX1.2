package com.jeecms.backup.domain.querydsl;

import com.jeecms.backup.domain.DatabaseBackupRecord;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QDatabaseBackupRecord is a Querydsl query type for DatabaseBackupRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDatabaseBackupRecord extends EntityPathBase<DatabaseBackupRecord> {

    private static final long serialVersionUID = -1951223730L;

    public static final QDatabaseBackupRecord databaseBackupRecord = new QDatabaseBackupRecord("databaseBackupRecord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appBakPath = createString("appBakPath");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath databaseName = createString("databaseName");

    public final StringPath dataSourceIp = createString("dataSourceIp");

    public final NumberPath<Integer> dataSourcePort = createNumber("dataSourcePort", Integer.class);

    public final StringPath dbBakPath = createString("dbBakPath");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final StringPath errMsg = createString("errMsg");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QDatabaseBackupRecord(String variable) {
        super(DatabaseBackupRecord.class, forVariable(variable));
    }

    public QDatabaseBackupRecord(Path<? extends DatabaseBackupRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDatabaseBackupRecord(PathMetadata metadata) {
        super(DatabaseBackupRecord.class, metadata);
    }

}

