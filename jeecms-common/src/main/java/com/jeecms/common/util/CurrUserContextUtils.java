package com.jeecms.common.util;

import com.jeecms.common.base.domain.IBaseUser;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.local.UserThreadLocal;

/**
 * 获取当前用户名
 * @author: tom
 * @date: 2018年7月11日 下午3:14:39
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CurrUserContextUtils {
	public static String getCurrentUsername() {
		IBaseUser user = UserThreadLocal.getUser();
		if (user != null) {
			return user.getUsername();
		}
		return WebConstants.ANONYMOUSUSER;
	}
}
