package com.jeecms.wechat.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.Base64Utils;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.applet.VersionManageApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.OpenReturnCode;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.ChangeVisitstatusRequest;
import com.jeecms.common.wechat.bean.request.applet.CommitRequest;
import com.jeecms.common.wechat.bean.request.applet.GetAudistatusRequest;
import com.jeecms.common.wechat.bean.request.applet.ReleaseRequest;
import com.jeecms.common.wechat.bean.request.applet.SubmitAuditRequest;
import com.jeecms.common.wechat.bean.request.applet.common.AuditList;
import com.jeecms.common.wechat.bean.request.applet.common.Ext;
import com.jeecms.common.wechat.bean.request.mp.news.ReqMessage;
import com.jeecms.common.wechat.bean.response.applet.CommitResponse;
import com.jeecms.common.wechat.bean.response.applet.GetAudistatusResponse;
import com.jeecms.common.wechat.bean.response.applet.GetPageResponse;
import com.jeecms.common.wechat.bean.response.applet.ReleaseResponse;
import com.jeecms.common.wechat.bean.response.applet.SubmitAuditResponse;
import com.jeecms.common.wechat.bean.response.applet.UndocodeauditResponse;
import com.jeecms.wechat.dao.MiniprogramVersionDao;
import com.jeecms.wechat.domain.MiniprogramCode;
import com.jeecms.wechat.domain.MiniprogramVersion;
import com.jeecms.wechat.domain.dto.AuditVersionDto;
import com.jeecms.wechat.domain.dto.CategoryDto;
import com.jeecms.wechat.domain.dto.MiniprogramVersionGetButton;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.MiniprogramCodeService;
import com.jeecms.wechat.service.MiniprogramVersionService;

