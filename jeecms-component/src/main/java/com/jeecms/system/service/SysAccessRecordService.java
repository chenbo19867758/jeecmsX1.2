/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.dto.StatisticsFlowDto;
import com.jeecms.statistics.domain.dto.StatisticsFlowRealTimeItemDto;
import com.jeecms.system.domain.SysAccessRecord;
import net.sf.ehcache.Ehcache;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 浏览记录Service
 *
 * @author ljw
 * @version 1.0
 * @date 2019-06-22
 */
public interface SysAccessRecordService extends IBaseService<SysAccessRecord, Integer> {

	/**
	 * 添加记录
	 *
	 * @param request 请求
	 * @throws GlobalException 异常
	 * @Title: recordInfo
	 */
	void recordInfo(HttpServletRequest request) throws GlobalException;

	/**
	 * 统计来源
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         是否新客户
	 * @param accessSourceClient 访问来源(1:PC  2:移动端H5  3:微信客户端H5 4:IOS 5:安卓 6:小程序)
	 * @param sourceType         来源类型
	 * @return list
	 */
	List<SysAccessRecord> getSource(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
									Short accessSourceClient, Integer sourceType);

	/**
	 * 受访分析
	 *
	 * @param beginTime    开始时间
	 * @param endTime      结束时间
	 * @param siteId       站点id
	 * @param newVisitor   是否新客户
	 * @param sorceUrlType
	 * @return list
	 */
	List<SysAccessRecord> getAccessPage(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
										Integer sorceUrlType);

	/**
	 * 站点统计缓存入库
	 *
	 * @param cache 站点统计缓存
	 */
	void freshSiteAttrCacheToDB(Ehcache cache);

	/**
	 * 统计cookie次数
	 *
	 * @param cookie cookie值
	 * @Title: recordInfo
	 */
	Long countCookie(String cookie);

	/**
	 * 统计userId次数
	 *
	 * @param userId 用户ID
	 * @Title: countUserId
	 */
	Long countUserId(Integer userId);

	/**
	 * 获取某天访问ip数
	 *
	 * @param date   时间
	 * @param siteId 站点id
	 * @return long
	 */
	long countIp(Date date, Integer siteId);

	/**
	 * 记录列表
	 *
	 * @param start      开始时间
	 * @param end        结束时间
	 * @param siteId     站点ID
	 * @param sourceType 来源类型
	 * @param province   省份
	 * @param visitor    是否新客户
	 * @return List
	 * @throws GlobalException 异常
	 * @Title: haveList
	 */
	List<SysAccessRecord> haveList(Date start, Date end, Integer siteId, Integer sourceType,
								   String province, Boolean visitor) throws GlobalException;

	/**
	 * 根据时间+站点查询出list集合
	 *
	 * @param startTime 起始时间
	 * @param endTime   截止时间
	 * @param siteId    站点
	 * @Title: findByDate
	 * @return: List
	 */
	List<SysAccessRecord> findByDate(Date startTime, Date endTime, Integer siteId);

	/**
	 * 获取某天的访问记录数据
	 *
	 * @param date   时间
	 * @param siteId 站点id
	 * @return long
	 */
	long getContentByDate(Date date, Integer siteId);

	/**
	 * 根据条件查询出实时数据列表
	 *
	 * @param dto       条件dto
	 * @param startTime 起始时间
	 * @param endTime   截止时间
	 * @Title: getRealTimeItem
	 * @return: List
	 */
	List<SysAccessRecord> getRealTimeItem(StatisticsFlowRealTimeItemDto dto, Date startTime, Date endTime);

	List<SysAccessRecord> getFlow(StatisticsFlowDto dto);

	/**
	 * 根据sessionid和站点获取访问记录
	 *
	 * @param sessionId sessionId
	 * @param siteId    站点id
	 * @return SysAccessRecord
	 */
	SysAccessRecord findBySessionId(String sessionId, Integer siteId);

	/**
	 * 根据ip和站点获取访问记录
	 *
	 * @param ip     ip
	 * @param siteId 站点id
	 * @param date   时间
	 * @return SysAccessRecord
	 */
	SysAccessRecord findByIp(String ip, Integer siteId, Date date);

}
