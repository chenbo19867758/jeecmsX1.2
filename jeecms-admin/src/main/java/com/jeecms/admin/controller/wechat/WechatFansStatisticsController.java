/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.wechat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFansStatistics;
import com.jeecms.wechat.domain.dto.WechatFansStatisticsDto;
import com.jeecms.wechat.domain.vo.WechatFansVO;
import com.jeecms.wechat.domain.vo.WechatStatisticsVO;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatFansStatisticsService;

/**
 * 统计用户控制器
 * 
 * @author: ljw
 * @date: 2019年5月29日 下午4:43:54
 */
@RequestMapping(value = "/statistics")
@RestController
public class WechatFansStatisticsController extends BaseController<WechatFansStatistics, Integer> {

	@Autowired
	private WechatFansStatisticsService wechatFansStatisticsService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	
	/**
	 * 用户增长，今日昨日数据
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping()
	@MoreSerializeField({ @SerializeField(clazz = WechatFansStatistics.class, includes = { "newUser", "cancelUser",
			"netGrowthUser", "countUser" }), })
	public ResponseInfo increase(HttpServletRequest request, String appId) throws GlobalException {
		// appId为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(wechatFansStatisticsService.getFansData(siteId, appId));
	}

	/**
	 * 用户增长统计汇总
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/collect")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansStatistics.class, includes = { "newUser", "cancelUser",
			"netGrowthUser", "countUser", "name" }), })
	public ResponseInfo collect(HttpServletRequest request, String appId) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(wechatFansStatisticsService.getFansData(siteId, appId));
	}

	/**
	 * 趋势图
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param dto     传输对象
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@PostMapping("/map")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansStatistics.class, includes = { "newUser", "cancelUser",
			"netGrowthUser", "countUser", "statisticsDate" }), })
	public ResponseInfo map(HttpServletRequest request, @RequestBody WechatFansStatisticsDto dto)
			throws GlobalException {
		// appId为空直接返回
		if (!StringUtils.isNotBlank(dto.getAppid())) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 默认传入siteId
		dto.setSiteId(siteId);
		return new ResponseInfo(wechatFansStatisticsService.mapData(dto));
	}

	/**
	 * 趋势汇总统计
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param dto     传输对象
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@PostMapping("/map/collect")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansStatistics.class, includes = { "newUser", "cancelUser",
			"netGrowthUser", "countUser", "statisticsDate" }), })
	public ResponseInfo mapCollect(HttpServletRequest request, @RequestBody WechatFansStatisticsDto dto)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 默认传入siteId
		dto.setSiteId(siteId);
		return new ResponseInfo(wechatFansStatisticsService.mapData(dto));
	}

	/**
	 * 按公众号查看
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param dto     传输对象
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/tencent")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansStatistics.class, includes = { "newUser", "cancelUser",
			"netGrowthUser", "countUser", "statisticsDate", }), })
	public ResponseInfo tencent(HttpServletRequest request, @RequestBody WechatFansStatisticsDto dto)
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 默认传入siteId
		dto.setSiteId(siteId);
		return new ResponseInfo(wechatFansStatisticsService.tencentData(dto));
	}

	/**
	 * 用户属性（性别）
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/sex")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion"}), })
	public ResponseInfo sexlga(HttpServletRequest request, Boolean order,
			String appId) throws Exception {
		// appId为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.sex(siteId, appId);
		if (order != null && !order) {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
					.collect(Collectors.toList());
		} else {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
					.collect(Collectors.toList());
		}
		return new ResponseInfo(vos);
	}

	/**
	 * 用户属性（语言）
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/lga")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion" }), })
	public ResponseInfo lga(HttpServletRequest request, Boolean order,
			String appId) throws Exception {
		// appId为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.lga(siteId, appId);
		if (order != null && !order) {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
					.collect(Collectors.toList());
		} else {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
					.collect(Collectors.toList());
		}
		return new ResponseInfo(vos);
	}

	/**
	 * 用户属性（性别）汇总
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/sex/collect")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion" }), })
	public ResponseInfo sexCollect(HttpServletRequest request, Boolean order,
			String appId) throws Exception {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.sex(siteId, appId);
		if (order != null && !order) {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
					.collect(Collectors.toList());
		} else {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
					.collect(Collectors.toList());
		}
		return new ResponseInfo(vos);
	}

	/**
	 * 用户属性（语言）汇总
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/lga/collect")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion" }), })
	public ResponseInfo lgaCollect(HttpServletRequest request, Boolean order,
			String appId) throws Exception {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.lga(siteId, appId);
		if (order != null && !order) {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
					.collect(Collectors.toList());
		} else {
			vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
					.collect(Collectors.toList());
		}
		return new ResponseInfo(vos);
	}

	/**
	 * 用户属性（省份）
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param order 排序方式，true升序，false降序
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/province")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion" }), })
	public ResponseInfo province(HttpServletRequest request, String appId, 
			Boolean order, Pageable pageable)
			throws Exception {
		// appId为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.province(siteId, appId);
		PageImpl<WechatFansVO> page = null;
		if (vos != null) {
			if (order != null && order) {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
						.collect(Collectors.toList());
			}
			Integer sum = vos.size();
			vos = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
			page = new PageImpl<WechatFansVO>(vos, pageable, sum);
		}
		return new ResponseInfo(page);
	}

	/**
	 * 用户属性（省份）汇总
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/province/collect")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion" }), })
	public ResponseInfo provinceCollect(HttpServletRequest request, String appId, 
			Boolean order, Pageable pageable) throws Exception {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.province(siteId, appId);
		PageImpl<WechatFansVO> page = null;
		if (vos != null) {
			if (order != null && !order) {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
						.collect(Collectors.toList());
			}
			Integer sum = vos.size();
			vos = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
			page = new PageImpl<WechatFansVO>(vos, pageable, sum);
		} 
		return new ResponseInfo(page);
	}

	/**
	 * 用户属性（城市）
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/city")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number",
			"proportion" }), })
	public ResponseInfo city(HttpServletRequest request, String appId,
			Boolean order, String provinceName, Pageable pageable) throws Exception {
		// appId为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.city(siteId, appId, provinceName);
		PageImpl<WechatFansVO> page = null;
		if (vos != null) {
			if (order != null && !order) {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
						.collect(Collectors.toList());
			}
			Integer sum = vos.size();
			vos = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
			page = new PageImpl<WechatFansVO>(vos, pageable, sum);
		} 
		return new ResponseInfo(page);
	}

	/**
	 * 用户属性（城市）汇总
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/city/collect")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion" }), })
	public ResponseInfo cityCollect(HttpServletRequest request, Boolean order, 
			String appId, String provinceName, Pageable pageable) throws Exception {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.city(siteId, appId, provinceName);
		PageImpl<WechatFansVO> page = null;
		if (vos != null) {
			if (order != null && !order) {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber).reversed())
						.collect(Collectors.toList());
			} else {
				vos = vos.stream().sorted(Comparator.comparing(WechatFansVO::getNumber))
						.collect(Collectors.toList());
			}
			Integer sum = vos.size();
			vos = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
			page = new PageImpl<WechatFansVO>(vos, pageable, sum);
		}
		return new ResponseInfo(page);
	}

	/**
	 * 下载表格
	 * 
	 * @Title: download
	 * @param request 请求
	 * @param dto     传输DTO
	 * @throws GlobalException 异常
	 */
	@PostMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WechatFansStatisticsDto dto) throws GlobalException, IOException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		// 默认传入siteId
		dto.setSiteId(siteId);
		Workbook workbook = wechatFansStatisticsService.export(dto);
		ResponseUtils.exportExcel(request, response, "用户统计", workbook);
	}
	
	/**
	 * 用户增长列表分页
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId  公众号APPID
	 * @param beginDate  开始时间
	 * @param endDate  结束时间
	 * @throws GlobalException 异常
	 */
	@GetMapping("/page")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansStatistics.class, 
			includes = { "statisticsDates", "newUser", "cancelUser", "netGrowthUser", "countUser" }), })
	public ResponseInfo page(HttpServletRequest request, 
			String appId, Date beginDate, Date endDate,  
			Integer sortType, Boolean order,
			Pageable pageable) 
			throws GlobalException {
		Map<String, String[]> params = super.getCommonParams(request);
		if (beginDate != null && endDate != null) {
			String start = MyDateUtils.formatDate(MyDateUtils.getStartDate(beginDate), 
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
			String end = MyDateUtils.formatDate(MyDateUtils.getFinallyDate(endDate), 
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
			// 计算时间范围浏览记录
			params.put("GTE_statisticsDate_Timestamp", new String[] { start });
			params.put("LTE_statisticsDate_Timestamp", new String[] { end });
		}
		// appId为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		params.put("EQ_appId_String", new String[] { appId });
		Page<WechatFansStatistics> page = service.getPage(params, pageable, true);
		List<WechatFansStatistics> list = page.getContent();
		if (!list.isEmpty()) {
			list = sort(list, sortType, order);
		}
		return new ResponseInfo(new PageImpl<WechatFansStatistics>(list, 
				pageable, page.getTotalElements()));
	}
	
	/**
	 * 用户增长列表分页汇总
	 * 
	 * @Title: pageSum
	 * @param request 请求
	 * @param beginDate  开始时间
	 * @param endDate  结束时间
	 * @throws GlobalException 异常
	 */
	@GetMapping("/pageSum")
	@MoreSerializeField({ @SerializeField(clazz = WechatStatisticsVO.class, 
			includes = { "statisticsDates", "newUser", "cancelUser", "netGrowthUser", "countUser" }), })
	public ResponseInfo pageSum(HttpServletRequest request, 
			Date beginDate, Date endDate, 
			Integer sortType, Boolean order,
			Pageable pageable) 
			throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> appids = new ArrayList<String>(10);
		Map<String, String[]> param = new HashMap<String, String[]>(10);
		param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
		List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
		// 站点没有APPID，直接返回
		if (abs.isEmpty()) {
			return new ResponseInfo();
		}
		if (beginDate != null && endDate != null) {
			beginDate = MyDateUtils.getStartDate(beginDate);
			endDate = MyDateUtils.getFinallyDate(endDate);
		}
		for (AbstractWeChatInfo ab : abs) {
			appids.add(ab.getAppId());
		} 
		Page<WechatStatisticsVO> page = wechatFansStatisticsService.pages(beginDate, 
				endDate, appids, pageable);
		List<WechatStatisticsVO> list = page.getContent();
		if (!list.isEmpty()) {
			list = sortVo(list, sortType, order);
		}
		return new ResponseInfo(new PageImpl<WechatStatisticsVO>(list, 
				pageable, page.getTotalElements()));
	}
	
	
	/**
	 * 用户属性（省份）列表
	 * 
	 * @Title: page
	 * @param request 请求
	 * @param appId   微信公众号ID
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping("/province/list")
	@MoreSerializeField({ @SerializeField(clazz = WechatFansVO.class, includes = { "name", "number", 
			"proportion"}), })
	public ResponseInfo provinceList(HttpServletRequest request, String appId) throws Exception {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<WechatFansVO> vos = wechatFansStatisticsService.province(siteId, appId);
		return new ResponseInfo(vos);
	}
	
	/**
	 * 排序
	* @Title: sort 
	 */
	public List<WechatFansStatistics> sort(List<WechatFansStatistics> list, Integer sortType, Boolean order) {
		if (sortType != null && order != null) {
			//日期,true正序 false倒序
			if (sortType == WechatFansStatistics.ORDER_TYPE_DATE) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getStatisticsDate))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getStatisticsDate).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_NEW) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getNewUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getNewUser).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_QUIT) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getCancelUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getCancelUser).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_NORMAL) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getNetGrowthUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getNetGrowthUser).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_SUM) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getCountUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatFansStatistics::getCountUser).reversed())
							.collect(Collectors.toList());
				}
			}
		}
		return list;
	}
	
	/**
	 * 排序
	* @Title: sort 
	 */
	public List<WechatStatisticsVO> sortVo(List<WechatStatisticsVO> list, Integer sortType, Boolean order) {
		if (sortType != null && order != null) {
			//日期,true正序 false倒序
			if (sortType == WechatFansStatistics.ORDER_TYPE_DATE) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getStatisticsDate))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getStatisticsDate).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_NEW) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getNewUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getNewUser).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_QUIT) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getCancelUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getCancelUser).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_NORMAL) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getNetGrowthUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getNetGrowthUser).reversed())
							.collect(Collectors.toList());
				}
			} else if (sortType == WechatFansStatistics.ORDER_TYPE_SUM) {
				if (order) {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getCountUser))
							.collect(Collectors.toList());
				} else {
					list = list.stream().sorted(Comparator
							.comparing(WechatStatisticsVO::getCountUser).reversed())
							.collect(Collectors.toList());
				}
			}
		}
		return list;
	}
	
}