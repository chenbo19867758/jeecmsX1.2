/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.util.FrontUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/9/19 11:01
 */

@Controller
public class RssController {

	private static final String RSS_TPL = "rss";

	@RequestMapping(value = "/rss", method = RequestMethod.GET)
	public String rss(HttpServletRequest request, HttpServletResponse response,
					  ModelMap model) {
		response.setContentType("text/xml;charset=UTF-8");
		FrontUtils.frontData(request, model);
		return FrontUtils.getTplPath(request, RSS_TPL);
	}
}
