/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao.ext;

import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.vo.QuestionnaireFrontListVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 投票调查扩展Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/23 10:15
 */
public interface SysQuestionnaireDaoExt {

	/**
	 * 分页
	 *
	 * @param title     标题
	 * @param status    状态
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param siteId    站点id
	 * @param pageable  分页组件
	 * @return Page
	 */
	Page<SysQuestionnaire> page(String title, Integer status, Date beginTime, Date endTime, Integer siteId, Pageable pageable);

	/**
	 * 进行中和已结束分页
	 *
	 * @param siteId   站点id
	 * @param pageable 分页组件
	 * @return Page
	 */
	Page<QuestionnaireFrontListVo> page(Integer siteId, Integer orderBy, Pageable pageable);

	/**
	 * 进行中和已结束分页
	 *
	 * @param siteId 站点id
	 * @param count  数量
	 * @return List
	 */
	List<QuestionnaireFrontListVo> list(Integer siteId, Integer orderBy, int count);

	/**
	 * 根据工作流id获取列表
	 *
	 * @param workfolwId 工作流id
	 * @param siteId     站点id
	 * @return List
	 */
	List<SysQuestionnaire> findByWorkflowId(Integer workfolwId, Integer siteId);
}
