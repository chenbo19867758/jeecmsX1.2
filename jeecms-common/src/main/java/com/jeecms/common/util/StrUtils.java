package com.jeecms.common.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.constants.WebConstants;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 * 
 * @author: tom
 * @date: 2018年12月26日 下午3:33:20
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class StrUtils {
	static final Pattern NUMBER_PATTERN = Pattern.compile("[1-9]*");

	static final Pattern NUM_PATTERN = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");

	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}

	/**
	 * 处理url url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 *            URL
	 * @return
	 */
	public static String handelUrl(String url) {
		if (url == null) {
			return null;
		}
		url = url.trim();
		if (StringUtils.isBlank(url) || url.startsWith(WebConstants.PROTOCOL_HTTP)
				|| url.startsWith(WebConstants.PROTOCOL_HTTPS)) {
			return url;
		} else {
			return "http://" + url.trim();
		}
	}

	/**
	 * 分割并且去除空格
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @param sep2
	 *            第二个分隔符
	 * @return 如果str为空，则返回null。
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			str = StringUtils.replace(str, sep2, sep);
		}
		String[] arr = StringUtils.split(str, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 *            字符串
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 把html内容转为文本
	 * 
	 * @param html
	 *            需要处理的html文本
	 * @return
	 */
	public static String trimHtml2Txt(String html) {
		// 去掉head
		html = html.replaceAll("\\<head>[\\s\\S]*?</head>(?i)", "");
		// 去掉注释
		html = html.replaceAll("\\<!--[\\s\\S]*?-->", "");
		html = html.replaceAll("\\<![\\s\\S]*?>", "");
		// 去掉样式
		html = html.replaceAll("\\<style[^>]*>[\\s\\S]*?</style>(?i)", "");
		// 去掉js
		html = html.replaceAll("\\<script[^>]*>[\\s\\S]*?</script>(?i)", "");
		// 去掉word标签
		html = html.replaceAll("\\<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");
		html = html.replaceAll("\\<xml>[\\s\\S]*?</xml>(?i)", "");
		html = html.replaceAll("\\<table>[\\s\\S]*?</table>(?i)", "");
		html = html.replaceAll("\\<html[^>]*>|<body[^>]*>|</html>|</body>(?i)", "");
		// 去掉换行
		html = html.replaceAll("\\\r\n|\n|\r", "");
		html = html.replaceAll("\\<br[^>]*>(?i)", "\r\n");
		html = html.replaceAll("\\</p>(?i)", "\r\n");
		html = html.replaceAll("\\<p>(?i)", "\r\n");
		// 图片替换特殊标记，客户端自己下载图片并替换标记
		// <img[^>]*/>
		/*
		 * String regular="<(?i)img(.*?)src=\"(.*?)>"; String
		 * img_pre="<(?i)img(.*?)src=\""; String img_sub="\"(.*?)>"; Pattern
		 * p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE); Matcher m =
		 * p.matcher(html); String src = null; String newSrc=null; while
		 * (m.find()) { src=m.group(); newSrc=src.replaceAll(img_pre,
		 * Constants.API_PLACEHOLDER_IMAGE_BEGIN) .replaceAll(img_sub,
		 * Constants.API_PLACEHOLDER_IMAGE_END).trim(); html=html.replace(src,
		 * newSrc); } String
		 * videoregular="<video(.*?)src=\"(.*?)\"(.*?)</video>"; String
		 * video_pre="<video(.*?)src=\""; String video_sub="\"(.*?)</video>";
		 * Pattern
		 * videop=Pattern.compile(videoregular,Pattern.CASE_INSENSITIVE);
		 * Matcher videom = videop.matcher(html); String videosrc = null; String
		 * videonewSrc=null; while (videom.find()) { videosrc=videom.group();
		 * videonewSrc=videosrc.replaceAll(video_pre,
		 * Constants.API_PLACEHOLDER_VIDEO_BEGIN) .replaceAll(video_sub,
		 * Constants.API_PLACEHOLDER_VIDEO_END).trim();
		 * html=html.replace(videosrc, videonewSrc); }
		 */
		// 去除分页
		html = html.replace("[NextPage][/NextPage]", "");
		html = html.replaceAll("\\<[^>]+>", "");
		// html = html.replaceAll("\\ ", " ");
		return html.trim();
	}

	/**
	 * 获取视频地址
	 * 
	 * @Title: getVideoSrc
	 * @param htmlCode
	 *            html代码
	 * @return: List
	 */
	public static List<String> getVideoSrc(String htmlCode) {
		List<String> imageSrcList = new ArrayList<String>();
		String regular = "<video(.*?)src=\"(.*?)\"(.*?)</video>";
		String videoPre = "<video(.*?)src=\"";
		String videoSub = "\"(.*?)</video>";
		Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlCode);
		String src = null;
		while (m.find()) {
			src = m.group();
			src = src.replaceAll(videoPre, "").replaceAll(videoSub, "").trim();
			imageSrcList.add(src);
		}
		return imageSrcList;
	}

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				int len256 = 256;
				if (s.codePointAt(i - 1) < len256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

	public static String htmlCut(String s, int len, String append) {
		String text = html2Text(s, len * 2);
		return textCut(text, len, append);
	}

	/**
	 * html转txt
	 * 
	 * @Title: html2Text
	 * @param html
	 *            html
	 * @param len
	 *            长度
	 * @return: String
	 */
	public static String html2Text(String html, int len) {
		try {
			Lexer lexer = new Lexer(html);
			Node node;
			StringBuilder sb = new StringBuilder(html.length());
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof TextNode) {
					sb.append(node.toHtml());
				}
				if (sb.length() > len) {
					break;
				}
			}
			return sb.toString();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getSuffix(String str) {
		int splitIndex = str.lastIndexOf(".");
		return str.substring(splitIndex + 1);
	}

	/**
	 * 补齐不足长度
	 * 
	 * @param length
	 *            长度
	 * @param number
	 *            数字
	 * @return
	 */
	public static String lpad(int length, Long number) {
		String f = "%0" + length + "d";
		return String.format(f, number);
	}

	public static boolean isGreaterZeroNumeric(String str) {
		return NUMBER_PATTERN.matcher(str).matches();
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Matcher isNum = NUM_PATTERN.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 利用正则表达式判断对象是否是数字
	 * @Title: isNumeric 
	 * @param obj Object
	 * @return: boolean
	 */
	public static boolean isNumeric(Object obj) {
		boolean isNumber = obj != null && obj instanceof String && StrUtils.isNumeric((String) obj);
		return isNumber;
	}

	/**
	 * 获取字符串数组，按逗号,分隔
	 * 
	 * @Title: getStrArray
	 * @param ids
	 *            字符串
	 * @return: String[]
	 */
	public static String[] getStrArray(String ids) {
		String[] idArrays = null;
		if (StringUtils.isNotBlank(ids)) {
			idArrays = ids.split(",");
		}
		return idArrays;
	}

	/**
	 * 集合转字符串,逗号,拼接元素
	 * 
	 * @Title: iterableToString
	 * @param list
	 *            集合
	 * @return: String
	 */
	public static <T> String iterableToString(Iterable<T> list) {
		if (list == null) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (T string : list) {
			result.append(string.toString()).append(WebConstants.ARRAY_SPT);
		}
		String str = result.toString();
		if (list.iterator().hasNext()) {
			str = str.substring(0, result.length() - 1);
		}
		return str;
	}

	/**
	 * 是否任意一个为空
	 * 
	 * @Title: isAnyEmpty
	 * @param css
	 *            元素
	 * @return: boolean
	 */
	public static boolean isAnyEmpty(final CharSequence... css) {
		if (css == null || css.length == 0) {
			return false;
		}
		for (final CharSequence cs : css) {
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 显示不可见字符的Unicode
	 * 
	 * @param input
	 *            输入字符串
	 * @return
	 */
	public static String escapeUnicode(String input) {
		if (StringUtils.isBlank(input)) {
			return " ";
		}
		StringBuilder sb = new StringBuilder(input.length());
		@SuppressWarnings("resource")
		Formatter format = new Formatter(sb);
		for (char c : input.toCharArray()) {
			if (c < 128) {
				sb.append(c);
			} else {
				format.format("\\u%04x", (int) c);
			}
		}
		return sb.toString();
	}

	/**
	 * 生成指定随机数
	 * 
	 * @Title: getCaptcha
	 * @Description:
	 * @param: @return
	 * @return: String
	 */
	public static String getRandStr(int num) {
		// 默认6位
		num = num != 0 ? num : 6;
		Random r = new SecureRandom();
		StringBuffer str = new StringBuffer();
		int i = 0;
		while (i < num) {
			str.append(r.nextInt(10));
			i++;
		}
		return str.toString();
	}

	/**
	 * 校验是否为手机号
	 * 
	 * @Title: isPhone
	 * @param phone
	 *            手机号
	 * @return: boolean
	 */
	public static boolean isPhone(String phone) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|"
				+ "(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		int phoneLen = 11;
		if (phone.length() != phoneLen) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			return m.matches();
		}
	}

	/**
	 * 校验是否为邮箱
	 * 
	 * @Title: isEmail
	 * @param string
	 *            字符串
	 * @return: boolean
	 */
	public static boolean isEmail(String string) {
		if (string == null) {
			return false;
		}
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(string);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验是否为为银行卡
	 * 
	 * @Title: isBankCard
	 * @param cardId
	 *            号码
	 * @return: boolean
	 */
	public static boolean isBankCard(String cardId) {
		int cardIdLenMin = 16;
		int cardIdLenMax = 19;
		if (cardId == null || cardId.length() < cardIdLenMin || cardId.length() > cardIdLenMax) {
			return false;
		}

		char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
		char n = 'N';
		if (bit == n) {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	private static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhnSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhnSum += k;
		}
		return (luhnSum % 10 == 0) ? '0' : (char) ((10 - luhnSum % 10) + '0');
	}

	/**
	 * 隐藏关键信息数据， 手机号：保留前三位及后四位 邮箱：保留@前面一位，@后面全部显示 银行卡：保留前四位及后四位 普通用户名：保留前一位及后一位
	 * 
	 * @Title: hideStr
	 * @param str
	 *            源字符串
	 * @return: String
	 */
	public static String hideStr(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		} else if (isPhone(str)) {
			return str.substring(0, 3) + "****" + str.substring(str.length() - 4);
		} else if (isEmail(str)) {
			int index = str.indexOf("@");
			String emailName = str.substring(0, index + 1);
			return emailName.substring(0, 1) + "****" + str.substring(index);
		} else if (isBankCard(str)) {
			return str.substring(0, 4) + "****" + str.substring(str.length() - 4);
		} else {
			return str.substring(0, 1) + "****" + str.substring(str.length() - 1);
		}
	}

	/**
	 * 请求参数字符串转换JSONObject
	 * 
	 * @Title: urlParamToJson
	 * @param parmStr
	 *            字符串
	 * @return: JSONObject
	 */
	public static JSONObject urlParamToJson(String parmStr) {
		JSONObject object = new JSONObject();
		String simple = "&";
		String eq = "=";
		if (parmStr.contains(simple)) {
			String[] uStrings = parmStr.split(simple);
			for (String string : uStrings) {
				String[] common = string.split(eq);
				object.put(common[0], common[1]);
			}
		} else {
			String[] common = parmStr.split(eq);
			object.put(common[0], common[1]);
		}
		return object;
	}

	/***
	 * 获取顶级域名 (国内域名基本上覆盖，国外域名很多未覆盖到)
	 * 
	 * @param domain
	 *            域名
	 * @return
	 */
	public static String getTopDomain(String domain) {
		String regStr = "[0-9a-zA-Z]+((\\.com.cn)|(\\.com.hk)|(\\.net.cn)|(\\.org.cn)|(\\.gov.cn)||(\\.edu.cn)(\\.com)|(\\.cn)|(\\.org)|(\\.gov)|(\\.net)|(\\.edu)|(\\.xyz)|(\\.xin)|(\\.club)|(\\.shop)|(\\.site)|(\\.wang)"
				+ "|(\\.top)|(\\.win)|(\\.online)|(\\.tech)|(\\.store)|(\\.bid)|(\\.cc)|(\\.ren)|(\\.lol)|(\\.pro)|(\\.red)|(\\.kim)|(\\.space)|(\\.link)|(\\.click)|(\\.news)|(\\.news)|(\\.ltd)|(\\.website)"
				+ "|(\\.biz)|(\\.help)|(\\.mom)|(\\.work)|(\\.date)|(\\.loan)|(\\.mobi)|(\\.live)|(\\.studio)|(\\.info)|(\\.pics)|(\\.photo)|(\\.trade)|(\\.vc)|(\\.party)|(\\.game)|(\\.rocks)|(\\.band)"
				+ "|(\\.gift)|(\\.wiki)|(\\.design)|(\\.software)|(\\.social)|(\\.lawyer)|(\\.engineer)|(\\.org)|(\\.name)|(\\.tv)|(\\.me)|(\\.asia)|(\\.co)|(\\.press)|(\\.video)|(\\.market)"
				+ "|(\\.games)|(\\.science)|(\\.中国)|(\\.公司)|(\\.网络)|(\\.pub)"
				+ "|(\\.la)|(\\.auction)|(\\.email)|(\\.sex)|(\\.sexy)|(\\.one)|(\\.host)|(\\.rent)|(\\.fans)|(\\.cn.com)|(\\.life)|(\\.cool)|(\\.run)"
				+ "|(\\.gold)|(\\.rip)|(\\.ceo)|(\\.sale)|(\\.hk)|(\\.io)|(\\.gg)|(\\.tm)|(\\.gs)|(\\.us))";
		Pattern p = Pattern.compile(regStr);
		Matcher m = p.matcher(domain);
		String topdomain = "";
		// 获取一级域名
		while (m.find()) {
			topdomain = m.group();
		}
		return topdomain;
	}

}
