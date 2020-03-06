/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.content.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.dao.ext.CmsModelTplDaoExt;
import com.jeecms.content.domain.CmsModelTpl;
import com.jeecms.content.domain.querydsl.QCmsModelTpl;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;

import java.util.List;

/**
 * 模板扩展
 * @author: ljw
 * @date: 2019年4月24日 上午11:49:05
 */
public class CmsModelTplDaoImpl extends BaseDao<CmsModelTpl> implements CmsModelTplDaoExt {


	@Override
	public List<CmsModelTpl> models(Integer siteId, Integer modelId, String solution) throws GlobalException {
		QCmsModelTpl tpl = QCmsModelTpl.cmsModelTpl;
		BooleanBuilder builder = new BooleanBuilder();
		if (siteId != null) {
			builder.and(tpl.siteId.eq(siteId));
		}
		if (modelId != null) {
			builder.and(tpl.modelId.eq(modelId));
		}
		if (StringUtils.isNotBlank(solution)) {
			builder.and(tpl.tplSolution.eq(solution));
		}
		return getJpaQueryFactory().select(tpl)
				.setHint(QueryHints.HINT_CACHEABLE, true)
				.from(tpl).where(builder).fetch();
	}

	@Override
	public List<CmsModelTpl> getList(Integer siteId, Integer modelId, Short tplType) {
		QCmsModelTpl tpl = QCmsModelTpl.cmsModelTpl;
		BooleanBuilder builder = new BooleanBuilder();
		if (siteId != null) {
			builder.and(tpl.siteId.eq(siteId));
		}
		if (modelId != null) {
			builder.and(tpl.modelId.eq(modelId));
		}
		if (tplType != null) {
			builder.and(tpl.tplType.eq(tplType));
		}
		return getJpaQueryFactory().select(tpl)
				.setHint(QueryHints.HINT_CACHEABLE, true)
				.from(tpl).where(builder).fetch();
	}

}
