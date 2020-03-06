package com.jeecms.common.wechat.util.serialize;

import java.io.InputStream;

/**
 * 微信交互数据序列化代理实现接口.不同的序列化需要实现该接口.
 *
 * @author vioao
 */
public interface SerializeDelegate {
    
	/**
	 * JSON字符串转Bean.
	 * @Title: jsonToBean  
	 * @param json
	 * @param clazz
	 * @return      
	 * @return: T
	 */
    <T> T jsonToBean(String json, Class<T> clazz);

    /**
     * Bean转JSON字符串.
     * @Title: beanToJson  
     * @param object
     * @return      
     * @return: String
     */
    String beanToJson(Object object);

    /**
     * 将xml转换为对象.
     * @Title: xmlToBean  
     * @param xml
     * @param c
     * @return      
     * @return: T
     */
    <T> T xmlToBean(String xml, Class<T> c);
    
    /**
     * 将xml转换为对象.
     * @Title: xmlInputStreamToBean  
     * @param xml
     * @param c
     * @return      
     * @return: T
     */
    <T> T xmlInputStreamToBean(InputStream xml, Class<T> c);

    /**
     * 将对象转换为xml.
     * @Title: beanToXml  
     * @param obj
     * @return      
     * @return: String
     */
    String beanToXml(Object obj);
}
