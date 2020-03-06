package com.jeecms.common.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 缓存配置
 * 
 * @author: tom
 * @date: 2018年12月24日 下午5:54:18
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {
	static Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);
	String queryMethod = "find";

	/**
	 * 自定义生成key的规则
	 * 
	 * @return
	 */
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Object generate(Object o, Method method, Object... objects) {
				// 格式化缓存key字符串
				StringBuilder sb = new StringBuilder();
				// 追加类名
				sb.append(o.getClass().getName()).append(".");
				if (objects != null) {
					if (objects.length > 0 && objects[0] != null) {
						if (method.getName().startsWith(queryMethod)) {
							// 查询类方法
							for (Object obj : objects) {
								if (obj != null) {
									sb.append(obj.toString());
								}
							}
						} else {
							// 普通save update delete方法
							if (objects[0].getClass().isAssignableFrom(Collection.class)
									|| objects[0].getClass().isArray()) {
								sb.append("array");
							} else {
								Object id = null;
								Class clazz = objects[0].getClass();
								Method getId;
								try {
									getId = clazz.getDeclaredMethod("getId");
									id = getId.invoke(objects[0]);
								} catch (Exception e) {
									logger.error(e.getMessage());
								}
								if (id != null) {
									sb.append(id);
								} else {
									sb.append(objects[0].toString());
								}
							}
						}
					}
				}
				return sb.toString();
			}
		};
	}

}
