package com.jeecms.common.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.jeecms.common.web.springmvc.MessageResolver;

/**
 * Description: 非法参数验证工具类。执行参数验证，并封装非法参数的错误或异常信息。
 * @author: tom
 * @date:   2019年3月8日 下午4:27:33   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class IllegalParamValidationUtils {

	/** 非法参数异常信息对象。 */
	private static ThreadLocal<IllegalParamExceptionInfo> illegalParamExceptionInfo = new ThreadLocal<IllegalParamExceptionInfo>();

	/**
	 * Description: 初始化非法参数的提示信息的字符串（带字段名的）及其Map集合，以及其对应的验证异常信息对象。
	 * @param result
	 *            参数验证的绑定结果集对象。
	 */
	public static void initIllegalParamMsg(BindingResult result) {
		/** 临时串，用于组装验证出错的所有字段的错误提示信息（带字段的）。 */
		StringBuffer tempIllegalParamMsg = new StringBuffer();
		/** 获取验证出错的所有字段。 */
		List<FieldError> fieldErrors = result.getFieldErrors();
		/** 临时Map，用于非法参数无效提示信息的Map集合。 */
		Map<String, String> tempIllegalParamMsgMap = new HashMap<String, String>(200);
		/** 遍历并获取验证出错的字段的错误提示信息，并组装验证错误提示信息的Map集合和字符串。 */
		for (FieldError fieldError : fieldErrors) {
			/** 组装非法参数无效提示信息的字符串。参数名与其无效提示信息时间采用“:”分割。各参数及无效提示信息之间采用“，”分割）。 */
			tempIllegalParamMsg.append(fieldError.getField() + ":" + MessageResolver.getMessage(fieldError) + ", ");
			/** 组装非法参数无效提示信息的Map集合。 */
			tempIllegalParamMsgMap.put(fieldError.getField(), MessageResolver.getMessage(fieldError));
		}
		String illegalParamMsg = tempIllegalParamMsg.toString();
		StringUtils.trimTrailingWhitespace(illegalParamMsg);
		StringUtils.trimTrailingCharacter(illegalParamMsg, ',');
		illegalParamExceptionInfo.set(new IllegalParamExceptionInfo(illegalParamMsg, tempIllegalParamMsgMap));
	}
	
	/**
	 * 字段不能全为空验证
	 * @Title: initIllegalParamMsg  
	 * @param params      
	 * @return: void
	 */
	public static void initIllegalParamMsg(String...params) {
		/** 临时串，用于组装验证出错的所有字段的错误提示信息（带字段的）。 */
		StringBuffer tempIllegalParamMsg = new StringBuffer();
		String code=SystemExceptionEnum.INCOMPLETE_PARAM.getCode();
		/** 遍历并获取验证出错的字段的错误提示信息，并组装验证错误提示信息的Map集合和字符串。 */
		for (String p : params) {
			/** 组装非法参数无效提示信息的字符串。参数名与其无效提示信息时间采用“:”分割。各参数及无效提示信息之间采用“，”分割）。 */
			tempIllegalParamMsg.append(p+ ", ");
		}
		String illegalParamMsg = tempIllegalParamMsg.toString();
		StringUtils.trimTrailingWhitespace(illegalParamMsg);
		StringUtils.trimTrailingCharacter(illegalParamMsg, ',');
		illegalParamMsg=illegalParamMsg.substring(0, illegalParamMsg.length()-2);
		illegalParamExceptionInfo.set(new IllegalParamExceptionInfo(code,illegalParamMsg, illegalParamMsg.toString()));
	}

	/**
	 * Description: 获取非法参数异常信息对象。
	 * @return 非法参数异常信息对象。
	 */
	public static IllegalParamExceptionInfo getIllegalParamExceptionInfo() {
		return illegalParamExceptionInfo.get();
	}
	
	/**
	 * 清空数据
	 * TODO
	 * @Title: clean        
	 * @return: void
	 */
	public void clean() {
		illegalParamExceptionInfo.remove();
	}

}
