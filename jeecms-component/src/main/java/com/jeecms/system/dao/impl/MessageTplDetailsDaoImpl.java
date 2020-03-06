package com.jeecms.system.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.ext.MessageTplDetailsExt;
import com.jeecms.system.domain.MessageTplDetails;
import com.jeecms.system.domain.querydsl.QMessageTpl;
import com.jeecms.system.domain.querydsl.QMessageTplDetails;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Repository
public class MessageTplDetailsDaoImpl extends BaseDao<MessageTplDetails> implements MessageTplDetailsExt {


	@Override
	public MessageTplDetails findByMesCodeAndType(String mesCode, Short mesType, Short detailMesType,
			boolean hasDeleted) throws GlobalException {
		QMessageTplDetails messageTplDetails = QMessageTplDetails.messageTplDetails;
		QMessageTpl messageTpl = QMessageTpl.messageTpl;
		BooleanExpression condition = messageTpl.hasDeleted.eq(hasDeleted);
		if (StringUtils.isNotBlank(mesCode)) {
			condition = condition.and(messageTpl.mesCode.eq(mesCode));
		}
//		if (mesType != null) {
//                        condition = condition.and(messageTpl.mesType.eq(mesType));
//                }
                if (detailMesType != null) {
                        condition = condition.and(messageTplDetails.mesType.eq(detailMesType));
                }
		
		return getJpaQueryFactory().select(messageTplDetails).from(messageTplDetails)
				.innerJoin(messageTplDetails.messageTpl, messageTpl).where(condition).fetchFirst();
	}

}
