/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysHotWord;
import com.jeecms.system.domain.SysHotWordCategory;
import com.jeecms.system.domain.dto.HotWordDto;
import com.jeecms.system.service.SysHotWordService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 热词Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-28
 */
@RequestMapping("/hotWords")
@RestController
public class SysHotWordController extends BaseAdminController<SysHotWord, Integer> {

	@Autowired
	private SysHotWordService service;

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
	@SerializeField(clazz = SysHotWord.class, includes = {"id", "hotWord", "linkUrl", "isTargetBlank", "remark",
			"useCount", "clickCount", "cateName"})
	public ResponseInfo page(HttpServletRequest request, Integer hotWordCategoryId,
							 String hotWord, @PageableDefault(sort = "createTime",
			direction = Direction.DESC) Pageable pageable) throws GlobalException {
		validateId(hotWordCategoryId);
		Map<String, String[]> params = new HashMap<String, String[]>(3);
		if (SystemContextUtils.getSiteId(request) != null) {
			params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
		}
		params.put("EQ_hotWordCategoryId_Integer", new String[]{String.valueOf(hotWordCategoryId)});
		params.put("LIKE_hotWord_String", new String[]{hotWord});
		return new ResponseInfo(service.getPage(params, pageable, false));
	}

	/**
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@Override
	@GetMapping(value = "/{id}")
	@MoreSerializeField({@SerializeField(clazz = SysHotWordCategory.class, includes = {"id", "cateName"}),
			@SerializeField(clazz = SysHotWord.class, includes = {"id", "hotWord", "linkUrl", "isTargetBlank",
					"remark", "clickCount", "useCount"})})
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
	public ResponseInfo save(@RequestBody @Valid HotWordDto dto, HttpServletRequest request,
							 BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.saveBatch(dto, SystemContextUtils.getSiteId(request));
		return new ResponseInfo();
	}

	/**
	 * 唯一性校验
	 *
	 * @param hotWord 热词
	 * @param id      id
	 * @return true 唯一 false 不唯一
	 */
	@GetMapping("/unique/hotWord")
	public ResponseInfo checkByHotWord(String hotWord, Integer id) {
		return new ResponseInfo(service.checkByHotWord(hotWord, id));
	}


	/**
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	@Override
	public ResponseInfo update(@RequestBody @Valid SysHotWord sysHotWord, BindingResult result) throws GlobalException {
		if (!service.checkByHotWord(sysHotWord.getHotWord(), sysHotWord.getId())) {
			return new ResponseInfo(SysOtherErrorCodeEnum.HOT_WORD_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.HOT_WORD_ALREADY_EXIST.getDefaultMessage());
		}
		return super.update(sysHotWord, result);
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
		validateIds(dels.getIds());
		return super.physicalDelete(dels.getIds());
	}
}



