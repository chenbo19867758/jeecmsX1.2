/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.domain.StatisticsFlow;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.vo.AccessAreaVo;
import com.jeecms.statistics.domain.vo.AccessUserVo;
import com.jeecms.statistics.service.StatisticsFlowService;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.SysAccessRecordService;

/**
 * 地域分布控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2019-06-25
 */
@RequestMapping("/statisticsvisit")
@RestController
public class StatisticsVisitorController extends BaseController<StatisticsFlow, Integer> {

	@Autowired
	private StatisticsFlowService statisticsFlowService;
	@Autowired
	private SysAccessRecordService sysAccessRecordService;

	/**
	 * 实时地域分布列表
	 * 
	 * @Title: area
	 * @param dto
	 *            请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@PostMapping(value = "/timearea")
	@SerializeField(clazz = AccessAreaVo.class, excludes = { "name", "cityList" })
	public ResponseInfo timearea(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return new ResponseInfo(statisticsFlowService.timeArea(dto));
	}

	/**
	 * 实时地域分布列表分页
	 * 
	 * @Title: area
	 * @param dto
	 *            请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@PostMapping(value = "/timepage")
	@SerializeField(clazz = AccessAreaVo.class, includes = { "name", "pv", "uv", "ips", "jump", "visitTime",
			"cityList" })
	public ResponseInfo timepage(@RequestBody @Valid StatisticsDto dto, Pageable pageable) throws Exception {
		pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
		return new ResponseInfo(statisticsFlowService.timeArea(dto, pageable));
	}

	/**
	 * 地域分布列表
	 * 
	 * @Title: area
	 * @param dto
	 *            请求
	 * @return ResponseInfo 响应
	 * @throws Exception
	 *             异常
	 */
	@PostMapping(value = "/area")
	@SerializeField(clazz = AccessAreaVo.class, excludes = { "name", "cityList" })
	public ResponseInfo area(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return new ResponseInfo(statisticsFlowService.area(dto));
	}

	/**
	 * 地域分布列表分页
	 * 
	 * @Title: area
	 * @param dto
	 *            请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@PostMapping(value = "/areapage")
	@SerializeField(clazz = AccessAreaVo.class, includes = { "name", "pv", "uv", "ips", "jump", "visitTime",
			"cityList" })
	public ResponseInfo areapage(@RequestBody @Valid StatisticsDto dto, Pageable pageable) throws Exception {
		pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
		return new ResponseInfo(statisticsFlowService.timePage(dto, pageable));
	}

	/**
	 * 实时新老用户
	 * 
	 * @Title: area
	 * @param dto
	 *            请求
	 * @return ResponseInfo 响应
	 * @throws Exception
	 *             异常
	 */
	@PostMapping(value = "/timeuser")
	@SerializeField(clazz = AccessAreaVo.class, excludes = { "name", "cityList" })
	public ResponseInfo timeuser(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return new ResponseInfo(statisticsFlowService.timeUsers(dto));
	}

