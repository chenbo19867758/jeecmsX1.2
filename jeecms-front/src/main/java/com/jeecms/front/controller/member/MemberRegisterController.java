/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.front.controller.member;

import static com.jeecms.common.exception.error.UserErrorCodeEnum.EMAIL_ALREADY_EXIST;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.EMAIL_INVALID;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.EMAIL_UNBOUND_MEMBER;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.PHONE_ALREADY_EXIST;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.PHONE_INVALID;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.PHONE_UNBOUND_MEMBER;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.USERNAME_ALREADY_EXIST;
import static com.jeecms.common.exception.error.UserErrorCodeEnum.VALIDATE_CODE_UNTHROUGH;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_REGISTER;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PSTR;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_VALIDATE_USER_INFOR;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_SECOND_LEVEL_IDENTITY_NEW_VALIDATE_USER_INFOR;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_MEMBER_LOGIN_PHONE;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_REGISTER;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_RETRIEVE_PASSWORD;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.STATUS_PASS;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_NEW_VALIDATE_USER_INFOR;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.CODE_TYPE_VALIDATE_USER_INFOR;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.dto.CoreUserDto.One;
import com.jeecms.auth.service.CaptchaService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.captcha.KaptchaConfig;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.CaptchaExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.member.domain.dto.MemberRegisterDto;
import com.jeecms.member.domain.dto.PcMemberDto;
import com.jeecms.member.domain.dto.PcMemberDto.RetrievePasswordByEmail;
import com.jeecms.member.domain.dto.PcMemberDto.RetrievePasswordByPhone;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.dto.SendValidateCodeDTO;
import com.jeecms.system.domain.dto.SendValidateCodeDTO.SendValidateCode;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.system.service.SmsService;
import com.jeecms.util.SystemContextUtils;

/**   
 * 会员注册控制器
 * @author: ljw
 * @date:   2019年6月21日 下午5:49:03     
 */
@RestController
@RequestMapping("/register")
@Validated
public class MemberRegisterController extends BaseController<CoreUser, Integer> {

	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private GlobalConfigService globalConfigService;
	@Autowired
	private CaptchaService captchaService;

	/**
	 * 注册
	* @Title: registerByPhone 
	* @param dto 传输
	* @param result 检验
	* @param request 请求
	* @throws Exception 异常
	 */
	@PostMapping
	public ResponseInfo registerByPhone(@RequestBody @Validated(One.class) MemberRegisterDto dto,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.validateBindingResult(result);
		//判断后台是否开启会员功能或者是否开启会员注册
		GlobalConfig config = globalConfigService.get();
		if (!config.getMemberOpen() 
				|| !config.getConfigAttr().getIsMemberRegister()) {
			throw new GlobalException(SystemExceptionEnum.MEMBER_CLOSE);
		}
		//判断是否开启会员图形验证码
		if (config.getMemberRegisterCaptcha()) {
			if (StringUtils.isNotBlank(dto.getCaptcha())) {
				String captchaSessionid = dto.getSessionId();
				Boolean flag = captchaService.validCaptcha(request, response, dto.getCaptcha(), captchaSessionid);
				if (!flag) {
		            throw new GlobalException(new CaptchaExceptionInfo());
				}
			} else {
				throw new GlobalException(new CaptchaExceptionInfo());
			}
		}
		//默认存入站点ID
		dto.setSiteId(SystemContextUtils.getSiteId(request));
		if (!coreUserService.validName(dto.getUsername())) {
			return new ResponseInfo(USERNAME_ALREADY_EXIST.getCode(), USERNAME_ALREADY_EXIST.getDefaultMessage());
		}
		//判断是否需要验证邮箱，手机
		if (StringUtils.isNotBlank(dto.getEmail()) && StringUtils.isNotBlank(dto.getTelephone())) {
			dto.setType(MemberRegisterDto.TYPE_ALL);
		} else if (StringUtils.isNotBlank(dto.getEmail())) {
			dto.setType(MemberRegisterDto.TYPE_EMAIL);
		} else if (StringUtils.isNotBlank(dto.getTelephone())) {
			dto.setType(MemberRegisterDto.TYPE_PHONE);
		} else {
			dto.setType(MemberRegisterDto.TYPE_NONE);
		}
		return coreUserService.savePCMember(dto);
	}

