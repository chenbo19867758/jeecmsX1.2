/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.dao.ContentTypeDao;
import com.jeecms.system.domain.ContentType;
import com.jeecms.system.service.ContentTypeService;

/**
 * 内容类型管理service实现
 *
 * @version 1.0
 * @author: wulongwei
 * @date: 2019年5月5日 上午10:07:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentTypeServiceImpl extends BaseServiceImpl<ContentType, ContentTypeDao, Integer> implements ContentTypeService {


	@Override
	public ResponseInfo saveContentTypeInfo(ContentType contentType)
			throws GlobalException {
		this.checkContentTypeInfo(contentType);
		if (contentType.getLogoId() != null) {
			contentType.setLogoResource(resDataService.findById(contentType.getLogoId()));
		}
		super.save(contentType);
		return new ResponseInfo();
	}


	@Override
	public ResponseInfo updateContentTypeInfo(ContentType contentType)
			throws GlobalException {
		this.checkContentTypeInfo(contentType);
		if (contentType.getLogoId() != null) {
			contentType.setLogoResource(resDataService.findById(contentType.getLogoId()));
		}
		super.updateAll(contentType);
		return new ResponseInfo();
	}

	/**
	 * 校验内容类型信息：<br>
	 * typeName不能重复
	 *
	 * @param contentType
	 * @throws GlobalException
	 * @Title: checkContentTypeInfo
	 * @return: void
	 */
	public void checkContentTypeInfo(ContentType contentType) throws GlobalException {
		ContentType contentTypeInfo = dao.findByTypeNameAndHasDeleted(contentType.getTypeName(), false);
		if (contentTypeInfo != null && (contentType.getId() != null ? !contentType.getId().equals(contentTypeInfo.getId()) : true)) {
			throw new GlobalException(new SystemExceptionInfo(SettingErrorCodeEnum.TYPE_NAME_ALREADY_EXIST.getDefaultMessage(),
					SettingErrorCodeEnum.TYPE_NAME_ALREADY_EXIST.getCode()));
		}
	}

	@Override
	public ContentType findByTypeName(String typeName) throws GlobalException {
		return dao.findByTypeNameAndHasDeleted(typeName, false);
	}

	@Autowired
	private ResourcesSpaceDataService resDataService;
}