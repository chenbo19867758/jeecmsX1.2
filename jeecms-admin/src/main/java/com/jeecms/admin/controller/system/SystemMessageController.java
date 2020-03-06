/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUserMessage;
import com.jeecms.auth.service.CoreUserMessageService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.system.domain.vo.MessageVo;
import com.jeecms.system.service.SysMessageService;
import com.jeecms.util.SystemContextUtils;

/**
 * 收件箱控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-01-23
 */
@RequestMapping("/systemmessage")
@RestController
public class SystemMessageController extends BaseController<CoreUserMessage, Integer> {

	@Autowired
	private CoreUserMessageService coreUserMessageService;
	@Autowired
	private SysMessageService sysMessageService;

	/**
	 * 收件箱（系统消息+私信）列表分页
	 * @Description:
	 * @param: status 信件状态
	 * @param: startTime 开始时间
	 * @param: endTime 结束时间
	 * @param: title 标题
	 * @param: content 内容
	 * @param: pageable 分页对象
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@GetMapping(value = "/inbox")
	@MoreSerializeField(@SerializeField(clazz = MessageVo.class, includes = { "messageId", "content", "title",
			"createTimes", "status", "coreUserMessageId", "sendUserName" }))
	public ResponseInfo inbox(HttpServletRequest request, @RequestParam("status") Boolean status,
			@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime,
			@RequestParam("title") String title, @RequestParam("content") String content,
			 @RequestParam("sendUserName") String sendUserName, Pageable pageable)
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		return coreUserMessageService.getMessagePage(pageable, userId, status, 
				startTime, endTime, title, content, sendUserName);
	}

	/**消息详情
	 * @Description: 完成
	 * @param messageId 消息ID
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@GetMapping()
	@MoreSerializeField({
			@SerializeField(clazz = SysMessage.class, includes = { "id", "title", 
					"content", "createTime", "sendUserName" }) })
	public ResponseInfo get(HttpServletRequest request, Integer messageId) throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		List<CoreUserMessage> list = coreUserMessageService.findByMessageId(messageId);
		//未读
		if (list.isEmpty()) {
			CoreUserMessage userMessage = new CoreUserMessage();
			// 未读的情况下改变状态
			userMessage.setStatus(SysMessage.MESSAGE_STATUS_NORMAL);
			userMessage.setUserId(userId);
			userMessage.setMessageId(messageId);
			coreUserMessageService.save(userMessage);
		} 
		return new ResponseInfo(sysMessageService.get(messageId));
	}

	/**
	 * 标记已读
	 * @Description: 完成
	 * @param del 传输
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@PutMapping()
	public ResponseInfo read(@RequestBody DeleteDto del) throws GlobalException {
		return coreUserMessageService.saveMessage(del.getIds());
	}

	/**
	 * 删除收件箱
	 * @Description: 完成
	 * @param: del 输出
	 * @throws GlobalException 异常
	 */
	@DeleteMapping()
	public ResponseInfo del(@RequestBody DeleteDto del) throws GlobalException {
		return coreUserMessageService.delMessage(del.getIds());
	}

	/**
	 * 收件箱（系统消息+私信）未读数量
	 * @Description:
	 * @param request 请求
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/number")
	public ResponseInfo unread(HttpServletRequest request) throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		Long messages = coreUserMessageService.unreadMessage(userId);
		JSONObject object = new JSONObject();
		object.put("unreadMessage", messages);
		return new ResponseInfo(object);
	}

}