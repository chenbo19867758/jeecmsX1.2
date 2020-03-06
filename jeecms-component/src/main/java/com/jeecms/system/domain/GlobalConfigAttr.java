/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.domain;

import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.StrUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置属性表
 * 
 * @author: wulongwei
 * @date: 2019年4月13日 下午4:08:23
 */
public class GlobalConfigAttr implements Serializable {
        private static final long serialVersionUID = 1L;

        public static final int MAP_INIT_SIZE = 2 << 5;

        /** 标题 */
        public static final String SYS_TITLE = "title";
        /** 描述 */
        public static final String SYS_DESCRIPTION = "description";
        /** 关键词 */
        public static final String SYS_KEYWORDS = "keywords";

        /** 客服电话 */
        public static final String SYS_CUSTOMERSERVER_PHONE = "serverPhone";
        /** 客服邮箱 */
        public static final String SYS_CUSTOMERSERVER_EMAIL = "serverEmail";
        /** 联系地址 */
        public static final String SYS_CONTACT_ADDRESS = "contactAddress";
        /** 平台客服 */
        public static final String SYS_CUSTOMERSERVER_PLARFORM = "serverPlatform";
        /** 系统上传文件路径 */
        public static final String SYS_UPLOAD_FOLDER_PATH = "uploadFolderPath";

        /** 首页搜索关键词 */
        public static final String DISPLAY_PAGEHOME_KEYWORDS = "pHKeywords";
        /** 热门搜索词 */
        public static final String DISPLAY_HOTS_KEYWORDS = "hotsKeywords";
        /** 版权信息及ICP备案号 */
        public static final String DISPLAY_ICP_RECORDNUMBER = "icpRecordNumber";

        /** 腾讯地图密钥 */
        public static final String EXT_TENCENTMAP_KEY = "tencentmapKey";
        /** 统计代码 */
        public static final String EXT_STATISTICAL_CODE = "statisticalCode";

        /** PC模板方案 */
        public static final String PC_SOLUTION = "pcSolution";
        /** 手机端模板方案 */
        public static final String MOBILE_SOLUTION = "mobileSolution";
        /** 平板模板方案 */
        public static final String TABLET_SOLUTION = "tabletSolution";

        /** 系统无法匹配到任何微信设置的回复规则时回复文本 */
        public static final String WECHAT_REPLY_CONTENT = "wechatReplyContent";

        public static final String SYS_IN_BLACK = "inBlack";
        /**授权状态**/
        public static final String SYS_STATE = "state";
        public static final String SITE_DOMAIN = "siteDomain";
        /**用户单位**/
        public static final String SITE_NAME = "siteName";
        /** 产品授权码 **/
        public static final String SYS_AU_CODE = "authCode";
        public static final String SYS_LIMIT = "limit";
        /** 授权有效时间 **/
        public static final String SYS_AU_EXPIRED_DATE = "authExpiredDate";
        /** 售后服务到期时间 **/
        public static final String SYS_AFTERSALE_EXPIRED_DATE = "aftersaleExpiredDate";
        /** 最近启动时间 **/
        public static final String SYS_START_DATE = "systemStartDate";
        /** 版权所有 **/
        public static final String SYS_COPYRIGHT = "systemCopyright";
        /** 官网地址 **/
        public static final String SYS_URL = "systemUrl";
        /** 产品名称 **/
        public static final String PRODUCT_NAME = "productName";
        
        // jeecms系统设置信息
        /** 是否开启会员功能 */
        public static final String IS_MEMBER_FUNCTION = "isMemberFunction";
        /** 是否开启会员注册 */
        public static final String IS_MEMBER_REGOSTER = "isMemberRegister";
        /** 会员注册是否需要图片验证码 */
        public static final String IS_ACTIVATION_IMAGE = "isActivationImage";
        /** 会员注册是否需要审核 */
        public static final String MEMBER_REGISTER_EXAMINE = "memberRegisterExamine";
        /** 部署路径 */
        public static final String DEPLOYMENT_PATH = "deploymentPath";
        /** 服务端口 **/
        public static final String SERVER_PORT = "serverPort";
        /** 动态页后缀 **/
        public static final String URL_SUFFIX_JHTML = "urlSuffixJhtml";
        /** openoffice安装目录 */
        public static final String OPEN_OFFICE_CATALOG = "openOfficeCatalog";
        /*** openoffice端口 */
        public static final String OPEN_OFFICE_PORT = "openOfficePort";
        /** 数据备份文件存储服务器1本地服务器 2.FTP 3.OSS存储器（默认为1） **/
        public static final String DATA_BACKUPS_MEMORY_TYPE = "dataBackupsMemoryType";
        /** 数据备份文件存储服务器 **/
        public static final String DATA_BACKUPS_MEMORY = "dataBackupsMemory";
        /** 模板文件存储服务器1本地服务器 2.FTP	（默认为1） **/
        public static final String TEMPLATE_FILE_TYPE = "templateFileType";
        /** 模板文件存储服务器 **/
        public static final String TEMPLATE_FILE = "templateFile";
        /** 是否开启百度推送 */
        public static final String IS_BAIDU_PUSH = "isBaiduPush";
        /** 百度推送Token */
        public static final String BAIDU_PUSH_TOKEN = "baiduPushToken";
        /** 组织对所有用户可见 */
        public static final String IS_VISIBLE = "IsVisible";
        /** 是否开启内网设置 */
        public static final String IS_INTRANET_SET = "isIntranetSet";
        /** 是否生成内容页二维码 */
        public static final String IS_CONTENT_CODE = "isContentCode";
        /** 是否允许输入敏感词 */
        public static final String IS_SENSITIVE_WORDS = "isSensitiveWords";
        /** 敏感词默认替换 */
        public static final String SENSITIVE_WORDS_REPLACE = "sensitiveWordsReplace";

