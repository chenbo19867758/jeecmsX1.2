package com.jeecms;

/**
 * 版本
 * 
 * @author: tom
 * @date: 2019年1月10日 下午5:24:15
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Version {
	public static final  String MAJOR_VERSION = "x1";
	public static final  int MINOR_VERSION = 2;
	public static final  int REVISION_VERSION = 0;
	
	public static final  String MAJOR_NAME = "JEECMS";

	public static String getVersionNumber() {
		return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION + "." + Version.REVISION_VERSION;
	}
	
	public static String getVersion() {
		return Version.MAJOR_NAME;
	}
}
