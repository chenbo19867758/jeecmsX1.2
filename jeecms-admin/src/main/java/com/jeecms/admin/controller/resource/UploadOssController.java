package com.jeecms.admin.controller.resource;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.UploadOss;

/**
 * @Description:OSS controller层
 * @author: tom
 * @date: 2018年1月24日 下午15:40:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
@RequestMapping("/oss")
public class UploadOssController extends BaseController<UploadOss, Integer> {
	
	@PostConstruct
	public void init() {
		String[] queryParams = { "[name,ossName]_LIKE_String"};
		super.setQueryParams(queryParams);
	}
	
	/**
	 * 分页查询所有
	 */
	@RequestMapping(value = "/page",method=RequestMethod.GET)
	@SerializeField(clazz = UploadOss.class,excludes = {"createTime","createUser","updateTime","updateUser"})
	public ResponseInfo getPage(HttpServletRequest request, HttpServletResponse response,
			@PageableDefault(sort = { "id" }, direction = Direction.DESC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, true);
	}
	
	/**
	 * 不分页查询所有
	 */
	@RequestMapping(value = "/list")
	public ResponseInfo list(HttpServletRequest request, Paginable paginable) throws GlobalException {
		return super.getList(request, paginable, true);
	}
	
	/**
	 * 获取详情
	 */
	@RequestMapping(value = "/get",method=RequestMethod.GET)
	@SerializeField(clazz = UploadOss.class,excludes = {"createTime","createUser","updateTime","updateUser"})
	@Override
	public ResponseInfo get(Integer id) throws GlobalException {
		return super.get(id);
	}
	
	/**
	 * 添加
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	@Override
	public ResponseInfo save(@RequestBody UploadOss oss, BindingResult result) throws GlobalException {
		super.save(oss, result);
		return new ResponseInfo();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	@Override
	public ResponseInfo update(@RequestBody UploadOss oss, BindingResult result) throws GlobalException {
		super.update(oss, result);
		return new ResponseInfo();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete",method=RequestMethod.POST)
	public ResponseInfo update(@RequestBody @Valid  DeleteDto dels) throws GlobalException {
		super.physicalDelete(dels.getIds());
		return new ResponseInfo();
	}

}
