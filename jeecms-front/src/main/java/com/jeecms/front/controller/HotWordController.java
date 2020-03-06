/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysHotWord;
import com.jeecms.system.service.SysHotWordService;
import com.jeecms.util.SystemContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 热词控制器
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/17 11:00
 */

@RestController
@RequestMapping("/hotWord")
public class HotWordController {

	private static final Logger logger = LoggerFactory.getLogger(HotWordController.class);

	@Autowired
	private SysHotWordService hotWordService;

	@GetMapping("/list")
	public ResponseInfo list(HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		HashMap<String, String[]> params = new HashMap<String, String[]>(1);
		params.put("EQ_siteId_Integer", new String[]{siteId.toString()});
		List<SysHotWord> list = hotWordService.getList(params, null, false);
		return new ResponseInfo(list);
	}

	@GetMapping("/click")
	public ResponseInfo click(Integer id, HttpServletRequest request) {
		try {
			hotWordService.click(SystemContextUtils.getSiteId(request), id);
		} catch (GlobalException e) {
			logger.error(e.getMessage());
		}
		return new ResponseInfo();
	}
}
