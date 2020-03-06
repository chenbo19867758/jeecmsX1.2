package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 用户栏目类权限cache
 * @author: tom
 * @date:   2019年8月16日 下午3:07:44
 */
@Component
@Qualifier(value = CacheConstants.USER_OWNER_CHANNEL_CACHE)
public class UserOwnerChannelDataCache extends EhCacheFactoryBean {

}
