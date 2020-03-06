package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;


/**
 * 用户文档类权限cache
 * @author: tom
 * @date:   2019年8月16日 下午3:07:14
 */
@Component
@Qualifier(value = CacheConstants.USER_OWNER_CONTENT_CACHE)
public class UserOwnerContentDataCache extends EhCacheFactoryBean {

}
