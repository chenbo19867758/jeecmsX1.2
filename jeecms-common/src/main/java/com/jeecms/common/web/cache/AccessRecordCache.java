package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Description:访问记录Cache
 * @author: ztx
 * @date: 2019年1月7日 上午11:03:51
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
@Qualifier(value = CacheConstants.ACCESSRECORD_CACHE)
public class AccessRecordCache extends EhCacheFactoryBean {

}
