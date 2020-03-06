/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.service;

import com.alibaba.fastjson.JSONArray;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.vo.QuestionnaireAnswerViewVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireExportListVo;
import com.jeecms.questionnaire.domain.vo.QuestionnairePieVo;
import com.jeecms.questionnaire.domain.vo.QuestionnaireSubjectGroupVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 问卷结果Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
public interface SysQuestionnaireAnswerService extends IBaseService<SysQuestionnaireAnswer, Integer> {


	/**
	 * 获取列表信息
	 *
	 * @param isEffective     是否有效
	 * @param province        省
	 * @param city            城市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              ip
	 * @param replayName      用户名
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param siteId          站点id
	 * @param questionnaireId 问卷id
	 * @param pageable        分页组件
	 * @return Page
	 */
	Page<SysQuestionnaireAnswer> getPage(Boolean isEffective, String province, String city, Integer orderBy,
										 String device, Integer deviceType, String ip, String replayName, Date beginTime,
										 Date endTime, Integer siteId, Integer questionnaireId, Pageable pageable);

	/**
	 * 获取列表信息
	 *
	 * @param province        省
	 * @param city            城市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              ip
	 * @param replayName      用户名
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param subjectId       题目id
	 * @param siteId          站点id
	 * @param questionnaireId 问卷id
	 * @param options         排除的答案
	 * @param pageable        分页组件
	 * @return Page
	 */
	Page<SysQuestionnaireAnswer> getPageBySubject(String province, String city, String device, Integer deviceType, String ip,
												  String replayName, Date beginTime, Date endTime, Integer subjectId, Integer orderBy,
												  Integer siteId, Integer questionnaireId, String options, Pageable pageable);

	/**
	 * 根据id获取用户回答的问题
	 *
	 * @param id 回答Id
	 * @return List
	 */
	QuestionnaireAnswerViewVo getById(Integer id);

	/**
	 * 饼图-题目
	 *
	 * @param questionnaireId 问卷id
	 * @param subjectId       题目id
	 * @param siteId          站点id
	 * @return list
	 */
	List<QuestionnairePieVo> pieChart(Integer questionnaireId, Integer subjectId, Integer siteId);

	/**
	 * 根据题目分组
	 *
	 * @param questionnaireId 问卷id
	 * @param siteId          站点id
	 * @return List
	 */
	List<QuestionnaireSubjectGroupVo> groupBySubject(Integer questionnaireId, Integer siteId);

	/**
	 * 获取列表信息
	 *
	 * @param isEffective     是否有效
	 * @param province        省
	 * @param city            城市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param orderBy         排序
	 * @param ip              ip
	 * @param replayName      用户名
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param siteId          站点id
	 * @param questionnaireId 问卷id
	 * @return Page
	 */
	List<SysQuestionnaireAnswer> getList(Boolean isEffective, String province, String city, String device,
										 Integer deviceType, String ip, String replayName, Date beginTime,
										 Date endTime, Integer orderBy, Integer siteId, Integer questionnaireId);

	/**
	 * 获取用户为单位的导出列表数据
	 *
	 * @param isEffective     是否有效
	 * @param province        省
	 * @param city            城市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              ip
	 * @param replayName      用户名
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param siteId          站点id
	 * @param questionnaireId 问卷id
	 * @return List
	 */
	List<QuestionnaireExportListVo> getExportList(Boolean isEffective, String province, String city, String device,
												  Integer deviceType, String ip, String replayName, Date beginTime,
												  Date endTime, Integer orderBy,  Integer siteId,
												  Integer questionnaireId, Integer page, Integer size);

	/**
	 * 标记有效/无效
	 *
	 * @param ids    问卷结果id集合
	 * @param status true 有效
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<SysQuestionnaireAnswer> markStatus(Integer[] ids, Boolean status) throws GlobalException;

	/**
	 * 删除问卷结果
	 *
	 * @param ids ids
	 * @throws GlobalException 异常
	 */
	void deleteByIds(Integer[] ids) throws GlobalException;

	/**
	 * 设备饼图
	 *
	 * @param questionnaireId 问卷id
	 * @param siteId          站点id
	 * @return List
	 */
	List<QuestionnairePieVo> devicesPieChart(Integer questionnaireId, Integer siteId);

	/**
	 * 地图统计图
	 *
	 * @param questionnaireId 问卷id
	 * @param siteId          站点id
	 * @return list
	 */
	List<QuestionnairePieVo> statisticsMapChart(Integer questionnaireId, Integer siteId);

	/**
	 * 趋势分析
	 *
	 * @param questionnaireId 问卷id
	 * @param device          设备
	 * @param deviceType      设备类型 1pc 2移动
	 * @param province        省
	 * @param city            市
	 * @param showType        显示方式 1时2天3周4月
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param siteId          站点id
	 * @return JSONArray
	 */
	JSONArray statisticsAreaChart(Integer questionnaireId, String device, Integer deviceType, String province, String city,
								  int showType, Date beginTime, Date endTime, Integer siteId);

	/**
	 * 根据问卷id和站点查询问卷结果
	 *
	 * @param questionnaireId 问卷id
	 * @param showFile        true 显示文件， false 不显示文件
	 * @param siteId          站点id
	 * @return List
	 */
	List<SysQuestionnaireAnswer> findByQuestionnaireId(Integer questionnaireId, Boolean showFile, Integer siteId);

	/**
	 * 填写问卷
	 *
	 * @param questionnaireId 问卷id
	 * @param request         {@link HttpServletRequest}
	 * @param response        {@link HttpServletResponse}
	 * @throws GlobalException 异常
	 * @throws IOException     IO异常
	 */
	void save(Integer questionnaireId, HttpServletRequest request, HttpServletResponse response) throws GlobalException;

	/**
	 * 查找答案
	 *
	 * @param subjectId       题目id
	 * @param questionnaireId 问卷id
	 * @param pageable        分页组件
	 * @return page
	 */
	Page<SysQuestionnaireAnswer> findBySubjectId(Integer subjectId, Integer questionnaireId, Pageable pageable);
}
