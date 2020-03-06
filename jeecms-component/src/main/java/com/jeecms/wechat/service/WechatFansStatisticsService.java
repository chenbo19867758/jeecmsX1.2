package com.jeecms.wechat.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.userstatistics.UserStatistics;
import com.jeecms.wechat.domain.WechatFansStatistics;
import com.jeecms.wechat.domain.dto.WechatFansStatisticsDto;
import com.jeecms.wechat.domain.vo.WechatFansVO;
import com.jeecms.wechat.domain.vo.WechatStatisticsVO;

/**
 * 用户数据统计Service
 * 
 * @author ljw
 * @date 2019年05月30日
 */
public interface WechatFansStatisticsService extends IBaseService<WechatFansStatistics, Integer> {

	/**
	 * 将微信查询出的数据保存到数据库(昨天的数据 定时保存)
	 * 
	 * @Title: saveFansStatistics
	 * @param statistics    查询对象
	 * @param validateToken token验证
	 * @throws GlobalException 全局异常
	 */
	void saveFansStatistics(UserStatistics statistics, ValidateToken validateToken) throws GlobalException;

	/**
	 * 今天的数据(实时显示) + 昨天的数据 统计汇总
	 * 
	 * @Title: getFansData
	 * @param siteId 站点ID
	 * @param appid  公众号ID
	 * @throws GlobalException 全局异常
	 * @return: JSONArray
	 */
	JSONArray getFansData(Integer siteId, String appid) throws GlobalException;

	/**
	 * 趋势数据
	 * 
	 * @Title: getFansData
	 * @param dto 传输数据
	 * @throws GlobalException 全局异常
	 * @return: JSONArray
	 */
	JSONArray mapData(WechatFansStatisticsDto dto) throws GlobalException;

	/**
	 * 公众号查看数据
	 * 
	 * @Title: tencentData
	 * @param dto 传输数据
	 * @throws GlobalException 全局异常
	 * @return: JSONObject
	 */
	JSONObject tencentData(WechatFansStatisticsDto dto) throws GlobalException;

	/**
	 * 公众号查看用户属性（性别）
	 * 
	 * @Title: tencentData
	 * @param siteId 站点ID
	 * @param appid  微信公众号ID
	 * @throws GlobalException 全局异常
	 * @return: SexLanguageVO
	 */
	List<WechatFansVO> sex(Integer siteId, String appid) throws Exception;
	
	/**
	 * 公众号查看用户属性（语言）
	 * 
	 * @Title: tencentData
	 * @param siteId 站点ID
	 * @param appid  微信公众号ID
	 * @throws GlobalException 全局异常
	 * @return: SexLanguageVO
	 */
	List<WechatFansVO> lga(Integer siteId, String appid) throws Exception;


	/**
	 * 公众号查看用户属性（省份）
	 * 
	 * @Title: tencentData
	 * @param siteId 站点ID
	 * @param appid  微信公众号ID
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<WechatFansVO> province(Integer siteId, String appid) throws Exception;

	/**
	 * 公众号查看用户属性（城市）
	 * 
	 * @Title: tencentData
	 * @param siteId 站点ID
	 * @param appid  微信公众号ID
	 * @param provinceName 省份名称
	 * @throws GlobalException 全局异常
	 * @return: List
	 */
	List<WechatFansVO> city(Integer siteId, String appid, String provinceName) throws Exception;

	/**
	 * 统计 各个国家的分布的数据 和 中国地区 各个省份的分布的数据
	 * 
	 * @Title: getFansRegion
	 * @param appId 公众号ID
	 * @throws GlobalException 全局异常
	 * @return: ResponseInfo 返回
	 */
	ResponseInfo getFansRegion(String appId) throws GlobalException;

	/**
	 * 统计开通了会员的和没有开通会员的用户的数量
	 * 
	 * @Title: getFansConut
	 * @param appId 公众号ID
	 * @throws GlobalException 全局异常
	 * @return: ResponseInfo 返回
	 */
	ResponseInfo getFansConut(String appId) throws GlobalException;

	/**
	 * 遍历统计的数据，格式转换
	 * 
	 * @Title: forEach
	 * @param list 数据
	 * @return: JSONArray
	 */
	JSONArray forEach(List<String> list);

	/**
	 * 规定时间内新增用户的数量
	 * 
	 * @Title: getFansAdd
	 * @param statistics 请求对象
	 * @param appId      公众号ID
	 * @throws GlobalException 全局异常
	 * @return: ResponseInfo
	 */
	ResponseInfo getFansAdd(UserStatistics statistics, String appId) throws GlobalException;

	/**
	 * 下载表格
	 * 
	 * @Title: export
	 * @param dto 传输
	 * @return Workbook 表格对象
	 * @throws GlobalException 异常
	 */
	Workbook export(WechatFansStatisticsDto dto) throws GlobalException;
	
	/**
	 * 用户增长汇总数据
	* @Title: pages 
	* @param startDate 开始时间
	* @param endDate 结束时间
	* @param appids 公众号集合
	* @param pageable 分页对象
	* @throws GlobalException 异常
	 */
	Page<WechatStatisticsVO> pages(Date startDate, Date endDate, List<String> appids, Pageable pageable) 
			throws GlobalException;
}
