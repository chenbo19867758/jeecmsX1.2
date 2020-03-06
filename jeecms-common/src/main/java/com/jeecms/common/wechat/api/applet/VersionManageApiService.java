package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.MediaFile;
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

/**
 * 
 * @Description: 小程序代码版本管理service接口
 * @author: chenming
 * @date:   2018年10月31日 下午5:54:45     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface VersionManageApiService {
	
	/**
	 * 为授权的小程序帐号上传小程序代码
	 * @Title: commit  
	 * @param request
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: CommitResponse
	 */
	CommitResponse commit(CommitRequest request,ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 将第三方提交的代码包提交审核
	 * @Title: submitAudit  
	 * @param request
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: SubmitAuditResponse
	 */
	SubmitAuditResponse submitAudit(SubmitAuditRequest request,ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 发布已经审核的代码
	 * @Title: release  
	 * @param request
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: ReleaseResponse
	 */
	ReleaseResponse release(ReleaseRequest request,ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 修改小程序线上代码的可见状态
	 * @Title: changeVisitstatus  
	 * @param request
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: ChangeVisitstatusResponse
	 */
	ChangeVisitstatusResponse changeVisitstatus(ChangeVisitstatusRequest request,ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 小程序审核撤回
	 * @Title: undocodeaudit  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: UndocodeauditResponse
	 */
	UndocodeauditResponse undocodeaudit(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 获取小程序的第三方提交代码的页面配置
	 * @Title: getPage  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetPageResponse
	 */
	GetPageResponse getPage(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 获取体验二维码
	 * @Title: getQrcode  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: MediaFile
	 */
	MediaFile getQrcode(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 获取某个审核版本的状态
	 * @Title: getAudistatus  
	 * @param request
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetAudistatusResponse
	 */
	GetAudistatusResponse getAudistatus(GetAudistatusRequest request,ValidateToken validateToken) throws GlobalException;
}
