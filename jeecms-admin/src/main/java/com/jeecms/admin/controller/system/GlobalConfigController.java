/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.constants.TplConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: wulongwei
 * @date:   2019年4月13日 下午3:54:44     
 */
@RestController
@RequestMapping("/config/global")
public class GlobalConfigController extends BaseController<GlobalConfig, Integer> {

	/**
	 * 获取详情
	 */
	@MoreSerializeField({
			@SerializeField(clazz = GlobalConfig.class, includes = { "id",  "attrs", "aduitMemberFlowId"}),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url" })})
	@GetMapping
	public ResponseInfo get() throws GlobalException {
		return new ResponseInfo(service.getGlobalConfig());
	}

	/**
	 * 添加系统配置信息
	 */
	@PutMapping
	@Override
	public ResponseInfo save(@RequestBody @Valid GlobalConfig config, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		return service.saveGlobalConfig(config);
	}

	/**
	 * 获取webapp文件下的文件夹名称
	 */
	@GetMapping(value = "folderList")
	public ResponseInfo folderList(@RequestParam(required = false, 
		defaultValue = TplConstants.TPL_BASE) String relativePath)throws GlobalException {
		List<String> result = service.folderList(relativePath);
		return new ResponseInfo(result);
	}

	/**
	 * 获取全局配置信息
	 */
	@RequestMapping(value = "/getConfigAttr", method = RequestMethod.GET)
	public ResponseInfo getConfigAttr() throws GlobalException {
		return new ResponseInfo(service.get().getConfigAttr());
	}
	
	@Autowired
	private GlobalConfigService service;

}
