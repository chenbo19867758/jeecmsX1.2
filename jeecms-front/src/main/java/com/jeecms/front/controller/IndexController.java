/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.ChastityUtil;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentService;
import com.jeecms.statistics.service.SiteFlowCacheService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页数据
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/15 15:26
 */

@RestController
@RequestMapping("/index")
public class IndexController {

	@Autowired
	private ChannelService channelService;
	@Autowired
	private ContentService contentService;

	/**
	 * 获取栏目导航栏
	 *
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/navigationBar")
	public ResponseInfo navigationBar(HttpServletRequest request) {
		return new ResponseInfo(channelService.findTopList(SystemContextUtils.getSiteId(request), false));
	}

	/**
	 * 内容列表（根据栏目分类）
	 *
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/contentList")
	public ResponseInfo contentList(HttpServletRequest request) {
		List<Channel> channels = channelService.findTopList(SystemContextUtils.getSiteId(request), false);
		Integer[] channelIds = new Integer[channels.size()];
		for (int i = 0; i < channels.size(); i++) {
			channelIds[i] = channels.get(0).getId();
		}
		List<Content> contents = contentService.findByChannels(channelIds);
		Map<Channel, List<Content>> map =
				contents.parallelStream().collect(Collectors.groupingBy(Content::getChannel));
		Map<String, List<Content>> listMap = new HashMap<String, List<Content>>(map.size());
		for (Channel channel : map.keySet()) {
			listMap.put(channel.getName(), map.get(channel));
		}
		return new ResponseInfo(listMap);
	}

	/**
	 * 热点新闻
	 *
	 * @param pageable {@link Pageable}
	 * @param request  {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/breakingNews")
	public ResponseInfo breakingNews(@PageableDefault(sort = "views",
			direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
		Map<String, String[]> params = new HashMap<String, String[]>(1);
		params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
		contentService.getPage(params, pageable, false);
		return new ResponseInfo();
	}

	/**
	 * 内容分页
	 *
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 */
	@GetMapping("/content")
	public ResponseInfo content(Integer count, HttpServletRequest request) {
		Map<String, String[]> params = new HashMap<String, String[]>(1);
		Integer siteId = SystemContextUtils.getSiteId(request);
		params.put("EQ_siteId_Integer", new String[]{siteId.toString()});
		params.put("EQ_typeId_Integer", new String[]{siteId.toString()});
		List<Sort.Order> orders = new ArrayList<Sort.Order>();
		//排序
		orders.add(new Sort.Order(Sort.Direction.ASC, "sortNum"));
		Pageable pageable = PageRequest.of(1, count == null ? 0 : count, Sort.by(orders));
		contentService.getList(params, null, false);
		return new ResponseInfo();
	}

	/**
	 * 统计pv，uv，ip
	 *
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/flow")
	public ResponseInfo flow(HttpServletRequest request) throws GlobalException {
		return new ResponseInfo(flowCache.flow(request, null, null));
	}

	public static class UserBean {
		private String userId;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId == null ? null : userId.trim();
		}
	}

	@Autowired
	private SiteFlowCacheService flowCache;
	@Autowired
	private ChastityUtil chastityUtil;

}
