package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.applet.VersionManageApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.OpenReturnCode;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.ChangeVisitstatusRequest;
import com.jeecms.common.wechat.bean.request.applet.CommitRequest;
import com.jeecms.common.wechat.bean.request.applet.GetAudistatusRequest;
import com.jeecms.common.wechat.bean.request.applet.ReleaseRequest;
import com.jeecms.common.wechat.bean.request.applet.SubmitAuditRequest;
import com.jeecms.common.wechat.bean.response.applet.ChangeVisitstatusResponse;
import com.jeecms.common.wechat.bean.response.applet.CommitResponse;
import com.jeecms.common.wechat.bean.response.applet.GetAudistatusResponse;
import com.jeecms.common.wechat.bean.response.applet.GetPageResponse;
import com.jeecms.common.wechat.bean.response.applet.ReleaseResponse;
import com.jeecms.common.wechat.bean.response.applet.SubmitAuditResponse;
import com.jeecms.common.wechat.bean.response.applet.UndocodeauditResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 小程序代码版本管理service接口实现类
 * @author: chenming
 * @date:   2018年10月31日 下午6:01:19     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class VersionManageApiServiceImpl implements VersionManageApiService{
	/** 为授权的小程序账号上传小程序代码*/
	public final String COMMIT=Const.DoMain.API_URI.concat("/wxa/commit");
	/** 将第三方提交的代码包提交审核*/
	public final String SUBMIT_AUDIT=Const.DoMain.API_URI.concat("/wxa/submit_audit");
	/** 发布已经通过审核的小程序*/
	public final String RELEASE=Const.DoMain.API_URI.concat("/wxa/release");
	/** 修改小程序线上代码的可见状态*/
	public final String CHANGE_VISITSTATUS=Const.DoMain.API_URI.concat("/wxa/change_visitstatus");
	/** 小程序审核撤回*/
	public final String UNDOCODE_AUDIT=Const.DoMain.API_URI.concat("/wxa/undocodeaudit");
	/** 获取小程序的第三方提交代码的页面配置*/
	public final String GET_PAGE=Const.DoMain.API_URI.concat("/wxa/get_page");
	/** 获取体验二维码*/
	public final String GET_QRCODE=Const.DoMain.API_URI.concat("/wxa/get_qrcode");
	/** 获取某个审核版本的状态*/
	public final String GET_AUDISTATUS=Const.DoMain.API_URI.concat("/wxa/get_auditstatus");
	
	public final String ACCESS_TOKEN="access_token";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public CommitResponse commit(CommitRequest request, ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		CommitResponse response=HttpUtil.postJsonBean(COMMIT, params, SerializeUtil.beanToJson(request), CommitResponse.class);
		return response;
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public SubmitAuditResponse submitAudit(SubmitAuditRequest request, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		SubmitAuditResponse response=HttpUtil.postJsonBean(SUBMIT_AUDIT, params, SerializeUtil.beanToJson(request), SubmitAuditResponse.class);
		return response;
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public ReleaseResponse release(ReleaseRequest request, ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		ReleaseResponse response=HttpUtil.postJsonBean(RELEASE, params, SerializeUtil.beanToJson(request), ReleaseResponse.class);
		return response;
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public ChangeVisitstatusResponse changeVisitstatus(ChangeVisitstatusRequest request, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		ChangeVisitstatusResponse response=HttpUtil.postJsonBean(CHANGE_VISITSTATUS, params, SerializeUtil.beanToJson(request), ChangeVisitstatusResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			/** 需要将此处的英文提示修改成为中文提示*/
			Integer errcode=Integer.valueOf(response.getErrcode());
			if (errcode!=-1) {
				errcode=Math.abs(errcode);
			}
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),OpenReturnCode.get(errcode)));
		}
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public UndocodeauditResponse undocodeaudit(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		UndocodeauditResponse response=HttpUtil.getJsonBean(UNDOCODE_AUDIT, params, UndocodeauditResponse.class);
		return response;
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetPageResponse getPage(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetPageResponse response=HttpUtil.getJsonBean(GET_PAGE, params, GetPageResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			/** 需要将此处的英文提示修改成为中文提示*/
			Integer errcode=Integer.valueOf(response.getErrcode());
			if (errcode!=-1) {
				errcode=Math.abs(errcode);
			}
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),OpenReturnCode.get(errcode)));
		}
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public MediaFile getQrcode(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		MediaFile mediaFile=HttpUtil.downloadByGet(GET_QRCODE, params);
		if (mediaFile!=null) {
			return mediaFile;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.SYSTEM_ERROR.getCode(),RPCErrorCodeEnum.SYSTEM_ERROR.getDefaultMessage()));
		}
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetAudistatusResponse getAudistatus(GetAudistatusRequest request, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(1);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetAudistatusResponse response = HttpUtil.postJsonBean(GET_AUDISTATUS, params, SerializeUtil.beanToJson(request), GetAudistatusResponse.class);;
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			Integer errcode=Integer.valueOf(response.getErrcode());
			if (errcode!=-1) {
				errcode=Math.abs(errcode);
			}
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),OpenReturnCode.get(errcode)));
		}
	}
}
