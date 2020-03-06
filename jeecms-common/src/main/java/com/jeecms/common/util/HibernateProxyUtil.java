package com.jeecms.common.util;/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;

/**
 * hibernate延迟加载工具类
 * @author: tom
 * @date: 2019/12/20 12:00   
 */
public class HibernateProxyUtil {
    /***
     * 立即加载延迟加载对象或者集合
     * @param object
     * @return
     */
    public static Object loadHibernateProxy(Object object) {
        if (object instanceof HibernateProxy) {
            /**处理数据错误的情况，代理对象不存在导致解析错误*/
            try {
                object = ProxyUtil.deProxy(object);
            } catch (Exception e) {
                return object;
            }
        }
        if (object instanceof PersistentCollection) {
            // 实体关联集合一对多等
            PersistentCollection collection = (PersistentCollection) object;
            if (!collection.wasInitialized()) {
                collection.forceInitialization();
            }
        }
        return object;
    }
}
