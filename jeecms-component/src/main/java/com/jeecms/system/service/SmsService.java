package com.jeecms.system.service;

import com.jeecms.common.base.domain.ThirdPartyResultDTO;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.Sms;
import com.jeecms.system.domain.dto.SendValidateCodeDTO;

/**
 * 短信服务配置service接口
 * @author: chenming
 * @date:   2019年4月13日 上午11:28:16
 */
public interface SmsService extends IBaseService<Sms, Integer> {

	/**
	 * 查找默认发送
	 * @Title: findDefault  
	 * @throws GlobalException 程序异常     
	 * @return: Sms
	 */
	Sms findDefault() throws GlobalException;

	/**
	 * 发送邮箱消息
	 * @Title: sendEmailMsg  
	 * @param bean 发送codeDto
	 * @throws GlobalException 程序异常      
	 * @return: ResponseInfo
	 */
	ResponseInfo sendEmailMsg(SendValidateCodeDTO bean) throws GlobalException;

	/**
	 * 发送手机短信消息
	 * @Title: sendPhoneMsg  
	 * @param bean 发送codeDto
	 * @throws GlobalException 程序异常     
	 * @return: ResponseInfo
	 */
	ResponseInfo sendPhoneMsg(SendValidateCodeDTO bean) throws GlobalException;

	/**
	 * 验证码有效性验证 0表示通过,小于0表示失败,大于0返回的是当前发送验证码次数,具体看 #ValidateCodeConstants
	 * 存在更改短信平台但是短信次数没刷新问题,但是前台感觉不到
	 * @Title: validateCode  
	 * @param attrName 服务端属性名称
	 * @param validateCode 待验证的验证码
	 * @throws GlobalException 程序异常      
	 * @return: int
	 */
	int validateCode(String attrName, String validateCode) throws GlobalException;

	/**
	 * 验证码有效性验证,不验证是否超出发送次数 0表示通过,小于0表示失败,大于0返回的是当前发送验证码次数,具体看
	 * #ValidateCodeConstants 存在更改短信平台但是短信次数没刷新问题,但是前台感觉不到
	 * @Title: notValidateSendCountCode  
	 * @param attrName 服务端属性名称
	 * @throws GlobalException    程序异常  
	 * @return: int
	 */
	int notValidateSendCountCode(String attrName) throws GlobalException;
	
	/**
	 * 查询数据库的唯一的短信接口
	 * @Title: findOnly  
	 * @throws GlobalException  程序异常      
	 * @return: Sms
	 */
	Sms findOnly() throws GlobalException;
	
	/**
	 * 保存
	 * @Title: save  
	 * @param sms 	短信设置实体类
	 * @throws GlobalException 程序异常  
	 * @return: Sms	
	 */
	@Override
	Sms save(Sms sms) throws GlobalException;
	
	/**
	 * 修改
	 * @Title: save  
	 * @param sms 	短信设置实体类
	 * @throws GlobalException      程序异常  
	 * @return: Sms	
	 */
	Sms update(Sms sms) throws GlobalException;
}
