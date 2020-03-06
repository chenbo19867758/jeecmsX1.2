/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service.impl;

import static com.jeecms.statistics.domain.dto.StatisticsDto.ORDER_TYPE_IP;
import static com.jeecms.statistics.domain.dto.StatisticsDto.ORDER_TYPE_JUMP;
import static com.jeecms.statistics.domain.dto.StatisticsDto.ORDER_TYPE_PV;
import static com.jeecms.statistics.domain.dto.StatisticsDto.ORDER_TYPE_TIME;
import static com.jeecms.statistics.domain.dto.StatisticsDto.ORDER_TYPE_UV;
import static com.jeecms.statistics.domain.dto.StatisticsDto.ORDER_TYPE_PAGE;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.constants.StatisticsConstant;

import com.jeecms.statistics.dao.StatisticsFlowDao;
import com.jeecms.statistics.domain.StatisticsFlow;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.dto.StatisticsFlowDto;
import com.jeecms.statistics.domain.dto.StatisticsFlowRealTimeItemDto;
import com.jeecms.statistics.domain.vo.AccessAreaVo;
import com.jeecms.statistics.domain.vo.AccessDeviceVo;
import com.jeecms.statistics.domain.vo.AccessDeviceVo.DeviceVo;
import com.jeecms.statistics.domain.vo.AccessUserVo;
import com.jeecms.statistics.domain.vo.BaseAccessVo;
import com.jeecms.statistics.domain.vo.StatisitcsOverviewVo;
import com.jeecms.statistics.domain.vo.StatisitcsOverviewVos;
import com.jeecms.statistics.domain.vo.StatisticsFlowImageVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowListVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowRealTimeItemVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowRealTimeVo;
import com.jeecms.statistics.domain.vo.StatisticsFlowVo;
import com.jeecms.statistics.domain.vo.StatisticsVisitorVo;
import com.jeecms.statistics.service.StatisticsFlowService;
import com.jeecms.statistics.service.TimeCuttingService;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.SysAccessRecordService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;




