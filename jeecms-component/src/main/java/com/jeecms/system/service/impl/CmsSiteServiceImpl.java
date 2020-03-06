package com.jeecms.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SiteErrorCodeEnum;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.CookieUtils;
import com.jeecms.component.listener.SiteListener;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.content.service.CmsModelTplService;
import com.jeecms.system.dao.CmsSiteDao;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteExt;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.SiteModelTpl;
import com.jeecms.system.domain.vo.CmsModelTplVo;
import com.jeecms.system.domain.vo.CmsSiteVo;
import com.jeecms.system.exception.SiteExceptionInfo;
import com.jeecms.system.service.CmsSiteExtService;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.system.service.SiteModelTplService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 站点service实现
 *
 * @author: tom
 * @date: 2018年11月5日 下午2:01:05
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = { Exception.class })
public class CmsSiteServiceImpl extends BaseServiceImpl<CmsSite, CmsSiteDao, Integer> implements CmsSiteService {
	static Logger logger = LoggerFactory.getLogger(CmsSiteServiceImpl.class);
	public static final String SITE_PARAM = "siteId";
	public static final String SITE_COOKIE = "_site_id_cookie";
	public static final String PERMISSION_MODEL = "_permission_key";
	public static final String SITE_PATH_PARAM = "path";

	@Override
	@Transactional(readOnly = true, rollbackFor = { Exception.class })
	public List<CmsSite> findByParentId(Integer parentId) {
		return dao.findByParentIdAndHasDeleted(parentId, false);
	}

