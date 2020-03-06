/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.content.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.dao.CmsModelDao;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.dto.CmsModelDto;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.util.SystemContextUtils;

/**
 * 模型service实现
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月19日 上午9:08:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CmsModelServiceImpl extends BaseServiceImpl<CmsModel, CmsModelDao, Integer> implements CmsModelService {
	
	/**定义提供客户端需要的模型字段数据过滤模式
	 * ALL ：原始输出，不进行过滤
	 * SHOW_CHANNEL_AND_CONTENT：栏目及内容模型的字段中过滤掉有些特殊字段需要过滤，具体为：
	 * SHOW_MEMBER_REGISTOR : 会员模型的字段中过滤设置 “应用到会员注册” 字段*/
	public enum FilterModel{ALL,SHOW_CHANNEL_AND_CONTENT,SHOW_MEMBER_REGISTOR}
	
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public List<CmsModel> getList(boolean containDisabled, Integer siteId) {
        return dao.findByIsEnableAndSiteIdAndHasDeleted(containDisabled,
                siteId, false);
    }

    @Override
    public CmsModel getDefModel(Integer siteId) {
        List<CmsModel> models = getList(false, siteId);
        if (models != null && models.size() > 0) {
            return models.get(0);
        }
        return null;
    }
    
    @Override
	public void updatePriority(DragSortDto sortDto) throws GlobalException{
    	CmsModel  fromModel =  this.findById(sortDto.getFromId());
    	CmsModel toModel = this.findById(sortDto.getToId());
    	/**移动目标设置成目标位置排序，排序权重根据原始拖动元素相对于拖动目标元素位置的前后改变位置
    	*在前拖动元素位置排序权重-1
    	*在后拖动元素位置排序权重+1
    	*/
    	if(fromModel.getSortNum() >=  toModel.getSortNum() && fromModel.getSortWeight() > toModel.getSortWeight()
    			&& fromModel.getCreateTime().compareTo(toModel.getCreateTime()) > 0){
        	fromModel.setSortWeight(toModel.getSortWeight()-1);
    	}else {
        	fromModel.setSortWeight(toModel.getSortWeight()+1);
    	}
    	fromModel.setSortNum(toModel.getSortNum());
    	super.update(fromModel);
	}
    

    @Override
    public ResponseInfo getModelPage(Short tplType, Short isGlobal, Boolean isDisable,
            String modelName, Integer siteId, Pageable pageable)
            throws GlobalException {
        return new ResponseInfo(dao.getPage(tplType, isGlobal, isDisable, modelName,
                siteId, pageable));
    }

    @Override
    public ResponseInfo saveThisSiteModel(CmsModel model)
            throws GlobalException {
    	//本站模型不允许添加会员模型
    	if(CmsModel.MEMBER_TYPE.equals(model.getTplType())) {
			 throw new GlobalException(new SystemExceptionInfo(
	                    SettingErrorCodeEnum.LOCAL_SITE_MODEL_IS_NOT_ALLOW_CREATE_MEMBER_MODEL.getDefaultMessage(),
	                    SettingErrorCodeEnum.LOCAL_SITE_MODEL_IS_NOT_ALLOW_CREATE_MEMBER_MODEL.getCode()));
    	}
        model.setIsGlobal(CmsModel.THIS_SITE_MODEL);
        this.checkModelName(model);
        CmsModel maxModel = super.dao.getMaxModelType(model.getTplType());
        Integer sortNum = 0 , sortWeight = 0;
        if(maxModel != null) {
        	sortNum = maxModel.getSortNum()+1;
        	sortWeight = maxModel.getSortWeight()+1;
        }
        model.setSortNum(sortNum);
        model.setSortWeight(sortWeight);
        super.save(model);
        return new ResponseInfo();
    }

    @Override
    public ResponseInfo saveWholeSiteModel(CmsModel model)
            throws GlobalException {
    	//全站模型仅允许添加一个会员模型
    	if(CmsModel.MEMBER_TYPE.equals(model.getTplType())) {
    		List<CmsModel> models = dao.getList(CmsModel.MEMBER_TYPE, true, null);
    		if(!CollectionUtils.isEmpty(models)) {
    			 throw new GlobalException(new SystemExceptionInfo(
 	                    SettingErrorCodeEnum.MEMBER_MODEL_IS_EXIST.getDefaultMessage(),
 	                    SettingErrorCodeEnum.MEMBER_MODEL_IS_EXIST.getCode()));
    		}
    		//添加会员模型默认为启用状态
    		model.setIsEnable(true);
    	}
        model.setIsGlobal(CmsModel.WHOLE_SITE_MODEL);
        this.checkModelName(model);
        CmsModel maxModel = super.dao.getMaxModelType(model.getTplType());
        Integer sortNum = 0 , sortWeight = 0;
        if(maxModel != null) {
        	sortNum = maxModel.getSortNum()+1;
        	sortWeight = maxModel.getSortWeight()+1;
        }
        model.setSortNum(sortNum);
        model.setSortWeight(sortWeight);
        super.save(model);
        return new ResponseInfo();
    }

    @Override
    public ResponseInfo updateModel(CmsModel model) throws GlobalException {
        CmsModel modelInfo = super.get(model.getId());
        if(CmsModel.MEMBER_TYPE.equals(modelInfo.getTplType())) {
        	 throw new GlobalException(new SystemExceptionInfo(
                     SettingErrorCodeEnum.MEMBER_IS_ALLOW_UPDATE.getDefaultMessage(),
                     SettingErrorCodeEnum.MEMBER_IS_ALLOW_UPDATE.getCode()));
        }
        model.setTplType(modelInfo.getTplType());
        super.update(model);
        return new ResponseInfo();
    }

    @Override
    public ResponseInfo isEnable(CmsModelDto modelDto) throws GlobalException {
        CmsModel model = super.get(modelDto.getId());
        model.setIsEnable(modelDto.getIsEnable());
        super.update(model);
        return new ResponseInfo();
    }

    @Override
    public ResponseInfo checkModelName(Integer id ,Short tplType, String modelName,Integer siteId,Short isGlobal)
            throws GlobalException {
        CmsModel modelInfo = dao.findByModelNameAndTplTypeAndHasDeleted(
                modelName, tplType, false);
        if(modelInfo != null) {
        	if(isGlobal.equals(CmsModel.THIS_SITE_MODEL) ) {
        		//本站模型中,
        		//新增下相同站点内不允许重复；不同站点内可以重复; 
        		//编辑情况下,和自身相同可用，和自身不同且为本站其他模型不可用，和自身不同且为其他站点模型可用
        		if(id != null ) {
        			if(id.equals(modelInfo.getId())) {
        				return new ResponseInfo(Boolean.TRUE);
        			}else {
        				if(modelInfo.getSite() !=null || siteId.equals(modelInfo.getSiteId()))  {
        					return new ResponseInfo(Boolean.TRUE);
        				}else {
        					return new ResponseInfo(Boolean.FALSE);
        				}
        			}
        		}else {
    				if(modelInfo.getSite() !=null && siteId.equals(modelInfo.getSiteId())) {
    					return new ResponseInfo(Boolean.FALSE);
        			}else{
        				return new ResponseInfo(Boolean.TRUE);
        			}
        		}
        	}else {
        		//全站模型中，新增下校验全局不允许重复，编辑情况下相同依然可用 
        		if(id != null &&  id.equals(modelInfo.getId())) {
        			return new ResponseInfo(Boolean.TRUE);
        		}else {
        			return new ResponseInfo(Boolean.FALSE);
        		}
        	}
		}
		return new ResponseInfo(Boolean.TRUE);
    }
    
    /**
     * 校验modelName，同一类型下的modelName不能相同
     * 
     * @Title: checkModelName
     * @param model
     * @throws GlobalException
     * @return: void
     */
    private void checkModelName(CmsModel model) throws GlobalException {
        CmsModel modelInfo = dao.findByModelNameAndTplTypeAndHasDeleted( model.getModelName(), model.getTplType(), false);
        if (modelInfo != null) {
            throw new GlobalException(new SystemExceptionInfo(
                    SettingErrorCodeEnum.MODEL_NAME_ALREADY_EXIST.getDefaultMessage(),
                    SettingErrorCodeEnum.MODEL_NAME_ALREADY_EXIST.getCode()));
        }
    }
    
	@Override
	public CmsModel getInfo(Integer id) throws GlobalException {
		return getCmsModelByCondition(id,FilterModel.ALL);
		
	}
	
	@Override
	public CmsModel getChannelOrContentModel(Integer id) throws GlobalException {
		return getCmsModelByCondition(id,FilterModel.SHOW_CHANNEL_AND_CONTENT);
	}
	
	@Override
	public CmsModel getFrontMemberModel() throws GlobalException {
		return getCmsModelByCondition(null,FilterModel.SHOW_MEMBER_REGISTOR);
	}
	
	/**
	 *  通过id获取及过滤模式获取需要的模型及模型字段信息
	 *  id 为null时，默认获取会员模型
	 *  过滤模式见 {@link FilterModel}}
	 * @param id   
	 * @param filterModel
	 * @return
	 * @throws GlobalException
	 */
	private CmsModel getCmsModelByCondition(Integer id, FilterModel filterModel) throws GlobalException {
		CmsModel model = null;
		if (null != id) {
			model = super.findById(id);
		} else {
			List<CmsModel> models = dao.getList(CmsModel.MEMBER_TYPE, true, null);
			if (CollectionUtils.isEmpty(models)) {
				return null;
			}
			model = models.get(0);
		}
		JSONObject enableJson = new JSONObject();
		List<String> groupTypes = new ArrayList<String>(0);
		// 根据不同模型类型，初始化默认字段分组数据
		if (CmsModel.CHANNEL_TYPE.equals(model.getTplType())) {
			groupTypes = new ArrayList<String>(Arrays.asList(CmsModelItem.CHANNEL_GROUP_TYPE));
		} else if (CmsModel.CONTENT_TYPE.equals(model.getTplType())) {
			groupTypes = new ArrayList<String>(Arrays.asList(CmsModelItem.CONTENT_GROUP_TYPE));
		} else if (CmsModel.MEMBER_TYPE.equals(model.getTplType())) {
			groupTypes = new ArrayList<String>(Arrays.asList(CmsModelItem.MEMBER_GROUP_TYPE));
		}
		GlobalConfigAttr configAttr = SystemContextUtils.getGlobalConfig(RequestUtils.getHttpServletRequest())
				.getConfigAttr();
		if (!CollectionUtils.isEmpty(model.getItems())) {
			// 现在仅是修改了内容模型，所以只有内容模型需要如此处理
			if (CmsModel.CONTENT_TYPE.equals(model.getTplType())) {
				// 判断其是否存在老数据(不存在块分组)
				List<CmsModelItem> notBlackItems = model.getItems().stream()
						.filter(item -> item.getBlockName() == null).collect(Collectors.toList());
				// 没有块分组，说明该模型不是老数据，是新数据
				if (CollectionUtils.isEmpty(notBlackItems)) {
					// 根据分组信息进行分组
					Map<String, List<CmsModelItem>> groupMaps = model.getItems().stream()
							.filter(item -> item.getBlockName() != null)
							.collect(Collectors.groupingBy(CmsModelItem::getGroupType));
					for (Map.Entry<String, List<CmsModelItem>> item : groupMaps.entrySet()) {
						String key = item.getKey();
						// 删除掉现在需要处理的 字段 分组
						groupTypes.remove(key);
						List<CmsModelItem> list = item.getValue();
						JSONArray groupJsons = new JSONArray();
						// 判断该分组下是否含有字段集，为空跳过，赋值空数组
						if (CollectionUtils.isEmpty(list)) {
							enableJson.put(key, groupJsons);
							continue;
						}
						// 根据分组信息+块名进行分组
						Map<Integer, List<CmsModelItem>> blackMaps = list.stream()
								.collect(Collectors.groupingBy(CmsModelItem::getBlockName));
						for (Map.Entry<Integer, List<CmsModelItem>> blackItem : blackMaps.entrySet()) {
							// 块JSON集合
							JSONArray blackJsons = new JSONArray();
							// 块内集合进行分组，根据分组信息+块名+排序值进行分组(块内可能分为两小块)
							Map<Integer, List<CmsModelItem>> rowMaps = blackItem.getValue()
									.stream()
									.sorted(Comparator.comparing(CmsModelItem::getSortNum))
									.collect(Collectors.groupingBy(CmsModelItem::getSortNum));
							for (Entry<Integer, List<CmsModelItem>> rowItemMap : rowMaps.entrySet()) {
								// 行JSON集合
								JSONArray rowJsons = new JSONArray();
								// 处理一行
								rowJsons = this.filterItem(rowItemMap.getValue(), rowJsons, filterModel, configAttr, false, false);
								if (CollectionUtils.isEmpty(rowJsons)) {
									continue;
								}
								// 将块装入块中
								blackJsons.add(rowJsons);
							}
							if (CollectionUtils.isEmpty(blackJsons)) {
								continue;
							}
							// 将块转入到分组JSON集合中
							groupJsons.add(blackJsons);
						}
						// 将分组JSON集合放入到未启用JSON中
						enableJson.put(key, groupJsons);
					}
					// 由于保存字段时，没有字段掉分组会过滤不存入数据db，应前端要求，没有字段的分组也要返回key,并且值为空数组
					for (String str : groupTypes) {
						enableJson.put(str, new JSONArray());
					}
				} else {
					// 单图上传、多图上传、视频上传、音频上传、附件上传、正文 这些数据类型需要进行块分组
					List<String> dataTypeNames = Arrays.asList(CmsModelConstant.SINGLE_CHART_UPLOAD,
							CmsModelConstant.MANY_CHART_UPLOAD, CmsModelConstant.VIDEO_UPLOAD, CmsModelConstant.TYPE_SYS_CONTENT_RESOURCE,
							CmsModelConstant.AUDIO_UPLOAD, CmsModelConstant.ANNEX_UPLOAD, CmsModelConstant.CONTENT_TXT);
					// 分组JSON集合
					JSONArray groupJsons = new JSONArray();
					// 需要块分组的 模型字段 集合
					List<CmsModelItem> dataTypeItems = model.getItems().stream()
							.filter(item -> dataTypeNames.contains(item.getDataType())).collect(Collectors.toList());
					List<CmsModelItem> singleImages = dataTypeItems.stream().filter(item -> CmsModelConstant.TYPE_SYS_CONTENT_RESOURCE.equals(item.getDataType())).collect(Collectors.toList());
					// 不需要分组的 模型字段 集合
					List<CmsModelItem> notDataTypeItems = model.getItems().stream()
							.filter(item -> !dataTypeNames.contains(item.getDataType())).collect(Collectors.toList());
					// 不需要分组的模型集合，不为null，不为空，进行数据处理
					if (!CollectionUtils.isEmpty(notDataTypeItems)) {
						// 使用分组，并将块JSON集合传入其中，每一次遍历都将产生一个JSON集合，并将集合放入到块JSON集合中
						groupJsons = this.filterItem(notDataTypeItems, groupJsons, filterModel, configAttr,true,true);
					}
					// 需要分组的模型集合，不为null，不为空进行数据处理
					if (!CollectionUtils.isEmpty(dataTypeItems)) {
						// 将需要分组的模型集合通过字段类型进行分组
						Map<String, List<CmsModelItem>> dataTypeMaps = dataTypeItems.stream()
								.collect(Collectors.groupingBy(CmsModelItem::getDataType));
						for (Map.Entry<String, List<CmsModelItem>> dataTypeItem : dataTypeMaps.entrySet()) {
							List<CmsModelItem> values = dataTypeItem.getValue();
							if (CmsModelConstant.TYPE_SYS_CONTENT_RESOURCE.equals(dataTypeItem.getKey())) {
								continue;
							}
							if (dataTypeItem.getKey().equals(CmsModelConstant.SINGLE_CHART_UPLOAD)) {
								if (!CollectionUtils.isEmpty(singleImages)) {
									values.addAll(singleImages);
								}
							}
							// 块JSON集合
							JSONArray blackJsons = new JSONArray();
							// 遍历分组，将分组中的 模型字段集合通过
							JSONArray rowJsons = new JSONArray();
							// 不适用分组将新建的行传入其中，每次遍历都将对象放入行json中，后用块装行
							rowJsons = this.filterItem(values, rowJsons, filterModel, configAttr, true, false);
							blackJsons.add(rowJsons);
							groupJsons.add(blackJsons);
						}
					}
					// 此处处理的数据为老数据(没有块分组)的数据，所以将所有的数据全部丢入主体中
					enableJson.put(CmsModelItem.FORM_LIST_BASE, groupJsons);
					// 由于保存字段时，没有字段掉分组会过滤不存入数据db，应前端要求，没有字段的分组也要返回key,并且值为空数组
					enableJson.put(CmsModelItem.FORM_LIST_EXTEND, new JSONArray());
				}
			} else {
				// 处理非内容模型(现在仅修改内容模型，所以此处仅说明处理该模型)
				enableJson = this.initNotContentModel(model, configAttr, filterModel, groupTypes, enableJson);
			}
		}
		model.setEnableJson(enableJson);
		return model;
	}
	
