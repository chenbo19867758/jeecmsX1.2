/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.SysLinkTypeDaoExt;
import com.jeecms.system.domain.SysLinkType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;


/**
 * 友情链接分类Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-10
 */
public interface SysLinkTypeDao extends IBaseDao<SysLinkType, Integer>, SysLinkTypeDaoExt {

        /**
         * 获取站点下友情链接
         *
         * @param siteId 站点id
         * @return 友情链接列表
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<SysLinkType> findAllBySiteId(Integer siteId);

        /**
         * 通过类别名称获取站点下友情链接类别对象
         *
         * @param typeName 类别名称
         * @param siteId   站点id
         * @return 友情链接类别对象
         */
        @Query("select bean from SysLinkType bean where bean.hasDeleted = false and bean.typeName = ?1 "
                + "and bean.siteId = ?2")
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        SysLinkType findByNameAndId(String typeName, Integer siteId);
}
