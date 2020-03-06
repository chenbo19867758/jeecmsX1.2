/*
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.dto.CmsModelDto;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 模型管理controller
 * 
 * @author: wulongwei
 * @date: 2019年4月17日 下午1:37:42
 */
@RestController
@RequestMapping("/model")
public class CmsModelController extends BaseController<CmsModel, Integer> {

	/**
	 * 获取模型信息详情
	 */
	@GetMapping(value = "/{id}")
	@MoreSerializeField({
			@SerializeField(clazz = CmsModel.class, includes = { "id", "modelName", "isGlobal", "isEnable", "tplType",
					"site", "unEnableJson", "enableJson" }),
			@SerializeField(clazz = CmsSite.class, includes = { "id" }) })
	@Override
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return new ResponseInfo(modelService.getInfo(id));
	}

	/**
	 * 获取会员模型信息详情
	 */
	@GetMapping(value = "/member")
	@SerializeField(clazz = CmsModel.class, includes = { "id", "modelName", "unEnableJson", "enableJson" })
	public ResponseInfo getMemberModel() throws GlobalException {
		return new ResponseInfo(modelService.getInfo(null));
	}

	/**
	 * 分页查询模型信息
	 * 
	 * @Title: page
	 * @param request
	 * @param response
	 * @param pageable
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping("/page")
	@SerializeField(clazz = CmsModel.class, includes = { "id", "modelName", "siteId", "isEnable", "tplType",
			"isGlobal" })
	public ResponseInfo page(HttpServletRequest request, HttpServletResponse response, Short isGlobal, Short tplType,
			Boolean isEnable, String modelName, Pageable pageable) throws GlobalException {
		return modelService.getModelPage(tplType, isGlobal, isEnable, modelName, SystemContextUtils.getSiteId(request),
				pageable);
	}

	/**
	 * 保存本站模型
	 * 
	 * @Title: thisSite
	 * @param model
	 * @param result
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/thisSite")
	public ResponseInfo thisSite(HttpServletRequest request, @RequestBody @Valid CmsModel model, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		model.setSite(SystemContextUtils.getSite(request));
		return modelService.saveThisSiteModel(model);
	}

	/**
	 * 保存全站模型
	 * 
	 * @Title: wholeSite
	 * @param model
	 * @param result
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/wholeSite")
	public ResponseInfo wholeSite(@RequestBody @Valid CmsModel model, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return modelService.saveWholeSiteModel(model);
	}

	/**
	 * 修改模型信息
	 * 
	 * @Title: updateModel
	 * @param model
	 * @param result
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping
	public ResponseInfo updateModel(@RequestBody @Valid CmsModel model, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return modelService.updateModel(model);
	}

	/**
	 * 是否启用模型
	 * 
	 * @Title: isEnable
	 * @param modelDto
	 * @param result
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping("/isEnable")
	public ResponseInfo isEnable(@RequestBody @Valid CmsModelDto modelDto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		return modelService.isEnable(modelDto);
	}

	/**
	 * 删除模型
	 * 
	 * @Title: deleteIds
	 * @param details
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping
	public ResponseInfo deleteIds(@RequestBody @Valid DeleteDto details) throws GlobalException {
		Integer[] ids = details.getIds();
		return super.deleteBeatch(ids);
	}

	/**
	 * 校验modelName是否可用
	 * 
	 * @Title: checkModelName
	 * @param modelName
	 * @param tplType
	 * @return
	 * @throws GlobalException
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/modelName/unique", method = RequestMethod.GET)
	public ResponseInfo checkModelName(@NotNull String modelName, @NotNull Short isGlobal, @NotNull Short tplType,
			Integer id, HttpServletRequest request) throws GlobalException {
		return modelService.checkModelName(id, tplType, modelName, SystemContextUtils.getSiteId(request), isGlobal);
	}

	/**
	 * 拖拽移动目标
	 * 
	 * @param sortDto
	 * @param result
	 * @return
	 * @throws GlobalException
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public ResponseInfo dragSort(@RequestBody DragSortDto sortDto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		modelService.updatePriority(sortDto);
		return new ResponseInfo();
	}

	/**
	 * 获取模型列表(tplType 模型类型)
	 *
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/list")
	@SerializeField(clazz = CmsModel.class, includes = { "id", "modelName" })
	public ResponseInfo list(HttpServletRequest request, Short tplType) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(modelService.getModelList(tplType, true, siteId));
	}

	@Autowired
	private CmsModelService modelService;
}
