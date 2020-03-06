package com.jeecms.system.service;

import com.jeecms.common.web.Location;

/**
 * @author: ztx
 * @date: 2018年11月30日 下午2:11:58
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface AddressService {

	/**
	 * 根据ip获取区域信息(调用腾讯地图API)
	 * @Title: getAddressByIP
	 * @param ip ip地址
	 * @return
	 * @throws Exception 程序异常     
	 * @return: Location
	 */
	Location getAddressByIP(String ip) throws Exception;

}
