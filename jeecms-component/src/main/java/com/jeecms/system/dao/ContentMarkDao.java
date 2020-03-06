/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.ContentMark;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;


/**
 * 发文字号管理Dao
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-21
 */
public interface ContentMarkDao extends IBaseDao<ContentMark, Integer> {

        /**
         * 通过markName和标记词种类查找
         *
         * @param markName 机关代字/年号
         * @param markType 标记词种类(1机关代字 2年份)
         * @return ContentMark
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<ContentMark> findByMarkNameAndMarkType(String markName, Integer markType);

        /**
         * 通过id和标记词种类查找
         *
         * @param id       发文字号管理id
         * @param markType 标记词种类(1机关代字 2年份)
         * @return ContentMark
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<ContentMark> findByIdAndMarkType(Integer id, Integer markType);

        /**
         * 逻辑删除
         *
         * @param id 发文字号管理id
         * @param markType 标记词种类(1机关代字 2年份)
         * @return int
         */
        @Query(value = "delete from ContentMark bean where bean.id=?1 and bean.markType=?2")
        @Modifying
        int deleteByIdAndMarkType(Integer id, Integer markType);
        
        /**
         * 通过标记词种类查询出list集合
         * @Title: findByMarkTypeAndHasDeleted  
         * @param markType	标记词种类
         * @param hasDeleted	删除标记
         * @return: List
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<ContentMark> findByMarkTypeAndHasDeleted(Integer markType, boolean hasDeleted);

        /**
         * 获取发文字号或年号的最大排序值
         * @param markType 标记词种类(1机关代字 2年份)
         * @return
         */
        @Query(value = "select max(bean.sortNum) from ContentMark bean where bean.hasDeleted=0 and bean.markType=?1")
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        Integer getMaxSortNum(Integer markType);
}
