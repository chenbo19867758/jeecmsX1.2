/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.content.dao.ContentRelationDao;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentRelation;
import com.jeecms.content.domain.dto.ContentRelationDto;
import com.jeecms.content.service.ContentRelationService;
import com.jeecms.content.service.ContentService;
import com.jeecms.system.domain.CmsDataPerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容"相关内容"service实现类
 *
 * @author: chenming
 * @date: 2019年6月21日 下午4:32:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentRelationServiceImpl extends BaseServiceImpl<ContentRelation, ContentRelationDao, Integer>
		implements ContentRelationService {

	@Override
	public void save(ContentRelationDto dto) throws GlobalException {
		List<Content> contents = contentService.findAllById(dto.getContentIds());
		Content sourceContent = contentService.findById(dto.getContentId());
		if (contents == null || contents.size() == 0 || sourceContent == null) {
			return;
		}
		// 校验权限
		List<Content> validContents = new ArrayList<Content>(contents);
		validContents.add(sourceContent);
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_EDIT, validContents, null)) {
			throw new GlobalException(new SystemExceptionInfo(
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode()));
		}
		if (dto.getContentIds().contains(dto.getContentId())) {
			throw new GlobalException(new SystemExceptionInfo(
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getDefaultMessage(),
					RPCErrorCodeEnum.INCOMING_ID_TYPE_IS_INCORRECT.getCode()));
		}
		List<ContentRelation> list = findByContentId(dto.getContentId());
		List<Integer> original = new ArrayList<Integer>(list.size());
		for (ContentRelation relation : list) {
			original.add(relation.getRelationContentId());
		}
		List<ContentRelation> contentRelations = new ArrayList<ContentRelation>();
		for (Content content : contents) {
			if (!original.contains(content.getId())) {
				ContentRelation contentRelation = new ContentRelation();
				contentRelation.setRelationContentId(content.getId());
				contentRelation.setContentId(dto.getContentId());
				contentRelation.setSortNum(10);
				contentRelation.setSortWeight(0);
				contentRelation.setContent(sourceContent);
				contentRelation.setRelationContent(content);
				contentRelations.add(contentRelation);
			}
		}
		if (contentRelations.size() > 0) {
			super.saveAll(contentRelations);
		}
	}

	@Override
	public void sort(ContentRelationDto dto) throws GlobalException {
		List<ContentRelation> contentRelations = super.findAllById(dto.getContentRelationIds());
		if (contentRelations == null || contentRelations.size() == 0) {
			return;
		}
		ContentRelation contentRelation = super.findById(dto.getContentRelationId());
		if (contentRelation == null) {
			return;
		}
		List<Integer> contentIds = contentRelations.stream().map(ContentRelation::getContentId)
				.collect(Collectors.toList());
		contentIds.add(contentRelation.getContentId());
		List<Content> contents = contentService.findAllById(contentIds);
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_EDIT, contents, null)) {
			throw new GlobalException(new SystemExceptionInfo(
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode()));
		}
		List<ContentRelation> newContentRelations = new ArrayList<ContentRelation>();
		// 得到排序值
		Integer sortNum = contentRelation.getSortNum();
		// 得到权重
		Integer sortWeight = contentRelation.getSortWeight();
		// 排序之前
		if (dto.getLocation()) {
			for (ContentRelation relation : contentRelations) {
				// 如果排序值为0，增加权重
				if (sortNum.equals(0)) {
					relation.setSortWeight(sortWeight + 1);
				} else {
					relation.setSortNum(sortNum - 1);
				}
				newContentRelations.add(relation);
			}
		} else {
			// 排序之后
			for (ContentRelation relation : contentRelations) {
				// 如果排序值为0，减少权重
				if (sortNum.equals(0)) {
					if (sortWeight.equals(0)) {
						// 排序，权重都为0，不做操作
						break;
					} else {
						relation.setSortWeight(sortWeight - 1);
					}
				} else {
					relation.setSortNum(sortNum + 1);
				}
				newContentRelations.add(relation);
			}
		}
		super.batchUpdate(newContentRelations);
	}

	@Override
	public List<ContentRelation> findByContentId(Integer contentId) {
		return dao.findByContentId(contentId);
	}

	@Autowired
	private ContentService contentService;


}