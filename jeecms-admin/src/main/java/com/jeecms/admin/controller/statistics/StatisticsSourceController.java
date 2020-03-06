/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.statistics;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.domain.StatisticsSource;
import com.jeecms.statistics.domain.vo.StatisticsSourceListVo;
import com.jeecms.statistics.domain.vo.StatisticsSourcePieVo;
import com.jeecms.statistics.domain.vo.StatisticsSourceTableVo;
import com.jeecms.statistics.domain.vo.StatisticsSourceVo;
import com.jeecms.statistics.service.StatisticsSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

import static com.jeecms.statistics.service.StatisticsSourceService.EXTERNAL_LINK_DOMAIN;
import static com.jeecms.statistics.service.StatisticsSourceService.EXTERNAL_LINK_URL;

/**
 * 来源统计Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-06-26
 */
@RequestMapping("/statisticsSource")
@RestController
public class StatisticsSourceController extends BaseController<StatisticsSource, Integer> {

	@Autowired
	private StatisticsSourceService service;

	/**
	 * 来源类型统计
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型(1pv 2uv 3 ip)
	 * @return ResponseInfo
	 */
	@GetMapping("/sourceType")
	@MoreSerializeField({@SerializeField(clazz = StatisticsSourceVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "fields", "list", "pie", "objects"}),
			@SerializeField(clazz = StatisticsSourceListVo.class,
					includes = {"pvs", "uvs", "ips", "bounce", "average", "name", "list"}),
			@SerializeField(clazz = StatisticsSourcePieVo.class, includes = {"pv", "uv", "ip",
					"pvPercentage", "uvPercentage", "ipPercentage", "name"}),
			@SerializeField(clazz = StatisticsSourceTableVo.class,
					includes = {"pvs", "uvs", "ips", "pvProportion", "uvProportion",
							"name", "ipProportion", "time"})})
	public ResponseInfo sourceType(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
								   Short accessSourceClient, Integer sortType, Integer showType) throws IllegalAccessException {
		sortType = sortType == null ? 0 : sortType;
		Date nowDate = Calendar.getInstance().getTime();
		beginTime = beginTime != null ? beginTime : nowDate;
		endTime = endTime != null ? endTime : nowDate;
		StatisticsSourceVo vo = service.sourceType(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId, newVisitor, accessSourceClient, sortType, showType);
		return new ResponseInfo(vo);
	}

	/**
	 * 来源网站统计
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型(1pv 2uv 3 ip)
	 * @return ResponseInfo
	 */
	@GetMapping("/sourceUrl")
	@MoreSerializeField({@SerializeField(clazz = StatisticsSourceVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "fields", "pie", "objects"}),
			@SerializeField(clazz = StatisticsSourcePieVo.class, includes = {"pv", "uv", "ip",
					"pvPercentage", "uvPercentage", "ipPercentage", "name"}),
			@SerializeField(clazz = StatisticsSourceTableVo.class,
					includes = {"pvs", "uvs", "ips", "pvProportion", "uvProportion",
							"name", "ipProportion", "time"})})
	public ResponseInfo sourceUrl(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
								  Short accessSourceClient, Integer sortType, Integer showType) throws IllegalAccessException {
		sortType = sortType == null ? 1 : sortType;
		Date nowDate = Calendar.getInstance().getTime();
		beginTime = beginTime != null ? beginTime : nowDate;
		endTime = endTime != null ? endTime : nowDate;
		StatisticsSourceVo vo = service.getBySourceUrl(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId,
				newVisitor, accessSourceClient, sortType, showType);
		return new ResponseInfo(vo);
	}

	/**
	 * 来源网站统计分页列表
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param pageable           分页组件
	 * @return ResponseInfo
	 */
	@GetMapping("/sourceUrl/page")
	@SerializeField(clazz = StatisticsSourceListVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "name", "proportion"})
	public ResponseInfo sourceUrl(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
								  Short accessSourceClient, Integer sortType, @PageableDefault() Pageable pageable) throws IllegalAccessException {
		sortType = sortType == null ? 1 : sortType;
		Date nowDate = Calendar.getInstance().getTime();
		beginTime = beginTime != null ? beginTime : nowDate;
		endTime = endTime != null ? endTime : nowDate;
		Page<StatisticsSourceListVo> vo = service.getBySourceUrl(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId, newVisitor, accessSourceClient, sortType, pageable);
		return new ResponseInfo(vo);
	}

	/**
	 * 搜索引擎统计
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型(1pv 2uv 3 ip)
	 * @return ResponseInfo
	 */
	@GetMapping("/searchEngine")
	@MoreSerializeField({@SerializeField(clazz = StatisticsSourceVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "fields", "list", "pie", "objects"}),
			@SerializeField(clazz = StatisticsSourcePieVo.class, includes = {"pv", "uv", "ip",
					"pvPercentage", "uvPercentage", "ipPercentage", "name"}),
			@SerializeField(clazz = StatisticsSourceTableVo.class,
					includes = {"pvs", "uvs", "ips", "pvProportion", "uvProportion",
							"name", "ipProportion", "total", "time"})})
	public ResponseInfo searchEngine(Date beginTime, Date endTime, Integer siteId, Boolean newVisitor,
									 Short accessSourceClient, Integer sortType, Integer showType) throws IllegalAccessException {
		sortType = sortType == null ? 1 : sortType;
		Date nowDate = Calendar.getInstance().getTime();
		beginTime = beginTime != null ? beginTime : nowDate;
		endTime = endTime != null ? endTime : nowDate;
		StatisticsSourceVo vo = service.searchEngine(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId,
				newVisitor, accessSourceClient, sortType, showType);
		return new ResponseInfo(vo);
	}

	/**
	 * 外部链接统计（域名）
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型(1pv 2uv 3 ip)
	 * @return ResponseInfo
	 */
	@GetMapping("/domain")
	@MoreSerializeField({@SerializeField(clazz = StatisticsSourceVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "fields", "pie", "objects"}),
			@SerializeField(clazz = StatisticsSourcePieVo.class, includes = {"pv", "uv", "ip",
					"pvPercentage", "uvPercentage", "ipPercentage", "name"}),
			@SerializeField(clazz = StatisticsSourceTableVo.class,
					includes = {"pvs", "uvs", "ips", "pvProportion", "uvProportion",
							"name", "ipProportion", "total", "time"})})
	public ResponseInfo domain(Date beginTime, Date endTime, Integer siteId,
							   Boolean newVisitor, Short accessSourceClient,
							   Integer sortType, Integer showType) throws IllegalAccessException {
		StatisticsSourceVo vo = service.externalLink(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId, newVisitor,
				accessSourceClient, sortType, EXTERNAL_LINK_DOMAIN, showType);
		return new ResponseInfo(vo);
	}

	/**
	 * 外部链接统计（域名）
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param domain             域名
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param pageable           分页组件
	 * @return ResponseInfo
	 */
	@GetMapping("/domain/page")
	@SerializeField(clazz = StatisticsSourceListVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "name", "proportion"})
	public ResponseInfo domain(Date beginTime, Date endTime, Integer siteId,
							   Boolean newVisitor, Short accessSourceClient, String domain,
							   Integer sortType, @PageableDefault() Pageable pageable) throws IllegalAccessException {
		Date nowDate = Calendar.getInstance().getTime();
		beginTime = beginTime != null ? beginTime : nowDate;
		endTime = endTime != null ? endTime : nowDate;
		sortType = sortType == null ? 1 : sortType;
		Page<StatisticsSourceListVo> page = service.externalLinkPage(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId, newVisitor,
				accessSourceClient, sortType, domain, EXTERNAL_LINK_DOMAIN, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 外部链接统计（URL）
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param showType           显示类型(1pv 2uv 3 ip)
	 * @return ResponseInfo
	 */
	@GetMapping("/url")
	@MoreSerializeField({@SerializeField(clazz = StatisticsSourceVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "fields", "pie", "objects"}),
			@SerializeField(clazz = StatisticsSourcePieVo.class, includes = {"pv", "uv", "ip",
					"pvPercentage", "uvPercentage", "ipPercentage", "name"}),
			@SerializeField(clazz = StatisticsSourceTableVo.class,
					includes = {"pvs", "uvs", "ips", "pvProportion", "uvProportion",
							"name", "ipProportion", "total", "time"})})
	public ResponseInfo url(Date beginTime, Date endTime, Integer siteId,
							Boolean newVisitor, Short accessSourceClient,
							Integer sortType, Integer showType) throws IllegalAccessException {
		StatisticsSourceVo vo = service.externalLink(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId, newVisitor,
				accessSourceClient, sortType, EXTERNAL_LINK_URL, showType);
		return new ResponseInfo(vo);
	}

	/**
	 * 外部链接统计（URL）
	 *
	 * @param beginTime          开始时间
	 * @param endTime            结束时间
	 * @param siteId             站点id
	 * @param newVisitor         true 新客户 false 老客户
	 * @param domain             url
	 * @param accessSourceClient 访问来源(1计算机  2移动设备)
	 * @param sortType           0 PV正序 1 PV倒序 2 UV正序 3 UV倒序 4 IP数正序 5 IP数倒序 6 跳出率正序 7 跳出率倒序 8 平均访问时长正序 9 平均访问时长倒序
	 * @param pageable           分页组件
	 * @return ResponseInfo
	 */
	@GetMapping("/url/page")
	@SerializeField(clazz = StatisticsSourceListVo.class,
			includes = {"pvs", "uvs", "ips", "bounce", "average", "name", "proportion"})
	public ResponseInfo url(Date beginTime, Date endTime, Integer siteId,
							Boolean newVisitor, Short accessSourceClient, String domain,
							Integer sortType, @PageableDefault() Pageable pageable) throws IllegalAccessException {
		sortType = sortType == null ? 1 : sortType;
		Date nowDate = Calendar.getInstance().getTime();
		beginTime = beginTime != null ? beginTime : nowDate;
		endTime = endTime != null ? endTime : nowDate;
		Page<StatisticsSourceListVo> page = service.externalLinkPage(MyDateUtils.getStartDate(beginTime),
				MyDateUtils.getFinallyDate(endTime), siteId, newVisitor,
				accessSourceClient, sortType, domain, EXTERNAL_LINK_URL, pageable);
		return new ResponseInfo(page);
	}

}



