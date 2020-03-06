package com.jeecms.admin.controller.wechat;

import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.MiniprogramCode;
import com.jeecms.wechat.domain.MiniprogramVersion;
import com.jeecms.wechat.domain.MiniprogramVersion.UpdateReleaseStatus;
import com.jeecms.wechat.domain.dto.MiniprogramAuditDto;
import com.jeecms.wechat.domain.dto.MiniprogramVersionOperateDto;
import com.jeecms.wechat.domain.dto.MiniprogramVersionOperateDto.IncludeId;
import com.jeecms.wechat.domain.dto.MiniprogramVersionOperateDto.NotIncludeId;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.MiniprogramCodeService;
import com.jeecms.wechat.service.MiniprogramVersionService;

/**
 * 小程序代码版本管理controller层
 * 
 * @author: chenming
 * @date: 2019年6月10日 上午10:11:06
 */
@RequestMapping("/miniprogramversion")
@RestController
public class MiniprogramVersionController extends BaseController<MiniprogramVersion, Integer> {
	
	@Autowired
	private MiniprogramVersionService vService;
	@Autowired
	private MiniprogramCodeService cService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	
	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 提交代码
	 */
	@PostMapping("/submit")
	public ResponseInfo addSubmitVersion(
			@RequestBody @Validated(value = NotIncludeId.class) MiniprogramVersionOperateDto dto,
			BindingResult result, HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, dto.getAppId());
		MiniprogramCode mCode = cService.getNew();
		if (mCode == null) {
			return new ResponseInfo(RPCErrorCodeEnum.NO_LATEST_TEMPLATE_SET.getCode(),
					RPCErrorCodeEnum.NO_LATEST_TEMPLATE_SET.getDefaultMessage());
		}
		boolean returnCode = vService.submitVersion(dto.getAppId());
		return new ResponseInfo(returnCode);
	}

	/**
	 * 获取提交代码失败记录，如果没有提交代码失败记录，即获取最新模板数据
	 */
	@GetMapping("/submit/record")
	@MoreSerializeField({
			@SerializeField(clazz = MiniprogramVersion.class, includes = { 
					"codeVersion", "failReason", "createTime" }),
			@SerializeField(clazz = MiniprogramCode.class, includes = { "codeVersion", "createTime" }) 
	})
	public ResponseInfo getNewVersion(@RequestParam String appId) throws GlobalException {
		MiniprogramVersion mVersion = vService.getSubmitFail(appId);
		MiniprogramCode mCode = cService.getNew();
		if (mVersion != null && mCode != null && mVersion.getTemplateId().equals(mCode.getTemplateId())) {
			return new ResponseInfo(mVersion);
		} else {
			return new ResponseInfo(mCode);
		}
	}

	/**
	 * 获取小程序的第三方提交代码的页面配置
	 */
	@GetMapping("/leaf")
	public ResponseInfo getPage(@RequestParam String appId) throws GlobalException {
		return vService.getPage(appId);
	}

	/**
	 * 获取授权小程序帐号的可选类目
	 */
	@GetMapping("/category")
	public ResponseInfo getCategory(@RequestParam String appId) throws GlobalException {
		return vService.getCategory(appId);
	}

	/**
	 * 提交审核
	 */
	@PostMapping("/audit")
	public ResponseInfo addAuditVersion(@RequestBody @Valid MiniprogramAuditDto dto, 
			BindingResult result, HttpServletRequest request)throws GlobalException {
		super.validateBindingResult(result);
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, dto.getAppId());
		vService.auditVersion(dto.getAppId(), dto.getVersionDto());
		return new ResponseInfo(true);

	}

	/**
	 * 获取审核数据
	 */
	@GetMapping("/audit")
	@SerializeField(clazz = MiniprogramVersion.class, includes = { "id", "appId", "codeVersion", 
			"createTiem", "status", "failReason", "auditTime", "createTime" })
	public ResponseInfo getAudit(@RequestParam String appId) 
			throws GlobalException, ParseException {
		return new ResponseInfo(vService.getAudit(appId));
	}

	/**
	 * 撤销审核
	 */
	@DeleteMapping("/audit")
	public ResponseInfo deleteAudit(@RequestBody @Valid DeleteDto ids, 
			BindingResult result, HttpServletRequest request)throws GlobalException {
		super.validateBindingResult(result);
		MiniprogramVersion version = vService.findById(ids.getIds()[0]);
		if (version == null) {
			return new ResponseInfo();
		}
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, version.getAppId());
		if (!MiniprogramVersion.AUDIT_VERSION.equals(version.getVersionType())) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		return vService.deleteAudit(version);
	}

	/**
	 * 提交发布
	 */
	@PostMapping("/release")
	public ResponseInfo addRelease(
			@RequestBody @Validated(value = IncludeId.class)MiniprogramVersionOperateDto dto,
			BindingResult result,HttpServletRequest request) throws GlobalException {
		super.validateBindingResult(result);
		MiniprogramVersion version = vService.findById(dto.getId());
		if (version == null) {
			return new ResponseInfo();
		}
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, version.getAppId());
		if (MiniprogramVersion.AUDIT_VERSION.equals(version.getVersionType())
				&&
			version.getStatus().equals(2)) {
			vService.releaseVersion(version.getAppId(), dto.getId());
		} else {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		return new ResponseInfo(true);
	}

	/**
	 * 获取发布版本
	 */
	@GetMapping("/release")
	@SerializeField(clazz = MiniprogramVersion.class, includes = { "appId", "id", "codeVersion", "action",
			"createTime" })
	public ResponseInfo getRelease(@RequestParam String appId) throws GlobalException {
		return new ResponseInfo(vService.getRelease(appId));
	}

	/**
	 * 修改发布版本状态
	 */
	@PutMapping("/release/status")
	public ResponseInfo updateReleaseStatus(
			@RequestBody @Validated(value = UpdateReleaseStatus.class)MiniprogramVersion mVersion,
			HttpServletRequest request)throws GlobalException {
		MiniprogramVersion newVersion = vService.findById(mVersion.getId());
		if (newVersion == null) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, newVersion.getAppId());
		if (!MiniprogramVersion.RELEASE_VERSION.equals(newVersion.getVersionType())) {
			return new ResponseInfo(RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage());
		}
		String action = mVersion.getAction();
		if (MiniprogramVersion.VERSION_CLOSE.equals(action) 
				|| 
			MiniprogramVersion.VERSION_OPEN.equals(action)) {
			if (mVersion.getAction().equals(newVersion.getAction())) {
				return new ResponseInfo();
			}
			newVersion.setAction(mVersion.getAction());
			vService.updateReleaseStatus(newVersion);
			return new ResponseInfo(true);
		} else {
			return new ResponseInfo(RPCErrorCodeEnum.ONLINE_VERSION_STATUS_ERR.getCode(),
					RPCErrorCodeEnum.ONLINE_VERSION_STATUS_ERR.getDefaultMessage());
		}
	}

	/**
	 * 1. 显示上传代码按钮 2. 显示提交发布按钮 3. 显示提交审核按钮 4. 撤回审核按钮 5. 重新审核按钮 6. 更新版本提示,7.是否可以调用二维码
	 */
	@GetMapping("/button")
	public ResponseInfo isShow(@RequestParam String appId) throws GlobalException {
		return new ResponseInfo(vService.isShow(appId));
	}

	/**
	 * 获取体验版二维码
	 */
	@GetMapping("/qrcode")
	public ResponseInfo getQrcode(@RequestParam String appId,HttpServletRequest request) throws Exception {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		return new ResponseInfo(vService.getQrcode(appId));
	}
	
}
