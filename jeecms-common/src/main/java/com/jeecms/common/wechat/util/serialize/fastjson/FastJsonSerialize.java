package com.jeecms.common.wechat.util.serialize.fastjson;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.jeecms.common.wechat.util.serialize.SerializeDelegate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * 
 * @Description:FastJson 及xstream 实现的json和xml序列化.
 * @author: qqwang
 * @date:   2018年6月21日 下午3:31:55     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class FastJsonSerialize implements SerializeDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonSerialize.class);
	@Override
	public <T> T jsonToBean(String json, Class<T> clazz) {
		T  bean = JSONObject.parseObject(json,clazz);
		return bean;
	}

	@Override
	public String beanToJson(Object object) {
		SerializeConfig config = new SerializeConfig();
		config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
		LOGGER.info("bean To Json:{}",JSONObject.toJSONString(object, config));
		return  JSONObject.toJSONString(object, config);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T xmlToBean(String xml, Class<T> clazz) {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new String[]{clazz.getName()});
        xStream.processAnnotations(clazz);
        T t = (T)xStream.fromXML(xml);
        return t;
	}
	
	@Override
	public String beanToXml(Object obj) {
		XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);
	}

	public static Document strToDocument(String xml) throws DocumentException {
		return DocumentHelper.parseText(xml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T xmlInputStreamToBean(InputStream xml, Class<T> clazz) {
		if(xml != null ){
			 XStream xStream = new XStream(new DomDriver());
		     XStream.setupDefaultSecurity(xStream);
		     xStream.allowTypes(new String[]{clazz.getName()});
		    xStream.processAnnotations(clazz);
		    Object obj =xStream.fromXML(xml);
		    T t = (T)obj;
		    return t;
		}
		return null;
	}
	
	
	
}
