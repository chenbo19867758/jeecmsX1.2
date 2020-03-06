/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.dao.CmsModelDao;
import com.jeecms.content.dao.CmsModelItemDao;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.vo.ModelItemDto;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.content.service.FlowService;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.util.SystemContextUtils;

/**
 * 模型字段service实现层
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月17日 下午3:13:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CmsModelItemServiceImpl extends BaseServiceImpl<CmsModelItem, CmsModelItemDao, Integer>
		implements CmsModelItemService {

	@Override
	public ResponseInfo saveCmsModelItem(ModelItemDto modelItemDto) throws GlobalException {
		CmsModel oldModel = cmsModelService.get(modelItemDto.getModelId());
		if (CmsModel.CONTENT_TYPE.equals(oldModel.getTplType())) {
			return this.saveContentCmsModelItem(modelItemDto, oldModel);
		}
		return this.saveOtherCmsModelItem(modelItemDto, oldModel);
	}
	
	/**
	  * 新增内容模型字段
	  * @Title: saveContentCmsModelItem
	  * @param modelItemDto	模型字段提交dto
	  * @param oldModel		当前修改的模型对象
	  * @throws GlobalException	全局异常
	  * @return: ResponseInfo
	 */
	private ResponseInfo saveContentCmsModelItem(ModelItemDto modelItemDto, CmsModel oldModel) throws GlobalException {
		// 删除对应的信息
		dao.deleteModelItems(modelItemDto.getModelId());
		List<CmsModelItem> items = new ArrayList<>();
		// 将模型启动字段转换成JSON对象，这也是需要处理的数据
		JSONObject enableJson = JSONObject.parseObject(modelItemDto.getEnableJson());
		CmsModel cmsModel = cmsModelService.get(modelItemDto.getModelId());
		// 覆盖保存未启用字段
		cmsModel.setUnEnableJsonStr(modelItemDto.getUnEnableJson());
		// 覆盖保存启用字段启用字段
		for (Entry<String, Object> entry : enableJson.entrySet()) {
			if (entry.getValue() instanceof JSONArray) {
				// 取出分组的JSON集合
				JSONArray groupJson = (JSONArray) entry.getValue();
				for (int i = 0; i < groupJson.size(); i++) {
					// 取出分组中块JSON集合
					JSONArray jsonBolcks = groupJson.getJSONArray(i);
					for (int j = 0; j < jsonBolcks.size(); j++) {
						// 取出块中行JSON集合
						JSONArray rows = jsonBolcks.getJSONArray(j);
						if (rows == null) {
							continue;
						}
						for (int k = 0; k < rows.size(); k++) {
							// 取出行中单个对象
							JSONObject jsonItem = rows.getJSONObject(k);
							JSONObject itemVal = jsonItem.getJSONObject(CmsModelItem.COMPENT_VAL_KEY);
							CmsModelItem modelItem = new CmsModelItem();
							modelItem.setModel(cmsModel);
							// 字段名称
							modelItem.setField(itemVal.getString(CmsModelItem.FIELD));
							// 字段label
							modelItem.setItemLabel(itemVal.getString(CmsModelItem.ITEM_LABEL));
							// 字段默认值
							modelItem.setDefValue(itemVal.getString(CmsModelItem.DEF_VALUE));
							// 字段是否必填
							modelItem.setIsRequired(itemVal.getBoolean(CmsModelItem.IS_REQUIRED));
							// 字段提示文字
							modelItem.setPlaceholder(itemVal.getString(CmsModelItem.PLACEHOLDER));
							// 字段辅助文字
							modelItem.setTipText(itemVal.getString(CmsModelItem.TIP_TEXT));
							// 字段类型，详见 {@link CmsModelItem#DataTypeEnum}
							modelItem.setDataType(jsonItem.getString(CmsModelItem.DATE_TYPE));
							// 字段分组，详见 {@link CmsModelItem}
							modelItem.setGroupType(entry.getKey());
							// 字段是否自定义
							modelItem.setIsCustom(jsonItem.getBoolean(CmsModelItem.IS_CUSTOM));
							// 字段在当前模型中的分组中的显示顺序
							modelItem.setSortNum(jsonItem.getInteger(CmsModelItem.INDEX));
							// 该字段的完整json字符串格式数据
							modelItem.setContent(jsonItem.toJSONString());
							// i为分组块集合中的顺序，i即为该组中的块的唯一值
							modelItem.setBlockName(i);
							modelItem.setSortWeight(jsonItem.getInteger(CmsModelItem.SORT_WEIGHT_NAME));
							items.add(modelItem);
						}
					}
				}
			}
		}
		super.saveAll(items);
		super.flush();
		cmsModelService.update(cmsModel);
		cmsModelService.flush();
		return new ResponseInfo();
	}

	/**
	  * 新增其它模型(栏目或会员)字段
	  * @Title: saveOtherCmsModelItem
	  * @param modelItemDto	模型字段提交dto
	  * @param oldModel		当前需要修改的模型对象
	  * @throws GlobalException	全局异常
	  * @return: ResponseInfo
	 */
	private ResponseInfo saveOtherCmsModelItem(ModelItemDto modelItemDto, CmsModel oldModel) throws GlobalException {
		boolean oldIsWorkflow = oldModel.existItem(CmsModelConstant.FIELD_SYS_WORKFLOW);
		// 删除对应的信息
		dao.deleteModelItems(modelItemDto.getModelId());
		List<CmsModelItem> items = new ArrayList<>();
		JSONObject enableJson = JSONObject.parseObject(modelItemDto.getEnableJson());
		CmsModel cmsModel = cmsModelService.get(modelItemDto.getModelId());
		// 覆盖保存未启用字段
		cmsModel.setUnEnableJsonStr(modelItemDto.getUnEnableJson());
		boolean newIsWorkflowId = false;
		// 覆盖保存启用字段启用字段
		for (Entry<String, Object> entry : enableJson.entrySet()) {
			if (entry.getValue() instanceof JSONArray) {
				JSONArray jsons = (JSONArray) entry.getValue();
				for (int i = 0; i < jsons.size(); i++) {
					JSONObject jsonItem = jsons.getJSONObject(i);
					JSONObject itemVal = jsonItem.getJSONObject(CmsModelItem.COMPENT_VAL_KEY);
					CmsModelItem modelItem = new CmsModelItem();
					modelItem.setModel(cmsModel);
					// 字段名称
					modelItem.setField(itemVal.getString(CmsModelItem.FIELD));

					if (CmsModelConstant.FIELD_SYS_WORKFLOW.equals(itemVal.getString(CmsModelItem.FIELD))) {
						newIsWorkflowId = true;
					}

					// 字段label
					modelItem.setItemLabel(itemVal.getString(CmsModelItem.ITEM_LABEL));
					// 字段默认值
					modelItem.setDefValue(itemVal.getString(CmsModelItem.DEF_VALUE));
					// 字段是否必填
					modelItem.setIsRequired(itemVal.getBoolean(CmsModelItem.IS_REQUIRED));
					// 字段提示文字
					modelItem.setPlaceholder(itemVal.getString(CmsModelItem.PLACEHOLDER));
					// 字段辅助文字
					modelItem.setTipText(itemVal.getString(CmsModelItem.TIP_TEXT));
					// 字段类型，详见 {@link CmsModelItem#DataTypeEnum}
					modelItem.setDataType(jsonItem.getString(CmsModelItem.DATE_TYPE));
					// 字段分组，详见 {@link CmsModelItem}
					modelItem.setGroupType(entry.getKey());
					// 字段是否自定义
					modelItem.setIsCustom(jsonItem.getBoolean(CmsModelItem.IS_CUSTOM));
					// 字段在当前模型中的分组中的显示顺序
					modelItem.setSortNum(jsonItem.getInteger(CmsModelItem.INDEX));
					// 该字段的完整json字符串格式数据
					modelItem.setContent(jsonItem.toJSONString());
					items.add(modelItem);
				}
			}
		}
		CmsModel bean = cmsModelService.update(cmsModel);
		super.saveAll(items);
		super.flush();
		/**
		 * 原先模型有工作流：1. 有工作流 2. 没工作流 ->处理工作流 原型模型没工作流
		 */
		if (oldIsWorkflow) {
			if (!newIsWorkflowId) {
				List<Integer> contentIds = channelService.getContentByModel(bean.getId());
				if (contentIds.size() > 0) {
					flowService.doInterruptDataFlow(ContentConstant.WORKFLOW_DATA_TYPE_CONTENT, contentIds,
							SystemContextUtils.getCoreUser());
				}
			}
		}
		return new ResponseInfo();
	}

