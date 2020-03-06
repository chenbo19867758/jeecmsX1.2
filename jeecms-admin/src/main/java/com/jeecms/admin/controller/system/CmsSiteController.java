/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.jeecms.admin.controller.BaseTreeAdminController;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.local.ThreadPoolService;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.util.TplUtils;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.resource.service.TplResourceService;
import com.jeecms.resource.service.TplService;
import com.jeecms.resource.service.UploadOssService;
import com.jeecms.system.domain.CmsDataPerm.OpeSiteEnum;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.CmsSiteExt;
import com.jeecms.system.domain.SiteModelTpl;
import com.jeecms.system.domain.dto.CmsSiteAgent;
import com.jeecms.system.domain.dto.CmsSiteExtDto;
import com.jeecms.system.domain.vo.CmsSiteVo;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.system.service.CmsSiteExtService;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.FtpService;
import com.jeecms.system.service.SiteModelTplService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jeecms.common.constants.TplConstants.TPL_BASE;
import static com.jeecms.common.constants.WebConstants.SPT;

/**
 * 站点管理
 *
 * @author: tom/ljw
 * @date: 2018年3月9日 下午3:22:24
 */
@RequestMapping("/sites")
@RestController
public class CmsSiteController extends BaseTreeAdminController<CmsSite, Integer> {

	private static final Logger logger = LoggerFactory.getLogger(CmsSiteController.class);

	/**
	 * 获取用户可查看站点
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public ResponseInfo getViewSites(HttpServletRequest request, Paginable paginable) throws GlobalException {
		// 获取用户可查看站点
		List<CmsSite> siteList = SystemContextUtils.getUser(request).getViewSites();
		if (!siteList.isEmpty()) {
			// 过滤加入回收站，删除
			siteList = siteList.stream().filter(x -> !x.getIsDelete()).filter(x -> !x.getHasDeleted())
					.collect(Collectors.toList());
		}
		siteList = CmsSiteAgent.sortBySortNumAndChild(siteList);
		JSONArray responseData = null;
		responseData = super.getChildTree(siteList, false, "name", "domain", "path", "isOpen", "previewUrl", 
				"newChildAble", "deleteAble");
		return new ResponseInfo(responseData);
	}

	/**
	 * 获取用户可管理站点列表
	 */
	@RequestMapping(value = "/ownsite", method = RequestMethod.GET)
	@SerializeField(clazz = CmsSite.class, includes = { "id", "name" })
	public ResponseInfo getOwnerSites(HttpServletRequest request, Paginable paginable) throws GlobalException {
		// 获取用户可管理站点
		List<Integer> siteList = SystemContextUtils.getUser(request).getOwnerSiteIds();
		List<CmsSite> sites = siteService.findByIds(siteList);
		if (!sites.isEmpty()) {
			// 过滤加入回收站，删除
			sites = sites.stream().filter(x -> !x.getIsDelete()).filter(x -> !x.getHasDeleted())
					.collect(Collectors.toList());
		}
		sites = CmsSiteAgent.sortBySortNumAndChild(sites);
		return new ResponseInfo(sites);
	}

	/**
	 * 获取用户可站点推送的站点树型结构
	 */
	@RequestMapping(value = "/ownsitepush", method = RequestMethod.GET)
	public ResponseInfo ownsitePush(HttpServletRequest request) throws GlobalException {
		// 获取用户可管理站点
		List<Integer> siteList = SystemContextUtils.getUser(request).getOwnerSiteIds();
		List<CmsSite> sites = service.findAllById(siteList);
		if (!sites.isEmpty()) {
			// 过滤加入回收站，删除,是否接受站群推送
			sites = sites.stream()
					.filter(x -> !x.getIsDelete())
					.filter(x -> !x.getHasDeleted())
					.filter(x -> x.getSitePushOpen()).collect(Collectors.toList());
		}
		sites = CmsSiteAgent.sortByIdAndChild(sites);
		JSONArray responseData = null;
		responseData = super.getChildTree(sites, false, "name");
		return new ResponseInfo(responseData);
	}

	/**
	 * 查询用户站群权限的站点树(与上面ownsitepush接口功能一致)
	 */
	@RequestMapping(value = "/getOwnerTree", method = RequestMethod.GET)
	public ResponseInfo getOwnerTree(HttpServletRequest request, Paginable paginable) throws GlobalException {
		Set<CmsSite> sites = SystemContextUtils.getUser(request).getOwnerSites();
		JSONArray responseData = null;
		List<CmsSite> ownerSites = CmsSiteAgent.sortByIdAndChild(sites);
		Integer siteId = SystemContextUtils.getSiteId(request);
		if (!sites.isEmpty()) {
			// 过滤加入回收站，删除,是否接受站群推送
			ownerSites = ownerSites.stream()
					.filter(x -> !x.getIsDelete())
					.filter(x -> !x.getHasDeleted())
					.filter(x -> x.getSitePushOpen())
					.filter(x -> !x.getId().equals(siteId)).collect(Collectors.toList());
		}
		responseData = super.getChildTree(ownerSites, false, "name");
		return new ResponseInfo(responseData);
	}

