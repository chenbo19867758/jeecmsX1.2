package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;


/**
 * 用户站群权限cache
 * @author: tom
 * @date:   2019年8月16日 下午3:08:12
 */
@Component
@Qualifier(value = CacheConstants.USER_OWNER_SITE_CACHE)
public class UserOwnerSiteCache extends EhCacheFactoryBean {

}