        // jeecms账户安全设置信息
        /** 是否开启安全设置 */
        public static final String SECURITY_OPEN = "securityOpen";
        /** 密码最小位数 */
        public static final String PASS_MIN = "passMin";
        /** 密码最大位数 */
        public static final String PASS_MAX = "passMax";
        /**
         * 密码组成规则 1无要求 2必须包含字母和数字 3必须包含大写字母、小写字母、数字 4必须包含字母、数字、特殊字符 5必须包含大写字母、小写字母、数字、特殊字符
         */
        public static final String PASS_COMPOSITION_RULE = "passCompositionRule";
        /** 是否允许密码与用户名相同 */
        public static final String PASS_IS_EQUALLY = "passIsEqually";
        /** 是否强制要求用户定期修改密码 */
        public static final String PASS_REGULAR_CHANGE = "passRegularChange";
        /** 是否强制要求用户定期修改密码系统设置调整时间 */
        public static final String PASS_REGULAR_CHANGE_SET_TIME = "passRegularChangeSetTime";
        /** 强制修改密码周期 单位天 */
        public static final String PASS_REGULAR_CYCLE = "passRegularCycle";
        /** 首次登录是否强制要求修改密码 */
        public static final String PASS_FIRST_NEED_UPDATE = "passFirstNeedUpdate";
        /** 重置密码后首次登录是否强制要求修改密码 */
        public static final String PASS_RESET_NEED_UPDATE = "passResetNeedUpdate";
        /** 登录时密码错误校验周期 0则不限制 */
        public static final String CHECK_CYCLE = "checkCycle";
        /** 登录时密码校验周期单位 1天 2时 3分 */
        public static final String CHECK_CYCLE_UNIT = "checkCycleUnit";
        /** 登录时密码最大错误次数 */
        public static final String LOGIN_ERROR_COUNT = "loginErrorCount";
        /** 处理方式 0请选择 1显示验证码 2锁定账号(禁用) 3一定时间内禁止登录 */
        public static final String PROCESSING_MODE = "processingMode";
        /** 禁止登录时长类型 1.天 2.小时 3.分钟 */
        public static final String LOGIN_LIMIT_TYPE = "loginLimitType";
        /** 禁止登录时长 */
        public static final String LOGIN_LIMIT = "loginLimit";
        /** 登录后长时间未操作自动退出账号 */
        public static final String AUTO_LOGOUT = "autoLogout";
        /** 自动退出周期 单位分钟 */
        public static final String AUTO_LOGOUT_MINUTE = "autoLogoutMinute";
        /** 是否允许同一账号异地登录 */
        public static final String LOGIN_MUTI = "loginMuti";
        /** 长期未登录锁定账号 */
        public static final String ACCOUNT_LOCK_AUTO = "accountLockAuto";
        /** 长期未登录锁定账号天数 默认0 */
        public static final String ACCOUNT_LOCK_AUTO_DAY = "accountLockAutoDay";
        /** 是否开启内容发文字号 **/
        public static final String OPEN_CONTENT_ISSUE = "openContentIssue";
        /** 是否开启内容密级 */
        public static final String OPEN_CONTENT_SECURITY = "openContentSecurity";
        /** 是否开启附件密级 */
        public static final String OPEN_ATTACHMENT_SECURITY = "openAttachmentSecurity";

        /** logo图关联ID */
        public static final String FRONT_IMG_ID = "frontImgId";
        /** 用户登录及注册logo图关联ID */
        public static final String OPER_IDENTITY_IMG_ID = "operIdentityImgId";
        /** 管理端登录页logo图关联ID */
        public static final String LOGIN_SYS_IMG_ID = "loginSysImgId";
        /** 管理端登录页logo图 */
        public static final String LOGIN_SYS_IMG_URL = "loginSysImgUrl";
        /** 管理端logo图关联ID */
        public static final String SYS_IMG_ID = "sysImgId";
        /** 管理端logo图 */
        private static final String SYS_IMG_URL = "sysImgUrl";
        /** 微信公众号图片ID */
        public static final String WECHAT_PUBLIC_NUMBER_IMG_ID = "wechatPublicNumberImgId";
        /** 微信小程序图片ID */
        public static final String WECHAT_SMALL_IMG_ID = "wechatSmallImgId";
        
        /**系统标志资源ID**/
        public static final String SYSTEM_FLAG_RESOURCE_ID = "systemFlagResourceId";
        /**系统标志资源URL**/
        public static final String SYSTEM_FLAG_RESOURCE_URL = "systemFlagResourceUrl";
        
        /**百度语音appid**/
        public static final String BAIDU_VOICE_APPKEY = "baiduVoiceAppKey";
        /**百度语音secret**/
        public static final String BAIDU_VOICE_SECRET = "baiduVoiceSecret";
        
