package com.jeecms.threadmsg.common;

/**
 * 消费消息返回结果
 * @author: ztx
 * @date: 2018年8月27日 上午11:32:51
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MessageResult {

	/** 返回消息code码 */
	private int code;
	/** 返回消息 */
	private String message;
	/** 返回数据 */
	private Object data;

	/**
	 * 消息结果
	 */
	public MessageResult() {
		this.code = 200;
		this.message = "消费成功";
		this.data = new Object();
	}

	/**
	 * 消息成功
	 * @param data 数据对象
	 * @return 
	 */
	public MessageResult(Object data) {
		this.code = 200;
		this.message = "消费成功";
		this.data = data;
	}

	/**
	 * 消息返回方法
	 * @param code 状态码
	 * @param message 消息
	 * @param data 对象数据
	 */
	public MessageResult(String code, String message, Object data) {
		this.code = Integer.parseInt(code);
		this.message = message;
		this.data = data;
	}

	public MessageResult(String code, String message) {
		this.code = Integer.parseInt(code);
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MessageResult [code=" + code + ", message=" + message 
				+ ", data=" + data + "]";
	}

}
