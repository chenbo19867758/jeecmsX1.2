package com.jeecms.system.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.CmsDataPermDaoExt;
import com.jeecms.system.domain.CmsDataPerm;

/**
 * 数据权限dao
 * 
 * @author: tom
 * @date: 2018年11月5日 下午1:52:48
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsDataPermDao extends IBaseDao<CmsDataPerm, Integer>, CmsDataPermDaoExt {

        @Modifying
        @Query(value = "delete from CmsDataPerm perm where perm.siteId=?1")
        void deleteBySiteId(Integer siteId);

        @Modifying
        @Query(value = "delete from CmsDataPerm perm where (perm.dataType=2 or perm.dataType=3) and perm.dataId=?1")
        void deleteByChannelId(Integer channelId);
}
