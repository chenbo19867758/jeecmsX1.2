/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.constants;

/**
 * 模型常量
 * 
 * @author: tom
 * @date: 2019年5月13日 下午3:20:55
 */
public class CmsModelConstant {

	/**
	 * 字段类型
	 */
	/** 单行文本 */
	public static final String TEXT = "input";
	/** 单选 */
	public static final String SINGLE_CHOOSE = "radio";
	/** 多选 */
	public static final String MANY_CHOOSE = "checkbox";
	/** 选择框 */
	public static final String DROP_DOWN = "select";
	/** 日期 */
	public static final String DATE = "dateTime";
	/** 单图上传 */
	public static final String SINGLE_CHART_UPLOAD = "imageUpload";
	/** 附件上传 */
	public static final String ANNEX_UPLOAD = "fileUpload";
	/** 视频上传 */
	public static final String VIDEO_UPLOAD = "videoUpload";
	/** 多图上传 */
	public static final String MANY_CHART_UPLOAD = "multiImageUpload";
	/** 音频上传 */
	public static final String AUDIO_UPLOAD = "audioUpload";
	/** 多行文本 */
	public static final String TEXTS = "textarea";
	
	public static final String SOURCE = "source";
	/** 年龄*/
	public static final String AGE = "age";
	/** 手机号*/
	public static final String MOBILE = "mobile";
	/** 座机号*/
	public static final String PHONE = "phone";
	/** 邮箱*/
	public static final String EMIAL = "email";
	/** 真实姓名*/
	public static final String REALNAME = "realname";
	/** 生日*/
	public static final String BIRTHDAY = "birthday";
	/** 性别*/
	public static final String SEX = "sex";
	/** 所在地 */
	public static final String ADDRESS = "address";
	/** 组织 */
	public static final String TISSUE = "organize";
	/** 身份证号*/
	public static final String IDENTITY = "identity";
	/** 城市 */
	public static final String CITY = "city";
	/** 传真*/
	public static final String FAX = "fax";
	
	/** 内容：正文*/
	public static final String CONTENT_TXT = "content";
	/** 富文本 */
	public static final String RICH_TEXT = "ueditor";

	/** 默认字段：单图上传*/
	public static final String TYPE_SYS_CONTENT_RESOURCE = "singleImage";
	
	// 内容默认字段名称
	/** 所属栏目*/
	public static final String FIELD_SYS_CHANNEL = "channelId";
	/** 摘要*/
	public static final String FIELD_SYS_DESCRIPTION = "description";
	/** 关键词*/
	public static final String FIELD_SYS_KEY_WORD = "keyWord";
	/** 副标题*/
	public static final String FIELD_SYS_SHORT_TITLE = "shortTitle";
	/** 作者*/
	public static final String FIELD_SYS_AUTHOR = "author";
	/** 标题*/
	public static final String FIELD_SYS_TITLE = "title";
	/** pc模板字段名*/
	public static final String FIELD_SYS_TPL_PC = "tplPc";
	/** 手机模板字段名*/
	public static final String FIELD_SYS_TPL_MOBILE = "tplMobile";
	/** 内容模板设置*/
	public static final String FIELD_SYS_TPL_CONTENT = "contentTpls";
	/** 是否开启静态服务*/
	public static final String FIELD_SYS_STATIC_CHANNEL = "staticChannel";
	/** 限制上传的文件类型格式*/
	public static final String FIELD_ATTR_LIMIT_FILE_TYPE = "limitFileType";
	/** 限制的上传文件大小*/
	public static final String FIELD_ATTR_LIMIT_FILE_SIZE = "limitFileSize";
	/** 工作流设置*/
	public static final String FIELD_SYS_WORKFLOW = "workflowId";
	/** 浏览设置*/
	public static final String FIELD_SYS_VIEW_CONTROL = "viewControl";
	/** 评论设置*/
	public static final String FIELD_SYS_COMMENT_CONTROL = "allowComment";
	/** 密级设置*/
	public static final String FIELD_SYS_CONTENT_SECRET = "contentSecretId";
	/** 来源设置*/
	public static final String FIELD_SYS_CONTENT_SOURCE = "contentSourceId";
	/** Tag词*/
	public static final String FIELD_SYS_CONTENT_CONTENTTAG = "contentTag";
	/** 正文*/
	public static final String FIELD_SYS_CONTENT_CONTXT = "content";
	/** 外部链接*/
	public static final String FIELD_SYS_CONTENT_OUTLINK = "outLink";
	/** 发布平台*/
	public static final String FIELD_SYS_CONTENT_RELEASE_TERRACE = "releaseTerrace";
	/** 单图上传*/
	public static final String FIELD_SYS_CONTENT_RESOURCE = "resource";
	/** 发文字号*/
	public static final String FIELD_SYS_CONTENT_POST_CONTENT = "postContent";
	/** 发布时间*/
	public static final String FIELD_SYS_RELEASE_TIME = "releaseTime";
	/** 下线时间*/
	public static final String FIELD_SYS_OFFLINE_TIME = "offlineTime";
	/** 文库文档*/
	public static final String FIELD_SYS_TEXTLIBRARY = "textLibrary";
	
