/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.questionnaire.dao.ext.SysQuestionnaireDaoExt;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import org.springframework.data.jpa.repository.Query;

/**
 * 投票调查Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
public interface SysQuestionnaireDao extends IBaseDao<SysQuestionnaire, Integer>, SysQuestionnaireDaoExt {

    /**
     * 根据id和站点id获取投票调查
     *
     * @param id     投票调查id
     * @param siteId 站点id
     * @return SysQuestionnaire
     */
    @Query("select bean from SysQuestionnaire bean where bean.id = ?1 and bean.siteId = ?2 and bean.hasDeleted = false")
    SysQuestionnaire findByIdAndSiteId(Integer id, Integer siteId);
}
