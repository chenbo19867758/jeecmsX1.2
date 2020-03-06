/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
public interface SysQuestionnaireSubjectDao extends IBaseDao<SysQuestionnaireSubject, Integer> {

    /**
     * 根据投票调查id获取题目
     *
     * @param questionnaireId 投票调查id
     * @return List
     */
    @Query("select bean from SysQuestionnaireSubject bean where bean.questionnaireId = ?1 and bean.hasDeleted = false order by bean.index")
    List<SysQuestionnaireSubject> findByQuestionnaireId(Integer questionnaireId);
}
