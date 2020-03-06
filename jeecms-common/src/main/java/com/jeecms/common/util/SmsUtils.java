package com.jeecms.common.util;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;

/**
 * 短信服务工具类
 * 
 * @author: tom
 * @date: 2018年12月26日 下午3:23:56
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SmsUtils {
	private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);

	/**
	 * 阿里短信服务,成功返回true,失败则返回对应信息
	 * 
	 * @Title: sendByALi
	 * @param accessKey
	 *            accessKey
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @param smsSign
	 *            smsSign
	 * @param mobilePhone
	 *            手机号
	 * @param params
	 *            模板变量中替换参数，如模板内容为"亲爱的${name},您的验证码为${code}"时，此参数map的值为{name:张三
	 *            ,code:123456} ，结构无视顺序
	 * @param: @return
	 * @return: ResponseInfo
	 */
	public static ResponseInfo sendByALi(String accessKey, String accessKeySecret, 
			String smsSign, String msgTplCode,
			String mobilePhone, Map<String, String> params) {
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
			// 组装请求对象
			SendSmsRequest request = new SendSmsRequest();
			// 使用post提交
			request.setMethod(MethodType.POST);
			// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
			request.setPhoneNumbers(mobilePhone);
			// 必填:短信签名-可在短信控制台中找到
			request.setSignName(smsSign);
			// 必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(msgTplCode);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name}用户,您的验证码为${code}"时,此处的值为
			// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
			// 格式化模板参数
			if (params != null && params.size() > 0) {
				JSONObject json = new JSONObject();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					json.put(entry.getKey(), entry.getValue());
				}
				request.setTemplateParam(JSON.toJSONString(json));
			}
			// 初始化ascClient
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, accessKeySecret);
			IAcsClient acsClient = new DefaultAcsClient(profile);
			// 请求失败这里会抛ClientException异常
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			String resCodeOk = "OK";
			String resCodeMobileNumError = "isv.MOBILE_NUMBER_ILLEGAL";
			if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals(resCodeOk)) {
				return new ResponseInfo(SystemExceptionEnum.SUCCESSFUL.getCode(),
						SystemExceptionEnum.SUCCESSFUL.getDefaultMessage());
			} else if (resCodeMobileNumError.equals(sendSmsResponse.getCode())) {
				return new ResponseInfo(RPCErrorCodeEnum.PHONE_NUMBER_INVALID.getCode(),
						RPCErrorCodeEnum.PHONE_NUMBER_INVALID.getDefaultMessage(),
						RPCErrorCodeEnum.PHONE_NUMBER_INVALID);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ResponseInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),
				RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getDefaultMessage());
	}

	/**
	 * 腾讯短信服务
	 * 
	 * @Title: sendByTecent
	 * @param accessKey
	 *            accessKey
	 * @param accessKeySecret
	 *            accessKeySecret
	 * @param smsSign
	 *            前面
	 * @param mobilePhone
	 *            手机号
	 * @param values
	 *            指定短信模板中的动态参数数组，数组的个数、元素顺序需和对应短信模板内容一致，
	 *            如"亲爱的${name},您的验证码为${code}"时 ,此参数数组内容为 [ 张三 , 123456]
	 * @param: @return
	 * @return: ResponseInfo
	 */
	public static ResponseInfo sendByTecent(String accessKey, 
			String accessKeySecret, String smsSign, Integer msgTplId,
			String mobilePhone, ArrayList<String> values) {
		try {
			SmsSingleSender sender = new SmsSingleSender(Integer.parseInt(accessKey), accessKeySecret);
			SmsSingleSenderResult result = sender.sendWithParam("86", 
					mobilePhone, msgTplId, values, smsSign, "", "");
			logger.debug("sendByTecent result=" + result.result);
			if (result.result == 0) {
				return new ResponseInfo();
			} else if ("".equals(result.errMsg)) {
				// TODO 通不了,这里判断手机号非法
				return new ResponseInfo(RPCErrorCodeEnum.PHONE_NUMBER_INVALID.getCode(),
						RPCErrorCodeEnum.PHONE_NUMBER_INVALID.getDefaultMessage(),
						RPCErrorCodeEnum.PHONE_NUMBER_INVALID);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ResponseInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),
				RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getDefaultMessage());
	}
}
