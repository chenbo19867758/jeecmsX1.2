package com.jeecms.common.jsonfilter.serializer;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.StringCodec;
import com.jeecms.common.util.XssUtil;

/**
 * 增加xss过滤
 * @author: tom
 * @date: 2018年11月30日 下午2:27:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class StringDeserializer extends StringCodec {

	public static StringCodec instance = new StringDeserializer();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
		if (clazz == StringBuffer.class) {
			final JSONLexer lexer = parser.lexer;
			if (lexer.token() == JSONToken.LITERAL_STRING) {
				String val = lexer.stringVal();
				lexer.nextToken(JSONToken.COMMA);
				/** 增加xss过滤 */
				if (StringUtils.isNoneBlank(val)) {
					val = XssUtil.cleanXSS(val);
				}
				return ((T) new StringBuffer(val));
			}

			Object value = parser.parse();

			if (value == null) {
				return null;
			}

			/** 增加xss过滤 */
			if (value instanceof String && StringUtils.isNoneBlank((String) value)) {
				value = XssUtil.cleanXSS((String) value);
			}
			return (T) new StringBuffer(value.toString());
		}

		if (clazz == StringBuilder.class) {
			final JSONLexer lexer = parser.lexer;
			if (lexer.token() == JSONToken.LITERAL_STRING) {
				String val = lexer.stringVal();
				lexer.nextToken(JSONToken.COMMA);

				/** 增加xss过滤 */
				if (StringUtils.isNoneBlank(val)) {
					val = XssUtil.cleanXSS(val);
				}
				return (T) new StringBuilder(val);
			}

			Object value = parser.parse();

			if (value == null) {
				return null;
			}

			/** 增加xss过滤 */
			if (value instanceof String && StringUtils.isNoneBlank((String) value)) {
				value = XssUtil.cleanXSS((String) value);
			}
			return (T) new StringBuilder(value.toString());
		}
		return deserialze(parser);
	}

	/**
	 * 反序列化对象解析 增加xss过滤
	 * @Title: deserialze  
	 * @param parser DefaultJSONParser
	 * @return: T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialze(DefaultJSONParser parser) {
		final JSONLexer lexer = parser.getLexer();
		if (lexer.token() == JSONToken.LITERAL_STRING) {
			String val = lexer.stringVal();
			/** 增加xss过滤 */
			if (StringUtils.isNoneBlank(val)) {
				val = XssUtil.cleanXSS(val);
			}
			lexer.nextToken(JSONToken.COMMA);
			return (T) val;
		}

		if (lexer.token() == JSONToken.LITERAL_INT) {
			String val = lexer.numberString();
			/** 增加xss过滤 */
			if (StringUtils.isNoneBlank(val)) {
				val = XssUtil.cleanXSS(val);
			}
			lexer.nextToken(JSONToken.COMMA);
			return (T) val;
		}

		Object value = parser.parse();

		if (value == null) {
			return null;
		}
		/** 增加xss过滤 */
		if (value instanceof String && StringUtils.isNoneBlank((String) value)) {
			value = XssUtil.cleanXSS((String) value);
		}
		return ((T) value.toString());
	}

}