/**
 * 趋势分析service实体类
 *
 * @author: chenming
 * @date: 2019年6月25日 下午2:12:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsFlowServiceImpl extends BaseServiceImpl<StatisticsFlow, StatisticsFlowDao, Integer>
		implements StatisticsFlowService {


	@Autowired
	private SysAccessRecordService sysAccessRecordService;
	@Autowired
	private TimeCuttingService timeCuttingService;

	@Override
	public List<StatisticsFlowRealTimeVo> getRealTimeVo(Integer siteId) {
		/**
		 * 底下如此记录uv(可能按照某个大维度而言uv计算重复了，但是按照小维度(4分钟之内)，而言这种计算才是合理的)
		 */
		Date endTime = new Date();
		Date startTime = MyDateUtils.getMinuteBeforeTime(endTime, 30);
		// 根据4分钟进行时间切割
		List<Date> dates = timeCuttingService.getIntervalTimeList(startTime, endTime, 4);
		// 时间和操作记录集合
		Map<Date, List<SysAccessRecord>> dateMap = new LinkedHashMap<Date, List<SysAccessRecord>>();
		List<SysAccessRecord> accessRecords = sysAccessRecordService.findByDate(startTime, endTime, siteId);
		// 遍历时间取出该时间段内的记录list集合
		for (Date date : dates) {
			Long time = startTime.getTime();
			List<SysAccessRecord> records = accessRecords.stream()
					.filter(
							accessRecord -> accessRecord.getCreateTime().getTime() >= time && accessRecord.getCreateTime().getTime() <= date.getTime())
					.collect(Collectors.toList());
			startTime = date;
			dateMap.put(date, records);
		}
		List<String> dateList = new ArrayList<String>();
		List<Integer> pvs = new ArrayList<Integer>();
		List<Integer> uvs = new ArrayList<Integer>();
		// 遍历map将其放到list集合中
		for (Date date : dateMap.keySet()) {
			dateList.add(MyDateUtils.formatDate(date, MyDateUtils.COM_H_M_PATTERN));
			List<SysAccessRecord> records = dateMap.get(date);
			if (records != null && records.size() > 0) {
				pvs.add(records.size());
				uvs.add(this.receiveUv(records));
			} else {
				pvs.add(0);
				uvs.add(0);
			}
		}
		List<StatisticsFlowRealTimeVo> vos = new ArrayList<StatisticsFlowRealTimeVo>();
		for (int i = 0; i < dateList.size(); i++) {
			StatisticsFlowRealTimeVo vo = new StatisticsFlowRealTimeVo(dateList.get(i), pvs.get(i), uvs.get(i));
			vos.add(vo);
		}
		return vos;
	}

	@Override
	public Integer getRealTimeUvNum(Integer siteId) {
		Date endTime = new Date();
		Date startTime = MyDateUtils.getMinuteBeforeTime(endTime, 30);
		List<SysAccessRecord> accessRecords = sysAccessRecordService.findByDate(startTime, endTime, siteId);
		Integer uvNum = 0;
		if (accessRecords != null && accessRecords.size() > 0) {
			uvNum = this.receiveUv(accessRecords);
		}
		return uvNum;
	}
	
	@Override
	public List<StatisticsFlow> flowList(Date start, Date end, Integer siteId, Integer sourceType,
										 Boolean visit, String province)
			throws GlobalException {
		return dao.getFlowList(start, end, siteId, sourceType, visit, province);
	}

	@Override
	public AccessAreaVo timeArea(StatisticsDto dto) throws Exception {
		AccessAreaVo vo = new AccessAreaVo();
		List<StatisticsVisitorVo> pvList = new ArrayList<StatisticsVisitorVo>(34);
		List<StatisticsVisitorVo> uvList = new ArrayList<StatisticsVisitorVo>(34);
		List<StatisticsVisitorVo> ipsList = new ArrayList<StatisticsVisitorVo>(34);
		Date start = MyDateUtils.getStartDate(new Date());
		//查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService
				.haveList(start, new Date(), dto.getSiteId(), dto.getSourceType(),
						null, dto.getVisitor());
		//得到总的PV数
		Integer pv = records.size();
		vo.setPv(pv);
		//总的访客数
		Integer uv = records.stream()
				.filter(x -> StringUtils.isNotBlank(x.getCookieId()))
				.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
		vo.setUv(uv);
		//总的IP数
		Integer ips = records.stream()
				.filter(x -> StringUtils.isNotBlank(x.getAccessIp()))
				.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
		vo.setIps(ips);
		//跳出率计算：只出现一个的Session次数/PV总数
		//平均访问时长计算：总的访问时间/session次数
		List<BigDecimal> jumpAndTime = jumpAndTime(records);
		vo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
		vo.setVisitTime(jumpAndTime.get(1));
		vo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
		//省份PV数据
		Map<String, List<SysAccessRecord>> province = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getAccessProvince));
		for (String key : province.keySet()) {
			List<SysAccessRecord> sysRecords = province.get(key);
			StatisticsVisitorVo vi = new StatisticsVisitorVo();
			vi.setType(key);
			vi.setValue(sysRecords.size());
			BigDecimal sum = MathUtil.div(new BigDecimal(sysRecords.size()),
					new BigDecimal(pv), MathUtil.SCALE_LEN_COMMON);
			vi.setDecimal(sum.multiply(new BigDecimal(100)));
			pvList.add(vi);
		}
		vo.setPvList(pvList);
		//省份访客数据
		for (String key : province.keySet()) {
			List<SysAccessRecord> sysRecords = province.get(key);
			Integer visit = sysRecords.stream()
					.filter(x -> StringUtils.isNotBlank(x.getCookieId()))
					.collect(Collectors.groupingBy(SysAccessRecord::getCookieId))
					.size();
			StatisticsVisitorVo vi = new StatisticsVisitorVo();
			vi.setType(key);
			vi.setValue(visit);
			BigDecimal sum = MathUtil.div(new BigDecimal(visit),
					new BigDecimal(uv), MathUtil.SCALE_LEN_COMMON);
			vi.setDecimal(sum.multiply(new BigDecimal(100)));
			uvList.add(vi);
		}
		vo.setUvList(uvList);
		//IPS 访客数据
		for (String key : province.keySet()) {
			List<SysAccessRecord> sysRecords = province.get(key);
			Integer ip = sysRecords.stream()
					.filter(x -> StringUtils.isNotBlank(x.getAccessIp()))
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp))
					.size();
			StatisticsVisitorVo vi = new StatisticsVisitorVo();
			vi.setType(key);
			vi.setValue(ip);
			BigDecimal sum = MathUtil.div(new BigDecimal(ip),
					new BigDecimal(ips), MathUtil.SCALE_LEN_COMMON);
			vi.setDecimal(sum);
			ipsList.add(vi);
		}
		vo.setIpsList(ipsList);
		return vo;
	}

	@Override
	public JSONObject timeArea(StatisticsDto dto, Pageable pageable) throws Exception {
		List<AccessAreaVo> vos = new ArrayList<AccessAreaVo>(10);
		Date start = MyDateUtils.getStartDate(new Date());
		//查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService
				.haveList(start, new Date(), dto.getSiteId(), dto.getSourceType(),
						null, dto.getVisitor());
		//省份PV数据
		Map<String, List<SysAccessRecord>> province = records.stream()
				.filter(x -> StringUtils.isNotBlank(x.getAccessProvince()))
				.collect(Collectors.groupingBy(SysAccessRecord::getAccessProvince));
		for (String key : province.keySet()) {
			List<BaseAccessVo> cityList = new ArrayList<BaseAccessVo>(10);
			List<SysAccessRecord> sysRecords = province.get(key);
			Map<String, List<SysAccessRecord>> city = sysRecords.stream()
					.filter(x -> StringUtils.isNotBlank(x.getAccessCity()))
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessCity));
			for (String citykey : city.keySet()) {
				BaseAccessVo cityvo = new BaseAccessVo();
				List<SysAccessRecord> cityRecords = city.get(citykey);
				// pv数
				Integer pv = cityRecords.size();
				cityvo.setName(citykey);
				cityvo.setPv(pv);
				// 总的访客数
				Integer uv = cityRecords.stream()
						.filter(x -> StringUtils.isNotBlank(x.getCookieId()))
						.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
				cityvo.setUv(uv);
				// 总的IP数
				Integer ips = cityRecords.stream()
						.filter(x -> StringUtils.isNotBlank(x.getAccessIp()))
						.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
				cityvo.setIps(ips);
				List<BigDecimal> jumpAndTime = jumpAndTime(cityRecords);
				cityvo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
				cityvo.setVisitTime(jumpAndTime.get(1));
				cityvo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
				cityList.add(cityvo);
			}
			AccessAreaVo vo = new AccessAreaVo();
			vo.setName(key);
			//pv数
			Integer pv = sysRecords.size();
			vo.setPv(pv);
			//总的访客数
			Integer uv = sysRecords.stream()
					.filter(x -> StringUtils.isNotBlank(x.getCookieId()))
					.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
			vo.setUv(uv);
			//总的IP数
			Integer ips = sysRecords.stream()
					.filter(x -> StringUtils.isNotBlank(x.getAccessIp()))
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
			vo.setIps(ips);
			List<BigDecimal> jumpAndTime = jumpAndTime(sysRecords);
			vo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
			vo.setVisitTime(jumpAndTime.get(1));
			vo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
			vo.setCityList(cityList);
			vos.add(vo);
		}
		List<AccessAreaVo> areaVo = new ArrayList<AccessAreaVo>(10);
		//排序
		if (!vos.isEmpty()) {
			vos = sort(dto.getOrderType(), dto.getOrder(), vos);
			areaVo = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
		}
		PageImpl<AccessAreaVo> page = new PageImpl<AccessAreaVo>(areaVo, pageable, vos.size());
		JSONObject object = new JSONObject();
		object.put("page", page);
		object.put("collect", collect(areaVo));
		return object;
	}

	/**
	 * 区域排序
	 *
	 * @param orderType 类型
	 * @param order     方式
	 * @param vos       数据
	 * @return
	 * @Title: sort
	 */
	public List<AccessAreaVo> sort(Integer orderType, Boolean order, List<AccessAreaVo> vos) {
		if (orderType == null || order == null) {
			vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getPv))
					.collect(Collectors.toList());
			return vos;
		}
		if (orderType.equals(ORDER_TYPE_PV)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getPv))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getPv).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_UV)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getUv))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getUv).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_IP)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getIps))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getIps).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_JUMP)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getJump))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getJump).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_TIME)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getVisitTime))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getVisitTime).reversed())
						.collect(Collectors.toList());
			}
		} else {
			vos = vos.stream().sorted(Comparator.comparing(AccessAreaVo::getPv))
					.collect(Collectors.toList());
		}
		return vos;
	}

	/**
	 * 汇总
	 *
	 * @param areaVo 区域VO
	 * @return
	 * @Title: collect
	 */
	public BaseAccessVo collect(List<AccessAreaVo> areaVo) {
		BaseAccessVo vo = new BaseAccessVo();
		vo.setName("当前列表汇总");
		vo.setPv(areaVo.stream().map(AccessAreaVo::getPv).reduce(0, Integer::sum));
		vo.setUv(areaVo.stream().map(AccessAreaVo::getUv).reduce(0, Integer::sum));
		vo.setIps(areaVo.stream().map(AccessAreaVo::getIps).reduce(0, Integer::sum));
		vo.setJump(areaVo.stream().map(AccessAreaVo::getJump).reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitTime(areaVo.stream().map(AccessAreaVo::getVisitTime)
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitTimes(MyDateUtils.secToTime(vo.getVisitTime().intValue()));
		return vo;
	}

	/**
	 * 汇总
	 *
	 * @param deviceVo 设备VO
	 * @return
	 * @Title: collectDevice
	 */
	public BaseAccessVo collectDevice(List<AccessDeviceVo> deviceVo) {
		BaseAccessVo vo = new BaseAccessVo();
		vo.setName("当前列表汇总");
		vo.setPv(deviceVo.stream().map(AccessDeviceVo::getPv).reduce(0, Integer::sum));
		vo.setUv(deviceVo.stream().map(AccessDeviceVo::getUv).reduce(0, Integer::sum));
		vo.setIps(deviceVo.stream().map(AccessDeviceVo::getIps).reduce(0, Integer::sum));
		vo.setJump(deviceVo.stream().map(AccessDeviceVo::getJump).reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitTime(deviceVo.stream().map(AccessDeviceVo::getVisitTime)
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitTimes(MyDateUtils.secToTime(vo.getVisitTime().intValue()));
		return vo;
	}

	/**
	 * 汇总
	 *
	 * @param userVo 客户VO
	 * @return
	 * @Title: collectUser
	 */
	public AccessUserVo collectUser(List<AccessUserVo> userVo) {
		AccessUserVo vo = new AccessUserVo();
		vo.setName("当前列表汇总");
		vo.setPv(userVo.stream().map(AccessUserVo::getPv).reduce(0, Integer::sum));
		vo.setUv(userVo.stream().map(AccessUserVo::getUv).reduce(0, Integer::sum));
		vo.setIps(userVo.stream().map(AccessUserVo::getIps).reduce(0, Integer::sum));
		vo.setJump(userVo.stream().map(AccessUserVo::getJump).reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitPage(userVo.stream().map(AccessUserVo::getVisitPage)
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitTime(userVo.stream().map(AccessUserVo::getVisitTime)
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		vo.setVisitTimes(MyDateUtils.secToTime(vo.getVisitTime().intValue()));
		return vo;
	}

	/**
	 * 计算跳出率和平均访问时长
	 *
	 * @param records 记录列表
	 * @throws IllegalAccessException 转换异常
	 * @Title: jumpAndTime
	 */
	public List<BigDecimal> jumpAndTime(List<SysAccessRecord> records)
			throws IllegalAccessException {
		//过滤session为空
		records = records.stream()
				.filter(x -> StringUtils.isNotBlank(x.getSessionId()))
				.collect(Collectors.toList());
		LongAdder adder = new LongAdder();
		LongAdder timer = new LongAdder();
		Map<String, List<SysAccessRecord>> map = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		for (List<SysAccessRecord> list : map.values()) {
			//只出现一次的Session
			if (list.size() == 1) {
				adder.increment();
			} else {
				//统计总时长
				//按照创建时间排序
				list = list.stream().sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
						.collect(Collectors.toList());
				Long sum = list.get(list.size() - 1).getCreateTime().getTime()
						- list.get(0).getCreateTime().getTime();
				sum = Math.abs(sum) / 1000;
				timer.add(sum);
			}
		}
		BigDecimal jump = MathUtil.div(new BigDecimal(adder.sumThenReset()),
				new BigDecimal(records.size()), MathUtil.SCALE_LEN_COMMON);
		//平均访问时长计算：总的访问时间/ 次数
		BigDecimal visitTime = MathUtil.div(new BigDecimal(timer.sumThenReset()),
				new BigDecimal(map.size()), MathUtil.SCALE_LEN_COMMON);
		return Arrays.asList(jump, visitTime);
	}

	@Override
	public AccessAreaVo area(StatisticsDto dto) throws Exception {
		List<StatisticsVisitorVo> pvList = new ArrayList<StatisticsVisitorVo>(34);
		List<StatisticsVisitorVo> uvList = new ArrayList<StatisticsVisitorVo>(34);
		List<StatisticsVisitorVo> ipsList = new ArrayList<StatisticsVisitorVo>(34);
		AccessAreaVo vo = new AccessAreaVo();
		List<StatisticsFlow> folws = flowList(new Date(dto.getStartTime()),
				new Date(dto.getEndTime()), dto.getSiteId(),
				dto.getSourceType(), dto.getVisitor(), null);
		//得到PV,结果累加
		Integer pv = folws.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
		vo.setPv(pv);
		//得到uv
		Integer uv = folws.stream().map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
		vo.setUv(uv);
		//得到IPS
		Integer ips = folws.stream().map(StatisticsFlow::getIps).reduce(0, Integer::sum);
		vo.setIps(ips);
		//跳出率
		Integer only = folws.stream().map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
		BigDecimal jump = MathUtil.div(new BigDecimal(only),
				new BigDecimal(pv), MathUtil.SCALE_LEN_COMMON);
		vo.setJump(jump.multiply(new BigDecimal(100)));
		//得到访问时长
		Integer time = folws.stream().map(StatisticsFlow::getAccessHoureLong).reduce(0, Integer::sum);
		Integer session = folws.stream().map(StatisticsFlow::getSession).reduce(0, Integer::sum);
		BigDecimal hour = MathUtil.div(new BigDecimal(time),
				new BigDecimal(session), MathUtil.SCALE_LEN_COMMON);
		vo.setVisitTime(hour);
		vo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
		//省份PV数据
		Map<String, List<StatisticsFlow>> province = folws.stream()
				.filter(x -> StringUtils.isNotBlank(x.getVisitorProvince()))
				.collect(Collectors.groupingBy(StatisticsFlow::getVisitorProvince));
		for (String key : province.keySet()) {
			List<StatisticsFlow> pvValue = province.get(key);
			StatisticsVisitorVo vi = new StatisticsVisitorVo();
			Integer sum = pvValue.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
			vi.setType(key);
			vi.setValue(sum);
			BigDecimal dec = MathUtil.div(new BigDecimal(sum), new BigDecimal(pv),
					MathUtil.SCALE_LEN_COMMON);
			vi.setDecimal(dec.multiply(new BigDecimal(100)));
			pvList.add(vi);
		}
		vo.setPvList(pvList);
		// 省份访客数据
		for (String key : province.keySet()) {
			List<StatisticsFlow> uvValue = province.get(key);
			Integer sum = uvValue.stream().map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
			StatisticsVisitorVo vi = new StatisticsVisitorVo();
			vi.setType(key);
			vi.setValue(sum);
			BigDecimal dec = MathUtil.div(new BigDecimal(sum),
					new BigDecimal(uv), MathUtil.SCALE_LEN_COMMON);
			vi.setDecimal(dec.multiply(new BigDecimal(100)));
			uvList.add(vi);
		}
		vo.setUvList(uvList);
		// IPS 访客数据
		for (String key : province.keySet()) {
			List<StatisticsFlow> ipsValue = province.get(key);
			Integer sum = ipsValue.stream().map(StatisticsFlow::getIps).reduce(0, Integer::sum);
			StatisticsVisitorVo vi = new StatisticsVisitorVo();
			vi.setType(key);
			vi.setValue(sum);
			BigDecimal dec = MathUtil.div(new BigDecimal(sum),
					new BigDecimal(ips), MathUtil.SCALE_LEN_COMMON);
			vi.setDecimal(dec);
			ipsList.add(vi);
		}
		vo.setIpsList(ipsList);
		return vo;
	}

	@Override
	public JSONObject timePage(StatisticsDto dto, Pageable pageable) throws Exception {
		List<AccessAreaVo> vos = new ArrayList<AccessAreaVo>(10);
		List<StatisticsFlow> folws = flowList(new Date(dto.getStartTime()),
				new Date(dto.getEndTime()), dto.getSiteId(),
				dto.getSourceType(), dto.getVisitor(), null);
		//省份数据
		Map<String, List<StatisticsFlow>> province = folws.stream()
				.filter(x -> StringUtils.isNotBlank(x.getVisitorProvince()))
				.collect(Collectors.groupingBy(StatisticsFlow::getVisitorProvince));
		for (String key : province.keySet()) {
			List<BaseAccessVo> cityList = new ArrayList<BaseAccessVo>(10);
			List<StatisticsFlow> provinceList = province.get(key);
			Map<String, List<StatisticsFlow>> city = provinceList.stream()
					.collect(Collectors.groupingBy(StatisticsFlow::getVisitorCity));
			for (String citykey : city.keySet()) {
				BaseAccessVo cityvo = new BaseAccessVo();
				List<StatisticsFlow> cityFlow = city.get(citykey);
				cityvo.setName(citykey);
				//pv数
				Integer pv = cityFlow.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
				cityvo.setPv(pv);
				//总的访客数
				Integer uv = cityFlow.stream()
						.map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
				cityvo.setUv(uv);
				//总的IP数
				Integer ips = cityFlow.stream()
						.map(StatisticsFlow::getIps).reduce(0, Integer::sum);
				cityvo.setIps(ips);
				//计算跳出率
				Integer only = cityFlow.stream()
						.map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
				BigDecimal jump = MathUtil.div(new BigDecimal(only), new BigDecimal(pv),
						MathUtil.SCALE_LEN_COMMON);
				cityvo.setJump(jump.multiply(new BigDecimal(100)));
				// 得到访问时长
				Integer time = cityFlow.stream().map(StatisticsFlow::getAccessHoureLong)
						.reduce(0, Integer::sum);
				Integer session = cityFlow.stream().map(StatisticsFlow::getSession)
						.reduce(0, Integer::sum);
				BigDecimal hour = MathUtil.div(new BigDecimal(time), new BigDecimal(session),
						MathUtil.SCALE_LEN_COMMON);
				cityvo.setVisitTime(hour);
				cityvo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
				cityList.add(cityvo);
			}
			AccessAreaVo vo = new AccessAreaVo();
			vo.setName(key);
			//pv数
			Integer pv = provinceList.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
			vo.setPv(pv);
			//总的访客数
			Integer uv = provinceList.stream()
					.map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
			vo.setUv(uv);
			//总的IP数
			Integer ips = provinceList.stream()
					.map(StatisticsFlow::getIps).reduce(0, Integer::sum);
			vo.setIps(ips);
			//计算跳出率
			Integer only = provinceList.stream()
					.map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
			BigDecimal jump = MathUtil.div(new BigDecimal(only), new BigDecimal(pv),
					MathUtil.SCALE_LEN_COMMON);
			vo.setJump(jump.multiply(new BigDecimal(100)));
			// 得到访问时长
			Integer time = provinceList.stream().map(StatisticsFlow::getAccessHoureLong)
					.reduce(0, Integer::sum);
			Integer session = provinceList.stream().map(StatisticsFlow::getSession).reduce(0, Integer::sum);
			BigDecimal hour = MathUtil.div(new BigDecimal(time), new BigDecimal(session),
					MathUtil.SCALE_LEN_COMMON);
			vo.setVisitTime(hour);
			vo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
			vo.setCityList(cityList);
			vos.add(vo);
		}
		List<AccessAreaVo> areaVo = new ArrayList<AccessAreaVo>(10);
		//排序
		if (!vos.isEmpty()) {
			vos = sort(dto.getOrderType(), dto.getOrder(), vos);
			areaVo = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
		}
		PageImpl<AccessAreaVo> page = new PageImpl<AccessAreaVo>(areaVo, pageable, vos.size());
		JSONObject object = new JSONObject();
		object.put("page", page);
		object.put("collect", collect(areaVo));
		return object;
	}

	@Override
	public AccessDeviceVo timeDevice(Integer siteID) throws Exception {
		AccessDeviceVo vo = new AccessDeviceVo();
		List<DeviceVo> pvList = new ArrayList<AccessDeviceVo.DeviceVo>(10);
		List<DeviceVo> uvList = new ArrayList<AccessDeviceVo.DeviceVo>(10);
		List<DeviceVo> ipsList = new ArrayList<AccessDeviceVo.DeviceVo>(10);
		Date start = MyDateUtils.getStartDate(new Date());
		//查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService
				.haveList(start, new Date(), siteID, null,
						null, null);
		//得到总的PV数
		Integer pv = records.size();
		vo.setPv(pv);
		//总的访客数
		Integer uv = records.stream()
				.filter(x -> x.getCookieId() != null)
				.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
		vo.setUv(uv);
		//总的IP数
		Integer ips = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
		vo.setIps(ips);
		//跳出率计算：只出现一个的Session次数/PV总数
		//平均访问时长计算：总的访问时间/session次数
		List<BigDecimal> jumpAndTime = jumpAndTime(records);
		vo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
		vo.setVisitTime(jumpAndTime.get(1));
		vo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
		//设备类型数据
		Map<Short, List<SysAccessRecord>> device = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getDeviceType));
		for (Short key : device.keySet()) {
			List<SysAccessRecord> sysRecords = device.get(key);
			DeviceVo vi = new AccessDeviceVo().new DeviceVo();
			if (key == SysAccessRecord.COMPUTER) {
				vi.setType("计算机");
			} else {
				vi.setType("移动设备");
			}
			vi.setValue(sysRecords.size());
			pvList.add(vi);
		}
		vo.setPvList(pvList);
		//uv数据
		for (Short key : device.keySet()) {
			List<SysAccessRecord> sysRecords = device.get(key);
			Integer visit = sysRecords.stream()
					.filter(x -> x.getCookieId() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getCookieId))
					.size();
			DeviceVo vi = new AccessDeviceVo().new DeviceVo();
			if (key == SysAccessRecord.COMPUTER) {
				vi.setType("计算机");
			} else {
				vi.setType("移动设备");
			}
			vi.setValue(visit);
			uvList.add(vi);
		}
		vo.setUvList(uvList);
		//IPS数据
		for (Short key : device.keySet()) {
			List<SysAccessRecord> sysRecords = device.get(key);
			Integer ip = sysRecords.stream().collect(Collectors.groupingBy(SysAccessRecord::getAccessIp))
					.size();
			DeviceVo vi = new AccessDeviceVo().new DeviceVo();
			if (key == SysAccessRecord.COMPUTER) {
				vi.setType("计算机");
			} else {
				vi.setType("移动设备");
			}
			vi.setValue(ip);
			ipsList.add(vi);
		}
		vo.setIpsList(ipsList);
		return vo;
	}

	@Override
	public JSONObject timeDeviceList(Integer siteID, Integer orderType, Boolean order)
			throws Exception {
		List<AccessDeviceVo> vos = new ArrayList<AccessDeviceVo>(10);
		Date start = MyDateUtils.getStartDate(new Date());
		//查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService
				.haveList(start, new Date(), siteID, null,
						null, null);
		//设备数据
		Map<Short, List<SysAccessRecord>> deviceType = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getDeviceType));
		for (Short key : deviceType.keySet()) {
			List<BaseAccessVo> deviceList = new ArrayList<BaseAccessVo>(10);
			List<SysAccessRecord> sysRecords = deviceType.get(key);
			Map<String, List<SysAccessRecord>> device = sysRecords.stream()
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessDevice));
			for (String devicekey : device.keySet()) {
				BaseAccessVo devicevo = new BaseAccessVo();
				List<SysAccessRecord> deviceRecords = device.get(devicekey);
				// pv数
				Integer pv = deviceRecords.size();
				devicevo.setName(devicekey);
				devicevo.setPv(pv);
				// 总的访客数
				Integer uv = deviceRecords.stream()
						.filter(x -> x.getCookieId() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
				devicevo.setUv(uv);
				// 总的IP数
				Integer ips = deviceRecords.stream()
						.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
				devicevo.setIps(ips);
				List<BigDecimal> jumpAndTime = jumpAndTime(deviceRecords);
				devicevo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
				devicevo.setVisitTime(jumpAndTime.get(1));
				devicevo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
				deviceList.add(devicevo);
			}
			AccessDeviceVo vo = new AccessDeviceVo();
			if (key == SysAccessRecord.COMPUTER) {
				vo.setName("计算机");
			} else {
				vo.setName("移动设备");
			}
			//pv数
			Integer pv = sysRecords.size();
			vo.setPv(pv);
			//总的访客数
			Integer uv = sysRecords.stream()
					.filter(x -> x.getCookieId() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
			vo.setUv(uv);
			//总的IP数
			Integer ips = sysRecords.stream()
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
			vo.setIps(ips);
			List<BigDecimal> jumpAndTime = jumpAndTime(sysRecords);
			vo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
			vo.setVisitTime(jumpAndTime.get(1));
			vo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
			vo.setDeviceList(deviceList);
			vos.add(vo);
		}
		//排序
		if (!vos.isEmpty()) {
			vos = sortDevice(orderType, order, vos);
		}
		JSONObject object = new JSONObject();
		object.put("list", vos);
		object.put("collect", collectDevice(vos));
		return object;
	}

	/**
	 * 区域排序
	 *
	 * @param orderType 类型
	 * @param order     方式
	 * @param vos       数据
	 * @return
	 * @Title: sort
	 */
	public List<AccessDeviceVo> sortDevice(Integer orderType, Boolean order, List<AccessDeviceVo> vos) {
		if (orderType == null || order == null) {
			vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getPv))
					.collect(Collectors.toList());
			return vos;
		}
		if (orderType.equals(ORDER_TYPE_PV)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getPv))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getPv).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_UV)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getUv))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getUv).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_IP)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getIps))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getIps).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_JUMP)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getJump))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getJump).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_TIME)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getVisitTime))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getVisitTime).reversed())
						.collect(Collectors.toList());
			}
		} else {
			vos = vos.stream().sorted(Comparator.comparing(AccessDeviceVo::getPv))
					.collect(Collectors.toList());
		}
		return vos;
	}

	@Override
	public AccessDeviceVo device(StatisticsDto dto) throws Exception {
		AccessDeviceVo vo = new AccessDeviceVo();
		List<DeviceVo> pvList = new ArrayList<AccessDeviceVo.DeviceVo>(10);
		List<DeviceVo> uvList = new ArrayList<AccessDeviceVo.DeviceVo>(10);
		List<DeviceVo> ipsList = new ArrayList<AccessDeviceVo.DeviceVo>(10);
		List<StatisticsFlow> folws = flowList(new Date(dto.getStartTime()),
				new Date(dto.getEndTime()), dto.getSiteId(),
				null, null, null);
		//得到PV,结果累加
		Integer pv = folws.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
		vo.setPv(pv);
		//得到uv
		Integer uv = folws.stream().map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
		vo.setUv(uv);
		//得到IPS
		Integer ips = folws.stream().map(StatisticsFlow::getIps).reduce(0, Integer::sum);
		vo.setIps(ips);
		//跳出率
		Integer only = folws.stream().map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
		BigDecimal jump = MathUtil.div(new BigDecimal(only),
				new BigDecimal(pv), MathUtil.SCALE_LEN_COMMON);
		vo.setJump(jump.multiply(new BigDecimal(100)));
		//得到访问时长
		Integer time = folws.stream().map(StatisticsFlow::getAccessHoureLong).reduce(0, Integer::sum);
		Integer session = folws.stream().map(StatisticsFlow::getSession).reduce(0, Integer::sum);
		BigDecimal hour = MathUtil.div(new BigDecimal(time),
				new BigDecimal(session), MathUtil.SCALE_LEN_COMMON);
		vo.setVisitTime(hour);
		vo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
		//设备PV数据
		Map<Integer, List<StatisticsFlow>> device = folws.stream()
				.collect(Collectors.groupingBy(StatisticsFlow::getVisitorDeviceType));
		for (Integer key : device.keySet()) {
			List<StatisticsFlow> pvValue = device.get(key);
			DeviceVo vi = new AccessDeviceVo().new DeviceVo();
			if (key == StatisticsFlow.COMPUTER) {
				vi.setType("计算机");
			} else {
				vi.setType("移动设备");
			}
			vi.setValue(pvValue.size());
			pvList.add(vi);
		}
		vo.setPvList(pvList);
		// 设备UV数据
		for (Integer key : device.keySet()) {
			List<StatisticsFlow> uvValue = device.get(key);
			Integer sum = uvValue.stream().map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
			DeviceVo vi = new AccessDeviceVo().new DeviceVo();
			if (key == StatisticsFlow.COMPUTER) {
				vi.setType("计算机");
			} else {
				vi.setType("移动设备");
			}
			vi.setValue(sum);
			uvList.add(vi);
		}
		vo.setUvList(uvList);
		// 设备IPS数据
		for (Integer key : device.keySet()) {
			List<StatisticsFlow> ipsValue = device.get(key);
			Integer sum = ipsValue.stream().map(StatisticsFlow::getIps).reduce(0, Integer::sum);
			DeviceVo vi = new AccessDeviceVo().new DeviceVo();
			if (key == StatisticsFlow.COMPUTER) {
				vi.setType("计算机");
			} else {
				vi.setType("移动设备");
			}
			vi.setValue(sum);
			ipsList.add(vi);
		}
		vo.setIpsList(ipsList);
		return vo;
	}

	@Override
	public JSONObject deviceList(StatisticsDto dto) throws Exception {
		List<AccessDeviceVo> vos = new ArrayList<AccessDeviceVo>(10);
		List<StatisticsFlow> folws = flowList(new Date(dto.getStartTime()),
				new Date(dto.getEndTime()), null,
				null, null, null);
		//设备数据
		Map<Integer, List<StatisticsFlow>> deviceType = folws.stream()
				.collect(Collectors.groupingBy(StatisticsFlow::getVisitorDeviceType));
		for (Integer key : deviceType.keySet()) {
			List<BaseAccessVo> devices = new ArrayList<BaseAccessVo>(10);
			List<StatisticsFlow> deviceList = deviceType.get(key);
			Map<String, List<StatisticsFlow>> device = deviceList.stream()
					.filter(x -> StringUtils.isNotBlank(x.getVisitorDeviceOs()))
					.collect(Collectors.groupingBy(StatisticsFlow::getVisitorDeviceOs));
			for (String devicekey : device.keySet()) {
				BaseAccessVo deviceVo = new BaseAccessVo();
				List<StatisticsFlow> cityFlow = device.get(devicekey);
				deviceVo.setName(devicekey);
				//pv数
				Integer pv = cityFlow.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
				deviceVo.setPv(pv);
				//总的访客数
				Integer uv = cityFlow.stream()
						.map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
				deviceVo.setUv(uv);
				//总的IP数
				Integer ips = cityFlow.stream()
						.map(StatisticsFlow::getIps).reduce(0, Integer::sum);
				deviceVo.setIps(ips);
				//计算跳出率
				Integer only = cityFlow.stream()
						.map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
				BigDecimal jump = MathUtil.div(new BigDecimal(only), new BigDecimal(pv),
						MathUtil.SCALE_LEN_COMMON);
				deviceVo.setJump(jump.multiply(new BigDecimal(100)));
				// 得到访问时长
				Integer time = cityFlow.stream().map(StatisticsFlow::getAccessHoureLong)
						.reduce(0, Integer::sum);
				Integer session = cityFlow.stream().map(StatisticsFlow::getSession)
						.reduce(0, Integer::sum);
				BigDecimal hour = MathUtil.div(new BigDecimal(time), new BigDecimal(session),
						MathUtil.SCALE_LEN_COMMON);
				deviceVo.setVisitTime(hour);
				deviceVo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
				devices.add(deviceVo);
			}
			AccessDeviceVo vo = new AccessDeviceVo();
			if (key == StatisticsFlow.COMPUTER) {
				vo.setName("计算机");
			} else {
				vo.setName("移动设备");
			}
			//pv数
			Integer pv = deviceList.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
			vo.setPv(pv);
			//总的访客数
			Integer uv = deviceList.stream()
					.map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
			vo.setUv(uv);
			//总的IP数
			Integer ips = deviceList.stream()
					.map(StatisticsFlow::getIps).reduce(0, Integer::sum);
			vo.setIps(ips);
			//计算跳出率
			Integer only = deviceList.stream()
					.map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
			BigDecimal jump = MathUtil.div(new BigDecimal(only), new BigDecimal(pv),
					MathUtil.SCALE_LEN_COMMON);
			vo.setJump(jump.multiply(new BigDecimal(100)));
			// 得到访问时长
			Integer time = deviceList.stream().map(StatisticsFlow::getAccessHoureLong)
					.reduce(0, Integer::sum);
			Integer session = deviceList.stream().map(StatisticsFlow::getSession).reduce(0, Integer::sum);
			BigDecimal hour = MathUtil.div(new BigDecimal(time), new BigDecimal(session),
					MathUtil.SCALE_LEN_COMMON);
			vo.setVisitTime(hour);
			vo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
			vo.setDeviceList(devices);
			vos.add(vo);
		}
		JSONObject object = new JSONObject();
		object.put("list", vos);
		object.put("collect", collectDevice(vos));
		return object;
	}

	@Override
	public JSONObject timeUsers(StatisticsDto dto) throws Exception {
		List<AccessUserVo> vos = new ArrayList<AccessUserVo>(10);
		Date start = MyDateUtils.getStartDate(new Date());
		Date end = MyDateUtils.getFinallyDate(new Date());
		//查询列表记录
		List<SysAccessRecord> records = sysAccessRecordService
				.haveList(start, end, dto.getSiteId(), dto.getSourceType(),
						dto.getProvince(), null);
		if (records.isEmpty()) {
			AccessUserVo vo = new AccessUserVo();
			vo.setName("新客户");
			vo.setIsNew(true);
			vo.setPv(0);
			vo.setUv(0);
			vo.setIps(0);
			vo.setJump(BigDecimal.ZERO);
			vo.setVisitTime(BigDecimal.ZERO);
			vo.setVisitPage(BigDecimal.ZERO);
			AccessUserVo voo = new AccessUserVo();
			voo.setName("老客户");
			vo.setIsNew(false);
			voo.setPv(0);
			voo.setUv(0);
			voo.setIps(0);
			voo.setJump(BigDecimal.ZERO);
			voo.setVisitTime(BigDecimal.ZERO);
			voo.setVisitPage(BigDecimal.ZERO);
			vos.add(vo);
			vos.add(voo);
			JSONObject object = new JSONObject();
			object.put("list", vos);
			object.put("collect", collectUser(vos));
			return object;
		}
		//session分组
		Map<String, List<SysAccessRecord>> session = records.stream()
				.filter(x -> StringUtils.isNotBlank(x.getSessionId()))
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		//新老客户数据
		Map<Boolean, List<SysAccessRecord>> deviceType = records.stream()
				.filter(x -> x.getNewVisitor() != null)
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
			//pv数
			Integer pv = sysRecords.size();
			vo.setPv(pv);
			//总的访客数
			Integer uv = uvNumber(sysRecords);
			vo.setUv(uv);
			//总的IP数
			Integer ips = sysRecords.stream()
					.filter(x -> x.getAccessIp() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
			vo.setIps(ips);
			List<BigDecimal> jumpAndTime = jumpAndTime(sysRecords);
			vo.setJump(jumpAndTime.get(0).multiply(new BigDecimal(100)));
			vo.setVisitTime(jumpAndTime.get(1));
			vo.setVisitTimes(MyDateUtils.secToTime(jumpAndTime.get(1).intValue()));
			//平均访问页数:PV/session个数
			BigDecimal page = MathUtil.div(new BigDecimal(pv),
					new BigDecimal(session.size()),
					MathUtil.SCALE_LEN_COMMON);
			vo.setVisitPage(page);
			vos.add(vo);
		}
		//排序
		if (!vos.isEmpty()) {
			vos = sortUser(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("list", vos);
		object.put("collect", collectUser(vos));
		return object;
	}

	@Override
	public JSONObject users(StatisticsDto dto) throws Exception {
		List<AccessUserVo> vos = new ArrayList<AccessUserVo>(10);
		List<StatisticsFlow> folws = flowList(new Date(dto.getStartTime()),
				new Date(dto.getEndTime()), dto.getSiteId(),
				dto.getSourceType(), null, dto.getProvince());
		//新老客户
		Map<Boolean, List<StatisticsFlow>> user = folws.stream()
				.collect(Collectors.groupingBy(StatisticsFlow::getIsNewVisitor));
		for (Boolean key : user.keySet()) {
			List<StatisticsFlow> userList = user.get(key);
			AccessUserVo vo = new AccessUserVo();
			if (key) {
				vo.setName("新客户");
				vo.setIsNew(true);
			} else {
				vo.setName("老客户");
				vo.setIsNew(false);
			}
			//pv数
			Integer pv = userList.stream().map(StatisticsFlow::getPvs).reduce(0, Integer::sum);
			vo.setPv(pv);
			//总的访客数
			Integer uv = userList.stream()
					.map(StatisticsFlow::getUvs).reduce(0, Integer::sum);
			vo.setUv(uv);
			//总的IP数
			Integer ips = userList.stream()
					.map(StatisticsFlow::getIps).reduce(0, Integer::sum);
			vo.setIps(ips);
			//计算跳出率
			Integer only = userList.stream()
					.map(StatisticsFlow::getOnlyOnePv).reduce(0, Integer::sum);
			BigDecimal jump = MathUtil.div(new BigDecimal(only), new BigDecimal(pv),
					MathUtil.SCALE_LEN_COMMON);
			vo.setJump(jump.multiply(new BigDecimal(100)));
			// 得到访问时长
			Integer time = userList.stream().map(StatisticsFlow::getAccessHoureLong)
					.reduce(0, Integer::sum);
			// 得到session个数
			Integer session = userList.stream().map(StatisticsFlow::getSession).reduce(0, Integer::sum);
			BigDecimal hour = MathUtil.div(new BigDecimal(time), new BigDecimal(session),
					MathUtil.SCALE_LEN_COMMON);
			vo.setVisitTime(hour);
			vo.setVisitTimes(MyDateUtils.secToTime(hour.intValue()));
			//平均访问页数:PV/session个数
			BigDecimal page = MathUtil.div(new BigDecimal(pv),
					new BigDecimal(session),
					MathUtil.SCALE_LEN_COMMON);
			vo.setVisitPage(page);
			vos.add(vo);
		}
		//排序
		if (!vos.isEmpty()) {
			vos = sortUser(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("list", vos);
		object.put("collect", collectUser(vos));
		return object;
	}

	/**
	 * 新老客户排序
	 *
	 * @param orderType 类型
	 * @param order     方式
	 * @param vos       数据
	 * @return
	 * @Title: sortUser
	 */
	public List<AccessUserVo> sortUser(Integer orderType, Boolean order, List<AccessUserVo> vos) {
		if (orderType == null || order == null) {
			vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getPv))
					.collect(Collectors.toList());
			return vos;
		}
		if (orderType.equals(ORDER_TYPE_PV)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getPv))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getPv).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_UV)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getUv))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getUv).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_IP)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getIps))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getIps).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_JUMP)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getJump))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getJump).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_TIME)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getVisitTime))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getVisitTime).reversed())
						.collect(Collectors.toList());
			}
		} else if (orderType.equals(ORDER_TYPE_PAGE)) {
			if (order) {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getVisitPage))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getVisitPage).reversed())
						.collect(Collectors.toList());
			}
		} else {
			vos = vos.stream().sorted(Comparator.comparing(AccessUserVo::getPv))
					.collect(Collectors.toList());
		}
		return vos;
	}

	/**
	 * 通过List<SysAccessRecord>集合计算出uv数
	 */
	private int receiveUv(List<SysAccessRecord> records) {
		// 方法进行此records一定不为空，至少存在一个值
		List<SysAccessRecord> logRecords = records.stream()
				.filter(record -> record.getLoginUserId() != null).collect(Collectors.toList());
		List<SysAccessRecord> notLogRecords = records.stream()
				.filter(record -> record.getLoginUserId() == null && record.getCookieId() != null)
				.collect(Collectors.toList());
		Integer logUv = 0;
		if (logRecords != null && logRecords.size() > 0) {
			// 集合过滤掉userId不为空的(登录的),而后通过userId分组，再取出userId的个数
			logUv = logRecords.stream().collect(Collectors.groupingBy(SysAccessRecord::getLoginUserId))
					.keySet().size();
		}
		Integer notLogUv = 0;
		if (notLogRecords != null && notLogRecords.size() > 0) {
			// 集合过滤掉userId为空的(游客访问)，而后通过cookieId分组，再取出cookieId的个数
			notLogUv = notLogRecords.stream().collect(Collectors.groupingBy(SysAccessRecord::getCookieId))
					.keySet().size();
		}
		return logUv + notLogUv;
	}

	public Map<Date, Date> groupByWeek(Date startTime, Date endTime) {
		Map<Date, Date> dateMap = new LinkedHashMap<Date, Date>();
		Calendar cl1 = Calendar.getInstance();
		cl1.setTime(startTime);
		Calendar cl2 = Calendar.getInstance();
		cl2.setTime(endTime);
		Calendar cl3 = Calendar.getInstance();
		if (cl1.get(Calendar.DAY_OF_WEEK) - 1 == 1) {
			cl1.add(Calendar.DAY_OF_MONTH, 6);

		} else {
			cl1.add(Calendar.DAY_OF_MONTH, 8 - cl1.get(Calendar.DAY_OF_WEEK));
		}
		dateMap.put(MyDateUtils.getStartDate(startTime), MyDateUtils.getStartDate(cl1.getTime()));
		while (cl3.getTime().getTime() < cl2.getTime().getTime()) {
			cl1.add(Calendar.DAY_OF_MONTH, 1);
			Date s = cl1.getTime();
			cl1.add(Calendar.DAY_OF_MONTH, 6);
			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(cl1.getTime()));
			cl3.setTime(cl1.getTime());
			cl3.add(Calendar.DAY_OF_MONTH, 7);
		}
