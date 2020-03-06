package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 内容信息枚举  号段范围 10501~11000
 * @author: tom
 * @date:   2018年11月6日 下午6:52:33     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum ContentErrorCodeEnum implements ExceptionInfo {
	
	/**内容维护发生了错误*/
	CONTENT_COMMON_ERROR("10501","content common error!"),
	/**没有该栏目的内容修改权限*/
	NO_PERMISSION_MODIFY_ERROR("10502", "没有该栏目的内容修改权限!"),
	/**没有该栏目的内容删除权限*/
	NO_PERMISSION_DEL_ERROR("10503", "没有该栏目的内容删除权限！"),
	/**没有该栏目的内容发布权限*/
	NO_PERMISSION_PUBLISH_ERROR("10504", "没有该栏目的内容发布权限!"),
	/**没有该栏目的内容微博推送权限*/
	NO_PERMISSION_WEIBO_PUSH_ERROR("10505", "没有该栏目的内容微博推送权限!"),
	/**没有该栏目的内容创建权限*/
	NO_PERMISSION_CREATE_CONTENT_ERROR("10506", "没有该栏目的内容创建权限!"),
	/**没有该栏目的内容站点推送权限*/
	NO_PERMISSION_SITE_PUSH_ERROR("10507", "没有该栏目的内容站点推送权限!"),
	/**没有该栏目的内容微信推送权限*/
	NO_PERMISSION_WECHAT_PUSH_ERROR("10500", "没有该栏目的内容微信推送权限!"),
	/**没有该栏目的内容查看权限*/
	NO_PERMISSION_VIEW_ERROR("10508", "没有该栏目的内容查看权限!"),
	/**不可引用自身的栏目**/
	NO_QUOTE_CHANNEL_MYSELF_ERROR("10509", "不可引用自身的栏目!"),
	/** 该栏目已经存在该内容的引用**/
	ALREADY_QUOTE_CHANNEL_CONTENT("10510", "该栏目已经存在该内容的引用"),
	/** 站点或栏目内标题不允许重复*/
	CONTENT_TITLE_IS_NOT_ALLOWED_TO_REPEAT("10511", "Content title is not allowed to repeat!"),
	/** 附件必须加密*/
	ATTACHMENTS_MUST_BE_ENCRYPTED("10512", "Attachments must be encrypted"),
	/** 内容必须加密*/
	CONTENT_MUST_BE_ENCRYPTED("10513", "Content must be encrypted"),
	/** 删除远程云存储或FTP文件失败*/
	DELETE_REMOTE_HTML_ERROR("10514", "delete remote oss or ftp html page error"),
	/** 生成静态html-未找到模板*/
	CREATE_STATIC_HTML_IO_ERROR("10515", "create static html page error"),
	/** 生成静态html-模板语法错误*/
	CREATE_STATIC_HTML_TPL_ERROR("10516", "create static html page error"),
	/**没有该栏目的内容置顶权限*/
	NO_PERMISSION_TOP_ERROR("10517", "No permission to top the channel content!"),
	/**没有该栏目的内容移动权限*/
	NO_PERMISSION_MOVE_ERROR("10518", "No permission to move the channel content!"),
	/**没有该栏目的内容排序权限*/
	NO_PERMISSION_SORT_ERROR("10519", "No permission to sort the channel content!"),
	/**没有该栏目的内容复制权限*/
	NO_PERMISSION_COPY_ERROR("10520", "No permission to copy the channel content!"),
	/**没有该栏目的内容引用权限*/
	NO_PERMISSION_QUOTE_ERROR("10521", "No permission to Quote the channel content!"),
	/**没有该栏目的内容内容类型操作权限*/
	NO_PERMISSION_TYPE_ERROR("10522", "No permission to type the channel content!"),
	/**没有该栏目的内容归档操作权限*/
	NO_PERMISSION_FILE_ERROR("10523", "No permission to file the channel content!"),
	/** 模型为空*/
	THE_MODEL_IS_NULL("10524","The model is null!"),
	/** 已发布的内容不能修改*/
	PUBLISHED_CONTENT_CANNOT_BE_EDITED("10525","Published content cannot be edited!"),
	/** 不能在下线时间后或下线时间小于发布时间进行发布操作*/
	CONTENT_CANNOT_BE_PUBLISHED("10526","Content cannot be published"),
	/** 内容所在栏目未配置工作流*/
	CONTENT_CHANNEL_NOT_IN_FLOW("10527","Workflow is not configured in the content channel"),
	/** 内容未提交流程，不能审核*/
	FLOW_NOT_START("10528","Content not submitted process, not audited"),
	/** 流程任务不存在，流程已处理或者不是您操作的数据*/
	FLOW_TASK_NOT_EXIST("10529","Process tasks do not exist"),
	/**流程节点不存在*/
	FLOW_NODE_NOT_EXIST("10530","Process node do not exist"),
	/**处理意见必填*/
	FLOW_PROCESS_REASON_MUST("10531","Handling opinions must be filled in"),
	/**没有可以撤回的动作*/
	FLOW_NO_ACTION_CANCEL("10532","There is no action that can be withdrawn"),
	/**流程已经结束，不允许撤回*/
	FLOW_HAS_END("10533","The process is over and no withdrawal is allowed"),
	/**流程配置该动作不允许撤回*/
	FLOW_CANNOT_CANCEL("10534","Process configuration This action is not allowed to be withdrawn"),
	/**会签不支持撤回*/
	FLOW_SIGN_JOINTLY_CANNOT_CANCEL("10535","Signature does not support withdrawal"),
	/**数据正在流转中，不能再次发起提交审核*/
	DATA_IN_FLOW("10536","Data is in circulation and submission audits cannot be initiated again"),
	/** 内容状态不是暂存*/
	CONTENT_NOT_IS_TEMPORARY_STORAGE("10537","Content not is temporary storage!"),
	/** 用户下载权限不足*/
	CONTENT_DOWNLOAD_NOT_PERMISSIONS("10538","Insufficient download permissions for users!"),
	/** 只支持导入doc,docx文件格式*/
	IMPORT_FORMAT_NOT_SUPPORTED("10539","Only import doc,docx file format is supported!"),
	/** 引用的内容无法推送*/
	QUOTED_CONTENT_CANNOT_BE_PUSHED("10540","Quoted content cannot be pushed!"),
	/** 新增或修改传入的状态错误*/
	ADD_OR_UPDATE_INCOMING_STATUS_ERROR("10541","Add or update incoming status error"),
	/** 无法修改该内容状态*/
	CONTENT_STATUS_ERROR("10542","The state of the content cannot be modified"),
	/** 内容id传入错误*/
	CONTENT_ID_PASSED_ERROR("10543","Content id passed error!"),
	/** 评论ID传入错误*/
	COMMENT_ID_PASSED_ERROR("10544","Comment id passed error!"),
	/** openoffice 服务启动失败*/
	OPEN_OFFICE_SERVER_ERROR("10545","Service startup failure!"),
	/**openOffice 转换文件失败*/
	OPEN_OFFICE_CONVERSION_ERROR("10546","openOffice conversion failure!"),
	/** 栏目没有工作流，不能提交审核*/
	CHANNEL_NOT_WORKFLOW_NOT_REVIEW("10547","Channel not workflow not review"),
	/** openoffice 未成功安装*/
	OPEN_OFFICE_SERVER_UNINSTALL("10548","Openoffice Service Unsuccessful installation!"),
	/** 栏目存在子栏目，无法还原*/
	CHANNEL_HAS_CHILD_ERROR("10549","Columns exist subcolumns, unable to restore!"),
	/**会签节点不存在人员*/
	FLOW_PROCESS_NO_PERSON_MUST("10550","Countersign node does not exist"),
	/** 下载资源不存在*/
	CONTENT_DOWNLOAD_FILE_EXIST("10551","file loss"),;
	
	/** 异常代码。 */
	private String code;

	/** 异常对应的默认提示信息。 */
	private String defaultMessage;

	/** 异常对应的原始提示信息。 */
	private String originalMessage;

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 需转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/** 异常对应的响应数据。 */
	private Object data = new Object();

	/**
	 * Description: 根据异常的代码、默认提示信息构建一个异常信息对象。
	 *
	 * @param code
	 *            异常的代码。
	 * 
	 * @param defaultMessage
	 *            异常的默认提示信息。
	 */
	ContentErrorCodeEnum(String code, String defaultMessage) {
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
