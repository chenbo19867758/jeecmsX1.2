package com.jeecms.admin.controller.system;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.EmailUtils;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.system.domain.Email;
import com.jeecms.system.domain.dto.EmailDto;
import com.jeecms.system.service.EmailService;

/**
 * 邮箱设置Controller
 * 
 * @author: chenming
 * @date: 2019年4月12日 下午1:59:39
 */
@RestController
@RequestMapping("/email")
public class EmailController extends BaseController<Email, Integer> {

	@Autowired
	private EmailService service;

	/**
	 * 开启
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo saveOpen(@RequestBody @Validated Email email, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		if (email.getIsEnable()) {
			if (email.getId() != null) {
				service.update(email);
			} else {
				service.save(email);
			}
		} else {
			Email newEmail = new Email();
			if (email.getId() != null) {
				Email oldEmail = service.findOnly();
				MyBeanUtils.copyProperties(oldEmail, newEmail);
				newEmail.setIsEnable(false);
				newEmail.setIsGloable(false);
				service.update(newEmail);
			} else {
				newEmail.setIsEnable(false);
				newEmail.setIsGloable(false);
				service.save(newEmail);
			}
		}
		return new ResponseInfo(true);
	}

	/**
	 * 获取
	 */
	@RequestMapping(method = RequestMethod.GET)
	@SerializeField(clazz = Email.class, includes = { "id", "isGloable", "smtpService", "smtpPort", "emailName",
			"emailPassword", "isSsl", "isEnable" })
	public ResponseInfo get(HttpServletResponse response) throws GlobalException {
		return new ResponseInfo(service.findOnly());
	}

	/**
	 * 发送测试邮件
	 */
	@RequestMapping(value = "/calibration", method = RequestMethod.POST)
	public ResponseInfo sendTestEmail(@RequestBody @Valid EmailDto emailDto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		Email email = new Email();
		MyBeanUtils.copyProperties(emailDto, email);
		return new ResponseInfo(EmailUtils.sendEmail(email.getHtmlEmail(), emailDto.getToAddress(), "这是测试邮件标题",
				"这是测试邮件内容", email.getIsSsl()));
	}
}
