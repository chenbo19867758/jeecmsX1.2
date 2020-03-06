/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.controller;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.util.SystemContextUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的消息Controller
 * 
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/25
 */

@RestController
@RequestMapping("/messages")
public class MessageController extends BaseController<SysMessage, Integer> {

	private Map<String, String[]> params = new HashMap<String, String[]>();

	@GetMapping("/page")
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable)
					throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		// 正常状态的消息
		params.put("EQ_status_Integer", new String[] { String.valueOf(SysMessage.MESSAGE_STATUS_NORMAL) });
		// 当前用户
		params.put("EQ_memberId_Integer", new String[] { String.valueOf(userId) });
		return super.getPage(request, pageable, false);
	}
}
