package com.jeecms.resource.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MimeType {

	public static final Map<String, String> TYPES = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("image/gif", ".gif");
			put("image/jpeg", ".jpg");
			put("image/jpg", ".jpg");
			put("image/png", ".png");
			put("image/bmp", ".bmp");
		}
	};

	public static String getSuffix(String mime) {
		return MimeType.TYPES.get(mime);
	}

}
