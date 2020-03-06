/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.dao.StatisticsAccessPageDao;
import com.jeecms.statistics.domain.StatisticsAccessPage;
import com.jeecms.statistics.domain.vo.StatisticsAccessPageVo;
import com.jeecms.statistics.domain.vo.StatisticsEntryPageVo;
import com.jeecms.statistics.domain.vo.StatisticsPageListVo;
import com.jeecms.statistics.service.StatisticsAccessPageService;
import com.jeecms.statistics.service.StatisticsServiceUtils;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.SysAccessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jeecms.common.util.MyDateUtils.COM_Y_M_D_PATTERN;
import static com.jeecms.statistics.domain.StatisticsAccessPage.URL_TYPE_ACCESS;
import static com.jeecms.statistics.domain.StatisticsAccessPage.URL_TYPE_ENTRY;

/**
 * 受访分析Service实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-07-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticsAccessPageServiceImpl extends BaseServiceImpl<StatisticsAccessPage,
		StatisticsAccessPageDao, Integer> implements StatisticsAccessPageService {

	@Autowired
	private SysAccessRecordService accessRecordService;

	@Override
	public void statisticsAccessPage() throws GlobalException {
		Date date = Calendar.getInstance().getTime();
		Date beginTime = MyDateUtils.getSpecficDateStart(date, -1);
		Date endTime = MyDateUtils.getSpecficDateEnd(date, -1);
		List<SysAccessRecord> list = accessRecordService.getAccessPage(beginTime, endTime, null, null, null);
		Map<Integer, Map<Boolean, Map<Integer, Map<String, List<SysAccessRecord>>>>> map =
				list.parallelStream().filter(o -> o.getSiteId() != null)
						.filter(o -> o.getNewVisitor() != null)
						.filter(o -> o.getSorceUrlType() != null)
						.filter(o -> o.getAccessUrl() != null)
						.collect(Collectors.groupingBy(SysAccessRecord::getSiteId,
								Collectors.groupingBy(SysAccessRecord::getNewVisitor,
										Collectors.groupingBy(SysAccessRecord::getSorceUrlType,
												Collectors.groupingBy(SysAccessRecord::getAccessUrl)))));
		List<StatisticsAccessPage> accessPages = new ArrayList<>();
		//站点分组
		for (Integer siteId : map.keySet()) {
			Map<Boolean, Map<Integer, Map<String, List<SysAccessRecord>>>> siteMap = map.get(siteId);
			//是否新客户分组
			for (Boolean newVisitor : siteMap.keySet()) {
				Map<Integer, Map<String, List<SysAccessRecord>>> newVisitorMap = siteMap.get(newVisitor);
				//来源类型分组
				for (Integer sourceType : newVisitorMap.keySet()) {
					Map<String, List<SysAccessRecord>> sourceTypeMap = newVisitorMap.get(sourceType);
					//访问地址分组
					for (String url : sourceTypeMap.keySet()) {
						List<SysAccessRecord> records = sourceTypeMap.get(url);
						StatisticsAccessPage bean = new StatisticsAccessPage();
						bean.setSiteId(siteId);
						bean.setNewVisitor(newVisitor);
						bean.setSourceType(sourceType);
						bean.setUrl(url);
						bean.setStatisticsDay(MyDateUtils.formatDate(beginTime, COM_Y_M_D_PATTERN));
						bean.setPvs(records.size());
						bean.setUvs(StatisticsServiceUtils.uvCalculation(records));
						bean.setAccessHoureLong(Integer.valueOf(String.valueOf(getAccessPageDuration(records))));
						bean.setOnlyOnePv(onlyOne(records));
						Map<String, List<SysAccessRecord>> recordMap =
								list.parallelStream().collect(Collectors.groupingBy(SysAccessRecord::getAccessUrl));
						String accessUrl = "";
						//第一个就是入口页面
						int num = 1;
						for (String access : recordMap.keySet()) {
							if (num == 1) {
								accessUrl = access;
							}
							if (access.equalsIgnoreCase(url)) {
								break;
							} else {
								num++;
							}
						}
						bean.setFlows(recordMap.size() - num);
						bean.setUrlType(accessUrl.equalsIgnoreCase(url) ? 2 : 1);
						accessPages.add(bean);
					}
				}
			}
		}
		super.saveAll(accessPages);
	}

	@Override
	public StatisticsAccessPageVo accessPage(Date beginTime, Date endTime,
											 Integer siteId,
											 Boolean newVisitor,
											 Integer sorceUrlType,
											 int sortType, String url,
											 Pageable pageable) throws IllegalAccessException {
		endTime = (endTime != null) ? MyDateUtils.getFinallyDate(endTime) : null;
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				return accessPageHistory(beginTime, endTime, siteId,
						sorceUrlType, newVisitor, sortType, url, pageable);
			}
		}
		List<SysAccessRecord> list = accessRecordService.getAccessPage(
				beginTime, endTime, siteId, newVisitor, sorceUrlType);
		int pvs = list.size();
		int uvs = StatisticsServiceUtils.uvCalculation(list);
		StatisticsAccessPageVo vo = new StatisticsAccessPageVo();
		vo.setPvs(pvs);
		vo.setUvs(uvs);
		//根据cookies分组
		Map<String, List<SysAccessRecord>> map = list.parallelStream()
				.filter(o -> o.getCookieId() != null)
				.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getCookieId));
		vo.setPageViews(pvs - map.size());
		long average = 0;
		for (List<SysAccessRecord> records : map.values()) {
			average += Math.abs(getAccessPageDuration(records));
		}
		vo.setAverage(average);
		vo.setAverageTime(StatisticsServiceUtils.diffTime(average));
		//根据访问地址分组
		Stream<SysAccessRecord> stream = list.parallelStream()
				.filter(o -> o.getAccessUrl() != null);
		if (url != null) {
			stream = stream.filter(o -> o.getAccessUrl().contains(url));
		}
		Map<String, List<SysAccessRecord>> accessUrlMap = stream
				.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
				.collect(Collectors.groupingBy(SysAccessRecord::getAccessUrl));
		List<StatisticsPageListVo> voList = new ArrayList<StatisticsPageListVo>();
		for (String accessUrl : accessUrlMap.keySet()) {
			StatisticsPageListVo pageVo = new StatisticsPageListVo();
			List<SysAccessRecord> accessRecords = accessUrlMap.get(accessUrl);
			int pv = accessRecords.size();
			int uv = StatisticsServiceUtils.uvCalculation(accessRecords);
			pageVo.setUrl(accessUrl);
			pageVo.setPvs(pv);
			pageVo.setUvs(uv);
			//根据session分组
			Map<String, List<SysAccessRecord>> collect = accessRecords.parallelStream()
					.filter(o -> o.getSessionId() != null)
					.sorted(Comparator.comparing(SysAccessRecord::getCreateTime))
					.collect(Collectors.groupingBy(SysAccessRecord::getSessionId));
			int num = 1;
			for (String session : collect.keySet()) {
				String access = collect.get(session).get(0).getAccessUrl();
				if (access.equalsIgnoreCase(accessUrl)) {
					break;
				} else {
					num++;
				}
			}
			pageVo.setProportion(MathUtil.div(new BigDecimal(pv * 100),
					new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
			int pageViews = accessRecords.size() - num;
			pageVo.setPageViews(pageViews);
			for (List<SysAccessRecord> records : collect.values()) {
				average += Math.abs(getAccessPageDuration(records));
			}
			pageVo.setAverage(average);
			pageVo.setAverageTime(StatisticsServiceUtils.diffTime(average));
			voList.add(pageVo);
		}
		//排序
		voList = sortAccess(sortType, voList);
		vo.setPage(listToPage(voList, pageable));
		return vo;
	}


	@Override
	public StatisticsEntryPageVo entrancePage(Date beginTime, Date endTime,
											  Integer siteId, int sortType, String url,
											  Pageable pageable) throws IllegalAccessException {
		endTime = (endTime != null) ? MyDateUtils.getFinallyDate(endTime) : null;
		if (beginTime != null) {
			if (!MyDateUtils.isInDate(beginTime, Calendar.getInstance().getTime())) {
				return entryPageHistory(beginTime, endTime, siteId, sortType, url, pageable);
			}
		}
		StatisticsEntryPageVo vo = new StatisticsEntryPageVo();
		List<SysAccessRecord> list = accessRecordService.getAccessPage(
				beginTime, endTime, siteId, null, null);
		//获取cookie不为空的
		List<SysAccessRecord> accessRecords = list.parallelStream()
				.filter(o -> o.getCookieId() != null)
				.collect(Collectors.toList());
		Map<String, List<SysAccessRecord>> map = accessRecords.parallelStream()
				.collect(Collectors.groupingBy(SysAccessRecord::getCookieId));
		int uvs = map.size();
		vo.setUvs(uvs);
		vo.setPageViews(accessRecords.size() - uvs);
		//入口页面
		Set<String> entrance = new HashSet<String>();
		for (List<SysAccessRecord> value : map.values()) {
			entrance.add(value.get(0).getAccessUrl());
		}
		long averageTotal = 0;
		long pvTotal = 0;
		BigDecimal bounceTotla = BigDecimal.ZERO;
		List<StatisticsPageListVo> pageListVos = new ArrayList<StatisticsPageListVo>(entrance.size());
		for (String s : entrance) {
			StatisticsPageListVo pageVo = new StatisticsPageListVo();
			//跳出次数
			int bounceCount = 0;
			int uv = 0;
			long pv = 0;
			//贡献下游浏览量
			int pageViews = 0;
			//平均访问时长
			long average = 0;
			for (List<SysAccessRecord> value : map.values()) {
				if (s.equalsIgnoreCase(value.get(0).getAccessUrl())) {
					//如果长度为1则表示需要增加跳出率
					if (value.size() == 1) {
						bounceCount++;
					} else {
						Date end = value.get(1).getCreateTime();
						Date begin = value.get(0).getCreateTime();
						average += Math.abs((end.getTime() - begin.getTime()) / 1000);
					}
					uv++;
					pageViews += value.size() - 1;
					pv += value.parallelStream().filter(o -> o.getAccessUrl().equalsIgnoreCase(s)).count();
				}
			}
			pvTotal += pv;
			averageTotal += average;
			pageVo.setUrl(s);
			BigDecimal bounce = MathUtil.div(new BigDecimal(bounceCount * 100),
					new BigDecimal(map.size()), MathUtil.SCALE_LEN_COMMON);
			bounceTotla = MathUtil.add(bounce, bounceTotla);
			pageVo.setBounce(bounce);
			pageVo.setUvs(uv);
			pageVo.setPvs((int) pv);
			pageVo.setPageViews(pageViews);
			pageVo.setAverage(average);
			pageVo.setAverageTime(StatisticsServiceUtils.diffTime(average));
			pageVo.setProportion(MathUtil.div(new BigDecimal(uv * 100),
					new BigDecimal(uvs), MathUtil.SCALE_LEN_COMMON));
			pageListVos.add(pageVo);
		}
		vo.setBounce(MathUtil.div(bounceTotla,
				new BigDecimal(map.size()), MathUtil.SCALE_LEN_COMMON));
		long ave = map.size() != 0 ? averageTotal / map.size() : 0;
		vo.setAverage(ave);
		vo.setPvs((int) pvTotal);
		vo.setAverageTime(StatisticsServiceUtils.diffTime(ave));
		pageListVos = pageListVos.parallelStream()
				.filter(o -> o.getUrl().contains(url == null ? "" : url))
				.collect(Collectors.toList());
		pageListVos = sort(sortType, pageListVos);
		vo.setPage(listToPage(pageListVos, pageable));
		return vo;
	}


	/**
	 * 受访分析（受访页面）历史
	 *
	 * @param beginTime    开始时间
	 * @param endTime      结束时间
	 * @param siteId       站点id
	 * @param sorceUrlType 来源网站类型 （1-搜索引擎 2-外部链接 3-直接访问）
	 * @param newVisitor   true 新客户 false 老客户
	 * @param sortType     – 0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 平均停留时长正序 7 平均停留时长倒序
	 * @return StatisticsAccessPageVo
	 */
	private StatisticsAccessPageVo accessPageHistory(Date beginTime, Date endTime,
													 Integer siteId, Integer sorceUrlType,
													 Boolean newVisitor, int sortType, String url,
													 Pageable pageable) throws IllegalAccessException {
		StatisticsAccessPageVo vo = new StatisticsAccessPageVo();
		List<StatisticsAccessPage> list = dao.getList(beginTime, endTime,
				siteId, sorceUrlType, newVisitor, URL_TYPE_ACCESS);
		int pvs = list.parallelStream().filter(o -> o.getPvs() != null)
				.mapToInt(StatisticsAccessPage::getPvs).sum();
		int uvs = list.parallelStream().filter(o -> o.getUvs() != null)
				.mapToInt(StatisticsAccessPage::getUvs).sum();
		int pageViews = list.parallelStream().filter(o -> o.getFlows() != null)
				.mapToInt(StatisticsAccessPage::getFlows).sum();
		long averages = list.parallelStream().filter(o -> o.getAccessHoureLong() != null)
				.mapToInt(StatisticsAccessPage::getAccessHoureLong).sum();
		vo.setPvs(pvs);
		vo.setUvs(uvs);
		vo.setPageViews(pageViews);
		averages = Math.abs(pvs == 0 ? 0 : averages / pvs);
		vo.setAverage(averages);
		vo.setAverageTime(StatisticsServiceUtils.diffTime(averages));
		url = url == null ? "" : url;
		String finalUrl = url;
		Map<String, List<StatisticsAccessPage>> map = list.parallelStream().filter(o -> o.getUrl() != null)
				.filter(o -> o.getUrl().contains(finalUrl))
				.collect(Collectors.groupingBy(StatisticsAccessPage::getUrl));
		List<StatisticsPageListVo> listVos = new ArrayList<StatisticsPageListVo>(map.size());
		for (List<StatisticsAccessPage> accessPages : map.values()) {
			StatisticsPageListVo pageListVo = new StatisticsPageListVo();
			int pv = accessPages.parallelStream().filter(o -> o.getPvs() != null)
					.mapToInt(StatisticsAccessPage::getPvs).sum();
			int uv = accessPages.parallelStream().filter(o -> o.getUvs() != null)
					.mapToInt(StatisticsAccessPage::getUvs).sum();
			int pageView = accessPages.parallelStream().filter(o -> o.getFlows() != null)
					.mapToInt(StatisticsAccessPage::getFlows).sum();
			long average = accessPages.parallelStream().filter(o -> o.getAccessHoureLong() != null)
					.mapToInt(StatisticsAccessPage::getAccessHoureLong).sum();
			pageListVo.setPvs(pv);
			pageListVo.setUvs(uv);
			pageListVo.setPageViews(pageView);
			pageListVo.setProportion(MathUtil.div(new BigDecimal(pageView * 100),
					new BigDecimal(pageViews), MathUtil.SCALE_LEN_COMMON));
			average = Math.abs(pv == 0 ? 0 : average / pv);
			pageListVo.setAverage(average);
			pageListVo.setAverageTime(StatisticsServiceUtils.diffTime(average));
			pageListVo.setUrl(accessPages.get(0).getUrl());
			listVos.add(pageListVo);
		}
		listVos = sortAccess(sortType, listVos);
		vo.setPage(listToPage(listVos, pageable));
		return vo;
	}

	/**
	 * 受访分析（入口页面）历史
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param siteId    站点id
	 * @param sortType  – 0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 平均停留时长正序 7 平均停留时长倒序
	 * @return StatisticsAccessPageVo
	 */
	private StatisticsEntryPageVo entryPageHistory(Date beginTime, Date endTime,
												   Integer siteId,
												   int sortType, String url,
												   Pageable pageable) throws IllegalAccessException {
		StatisticsEntryPageVo vo = new StatisticsEntryPageVo();
		List<StatisticsAccessPage> list = dao.getList(beginTime, endTime, siteId, null, null, URL_TYPE_ENTRY);
		int pvs = list.parallelStream().filter(o -> o.getPvs() != null)
				.mapToInt(StatisticsAccessPage::getPvs).sum();
		int uvs = list.parallelStream().filter(o -> o.getUvs() != null)
				.mapToInt(StatisticsAccessPage::getUvs).sum();
		int pageViews = list.parallelStream().filter(o -> o.getFlows() != null)
				.mapToInt(StatisticsAccessPage::getFlows).sum();
		long averages = list.parallelStream().filter(o -> o.getAccessHoureLong() != null)
				.mapToInt(StatisticsAccessPage::getAccessHoureLong).sum();
		int bounces = list.parallelStream().filter(o -> o.getOnlyOnePv() != null)
				.mapToInt(StatisticsAccessPage::getOnlyOnePv).sum();
		vo.setPvs(pvs);
		vo.setUvs(uvs);
		vo.setPageViews(pageViews);
		averages = Math.abs(pvs == 0 ? 0 : averages / pvs);
		vo.setAverage(averages);
		vo.setAverageTime(StatisticsServiceUtils.diffTime(averages));
		vo.setBounce(MathUtil.div(new BigDecimal(bounces * 100),
				new BigDecimal(pvs), MathUtil.SCALE_LEN_COMMON));
		url = url == null ? "" : url;
		String finalUrl = url;
		Map<String, List<StatisticsAccessPage>> map = list.parallelStream().filter(o -> o.getUrl() != null)
				.filter(o -> o.getUrl().contains(finalUrl))
				.collect(Collectors.groupingBy(StatisticsAccessPage::getUrl));
		List<StatisticsPageListVo> listVos = new ArrayList<StatisticsPageListVo>(map.size());
		for (List<StatisticsAccessPage> accessPages : map.values()) {
			StatisticsPageListVo pageListVo = new StatisticsPageListVo();
			int pv = accessPages.parallelStream().filter(o -> o.getPvs() != null)
					.mapToInt(StatisticsAccessPage::getPvs).sum();
			pageListVo.setPvs(pv);
			int uv = accessPages.parallelStream().filter(o -> o.getUvs() != null)
					.mapToInt(StatisticsAccessPage::getUvs).sum();
			pageListVo.setUvs(uv);
			int pageView = accessPages.parallelStream().filter(o -> o.getFlows() != null)
					.mapToInt(StatisticsAccessPage::getFlows).sum();
			pageListVo.setPageViews(pageView);
			pageListVo.setProportion(MathUtil.div(new BigDecimal(pageView * 100),
					new BigDecimal(pageViews), MathUtil.SCALE_LEN_COMMON));
			long average = accessPages.parallelStream().filter(o -> o.getAccessHoureLong() != null)
					.mapToInt(StatisticsAccessPage::getAccessHoureLong).sum();
			average = pv == 0 ? 0 : Math.abs(average) / pv;
			pageListVo.setAverage(average);
			pageListVo.setAverageTime(StatisticsServiceUtils.diffTime(average));
			int bounce = accessPages.parallelStream().filter(o -> o.getOnlyOnePv() != null)
					.mapToInt(StatisticsAccessPage::getOnlyOnePv).sum();
			pageListVo.setBounce(MathUtil.div(new BigDecimal(bounce * 100),
					new BigDecimal(pv), MathUtil.SCALE_LEN_COMMON));
			pageListVo.setUrl(accessPages.get(0).getUrl());
			listVos.add(pageListVo);
		}
		listVos = sort(sortType, listVos);
		vo.setPage(listToPage(listVos, pageable));
		return vo;
	}

	/**
	 * 得到平均停留时长
	 *
	 * @param list 访问记录列表
	 * @return 平均停留时长
	 */
	private long getAccessPageDuration(List<SysAccessRecord> list) {
		long average = 0;
		long end = list.get(list.size() - 1).getCreateTime().getTime();
		long begin = list.get(0).getCreateTime().getTime();
		long total = Math.abs(end - begin);
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).getAccessUrl().equalsIgnoreCase(list.get(i + 1).getAccessUrl())) {
				list.remove(i + 1);
			}
		}
		int pageNum = list.size();
		if (pageNum > 1) {
			average += total / pageNum - 1;
		}

		return average / 1000;
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
	 * list转分页
	 *
	 * @param list     列表
	 * @param pageable 分页数据
	 * @return PageImpl
	 */
	private PageImpl<StatisticsPageListVo> listToPage(List<StatisticsPageListVo> list, Pageable pageable) {
		int size = list.size();
		list = list.stream()
				.skip(pageable.getPageSize() * (pageable.getPageNumber()))
				.limit(pageable.getPageSize()).collect(Collectors.toList());
		return new PageImpl<StatisticsPageListVo>(list, pageable, size);
	}

	/**
	 * 受访页面排序
	 *
	 * @param sortType 排序类型 0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 平均停留时长正序 7 平均停留时长倒序
	 * @param list     排序列表
	 * @return 排序后列表
	 */
	private List<StatisticsPageListVo> sortAccess(int sortType, List<StatisticsPageListVo> list) {
		switch (sortType) {
			case 1:
				//PV倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPvs).reversed())
						.collect(Collectors.toList());
			case 2:
				//UV正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getUvs))
						.collect(Collectors.toList());
			case 3:
				//UV倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getUvs).reversed())
						.collect(Collectors.toList());
			case 4:
				//贡献下游浏览量正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPageViews))
						.collect(Collectors.toList());
			case 5:
				//贡献下游浏览量倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPageViews)
								.reversed())
						.collect(Collectors.toList());
			case 6:
				//平均停留时长正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getAverage))
						.collect(Collectors.toList());
			case 7:
				//平均停留时长倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getAverage)
								.reversed())
						.collect(Collectors.toList());
			default:
				//PV正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPvs))
						.collect(Collectors.toList());
		}
	}

	/**
	 * 入口页面排序
	 *
	 * @param sortType 排序类型 0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 贡献下游浏览量正序 5 贡献下游浏览量倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param list     排序列表
	 * @return 排序后列表
	 */
	private List<StatisticsPageListVo> sort(int sortType, List<StatisticsPageListVo> list) {
		switch (sortType) {
			case 1:
				//PV倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPvs).reversed())
						.collect(Collectors.toList());
			case 2:
				//UV正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getUvs))
						.collect(Collectors.toList());
			case 3:
				//UV倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getUvs).reversed())
						.collect(Collectors.toList());
			case 4:
				//贡献浏览量正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPageViews))
						.collect(Collectors.toList());
			case 5:
				//贡献浏览量倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPageViews)
								.reversed())
						.collect(Collectors.toList());
			case 6:
				//跳出率正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getBounce))
						.collect(Collectors.toList());
			case 7:
				//跳出率倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getBounce)
								.reversed())
						.collect(Collectors.toList());
			case 8:
				//平均访问时长正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getAverage))
						.collect(Collectors.toList());
			case 9:
				//平均访问时长倒序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getAverage)
								.reversed())
						.collect(Collectors.toList());
			default:
				//PV正序
				return list.parallelStream()
						.sorted(Comparator.comparing(StatisticsPageListVo::getPvs))
						.collect(Collectors.toList());
		}
	}

}