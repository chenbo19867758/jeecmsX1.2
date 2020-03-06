/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.dao.ContentTxtDao;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentTxt;
import com.jeecms.content.domain.dto.ContentSaveDto;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.content.service.ContentTxtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容txt文本service实现类
 * @author: chenming
 * @date:   2019年7月11日 下午2:50:22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentTxtServiceImpl extends BaseServiceImpl<ContentTxt, ContentTxtDao, Integer>
		implements ContentTxtService {

	@Override
	public List<ContentTxt> saveTxts(List<ContentTxt> contentTxts, Content content) throws GlobalException {
		for (ContentTxt contentTxt : contentTxts) {
			contentTxt.setContent(content);
			contentTxt.setContentId(content.getId());
		}
		return super.saveAll(contentTxts);
	}

	@Override
	public void deleteTxts(Integer contentId) throws GlobalException {
		List<ContentTxt> contentTxts = dao.findByContentId(contentId);
		super.physicalDeleteInBatch(contentTxts);
	}

	@Override
	public Map<String, String> initContentTxt(JSONObject json, Integer modelId, ContentSaveDto dto,boolean isUpdate) 
			throws GlobalException {
		List<CmsModelItem> items = cmsModelItemService.findByModelId(modelId);
		// 
		items = items.stream().filter(
				item -> CmsModelConstant.CONTENT_TXT.equals(item.getDataType()))
				.collect(Collectors.toList());
		Map<String,String> txtMap = new LinkedHashMap<String, String>();
		// 处理自定义的富文本
		if (items != null && items.size() > 0) {
			for (CmsModelItem cmsModelItem : items) {
				String field = cmsModelItem.getField();
				String value = json.getString(field);
				if (isUpdate) {
					if (StringUtils.isNotBlank(value)) {
						txtMap.put(field, value);
					}
				} else {
					if (StringUtils.isNotBlank(value)) {
						txtMap.put(field, value);
					} 
				}
			}
		}
		return txtMap;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public List<ContentTxt> getTxts(Integer contentId) {
		return dao.findByContentId(contentId);
	}
	
	@Autowired
	private CmsModelItemService cmsModelItemService;


}