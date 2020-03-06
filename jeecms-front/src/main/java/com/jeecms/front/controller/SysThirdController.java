/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysThird;
import com.jeecms.system.service.SysThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 第三登录获取controller
 * 
 * @author: ztx
 * @date: 2019年1月18日 下午4:08:17
 */
@RequestMapping("/thirds")
@RestController
public class SysThirdController extends BaseController<SysThird, Integer> {

	/**
	 * 获取第三方登录配置信息
	 */
	@GetMapping()
	@MoreSerializeField({ @SerializeField(clazz = SysThird.class, includes = { "code", "isEnable" }) })
	public ResponseInfo thirdInfo() throws GlobalException {
		List<SysThird> result = service.getList();
		return new ResponseInfo(result);
	}

	@Autowired
	private SysThirdService service;
}
