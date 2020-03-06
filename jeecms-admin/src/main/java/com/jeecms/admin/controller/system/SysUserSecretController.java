/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysSecret;
import com.jeecms.system.domain.SysUserSecret;
import com.jeecms.system.domain.dto.UserSecretDto;
import com.jeecms.system.service.SysUserSecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 人员密级Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-25
 */
@RequestMapping("/userSecrets")
@RestController
public class SysUserSecretController extends BaseController<SysUserSecret, Integer> {

	@Autowired
	private SysUserSecretService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}


	/**
	 * @Title: 列表分页
	 * @Description: TODO
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/page")
	@SerializeField(clazz = SysUserSecret.class, includes = {"id", "name", "contentSecretNames", "annexSecretNames"})
	public ResponseInfo page(HttpServletRequest request,
							 @PageableDefault(sort = "sortNum", direction = Direction.ASC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, false);
	}

	/**
	 * 列表
	 *
	 * @param request
	 * @param paginable
	 * @return
	 * @throws GlobalException
	 */
	@GetMapping(value = "/list")
	@SerializeField(clazz = SysUserSecret.class, includes = {"id", "name"})
	public ResponseInfo list(HttpServletRequest request, Paginable paginable) throws GlobalException {
		return super.getList(request, paginable, false);
	}

	/**
	 * @Title: 获取详情
	 * @Description: TODO
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@MoreSerializeField({@SerializeField(clazz = SysSecret.class, includes = {"id", "name", "secretType"}),
			@SerializeField(clazz = SysUserSecret.class, includes = {"id", "name", "sysSecrets"})})
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
	public ResponseInfo save(@RequestBody @Valid UserSecretDto userSecretDto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.save(userSecretDto);
		return new ResponseInfo(true);
	}

	/**
	 * 校验人员密级名称是否可用
	 *
	 * @param name 密级名称
	 * @param id   密级id
	 * @return ResponseInfo
	 */
	@GetMapping("/name/unique")
	public ResponseInfo check(String name, Integer id) {
		return new ResponseInfo(service.checkByName(name, id));
	}

	/**
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	public ResponseInfo update(@RequestBody @Valid UserSecretDto userSecretDto,
							   BindingResult result) throws GlobalException {
		validateId(userSecretDto.getId());
		validateBindingResult(result);
		service.update(userSecretDto);
		return new ResponseInfo(true);
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



