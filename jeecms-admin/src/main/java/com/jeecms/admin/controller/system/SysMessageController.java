/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.system.domain.dto.SysMessageDto;
import com.jeecms.system.service.SysMessageService;

/**
 * 发件箱控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2018-07-02
 */
@RequestMapping("/message")
@RestController
public class SysMessageController extends BaseController<SysMessage, Integer> {

	@Autowired
	private SysMessageService sysMessageService;

	/**构造**/
	@PostConstruct
	public void init() {
		String[] queryParams = {
					// 开始时间
					"[startTime,createTime]_GTE_Timestamp",
					// 结束时间
					"[endTime,createTime]_LTE_Timestamp",
					// 用户名
					"[username,coreUser.username]_LIKE",
					// 标题
					"title_LIKE",
					// 内容
					"content_LIKE", };
		super.setQueryParams(queryParams);
	}

	/**
	 * 发件箱 @Title: outbox @param @param request @param @param
	 * pageable @param @return @param @throws GlobalException @return
	 * ResponseInfo @throws
	 */
	@GetMapping(value = "/outbox")
	@MoreSerializeField(@SerializeField(clazz = SysMessage.class, includes = { "id", "content", 
			"title", "createTimes", "username" }))
	public ResponseInfo outbox(HttpServletRequest request, 
			@PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) 
					throws GlobalException {
		Map<String, String[]> params = super.getCommonParams(request);
		// 排除删除
		params.put("EQ_status_Integer", new String[] { SysMessage.MESSAGE_STATUS_NORMAL.toString() });
		Page<SysMessage> page = sysMessageService.getPage(params, pageable, false);
		return new ResponseInfo(page);
	}

	/**
	 * 获取详情
	 * @Description: 完成
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping()
	@MoreSerializeField({
			@SerializeField(clazz = SysMessage.class, includes = { "id", "title", 
					"content", "createTime", "sendUserName", "username" }) })
	@Override
	public ResponseInfo get(Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 发送信息
	 * @Description: 完成
	 * @param dto 传输
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@PostMapping()
	public ResponseInfo save(@RequestBody SysMessageDto dto) throws GlobalException {
		// 写死接收对象为管理员
		dto.setRecTargetType(SysMessage.TARGETTYPE_APPOINT_ADMIN);
		return sysMessageService.saveMessage(dto);
	}

	/**
	 * 删除
	 * @Description: 完成
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		return sysMessageService.deleteMessage(dels.getIds());
	}

}