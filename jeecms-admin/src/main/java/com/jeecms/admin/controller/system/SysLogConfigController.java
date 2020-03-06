/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.system.domain.SysLogConfig;
import com.jeecms.system.service.SysLogConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志策略Controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 10:56:39
 */
@RequestMapping("/logConfigs")
@RestController
public class SysLogConfigController extends BaseController<SysLogConfig, Integer> {

	@Autowired
	private SysLogConfigService service;

	/**
	 * 获取详情
	 *
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping()
	@SerializeField(clazz = SysLogConfig.class, includes = {"id", "dangerValue", "warnValue", "noticeEmailList",
			"noticeSmsList", "size", "warnSmsTmpId", "dangerSmsTmpId"})
	public ResponseInfo get() throws GlobalException {
		return new ResponseInfo(service.getDefault());
	}

	/**
	 * 添加
	 *
	 * @Title: 添加
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	@Override
	public ResponseInfo save(@RequestBody @Valid SysLogConfig sysLogConfig,
							 BindingResult result) throws GlobalException {
		String emailList = sysLogConfig.getNoticeEmailList();
		String smsList = sysLogConfig.getNoticeSmsList();
		Map<String, String> map = validate(emailList, smsList);
		if (map != null && map.size() > 0) {
			for (String key : map.keySet()) {
				return new ResponseInfo(key, map.get(key));
			}
		}
		return super.save(sysLogConfig, result);
	}

	/**
	 * 修改
	 *
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	@Override
	public ResponseInfo update(@RequestBody @Valid SysLogConfig sysLogConfig,
							   BindingResult result) throws GlobalException {
		validateId(sysLogConfig.getId());
		Map<String, String> map = validate(sysLogConfig.getNoticeEmailList(), sysLogConfig.getNoticeSmsList());
		if (map != null && map.size() > 0) {
			for (String key : map.keySet()) {
				return new ResponseInfo(key, map.get(key));
			}
		}
		return super.updateAll(sysLogConfig, result);
	}

	/**
	 * 批量校验邮箱和手机号
	 *
	 * @param emailList 邮箱(;隔开)
	 * @param smsList   手机号(;隔开)
	 * @return map
	 */
	private Map<String, String> validate(String emailList, String smsList) {
		Map<String, String> map = new HashMap<String, String>(1);
		if (StringUtils.isNotBlank(emailList)) {
			String[] e = emailList.split(";");
			if (e.length > SysLogConfig.MAX_SIZE) {
				map.put(SysOtherErrorCodeEnum.UP_TO_100_EMAIL.getCode(),
						SysOtherErrorCodeEnum.UP_TO_100_EMAIL.getDefaultMessage());
				return map;
			}
			for (String email : e) {
				if (!StrUtils.isEmail(email)) {
					map.put(SysOtherErrorCodeEnum.EMAIL_FORMAT_ERROR.getCode(),
							SysOtherErrorCodeEnum.EMAIL_FORMAT_ERROR.getDefaultMessage());
					return map;
				}

			}
		}
		if (StringUtils.isNotBlank(smsList)) {
			String[] e = smsList.split(";");
			if (e.length > SysLogConfig.MAX_SIZE) {
				map.put(SysOtherErrorCodeEnum.UP_TO_100_SMS.getCode(),
						SysOtherErrorCodeEnum.UP_TO_100_SMS.getDefaultMessage());
				return map;
			}
			for (String email : e) {
				if (!StrUtils.isPhone(email)) {
					map.put(SysOtherErrorCodeEnum.PHONE_FORMAT_ERROR.getCode(),
							SysOtherErrorCodeEnum.PHONE_FORMAT_ERROR.getDefaultMessage());
					return map;
				}

			}
		}
		return null;
	}

}



