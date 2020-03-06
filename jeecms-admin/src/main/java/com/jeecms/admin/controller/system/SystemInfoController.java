/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.Version;
import com.jeecms.common.base.service.ISysConfigService;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.domain.vo.SystemInfoVo;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.util.SystemContextUtils;

/**
 * 系统信息控制器
 * 
 * @author: ljw
 * @date: 2019年7月12日 上午11:50:07
 */
@RequestMapping(value = "/systeminfo")
@RestController
public class SystemInfoController {

	@Value(value = "${spring.datasource.url}")
	private String url;
	@Autowired
	private GlobalConfigService configService;
	@Autowired
	private ISysConfigService systemConfigService;
	
	/**
	 * 产品信息
	 * 
	 * @Description: productinfo
	 * @Title: productinfo 产品信息
	 * @return: ResponseInfo 响应
	 * @throws Exception 异常
	 */
	@GetMapping()
	public ResponseInfo productinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CmsSite site = SystemContextUtils.getSite(request);
		// 产品信息
		SystemInfoVo vo = new SystemInfoVo();
		GlobalConfig config = configService.get();
		//得到系统配置
		GlobalConfigAttr configAttr = config.getConfigAttr();
		//产品名称
		vo.setProductName(configAttr.getProductName());
		//版权所有
		vo.setCopyright(configAttr.getSysCopyright());
		//公司官网
		vo.setSystemUrl(configAttr.getSystemUrl());
		//产品授权码
		vo.setAuthCode(configAttr.getAuthCode());
		//单位名称
		vo.setSiteName(site.getName());
		//版本
		vo.setVersion(Version.getVersionNumber());
		//授权有效时间
		Date authExpiredDate = configAttr.getAuthExpiredDate();
		vo.setAuthExpiredDate(MyDateUtils.formatDate(authExpiredDate));
		//售后服务到期时间
		Date aftersaleExpiredDate = configAttr.getAftersaleExpiredDate();
		vo.setAfterSaleExpiredDate(MyDateUtils.formatDate(aftersaleExpiredDate));
		//授权状态
		Integer authState = configAttr.getSysState();
		vo.setAuthState(authState);
		vo.setAuthorization("");
		if (authState != null) {
			if (authState == 1) {
				if (authExpiredDate == null) {
					vo.setAuthorization("永久授权");
				} else {
					vo.setAuthorization("定期授权，到期时间：" + MyDateUtils.formatDate(authExpiredDate));
				}
			} else if (authState == 2) {
				vo.setAuthorization("未授权");
			} else if (authState == 3) {
				vo.setAuthorization("未完成授权");
			} else if (authState == 4) {
				vo.setAuthorization("授权已过期");
			}
		}
		// 操作系统
		String os = System.getProperty("os.name");
		// 判断JDK版本
		String jdk = System.getProperty("java.version");
		vo.setSystem(os);
		vo.setJdk(jdk);
		try {
			if (url.contains("jdbc:mysql")) {
				String[] sf = url.split("//");
				String sg = sf[1];
				sg = sg.substring(0, sg.indexOf(":"));
				vo.setDbUrl(sg);
				vo.setDbType("Mysql");
				vo.setDbPort(3306);
				String[] sdString = url.split("3306");
				String sgString = sdString[1];
				Integer aInteger = sgString.indexOf("?");
				vo.setDbName(sgString.subSequence(1, aInteger).toString());
			} else if (url.contains("jdbc:oracle")) {
				String[] sb = url.split(":");
				String sg = sb[3];
				vo.setDbUrl(sg);
				vo.setDbType("Oracle");
				vo.setDbPort(1521);
				String[] sdString = url.split(":");
				String sf = sdString[sdString.length - 1];
				vo.setDbName(sf);
			} else if (url.contains("jdbc:sqlserver")) {
				String[] sb = url.split("//");
				String sg = sb[1];
				sg = sg.substring(0, sg.indexOf(":"));
				vo.setDbUrl(sg);
				vo.setDbType("SQLServer");
				vo.setDbPort(1433);
				String[] sdString = url.split("DatabaseName");
				String sf = sdString[sdString.length - 1].substring(1);
				vo.setDbName(sf);
			} else if (url.contains("jdbc:dm")) {
				String[] sf = url.split("//");
				String sg = sf[1];
				String[] sdString = sg.split("/");
				String[] urlPort = sdString[0].split(":");
				vo.setDbUrl(urlPort[0]);
				vo.setDbPort(Integer.parseInt(urlPort[1]));
				String database = sdString[1];
				vo.setDbType("dm");
				vo.setDbName(database);
			} else {
				vo.setDbUrl("");
				vo.setDbType("其他");
				vo.setDbPort(null);
				vo.setDbName("");
			}
		} catch (Exception e) {
			vo.setDbUrl("");
			vo.setDbType("其他");
			vo.setDbPort(null);
			vo.setDbName("");
		}
		return new ResponseInfo(vo);
	}
	
	/**
	 * 更改授权码
	 * @Description:change
	 * @Title: change  
	 * @return ResponseInfo 响应
	 * @throws: Exception 异常
	 */
	@GetMapping(value = "/change")
	public ResponseInfo change(String attrValue) throws Exception {
		systemConfigService.change(GlobalConfigAttr.SYS_AU_CODE, attrValue);
		return new ResponseInfo();
	}
}
