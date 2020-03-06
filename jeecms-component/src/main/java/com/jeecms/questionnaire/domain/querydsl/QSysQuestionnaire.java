package com.jeecms.questionnaire.domain.querydsl;

import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysQuestionnaire is a Querydsl query type for SysQuestionnaire
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysQuestionnaire extends EntityPathBase<SysQuestionnaire> {

    private static final long serialVersionUID = -57571405L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysQuestionnaire sysQuestionnaire = new QSysQuestionnaire("sysQuestionnaire");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> answerCount = createNumber("answerCount", Integer.class);

    public final ListPath<com.jeecms.questionnaire.domain.SysQuestionnaireAnswer, QSysQuestionnaireAnswer> answers = this.<com.jeecms.questionnaire.domain.SysQuestionnaireAnswer, QSysQuestionnaireAnswer>createList("answers", com.jeecms.questionnaire.domain.SysQuestionnaireAnswer.class, QSysQuestionnaireAnswer.class, PathInits.DIRECT2);

    public final StringPath bgConfig = createString("bgConfig");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData bgImg;

    public final NumberPath<Integer> bgImgId = createNumber("bgImgId", Integer.class);

    public final BooleanPath checkStatus = createBoolean("checkStatus");

    public final StringPath contConfig = createString("contConfig");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> currNodeId = createNumber("currNodeId", Integer.class);

    public final StringPath details = createString("details");

    public final StringPath flowProcessId = createString("flowProcessId");

    public final NumberPath<Integer> flowStartUserId = createNumber("flowStartUserId", Integer.class);

    public final StringPath fontConfig = createString("fontConfig");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath headConfig = createString("headConfig");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData headImg;

    public final NumberPath<Integer> headImgId = createNumber("headImgId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> pageViews = createNumber("pageViews", Integer.class);

    public final StringPath qrCode = createString("qrCode");

    public final QSysQuestionnaireConfig questionnaireConfig;

    public final com.jeecms.system.domain.querydsl.QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath subConfig = createString("subConfig");

    public final ListPath<com.jeecms.questionnaire.domain.SysQuestionnaireSubject, QSysQuestionnaireSubject> subjects = this.<com.jeecms.questionnaire.domain.SysQuestionnaireSubject, QSysQuestionnaireSubject>createList("subjects", com.jeecms.questionnaire.domain.SysQuestionnaireSubject.class, QSysQuestionnaireSubject.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> workflowId = createNumber("workflowId", Integer.class);

    public QSysQuestionnaire(String variable) {
        this(SysQuestionnaire.class, forVariable(variable), INITS);
    }

    public QSysQuestionnaire(Path<? extends SysQuestionnaire> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysQuestionnaire(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysQuestionnaire(PathMetadata metadata, PathInits inits) {
        this(SysQuestionnaire.class, metadata, inits);
    }

    public QSysQuestionnaire(Class<? extends SysQuestionnaire> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bgImg = inits.isInitialized("bgImg") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("bgImg"), inits.get("bgImg")) : null;
        this.headImg = inits.isInitialized("headImg") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("headImg"), inits.get("headImg")) : null;
        this.questionnaireConfig = inits.isInitialized("questionnaireConfig") ? new QSysQuestionnaireConfig(forProperty("questionnaireConfig"), inits.get("questionnaireConfig")) : null;
        this.site = inits.isInitialized("site") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

