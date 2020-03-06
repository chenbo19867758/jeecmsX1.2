/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service.impl;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.statistics.domain.StatisticsSource;
import com.jeecms.statistics.domain.vo.SiteFlow;
import com.jeecms.statistics.service.SiteFlowCacheService;
import com.jeecms.statistics.service.StatisticsSourceService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.SysAccessRecord;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysAccessRecordService;
import com.jeecms.util.SystemContextUtils;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 站点流量缓存接口实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/24 14:05
 */

@Service
@Transactional(rollbackFor = {Exception.class})
public class SiteFlowCacheImpl implements SiteFlowCacheService {

	private static final Logger log = LoggerFactory.getLogger(SiteFlowCacheImpl.class);

	private static final String VISIT_COUNT = "visitCount";
	private static final String LAST_VISIT_TIME = "lastVisitTime";
	private static final String CACHE_SITE_FLOW = "cacheSiteFlow";
	private static final String CACHE_KEY_SPLIT = "_";

	@Resource(name = CacheConstants.SITE_FLOW_CACHE)
	private Ehcache cache;
	@Resource(name = CacheConstants.LAST_ACCESSRECORD_CACHE)
	private Ehcache lastAccessCache;
	@Resource(name = CacheConstants.UV_ACCESSRECORD_CACHE)
	private Ehcache uvAccessCache;
	@Resource(name = CacheConstants.IP_ACCESSRECORD_CACHE)
	private Ehcache ipAccessCache;

	@Override
	public SiteFlow flow(HttpServletRequest request, String page, String referer) throws GlobalException {
		String ip = RequestUtils.getRemoteAddr(request);
		CmsSite site = SystemContextUtils.getSite(request);
		Date nowDate = MyDateUtils.getStartDate(Calendar.getInstance().getTime());
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		Integer visitCount = (Integer) session.getAttribute(VISIT_COUNT);
		SysAccessRecord lastAccess = findLastAccess(site.getId());
		SysAccessRecord accessRecord;
		//是否第一次访问
		boolean firstVisitToday = false;
		//是否新用户
		boolean newVisitor = false;
		//是否新ip
		boolean newIp = false;
		if (visitCount == null) {
			visitCount = 0;
			if (lastAccess == null) {
				firstVisitToday = true;
			} else {
				boolean f = lastAccess.getCreateTime() != null && lastAccess.getCreateTime().before(nowDate);
				if (f) {
					firstVisitToday = true;
				}
			}
			accessRecord = findIp(ip, site.getId(), Calendar.getInstance().getTime());
			if (accessRecord == null) {
				log.info("访问ip:" + ip);
				newIp = true;
			}
			newVisitor = true;
		} else {
			//如果没有登录，使用cookie判断uv是否增加
			CoreUser user = SystemContextUtils.getUser(request);
			if (user == null) {
				if (request.getCookies() != null) {
					for (Cookie cookie : request.getCookies()) {
						if (WebConstants.IDENTITY_COOKIE.equals(cookie.getName())) {
							sessionId = cookie.getValue();
						}
					}
				}
			}
			accessRecord = findIp(ip, site.getId(), Calendar.getInstance().getTime());
			if (accessRecord == null) {
				log.info("访问ip:" + ip);
				newIp = true;
			}
			accessRecord = findAccess(sessionId, site.getId());
			if (accessRecord == null) {
				log.info("访问用户：" + sessionId);
				newVisitor = true;
			}
		}
		session.setAttribute(VISIT_COUNT, visitCount + 1);
		//当天第一次访问统计昨日数据
		if (firstVisitToday) {
			log.info("当天首次访问，时间：{}", MyDateUtils.formatDate(Calendar.getInstance().getTime()));
			Date date = Calendar.getInstance().getTime();
			date = MyDateUtils.getDayAfterTime(date, -1);
			//昨日pv
			long pvs = accessRecordService.getContentByDate(date, site.getId());
			/*List<SysAccessRecord> list = accessRecordService.getSource(MyDateUtils.getStartDate(date),
					MyDateUtils.getFinallyDate(date), site.getId(), null, null, null);*/
			//登录用户的uv
			/*int userNum = list.parallelStream().filter(SysAccessRecord::getIsLogin)
					.collect(Collectors.groupingBy(SysAccessRecord::getLoginUserId)).size();
			//未登录用户的uv
			int cookieNum = list.parallelStream().filter(o -> o.getCookieId() != null)
					.filter(o -> !o.getIsLogin())
					.collect(Collectors.groupingBy(SysAccessRecord::getCookieId)).size();*/
			//昨日uv
			List<StatisticsSource> sources = sourceService.getList(site.getId());
			int uvs = sourceService.getUv(sources);
			//昨日ip
			//long ips = accessRecordService.countIp(date, site.getId());
			int ips = sourceService.getIp(sources);
			Thread thread = new StatisticThread(site, pvs, (long) uvs, (long) ips);

			//昨日
			site.getCmsSiteCfg().setYesterdayPv(String.valueOf(pvs));
			site.getCmsSiteCfg().setYesterdayUv(String.valueOf(uvs));
			site.getCmsSiteCfg().setYesterdayIp(String.valueOf(ips));

			//峰值
			Long peakPv = Long.valueOf(site.getConfig().getPeakPv());
			if (pvs > peakPv) {
				site.getCmsSiteCfg().setPeakPv(String.valueOf(pvs));
			}
			Long peakUv = Long.valueOf(site.getConfig().getPeakUv());
			if (uvs > peakUv) {
				site.getCmsSiteCfg().setPeakUv(String.valueOf(uvs));
			}
			Long peakIp = Long.valueOf(site.getConfig().getPeakIp());
			if (ips > peakIp) {
				site.getCmsSiteCfg().setPeakIp(String.valueOf(ips));
			}
			thread.start();
		}
		return totalCache(site, newVisitor, newIp, firstVisitToday, request);
	}