        /**是否开启单点登录**/
        public static final String SSO_LOGIN_OPEN = "ssoLoginOpen";
        /**单点登录APPID**/
        public static final String SSO_LOGIN_APPID = "ssoLoginAppId";
        /**单点登录APPSecret**/
        public static final String SSO_LOGIN_APPSECRET = "ssoLoginSecret";
        
        /** 属性键值对 */
        private Map<String, String> attr;

        public static final String EMPTY = "";
        public static final String TRUE_STRING = "1";
        public static final String FALSE_STRING = "0";

        /** 初始化信息 */
        public void init() {
                /** 初始化系统设置信息 */
                this.setOpenContentSecurity(FALSE_STRING);
                this.setOpenAttachmentSecurity(FALSE_STRING);
                this.setIsMemberFunction(TRUE_STRING);
                this.setIsMemberRegister(TRUE_STRING);
                this.setIsActivationImage(TRUE_STRING);
                this.setServerPort(EMPTY);
                this.setOpenOfficeCatalog(EMPTY);
                this.setOpenOfficePort(EMPTY);
                this.setIsBaiduPush(TRUE_STRING);
                this.setBaiduPushToken(EMPTY);
                this.setIsVisible(TRUE_STRING);
                this.setIsIntranetSet(FALSE_STRING);
                this.setIsContentCode(FALSE_STRING);
                this.setIsSensitiveWords(IS_SENSITIVE_WORDS);
                /** 初始化账户安全信息 */
                this.setSecurityOpen(FALSE_STRING);
                this.setPassMin(EMPTY);
                this.setPassMax(EMPTY);
                this.setPassCompositionRule(TRUE_STRING);
                this.setPassIsEqually(FALSE_STRING);
                this.setPassRegularChange(FALSE_STRING);
                this.setPassRegularCycle(EMPTY);
                this.setPassFirstNeedUpdate(FALSE_STRING);
                this.setPassResetNeedUpdate(FALSE_STRING);
                this.setCheckCycle(EMPTY);
                this.setLoginErrorCount(EMPTY);
                this.setProcessingMode(FALSE_STRING);
                this.setAutoLogout(FALSE_STRING);
                this.setAutoLogoutMinute(0);
                this.setLoginMuti(TRUE_STRING);
                this.setAccountLockAuto(FALSE_STRING);
                this.setAccountLockAutoDay(FALSE_STRING);
                this.setDataBackupsMemoryType(TRUE_STRING);
        }

        public GlobalConfigAttr() {
        }

        public GlobalConfigAttr(Map<String, String> attr) {
                this.attr = attr;
        }

        public Map<String, String> getAttr() {
                if (null == attr) {
                        attr = new HashMap<>(MAP_INIT_SIZE);
                }
                return attr;
        }

        public void setAttr(Map<String, String> attr) {
                this.attr = attr;
        }

        public String getTitle() {
                String str = this.getAttr().get(SYS_TITLE);
                return str;
        }

        public void setTitle(String str) {
                this.getAttr().put(SYS_TITLE, str);
        }

        public String getDescription() {
                String str = this.getAttr().get(SYS_DESCRIPTION);
                return str;
        }

        public void setDescription(String str) {
                this.getAttr().put(SYS_DESCRIPTION, str);
        }

        public String getKeywords() {
                String str = this.getAttr().get(SYS_KEYWORDS);
                return str;
        }

        public void setKeywords(String str) {
                this.getAttr().put(SYS_KEYWORDS, str);
        }

        public String getServerPhone() {
                String str = this.getAttr().get(SYS_CUSTOMERSERVER_PHONE);
                return str;
        }

        public void setServerPhone(String str) {
                this.getAttr().put(SYS_CUSTOMERSERVER_PHONE, str);
        }

        public String getServerEmail() {
                String str = this.getAttr().get(SYS_CUSTOMERSERVER_EMAIL);
                return str;
        }

        public void setServerEmail(String str) {
                this.getAttr().put(SYS_CUSTOMERSERVER_EMAIL, str);
        }

        public String getContactAddress() {
                String str = this.getAttr().get(SYS_CONTACT_ADDRESS);
                return str;
        }

        public void setContactAddress(String str) {
                this.getAttr().put(SYS_CONTACT_ADDRESS, str);
        }

        public String getServerPlatform() {
                String str = this.getAttr().get(SYS_CUSTOMERSERVER_PLARFORM);
                return str;
        }

        public void setServerPlatform(String str) {
                this.getAttr().put(SYS_CUSTOMERSERVER_PLARFORM, str);
        }

        public String getPHKeywords() {
                String str = this.getAttr().get(DISPLAY_PAGEHOME_KEYWORDS);
                return str;
        }

        public void setPHKeywords(String str) {
                this.getAttr().put(DISPLAY_PAGEHOME_KEYWORDS, str);
        }

        public String getHotsKeywords() {
                String str = this.getAttr().get(DISPLAY_HOTS_KEYWORDS);
                return str;
        }

        public void setHotsKeywords(String str) {
                this.getAttr().put(DISPLAY_HOTS_KEYWORDS, str);
        }

        public String getIcpRecordNumber() {
                String str = this.getAttr().get(DISPLAY_ICP_RECORDNUMBER);
                return str;
        }

