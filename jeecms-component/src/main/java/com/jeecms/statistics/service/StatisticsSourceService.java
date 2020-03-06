/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.StatisticsSource;
import com.jeecms.statistics.domain.vo.StatisticsSourceListVo;
import com.jeecms.statistics.domain.vo.StatisticsSourceVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 来源统计Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-26
 */
public interface StatisticsSourceService extends IBaseService<StatisticsSource, Integer> {

	/**
	 * 外部链接 域名
	 */
	int EXTERNAL_LINK_DOMAIN = 1;
	/**
	 * 外部链接 地址
	 */
	int EXTERNAL_LINK_URL = 2;

	/**
	 * 获取昨天的历史记录
	 * @param siteId
	 * @return
	 */
	List<StatisticsSource> getList(Integer siteId);

	/**
	 * 统计昨天的数据
	 *
	 * @throws GlobalException 异常
	 */
	void statisticsYesterday() throws GlobalException;

	/**
	 * 来源类型统计
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 1 pc 2移动端
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型（1pv 2uv 3ip）
	 * @return StatisticsSourceVo
	 * @throws IllegalAccessException
	 */
	StatisticsSourceVo sourceType(Date beginTime, Date endTime, Integer siteId,
								  Boolean newVisitor, Short accessSourceClient,
								  int sortType, Integer showType) throws IllegalAccessException;

	/**
	 * 来源网站统计
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 1 pc 2移动端
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型（1pv 2uv 3ip）
	 * @return StatisticsSourceVo
	 * @throws IllegalAccessException
	 */
	StatisticsSourceVo getBySourceUrl(Date beginTime, Date endTime, Integer siteId,
									  Boolean newVisitor, Short accessSourceClient,
									  int sortType, Integer showType) throws IllegalAccessException;

	/**
	 * 来源网站统计(列表)
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 1 pc 2移动端
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param pageable           分页组件
	 * @return Page<StatisticsSourceListVo>
	 * @throws IllegalAccessException
	 */
	Page<StatisticsSourceListVo> getBySourceUrl(Date beginTime, Date endTime, Integer siteId,
												Boolean newVisitor, Short accessSourceClient,
												int sortType, Pageable pageable) throws IllegalAccessException;

	/**
	 * 搜索引擎统计
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 1 pc 2移动端
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型（1pv 2uv 3ip）
	 * @return StatisticsSourceVo
	 * @throws IllegalAccessException
	 */
	StatisticsSourceVo searchEngine(Date beginTime, Date endTime, Integer siteId,
									Boolean newVisitor, Short accessSourceClient,
									int sortType, Integer showType) throws IllegalAccessException;

	/**
	 * 外部链接
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 1 pc 2移动端
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param type               1域名 2URL
	 * @param showType           显示类型（1pv 2uv 3ip）
	 * @return StatisticsSourceVo
	 * @throws IllegalAccessException
	 */
	StatisticsSourceVo externalLink(Date beginTime, Date endTime, Integer siteId,
									Boolean newVisitor, Short accessSourceClient,
									Integer sortType, int type,
									Integer showType) throws IllegalAccessException;

	/**
	 * 外部链接（列表）
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 1 pc 2移动端
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param type               1域名 2URL
	 * @param pageable           分页
	 * @return Page<StatisticsSourceListVo>
	 * @throws IllegalAccessException
	 */
	Page<StatisticsSourceListVo> externalLinkPage(Date beginTime, Date endTime, Integer siteId,
												  Boolean newVisitor, Short accessSourceClient,
												  int sortType, String url, int type,
												  Pageable pageable) throws IllegalAccessException;

	/**
	 * 获取uv数
	 * @param list list
	 * @return int
	 */
	int getUv(List<StatisticsSource> list);

	/**
	 * 获取ip数
	 * @param list list
	 * @return int
	 */
	int getIp(List<StatisticsSource> list);
}
