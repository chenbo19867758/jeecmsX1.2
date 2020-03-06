/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.audit;

import com.jeecms.audit.domain.AuditModelItem;
import com.jeecms.audit.domain.AuditModelSet;
import com.jeecms.audit.domain.dto.AuditModelDto;
import com.jeecms.audit.service.AuditModelSetService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.content.util.CmsModelUtil;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模型设置控制器
 * @author: ljw
 * @version: 1.0
 * @date 2019-12-16
 */
@RequestMapping("/auditmodelset")
@RestController
public class AuditModelSetController extends BaseController<AuditModelSet, Integer> {

	@Autowired
	private AuditModelSetService auditModelSetService;
	@Autowired
	private CmsModelItemService cmsModelItemService;
	
	/**
	 * 内容模型设置列表
	 * 
	 * @param: request 请求
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@MoreSerializeField({
			@SerializeField(clazz = AuditModelSet.class, includes = { "id", "modelId",
					"modelName", "itemsName", "itemIds" }), })
	public ResponseInfo page(HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		//过滤站点
		Map<String, String[]> params = new HashMap<>(20);
		params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
		List<AuditModelSet> sets = auditModelSetService.getList(params, null, true);
		//时间倒序
		sets = sets.stream().sorted(Comparator.comparing(AuditModelSet::getCreateTime).reversed())
			.collect(Collectors.toList());
		return new ResponseInfo(sets);
	}
	
	/**
	 *  选择内容模型下拉列表
	 * @param: request 请求
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/models", method = RequestMethod.GET)
	public ResponseInfo models(HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(auditModelSetService.models(siteId));
	}
	
	/**
	 *  选择内容模型ID查询字段
	 * @param modelId 模型ID
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/items", method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = CmsModelItem.class, includes = { "id", "field",
				"itemLabel" }), })
	public ResponseInfo items(Integer modelId) throws GlobalException {
		List<CmsModelItem> items = cmsModelItemService.findByModelId(modelId);
		List<CmsModelItem> modelItems = CmsModelUtil.checkContentCmsModelItem(items, ContentConstant.ContentCheckFieldAndDataType.txtAndImg);
		return new ResponseInfo(modelItems);
	}

	/**
	 *  获取详情
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@MoreSerializeField({
		@SerializeField(clazz = AuditModelSet.class, includes = { "id", "modelId",
				"items" }),
		@SerializeField(clazz = AuditModelItem.class, includes = { "id", "modelItemId",
		 }),})
	public ResponseInfo get(@NotNull @PathVariable(value = "id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 添加/修改模型设置
	 * 
	 * @param: auditModelSet 模型
	 * @throws: GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping()
	public ResponseInfo save(HttpServletRequest request, @RequestBody @Valid AuditModelDto dto)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		dto.setSiteId(siteId);
		return auditModelSetService.saveOrUpdateModelSet(dto);
	}

	/**
	 * 删除模型
	 * @param: @param ids
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		return super.deleteBeatch(dels.getIds());
	}
}
