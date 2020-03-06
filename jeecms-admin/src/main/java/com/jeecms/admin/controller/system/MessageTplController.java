/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.MessageTpl;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.service.MessageTplService;
import com.jeecms.util.SystemContextUtils;

/**
 * 消息模板设置
 * 
 * @author: ljw
 * @version 1.0
 * @date: 2019年8月15日 上午9:57:12
 */
@RestController
@RequestMapping("/messageTpl")
public class MessageTplController extends BaseController<MessageTpl, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 分页查询
	* @Title: page 
	* @param request 请求
	* @param pageable 分页
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/page")
	@SerializeField(clazz = MessageTpl.class, includes = { "id", "mesTitle", "mesCode", "remark", "tplType" })
	public ResponseInfo page(HttpServletRequest request, 
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) 
					throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		//区分模板类型
		params.put("EQ_tplType_Integer", 
				new String[] { request.getParameter("tplType") });
		if (SystemContextUtils.getSiteId(request) != null) {
			params.put("EQ_siteId_Integer", 
					new String[] { SystemContextUtils.getSiteId(request).toString() });
		}
		return new ResponseInfo(service.getPage(params, pageable, false));
	}

	/**
	 * 获取单个对象
	 */
	@GetMapping(value = "/{id}")
	@MoreSerializeField({
			@SerializeField(clazz = MessageTpl.class, includes = { "id", "mesTitle", "mesCode", 
					"remark", "siteId", "tplType", "details" }),
			@SerializeField(clazz = MessageTplDetails.class, includes = { "id", "mesTplId", 
					"isOpen", "mesType",
					"tplId", "mesTitle", "mesContent", "extendedField" }) })
	@Override
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 添加消息模版
	* @Title: save 
	* @param request 请求
	* @param tpl 请求对象
	* @param result 检验
	* @throws GlobalException 异常
	 */
	@PostMapping
	public ResponseInfo save(HttpServletRequest request,
			@RequestBody @Valid MessageTpl tpl, BindingResult result) throws GlobalException {
		if (messageTplService.findByMesCode(tpl.getMesCode()) != null) {
			return new ResponseInfo(SettingErrorCodeEnum.MSG_CODE_EXIST.getCode(),
					SettingErrorCodeEnum.MSG_CODE_EXIST.getDefaultMessage());
		} else {
			tpl.setSiteId(SystemContextUtils.getSiteId(request));
			return messageTplService.saveMessageTpl(tpl);
		}
	}

	/**
	 * 修改消息模版
	* @Title: update 
	* @param tpl 模板对象
	* @throws GlobalException 异常
	 */
	@PutMapping
	public ResponseInfo update(@RequestBody @Valid MessageTpl tpl) throws GlobalException {
		messageTplService.updateMessageTpl(tpl);
		return new ResponseInfo();

	}

	/**
	 * 校验模板标识是否是唯一的
	* @Title: checkMesCode 
	* @param mesCode 模板标识
	* @param id 模板ID
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/mesCode/unique")
	public ResponseInfo checkMesCode(@RequestParam String mesCode, Integer id) throws GlobalException {
		MessageTpl tpl = messageTplService.findByMesCode(mesCode);
		if (tpl != null) {
			if (id != null && tpl.getId().equals(id)) {
				return new ResponseInfo(Boolean.TRUE);
			} else {
				return new ResponseInfo(Boolean.FALSE);
			}
		}
		return new ResponseInfo(Boolean.TRUE);
	}

	@Autowired
	private MessageTplService messageTplService;
}
