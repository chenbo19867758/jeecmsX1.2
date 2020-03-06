/**
 * 
 */

package com.jeecms.system.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.CmsSiteDaoExt;
import com.jeecms.system.domain.CmsSite;

/**
 * 站点dao
 * 
 * @author: tom
 * @date: 2018年11月5日 下午1:52:06
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsSiteDao extends IBaseDao<CmsSite, Integer>, CmsSiteDaoExt {

        /**
         * 根据父站点ID获取站点列表 TODO
         * 
         * @Title: findByParentIdAndHasDeleted
         * @param parentId
         *                父站点ID
         * @param hasDeleted
         *                是否删除
         * @return: List 站点列表
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<CmsSite> findByParentIdAndHasDeleted(Integer parentId, Boolean hasDeleted);

        /**
         * 根据域名获取站点
         * 
         * @Title: findByDomainAndHasDeleted
         * @param domain
         *                域名
         * @param hasDeleted
         *                是否删除
         * @return: CmsSite 站点
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<CmsSite> findByDomainAndHasDeleted(String domain, Boolean hasDeleted);

        /**
         * 根据路径获取站点
         * 
         * @Title: findByPath
         * @param path
         *                路径
         * @return: CmsSite 站点
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        CmsSite findByPath(String path);

        /**
         * 查询所有
         * 
         * @return: List List
         */
        @Override
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<CmsSite> findAll();
}
