package com.jeecms.common.constants;

import org.springframework.beans.factory.annotation.Value;

/**
 * 加密内容常量配置 如DES请求参数key
 * @author: tom
 * @date:   2018年12月24日 下午6:10:18     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ContentSecurityConstants {
	/**
	 * DES方式请求的request key
	 */
	@Value("${security.des.param.name}")
	public static final String DES_PARAMETER_NAME = "desStr";
	/**
	 * 3DES解密key,限定24位
	 */
	@Value("${security.des.key}")
	public static final String DES_KEY = "WfJTKO9S4eLkrPz2JKrAnzdb";
	/**
	 * 3DES解密iv,限定8位
	 */
	@Value("${security.des.iv}")
	public static final String DES_IV = "D076D35C";
	/**
	 * 加密内容字符集
	 */
	public static final String CONTENT_CHARSET = "UTF-8";
	/**
	 * 传递的attribute前缀
	 */
	public static final String ATTRIBUTE_PREFFIX = "security_";
	/** 加密request */
	public static final String SECURITY_REQUEST = "req_security";
}