	/**
	 *  发送短信验证码
	* @Title: sendPhoneMsg 
	* @param bean 传输实体
	* @param result 检验
	* @return
	* @throws GlobalException
	 */
	@PostMapping("/sendPhoneMsg")
	public ResponseInfo sendPhoneMsg(
			@RequestBody @Validated(value = { SendValidateCode.class }) SendValidateCodeDTO bean, BindingResult result)
					throws GlobalException {
		super.validateBindingResult(result);
		Boolean flag = coreUserService.validPhone(bean.getTargetNumber(),null);
		Integer type = bean.getType();
		if (type.equals(CODE_TYPE_REGISTER) && !flag) { // 注册时的电话号码已被绑定
			return new ResponseInfo(PHONE_ALREADY_EXIST.getCode(), PHONE_ALREADY_EXIST.getDefaultMessage(), false);
		} else if ((type.equals(CODE_TYPE_RETRIEVE_PASSWORD) || type.equals(CODE_TYPE_MEMBER_LOGIN_PHONE))
				&& flag) { // 密码找回,根据手机号登录时的手机号未绑定会员
			return new ResponseInfo(PHONE_UNBOUND_MEMBER.getCode(), PHONE_UNBOUND_MEMBER.getDefaultMessage(), false);
		}
		bean.setValidateCode(null);
		return smsService.sendPhoneMsg(bean);
	}

	/**
	 * 发送邮箱验证码
	* @Title: sendEmailMsg 
	* @param bean 传输对象
	* @param result
	* @return
	* @throws GlobalException
	 */
	@PostMapping(value = "/sendEmailMsg")
	public ResponseInfo sendEmailMsg(
			@RequestBody @Validated(value = { SendValidateCode.class }) SendValidateCodeDTO bean, BindingResult result)
					throws GlobalException {
		super.validateBindingResult(result);
		Integer type = bean.getType();
		if (type.equals(CODE_TYPE_RETRIEVE_PASSWORD)) { 
			// 根据用户名或邮箱查找会员
			CoreUser user = coreUserService.findByEmailOrUsername(bean.getTargetNumber());
			if (user == null || !StringUtils.isNotBlank(user.getEmail())) {
				return new ResponseInfo(UserErrorCodeEnum.EMAIL_UNBOUND_MEMBER.getCode(),
						UserErrorCodeEnum.EMAIL_UNBOUND_MEMBER.getDefaultMessage(), false);
			}
			bean.setTargetNumber(user.getEmail());
			bean.setValidateCode(null);
			return smsService.sendEmailMsg(bean);
		}
		//true为通过，false不通过
		Boolean flag = coreUserService.validMail(bean.getTargetNumber(),null);
		if (type.equals(CODE_TYPE_REGISTER) && !flag) { 
			// 注册时的邮箱已被绑定
			return new ResponseInfo(EMAIL_ALREADY_EXIST.getCode(), 
					EMAIL_ALREADY_EXIST.getDefaultMessage(), false);
		} else if (type.equals(CODE_TYPE_VALIDATE_USER_INFOR) && flag) {
			// 身份验证
			return new ResponseInfo(EMAIL_UNBOUND_MEMBER.getCode(), 
					EMAIL_UNBOUND_MEMBER.getDefaultMessage(), false);
		} else if (type.equals(CODE_TYPE_NEW_VALIDATE_USER_INFOR) && !flag) {
			// 验证新号码(手机或邮箱)
			return new ResponseInfo(EMAIL_ALREADY_EXIST.getCode(), 
					EMAIL_ALREADY_EXIST.getDefaultMessage(), false);
		} 
		bean.setValidateCode(null);
		return smsService.sendEmailMsg(bean);
	}
	
	/**
	 * 验证用户名
	 * 
	 * @param: validName
	 * @return
	 */
	@GetMapping(value = "/username/unique")
	public ResponseInfo username(@NotNull @Length(min = 6, max = 18)
		@RequestParam(name = "username", required =true) String username) throws GlobalException {
		Boolean flag = coreUserService.validName(username);
		return new ResponseInfo(flag);
	}

	/**
	 * 验证邮箱
	 * 
	 * @param: validMail
	 * @return
	 */
	@GetMapping(value = "/mail/unique")
	public ResponseInfo mail(@Email String mail, Integer id) throws GlobalException {
		Boolean flag = coreUserService.validMail(mail, id);
		return new ResponseInfo(flag);
	}

	/**
	 * 验证手机
	 * 
	 * @param: validMail
	 * @return
	 */
	@GetMapping(value = "/phone/unique")
	public ResponseInfo validMail(String phone, Integer id) throws GlobalException {
		Boolean flag = coreUserService.validPhone(phone, id);
		return new ResponseInfo(flag);
	}
	
