package com.jeecms.questionnaire.domain.querydsl;

import com.jeecms.questionnaire.domain.SysQuestionnaireConfig;
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
 * QSysQuestionnaireConfig is a Querydsl query type for SysQuestionnaireConfig
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysQuestionnaireConfig extends EntityPathBase<SysQuestionnaireConfig> {

    private static final long serialVersionUID = 1789928661L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysQuestionnaireConfig sysQuestionnaireConfig = new QSysQuestionnaireConfig("sysQuestionnaireConfig");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final BooleanPath answerLimit = createBoolean("answerLimit");

    public final DateTimePath<java.util.Date> beginTime = createDateTime("beginTime", java.util.Date.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData coverPic;

    public final NumberPath<Integer> coverPicId = createNumber("coverPicId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> deviceAnswerFrequencyLimit = createNumber("deviceAnswerFrequencyLimit", Integer.class);

    public final NumberPath<Short> deviceAnswerFrequencyLimitUnit = createNumber("deviceAnswerFrequencyLimitUnit", Short.class);

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> ipAnswerFrequencyLimit = createNumber("ipAnswerFrequencyLimit", Integer.class);

    public final NumberPath<Short> ipAnswerFrequencyLimitUnit = createNumber("ipAnswerFrequencyLimitUnit", Short.class);

    public final BooleanPath isOnlyWechat = createBoolean("isOnlyWechat");

    public final BooleanPath isVerification = createBoolean("isVerification");

    public final NumberPath<Short> processType = createNumber("processType", Short.class);

    public final StringPath prompt = createString("prompt");

    public final QSysQuestionnaire questionnaire;

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData shareLogo;

    public final NumberPath<Integer> shareLogoId = createNumber("shareLogoId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> userAnswerFrequencyLimit = createNumber("userAnswerFrequencyLimit", Integer.class);

    public final NumberPath<Short> userAnswerFrequencyLimitUnit = createNumber("userAnswerFrequencyLimitUnit", Short.class);

    public final NumberPath<Integer> wechatAnswerFrequencyLimit = createNumber("wechatAnswerFrequencyLimit", Integer.class);

    public final NumberPath<Short> wechatAnswerFrequencyLimitUnit = createNumber("wechatAnswerFrequencyLimitUnit", Short.class);

    public final NumberPath<Integer> workflowId = createNumber("workflowId", Integer.class);

    public QSysQuestionnaireConfig(String variable) {
        this(SysQuestionnaireConfig.class, forVariable(variable), INITS);
    }

    public QSysQuestionnaireConfig(Path<? extends SysQuestionnaireConfig> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysQuestionnaireConfig(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysQuestionnaireConfig(PathMetadata metadata, PathInits inits) {
        this(SysQuestionnaireConfig.class, metadata, inits);
    }

    public QSysQuestionnaireConfig(Class<? extends SysQuestionnaireConfig> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coverPic = inits.isInitialized("coverPic") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("coverPic"), inits.get("coverPic")) : null;
        this.questionnaire = inits.isInitialized("questionnaire") ? new QSysQuestionnaire(forProperty("questionnaire"), inits.get("questionnaire")) : null;
        this.shareLogo = inits.isInitialized("shareLogo") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("shareLogo"), inits.get("shareLogo")) : null;
    }

}