	/**
	 * 统计当前流量信息入统计表
	 */
	private class StatisticThread extends Thread {
		private CmsSite site;
		private Long pvs;
		private Long uvs;
		private Long ips;

		StatisticThread(CmsSite site, Long pvs, Long uvs, Long ips) {
			this.site = site;
			this.pvs = pvs;
			this.uvs = uvs;
			this.ips = ips;
		}

		@Override
		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put(CmsSiteConfig.YESTERDAY_PV, String.valueOf(pvs));
			map.put(CmsSiteConfig.YESTERDAY_UV, String.valueOf(uvs));
			map.put(CmsSiteConfig.YESTERDAY_IP, String.valueOf(ips));
			map.put(CmsSiteConfig.TODAY_PV, String.valueOf(1));
			map.put(CmsSiteConfig.TODAY_UV, String.valueOf(1));
			map.put(CmsSiteConfig.TODAY_IP, String.valueOf(1));
			
			//峰值
			Long peakPv = Long.valueOf(site.getConfig().getPeakPv());
			if (pvs > peakPv) {
				map.put(CmsSiteConfig.PEAK_PV, String.valueOf(pvs));
			}
			Long peakUv = Long.valueOf(site.getConfig().getPeakUv());
			if (uvs > peakUv) {
				map.put(CmsSiteConfig.PEAK_UV, String.valueOf(uvs));
			}
			Long peakIp = Long.valueOf(site.getConfig().getPeakIp());
			if (ips > peakIp) {
				map.put(CmsSiteConfig.PEAK_IP, String.valueOf(ips));
			}
			siteService.updateAttr(site.getId(), map);
		}
	}

	private SysAccessRecord findAccess(String sessionId, Integer siteId) {
		Element accessElement = uvAccessCache.get(sessionId + CACHE_KEY_SPLIT + siteId);
		if (accessElement != null) {
			return (SysAccessRecord) accessElement.getObjectValue();
		} else {
			return accessRecordService.findBySessionId(sessionId, siteId);
		}
	}

	private SysAccessRecord findIp(String ip, Integer siteId, Date date) {
		Element accessElement = ipAccessCache.get(ip + CACHE_KEY_SPLIT + siteId);
		if (accessElement != null) {
			return (SysAccessRecord) accessElement.getObjectValue();
		}
		return accessRecordService.findByIp(ip, siteId, date);
	}

	private SysAccessRecord findLastAccess(Integer siteId) {
		Element accessElement = lastAccessCache.get(siteId);
		SysAccessRecord lastAccess = null;
		if (accessElement != null) {
			lastAccess = (SysAccessRecord) accessElement.getObjectValue();
		} else {
			Date date = Calendar.getInstance().getTime();
			long sum = accessRecordService.getContentByDate(date, siteId);
			if (sum > 0) {
				lastAccess = new SysAccessRecord();
				lastAccess.setCreateTime(MyDateUtils.getStartDate(date));
			}
		}
		return lastAccess;

	}

	/**
	 * @param site       站点
	 * @param newVisitor 是否新访客
	 * @return
	 */
	public SiteFlow totalCache(CmsSite site, boolean newVisitor, boolean newIp, boolean firstVisitToday,
							   HttpServletRequest request) throws GlobalException {
		//总
		Long pvTotal = Long.valueOf(site.getConfig().getPvTotal());
		Long uvTotal = Long.valueOf(site.getConfig().getUvTotal());
		Long ipTotal = Long.valueOf(site.getConfig().getIpTotal());
		//今日
		Long todayPv = Long.valueOf(site.getConfig().getTodayPv());
		Long todayUv = Long.valueOf(site.getConfig().getTodayUv());
		Long todayIp = Long.valueOf(site.getConfig().getTodayIp());
		//昨日
		Long yesterdayPv = Long.valueOf(site.getConfig().getYesterdayPv());
		Long yesterdayUv = Long.valueOf(site.getConfig().getYesterdayUv());
		Long yesterdayIp = Long.valueOf(site.getConfig().getYesterdayIp());
		//峰值
		Long peakPv = Long.valueOf(site.getConfig().getPeakPv());
		Long peakUv = Long.valueOf(site.getConfig().getPeakUv());
		Long peakIp = Long.valueOf(site.getConfig().getPeakIp());
		Element siteFlowCache = cache.get(CACHE_SITE_FLOW + CACHE_KEY_SPLIT + site.getId());
		SiteFlow siteFlow;
		if (siteFlowCache != null) {
			siteFlow = (SiteFlow) siteFlowCache.getObjectValue();
		} else {
			siteFlow = new SiteFlow(pvTotal, uvTotal, ipTotal, todayPv, todayUv, todayIp, yesterdayPv, yesterdayUv,
					yesterdayIp, peakPv, peakUv, peakIp);
		}
		Long pv, uv, ip, pvToday, uvToday, ipToday;
		if (siteFlow.getPvTotal() != null) {
			pv = siteFlow.getPvTotal() + 1;
		} else {
			pv = 1L;
		}
		if (siteFlow.getTodayPv() != null) {
			if (firstVisitToday) {
				pvToday = 1L;
			} else {
				pvToday = siteFlow.getTodayPv() + 1;
			}
		} else {
			pvToday = 1L;
		}
		if (siteFlow.getUvTotal() != null) {
			Long total = siteFlow.getUvTotal();
			uv = newVisitor ? total + 1 : total;
		} else {
			uv = newVisitor ? 1L : 0L;
		}
		if (siteFlow.getTodayUv() != null) {
			Long total = siteFlow.getTodayUv();
			if (firstVisitToday) {
				uvToday = newVisitor ? 1L : 0L;
			} else {
				uvToday = newVisitor ? total + 1 : total;
			}
		} else {
			uvToday = newVisitor ? 1L : 0L;
		}
		if (siteFlow.getIpTotal() != null) {
			Long total = siteFlow.getIpTotal();
			ip = newIp ? total + 1 : total;
		} else {
			ip = newIp ? 1L : 0L;
		}
		if (siteFlow.getTodayIp() != null) {
			Long total = siteFlow.getTodayIp();
			if (firstVisitToday) {
				ipToday = newIp ? 1L : 0L;
			} else {
				ipToday = newIp ? total + 1 : total;
			}
		} else {
			ipToday = newIp ? 1L : 0L;
		}
		SiteFlow flows = new SiteFlow(pv, uv, ip, pvToday, uvToday, ipToday, yesterdayPv, yesterdayUv, yesterdayIp, peakPv, peakUv, peakIp);
		cache.put(new Element(CACHE_SITE_FLOW + CACHE_KEY_SPLIT + site.getId(), flows));
		accessRecordService.recordInfo(request);
		return flows;
	}

	@Autowired
	private CmsSiteService siteService;
	@Autowired
	private SysAccessRecordService accessRecordService;
	@Autowired
	private StatisticsSourceService sourceService;

}