	/**
	 * 前台新增会员字段
	* @Title: addParam 
	* @param request 请求
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	@MoreSerializeField({ @SerializeField(clazz = CmsModel.class, includes = { "enableJson" }) })
	@GetMapping(value = "/addition/model")
	public ResponseInfo addParam(HttpServletRequest request) 
			throws GlobalException {
		return new ResponseInfo(cmsModelService.getFrontMemberModel());
	}
	
	/**
	 * 根据电话号码找回密码
	* @Title: rectrieveByPhone 
	* @param bean PC端的会员DTO
	* @param result 检验
	* @param request
	* @return
	* @throws Exception
	 */
	@PostMapping(value = "/rectrieve/phone")
	public ResponseInfo rectrieveByPhone(
			@RequestBody @Validated(value = RetrievePasswordByPhone.class) PcMemberDto bean, BindingResult result) 
					throws Exception {
		super.validateBindingResult(result);
		CoreUser member = coreUserService.findByPhone(bean.getPhone());
		if (member == null) {
			return new ResponseInfo(PHONE_INVALID.getCode(), PHONE_INVALID.getDefaultMessage());
		}
		bean.setId(member.getId());
		//设置类型为手机找回
		bean.setType(PcMemberDto.TYPE_TELEPHONE);
		return coreUserService.rectrieve(bean);
	}

	/**
	 * 根据邮箱找回密码
	* @Title: rectrieveByEmail 
	* @param bean
	* @param result
	* @param request
	* @return
	* @throws Exception
	 */
	@PostMapping(value = "/rectrieve/key")
	public ResponseInfo rectrieveByEmail(
			@RequestBody @Validated(value = RetrievePasswordByEmail.class) PcMemberDto bean, BindingResult result)
					throws Exception {
		super.validateBindingResult(result);
		CoreUser member = coreUserService.findByEmailOrUsername(bean.getKey());
		if (member == null) {
			return new ResponseInfo(EMAIL_INVALID.getCode(), EMAIL_INVALID.getDefaultMessage());
		}
		bean.setId(member.getId());
		//设置类型为邮箱找回
		bean.setType(PcMemberDto.TYPE_EMAIL);
		bean.setEmail(member.getEmail());
		return coreUserService.rectrieve(bean);
	}
	
	/**
	 * 验证邮箱验证码
	 * @Title: mailAuthcode
	 * @param email 邮箱
	 * @param code  验证码
	 * @param type  1.注册2.找回密码3.身份验证4.验证新号码(手机或邮箱)
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/mail/authcode")
	public ResponseInfo mailAuthcode(String email, String code, Integer type) throws GlobalException {
		if (type == CODE_TYPE_REGISTER) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_REGISTER + email;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		} else if (type == CODE_TYPE_RETRIEVE_PASSWORD) {
			// 根据用户名或邮箱查找会员
			CoreUser user = coreUserService.findByEmailOrUsername(email);
			if (user == null || !StringUtils.isNotBlank(user.getEmail())) {
				return new ResponseInfo(UserErrorCodeEnum.EMAIL_UNBOUND_MEMBER.getCode(),
						UserErrorCodeEnum.EMAIL_UNBOUND_MEMBER.getDefaultMessage(), false);
			}
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PSTR + user.getEmail();
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		} else if (type == CODE_TYPE_VALIDATE_USER_INFOR) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_VALIDATE_USER_INFOR + email;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		} else if (type == CODE_TYPE_NEW_VALIDATE_USER_INFOR) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_NEW_VALIDATE_USER_INFOR + email;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		}
		return new ResponseInfo(true);
	}
	
	/**
	 * 验证手机验证码
	 * @Title: mobileAuthcode
	 * @param mobile 手机号
	 * @param code  验证码
	 * @param type  1.注册2.找回密码3.身份验证4.验证新号码(手机或邮箱)
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/mobile/authcode")
	public ResponseInfo mobileAuthcode(String mobile, String code, Integer type) throws Exception {
		if (type == 1) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_REGISTER + mobile;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		} else if (type == 2) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PSTR + mobile;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		} else if (type == 3) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_VALIDATE_USER_INFOR + mobile;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		} else if (type == 4) {
			String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_NEW_VALIDATE_USER_INFOR + mobile;
			int status = smsService.validateCode(sessionKey, code);
			if (STATUS_PASS > status) {
				return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
						false);
			}
		}
		return new ResponseInfo(true);
	}
}
