/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysSecret;
import com.jeecms.system.service.SysSecretService;
import org.hibernate.validator.constraints.Range;
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

/**
 * 内容/附件密级Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-25
 */
@RequestMapping("/secrets")
@RestController
public class SysSecretController extends BaseController<SysSecret, Integer> {

	@Autowired
	private SysSecretService sysSecretService;

	@PostConstruct
	public void init() {
		String[] queryParams = {"secretType_EQ_Integer"};
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
	@SerializeField(clazz = SysSecret.class, includes = {"id", "name"})
	public ResponseInfo page(HttpServletRequest request,
							 @PageableDefault(sort = "sortNum", direction = Direction.ASC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, true);
	}

	/**
	 * 内容/附件列表
	 *
	 * @param request   HttpServletRequest
	 * @param paginable Paginable
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/list")
	@SerializeField(clazz = SysSecret.class, includes = {"id", "name"})
	public ResponseInfo list(HttpServletRequest request, Paginable paginable) throws GlobalException {
		return super.getList(request, paginable, true);
	}

	/**
	 * @Title: 获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@SerializeField(clazz = SysSecret.class, includes = {"id", "name"})
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
	@Override
	public ResponseInfo save(@RequestBody @Valid SysSecret sysSecret, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		if (!sysSecretService.checkByName(sysSecret.getName(), null, sysSecret.getSecretType())) {
			if (SysSecret.CONTENT_SECRET.equals(sysSecret.getSecretType())) {
				return new ResponseInfo(SysOtherErrorCodeEnum.CONTENT_SECRET_EXIST_ERROR.getCode(),
						SysOtherErrorCodeEnum.CONTENT_SECRET_EXIST_ERROR);
			} else {
				return new ResponseInfo(SysOtherErrorCodeEnum.FILE_SECRET_EXIST_ERROR.getCode(),
						SysOtherErrorCodeEnum.FILE_SECRET_EXIST_ERROR);
			}
		}
		return super.save(sysSecret, result);
	}

	/**
	 * 校验密级名称是否可用
	 *
	 * @param name       密级名称
	 * @param id         密级id
	 * @param secretType 密级类型
	 * @return ResponseInfo
	 */
	@GetMapping("/name/unique")
	public ResponseInfo checkByName(String name, Integer id, @Range(min = 1, max = 2,
			message = "类型只有1或者2") Integer secretType) {
		boolean flag = sysSecretService.checkByName(name, id, secretType);
		return new ResponseInfo(flag);
	}

	/**
	 * @Title: 修改
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	@Override
	public ResponseInfo update(@RequestBody @Valid SysSecret sysSecret,
							   BindingResult result) throws GlobalException {
		validateId(sysSecret.getId());
		validateBindingResult(result);
		SysSecret bean = service.findById(sysSecret.getId());
		if (!sysSecretService.checkByName(sysSecret.getName(), sysSecret.getId(), bean.getSecretType())) {
			if (SysSecret.CONTENT_SECRET.equals(sysSecret.getSecretType())) {
				return new ResponseInfo(SysOtherErrorCodeEnum.CONTENT_SECRET_EXIST_ERROR.getCode(),
						SysOtherErrorCodeEnum.CONTENT_SECRET_EXIST_ERROR);
			} else {
				return new ResponseInfo(SysOtherErrorCodeEnum.FILE_SECRET_EXIST_ERROR.getCode(),
						SysOtherErrorCodeEnum.FILE_SECRET_EXIST_ERROR);
			}
		}
		return super.update(sysSecret, result);
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



