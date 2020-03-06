/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysSearchWord;
import com.jeecms.system.service.SysSearchWordService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 搜索词控制层
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/16 17:38
 */

@RestController
@RequestMapping("/searchWord")
public class SearchWordController {

	@Autowired
	private SysSearchWordService service;

	/**
	 * 搜索词列表
	 *
	 * @param count   搜索词数量
	 * @param request {@link HttpServletRequest}
	 * @return 搜索词列表
	 */
	@RequestMapping("/list")
	@SerializeField(clazz = SysSearchWord.class, includes = {"word"})
	public ResponseInfo list(Integer count, Integer orderBy, HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Paginable paginable = new PaginableRequest(0, count);
		orderBy = orderBy == null ? SysSearchWord.ODER_BY_SORT_NUM_DESC : orderBy;
		List<SysSearchWord> searchWords = service.getList(siteId, orderBy, paginable);
		return new ResponseInfo(searchWords);
	}
}
