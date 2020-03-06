/**
 * 
 */

package com.jeecms.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeecms.common.exception.GlobalException;

/**
 * 验证码service接口
 * 
 * @author: tom
 * @date: 2018年9月27日 上午11:33:16
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CaptchaService {

        /**
         * 验证验证码
         * 
         * @Title: validCaptcha
         * @param request
         * @param response
         * @param code
         * @param sessionId
         * @return
         * @throws GlobalException
         * @return: boolean
         */
        public boolean validCaptcha(HttpServletRequest request, HttpServletResponse response, String code,
                        String sessionId) throws GlobalException;
}
