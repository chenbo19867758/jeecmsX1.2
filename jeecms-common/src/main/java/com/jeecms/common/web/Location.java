package com.jeecms.common.web;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:当前定位信息
 * @author: wqq
 * @date: 2018年6月6日 下午7:13:50
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 查询状态码 状态码，0为正常, 310请求参数信息有误， 311Key格式错误, 306请求有护持信息请检查字符串, 110请求来源未被授权
	 */
	private Integer status;
	/** 状态说明 */
	private String message;
	/** 定位信息 */
	private LocationResult result;

	public LocationResult getResult() {
		return result;
	}

	public void setResult(LocationResult result) {
		this.result = result;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocationResult getLocationResult() {
		return new LocationResult();
	}

	/** IP定位结果 */
	public class LocationResult implements Serializable {
		private static final long serialVersionUID = 1L;
		private String ip;
		private Locat location;
		private AdInfo adInfo;

		public LocationResult() {
			super();
			//初始化 Locat  AdInfo对象
			setAdInfo(new AdInfo());
			setLocation(new Locat());
		}

		public LocationResult(String ip, Locat location, AdInfo adInfo) {
			super();
			this.ip = ip;
			this.location = location;
			this.adInfo = adInfo;
		}

		public Locat setLocat(Double lng, Double lat) {
			Locat location = getLocation();
			location.setLat(lat);
			location.setLng(lng);
			return location;
		}

		public AdInfo setAdInfo(String nation, String province, 
				String city, String district, String adcode) {
			AdInfo info = getAdInfo();
			info.setAdcode(adcode);
			info.setCity(city);
			info.setDistrict(district);
			info.setNation(nation);
			info.setProvince(province);
			return info;
		}

		/** ip信息 */
		public class Locat implements Serializable {
			private static final long serialVersionUID = 1L;
			/** 经度 */
			private Double lng;
			/** 纬度 */
			private Double lat;

			public Double getLng() {
				return lng;
			}

			public void setLng(Double lng) {
				this.lng = lng;
			}

			public Double getLat() {
				return lat;
			}

			public void setLat(Double lat) {
				this.lat = lat;
			}

			public Locat(Double lng, Double lat) {
				super();
				this.lng = lng;
				this.lat = lat;
			}

			public Locat() {
				super();
			}

		}

		/** 定位行政区信息 */
		public class AdInfo implements Serializable {
			private static final long serialVersionUID = 1L;
			/** 国家 */
			private String nation;
			/** 省 */
			private String province;
			/** 市 */
			private String city;
			/** 区 */
			private String district;
			/** 区编号 */
			private String adcode;

			public String getNation() {
				return nation;
			}

			public void setNation(String nation) {
				this.nation = nation;
			}

			public String getProvince() {
				return province;
			}

			public void setProvince(String province) {
				this.province = province;
			}

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public String getDistrict() {
				return district;
			}

			public void setDistrict(String district) {
				this.district = district;
			}

			public String getAdcode() {
				return adcode;
			}

			public void setAdcode(String adcode) {
				this.adcode = adcode;
			}

			public AdInfo(String nation, String province, String city, String district, String adcode) {
				super();
				this.nation = nation;
				this.province = province;
				this.city = city;
				this.district = district;
				this.adcode = adcode;
			}

			public AdInfo() {
				super();
			}

		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public Locat getLocation() {
			return location;
		}

		public void setLocation(Locat location) {
			this.location = location;
		}

		public AdInfo getAdInfo() {
			return adInfo;
		}

		public void setAdInfo(AdInfo adInfo) {
			this.adInfo = adInfo;
		}
	}

	/**
	 * 通过区县行政区编号获取市编号
	 * 
	 * @Title: getCityCode
	 * @Description:
	 * @param: @param
	 *             adCode
	 * @param: @return
	 * @return: String
	 */
	public String getCityCode(String adCode) {
		return StringUtils.isNotBlank(adCode) && adCode.length() >= 4 ? adCode.substring(0, 4) + "00" : "";
	}

	/**
	 * 通过区县行政区编号获取省编号
	 * 
	 * @Title: getProvinceCode
	 * @Description:
	 * @param: @param
	 *             adCode
	 * @param: @return
	 * @return: String
	 */
	public String getProvinceCode(String adCode) {
		return StringUtils.isNotBlank(adCode) && adCode.length() >= 2 ? adCode.substring(0, 2) + "0000" : "";
	}
}
