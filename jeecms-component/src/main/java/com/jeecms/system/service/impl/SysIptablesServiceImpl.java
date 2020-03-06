/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.dao.SysIptablesDao;
import com.jeecms.system.domain.SysIptables;
import com.jeecms.system.domain.dto.SysIptablesDto;
import com.jeecms.system.service.SysIptablesService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 防火墙配置Service实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysIptablesServiceImpl extends BaseServiceImpl<SysIptables, SysIptablesDao, Integer> implements SysIptablesService {

	private static final Logger logging = LoggerFactory.getLogger(SysIptablesServiceImpl.class);

	@Override
	public SysIptables update(SysIptablesDto dto, HttpServletRequest request) throws GlobalException {

		SysIptables bean = getSysIptables();
		bean.setIsEnable(dto.getEnable());
		//开启防火墙
		if (dto.getEnable()) {
			SysIptablesDto.InNetworkIpJson[] ipJsons = dto.getInNetworkIpJsons();
			Integer limitInNetworkModel = dto.getLimitInNetworkModel();
			// 1、限制内网ip   2、黑/白名单不为空
			if (!SysIptables.NETWORK_NOT_LIMITED.equals(limitInNetworkModel) && ipJsons != null && ipJsons.length > 0) {
				boolean flag = false;
				for (SysIptablesDto.InNetworkIpJson inNetworkIpJson : ipJsons) {
					String startIp = inNetworkIpJson.getStartIp();
					String endIp = inNetworkIpJson.getEndIp();
					RequestUtils.ipIsInner(startIp);
					RequestUtils.ipIsInner(endIp);
					try {
						//校验客户端ip是否在区间内
						if (checkIp(startIp, endIp, request.getRemoteAddr())) {
							//如果是黑名单
							if (SysIptables.NETWORK_BLACKLIST.equals(limitInNetworkModel)) {
								throw new GlobalException(new SystemExceptionInfo(
										SettingErrorCodeEnum.IP_ADDRESS_IS_IN_THE_RANGE.getDefaultMessage(),
										SettingErrorCodeEnum.IP_ADDRESS_IS_IN_THE_RANGE.getCode()));
							}
							flag = true;
						}
					} catch (UnknownHostException e) {
						logging.error(e.getMessage());
						throw new GlobalException(new SystemExceptionInfo(
								SettingErrorCodeEnum.IP_ADDRESS_ERROR.getDefaultMessage(),
								SettingErrorCodeEnum.IP_ADDRESS_ERROR.getCode()));
					}
				}
				//如果是白名单，并且flag等于false表示ip不在任意一个ip段内
				if (!flag && SysIptables.NETWORK_WHITELIST.equals(limitInNetworkModel)) {
					throw new GlobalException(new SystemExceptionInfo(
							SettingErrorCodeEnum.IP_ADDRESS_IS_NOT_IN_THE_RANGE.getDefaultMessage(),
							SettingErrorCodeEnum.IP_ADDRESS_IS_NOT_IN_THE_RANGE.getCode()));
				}
			}

			bean.setLimitInNetworkModel(dto.getLimitInNetworkModel());
			JSONArray jsonArray = JSONArray.fromObject(dto.getInNetworkIpJsons());
			bean.setInNetworkIpJson(jsonArray.toString());
			bean.setLimitDomain(dto.getLimitDomain());
			bean.setAllowLoginHours(dto.getAllowLoginHours());
			bean.setAllowLoginWeek(dto.getAllowLoginWeek());
		}
		return bean.getId() != null ? update(bean) : save(bean);
	}

	@Override
	public SysIptables getSysIptables() {
		List<SysIptables> list = findAll(true);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return new SysIptables();
		}
	}


	@Override
	public boolean checkIp(String beginIp, String endIp, String address) throws UnknownHostException {
		long startIp = ipToLong(InetAddress.getByName(beginIp));
		long finishIp = ipToLong(InetAddress.getByName(endIp));
		long ipCheck = ipToLong(InetAddress.getByName(address));
		return ipCheck > startIp && ipCheck < finishIp;
	}

	/**
	 * ip转为long类型
	 *
	 * @param ip ip地址
	 * @return long
	 */
	private static long ipToLong(InetAddress ip) {
		long result = 0;
		byte[] ipAdds = ip.getAddress();
		for (byte b : ipAdds) {
			result <<= 8;
			result |= b & 0xff;
		}
		return result;
	}
}