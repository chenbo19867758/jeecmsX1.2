/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.dao.AbstractWeChatOpenDao;
import com.jeecms.wechat.domain.AbstractWeChatOpen;
import com.jeecms.wechat.service.AbstractWeChatOpenService;
import com.jeecms.wechat.service.MiniprogramCodeService;

/**
 * 微信开发平台service实现
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月7日 下午3:27:53
 */
@Service
public class AbstractWeChatOpenServiceImpl extends BaseServiceImpl<AbstractWeChatOpen, AbstractWeChatOpenDao, Integer>
		implements AbstractWeChatOpenService {

	@Override
	public AbstractWeChatOpen findOpenConfig() {
		return super.dao.findByHasDeleted(false);
	}

	@Override
	public ResponseInfo saveAbstractWeChatOpen(AbstractWeChatOpen abstractWeChatOpen) throws GlobalException {
		// 只存在一条，如果数据库中有数据，则不能做添加，只能做修改操作，防止违法调用接口，直接返回不做添加处理
		if (dao.findByHasDeleted(false) != null) {
			return new ResponseInfo();
		}
		AbstractWeChatOpen weChatOpen = this.checkWechatOpenInfo(abstractWeChatOpen);
		weChatOpen.setName(abstractWeChatOpen.getName());
		weChatOpen.setAppId(abstractWeChatOpen.getAppId());
		weChatOpen.setAppSecret(abstractWeChatOpen.getAppSecret());
		super.save(weChatOpen);
		return new ResponseInfo();
	}

	/**
	 * 修改平台信息
	 */
	@Override
	public ResponseInfo updateAbstractWeChatOpen(AbstractWeChatOpen abstractWeChatOpen) throws GlobalException {
		String appId = this.findOpenConfig().getAppId();
		AbstractWeChatOpen weChatOpen = this.checkWechatOpenInfo(abstractWeChatOpen);
		weChatOpen.setId(abstractWeChatOpen.getId());
		weChatOpen.setName(abstractWeChatOpen.getName());
		weChatOpen.setAppId(abstractWeChatOpen.getAppId());
		weChatOpen.setAppSecret(abstractWeChatOpen.getAppSecret());
		super.update(weChatOpen);
		// 如果修改后的平台信息和之前不同，那么说明用户需要更换开放平台，那么之前开放平台的草稿箱和模板应该全部被干掉
		if (!appId.equals(abstractWeChatOpen.getAppId())) {
			miniprogramCodeService.clear();
		}
		return new ResponseInfo();
	}

	/**
	 * 校验填写的微信开发平台信息<br>
	 * 1.消息校验Token输入是否符合规则 必须是长度为16位的字符串，只能是字母和数字<br>
	 * 2.消息加解密Key输入是否符合规则 必须是长度为43位的字符串，只能是字母和数字
	 * 
	 * @Title: checkWechatOpenInfo
	 * @param abstractWeChatOpen
	 * @return
	 * @throws GlobalException
	 * @return: AbstractWeChatOpen
	 */
	public AbstractWeChatOpen checkWechatOpenInfo(AbstractWeChatOpen abstractWeChatOpen) throws GlobalException {
		String token = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{16}$";
		String kPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{43}$";
		AbstractWeChatOpen weChatOpen = new AbstractWeChatOpen();
		// 验证消息校验Token 16位 字母或者数字
		if (abstractWeChatOpen.getMessageValidateToken().matches(token)) {
			weChatOpen.setMessageValidateToken(abstractWeChatOpen.getMessageValidateToken());
		} else {
			throw new GlobalException(new SystemExceptionInfo(RPCErrorCodeEnum.TOKEN_INPUT_ERROR.getDefaultMessage(),
					RPCErrorCodeEnum.TOKEN_INPUT_ERROR.getCode()));
		}
		// 验证消息校验Token 43位 字母或者数字
		if (abstractWeChatOpen.getMessageDecryptKey().matches(kPattern)) {
			weChatOpen.setMessageDecryptKey(abstractWeChatOpen.getMessageDecryptKey());
		} else {
			throw new GlobalException(new SystemExceptionInfo(RPCErrorCodeEnum.KEY_INPUT_ERROR.getCode(),
					RPCErrorCodeEnum.KEY_INPUT_ERROR.getDefaultMessage()));
		}
		return weChatOpen;
	}
	
	@Autowired
	private MiniprogramCodeService miniprogramCodeService;

}
