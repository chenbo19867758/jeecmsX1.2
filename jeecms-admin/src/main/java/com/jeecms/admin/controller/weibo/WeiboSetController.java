/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.admin.controller.weibo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.WeiboAppConfig;
import com.jeecms.weibo.service.WeiboAppConfigService;

/**   
 * 微博应用设置
 * @author: ljw
 * @date:   2019年6月14日 上午10:32:06     
 */
@RequestMapping(value = "/weiboSet")
@RestController
public class WeiboSetController extends BaseController<WeiboAppConfig, Integer> {

	@Autowired
	private WeiboAppConfigService weiboAppConfigService;
	
	/**
	 * 保存微博应用信息
	 * 
	 * @Title: save
	 * @param request 请求
	 * @param response 响应
	 * @param bean 微博传输对象
	 * @param result 检测
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping
	public ResponseInfo save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid WeiboAppConfig bean, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		//获得站点ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		WeiboAppConfig config = weiboAppConfigService.getBySiteId(siteId);
		//站点存在应用，则更新
		if (config == null) {
			bean.setSiteId(siteId);
			weiboAppConfigService.save(bean);
		} else {
			config.setAppId(bean.getAppId());
			config.setAppSecret(bean.getAppSecret());
			config.setDescription(bean.getDescription());
			weiboAppConfigService.update(config);
		}
		return new ResponseInfo();
	}
	
	/**
	 * 获取应用配置
	 * 
	 * @Title: save
	 * @param request 请求
	 * @param response 响应
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping
	@MoreSerializeField({
		@SerializeField(clazz = WeiboAppConfig.class, includes = { "id", "appId", "appSecret", 
				"description" }),
		 })
	public ResponseInfo info(HttpServletRequest request, HttpServletResponse response) 
			throws GlobalException {
		//获得站点ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		WeiboAppConfig config = weiboAppConfigService.getBySiteId(siteId);
		return new ResponseInfo(config);
	}
}
