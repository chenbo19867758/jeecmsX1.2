/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 百度Token缓存
 * @author ljw
 * @version 1.0
 * @date 2019/9/11 17:16
 */

@Component
@Qualifier(value = CacheConstants.BAIDU_VOICE_TOKEN_CACHE)
public class BaiduVoiceTokenCache extends EhCacheFactoryBean {
}
