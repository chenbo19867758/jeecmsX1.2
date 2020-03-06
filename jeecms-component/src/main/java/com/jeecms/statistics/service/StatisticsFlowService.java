/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.StatisticsFlow;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.dto.StatisticsFlowDto;
import com.jeecms.statistics.domain.dto.StatisticsFlowRealTimeItemDto;
import com.jeecms.statistics.domain.vo.AccessAreaVo;
import com.jeecms.statistics.domain.vo.AccessDeviceVo;
import com.jeecms.statistics.domain.vo.StatisitcsOverviewVos;
import com.jeecms.statistics.domain.vo.StatisticsFlowRealTimeItemVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowRealTimeVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowVo;
import com.jeecms.system.domain.SysAccessRecord;

/**
 * 趋势统计service接口
 * @author: chenming
 * @date:   2019年6月25日 下午2:11:53
 */
public interface StatisticsFlowService extends IBaseService<StatisticsFlow, Integer> {
	
	/**
	 * 获取趋势列表
	* @Title: flowList 
	* @param start 开始时间
	* @param end 结束时间
	* @param siteId 站点ID
	* @param sourceType 搜索来源
	* @param visit 是否新客户
	* @param province 省份
	* @return List 列表
	* @throws GlobalException 异常
	 */
	List<StatisticsFlow> flowList(Date start, Date end, Integer siteId, Integer sourceType, 
			Boolean visit, String province) throws GlobalException;
	
	/**
	 * 实时地域分布
	* @Title: timeArea 
	* @param dto 传输
	* @return AccessAreaVo 对象
	* @throws GlobalException 异常
	 */
	AccessAreaVo timeArea(StatisticsDto dto) throws Exception;
	
	/**
	 * 实时地域分布列表分页
	* @Title: timeArea 
	* @param dto 传输
	* @param pageable 分页
	* @return JSON 数据
	* @throws GlobalException 异常
	 */
	JSONObject timeArea(StatisticsDto dto, Pageable pageable) throws Exception;
	
	/**
	 * 地域分布
	* @Title: timeArea 
	* @param dto 传输
	* @return AccessAreaVo 对象
	* @throws GlobalException 异常
	 */
	AccessAreaVo area(StatisticsDto dto) throws Exception;
	
	/**
	 *  地域分布列表分页
	* @Title: timePage 
	* @param dto 传输
	* @param pageable 分页
	* @return JSON 数据
	* @throws GlobalException 异常
	 */
	JSONObject timePage(StatisticsDto dto, Pageable pageable) throws Exception;
	
	/**
	 * 实时网络设备
	* @Title: timeArea 
	* @param siteID 站点ID
	* @return AccessDeviceVo 对象
	* @throws GlobalException 异常
	 */
	AccessDeviceVo timeDevice(Integer siteID) throws Exception;
	
	/**
	 * 实时网络设备列表
	* @Title: timeArea 
	* @param siteID 站点ID
	* @param orderType 排序类型
	* @param order 排序方式
	* @return JSONObject 对象
	* @throws GlobalException 异常
	 */
	JSONObject timeDeviceList(Integer siteID, Integer orderType, Boolean order) throws Exception;
	
	/**
	 * 网络设备信息
	* @Title: device 
	* @param dto 传输对象
	* @return AccessDeviceVo 对象
	* @throws GlobalException 异常
	 */
	AccessDeviceVo device(StatisticsDto dto) throws Exception;
	
	/**
	 * 网络设备信息
	* @Title: device 
	* @param dto 传输对象
	* @return AccessDeviceVo 对象
	* @throws GlobalException 异常
	 */
	JSONObject deviceList(StatisticsDto dto) throws Exception;
	
	/**
	 * 实时新老客户数据
	* @Title: users 
	* @param dto 传输对象
	* @return JSONObject 对象
	* @throws GlobalException 异常
	 */
	JSONObject timeUsers(StatisticsDto dto) throws Exception;
	
	/**
	 * 新老客户数据
	* @Title: users 
	* @param dto 传输对象
	* @return JSONObject 对象
	* @throws GlobalException 异常
	 */
	JSONObject users(StatisticsDto dto) throws Exception;
	
	/**
	 * 查询实时数据(图表)
	 * @Title: getRealTimeVo  
	 * @param siteId	站点id
	 * @return: StatisticsFlowRealTimeVo
	 */
	List<StatisticsFlowRealTimeVo> getRealTimeVo(Integer siteId);
	
	/**
	 * 计算实时数据的:在线访问人数
	 * @Title: getRealTimeUvNum  
	 * @param siteId	站点ID值
	 * @return: Integer
	 */
	Integer getRealTimeUvNum(Integer siteId);
	
	/**
	 * 查询实时数据(列表)
	 * @Title: getRealTimeItemVo  
	 * @param dto	接收dto
	 * @return: List
	 */
	List<StatisticsFlowRealTimeItemVo> getRealTimeItemVo(StatisticsFlowRealTimeItemDto dto);
	
	/**
	 * 趋势分析
	 * @Title: getFlow  
	 * @param dto	趋势分析接收Dto
	 * @return: StatisticsFlowVo	区分分析返回VO
	 */
	StatisticsFlowVo getFlow(StatisticsFlowDto dto);
	
	/**
	 * 网站概况
	 * @Title: getOverviewVos  
	 * @param siteId	站点id
	 * @return: StatisitcsOverviewVos	返回的网站概况vo对象
	 */
	StatisitcsOverviewVos getOverviewVos(Integer siteId);
	
	/**
	 * 新增
	 * @Title: save  
	 * @throws GlobalException      全局异常
	 * @return: void
	 */
	void save() throws GlobalException;
	
	/**
	 * 访客数
	 * @Title: uvNumber  
	 * @throws GlobalException      全局异常
	 */
	Integer uvNumber(List<SysAccessRecord> records) throws GlobalException;
}
