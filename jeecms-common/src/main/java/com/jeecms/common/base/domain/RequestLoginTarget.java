package com.jeecms.common.base.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录类型
 * @author: tom
 * @date:   2018年12月24日 下午3:12:58     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum RequestLoginTarget {
	/**
	 * 平台
	 */
	admin,
	/**
	 * 会员中心
	 */
	member;
	
	private static Map<String, RequestLoginTarget> targets = new HashMap<String, RequestLoginTarget>(2);

	static {
		targets.put(admin.name(), admin);
		targets.put(member.name(), member);
	}

	/**
	 * 获取对应的枚举类型
	 * @Title: getValueOf
	 * @param target 字符串
	 * @return: RequestLoginTarget
	 */
	public static RequestLoginTarget getValueOf(String target){
		RequestLoginTarget t = targets.get(target);
		if (t == null) {
			t = targets.get(1);
		}
		return t;
	}
}