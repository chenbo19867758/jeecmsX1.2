package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;

@Component
@Qualifier(value = CacheConstants.SYS_LOG_CACHE)
public class SysLogCache extends EhCacheFactoryBean {

}
