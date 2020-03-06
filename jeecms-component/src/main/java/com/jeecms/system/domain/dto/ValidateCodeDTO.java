/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import static com.jeecms.system.domain.dto.ValidateCodeConstants.DEFAULT_EXPIRE_TIME;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.MAX_RESEND_COUNT;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_EXCEEDCOUNT;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_EXPIRED;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_ILLEGAL;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_UNEXPIRED;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_UNTHROUGH;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.DEFAULT_REPLY_TIME;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.exception.ExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;

/**
 * 验证码DTO
 * @author: ljw
 * @date: 2018年8月30日 下午3:02:22
 */
public class ValidateCodeDTO implements Serializable {
	private static final long serialVersionUID = -9163790547369251310L;
	private static final Logger logger = LoggerFactory.getLogger(ValidateCodeDTO.class);

	/** 验证码 */
	private String code;
	/** 创建时间 */
	private Date createTime;
	/** 验证次数 */
	private Integer count;
	/** 电话号码 */
	private String targetNumber;

	/**
	 * 构造函数
	 */
	public ValidateCodeDTO(String code, Date createTime, Integer count, String targetNumber) {
		super();
		this.code = code;
		this.createTime = createTime;
		this.count = count;
		this.targetNumber = targetNumber;
	}

	/**
	 * 判断是否过期
	 */
	public boolean isExpire() {
		return (System.currentTimeMillis() - this.getCreateTime().getTime()) / 1000 > DEFAULT_EXPIRE_TIME;
	}
	
	/**
	 * 判断是否允许重复发送,true表示允许,false表示不允许
	 */
	public boolean isSendExpire() {
		return (System.currentTimeMillis() - this.getCreateTime().getTime()) / 1000 > DEFAULT_REPLY_TIME;
	}

	/**
	 * 判断是否超出发送次数. true表示超额
	 */
	public boolean isExceedCount() {
		// 如果当前时间已经过去一天,直接重置为0
		int num = 1000 * 60 * 60;
		if ((System.currentTimeMillis() - this.getCreateTime().getTime()) / (num) >= 1L) {
			this.setCount(0);
			return false;
		}
		return MAX_RESEND_COUNT <= this.getCount();
	}

	/**
	 * 根据状态码返回对应异常信息
	 */
	public static ExceptionInfo exceptionAdapter(int status) throws GlobalException {
		ExceptionInfo result = null;
		switch (status) {
			case STATUS_EXPIRED:
				result = UserErrorCodeEnum.VALIDATE_CODE_UNTHROUGH;
				break;
			case STATUS_UNEXPIRED:
				result = UserErrorCodeEnum.VALIDATE_CODE_ALREADY_SEND;
				break;
			case STATUS_EXCEEDCOUNT:
				result = UserErrorCodeEnum.VALIDATE_CODE_EXCEEDCOUNT;
				break;
			case STATUS_UNTHROUGH:
				result = UserErrorCodeEnum.VALIDATE_CODE_UNTHROUGH;
				break;
			case STATUS_ILLEGAL:
				result = UserErrorCodeEnum.VALIDATE_CODE_ILLEGAL;
				break;
			default:
				logger.info("status is mismatching -->{}", status);
				throw new GlobalException(SystemExceptionEnum.ILLEGAL_PARAM);
		}
		return result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
	}
	
	@Override
	public String toString() {
		return "ValidateCodeDTO.java";
	}

}
