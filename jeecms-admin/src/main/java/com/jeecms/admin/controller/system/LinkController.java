package com.jeecms.admin.controller.system;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.domain.Link;
import com.jeecms.system.domain.SysLinkType;
import com.jeecms.system.domain.dto.LinkMoveDto;
import com.jeecms.system.domain.vo.LinkVo;
import com.jeecms.system.service.LinkService;
import com.jeecms.system.service.SysLinkTypeService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 友情链接控制层
 *
 * @Description:TODO
 * @author: wulongwei
 * @date: 2019年4月9日 上午10:05:23
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
@RequestMapping("/links")
public class LinkController extends BaseAdminController<Link, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 分页查询
	 *
	 * @param request  {@link HttpServletRequest}
	 * @param pageable {@link Pageable}
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/page")
	@MoreSerializeField({
			@SerializeField(clazz = LinkVo.class, includes = {"linkType", "links"}),
			@SerializeField(clazz = Link.class, includes = {"id", "linkName", "linkUrl",
					"isEnable", "sortNum", "resourcesSpaceData", "linkTypeName"}),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = {"id", "dimensions", "url"})})
	public ResponseInfo page(@PageableDefault(sort = "sortNum", direction = Direction.ASC) Pageable pageable,
							 Integer linkTypeId, HttpServletRequest request) throws GlobalException {
		validateId(linkTypeId);
		Map<String, String[]> params = getCommonParams(request);
		params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
		params.put("EQ_linkTypeId_Integer", new String[]{linkTypeId.toString()});
		Page<Link> page = service.getPage(params, pageable, false);

		LinkVo linkVo = new LinkVo();
		SysLinkType linkType = linkTypeService.get(linkTypeId);
		linkVo.setLinkType(linkType.getTypeName());
		linkVo.setLinks(page);
		return new ResponseInfo(linkVo);
	}

	/**
	 * 通过ID删除对象
	 */
	@DeleteMapping()
	public ResponseInfo deleteIds(@RequestBody @Valid DeleteDto details) throws GlobalException {
		Integer[] ids = details.getIds();
		super.deleteBeatch(ids);
		return new ResponseInfo();
	}

	/**
	 * 获取单个对象
	 *
	 * @param id 友情链接id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@Override
	@GetMapping(value = "/{id}")
	@MoreSerializeField({@SerializeField(clazz = Link.class, includes = {"id", "linkName",
			"linkUrl", "isEnable", "resourcesSpaceData", "linkTypeId", "linkTypeName"}),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = {"dimensions", "url","id"})})
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 修改友情链接
	 */
	@PutMapping()
	@Override
	public ResponseInfo update(@RequestBody @Valid Link link, BindingResult result) throws GlobalException {
		validateId(link.getId());
		Link bean = service.findById(link.getId());
		if (link.getLinkLogo() != null) {
			ResourcesSpaceData spaceData = spaceDataService.findById(link.getLinkLogo());
			bean.setResourcesSpaceData(spaceData);
			bean.setLinkLogo(link.getLinkLogo());
		} else {
			bean.setResourcesSpaceData(null);
			bean.setLinkLogo(null);
		}
		bean.setIsEnable(link.getIsEnable());
		bean.setLinkName(link.getLinkName());
		bean.setLinkUrl(link.getLinkUrl());
		super.updateAll(bean, result);
		return new ResponseInfo();
	}

	/**
	 * 添加友情链接
	 *
	 * @param result  {@link BindingResult}
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping()
	public ResponseInfo save(HttpServletRequest request, @RequestBody @Valid Link link,
							 BindingResult result) throws GlobalException {
		validateBindingResult(result);
		Integer siteId = SystemContextUtils.getSiteId(request);
		service.save(link, siteId);
		return new ResponseInfo();
	}

	/**
	 * 拖动排序
	 */
	@PutMapping("/sort")
	public ResponseInfo sort(@RequestBody(required = false) @Valid DragSortDto sort, BindingResult result)
			throws GlobalException {
		validateBindingResult(result);
		service.dragSort(sort);
		return new ResponseInfo();
	}

	/**
	 * 启用
	 *
	 * @param map id 友情链接id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/enable")
	public ResponseInfo enable(@RequestBody Map<String, Object> map) throws GlobalException {
		Integer id = Integer.parseInt(map.get("id").toString());
		validateId(id);
		service.isEnable(id, true);
		return new ResponseInfo();
	}

	/**
	 * 不启用
	 *
	 * @param map id 友情链接id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/unEnable")
	public ResponseInfo unEnable(@RequestBody Map<String, Object> map) throws GlobalException {
		Integer id = Integer.parseInt(map.get("id").toString());
		validateId(id);
		service.isEnable(id, false);
		return new ResponseInfo();
	}

	/**
	 * 移动到其他类别
	 *
	 * @param dto ids 需要移动的友情链接id  linkTypeId 类别id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/move")
	public ResponseInfo move(@RequestBody @Valid LinkMoveDto dto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.move(dto.getIds(), dto.getLinkTypeId());
		return new ResponseInfo(true);
	}

	@Autowired
	private LinkService service;
	@Autowired
	private SysLinkTypeService linkTypeService;
	@Autowired
	private ResourcesSpaceDataService spaceDataService;

}

