/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.constants;

/**
 * 问卷常量
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/17 15:57
 */
public class QuestionnaireConstant {

	//题目类型
	/**
	 * 单选
	 */
	public static Short SUBJECT_TYPE_RADIO = 1;
	/**
	 * 多选
	 */
	public static Short SUBJECT_TYPE_CHECKBOX = 2;
	/**
	 * 问答题
	 */
	public static Short SUBJECT_TYPE_QUESTIONS_AND_ANSWERS = 3;
	/**
	 * 下拉
	 */
	public static Short SUBJECT_TYPE_SELECT = 4;
	/**
	 * 级联
	 */
	public static Short SUBJECT_TYPE_CASCADE = 5;
	/**
	 * 附件
	 */
	public static Short SUBJECT_TYPE_FILE = 6;

	/**
	 * 未发布
	 */
	public static Integer STATUS_NO_REVIEW = 0;
	/**
	 * 流转中
	 */
	public static Integer STATUS_IN_CIRCULATION = 1;
	/**
	 * 已驳回
	 */
	public static Integer STATUS_DISMISSED = 2;
	/**
	 * 进行中
	 */
	public static Integer STATUS_PROCESSING = 3;
	/**
	 * 已结束
	 */
	public static Integer STATUS_OVER = 4;

	/**
	 * 数字正则
	 */
	public static final String REG_NUMBER = "^(-?\\d+)(\\.\\d+)?";

	/**
	 * 字母正则
	 */
	public static final String REG_ALPHABET = "^[A-Za-z]+$";


	/**
	 * 中文正则
	 */
	public static final String REG_CHINESE = "^[\\u4e00-\\u9fa5]+$";

	/**
	 * 邮箱正则
	 */
	public static final String REG_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

	/**
	 * 手机号正则
	 */
	public static final String REG_MOBILE = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

	/**
	 * 数字正则
	 */
	public static final Integer REG_NUMBER_TYPE = 5;

	/**
	 * 字母正则
	 */
	public static final Integer REG_ALPHABET_TYPE = 2;

	/**
	 * 中文正则
	 */
	public static final Integer REG_CHINESE_TYPE = 1;

	/**
	 * 邮箱正则
	 */
	public static final Integer REG_EMAIL_TYPE = 7;

	/**
	 * 手机号正则
	 */
	public static final Integer REG_MOBILE_TYPE = 8;

	/**
	 * 只能
	 */
	public static final Short LIMIT_UNIT_ONLY = 1;
	/**
	 * 每小时
	 */
	public static final Short LIMIT_UNIT_HOUR = 2;
	/**
	 * 每天
	 */
	public static final Short LIMIT_UNIT_DAY = 3;

	/**
	 * 发布时间降序
	 */
	public static final Integer PUBLISH_DESC = 1;
	/**
	 * 发布时间升序
	 */
	public static final Integer PUBLISH_ASC = 2;
	/**
	 * 创建时间降序
	 */
	public static final Integer CREATE_TIME_DESC = 3;
	/**
	 * 创建时间升序
	 */
	public static final Integer CREATE_TIME_ASC = 4;
	/**
	 * 结束时间降序(优先有结束时间的，没有设置结束时间的按发布时间)
	 */
	public static final Integer END_TIME_DESC = 5;
	/**
	 * 结束时间升序(优先有结束时间的，没有设置结束时间的按发布时间)
	 */
	public static final Integer END_TIME_ASC = 6;

	public static String getReg(Integer type) {
		/*['',
				/^[\u4e00-\u9fa5]{0,}$/,
				/^[A-Za-z]+$/,
				/^[0-9]*$/,
				/^[A-Za-z0-9]+/,
				/^[1-9]\d*$/,
				/^[\u4E00-\u9FA5A-Za-z0-9]+$/,
				/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
				/^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/,
				/^(\(\d{3,4}-)|\d{3.4}-)?\d{7,8}$/,
				/^((13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}||(\(\d{3,4}-)|\d{3.4}-)?\d{7,8})$/,
				/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/,
				/[1-9]\d{5}(?!\d)/
				]
*/
		if (type == null) {
			return null;
		} else if (REG_NUMBER_TYPE.equals(type)) {
			return REG_NUMBER;
		} else if (REG_ALPHABET_TYPE.equals(type)) {
			return REG_ALPHABET;
		} else if (REG_CHINESE_TYPE.equals(type)) {
			return REG_CHINESE;
		} else if (REG_EMAIL_TYPE.equals(type)) {
			return REG_EMAIL;
		} else if (REG_MOBILE_TYPE.equals(type)) {
			return REG_MOBILE;
		} else {
			return "";
		}
	}
}
