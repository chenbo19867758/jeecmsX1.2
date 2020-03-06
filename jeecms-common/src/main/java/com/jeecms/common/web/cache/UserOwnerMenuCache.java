package com.jeecms.common.web.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 用户菜单类权限cache
 * @author: tom
 * @date:   2019年8月16日 下午3:07:57
 */
@Component
@Qualifier(value = CacheConstants.USER_OWNER_MENU_CACHE)
public class UserOwnerMenuCache extends EhCacheFactoryBean {

}
