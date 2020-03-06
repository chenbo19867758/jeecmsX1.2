package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 栏目错误码枚举  号段范围 11001~11500
 * @author: tom
 * @date:   2018年11月6日 下午6:51:50     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum ChannelErrorCodeEnum implements ExceptionInfo {
	/**栏目通用错误*/
	CHANNEL_ERROR("11001", "channel common error!"),
	/**没有该栏目修改权限*/
	NO_PERMISSION_MODIFY_CHANNEL_ERROR("11002", "没有该栏目修改权限!"),
	/**没有该栏目删除权限*/
	NO_PERMISSION_DEL_CHANNEL_ERROR("11003", "没有该栏目删除权限!"),
	/**没有该栏目查看权限*/
	NO_PERMISSION_VIEW_CHANNEL_ERROR("11004", "没有该栏目查看权限!"),
	/**没有该栏目静态化权限*/
	NO_PERMISSION_STATIC_CHANNEL_ERROR("11005", "没有该栏目静态化权限!"),
	/**没有该栏目创建子栏目权限*/
	NO_PERMISSION_CREATE_CHANNEL_ERROR("11006", "没有该栏目创建子栏目权限!"),
	/**没有该栏目分配权限*/
	NO_PERMISSION_PERM_ASSIGN_CHANNEL_ERROR("11007", "没有该栏目分配权限!"),
	/**没有该栏目合并权限*/
	NO_PERMISSION_MERGE_CHANNEL_ERROR("11008", "没有该栏目合并权限!"),
	/** 栏目不在站点下面*/
	CHANNEL_IS_NOT_UNDER_THE_SITE("11009","Channel is not under the site!"),
	/** 栏目父类id传入错误*/
	CHANNEL_PARENT_CLASS_ID_PASSED_ERROR("11010","Channel parent class id passed error!"),
	/** 栏目id传入错误*/
	CHANNEL_ID_PASSED_ERROR("11011","Channel id passed error!"),
	/** 该栏目不是底层栏目*/
	THE_INCOMING_COLUMN_IS_NOT_THE_BOTTOM_COLUMN("11012","The incoming column is not the bottom column!"),
	/** 该栏目未加入回收站*/
	THE_CHANNEL_NOT_ADDED_TO_THE_RECYCLE_BIN("11013","The channel not added to the recycle bin!"),
	/** 选中子集必须选中父级*/
	SELECT_A_SUBSET_MUST_SELECT_A_PARENT("11014","Select a subset must select a parent"),
	/** 父类栏目拥有内容，还原失败*/
	PARENT_CLASS_HAS_CONTENT("11015","Parent class has content!"),
	/** 父类栏目拥有内容，无法新建子栏目*/
	PARENT_CLASS_HAS_CONTENT_NOT_SAVE("11016","Parent class has content not save!"),
	/** 传入的栏目名称格式错误*/
	CHANNEL_NAME_FORMAT_PASSED_ERROR("11017","Channel name format passed error!"),
	/** 目标栏目不是底层栏目，无法合并*/
	AIMS_CHANNEL_IS_NOT_BOTTOM_NOT_MERGE("11018","Aims channel is not bottom not merge!"),
	/** 栏目路径目录不能为index*/
	CHANNEL_PATH_IS_NOT_INDEX("11019","Channel path is not index"),
	/** 选择栏目不能为空 */
	CHANNEL_IS_NOT_NULL("11020","Channel is not null"),
	;

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
	ChannelErrorCodeEnum(String code, String defaultMessage) {
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
