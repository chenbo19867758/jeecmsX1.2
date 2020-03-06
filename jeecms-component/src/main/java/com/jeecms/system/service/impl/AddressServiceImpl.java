package com.jeecms.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecms.common.web.Location;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.util.IpUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.service.AddressService;
/**
 * @author: ztx
 * @date: 2018年11月30日 下午2:54:20
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class AddressServiceImpl implements AddressService {

	@Override
	public Location getAddressByIP(String ip) throws Exception {
		Location location;
		Object locationCache=cacheProvider.getCache(CacheConstants.IP_LOCATION_CACHE, ip);
		if(locationCache!=null){
			location=(Location) locationCache;
		}else{
			/**局域网IP无需定位*/
			if(!RequestUtils.ipIsInner(ip)){
				//GlobalConfig config = globalConfigService.get();
				//String thirdPartyMapKey = null;
				//if (StringUtils.isBlank(thirdPartyMapKey = config.getConfigAttr().getTencentmapKey())) {
				//throw new GlobalException(RPCErrorCodeEnum.THIRD_PARTY_INFO_UNCONFIGURATION);
				//}
				//Map<String, String> param = ImmutableMap.of("ip", ip, "key", thirdPartyMapKey);
				// HttpUtil.getJsonBean("https://apis.map.qq.com/ws/location/v1/ip?", param, Location.class);
				location = IpUtils.getLocation(ip);
				if (location != null) {
					cacheProvider.setCache(CacheConstants.IP_LOCATION_CACHE, ip ,location);
				} else {
					return null;
				}
//				if (!MemberConstants.THIRD_IDENTITY_TENCENT_MAP_OK.equals(location.getStatus())) {
//					// 310请求参数信息有误，311Key格式错误, 306请求有护持信息请检查字符串, 110请求来源未被授权
//					String message = location.getMessage();
//					ip = java.text.Normalizer.normalize(ip, java.text.Normalizer.Form.NFKD);
//					message = java.text.Normalizer.normalize(message, java.text.Normalizer.Form.NFKD);
//					logger.error("调用腾讯地图API失败!ip地址:{},返回信息:{}", ip, message);
//					return null;
//				}
			}else{
				return null;
			}
		}
		return location;
	}

	@Autowired
	private CacheProvider cacheProvider;
}
