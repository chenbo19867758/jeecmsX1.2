/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.questionnaire.dao.ext.SysQuestionnaireAnswerDaoExt;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;

/**
 * 投票问卷Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
public interface SysQuestionnaireAnswerDao extends IBaseDao<SysQuestionnaireAnswer, Integer>, SysQuestionnaireAnswerDaoExt {

}
