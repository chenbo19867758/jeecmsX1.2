/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.vo.ModelItemDto;
import com.jeecms.content.service.CmsModelItemService;

/**   
 * 模型字段控制层
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年4月24日 上午11:44:33     
 */
@RestController
@RequestMapping("/modelItem")
public class CmsModelItemController extends BaseController<CmsModelItem, Integer>{

	/**
	 * 添加模型字段
	 * @Title: saveCmsModelItem  
	 * @param modelItemMap  
	 * @return
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@PostMapping
	public ResponseInfo saveCmsModelItem(@RequestBody ModelItemDto modelItemDto,BindingResult result) throws GlobalException {
		validateBindingResult(result);
		return cmsModelItemService.saveCmsModelItem(modelItemDto);
	}
	
	
	@Autowired
	private CmsModelItemService cmsModelItemService;
	
}