        public void setIcpRecordNumber(String str) {
                this.getAttr().put(DISPLAY_PAGEHOME_KEYWORDS, str);
        }

        public String getTencentmapKey() {
                String str = this.getAttr().get(EXT_TENCENTMAP_KEY);
                return str;
        }

        public void setTencentmapKey(String str) {
                this.getAttr().put(EXT_TENCENTMAP_KEY, str);
        }

        public String getStatisticalCode() {
                String str = this.getAttr().get(EXT_STATISTICAL_CODE);
                return str;
        }

        public void setStatisticalCode(String str) {
                this.getAttr().put(EXT_STATISTICAL_CODE, str);
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

        public String getSiteDomain() {
                String str = getAttr().get(SITE_DOMAIN);
                return str;
        }

        public void setSiteDomain(String domain) {
                getAttr().put(SITE_DOMAIN, domain);
        }

        public String getSiteName() {
                String str = getAttr().get(SITE_NAME);
                return str;
        }

        public void setSiteName(String siteName) {
                getAttr().put(SITE_NAME, siteName);
        }

        public Integer getSysState() {
                String str = getAttr().get(SYS_STATE);
                if (StringUtils.isBlank(str)) {
                        return 2;
                }
                return Integer.parseInt(str);
        }

        public void setSysState(Integer state) {
                getAttr().put(SYS_STATE, state.toString());
        }

        public Boolean getInBlack() {
                String str = this.getAttr().get(SYS_IN_BLACK);
                if (StringUtils.isNotBlank(str)) {
                        return Boolean.valueOf(str);
                }
                return false;
        }

        public void setInBlack(Boolean str) {
                this.getAttr().put(SYS_IN_BLACK, str.toString());
        }

        public String getAuthCode() {
                String str = getAttr().get(SYS_AU_CODE);
                return str;
        }

        public void setAuthCode(String code) {
                getAttr().put(SYS_AU_CODE, code);
        }
        
        public String getSysCopyright() {
            String str = getAttr().get(SYS_COPYRIGHT);
            return str;
        }

        public void setSysCopyright(String code) {
            getAttr().put(SYS_COPYRIGHT, code);
    	}

        public Date getAuthExpiredDate() {
                String str = getAttr().get(SYS_AU_EXPIRED_DATE);
                if (StringUtils.isNoneBlank(str)) {
                        return MyDateUtils.parseDate(str);
                }
                return null;
        }

        public void setAuthExpiredDate(Date date) {
                getAttr().put(SYS_AU_EXPIRED_DATE, MyDateUtils.formatDate(date));
        }
    
	/** 售后服务到时时间 **/
	public Date getAftersaleExpiredDate() {
		String str = getAttr().get(SYS_AFTERSALE_EXPIRED_DATE);
		if (StringUtils.isNoneBlank(str)) {
			return MyDateUtils.parseDate(str);
		}
		return null;
	}

	public void setAftersaleExpiredDate(Date date) {
		getAttr().put(SYS_AFTERSALE_EXPIRED_DATE, MyDateUtils.formatDate(date));
	}

        public Date getSystemStartDate() {
                String str = getAttr().get(SYS_START_DATE);
                if (StringUtils.isNoneBlank(str)) {
                        return MyDateUtils.parseDate(str, MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
                }
                return null;
        }

        public void setSystemStartDate(Date date) {
                getAttr().put(SYS_START_DATE, MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
        }

        public Boolean getIsLimit() {
                String str = getAttr().get(SYS_LIMIT);
                if (StringUtils.isNoneBlank(str)) {
                        return Boolean.valueOf(str);
                } else {
                        return false;
                }
        }

        public String getMobileSolution() {
                String str = getAttr().get(MOBILE_SOLUTION);
                return str;
        }

        public String getTabletSolution() {
                String str = getAttr().get(TABLET_SOLUTION);
                return str;
        }

        public void setMobileSolution(String solution) {
                getAttr().put(MOBILE_SOLUTION, solution);
        }

        public String getWechatReplyContent() {
                String str = getAttr().get(WECHAT_REPLY_CONTENT);
                return str;
        }

        public void setWechatReplyContent(String wechatReplyContent) {
                getAttr().put(WECHAT_REPLY_CONTENT, wechatReplyContent);
        }
        
        public String getProductName() {
            String str = getAttr().get(PRODUCT_NAME);
            return str;
        }

        public void setProductName(String productName) {
            getAttr().put(PRODUCT_NAME, productName);
        }

        /** jeecms系统设置信息 勿删 */
        public Integer getServerPort() {
                String str = getAttr().get(SERVER_PORT);
                if (StringUtils.isBlank(str) && !StringUtils.isNumeric(str)) {
                        return 80;
                }
                return Integer.parseInt(str);
        }

        public void setServerPort(String port) {
                getAttr().put(SERVER_PORT, port.toString());
        }

        public Boolean getUrlSuffixJhtml() {
                String urlSuffix = getAttr().get(URL_SUFFIX_JHTML);
                if(TRUE_STRING.equals(urlSuffix)){
                        return true;
                }
                return false;
        }

        public void setUrlSuffixJhtml(Boolean use) {
                getAttr().put(URL_SUFFIX_JHTML, use.toString());
        }

        public String getDeploymentPath() {
                String str = getAttr().get(DEPLOYMENT_PATH);
                if (StringUtils.isBlank(str)) {
                        str = "";
                }
                return str;
        }

        public void setDeploymentPath(String deploymentPath) {
                getAttr().put(DEPLOYMENT_PATH, deploymentPath);
        }

        public Boolean getIsIntranetSet() {
                String str = getAttr().get(IS_INTRANET_SET);
                if (StringUtils.isBlank(str)) {
                        return false;
                }
                return TRUE_STRING.equals(str);
        }

        public void setIsIntranetSet(String isIntranetSet) {
                getAttr().put(IS_INTRANET_SET, isIntranetSet);
        }

        public String getOpenOfficeCatalog() {
                String str = getAttr().get(OPEN_OFFICE_CATALOG);
                return str;
        }

        public void setOpenOfficeCatalog(String openOfficeCatalog) {
                getAttr().put(OPEN_OFFICE_CATALOG, openOfficeCatalog);
        }

        public String getOpenOfficePort() {
                String str = getAttr().get(OPEN_OFFICE_PORT);
                return str;
        }

        public void setOpenOfficePort(String openOfficePort) {
                getAttr().put(OPEN_OFFICE_PORT, openOfficePort);
        }

	public Integer getDataBackupsMemoryType() {
		String str = getAttr().get(DATA_BACKUPS_MEMORY_TYPE);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return GlobalConfig.BACKUP_TYPE_LOCAL;
	}

	public void setDataBackupsMemoryType(String dataBackupsMemoryType) {
		getAttr().put(DATA_BACKUPS_MEMORY_TYPE, dataBackupsMemoryType);
	}
        
	public Integer getDataBackupsMemory() {
		String str = getAttr().get(DATA_BACKUPS_MEMORY);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return null;
	}

	public void setDataBackupsMemory(String dataBackupsMemory) {
		getAttr().put(DATA_BACKUPS_MEMORY, dataBackupsMemory);
	}
        
	public Integer getTemplateFileType() {
		String str = getAttr().get(TEMPLATE_FILE_TYPE);
		return Integer.valueOf(str);
	}

	public void setTemplateFileType(String templateFileType) {
		getAttr().put(TEMPLATE_FILE_TYPE, templateFileType);
	}

	public String getTemplateFile() {
		String str = getAttr().get(TEMPLATE_FILE);
		return str;
	}

	public void setTemplateFile(String templateFile) {
		getAttr().put(TEMPLATE_FILE_TYPE, templateFile);
	}

        public Boolean getIsBaiduPush() {
                String str = getAttr().get(IS_BAIDU_PUSH);
                if (StringUtils.isBlank(str) && !StringUtils.isNumeric(str)) {
                        return false;
                }
                return TRUE_STRING.equals(str);
        }

        public void setIsBaiduPush(String isBaiduPush) {
                getAttr().put(IS_BAIDU_PUSH, isBaiduPush);
        }

        public String getBaiduPushToken() {
                String str = getAttr().get(BAIDU_PUSH_TOKEN);
                return str;
        }

        public void setBaiduPushToken(String baiduPushToken) {
                getAttr().put(BAIDU_PUSH_TOKEN, baiduPushToken);
        }
        
        /**
         * 会员功能是否开启
         * @Title: getMemberOpen
         * @return: boolean
         */
        public boolean getMemberOpen() {
                if(FALSE_STRING.endsWith(getIsMemberFunction())){
                        return false;
                }
                return true;
        }
        
        public String getIsMemberFunction() {
                return getAttr().get(IS_MEMBER_FUNCTION);
        }

        public void setIsMemberFunction(String isMemberFunction) {
                getAttr().put(IS_MEMBER_FUNCTION, isMemberFunction);

        }

	public boolean getIsMemberRegister() {
		String str = getAttr().get(IS_MEMBER_REGOSTER);
		return TRUE_STRING.equals(str);
	}

        public void setIsMemberRegister(String isMemberRegister) {
                getAttr().put(IS_MEMBER_REGOSTER, isMemberRegister);
        }

        public String getIsContentCode() {
                String str = getAttr().get(IS_CONTENT_CODE);
                return str;
        }

        public void setIsContentCode(String isContentCode) {
                getAttr().put(IS_CONTENT_CODE, isContentCode);
        }

        public Boolean getSecurityOpen() {
                String str = getAttr().get(SECURITY_OPEN);
                return TRUE_STRING.equals(str);
        }

        public void setSecurityOpen(String securityOpen) {
                getAttr().put(SECURITY_OPEN, securityOpen);
        }

        public String getPassMin() {
                String str = getAttr().get(PASS_MIN);
                return str;
        }

        public void setPassMin(String passMin) {
                getAttr().put(PASS_MIN, passMin);
        }

        public String getPassMax() {
                String str = getAttr().get(PASS_MAX);
                return str;
        }

        public void setPassMax(String passMax) {
                getAttr().put(PASS_MAX, passMax);
        }

        public String getPassCompositionRule() {
                String str = getAttr().get(PASS_COMPOSITION_RULE);
                return str;
        }

        public void setPassCompositionRule(String passCompositionRule) {
                getAttr().put(PASS_COMPOSITION_RULE, passCompositionRule);
        }

        public Boolean getPassIsEqually() {
                String str = getAttr().get(PASS_IS_EQUALLY);
                return TRUE_STRING.equals(str);
        }

        public void setPassIsEqually(String passIsEqually) {
                getAttr().put(PASS_IS_EQUALLY, passIsEqually);
        }

        public String getPassRegularChange() {
                String str = getAttr().get(PASS_REGULAR_CHANGE);
                return str;
        }

        public void setPassRegularChange(String passRegularChange) {
                getAttr().put(PASS_REGULAR_CHANGE, passRegularChange);
        }

        /**
         * 获取最后强制密码修改周期 设置时间
         * 
         * @Title: getPassRegularChangeSetTime
         * @return: Date
         */
        public Date getPassRegularChangeSetTime() {
                String str = getAttr().get(PASS_REGULAR_CHANGE_SET_TIME);
                if (StringUtils.isNoneBlank(str)) {
                        return MyDateUtils.parseDate(str, MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
                }
                return Calendar.getInstance().getTime();
        }

        public void setPassRegularChangeSetTime(String passRegularChangeSetTime) {
                getAttr().put(PASS_REGULAR_CHANGE_SET_TIME, passRegularChangeSetTime);
        }

        public String getPassFirstNeedUpdate() {
                String str = getAttr().get(PASS_FIRST_NEED_UPDATE);
                return str;
        }

        public void setPassFirstNeedUpdate(String passFirstNeedUpdate) {
                getAttr().put(PASS_FIRST_NEED_UPDATE, passFirstNeedUpdate);
        }

        public String getPassResetNeedUpdate() {
                String str = getAttr().get(PASS_RESET_NEED_UPDATE);
                return str;
        }

        public void setPassResetNeedUpdate(String passResetNeedUpdate) {
                getAttr().put(PASS_RESET_NEED_UPDATE, passResetNeedUpdate);
        }

        /**
         * 登录时密码错误校验周期 0则不限制
         * @Title: getCheckCycle
         * @return: Integer
         */
        public Integer getCheckCycle() {
                String str = getAttr().get(CHECK_CYCLE);
                if(StringUtils.isNoneBlank(str)&&StrUtils.isNumeric(str)){
                        return Integer.parseInt(str);
                }
                return 0;
        }

        public void setCheckCycle(String checkCycle) {
                getAttr().put(CHECK_CYCLE, checkCycle);
        }

        /**
         * 获取时间单位
         * 
         * @Title: getCheckCycleUnit
         * @return: Integer
         */
        public Integer getCheckCycleUnit() {
                String str = getAttr().get(CHECK_CYCLE_UNIT);
                if (StringUtils.isNoneBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return 1;
        }

        public void setCheckCycleUnit(Integer checkCycleUnit) {
                getAttr().put(CHECK_CYCLE_UNIT, checkCycleUnit.toString());
        }

        /**
         * 登录时密码最大错误次数 0则不限制
         * 
         * @Title: getLoginErrorCount
         * @return: Integer
         */
        public Integer getLoginErrorCount() {
                String str = getAttr().get(LOGIN_ERROR_COUNT);
                if (StringUtils.isNoneBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return 0;
        }

        public void setLoginErrorCount(String loginErrorCount) {
                getAttr().put(LOGIN_ERROR_COUNT, loginErrorCount);
        }

        /**
         * 获取登录错误次数处理模式
         * 
         * @Title: getProcessingMode
         * @return: Integer
         */
        public Integer getProcessingMode() {
                String str = getAttr().get(PROCESSING_MODE);
                if (StringUtils.isNotBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return 0;
        }

        public void setProcessingMode(String processingMode) {
                getAttr().put(PROCESSING_MODE, processingMode);
        }

        /**
         * 禁止登录时长
         * 
         * @Title: getLoginLimit
         * @return: Integer
         */
        public Integer getLoginLimit() {
                String str = getAttr().get(LOGIN_LIMIT);
                if (StringUtils.isNotBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return 0;
        }

        public void setLoginLimit(Integer loginLimit) {
                getAttr().put(LOGIN_LIMIT, loginLimit.toString());
        }

        public Integer getLoginLimitType() {
                String str = getAttr().get(LOGIN_LIMIT_TYPE);
                if (StringUtils.isNotBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return null;
        }

        public void setLoginLimitType(String loginLimitType) {
                getAttr().put(LOGIN_LIMIT_TYPE, loginLimitType);
        }

        public String getAutoLogout() {
                String str = getAttr().get(AUTO_LOGOUT);
                return str;
        }

        public void setAutoLogout(String autoLogout) {
                getAttr().put(AUTO_LOGOUT, autoLogout);
        }

        /**
         * 获取自动退出时间
         * 
         * @Title: getAutoLogoutMinute
         * @return: Integer
         */
        public Integer getAutoLogoutMinute() {
                String str = getAttr().get(AUTO_LOGOUT_MINUTE);
                if (StringUtils.isNoneBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return 0;
        }

        public void setAutoLogoutMinute(Integer autoLogoutMinute) {
                getAttr().put(AUTO_LOGOUT_MINUTE, autoLogoutMinute.toString());
        }

        /**
         * 是否允许多处同时登陆
         * 
         * @Title: getLoginMuti
         * @return: Boolean true 允许
         */
        public Boolean getLoginMuti() {
                String str = getAttr().get(LOGIN_MUTI);
                if (StringUtils.isNoneBlank(str)) {
                        if (FALSE_STRING.equals(str.toLowerCase())) {
                                return false;
                        }
                }
                return true;
        }

        public void setLoginMuti(String loginMuti) {
                getAttr().put(LOGIN_MUTI, loginMuti);
        }

        /**
         * 是否开启 长期未登录锁定账号
         * 
         * @Title: getAccountLockAuto
         * @return: boolean true开启了
         */
        public boolean getAccountLockAuto() {
                String str = getAttr().get(ACCOUNT_LOCK_AUTO);
                if (StringUtils.isNoneBlank(str)) {
                        if (TRUE_STRING.equals(str.toLowerCase())) {
                                return true;
                        }
                }
                return false;
        }

        public void setAccountLockAuto(String accountLockAuto) {
                getAttr().put(ACCOUNT_LOCK_AUTO, accountLockAuto);
        }

        /**
         * 长期未登录锁定账号的天数
         * 
         * @Title: getAccountLockAutoDay
         * @return: Integer
         */
        public Integer getAccountLockAutoDay() {
                String str = getAttr().get(ACCOUNT_LOCK_AUTO_DAY);
                if (StringUtils.isNoneBlank(str) && StrUtils.isNumeric(str)) {
                        return Integer.parseInt(str);
                }
                return 0;
        }

        public void setAccountLockAutoDay(String accountLockAutoDay) {
                getAttr().put(ACCOUNT_LOCK_AUTO_DAY, accountLockAutoDay);
        }

        public String getFrontImgId() {
                String str = getAttr().get(FRONT_IMG_ID);
                return str;
        }

        public void setFrontImgId(String frontImgId) {
                getAttr().put(FRONT_IMG_ID, frontImgId);
        }

        public String getOperIdentityImgId() {
                String str = getAttr().get(OPER_IDENTITY_IMG_ID);
                return str;
        }

        public void setOperIdentityImgId(String operIdentityImgId) {
                getAttr().put(OPER_IDENTITY_IMG_ID, operIdentityImgId);
        }

        public Integer getLoginSysImgId() {
                String str = getAttr().get(LOGIN_SYS_IMG_ID);
                if (StringUtils.isNotBlank(str)) {
                        return Integer.parseInt(str);
                }
                return null;
        }

        public void setLoginSysImgId(String loginSysImgId) {
                getAttr().put(LOGIN_SYS_IMG_ID, loginSysImgId);
        }

        public static String getLoginSysImgUrl() {
                return LOGIN_SYS_IMG_URL;
        }

        public void setLoginSysImgUrl(String loginSysImgUrl) {
                getAttr().put(LOGIN_SYS_IMG_URL, loginSysImgUrl);
        }

        public Integer getSysImgId() {
                String str = getAttr().get(SYS_IMG_ID);
                if (StringUtils.isNotBlank(str)) {
                        return Integer.parseInt(str);
                }
                return null;
        }

        public void setSysImgId(String sysImgId) {
                getAttr().put(SYS_IMG_ID, sysImgId);
        }

        @Transient
        public String getSysImgUrl() {
                return getAttr().get(SYS_IMG_URL);
        }

        public void setSysImgUrl(String sysImgUrl) {
                getAttr().put(SYS_IMG_URL, sysImgUrl);
        }

        public String getWechatPublicNumberImgId() {
                String str = getAttr().get(WECHAT_PUBLIC_NUMBER_IMG_ID);
                return str;
        }

        public void setWechatPublicNumberImgId(String wechatPublicNumberImgId) {
                getAttr().put(WECHAT_PUBLIC_NUMBER_IMG_ID, wechatPublicNumberImgId);
        }

        public String getWechatSmallImgId() {
                String str = getAttr().get(WECHAT_SMALL_IMG_ID);
                return str;
        }

        public void setWechatSmallImgId(String wechatSmallImgId) {
                getAttr().put(WECHAT_SMALL_IMG_ID, wechatSmallImgId);
        }

        /**
         * 强制修改密码周期 单位天
         * 
         * @Title: getPassRegularCycle
         * @return: Integer
         */
        public Integer getPassRegularCycle() {
                String str = getAttr().get(PASS_REGULAR_CYCLE);
                if (StringUtils.isNoneBlank(str) && StrUtils.isNumeric(str)) {
                        Integer.parseInt(str);
                }
                return 7;
        }

        public void setPassRegularCycle(String passRegularCycle) {
                getAttr().put(PASS_REGULAR_CYCLE, passRegularCycle);
        }

        public Boolean getIsVisible() {
                String str = getAttr().get(IS_VISIBLE);
                return TRUE_STRING.equals(str);
        }

        public void setIsVisible(String isVisible) {
                getAttr().put(IS_VISIBLE, isVisible);
        }

        public String getIsActivationImage() {
                String str = getAttr().get(IS_ACTIVATION_IMAGE);
                return str;
        }

        public void setIsActivationImage(String isActivationImage) {
                getAttr().put(IS_ACTIVATION_IMAGE, isActivationImage);
        }

        public Boolean getMemberRegisterExamine() {
                String str = getAttr().get(MEMBER_REGISTER_EXAMINE);
                if (StringUtils.isNotBlank(str)) {
                        return TRUE_STRING.equals(str);
                }
                return null;
        }

        public void setMemberRegisterExamine(String memberRegisterExamine) {
                getAttr().put(MEMBER_REGISTER_EXAMINE, memberRegisterExamine);
        }

        public String getIsSensitiveWords() {
                String str = getAttr().get(IS_SENSITIVE_WORDS);
                return str;
        }

        public void setIsSensitiveWords(String isSensitiveWords) {
                getAttr().put(IS_SENSITIVE_WORDS, isSensitiveWords);
        }

        public String getSensitiveWordsReplace() {
                String str = getAttr().get(SENSITIVE_WORDS_REPLACE);
                return str;
        }

        public void setSensitiveWordsReplace(String sensitiveWordsReplace) {
                getAttr().put(SENSITIVE_WORDS_REPLACE, sensitiveWordsReplace);
        }

        /**
         * 是否开启内容密级
         * 
         * @Title: getOpenContentSecurity
         * @return: boolean
         */
        public boolean getOpenContentSecurity() {
                String str = this.getAttr().get(OPEN_CONTENT_SECURITY);
                if (StringUtils.isNotBlank(str)) {
                        if ((TRUE_STRING).equals(str)) {
                                return true;
                        }
                }
                return false;
        }

        public void setOpenContentSecurity(String openContentSecurity) {
                getAttr().put(OPEN_CONTENT_SECURITY, openContentSecurity);
        }

        public boolean getOpenContentIssue() {
                String str = this.getAttr().get(OPEN_CONTENT_ISSUE);
                if (StringUtils.isNotBlank(str)) {
                        if ((TRUE_STRING).equals(str)) {
                                return true;
                        }
                }
                return false;
        }

        public void setOpenContentIssue(String openContentIssue) {
                getAttr().put(OPEN_CONTENT_ISSUE, openContentIssue);
        }

        public boolean getOpenAttachmentSecurity() {
                String str = this.getAttr().get(OPEN_ATTACHMENT_SECURITY);
                if (StringUtils.isNotBlank(str)) {
                        if ((TRUE_STRING).equals(str)) {
                                return true;
                        }
                }
                return false;
        }

        public void setOpenAttachmentSecurity(String openAttachmentSecurity) {
                getAttr().put(OPEN_ATTACHMENT_SECURITY, openAttachmentSecurity);
        }
        
	public void setSystemUrl(String systemUrl) {
		getAttr().put(SYS_URL, systemUrl);
	}

	public String getSystemUrl() {
		String str = getAttr().get(SYS_URL);
		return str;
	}
	
	/**系统标志资源ID**/
	public void setSystemFlagResourceId(String systemFlagResourceId) {
		getAttr().put(SYSTEM_FLAG_RESOURCE_ID, systemFlagResourceId);
	}

	public String getSystemFlagResourceId() {
		String str = getAttr().get(SYSTEM_FLAG_RESOURCE_ID);
		return str;
	}
	
	/**系统标志资源URL**/
	public void setSystemFlagResourceUrl(String systemFlagResourceUrl) {
		getAttr().put(SYSTEM_FLAG_RESOURCE_URL, systemFlagResourceUrl);
	}

	public String getSystemFlagResourceUrl() {
		String str = getAttr().get(SYSTEM_FLAG_RESOURCE_URL);
		return str;
	}
	
	/**百度语音appid**/
	public void setBaiduVoiceAppkey(String baiduVoiceAppkey) {
		getAttr().put(BAIDU_VOICE_APPKEY, baiduVoiceAppkey);
	}

	public String getBaiduVoiceAppkey() {
		String str = getAttr().get(BAIDU_VOICE_APPKEY);
		return str;
	}
	
	/**百度语音secret**/
	public void setBaiduVoiceSecret(String systemFlagResourceUrl) {
		getAttr().put(BAIDU_VOICE_SECRET, systemFlagResourceUrl);
	}

	public String getBaiduVoiceSecret() {
		String str = getAttr().get(BAIDU_VOICE_SECRET);
		return str;
	}
	
	/**是否开启SSO**/
	public void setSsoLoginAppOpen(String ssoLoginAppOpen) {
		getAttr().put(SSO_LOGIN_OPEN, ssoLoginAppOpen);
	}

	/**get函数**/
	public boolean getSsoLoginAppOpen() {
		String str = getAttr().get(SSO_LOGIN_OPEN);
		if (StringUtils.isNotBlank(str)) {
			if ((TRUE_STRING).equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	/**单点登录APPID**/
	public void setSsoLoginAppId(String ssoLoginAppId) {
		getAttr().put(SSO_LOGIN_APPID, ssoLoginAppId);
	}

	public String getSsoLoginAppId() {
		String str = getAttr().get(SSO_LOGIN_APPID);
		return str;
	}
	
	/**单点登录APPSecret**/
	public void setSsoLoginAppSecret(String ssoLoginAppSecret) {
		getAttr().put(SSO_LOGIN_APPSECRET, ssoLoginAppSecret);
	}

	public String getSsoLoginAppSecret() {
		String str = getAttr().get(SSO_LOGIN_APPSECRET);
		return str;
	}

}