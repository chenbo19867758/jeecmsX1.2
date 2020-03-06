package com.jeecms.common.web.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.web.Location.LocationResult;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;

/**
 * 根据IP获取定位相关信息 This product includes GeoLite2 data created by MaxMind,
 * available from <a href="http://www.maxmind.com">http://www.maxmind.com</a>.
 * 
 * @author: tom
 * @date: 2019年1月16日 下午2:10:39
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class IpUtils {
	
	static Logger logger = LoggerFactory.getLogger(IpUtils.class);
	/**
	 * GeoIP2-City 数据库文件
	 */
	static InputStream database;
	/**
	 * 创建 DatabaseReader对象
	 */
	static DatabaseReader reader;

	static {
		initFile();
	}

	public static void initFile() {
		File file = new File(IpUtils.logger.getClass().getClassLoader().getResource("").getPath() + "geolite2/GeoLite2-City.mmdb");
		if (file.exists()) {
			database = IpUtils.logger.getClass().getClassLoader()
					.getResourceAsStream("geolite2/GeoLite2-City.mmdb");
		}
		try {
			if (database != null) {
				reader = new DatabaseReader.Builder(database).build();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (database != null) {
					database.close();
				}
				
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 获取地址信息，有缺陷获取不到区县和编码
	 * 
	 * @Title: getLocation
	 * @param ip
	 *            IP地址
	 * @return: LocationResult
	 */
	public static com.jeecms.common.web.Location getLocation(String ip) {
		
		
		if (reader == null) {
			initFile();
		}
		
		
		String lan = "zh-CN";
		InetAddress ipAddress;
		CityResponse response = null;
		try {
			ipAddress = InetAddress.getByName(ip);
			if (reader != null) {
				response = reader.city(ipAddress);
			}
		} catch (Exception e) {
			// 非重要信息采用debug
			logger.debug("ip getLocation error :" + e.getMessage());
		}
		com.jeecms.common.web.Location location = new com.jeecms.common.web.Location();
		LocationResult locationRes = location.getLocationResult();
		if (response != null) {
			Country country = response.getCountry();
			Subdivision subdivision = response.getMostSpecificSubdivision();
			City city = response.getCity();
			Postal postal = response.getPostal();
			Continent continent = response.getContinent();
			Location geoLocat = response.getLocation();
			locationRes.setIp(ip);
			locationRes.setAdInfo(country.getNames().get(lan), subdivision.getNames().get(lan),
					city.getNames().get(lan), continent.getNames().get(lan), postal.getCode());
			locationRes.setLocat(geoLocat.getLongitude(), geoLocat.getLatitude());
			location.setResult(locationRes);
		}
		return location;
	}
}
