package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;

/**   
 * 验证码缓存
 * @author: tom
 * @date:   2018年9月27日 上午10:55:17     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.  Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
@Qualifier(value = CacheConstants.SMS)
public class SmsCache extends EhCacheFactoryBean {
}
