package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.Content;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContent is a Querydsl query type for Content
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContent extends EntityPathBase<Content> {

    private static final long serialVersionUID = 1287796710L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContent content = new QContent("content");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    public final com.jeecms.channel.domain.querydsl.QChannel channel;

    public final NumberPath<Integer> channelId = createNumber("channelId", Integer.class);

    public final NumberPath<Integer> commentControl = createNumber("commentControl", Integer.class);

    public final NumberPath<Integer> comments = createNumber("comments", Integer.class);

    public final ListPath<com.jeecms.content.domain.ContentAttr, QContentAttr> contentAttrs = this.<com.jeecms.content.domain.ContentAttr, QContentAttr>createList("contentAttrs", com.jeecms.content.domain.ContentAttr.class, QContentAttr.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.content.domain.ContentChannel, QContentChannel> contentChannels = this.<com.jeecms.content.domain.ContentChannel, QContentChannel>createList("contentChannels", com.jeecms.content.domain.ContentChannel.class, QContentChannel.class, PathInits.DIRECT2);

    public final ListPath<Content, QContent> contentCopys = this.<Content, QContent>createList("contentCopys", Content.class, QContent.class, PathInits.DIRECT2);

    public final QContentExt contentExt;

    public final ListPath<com.jeecms.content.domain.ContentRecord, QContentRecord> contentRecords = this.<com.jeecms.content.domain.ContentRecord, QContentRecord>createList("contentRecords", com.jeecms.content.domain.ContentRecord.class, QContentRecord.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.content.domain.ContentRelation, QContentRelation> contentRelations = this.<com.jeecms.content.domain.ContentRelation, QContentRelation>createList("contentRelations", com.jeecms.content.domain.ContentRelation.class, QContentRelation.class, PathInits.DIRECT2);

    public final NumberPath<Integer> contentSecretId = createNumber("contentSecretId", Integer.class);

    public final ListPath<com.jeecms.system.domain.ContentTag, com.jeecms.system.domain.querydsl.QContentTag> contentTags = this.<com.jeecms.system.domain.ContentTag, com.jeecms.system.domain.querydsl.QContentTag>createList("contentTags", com.jeecms.system.domain.ContentTag.class, com.jeecms.system.domain.querydsl.QContentTag.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.content.domain.ContentTxt, QContentTxt> contentTxts = this.<com.jeecms.content.domain.ContentTxt, QContentTxt>createList("contentTxts", com.jeecms.content.domain.ContentTxt.class, QContentTxt.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.system.domain.ContentType, com.jeecms.system.domain.querydsl.QContentType> contentTypes = this.<com.jeecms.system.domain.ContentType, com.jeecms.system.domain.querydsl.QContentType>createList("contentTypes", com.jeecms.system.domain.ContentType.class, com.jeecms.system.domain.querydsl.QContentType.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.content.domain.ContentVersion, QContentVersion> contentVersions = this.<com.jeecms.content.domain.ContentVersion, QContentVersion>createList("contentVersions", com.jeecms.content.domain.ContentVersion.class, QContentVersion.class, PathInits.DIRECT2);

    public final NumberPath<Integer> copySourceContentId = createNumber("copySourceContentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    public final NumberPath<Integer> createType = createNumber("createType", Integer.class);

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> downloads = createNumber("downloads", Integer.class);

    public final NumberPath<Integer> downs = createNumber("downs", Integer.class);

    public final BooleanPath edit = createBoolean("edit");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final BooleanPath hasStatic = createBoolean("hasStatic");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final DateTimePath<java.util.Date> offlineTime = createDateTime("offlineTime", java.util.Date.class);

    public final com.jeecms.auth.domain.querydsl.QCoreUser publishUser;

    public final NumberPath<Integer> publishUserId = createNumber("publishUserId", Integer.class);

    public final BooleanPath recycle = createBoolean("recycle");

    public final BooleanPath releaseApp = createBoolean("releaseApp");

    public final BooleanPath releaseMiniprogram = createBoolean("releaseMiniprogram");

    public final BooleanPath releasePc = createBoolean("releasePc");

    public final DateTimePath<java.util.Date> releaseTime = createDateTime("releaseTime", java.util.Date.class);

    public final BooleanPath releaseWap = createBoolean("releaseWap");

    public final com.jeecms.system.domain.querydsl.QSysSecret secret;

    public final StringPath shortTitle = createString("shortTitle");

    public final com.jeecms.system.domain.querydsl.QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    public final NumberPath<Integer> sortWeight = createNumber("sortWeight", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath titleColor = createString("titleColor");

    public final BooleanPath titleIsBold = createBoolean("titleIsBold");

    public final BooleanPath top = createBoolean("top");

    public final DateTimePath<java.util.Date> topEndTime = createDateTime("topEndTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> topStartTime = createDateTime("topStartTime", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> ups = createNumber("ups", Integer.class);

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final ListPath<com.jeecms.interact.domain.UserComment, com.jeecms.interact.domain.querydsl.QUserComment> userComments = this.<com.jeecms.interact.domain.UserComment, com.jeecms.interact.domain.querydsl.QUserComment>createList("userComments", com.jeecms.interact.domain.UserComment.class, com.jeecms.interact.domain.querydsl.QUserComment.class, PathInits.DIRECT2);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Short> viewControl = createNumber("viewControl", Short.class);

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QContent(String variable) {
        this(Content.class, forVariable(variable), INITS);
    }

    public QContent(Path<? extends Content> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContent(PathMetadata metadata, PathInits inits) {
        this(Content.class, metadata, inits);
    }

    public QContent(Class<? extends Content> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new com.jeecms.channel.domain.querydsl.QChannel(forProperty("channel"), inits.get("channel")) : null;
        this.contentExt = inits.isInitialized("contentExt") ? new QContentExt(forProperty("contentExt"), inits.get("contentExt")) : null;
        this.model = inits.isInitialized("model") ? new QCmsModel(forProperty("model"), inits.get("model")) : null;
        this.publishUser = inits.isInitialized("publishUser") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("publishUser"), inits.get("publishUser")) : null;
        this.secret = inits.isInitialized("secret") ? new com.jeecms.system.domain.querydsl.QSysSecret(forProperty("secret")) : null;
        this.site = inits.isInitialized("site") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

