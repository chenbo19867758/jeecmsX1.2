/**
 * 
 */

package com.jeecms.auth.service.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jeecms.auth.service.CaptchaService;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.session.SessionProvider;

/**
 * 验证码服务实现
 * 
 * @author: tom
 * @date: 2018年9月27日 上午11:33:57
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CaptchaServiceImpl implements CaptchaService {

        @Override
        public boolean validCaptcha(HttpServletRequest request, HttpServletResponse response, String code,
                        String sessionId) throws GlobalException {
                String id = session.getSessionId(request);
                if (StringUtils.isNoneBlank(sessionId)) {
                        id = sessionId;
                }
                Serializable targetCode = cacheProvider.getCache(CacheConstants.CAPTCHA,
                                WebConstants.KCAPTCHA_PREFIX + id);
		if (targetCode != null) {
			String tarCode = JSON.toJSONString(targetCode).replace("\"", "");
			Boolean flag = tarCode.equalsIgnoreCase(code);
			if (flag) {
				cacheProvider.clearCache(CacheConstants.CAPTCHA, WebConstants.KCAPTCHA_PREFIX + id);
				return flag;
			}
		}
                // 获取不到缓存中验证码按验证码不通过处理
                return false;
        }

        @Autowired
        private CacheProvider cacheProvider;
        @Autowired
        private SessionProvider session;
}
