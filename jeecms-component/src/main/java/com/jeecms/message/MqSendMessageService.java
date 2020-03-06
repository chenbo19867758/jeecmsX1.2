package com.jeecms.message;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;

/**
 * 会员注册消息服务
 * @author: chenming
 * @date:   2019年6月17日 上午10:06:44
 */
public interface MqSendMessageService {

	/**
	 * 通用发送接口
	 * @Title: sendMemberMsg  
	 * @param targetType	接收对象类型 1-全部 2-全部管理员 3-全部会员 4-组织 5-指定管理员 6-会员等级 7-会员组 8-指定会员 
	 * @param orgId	组织id
	 * @param memberGroupId	会员组id
	 * @param memberLevelId	会员等级id
	 * @param receiveIds	接收方id数据->1.指定管理员、2.指定会员
	 * @param messageCode	模板标识
	 * @param scene	消息场景(当发送类型中有短信是此值必填)
	 * @param title	标题(优先于取消息场景信息，如果为空再去标题和文本内容)
	 * @param textContent	文本内容
	 * @param toPhone	接收方手机号
	 * @param toEmail	接收方邮件
	 * @param data	数据
	 * @param sendType	${@link MqConstants } 发送类型：1.邮箱，2.短信，3.站内信，4.邮箱+短信，5.短信+邮箱+站内信，6. 邮箱+站内信，7. 短信+站内信
	 * @param siteId	发送方服务站点id值，当发送类型为：1,2,4,5时必填
	 * @throws GlobalException      全局异常
	 * @return: void
	 */
	void sendMemberMsg(Integer targetType,Integer orgId,Integer memberGroupId,Integer memberLevelId,
			List<Integer> receiveIds,String messageCode,MessageSceneEnum scene,
			String title,String textContent,List<String> toPhones,List<String> toEmails,JSONObject data,
			Short sendType,Integer siteId) throws GlobalException;

	
	
	
	/**
	 * 发送验证码
	 * @Title: sendValidateCodeMsg  
	 * @param messageCode	模板标识
	 * @param scene	消息场景
	 * @param title	标题
	 * @param textContent	文本内容
	 * @param toPhone	接收方电话
	 * @param toEmail	接收方邮件
	 * @param sendType	发送类型：1.邮箱，2.短信，3.站内信，4.邮箱+短信，5.短信+邮箱+站内信
	 * @param siteId	站点id值
	 * @param data	数据
	 * @throws GlobalException    全局异常  
	 * @return: void
	 */
	void sendValidateCodeMsg(String messageCode,MessageSceneEnum scene,String title,String textContent,
			String toPhone, String toEmail, Short sendType,Integer siteId,JSONObject data) 
					throws GlobalException;

}
