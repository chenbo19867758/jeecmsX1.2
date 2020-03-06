package com.jeecms.wechat.service;

import java.text.ParseException;
import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.bean.request.mp.news.ReqMessage;
import com.jeecms.wechat.domain.MiniprogramVersion;
import com.jeecms.wechat.domain.dto.AuditVersionDto;
import com.jeecms.wechat.domain.dto.MiniprogramVersionGetButton;

/**
 * 小程序代码版本管理service接口
 * @author: chenming
 * @date:   2019年6月13日 上午9:53:53
 */
public interface MiniprogramVersionService extends IBaseService<MiniprogramVersion, Integer> {
	
	/**
	 * 提交开发版本
	 * @Title: submitVersion  
	 * @param appId	小程序appId
	 * @throws GlobalException    全局异常  
	 * @return: boolean
	 */
	boolean submitVersion(String appId) throws GlobalException;
	
	/**
	 * 查询是否有查询失败的数据
	 * @Title: getSubmitFail  
	 * @param appId	小程序appId
	 * @throws GlobalException 	全局异常     
	 * @return: MiniprogramVersion
	 */
	MiniprogramVersion getSubmitFail(String appId) throws GlobalException;
	
	/**
	 * 提交审核版本
	 * @Title: auditVersion  
	 * @param appId	小程序appId
	 * @param aDtos	提交审核dto集合
	 * @throws GlobalException 全局异常     
	 * @return: void
	 */
	void auditVersion(String appId,List<AuditVersionDto> aDtos) throws GlobalException;
	
	/**
	 * 获取小程序的第三方提交代码的页面配置
	 * @Title: getPage  
	 * @param appId	小程序appId
	 * @throws GlobalException   全局异常  
	 * @return: ResponseInfo
	 */
	ResponseInfo getPage(String appId) throws GlobalException; 
	
	/**
	 * 获取授权小程序帐号的可选类目
	 * @Title: getCategory  
	 * @param appId	小程序appId
	 * @throws GlobalException  全局异常    
	 * @return: ResponseInfo
	 */
	ResponseInfo getCategory(String appId) throws GlobalException;
	
	/**
	 * 修改审核版本状态
	 * @Title: updateAuditStatus  
	 * @param appId	小程序appId
	 * @param reqMessage	微信端返回的请求message的XML
	 * @throws GlobalException	全局异常
	 * @throws ParseException     异常
	 * @return: void
	 */
	void updateAuditStatus(String appId,ReqMessage reqMessage) throws GlobalException,ParseException;
	
	/**
	 * 获取审核版本
	 * @Title: getAudit  
	 * @param appId	小程序appId
	 * @throws GlobalException	全局异常
	 * @throws ParseException     异常 
	 * @return: MiniprogramVersion
	 */
	MiniprogramVersion getAudit(String appId) throws GlobalException, ParseException;
	
	/**
	 * 撤销审核
	 * @Title: deleteAudit  
	 * @param mVersion	小程序版本管理实体类
	 * @throws GlobalException 	全局异常     
	 * @return: ResponseInfo
	 */
	ResponseInfo deleteAudit(MiniprogramVersion mVersion) throws GlobalException;
	
	/**
	 * 发布已审核版本
	 * @Title: releaseVersion  
	 * @param appId	小程序appId
	 * @param id	小程序版本管理实体类id值
	 * @throws GlobalException     全局异常
	 * @return: void
	 */
	void releaseVersion(String appId,Integer id) throws GlobalException;
	
	/**
	 * 查询已经发布的版本
	 * @Title: getRelease  
	 * @param appId	小程序appId
	 * @throws GlobalException    全局异常  
	 * @return: MiniprogramVersion
	 */
	MiniprogramVersion getRelease(String appId) throws GlobalException;
	
	/**
	 * 修改发布版本状态
	 * @Title: updateReleaseStatus  
	 * @param mVersion	小程序版本管理实体类
	 * @throws GlobalException    全局异常
	 * @return: void
	 */
	void updateReleaseStatus(MiniprogramVersion mVersion) throws GlobalException;
	
	/**
	 * 是否显示各种按钮
	 * @Title: isShow  
	 * @param appId	小程序appId
	 * @throws GlobalException  全局异常    
	 * @return: Boolean
	 */
	MiniprogramVersionGetButton isShow(String appId) throws GlobalException;
	
	/**
	 * 显示体验者二维码
	 * @Title: getQrcode  
	 * @param appId	小程序appId
	 * @throws Exception   最大异常   
	 * @return: void
	 */
	String getQrcode(String appId) throws Exception;
	
}
