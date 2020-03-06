/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.dao.StatisticsSourceDao;
import com.jeecms.statistics.domain.StatisticsSource;
import com.jeecms.statistics.domain.vo.StatisticsSourceListVo;
import com.jeecms.statistics.domain.vo.StatisticsSourcePieVo;
import com.jeecms.statistics.domain.vo.StatisticsSourceVo;
import com.jeecms.statistics.service.StatisticsServiceUtils;
import com.jeecms.statistics.service.StatisticsSourceService;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.SysAccessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.jeecms.common.util.MyDateUtils.COM_H_PATTERN;
import static com.jeecms.system.domain.SysAccessRecord.RESOURCE_EXT;
import static com.jeecms.system.domain.SysAccessRecord.RESOURCE_SEARCHER;

/**
 * 来源统计Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsSourceServiceImpl extends BaseServiceImpl<StatisticsSource,
		StatisticsSourceDao, Integer> implements StatisticsSourceService {

	/**
	 * 来源类型历史记录
	 */
	private static int HISTORY_SOURCE_TYPE = 0;
	/**
	 * 来源网站历史记录
	 */
	private static int HISTORY_SOURCE_URL = 1;
	/**
	 * 搜索引擎历史记录
	 */
	private static int HISTORY_SEARCH_ENGINE = 2;
	/**
	 * URL历史记录
	 */
	private static int HISTORY_URL = 3;
	/**
	 * 域名历史记录
	 */
	private static int HISTORY_DOMAIN = 4;

	/**
	 * 显示pv
	 */
	private static Integer SHOW_TYPE_PV = 1;
	/**
	 * 显示uv
	 */
	private static Integer SHOW_TYPE_UV = 2;
	/**
	 * 显示ip
	 */
	private static Integer SHOW_TYPE_IP = 3;

	@Autowired
	private SysAccessRecordService accessRecordService;

	@Override
	public List<StatisticsSource> getList(Integer siteId) {
		Date date = Calendar.getInstance().getTime();
		date = MyDateUtils.getDayAfterTime(date, -1);
		return dao.getList(MyDateUtils.getStartDate(date),
				MyDateUtils.getFinallyDate(date), siteId, null, null, null);
	}

	@Override
	public void statisticsYesterday() throws GlobalException {
		Date date = Calendar.getInstance().getTime();
		Date beginTime = MyDateUtils.getSpecficDateStart(date, -1);
		Date endTime = MyDateUtils.getSpecficDateEnd(date, -1);
		List<SysAccessRecord> list = accessRecordService.getSource(beginTime,
				endTime, null, null, null, null);
		Map<Integer, Map<String, Map<Boolean, Map<Short, Map<Integer, List<SysAccessRecord>>>>>> map =
				list.parallelStream().filter(o -> o.getSiteId() != null)
						.filter(o -> o.getCreateTime() != null)
						.filter(o -> o.getNewVisitor() != null)
						.filter(o -> o.getDeviceType() != null)
						.filter(o -> o.getSorceUrlType() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getSiteId,
								Collectors.groupingBy(o -> MyDateUtils.formatDate(o.getCreateTime(), COM_H_PATTERN),
										Collectors.groupingBy(SysAccessRecord::getNewVisitor,
												Collectors.groupingBy(SysAccessRecord::getDeviceType,
														Collectors.groupingBy(SysAccessRecord::getSorceUrlType))))));
		List<StatisticsSource> sources = new ArrayList<>();
		//站点分组
		for (Integer siteId : map.keySet()) {
			Map<String, Map<Boolean, Map<Short, Map<Integer, List<SysAccessRecord>>>>> siteMap = map.get(siteId);
			//小时分组
			for (String time : siteMap.keySet()) {
				Map<Boolean, Map<Short, Map<Integer, List<SysAccessRecord>>>> timeMap = siteMap.get(time);
				//是否新客户分组
				for (Boolean newVisitor : timeMap.keySet()) {
					Map<Short, Map<Integer, List<SysAccessRecord>>> newVisitorMap = timeMap.get(newVisitor);
					//访问设备分组
					for (Short deviceType : newVisitorMap.keySet()) {
						Map<Integer, List<SysAccessRecord>> sourceUrlTypeMap = newVisitorMap.get(deviceType);
						//来源类型分组
						for (Integer sourceUrlType : sourceUrlTypeMap.keySet()) {
							List<SysAccessRecord> records = sourceUrlTypeMap.get(sourceUrlType);
							if (SysAccessRecord.RESOURCE_SEARCHER.equals(sourceUrlType)) {
								//搜索引擎
								Map<String, List<SysAccessRecord>> engineNameMap = records.parallelStream()
										.filter(o -> o.getEngineName() != null)
										.collect(Collectors.groupingBy(SysAccessRecord::getEngineName));
								for (String engineName : engineNameMap.keySet()) {
									List<SysAccessRecord> accessRecords = sourceUrlTypeMap.get(sourceUrlType);
									int pvs = accessRecords.size();
									int uvs = uvCalculation(accessRecords);
									int ips = ipCalculation(accessRecords);
									int onlyOnePv = onlyOne(accessRecords);
									long accessTime = accessTimeTotal(accessRecords);
									StatisticsSource bean = new StatisticsSource(siteId,
											MyDateUtils.formatDate(beginTime),
											sourceUrlType, newVisitor, Integer.valueOf(deviceType),
											null, pvs, uvs, ips, Integer.valueOf(String.valueOf(accessTime)),
											onlyOnePv, Integer.parseInt(time), engineName, null);
									sources.add(bean);
								}
							} else if (SysAccessRecord.RESOURCE_EXT.equals(sourceUrlType)) {
								//外部链接
								Map<String, Map<String, List<SysAccessRecord>>> outMap =
										records.parallelStream().filter(o -> o.getSourceDomain() != null)
												.filter(o -> o.getSourceUrl() != null)
												.collect(Collectors.groupingBy(SysAccessRecord::getSourceDomain,
														Collectors.groupingBy(SysAccessRecord::getSourceUrl)));
								//来源域名分组
								for (String sourceDomain : outMap.keySet()) {
									Map<String, List<SysAccessRecord>> sourceDomainMap = outMap.get(sourceDomain);
									//来源网站分组
									for (String sourceUrl : sourceDomainMap.keySet()) {
										int pvs = records.size();
										int uvs = uvCalculation(records);
										int ips = ipCalculation(records);
										int onlyOnePv = onlyOne(records);
										long accessTime = accessTimeTotal(records);
										StatisticsSource bean = new StatisticsSource(siteId,
												MyDateUtils.formatDate(beginTime),
												sourceUrlType, newVisitor, Integer.valueOf(deviceType),
												sourceUrl, pvs, uvs, ips, Integer.valueOf(String.valueOf(accessTime)),
												onlyOnePv, Integer.parseInt(time), null, sourceDomain);
										sources.add(bean);
									}
								}
							} else if (SysAccessRecord.RESOURCE_SELF.equals(sourceUrlType)) {
								//直接访问
								int pvs = records.size();
								int uvs = uvCalculation(records);
								int ips = ipCalculation(records);
								int onlyOnePv = onlyOne(records);
								long accessTime = accessTimeTotal(records);
								StatisticsSource bean = new StatisticsSource(siteId, MyDateUtils.formatDate(beginTime),
										sourceUrlType, newVisitor, Integer.valueOf(deviceType),
										null, pvs, uvs, ips, Integer.valueOf(String.valueOf(accessTime)),
										onlyOnePv, Integer.parseInt(time), null, null);
								sources.add(bean);
							}
						}
					}
				}
			}
		}
		super.saveAll(sources);
	}

	@Override
	public StatisticsSourceVo sourceType(Date beginTime, Date endTime, Integer siteId,
										 Boolean newVisitor, Short accessSourceClient,
										 int sortType, Integer showType) throws IllegalAccessException {
		if (endTime != null) {
			endTime = MyDateUtils.getFinallyDate(endTime);
		}
		//开始时间不为空且不是单天调用历史记录
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				return history(beginTime, endTime, siteId, newVisitor, accessSourceClient != null
						? Integer.valueOf(accessSourceClient) : null, sortType, HISTORY_SOURCE_TYPE, showType);
			}
		}
		List<SysAccessRecord> list = accessRecordService.getSource(beginTime,
				endTime, siteId, newVisitor, accessSourceClient, null);
		StatisticsSourceVo vo = new StatisticsSourceVo();
		int pvs = list.size();
		int uvs = uvCalculation(list);
		int ips = ipCalculation(list);
		vo.setPvs(pvs);
		vo.setUvs(uvs);
		vo.setIps(ips);
		vo.setAverage(StatisticsServiceUtils.averageAccessDuration(list));
		vo.setBounce(bounceRate(list));
		//根据访问类型分组
		Map<String, List<SysAccessRecord>> map = list.parallelStream()
				.filter(o -> o.getSorceUrlType() != null)
				.collect(Collectors.groupingBy(o -> getSourceType(o.getSorceUrlType())));
		List<StatisticsSourceListVo> listVos = getByList(map);
		if (map.size() == 0) {
			vo.setFields(new String[]{"未知"});
		} else {
			String[] fields = new String[map.keySet().size()];
			vo.setFields(map.keySet().toArray(fields));
		}
		vo.setPie(getPieByType(pvs, uvs, ips, map));
		vo.setList(sort(sortType, listVos));
		vo.setObjects(getLineChart(list, map, HISTORY_SOURCE_TYPE, showType));
		return vo;
	}

	@Override
	public StatisticsSourceVo getBySourceUrl(Date beginTime, Date endTime, Integer siteId,
											 Boolean newVisitor, Short accessSourceClient,
											 int sortType, Integer showType) throws IllegalAccessException {
		if (endTime != null) {
			endTime = MyDateUtils.getFinallyDate(endTime);
		}
		//开始时间不为空且不是单天调用历史记录
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				return history(beginTime, endTime, siteId, newVisitor,
						accessSourceClient != null ? Integer.valueOf(accessSourceClient) : null,
						sortType, HISTORY_SOURCE_URL, showType);
			}
		}
		StatisticsSourceVo vo = new StatisticsSourceVo();
		List<SysAccessRecord> list = accessRecordService.getSource(beginTime,
				endTime, siteId, newVisitor, accessSourceClient, null);
		int pvs = list.size();
		vo.setPvs(pvs);
		int uvs = uvCalculation(list);
		vo.setUvs(uvs);
		int ips = ipCalculation(list);
		vo.setIps(ips);
		vo.setAverage(StatisticsServiceUtils.averageAccessDuration(list));
		vo.setBounce(bounceRate(list));
		//根据来源网站分组
		Map<String, List<SysAccessRecord>> map = list.parallelStream()
				.filter(o -> o.getSourceUrl() != null)
				.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
		vo.setObjects(getLineChart(list, map, HISTORY_SOURCE_URL, showType));
		vo.setPie(getPie(pvs, uvs, ips, map));
		if (map.size() == 0) {
			vo.setFields(new String[]{"未知"});
		} else {
			String[] fields = new String[map.keySet().size()];
			vo.setFields(map.keySet().toArray(fields));
		}
		return vo;
	}

	@Override
	public Page<StatisticsSourceListVo> getBySourceUrl(Date beginTime, Date endTime,
													   Integer siteId, Boolean newVisitor,
													   Short accessSourceClient,
													   int sortType, Pageable pageable) throws IllegalAccessException {
		if (endTime != null) {
			endTime = MyDateUtils.getFinallyDate(endTime);
		}
		//开始时间不为空且不是单天调用历史记录
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				List<StatisticsSource> list = dao.getList(beginTime,
						endTime, siteId, newVisitor, accessSourceClient != null
								? Integer.valueOf(accessSourceClient) : null, null);
				Map<String, List<StatisticsSource>> map = getMapHistory(list, "", HISTORY_SOURCE_URL);
				List<StatisticsSourceListVo> sourceListVos = getListHistory(map, getPV(list));
				sourceListVos = sort(sortType, sourceListVos);
				return listToPage(sourceListVos, pageable);
			}
		}
		List<SysAccessRecord> list = accessRecordService.getSource(beginTime,
				endTime, siteId, newVisitor, accessSourceClient, null);
		//根据来源网站分组
		Map<String, List<SysAccessRecord>> map = list.parallelStream()
				.filter(o -> o.getSourceUrl() != null)
				.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
		List<StatisticsSourceListVo> listVos = getList(map, list.size());
		listVos = sort(sortType, listVos);
		return listToPage(listVos, pageable);
	}

	@Override
	public StatisticsSourceVo searchEngine(Date beginTime, Date endTime, Integer siteId,
										   Boolean newVisitor, Short accessSourceClient,
										   int sortType, Integer showType) throws IllegalAccessException {
		if (endTime != null) {
			endTime = MyDateUtils.getFinallyDate(endTime);
		}
		//开始时间不为空且不是当天调用历史记录
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				return history(beginTime, endTime, siteId, newVisitor, accessSourceClient != null
						? Integer.valueOf(accessSourceClient) : null, sortType, HISTORY_SEARCH_ENGINE, showType);
			}
		}
		List<SysAccessRecord> list = accessRecordService.getSource(beginTime,
				endTime, siteId, newVisitor, accessSourceClient, RESOURCE_SEARCHER);
		StatisticsSourceVo vo = new StatisticsSourceVo();
		int pvs = list.size();
		vo.setPvs(pvs);
		int uvs = uvCalculation(list);
		vo.setUvs(uvs);
		int ips = ipCalculation(list);
		vo.setIps(ips);
		vo.setAverage(StatisticsServiceUtils.averageAccessDuration(list));
		vo.setBounce(bounceRate(list));
		//根据搜索引擎分组
		Map<String, List<SysAccessRecord>> map = list.parallelStream()
				.filter(o -> o.getEngineName() != null)
				.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getEngineName));
		List<StatisticsSourceListVo> listVos = getList(map, pvs);
		listVos = sort(sortType, listVos);
		vo.setList(listVos);
		if (map.size() == 0) {
			vo.setFields(new String[]{"未知"});
		} else {
			String[] fields = new String[map.keySet().size()];
			vo.setFields(map.keySet().toArray(fields));
		}
		vo.setObjects(getLineChart(list, map, HISTORY_SEARCH_ENGINE, showType));
		vo.setPie(getPie(pvs, uvs, ips, map));
		return vo;
	}

	@Override
	public StatisticsSourceVo externalLink(Date beginTime, Date endTime,
										   Integer siteId, Boolean newVisitor,
										   Short accessSourceClient,
										   Integer sortType, int type,
										   Integer showType) throws IllegalAccessException {
		sortType = sortType == null ? 0 : sortType;
		Date nowDate = Calendar.getInstance().getTime();
		Date beginDate = beginTime != null ? beginTime : MyDateUtils.getStartDate(nowDate);
		Date endDate = endTime != null ? endTime : MyDateUtils.getFinallyDate(nowDate);
		if (endDate != null) {
			endDate = MyDateUtils.getFinallyDate(endDate);
		}
		int status = type == EXTERNAL_LINK_URL ? HISTORY_URL : HISTORY_DOMAIN;
		//开始时间不为空且不是单天调用历史记录
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginDate, Calendar.getInstance().getTime())) {
				return history(beginDate, endDate, siteId, newVisitor, accessSourceClient != null
						? Integer.valueOf(accessSourceClient) : null, sortType, status, showType);
			}
		}
		List<SysAccessRecord> list = accessRecordService.getSource(beginDate,
				endDate, siteId, newVisitor, accessSourceClient, RESOURCE_EXT);
		StatisticsSourceVo vo = new StatisticsSourceVo();
		Map<String, List<SysAccessRecord>> map = new HashMap<String, List<SysAccessRecord>>();
		if (type == EXTERNAL_LINK_URL) {
			//根据链接分组
			list = list.parallelStream()
					.filter(o -> o.getSourceUrl() != null)
					.collect(Collectors.toList());
			if (list.size() > 0) {
				map = list.parallelStream().sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
						.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
			}
		} else {
			//根据域名分组
			list = list.parallelStream()
					.filter(o -> o.getSourceDomain() != null)
					.collect(Collectors.toList());
			if (list.size() > 0) {
				map = list.parallelStream()
						.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
						.collect(Collectors.groupingBy(SysAccessRecord::getSourceDomain));
			}
		}
		int pvs = list.size();
		vo.setPvs(pvs);
		int uvs = uvCalculation(list);
		vo.setUvs(uvs);
		int ips = ipCalculation(list);
		vo.setIps(ips);
		vo.setAverage(StatisticsServiceUtils.averageAccessDuration(list));
		vo.setBounce(bounceRate(list));
		if (map.size() == 0) {
			vo.setFields(new String[]{"未知"});
		} else {
			String[] fields = new String[map.keySet().size()];
			vo.setFields(map.keySet().toArray(fields));
		}
		vo.setObjects(getLineChart(list, map, status, showType));
		vo.setPie(getPie(pvs, uvs, ips, map));
		return vo;
	}

	/**
	 * 折线图数据
	 *
	 * @param list 访问记录集合
	 * @param map  分组后数据
	 * @param type 类型(0来源类型分组1来源网站分组2搜素引擎分组3来源域名4来源url)
	 * @return JSONArray
	 */
	private JSONArray getLineChart(List<SysAccessRecord> list, Map<String, List<SysAccessRecord>> map, int type,
								   Integer showType) {
		Map<String, List<SysAccessRecord>> timeMap = list.parallelStream()
				.filter(o -> o.getCreateTime() != null)
				.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
				.collect(Collectors.groupingBy(o -> MyDateUtils.formatDate(
						o.getCreateTime(), COM_H_PATTERN)));
		JSONArray array = new JSONArray();
		for (int i = 0; i < 24; i++) {
			JSONObject object = new JSONObject();
			String time = i < 10 ? "0" + i : String.valueOf(i);
			object.put("time", time);
			List<SysAccessRecord> accessRecords = timeMap.get(time);
			if (accessRecords == null) {
				for (String key : map.keySet()) {
					object.put(key, 0);
				}
			} else {
				Map<String, List<SysAccessRecord>> createMap = getMap(accessRecords, type);
				for (String key : map.keySet()) {
					List<SysAccessRecord> recordList = createMap.get(key);
					if (recordList == null) {
						object.put(key, 0);
					} else {
						if (SHOW_TYPE_PV.equals(showType)) {
							int pv = createMap.get(key).size();
							object.put(key, pv);
						} else if (SHOW_TYPE_UV.equals(showType)) {
							int uv = uvCalculation(createMap.get(key));
							object.put(key, uv);
						} else if (SHOW_TYPE_IP.equals(showType)) {
							int ip = ipCalculation(createMap.get(key));
							object.put(key, ip);
						} else {
							int pv = createMap.get(key).size();
							object.put(key, pv);
						}
					}
				}
			}
			array.add(object);
		}
		return array;
	}

	@Override
	public Page<StatisticsSourceListVo> externalLinkPage(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
														 Short accessSourceClient, int sortType, String url,
														 int type, Pageable pageable) throws IllegalAccessException {
		if (endTime != null) {
			endTime = MyDateUtils.getFinallyDate(endTime);
		}
		//开始时间不为空且不是单天调用历史记录
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				List<StatisticsSource> list = dao.getList(beginTime,
						endTime, siteId, newVisitor, accessSourceClient != null
								? Integer.valueOf(accessSourceClient) : null, RESOURCE_EXT);
				Map<String, List<StatisticsSource>> map = getMapHistory(list, url == null ? "" : url,
						type == EXTERNAL_LINK_URL ? HISTORY_URL : HISTORY_DOMAIN);
				List<StatisticsSourceListVo> sourceListVos = getListHistory(map, getPV(list));
				sourceListVos = sort(sortType, sourceListVos);
				return listToPage(sourceListVos, pageable);
			}
		}
		List<SysAccessRecord> list = accessRecordService.getSource(beginTime,
				endTime, siteId, newVisitor, accessSourceClient, null);
		Map<String, List<SysAccessRecord>> map;
		url = url == null ? "" : url;
		if (type == EXTERNAL_LINK_URL) {
			//根据链接分组
			String finalUrl = url;
			map = list.parallelStream()
					.filter(o -> o.getSourceUrl() != null)
					.filter(o -> o.getSourceUrl().contains(finalUrl))
					.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
					.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
		} else {
			//根据域名分组
			String finalUrl1 = url;
			map = list.parallelStream()
					.filter(o -> o.getSourceDomain() != null)
					.filter(o -> o.getSourceDomain().contains(finalUrl1))
					.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
					.collect(Collectors.groupingBy(SysAccessRecord::getSourceDomain));
		}
		List<StatisticsSourceListVo> listVos = getList(map, list.size());
		listVos = sort(sortType, listVos);
		return listToPage(listVos, pageable);
	}

	/**
	 * 获取来源类型列表（实时）
	 *
	 * @param map map
	 * @return List
	 */
	private List<StatisticsSourceListVo> getByList(Map<String,
			List<SysAccessRecord>> map) throws IllegalAccessException {
		List<StatisticsSourceListVo> listVos = new ArrayList<StatisticsSourceListVo>();
		for (String sorceUrlType : map.keySet()) {
			StatisticsSourceListVo listVo = new StatisticsSourceListVo();
			List<SysAccessRecord> records = map.get(sorceUrlType);
			if (sorceUrlType.equals(getSourceType(RESOURCE_SEARCHER))) {
				//搜索引擎
				Map<String, List<SysAccessRecord>> s = records.parallelStream()
						.filter(o -> o.getSourceUrl() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getEngineName));
				List<StatisticsSourceListVo> sourceListVos = getList(s, 0);
				listVo.setList(sourceListVos);
			} else if (sorceUrlType.equals(getSourceType(RESOURCE_EXT))) {
				//外部链接
				Map<String, List<SysAccessRecord>> s = records.parallelStream()
						.filter(o -> o.getSourceUrl() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
				List<StatisticsSourceListVo> sourceListVos = getList(s, 0);
				listVo.setList(sourceListVos);
			}
			List<SysAccessRecord> accessRecords = map.get(sorceUrlType);
			int pv = map.get(sorceUrlType).size();
			int ip = ipCalculation(accessRecords);
			int uv = uvCalculation(accessRecords);
			listVo.setPvs(pv);
			listVo.setUvs(uv);
			listVo.setIps(ip);
			listVo.setAverage(StatisticsServiceUtils.averageAccessDuration(accessRecords));
			listVo.setName(sorceUrlType);
			listVo.setBounce(bounceRate(accessRecords));
			listVos.add(listVo);
		}
		return listVos;
	}

	/**
	 * 获取来源类型饼图（实时）
	 *
	 * @param pvs pv数
	 * @param uvs uv数
	 * @param ips ip数
	 * @param map Map
	 * @return StatisticsSourcePieVo集合
	 */
	private List<StatisticsSourcePieVo> getPieByType(int pvs, int uvs, int ips,
													 Map<String, List<SysAccessRecord>> map) throws IllegalAccessException {
		List<StatisticsSourcePieVo> pieVos = new ArrayList<StatisticsSourcePieVo>();
		for (String sorceUrlType : map.keySet()) {
			StatisticsSourcePieVo pieVo = new StatisticsSourcePieVo();
			pieVo.setName(sorceUrlType);
			List<SysAccessRecord> accessRecords = map.get(sorceUrlType);
			int pv = map.get(sorceUrlType).size();
			int ip = ipCalculation(accessRecords);
			int uv = uvCalculation(accessRecords);
			pieVo.setPv(pv);
			pieVo.setPvPercentage(MathUtil.div(new BigDecimal(pv * 100),
					new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			pieVo.setUv(uv);
			pieVo.setPvPercentage(MathUtil.div(new BigDecimal(uv * 100),
					new BigDecimal(uvs), MathUtil.SCALE_LEN_COMMON));
			pieVo.setIp(ip);
			pieVo.setPvPercentage(MathUtil.div(new BigDecimal(ip * 100),
					new BigDecimal(ips), MathUtil.SCALE_LEN_COMMON));
			pieVos.add(pieVo);
		}
		return pieVos;
	}

	/**
	 * 获取来源类型列表(历史)
	 *
	 * @param map map
	 * @return List
	 */
	private List<StatisticsSourceListVo> getByListHistory(Map<String, List<StatisticsSource>> map) throws IllegalAccessException {
		List<StatisticsSourceListVo> listVos = new ArrayList<StatisticsSourceListVo>();
		for (String sorceUrlType : map.keySet()) {
			StatisticsSourceListVo listVo = new StatisticsSourceListVo();
			List<StatisticsSource> records = map.get(sorceUrlType);
			if (sorceUrlType.equals(RESOURCE_SEARCHER.toString())) {
				//搜索引擎
				Map<String, List<StatisticsSource>> s = records.parallelStream()
						.filter(o -> o.getEngineName() != null)
						.collect(Collectors.groupingBy(StatisticsSource::getEngineName));
				List<StatisticsSourceListVo> sourceListVos = getListHistory(s, getPV(records));
				listVo.setList(sourceListVos);
			} else if (sorceUrlType.equals(RESOURCE_EXT.toString())) {
				//外部链接
				Map<String, List<StatisticsSource>> s = records.parallelStream()
						.filter(o -> o.getSorceUrl() != null)
						.collect(Collectors.groupingBy(StatisticsSource::getSorceUrl));
				List<StatisticsSourceListVo> sourceListVos = getListHistory(s, getPV(records));
				listVo.setList(sourceListVos);
			}
			List<StatisticsSource> accessRecords = map.get(sorceUrlType);
			int pv = getPV(accessRecords);
			int ip = getIp(accessRecords);
			int uv = getUv(accessRecords);
			listVo.setPvs(pv);
			listVo.setUvs(uv);
			listVo.setIps(ip);
			long average = getAverage(accessRecords);
			listVo.setAverage(average == 0 ? 0 : average / pv);
			listVo.setName(sorceUrlType);
			listVo.setBounce(MathUtil.div(new BigDecimal(getOnlyOnePv(accessRecords) * 100),
					new BigDecimal(pv), MathUtil.SCALE_LEN_COMMON));
			listVos.add(listVo);
		}
		return listVos;
	}

	/**
	 * 来源类型历史记录饼图数据
	 *
	 * @param pvs pv数
	 * @param uvs uv数
	 * @param ips ip数
	 * @param map Map
	 * @return StatisticsSourcePieVo集合
	 */
	private List<StatisticsSourcePieVo> getByPie(int pvs, int uvs, int ips, Map<String, List<StatisticsSource>> map) throws IllegalAccessException {
		List<StatisticsSourcePieVo> pieVos = new ArrayList<StatisticsSourcePieVo>();
		for (String sorceUrlType : map.keySet()) {
			StatisticsSourcePieVo pieVo = new StatisticsSourcePieVo();
			pieVo.setName(sorceUrlType);
			List<StatisticsSource> accessRecords = map.get(sorceUrlType);
			int pv = getPV(accessRecords);
			int ip = getIp(accessRecords);
			int uv = getUv(accessRecords);
			pieVo.setPv(pv);
			pieVo.setPvPercentage(MathUtil.div(new BigDecimal(pv * 100),
					new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			pieVo.setUv(uv);
			pieVo.setUvPercentage(MathUtil.div(new BigDecimal(uv * 100),
					new BigDecimal(uvs), MathUtil.SCALE_LEN_COMMON));
			pieVo.setIp(ip);
			pieVo.setIpPercentage(MathUtil.div(new BigDecimal(ip * 100),
					new BigDecimal(ips), MathUtil.SCALE_LEN_COMMON));
			pieVos.add(pieVo);
		}
		return pieVos;
	}

	/**
	 * 来源分析统计历史
	 *
	 * @param beginTime         开始时间
	 * @param endTime           结束时间
	 * @param siteId            站点id
	 * @param newVisitor        true 新客户 false 老客户
	 * @param visitorDeviceType 1 pc 2移动端
	 * @param sortType          0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @return
	 */
	private StatisticsSourceVo history(Date beginTime, Date endTime, Integer siteId,
									   Boolean newVisitor, Integer visitorDeviceType,
									   int sortType, int type, Integer showType) throws IllegalAccessException {
		Integer sorceUrlType = null;
		if (type == HISTORY_SEARCH_ENGINE) {
			sorceUrlType = RESOURCE_SEARCHER;
		} else if (type == HISTORY_URL || type == HISTORY_DOMAIN) {
			sorceUrlType = RESOURCE_EXT;
		}
		StatisticsSourceVo vo = new StatisticsSourceVo();
		List<StatisticsSource> list = dao.getList(beginTime,
				endTime, siteId, newVisitor, visitorDeviceType, sorceUrlType);
		int pvs = getPV(list);
		int uvs = getUv(list);
		int ips = getIp(list);
		int onlyOnePvs = getOnlyOnePv(list);
		long averages = getAverage(list);
		vo.setPvs(pvs);
		vo.setUvs(uvs);
		vo.setIps(ips);
		vo.setBounce(MathUtil.div(new BigDecimal(onlyOnePvs * 100),
				new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
		vo.setAverage(pvs == 0 ? 0 : averages / pvs);
		Map<String, List<StatisticsSource>> map = getMapHistory(list, "", type);

		List<StatisticsSourceListVo> sourceListVos;
		if (type == HISTORY_SOURCE_TYPE) {
			sourceListVos = getByListHistory(map);
			vo.setPie(getByPie(pvs, uvs, ips, map));
		} else {
			vo.setPie(getPieByHistory(pvs, uvs, ips, map));
			sourceListVos = getListHistory(map, pvs);
		}
		if (map.size() == 0) {
			vo.setFields(new String[]{"未知"});
		} else {
			String[] fields = new String[map.keySet().size()];
			vo.setFields(map.keySet().toArray(fields));
		}
		vo.setObjects(getLineChartHistory(beginTime, endTime, list, map, type, showType));
		//排序
		if (type == HISTORY_SOURCE_TYPE || type == HISTORY_SEARCH_ENGINE) {
			vo.setList(sort(sortType, sourceListVos));
		}
		return vo;
	}

	/**
	 * 获取来源分析列表（实时）
	 *
	 * @param map 数据
	 * @return List
	 */
	private List<StatisticsSourceListVo> getList(Map<String, List<SysAccessRecord>> map, int pvs) throws IllegalAccessException {
		List<StatisticsSourceListVo> listVos = new ArrayList<StatisticsSourceListVo>();
		for (String sourceUrl : map.keySet()) {
			StatisticsSourceListVo listVo = new StatisticsSourceListVo();
			int pv = map.get(sourceUrl).size();
			listVo.setPvs(pv);
			int ip = ipCalculation(map.get(sourceUrl));
			listVo.setIps(ip);
			int uv = uvCalculation(map.get(sourceUrl));
			listVo.setUvs(uv);
			if (pvs != 0) {
				listVo.setProportion(MathUtil.div(new BigDecimal(pv * 100),
						new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			}
			listVo.setName(sourceUrl);
			listVo.setAverage(StatisticsServiceUtils.averageAccessDuration(map.get(sourceUrl)));
			listVo.setBounce(bounceRate(map.get(sourceUrl)));
			listVos.add(listVo);
		}
		return listVos;
	}

	/**
	 * 获取来源分析饼图（实时）
	 *
	 * @param map 数据
	 * @return List
	 */
	private List<StatisticsSourcePieVo> getPie(int pvs, int uvs, int ips, Map<String, List<SysAccessRecord>> map) throws IllegalAccessException {
		List<StatisticsSourcePieVo> pieVos = new ArrayList<StatisticsSourcePieVo>();
		for (String sourceUrl : map.keySet()) {
			StatisticsSourcePieVo pieVo = new StatisticsSourcePieVo();
			int pv = map.get(sourceUrl).size();
			pieVo.setPv(pv);
			pieVo.setPvPercentage(MathUtil.div(new BigDecimal(pv * 100),
					new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			int ip = ipCalculation(map.get(sourceUrl));
			pieVo.setIp(ip);
			pieVo.setIpPercentage(MathUtil.div(new BigDecimal(ip * 100),
					new BigDecimal(ips), MathUtil.SCALE_LEN_COMMON));
			int uv = uvCalculation(map.get(sourceUrl));
			pieVo.setUv(uv);
			pieVo.setUvPercentage(MathUtil.div(new BigDecimal(uv * 100),
					new BigDecimal(uvs), MathUtil.SCALE_LEN_COMMON));
			pieVo.setName(sourceUrl);
			pieVos.add(pieVo);
		}
		return pieVos;
	}

	/**
	 * 获取来源分析历史列表
	 *
	 * @param map 数据
	 * @return List
	 */
	private List<StatisticsSourceListVo> getListHistory(Map<String, List<StatisticsSource>> map, int pvs) throws IllegalAccessException {
		List<StatisticsSourceListVo> sourceListVos = new ArrayList<StatisticsSourceListVo>();
		for (String name : map.keySet()) {
			StatisticsSourceListVo listVo = new StatisticsSourceListVo();
			List<StatisticsSource> sources = map.get(name);
			int pv = getPV(sources);
			int uv = getUv(sources);
			int ip = getIp(sources);
			listVo.setPvs(pv);
			listVo.setUvs(uv);
			listVo.setIps(ip);
			if (pvs != 0) {
				listVo.setProportion(MathUtil.div(new BigDecimal(pv * 100),
						new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			}
			listVo.setName(name);
			int onlyOnePv = getOnlyOnePv(sources);
			listVo.setBounce(MathUtil.div(new BigDecimal(onlyOnePv * 100),
					new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			long average = getAverage(sources);
			listVo.setAverage(average == 0 ? 0 : average / sources.size());
			sourceListVos.add(listVo);
		}
		return sourceListVos;
	}

	/**
	 * 获取来源分析饼图（历史）
	 *
	 * @param map 数据
	 * @return List
	 */
	private List<StatisticsSourcePieVo> getPieByHistory(int pvs, int uvs, int ips,
														Map<String, List<StatisticsSource>> map)
			throws IllegalAccessException {
		List<StatisticsSourcePieVo> pieVos = new ArrayList<StatisticsSourcePieVo>();
		for (String sourceUrl : map.keySet()) {
			StatisticsSourcePieVo pieVo = new StatisticsSourcePieVo();
			int pv = getPV(map.get(sourceUrl));
			pieVo.setPv(pv);
			pieVo.setPvPercentage(MathUtil.div(new BigDecimal(pv * 100),
					new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			int ip = getIp(map.get(sourceUrl));
			pieVo.setIp(ip);
			pieVo.setIpPercentage(MathUtil.div(new BigDecimal(ip * 100),
					new BigDecimal(uvs), MathUtil.SCALE_LEN_COMMON));
			int uv = getUv(map.get(sourceUrl));
			pieVo.setUv(uv);
			pieVo.setUvPercentage(MathUtil.div(new BigDecimal(uv * 100),
					new BigDecimal(ips), MathUtil.SCALE_LEN_COMMON));
			pieVo.setName(sourceUrl);
			pieVos.add(pieVo);
		}
		return pieVos;
	}

	/**
	 * 折线图数据
	 *
	 * @param list 访问记录集合
	 * @param map  分组后数据
	 * @param type 类型(0来源类型分组1来源网站分组2搜素引擎分组3来源域名4来源url)
	 * @return JSONArray
	 */
	private JSONArray getLineChartHistory(Date beginTime, Date endTime,
										  List<StatisticsSource> list, Map<String,
			List<StatisticsSource>> map, int type, Integer showType) {
		//是否同一天
		Map<String, List<StatisticsSource>> timeMap;
		boolean isInDate = MyDateUtils.getDaysBetweenDate(beginTime, endTime) == 0;
		List<String> days;
		if (isInDate) {
			timeMap = list.parallelStream()
					.filter(o -> o.getStatisticsDay() != null)
					.sorted(Comparator.comparing(StatisticsSource::getStatisticsDay))
					.collect(Collectors.groupingBy(o -> o.getStatisticsHour() < 10 ? "0" + o.getStatisticsHour() :
							String.valueOf(o.getStatisticsHour())));
			days = new ArrayList<>(24);
			for (int i = 0; i < 24; i++) {
				days.add(i < 10 ? "0" + i : String.valueOf(i));
			}
		} else {
			timeMap = list.parallelStream()
					.filter(o -> o.getStatisticsDay() != null)
					.sorted(Comparator.comparing(StatisticsSource::getStatisticsDay))
					.collect(Collectors.groupingBy(StatisticsSource::getStatisticsDay));
			//获取两个日期之间的所有日期
			DateFormat dateFormat = new SimpleDateFormat(MyDateUtils.COM_Y_M_D_PATTERN);
			days = MyDateUtils.getDays(dateFormat.format(beginTime), dateFormat.format(endTime));
		}
		JSONArray array = new JSONArray();
		for (String time : days) {
			JSONObject object = new JSONObject();
			object.put("time", time);
			List<StatisticsSource> sources = timeMap.get(time);
			if (sources == null) {
				for (String key : map.keySet()) {
					object.put(key, 0);
				}
			} else {
				Map<String, List<StatisticsSource>> createMap = getMapHistory(sources, "", type);
				for (String key : map.keySet()) {
					List<StatisticsSource> recordList = createMap.get(key);
					if (recordList == null) {
						object.put(key, 0);
					} else {
						if (SHOW_TYPE_PV.equals(showType)) {
							int pv = getPV(createMap.get(key));
							object.put(key, pv);
						} else if (SHOW_TYPE_UV.equals(showType)) {
							int uv = getUv(createMap.get(key));
							object.put(key, uv);
						} else if (SHOW_TYPE_IP.equals(showType)) {
							int ip = getIp(createMap.get(key));
							object.put(key, ip);
						} else {
							int pv = getPV(createMap.get(key));
							object.put(key, pv);
						}
					}
				}
			}
			array.add(object);
		}
		return array;
	}

	/**
	 * 计算UV
	 *
	 * @param list 访问记录列表
	 * @return uv
	 */
	private int uvCalculation(List<SysAccessRecord> list) {
		//登录用户的uv
		int userNum = list.parallelStream().filter(o -> o.getLoginUserId() != null)
				.collect(Collectors.groupingBy(SysAccessRecord::getLoginUserId)).size();
		//未登录用户的uv
		int cookieNum = list.parallelStream().filter(o -> o.getCookieId() != null)
				.filter(o -> o.getLoginUserId() == null)
				.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();
		return userNum + cookieNum;
	}

	/**
	 * 计算ip数
	 *
	 * @param list 访问记录列表
	 * @return ip数
	 */
	private int ipCalculation(List<SysAccessRecord> list) {
		return list.parallelStream().collect(Collectors.groupingBy(SysAccessRecord::getAccessIp)).size();
	}

	/**
	 * 跳出率
	 *
	 * @param list 访问内容列表
	 * @return 跳出率
	 */
	private BigDecimal bounceRate(List<SysAccessRecord> list) throws IllegalAccessException {
		//根据session分组
		Map<String, List<SysAccessRecord>> sessionList =
				list.parallelStream().filter(o -> o.getSessionId() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		int num = 0;
		int bounceRateNum = sessionList.size();
		//循环遍历，判断每个list的长度，如果为1则表示跳出
		for (List<SysAccessRecord> records : sessionList.values()) {
			if (records.size() == 1) {
				num++;
			}
		}
		BigDecimal bounceRate = BigDecimal.ZERO;
		//0不能作被除数
		if (bounceRateNum != 0) {
			bounceRate = (MathUtil.div(new BigDecimal(num * 100),
					new BigDecimal(bounceRateNum), MathUtil.SCALE_LEN_COMMON));
		}
		return bounceRate;
	}

	/**
	 * 获取只访问一次的数量
	 *
	 * @param list List
	 * @return 只访问一次的数量
	 */
	private int onlyOne(List<SysAccessRecord> list) {
		//根据session分组
		Map<String, List<SysAccessRecord>> sessionList =
				list.parallelStream().filter(o -> o.getSessionId() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		int num = 0;
		//循环遍历，判断每个list的长度，如果为1则表示跳出
		for (List<SysAccessRecord> records : sessionList.values()) {
			if (records.size() == 1) {
				num++;
			}
		}
		return num;
	}

	/**
	 * 获取总访问时长
	 *
	 * @param list list
	 * @return 总访问时长
	 */
	private long accessTimeTotal(List<SysAccessRecord> list) {
		//根据session分组
		Map<String, List<SysAccessRecord>> sessionMap = list.parallelStream()
				.filter(o -> o.getSessionId() != null)
				.sorted(Comparator.comparing(AbstractDomain::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
		long total = 0;
		for (List<SysAccessRecord> records : sessionMap.values()) {
			if (records.size() > 1) {
				long begin = records.get(records.size() - 1).getCreateTime().getTime();
				long end = records.get(0).getCreateTime().getTime();
				total += Math.abs(end - begin);
			}
		}
		return total / 1000;
	}

	/**
	 * 获取pv数
	 *
	 * @param list List
	 * @return pv数
	 */
	private int getPV(List<StatisticsSource> list) {
		return list.parallelStream().filter(o -> o.getPvs() != null)
				.mapToInt(StatisticsSource::getPvs).sum();
	}

	/**
	 * 获取uv数
	 *
	 * @param list List
	 * @return ip数
	 */
	@Override
	public int getUv(List<StatisticsSource> list) {
		return list.parallelStream().filter(o -> o.getUvs() != null)
				.mapToInt(StatisticsSource::getUvs).sum();
	}

	/**
	 * 获取ip数
	 *
	 * @param list List
	 * @return ip数
	 */
	@Override
	public int getIp(List<StatisticsSource> list) {
		return list.parallelStream().filter(o -> o.getIps() != null)
				.mapToInt(StatisticsSource::getIps).sum();
	}

	/**
	 * 获取只访问一次页面的访问次数
	 *
	 * @param list List
	 * @return 只访问一次页面的访问次数
	 */
	private int getOnlyOnePv(List<StatisticsSource> list) {
		return list.parallelStream().filter(o -> o.getOnlyOnePv() != null)
				.mapToInt(StatisticsSource::getOnlyOnePv).sum();
	}

	/**
	 * 获取总访问时长(单位：秒)
	 *
	 * @param list List
	 * @return 总访问时长(单位 ： 秒)
	 */
	private long getAverage(List<StatisticsSource> list) {
		long average = list.parallelStream().filter(o -> o.getAccessHoureLong() != null)
				.mapToLong(StatisticsSource::getAccessHoureLong).sum();
		return list.size() == 0 ? 0 : average;
	}

	/**
	 * 入口页面排序
	 *
	 * @param sortType 排序类型 0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 ip正序 5 IP倒序 6 平均停留时长正序 7 平均停留时长倒序
	 * @param list     排序列表
	 * @return 排序后列表
	 */
	private List<StatisticsSourceListVo> sort(int sortType, List<StatisticsSourceListVo> list) {
		switch (sortType) {
			case 1:
				//PV倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getPvs).reversed())
						.collect(Collectors.toList());
			case 2:
				//UV正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getUvs))
						.collect(Collectors.toList());
			case 3:
				//UV倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getUvs).reversed())
						.collect(Collectors.toList());
			case 4:
				//Ip数正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getIps))
						.collect(Collectors.toList());
			case 5:
				//Ip数倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getIps)
								.reversed())
						.collect(Collectors.toList());
			case 6:
				//跳出率正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getBounce))
						.collect(Collectors.toList());
			case 7:
				//跳出率倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getBounce)
								.reversed())
						.collect(Collectors.toList());
			case 8:
				//平均访问时长正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getAverage))
						.collect(Collectors.toList());
			case 9:
				//平均访问时长倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getAverage)
								.reversed())
						.collect(Collectors.toList());
			default:
				//PV正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsSourceListVo::getPvs))
						.collect(Collectors.toList());
		}
	}

	/**
	 * list转分页
	 *
	 * @param list     列表
	 * @param pageable 分页数据
	 * @return PageImpl
	 */
	private PageImpl<StatisticsSourceListVo> listToPage(List<StatisticsSourceListVo> list, Pageable pageable) {
		int size = list.size();
		if (list != null) {
			list = list.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
			return new PageImpl<StatisticsSourceListVo>(list, pageable, size);
		}
		return null;
	}

	/**
	 * 获取不同的分组map(历史)
	 *
	 * @param list 需要分组的列表
	 * @param type 类型(0来源类型分组1来源网站分组2搜素引擎分组3来源域名4来源url)
	 * @return
	 */
	private Map<String, List<StatisticsSource>> getMapHistory(List<StatisticsSource> list,
															  String url, int type) {
		Map<String, List<StatisticsSource>> map;
		if (type == HISTORY_SOURCE_URL) {
			//以来源网站分组
			map = list.parallelStream().filter(o -> o.getSorceUrl() != null)
					.collect(Collectors.groupingBy(StatisticsSource::getSorceUrl));
		} else if (type == HISTORY_SEARCH_ENGINE) {
			//以搜索引擎分组
			map = list.parallelStream().filter(o -> o.getEngineName() != null)
					.collect(Collectors.groupingBy(StatisticsSource::getEngineName));
		} else if (type == HISTORY_URL) {
			//以来源url分组
			map = list.parallelStream().filter(o -> o.getSorceUrl() != null)
					.filter(o -> o.getSorceUrl().contains(url))
					.collect(Collectors.groupingBy(StatisticsSource::getSorceUrl));
		} else if (type == HISTORY_DOMAIN) {
			//以来源域名
			map = list.parallelStream().filter(o -> o.getSourceDomain() != null)
					.filter(o -> o.getSourceDomain().contains(url))
					.collect(Collectors.groupingBy(StatisticsSource::getSourceDomain));
		} else {
			//默认以来源类型分组
			map = list.parallelStream().filter(o -> o.getSorceUrlType() != null)
					.collect(Collectors.groupingBy(o -> getSourceType(o.getSorceUrlType())));
		}
		return map;
	}

	/**
	 * 获取不同的分组map(实时)
	 *
	 * @param list 需要分组的列表
	 * @param type 类型(0来源类型分组1来源网站分组2搜素引擎分组3来源域名4来源url)
	 * @return
	 */
	private Map<String, List<SysAccessRecord>> getMap(List<SysAccessRecord> list, int type) {
		Map<String, List<SysAccessRecord>> map;
		if (type == HISTORY_SOURCE_URL) {
			//以来源网站分组
			map = list.parallelStream().filter(o -> o.getSourceUrl() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
		} else if (type == HISTORY_SEARCH_ENGINE) {
			//以搜索引擎分组
			map = list.parallelStream().filter(o -> o.getEngineName() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getEngineName));
		} else if (type == HISTORY_URL) {
			//以来源url分组
			map = list.parallelStream().filter(o -> o.getSourceUrl() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getSourceUrl));
		} else if (type == HISTORY_DOMAIN) {
			//以来源域名
			map = list.parallelStream().filter(o -> o.getSourceDomain() != null)
					.collect(Collectors.groupingBy(SysAccessRecord::getSourceDomain));
		} else {
			//默认以来源类型分组
			map = list.parallelStream().filter(o -> o.getSorceUrlType() != null)
					.collect(Collectors.groupingBy(o -> getSourceType(o.getSorceUrlType())));
		}
		return map;
	}

	/**
	 * 来源类型
	 *
	 * @param sourceType 来源类型
	 * @return 来源类型
	 */
	private String getSourceType(Integer sourceType) {
		if (sourceType.equals(RESOURCE_SEARCHER)) {
			return "搜索引擎";
		} else if (sourceType.equals(RESOURCE_EXT)) {
			return "外部链接";
		} else if (sourceType.equals(SysAccessRecord.RESOURCE_SELF)) {
			return "直接访问";
		}
		return "其他";
	}

}