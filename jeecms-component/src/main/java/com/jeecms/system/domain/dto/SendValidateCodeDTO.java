/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import static com.jeecms.constants.MessageTplCodeConstants.USER_RECOVERY_VALIDATE_CODE_TPL;
import static com.jeecms.constants.MessageTplCodeConstants.USER_REGISTER_VALIDATE_CODE_TPL;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_REGISTER;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PSTR;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_REGISTER;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_RETRIEVE_PASSWORD;

import java.util.TreeMap;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.constants.MessageTplCodeConstants;

/**
 * 验证码DTO,用于发送验证码
 * @author: ztx
 * @date: 2018年9月3日 下午3:57:23
 */
public class SendValidateCodeDTO {
	private static final Logger logger = LoggerFactory.getLogger(SendValidateCodeDTO.class);

	/** 验证码类型,如:用户注册,密码找回 */
	private Integer type;
	/** 目标号码 */
	private String targetNumber;
	/** 自定义验证码,为空则服务端生成随机数 */
	private String validateCode;
	/** 短信文案需要的动态替换变量 */
	private TreeMap<String, String> treeMap;

	@NotNull(groups = { LoginSubmit.class, SendValidateCode.class })
	@Digits(integer = 1, fraction = 0, groups = { LoginSubmit.class, SendValidateCode.class })
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@NotBlank(groups = { LoginSubmit.class, SendValidateCode.class })
	@Length(max = 50, groups = { LoginSubmit.class, SendValidateCode.class })
	public String getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
	}

	@NotBlank(groups = { LoginSubmit.class })
	@Length(max = 150, groups = { LoginSubmit.class })
	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public TreeMap<String, String> getTreeMap() {
		return treeMap;
	}

	public void setTreeMap(TreeMap<String, String> treeMap) {
		this.treeMap = treeMap;
	}

	/**
	 * 获取二级前缀名称
	 */
	public String getSecondLevelName() throws GlobalException {
		String levelName = null;
		switch (this.getType()) {
			case CODE_TYPE_REGISTER:
				levelName = CODE_SECOND_LEVEL_IDENTITY_REGISTER;
				break;
			case CODE_TYPE_RETRIEVE_PASSWORD:
				levelName = CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PSTR;
				break;
			case ValidateCodeConstants.CODE_TYPE_VALIDATE_USER_INFOR:
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_VALIDATE_USER_INFOR;
				break;
			case ValidateCodeConstants.CODE_TYPE_NEW_VALIDATE_USER_INFOR:
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_NEW_VALIDATE_USER_INFOR;
				break;
			case ValidateCodeConstants.CODE_TYPE_MEMBER_LOGIN_PHONE:
				//内容审核登录也使用该标识
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_USER_LOGIN_PHONE;
				break;
			case ValidateCodeConstants.CODE_TYPE_UPDATE_PASSWORD:
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_UPDATE_PSTR;
				break;
			case ValidateCodeConstants.CODE_TYPE_EMAIL_BINDING:
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_EMAIL_BINDING;
				break;
			case ValidateCodeConstants.CODE_TYPE_PHONE_BINDING:
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_PHONE_BINDING;
				break;
			case ValidateCodeConstants.CODE_TYPE_UPDATE_PAY_PASSWORD:
				levelName = ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_UPDATE_PAY_PSTR;
				break;
			default:
				logger.info(java.text.Normalizer.normalize(
						String.format("type is mismatching -->%s", type),
					java.text.Normalizer.Form.NFKD));
				throw new GlobalException(SystemExceptionEnum.ILLEGAL_PARAM);
		}
		return levelName;
	}

	/**
	 * 获取消息验证码模板
	 */
	public String getMessageTplCode() throws GlobalException {
		String result = null;
		switch (this.getType()) {
			case CODE_TYPE_REGISTER:
				result = USER_REGISTER_VALIDATE_CODE_TPL;
				break;
			case CODE_TYPE_RETRIEVE_PASSWORD:
				result = USER_RECOVERY_VALIDATE_CODE_TPL;
				break;
			case ValidateCodeConstants.CODE_TYPE_VALIDATE_USER_INFOR:
			case ValidateCodeConstants.CODE_TYPE_UPDATE_PASSWORD:
			case ValidateCodeConstants.CODE_TYPE_EMAIL_BINDING:
			case ValidateCodeConstants.CODE_TYPE_PHONE_BINDING:
			case ValidateCodeConstants.CODE_TYPE_UPDATE_PAY_PASSWORD:
				result = MessageTplCodeConstants.VALIDATE_USER_INFO_TPL;
				break;
			case ValidateCodeConstants.CODE_TYPE_NEW_VALIDATE_USER_INFOR:
				result = MessageTplCodeConstants.VALIDATE_NEW_USER_INFO_TPL;
				break;
			case ValidateCodeConstants.CODE_TYPE_MEMBER_LOGIN_PHONE:
				result = MessageTplCodeConstants.USER_LOGIN_VALIDATE_CODE_TPL;
				break;
			default:
				logger.info(java.text.Normalizer.normalize(
						String.format("type is mismatching -->%s", type),
					java.text.Normalizer.Form.NFKD));
				throw new GlobalException(SystemExceptionEnum.ILLEGAL_PARAM);
		}
		return result;
	}

	/**
	 * 获取消息验证码模板
	 */
	public TreeMap<String, String> getTreeMapParam() throws GlobalException {
		TreeMap<String, String> result = new TreeMap<>();
		switch (this.getType()) {
			case CODE_TYPE_REGISTER:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case CODE_TYPE_RETRIEVE_PASSWORD:
				result.put("code", this.getValidateCode());
				break;
			case ValidateCodeConstants.CODE_TYPE_VALIDATE_USER_INFOR:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case ValidateCodeConstants.CODE_TYPE_NEW_VALIDATE_USER_INFOR:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case ValidateCodeConstants.CODE_TYPE_MEMBER_LOGIN_PHONE:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case ValidateCodeConstants.CODE_TYPE_UPDATE_PASSWORD:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case ValidateCodeConstants.CODE_TYPE_EMAIL_BINDING:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case ValidateCodeConstants.CODE_TYPE_PHONE_BINDING:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			case ValidateCodeConstants.CODE_TYPE_UPDATE_PAY_PASSWORD:
				result.put("code", this.getValidateCode());
				result.put("expire", String.valueOf(ValidateCodeConstants.DEFAULT_EXPIRE_TIME / 60));
				break;
			default:
				logger.info(java.text.Normalizer.normalize(
						String.format("type is mismatching -->%s", type),
					java.text.Normalizer.Form.NFKD));
				throw new GlobalException(SystemExceptionEnum.ILLEGAL_PARAM);
		}
		return result;
	}

	public interface LoginSubmit {
	}

	public interface SendValidateCode {
	}

	@Override
	public String toString() {
		return "SendValidateCodeDTO.java";
	}

}
