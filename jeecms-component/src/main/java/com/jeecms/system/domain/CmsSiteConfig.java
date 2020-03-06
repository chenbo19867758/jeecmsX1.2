/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.constants.UploadEnum;
import com.jeecms.common.constants.UploadEnum.UploadLimitType;
import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.constants.UploadEnum.WaterMarkSet;
import com.jeecms.common.util.StrUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * 站点设置
 * </p>
 * 
 * @author: tom
 * @date: 2018年11月6日 下午2:30:59
 * 
 */
public class CmsSiteConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/******************** 内容配置 **********************************/
	public static final int MAP_INIT_SIZE = 2 << 5;
	/*** 文档标题重复设置 1允许重复 2站点内不允许重复 3同一栏目下不允许重复 */
	public static final String TITLE_REPEAT = "titleRepeat";
	/**已发布内容允许编辑**/
	public static final String CONTENT_COMMIT_ALLOW_UPDATE = "contentCommitAllowUpdate";
	/** 是否开启内容自动保存 */
	public static final String CONTENT_AUTO_SAVE = "contentAutoSave";
	/** 保存内容时自动保存版本 */
	public static final String CONTENT_SAVE_VERSION = "contentSaveVersion";
	/** 是否开启新内容标记 */
	public static final String OPEN_CONTENT_NEW_FLAG = "openContentNewFlag";
	/** 新内容定义 **/
	public static final String CONTENT_NEW_FLAG = "contentNewFlag";
	/** 新内容定义-单位 1.天 2.小时 */
	public static final String CONTENT_NEW_FLAG_TYPE = "contentNewFlagType";
	/**新内容标志，资源ID**/
	public static final String CONTENT_NEW_FLAG_DEFINITION = "contentNewFlagDefinition";
	/**新内容标志，资源URL**/
	public static final String CONTENT_NEW_FLAG_DEFINITION_URL = "contentNewFlagDefinitionUrl";
	/**点赞是否需要登录**/
	public static final String CONTENT_LIKE_LOGIN = "contentLikeLogin";
	
	/******************** 水印配置 **********************************/
	/** 水印设置 1使用图片水印 2使用文字水印 3不使用水印 */
	public static final String WATERMARK_SET = "watermarkSet";
	/** 水印图片 */
	public static final String WATERMARK_PICTURE = "watermarkPicture";
	/** 水印图片URL */
	public static final String WATERMARK_PICTURE_URL = "watermarkPictureUrl";
	/** 水印位置 1左上 2上 3右上 4左 5中 6右 7左下 8下 9右下 */
	public static final String WATERMARK_POSITION = "watermarkPosition";
	/** 水印文字 */
	public static final String WATERMARK_TXT = "watermarkTxt";
	/** 文字大小 */
	public static final String WATERMARK_TXT_SIZE = "watermarkTxtSize";
	/** 文字颜色 */
	public static final String WATERMARK_TXT_COLOUR = "watermarkTxtColour";
	/** 文字透明度 */
	public static final String WATERMARK_TXT_TRANSPRENCY = "watermarkTxtTransparency";

	/******************** 栏目配置 **********************************/
	/** 栏目默认显示在循环列表 */
	public static final String CHANNEL_DISPLAY_LIST = "channelDisplayList";
	/** 栏目名称允许重复 */
	public static final String CHANNEL_NAME_REPEAT = "channelNameRepeat";
	/** 栏目及内容浏览限制类型（1.都不需要登录2.仅内容页需登录 3.都需要登录） **/
	public static final String CHANNEL_VISIT_LIMIT_TYPE = "channelVisitLimitType";
	/** 栏目默认允许投稿 **/
	public static final String CHANNEL_NORMAL_LIMIT_CONTRIBUTE = "channelNormalLimitContribute";
	/** 内容列表每页默认显示内容数 **/
	public static final String CHANNEL_DISPLAY_CONTENT_NUMBER = "channelDisplayContentNumber";

	/******************** 静态化配置 **********************************/
	/** 是否开启静态化 */
	public static final String OPEN_STATIC = "openStatic";
	/**
	 * 是否开启PC静态化
	 */
	public static final String OPEN_PC_STATIC = "openPcStatic";
	/**
	 * 是否开启手机静态化
	 */
	public static final String OPEN_MOBILE_STATIC = "openMobileStatic";
	/** 静态文件类型 */
	public static final String STATIC_HTML_SUFFIX = "staticHtmlSuffix";
	/** 静态文件存储服务器类型1本地服务器 2.FTP 3.OSS存储器 **/
	public static final String STATIC_SERVER_MEMORY_TYPE = "staticServerMemoryType";
	/** 静态文件存储服务器 **/
	public static final String STATIC_SERVER_MEMORY = "staticServerMemory";
	/** 发布内容时自动生成首页静态页 */
	public static final String STATIC_AUTO_INDEX = "staticAutoIndex";
	/** 发布内容时自动生成栏目静态页 */
	public static final String STATIC_AUTO_CHANNEL = "staticAutoChannel";
	/** 发布内容时自动生成栏目静态页-列表页数 */
	public static final String STATIC_AUTO_CHANNEL_PAGE = "staticAutoChannelPage";
	/** 首页静态化文件是否已生成 */
	public static final String STATIC_HTML_HAS_CREATE = "staticIndexHtmlHasCreate";

	/******************** 评论配置 **********************************/
	/** 评论设置 1允许游客评论 2允许登录后评论 3不允许评论 */
	public static final String COMMENT_SET = "commentSet";
	/**评论周期**/
	public static final String COMMENT_CYCLE = "commentCycle";
	/** 评论是否需要审核 */
	public static final String COMMENT_AUDIT = "commentAudit";
	/** 评论是否允许输入链接 */
	public static final String COMMENT_ALLOWED_LINK = "commentAllowedLink";
	/** 链接替换 */
	public static final String COMMENT_LINK = "commentLink";

	/******************** 短信服务配置 **********************************/
	/** 服务商1.阿里云2.腾讯云 **/
	public static final String SERVICE_PROVIDERS = "serviceProviders";
	/** AccessKeyID / AppID **/
	public static final String ACCESSKEY_ID = "accesskeyId";
	/** AccessKeySecret/AppKey **/
	public static final String ACCESSKEY_SERRET = "accesskeySecret";
	/** 短信签名 **/
	public static final String MESSAGE_SIGNATURES = "messageSignatures";

	/******************** 邮件服务配置 **********************************/
	/** SMTP 服务器 **/
	public static final String SMTP_SERVICE = "SMTPService";
	/** SMTP 端口 **/
	public static final String SMTP_PORT = "SMTPPort";
	/** 发件账号 **/
	public static final String SEND_ACCOUNT = "sendAccount";
	/** 邮箱密码 **/
	public static final String EMAIL_PASSWORD = "emailPassword";
	/** 是否使用SSL协议(1使用 0不使用) **/
	public static final String SSL_USE = "sslUse";

	/******************** 站点访问配置 **********************************/
	/** 登录后才能访问站点 **/
	private static final String LOGIN_SUCCESS_VISIT_SITE = "loginSuccessVisitSite";
	/** 是否采用相对路径 */
	public static final String URL_RELATIVE = "urlRelative";

	/******************** 会员登录跳转配置 **********************************/
	/** 是否指定会员登录成功后跳转至指定地址 */
	public static final String MEMBER_REDIRECT_ASSIGN = "memberRedirectAssign";
	/** 会员登录成功后跳转至指定地址 */
	public static final String MEMBER_REDIRECT_URL = "memberRedirectUrl";

	/******************** 网站群推送配置 **********************************/
	/** 是否接受网站群推送 */
	public static final String SITE_PUSH_OPEN = "sitePushOpen";
	/** 接受网站群推送密钥 */
	public static final String SITE_PUSH_SECRET = "sitePushSecret";
	/** 是否允许网站群采集 */
	public static final String SITE_ACQU_OPEN = "siteAcquOpen";
	/** 网站群采集密钥 */
	public static final String SITE_ACQU_SECRET = "siteAcquSecret";

	/******************** 统计配置 **********************************/
	/** 是否开启站内统计 */
	public static final String OPEN_SITE_STATISTIC = "openSiteStatistic";
	/** 是否使用第三方统计 */
	public static final String OPEN_THIRD_STATISTIC = "openThirdStatistic";
	/** 第三方统计代码 */
	public static final String THIRD_STATISTIC_CODE = "thirdStatisticCode";

	/******************** 文件上传配置 **********************************/
	/** 单张图片文件大小限制 */
	public static final String UPLOAD_PIC_MAXSIZE = "uploadPicSize";
	/** 单张图片文件大小限制单位1.KB,2.MB */
	public static final String UPLOAD_PIC_MAXSIZE_TYPE = "uploadPicSizeType";
	/** 允许上传的图片文件类型 */
	public static final String UPLOAD_PIC_SUFFIX = "uploadPicSuffix";
	/** 单视频文件大小限制 */
	public static final String UPLOAD_VIDEO_MAXSIZE = "uploadVideoSize";
	/** 单视频文件大小限制单位1.KB,2.MB */
	public static final String UPLOAD_VIDEO_MAXSIZE_TYPE = "uploadVideoSizeType";
	/** 允许上传的视频文件类型 */
	public static final String UPLOAD_VIDEO_SUFFIX = "uploadVideoSuffix";
	/** 单个音频文件文件大小限制 */
	public static final String UPLOAD_AUDIO_MAXSIZE = "uploadAudioSize";
	/** 单个音频文件文件大小限制1.KB,2.MB */
	public static final String UPLOAD_AUDIO_MAXSIZE_TYPE = "uploadAudioSizeType";
	/** 允许上传的的音频文件类型 */
	public static final String UPLOAD_AUDIO_SUFFIX = "uploadAudioSuffix";
	/** 单个文档文件大小限制 */
	public static final String UPLOAD_DOCUMENT_MAXSIZE = "uploadDocumentSize";
	/** 单个文档文件大小限制1.KB,2.MB */
	public static final String UPLOAD_DOCUMENT_MAXSIZE_TYPE = "uploadDocumentSizeType";
	/** 允许上传的的文档文件类型 */
	public static final String UPLOAD_DOCUMENT_SUFFIX = "uploadDocumentSuffix";
	/** 单附件文件大小限制 */
	public static final String UPLOAD_ATTACHMENT_MAXSIZE = "uploadAttachmentSize";
	/** 单附件文件大小限制1.KB,2.MB */
	public static final String UPLOAD_ATTACHMENT_MAXSIZE_TYPE = "uploadAttachmentSizeType";
	/** 允许上传的附件类型1.不限制2.设置允许类型3.设置禁止类型 */
	public static final String UPLOAD_ATTACHMENT_SUFFIX_TYPE = "uploadAttachmentSuffixType";
	/** 允许上传的附件类型,多个类型与逗号隔开 */
	public static final String UPLOAD_ATTACHMENT_SUFFIX = "uploadAttachmentSuffix";
	/** 上传文件存储服务器类型 local 本地服务器、ftp FTP服务器、oss 云存储 **/
	public static final String UPLOAD_FILE_MEMORY_TYPE = "uploadFileMemoryType";
	/** 上传文件存储服务器 七牛等 **/
	public static final String UPLOAD_FILE_MEMORY = "uploadFileMemory";

	/** 附件上传最大限制 ,暂未使用 */
	public static final String SYS_UPLOAD_MAXSIZE = "uploadSize";
	/** 附件上传后缀 ,暂未使用 */
	public static final String SYS_UPLOAD_SUFFIX = "uploadSuffix";
	/** 登录错误几次显示验证码 */
	public static final String SYS_LOGIN_VALID_NEED_CAPTCHA = "loginValidNeedCaptcha";
	/** 系统上传文件路径 ,暂未使用 */
	public static final String SYS_UPLOAD_FOLDER_PATH = "uploadFolderPath";

	/******************** 默认模板设置 **********************************/
	/** PC模板方案 */
	public static final String PC_SOLUTION = "pcSolution";
	/** 手机端模板方案 */
	public static final String MOBILE_SOLUTION = "mobileSolution";
	/** PC端首页模板 **/
	public static final String PC_HOMEPAGE_TEMPLATES = "pcHomePageTemplates";
	/** 手机端首页模板 **/
	public static final String MOBILE_HOMEPAGE_TEMPLATES = "mobileHomePageTemplates";
	/** 平板模板方案 */
	public static final String TABLET_SOLUTION = "tabletSolution";

	/** 投稿状态 **/
	public static final String SUBMIT_STATUS = "submitStatus";
	/** 每成功投稿篇数 **/
	public static final String SUBMIT_SUCCESS_NUMBER = "submitSuccessNumber";
	/** 获得积分 **/
	public static final String SUBMIT_SUCCESS_SCORE = "submitSuccessScore";
	/** 积分获取限制 **/
	public static final String SUBMIT_SCORE_LIMIT = "submitScoreLimit";
	/** 每日投稿最多可获取积分 **/
	public static final String SUBMIT_ONEDAY_MAX_SCORE = "submitOnedayMaxScore";

	/** 评论状态 **/
	public static final String COMMENT_STATUS = "commentStatus";
	/** 每成功评论 **/
	public static final String COMMENT_SUCCESS_NUMBER = "commentSuccessNumber";
	/** 获得积分 **/
	public static final String COMMENT_SUCCESS_SCORE = "commentSuccessScore";
	/** 积分获取限制 **/
	public static final String COMMENT_SCORE_LIMIT = "commentScoreLimit";
	/** 每日评论最多可获取积分 **/
	public static final String COMMENT_ONEDAY_MAX_SCORE = "commentOnedayMaxScore";

	/** 注册状态 **/
	public static final String REGISTER_STATUS = "registerStatus";
	/** 注册成功获得积分 **/
	public static final String REGISTER_SUCCESS_SCORE = "registerSuccessScore";

	/** 完善信息状态 **/
	public static final String PERFECT_MESSAGE_STATUS = "perfectMessageStatus";
	/** 完善个人信息获得积分 **/
	public static final String PERFECT_MESSAGE_SUCCESS_SCORE = "perfectMessageSuccessScore";

	/******************** 首页统计 **********************************/
	/** 累计pv */
	public static final String PV_TOTAL = "pvTotal";
	/** 累计uv */
	public static final String UV_TOTAL = "uvTotal";
	/** 累计ip */
	public static final String IP_TOTAL = "ipTotal";
	/** 今日pv */
	public static final String TODAY_PV = "todayPv";
	/** 今日uv */
	public static final String TODAY_UV = "todayUv";
	/** 今日ip */
	public static final String TODAY_IP = "todayIp";
	/** 昨日pv */
	public static final String YESTERDAY_PV = "yesterdayPv";
	/** 昨日uv */
	public static final String YESTERDAY_UV = "yesterdayUv";
	/** 昨日ip */
	public static final String YESTERDAY_IP = "yesterdayIp";
	/** 峰值pv */
	public static final String PEAK_PV = "peakPv";
	/** 峰值uv */
	public static final String PEAK_UV = "peakUv";
	/** 峰值ip */
	public static final String PEAK_IP = "peakIp";

	/**
	 * 投票调查工作流id
	 */
	public static final String SURVEY_CONFIGURATION_ID = "surveyConfigurationId";

	public static final String EMPTY = "";
	public static final String TRUE_STRING = "1";
	public static final String FALSE_STRING = "0";
	/**
	 * 上传单位 KB
	 */
	public static final String UPLOAD_SIZE_UNIT_KB = "1";

	/**
	 * 上传单位 MB
	 */
	public static final String UPLOAD_SIZE_UNIT_MB = "2";


	/** 属性键值对 */
	private Map<String, String> attr;

	public CmsSiteConfig() {
	}

	/**
	 * <p>
	 * 初始化扩展类
	 * </p>
	 * 
	 * @Title: init
	 * @return: void
	 */
	public void init() {
		this.setTitleRepeat(EMPTY);
		this.setContentAutoSave(TRUE_STRING);
		this.setOpenContentNewFlag(FALSE_STRING);
		this.setContentNewFlagType(EMPTY);
		this.setWatermarkSet("3");
		this.setWatermarkPosition(EMPTY);
		this.setWatermarkTxt(EMPTY);
		this.setWatermarkTxtSize("14");
		this.setWatermarkTxtColour("FF0123");
		this.setWatermarkTxtTransparency("100");
		this.setChannelDisplayList(TRUE_STRING);
		this.setChannelNameRepeat(TRUE_STRING);
		this.setChannelVisitLimitType("2");
		this.setChannelNormalLimitContribute(TRUE_STRING);
		this.setOpenStatic(FALSE_STRING);
		this.setStaticHtmlSuffix("htm");
		this.setCommentSet(EMPTY);
		this.setCommentAllowedLink(FALSE_STRING);
		this.setServiceProviders(EMPTY);
		this.setAccesskeyId(EMPTY);
		this.setAccesskeySecret(EMPTY);
		this.setMessageSignatures(EMPTY);
		this.setSMTPService(EMPTY);
		this.setSMTPPort(EMPTY);
		this.setSendAccount(EMPTY);
		this.setEmailPassword(EMPTY);
		this.setSslUse(TRUE_STRING);
		this.setMemberRedirectAssign(FALSE_STRING);
		this.setMemberRedirectUrl(EMPTY);
		this.setSitePushOpen(TRUE_STRING);
		this.setSitePushSecret(EMPTY);
		this.setSiteAcquOpen(TRUE_STRING);
		this.setSiteAcquSecret(EMPTY);
		this.setOpenSiteStatistic(TRUE_STRING);
		this.setOpenThirdStatistic(FALSE_STRING);
		this.setThirdStatisticCode(EMPTY);
		this.setUploadPicSize("0");
		this.setUploadPicSuffix(EMPTY);
		this.setUploadVideoSize("0");
		this.setUploadVideoSuffix(EMPTY);
		this.setUploadAudioSize("0");
		this.setUploadAudioSuffix(EMPTY);
		this.setUploadAttachmentSize("0");
		this.setUploadAttachmentSuffix(EMPTY);
		/** 模板设置 **/
		this.setPcSolution(EMPTY);
		this.setMobileSolution(EMPTY);
		this.setPcHomePageTemplates(EMPTY);
		this.setMobileHomePageTemplates(EMPTY);
		/** 初始化积分配置 **/
		// 投稿
		this.setSubmitStatus(TRUE_STRING);
		this.setSubmitScoreLimit(TRUE_STRING);
		this.setSubmitOnedayMaxScore(TRUE_STRING);
		this.setSubmitSuccessNumber(TRUE_STRING);
		this.setSubmitSuccessScore(TRUE_STRING);
		// 评论
		this.setCommentStatus(TRUE_STRING);
		this.setCommentOnedayMaxScore(TRUE_STRING);
		this.setCommentScoreLimit(TRUE_STRING);
		this.setCommentSuccessNumber(TRUE_STRING);
		this.setCommentSuccessScore(TRUE_STRING);
		// 注册
		this.setRegisterStatus(TRUE_STRING);
		this.setRegisterSuccessScore(TRUE_STRING);
		// 完善信息
		this.setPerfectMessageStatus(TRUE_STRING);
		this.setPerfectMessageSuccessScore(TRUE_STRING);
		this.setChannelVisitLimitType("3");
	}

	public CmsSiteConfig(Map<String, String> attr) {
		this.attr = attr;
	}

	/**
	 * 获取数据
	 * 
	 * @Title: getAttr
	 * @return
	 */
	public Map<String, String> getAttr() {
		if (null == attr) {
			attr = new HashMap<>(MAP_INIT_SIZE);
		}
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}

	/**
	 * 附件上传最大限制 ,暂未使用
	 * 
	 * @Title: getUploadSize
	 * @return
	 */
	public Integer getUploadSize() {
		String str = this.getAttr().get(SYS_UPLOAD_MAXSIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return null;
	}

	public void setUploadSize(Integer str) {
		this.getAttr().put(SYS_UPLOAD_MAXSIZE, str.toString());
	}

	public String getUploadSuffix() {
		String str = this.getAttr().get(SYS_UPLOAD_SUFFIX);
		return str;
	}

	public void setUploadSuffix(String str) {
		this.getAttr().put(SYS_UPLOAD_SUFFIX, str);
	}

	/**
	 * 内容是否允许重复设置 1允许重复 2站点内不允许重复 3同一栏目下不允许重复
	 * 
	 * @Title: getTitleRepeat
	 * @return
	 */
	public Integer getTitleRepeat() {
		String str = this.getAttr().get(TITLE_REPEAT);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}

	public void setTitleRepeat(String titleRepeat) {
		getAttr().put(TITLE_REPEAT, titleRepeat);
	}

	/**
	 * 是否开启内容自动保存
	 * 
	 * @Title: getContentAutoSave
	 * @return
	 */
	public boolean getContentAutoSave() {
		String str = this.getAttr().get(CONTENT_AUTO_SAVE);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setContentAutoSave(String contentAutoSave) {
		getAttr().put(CONTENT_AUTO_SAVE, contentAutoSave);
	}
	
	/**
	 * 是否允许内容发布编辑
	 * 
	 * @Title: getContentCommitAllowUpdate
	 * @return
	 */
	public boolean getContentCommitAllowUpdate() {
		String str = this.getAttr().get(CONTENT_COMMIT_ALLOW_UPDATE);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setContentCommitAllowUpdate(String contentCommitAllowUpdate) {
		getAttr().put(CONTENT_COMMIT_ALLOW_UPDATE, contentCommitAllowUpdate);
	}

	/**
	 * 是否开启新内容标记
	 * 
	 * @Title: getOpenContentNewFlag
	 * @return
	 */
	public boolean getOpenContentNewFlag() {
		String str = this.getAttr().get(OPEN_CONTENT_NEW_FLAG);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setOpenContentNewFlag(String openContentNewFlag) {
		getAttr().put(OPEN_CONTENT_NEW_FLAG, openContentNewFlag);
	}
	
	/**
	 * 新内容标志
	 * 
	 * @Title: getContentNewDefinition
	 * @return
	 */
	public String getContentNewDefinition() {
		return this.getAttr().get(CONTENT_NEW_FLAG_DEFINITION);
	}

	public void setContentNewDefinition(String contentNewDefinition) {
		getAttr().put(CONTENT_NEW_FLAG_DEFINITION, contentNewDefinition);
	}
	
	/**
	 * 新内容标志url
	 * 
	 * @Title: getContentNewDefinition
	 * @return
	 */
	public String getContentNewDefinitionUrl() {
		return this.getAttr().get(CONTENT_NEW_FLAG_DEFINITION_URL);
	}

	public void setContentNewDefinitionUrl(String contentNewDefinitionUrl) {
		getAttr().put(CONTENT_NEW_FLAG_DEFINITION_URL, contentNewDefinitionUrl);
	}

	/**
	 * 新内容定义-单位 1.天 2.小时
	 * 
	 * @Title: getContentNewFlagType
	 * @return
	 */
	public Integer getContentNewFlagType() {
		String str = this.getAttr().get(CONTENT_NEW_FLAG_TYPE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}

	public void setContentNewFlagType(String contentNewFlagType) {
		getAttr().put(CONTENT_NEW_FLAG_TYPE, contentNewFlagType);
	}

	/**
	 * 新内容定义
	 * 
	 * @Title: getContentNewFlag
	 * @return
	 */
	public Integer getContentNewFlag() {
		String str = this.getAttr().get(CONTENT_NEW_FLAG);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}

	public void setContentNewFlag(String contentNewFlag) {
		getAttr().put(CONTENT_NEW_FLAG, contentNewFlag);
	}

	/**
	 * url是否采用相对地址 true 是 false则为绝对地址
	 * 
	 * @Title: getUrlRelative
	 * @return: boolean
	 */
	public boolean getUrlRelative() {
		String str = this.getAttr().get(URL_RELATIVE);
		if (StringUtils.isNotBlank(str)) {
			if (FALSE_STRING.equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setUrlRelative(String urlRelative) {
		getAttr().put(URL_RELATIVE, urlRelative);
	}

	/**
	 * 获取水印设置 1使用图片水印 2使用文字水印 3不使用水印
	 * 
	 * @Title: getWatermarkSet
	 * @return: Integer
	 */
	public Integer getWatermarkSet() {
		String str = this.getAttr().get(WATERMARK_SET);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return WaterMarkSet.no.getValue();
	}

	/**
	 * 是否开启了水印
	 * 
	 * @Title: getWatermarkOpen
	 * @return: boolean
	 */
	public boolean getWatermarkOpen() {
		if (WaterMarkSet.no.getValue().equals(getWatermarkSet())) {
			return false;
		}
		return true;
	}

	public void setWatermarkSet(String watermarkSet) {
		getAttr().put(WATERMARK_SET, watermarkSet);
	}

	/**
	 * 水印位置
	 * @Title: 水印位置 1左上 2上 3右上 4左 5中 6右 7左下 8下 9右下
	 * @return
	 */
	public Integer getWatermarkPosition() {
		String str = this.getAttr().get(WATERMARK_POSITION);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}

	public void setWatermarkPosition(String watermarkPosition) {
		getAttr().put(WATERMARK_POSITION, watermarkPosition);
	}

	public String getWatermarkTxt() {
		return this.getAttr().get(WATERMARK_TXT);
	}

	public void setWatermarkTxt(String watermarkTxt) {
		getAttr().put(WATERMARK_TXT, watermarkTxt);
	}

	public String getWatermarkPicture() {
		return this.getAttr().get(WATERMARK_PICTURE);
	}

	public void setWatermarkPicture(String watermarkPicture) {
		getAttr().put(WATERMARK_PICTURE, watermarkPicture);
	}
	
	public String getWatermarkPictureUrl() {
		return this.getAttr().get(WATERMARK_PICTURE_URL);
	}

	public void setWatermarkPictureUrl(String watermarkPictureUrl) {
		getAttr().put(WATERMARK_PICTURE_URL, watermarkPictureUrl);
	}
	
	public String getUploadFileMemory() {
		return this.getAttr().get(UPLOAD_FILE_MEMORY);
	}

	public void setUploadFileMemory(String uploadFileMemory) {
		getAttr().put(UPLOAD_FILE_MEMORY, uploadFileMemory);
	}

	/**
	 * 文字大小 
	* @Title: getWatermarkTxtSize 
	* @return
	 */
	public Integer getWatermarkTxtSize() {
		String str = this.getAttr().get(WATERMARK_TXT_SIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}

	public void setWatermarkTxtSize(String watermarkTxtSize) {
		getAttr().put(WATERMARK_TXT_SIZE, watermarkTxtSize);
	}

	public String getWatermarkTxtColour() {
		return this.getAttr().get(WATERMARK_TXT_COLOUR);
	}

	/**
	 * 验证颜色
	 * 
	 * @Title: setWatermarkTxtColour
	 * @param watermarkTxtColour 颜色值
	 */
	public void setWatermarkTxtColour(String watermarkTxtColour) {
		// 验证颜色值是否符合正则
		if (!Pattern.matches(GlobalConfig.COLOR, watermarkTxtColour)) {
			watermarkTxtColour = "";
		}
		getAttr().put(WATERMARK_TXT_COLOUR, watermarkTxtColour);
	}

	/**
	 * 文字透明度
	 * 
	 * @Title: getWatermarkTxtTransparency
	 * @return
	 */
	public Integer getWatermarkTxtTransparency() {
		String str = this.getAttr().get(WATERMARK_TXT_TRANSPRENCY);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 1;
	}

	public void setWatermarkTxtTransparency(String watermarkTxtTransparency) {
		getAttr().put(WATERMARK_TXT_TRANSPRENCY, watermarkTxtTransparency);
	}

	/**
	 * 栏目默认显示在循环列表 
	* @Title: getChannelDisplayList 
	* @return
	 */
	public boolean getChannelDisplayList() {
		String str = this.getAttr().get(CHANNEL_DISPLAY_LIST);
		if (StringUtils.isNotBlank(str)) {
			if ((FALSE_STRING).equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setChannelDisplayList(String channelDisplayList) {
		getAttr().put(CHANNEL_DISPLAY_LIST, channelDisplayList);
	}

	/**
	 * 栏目名称允许重复 
	* @Title: getChannelNameRepeat 
	* @return
	 */
	public boolean getChannelNameRepeat() {
		String str = this.getAttr().get(CHANNEL_NAME_REPEAT);
		if (StringUtils.isNotBlank(str)) {
			if ((FALSE_STRING).equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setChannelNameRepeat(String channelNameRepeat) {
		getAttr().put(CHANNEL_NAME_REPEAT, channelNameRepeat);
	}

	/**
	 * 是否开启静态化
	 * 
	 * @Title: getOpenStatic
	 * @return: boolean
	 */
	public boolean getOpenStatic() {
		String str = this.getAttr().get(OPEN_STATIC);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setOpenStatic(String openStatic) {
		getAttr().put(OPEN_STATIC, openStatic);
	}

	/**
	 * 是否开启Pc静态化
	 * 
	 * @Title: getOpenPcStatic
	 * @return: boolean
	 */
	public boolean getOpenPcStatic() {
		String str = this.getAttr().get(OPEN_PC_STATIC);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setOpenPcStatic(String openPcStatic) {
		getAttr().put(OPEN_PC_STATIC, openPcStatic);
	}

	/**
	 * 是否开启手机静态化
	 * 
	 * @Title: getOpenMobileStatic
	 * @return: boolean
	 */
	public boolean getOpenMobileStatic() {
		String str = this.getAttr().get(OPEN_MOBILE_STATIC);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setOpenMobileStatic(String openMobileStatic) {
		getAttr().put(OPEN_MOBILE_STATIC, openMobileStatic);
	}

	public void setStaticHtmlSuffix(String staticHtmlSuffix) {
		getAttr().put(STATIC_HTML_SUFFIX, staticHtmlSuffix);
	}

	/**
	 * 获取静态页后缀格式
	 * 
	 * @Title: getStaticHtmlSuffix
	 * @return: String
	 */
	public String getStaticHtmlSuffix() {
		String str = this.getAttr().get(STATIC_HTML_SUFFIX);
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		return "htm";
	}

	public void setStaticServerMemoryType(String staticServerMemoryType) {
		getAttr().put(STATIC_SERVER_MEMORY_TYPE, staticServerMemoryType);
	}

	/**
	 * 静态文件存储服务器类型 字符串值 local 、ftp、oss 如果配置错误或者未配置 则默认本地存储
	 * 
	 * @Title: getStaticServerMemoryType
	 * @return: String
	 */
	public String getStaticServerMemoryType() {
		String type = this.getAttr().get(STATIC_SERVER_MEMORY_TYPE);
		if (UploadServerType.isCorrectServerTypeStr(type)) {
			return type;
		}
		return UploadServerType.local.name();
	}

	public void setStaticServerMemory(String staticServerMemory) {
		getAttr().put(STATIC_SERVER_MEMORY, staticServerMemory);
	}

	/**
	 * 静态文件存储服务器 
	* @Title: getStaticServerMemory 
	* @return
	 */
	public String getStaticServerMemory() {
		String str = this.getAttr().get(STATIC_SERVER_MEMORY);
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		return null;
	}

	public void setStaticAutoIndex(String staticAutoIndex) {
		getAttr().put(STATIC_AUTO_INDEX, staticAutoIndex);
	}

	/**
	 * 发布内容时自动生成首页静态页
	 * 
	 * @Title: getStaticAutoIndex
	 * @return: boolean
	 */
	public boolean getStaticAutoIndex() {
		String str = this.getAttr().get(STATIC_AUTO_INDEX);
		if (StringUtils.isNotBlank(str)) {
			if (FALSE_STRING.equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setStaticAutoChannel(String staticAutoIndex) {
		getAttr().put(STATIC_AUTO_CHANNEL, staticAutoIndex);
	}

	/**
	 * 发布内容时自动生成栏目静态页
	 * 
	 * @Title: getStaticAutoChannel
	 * @return: Boolean
	 */
	public boolean getStaticAutoChannel() {
		String str = this.getAttr().get(STATIC_AUTO_CHANNEL);
		if (StringUtils.isNotBlank(str)) {
			if (FALSE_STRING.equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setStaticAutoChannelPage(String commentSet) {
		getAttr().put(STATIC_AUTO_CHANNEL_PAGE, commentSet);
	}

	/**
	 * 发布内容时自动生成栏目静态页-页数 null 则生成栏目下所有页面
	 * 
	 * @Title: getStaticAutoChannelPage
	 * @return: Integer
	 */
	public Integer getStaticAutoChannelPage() {
		String str = this.getAttr().get(STATIC_AUTO_CHANNEL_PAGE);
		if (StringUtils.isNotBlank(str) && StrUtils.isNumeric(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}

	public void setHasStaticIndexPage(String hasStaticIndexPage) {
		getAttr().put(STATIC_HTML_HAS_CREATE, hasStaticIndexPage);
	}

	/**
	 * 是否已生成首页静态页
	 *
	 * @Title: getHasStaticIndexPage
	 * @return: Integer
	 */
	public Boolean getHasStaticIndexPage() {
		String str = this.getAttr().get(STATIC_HTML_HAS_CREATE);
		if (StringUtils.isNotBlank(str) ) {
			return TRUE_STRING.equals(str);
		}
		return false;
	}


	public void setCommentSet(String commentSet) {
		getAttr().put(COMMENT_SET, commentSet);
	}

	/**
	 * 评论设置 1允许游客评论 2允许登录后评论 3不允许评论 
	* @Title: getCommentSet 
	* @return
	 */
	public Integer getCommentSet() {
		String str = this.getAttr().get(COMMENT_SET);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 3;
	}
	
	public void setCommentCycle(String commentCycle) {
		getAttr().put(COMMENT_CYCLE, commentCycle);
	}

	/**
	 * 评论周期 0 不限制 默认 5 
	* @Title: getCommentSet 
	* @return
	 */
	public Integer getCommentCycle() {
		String str = this.getAttr().get(COMMENT_CYCLE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 5;
	}
	
	public void setCommentAudit(String commentAudit) {
		getAttr().put(COMMENT_AUDIT, commentAudit);
	}

	public String getCommentAudit() {
		String str = this.getAttr().get(COMMENT_AUDIT);
		return str;
	}

	/**
	 * 评论是否允许输入链接 
	* @Title: getCommentAllowedLink 
	* @return
	 */
	public boolean getCommentAllowedLink() {
		String str = this.getAttr().get(COMMENT_ALLOWED_LINK);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setCommentAllowedLink(String commentAllowedLink) {
		getAttr().put(COMMENT_ALLOWED_LINK, commentAllowedLink);
	}

	public String getCommentLink() {
		String str = this.getAttr().get(COMMENT_LINK);
		return str;
	}

	public void setCommentLink(String commentLink) {
		getAttr().put(COMMENT_LINK, commentLink);
	}

	public String getServiceProviders() {
		String str = this.getAttr().get(SERVICE_PROVIDERS);
		return str;
	}

	public void setServiceProviders(String str) {
		this.getAttr().put(SERVICE_PROVIDERS, str);
	}

	public String getAccesskeyId() {
		String str = this.getAttr().get(ACCESSKEY_ID);
		return str;
	}

	public void setAccesskeyId(String str) {
		this.getAttr().put(ACCESSKEY_ID, str);
	}

	public String getAccesskeySecret() {
		String str = this.getAttr().get(ACCESSKEY_SERRET);
		return str;
	}

	public void setAccesskeySecret(String str) {
		this.getAttr().put(ACCESSKEY_SERRET, str);
	}

	public String getMessageSignatures() {
		String str = this.getAttr().get(MESSAGE_SIGNATURES);
		return str;
	}

	public void setMessageSignatures(String str) {
		this.getAttr().put(MESSAGE_SIGNATURES, str);
	}

	public String getSMTPService() {
		String str = this.getAttr().get(SMTP_SERVICE);
		return str;
	}

	public void setSMTPService(String str) {
		this.getAttr().put(SMTP_SERVICE, str);
	}

	public String getSMTPPort() {
		String str = this.getAttr().get(SMTP_PORT);
		return str;
	}

	public void setSMTPPort(String str) {
		this.getAttr().put(SMTP_PORT, str);
	}

	public String getSendAccount() {
		String str = this.getAttr().get(SEND_ACCOUNT);
		return str;
	}

	public void setSendAccount(String str) {
		this.getAttr().put(SEND_ACCOUNT, str);
	}

	public String getEmailPassword() {
		String str = this.getAttr().get(EMAIL_PASSWORD);
		return str;
	}

	public void setEmailPassword(String str) {
		this.getAttr().put(EMAIL_PASSWORD, str);
	}

	public String getSslUse() {
		String str = this.getAttr().get(SSL_USE);
		return str;
	}

	public void setSslUse(String str) {
		this.getAttr().put(SSL_USE, str);
	}

	/**
	 * 是否登录后才能访问站点
	 * 
	 * @Title: getLoginSuccessVisitSite
	 * @return: boolean true是 默认否
	 */
	public boolean getLoginSuccessVisitSite() {
		String str = this.getAttr().get(LOGIN_SUCCESS_VISIT_SITE);
		if (StringUtils.isNoneBlank(str)) {
			if (TRUE_STRING.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void setLoginSuccessVisitSite(String str) {
		this.getAttr().put(LOGIN_SUCCESS_VISIT_SITE, str);
	}

	public String getMemberRedirectAssign() {
		String str = this.getAttr().get(MEMBER_REDIRECT_ASSIGN);
		return str;
	}

	public void setMemberRedirectAssign(String str) {
		this.getAttr().put(MEMBER_REDIRECT_ASSIGN, str);
	}

	public String getMemberRedirectUrl() {
		String str = this.getAttr().get(MEMBER_REDIRECT_URL);
		return str;
	}

	public void setMemberRedirectUrl(String str) {
		this.getAttr().put(MEMBER_REDIRECT_URL, str);
	}

	/** 是否接受网站群推送 **/
	public boolean getSitePushOpen() {
		String str = this.getAttr().get(SITE_PUSH_OPEN);
		if (StringUtils.isNotBlank(str)) {
			if ((FALSE_STRING).equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setSitePushOpen(String str) {
		this.getAttr().put(SITE_PUSH_OPEN, str);
	}

	public String getSitePushSecret() {
		String str = this.getAttr().get(SITE_PUSH_SECRET);
		return str;
	}

	public void setSitePushSecret(String str) {
		this.getAttr().put(SITE_PUSH_SECRET, str);
	}

	public String getSiteAcquOpen() {
		String str = this.getAttr().get(SITE_ACQU_OPEN);
		return str;
	}

	public void setSiteAcquOpen(String str) {
		this.getAttr().put(SITE_ACQU_OPEN, str);
	}

	public String getSiteAcquSecret() {
		String str = this.getAttr().get(SITE_ACQU_SECRET);
		return str;
	}

	public void setSiteAcquSecret(String str) {
		this.getAttr().put(SITE_ACQU_SECRET, str);
	}

	/**
	 * 是否开启站内统计 
	* @Title: getOpenSiteStatistic 
	* @return
	 */
	public boolean getOpenSiteStatistic() {
		String str = this.getAttr().get(OPEN_SITE_STATISTIC);
		if (StringUtils.isNotBlank(str)) {
			if ((FALSE_STRING).equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setOpenSiteStatistic(String openSiteStatistic) {
		getAttr().put(OPEN_SITE_STATISTIC, openSiteStatistic);
	}

	/**
	 * 是否使用第三方统计
	 * 
	 * @Title: getOpenThirdStatistic
	 * @return
	 */
	public boolean getOpenThirdStatistic() {
		String str = this.getAttr().get(OPEN_THIRD_STATISTIC);
		if (StringUtils.isNotBlank(str)) {
			if ((FALSE_STRING).equals(str)) {
				return false;
			}
		}
		return true;
	}

	public void setOpenThirdStatistic(String openThirdStatistic) {
		getAttr().put(OPEN_THIRD_STATISTIC, openThirdStatistic);
	}

	public String getThirdStatisticCode() {
		return this.getAttr().get(THIRD_STATISTIC_CODE);
	}

	public void setThirdStatisticCode(String thirdStatisticCode) {
		getAttr().put(THIRD_STATISTIC_CODE, thirdStatisticCode);
	}

	/**
	 * 允许上传的图片文件类型
	 * 
	 * @Title: getUploadPicSuffix
	 * @return
	 */
	public List<String> getUploadPicSuffix() {
		String pic = this.getAttr().get(UPLOAD_PIC_SUFFIX);
		if (StringUtils.isNoneBlank(pic)) {
			return Arrays.asList(pic.split(","));
		}
		return new ArrayList<String>();
	}
	

	public void setUploadPicSuffix(String uploadPicSuffix) {
		getAttr().put(UPLOAD_PIC_SUFFIX, uploadPicSuffix);
	}

	public void setUploadPicSize(String uploadPicSize) {
		getAttr().put(UPLOAD_PIC_MAXSIZE, uploadPicSize);
	}

	/**
	 * 单张图片文件大小限制 0则不限制
	 * 
	 * @Title: getUploadPicSize
	 * @return
	 */
	public Integer getUploadPicSize() {
		String str = this.getAttr().get(UPLOAD_PIC_MAXSIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 0;
	}

	public void setUploadPicSizeType(String uploadPicSizeType) {
		getAttr().put(UPLOAD_PIC_MAXSIZE_TYPE, uploadPicSizeType);
	}

	public String getUploadPicSizeType() {
		String str = this.getAttr().get(UPLOAD_PIC_MAXSIZE_TYPE);
		return str;
	}

	/**
	 * 允许上传的视频文件类型 集合
	 * 
	 * @Title: getUploadVideoSuffix
	 * @return
	 */
	public List<String> getUploadVideoSuffix() {
		String video = this.getAttr().get(UPLOAD_VIDEO_SUFFIX);
		if (StringUtils.isNoneBlank(video)) {
			return Arrays.asList(video.split(","));
		}
		return new ArrayList<String>();
	}

	public void setUploadVideoSuffix(String uploadVideoSuffix) {
		getAttr().put(UPLOAD_VIDEO_SUFFIX, uploadVideoSuffix);
	}

	public void setUploadVideoSize(String uploadVideoSize) {
		getAttr().put(UPLOAD_VIDEO_MAXSIZE, uploadVideoSize);
	}

	/**
	 * 单视频文件大小限制 0则不限制
	 * 
	 * @Title: setUploadVideoSize
	 * @return
	 */
	public Integer getUploadVideoSize() {
		String str = this.getAttr().get(UPLOAD_VIDEO_MAXSIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 0;
	}

	/**
	 * 允许上传的的音频文件类型
	 * 
	 * @Title: getUploadAudioSuffix
	 * @return
	 */
	public List<String> getUploadAudioSuffix() {
		String audio = this.getAttr().get(UPLOAD_AUDIO_SUFFIX);
		if (StringUtils.isNoneBlank(audio)) {
			return Arrays.asList(audio.split(","));
		}
		return new ArrayList<String>();
	}

	public void setUploadAudioSuffix(String uploadAudioSuffix) {
		getAttr().put(UPLOAD_AUDIO_SUFFIX, uploadAudioSuffix);
	}

	public void setUploadAudioSize(String uploadAudioSize) {
		getAttr().put(UPLOAD_AUDIO_MAXSIZE, uploadAudioSize);
	}

	/**
	 * 单音频文件大小限制 0则不限制
	 * 
	 * @Title: getUploadAudioSize
	 * @return
	 */
	public Integer getUploadAudioSize() {
		String str = this.getAttr().get(UPLOAD_AUDIO_MAXSIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 0;
	}

	/**
	 * 允许上传的附件类型,多个类型与逗号隔开 
	* @Title: getUploadAttachmentSuffix 
	* @return
	 */
	public List<String> getUploadAttachmentSuffix() {
		String atta = this.getAttr().get(UPLOAD_ATTACHMENT_SUFFIX);
		if (StringUtils.isNoneBlank(atta)) {
			return Arrays.asList(atta.split(","));
		}
		return new ArrayList<String>();
	}

	public void setUploadAttachmentSuffix(String uploadAttachmentSuffix) {
		getAttr().put(UPLOAD_ATTACHMENT_SUFFIX, uploadAttachmentSuffix);
	}

	public String getUploadAttachmentSuffixType() {
		return this.getAttr().get(UPLOAD_ATTACHMENT_SUFFIX_TYPE);
	}
	
	public UploadLimitType getUploadAttachmentLimitType(){
		return UploadLimitType.getValueOf(getUploadAttachmentSuffixType());
	}
	
	
	public void setUploadAttachmentSuffixType(String uploadAttachmentSuffixType) {
		getAttr().put(UPLOAD_ATTACHMENT_SUFFIX_TYPE, uploadAttachmentSuffixType);
	}

	/**
	 * 获取上传文件存储服务器字符串值 local 、ftp、oss 如果配置错误或者未配置 则默认本地存储
	 * 
	 * @Title: getUploadFileMemoryType
	 * @return: String
	 */
	public String getUploadFileMemoryType() {
		String type = this.getAttr().get(UPLOAD_FILE_MEMORY_TYPE);
		if (UploadServerType.isCorrectServerTypeStr(type)) {
			return type;
		}
		return UploadServerType.local.name();
	}

	/**
	 * 获取文件上传类型
	 * 
	 * @Title: getUploadServerType
	 * @return: UploadEnum.UploadServerType
	 */
	public UploadEnum.UploadServerType getUploadServerType() {
		return UploadEnum.UploadServerType.valueOf(getUploadFileMemoryType());
	}

	/**
	 * 获取静态页面上传类型
	 * 
	 * @Title: getStaticServerType
	 * @return: UploadEnum.UploadServerType
	 */
	public UploadEnum.UploadServerType getStaticServerType() {
		return UploadEnum.UploadServerType.valueOf(getStaticServerMemoryType());
	}

	public void setUploadFileMemoryType(String uploadFileMemoryType) {
		getAttr().put(UPLOAD_FILE_MEMORY_TYPE, uploadFileMemoryType);
	}

	public void setUploadAttachmentSize(String uploadAttachmentSize) {
		getAttr().put(UPLOAD_ATTACHMENT_MAXSIZE, uploadAttachmentSize);
	}

	/**
	 * 单附件文件大小限制 0则不限制
	 * 
	 * @Title: getUploadAttachmentSize
	 * @return
	 */
	public Integer getUploadAttachmentSize() {
		String str = this.getAttr().get(UPLOAD_ATTACHMENT_MAXSIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 0;
	}

	/**
	 * 登录错误几次显示验证码 
	* @Title: getLoginValidNeedCaptcha 
	* @return
	 */
	public Integer getLoginValidNeedCaptcha() {
		String loginValidNeedCaptcha = getAttr().get(SYS_LOGIN_VALID_NEED_CAPTCHA);
		if (StringUtils.isNotBlank(loginValidNeedCaptcha)) {
			return Integer.parseInt(loginValidNeedCaptcha);
		} else {
			return null;
		}
	}

	public void setLoginValidNeedCaptcha(Integer loginValidNeedCaptcha) {
		getAttr().put(SYS_LOGIN_VALID_NEED_CAPTCHA, loginValidNeedCaptcha.toString());
	}

	public Integer getAllowFileMaxSize() {
		return getUploadSize();
	}

	public String getUploadFolderPath() {
		String str = getAttr().get(SYS_UPLOAD_FOLDER_PATH);
		return str;
	}

	public void setUploadFolderPath(String uploadFolderPath) {
		getAttr().put(SYS_UPLOAD_FOLDER_PATH, uploadFolderPath);
	}

	public String getPcSolution() {
		String str = getAttr().get(PC_SOLUTION);
		return str;
	}

	public void setPcSolution(String solution) {
		getAttr().put(PC_SOLUTION, solution);
	}

	public String getMobileSolution() {
		String str = getAttr().get(MOBILE_SOLUTION);
		return str;
	}

	public String getTabletSolution() {
		String str = getAttr().get(TABLET_SOLUTION);
		return str;
	}

	public String setTabletSolution() {
		String str = getAttr().get(TABLET_SOLUTION);
		return str;
	}

	public void setMobileSolution(String solution) {
		getAttr().put(MOBILE_SOLUTION, solution);
	}

	/**
	 * 站点配置PC首页模板
	 * 
	 * @Title: getPcHomePageTemplates
	 * @return: String
	 */
	public String getPcHomePageTemplates() {
		String str = getAttr().get(PC_HOMEPAGE_TEMPLATES);
		return str;
	}

	public void setPcHomePageTemplates(String str) {
		getAttr().put(PC_HOMEPAGE_TEMPLATES, str);
	}

	/**
	 * 站点配置手机首页模板
	 * 
	 * @Title: getMobileHomePageTemplates
	 * @return: String
	 */
	public String getMobileHomePageTemplates() {
		String str = getAttr().get(MOBILE_HOMEPAGE_TEMPLATES);
		return str;
	}

	public void setMobileHomePageTemplates(String str) {
		getAttr().put(MOBILE_HOMEPAGE_TEMPLATES, str);
	}

	/**
	 * 栏目及内容浏览限制类型（1.都不需要登录2.仅内容页需登录 3.都需要登录
	 * 
	 * @Title: getChannelVisitLimitType
	 * @return: Integer 默认1 都不需要登录
	 */
	public Short getChannelVisitLimitType() {
		String str = getAttr().get(CHANNEL_VISIT_LIMIT_TYPE);
		if (StringUtils.isNoneBlank(str) && StrUtils.isNumeric(str)) {
			return Short.parseShort(str);
		}
		return SysConstants.VISIT_LOGIN_NO_LIMIT;
	}

	public void setChannelVisitLimitType(String str) {
		getAttr().put(CHANNEL_VISIT_LIMIT_TYPE, str);
	}

	public String getChannelNormalLimitContribute() {
		String str = getAttr().get(CHANNEL_NORMAL_LIMIT_CONTRIBUTE);
		return str;
	}

	public void setChannelNormalLimitContribute(String str) {
		getAttr().put(CHANNEL_NORMAL_LIMIT_CONTRIBUTE, str);
	}

	public String getChannelDisplayContentNumber() {
		String str = getAttr().get(CHANNEL_DISPLAY_CONTENT_NUMBER);
		return str;
	}

	public void setChannelDisplayContentNumber(String str) {
		getAttr().put(CHANNEL_DISPLAY_CONTENT_NUMBER, str);
	}

	/**是否开启投稿积分**/
	public String getSubmitStatus() {
		String str = this.getAttr().get(SUBMIT_STATUS);
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		return FALSE_STRING;
	}

	public void setSubmitStatus(String str) {
		getAttr().put(SUBMIT_STATUS, str);
	}

	/**每成功投稿篇数 **/
	public Integer getSubmitSuccessNumber() {
		String str = getAttr().get(SUBMIT_SUCCESS_NUMBER);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setSubmitSuccessNumber(String str) {
		getAttr().put(SUBMIT_SUCCESS_NUMBER, str);
	}

	/**获得积分 **/
	public Integer getSubmitSuccessScore() {
		String str = getAttr().get(SUBMIT_SUCCESS_SCORE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setSubmitSuccessScore(String str) {
		getAttr().put(SUBMIT_SUCCESS_SCORE, str);
	}

	/**积分获取限制**/
	public String getSubmitScoreLimit() {
		String str = getAttr().get(SUBMIT_SCORE_LIMIT);
		if (StringUtils.isNotEmpty(str)) {
			return str;
		}
		return FALSE_STRING;
	}

	public void setSubmitScoreLimit(String str) {
		getAttr().put(SUBMIT_SCORE_LIMIT, str);
	}

	/**每日投稿最多可获取积分**/
	public Integer getSubmitOnedayMaxScore() {
		String str = getAttr().get(SUBMIT_ONEDAY_MAX_SCORE);
		if (StringUtils.isNotEmpty(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setSubmitOnedayMaxScore(String str) {
		getAttr().put(SUBMIT_ONEDAY_MAX_SCORE, str);
	}

	/**是否开启评论积分**/
	public String getCommentStatus() {
		String str = this.getAttr().get(COMMENT_STATUS);
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		return FALSE_STRING;
	}

	public void setCommentStatus(String str) {
		getAttr().put(COMMENT_STATUS, str);
	}

	/**每成功评论**/
	public Integer getCommentSuccessNumber() {
		String str = getAttr().get(COMMENT_SUCCESS_NUMBER);
		if (StringUtils.isNotEmpty(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setCommentSuccessNumber(String str) {
		getAttr().put(COMMENT_SUCCESS_NUMBER, str);
	}

	/**评论获得积分**/
	public Integer getCommentSuccessScore() {
		String str = getAttr().get(COMMENT_SUCCESS_SCORE);
		if (StringUtils.isNotEmpty(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setCommentSuccessScore(String str) {
		getAttr().put(COMMENT_SUCCESS_SCORE, str);
	}

	/**评论积分获取限制 **/
	public String getCommentScoreLimit() {
		String str = getAttr().get(COMMENT_SCORE_LIMIT);
		if (StringUtils.isNotEmpty(str)) {
			return str;
		}
		return FALSE_STRING;
	}

	public void setCommentScoreLimit(String str) {
		getAttr().put(COMMENT_SCORE_LIMIT, str);
	}

	/**每日评论最多可获取积分**/
	public Integer getCommentOnedayMaxScore() {
		String str = getAttr().get(COMMENT_ONEDAY_MAX_SCORE);
		if (StringUtils.isNotEmpty(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setCommentOnedayMaxScore(String str) {
		getAttr().put(COMMENT_ONEDAY_MAX_SCORE, str);
	}
	
	/**是否开启注册积分**/
	public String getRegisterStatus() {
		String str = this.getAttr().get(REGISTER_STATUS);
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		return FALSE_STRING;
	}

	public void setRegisterStatus(String str) {
		getAttr().put(REGISTER_STATUS, str);
	}

	/**注册得到积分**/
	public Integer getRegisterSuccessScore() {
		String str = getAttr().get(REGISTER_SUCCESS_SCORE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setRegisterSuccessScore(String str) {
		getAttr().put(REGISTER_SUCCESS_SCORE, str);
	}

	/**是否开启完善信息积分**/
	public String getPerfectMessageStatus() {
		String str = this.getAttr().get(PERFECT_MESSAGE_STATUS);
		if (StringUtils.isNotBlank(str)) {
			return str;
		}
		return FALSE_STRING;
	}

	public void setPerfectMessageStatus(String str) {
		getAttr().put(PERFECT_MESSAGE_STATUS, str);
	}

	/**完善个人信息得到的积分**/
	public Integer getPerfectMessageSuccessScore() {
		String str = getAttr().get(PERFECT_MESSAGE_SUCCESS_SCORE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return 0;
	}

	public void setPerfectMessageSuccessScore(String str) {
		getAttr().put(PERFECT_MESSAGE_SUCCESS_SCORE, str);
	}

	public String getContentSaveVersion() {
		String str = getAttr().get(CONTENT_SAVE_VERSION);
		return str;
	}

	public void setContentSaveVersion(String str) {
		getAttr().put(CONTENT_SAVE_VERSION, str);
	}

	public void setUploadVideoSizeType(String uploadVideoSizeType) {
		getAttr().put(UPLOAD_VIDEO_MAXSIZE_TYPE, uploadVideoSizeType);
	}

	public String getUploadVideoSizeType() {
		String str = this.getAttr().get(UPLOAD_VIDEO_MAXSIZE_TYPE);
		return str;
	}

	public void setUploadAudioSizeType(String uploadAudioSizeType) {
		getAttr().put(UPLOAD_AUDIO_MAXSIZE_TYPE, uploadAudioSizeType);
	}

	public String getUploadAudioSizeType() {
		String str = this.getAttr().get(UPLOAD_AUDIO_MAXSIZE_TYPE);
		return str;
	}

	public void setUploadAttachmentSizeType(String uploadAttachmentSizeType) {
		getAttr().put(UPLOAD_ATTACHMENT_MAXSIZE_TYPE, uploadAttachmentSizeType);
	}

	public String getUploadAttachmentSizeType() {
		String str = this.getAttr().get(UPLOAD_ATTACHMENT_MAXSIZE_TYPE);
		return str;
	}

	/**
	 * 允许上传的的文档文件类型 
	* @Title: getUploadDocumentSuffix 
	* @return
	 */
	public List<String> getUploadDocumentSuffix() {
		String doc = this.getAttr().get(UPLOAD_DOCUMENT_SUFFIX);
		if (StringUtils.isNoneBlank(doc)) {
			return Arrays.asList(doc.split(","));
		}
		return new ArrayList<String>();
	}

	public void setUploadDocumentSuffix(String uploadDocumentSuffix) {
		getAttr().put(UPLOAD_DOCUMENT_SUFFIX, uploadDocumentSuffix);
	}

	public void setUploadDocumentSize(String uploadDocumentSize) {
		getAttr().put(UPLOAD_DOCUMENT_MAXSIZE, uploadDocumentSize);
	}

	/**
	 * 单文档文件大小限制 0则不限制
	 * 
	 * @Title: getUploadAudioSize
	 * @return
	 */
	public Integer getUploadDocumentSize() {
		String str = this.getAttr().get(UPLOAD_DOCUMENT_MAXSIZE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 0;
	}

	public String getUploadDocumentSizeType() {
		return this.getAttr().get(UPLOAD_DOCUMENT_MAXSIZE_TYPE);
	}

	public void setUploadDocumentSizeType(String uploadDocumentSizeType) {
		getAttr().put(UPLOAD_DOCUMENT_MAXSIZE_TYPE, uploadDocumentSizeType);
	}
	
	public String getContentLikeLogin() {
		return this.getAttr().get(CONTENT_LIKE_LOGIN);
	}

	public void setContentLikeLogin(String contentLikeLogin) {
		getAttr().put(CONTENT_LIKE_LOGIN, contentLikeLogin);
	}

	public String getPvTotal() {
		return this.getAttr().get(PV_TOTAL) == null ? "0" : this.getAttr().get(PV_TOTAL);
	}

	public void setPvTotal(String pvTotal) {
		getAttr().put(PV_TOTAL, pvTotal);
	}

	public String getUvTotal() {
		return this.getAttr().get(UV_TOTAL) == null ? "0" : this.getAttr().get(UV_TOTAL);
	}

	public void setUvTotal(String uvTotal) {
		getAttr().put(UV_TOTAL, uvTotal);
	}

	public String getIpTotal() {
		return this.getAttr().get(IP_TOTAL) == null ? "0" : this.getAttr().get(IP_TOTAL);
	}

	public void setIpTotal(String ipTotal) {
		getAttr().put(IP_TOTAL, ipTotal);
	}

	public String getTodayPv() {
		return this.getAttr().get(TODAY_PV) == null ? "0" : this.getAttr().get(TODAY_PV);
	}

	public void setTodayPv(String todayPv) {
		getAttr().put(TODAY_PV, todayPv);
	}

	public String getTodayUv() {
		return this.getAttr().get(TODAY_UV) == null ? "0" : this.getAttr().get(TODAY_UV);
	}

	public void setTodayUv(String todayUv) {
		getAttr().put(TODAY_UV, todayUv);
	}

	public String getTodayIp() {
		return this.getAttr().get(TODAY_IP) == null ? "0" : this.getAttr().get(TODAY_IP);
	}

	public void setTodayIp(String todayIp) {
		getAttr().put(TODAY_IP, todayIp);
	}

	public String getYesterdayPv() {
		return this.getAttr().get(YESTERDAY_PV) == null ? "0" : this.getAttr().get(YESTERDAY_PV);
	}

	public void setYesterdayPv(String yesterdayPv) {
		getAttr().put(YESTERDAY_PV, yesterdayPv);
	}

	public String getYesterdayUv() {
		return this.getAttr().get(YESTERDAY_UV) == null ? "0" : this.getAttr().get(YESTERDAY_UV);
	}

	public void setYesterdayUv(String yesterdayUv) {
		getAttr().put(YESTERDAY_UV, yesterdayUv);
	}

	public String getYesterdayIp() {
		return this.getAttr().get(YESTERDAY_IP) == null ? "0" : this.getAttr().get(YESTERDAY_IP);
	}

	public void setYesterdayIp(String yesterdayIp) {
		getAttr().put(YESTERDAY_IP, yesterdayIp);
	}

	public  String getPeakPv() {
		return this.getAttr().get(PEAK_PV) == null ? "0" : this.getAttr().get(PEAK_PV);
	}

	public void setPeakPv(String peakPv) {
		getAttr().put(PEAK_PV, peakPv);
	}

	public  String getPeakUv() {
		return this.getAttr().get(PEAK_UV) == null ? "0" : this.getAttr().get(PEAK_UV);
	}

	public void setPeakUv(String peakUv) {
		getAttr().put(PEAK_UV, peakUv);
	}

	public  String getPeakIp() {
		return this.getAttr().get(PEAK_IP) == null ? "0" : this.getAttr().get(PEAK_IP);
	}

	public void setPeakIp(String peakIp) {
		getAttr().put(PEAK_IP, peakIp);
	}

	public Integer getSurveyConfigurationId() {
		String s = this.getAttr().get(SURVEY_CONFIGURATION_ID);
		if (StringUtils.isNotBlank(s)) {
			return Integer.parseInt(s);
		}
		return null;
	}

	public void setSurveyConfigurationId(Integer surveyConfigurationId) {
		if (surveyConfigurationId == null) {
			getAttr().put(SURVEY_CONFIGURATION_ID, "");
		} else {
			getAttr().put(SURVEY_CONFIGURATION_ID, String.valueOf(surveyConfigurationId));
		}
	}
}