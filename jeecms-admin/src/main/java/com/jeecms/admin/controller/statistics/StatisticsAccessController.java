/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.statistics;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.statistics.domain.StatisticsAccess;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.service.StatisticsAccessService;

/**
 * 忠诚度分析
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-06-25
 */
@RequestMapping("/statisticsaccess")
@RestController
public class StatisticsAccessController extends BaseController<StatisticsAccess, Integer> {

	@Autowired
	private StatisticsAccessService statisticsAccessService;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}
	
	/**
	 * 实时访问页数控制器
	 * 
	 * @Title: pageInfo 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/timePage")
	public ResponseInfo timePage(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return statisticsAccessService.timePage(dto);
	}
	
	/**
	 * 实时访问深度控制器
	 * 
	 * @Title: pageInfo 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/timeHigh")
	public ResponseInfo timeHigh(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return statisticsAccessService.timeHigh(dto);
	}
	
	/**
	 * 实时访问时长控制器
	 * 
	 * @Title: pageInfo 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/time")
	public ResponseInfo time(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return statisticsAccessService.time(dto);
	}
	
	

	/**
	 * 访问页数控制器
	 * 
	 * @Title: pageInfo 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/pageInfo")
	public ResponseInfo pageInfo(@RequestBody @Valid StatisticsDto dto) throws Exception {
		dto.setType(StatisticsDto.PAGE_TYPE);
		return statisticsAccessService.pageInfo(dto);
	}
	
	/**
	 * 访问深度控制器
	 * 
	 * @Title: highInfo 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/highInfo")
	public ResponseInfo highInfo(@RequestBody @Valid StatisticsDto dto) throws Exception {
		dto.setType(StatisticsDto.HIGH_TYPE);
		return statisticsAccessService.highInfo(dto);
	}
	
	/**
	 * 访问时长控制器
	 * 
	 * @Title: timeInfo 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/timeInfo")
	public ResponseInfo timeInfo(@RequestBody @Valid StatisticsDto dto) throws Exception {
		dto.setType(StatisticsDto.TIME_TYPE);
		return statisticsAccessService.timeInfo(dto);
	}

}