package com.jeecms.questionnaire.domain.querydsl;

import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysQuestionnaireAnswer is a Querydsl query type for SysQuestionnaireAnswer
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysQuestionnaireAnswer extends EntityPathBase<SysQuestionnaireAnswer> {

    private static final long serialVersionUID = 1731912017L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysQuestionnaireAnswer sysQuestionnaireAnswer = new QSysQuestionnaireAnswer("sysQuestionnaireAnswer");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath answer = createString("answer");

    public final StringPath answerId = createString("answerId");

    public final MapPath<String, String, StringPath> attr = this.<String, String, StringPath>createMap("attr", String.class, String.class, StringPath.class);

    public final StringPath city = createString("city");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath device = createString("device");

    public final NumberPath<Integer> deviceType = createNumber("deviceType", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final BooleanPath isEffective = createBoolean("isEffective");

    public final StringPath province = createString("province");

    public final QSysQuestionnaire questionnaire;

    public final NumberPath<Integer> questionnaireId = createNumber("questionnaireId", Integer.class);

    public final NumberPath<Integer> replayId = createNumber("replayId", Integer.class);

    public final StringPath replayName = createString("replayName");

    public final QSysQuestionnaireSubject subject;

    public final NumberPath<Integer> subjectId = createNumber("subjectId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysQuestionnaireAnswer(String variable) {
        this(SysQuestionnaireAnswer.class, forVariable(variable), INITS);
    }

    public QSysQuestionnaireAnswer(Path<? extends SysQuestionnaireAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysQuestionnaireAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysQuestionnaireAnswer(PathMetadata metadata, PathInits inits) {
        this(SysQuestionnaireAnswer.class, metadata, inits);
    }

    public QSysQuestionnaireAnswer(Class<? extends SysQuestionnaireAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.questionnaire = inits.isInitialized("questionnaire") ? new QSysQuestionnaire(forProperty("questionnaire"), inits.get("questionnaire")) : null;
        this.subject = inits.isInitialized("subject") ? new QSysQuestionnaireSubject(forProperty("subject"), inits.get("subject")) : null;
    }

}