	@Override
	public List<CmsSite> findAll(Boolean isCycle, boolean cacheable) {
		Map<String, String[]> map = new HashMap<String, String[]>(3);
		if (isCycle != null) {
			map.put("EQ_isDelete_Boolean", new String[] { isCycle.toString() });
		}
		map.put("EQ_hasDeleted_Boolean", new String[] { "false" });
		return super.getList(map, new PaginableRequest(0, Integer.MAX_VALUE), cacheable);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = { Exception.class })
	public List<CmsSite> findByDomain(String domain) {
		return dao.findByDomain(domain);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = { Exception.class })
	public CmsSite findByPath(String path) {
		return dao.findByPath(path);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = { Exception.class })
	public boolean existSitePath(Integer siteId, String path) {
		CmsSite site = findByPath(path);
		if (site != null && !site.getHasDeleted()) {
			/** 修改路径未修改返回false */
			if (siteId != null && siteId.equals(site.getId())) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public CmsSite save(CmsSite currSite, CoreUser currUser, CmsSite bean) throws GlobalException {
		Integer parentId = bean.getParentId();
		if (parentId == null) {
			throw new GlobalException(new SiteExceptionInfo(SiteErrorCodeEnum.SITE_PARENTID_NOT_EMPLY.getCode(),
				SiteErrorCodeEnum.SITE_PARENTID_NOT_EMPLY.getDefaultMessage()));
		}
		bean.init();
		// 得到全局配置
		GlobalConfig globalConfig = globalConfigService.get();
		bean.setGlobalConfig(globalConfig);
		String path = bean.getPath();
		/** 检查站点路径不允许重复和为空 */
		if (StringUtils.isBlank(path) || existSitePath(null, path)) {
			String msg = MessageResolver.getMessage(SiteErrorCodeEnum.SITE_PATH_ERROR.getCode(),
				SiteErrorCodeEnum.SITE_PATH_ERROR.getDefaultMessage());
			throw new GlobalException(new SiteExceptionInfo(SiteErrorCodeEnum.SITE_PATH_ERROR.getCode(), msg));
		}
		CmsSite parent = findById(bean.getParentId());
		bean.setParent(parent);
		parent.getChilds().add(bean);
		Map<String, String> parentMap = parent.getCfg();
		Map<String, String> map = new HashMap<String, String>(parentMap);
		/** 继承父站点扩展 **/
		bean.setCfg(map);
		/** 初始化站点Ext **/
		CmsSiteExt ext = new CmsSiteExt();
		ext.setCmsSite(bean);
		bean.setCmsSiteExt(ext);
		bean.setSortWeight(0);
		CmsSite beans = super.save(bean);
		super.flush();
		/** 将上级站点的选择的站点模板信息复制到当前站点 **/
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		params.put("EQ_siteId_Integer", new String[] { bean.getParentId().toString() });
		List<SiteModelTpl> smt = siteModelTplService.getList(params, null, true);
		List<SiteModelTpl> newSmt = new ArrayList<SiteModelTpl>(10);
		for (SiteModelTpl siteModelTpl : smt) {
			SiteModelTpl siteTpl = new SiteModelTpl(siteModelTpl.getModelId(), beans, beans.getId(),
				siteModelTpl.getPcTplPath(), siteModelTpl.getMobileTplPath());
			newSmt.add(siteTpl);
		}
		if (newSmt != null && !newSmt.isEmpty()) {
			siteModelTplService.saveAll(newSmt);
			siteModelTplService.flush();
		}
		// 新建某个站点的子站点时，需要复制该站点的模板
		List<CmsModelTpl> cmsModelTpls = cmsModelTplService.getList(parentId, null, null);
		// 用于站点默认模板的下拉列表
		List<CmsModelTpl> modelTpl = new ArrayList<CmsModelTpl>(10);
		for (CmsModelTpl tpl : cmsModelTpls) {
			CmsModelTpl newTpl = new CmsModelTpl();
			newTpl.setModelId(tpl.getModelId());
			newTpl.setModel(tpl.getModel());
			newTpl.setTplPath(tpl.getTplPath());
			newTpl.setSiteId(bean.getId());
			newTpl.setTplType(tpl.getTplType());
			newTpl.setTplSolution(tpl.getTplSolution());
			modelTpl.add(newTpl);
		}
		if (modelTpl != null && !modelTpl.isEmpty()) {
			cmsModelTplService.saveAll(modelTpl);
			cmsModelTplService.flush();
		}
		/** 主动清空用户权限缓存 */
		userService.clearAllUserCache();
		/** 处理管理员 本角色 本部门的数据权限 */
		/**
		 * 站点监听事物调用
		 */
		for (SiteListener listener : siteListeners) {
			listener.afterSiteSave(bean);
		}
		// 创建目录以及复制父站点全部文件
		/**
		 * 没有返回结果的异步任务
		 */
		try {
			cmsSiteExtService.saveDir(bean.getParentId(), beans.getId());
		} catch (IOException e) {
			logger.info("copy parent site error " + e.getMessage());
		}
		return bean;
	}

	@Override
	public CmsSite update(CmsSite bean) throws GlobalException {
		// CoreUser user =
		// SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
		// user.checkSiteDataPerm(bean.getId(), OpeSiteEnum.EDIT);
		// /** 检查站点路径不允许重复和为空 */
		// String path = bean.getPath();
		// if (existSitePath(bean.getId(), path)) {
		// String msg =
		// MessageResolver.getMessage(SiteErrorCodeEnum.SITE_PATH_ERROR.getCode(),
		// SiteErrorCodeEnum.SITE_PATH_ERROR.getDefaultMessage());
		// throw new GlobalException(
		// new SiteExceptionInfo(SiteErrorCodeEnum.SITE_PATH_ERROR.getCode(),
		// msg));
		// }
		// /** 检查站点路径不允许重复 */
		// if (existSiteDomain(bean.getId(), bean.getDomain())) {
		// String msg =
		// MessageResolver.getMessage(SiteErrorCodeEnum.SITE_DOMAIN_ERROR.getCode(),
		// SiteErrorCodeEnum.SITE_DOMAIN_ERROR.getDefaultMessage());
		// throw new GlobalException(
		// new SiteExceptionInfo(SiteErrorCodeEnum.SITE_DOMAIN_ERROR.getCode(),
		// msg));
		// }
		return super.update(bean);
	}

//	@Override
//	public CmsSiteConfig updateSiteConfig(CmsSite site, CmsSiteConfig config) throws GlobalException {
//		Map<String, String> attrMap = config.getAttr();
//		site.setCfg(attrMap);
//		return config;
//	}

	@Override
	public void updateAttr(Integer siteId, Map<String, String> attr) {
		CmsSite site = findById(siteId);
        Map<String, String> map = site.getCfg();
        map.putAll(attr);
	}

//	@Override
//	public CmsSite delete(Integer id) throws GlobalException {
//		CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
//		// user.checkSiteDataPerm(id, OpeSiteEnum.DEL);
//		/** 处理管理员 本角色 本部门的数据权限 */
//		CmsSite bean = super.findById(id);
//		CmsSite parent = bean.getParent();
//		if (parent != null) {
//			bean.getParent().getChildren().remove(bean);
//		}
//		dao.deleteById(id);
//		logger.info("delete site " + id);
//		return bean;
//	}

//	@Override
//	public List<CmsSite> delete(Integer[] ids) throws GlobalException {
//		List<CmsSite> beans = new ArrayList<CmsSite>();
//		for (int i = 0, len = ids.length; i < len; i++) {
//			CoreUser user = SystemContextUtils.getUser(RequestUtils.getHttpServletRequest());
//			// user.checkSiteDataPerm(ids[i], OpeSiteEnum.DEL);
//			beans.add(delete(ids[i]));
//		}
//		for (SiteListener listener : siteListeners) {
//			listener.beforeSitePhysicDelete(ids);
//		}
//		return beans;
//	}

	@Override
	public ResponseInfo models(Integer siteId, String solution) throws GlobalException {
		List<CmsModelTplVo> vos = new ArrayList<CmsModelTplVo>(10);
		if (siteId != null && StringUtils.isNotBlank(solution)) {
			// 获取栏目模型(包含启用以及本站点)
			List<CmsModel> channelModels = cmsModelService.findList(CmsModel.CHANNEL_TYPE, siteId);
			for (CmsModel cmsModel : channelModels) {
				List<CmsModelTpl> tpls = cmsModelTplService.models(siteId, cmsModel.getId(), solution);
				if (tpls.isEmpty()) {
					CmsModelTplVo vo = new CmsModelTplVo();
					vo.setModelId(cmsModel.getId());
					vo.setModelName(cmsModel.getModelName());
					vo.setType(CmsModel.CHANNEL_TYPE);
					vo.setTplPath("");
					vos.add(vo);
					continue;
				}
				for (CmsModelTpl cmsModelTpl : tpls) {
					CmsModelTplVo vo = new CmsModelTplVo();
					vo.setModelId(cmsModelTpl.getModelId());
					vo.setModelName(cmsModel.getModelName());
					vo.setType(CmsModel.CHANNEL_TYPE);
					vo.setTplPath(cmsModelTpl.getTplPath());
					vos.add(vo);
				}
			}
			// 获取内容模型(包含启用以及本站点)
			List<CmsModel> contentModels = cmsModelService.findList(CmsModel.CONTENT_TYPE, siteId);
			for (CmsModel cmsModel : contentModels) {
				List<CmsModelTpl> tpls = cmsModelTplService.models(siteId, cmsModel.getId(), solution);
				if (tpls.isEmpty()) {
					CmsModelTplVo vo = new CmsModelTplVo();
					vo.setModelId(cmsModel.getId());
					vo.setModelName(cmsModel.getModelName());
					vo.setType(CmsModel.CONTENT_TYPE);
					vo.setTplPath("");
					vos.add(vo);
					continue;
				}
				for (CmsModelTpl cmsModelTpl : tpls) {
					CmsModelTplVo vo = new CmsModelTplVo();
					vo.setModelId(cmsModelTpl.getModelId());
					vo.setModelName(cmsModel.getModelName());
					vo.setType(CmsModel.CONTENT_TYPE);
					vo.setTplPath(cmsModelTpl.getTplPath());
					vos.add(vo);
				}
			}
		}
		return new ResponseInfo(vos);
	}

	@Override
	public List<CmsSite> physicalDelete(Integer[] ids) throws GlobalException {
		for (SiteListener lis : siteListeners) {
			lis.beforeSitePhysicDelete(ids);
		}
		return super.physicalDelete(ids);
	}

	@Override
	public void updatePriority(DragSortDto sortDto, List<CmsSite> cmsList) throws GlobalException {
		/**
		 * 如果是isMove为true说明是移动，则是修改 dto.getIds的[0]->需要移动/修改的值，[1]->移动的位置或者修改的位置
		 */
		/**
		 * 当dto.getIds的长度只有1的时候，则说明将此站点移动到统一序列的站点的最上层
		 */
		Integer parentId = super.findById(sortDto.getFromId()).getParentId();
		if (parentId != null) {
			cmsList = cmsList.stream().filter(site -> parentId.equals(site.getParentId()))
				.sorted(Comparator.comparing(CmsSite::getSortNum).reversed()
					.thenComparing(Comparator.comparing(CmsSite::getCreateTime).reversed()))
				.collect(Collectors.toList());
		} else {
			cmsList = cmsList.stream().filter(site -> site.getParentId() == null)
				.sorted(Comparator.comparing(CmsSite::getSortNum).reversed()
					.thenComparing(Comparator.comparing(CmsSite::getCreateTime).reversed()))
				.collect(Collectors.toList());
		}
		// 最大排序值
		int i = cmsList.size() * 2;
		// 需要移动的对象的排序值
		int newSort = 0;
		// 需要移动的对象在list集合中的位置
		int index = 0;
		for (int j = 0; j < cmsList.size(); j++) {
			CmsSite site = cmsList.get(j);
			site.setSortNum(i);
			// 如果传入了移动到的位置，并且当前遍历的是站点需要修改的值，那么将newSort最为此站点-1刚好在需要修改的这个栏目的下方
			if (sortDto.getToId() != null && site.getId().equals(sortDto.getToId())) {
				newSort = site.getSortNum() - 1;
			}
			if (sortDto.getNextId() != null && site.getId().equals(sortDto.getNextId())) {
				newSort = site.getSortNum() + 1;
			}
			// 取出需要移动的值在list集合中的位置
			if (site.getId().equals(sortDto.getFromId())) {
				index = j;
			}
			i -= 2;
		}
		cmsList.get(index).setSortNum(newSort);
		super.batchUpdate(cmsList);
	}

	@Override
	public CmsSiteVo getModel(Integer id) throws GlobalException {
		List<JSONObject> modelTpls = new ArrayList<JSONObject>(10);
		List<CmsModel> sum = new ArrayList<CmsModel>(10);
		CmsSite site = super.findById(id);
		// 得到站点默认模板
		List<SiteModelTpl> tpls = site.getModelTpls();
		// 获取栏目模型(包含启用以及本站点)
		List<CmsModel> channelModels = cmsModelService.findList(CmsModel.CHANNEL_TYPE, id);
		// 获取内容模型(包含启用以及本站点)
		List<CmsModel> contentModels = cmsModelService.findList(CmsModel.CONTENT_TYPE, id);
		sum.addAll(channelModels);
		sum.addAll(contentModels);
		// 将最新的模型与站点保存的默认模型模板做比较，如果存在，则加上数据，反之留空
		for (CmsModel cmsModel : sum) {
			JSONObject obj = new JSONObject();
			Optional<SiteModelTpl> optional = tpls.stream().filter(x -> x.getModelId().equals(cmsModel.getId()))
				.findFirst();
			if (optional.isPresent()) {
				obj.put("mobileTplPath", optional.get().getMobileTplPath());
				obj.put("pcTplPath", optional.get().getPcTplPath());
			} else {
				obj.put("mobileTplPath", "");
				obj.put("pcTplPath", "");
			}
			obj.put("modelId", cmsModel.getId());
			obj.put("model", cmsModel);
			modelTpls.add(obj);
		}
		CmsSiteVo vo = new CmsSiteVo(site);
		vo.setModelTpls(modelTpls);
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<CmsSite> findByIds(Collection<Integer> ids) {
		return dao.findByIds(ids);
	}

	@Override
	public CmsSite getCurrSite(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		CmsSite site = getByParams(request, response);
		if (site == null) {
			site = getByPath(request, response);
		}
		if (site == null) {
			site = getByDomain(request, response);
		}
		/** 在没有站点相关参数的情况下，取管理员默认有管理权限的站点 */
		if (site == null) {
			site = getByUserSites(request, response);
		}
		if (site == null) {
			site = getByCookie(request);
		}
		if (site == null) {
			site = getByDefault();
		}
		if (site == null) {
			throw new GlobalException(new SystemExceptionInfo());
		} else {
			return site;
		}
	}

	private CmsSite getByParams(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		String p = request.getHeader(SITE_PARAM);
		if (!StringUtils.isBlank(p) && StrUtils.isNumeric(p)) {
			try {
				Integer siteId = Integer.parseInt(p);
				CmsSite site = findById(siteId);
				if (site != null) {
					// 若使用参数选择站点，则应该把站点保存至cookie中才好。
					CookieUtils.addCookie(request, response, SITE_COOKIE, site.getId().toString(), null, null);
					return site;
				}
			} catch (NumberFormatException e) {
				logger.warn("param site id format exception", e);
			}
		}
		return null;
	}

	private CmsSite getByPath(HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter(SITE_PATH_PARAM);
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		if (StringUtils.isNoneBlank(ctx)) {
			uri = uri.substring(ctx.length());
		}
		if(uri.startsWith(WebConstants.INTRANET_PREFIX)){
			p = uri.substring(WebConstants.INTRANET_PREFIX.length());
			p = p.substring(0,p.indexOf("/"));
		}
		if (!StringUtils.isBlank(p)) {
			CmsSite site = findByPath(p);
			if (site != null) {
				// 若使用参数选择站点，则应该把站点保存至cookie中才好。
				CookieUtils.addCookie(request, response, SITE_COOKIE, site.getId().toString(), null, null);
				return site;
			}
		}
		return null;
	}

	private CmsSite getByCookie(HttpServletRequest request) throws GlobalException {
		Cookie cookie = CookieUtils.getCookie(request, SITE_COOKIE);
		if (cookie != null) {
			String v = cookie.getValue();
			if (!StringUtils.isBlank(v)) {
				try {
					Integer siteId = Integer.parseInt(v);
					return findById(siteId);
				} catch (NumberFormatException e) {
					logger.warn("cookie site id format exception", e);
				}
			}
		}
		return null;
	}

	private CmsSite getByDomain(HttpServletRequest request, HttpServletResponse response) {
		String domain = request.getServerName();
		if (!StringUtils.isBlank(domain)) {
			List<CmsSite> sites = findByDomain(domain);
			CmsSite site = !sites.isEmpty() ? sites.get(0) : null;
			if (site != null) {
				// 若使用参数选择站点，则应该把站点保存至cookie中才好。
				CookieUtils.addCookie(request, response, SITE_COOKIE, site.getId().toString(), null, null);
				return site;
			}
		}
		return null;
	}

	private CmsSite getByDefault() {
		List<CmsSite> list = findAll(false, false);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取拥有站点权限的第一个站点
	 *
	 * @Title: getByUserSites
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return: CmsSite
	 */
	private CmsSite getByUserSites(HttpServletRequest request, HttpServletResponse response) {
		CoreUser user = SystemContextUtils.getUser(request);
		if (user != null && user.getAdmin()) {
			Set<CmsSite> sites = user.getOwnerSites();
			if (sites != null && sites.size() > 0) {
				CmsSite site = sites.iterator().next();
				return site;
			}
		}
		return null;
	}

	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private GlobalConfigService globalConfigService;
	@Autowired
	private List<SiteListener> siteListeners;
	@Autowired
	private CmsModelTplService cmsModelTplService;
	@Autowired
	private SiteModelTplService siteModelTplService;
	@Autowired
	private CoreUserService userService;
	@Autowired
	private CmsSiteExtService cmsSiteExtService;

}
