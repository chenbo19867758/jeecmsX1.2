/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.questionnaire.dao.SysQuestionnaireSubjectDao;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;
import com.jeecms.questionnaire.service.SysQuestionnaireSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysQuestionnaireSubjectServiceImpl extends BaseServiceImpl<SysQuestionnaireSubject, SysQuestionnaireSubjectDao, Integer> implements SysQuestionnaireSubjectService {


    @Override
    public List<SysQuestionnaireSubject> findByQuestionnaireId(Integer questionnaireId) {
        return dao.findByQuestionnaireId(questionnaireId);
    }
}