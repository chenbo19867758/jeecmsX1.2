package com.jeecms.message;

/**
 * 消息队列属性
 * 
 * @author: ztx
 * @date: 2018年8月6日 下午5:38:03
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MqConstants {

	/**
	 * 消息场景对应需要的基本操作标识符
	 */
	public static enum MessageSceneOperationEnum {
		/** 验证码 */
		VALIDATE_CODE(MESSAGE_SCENE_OPERATION_VALIDATE_CODE),
		/** 用户消息 */
		USER_MESSAGE(USER_SCENE_OPERATION_STORE_MESSAGE);

		private int operation;

		private MessageSceneOperationEnum(int operation) {
			this.operation = operation;
		}

		public int getOperation() {
			return operation;
		}

		public void setOperation(int operation) {
			this.operation = operation;
		}

	}
	
	/** 全部用户*/
	public static final int USER_ALL = 1;
	/** 全部管理员*/
	public static final int MANAGER_ALL = 2;
	/** 全部会员*/
	public static final int MEMBER_ALL = 3;
	/** 组织*/
	public static final int ASSIGN_ORG = 4;
	/** 指定管理员*/
	public static final int ASSIGN_MANAGER = 5;
	/** 指定会员等级*/
	public static final int ASSIGN_MANAGER_LEVEL = 6;
	/** 指定会员组*/
	public static final int ASSIGN_MEMBER_GROUP = 7;
	/** 指定会员*/
	public static final int ASSIGN_MEMBER = 8;
	
	/** 发送方式为邮件*/
	public static final short SEND_EMAIL = 1;
	/** 发送方式为短信*/
	public static final short SEND_SMS = 2;
	/** 发送方式为站内信*/
	public static final short SEND_SYSTEM_STATION = 3;
	/** 发送方式为邮件+短信*/
	public static final short SEND_EMAIL_SMS = 4;
	/** 发送方式为短信+邮件+站内信*/
	public static final short SEND_ALL = 5;
	/** 发送方式站内信+邮件*/
	public static final short SEND_SYSTEM_STATION_EMAIL = 6;
	/** 发送方式站内信+短信*/
	public static final short SEND_SYSTEM_STATION_SMS = 7;
	
	/*
	 * 线程消息任务操作标识符
	 */

	/** 5.验证码 */
	public static final int MESSAGE_SCENE_OPERATION_VALIDATE_CODE = MqConstants.MESSAGE_QUEUE_EMAIL
			| MqConstants.MESSAGE_QUEUE_SMS;
	
	/** 13.用户消息*/
	public static final int USER_SCENE_OPERATION_STORE_MESSAGE = MqConstants.MESSAGE_QUEUE_EMAIL
			| MqConstants.MESSAGE_QUEUE_SMS | MqConstants.MESSAGE_QUEUE_SYSTEM_STATION_MAIL;

	/** 1.邮件 */
	public static final int MESSAGE_QUEUE_EMAIL = 1 << 0;
	/** 4.短信 */
	public static final int MESSAGE_QUEUE_SMS = 1 << 2;
	/** 8站内信 */
	public static final int MESSAGE_QUEUE_SYSTEM_STATION_MAIL = 1 << 3;

	/** 初始并发用户数量 */
	public static final int CONCURRENT_CONSUMER = 1;

	/** 创建的并发用户的最大数目 */
	public static final int CONCURRENT_MAX_CONSUMER = 5;

	/** 线程池维护线程所允许的空闲时间 */
	public static final int INTERVAL_MILS = 0;

	/** 线程池所使用的缓冲队列大小 */
	public static final int WORK_QUEUE_SIZE = 25;
	
	/** 接收对象 1 管理员*/
	public static final int RECEIVE_TYPE_ADMIN = 1;
	/** 接收对象 2会员*/
	public static final int RECEIVE_TYPE_MEMBER = 2;
}