	// 栏目默认字段名称
	/** 栏目是否包含分页*/
	public static final String FIELD_SYS_CHANNEL_LIST = "isListChannel";
	/** 上级栏目*/
	public static final String FIELD_CHANNEL_PARENT_ID = "channelParentId";
	/** 栏目名称*/
	public static final String FIELD_CHANNEL_NAME = "channelName";
	/** 访问路径*/
	public static final String FIELD_CHANNEL_PATH = "channelPath";
	/** 内容模板*/
	public static final String FIELD_CHANNEL_CONTENT_TPLS = "contentTpls";
	/** 外部链接*/
	public static final String FIELD_CHANNEL_LINK = "link";
	/** 显示在栏目循环列表*/
	public static final String FIELD_CHANNEL_DISPLAY = "display";
	/** PC端模板*/
	public static final String FIELD_CHANNEL_TPL_PC = "tplPc";
	/** 手机端模板*/
	public static final String FIELD_CHANNEL_TPL_MOBILE = "tplMobile";
	/** 浏览设置*/
	public static final String FIELD_CHANNEL_VIEW_CONTROL = "viewControl";
	/** 评论设置*/
	public static final String FIELD_CHANNEL_COMMENT_CONTROL = "commentControl";
	/** 工作流设置*/
	public static final String FIELD_CHANNEL_WORKFLOWID = "workflowId";
	/** SEO标题*/
	public static final String FIELD_CHANNEL_SEO_TITLE = "seoTitle";
	/** SEO关键字*/
	public static final String FIELD_CHANNEL_SEO_KEYWORD = "seoKeywork";
	/** SEO描述*/
	public static final String FIELD_CHANNEL_SEO_DESCRIPTION = "seoDescription";
	/** 是否允许投稿*/
	public static final String FIELD_CHANNEL_CONTRIBUTE = "contribute";
	/** 内容列表每页显示的内容数*/
	public static final String FIELD_CHANNEL_PAGE_SIZE = "pageSize";
	/** 栏目描述*/
	public static final String FIELD_CHANNEL_TXT = "txt";
	/** 栏目图片*/
	public static final String FIELD_CHANNEL_RESOURCE_ID = "resourceId";
	
	// 会员模型默认字段
	/**邮箱**/
	public static final String FIELD_MEMBER_EMAIL = "email";
	/**会员组**/
	public static final String FIELD_MEMBER_USERGROUP = "userGroup";
	/**会员等级**/
	public static final String FIELD_MEMBER_USERLEVEL = "userLevel";
	/**手机号**/
	public static final String FIELD_MEMBER_TELEPHONE = "telephone";
	/**密码**/
	public static final String FIELD_MEMBER_PASSWORD = "password";
	/**积分**/
	public static final String FIELD_MEMBER_INTEGRAL = "integral";
	/**真实姓名**/
	public static final String FIELD_MEMBER_REALNAME = "realname";
	/**会员审核状态**/
	public static final String FIELD_MEMBER_CHECKSTATUS = "checkStatus";
	/**用户名**/
	public static final String FIELD_MEMBER_USERNAME = "username";
	/**前台需要字段**/
	/**会员状态**/
	public static final String FIELD_MEMBER_STATUS = "status";
	/**用户ID**/
	public static final String FIELD_MEMBER_ID = "id";
	
	/** 模型content字段中的value对象*/
	public static final String MODEL_ITEM_CONTENT_VALUE = "value";
	/** 模型content字段中的value对象中的defaultValue的默认值*/
	public static final String MODEL_ITEM_CONTENT_DEFAULT_VALUE = "defaultValue";
}
