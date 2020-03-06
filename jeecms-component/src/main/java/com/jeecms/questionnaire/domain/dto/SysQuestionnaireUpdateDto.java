/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

import com.jeecms.questionnaire.domain.vo.BgConfigBean;
import com.jeecms.questionnaire.domain.vo.ContConfigBean;
import com.jeecms.questionnaire.domain.vo.FontConfigBean;
import com.jeecms.questionnaire.domain.vo.HeadConfigBean;
import com.jeecms.questionnaire.domain.vo.SubConfigBean;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/24 16:00
 */
public class SysQuestionnaireUpdateDto extends SysQuestionnaireSaveDto {

    private Integer id;

    @NotNull
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SysQuestionnaireUpdateDto() {
        super();
    }

    public SysQuestionnaireUpdateDto(String title, String details, int status, Integer coverPic, Boolean isVerification, Short processType, String prompt, Integer workflowId, Date beginTime, Date endTime, Boolean answerLimit, Integer userAnswerFrequencyLimit, Short userAnswerFrequencyLimitUnit, Integer ipAnswerFrequencyLimit, Short ipAnswerFrequencyLimitUnit, Integer deviceAnswerFrequencyLimit, Short deviceAnswerFrequencyLimitUnit, Boolean isOnlyWechat, Integer wechatAnswerFrequencyLimit, Short wechatAnswerFrequencyLimitUnit, Integer shareLogo, String description, List<QuestionnaireSubjectDto> subjects, BgConfigBean bgConfig, HeadConfigBean headConfig, ContConfigBean contConfig, FontConfigBean fontConfig, SubConfigBean subConfig, Integer id) {
        super(title, details, status, coverPic, isVerification, processType, prompt, workflowId, beginTime, endTime, answerLimit, userAnswerFrequencyLimit, userAnswerFrequencyLimitUnit, ipAnswerFrequencyLimit, ipAnswerFrequencyLimitUnit, deviceAnswerFrequencyLimit, deviceAnswerFrequencyLimitUnit, isOnlyWechat, wechatAnswerFrequencyLimit, wechatAnswerFrequencyLimitUnit, shareLogo, description, subjects, bgConfig, headConfig, contConfig, fontConfig, subConfig);
        this.id = id;
    }
}
