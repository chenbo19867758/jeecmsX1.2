/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MathUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.UserStatisticsApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.userstatistics.UserStatistics;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserCumulateResponse;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserCumulateResponse.UserCountResult;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserSummaryResponse;
import com.jeecms.common.wechat.bean.response.mp.userstatistics.UserSummaryResponse.UserSummaryResult;
import com.jeecms.statistics.domain.vo.AccessAreaVo;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.dao.WechatFansStatisticsDao;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFansStatistics;
import com.jeecms.wechat.domain.dto.WechatFansStatisticsDto;
import com.jeecms.wechat.domain.vo.WechatFansVO;
import com.jeecms.wechat.domain.vo.WechatStatisticsVO;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatFansService;
import com.jeecms.wechat.service.WechatFansStatisticsService;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 用户数据统计
 * 
 * @author ljw
 * @date 2018年8月3日
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class WechatFansStatisticsServiceImpl extends
		BaseServiceImpl<WechatFansStatistics, WechatFansStatisticsDao, Integer> 
		implements WechatFansStatisticsService {

	@Autowired
	private UserStatisticsApiService apiService;
	@Autowired
	private WechatFansService wechatFansService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;

	@Override
	@ValidWeChatToken(Const.ValidTokenType.ACCESS_TOKEN)
	public void saveFansStatistics(UserStatistics statistics, ValidateToken validateToken) throws GlobalException {
		// 查询出获取用户增减数据
		UserSummaryResponse response = apiService.getUserSummary(statistics, validateToken);
		// 获取累计用户数据
		UserCumulateResponse getusercumulateResponse = apiService.getUserCumulate(statistics, validateToken);
		// 将数据封装到实体类中
		WechatFansStatistics fansStatistics = new WechatFansStatistics();
		
		fansStatistics.setAppId(validateToken.getAppId());
		List<UserSummaryResult> userSummaryList = response.getList();
		List<UserCountResult> userCumulateList = getusercumulateResponse.getList();
		// 遍历用户增减数据的数据集合
		if (userSummaryList.isEmpty()) {
			fansStatistics.setNewUser(0);
			fansStatistics.setCancelUser(0);
			fansStatistics.setNetGrowthUser(0);
		}
		for (UserSummaryResult list : userSummaryList) {
			//用户来源
			fansStatistics.setUserSource(list.getUserSource());
			// 新关注的用户的数量
			fansStatistics.setNewUser(list.getNewUser());
			// 取消关注的用户的数量
			fansStatistics.setCancelUser(list.getCancelUser());
			// 净关注的用户的数量=新关注的用户的数量-取消关注的用户的数量
			fansStatistics.setNetGrowthUser(list.getNewUser() - list.getCancelUser());
		}
		// 遍历用户累计的数据的集合
		for (UserCountResult list : userCumulateList) {
			fansStatistics.setStatisticsDate(StringUtils.isNotBlank(list.getRefDate()) 
					? MyDateUtils.parseDate(list.getRefDate()) : null);
			// 前一天累计的用户的数量
			fansStatistics.setCountUser(list.getCumulateUser());
		}
		// 保存了前一天的数据
		super.save(fansStatistics);
		super.flush();
	}

	/**
	 * 今天的粉丝的数据(非定时任务)
	 * 
	 */
	@Override
	public JSONArray getFansData(Integer siteId, String appid) throws GlobalException {
		// 获取到当天0点的时间
		Date startTime = MyDateUtils.getStartDate(new Date());
		// 获取到数据库中前一天的数据
		Date startYes = MyDateUtils.getStartDate(MyDateUtils.getSpecficDate(new Date(), -1));
		Date endYes = MyDateUtils.getFinallyDate(MyDateUtils.getSpecficDate(new Date(), -1));
		JSONArray array = new JSONArray();
		if (StringUtils.isNotBlank(appid)) {
			WechatFansStatistics yesterday = new WechatFansStatistics();
			WechatFansStatistics today = new WechatFansStatistics();
			// 查询粉丝总数
			Long fansSum = wechatFansService.fansCount(appid);
			// 查询今天关注的粉丝数
			Integer todaySum = wechatFansService.timeCount(startTime.getTime(), 
					new Date().getTime(), appid);
			// 查询昨日数据
			List<WechatFansStatistics> fans = dao.selectFans(Arrays.asList(appid), startYes, endYes);
			// 如果没有数据，将空数据转换成0
			if (fans.isEmpty()) {
				yesterday.setAppId(appid);
				yesterday.setNewUser(0);
				yesterday.setCancelUser(0);
				yesterday.setNetGrowthUser(0);
				yesterday.setCountUser(fansSum.intValue());
			} else {
				yesterday = fans.get(0);
			}
			// 昨天累计的用户的数量
			Integer old = yesterday.getCountUser();
			// 净关注人数=今天总数-昨天总数
			Integer netConcern = fansSum.intValue() - old;
			// 今天取消关注人数=今天关注数-今天净关注数
			Integer cancel = todaySum - netConcern;
			today.setAppId(appid);
			today.setCancelUser(cancel);
			today.setNewUser(todaySum);
			today.setNetGrowthUser(netConcern);
			today.setCountUser(fansSum.intValue());
			JSONObject object = new JSONObject();
			object.put("today", today);
			object.put("yesterday", yesterday);
			array.add(object);
		} else {
			// 查询该站点已授权的公众号信息
			Map<String, String[]> params = new HashMap<String, String[]>(10);
			params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(params, null, true);
			//过滤小程序
			abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
			.collect(Collectors.toList());
			// 遍历公众号，汇总数据
			for (AbstractWeChatInfo ab : abs) {
				WechatFansStatistics yesterday = new WechatFansStatistics();
				WechatFansStatistics today = new WechatFansStatistics();
				yesterday.setName(ab.getWechatName());
				today.setName(ab.getWechatName());
				// 查询粉丝总数
				Long fansSum = wechatFansService.fansCount(ab.getAppId());
				// 查询今天关注的粉丝数
				Integer todaySum = wechatFansService.timeCount(startTime.getTime(), 
						new Date().getTime(), ab.getAppId());
				// 查询昨日数据
				List<WechatFansStatistics> fansStatistics = 
						dao.selectFans(Arrays.asList(ab.getAppId()), 
						startYes, endYes);
				// 如果没有数据，将空数据转换成0
				if (fansStatistics.isEmpty()) {
					yesterday.setAppId(appid);
					yesterday.setNewUser(0);
					yesterday.setCancelUser(0);
					yesterday.setNetGrowthUser(0);
					yesterday.setCountUser(fansSum.intValue());
				} else {
					yesterday = fansStatistics.get(0);
				}
				// 昨天累计的用户的数量
				Integer old = yesterday.getCountUser();
				// 净关注人数=今天总数-昨天总数
				Integer netConcern = fansSum.intValue() - old;
				// 今天取消关注人数=今天关注数-今天净关注数
				Integer cancel = todaySum - netConcern;
				today.setAppId(appid);
				today.setCancelUser(cancel);
				today.setNewUser(todaySum);
				today.setNetGrowthUser(netConcern);
				today.setCountUser(fansSum.intValue());
				JSONObject object = new JSONObject();
				object.put("today", today);
				object.put("yesterday", yesterday);
				array.add(object);
			}
		}
		return array;
	}
	
	@Override
	public JSONArray mapData(WechatFansStatisticsDto dto) throws GlobalException {
		// 今日新增人数累加器
		LongAdder newAdder = new LongAdder();
		// 取消人数累加器
		LongAdder quitAdder = new LongAdder();
		// 净增人数累加器
		LongAdder addAdder = new LongAdder();
		// 总人数累加器
		LongAdder sumAdder = new LongAdder();
		// 如果前台日期传值为空，默认七天的数据
		if (dto.getBeginDate() == null || dto.getEndDate() == null) {
			dto.setBeginDate(MyDateUtils.getStartDate(MyDateUtils.getDayAfterTime(new Date(), -7)));
			dto.setEndDate(MyDateUtils.getFinallyDate(MyDateUtils.getDayAfterTime(new Date(), -1)));
		}
		List<String> list = MyDateUtils.getDays(MyDateUtils.formatDate(dto.getBeginDate()),
				MyDateUtils.formatDate(dto.getEndDate()));
		// 查询该站点已授权的公众号信息
		Map<String, String[]> params = new HashMap<String, String[]>(10);
		params.put("EQ_siteId_Integer", new String[] { dto.getSiteId().toString() });
		List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(params, null, true);
		//过滤小程序
		abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
		.collect(Collectors.toList());
		JSONArray array = new JSONArray();
		// 如果APPID为空，遍历该站点全部授权公众号
		if (StringUtils.isNotBlank(dto.getAppid())) {
			// 查询微信统计
			List<WechatFansStatistics> fansStatistics = 
					dao.selectFans(Arrays.asList(dto.getAppid()), dto.getBeginDate(),
					dto.getEndDate());
			if (dto.getUserSource() != null) {
				fansStatistics = fansStatistics.stream()
					.filter(x -> x.getUserSource().equals(dto.getUserSource()))
					.collect(Collectors.toList());
			}
			for (String date : list) {
				WechatFansStatistics obj = new WechatFansStatistics();
				List<WechatFansStatistics> wfs = fansStatistics.stream().filter(x ->
						MyDateUtils.formatDate(x.getStatisticsDate()).equals(date))
						.collect(Collectors.toList());
				if (wfs.isEmpty()) {
					obj.setAppId(dto.getAppid());
					obj.setNewUser(0);
					obj.setCancelUser(0);
					obj.setNetGrowthUser(0);
					obj.setCountUser(0);
					obj.setStatisticsDate(MyDateUtils.parseDate(date));
				} else {
					obj = wfs.get(0);
				}
				JSONObject object = new JSONObject();
				object.put("date", date);
				object.put("info", obj);
				array.add(object);
			}
		} else {
			// 根据时间查询微信统计数据
			List<WechatFansStatistics> fansStatistics = dao.selectFansDate(dto.getBeginDate(), 
					dto.getEndDate());
			if (dto.getUserSource() != null) {
				fansStatistics = fansStatistics.stream()
						.filter(x -> x.getUserSource().equals(dto.getUserSource()))
						.collect(Collectors.toList());
			}
			for (String date : list) {
				WechatFansStatistics obj = new WechatFansStatistics();
				List<WechatFansStatistics> fansList = new ArrayList<WechatFansStatistics>(10);
				// 重新组成新集合
				for (AbstractWeChatInfo ab : abs) {
					List<WechatFansStatistics> wfs = fansStatistics.stream()
							.filter(x -> MyDateUtils.formatDate(x.getStatisticsDate())
									.equals(date))
							.filter(x -> x.getAppId().equals(ab.getAppId()))
							.collect(Collectors.toList());
					fansList.addAll(wfs);
				}
				if (fansList.isEmpty()) {
					obj.setAppId(dto.getAppid());
					obj.setNewUser(0);
					obj.setCancelUser(0);
					obj.setNetGrowthUser(0);
					obj.setCountUser(0);
					obj.setStatisticsDate(MyDateUtils.parseDate(date));
				} else {
					for (WechatFansStatistics fans : fansList) {
						newAdder.add(fans.getNewUser());
						quitAdder.add(fans.getCancelUser());
						addAdder.add(fans.getNetGrowthUser());
						sumAdder.add(fans.getCountUser());
					}
					obj.setCancelUser(quitAdder.intValue());
					obj.setNewUser(newAdder.intValue());
					obj.setNetGrowthUser(addAdder.intValue());
					obj.setCountUser(sumAdder.intValue());
					obj.setStatisticsDate(MyDateUtils.parseDate(date));
				}
				JSONObject object = new JSONObject();
				object.put("date", date);
				object.put("info", obj);
				array.add(object);
				newAdder.reset();
				quitAdder.reset();
				addAdder.reset();
				sumAdder.reset();
			}
		}
		return array;
	}

	@Override
	public JSONObject tencentData(WechatFansStatisticsDto dto) throws GlobalException {
		// 如果前台日期传值为空，默认七天的数据
		if (dto.getBeginDate() == null || dto.getEndDate() == null) {
			dto.setBeginDate(MyDateUtils.getStartDate(MyDateUtils.getDayAfterTime(new Date(), -7)));
			dto.setEndDate(MyDateUtils.getFinallyDate(MyDateUtils.getDayAfterTime(new Date(), -1)));
		}
		List<String> list = MyDateUtils.getDays(MyDateUtils.formatDate(dto.getBeginDate()),
				MyDateUtils.formatDate(dto.getEndDate()));
		// 查询该站点已授权的公众号信息
		Map<String, String[]> params = new HashMap<String, String[]>(10);
		params.put("EQ_siteId_Integer", new String[] { dto.getSiteId().toString() });
		List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(params, null, true);
		//过滤小程序
		abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
		.collect(Collectors.toList());
		// 按公众号查看
		JSONArray arrays = new JSONArray();
		if (!abs.isEmpty()) {
			for (AbstractWeChatInfo ab : abs) {
				JSONArray array = new JSONArray();
				JSONObject wechat = new JSONObject();
				// 根据时间查询微信统计数据
				List<WechatFansStatistics> fansStatistics = 
						dao.selectFans(Arrays.asList(ab.getAppId()), 
						dto.getBeginDate(),
						dto.getEndDate());
				if (dto.getUserSource() != null) {
					fansStatistics = fansStatistics.stream()
							.filter(x -> x.getUserSource().equals(dto.getUserSource()))
							.collect(Collectors.toList());
				}
				for (String date : list) {
					WechatFansStatistics obj = new WechatFansStatistics();
					List<WechatFansStatistics> wfs = fansStatistics.stream().filter(x -> 
						MyDateUtils.formatDate(x.getStatisticsDate()).equals(date))
							.collect(Collectors.toList());
					if (wfs.isEmpty()) {
						obj.setAppId(ab.getAppId());
						obj.setNewUser(0);
						obj.setCancelUser(0);
						obj.setNetGrowthUser(0);
						obj.setCountUser(0);
						obj.setStatisticsDate(MyDateUtils.parseDate(date));
					} else {
						obj = wfs.get(0);
					}
					JSONObject object = new JSONObject();
					object.put("date", date);
					object.put("obj", obj);
					array.add(object);
				}
				wechat.put("name", ab.getWechatName());
				wechat.put("info", array);
				arrays.add(wechat);
			}
		} else {
			JSONObject object = new JSONObject();
			object.put("appname", "");
			object.put("array", arrays);
			return object;
		}
		//重新组装数据
		return construction(arrays,dto.getType());
	}
	
	/**
	 * 重新构造数据
	* @Title: construction  
	* @param arrays 数据
	* @param type 类型
	* @return
	 */
	public JSONObject construction(JSONArray arrays, Integer type) {
		Integer value = 0;
		//数据人数
		JSONArray list = new JSONArray();
		//公众号数组
		List<String> wechat = new ArrayList<String>(10);
		for (Object object : arrays) {
			JSONObject object2 = JSONObject.parseObject(object.toString());
			//得到公众号名称
			String name = object2.getString("name");
			if (StringUtils.isNotBlank(name)) {
				wechat.add(name);
			}
			JSONArray array = object2.getJSONArray("info");
			for (Object object3 : array) {
				JSONObject object6 = new JSONObject();
				JSONObject object4 = JSONObject.parseObject(object3.toString());
				//得到时间
				String date = object4.getString("date");
				JSONObject object5 = object4.getJSONObject("obj");
				object6.put("name", name);
				object6.put("date", date);
				//查询类型1.新增人数 2.取消关注 3.净增人数 4.累计人数
				if (type == 1) {
					//新增人数
					value = object5.getInteger("newUser");
					object6.put("value", value);
					list.add(object6);
				} else if (type == 2) {
					//取消关注人数
					value = object5.getInteger("cancelUser");
					object6.put("value", value);
					list.add(object6);
				} else if (type == 3) {
					//净增人数
					value = object5.getInteger("netGrowthUser");
					object6.put("value", value);
					list.add(object6);
				} else if (type == 4) {
					//累计人数
					value = object5.getInteger("countUser");
					object6.put("value", value);
					list.add(object6);
				}
			}
		}
		JSONObject object = new JSONObject();
		object.put("appname", wechat);
		object.put("array", list);
		return object;
	}

	/**
	 * 数组转换
	 */
	@Override
	public JSONArray forEach(List<String> list) {
		JSONArray arrays = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Object sd = list.get(i);
			String json = JSONObject.toJSONString(sd).replaceAll("\"", "");
			String[] arr = json.substring(1, json.length() - 1).split(",");
			JSONObject obj = new JSONObject();
			if (arr != null && arr.length != 0) {
				if (StringUtils.isBlank(arr[0])) {
					arr[0] = "未知";
				}
				switch (arr[0]) {
					case "0":
						arr[0] = "未知";
						break;
					case "1":
						arr[0] = "男";
						break;
					case "2":
						arr[0] = "女";
						break;
					case "ADD_SCENE_OTHERS":
						arr[0] = "其他";
						break;
					case "ADD_SCENE_SEARCH":
						arr[0] = "公众号搜索";
						break;
					case "ADD_SCENE_ACCOUNT_MIGRATION":
						arr[0] = "公众号迁移";
						break;
					case "ADD_SCENE_PROFILE_CARD":
						arr[0] = "名片分享";
						break;
					case "ADD_SCENE_QR_CODE":
						arr[0] = "扫描二维码";
						break;
					case "ADD_SCENEPROFILE LINK":
						arr[0] = "图文页内名称点击";
						break;
					case "ADD_SCENE_PROFILE_ITEM":
						arr[0] = "图文页右上角菜单";
						break;
					case "ADD_SCENE_PAID":
						arr[0] = "支付后关注";
						break;
					default:
				}
				obj.put("name", arr[0]);
				obj.put("value", Integer.parseInt(arr[1]));
				arrays.add(obj);
			}
		}
		return arrays;
	}

	/**
	 * 统计 各个国家的分布的数据 和 中国地区 各个省份的分布的数据
	 */
	@Override
	public ResponseInfo getFansRegion(String appId) throws GlobalException {
		List<String> world = wechatFansService.selectFansCountry(appId);
		List<String> china = wechatFansService.selectFansProvince(appId);
		Map<String, JSONArray> map = new HashMap<>(20);
		map.put("world", forEach(world));
		map.put("china", forEach(china));
		return new ResponseInfo(map);
	}

	/**
	 * 获取开通会员的用户的数量
	 */
	@Override
	public ResponseInfo getFansConut(String appId) throws GlobalException {
		int count = wechatFansService.selectFansCount(appId);
		Long sum = wechatFansService.fansCount(appId);
		Map<String, Integer> map = new HashMap<>(20);
		map.put("已开通", count);
		map.put("未开通", sum.intValue() - count);
		return new ResponseInfo(map);
	}

	/**
	 * 获取不同时期粉丝的数据
	 */
	@Override
	public ResponseInfo getFansAdd(UserStatistics statistics, String appId) throws GlobalException {
		List<WechatFansStatistics> fansStatistics = dao.selectFans(Arrays.asList(appId),
				MyDateUtils.parseDate(statistics.getBeginDate()), 
				MyDateUtils.parseDate(statistics.getEndDate()));
		return new ResponseInfo(fansStatistics);
	}

	@Override
	public List<WechatFansVO> sex(Integer siteId, String appid) throws Exception {
		List<String> appids = new ArrayList<String>();
		// 公众号不为空,根据APPID查询粉丝
		if (StringUtils.isNotBlank(appid)) {
			appids.add(appid);
		} else {
			// 公众号为空，查询该站点已授权的公众号信息
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			//过滤小程序
			abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
			.collect(Collectors.toList());
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			} 
		}
		List<WechatFansVO> sex = wechatFansService.fansVOs(appids, WechatConstants.SEX, null);
		//得到总数
		Long sum = sex.stream().map(WechatFansVO::getNumber).reduce(0L, Long::sum);
		return sexs(sex, sum);
	}
	
	protected List<WechatFansVO> sexs(List<WechatFansVO> sex, Long sum) throws IllegalAccessException {
		for (WechatFansVO vo : sex) {
			// 性别未知
			if (vo.getSex().equals(WechatConstants.FANS_SEX_UNKNOW)) {
				vo.setProportion(MathUtil
						.div(new BigDecimal(vo.getNumber()), 
								new BigDecimal(sum), 2).multiply(new BigDecimal(100)));
				vo.setName("未知");
			} else if (vo.getSex().equals(WechatConstants.FANS_SEX_MAN)) {
				// 男性
				vo.setProportion(MathUtil
						.div(new BigDecimal(vo.getNumber()), 
								new BigDecimal(sum),2).multiply(new BigDecimal(100)));
				vo.setName("男");
			} else {
				// 女性
				vo.setProportion(MathUtil
						.div(new BigDecimal(vo.getNumber()), 
								new BigDecimal(sum),2).multiply(new BigDecimal(100)));
				vo.setName("女");
			}
		}
		return sex;
	}
	
	@Override
	public List<WechatFansVO> lga(Integer siteId, String appid) throws Exception {
		List<String> appids = new ArrayList<String>();
		// 公众号不为空,根据APPID查询粉丝
		if (StringUtils.isNotBlank(appid)) {
			appids.add(appid);
		} else {
			// 公众号为空，查询该站点已授权的公众号信息
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			//过滤小程序
			abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
			.collect(Collectors.toList());
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			} 
		}
		List<WechatFansVO> language = wechatFansService.fansVOs(appids, WechatConstants.LANGUAGE, null);
		//得到总数
		Long sum = language.stream().map(WechatFansVO::getNumber).reduce(0L, Long::sum);
		return languages(language, sum);
	}

	protected List<WechatFansVO> languages(List<WechatFansVO> language, Long sum) 
			throws IllegalAccessException {
		List<WechatFansVO> lists = new ArrayList<WechatFansVO>(10);
		CopyOnWriteArrayList<WechatFansVO> list = new CopyOnWriteArrayList<WechatFansVO>(language);
		for (WechatFansVO vo : list) {
			// 简体中文
			if (vo.getName().equals(WechatConstants.LANGUAGE_TYPE_CN)) {
				vo.setProportion(MathUtil
						.div(new BigDecimal(vo.getNumber()), 
								new BigDecimal(sum),2).multiply(new BigDecimal(100)));
				vo.setName("简体中文");
				list.remove(vo);
				lists.add(vo);
			} else if (vo.getName().equals(WechatConstants.LANGUAGE_TYPE_TW)) {
				vo.setProportion(MathUtil
						.div(new BigDecimal(vo.getNumber()), 
								new BigDecimal(sum),2).multiply(new BigDecimal(100)));
				// 繁体
				vo.setName("繁体中文");
				list.remove(vo);
				lists.add(vo);
			} else if (vo.getName().equals(WechatConstants.LANGUAGE_TYPE_EN)) {
				vo.setProportion(MathUtil
						.div(new BigDecimal(vo.getNumber()), 
								new BigDecimal(sum),2).multiply(new BigDecimal(100)));
				// 英语
				vo.setName("英语");
				list.remove(vo);
				lists.add(vo);
			} 
		}
		if (!list.isEmpty()) {
			WechatFansVO vo = new WechatFansVO();
			//其他数量叠加
			Long another = list.stream().map(WechatFansVO::getNumber).reduce(0L, Long::sum);
			vo.setNumber(another);
			vo.setProportion(
					MathUtil.div(new BigDecimal(another),
							new BigDecimal(sum), 2).multiply(new BigDecimal(100)));
			// 其他
			vo.setName("其他");
			lists.add(vo);
		}
		return lists;
	}

	@Override
	public List<WechatFansVO> province(Integer siteId, String appid) throws Exception {
		List<String> appids = new ArrayList<String>();
		// 公众号不为空,根据APPID查询粉丝
		if (StringUtils.isNotBlank(appid)) {
			appids.add(appid);
		} else {
			// 公众号为空，查询该站点已授权的公众号信息
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			//过滤小程序
			abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
			.collect(Collectors.toList());
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			} 
		}
		//省份统计
		List<WechatFansVO> province = wechatFansService.fansVOs(appids, WechatConstants.PROVINCE, null);
		//去除空的省份名称
		province = province.stream().filter(x -> StringUtils.isNotBlank(x.getName()))
					.collect(Collectors.toList());
		Long sum = province.stream().map(WechatFansVO::getNumber).reduce(0L, Long::sum);
		for (WechatFansVO wechatFansVO : province) {
			wechatFansVO.setProportion(MathUtil
					.div(new BigDecimal(wechatFansVO.getNumber()), 
							new BigDecimal(sum),2).multiply(new BigDecimal(100)));
		}
		return province;
	}

	@Override
	public List<WechatFansVO> city(Integer siteId, String appid,String provinceName)
			throws Exception {
		List<String> appids = new ArrayList<String>();
		// 公众号不为空,根据APPID查询粉丝
		if (StringUtils.isNotBlank(appid)) {
			appids.add(appid);
		} else {
			// 公众号为空，查询该站点已授权的公众号信息
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			//过滤小程序
			abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
			.collect(Collectors.toList());
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			} 
		} 
		//城市统计
		List<WechatFansVO> city = wechatFansService.fansVOs(appids, WechatConstants.CITY, provinceName);
		//去除空的城市名称
		city = city.stream().filter(x -> StringUtils.isNotBlank(x.getName()))
					.collect(Collectors.toList());
		Long sum = city.stream().map(WechatFansVO::getNumber).reduce(0L, Long::sum);
		for (WechatFansVO wechatFansVO : city) {
			wechatFansVO.setProportion(MathUtil
					.div(new BigDecimal(wechatFansVO.getNumber()), 
							new BigDecimal(sum),2).multiply(new BigDecimal(100)));
		}
		return city;
	}

	@Override
	public Workbook export(WechatFansStatisticsDto dto) throws GlobalException {
		// 如果前台日期传值为空，默认七天的数据
		if (dto.getBeginDate() == null || dto.getEndDate() == null) {
			dto.setBeginDate(MyDateUtils.getStartDate(MyDateUtils.getDayAfterTime(new Date(), -7)));
			dto.setEndDate(MyDateUtils.getFinallyDate(MyDateUtils.getDayAfterTime(new Date(), -1)));
		}
		List<String> appids = new ArrayList<String>(10);
		List<WechatFansStatistics> datas = new ArrayList<WechatFansStatistics>(16);
		// 查询该站点已授权的公众号信息
		Map<String, String[]> params = new HashMap<String, String[]>(10);
		params.put("EQ_siteId_Integer", new String[] { dto.getSiteId().toString() });
		List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(params, null, true);
		//过滤小程序
		abs = abs.stream().filter(x -> !x.getWechatType().equals(Short.valueOf("0")))
		.collect(Collectors.toList());
		// 判断APPID是否为空
		if (StringUtils.isNotBlank(dto.getAppid())) {
			appids.add(dto.getAppid());
		} else {
			for (AbstractWeChatInfo abstractWeChatInfo : abs) {
				appids.add(abstractWeChatInfo.getAppId());
			}
		}
		List<WechatStatisticsVO> list = dao.groupFans(appids, dto.getBeginDate(), dto.getEndDate());
		for (WechatStatisticsVO vo : list) {
			WechatFansStatistics wfs = new WechatFansStatistics();
			wfs.setCancelUser(vo.getCancelUser().intValue());
			wfs.setCountUser(vo.getCountUser().intValue());
			wfs.setNetGrowthUser(vo.getNetGrowthUser().intValue());
			wfs.setNewUser(vo.getNewUser().intValue());
			wfs.setStatisticsDate(vo.getStatisticsDate());
			datas.add(wfs);
		}
		Workbook workbook = null;
		int capacity = 1 << 49;
		String title = "用户统计";
		String sheetName = "微信用户统计";
		if (datas == null || datas.isEmpty()) {
			workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName),
					WechatFansStatistics.class,
					new ArrayList<>());
		} else if (datas.size() < capacity) {
			workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), 
					WechatFansStatistics.class,
					datas);
		} else {
			int count = datas.size() % capacity == 0 
					? datas.size() / capacity : datas.size() / capacity + 1;
			int fromIndex = 0;
			int toIndex = 0;
			for (int i = 1; i <= count; i++) {
				fromIndex = capacity * (i - 1);
				toIndex = capacity * i;
				if (toIndex > datas.size()) {
					toIndex = datas.size();
				}
				workbook = ExcelExportUtil.exportBigExcel(new ExportParams(title, sheetName),
						WechatFansStatistics.class, datas.subList(fromIndex, toIndex));
			}
		}
		ExcelExportUtil.closeExportBigExcel();
		return workbook;
	}

	@Override
	public Page<WechatStatisticsVO> pages(Date startDate, Date endDate, List<String> appids, Pageable pageable) 
			throws GlobalException {
		List<WechatStatisticsVO> vos = dao.groupFans(appids, startDate, endDate);
		PageImpl<WechatStatisticsVO> page = new PageImpl<WechatStatisticsVO>(vos,pageable,vos.size());;
		if (!vos.isEmpty()) {
			vos = vos.stream()
					.skip(pageable.getPageSize() * (pageable.getPageNumber()))
					.limit(pageable.getPageSize()).collect(Collectors.toList());
			page = new PageImpl<WechatStatisticsVO>(vos,pageable,vos.size());
		}
		return page;
	}

}