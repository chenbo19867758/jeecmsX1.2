/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.component.listener.ContentListener;
import com.jeecms.content.dao.ContentCheckDetailDao;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentCheckDetail;
import com.jeecms.content.service.ContentCheckDetailService;

/**
 * 内容智能审核失败详情service实现类
 * @author: chenming
 * @date:   2019年12月25日 下午2:36:49
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentCheckDetailServiceImpl extends BaseServiceImpl<ContentCheckDetail, ContentCheckDetailDao, Integer>
		implements ContentCheckDetailService,ContentListener {

	@Override
	public JSONObject getCheckBanContent(ContentCheckDetail detail,List<CmsModelItem> items) {
		if (detail == null) {
			return null;
		}
		if (StringUtils.isNotBlank(detail.getCheckBanContent())) {
			// 查询出当前内容中拥有的模型字段 名称集合
			List<String> fileds = items.stream().map(CmsModelItem::getField).collect(Collectors.toList());
			JSONObject json = JSONObject.parseObject(detail.getCheckBanContent());
			JSONObject returnJson = json;
			// 过滤掉不在模型字段集合中的字段
			for (String key : json.keySet()) {
				if (!fileds.contains(key)) {
					returnJson.remove(key);
				}
			}
			return returnJson;
		}
		return null;
	}
		

	@Override
	public List<ContentCheckDetail> findByContentIds(List<Integer> contentIds) {
		return dao.findByContentIds(contentIds);
	}

	@Override
	public List<ContentCheckDetail> findUnderReviews() {
		return dao.findUnderReviews();
	}

	@Override
	public ContentCheckDetail findByCheckMark(String checkMark,Integer status) {
		if (status != null) {
			return dao.findByCheckMarkAndHasDeletedAndStatus(checkMark, false, status);
		}
		return dao.findByCheckMarkAndHasDeleted(checkMark, false);
	}


	@Override
	public void afterDelete(List<Content> contents) throws GlobalException {
		if (!CollectionUtils.isEmpty(contents)) {
			List<Integer> contentIds = contents.stream().map(Content::getId).collect(Collectors.toList());
			List<ContentCheckDetail> details = dao.findByContentIds(contentIds);
			if (!CollectionUtils.isEmpty(details)) {
				super.physicalDeleteInBatch(details);
			}
		}
	}


	@Override
	public void afterSave(Content content) throws GlobalException {
		
	}


	@Override
	public Map<String, Object> preChange(Content content) {
		return null;
	}


	@Override
	public void afterChange(Content content, Map<String, Object> map) throws GlobalException {
		
	}


	@Override
	public void afterContentRecycle(List<Integer> contentIds) throws GlobalException {
		// 按照原型而言，审核中或者审核失败的内容全部都是调用删除(不加入回收站)的方法。所以此处设置该方法只是为了防止万一事件
		if (!CollectionUtils.isEmpty(contentIds)) {
			List<ContentCheckDetail> details = dao.findByContentIds(contentIds);
			if (!CollectionUtils.isEmpty(details)) {
				super.physicalDeleteInBatch(details);
			}
		}
	}

}