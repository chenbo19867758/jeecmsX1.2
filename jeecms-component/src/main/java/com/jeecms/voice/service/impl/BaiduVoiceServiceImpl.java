/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.voice.service.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.voice.service.BaiduVoiceService;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**   
 * 语音实现类
 * @author: ljw
 * @date:   2019年9月11日 下午1:56:05     
 */
@Service
public class BaiduVoiceServiceImpl implements BaiduVoiceService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaiduVoiceServiceImpl.class);

	/** 百度语音Token请求Url **/
	public static final String ACCESS_TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token";
	/** 文字转语音请求Url **/
	public static final String TEXT2AUDIO_URL = "http://tsn.baidu.com/text2audio";
	
	@Autowired
	private GlobalConfigService globalConfigService;
	@Resource(name = CacheConstants.BAIDU_VOICE_TOKEN_CACHE)
	private Ehcache voiceTokenCache;
	
	@Override
	public String getToken() throws GlobalException {
		//获取系统设置
		GlobalConfigAttr attr = globalConfigService.get().getConfigAttr();
		String appkey = attr.getBaiduVoiceAppkey();
		String secret = attr.getBaiduVoiceSecret();
		//判断是否为空
		if (StringUtils.isNotBlank(appkey)
				&& StringUtils.isNotBlank(secret)) {
			Map<String, String> params = new HashMap<String, String>(16);
			params.put("client_id", appkey);
			params.put("client_secret", secret);
			params.put("grant_type", "client_credentials");
			JSONObject jsonObject = HttpUtil.getJsonBean(ACCESS_TOKEN_URL, params, JSONObject.class);
			//判断是否成功
			if (jsonObject.containsKey("access_token")) {
				String token = jsonObject.getString("access_token");
				voiceTokenCache.put(new Element("voiceTokenCache", token));
				LOGGER.info(jsonObject.getString("access_token"));
				return token;
			} else {
				throw new GlobalException(RPCErrorCodeEnum.SIGN_ERROR);
			}
		} else {
			throw new GlobalException(RPCErrorCodeEnum.BAIDU_VOICE_NOT_CONFIGURED);
		}
	}

	@Override
	public String text2audio(String text) throws Exception {
		Map<String, String> params = new HashMap<String, String>(16);
		//其中2次urlencode可以覆盖全部的特殊字符
		String one = URLEncoder.encode(text, "UTF-8");
		String result = URLEncoder.encode(one, "UTF-8");
		//合成的文本，使用UTF-8编码。小于2048个中文字或者英文数字。
		params.put("tex", result);
		//用户唯一标识，用来计算UV值。
		params.put("cuid", getMac());
		//客户端类型选择，web端填写固定值1
		params.put("ctp", "1");
		//固定值zh。语言选择,目前只有中英文混合模式，填写固定值zh
		params.put("lan", "zh");
		//开放平台获取到的开发者access_token,优先取缓存，缓存取不到在请求
		Element element = voiceTokenCache.get("voiceTokenCache");
		String token = "";
		if (element != null && !element.isExpired()) {
			params.put("tok", (String)element.getObjectValue());
			token = (String)element.getObjectValue();
		} else {
			params.put("tok", getToken());
			token = getToken();
		}
		StringBuilder builder = new StringBuilder();
		builder.append(TEXT2AUDIO_URL)
		.append("?").append("tex=").append(result)
		.append("&cuid=").append(1).append("&ctp=")
		.append(1).append("&lan=").append("zh")
		.append("&tok=").append(token);
		return builder.toString();
	}
	
	/**
	 * 获取本机的Mac地址
	* @Title: getMac 
	* @return
	 */
	public String getMac() {
		InetAddress ia;
		byte[] mac = null;
		try {
			// 获取本地IP对象
			ia = InetAddress.getLocalHost();
			// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase();
	}

}
