/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.dto.SysQuestionnaireSaveDto;
import com.jeecms.questionnaire.domain.dto.SysQuestionnaireUpdateDto;
import com.jeecms.questionnaire.domain.vo.QuestionnaireFrontListVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireVo;
import com.jeecms.system.domain.CmsSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 投票调查Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
public interface SysQuestionnaireService extends IBaseService<SysQuestionnaire, Integer> {

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
	 * 进行中和已结束列表
	 *
	 * @param siteId 站点id
	 * @param count  数量
	 * @return List
	 */
	List<QuestionnaireFrontListVo> list(Integer siteId, Integer orderBy, int count);

	/**
	 * 根据id和站点查询调查
	 *
	 * @param id     投票调查id
	 * @param siteId 站点id
	 * @return SysQuestionnaire
	 */
	SysQuestionnaire findByIdAndSiteId(Integer id, Integer siteId);

	/**
	 * 根据id查找投票调查数据
	 *
	 * @param id     投票调查id
	 * @param siteId 站点id
	 * @param type   true 修改状态后调用需要传true
	 * @return QuestionnaireVo
	 * @throws GlobalException 异常
	 */
	QuestionnaireVo findById(Integer id, Integer siteId, Boolean type) throws GlobalException;

	/**
	 * 复制投票问卷
	 *
	 * @param id    投票id
	 * @param title 标题
	 * @param site  站点
	 * @return SysQuestionnaire
	 * @throws GlobalException 异常
	 */
	SysQuestionnaire copy(Integer id, String title, CmsSite site) throws GlobalException;

	/**
	 * 新增问卷
	 *
	 * @param dto  问卷dto
	 * @param site 站点
	 * @return SysQuestionnaire
	 * @throws GlobalException 异常
	 */
	SysQuestionnaire save(SysQuestionnaireSaveDto dto, CmsSite site) throws GlobalException;

	/**
	 * 修改问卷
	 *
	 * @param dto  修改问卷Dto
	 * @param site 站点id
	 * @return SysQuestionnaire
	 * @throws GlobalException 异常
	 */
	SysQuestionnaire update(SysQuestionnaireUpdateDto dto, CmsSite site) throws GlobalException;

	/**
	 * 修改投票调查状态
	 *
	 * @param ids    投票调查id数组
	 * @param status 状态 0 暂停 1 提交审核 2 驳回 3 发布 4 时间到
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<SysQuestionnaire> updateStatus(Integer[] ids, Integer status, Integer siteId) throws GlobalException;

	/**
	 * 发布
	 *
	 * @param id 问卷id
	 * @throws GlobalException 异常
	 */
	void publish(Integer id) throws GlobalException;

	/**
	 * 审核
	 *
	 * @param ids          投票调查id数组
	 * @param reviewStatus true 通过 false 失败
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<SysQuestionnaire> review(Integer[] ids, boolean reviewStatus) throws GlobalException;

	/**
	 * 清空问卷的答案
	 *
	 * @param id     问卷id
	 * @param siteId 站点id
	 * @throws GlobalException 全局异常
	 */
	void cleanAnswer(Integer id, Integer siteId) throws GlobalException;

	/**
	 * 开启自动结束定时任务
	 *
	 * @param date            时间
	 * @param questionnaireId 问卷id
	 */
	void startUpEndJob(Date date, Integer questionnaireId);

	/**
	 * 修改工作流需要重置流转中的任务
	 *
	 * @param workflowId 工作流id
	 * @param siteId     站点id
	 * @throws GlobalException 异常
	 */
	void updateWorkFlow(Integer workflowId, Integer siteId) throws GlobalException;

	/**
	 * 删除问卷
	 *
	 * @param ids 问卷id集合
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<SysQuestionnaire> deleteBatch(Integer[] ids) throws GlobalException;

	/**
	 * 发布
	 * @param id 问卷id
	 */
	void publishJob(Integer id);

	/**
	 * 下线
	 * @param id 问卷id
	 */
	void offlineJob(Integer id);
}
