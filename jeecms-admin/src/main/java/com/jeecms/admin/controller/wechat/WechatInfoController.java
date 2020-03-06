package com.jeecms.admin.controller.wechat;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.ueditor.ResourceType;
import com.jeecms.common.wechat.api.applet.GetQrcodeApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.resource.service.impl.UploadService;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.dto.WechatInfoAddCoreUserDto;
import com.jeecms.wechat.service.AbstractWeChatInfoService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 授权管理
 * @author: chenming
 * @date:   2019年6月10日 下午6:00:29
 */
@RequestMapping(value = "/wechatInfo")
@RestController
public class WechatInfoController extends BaseController<AbstractWeChatInfo, Integer> {
	
	@Autowired
	private AbstractWeChatInfoService service;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private GetQrcodeApiService getQrcodeApiService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private CmsSiteService cmsSiteService;
	
	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}
	
	/**
	 * 分页查询公众号列表 （只能查询出公众号列表，小程序不在查询范围）
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = AbstractWeChatInfo.class, includes = { 
					"id", "headImg", "wechatName", "wechatType", "isSetAdmin", "isDefaultAuth",
					"verifyStatus", "type", "createTime", "createUser", "isDefaultAuth","appId","userNames" }),
			})
	public ResponseInfo page(HttpServletRequest request, @RequestParam(value = "type") Integer type,
			@PageableDefault(sort = "id", direction = Direction.ASC) Pageable pageable) 
					throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		Map<String, String[]> map = new HashMap<String, String[]>(2);
		map.put("EQ_type_Integer", new String[] {String.valueOf(type)});
		map.put("EQ_siteId_Integer", new String[] {String.valueOf(siteId)});
		super.setSearchParams(map);
		return super.getPage(request, pageable, false);
	}
	
	/**
	 * 查询小程序、公众号列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = AbstractWeChatInfo.class, includes = { 
					"id", "headImg", "wechatName", "wechatType", "isSetAdmin", "isDefaultAuth",
					"verifyStatus", "type", "createTime", "createUser", "isDefaultAuth","appId" }),
			})
	public ResponseInfo page(HttpServletRequest request, @RequestParam(value = "type") Short type,
			@PageableDefault(sort = "id", direction = Direction.ASC) Pageable pageable) 
					throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		CoreUser user = SystemContextUtils.getCoreUser();
		if (user == null) {
			return new ResponseInfo();
		}
		List<AbstractWeChatInfo> infos = user.getWechatInfos();
		if (infos != null && infos.size() > 0) {
			infos = infos.stream()
					.filter(info -> info.getSiteId().equals(siteId) && info.getType().equals(type) 
							&& !info.getHasDeleted())
					.collect(Collectors.toList());
		}
		return new ResponseInfo(infos);
	}
	
	/**
	 * 获取单个信息
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@MoreSerializeField({ 
		@SerializeField(clazz = AbstractWeChatInfo.class, includes = { 
				"type","headImg","wechatName","principalName","wechatType","verifyStatus","appId",
				"globalId","grantType","qrcodeUrl","funcInfo","createUser","createTime",
				"isDefaultAuth"})
	})
	@Override
	public ResponseInfo get(@PathVariable(name = "id") Integer id) throws GlobalException {
		AbstractWeChatInfo info = service.findById(id);
		String qrcodeUrl = info.getQrcodeUrl();
		if (AbstractWeChatInfo.TYPE_MINIPROGRAM.equals(info.getType())) {
			if (StringUtils.isBlank(qrcodeUrl)) {
				ValidateToken validateToken = new ValidateToken();
				validateToken.setAppId(info.getAppId());
				MediaFile mediaFile = getQrcodeApiService.getQrcode(validateToken);
				if (mediaFile != null) {
					File file = null;
					try {
						file = File.createTempFile(info.getAppId() + "qrcodeUtl",
								".jpg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					try (OutputStream output = new FileOutputStream(file);
							BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);) {
						bufferedOutput.write(mediaFile.getFileStream().toByteArray());
					} catch (Exception e) {
						e.printStackTrace();
					} 
					CmsSite site = cmsSiteService.findById(info.getSiteId());
					qrcodeUrl = uploadService.doUpload(file, false, null, ResourceType.IMAGE, site).getFileUrl();
				}
			}
			info.setQrcodeUrl(qrcodeUrl);
			AbstractWeChatInfo bean = service.update(info);
			return new ResponseInfo(bean);
		}
		return new ResponseInfo(info);
	}
	
	/**
	 * 取消授权，此处取消授权仅仅是删除数据库中的一条记录
	 * 如果在微信端取消授权->WechatContent-authCallBack接受数据进行业务操作
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) 
			throws GlobalException {
		super.checkServerMode();
		super.validateBindingResult(result);
		AbstractWeChatInfo info = service.findById(ids.getIds()[0]);
		List<AbstractWeChatInfo> infos = service.findWeChatInfo(info.getType(), info.getSiteId());
		// 此处判断本身正常情况下无需进行此处判断，但是需要取消它人调用接口，但是传入一个已经删除的对象
		if (infos != null && infos.size() > 0) {
			if (infos.size() > 1) {
				// 不止存在最后一个数据，判断，如果是默认则直接抛出异常
				if (info.getIsDefaultAuth()) {
					return new ResponseInfo(
							RPCErrorCodeEnum.UNABLE_TO_DEAUTHORIZE_BY_DEFAULT.getCode(),
							RPCErrorCodeEnum.UNABLE_TO_DEAUTHORIZE_BY_DEFAULT
							.getDefaultMessage());
				} else {
					service.delete(info);
				}
			// 是最后一个数据，无需判断，直接删除即可 
			} else {
				service.delete(info);
			}
		}
		return new ResponseInfo();
	}

	/**
	 * 设置默认登录授权账号
	 */
	@RequestMapping(value = "/default", method = RequestMethod.GET)
	public ResponseInfo setDefault(HttpServletRequest request,@RequestParam(value = "id") Integer id) 
			throws GlobalException {
		AbstractWeChatInfo info = service.findById(id);
		/** 如果是开启了默认快捷登录的账号或者是删除的对象，则直接抛出啊异常*/
		if (info.getIsDefaultAuth() || info.getHasDeleted()) {
			return new ResponseInfo(false);
		}
		AbstractWeChatInfo defaultInfo = service.findDefault(info.getType(), info.getSiteId());
		List<AbstractWeChatInfo> infos = new ArrayList<AbstractWeChatInfo>();
		if (defaultInfo != null) {
			defaultInfo.setIsDefaultAuth(false);
			infos.add(defaultInfo);
		}
		info.setIsDefaultAuth(true);
		infos.add(info);
		service.batchUpdate(infos);
		return new ResponseInfo(true);
	}
	
	// TODO 底下的所有id都必须进行小程序校验
	
	/**
	 * 获取当前小程序最新服务类目
	 */
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public ResponseInfo getCategory(@RequestParam(value = "id") Integer id) throws GlobalException {
		this.checkId(id, AbstractWeChatInfo.TYPE_MINIPROGRAM);
		return service.getCategory(id);
	}

	/**
	 * 显示服务器域名
	 */
	@RequestMapping(value = "/displayModifyDomain/{id}", method = RequestMethod.GET)
	public ResponseInfo displayModifyDomain(@PathVariable(name = "id") Integer id) throws GlobalException {
		return service.getModifyDomain(id);
	}

	/**
	 * 重置服务器域名
	 */
	@RequestMapping(value = "/modifyDomain/{id}", method = RequestMethod.GET)
	public ResponseInfo modifyDomain(@PathVariable(name = "id") Integer id,HttpServletRequest request) 
			throws GlobalException {
		return service.setModifyDomain(request,id);
	}
	
	/**
	 * 校验id值
	 */
	private void checkId(Integer id, Short type) throws GlobalException {
		AbstractWeChatInfo info = service.findById(id);
		if (!type.equals(info.getType())) {
			throw new GlobalException(
					new SystemExceptionInfo(
						RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage(),
						RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode()));
		}
	}
	
	/**
	 * 设置管理员
	 */
	@RequestMapping(value = "/coreUser", method = RequestMethod.POST)
	public ResponseInfo addCoreUser(@RequestBody WechatInfoAddCoreUserDto dto) throws GlobalException {
		AbstractWeChatInfo info = service.findById(dto.getId());
		List<CoreUser> coreUsers = coreUserService.findAllById(Arrays.asList(dto.getUserIds()));
		// 如果传入的用户数组中存在不是管理员的用户，直接抛出异常
		for (CoreUser coreUser : coreUsers) {
			if (!coreUser.getAdmin()) {
				return new ResponseInfo(UserErrorCodeEnum.USER_MUST_BE_AN_ADMINISTRATOR.getCode(),
						UserErrorCodeEnum.USER_MUST_BE_AN_ADMINISTRATOR.getDefaultMessage());
			}
		}
		info.setUsers(coreUsers);
		// 如果判断是没有设置管理员，则将状态改成true，否则不进行其它处理
		if (info.getUsers() != null && info.getUsers().size() > 0) {
			info.setIsSetAdmin(true);
		} else {
			info.setIsSetAdmin(false);
		}
		service.update(info);
		return new ResponseInfo(true);
	}
	
	/** 分页查询管理员*/
	@RequestMapping(value = "/coreUser/list", method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = CoreUser.class, includes = { "id","userExt",
				"username","org","roleNames"}),
		@SerializeField(clazz = CoreUserExt.class, includes = { "realname"}),
		@SerializeField(clazz = CmsOrg.class, includes = { "name"}),
	})
	public ResponseInfo getCoreUserPage(
			@RequestParam(value = "orgId", required = false) Integer orgId,
			@RequestParam(value = "roleid", required = false) Integer roleid,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "id") Integer id) throws GlobalException {
		AbstractWeChatInfo info = service.findById(id);
		if (info == null) {
			return new ResponseInfo();
		}
		List<CoreUser> coreUsers = info.getUsers();
		List<Integer> ids = null;
		if (coreUsers != null && coreUsers.size() > 0) {
			ids = coreUsers.stream().map(CoreUser::getId).collect(Collectors.toList());
		}
		List<CoreUser> coreUserList = coreUserService.listThirdManager(ids, orgId, roleid, username);
		return new ResponseInfo(coreUserList);
		
	}
}
