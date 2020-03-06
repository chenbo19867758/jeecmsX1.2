package com.jeecms.admin.controller.wechat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.PageableUtil;
import com.jeecms.wechat.domain.MiniprogramCode;
import com.jeecms.wechat.domain.dto.MiniprogramCodeDto;
import com.jeecms.wechat.service.MiniprogramCodeService;


/**
 * 草稿箱模板库Controller
 * 
 * @Description:
 * @author wulongwei
 * @date 2018年11月2日 下午2:16:40
 */
@RequestMapping(value = "/miniprogramCode")
@RestController
public class MiniprogramTemplateDraftController extends BaseController<MiniprogramCode, Integer> {

	/**
	 * 同步草稿箱
	 */
	@RequestMapping(value = "/synch/draft", method = RequestMethod.GET)
	public ResponseInfo synchronousDraft(HttpServletRequest request) throws GlobalException {
		String appId = service.getOpenAppId();
		if (StringUtils.isNotBlank(appId)) {
			service.synchronousDraft(appId);
		}
		return new ResponseInfo();
	}

	/**
	 * 同步模板库
	 */
	@RequestMapping(value = "/synch/template", method = RequestMethod.GET)
	public ResponseInfo synchronousTemplate(HttpServletRequest request) throws GlobalException {
		String appId = service.getOpenAppId();
		if (StringUtils.isNotBlank(appId)) {
			service.synchronousTemplate(appId);
		}
		return new ResponseInfo();
	}

	/**
	 * 分页查询
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({ 
		@SerializeField(clazz = MiniprogramCode.class, includes = { "id","codeVersion","codeDesc",
				"draftId","templateId","codeType","isNew","submitTimeStr"}) 
	})
	public ResponseInfo page(HttpServletRequest request,@RequestParam String codeType,Pageable pageable) 
					throws GlobalException {
		Map<String,String[]> params = super.getCommonParams(request);
		params.put("EQ_codeType_Integer", new String[] { codeType });
		List<Order> orders = new ArrayList<Sort.Order>(2);
		orders.add(new Order(Direction.DESC,"isNew"));
		orders.add(new Order(Direction.ASC,"createTime"));
		return new ResponseInfo(service.getPage(params, PageableUtil.by(pageable, orders), false));
	}
	
	/**
	 * 添加到模板库
	 */
	@RequestMapping(value = "/template", method = RequestMethod.PUT)
	public ResponseInfo addTemplate(@RequestBody @Valid MiniprogramCodeDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		MiniprogramCode code = service.findById(dto.getId());
		if (code == null) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		if (MiniprogramCode.TEMPLATE.equals(code.getCodeType())) {
			return new ResponseInfo(RPCErrorCodeEnum.TEMPLATE_CANNOT_BE_CONVERTED.getCode(),
					RPCErrorCodeEnum.TEMPLATE_CANNOT_BE_CONVERTED.getDefaultMessage());
		}
		
		String appId = service.getOpenAppId();
		if (StringUtils.isNotBlank(appId)) {
			service.addTemplate(appId, dto.getId());
		}
		return new ResponseInfo();
	}

	/**
	 * 删除模板库的模版
	 */
	@RequestMapping(value = "/template", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseInfo deleteTemplate(@RequestBody @Valid MiniprogramCodeDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		MiniprogramCode code = service.findById(dto.getId());
		if (MiniprogramCode.DRAFT.equals(code.getCodeType())) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		if (code.getIsNew()) {
			return new ResponseInfo(RPCErrorCodeEnum.TEMPLATE_CANNOT_BE_DELETED.getCode(),
					RPCErrorCodeEnum.TEMPLATE_CANNOT_BE_DELETED.getDefaultMessage());
		}
		String appId = service.getOpenAppId();
		if (StringUtils.isNotBlank(appId)) {
			service.deleteTemplate(appId,dto.getId());
		}
		return new ResponseInfo();
	}

	/**
	 * 设为最新版本
	 */
	@RequestMapping(value = "/template/status", method = RequestMethod.PUT)
	public ResponseInfo updateTemplate(@RequestBody @Valid MiniprogramCodeDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		MiniprogramCode code = service.findById(dto.getId());
		// 如果是草稿箱直接抛出异常
		if (MiniprogramCode.DRAFT.equals(code.getCodeType())) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		if (code.getIsNew()) {
			return new ResponseInfo();
		}
		service.updateTemplate(dto.getId());
		return new ResponseInfo();
	}

	@Autowired
	private MiniprogramCodeService service;
}
