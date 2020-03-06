/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.statistics;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.statistics.domain.StatisticsFlow;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.vo.AccessDeviceVo;
import com.jeecms.statistics.service.StatisticsFlowService;

/**
 * 网络设备控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-06-25
 */
@RequestMapping("/statisticsdevice")
@RestController
public class StatisticsDeviceController extends BaseController<StatisticsFlow, Integer> {

	@Autowired
	private StatisticsFlowService statisticsFlowService;

	/**
	 * 实时设备
	 * 
	 * @Title: area 
	 * @param siteId 站点ID
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/timedevice")
	@SerializeField(clazz = AccessDeviceVo.class, excludes = { "name", "deviceList"})
	public ResponseInfo timedevice(Integer siteId) throws Exception {
		return new ResponseInfo(statisticsFlowService.timeDevice(siteId));
	}
	
	/**
	 * 实时设备列表
	 * 
	 * @Title: area 
	 * @param siteId 站点ID
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/time")
	@SerializeField(clazz = AccessDeviceVo.class, includes = { "name", "pv", "uv", "ips", "jump", "visitTime",
			"deviceList", "visitTimes"})
	public ResponseInfo time(Integer siteId, Integer orderType, Boolean order) throws Exception {
		return new ResponseInfo(statisticsFlowService.timeDeviceList(siteId,orderType,order));
	}
	
	/**
	 * 设备信息
	 * 
	 * @Title: area 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/device")
	@SerializeField(clazz = AccessDeviceVo.class, excludes = { "name", "deviceList"})
	public ResponseInfo device(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return new ResponseInfo(statisticsFlowService.device(dto));
	}
	
	/**
	 *设备信息列表
	 * 
	 * @Title: area 
	 * @param dto 请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException 异常
	 */
	@PostMapping(value = "/devices")
	@SerializeField(clazz = AccessDeviceVo.class, includes = { "name", "pv", "uv", "ips", "jump", "visitTime",
		"deviceList"})
	public ResponseInfo devices(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return new ResponseInfo(statisticsFlowService.deviceList(dto));
	}
	
}