//	private CmsModel getCmsModelByCondition(Integer id, FilterModel filterModel) throws GlobalException {
//		CmsModel model = null;
//		if(null != id ) {
//			model = super.findById(id);
//		}else {
//			List<CmsModel> models = dao.getList(CmsModel.MEMBER_TYPE, true, null);
//			if( CollectionUtils.isEmpty(models)) {
//				return null;
//			}
//			model = models.get(0);
//		}
//		JSONObject enableJson = new JSONObject();
//		List<String> groupTypes = new ArrayList<String>(0) ;
//		//根据不同模型类型，初始化默认字段分组数据
//		if(CmsModel.CHANNEL_TYPE.equals(model.getTplType())) {
//			groupTypes =  new ArrayList<String>(Arrays.asList(CmsModelItem.CHANNEL_GROUP_TYPE));
//		}else if(CmsModel.CONTENT_TYPE.equals(model.getTplType())) {
//			groupTypes =  new ArrayList<String>(Arrays.asList(CmsModelItem.CONTENT_GROUP_TYPE));
//		}else if(CmsModel.MEMBER_TYPE.equals(model.getTplType())) {
//			groupTypes =  new ArrayList<String>(Arrays.asList(CmsModelItem.MEMBER_GROUP_TYPE));
//		}
//		if( !CollectionUtils.isEmpty(model.getItems())) {
//			Map<String,	List<CmsModelItem>>  groupMaps = model.getItems().stream().collect(Collectors.groupingBy(CmsModelItem::getGroupType));
//			for (Map.Entry<String, 	List<CmsModelItem>> item : groupMaps.entrySet()) {
//				String key = item.getKey();
//				//删除掉已存在字段掉分组
//				groupTypes.remove(key);
//				List<CmsModelItem>  list  = item.getValue();
//				JSONArray fieldJsons = new JSONArray();
//				//判断该分组下是否含有字段集，为空跳过，赋值空数组
//				if( CollectionUtils.isEmpty(list)) {
//					enableJson.put(key, fieldJsons);
//					continue;
//				}
//				//按照sortnum排序
//				list = list.stream().sorted(Comparator.comparing(CmsModelItem::getSortNum)).collect(Collectors.toList());
//				for (CmsModelItem field : list) {
//					JSONObject jsonObj = JSON.parseObject(field.getContent());
//					if(FilterModel.SHOW_CHANNEL_AND_CONTENT.equals(filterModel)) {
//						//如果系统设置尚未开启内容密级则过滤内容密级
//						if(CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT.equals(field.getField()) && !SystemContextUtils.getGlobalConfig(RequestUtils.getHttpServletRequest()).getConfigAttr().getOpenContentIssue()) {
//							continue;
//						}
//						//如果系统设置尚未开启内容密级则过滤内容密级
//						if(CmsModelConstant.FIELD_SYS_CONTENT_SECRET.equals(field.getField()) && !SystemContextUtils.getGlobalConfig(RequestUtils.getHttpServletRequest()).getConfigAttr().getOpenContentSecurity()) {
//							continue;
//						}
//					}else if(FilterModel.SHOW_MEMBER_REGISTOR.equals(filterModel)) {
//						//显示会员模型 ,isRegister 为空或为false时，默认为不应用到前台
//						JSONObject valueObje = jsonObj.getJSONObject(CmsModelItem.VALUE) ;
//						if( valueObje == null || (valueObje != null
//								&& valueObje.getBoolean(CmsModelItem.IS_REGISTER) != null
//								&& ! valueObje.getBoolean(CmsModelItem.IS_REGISTER))) continue;
//					}
//					fieldJsons.add(jsonObj);
//				}
//				enableJson.put(key, fieldJsons);
//			}
//			//由于保存字段时，没有字段掉分组会过滤不存入数据db，应前端要求，没有字段的分组也要返回key,并且值为空数组
//			for (String str : groupTypes) {
//				enableJson.put(str, new JSONArray());
//			}
//		}
//		model.setEnableJson(enableJson);
//		return model;
//	}
	
	/**
	 * 初始化非内容模型数据
	 * @Title: initNotContentModel  
	 * @param model			模型
	 * @param configAttr	全局配置
	 * @param filterModel	定义提供客户端需要的模型字段数据过滤模式
	 * @param groupTypes	分组类型
	 * @param enableJson	未启用JSON对象
	 * @return: JSONObject
	 */
	private JSONObject initNotContentModel(CmsModel model, GlobalConfigAttr configAttr, 
			FilterModel filterModel, List<String> groupTypes,JSONObject enableJson) {
		Map<String, List<CmsModelItem>> groupMaps = model.getItems().stream()
				.collect(Collectors.groupingBy(CmsModelItem::getGroupType));
		for (Map.Entry<String, List<CmsModelItem>> item : groupMaps.entrySet()) {
			String key = item.getKey();
			// 删除掉已存在字段掉分组
			groupTypes.remove(key);
			List<CmsModelItem> list = item.getValue();
			JSONArray fieldJsons = new JSONArray();
			// 判断该分组下是否含有字段集，为空跳过，赋值空数组
			if (CollectionUtils.isEmpty(list)) {
				enableJson.put(key, fieldJsons);
				continue;
			}
			fieldJsons = this.filterItem(list, fieldJsons, filterModel, configAttr, true, false);
			enableJson.put(key, fieldJsons);
		}
		// 由于保存字段时，没有字段掉分组会过滤不存入数据db，应前端要求，没有字段的分组也要返回key,并且值为空数组
		for (String str : groupTypes) {
			enableJson.put(str, new JSONArray());
		}
		return enableJson;
	}
	
	/**
	 * 处理模型字段排除某些字段
	 * @Title: filterItem  
	 * @param items			模型字段list集合
	 * @param array			装数据的JSON集合
	 * @param filterModel	定义提供客户端需要的模型字段数据过滤模式
	 * @param configAttr	全局配置
	 * @param isSortNum		是否通过sortNum进行排序：true->sortNum升序，false->sortWeight升序
	 * @param isNewArray	是否通过
	 * @return: JSONArray
	 */
	private JSONArray filterItem(List<CmsModelItem> items, JSONArray array, FilterModel filterModel,
			GlobalConfigAttr configAttr,boolean isSortNum,boolean isNewArray) {
		
		if (isSortNum) {
			// 块内小块，或者块进行排序
			items = items.stream().sorted(Comparator.comparing(CmsModelItem::getSortNum))
					.collect(Collectors.toList());
		} else {
			// 块内进行排序
			items = items.stream().sorted(Comparator.comparing(CmsModelItem::getSortWeight))
					.collect(Collectors.toList());
		}
		
		for (CmsModelItem item : items) {
			JSONObject jsonObj = JSON.parseObject(item.getContent());
			if (FilterModel.SHOW_CHANNEL_AND_CONTENT.equals(filterModel)) {
				// 如果系统设置尚未开启内容密级则过滤内容密级
				if (CmsModelConstant.FIELD_SYS_CONTENT_POST_CONTENT.equals(item.getField())
						&& !configAttr.getOpenContentIssue()) {
					continue;
				}
				// 如果系统设置尚未开启内容密级则过滤内容密级
				if (CmsModelConstant.FIELD_SYS_CONTENT_SECRET.equals(item.getField())
						&& !configAttr.getOpenContentSecurity()) {
					continue;
				}
			} else if (FilterModel.SHOW_MEMBER_REGISTOR.equals(filterModel)) {
				// 显示会员模型 ,isRegister 为空或为false时，默认为不应用到前台
				JSONObject valueObje = jsonObj.getJSONObject(CmsModelItem.VALUE);
				if (valueObje == null || (valueObje != null && valueObje.getBoolean(CmsModelItem.IS_REGISTER) != null
						&& !valueObje.getBoolean(CmsModelItem.IS_REGISTER))) {
					continue;
				}
			}
			if (jsonObj.get(CmsModelItem.SORT_WEIGHT_NAME) == null) {
				jsonObj.put(CmsModelItem.SORT_WEIGHT_NAME, null);
			}
			if (isNewArray) {
				JSONArray blacks = new JSONArray();
				JSONArray rows = new JSONArray();
				rows.add(jsonObj);
				blacks.add(rows);
				array.add(blacks);
			} else {
				array.add(jsonObj);
			}
		}
		return array;
	}
	
	@Override
	public List<CmsModel> getModelList(Short tplType, Boolean isEnable, Integer siteId) throws GlobalException {
		return dao.getList(tplType, isEnable, siteId);
	}
	
	@Override
	public List<CmsModel> findList(Short tplType, Integer siteId) throws GlobalException {
		
		return dao.getList(tplType, true, siteId);
	}

}
