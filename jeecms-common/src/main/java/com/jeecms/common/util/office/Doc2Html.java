package com.jeecms.common.util.office;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * HTML工具类
 * 
 * @author ljw
 * 
 */
public class Doc2Html {

	/**
	 * 清除格式style
	 * 
	 * @param htmlStr 带有格式的html,只保留body里面的内容
	 * @return 去除了不需要格式的html
	 */
	public static String clearStyle(String htmlStr) {
		Whitelist wl = Whitelist.basic(); 
		//保留以下标签
		//图片标签
		wl.addAttributes("img", "src");
		wl.addTags("div");
		//a链接
		wl.addAttributes("a", "href");
		htmlStr = Jsoup.clean(htmlStr, wl);
		return htmlStr;
	}

	/**
	 * 清除标签，只保留文字
	 * @param htmlStr 带有格式的html
	 * @return 只保留文字
	 */
	public static String onlyWords(String htmlStr) {
		return Jsoup.parse(htmlStr).text(); 
	}

}