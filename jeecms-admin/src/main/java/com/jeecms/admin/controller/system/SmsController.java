package com.jeecms.admin.controller.system;

import javax.servlet.http.HttpServletResponse;

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
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.system.domain.Sms;
import com.jeecms.system.service.SmsService;

/**
 * 短信设置Controller
 * 
 * @author: chenming
 * @date: 2019年4月13日 上午11:24:22
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends BaseController<Sms, Integer> {

	@Autowired
	private SmsService service;

	/**
	 * 关闭服务
	 */
	@RequestMapping(method = RequestMethod.POST)
	@Override
	public ResponseInfo save(@RequestBody @Validated Sms sms, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		if (sms.getIsEnable()) {
			if (sms.getId() != null) {
				service.update(sms);
			} else {
				service.save(sms);
			}
		} else {
			Sms newSms = new Sms();
			if (sms.getId() != null) {
				/**
				 * 此处如此操作是因为后台在执行update是需要获取到之前数据库中数据的全局配置状态
				 * 但使用getList获取数据时受到查询出的数据的游离状态是进行set操作对象属性的影响容易出现数据问题，
				 * 所以此处使用新建对象，将原对象的数据copyProperties的方式避免此问题
				 */
				Sms oldSms = service.findOnly();
				MyBeanUtils.copyProperties(oldSms, newSms);
				newSms.setIsEnable(false);
				newSms.setIsGloable(false);
				service.update(newSms);
			} else {
				newSms.setIsEnable(false);
				newSms.setIsGloable(false);
				service.save(newSms);
			}
		}
		return new ResponseInfo(true);
	}

	/**
	 * 获取详情
	 */
	@SerializeField(clazz = Sms.class, includes = { "id", "isGloable", "accessKey", "accesskeySecret", "isEnable",
			"serviceProvider", "smsSign" })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseInfo get(HttpServletResponse response) throws GlobalException {
		return new ResponseInfo(service.findOnly());
	}

}