//	@Override
//	public ResponseInfo saveCmsModelItem(ModelItemDto modelItemDto) throws GlobalException {
//		CmsModel oldModel = cmsModelService.get(modelItemDto.getModelId());
//		boolean oldIsWorkflow = oldModel.existItem(CmsModelConstant.FIELD_SYS_WORKFLOW);
//		// 删除对应的信息
//		dao.deleteModelItems(modelItemDto.getModelId());
//		List<CmsModelItem> items = new ArrayList<>();
//		JSONObject enableJson = JSONObject.parseObject(modelItemDto.getEnableJson());
//		CmsModel cmsModel = cmsModelService.get(modelItemDto.getModelId());
//		// 覆盖保存未启用字段
//		cmsModel.setUnEnableJsonStr(modelItemDto.getUnEnableJson());
//		boolean newIsWorkflowId = false;
//		// 覆盖保存启用字段启用字段
//		for (Entry<String, Object> entry : enableJson.entrySet()) {
//			if (entry.getValue() instanceof JSONArray) {
//				JSONArray jsons = (JSONArray) entry.getValue();
//				for (int i = 0; i < jsons.size(); i++) {
//					JSONObject jsonItem = jsons.getJSONObject(i);
//					JSONObject itemVal = jsonItem.getJSONObject(CmsModelItem.COMPENT_VAL_KEY);
//					CmsModelItem modelItem = new CmsModelItem();
//					modelItem.setModel(cmsModel);
//					// 字段名称
//					modelItem.setField(itemVal.getString(CmsModelItem.FIELD));
//					
//					if (CmsModelConstant.FIELD_SYS_WORKFLOW.equals(itemVal.getString(CmsModelItem.FIELD))) {
//						newIsWorkflowId = true;
//					}
//					
//					// 字段label
//					modelItem.setItemLabel(itemVal.getString(CmsModelItem.ITEM_LABEL));
//					// 字段默认值
//					modelItem.setDefValue(itemVal.getString(CmsModelItem.DEF_VALUE));
//					// 字段是否必填
//					modelItem.setIsRequired(itemVal.getBoolean(CmsModelItem.IS_REQUIRED));
//					// 字段提示文字
//					modelItem.setPlaceholder(itemVal.getString(CmsModelItem.PLACEHOLDER));
//					// 字段辅助文字
//					modelItem.setTipText(itemVal.getString(CmsModelItem.TIP_TEXT));
//					// 字段类型，详见 {@link CmsModelItem#DataTypeEnum}
//					modelItem.setDataType(jsonItem.getString(CmsModelItem.DATE_TYPE));
//					// 字段分组，详见 {@link CmsModelItem}
//					modelItem.setGroupType(entry.getKey());
//					// 字段是否自定义
//					modelItem.setIsCustom(jsonItem.getBoolean(CmsModelItem.IS_CUSTOM));
//					// 字段在当前模型中的分组中的显示顺序
//					modelItem.setSortNum(jsonItem.getInteger(CmsModelItem.INDEX));
//					// 该字段的完整json字符串格式数据
//					modelItem.setContent(jsonItem.toJSONString());
//					items.add(modelItem);
//				}
//			}
//		}
//		CmsModel bean = cmsModelService.update(cmsModel);
//		super.saveAll(items);
//		super.flush();
//		/**
//		 * 原先模型有工作流：1. 有工作流
//		 * 			   2. 没工作流	->处理工作流
//		 * 原型模型没工作流
//		 */
//		if (oldIsWorkflow) {
//			if (!newIsWorkflowId) {
//				 List<Integer> contentIds = channelService.getContentByModel(bean.getId());
//				 if (contentIds.size() > 0) {
//					 flowService.doInterruptDataFlow(ContentConstant.WORKFLOW_DATA_TYPE_CONTENT, contentIds,SystemContextUtils.getCoreUser());
//				}
//			}
//		}
//		return new ResponseInfo();
//	}

	@Override
	public List<CmsModelItem> findByModelId(Integer modelId) throws GlobalException {
		return dao.findByModelId(modelId);
	}

	@Override
	public List<CmsModelItem> findByModelIdAndType(Integer modelId, String type) throws GlobalException {
		return dao.findByModelIdAndGroupTypeAndHasDeleted(modelId, type, false);
	}

	public JSONObject getModelByModelIdOrModelTtype(Integer modelId, Integer modelType) {

		return null;
	}

	@Override
	public JSONArray getModelItemByModelId(Integer modelId) {
		CmsModel model = cmsModelService.findById(modelId);
		if (model.getHasDeleted()) {
			return null;
		}
		return getModelItem(model.getItems());
	}

	@Override
	public JSONArray getModelItemByMemberModel() {
		List<CmsModel> models = cmsModelDao.findByTplTypeAndHasDeleted(CmsModel.MEMBER_TYPE, false);
		if (!CollectionUtils.isEmpty(models)) {
			return null;
		}
		Set<CmsModelItem> items = models.get(0).getItems();
		return getModelItem(items);
	}

	private JSONArray getModelItem(Set<CmsModelItem> modelItems) {

		if (CollectionUtils.isEmpty(modelItems)) {
			return null;
		}
		modelItems = modelItems.stream().sorted(Comparator.comparing(CmsModelItem::getSortNum))
				.collect(Collectors.toSet());
		JSONArray jsons = new JSONArray();
		for (CmsModelItem item : modelItems) {
			// 排除已删除或json串为空数据
			if (item.getHasDeleted() || StringUtils.isBlank(item.getContent())) {
				continue;
			}
			jsons.add(JSON.parse(item.getContent()));
		}
		return jsons;
	}

	@Override
	public List<CmsModelItem> initializeCmsModelItem(List<CmsModelItem> modelItems, GlobalConfig globalConfig) {
		modelItems = modelItems.stream().sorted(Comparator.comparing(CmsModelItem::getSortNum)
				.thenComparing(Comparator.comparing(CmsModelItem::getCreateTime))).collect(Collectors.toList());
		// 如果系统设置尚未开启内容密级则过滤内容密级
		if (!globalConfig.getConfigAttr().getOpenContentSecurity()) {
			modelItems = modelItems.stream().filter(item -> !item.getField().equals("contentSecretId"))
					.collect(Collectors.toList());
		}
		// 如果系统设置尚未开启内容发文字号，过滤内容发文字号
		if (!globalConfig.getConfigAttr().getOpenContentIssue()) {
			modelItems = modelItems.stream().filter(item -> !item.getField().equals("postContent"))
					.collect(Collectors.toList());
		}
		return modelItems;
	}

	@Override
	public List<CmsModelItem> findByModelIdAndDataType(Integer modelId, String type) throws GlobalException {
		return dao.findByModelIdAndDataTypeAndHasDeleted(modelId, type, false);
	}

	@Override
	public List<CmsModelItem> collectionModelItem(Integer modelId) throws GlobalException {
		List<CmsModelItem> items = this.findByModelId(modelId);
		if (items != null && items.size() > 0) {
			items = items.stream()
					.filter(item -> StringUtils.equalsAny(item.getDataType(), "input", "textarea", "dateTime",
							"imageUpload", "videoUpload", "audioUpload", "fileUpload", "title", "subTitle", "tags",
							"abstract", "source", "releaseTime", "author", "content", "outsideLink", "keyword",
							"singleImage"))
					.collect(Collectors.toList());
		}
		return items;
	}

	@Override
	public Map<String, String> getDataType(Integer modelId, List<String> fields) throws GlobalException {
		List<CmsModelItem> items = this.findByModelId(modelId);
		Map<String, String> typeMap = new HashMap<String, String>(16);
		if (items != null && items.size() > 0) {
			Map<String, String> itemMap = items.stream()
					.collect(Collectors.toMap(CmsModelItem::getField, CmsModelItem::getDataType));
			for (String field : fields) {
				String dateType = itemMap.get(field);
				if (StringUtils.isNotBlank(dateType)) {
					typeMap.put(field, dateType);
				} else {
					typeMap.put(field, null);
				}
			}
		}
		if (typeMap.keySet().size() > 0) {
			return typeMap;
		}
		return null;
	}

	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private CmsModelDao cmsModelDao;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private FlowService flowService;

}
