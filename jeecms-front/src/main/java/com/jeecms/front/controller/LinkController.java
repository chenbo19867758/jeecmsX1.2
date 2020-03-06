/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.Link;
import com.jeecms.system.service.LinkService;
import com.jeecms.system.service.SysLinkTypeService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 友情链接控制层
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/17 10:16
 */

@RestController
@RequestMapping("/link")
public class LinkController {

	@Autowired
	private SysLinkTypeService linkTypeService;
	@Autowired
	private LinkService linkService;

	@GetMapping("/list")
	public ResponseInfo list(Integer linkCategoruId, HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Map<String, String[]> params = new HashMap<String, String[]>(1);
		params.put("EQ_siteId_Integer", new String[]{siteId.toString()});
		//获取站点下友情链接
		List<Link> links = linkService.getList(params, null, false);
		//根据友情链接类别分类
		Map<Integer, List<Link>> map = links.parallelStream()
				.collect(Collectors.groupingBy(Link::getLinkTypeId));
		Map<String, List<Link>> listMap = new HashMap<String, List<Link>>(map.size());
		//把map中key改为友情类别名称
		for (List<Link> list : map.values()) {
			listMap.put(list.get(0).getLinkName(), list);
		}
		return new ResponseInfo(listMap);
	}

}
