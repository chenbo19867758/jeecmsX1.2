package com.jeecms.content.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * 内容审核常量
 * @author: chenming
 * @date:   2020年1月15日 下午3:23:00
 */
public class ContentReviewConstant {
	
	/**
	 * 请求域名
	 * @author: chenming
	 * @date:   2020年1月16日 上午10:12:55
	 */
	public interface DoMain {
		String PLATFORM_URL = "http://api.jeecms.com";
	}
	
	/**
	 * 校验图片的参数
	 * @author: chenming
	 * @date:   2020年1月16日 上午10:13:14
	 */
	public interface CheckImg {
		// 图片格式
		final List<String> IMG_FORMAT = Arrays.asList(".png", ".jpg", ".jpeg", ".bmp", ".gif");
		// 图片的最大大小
		int MAX_SIZE = 4096;
		// 图片最长边长
		int MAX_LONG = 4096;
		// 图片最短边长
		int MIN_LONG = 128;
	}
	
	public static final String SEND_REQUEST_HEADER = "appId";
	
	public interface SendPlatformRequest {
		String CONTENT_MARK = "contentMark";
		String PHONE = "phone";
		String CONTENT_VALUE = "contentValue";
		String CONTENT_MARKS = "contentMarks";
	}
	
	public interface SendPlatformResponse {
		String CODE = "code";
		String DATA = "data";
		Integer CODE_VALUE_SUCCESS = 200;
		Integer CODE_VALUE_ERROR = -1;
		
	}
	
	public interface GetRecodeResponse {
		String CODE = "code";
		String CODE_VALUE_SUCCESS = "200";
		String DATA = "data";
		String PROCESS_CONTENT_MARKS = "processContentMarks";
		String RESULTS = "results";
		String CONTENT_MARK = "contentMark";
		String LIST = "list";
		String FAIL_NAME = "name";
		String FAIL_TEXT = "failText";
		String IMGS = "img";
	}
	
	/** 百度规定的图片格式*/
	public static final List<String> IMG_FORMAT = Arrays.asList(".png", ".jpg", ".jpeg", ".bmp", ".gif");
	
	
	/** 校验图片URL的正则表达式 */
	public static final String CHECK_IMG_URL_REGULAR = "^((https|http)?://)";
	/** 正文取出 文本 key */
	public static final String CONTENT_TXT = "txt";
	/** 正文取出 图片集合 key */
	public static final String CONTENT_IMG = "imgs";
	/** 多图资源中 资源id 标识符 */
	public static final String MANY_IMAGE_RESOURCE_ID = "resId";
	/** 多图资源中 图片资源URL key */
	public static final String MANY_IMAGE_URL = "imgUrl";

	/** 内容审核-内容文本标识 */
	public static final String REVIEW_VALUE_TXT = "txt";
	/** 内容审核-内容图片标识 */
	public static final String REVIEW_VALUE_IMG = "img";
	/** 内容审核-该内容模型字段标识 */
	public static final String REVIEW_VALUE_FIELD = "name";
	/** 内容审核-该内容资源key */
	public static final String REVIEW_VALUE_NAME = "contentValue";
	
	public static final String CONTENT_REVIEW_CACHE = "contentReviewCache";
	
	public static final String GET_PHONE_REQUEST_KEY = "productAppId";
	
	public static final String GET_PHONE_OBTAIN_KEY = "mobile";
	
	public static final String REVIEW_HEADER = "header";
	
	/** 内容审核-资源APPID */
	public static final String REVIEW_APPID = "appId";
	/** 内容审核-用户电话号码 */
	public static final String REVIEW_PHONE = "phone";
	/** 内容审核-本地内容标识 */
	public static final String REVIEW_CONTENT_MARK = "contentMark";

	/** 请求状态错误提示详情*/
	public static final Map<Integer, String> STATUS_ERROR_DATA = ImmutableMap.of(1, "云平台请求错误", 2, "缺少余量", 3, "图片格式不正确",
			4, "参数错误");
	
	public static final Map<Integer, Integer> STATUS_CHANGE_STATUS = ImmutableMap.of(0,1,1, 3, 2, 3, 3, 4, 4, 4);
}
