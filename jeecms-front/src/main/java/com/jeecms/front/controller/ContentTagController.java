/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentExt;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.system.domain.ContentTag;
import com.jeecms.system.service.ContentTagService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容Tag词控制层
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/16 17:27
 */
@RestController
@RequestMapping(value = "/contentTag")
public class ContentTagController {

	@Autowired
	private ContentTagService tagService;
	@Autowired
	private ContentFrontService contentFrontService;

	@GetMapping("/list")
	@SerializeField(clazz = ContentTag.class, includes = {"tagName"})
	public ResponseInfo list(Integer count, HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Paginable paginable = new PaginableRequest(0, count);
		Map<String, String[]> params = new HashMap<String, String[]>(1);
		params.put("EQ_siteId_Integer", new String[]{siteId.toString()});
		List<ContentTag> tags = tagService.getList(params, paginable, false);
		return new ResponseInfo(tags);
	}

	@GetMapping("/{id}")
	@MoreSerializeField({@SerializeField(clazz = Content.class, includes = {"id", "title", "contentExt"}),
			@SerializeField(clazz = ContentExt.class, includes = {"commentsDay"})})
	public ResponseInfo get(@PathVariable("id") Integer id, HttpServletRequest request) throws GlobalException {
		List<ContentTag> tags = tagService.findAllById(Arrays.asList(1, 2));
		List<Content> list = new ArrayList<Content>();
		for (ContentTag tag : tags) {
			List<Content> contents = tag.getContentList();
			list.addAll(contents);
		}
		Pageable pageable = PageRequest.of(1, 10);
		//List<Content> contents = contentFrontService.getList(tags, ContentConstant.ORDER_TYPE_COMMENTS_DAY_DESC);
		/*for (Content content : contents) {
			List<ContentAttr> attrs = content.getContentAttrs();
			for (ContentAttr attr : attrs) {
				attr.getResourcesSpaceData().getUrl();
			}
		}*/
		list = list.parallelStream().distinct().collect(Collectors.toList());
		list.parallelStream().sorted(Comparator.comparing(o -> o.getContentExt().getCommentsDay()));
		//List<Sort.Order> orders = ContentDirectiveAbstract.order(ContentConstant.ORDER_TYPE_COMMENTS_DAY_DESC);
		//Pageable pageable = PageRequest.of(1, 100, Sort.by(orders));

		Page<Content> page1 = new PageImpl<Content>(list, pageable, list.size());
		return new ResponseInfo(page1);
	}
}
