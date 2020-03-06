/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.ContentType;
import com.jeecms.system.service.ContentTypeService;
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
 * 内容类型管理控制层
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年5月5日 上午10:12:11
 */
 @RequestMapping("/contentType")
@RestController
public class ContentTypeController  extends BaseController<ContentType, Integer> {

	@PostConstruct
	public void init(){
		String[] queryParams = {"typeName_LIKE"};
		super.setQueryParams(queryParams);
	}

	
	/**
	 * @Title: 列表分页   
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@RequestMapping(value="/page",method=RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = ContentType.class, includes = { "id", "typeName", "logoId", "logoResource", "createTime", "createUser"}),
		@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url"})
	})
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "createTime", direction = Direction.DESC)Pageable pageable) throws GlobalException{
		return super.getPage(request, pageable, true);
	}

	/**
	 * @Title: 获取详情   
	 * @param: @param id
	 * @param: @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@Override
	@MoreSerializeField({
		@SerializeField(clazz = ContentType.class, includes = { "id", "typeName", "logoId", "logoResource", "createTime", "createUser"}),
		@SerializeField(clazz = ResourcesSpaceData.class, includes = { "id", "url"})
	})
	public ResponseInfo get(@PathVariable("id") Integer id)throws GlobalException{				
		return super.get(id);
	}
	
	/**
	 * @Title: 添加   
	 * @param: @param result
	 * @param: @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@PostMapping
	@Override
	public ResponseInfo save(@RequestBody @Valid ContentType contentType,BindingResult result) throws GlobalException{		
		super.validateBindingResult(result);
		return contentTypeService.saveContentTypeInfo(contentType);
	}
	
	/**
	 * @Title: 修改   
	 * @param: @param result
	 * @param: @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@PutMapping
	@Override
	public ResponseInfo update(@RequestBody @Valid ContentType contentType,BindingResult result) throws GlobalException{	
		super.validateBindingResult(result);
		return contentTypeService.updateContentTypeInfo(contentType);
	}
	
	/**
	 * @Title:  删除   
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@DeleteMapping
	public ResponseInfo delete(@RequestBody @Valid  DeleteDto dels) throws GlobalException{	
		return super.deleteBeatch(dels.getIds());
	}
	
	/**
	 * 校验类型名称是否可用
	 * @Title: checkMesCode  
	 * @param typeName
	 * @return
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/typeName/unique")
	public ResponseInfo checkMesCode(@RequestParam String typeName, Integer id) throws GlobalException {
		 ContentType contentType = contentTypeService.findByTypeName(typeName);
		 if(contentType != null) {
				if(id != null &&  id.equals(contentType.getId())) {
					return new ResponseInfo(Boolean.TRUE);
				}else {
					return new ResponseInfo(Boolean.FALSE);
				}
			}
			return new ResponseInfo(Boolean.TRUE);
	}
	
	@Autowired
	private ContentTypeService  contentTypeService;
}
