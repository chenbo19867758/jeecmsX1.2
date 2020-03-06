/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SysIptables;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.system.domain.dto.SysIptablesDto;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

/**
 * 防火墙配置Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-13
 */
public interface SysIptablesService extends IBaseService<SysIptables, Integer> {

	/**
	 * 修改防火墙配置
	 *
	 * @param dto 防火墙配置dto
	 * @return SysIptables
	 */
	SysIptables update(SysIptablesDto dto, HttpServletRequest request) throws GlobalException;

	/**
	 * 获取防火墙配置信息
	 *
	 * @return SysIptables
	 */
	SysIptables getSysIptables();

	/**
	 * 校验IP地址是否在IP段内
	 *
	 * @param beginIp 起始ip
	 * @param endIp   结束ip
	 * @param address 需校验id
	 * @return true 在时间段内 false 不在时间段内
	 * @throws UnknownHostException ip地址错误异常
	 */
	boolean checkIp(String beginIp, String endIp, String address) throws UnknownHostException;
}
