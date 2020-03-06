package com.jeecms.common.jsonfilter.filter;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.jeecms.common.util.ProxyUtil;
import com.jeecms.common.util.ReflectUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;

/**
 * 自定义FastJson过滤器
 * 
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
public class SimpleSerializerFilter extends SimplePropertyPreFilter {
	private Map<Class, HashSet<String>> includes;
	private Map<Class, HashSet<String>> excludes;
	private static final String[] EXCLUDE_PATTER = { "treeCondition", "lftName", "parentName", "rgtName",
		"hasDeletedName", "createUserName", "updateUserName", 
		"createTimeName", "updateTimeName", "hasDeleted" };

	public SimpleSerializerFilter(Map<Class, HashSet<String>> includes, Map<Class, HashSet<String>> excludes) {
		this.includes = includes;
		this.excludes = excludes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean apply(JSONSerializer serializer, Object source, String name) {
		List<String> excludefields = Arrays.asList(EXCLUDE_PATTER);
		if (excludefields.contains(name)) {
			return false;
		}

		if (!isEmpty(includes)) {
			for (Map.Entry<Class, HashSet<String>> include : includes.entrySet()) {
				Class objClass = include.getKey();
				Set<String> includeProp = include.getValue();
				if (objClass.isAssignableFrom(source.getClass())) {
					boolean includeFlag = includeProp.contains(name);
					if (includeFlag) {
						loadHibernateProxy(source, name);
					}
					return includeFlag;
				} else {
					continue;
				}
			}
		}
		if (!isEmpty(excludes)) {
			for (Map.Entry<Class, HashSet<String>> exclude : excludes.entrySet()) {
				Class objClass = exclude.getKey();
				Set<String> includeProp = exclude.getValue();
				if (objClass.isAssignableFrom(source.getClass())) {
					boolean includeFlag = !includeProp.contains(name);
					if (includeFlag) {
						loadHibernateProxy(source, name);
					}
					return includeFlag;
				} else {
					continue;
				}
			}
		}
		loadHibernateProxy(source, name);
		return true;
	}

	public boolean isEmpty(Map map) {
		return map == null || map.size() < 1;
	}

	/**
	 * 加载懒加载对象
	 * @Title: loadHibernateProxy  
	 * @param object 对象
	 * @param name     属性名
	 * @return: void
	 */
	public void loadHibernateProxy(Object object, String name) {
		/** PageImpl 对象无get 和字段 此处反射会有异常，该异常可以忽略 */
		Object value = object;
		try {
			value = ReflectUtils.getFieldValue(object, name);
		} catch (Exception e) {
			value = "";
			return;
		}
		if (value instanceof HibernateProxy) {
			/**处理数据错误的情况，代理对象不存在导致解析错误*/
			try {
				value = ProxyUtil.deProxy(value);
			} catch (Exception e) {
				ReflectUtils.setFieldValue(object, name, null);
				return;
			}
		}
		if (value instanceof PersistentCollection) {
			// 实体关联集合一对多等
			PersistentCollection collection = (PersistentCollection) value;
			if (!collection.wasInitialized()) {
				collection.forceInitialization();
			}
		}
	}
}