/**
 * 小程序代码版本管理service实现类
 * @author: chenming
 * @date:   2019年6月13日 下午2:38:29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MiniprogramVersionServiceImpl extends BaseServiceImpl<MiniprogramVersion, MiniprogramVersionDao, Integer>
		implements MiniprogramVersionService {

	@Autowired
	private VersionManageApiService vService;
	@Autowired
	private AbstractWeChatInfoService aService;
	@Autowired
	private MiniprogramCodeService cService;

	private ValidateToken getToken(String appId) {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(appId);
		return validateToken;
	}

	@Override
	public boolean submitVersion(String appId) throws GlobalException {
		/**
		 * 当和微信请求后，返回状态而后修改其状态：成功/失败
		 */
		MiniprogramCode mCode = cService.getNew();
		MiniprogramVersion mVersion = new MiniprogramVersion();
		mVersion.setTemplateId(mCode.getTemplateId());
		mVersion.setCodeVersion(mCode.getCodeVersion());
		mVersion.setCodeDesc(mCode.getCodeDesc());
		mVersion.setAppId(appId);
		mVersion.setVersionType(1);
		dao.deleteVersion(mVersion.getAppId(), 1);
		super.save(mVersion);
		Ext ext = new Ext();
		ext.setAppId(mVersion.getAppId());
		JSONObject object = new JSONObject();
		object.put("ext", ext);
		CommitRequest request = new CommitRequest();
		request.setTemplateId(mVersion.getTemplateId());
		request.setUserVersion(mVersion.getCodeVersion());
		request.setUserDesc(mVersion.getCodeDesc());
		request.setExtJson(object.toString());
		ValidateToken validateToken = this.getToken(mVersion.getAppId());
		CommitResponse response = vService.commit(request, validateToken);
		if (!Const.Mssage.SUCCESS_CODE.equals(response.getErrcode())) {
			/**
			 * 需要将此处的英文提示修改成为中文提示，微信返回的errcode实际上是-8000(负数)，
			 * 而在OpenReturnCode写的状态码则是8000(正数)，而-1在OpenReturnCode中写的则是
			 */
			Integer errcode = Integer.valueOf(response.getErrcode());
			if (errcode != -1) {
				errcode = Math.abs(errcode);
			}
			mVersion.setFailReason(OpenReturnCode.get(errcode));
			mVersion.setStatus(3);
			Date date = new Date();
			mVersion.setAuditTime(date);
			super.update(mVersion);
			return false;
		} else {
			mVersion.setStatus(2);
			super.update(mVersion);
			return true;
		}
	}

	@Override
	public MiniprogramVersion getSubmitFail(String appId) throws GlobalException {
		return dao.getSubmitFail(appId, 1, 3);
	}

	// TODO 底下校验考虑为抛出异常
	
	@Override
	public void auditVersion(String appId, List<AuditVersionDto> aDtos) throws GlobalException {
		MiniprogramVersion oldAuditVersion = dao.getSubmitFail(appId, 2, 1);
		// 如果审核记录不为空，直接停止
		if (oldAuditVersion != null) {
			return;
		}
		MiniprogramVersion mVersion = dao.getSubmitFail(appId, 1, 2);
		// 如果没有提交成功记录，直接停止
		if (mVersion == null) {
			return;
		}
		MiniprogramVersion auditVersion = new MiniprogramVersion();
		auditVersion.setVersionType(2);
		auditVersion.setStatus(1);
		auditVersion.setAppId(mVersion.getAppId());
		auditVersion.setTemplateId(mVersion.getTemplateId());
		auditVersion.setCodeVersion(mVersion.getCodeVersion());
		auditVersion.setCodeDesc(mVersion.getCodeDesc());
		dao.deleteVersion(appId, 2);
		super.save(auditVersion);

		List<AuditList> itemList = new ArrayList<>();
		AuditList auditList;
		for (AuditVersionDto aDto : aDtos) {
			auditList = new AuditList();
			CategoryDto cDto = aDto.getCategoryDto();
			auditList.setAddress(aDto.getAddress());
			auditList.setFirstClass(cDto.getFirstClass());
			auditList.setFirstId(cDto.getFirstId());
			auditList.setSecondClass(cDto.getSecondClass());
			auditList.setSecondId(cDto.getSecondId());
			auditList.setThirdClass(cDto.getThirdClass());
			auditList.setThirdId(cDto.getThirdId());
			auditList.setTag(aDto.getTag());
			auditList.setTitle(aDto.getTitle());
			itemList.add(auditList);
		}
		ValidateToken validateToken = this.getToken(appId);
		SubmitAuditRequest request = new SubmitAuditRequest(itemList);
		SubmitAuditResponse response = vService.submitAudit(request, validateToken);
		if (response.getAuditid() != null) {
			auditVersion.setAuditid(response.getAuditid());
			auditVersion.setStatus(1);
			super.update(auditVersion);
		} else {
			/** 需要将此处的英文提示修改成为中文提示 */
			Integer errcode = Integer.valueOf(response.getErrcode());
			if (errcode != -1) {
				errcode = Math.abs(errcode);
			}
			auditVersion.setFailReason(OpenReturnCode.get(errcode));
			auditVersion.setStatus(3);
			Date date = new Date();
			auditVersion.setAuditTime(date);
			super.update(auditVersion);
		}
	}

	@Override
	public MiniprogramVersion getAudit(String appId) throws GlobalException, ParseException {
		ValidateToken validateToken = this.getToken(appId);
		MiniprogramVersion mVersion = dao.getAudit(appId, 2);
		if (mVersion != null && mVersion.getStatus() == 1) {
			GetAudistatusResponse response = vService.getAudistatus(
					new GetAudistatusRequest(mVersion.getAuditid()),validateToken);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			switch (response.getStatus()) {
				case 0:
					mVersion.setStatus(2);
					mVersion.setAuditTime(dateFormat.parse(dateFormat.format(new Date())));
					super.update(mVersion);
					break;
				case 1:
					mVersion.setStatus(3);
					mVersion.setAuditTime(dateFormat.parse(dateFormat.format(new Date())));
					mVersion.setFailReason(response.getReason());
					super.update(mVersion);
					break;
				default:
					break;
			}
		}
		return mVersion;
	}

	@Override
	public void releaseVersion(String appId, Integer id) throws GlobalException {
		MiniprogramVersion mVersion = super.findById(id);
		MiniprogramVersion releaseVersion = new MiniprogramVersion();
		releaseVersion.setVersionType(3);
		releaseVersion.setAppId(mVersion.getAppId());
		releaseVersion.setTemplateId(mVersion.getTemplateId());
		releaseVersion.setCodeVersion(mVersion.getCodeVersion());
		releaseVersion.setCodeDesc(mVersion.getCodeDesc());
		releaseVersion.setAction("open");
		dao.deleteVersion(appId, 3);
		super.save(releaseVersion);
		ValidateToken validateToken = this.getToken(appId);
		ReleaseRequest request = new ReleaseRequest();
		ReleaseResponse response = vService.release(request, validateToken);
		if (!Const.Mssage.SUCCESS_CODE.equals(response.getErrcode())) {
			/** 需要将此处的英文提示修改成为中文提示 */
			Integer errcode = Integer.valueOf(response.getErrcode());
			if (errcode != -1) {
				errcode = Math.abs(errcode);
			}
			mVersion.setFailReason(OpenReturnCode.get(errcode));
			mVersion.setStatus(3);
			super.update(mVersion);
		} else {
			mVersion.setStatus(2);
			super.update(mVersion);
		}
	}

	@Override
	public void updateAuditStatus(String appId, ReqMessage reqMessage) throws GlobalException, ParseException {
		MiniprogramVersion mVersion = dao.getAudit(appId, 2);
		if (mVersion != null && mVersion.getStatus() == 1) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (reqMessage.getSuccTime() != null) {
				mVersion.setStatus(2);
				mVersion.setAuditTime(
						dateFormat.parse(dateFormat.format(reqMessage.getSuccTime() * 1000)));
				super.update(mVersion);
			} else {
				mVersion.setFailReason(reqMessage.getReason());
				mVersion.setAuditTime(
						dateFormat.parse(dateFormat.format(reqMessage.getFailTime() * 1000)));
				mVersion.setStatus(3);
				super.update(mVersion);
			}
		}

	}

	@Override
	public MiniprogramVersion getRelease(String appId) throws GlobalException {
		return dao.getAudit(appId, 3);
	}

	@Override
	public void updateReleaseStatus(MiniprogramVersion mVersion) throws GlobalException {
		ValidateToken validateToken = this.getToken(mVersion.getAppId());
		ChangeVisitstatusRequest request = new ChangeVisitstatusRequest(mVersion.getAction());
		vService.changeVisitstatus(request, validateToken);
		super.update(mVersion);
	}

	@Override
	public ResponseInfo deleteAudit(MiniprogramVersion mVersion) throws GlobalException {
		ValidateToken validateToken = this.getToken(mVersion.getAppId());
		UndocodeauditResponse response = vService.undocodeaudit(validateToken);
		if (Const.Mssage.SUCCESS_CODE.equals(response.getErrcode())) {
			super.physicalDelete(mVersion);
			return new ResponseInfo(true);
		} else {
			/** 需要将此处的英文提示修改成为中文提示 */
			Integer errcode = Integer.valueOf(response.getErrcode());
			if (errcode != -1) {
				errcode = Math.abs(errcode);
			}
			throw new GlobalException(new WeChatExceptionInfo(errcode+"", OpenReturnCode.get(errcode)));
		}

	}

	@Override
	public ResponseInfo getPage(String appId) throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		GetPageResponse response = vService.getPage(validateToken);
		return new ResponseInfo(response.getPageList());
	}

	@Override
	public ResponseInfo getCategory(String appId) throws GlobalException {
		String categories = aService.findAppId(appId).getCategories();
		List<CategoryDto> categorys = JSONArray.parseArray(categories, CategoryDto.class);
		for (int i = 0; i < categorys.size(); i++) {
			categorys.get(i).setId(i);
		}
		return new ResponseInfo(categorys);
	}

	@Override
	public MiniprogramVersionGetButton isShow(String appId) throws GlobalException {
		/**
		 * 后面判断mCode!=null，是处理极端情况：即用户本身有模板同步后执行了提交审核的工作，但是后面又到开放平台中删除所有模板，再次执行
		 * 同步操作，这时getNew就为空了
		 */
		Boolean returnState = false;
		MiniprogramVersion mVersion;
		MiniprogramCode mCode = cService.getNew();
		Integer templateId;
		Map<String, Boolean> returnMap = new HashMap<>(16);
//		do {
		// 显示上传代码按钮 1. 没有一个正在审核的版本 2. 有一个正在审核的版本，但此版本与版本库中最新的版本不一致
		// 是否有正在审核
		mVersion = dao.haveState(appId, 2, 1);
		if (mVersion != null) {
			templateId = mVersion.getTemplateId();
			if (mCode != null) {
				// Integer在-127-128是属于一个变量存在内存中，但如果不在这个范围内就会new有一个对象对象与对象使用==比较容易出错
				if (!templateId.equals(mCode.getTemplateId())) {
					returnState = true;
				}
			}
		} else {
			returnState = mCode != null ? true : false;
		}
		returnMap.put("1", returnState);
		returnState = false;
		// 显示提交发布按钮 有一个审核成功的版本 1. 没有一个发布版本 2. 有一个发布版本，但此版本与版本库中最新的版本不一致
		// 审核成功
		mVersion = dao.haveState(appId, 2, 2);
		if (mVersion != null) {
			/** 发布版本 */
			MiniprogramVersion miniprogramVersion = dao.noteState(appId, 3);
			templateId = mVersion.getTemplateId();
			if (miniprogramVersion != null) {
				if (!miniprogramVersion.getTemplateId().equals(templateId.intValue())) {
					returnState = true;
				}
			} else {
				returnState = true;
			}
		}
		returnMap.put("2", returnState);
		returnState = false;
		// 显示提交审核按钮 有一个提交成功的版本 1. 没有一个正在审核的版本
		/** 提交成功 */
		mVersion = dao.haveState(appId, 1, 2);
		if (mVersion != null) {
			/** 正在审核 */
			MiniprogramVersion miniprogramVersion = dao.haveState(appId, 2, 1);
			returnState = miniprogramVersion != null ? false : true;
		}
		returnMap.put("3", returnState);
		returnState = false;
		// 撤回审核按钮
		/** 正在审核 */
		mVersion = dao.haveState(appId, 2, 1);
		returnState = mVersion != null ? true : false;
		returnMap.put("4", returnState);
		returnState = false;
		// 重新审核
		/** 审核失败 */
		mVersion = dao.haveState(appId, 2, 3);
		returnState = mVersion != null ? true : false;
		returnMap.put("5", returnState);
		returnState = false;
		// 更新版本提示 有发布版本 1. 发布版本与版本库中的版本不一致
		/** 是否有发布版本 */
		mVersion = dao.noteState(appId, 3);
		if (mVersion != null) {
			if (mCode != null) {
				if (!mVersion.getTemplateId().equals(mCode.getTemplateId())) {
					returnState = true;
				}
			}
		}
		returnMap.put("6", returnState);
		returnState = false;
		// 是否可以调用二维码 1. 有一个审核版本(审核成功失败无所谓)
		/** 是否有审核版本 */
		mVersion = dao.noteState(appId, 2);
		returnState = mVersion != null ? true : false;
		returnMap.put("7", returnState);
//		} while (false);
		MiniprogramVersionGetButton button = new MiniprogramVersionGetButton(returnMap);
		return button;
	}

	@Override
	public String getQrcode(String appId) throws Exception {
		ValidateToken validateToken = this.getToken(appId);
		MediaFile mediaFile = vService.getQrcode(validateToken);
		byte[] bytes = mediaFile.getFileStream().toByteArray();
		return Base64Utils.imageToBase64ByByte(bytes);
	}
}