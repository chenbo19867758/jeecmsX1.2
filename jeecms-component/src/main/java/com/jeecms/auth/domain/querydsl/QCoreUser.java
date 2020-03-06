package com.jeecms.auth.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.auth.domain.CoreUser;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoreUser is a Querydsl query type for CoreUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoreUser extends EntityPathBase<CoreUser> {

    private static final long serialVersionUID = -784123152L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoreUser coreUser = new QCoreUser("coreUser");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final BooleanPath admin = createBoolean("admin");

    public final StringPath appId = createString("appId");

    public final NumberPath<Short> checkStatus = createNumber("checkStatus", Short.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final SetPath<com.jeecms.system.domain.CmsDataPerm, com.jeecms.system.domain.querydsl.QCmsDataPerm> dataPerms = this.<com.jeecms.system.domain.CmsDataPerm, com.jeecms.system.domain.querydsl.QCmsDataPerm>createSet("dataPerms", com.jeecms.system.domain.CmsDataPerm.class, com.jeecms.system.domain.querydsl.QCmsDataPerm.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final BooleanPath enabled = createBoolean("enabled");

    public final DateTimePath<java.util.Date> firstLoginErrorTime = createDateTime("firstLoginErrorTime", java.util.Date.class);

    public final NumberPath<Integer> groupId = createNumber("groupId", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> integral = createNumber("integral", Integer.class);

    public final BooleanPath isResetPassword = createBoolean("isResetPassword");

    public final StringPath lastLoginIp = createString("lastLoginIp");

    public final DateTimePath<java.util.Date> lastLoginTime = createDateTime("lastLoginTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastPasswordChange = createDateTime("lastPasswordChange", java.util.Date.class);

    public final NumberPath<Integer> levelId = createNumber("levelId", Integer.class);

    public final ListPath<com.jeecms.interact.domain.UserComment, com.jeecms.interact.domain.querydsl.QUserComment> likeComments = this.<com.jeecms.interact.domain.UserComment, com.jeecms.interact.domain.querydsl.QUserComment>createList("likeComments", com.jeecms.interact.domain.UserComment.class, com.jeecms.interact.domain.querydsl.QUserComment.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.content.domain.Content, com.jeecms.content.domain.querydsl.QContent> likeContents = this.<com.jeecms.content.domain.Content, com.jeecms.content.domain.querydsl.QContent>createList("likeContents", com.jeecms.content.domain.Content.class, com.jeecms.content.domain.querydsl.QContent.class, PathInits.DIRECT2);

    public final NumberPath<Integer> loginCount = createNumber("loginCount", Integer.class);

    public final NumberPath<Integer> loginErrorCount = createNumber("loginErrorCount", Integer.class);

    public final DateTimePath<java.util.Date> loginLimitEnd = createDateTime("loginLimitEnd", java.util.Date.class);

    public final ListPath<com.jeecms.member.domain.MemberAttr, com.jeecms.member.domain.querydsl.QMemberAttr> memberAttrs = this.<com.jeecms.member.domain.MemberAttr, com.jeecms.member.domain.querydsl.QMemberAttr>createList("memberAttrs", com.jeecms.member.domain.MemberAttr.class, com.jeecms.member.domain.querydsl.QMemberAttr.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.member.domain.SysUserThird, com.jeecms.member.domain.querydsl.QSysUserThird> memberThirds = this.<com.jeecms.member.domain.SysUserThird, com.jeecms.member.domain.querydsl.QSysUserThird>createList("memberThirds", com.jeecms.member.domain.SysUserThird.class, com.jeecms.member.domain.querydsl.QSysUserThird.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.auth.domain.CoreMenu, QCoreMenu> menus = this.<com.jeecms.auth.domain.CoreMenu, QCoreMenu>createList("menus", com.jeecms.auth.domain.CoreMenu.class, QCoreMenu.class, PathInits.DIRECT2);

    public final com.jeecms.system.domain.querydsl.QCmsOrg org;

    public final NumberPath<Integer> orgId = createNumber("orgId", Integer.class);

    public final BooleanPath passMsgHasSend = createBoolean("passMsgHasSend");

    public final StringPath password = createString("password");

    public final ListPath<com.jeecms.system.domain.CmsDataPermCfg, com.jeecms.system.domain.querydsl.QCmsDataPermCfg> permCfgs = this.<com.jeecms.system.domain.CmsDataPermCfg, com.jeecms.system.domain.querydsl.QCmsDataPermCfg>createList("permCfgs", com.jeecms.system.domain.CmsDataPermCfg.class, com.jeecms.system.domain.querydsl.QCmsDataPermCfg.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.auth.domain.CoreRole, QCoreRole> roles = this.<com.jeecms.auth.domain.CoreRole, QCoreRole>createList("roles", com.jeecms.auth.domain.CoreRole.class, QCoreRole.class, PathInits.DIRECT2);

    public final StringPath salt = createString("salt");

    public final SetPath<com.jeecms.system.domain.CmsSite, com.jeecms.system.domain.querydsl.QCmsSite> sites = this.<com.jeecms.system.domain.CmsSite, com.jeecms.system.domain.querydsl.QCmsSite>createSet("sites", com.jeecms.system.domain.CmsSite.class, com.jeecms.system.domain.querydsl.QCmsSite.class, PathInits.DIRECT2);

    public final com.jeecms.system.domain.querydsl.QCmsSite sourceSite;

    public final NumberPath<Integer> sourceSiteId = createNumber("sourceSiteId", Integer.class);

    public final StringPath telephone = createString("telephone");

    public final BooleanPath third = createBoolean("third");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QCoreUserExt userExt;

    public final com.jeecms.member.domain.querydsl.QMemberGroup userGroup;

    public final com.jeecms.member.domain.querydsl.QMemberLevel userLevel;

    public final StringPath username = createString("username");

    public final com.jeecms.system.domain.querydsl.QSysUserSecret userSecret;

    public final NumberPath<Integer> userSecretId = createNumber("userSecretId", Integer.class);

    public final ListPath<com.jeecms.wechat.domain.AbstractWeChatInfo, com.jeecms.wechat.domain.querydsl.QAbstractWeChatInfo> wechatInfos = this.<com.jeecms.wechat.domain.AbstractWeChatInfo, com.jeecms.wechat.domain.querydsl.QAbstractWeChatInfo>createList("wechatInfos", com.jeecms.wechat.domain.AbstractWeChatInfo.class, com.jeecms.wechat.domain.querydsl.QAbstractWeChatInfo.class, PathInits.DIRECT2);

    public final ListPath<com.jeecms.weibo.domain.WeiboInfo, com.jeecms.weibo.domain.querydsl.QWeiboInfo> weiboInfos = this.<com.jeecms.weibo.domain.WeiboInfo, com.jeecms.weibo.domain.querydsl.QWeiboInfo>createList("weiboInfos", com.jeecms.weibo.domain.WeiboInfo.class, com.jeecms.weibo.domain.querydsl.QWeiboInfo.class, PathInits.DIRECT2);

    public QCoreUser(String variable) {
        this(CoreUser.class, forVariable(variable), INITS);
    }

    public QCoreUser(Path<? extends CoreUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoreUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoreUser(PathMetadata metadata, PathInits inits) {
        this(CoreUser.class, metadata, inits);
    }

    public QCoreUser(Class<? extends CoreUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.org = inits.isInitialized("org") ? new com.jeecms.system.domain.querydsl.QCmsOrg(forProperty("org"), inits.get("org")) : null;
        this.sourceSite = inits.isInitialized("sourceSite") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("sourceSite"), inits.get("sourceSite")) : null;
        this.userExt = inits.isInitialized("userExt") ? new QCoreUserExt(forProperty("userExt"), inits.get("userExt")) : null;
        this.userGroup = inits.isInitialized("userGroup") ? new com.jeecms.member.domain.querydsl.QMemberGroup(forProperty("userGroup")) : null;
        this.userLevel = inits.isInitialized("userLevel") ? new com.jeecms.member.domain.querydsl.QMemberLevel(forProperty("userLevel"), inits.get("userLevel")) : null;
        this.userSecret = inits.isInitialized("userSecret") ? new com.jeecms.system.domain.querydsl.QSysUserSecret(forProperty("userSecret")) : null;
    }

}