	/**
	 * 获取站点列表
	 */
	@GetMapping
	@SerializeField(clazz = CmsSite.class, includes = { "id", "name", "domain", "path", "isOpen" })
	public ResponseInfo getChild(@RequestParam(value = "parentId", required = false) Integer parentId)
			throws GlobalException {
		List<CmsSite> sites = new ArrayList<CmsSite>(10);
		if (parentId != null) {
			sites = siteService.findByParentId(parentId);
		} else {
			sites = siteService.findByParentId(null);
		}
		if (!sites.isEmpty()) {
			// 过滤加入回收站，删除
			sites = sites.stream().filter(x -> !x.getIsDelete()).filter(x -> !x.getHasDeleted())
					.collect(Collectors.toList());
		}
		sites = CmsSiteAgent.sortByIdAndChild(sites);
		return new ResponseInfo(sites);
	}

	/**
	 * 基本信息详情
	 */
	@GetMapping(value = "/{id}")
	@MoreSerializeField({ @SerializeField(clazz = CmsSite.class, includes = { "id", "name", "path", "protocol",
			"isOpen", "iconPath", "description", "domain", "siteLanguage", "seoTitle", "seoKeyword", "domainAlias",
			"iconId", "seoDescription", "parentId", "nodeSiteIds", "alias", "url", "previewUrl",
			"deleteAble", "editAble", "openCloseAble", //权限
			"viewAble", "permAssignAble", "newChildAble", "staticAble" }), })//权限
	@Override
	public ResponseInfo get(@PathVariable Integer id) throws GlobalException {
		/**管理后台默认定是PC访问，获取站点首页url需要*/
		SystemContextUtils.setMobile(false);
		SystemContextUtils.setTablet(false);
		SystemContextUtils.setPc(true);
		return super.get(id);
	}