	/**
	 * 新老用户
	 * 
	 * @Title: area
	 * @param dto
	 *            请求
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@PostMapping(value = "/user")
	@SerializeField(clazz = AccessAreaVo.class, excludes = { "name", "cityList" })
	public ResponseInfo user(@RequestBody @Valid StatisticsDto dto) throws Exception {
		return new ResponseInfo(statisticsFlowService.users(dto));
	}

	/**
	 * 网站概述 新老用户
	 * 
	 * @Title: summarize
	 * @param siteId
	 *            站点ID
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@GetMapping(value = "/summarize")
	@SerializeField(clazz = AccessUserVo.class, excludes = { "visitPage", "ips" })
	public ResponseInfo summarize(Integer siteId, Long startTime, Long endTime) throws Exception {
		List<AccessUserVo> vos = new ArrayList<AccessUserVo>(10);
		Date start = new Date(startTime);
		Date end = new Date(endTime);
		// 查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService.haveList(MyDateUtils.getStartDate(start),
				MyDateUtils.getFinallyDate(end), siteId, null, null, null);
		// 新老客户数据
		Map<Boolean, List<SysAccessRecord>> deviceType = records.stream().filter(c -> c.getNewVisitor() != null)
				.collect(Collectors.groupingBy(SysAccessRecord::getNewVisitor));
		for (Boolean key : deviceType.keySet()) {
			List<SysAccessRecord> sysRecords = deviceType.get(key);
			AccessUserVo vo = new AccessUserVo();
			if (key) {
				vo.setName("新客户");
				vo.setIsNew(true);
			} else {
				vo.setName("老客户");
				vo.setIsNew(false);
			}
			// pv数
			Integer pv = sysRecords.size();
			vo.setPv(pv);
			// 总的访客数
			Integer uv = statisticsFlowService.uvNumber(sysRecords);
			vo.setUv(uv);
			List<BigDecimal> jumpAndTime = jumpAndTime(sysRecords);
			vo.setJump(jumpAndTime.get(0));
			vo.setVisitTime(jumpAndTime.get(1));
			vo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
			vos.add(vo);
		}
		// 填充数据
		if (vos.size() == 1) {
			AccessUserVo vo = vos.get(0);
			if (vo.getName().equals("新客户")) {
				AccessUserVo user = new AccessUserVo();
				user.setJump(BigDecimal.ZERO);
				user.setVisitTime(BigDecimal.ZERO);
				user.setVisitTimes("00:00:00");
				user.setUv(0);
				user.setPv(0);
				user.setName("老客户");
				user.setIsNew(false);
				vos.add(user);
			} else {
				AccessUserVo user = new AccessUserVo();
				user.setJump(BigDecimal.ZERO);
				user.setVisitTime(BigDecimal.ZERO);
				user.setVisitTimes("00:00:00");
				user.setUv(0);
				user.setPv(0);
				user.setName("新客户");
				user.setIsNew(true);
				vos.add(user);
			}
		}
		return new ResponseInfo(vos);
	}

	/**
	 * 计算跳出率和平均访问时长
	 * 
	 * @Title: jumpAndTime
	 * @param records
	 *            记录列表
	 * @throws IllegalAccessException
	 *             转换异常
	 */
	public List<BigDecimal> jumpAndTime(List<SysAccessRecord> records) throws IllegalAccessException {
		// 过滤session为空
		records = records.stream().filter(a -> a.getSessionId() != null).collect(Collectors.toList());
		LongAdder adder = new LongAdder();
		LongAdder timer = new LongAdder();
		Map<String, List<SysAccessRecord>> map = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		for (List<SysAccessRecord> list : map.values()) {
			// 只出现一次的Session
			if (list.size() == 1) {
				adder.increment();
			} else {
				// 统计总时长
				// 按照创建时间排序
				list = list.stream().sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
						.collect(Collectors.toList());
				Long sum = list.get(list.size() - 1).getCreateTime().getTime() 
						- list.get(0).getCreateTime().getTime();
				sum = Math.abs(sum) / 1000;
				timer.add(sum);
			}
		}
		BigDecimal jump = MathUtil.div(new BigDecimal(adder.sumThenReset()), new BigDecimal(records.size()),
				MathUtil.SCALE_LEN_COMMON);
		// 平均访问时长计算：总的访问时间/ 次数
		BigDecimal visitTime = MathUtil.div(new BigDecimal(timer.sumThenReset()), new BigDecimal(map.size()),
				MathUtil.SCALE_LEN_COMMON);
		return Arrays.asList(jump, visitTime);
	}

	/**
	 * 网站概述 地域分布
	 * 
	 * @Title: summarize
	 * @param siteId
	 *            站点ID
	 * @return ResponseInfo 响应
	 * @throws GlobalException
	 *             异常
	 */
	@GetMapping(value = "/summarizeArea")
	public ResponseInfo summarizeArea(Integer siteId, Long startTime, Long endTime) throws Exception {
		List<JSONObject> vos = new ArrayList<JSONObject>(10);
		Date start = new Date(startTime);
		Date end = new Date(endTime);
		// 查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService.haveList(MyDateUtils.getStartDate(start),
				MyDateUtils.getFinallyDate(end), siteId, null, null, null);
		// 过滤省份为空
		records = records.stream().filter(a -> StringUtils.isNotBlank(a.getAccessProvince()))
				.collect(Collectors.toList());
		// 省份数据
		Map<String, List<SysAccessRecord>> deviceType = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getAccessProvince));
		for (String key : deviceType.keySet()) {
			List<SysAccessRecord> sysRecords = deviceType.get(key);
			JSONObject vo = new JSONObject();
			vo.put("name", key);
			vo.put("value", sysRecords.size());
			vos.add(vo);
		}
		return new ResponseInfo(vos);
	}
}