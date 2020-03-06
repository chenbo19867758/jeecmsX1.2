/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.questionnaire.domain.SysQuestionnaireSubject;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
public interface SysQuestionnaireSubjectService extends IBaseService<SysQuestionnaireSubject, Integer> {

    /**
     * 根据投票调查获取对应题目
     *
     * @param questionnaireId 投票调查id
     * @return List
     */
    List<SysQuestionnaireSubject> findByQuestionnaireId(Integer questionnaireId);

}
