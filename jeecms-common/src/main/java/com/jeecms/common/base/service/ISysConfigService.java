package com.jeecms.common.base.service;

import java.util.Map;

import com.jeecms.common.base.domain.ISysConfig;
import com.jeecms.common.exception.GlobalException;

/**
 * 系统服务接口
 * 
 * @author: tom
 * @date: 2019年1月11日 下午6:06:24
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ISysConfigService {
	public ISysConfig findDefault() throws GlobalException;

	public void change(String attrName, String attrValue) throws GlobalException;
	
	public void change(Map<String,String>attrs) throws GlobalException;
}
