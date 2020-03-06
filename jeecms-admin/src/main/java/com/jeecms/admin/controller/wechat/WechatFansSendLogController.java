/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.wechat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.WechatFansExt;
import com.jeecms.wechat.domain.WechatFansSendLog;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatFansSendLogService;
import com.sun.istack.NotNull;

/**
 * 消息管理控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-05-28
 */
@RequestMapping("/wechatfanssendlog")
@RestController
public class WechatFansSendLogController extends BaseController<WechatFansSendLog, Integer> {

	@Autowired
	private WechatFansSendLogService wechatFansSendLogService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;

	/**
	 * 消息记录列表分页
	 * 
	 * @Title: page
	 * @param request   请求
	 * @param pageable  分页
	 * @param appId     公众号APPID
	 * @param startTime 开始时间
	 * @param black     是否屏蔽黑名单
	 * @param endTime   结束时间
	 * @param title     内容
	 * @return ResponseInfo 返回
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/page")
	@MoreSerializeField({
			@SerializeField(clazz = WechatFansSendLog.class, includes = { "id", "reply", "msgType", "fans", "madia",
					"createTime", "openId", "createUser" }),
			@SerializeField(clazz = WechatFans.class, includes = { "id", "username", "subscribeTime", "subscribeTimes",
					"headimgurl", "nickname", "openid", "fansExt", "sex", "tagidList", }),
			@SerializeField(clazz = WechatFansExt.class, includes = { "commentCount", "topCommentCount",
					"mesCount" }), })
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, @NotNull String appId,
			Date startTime, Boolean black, String openId, Date endTime, String title) throws GlobalException {
		Date startDate = null;
		Date endDate = null;
		// 如果时间不为空
		if (startTime != null || endTime != null) {
			startDate = MyDateUtils.getStartDate(startTime);
			endDate = MyDateUtils.getFinallyDate(endTime);
		}
		Page<WechatFansSendLog> logs = wechatFansSendLogService.getLogPage(Arrays.asList(appId), null, startDate,
				endDate, title, black, openId, pageable);
		return new ResponseInfo(logs);
	}

	/**
	 * 消息记录汇总
	 * 
	 * @Description: 完成
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/collect")
	@MoreSerializeField({
			@SerializeField(clazz = WechatFansSendLog.class, includes = { "id", "reply", "msgType", "fans", "madia",
					"createTime", "sendStatus" }),
			@SerializeField(clazz = WechatFans.class, includes = { "nickname", "headimgurl" }), })
	public ResponseInfo collect(HttpServletRequest request,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, String appId, Date startTime,
			Boolean black, Date endTime, String title) throws GlobalException {
		Date startDate = null;
		Date endDate = null;
		// 如果时间不为空
		if (startTime != null || endTime != null) {
			startDate = MyDateUtils.getStartDate(startTime);
			endDate = MyDateUtils.getStartDate(endTime);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> appids = new ArrayList<String>(10);
		if (StringUtils.isNotBlank(appId)) {
			appids.add(appId);
		} else {
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			}
		}
		// 站点没有APPID，直接返回
		if (appids.isEmpty()) {
			return new ResponseInfo();
		}
		Page<WechatFansSendLog> logs = wechatFansSendLogService.getLogPage(appids, WechatConstants.SEND_TYPE_FANS,
				startDate, endDate, title, black, null, pageable);
		return new ResponseInfo(logs);
	}

	/**
	 * 手动刷新消息,得到最新的消息数据
	 * 
	 * @Title: delete
	 * @param messageId 最新的messageId
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping()
	@MoreSerializeField({
			@SerializeField(clazz = WechatFansSendLog.class, includes = { "id", "reply", "msgType", "fans", "madia",
					"createTime" }),
			@SerializeField(clazz = WechatFans.class, includes = { "nickname", "headimgurl" }), })
	public ResponseInfo refresh(HttpServletRequest request, Integer messageId, String appId) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> appids = new ArrayList<String>(10);
		if (StringUtils.isNotBlank(appId)) {
			appids.add(appId);
		} else {
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			}
		}
		// 站点没有APPID，直接返回
		if (appids.isEmpty()) {
			return new ResponseInfo();
		}
		List<WechatFansSendLog> logList = new ArrayList<WechatFansSendLog>(10);
		List<WechatFansSendLog> logs = wechatFansSendLogService.getList(appids, WechatConstants.SEND_TYPE_FANS, null);
		// 不等于空，且大于给的消息ID最大值，给最新的信息
		for (WechatFansSendLog wechatFansSendLog : logs) {
			if (wechatFansSendLog.getId() > messageId) {
				logList.add(wechatFansSendLog);
			}
		}
		return new ResponseInfo(logList);
	}

	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @Description: 完成
	 * @param: dels 删除DTO
	 * @return: ResponseInfo 响应
	 */
	@DeleteMapping
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		return super.deleteBeatch(dels.getIds());
	}

}
