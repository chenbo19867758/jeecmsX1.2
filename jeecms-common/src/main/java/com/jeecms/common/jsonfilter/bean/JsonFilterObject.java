package com.jeecms.common.jsonfilter.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * fastjson过滤器对象，存储保留/排除字段和对应类，
 * 
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
public class JsonFilterObject {

	private Object jsonObject;

	private Map<Class, HashSet<String>> includes = new HashMap<Class, HashSet<String>>();

	private Map<Class, HashSet<String>> excludes = new HashMap<Class, HashSet<String>>();

	public JsonFilterObject() {
	}

	/**
	 * 构造器
	 * @param jsonObject 对象
	 * @param includes 包含的属性
	 * @param excludes 排除的属性
	 */
	public JsonFilterObject(Object jsonObject, Map<Class, HashSet<String>> includes,
			Map<Class, HashSet<String>> excludes) {
		this.jsonObject = jsonObject;
		this.includes = includes;
		this.excludes = excludes;
	}

	public Object getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(Object jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Map<Class, HashSet<String>> getIncludes() {
		return includes;
	}

	public void setIncludes(Map<Class, HashSet<String>> includes) {
		this.includes = includes;
	}

	public Map<Class, HashSet<String>> getExcludes() {
		return excludes;
	}

	public void setExcludes(Map<Class, HashSet<String>> excludes) {
		this.excludes = excludes;
	}
}
