/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.service;


import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.vo.SiteFlow;

import javax.servlet.http.HttpServletRequest;

/**
 * 站点流量缓存接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/24 14:05
 */
public interface SiteFlowCacheService {

	SiteFlow flow(HttpServletRequest request, String page, String referer) throws GlobalException;
}
