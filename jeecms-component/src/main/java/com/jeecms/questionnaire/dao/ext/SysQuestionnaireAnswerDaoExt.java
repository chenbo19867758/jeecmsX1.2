/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao.ext;

import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 问卷结果扩展Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/30 14:21
 */
public interface SysQuestionnaireAnswerDaoExt {

	/**
	 * 获取列表信息
	 *
	 * @param isEffective     是否有效
	 * @param province        省
	 * @param city            城市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              ip
	 * @param subjectId       题目id
	 * @param replayName      用户名
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param siteId          站点
	 * @param questionnaireId 问卷id
	 * @param pageable        分页组件
	 * @return Page
	 */
	Page<SysQuestionnaireAnswer> getPage(Boolean isEffective, String province, String city,
										 String device, Integer deviceType, String ip, Integer subjectId,
										 String replayName, Date beginTime, Date endTime,
										 Integer siteId, Integer questionnaireId, Pageable pageable);

	/**
	 * 获取列表信息
	 *
	 * @param isEffective     是否有效
	 * @param province        省
	 * @param city            城市
	 * @param device          设备
	 * @param deviceType      设备类型
	 * @param ip              ip
	 * @param subjectId       题目id
	 * @param replayName      用户名
	 * @param beginTime       开始时间
	 * @param endTime         结束时间
	 * @param orderBy         排序
	 * @param siteId          站点id
	 * @param questionnaireId 问卷id
	 * @return Page
	 */
	List<SysQuestionnaireAnswer> getList(Boolean isEffective, String province, String city, String device,
										 Integer deviceType, String ip, Integer subjectId,
										 String replayName, Date beginTime, Date endTime,
										 Integer orderBy, Integer siteId, Integer questionnaireId);

	/**
	 * 获取列表信息
	 *
	 * @param devices 设备
	 * @param ips     ip
	 * @param dates   结束时间
	 * @return Page
	 */
	List<SysQuestionnaireAnswer> find(List<Date> dates, List<String> ips, List<String> devices);

	/**
	 * 获取列表信息
	 *
	 * @param subjectId       题目id
	 * @param siteId          站点id
	 * @param questionnaireId 问卷id
	 * @return Page
	 */
	List<SysQuestionnaireAnswer> getList(Integer subjectId, Integer siteId, Integer questionnaireId);

	/**
	 * 根据问卷id查找结果
	 *
	 * @param siteId          站点id
	 * @param showFile        是否显示文件
	 * @param questionnaireId 问卷id
	 * @return List
	 */
	List<SysQuestionnaireAnswer> findByQuestionnaireId(Integer questionnaireId, Boolean showFile, Integer siteId);

	/**
	 * 根据问卷id和用户id获取数据
	 *
	 * @param replayName     用户id
	 * @param createTime     参与时间
	 * @param questIntegerId 问卷id
	 * @return List
	 */
	List<SysQuestionnaireAnswer> findByReplayNameAndQuestionnaireId(String replayName, Date createTime, Integer questIntegerId);

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
