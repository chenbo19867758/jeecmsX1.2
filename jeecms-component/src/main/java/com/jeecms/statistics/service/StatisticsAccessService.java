/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.statistics.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.statistics.domain.StatisticsAccess;
import com.jeecms.statistics.domain.dto.StatisticsDto;

/**
 * 忠诚度Service
* @author ljw
* @version 1.0
* @date 2019-06-25
*/
public interface StatisticsAccessService extends IBaseService<StatisticsAccess, Integer> {

	/**
	 * 统计忠诚度分析信息
	 * @Title: countAnalyze  
	 * @throws Exception 异常 
	 * @return: void
	 */
	void countAnalyze() throws Exception;
	
	/**
	 * 实时访问页数
	* @Title: timePage 
	* @param dto 传输
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	ResponseInfo timePage(StatisticsDto dto) throws Exception;
	
	/**
	 * 实时访问深度
	* @Title: timeHigh 
	* @param dto 传输
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	ResponseInfo timeHigh(StatisticsDto dto) throws Exception;
	
	/**
	 * 实时访问时长
	* @Title: time 
	* @param dto 传输
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	ResponseInfo time(StatisticsDto dto) throws Exception;
	
	/**
	 * 访问页数
	* @Title: pageInfo 
	* @param dto 传输
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	ResponseInfo pageInfo(StatisticsDto dto) throws Exception;
	
	/**
	 * 访问深度
	* @Title: highInfo 
	* @param dto 传输
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	ResponseInfo highInfo(StatisticsDto dto) throws Exception;
	
	/**
	 * 访问时长
	* @Title: timeInfo 
	* @param dto 传输
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	ResponseInfo timeInfo(StatisticsDto dto) throws Exception;
}
