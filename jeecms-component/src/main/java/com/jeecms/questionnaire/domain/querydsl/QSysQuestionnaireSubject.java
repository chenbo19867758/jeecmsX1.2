package com.jeecms.questionnaire.domain.querydsl;

import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
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
 * QSysQuestionnaireSubject is a Querydsl query type for SysQuestionnaireSubject
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysQuestionnaireSubject extends EntityPathBase<SysQuestionnaireSubject> {

    private static final long serialVersionUID = 1129178617L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysQuestionnaireSubject sysQuestionnaireSubject = new QSysQuestionnaireSubject("sysQuestionnaireSubject");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> allowFile = createNumber("allowFile", Integer.class);

    public final StringPath cascadeOption = createString("cascadeOption");

    public final NumberPath<Integer> column = createNumber("column", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final BooleanPath custom = createBoolean("custom");

    public final BooleanPath dragable = createBoolean("dragable");

    public final StringPath editor = createString("editor");

    public final BooleanPath fileCount = createBoolean("fileCount");

    public final NumberPath<Integer> fileCountLimit = createNumber("fileCountLimit", Integer.class);

    public final BooleanPath fileSize = createBoolean("fileSize");

    public final NumberPath<Integer> fileSizeLimit = createNumber("fileSizeLimit", Integer.class);

    public final StringPath fileSizeLimitUnit = createString("fileSizeLimitUnit");

    public final BooleanPath fileType = createBoolean("fileType");

    public final StringPath fileTypeLimit = createString("fileTypeLimit");

    public final NumberPath<Integer> groupIndex = createNumber("groupIndex", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath icon = createString("icon");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    public final NumberPath<Integer> inputHigh = createNumber("inputHigh", Integer.class);

    public final BooleanPath inputNumLimit = createBoolean("inputNumLimit");

    public final NumberPath<Integer> inputWidth = createNumber("inputWidth", Integer.class);

    public final BooleanPath isAnswer = createBoolean("isAnswer");

    public final BooleanPath isLimit = createBoolean("isLimit");

    public final NumberPath<Integer> limitCondition = createNumber("limitCondition", Integer.class);

    public final ListPath<com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption, QSysQuestionnaireSubjectOption> options = this.<com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption, QSysQuestionnaireSubjectOption>createList("options", com.jeecms.questionnaire.domain.SysQuestionnaireSubjectOption.class, QSysQuestionnaireSubjectOption.class, PathInits.DIRECT2);

    public final StringPath placeholder = createString("placeholder");

    public final StringPath preview = createString("preview");

    public final StringPath prompt = createString("prompt");

    public final StringPath prop = createString("prop");

    public final QSysQuestionnaire questionnaire;

    public final NumberPath<Integer> questionnaireId = createNumber("questionnaireId", Integer.class);

    public final BooleanPath radio = createBoolean("radio");

    public final StringPath title = createString("title");

    public final NumberPath<Short> type = createNumber("type", Short.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath value = createString("value");

    public final NumberPath<Integer> wordLimit = createNumber("wordLimit", Integer.class);

    public QSysQuestionnaireSubject(String variable) {
        this(SysQuestionnaireSubject.class, forVariable(variable), INITS);
    }

    public QSysQuestionnaireSubject(Path<? extends SysQuestionnaireSubject> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysQuestionnaireSubject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysQuestionnaireSubject(PathMetadata metadata, PathInits inits) {
        this(SysQuestionnaireSubject.class, metadata, inits);
    }

    public QSysQuestionnaireSubject(Class<? extends SysQuestionnaireSubject> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.questionnaire = inits.isInitialized("questionnaire") ? new QSysQuestionnaire(forProperty("questionnaire"), inits.get("questionnaire")) : null;
    }

}

