package com.jeecms.common.jsonfilter.filter;

import java.math.BigDecimal;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.jeecms.common.util.MathUtil;

/**
 * 重写fastjson 转换bigdecimal类型
 * 
 * @author: wangqq
 * @date: 2018年8月7日 上午8:53:54
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BigDecimalValueFilter implements ValueFilter {

	@Override
	public Object process(Object object, String name, Object value) {
		if (null != value && value instanceof BigDecimal) {
			String str = String.valueOf(value);
			return MathUtil.formatScale(str);
		}
		return value;
	}
}
