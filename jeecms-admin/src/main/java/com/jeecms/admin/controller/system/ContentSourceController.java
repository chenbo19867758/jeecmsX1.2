/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.ContentSource;
import com.jeecms.system.service.ContentSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 来源管理控制层
 *
 * @version 1.0
 * @author: wulongwei
 * @date: 2019年5月6日 下午2:08:20
 */
@RequestMapping("/sysSource")
@RestController
public class ContentSourceController extends BaseController<ContentSource, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = {"sourceName_LIKE"};
		super.setQueryParams(queryParams);
	}

	/**
	 * @Title: 来源信息列表分页
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@SerializeField(clazz = ContentSource.class, includes = {"id", "sourceName", "sourceLink", "isOpenTarget", "isDefault", "createTime", "createUser"})
	public ResponseInfo page(HttpServletRequest request,
							 @PageableDefault(sort = {"isDefault", "createTime"}, direction = Sort.Direction.DESC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, false);
	}

	/**
	 * @Title: 获取来源信息详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@Override
	@SerializeField(clazz = ContentSource.class, includes = {"id", "sourceName", "sourceLink", "isOpenTarget", "isDefault", "createTime", "createUser"})
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * @Title: 添加来源信息
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping
	@Override
	public ResponseInfo save(@RequestBody @Valid ContentSource sysSource, BindingResult result) throws GlobalException {
		return sysSourceService.saveSysSourceInfo(sysSource);
	}

	/**
	 * @Title: 修改来源信息
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping
	@Override
	public ResponseInfo update(@RequestBody @Valid ContentSource sysSource, BindingResult result) throws GlobalException {
		return sysSourceService.updateSysSourceInfo(sysSource);
	}

	/**
	 * @Title: 删除来源信息
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		return super.deleteBeatch(dels.getIds());
	}


	/**
	 * 校验来源名称是否可用
	 *
	 * @param sourceName
	 * @return
	 * @throws GlobalException
	 * @Title: checkSourceName
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/sourceName/unique")
	public ResponseInfo checkSourceName(@RequestParam String sourceName, Integer id) throws GlobalException {
		ContentSource bean = sysSourceService.findBySourceName(sourceName);
		boolean f;
		if (bean != null) {
			if (id != null) {
				f = bean.getId().equals(id);
			} else {
				f = false;
			}
		} else {
			f = true;
		}
		return new ResponseInfo(f);
	}

	@Autowired
	private ContentSourceService sysSourceService;
}