//		do {
//			cl1.add(Calendar.DAY_OF_MONTH, 1);
//			Date s = cl1.getTime();
//			cl1.add(Calendar.DAY_OF_MONTH, 6);
//			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(cl1.getTime()));
//			cl3.setTime(cl1.getTime());
//			cl3.add(Calendar.DAY_OF_MONTH, 7);
//		} while (cl3.getTime().getTime() < cl2.getTime().getTime());
		cl1.add(Calendar.DAY_OF_MONTH, 1);
		Date s = cl1.getTime();
		if (cl1.getTime().equals(endTime)) {
			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(s));
		} else {
			dateMap.put(MyDateUtils.getStartDate(s), MyDateUtils.getFinallyDate(endTime));
		}
		return dateMap;
	}

	@Override
	public List<StatisticsFlowRealTimeItemVo> getRealTimeItemVo(StatisticsFlowRealTimeItemDto dto) {
		// 默认时间为当前时间
		Date endTime = new Date();
		Date startTime = MyDateUtils.getStartDate(endTime);
		Date time = dto.getTime();
		if (time != null) {
			startTime = MyDateUtils.getStartDate(time);
			endTime = MyDateUtils.getFinallyDate(time);
		}
		// 根据筛选条件筛选出数据
		List<SysAccessRecord> records = sysAccessRecordService.getRealTimeItem(dto, startTime, endTime);
		if (records != null && records.size() > 0) {
			Integer minFrequency = dto.getMinFrequency();
			// 全局储存放置数据
			List<SysAccessRecord> accessRecords = new ArrayList<SysAccessRecord>();
			// 全局储存放置map
			Map<Integer, SysAccessRecord> accessRecordMap = new HashMap<Integer, SysAccessRecord>();
			if (minFrequency != null) {
				Map<Integer, List<SysAccessRecord>> frequencyMap = this.receiveFrequency(records);
				Map<Integer, List<SysAccessRecord>> minFrequencyMap =
						new LinkedHashMap<Integer, List<SysAccessRecord>>();
				for (Integer num : frequencyMap.keySet()) {
					if (num >= minFrequency) {
						minFrequencyMap.put(num, frequencyMap.get(num));
					}
				}
				Map<Integer, List<SysAccessRecord>> maxFrequencyMap =
						new LinkedHashMap<Integer, List<SysAccessRecord>>();
				Integer maxFrequency = dto.getMaxFrequency();
				if (maxFrequency != null) {
					for (Integer num : minFrequencyMap.keySet()) {
						if (num <= maxFrequency) {
							maxFrequencyMap.put(num, frequencyMap.get(num));
						}
					}
				}
				accessRecords.addAll(this.toIntegerList(maxFrequencyMap));
				accessRecordMap = accessRecords.stream()
						.collect(Collectors.toMap(SysAccessRecord::getId, s -> s));
			}
			Integer minDepth = dto.getMinDepth();
			Map<Integer, List<SysAccessRecord>> depthMap = this.receiveDepth(records);
			if (minDepth != null) {
				Map<Integer, List<SysAccessRecord>> minDepthMap =
						new LinkedHashMap<Integer, List<SysAccessRecord>>();
				for (Integer depthNum : depthMap.keySet()) {
					if (depthNum >= minDepth) {
						minDepthMap.put(depthNum, depthMap.get(depthNum));
					}
				}
				Map<Integer, List<SysAccessRecord>> maxDepthMap =
						new LinkedHashMap<Integer, List<SysAccessRecord>>();
				Integer maxDepth = dto.getMaxDepth();
				if (maxDepth != null) {
					for (Integer depthNum : minDepthMap.keySet()) {
						if (depthNum <= maxDepth) {
							maxDepthMap.put(depthNum, depthMap.get(depthNum));
						}
					}
				}
				depthMap = maxDepthMap;
			}
			accessRecords.clear();
			for (Integer num : depthMap.keySet()) {
				// 此处相当于将上面两个筛选条件的数据取一个交集
				for (SysAccessRecord sysAccessRecord : depthMap.get(num)) {
					sysAccessRecord.setPageSize(num);
					if (dto.getIsFrequency()) {
						if (accessRecordMap.size() > 0) {
							if (accessRecordMap.get(sysAccessRecord.getId()) != null) {
								accessRecords.add(sysAccessRecord);
							}
						}
					} else {
						accessRecords.add(sysAccessRecord);
					}
				}
			}
			if (accessRecordMap.size() == 0) {
				accessRecordMap = accessRecords.stream()
						.collect(Collectors.toMap(SysAccessRecord::getId, s -> s));
			} else {
				for (SysAccessRecord sysAccessRecord : accessRecords) {
					Integer id = sysAccessRecord.getId();
					if (accessRecordMap.get(id) != null) {
						accessRecordMap.put(id, sysAccessRecord);
					}
				}
			}

			Integer duration = dto.getDuration();
			Map<Integer, List<SysAccessRecord>> durationMap = this.receiveDuration(accessRecords, true,false);
			if (duration != null) {
				// 处理访问时长
				durationMap = this.spliceDurationMap(durationMap, duration);
			}
			List<String> sessions = new ArrayList<String>();
			List<SysAccessRecord> durations = new ArrayList<SysAccessRecord>();
			for (Integer i : durationMap.keySet()) {
				// 底下通过session进行分组因为在之前为了过滤掉筛选条件访问时长，所以在处理的时候我将相同的访问时长的list集合放到同一个Map的key中，所以到达此处必须再次通过session分组取出对应的集合
				List<SysAccessRecord> snapRecords = durationMap.get(i);
				Map<String, List<SysAccessRecord>> snapRecordMap = snapRecords.stream().collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
				for (String session : snapRecordMap.keySet()) {
					// 此处排序时因为，使用stream进行分组，其中的list会乱掉，所以再次排序，排序后再取第一个页面才是入口页面
					List<SysAccessRecord> newSnapRecords = snapRecordMap.get(session).stream().sorted(Comparator.comparing(SysAccessRecord::getId))
							.collect(Collectors.toList());
					// 此处去只取集合第一个是因为需要显示入口页面
					SysAccessRecord snapDuration = newSnapRecords.get(0);
					String sessionId = snapDuration.getSessionId();
					if (!sessions.contains(sessionId)) {
						snapDuration.setAccessTime(i);
						durations.add(snapDuration);
						sessions.add(sessionId);
					}
				}
			}
			List<StatisticsFlowRealTimeItemVo> vos = new ArrayList<StatisticsFlowRealTimeItemVo>();
			for (int i = 0; i < durations.size(); i++) {
				SysAccessRecord record = durations.get(i);
				SysAccessRecord newRecord = accessRecordMap.get(record.getId());
				if (newRecord != null) {
					StatisticsFlowRealTimeItemVo vo = new StatisticsFlowRealTimeItemVo();
					vo.setId(i + 1);
					vo.setCreateTime(record.getCreateTime());
					vo.setAreaName(record.getAccessCity());
					vo.setSourceUrl(record.getSourceUrl());
					vo.setEntranceUrl(record.getAccessUrl());
					vo.setUserName(record.getLoginUserName());
					vo.setAccessIp(record.getAccessIp());
					vo.setAccessTime(record.getAccessTime());
					vo.setAccessPage(newRecord.getPageSize());
					vos.add(vo);
				}
			}
			vos = this.sortFlowRealTimeItemVo(vos, dto.getSortTerm(), dto.getOrder());
			return vos;
		}
		return null;
	}

	private List<StatisticsFlowRealTimeItemVo> sortFlowRealTimeItemVo(List<StatisticsFlowRealTimeItemVo> vos, Short sortTerm, Boolean order) {
		switch (sortTerm) {
			case StatisticsFlow.SORT_TIME:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowRealTimeItemVo::getCreateTime)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowRealTimeItemVo::getCreateTime).reversed()).collect(Collectors.toList());
				}
				break;
			case StatisticsFlow.SORT_DURATION:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowRealTimeItemVo::getAccessTime)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowRealTimeItemVo::getAccessTime).reversed()).collect(Collectors.toList());
				}
				break;
			case StatisticsFlow.SORT_PAGE_NUM:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowRealTimeItemVo::getAccessPage)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowRealTimeItemVo::getAccessPage).reversed()).collect(Collectors.toList());
				}
				break;
			default:
				break;
		}
		return vos;
	}

	private Map<Integer, List<SysAccessRecord>> spliceDurationMap(Map<Integer, List<SysAccessRecord>> durationMap, Integer duration) {
		Map<Integer, List<SysAccessRecord>> numDurationMap = new LinkedHashMap<Integer, List<SysAccessRecord>>();
		switch (duration) {
			case 1:
				this.treatDurationMap(durationMap, numDurationMap, 0, 30);
				break;
			case 2:
				this.treatDurationMap(durationMap, numDurationMap, 31, 60);
				break;
			case 3:
				this.treatDurationMap(durationMap, numDurationMap, 61, 90);
				break;
			case 4:
				this.treatDurationMap(durationMap, numDurationMap, 91, 180);
				break;
			case 5:
				this.treatDurationMap(durationMap, numDurationMap, 181, 300);
				break;
			case 6:
				this.treatDurationMap(durationMap, numDurationMap, 301, 600);
				break;
			case 7:
				this.treatDurationMap(durationMap, numDurationMap, 600, null);
				break;
			default:
				break;
		}
		return numDurationMap;
	}

	private void treatDurationMap(Map<Integer, List<SysAccessRecord>> durationMap, Map<Integer, List<SysAccessRecord>> newDurationMap, Integer minTime, Integer maxTime) {
		for (Integer i : durationMap.keySet()) {
			if (i == 0) {
				newDurationMap.put(i, durationMap.get(i));
			}
			if (maxTime != null) {
				if (minTime != null ) {
					if (i >= minTime && i<= maxTime) {
						newDurationMap.put(i, durationMap.get(i));
					}
				} 
			} else {
				if (i >= minTime) {
					newDurationMap.put(i, durationMap.get(i));
				}
			}
		}
	}

	/**
	 * 计算访问时间
	 * filter -> 过滤，过滤session为1的情况(计算时长，需要过滤掉session为1的情况，但是在实时访客中此值又不需要过滤)
	 */
	private Map<Integer, List<SysAccessRecord>> receiveDuration(List<SysAccessRecord> records, boolean isInitMap,boolean filter) {
		records = records.stream().filter(record -> record.getSessionId() != null).collect(Collectors.toList());
		Map<Integer, List<SysAccessRecord>> newDurationMap = new LinkedHashMap<Integer, List<SysAccessRecord>>();
		// 过滤掉session为空的，并且根据session分组
		Map<String, List<SysAccessRecord>> durationMap = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		for (String session : durationMap.keySet()) {
			List<SysAccessRecord> accessRecords = durationMap.get(session);
			// 此处过滤掉访问session只为1的情况
			if (filter) {
				if (accessRecords.size() == 1) {
					continue;
				}
			}
			// 根据时间排序，防止出现list数据乱的情况
			accessRecords = accessRecords.stream().sorted(Comparator.comparing(SysAccessRecord::getCreateTime)).collect(Collectors.toList());
			Long minTime = accessRecords.get(0).getCreateTime().getTime();
			Long maxTime = accessRecords.get(accessRecords.size() - 1).getCreateTime().getTime();
			Long time = (maxTime - minTime) / 1000;
			Integer timeValue = time.intValue();
			// 是否需要初始化Map
			if (isInitMap) {
				this.initDurationMap(newDurationMap, timeValue, accessRecords);
			} else {
				if (newDurationMap.get(timeValue) != null) {
					newDurationMap.remove(timeValue);
					timeValue = timeValue * 2;
				}
				newDurationMap.put(timeValue, accessRecords);
			}
		}
		return newDurationMap;
	}


	private void initDurationMap(Map<Integer, List<SysAccessRecord>> durationMap, Integer num, List<SysAccessRecord> records) {
		List<SysAccessRecord> duration = durationMap.get(num);
		if (duration != null && duration.size() > 0) {
			duration.addAll(records);
			durationMap.put(num, duration);
		} else {
			durationMap.put(num, records);
		}
	}

	private Map<Integer, List<SysAccessRecord>> receiveDepth(List<SysAccessRecord> records) {
		if (records != null && records.size() > 0) {
			records = records.stream().filter(record -> record.getAccessUrl() != null && record.getSessionId() != null).collect(Collectors.toList());
			Map<Integer, List<SysAccessRecord>> newDepthMap = new LinkedHashMap<Integer, List<SysAccessRecord>>();
			Map<String, Map<String, List<SysAccessRecord>>> depthMap = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getAccessUrl, Collectors.groupingBy(SysAccessRecord::getSessionId)));
			for (String str : depthMap.keySet()) {
				Integer num = depthMap.get(str).size();
				List<SysAccessRecord> value = this.toStringList(depthMap.get(str));
				newDepthMap.put(num, value);
			}
			return newDepthMap;
		}
		return null;
	}

	/**
	 * 计算访问频次
	 */
	private Map<Integer, List<SysAccessRecord>> receiveFrequency(List<SysAccessRecord> records) {
		// 方法进行此records一定不为空，至少存在一个值
		List<SysAccessRecord> logRecords = records.stream().filter(record -> record.getLoginUserId() != null && record.getSessionId() != null)
				.collect(Collectors.toList());
		// 根据userId+sessionId分组计算已登录用户的访问频次
		Map<String, Map<Integer, List<SysAccessRecord>>> logRecordMap = logRecords.stream().collect(Collectors.groupingBy(SysAccessRecord::getSessionId, Collectors.groupingBy(SysAccessRecord::getLoginUserId)));
		Map<Integer, List<SysAccessRecord>> frequency = new LinkedHashMap<Integer, List<SysAccessRecord>>();
		for (String session : logRecordMap.keySet()) {
			Integer num = logRecordMap.get(session).size();
			List<SysAccessRecord> value = this.toIntegerList(logRecordMap.get(session));
			frequency.put(num, value);
		}
		List<SysAccessRecord> notLogRecords = records.stream()
				.filter(record -> record.getLoginUserId() == null && record.getCookieId() != null && record.getSessionId() != null)
				.collect(Collectors.toList());
		// 根据cookieId+session分组计算未登录用户的访问频次
		Map<String, Map<String, List<SysAccessRecord>>> notLogRecordMap = notLogRecords.stream().collect(Collectors.groupingBy(SysAccessRecord::getSessionId, Collectors.groupingBy(SysAccessRecord::getCookieId)));
		for (String cookieId : notLogRecordMap.keySet()) {
			Integer num = notLogRecordMap.get(cookieId).size();
			List<SysAccessRecord> newValue = this.toStringList(notLogRecordMap.get(cookieId));
			List<SysAccessRecord> value = frequency.get(num);
			if (value != null) {
				value.addAll(newValue);
			} else {
				frequency.put(num, newValue);
			}
		}
		return frequency;
	}

	/**
	 * 将key为string的map转化成list
	 */
	private List<SysAccessRecord> toStringList(Map<String, List<SysAccessRecord>> recordMap) {
		List<SysAccessRecord> records = new ArrayList<SysAccessRecord>();
		for (String str : recordMap.keySet()) {
			records.addAll(recordMap.get(str));
		}
		return records;
	}

	/**
	 * 将key为Integer的map转化成list
	 */
	private List<SysAccessRecord> toIntegerList(Map<Integer, List<SysAccessRecord>> recordMap) {
		List<SysAccessRecord> records = new ArrayList<SysAccessRecord>();
		for (Integer i : recordMap.keySet()) {
			records.addAll(recordMap.get(i));
		}
		return records;
	}

	private int receiveIp(List<SysAccessRecord> records) {
		records = records.stream().filter(record -> record.getAccessIp() != null).collect(Collectors.toList());
		int size = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).keySet().size();
		return size;
	}


	@Override
	public StatisticsFlowVo getFlow(StatisticsFlowDto dto) {
		List<String> dates = new ArrayList<String>();
		List<Integer> pvs = new ArrayList<Integer>();
		List<Integer> uvs = new ArrayList<Integer>();
		List<Integer> ips = new ArrayList<Integer>();
		List<BigDecimal> depths = new ArrayList<BigDecimal>();
		List<Integer> times = new ArrayList<Integer>();
		List<StatisticsFlowListVo> flowListVos = new ArrayList<StatisticsFlowListVo>();
		StatisitcsOverviewVo vo = null;
		Long startTime = MyDateUtils.getStartDate(new Date()).getTime();
		Long endTime = MyDateUtils.getFinallyDate(new Date()).getTime();
		Long minTime = dto.getMinTime().getTime();
		if (minTime >= startTime && minTime <= endTime) {
			dto.setMinTime(MyDateUtils.getStartDate(new Date()));
			dto.setMaxTime(new Date());
			List<SysAccessRecord> records = sysAccessRecordService.getFlow(dto);
			// 大于小时分组说明使用day天数分组
			if (dto.getShowStyle() > StatisticsFlow.SHOW_HOUR) {
				dates.add(MyDateUtils.formatDate(new Date()));
				StatisticsFlowListVo flowListVo = new StatisticsFlowListVo(1, MyDateUtils.formatDate(new Date()));
				this.splicRealTimeFlowImage(records, dates, pvs, uvs, ips, depths, times, false);
				this.splicRealTimeFlowList(records, flowListVo, flowListVos, false);
			} else {
				@SuppressWarnings("deprecation")
				Map<Integer, List<SysAccessRecord>> snapRecordMap = records.parallelStream()
						.collect(Collectors.groupingBy(o -> o.getCreateTime().getHours()));
				Map<Integer, List<SysAccessRecord>> recordMap = new LinkedHashMap<Integer, List<SysAccessRecord>>();
				snapRecordMap.entrySet().stream().sorted(Map.Entry.<Integer, List<SysAccessRecord>>comparingByKey())
						.forEachOrdered(e -> recordMap.put(e.getKey(), e.getValue()));
				Integer hourNum = MyDateUtils.getDiffIntHourTwoDate(MyDateUtils.getSpecficDateStart(new Date(), 0),
						new Date());
				for (int i = 0; i <= hourNum; i++) {
					List<SysAccessRecord> snapRecords = recordMap.get(i);
					dates.add(String.valueOf(i));
					StatisticsFlowListVo flowListVo = new StatisticsFlowListVo(i, this.hourToString(i));
					boolean isEnd = i == hourNum ? true : false;
					this.splicRealTimeFlowImage(snapRecords, dates, pvs, uvs, ips, depths, times, isEnd);
					this.splicRealTimeFlowList(snapRecords, flowListVo, flowListVos, isEnd);
				}
			}
		} else {
			/** 这里就是通过中间表取数据了，中间表中的规则是今天统计昨天的数据*/
			dto.setMinTime(MyDateUtils.getSpecficDateStart(dto.getMinTime(), 1));
			dto.setMaxTime(MyDateUtils.getSpecficDateEnd(dto.getMaxTime(), 1));

			List<StatisticsFlow> flows = dao.getFlow(dto);

			if (flows != null && flows.size() > 0) {
				Map<String, List<StatisticsFlow>> flowMap = new LinkedHashMap<String, List<StatisticsFlow>>();
				Map<Date, Date> dateMap = null;
				int i = 1;
				switch (dto.getShowStyle()) {
					case StatisticsFlow.SHOW_HOUR:
						Map<Integer, List<StatisticsFlow>> snapflowHourMap = flows.parallelStream()
								.collect(Collectors.groupingBy(o -> o.getStatisticsHour()));
						snapflowHourMap.entrySet().stream().sorted(Map.Entry.<Integer, List<StatisticsFlow>>comparingByKey()).forEachOrdered(e -> flowMap.put(String.valueOf(e.getKey()), e.getValue()));
						for (String str : flowMap.keySet()) {
							dates.add(str);
							List<StatisticsFlow> snapflows = flowMap.get(str);
							this.splicFlowImage(snapflows, dates, pvs, uvs, ips, depths, times);
							StatisticsFlowListVo flowListVo = new StatisticsFlowListVo(i, this.hourToString(Integer.valueOf(str)));
							this.splicFlowList(snapflows, flowListVo, flowListVos);
							i++;
						}
						break;
					case StatisticsFlow.SHOW_DAY:
						Map<String, List<StatisticsFlow>> snapflowDayMap = flows.parallelStream()
								.collect(Collectors.groupingBy(o -> o.getStatisticsDay()));
						snapflowDayMap.entrySet().stream().sorted(Map.Entry.<String, List<StatisticsFlow>>comparingByKey()).forEachOrdered(e -> flowMap.put(e.getKey(), e.getValue()));
						i = 1;
						for (String str : flowMap.keySet()) {
							dates.add(str);
							List<StatisticsFlow> snapflows = flowMap.get(str);
							this.splicFlowImage(snapflows, dates, pvs, uvs, ips, depths, times);
							StatisticsFlowListVo flowListVo = new StatisticsFlowListVo(i, str);
							this.splicFlowList(snapflows, flowListVo, flowListVos);
							i++;
						}
						break;
					case StatisticsFlow.SHOW_WEEK:
						dateMap = this.groupByWeek(dto.getMinTime(), dto.getMaxTime());
						for (Date date : dateMap.keySet()) {
							Date value = dateMap.get(date);
							List<StatisticsFlow> snapflows = flows.stream().filter(s -> s.getCreateTime().getTime() <= value.getTime() && s.getCreateTime().getTime() >= date.getTime()).collect(Collectors.toList());
							String key1 = MyDateUtils.formatDate(date);
							String key2 = MyDateUtils.formatDate(value);
							flowMap.put(key1 + " " + key2, snapflows);
						}
						for (String str : flowMap.keySet()) {
							dates.add(str);
							List<StatisticsFlow> snapflows = flowMap.get(str);
							this.splicFlowImage(snapflows, dates, pvs, uvs, ips, depths, times);
						}
						Map<String, List<StatisticsFlow>> newSnapflowDayMap = flows.parallelStream()
								.collect(Collectors.groupingBy(o -> o.getStatisticsDay()));
						Map<String, List<StatisticsFlow>> newFlowMap = new LinkedHashMap<String, List<StatisticsFlow>>();
						newSnapflowDayMap.entrySet().stream().sorted(Map.Entry.<String, List<StatisticsFlow>>comparingByKey()).forEachOrdered(e -> newFlowMap.put(e.getKey(), e.getValue()));
						i = 1;
						for (String str : newFlowMap.keySet()) {
							List<StatisticsFlow> snapflows = newFlowMap.get(str);
							StatisticsFlowListVo flowListVo = new StatisticsFlowListVo(i, str);
							this.splicFlowList(snapflows, flowListVo, flowListVos);
							i++;
						}
						break;
					case StatisticsFlow.SHOW_MONTH:
						dateMap = this.groupByMonth(dto.getMinTime(), dto.getMaxTime());
						for (Date date : dateMap.keySet()) {
							Date value = dateMap.get(date);
							List<StatisticsFlow> snapflows = flows.stream().filter(s -> s.getCreateTime().getTime() <= value.getTime() && s.getCreateTime().getTime() >= date.getTime()).collect(Collectors.toList());
							String key1 = MyDateUtils.formatDate(date);
							String key2 = MyDateUtils.formatDate(value);
							flowMap.put(key1 + " " + key2, snapflows);
						}
						for (String str : flowMap.keySet()) {
							dates.add(str);
							List<StatisticsFlow> snapflows = flowMap.get(str);
							this.splicFlowImage(snapflows, dates, pvs, uvs, ips, depths, times);
						}
						Map<String, List<StatisticsFlow>> resetSnapflowDayMap = flows.parallelStream()
								.collect(Collectors.groupingBy(o -> o.getStatisticsDay()));
						Map<String, List<StatisticsFlow>> resetNewFlowMap = new LinkedHashMap<String, List<StatisticsFlow>>();
						resetSnapflowDayMap.entrySet().stream().sorted(Map.Entry.<String, List<StatisticsFlow>>comparingByKey()).forEachOrdered(e -> resetNewFlowMap.put(e.getKey(), e.getValue()));
						i = 1;
						for (String str : resetNewFlowMap.keySet()) {
							List<StatisticsFlow> snapflows = resetNewFlowMap.get(str);
							StatisticsFlowListVo flowListVo = new StatisticsFlowListVo(i, str);
							this.splicFlowList(snapflows, flowListVo, flowListVos);
							i++;
						}
						break;
					default:
						break;
				}
			} else {
				// 
				this.initImageVo(dates, pvs, uvs, ips, depths, times, dto.getShowStyle(), dto.getMinTime(), dto.getMaxTime());
			}
		}
		
		if (dto.getSortTerm() != null && dto.getOrder() != null) {
			flowListVos = this.sort(flowListVos, dto.getSortTerm(), dto.getOrder());
		} else {
			flowListVos = this.sort(flowListVos, StatisticsFlow.SORT_DATE, false);
		}
		vo = this.initTotal(pvs, uvs, ips, depths, times);
		JSONArray array = new JSONArray();
		for (int j = 0; j < dates.size(); j++) {
			JSONObject object = new JSONObject();
			object.put(StatisticsConstant.FIELD_NAME_DATE, dates.get(j));
			if (dto.getShowStyle() == StatisticsFlow.SHOW_HOUR) {
				object.put(StatisticsConstant.FIELD_NAME_SHOW_TIME, String.valueOf(j));
			} else {
				object.put(StatisticsConstant.FIELD_NAME_SHOW_TIME, dates.get(j));
			}
			switch (dto.getShowType()) {
				case StatisticsConstant.SHOW_FILE_PV:
					object.put(StatisticsConstant.FIELD_NAME_PV, pvs.get(j));
					break;
				case StatisticsConstant.SHOW_FILE_UV:
					object.put(StatisticsConstant.FIELD_NAME_UV, uvs.get(j));
					break;
				case StatisticsConstant.SHOW_FILE_IP:
					object.put(StatisticsConstant.FIELD_NAME_IP, ips.get(j));
					break;
				case StatisticsConstant.SHOW_FILE_DEPTH:
					object.put(StatisticsConstant.FIELD_NAME_DEPTH, depths.get(j));
					break;
				case StatisticsConstant.SHOW_FILE_TIMES:
					object.put(StatisticsConstant.FIELD_NAME_TIMES, times.get(j));
					break;
				default:
					break;
			}
			array.add(object);
		}
		StatisticsFlowImageVo imageVo = new StatisticsFlowImageVo(array, vo);
		return new StatisticsFlowVo(flowListVos, imageVo);
	}

	private StatisitcsOverviewVo initTotal(List<Integer> pvs, List<Integer> uvs, List<Integer> ips, List<BigDecimal> depths, List<Integer> times) {
		Integer pv = pvs.stream().collect(Collectors.summingInt(s -> s));
		Integer uv = uvs.stream().collect(Collectors.summingInt(s -> s));
		Integer ip = ips.stream().collect(Collectors.summingInt(s -> s));
		BigDecimal decimal = new BigDecimal(0);
		for (BigDecimal depth : depths) {
			decimal = decimal.add(depth);
		}
		if (decimal.intValue() > 0) {
			decimal = decimal.divide(new BigDecimal(depths.size()), BigDecimal.ROUND_HALF_UP);
		}
		Integer time = times.stream().collect(Collectors.summingInt(s -> s));
		if (time > 0) {
			time = time / times.size();
		}
		StatisitcsOverviewVo vo = new StatisitcsOverviewVo(pv, uv, ip, MathUtil.formatScaleCast(decimal, 4), time);
		return vo;
	}

	private void initImageVo(List<String> dates, List<Integer> pvs, List<Integer> uvs, List<Integer> ips, List<BigDecimal> depths, List<Integer> times, Short showStyle, Date minTime, Date maxTime) {
		minTime = MyDateUtils.getSpecficDateStart(minTime, -1);
		maxTime = MyDateUtils.getSpecficDateEnd(maxTime, -1);
		Map<Date, Date> dateMap = null;
		String dateTime = null;
		switch (showStyle) {
			case StatisticsFlow.SHOW_HOUR:
				for (int i = 0; i < 24; i++) {
					dates.add(this.hourToString(i));
				}
				break;
			case StatisticsFlow.SHOW_DAY:
				dateMap = this.groupByDay(minTime, maxTime);
				for (Date date : dateMap.keySet()) {
					dates.add(MyDateUtils.formatDate(date));
				}
				break;
			case StatisticsFlow.SHOW_WEEK:
				dateMap = this.groupByWeek(minTime, maxTime);
				for (Date date : dateMap.keySet()) {
					dateTime = MyDateUtils.formatDate(date) + " - " + MyDateUtils.formatDate(dateMap.get(date));
					dates.add(dateTime);
				}
				break;
			case StatisticsFlow.SHOW_MONTH:
				dateMap = this.groupByMonth(minTime, maxTime);
				for (Date date : dateMap.keySet()) {
					dateTime = MyDateUtils.formatDate(date) + " - " + MyDateUtils.formatDate(dateMap.get(date));
					dates.add(dateTime);
				}
				break;
			default:
				break;
		}
		for (int i = 0; i < dates.size(); i++) {
			pvs.add(0);
			uvs.add(0);
			ips.add(0);
			depths.add(new BigDecimal(0));
			times.add(0);
		}

	}


	public Map<Date, Date> groupByDay(Date startDate, Date endDate) {
		Map<Date, Date> dateMap = new LinkedHashMap<Date, Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (true) {
			cal.add(Calendar.DATE, 0);
			if (cal.getTime().getTime() < endDate.getTime()) {
				dateMap.put(startDate, null);
			} else {
				break;
			}
			cal.add(Calendar.DATE, 1);
			startDate = cal.getTime();
		}
		return dateMap;
	}

	private List<StatisticsFlowListVo> sort(List<StatisticsFlowListVo> vos, Short sortTerm, Boolean order) {
		switch (sortTerm) {
			case StatisticsFlow.SORT_DATE:
				if (!order) {
					Collections.reverse(vos);
				}
				break;
			case StatisticsFlow.SORT_PV:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getPvNum)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getPvNum).reversed()).collect(Collectors.toList());
				}
				break;
			case StatisticsFlow.SORT_UV:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getUvNum)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getUvNum).reversed()).collect(Collectors.toList());
				}
				break;
			case StatisticsFlow.SORT_IP:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getIpNum)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getIpNum).reversed()).collect(Collectors.toList());
				}
				break;
			case StatisticsFlow.SORT_DEPTH:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getDepthNum)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getDepthNum).reversed()).collect(Collectors.toList());
				}
				break;
			case StatisticsFlow.SORT_TIME_NUM:
				if (order) {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getTimeNum)).collect(Collectors.toList());
				} else {
					vos = vos.stream().sorted(Comparator.comparing(StatisticsFlowListVo::getTimeNum).reversed()).collect(Collectors.toList());
				}
				break;
			default:
				break;
		}
		return vos;
	}

	private void splicFlowImage(List<StatisticsFlow> snapflows, List<String> dates, List<Integer> pvs, List<Integer> uvs, List<Integer> ips, List<BigDecimal> depths, List<Integer> times) {
		Integer pv = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getPvs));
		pvs.add(pv);
		Integer uv = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getUvs));
		uvs.add(uv);
		Integer ip = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getIps));
		ips.add(ip);
		Double depthNum = Double.valueOf(snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getOnlyOnePv)) + "");
		BigDecimal depth = pv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(depthNum / Double.valueOf(pv + "")), 4);
		depths.add(depth);
		Integer timeNum = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getAccessHoureLong));
		;
		Integer sissionNum = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getSession));
		times.add(sissionNum == 0 ? 0 : timeNum / sissionNum);
	}


	private void splicFlowList(List<StatisticsFlow> snapflows, StatisticsFlowListVo flowListVo, List<StatisticsFlowListVo> flowListVos) {
		Integer pv = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getPvs));
		Integer uv = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getUvs));
		Integer ip = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getIps));
		Double depthNum = Double.valueOf(snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getOnlyOnePv)) + "");
		// 如果ip不为0，pv为0，这是不可能的
		BigDecimal depth = pv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(depthNum / Double.valueOf(pv + "")), 4);
		Integer timeNum = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getAccessHoureLong));

		flowListVo.setPvNum(pv);
		flowListVo.setUvNum(uv);
		flowListVo.setIpNum(ip);
		flowListVo.setDepthNum(depth);
		Integer sissionNum = snapflows.stream().collect(Collectors.summingInt(StatisticsFlow::getSession));
		flowListVo.setTimeNum(sissionNum == 0 ? 0 : timeNum / sissionNum);
		flowListVos.add(flowListVo);
	}

	private void splicRealTimeFlowList(List<SysAccessRecord> records, StatisticsFlowListVo flowListVo, List<StatisticsFlowListVo> flowListVos, boolean isEnd) {
		Integer pv = records == null ? 0 : records.size();
		Integer uv = records == null ? 0 : this.receiveUv(records);
		BigDecimal depth = null;
		Map<Integer, List<SysAccessRecord>> durationMap = null;
		Integer timeNum = 0;
		Integer ip = records == null ? 0 : this.receiveIp(records);
		String depthMapValue = null;
		if (records != null && records.size() > 0 && this.receiveDepth(records).get(0) != null) {
			depthMapValue = this.receiveDepth(records).get(0).size() + "";
		}
		Double depthNum = depthMapValue == null ? 0 : Double.valueOf(depthMapValue);
		depth = pv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(depthNum / Double.valueOf(pv + "")), 4);
		durationMap = records == null ? new LinkedHashMap<Integer, List<SysAccessRecord>>() : this.receiveDuration(records, false,true);
		for (Integer time : durationMap.keySet()) {
			timeNum += time;
		}
		flowListVo.setPvNum(pv);
		flowListVo.setUvNum(uv);
		flowListVo.setIpNum(ip);
		if (isEnd) {
			flowListVo.setDepthNum(new BigDecimal(0));
			flowListVo.setTimeNum(0);
		} else {
			flowListVo.setDepthNum(depth);
			Integer sessionNum = this.receiveSessionNum(records);
			flowListVo.setTimeNum(sessionNum == 0 ? 0 : timeNum / sessionNum);
		}
		flowListVos.add(flowListVo);
	}

	private int receiveSessionNum(List<SysAccessRecord> records) {
		int sessionNum = 0;
		if (records != null && records.size() > 0) {
			// TODO 计算出正确的session的数量，方便处理平均时长
			records = records.stream().filter(record -> record.getSessionId() != null).collect(Collectors.toList());
			if (records != null && records.size() > 0) {
				Map<String,List<SysAccessRecord>> recordMap = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
				List<String> deleteSession = new ArrayList<String>();
				for (String session : recordMap.keySet()) {
					if (recordMap.get(session).size() == 1) {
						deleteSession.add(session);
					}
				}
				for (String session : deleteSession) {
					recordMap.remove(session);
				}
				sessionNum = recordMap.keySet().size();
			}
		}
		return sessionNum;
	}

	private void splicRealTimeFlowImage(List<SysAccessRecord> records, List<String> dates, List<Integer> pvs, List<Integer> uvs, List<Integer> ips, List<BigDecimal> depths, List<Integer> times, boolean isEnd) {
		Integer pv = 0;
		Integer uv = 0;
		BigDecimal depth = null;
		Map<Integer, List<SysAccessRecord>> durationMap = null;
		Integer timeNum = 0;
		Integer ip = 0;

		pv = records == null ? 0 : records.size();
		pvs.add(pv);
		uv = records == null ? 0 : this.receiveUv(records);
		uvs.add(uv);
		ip = records == null ? 0 : this.receiveIp(records);
		ips.add(ip);
		String depthMapValue = null;
		if (records != null && records.size() > 0 && this.receiveDepth(records).get(1) != null) {
			depthMapValue = this.receiveDepth(records).get(1).size() + "";
		}
		Double depthNum = depthMapValue == null ? 0 : Double.valueOf(depthMapValue);
		depth = pv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(depthNum / Double.valueOf(pv + "")), 4);
		depths.add(depth);
		durationMap = records == null ? new LinkedHashMap<Integer, List<SysAccessRecord>>() : this.receiveDuration(records, false,true);
		for (Integer time : durationMap.keySet()) {
			timeNum += time;
		}
		Integer sessionNum = this.receiveSessionNum(records);
		times.add(sessionNum == 0 ? 0 : timeNum / sessionNum);

	}


	@SuppressWarnings("deprecation")
	private Map<Date, Date> groupByMonth(Date startDate, Date endDate) {
		Map<Date, Date> dateMap = new LinkedHashMap<Date, Date>();

		Date firstDay = null;
		Date lastDay = null;

		Calendar dd = Calendar.getInstance();// 定义日期实例
		dd.setTime(startDate);// 设置日期起始时间
		Calendar cale = Calendar.getInstance();

		Calendar c = Calendar.getInstance();
		c.setTime(endDate);

		int startDay = startDate.getDate();
		int endDay = endDate.getDate();
		while (dd.getTime().before(endDate)) {// 判断是否到结束日期
			cale.setTime(dd.getTime());
			if (dd.getTime().equals(startDate)) {
				cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
				lastDay = cale.getTime();
				dateMap.put(startDate, lastDay);
			} else if (dd.get(Calendar.MONTH) == endDate.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
				cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
				firstDay = cale.getTime();
				dateMap.put(firstDay, endDate);

			} else {
				cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
				firstDay = cale.getTime();
				cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
				lastDay = cale.getTime();
				dateMap.put(firstDay, lastDay);
			}
			dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

		}
		if (endDay < startDay) {
			cale.setTime(endDate);
			cale.set(Calendar.DAY_OF_MONTH, 1);// 取第一天
			firstDay = cale.getTime();
			dateMap.put(firstDay, endDate);
		}
		return dateMap;

	}


	private String hourToString(Integer hour) {
		return hour + ":00 - " + hour + ":59:59";
	}

	@Override
	public StatisitcsOverviewVos getOverviewVos(Integer siteId) {

		Date endTime = new Date();
		Date startTime = MyDateUtils.getSpecficDateStart(endTime, 0);
		List<SysAccessRecord> records = sysAccessRecordService.findByDate(startTime, endTime, siteId);
		Integer pv = records == null ? 0 : records.size();
		Integer uv = records == null ? 0 : this.receiveUv(records);
		Integer ip = records == null ? 0 : this.receiveIp(records);

		String depthMapValue = null;
		if (records != null && records.size() > 0 && this.receiveDepth(records).get(0) != null) {
			depthMapValue = this.receiveDepth(records).get(0).size() + "";
		}
		Double depthNum = depthMapValue == null ? 0 : Double.valueOf(depthMapValue);

		BigDecimal depth = pv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(depthNum / Double.valueOf(pv + "")), 4);
		Map<Integer, List<SysAccessRecord>> durationMap = null;
		Integer timeNum = 0;

		durationMap = records == null ? new LinkedHashMap<Integer, List<SysAccessRecord>>() : this.receiveDuration(records, false,true);
		for (Integer time : durationMap.keySet()) {
			timeNum += time;
		}
		Integer sessionNum = this.receiveSessionNum(records);
		StatisitcsOverviewVo now = new StatisitcsOverviewVo(pv, uv, ip, depth, sessionNum == 0 ? 0 : timeNum / sessionNum);
		List<StatisticsFlow> flows = dao.findBySiteId(siteId);
		List<StatisticsFlow> yesterdayFlows = null;
		// 如果昨天的数据还没有产生那么直接返回null
		StatisitcsOverviewVo yesterday = null;
		StatisitcsOverviewVo highest = null;
		if (flows != null && flows.size() > 0) {
			yesterdayFlows = flows.stream().filter(flow -> flow.getCreateTime().getTime() >= startTime.getTime() && flow.getCreateTime().getTime() <= endTime.getTime()).collect(Collectors.toList());
			if (yesterdayFlows != null && yesterdayFlows.size() > 0) {
				pv = yesterdayFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getPvs));
				uv = yesterdayFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getUvs));
				ip = yesterdayFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getIps));
				Double yesterDepthNum = Double.valueOf(yesterdayFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getOnlyOnePv)) + "");
				BigDecimal yesterDepth = pv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(yesterDepthNum / Double.valueOf(pv + "")), 4);
				Integer yesterTimeNum = yesterdayFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getAccessHoureLong));
				;
				sessionNum = yesterdayFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getSession));
				yesterday = new StatisitcsOverviewVo(pv, uv, ip, yesterDepth, sessionNum == 0 ? 0 : yesterTimeNum / sessionNum);
			} else {
				yesterday = new StatisitcsOverviewVo(0, 0, 0, new BigDecimal(0), 0);
			}

			Map<String, List<StatisticsFlow>> flowMap = flows.stream().collect(Collectors.groupingBy(StatisticsFlow::getStatisticsDay));
			List<Integer> pvs = new ArrayList<Integer>();
			List<Integer> uvs = new ArrayList<Integer>();
			List<Integer> ips = new ArrayList<Integer>();
			List<BigDecimal> depths = new ArrayList<BigDecimal>();
			List<Integer> times = new ArrayList<Integer>();
			for (String str : flowMap.keySet()) {
				List<StatisticsFlow> snapFlows = flowMap.get(str);
				Integer snapPv = snapFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getPvs));
				pvs.add(snapPv);
				uvs.add(snapFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getUvs)));
				ips.add(snapFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getIps)));
				Integer snapDepth = snapFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getOnlyOnePv));
				depths.add(snapPv == 0 ? new BigDecimal(0) : MathUtil.formatScaleCast(new BigDecimal(snapDepth / Double.valueOf(snapPv + "")), 6));
				sessionNum = snapFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getSession));
				times.add(sessionNum == 0 ? 0 : snapFlows.stream().collect(Collectors.summingInt(StatisticsFlow::getAccessHoureLong)) / sessionNum);
			}
			highest = new StatisitcsOverviewVo(this.receiveMaxList(pvs), this.receiveMaxList(uvs), this.receiveMaxList(ips), this.receiveMaxBigDecimals(depths), this.receiveMaxList(times));
		} else {
			yesterday = new StatisitcsOverviewVo(0, 0, 0, new BigDecimal(0), 0);
			highest = new StatisitcsOverviewVo(0, 0, 0, new BigDecimal(0), 0);
		}

		StatisitcsOverviewVos vos = new StatisitcsOverviewVos(now, yesterday, highest);
		return vos;
	}

	private BigDecimal receiveMaxBigDecimals(List<BigDecimal> bigs) {
		BigDecimal max = bigs.stream().max(new Comparator<BigDecimal>() {
			@Override
			public int compare(BigDecimal o1, BigDecimal o2) {
				return o1.compareTo(o2);
			}
		}).get();
		return max;
	}

	private int receiveMaxList(List<Integer> list) {
		int max = list.stream().max(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		}).get();

		return max;
	}

	@Override
	public void save() throws GlobalException {
		Date beginTime = MyDateUtils.getSpecficDateStart(new Date(), -1);
		Date endTime = MyDateUtils.getFinallyDate(beginTime);
		List<StatisticsFlow> flows = new ArrayList<StatisticsFlow>();
		List<SysAccessRecord> records = sysAccessRecordService.getSource(beginTime, endTime, null, null, null, null);
		String statisticsDay = MyDateUtils.formatDate(beginTime);
		if (records != null && records.size() > 0) {
			// 对siteId进行分组
			records = records.stream().filter(record -> record.getSiteId() != null).collect(Collectors.toList());
			Map<Integer, List<SysAccessRecord>> siteMap = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getSiteId));
			for (Integer siteId : siteMap.keySet()) {
				// 对来源网站类型分组
				List<SysAccessRecord> siteList = siteMap.get(siteId);
				siteList = siteList.stream().filter(record -> record.getSorceUrlType() != null).collect(Collectors.toList());
				Map<Integer, List<SysAccessRecord>> sourceUrlTypeMap = siteList.stream().collect(Collectors.groupingBy(SysAccessRecord::getSorceUrlType));
				for (Integer sourceUrlTypeRecord : sourceUrlTypeMap.keySet()) {
					if (SysAccessRecord.RESOURCE_SEARCHER.equals(sourceUrlTypeRecord)) {
						List<SysAccessRecord> sourceUrlTypeRecordList = sourceUrlTypeMap.get(sourceUrlTypeRecord);
						sourceUrlTypeRecordList = sourceUrlTypeRecordList.stream().filter(record -> record.getEngineName() != null).collect(Collectors.toList());
						Map<String,List<SysAccessRecord>> engineNameMap = sourceUrlTypeRecordList.stream().collect(Collectors.groupingBy(SysAccessRecord::getEngineName));
						for (String engineName : engineNameMap.keySet()) {
							List<SysAccessRecord> engineNameList = engineNameMap.get(engineName);
							flows = this.initFlow(engineNameList, flows, sourceUrlTypeRecord, siteId, statisticsDay,engineName);
						}
					} else {
						flows = this.initFlow(sourceUrlTypeMap.get(sourceUrlTypeRecord), flows, sourceUrlTypeRecord, siteId, statisticsDay,null);
					}
				}
			}
		}
		if (flows.size() > 0) {
			Map<Integer, List<StatisticsFlow>> flowMap = flows.stream().collect(Collectors.groupingBy(StatisticsFlow::getSiteId));
			for (Integer siteId : flowMap.keySet()) {
				List<Integer> hours = flowMap.get(siteId).stream().map(StatisticsFlow::getStatisticsHour).collect(Collectors.toList());
				for (int i = 0; i < 24; i++) {
					if (!hours.contains(i)) {
						StatisticsFlow flow = new StatisticsFlow(siteId, statisticsDay, 0, false, 1, null, null, null, i, 0, 0, 0, 0, 0, 0,null);
						flows.add(flow);
					}
				}
			}
			super.saveAll(flows);
		}
	}


	private List<StatisticsFlow> initFlow(List<SysAccessRecord> sourceUrlTypeList,List<StatisticsFlow> flows,Integer sourceUrlTypeRecord,Integer siteId,String statisticsDay,String engineName) {
		// 对是否新用户分组
		sourceUrlTypeList = sourceUrlTypeList.stream().filter(record -> record.getNewVisitor() != null).collect(Collectors.toList());
		Map<Boolean, List<SysAccessRecord>> newVisitorMap = sourceUrlTypeList.stream().collect(Collectors.groupingBy(SysAccessRecord::getNewVisitor));
		for (Boolean newVisitor : newVisitorMap.keySet()) {
			// 对设备类型(1.计算机、2.移动设备)分组
			List<SysAccessRecord> newVisitorList = newVisitorMap.get(newVisitor);
			newVisitorList = newVisitorList.stream().filter(record -> record.getDeviceType() != null).collect(Collectors.toList());
			Map<Short, List<SysAccessRecord>> deviceTypeMap = newVisitorList.stream().collect(Collectors.groupingBy(SysAccessRecord::getDeviceType));
			for (Short deviceType : deviceTypeMap.keySet()) {
				// 对访客设备系统进行分组
				List<SysAccessRecord> deviceTypeList = deviceTypeMap.get(deviceType);
				deviceTypeList = deviceTypeList.stream().filter(record -> record.getAccessDevice() != null).collect(Collectors.toList());
				Map<String, List<SysAccessRecord>> accessDeviceTypeMap = deviceTypeList.stream().collect(Collectors.groupingBy(SysAccessRecord::getAccessDevice));
				for (String accessDevice : accessDeviceTypeMap.keySet()) {
					// 对城市分组(相当于同时对省份进行了分组)
					List<SysAccessRecord> accessDeviceList = accessDeviceTypeMap.get(accessDevice);
					accessDeviceList = accessDeviceList.stream().filter(record -> record.getAccessCity() != null).collect(Collectors.toList());
					Map<String, List<SysAccessRecord>> accessCityMap = accessDeviceList.stream().collect(Collectors.groupingBy(SysAccessRecord::getAccessCity));
					for (String city : accessCityMap.keySet()) {
						List<SysAccessRecord> accessCityList = accessCityMap.get(city);
						@SuppressWarnings("deprecation")
						Map<Integer, List<SysAccessRecord>> hourMap = accessCityList.parallelStream()
								.collect(Collectors.groupingBy(o -> o.getCreateTime().getHours()));
						for (Integer hour : hourMap.keySet()) {
							List<SysAccessRecord> hourList = hourMap.get(hour);
							Integer pvs = hourList.size();
							Integer uvs = this.receiveUv(hourList);
							Integer ips = this.receiveIp(hourList);
							Integer accessHoureLong = this.receiveHoureLong(hourList);
							Map<Integer, List<SysAccessRecord>> onePvMap = this.receiveDepth(hourList);
							Integer onlyOnePv = 0;
							if (onePvMap != null && onePvMap.get(0) != null) {
								onlyOnePv = onePvMap.get(0).size();
							}
							StatisticsFlow flow = new StatisticsFlow(siteId, statisticsDay, sourceUrlTypeRecord, newVisitor, Integer.valueOf(deviceType), accessDevice, hourList.get(0).getAccessProvince(), city, hour, pvs, uvs, ips, accessHoureLong, onlyOnePv, this.receiveSessionNum(hourList),engineName);
							flows.add(flow);
						}
					}
				}
			}
		}
		return flows;
	}
		
	private int receiveHoureLong(List<SysAccessRecord> records) {
		Integer houreLong = 0;
		records = records.stream().filter(record -> record.getSessionId() != null).collect(Collectors.toList());
		if (records != null && records.size() > 0) {

			Map<String, List<SysAccessRecord>> recordMap = records.stream().collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
			for (String session : recordMap.keySet()) {
				List<SysAccessRecord> snapRecords = recordMap.get(session);
				snapRecords = snapRecords.stream().sorted(Comparator.comparing(SysAccessRecord::getCreateTime)).collect(Collectors.toList());
				Long minTime = snapRecords.get(0).getCreateTime().getTime();
				Long maxTime = snapRecords.get(snapRecords.size() - 1).getCreateTime().getTime();
				Long time = (maxTime - minTime) / 1000;
				Integer timeValue = time.intValue();
				houreLong += timeValue;
			}
		}
		return houreLong;
	}

	/**
	访客数（UV）：当前站点下，
	有user_id，以user_id分组，统计user_id数量，记为：C1；
	无user_id，以cookie分组，统计cookie数量，记为：C2；
	访客数（UV）= C1+C2。**/
	@Override
	public Integer uvNumber(List<SysAccessRecord> records) throws GlobalException {
		Integer c1 = 0;
		Integer c2 = 0;
		if (records.isEmpty()) {
			return 0;
		}
		c1 = records.stream().filter(o -> o.getLoginUserId() != null)
				.collect(Collectors.groupingBy(SysAccessRecord::getLoginUserId)).size();
		c2 = records.stream().filter(o -> o.getLoginUserId() == null)
		.filter(o -> StringUtils.isNotBlank(o.getCookieId()))
		.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
		return c1 + c2;
	}

}