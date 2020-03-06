/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysThird;
import com.jeecms.system.service.SysThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;


/**
 * 第三登录设置controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-18
 */
@RequestMapping("/thirds")
@RestController
public class SysThirdController extends BaseController<SysThird, Integer> {

	@Autowired
	private SysThirdService sysThirdService;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * @Title: 通过code获取第三方登录配置
	 * @param: @param code
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping()
	@SerializeField(clazz = SysThird.class, includes = {"id", "appId", "appKey", "isEnable", "reMark"})
	public ResponseInfo get(String code) throws GlobalException {
		SysThird sysThird = sysThirdService.getCode(code);
		return new ResponseInfo(sysThird);
	}

	/**
	 * @param sysThird 第三方登录信息实体
	 * @Title: 添加/修改第三方登录信息
	 * @Description: TODO
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	@Override
	public ResponseInfo save(@RequestBody @Valid SysThird sysThird, BindingResult result) throws GlobalException {
		if (sysThird.getId() != null) {
			SysThird bean = new SysThird();
			bean.setId(sysThird.getId());
			bean.setReMark(sysThird.getReMark());
			bean.setIsEnable(sysThird.getIsEnable());
			bean.setAppId(sysThird.getAppId());
			bean.setAppKey(sysThird.getAppKey());
			return super.update(bean, result);
		}
		return super.save(sysThird, result);
	}
}
