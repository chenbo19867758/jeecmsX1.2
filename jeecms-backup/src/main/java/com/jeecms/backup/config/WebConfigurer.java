package com.jeecms.backup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 14:23
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	@Autowired
    private BackupProperties backupProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求
        registry.addInterceptor(new IpWhitelistInterceptor(backupProperties)).addPathPatterns("/**");
    }


}