	/**
	 * 扩展配置详情
	 */
	@GetMapping(value = "/ext")
	@MoreSerializeField({
			@SerializeField(clazz = CmsSite.class, includes = { "id", "cfg", "cmsSiteExt", "uploadPicSuffix",
					"uploadVideoSuffix", "uploadAttachmentSuffix", "uploadAudioSuffix", "uploadDocumentSuffix" }),
			@SerializeField(clazz = CmsSiteExt.class, includes = { "commentFlowId", "newContentResourceId",
					"staticPageFtpId", "staticPageOssId", "uploadFtpId", "uploadOssId", "watermarResourceId" }), })
	public ResponseInfo ext(Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 默认模板详情
	 */
	@GetMapping(value = "/model")
	@MoreSerializeField({ @SerializeField(clazz = CmsModel.class, includes = { "id", "modelName", "tplType" }), })
	public ResponseInfo model(Integer id) throws GlobalException {
		CmsSiteVo vo = siteService.getModel(id);
		return new ResponseInfo(vo);
	}

	/**
	 * 新增站点
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Valid CmsSite site, BindingResult result, HttpServletRequest request)
			throws GlobalException {
		super.validateBindingResult(result);
		CmsSite currSite = SystemContextUtils.getSite(request);
		CoreUser currUser = SystemContextUtils.getUser(request);
		if (site.getIconId() != null) {
			site.setIconRes(spaceDataService.findById(site.getIconId()));
		}
		//检查权限
		super.checkSiteDataPerm(site.getParentId(), OpeSiteEnum.NEWCHILD);
		CmsSite newSite = siteService.save(currSite, currUser, site);
		return new ResponseInfo(newSite.getId());
	}

	/**
	 * 修改站点基本信息
	 */
	@PutMapping()
	public ResponseInfo update(HttpServletRequest request, @Valid @RequestBody CmsSite site, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		// 判断站点路径是否存在
		Boolean flags = siteService.existSitePath(site.getId(), site.getDomain());
		if (flags) {
			return new ResponseInfo(SiteErrorCodeEnum.SITE_PATH_ERROR.getCode(),
					SiteErrorCodeEnum.SITE_PATH_ERROR.getDefaultMessage());
		}
		if (site.getIconId() != null) {
			site.setIconRes(spaceDataService.findById(site.getIconId()));
		}
		//检查权限
		super.checkSiteDataPerm(site.getId(), OpeSiteEnum.EDIT);
		siteService.update(site);
		clearPermCache();
		return new ResponseInfo();
	}

	/**
	 * 修改模板设置
	 */
	@PutMapping("/model")
	public ResponseInfo updateModel(@RequestBody @Valid CmsSiteExtDto dto, BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		CmsSite site = service.findById(dto.getId());
		site.getCfg().putAll(dto.getCfg());
		service.update(site);
		if (dto.getModelTpls() != null && !dto.getModelTpls().isEmpty()) {
			// 物理删除站点配置模板
			siteModelTplService.physicalDeleteInBatch(site.getModelTpls());
			List<SiteModelTpl> tpls = new ArrayList<SiteModelTpl>(10);
			// 重新添加站点配置模板
			for (SiteModelTpl tpl : dto.getModelTpls()) {
				tpl.setSite(site);
				tpl.setModel(cmsModelService.findById(tpl.getModelId()));
				tpls.add(tpl);
			}
			siteModelTplService.saveAll(tpls);
		}
		return new ResponseInfo();
	}

	/**
	 * 删除站点（加入回收站）
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(HttpServletRequest request,
			@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		List<CmsSite> toDelSites = new ArrayList<CmsSite>(10);
		List<Integer> list = Arrays.asList(dels.getIds());
		for (Integer integer : list) {
			CmsSite site = siteService.findById(integer);
			//检查权限
			super.checkSiteDataPerm(integer, OpeSiteEnum.DEL);
			// 顶级站点不能删除
			if (site.getParentId() == null) {
				return new ResponseInfo(SiteErrorCodeEnum.TOP_SITE_CANNOT_DELETE.getCode(),
						SiteErrorCodeEnum.TOP_SITE_CANNOT_DELETE.getDefaultMessage(), false);
			}
			List<CmsSite> childSites = new ArrayList<CmsSite>();
			/** 子站点有多层级 */
			childSites.addAll(site.getChildAll());
			for (CmsSite cmsSite : childSites) {
				cmsSite.setIsDelete(true);
			}
			toDelSites.addAll(childSites);
		}
		/** 主动清空用户、组织、角色权限缓存 */
		siteService.batchUpdate(toDelSites);
		clearPermCache();
		return new ResponseInfo();
	}

	/**
	 * 修改站点扩展表
	 */
	@PutMapping(value = "/ext")
	public ResponseInfo updateExt(@RequestBody @Valid CmsSiteExtDto dto, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		CmsSite site = service.findById(dto.getId());
		CmsSiteExt ext = site.getCmsSiteExt();
		if (ext != null) {
			ext.transfer(dto, ext);
		}
		Integer newContentResourceId = dto.getNewContentResourceId();
		Integer watermarkPicture = dto.getWatermarkResourceId();
		if (newContentResourceId != null) {
			ResourcesSpaceData data = spaceDataService.findById(newContentResourceId);
			dto.getCfg().put("contentNewFlagDefinition", data != null ? newContentResourceId.toString() : "");
			dto.getCfg().put("contentNewFlagDefinitionUrl", data != null ? data.getUrl() : "");
			ext.setNewContentResourceId(newContentResourceId);
		}
		if (watermarkPicture != null) {
			ResourcesSpaceData data = spaceDataService.findById(watermarkPicture);
			dto.getCfg().put("watermarkPicture", data != null ? watermarkPicture.toString() : "");
			dto.getCfg().put("watermarkPictureUrl", data != null ? data.getUrl() : "");
			ext.setWatermarkResource(data);
			ext.setWatermarkResourceId(watermarkPicture);
		}
		Set<String>cfgKeys = dto.getCfg().keySet();
		if (cfgKeys != null&&cfgKeys.contains(CmsSiteConfig.UPLOAD_FILE_MEMORY)) {
			String str = dto.getCfg().get(CmsSiteConfig.UPLOAD_FILE_MEMORY);
			if(StrUtils.isNumeric(str)){
				Integer id = Integer.parseInt(str);
				ext.setUploadFtpId(id);
				ext.setUploadFtp(ftpService.findById(id));
			}
		}
		if (cfgKeys != null&&cfgKeys.contains(CmsSiteConfig.STATIC_SERVER_MEMORY)) {
			String str = dto.getCfg().get(CmsSiteConfig.STATIC_SERVER_MEMORY);
			if(StrUtils.isNumeric(str)){
				Integer id = Integer.parseInt(str);
				ext.setStaticPageFtpId(id);
				ext.setStaticPageFtp(ftpService.findById(id));
			}
		}
		if (cfgKeys != null&&cfgKeys.contains(CmsSiteConfig.UPLOAD_FILE_MEMORY)) {
			String str = dto.getCfg().get(CmsSiteConfig.UPLOAD_FILE_MEMORY);
			if(StrUtils.isNumeric(str)){
				Integer id = Integer.parseInt(str);
				ext.setUploadOssId(id);
				ext.setUploadOss(uploadOssService.findById(id));
			}
		}
		if (cfgKeys != null&&cfgKeys.contains(CmsSiteConfig.STATIC_SERVER_MEMORY)) {
			String str = dto.getCfg().get(CmsSiteConfig.STATIC_SERVER_MEMORY);
			if(StrUtils.isNumeric(str)){
				Integer id = Integer.parseInt(str);
				ext.setStaticPageOssId(id);
				ext.setStaticPageOss(uploadOssService.findById(id));
			}
		}
		if (cfgKeys.size() > 0 &&cfgKeys.contains(CmsSiteConfig.SURVEY_CONFIGURATION_ID)) {
			Integer id = null;
			String str = dto.getCfg().get(CmsSiteConfig.SURVEY_CONFIGURATION_ID);
			if(StringUtils.isNotBlank(str) && StrUtils.isNumeric(str)){
				id = Integer.parseInt(str);
			}
			/**
			 * 没有返回结果的异步任务，先返回消息，后台运行生成索引
			 */
			Integer finalId = id;
			ThreadPoolService.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					try {
						questionnaireService.updateWorkFlow(finalId, site.getId());
					} catch (GlobalException e) {
						logger.error(e.getMessage());
					}
				}
			});
		}
		site.setCmsSiteExt(ext);
		siteExtService.update(ext);
		site.setCfg(dto.getCfg());
		service.update(site);
		return new ResponseInfo();
	}

	/**
	 * 站点回收站
	 */
	@GetMapping(value = "/recycle")
	@SerializeField(clazz = CmsSite.class, includes = { "id", "name", "updateTime", "updateUser" })
	public ResponseInfo recycle(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		HashMap<String, String[]> params = new HashMap<String, String[]>(16);
		params.put("EQ_isDelete_Boolean", new String[] { "true" });
		return new ResponseInfo(siteService.getList(params, null, false));
	}

	/**
	 * 站点回收站删除,此处不检查删除权限，因为在加入回收站的时候就已经判断
	 * 如果此时在检查的话，就会报站点删除没有权限的操作
	 */
	@DeleteMapping(value = "/recycle")
	public ResponseInfo del(HttpServletRequest request,
			@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		siteService.delete(dels.getIds());
		return new ResponseInfo();
	}

	/**
	 * 还原站点
	 */
	@PostMapping(value = "/restore")
	public ResponseInfo restore(@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		List<Integer> list = Arrays.asList(dels.getIds());
		List<CmsSite> cmssites = new ArrayList<CmsSite>(10);
		for (Integer integer : list) {
			CmsSite site = siteService.findById(integer);
			// 得到全部父结点数据
			cmssites.addAll(site.getSiteParents(site));
		}
		for (CmsSite cmsSite : cmssites) {
			cmsSite.setIsDelete(false);
		}
		siteService.batchUpdate(cmssites);
		/** 主动清空用户权限缓存 */
		clearPermCache();
		return new ResponseInfo();
	}

	/**
	 * 验证目录
	 *
	 * @param: path
	 *             目录
	 * @param: id
	 *             站点ID
	 */
	@RequestMapping(value = "/path/unique", method = RequestMethod.GET)
	public ResponseInfo path(String path, Integer id) throws GlobalException {
		Boolean flag = siteService.existSitePath(id, path);
		return flag ? new ResponseInfo(false) : new ResponseInfo(true);
	}

	/**
	 * 可管理上级站点+可查看站点
	 *
	 * @param: request
	 *             请求
	 */
	@GetMapping(value = "/childs")
	public ResponseInfo childs(HttpServletRequest request) throws GlobalException {
		List<Integer> list = SystemContextUtils.getUser(request).getViewSiteIds();
		List<CmsSite> sites = siteService.findAllById(list);
		JSONArray responseData = super.getChildTree(sites, false, "name", "newChildAble");
		return new ResponseInfo(responseData);
	}

	/**
	 * 获取模板下拉列表
	 *
	 * @param siteId
	 *            站点ID
	 * @param solution
	 *            模板方案
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 * @Title: models
	 */
	@GetMapping(value = "/models")
	public ResponseInfo models(Integer siteId, String solution) throws GlobalException {
		return siteService.models(siteId, solution);
	}

	/**
	 * 获取模板方案列表
	 *
	 * @param siteId
	 *            站点ID
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 * @Title: models
	 */
	@GetMapping(value = "/solutions")
	public ResponseInfo solutions(Integer siteId) throws GlobalException {
		CmsSite site = siteService.findById(siteId);
		String[] solutions = resourceService.getSolutions(site.getTplPath());
		return new ResponseInfo(solutions);
	}

	/**
	 * 首页模板列表
	 *
	 * @param siteId
	 *            站点ID
	 * @param solution
	 *            模板方案
	 * @throws GlobalException
	 *             异常
	 */
	@GetMapping("/tpl/list")
	public ResponseInfo getTpl(Integer siteId, String solution) throws GlobalException {
		CmsSite site = siteService.findById(siteId);
		List<String> indexTplList = getTplIndex(site, solution);
		return new ResponseInfo(indexTplList);
	}

	private List<String> getTplIndex(CmsSite site, String solution) throws GlobalException {
		// 得到站点目录
		String path = site.getPath();
		List<String> tplList = null;
		try {
			tplList = tplService.getIndexBySolutionPath(solution, site);
		} catch (IOException e) {
			logger.error("ftp配置错误");
		}
		return TplUtils.tplTrim(tplList, getTplPath(path) + SPT + solution + SPT, null);
	}

	private String getTplPath(String path) {
		return TPL_BASE + File.separator + path;
	}

	/**
	 * 开启站点
	 *
	 * @param dto 批量操作Dto
	 * @throws GlobalException 异常
	 * @Title: enableSite
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/on")
	public ResponseInfo enableSite(HttpServletRequest request, @RequestBody @Valid DeleteDto dto,
			BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		List<CmsSite> sites = service.findAllById(Arrays.asList(dto.getIds()));
		for (CmsSite site : sites) {
			// 检查权限
			super.checkSiteDataPerm(site.getId(), OpeSiteEnum.OPENCLOSE);
			site.setIsOpen(true);
		}
		service.batchUpdate(sites);
		/** 权限需要更新，站点列表取的权限里的站点数据 */
		clearPermCache();
		return new ResponseInfo();
	}

	/**
	 * 关闭站点
	 *
	 * @param dto 批量操作Dto
	 * @throws GlobalException 异常
	 * @Title: disableSite
	 * @return: ResponseInfo 返回
	 */
	@PostMapping("/off")
	public ResponseInfo disableSite(HttpServletRequest request, @RequestBody @Valid DeleteDto dto,
			BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		List<CmsSite> sites = service.findAllById(Arrays.asList(dto.getIds()));
		for (CmsSite site : sites) {
			// 检查权限
			super.checkSiteDataPerm(site.getId(), OpeSiteEnum.OPENCLOSE);
			site.setIsOpen(false);
		}
		service.batchUpdate(sites);
		/** 权限需要更新，站点列表取的权限里的站点数据 */
		clearPermCache();
		return new ResponseInfo();
	}

	/**
	 * 排序
	 * 
	 * @Title: dragSort
	 * @param sortDto
	 *            排序
	 * @param result
	 *            检验
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public ResponseInfo dragSort(@RequestBody DragSortDto sortDto, BindingResult result,
			HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		// 获取用户可查看站点
		List<CmsSite> siteList = SystemContextUtils.getUser(request).getViewSites();
		if (!siteList.isEmpty()) {
			// 过滤加入回收站，删除
			siteList = siteList.stream().filter(x -> !x.getIsDelete()).filter(x -> !x.getHasDeleted())
					.collect(Collectors.toList());
		}
		siteService.updatePriority(sortDto, siteList);
		/** 权限需要更新，站点列表取的权限里的站点数据 */
		clearPermCache();
		return new ResponseInfo();
	}

	private void clearPermCache() {
		/** 主动清空用户、组织、角色权限缓存 */
		userService.clearAllUserCache();
		orgService.clearAllOrgCache();
		roleService.clearAllRoleCache();
	}

	@Autowired
	private TplResourceService resourceService;
	@Autowired
	private CmsSiteService siteService;
	@Autowired
	private CmsSiteExtService siteExtService;
	@Autowired
	private ResourcesSpaceDataService spaceDataService;
	@Autowired
	private SiteModelTplService siteModelTplService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private TplService tplService;
	@Autowired
	private CoreUserService userService;
	@Autowired
	private CoreRoleService roleService;
	@Autowired
	private CmsOrgService orgService;
	@Autowired
	private FtpService ftpService;
	@Autowired
	private UploadOssService uploadOssService;
	@Autowired
	private SysQuestionnaireService questionnaireService;

}
