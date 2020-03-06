package com.jeecms.questionnaire.domain.querydsl;

import com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysQuestionnaireSubjectOption is a Querydsl query type for SysQuestionnaireSubjectOption
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysQuestionnaireSubjectOption extends EntityPathBase<SysQuestionnaireSubjectOption> {

    private static final long serialVersionUID = -911393842L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysQuestionnaireSubjectOption sysQuestionnaireSubjectOption = new QSysQuestionnaireSubjectOption("sysQuestionnaireSubjectOption");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final BooleanPath isOther = createBoolean("isOther");

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final StringPath name = createString("name");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData pic;

    public final NumberPath<Integer> picId = createNumber("picId", Integer.class);

    public final QSysQuestionnaireSubject questionnaireSubject;

    public final NumberPath<Integer> questionnaireSubjectId = createNumber("questionnaireSubjectId", Integer.class);

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysQuestionnaireSubjectOption(String variable) {
        this(SysQuestionnaireSubjectOption.class, forVariable(variable), INITS);
    }

    public QSysQuestionnaireSubjectOption(Path<? extends SysQuestionnaireSubjectOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysQuestionnaireSubjectOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysQuestionnaireSubjectOption(PathMetadata metadata, PathInits inits) {
        this(SysQuestionnaireSubjectOption.class, metadata, inits);
    }

    public QSysQuestionnaireSubjectOption(Class<? extends SysQuestionnaireSubjectOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pic = inits.isInitialized("pic") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("pic"), inits.get("pic")) : null;
        this.questionnaireSubject = inits.isInitialized("questionnaireSubject") ? new QSysQuestionnaireSubject(forProperty("questionnaireSubject"), inits.get("questionnaireSubject")) : null;
    }

}

