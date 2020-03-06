/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.local;

import com.jeecms.common.base.domain.IBaseUser;

/**
 * 当前登录用户线程变量
 * 
 * @author: tom
 * @date: 2019年4月28日 下午2:03:29
 */
public class UserThreadLocal {
	/**
	 * 当前用户线程变量
	 */
	private static ThreadLocal<IBaseUser> userHolder = new ThreadLocal<IBaseUser>();

	public static void setUser(IBaseUser user) {
		userHolder.set(user);
	}

	/**
	 * 获取当前用户
	 * 
	 * @Title: getUser
	 * @return IBaseUser
	 */
	public static IBaseUser getUser() {
		if (userHolder != null) {
			return userHolder.get();
		}
		return null;
	}

	/**
	 * 移除当前线程变量
	 * 
	 * @Title: removeUser
	 */
	public static void removeUser() {
		if (userHolder != null) {
			userHolder.remove();
		}
	}

}
