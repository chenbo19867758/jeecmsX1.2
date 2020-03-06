package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 其他错误枚举  号段范围 10001~10500
 *
 * @author: tom
 * @date: 2018年11月6日 下午6:51:36
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum SysOtherErrorCodeEnum implements ExceptionInfo {
        /**
         * 上传文件错误、默认提示信息。
         */
        UPLOAD_FILE_ERROR("10100", "file error !"),
        /**
         * 上传IO错误、默认提示信息。
         */
        UPLOAD_IO_ERROR("10102", "io error !"),
        /**
         * 上传非图片、默认提示信息。
         */
        UPLOAD_NOT_IMAGE_ERROR("10103", "not image !"),
        /**
         * 上传文件格式错误、默认提示信息。
         */
        UPLOAD_FORMAT_ERROR("10104", "format error !"),
        /**
         * 上传超出限制、默认提示信息。
         */
        UPLOAD_LIMIT_ERROR("10105", "upload limit !"),
        /**
         * 上传失败、默认提示信息。
         */
        UPLOAD_ERROR("10106", "file error !"),
        /**
         * 上传OSS失败、默认提示信息。
         */
        UPLOAD_OSS_ERROR("10107", "oss info error !"),
        /**
         * 上传文件文件夹错误、默认提示信息。
         */
        UPLOAD_SPACE_ERROR("10108", "space error !"),
        /**
         * lecene失败、默认提示信息。
         */
        LUCENE_CREATE_ERROR("10109", "lecene create error !"),
        /**
         * 索引获取失败
         */
        LUCENE_GET_ERROR("10110", "lecene get error !"),
        /**
         * 定时任务错误
         */
        JOB_ERROR("10111", "job  error !"),
        /**
         * 文件名或者路径是否非法的
         */
        PATH_OR_FILENAME_VALID("10112", "filename or path is valid!"),
        /**
         * 没有该组织管理权限
         */
        NO_ORG_PERMISSION_ERROR("10113", "No permission to manager the org!"),
        /**
         * 没有该角色管理权限
         */
        NO_ROLE_PERMISSION_ERROR("10114", "No permission to manager the role!"),
        /**
         * 没有该用户管理权限
         */
        NO_USER_PERMISSION_ERROR("10115", "No permission to manager the user!"),
        /**
         * 模板创建错误
         */
        TPL_RESOURCE_CREATE_ERROR("10119", "Resource creation error!"),
        /**
         * 热词已存在
         */
        HOT_WORD_ALREADY_EXIST("10120", "hot word already exist"),
        /**
         * 热词分类已存在
         */
        HOT_WORD_CATEGORY_ALREADY_EXIST("10121", "hot word category already exist"),
        /**
         * 敏感词已存在
         */
        SENSITIVE_WORD_ALREADY_EXIST("10122", "sensitive word already exist"),
        /**
         * 导入excel错误，默认提示信息
         */
        IMPORT_EXCEL_ERROR("10123", "import excel error"),
        /**
         * 搜索词已存在
         */
        SEARCH_WORD_ALREADY_EXIST("10124", "search word already exist"),
        /**
         * TAG词已存在
         */
        TAG_WORD_ALREADY_EXIST("10125", "tag word already exist"),
        /**
         * 水印图片不存在
         */
        UPLOAD_MARK_FILE_ERROR("10126", "markfile not exist !"),
        /**
         * 导出模板错误
         */
        EXPORT_TEMPLATE_ERROR("10127", "export template error"),
        /**
         * 导入模板错误
         */
        IMPORT_TEMPLATE_ERROR("10128", "import template error"),
        /**
         * 年号已存在
         */
        YEAR_NUM_ALREADY_EXIST("10129", "year num already exist"),
        /**
         * 机关代字已存在
         */
        AGENCY_WORD_ALREADY_EXIST("10130", "agency word already exist"),
        /**
         * 下载IO异常
         */
        DOWNLOAD_IO_ERROR("10131", "download io error"),
        /**
         * IO异常
         */
        IO_ERROR("10132", "io error"),
        /**
         * 通知短信最多上限100个
         */
        UP_TO_100_SMS("10133", "Up to 100 SMS"),
        /**
         * 通知邮箱最多上限100个
         */
        UP_TO_100_EMAIL("10134", "Up to 100 email"),
        /**
         * 手机格式错误
         */
        PHONE_FORMAT_ERROR("10135", "Phone format error"),
        /**
         * 邮箱格式错误
         */
        EMAIL_FORMAT_ERROR("10136", "email format error"),
        /**
         * 文件未找到
         */
        FILE_NOT_FIND("10137", "file not find"),
        /**
         * 语言标识已存在
         */
        LANG_ALREADY_EXIST("10138", "lang already exist"),
        /**
         * 语言标识不能为空
         */
        LANG_KEY_NOT_NUll("10139", "lang key not null"),
        /**
         * 语言名称不能为空
         */
        LANG_NAME_NOT_NULL("10140", "lang name not null"),
        /**
         * 系统默认无法删除
         */
        SYSTEM_CANNOT_BE_DELETED_BY_DEFAULT("10141", "The system cannot be deleted by default"),
        /**
         * 云存储地区码错误
         */
        OSS_BUCKET_AREA_ERROR("10142", "OSS bucket area error"),
        /**
         * 友情链接类型名称已存在
         */
        LINK_TYPE_NAME_ALREADY_EXIST("10143", "link type name already exist"),
        /**
         * 上传设置水印错误、默认提示信息。
         */
        UPLOAD_MARK_ERROR("10144", "upload mark error !"),
        /**
         * 工作流规则-线条孤立错误、默认提示信息。
         */
        WORKFLOW_RULE_LINE_ISOLATED_ERROR("10145", "line isolated error !"),
        /**
         * 工作流规则-节点孤立错误、默认提示信息。
         */
        WORKFLOW_RULE_NODE_ISOLATED_ERROR("10146", "node isolated error !"),
        /**
         * 工作流规则-开始节点缺失错误、默认提示信息。
         */
        WORKFLOW_RULE_START_NODE_MUST_ERROR("10147", "start node is must !"),
        /**
         * 工作流规则-结束节点缺失错误、默认提示信息。
         */
        WORKFLOW_RULE_END_NODE_MUST_ERROR("10148", "end node is must !"),
        /**
         * 工作流规则-开始节点重复错误、默认提示信息。
         */
        WORKFLOW_RULE_START_NODE_REPEAT_ERROR("10149", "start node is repeat !"),
        /**
         * 开始节点只能连接审核节点
         */
        WORKFLOW_RULE_START_NODE_ONLY_TOAUDIT_NODE_ERROR("10150", "Starting node can only connect audit nodes"),
        /**
         * 结束节点不能有连线出口
         */
        WORKFLOW_RULE_END_NODE_LINE_OUT_ERROR("10151", "End Node Can't Have Connection Exit"),
        /**
         * 结束节点必须有入口线
         */
        WORKFLOW_RULE_END_NODE_INLINE_MUST_ERROR("10152", "End node must have entry line"),
        /**
         * 同一审核节点出来的连线中不能存在相同的动作类型的连线
         */
        WORKFLOW_RULE_NODE_SAME_TYPE_LINE_ERROR("10153", "There is no connection of the same action type in the connection of the same audit node"),
        /**
         * 工作流规则-开始节点没有出口线错误、默认提示信息。
         */
        WORKFLOW_RULE_START_NODE_NO_OUTLINE_ERROR("10154", "Start Node No Outlet Line Error !"),
        /**
         * 工作流规则-线条必须指定动作错误、默认提示信息。
         */
        WORKFLOW_RULE_LINE_ACTION_MUST_ERROR("10155", "Lines Must Formulate Action !"),
        /**
         * 工作流规则错误、默认提示信息。
         */
        WORKFLOW_RULE_ERROR("10157", "workflow rule error !"),
        /**
         * 工作流规则-自定义的动作类型不能连接开始节点错误、默认提示信息。
         */
        WORKFLOW_RULE_CUSTOM_ACTION_MUST_NOT_START_ERROR("10158", "Custom action types cannot connect start node!"),
        /**
         * 工作流规则-若流程图中某个审核节点未设置通过连线，需要弹出提示，但依然能保存
         */
        WORKFLOW_RULE_AUDIT_NODE_PASS_OUT_LINE_TIP("10159", "audit node has not set a pass  connection!"),
        /**
         * 工作流部署错误、默认提示信息。
         */
        WORKFLOW_DEPLOY_ERROR("10160", "workflow deploy error !"),
        /**
         * 工作流规则-结束节点重复错误、默认提示信息。
         */
        WORKFLOW_RULE_END_NODE_REPEAT_ERROR("10161", "end node  is repeat !"),
        /**
         * 开始节点只能连接一个审核节点
         */
        WORKFLOW_RULE_START_NODE_ONLY_ONE_LINE_AUDIT_ERROR("10162", "Starting node can only connect one audit  node"),
        /**
         * 工作流规则-自定义的动作类型不能连接结束节点错误、默认提示信息。
         */
        WORKFLOW_RULE_CUSTOM_ACTION_MUST_NOT_END_ERROR("10163", "Custom action types cannot connect  end node errors !"),
        /**
         * 工作流规则-若流程图中某个审核节点未设置不通过连线，需要弹出提示，但依然能保存
         */
        WORKFLOW_RULE_AUDIT_NODE_REJECT_OUT_LINE_TIP("10164", "audit node has not set a reject connection!"),
        /**
         * 文件已存在
         */
        FILE_ALREADY_EXIST("10156", "file already exist"),
        /**
         * 工作流名称重复、默认提示信息。
         */
        WORKFLOW_NAME_EXIST_ERROR("10165", "workflow name exist !"),
        /**
         * 人员密级已存在
         */
        USER_SECRET_EXIST_ERROR("10166", "user secret name exist !"),
        /**
         * 内容密级已存在
         */
        CONTENT_SECRET_EXIST_ERROR("10167", "content secret name exist !"),
        /**
         * 附件密级已存在
         */
        FILE_SECRET_EXIST_ERROR("10168", "file secret name exist !"),
        /**
         * FTP未配置
         */
        FTP_NOT_CONFIGURATION("10169", "ftp not configuration!"),
        /**
         * FTP连接失败
         */
        FTP_CONNECTION_FAILED("10170", "ftp connection failed!"),
        /**
         * 不能更改自己的组织、角色
         */
        USER_CHANGE_ORG_ROLE_ERROR("10171", "Can't change your organization, role!"),
        /**
         * ftp密码不能超过40
         */
        FTP_PASSWORD_ERROR_LENGTH("10172", "ftp_password_no_than_40"),
        /**
         * 问卷答案必填
         */
        QUESTIONNAIRE_ANSWER_REQUIRED("10173", "questionnaire answer required"),
		/**
		 * 问卷问答题长度超过限制
		 */
		QUESTIONNAIRE_ANSWER_OTHER_LENGTH("10174", "questionnaire answer other length"),
        /**
         * 问卷问答题答案输入不规范
         */
        QUESTIONNAIRE_ANSWER_REGEX_ERROR("10175", "questionnaire answer regex error"),
        /**
         * 问卷已取消
         */
        QUESTIONNAIRE_CANCELLED("10176", "questionnaire cancelled"),
        /**
         * 登录后才能投票
         */
        QUESTIONNAIRE_IS_LOGIN("10177", "questionnaire is login"),
        /**
         * 超过答题次数
         */
        QUESTIONNAIRE_EXCEED_COUNT("10178", "questionnaire exceed count"),
        /**
         * 每个IP答题次数限制必填
         */
        QUESTIONNAIRE_IP_COUNT_REQUIRED("10179", "questionnaire ip count required"),
        /**
         * 每台电脑或手机答题次数限制必填
         */
        QUESTIONNAIRE_DEVICE_COUNT_REQUIRED("10180", "questionnaire device count required"),
        /**
         * 只能在微信中访问
         */
        QUESTIONNAIRE_WECHAT_ONLY("10181", "questionnaire wechat only"),
        /**
         * 内容审核名称已存在
         */
        AUDIT_STRATEGY_NAME_ALREADY_EXIST("10182", "audit strategy name already exist"),
        ;

        /**
         * 异常代码。
         */
        private String code;

        /**
         * 异常对应的默认提示信息。
         */
        private String defaultMessage;

        /**
         * 异常对应的原始提示信息。
         */
        private String originalMessage;

        /**
         * 当前请求的URL。
         */
        private String requestUrl;

        /**
         * 需转向（重定向）的URL，默认为空。
         */
        private String defaultRedirectUrl = "";

        /**
         * 异常对应的响应数据。
         */
        private Object data = new Object();

        /**
         * Description: 根据异常的代码、默认提示信息构建一个异常信息对象。
         *
         * @param code           异常的代码。
         * @param defaultMessage 异常的默认提示信息。
         */
        SysOtherErrorCodeEnum(String code, String defaultMessage) {
                this.code = code;
                this.defaultMessage = defaultMessage;
        }

        @Override
        public String getCode() {
                return code;
        }

        @Override
        public String getDefaultMessage() {
                return defaultMessage;
        }

        @Override
        public String getOriginalMessage() {
                return originalMessage;
        }

        @Override
        public void setOriginalMessage(String originalMessage) {
                this.originalMessage = originalMessage;
        }

        @Override
        public String getRequestUrl() {
                return requestUrl;
        }

        @Override
        public void setRequestUrl(String requestUrl) {
                this.requestUrl = requestUrl;
        }

        @Override
        public String getDefaultRedirectUrl() {
                return defaultRedirectUrl;
        }

        @Override
        public void setDefaultRedirectUrl(String defaultRedirectUrl) {
                this.defaultRedirectUrl = defaultRedirectUrl;
        }

        @Override
        public Object getData() {
                return data;
        }

        @Override
        public void setData(Object data) {
                this.data = data;
        }

}
