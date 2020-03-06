package com.jeecms.common.jsonfilter.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jeecms.common.jsonfilter.bean.JsonFilterObject;
import com.jeecms.common.jsonfilter.filter.BigDecimalValueFilter;
import com.jeecms.common.jsonfilter.filter.SimpleSerializerFilter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 继承fastjsoonHttpMessageConverter类的，重写writeInternal函数
 * 
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class JsonFilterHttpMessageConverter extends FastJsonHttpMessageConverter {

	private Charset charset;
	private SerializerFeature[] features;
	/**
	 * 添加属性过滤器
	 */
	private BigDecimalValueFilter bigDecimalValueFilter;

	/**
	 * 构造器 配置fastjson
	 */
	public JsonFilterHttpMessageConverter() {
		super();
		setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", Charset.forName("UTF8")),
				new MediaType("application", "*+json", Charset.forName("UTF8")),
				new MediaType("application", "jsonp", Charset.forName("UTF8")),
				new MediaType("application", "*+jsonp", Charset.forName("UTF8"))));
		setCharset(Charset.forName("UTF8"));
		//是否输出值为null的字段,默认为false
		setFeatures(SerializerFeature.WriteBigDecimalAsPlain, SerializerFeature.WriteMapNullValue,
				SerializerFeature.DisableCircularReferenceDetect,
				//数值字段如果为null,输出为0,而非null
				// SerializerFeature.WriteNullNumberAsZero,
				// List字段如果为null,输出为[],而非null
				SerializerFeature.WriteNullListAsEmpty, 
				// 字符类型字段如果为null,输出为”“,而非null
				SerializerFeature.WriteNullStringAsEmpty, 
				// Boolean字段如果为null,输出为false,而非null
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteMapNullValue
															
		);
		bigDecimalValueFilter = new BigDecimalValueFilter();
		/**自定义String类型反序列化类*/
		/**xss注入应该在输入时候控制*/
		//ParserConfig.getGlobalInstance().putDeserializer(String.class, new StringDeserializer());
	}

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		InputStream in = inputMessage.getBody();
		String inputStr = StreamUtils.copyToString(in, getFastJsonConfig().getCharset());
		return JSON.parseObject(inputStr, type, getFastJsonConfig().getFeatures());
	}

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		OutputStream out = outputMessage.getBody();
		if (obj instanceof JsonFilterObject) {
			JsonFilterObject jsonFilterObject = (JsonFilterObject) obj;
			SimpleSerializerFilter simpleSerializerFilter = new SimpleSerializerFilter(
					jsonFilterObject.getIncludes(),jsonFilterObject.getExcludes());
			// 添加值过滤器
			SerializeFilter[] serializeFilters = { simpleSerializerFilter, bigDecimalValueFilter };
			String text = JSON.toJSONString(jsonFilterObject.getJsonObject(), serializeFilters, features);
			byte[] bytes = text.getBytes(this.charset);
			out.write(bytes);
		} else {
			String text = JSON.toJSONString(obj, this.features);
			byte[] bytes = text.getBytes(this.charset);
			out.write(bytes);
		}
	}

	@Override
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void setFeatures(SerializerFeature... features) {
		this.features = features;
	}
}
