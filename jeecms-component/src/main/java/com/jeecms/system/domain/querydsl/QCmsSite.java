package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.CmsSite;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsSite is a Querydsl query type for CmsSite
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsSite extends EntityPathBase<CmsSite> {

    private static final long serialVersionUID = -1975272029L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsSite cmsSite = new QCmsSite("cmsSite");

    public final com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain _super;

    public final MapPath<String, String, StringPath> cfg = this.<String, String, StringPath>createMap("cfg", String.class, String.class, StringPath.class);

    public final ListPath<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel> channels = this.<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel>createList("channels", com.jeecms.channel.domain.Channel.class, com.jeecms.channel.domain.querydsl.QChannel.class, PathInits.DIRECT2);

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> children;

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> childs;

    public final QCmsSiteExt cmsSiteExt;

    //inherited
    public final DateTimePath<java.util.Date> createTime;

    //inherited
    public final StringPath createUser;

    public final StringPath description = createString("description");

    public final StringPath domain = createString("domain");

    public final StringPath domainAlias = createString("domainAlias");

    public final QGlobalConfig globalConfig;

    public final NumberPath<Integer> globalConfigId = createNumber("globalConfigId", Integer.class);
    
    public final NumberPath<Integer> sortWeight = createNumber("sortWeight", Integer.class);

    //inherited
    public final BooleanPath hasDeleted;

    public final NumberPath<Integer> iconId = createNumber("iconId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData iconRes;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isDelete = createBoolean("isDelete");

    public final BooleanPath isOpen = createBoolean("isOpen");

    //inherited
    public final NumberPath<Integer> lft;

    public final StringPath mobileIndexTpl = createString("mobileIndexTpl");

    public final ListPath<com.jeecms.system.domain.SiteModelTpl, QSiteModelTpl> modelTpls = this.<com.jeecms.system.domain.SiteModelTpl, QSiteModelTpl>createList("modelTpls", com.jeecms.system.domain.SiteModelTpl.class, QSiteModelTpl.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final QCmsSite parent;

    //inherited
    public final NumberPath<Integer> parentId;

    public final StringPath path = createString("path");

    public final StringPath pcIndexTpl = createString("pcIndexTpl");

    public final StringPath protocol = createString("protocol");

    //inherited
    public final NumberPath<Integer> rgt;

    public final StringPath seoDescription = createString("seoDescription");

    public final StringPath seoKeyword = createString("seoKeyword");

    public final StringPath seoTitle = createString("seoTitle");

    public final StringPath siteLanguage = createString("siteLanguage");

    //inherited
    public final NumberPath<Integer> sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime;

    //inherited
    public final StringPath updateUser;

    public QCmsSite(String variable) {
        this(CmsSite.class, forVariable(variable), INITS);
    }

    public QCmsSite(Path<? extends CmsSite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsSite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsSite(PathMetadata metadata, PathInits inits) {
        this(CmsSite.class, metadata, inits);
    }

    public QCmsSite(Class<? extends CmsSite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain(type, metadata, inits);
        this.children = _super.children;
        this.childs = _super.childs;
        this.cmsSiteExt = inits.isInitialized("cmsSiteExt") ? new QCmsSiteExt(forProperty("cmsSiteExt"), inits.get("cmsSiteExt")) : null;
        this.createTime = _super.createTime;
        this.createUser = _super.createUser;
        this.globalConfig = inits.isInitialized("globalConfig") ? new QGlobalConfig(forProperty("globalConfig")) : null;
        this.hasDeleted = _super.hasDeleted;
        this.iconRes = inits.isInitialized("iconRes") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("iconRes"), inits.get("iconRes")) : null;
        this.lft = _super.lft;
        this.parent = inits.isInitialized("parent") ? new QCmsSite(forProperty("parent")) : null;
        this.parentId = _super.parentId;
        this.rgt = _super.rgt;
        this.sortNum = _super.sortNum;
        this.updateTime = _super.updateTime;
        this.updateUser = _super.updateUser;
    }

}

