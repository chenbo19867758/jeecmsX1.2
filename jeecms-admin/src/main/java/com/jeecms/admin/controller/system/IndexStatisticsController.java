/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.WorkflowConstant;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentGetService;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.FlowService;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.system.domain.vo.IndexVo;
import com.jeecms.system.domain.vo.UpcomingVo;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页数据统计 extends BaseAuditController
 * 
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/12 14:07
 */
@RestController
@RequestMapping("/index")
public class IndexStatisticsController {

	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentGetService contentGetService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private CoreUserService userService;
	@Autowired
	private UserCommentService userCommentService;

	/**
	 * 首页今日统计和累计统计
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/statistics")
	public ResponseInfo contentNum(HttpServletRequest request) {
		Date now = Calendar.getInstance().getTime();
		Date begin = MyDateUtils.getStartDate(now);
		Date end = MyDateUtils.getFinallyDate(now);
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 今日内容发布数
		long toadyContent = contentGetService.getReleaseSum(begin, end, siteId);
		// 累计内容发布数
		long totalContent = contentGetService.getReleaseSum(null, null, siteId);
		// 今日评论数
		long todayComment = userCommentService.getCount(begin, end, siteId, null);
		// 累计评论数
		long totalComment = userCommentService.getCount(null, null, siteId, null);
		// 今日新增会员数
		long todayUser = userService.getNewUserSum(begin, end, siteId);
		// 累计新增会员数
		long totalUser = userService.getNewUserSum(null, null, siteId);
		// 今日投稿数
		long todaySubmission = contentGetService.getSubmissionSum(begin, end, siteId);
		// 累计投稿数
		long totalSubmission = contentGetService.getSubmissionSum(null, null, siteId);
		IndexVo vo = new IndexVo(toadyContent, totalContent, todayComment, totalComment, todayUser, totalUser,
				todaySubmission, totalSubmission);
		return new ResponseInfo(vo);
	}

	/**
	 * 待办事项
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	@GetMapping("/upcoming")
	public ResponseInfo upcoming(HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 待审会员数
		long totalUser = userService.getUserSum(null, null, siteId);
		// 获取待审评论数量
		long totalComment = userCommentService.getCount(null, null, siteId, UserComment.CHECK_WAIT);
		// 获取待办内容数量
		long toDealCount = flowService.getToDealCount(WorkflowConstant.WORKFLOW_DATA_TYPE_CONTENT);
		return new ResponseInfo(new UpcomingVo(totalUser, totalComment, toDealCount));
	}

	/**
	 * 获取最新发布内容
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return 内容列表
	 */
	@MoreSerializeField({
			@SerializeField(clazz = Content.class, includes = { "id", "title", "channel", "releaseTime" }),
			@SerializeField(clazz = Channel.class, includes = { "name" }) })
	public ResponseInfo release(HttpServletRequest request,
			@PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Map<String, String[]> params = new HashMap<String, String[]>(2);
		params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
		params.put("EQ_status_Integer", new String[] { String.valueOf(ContentConstant.STATUS_PUBLISH) });
		Page<Content> page = contentService.getPage(params, pageable, false);
		return new ResponseInfo(page);
	}

	/**
	 * 栏目访问量
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return ResponseInfo
	 */
	public ResponseInfo channelViews(HttpServletRequest request) {
		List<Channel> list = channelService.findList(SystemContextUtils.getSiteId(request), true);
		// 筛选出低栏目
		List<Channel> channels = list.parallelStream().filter(channel -> channel.getChilds() != null)
				.filter(channel -> channel.getChilds().size() > 0).collect(Collectors.toList());
		Integer[] channelIds = new Integer[channels.size()];
		for (int i = 0; i < channels.size(); i++) {
			channelIds[i] = channels.get(i).getId();
		}
		// 根据栏目id获取到内容
		List<Content> contents = contentService.findByChannels(channelIds);
		// 根据栏目id进行分组
		Map<Integer, List<Content>> map = contents.parallelStream()
				.collect(Collectors.groupingBy(Content::getChannelId));
		Map<String, Integer> views = new HashMap<String, Integer>(map.size());
		for (List<Content> contentList : map.values()) {
			// 统计该栏目下访问量
			int sum = contentList.parallelStream().mapToInt(Content::getViews).sum();
			views.put(contentList.get(0).getChannel().getName(), sum);
		}
		// 这里将map.entrySet()转换成list
		List<Map.Entry<String, Integer>> lists = new ArrayList<Map.Entry<String, Integer>>(views.entrySet());
		// 然后通过比较器来实现排序
		Collections.sort(lists, new Comparator<Map.Entry<String, Integer>>() {
			// 升序排序
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		return new ResponseInfo(views);
	}


	@Autowired
	FlowService flowService;
}
