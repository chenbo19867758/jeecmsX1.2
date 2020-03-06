/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.statistics.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.dao.StaAccessDao;
import com.jeecms.statistics.domain.StatisticsAccess;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.vo.AccessVo;
import com.jeecms.statistics.domain.vo.StatisticsVisitorVo;
import com.jeecms.statistics.service.StatisticsAccessService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysAccessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_1;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_10;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_11;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_15;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_16;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_2;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_20;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_21;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_3;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_4;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_49;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_5;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_6;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_7;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_8;
import static com.jeecms.statistics.domain.StatisticsAccess.PAGE_9;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_1;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_2;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_3;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_4;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_5;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_6;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_7;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_8;
import static com.jeecms.statistics.domain.StatisticsAccess.TIME_9;

/**
 * 忠诚度Service
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-06-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsAccessServiceImpl extends BaseServiceImpl<StatisticsAccess, StaAccessDao, Integer>
		implements StatisticsAccessService {

	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private SysAccessRecordService sysAccessRecordService;

	/**
	 * 得到昨天的日期
	 * 
	 * @Title: getDate
	 * @return
	 */
	protected Date getDate() {
		Date date = MyDateUtils.getSpecficDate(new Date(),-1);
		return date;
	}

	@Override
	public void countAnalyze() throws Exception {
		String startDate = MyDateUtils.formatDate(MyDateUtils.getStartDate(getDate()),
				MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
		String endDate = MyDateUtils.formatDate(MyDateUtils.getFinallyDate(getDate()),
				MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
		analyzePage(startDate, endDate);
		analyzeHigh(startDate, endDate);
		analyzeTime(startDate, endDate);
	}

	/**
	 * 得到记录
	 * 
	 * @Title: records
	 * @param start 开始时间
	 * @param end   结束时间
	 * @return
	 */
	private List<SysAccessRecord> records(String start, String end) {
		HashMap<String, String[]> params = new HashMap<String, String[]>(6);
		// 计算时间范围浏览记录
		params.put("GTE_createTime_Timestamp", new String[] { start });
		params.put("LTE_createTime_Timestamp", new String[] { end });
		List<SysAccessRecord> records = sysAccessRecordService.getList(params, null, true);
		return records;
	}

	/**
	 * 忠诚度分析(访问页数)
	 * 
	 * @Title: analyze
	 * @param start 开始时间
	 * @param end   结束时间
	 * @throws Exception 异常
	 * @return: void
	 */
	protected void analyzePage(String start, String end) throws Exception {
		// 得到全部站点的信息
		List<CmsSite> site = cmsSiteService.getList(null, null, true);
		List<StatisticsAccess> accesses = new ArrayList<StatisticsAccess>(100);
		List<SysAccessRecord> records = records(start, end);
		for (CmsSite cmsSite : site) {
			// 排除非站点数据，session为空的数目
			records = records.stream().filter(a -> a.getSiteId().equals(cmsSite.getId()))
					.filter(a -> a.getSessionId() != null).collect(Collectors.toList());
			// session 分组，分别统计同一页面累计
			Map<String, List<SysAccessRecord>> map = records.stream()
					.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
			//遍历session分组的数据
			for (List<SysAccessRecord> list : map.values()) {
				// 来源分组
				Map<Integer, List<SysAccessRecord>> list1 = list.stream()
						.collect(Collectors
								.groupingBy(SysAccessRecord::getSorceUrlType));
				for (Integer sourceType : list1.keySet()) {
					List<SysAccessRecord> list2 = list1.get(sourceType);
					StatisticsAccess record = new StatisticsAccess();
					record.setIsNewVisitor(list.get(0).getNewVisitor());
					record.setSorceUrlType(sourceType);
					record.setVisitorArea(list.get(0).getAccessProvince());
					record.setAccessCount(list2.size());
					record.setStatisticsDay(MyDateUtils.formatDate(getDate()));
					record.setSiteId(cmsSite.getId());
					record.setAccessPage(list.size());
					accesses.add(record);
				}
			}
		}
		super.saveAll(accesses);
	}

	/**
	 * 忠诚度分析(访问深度，需要结合请求地址)
	 * 
	 * @Title: analyze
	 * @param start 开始时间
	 * @param end   结束时间
	 * @throws Exception 异常
	 * @return: void
	 */
	protected void analyzeHigh(String start, String end) throws Exception {
		List<StatisticsAccess> accesses = new ArrayList<StatisticsAccess>(100);
		// 得到全部站点的信息
		List<CmsSite> site = cmsSiteService.getList(null, null, true);
		List<SysAccessRecord> records = records(start, end);
		for (CmsSite cmsSite : site) {
			// 排除session为空的记录以及站点数据
			records = records.stream().filter(a -> a.getSiteId().equals(cmsSite.getId()))
					.filter(a -> a.getSessionId() != null).collect(Collectors.toList());
			// session 分组
			Map<String, List<SysAccessRecord>> map = records.stream()
					.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
			// 统计访问深度,结合请求URL
			for (List<SysAccessRecord> list : map.values()) {
				// 请求网站分组
				Map<String, List<SysAccessRecord>> high = list.stream()
						.collect(Collectors.groupingBy(SysAccessRecord::getAccessUrl));
				for (List<SysAccessRecord> list1 : high.values()) {
					// 来源分组
					Map<Integer, List<SysAccessRecord>> source = list1.stream()
							.collect(Collectors
									.groupingBy(SysAccessRecord::getSorceUrlType));
					//遍历来源，分别统计数量
					for (Integer sourceType : source.keySet()) {
						StatisticsAccess record = new StatisticsAccess();
						record.setIsNewVisitor(list.get(0).getNewVisitor());
						record.setSorceUrlType(sourceType);
						record.setVisitorArea(list.get(0).getAccessProvince());
						record.setAccessCount(source.get(sourceType).size());
						record.setStatisticsDay(MyDateUtils.formatDate(getDate()));
						record.setSiteId(cmsSite.getId());
						record.setDepthPage(high.size());
						accesses.add(record);
					}
				}
			}
		}
		super.saveAll(accesses);
	}
	
	/**
	 * 访问时长分析
	 * session存在1次则为未知
	 * @Title: analyzeTime
	 * @param start 开始时间
	 * @param end   结束时间
	 * @throws Exception 异常
	 * @return: void
	 */
	protected void analyzeTime(String start, String end) throws Exception {
		List<StatisticsAccess> accesses = new ArrayList<StatisticsAccess>(100);
		// 得到全部站点的信息
		List<CmsSite> site = cmsSiteService.getList(null, null, true);
		List<SysAccessRecord> records = records(start, end);
		for (CmsSite cmsSite : site) {
			// 排除session为空的记录以及站点数据
			records = records.stream().filter(a -> a.getSiteId().equals(cmsSite.getId()))
					.filter(a -> a.getSessionId() != null).collect(Collectors.toList());
			// session 分组
			Map<String, List<SysAccessRecord>> map = records.stream()
					.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
			for (List<SysAccessRecord> list : map.values()) {
				//按照创建时间排序
				list = list.stream().sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
					.collect(Collectors.toList());
				// 来源分组
				Map<Integer, List<SysAccessRecord>> source = list.stream()
						.collect(Collectors
								.groupingBy(SysAccessRecord::getSorceUrlType));
				for (Integer sourceType : source.keySet()) {
					StatisticsAccess record = new StatisticsAccess();
					record.setIsNewVisitor(list.get(0).getNewVisitor());
					record.setSorceUrlType(sourceType);
					record.setVisitorArea(list.get(0).getAccessProvince());
					record.setAccessCount(source.get(sourceType).size());
					record.setStatisticsDay(MyDateUtils.formatDate(getDate()));
					record.setSiteId(cmsSite.getId());
					if (list.size() == 1) {
						record.setAccessTime(TIME_9);
					} else {
						//计算时长，根据创建时间相减
						Integer timeInteger = timeDate(list.get(list.size() - 1)
								.getCreateTime(),
								list.get(0).getCreateTime());
						record.setAccessTime(timeInteger);
					}
					accesses.add(record);
				}
			}
		}
		super.saveAll(accesses);
	}
	
	/**
	 * 判断时间段
	* @Title: time 
	* @param start 开始时间
	* @param end 结束时间
	 */
	protected Integer timeDate(Date start, Date end) {
		Long sum = end.getTime() - start.getTime();
		//求绝对值
		sum = Math.abs(sum) / 1000;
		if (0 <= sum && sum <= 9) {
			return TIME_1;
		} else if (sum >= 10 && sum <= 29) {
			return TIME_2;
		} else if (sum >= 30 && sum <= 59) {
			return TIME_3;
		} else if (sum >= 60 && sum <= 180) {
			return TIME_4;
		} else if (sum >= 181 && sum <= 600) {
			return TIME_5;
		} else if (sum >= 601 && sum <= 1800) {
			return TIME_6;
		} else if (sum >= 1801 && sum <= 3600) {
			return TIME_7;
		} else {
			return TIME_8;
		}
	}

	@Override
	public ResponseInfo pageInfo(StatisticsDto dto) throws Exception {
		List<StatisticsVisitorVo> vos = new ArrayList<StatisticsVisitorVo>(10);
		List<AccessVo> accesses = dao.getAccessVo(dto);
		//排序，统计次数倒序
		if (!accesses.isEmpty()) {
			accesses = accesses.stream().sorted(Comparator.comparing(AccessVo::getCount).reversed())
					.collect(Collectors.toList());
		}
		//得到总数
		Integer sum = accesses.stream().map(AccessVo::getCount).reduce(0, Integer::sum);
		for (AccessVo accessVo : accesses) {
			Integer key = accessVo.getKey();
			vos.add(accessPage(key,accessVo.getCount()));
		}
		//排序，按照访问次数降序
		if (!vos.isEmpty()) {
			vos = group(vos,sum).stream()
					.sorted(Comparator.comparing(StatisticsVisitorVo::getValue).reversed())
					.collect(Collectors.toList());
			//排序类型为1访问次数2.所占比例
			vos = sortVos(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("value", vos);
		object.put("sum", sum);
		return new ResponseInfo(object);
	}
	
	/**访问页数拼接数据**/
	protected StatisticsVisitorVo accessPage(Integer key, Integer count) 
			throws IllegalAccessException {
		StatisticsVisitorVo vo = new StatisticsVisitorVo();
		if (key.equals(PAGE_1)) {
			vo.setType("1页");
			vo.setValue(count);
		} else if (key.equals(PAGE_2)) {
			vo.setType("2页");
			vo.setValue(count);
		} else if (key.equals(PAGE_3)) {
			vo.setType("3页");
			vo.setValue(count);
		} else if (key.equals(PAGE_4)) {
			vo.setType("4页");
			vo.setValue(count);
		} else if (PAGE_5 <= key && key <= PAGE_10) {
			vo.setType("5-10页");
			vo.setValue(count);
		} else if (PAGE_11 <= key && key <= PAGE_20) {
			vo.setType("11-20页");
			vo.setValue(count);
		} else if (PAGE_21 <= key && key <= PAGE_49) {
			vo.setType("21-49页");
			vo.setValue(count);
		} else {
			vo.setType("50页以上");
			vo.setValue(count);
		}
		return vo;
	}
	
	/**访问页数拼接数据
	 * @throws IllegalAccessException **/
	protected List<StatisticsVisitorVo> group(List<StatisticsVisitorVo> vos, Integer sum) 
			throws IllegalAccessException {
		List<StatisticsVisitorVo> list = new ArrayList<StatisticsVisitorVo>(10);
		Map<String, List<StatisticsVisitorVo>> map = vos.stream()
				.collect(Collectors.groupingBy(StatisticsVisitorVo::getType));
		for (String key : map.keySet()) {
			StatisticsVisitorVo vo = new StatisticsVisitorVo();
			Integer count = map.get(key)
					.stream().map(StatisticsVisitorVo::getValue).reduce(0, Integer::sum);
			vo.setType(key);
			vo.setValue(count);
			vo.setDecimal(MathUtil.div(new BigDecimal(count), 
					new BigDecimal(sum), 4).multiply(new BigDecimal(100)));
			list.add(vo);
		}
		return list;
	}
	

	@Override
	public ResponseInfo highInfo(StatisticsDto dto) throws Exception {
		List<StatisticsVisitorVo> vos = new ArrayList<StatisticsVisitorVo>(10);
		List<AccessVo> accesses = dao.getAccessVo(dto);
		//排序，统计次数倒序
		if (!accesses.isEmpty()) {
			accesses = accesses.stream().sorted(Comparator.comparing(AccessVo::getCount).reversed())
					.collect(Collectors.toList());
		}
		//得到总数
		Integer sum = accesses.stream().map(AccessVo::getCount).reduce(0, Integer::sum);
		for (AccessVo accessVo : accesses) {
			Integer key = accessVo.getKey();
			vos.add(accessHigh(key,accessVo.getCount(),sum));
		}
		//排序，按照访问次数降序
		if (!vos.isEmpty()) {
			vos = group(vos,sum).stream()
					.collect(Collectors.toList());
			//排序类型为1访问次数2.所占比例
			vos = sortVos(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("value", vos);
		object.put("sum", sum);
		return new ResponseInfo(object);
	}
	
	/**访问深度拼接数据**/
	protected StatisticsVisitorVo accessHigh(Integer key, Integer count, Integer sum) 
			throws IllegalAccessException {
		StatisticsVisitorVo vo = new StatisticsVisitorVo();
		if (key.equals(PAGE_1)) {
			vo.setType("1页");
			vo.setValue(count);
		} else if (key.equals(PAGE_2)) {
			vo.setType("2页");
			vo.setValue(count);
		} else if (key.equals(PAGE_3)) {
			vo.setType("3页");
			vo.setValue(count);
		} else if (key.equals(PAGE_4)) {
			vo.setType("4页");
			vo.setValue(count);
		} else if (key.equals(PAGE_5)) {
			vo.setType("5页");
			vo.setValue(count);
		} else if (key.equals(PAGE_6)) {
			vo.setType("6页");
			vo.setValue(count);
		} else if (key.equals(PAGE_7)) {
			vo.setType("7页");
			vo.setValue(count);
		} else if (key.equals(PAGE_8)) {
			vo.setType("8页");
			vo.setValue(count);
		} else if (key.equals(PAGE_9)) {
			vo.setType("9页");
			vo.setValue(count);
		} else if (key.equals(PAGE_10)) {
			vo.setType("10页");
			vo.setValue(count);
		} else if (PAGE_10 < key && key <= PAGE_15) {
			vo.setType("11-15页");
			vo.setValue(count);
		} else if (PAGE_16 <= key && key <= PAGE_20) {
			vo.setType("16-20页");
			vo.setValue(count);
		} else {
			vo.setType("21页以上");
			vo.setValue(count);
		}
		return vo;
	}
	
	@Override
	public ResponseInfo timeInfo(StatisticsDto dto) throws Exception {
		List<AccessVo> accesses = new ArrayList<AccessVo>(10);
		List<StatisticsVisitorVo> vos = new ArrayList<StatisticsVisitorVo>(10);
		accesses = dao.getAccessVo(dto);
		//排序，统计次数倒序
		if (!accesses.isEmpty()) {
			accesses = accesses.stream().sorted(Comparator.comparing(AccessVo::getCount)
					.reversed()).collect(Collectors.toList());
		}
		//得到总数
		Integer sum = accesses.stream().map(AccessVo::getCount).reduce(0, Integer::sum);
		for (AccessVo accessVo : accesses) {
			Integer key = accessVo.getKey();
			vos.add(accessTime(key,accessVo.getCount(),sum));
		}
		//排序，按照访问次数降序
		if (!vos.isEmpty()) {
			vos = group(vos,sum).stream()
					.collect(Collectors.toList());
			//排序类型为1访问次数2.所占比例
			vos = sortVos(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("value", vos);
		object.put("sum", sum);
		return new ResponseInfo(object);
	}
	
	/**访问时长拼接**/
	protected StatisticsVisitorVo accessTime(Integer key, Integer count, Integer sum) 
			throws IllegalAccessException {
		StatisticsVisitorVo vo = new StatisticsVisitorVo();
		if (key.equals(TIME_1)) {
			vo.setType("0-9秒");
			vo.setValue(count);
		} else if (key.equals(TIME_2)) {
			vo.setType("10-29秒");
			vo.setValue(count);
		} else if (key.equals(TIME_3)) {
			vo.setType("30-60秒");
			vo.setValue(count);
		} else if (key.equals(TIME_4)) {
			vo.setType("1-3分钟");
			vo.setValue(count);
		} else if (key.equals(TIME_5)) {
			vo.setType("3-10分钟");
			vo.setValue(count);
		} else if (key.equals(TIME_6)) {
			vo.setType("10-30分钟");
			vo.setValue(count);
		} else if (key.equals(TIME_7)) {
			vo.setType("30-60分钟");
			vo.setValue(count);
		} else if (key.equals(TIME_8)) {
			vo.setType("1小时以上");
			vo.setValue(count);
		} else {
			vo.setType("未知");
			vo.setValue(count);
		}
		return vo;
	}

	@Override
	public ResponseInfo timePage(StatisticsDto dto) throws Exception {
		List<StatisticsVisitorVo> vos = new ArrayList<StatisticsVisitorVo>(10);
		List<SysAccessRecord> records = sysAccessRecordService.haveList(MyDateUtils.getStartDate(new Date()), 
				new Date(),dto.getSiteId(), dto.getSourceType(), dto.getProvince(),dto.getVisitor());
		// 排除session为空的数目
		records = records.stream()
				.filter(a -> a.getSessionId() != null).collect(Collectors.toList());
		Map<String, List<SysAccessRecord>> map = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		//遍历session分组的数据
		for (List<SysAccessRecord> list : map.values()) {
			vos.add(accessPage(list.size(),list.size()));
		}
		//排序，根据排序类型排序
		if (!vos.isEmpty()) {
			//得到总数
			Integer sum = vos.stream().map(StatisticsVisitorVo::getValue).reduce(0, Integer::sum);
			vos = group(vos,sum).stream()
					.collect(Collectors.toList());
			//排序类型为1访问次数2.所占比例
			vos = sortVos(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("value", vos);
		object.put("sum", records.size());
		return new ResponseInfo(object);
	}

	@Override
	public ResponseInfo timeHigh(StatisticsDto dto) throws Exception {
		List<StatisticsVisitorVo> vos = new ArrayList<StatisticsVisitorVo>(10);
		List<SysAccessRecord> records = sysAccessRecordService.haveList(MyDateUtils.getStartDate(new Date()), 
				new Date(),dto.getSiteId(), dto.getSourceType(), dto.getProvince(),dto.getVisitor());
		// 排除session为空的数目
		records = records.stream().filter(a -> a.getSessionId() != null).collect(Collectors.toList());
		Map<String, List<SysAccessRecord>> map = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		//遍历session分组的数据
		for (List<SysAccessRecord> list : map.values()) {
			// 请求网站分组
			Map<String, List<SysAccessRecord>> high = list.stream()
					.collect(Collectors.groupingBy(SysAccessRecord::getAccessUrl));
			vos.add(accessHigh(high.size(),list.size(),records.size()));
		}
		//排序，按照访问次数降序
		if (!vos.isEmpty()) {
			vos = group(vos,records.size()).stream()
					.collect(Collectors.toList());
			//排序类型为1访问次数2.所占比例
			vos = sortVos(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("value", vos);
		object.put("sum", records.size());
		return new ResponseInfo(object);
	}
	
	@Override
	public ResponseInfo time(StatisticsDto dto) throws Exception {
		List<StatisticsVisitorVo> vos = new ArrayList<StatisticsVisitorVo>(10);
		List<SysAccessRecord> records = sysAccessRecordService.haveList(MyDateUtils.getStartDate(new Date()), 
				new Date(),dto.getSiteId(), dto.getSourceType(), dto.getProvince(),dto.getVisitor());
		// 排除session为空的数目
		records = records.stream().filter(a -> a.getSessionId() != null).collect(Collectors.toList());
		Map<String, List<SysAccessRecord>> map = records.stream()
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		//遍历session分组的数据
		for (List<SysAccessRecord> list : map.values()) {
			if (list.size() == 1) {
				vos.add(accessTime(TIME_9,list.size(),records.size()));
			} else {
				//计算时长，根据创建时间相减
				Integer timeInteger = timeDate(list.get(list.size() - 1)
						.getCreateTime(),
						list.get(0).getCreateTime());
				vos.add(accessTime(timeInteger,list.size(),records.size()));
			}
		}
		//排序，按照访问次数降序
		if (!vos.isEmpty()) {
			vos = group(vos,records.size()).stream()
					.collect(Collectors.toList());
			//排序类型为1访问次数2.所占比例
			vos = sortVos(dto.getOrderType(), dto.getOrder(), vos);
		}
		JSONObject object = new JSONObject();
		object.put("value", vos);
		object.put("sum", records.size());
		return new ResponseInfo(object);
	}
	
	/**
	 * 排序
	* @Title: sortVos 
	* @param orderType 排序类型
	* @param order 排序方式
	* @param vos 数据
	 */
	public List<StatisticsVisitorVo> sortVos(Integer orderType, 
				Boolean order, List<StatisticsVisitorVo> vos) {
		//排序类型为1访问次数
		if (orderType != null && orderType.equals(1)) {
			if (order != null && !order) {
				vos = vos.stream().sorted(Comparator.comparing(StatisticsVisitorVo::getValue)
						.reversed()).collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(StatisticsVisitorVo::getValue))
						.collect(Collectors.toList());
			}
		} else {
			//2位所占比例
			if (order != null && !order) {
				vos = vos.stream().sorted(Comparator.comparing(StatisticsVisitorVo::getDecimal)
						.reversed()).collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(StatisticsVisitorVo::getDecimal))
						.collect(Collectors.toList());
			}
		}
		return vos;
	}
	
}