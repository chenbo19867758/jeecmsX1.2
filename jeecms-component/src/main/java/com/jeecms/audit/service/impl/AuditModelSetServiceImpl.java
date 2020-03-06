/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.service.impl;

import com.jeecms.audit.dao.AuditModelSetDao;
import com.jeecms.audit.domain.AuditModelItem;
import com.jeecms.audit.domain.AuditModelSet;
import com.jeecms.audit.domain.dto.AuditModelDto;
import com.jeecms.audit.domain.vo.AuditModelVo;
import com.jeecms.audit.service.AuditModelItemService;
import com.jeecms.audit.service.AuditModelSetService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.content.service.CmsModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模型设置实现类
* @author ljw
* @version 1.0
* @date 2019-12-16
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditModelSetServiceImpl extends BaseServiceImpl<AuditModelSet,AuditModelSetDao, Integer>  
		implements AuditModelSetService {

	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private AuditModelItemService auditModelItemService;
	@Autowired
	private CmsModelItemService cmsModelItemService;
	
	@Override
	public List<AuditModelVo> models(Integer siteId) throws GlobalException {
		List<AuditModelVo> vos = new ArrayList<AuditModelVo>(16);
		List<CmsModel> models = cmsModelService.findList(CmsModel.CONTENT_TYPE, siteId);
		//查询该站点已经设置了的模型
		Map<String, String[]> params = new HashMap<>(20);
		params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
		List<AuditModelSet> sets = super.getList(params, null, true);
		List<Integer> list = sets.stream().map(AuditModelSet::getModelId).collect(Collectors.toList());
		for (CmsModel cmsModel : models) {
			AuditModelVo vo = new AuditModelVo(cmsModel.getId(), cmsModel.getModelName());
			//判断是否置灰
			if (list.contains(cmsModel.getId())) {
				vo.setGray(true);
			} else {
				vo.setGray(false);
			}
			vos.add(vo);
		}
		return vos;
	}

	@Override
	public ResponseInfo saveOrUpdateModelSet(AuditModelDto dto) throws GlobalException {
		//先删再增，到达修改的效果
		List<AuditModelItem> entities = new ArrayList<AuditModelItem>(10);
		if (dto.getId() != null) {
			deleteAuditModelItems(dto.getId());
			AuditModelSet modelSet = findById(dto.getId());
			List<CmsModelItem> itemList = cmsModelItemService.findAllById(dto.getItems());
			for (CmsModelItem cmsModelItem : itemList) {
				AuditModelItem item = new AuditModelItem();
				item.setAuditModelId(dto.getId());
				item.setModelItemId(cmsModelItem.getId());
				item.setModelItemField(cmsModelItem.getField());
				item.setModelItem(cmsModelItem);
				entities.add(item);
			}
		} else {
			AuditModelSet set = new AuditModelSet();
			set.setSiteId(dto.getSiteId());
			set.setModelId(dto.getModelId());
			set.setModel(cmsModelService.findById(dto.getModelId()));
			set = super.save(set);
			List<CmsModelItem> items = cmsModelItemService.findAllById(dto.getItems());
			for (CmsModelItem cmsModelItem : items) {
				AuditModelItem item = new AuditModelItem();
				item.setAuditModelId(set.getId());
				item.setModelItemId(cmsModelItem.getId());
				item.setModelItemField(cmsModelItem.getField());
				item.setModelItem(cmsModelItem);
				entities.add(item);
			}
		}
		auditModelItemService.saveAll(entities);
		return new ResponseInfo();
	}

	/**物理删除审核模型字段**/
	void deleteAuditModelItems(Integer id) throws GlobalException {
		//直接物理删除
		List<AuditModelItem> items = auditModelItemService.findByAuditModelId(id);
		if (!items.isEmpty()) {
			List<Integer> idList = items.stream().map(AuditModelItem::getId)
					.collect(Collectors.toList());
			auditModelItemService.physicalDelete(idList.toArray(new Integer[idList.size()]));
		}
	}

	@Override
	public List<AuditModelItem> findByModelId(Integer modelId) {
		List<AuditModelSet> auditModelSets = dao.findByModelIdAndHasDeleted(modelId, false);
		// 如果未查询出模型配置直接抛出null
		if (CollectionUtils.isEmpty(auditModelSets)) {
			return null;
		}
		// 取第一个模型的配置(如果这里写错了写出来两个的话)的字段集合
		List<AuditModelItem> items = auditModelSets.get(0).getItems();
		if (CollectionUtils.isEmpty(items)) {
			return null;
		}
		return items;
	}
}