/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysHotWordCategory;
import com.jeecms.system.domain.dto.HotWordCategoryDto;
import com.jeecms.system.service.SysHotWordCategoryService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 热词分类Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-28
 */
@RequestMapping("/hotWordCategorys")
@RestController
public class SysHotWordCategoryController extends BaseAdminController<SysHotWordCategory, Integer> {

	@Autowired
	private SysHotWordCategoryService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}


	/**
	 * @Title: 列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/page")
	@SerializeField(clazz = SysHotWordCategory.class, includes = {"id", "cateName", "applyScope", "range"})
	public ResponseInfo page(HttpServletRequest request,
							 @PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(2);
		String cateName = request.getParameter("cateName");
		if (SystemContextUtils.getSiteId(request) != null) {
			params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
		}
		params.put("LIKE_cateName_String", new String[]{cateName});
		return new ResponseInfo(service.getPage(params, pageable, false));
	}

	/**
	 * @Title: 列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/list")
	@SerializeField(clazz = SysHotWordCategory.class, includes = {"id", "cateName"})
	public ResponseInfo list(HttpServletRequest request) throws GlobalException {
		return super.getList(request, null, false);
	}

	/**
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@MoreSerializeField({@SerializeField(clazz = Channel.class, includes = {"id"}), @SerializeField(clazz =
			SysHotWordCategory.class, includes = {"id", "cateName", "applyScope", "channels"})})
	@Override
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * @Title: 添加
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	public ResponseInfo save(@RequestBody @Valid HotWordCategoryDto dto,
							 HttpServletRequest request, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		//判断热词分类名是否存在
		if (!service.checkByCateName(dto.getCateName(), null)) {
			return new ResponseInfo(SysOtherErrorCodeEnum.HOT_WORD_CATEGORY_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.HOT_WORD_CATEGORY_ALREADY_EXIST.getDefaultMessage());
		}
		service.save(dto, siteId);
		return new ResponseInfo();
	}

	/**
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	public ResponseInfo update(@RequestBody @Valid HotWordCategoryDto dto,
							   BindingResult result) throws GlobalException {
		validateId(dto.getId());
		validateBindingResult(result);
		//判断热词分类名是否存在
		if (!service.checkByCateName(dto.getCateName(), dto.getId())) {
			return new ResponseInfo(SysOtherErrorCodeEnum.HOT_WORD_CATEGORY_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.HOT_WORD_CATEGORY_ALREADY_EXIST.getDefaultMessage());
		}
		service.update(dto);
		return new ResponseInfo();
	}

	/**
	 * 校验是否重名
	 *
	 * @param cateName 热词分类名称
	 * @param id       热词分类id
	 * @return true 可用 false 不可用
	 */
	@GetMapping("/cateName/unique")
	public ResponseInfo unique(String cateName, Integer id) {
		return new ResponseInfo(service.checkByCateName(cateName, id));
	}

	/**
	 * @Title: 删除
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		return super.physicalDelete(dels.getIds());
	}
}



