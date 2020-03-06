package com.jeecms.auth.dao;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.auth.dao.ext.CoreApiDaoExt;
import com.jeecms.auth.domain.CoreApi;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * Api管理dao层
 * 
 * @author: chenming
 * @date: 2019年4月9日 下午3:38:34
 */
public interface CoreApiDao extends IBaseDao<CoreApi, Integer>, CoreApiDaoExt {

        /**
         * 根据url查询
         * 
         * @param url
         *                路径地址
         * @param hasDeleted
         *                是否删除
         * @return CoreApi
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        CoreApi findByApiUrlAndHasDeleted(String url, Boolean hasDeleted);
        
        /**
         * 根据URL、请求方式进行查询
         * 
         * @Title: findByApiUrlAndRequestMethodAndHasDeleted
         * @param url
         *                路径地址
         * @param requestMethod
         *                请求方式
         * @param hasDeleted
         *                是否删除
         * @return
         * @return: CoreApi
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        CoreApi findByApiUrlAndRequestMethodAndHasDeleted(String url, Short requestMethod, Boolean hasDeleted);

}
