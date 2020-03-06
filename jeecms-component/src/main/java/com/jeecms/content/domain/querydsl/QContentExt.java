package com.jeecms.content.domain.querydsl;

import com.jeecms.content.domain.ContentExt;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QContentExt is a Querydsl query type for ContentExt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentExt extends EntityPathBase<ContentExt> {

    private static final long serialVersionUID = 2103969883L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentExt contentExt = new QContentExt("contentExt");

    public final StringPath author = createString("author");

    public final NumberPath<Integer> commentsDay = createNumber("commentsDay", Integer.class);

    public final NumberPath<Integer> commentsMonth = createNumber("commentsMonth", Integer.class);

    public final NumberPath<Integer> commentsWeek = createNumber("commentsWeek", Integer.class);

    public final QContent content;

    public final com.jeecms.system.domain.querydsl.QContentSource contentSource;

    public final NumberPath<Integer> contentSourceId = createNumber("contentSourceId", Integer.class);

    public final NumberPath<Integer> currNodeId = createNumber("currNodeId", Integer.class);

    public final StringPath description = createString("description");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData docResource;

    public final NumberPath<Integer> docResourceId = createNumber("docResourceId", Integer.class);

    public final NumberPath<Integer> downloadsDay = createNumber("downloadsDay", Integer.class);

    public final NumberPath<Integer> downloadsMonth = createNumber("downloadsMonth", Integer.class);

    public final NumberPath<Integer> downloadsWeek = createNumber("downloadsWeek", Integer.class);

    public final NumberPath<Integer> downsDay = createNumber("downsDay", Integer.class);

    public final NumberPath<Integer> downsMonth = createNumber("downsMonth", Integer.class);

    public final NumberPath<Integer> downsWeek = createNumber("downsWeek", Integer.class);

    public final StringPath flowProcessId = createString("flowProcessId");

    public final NumberPath<Integer> flowStartUserId = createNumber("flowStartUserId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isNewTarget = createBoolean("isNewTarget");

    public final StringPath issueNum = createString("issueNum");

    public final NumberPath<Integer> issueOrg = createNumber("issueOrg", Integer.class);

    public final NumberPath<Integer> issueYear = createNumber("issueYear", Integer.class);

    public final StringPath keyWord = createString("keyWord");

    public final StringPath outLink = createString("outLink");

    public final StringPath pdfUrl = createString("pdfUrl");

    public final NumberPath<Integer> picResId = createNumber("picResId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData reData;

    public final com.jeecms.system.domain.querydsl.QContentMark sueOrg;

    public final com.jeecms.system.domain.querydsl.QContentMark sueYear;

    public final StringPath tplMobile = createString("tplMobile");

    public final StringPath tplPc = createString("tplPc");

    public final NumberPath<Integer> upsDay = createNumber("upsDay", Integer.class);

    public final NumberPath<Integer> upsMonth = createNumber("upsMonth", Integer.class);

    public final NumberPath<Integer> upsWeek = createNumber("upsWeek", Integer.class);

    public final NumberPath<Integer> viewsDay = createNumber("viewsDay", Integer.class);

    public final NumberPath<Integer> viewsMonth = createNumber("viewsMonth", Integer.class);

    public final NumberPath<Integer> viewsWeek = createNumber("viewsWeek", Integer.class);

    public final StringPath wbMediaId = createString("wbMediaId");

    public final NumberPath<Integer> workflowId = createNumber("workflowId", Integer.class);

    public final StringPath wxMediaId = createString("wxMediaId");

    public QContentExt(String variable) {
        this(ContentExt.class, forVariable(variable), INITS);
    }

    public QContentExt(Path<? extends ContentExt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentExt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentExt(PathMetadata metadata, PathInits inits) {
        this(ContentExt.class, metadata, inits);
    }

    public QContentExt(Class<? extends ContentExt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
        this.contentSource = inits.isInitialized("contentSource") ? new com.jeecms.system.domain.querydsl.QContentSource(forProperty("contentSource")) : null;
        this.docResource = inits.isInitialized("docResource") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("docResource"), inits.get("docResource")) : null;
        this.reData = inits.isInitialized("reData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("reData"), inits.get("reData")) : null;
        this.sueOrg = inits.isInitialized("sueOrg") ? new com.jeecms.system.domain.querydsl.QContentMark(forProperty("sueOrg")) : null;
        this.sueYear = inits.isInitialized("sueYear") ? new com.jeecms.system.domain.querydsl.QContentMark(forProperty("sueYear")) : null;
    }

}

