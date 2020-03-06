/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.Link;

import java.util.List;

/**
 * 友情链接Service
 *
 * @author wulongwei
 * @version 1.0
 * @date 2018年12月25日 上午11:29:14
 */
public interface LinkService extends IBaseService<Link, Integer> {

        /**
         * 添加友情链接
         *
         * @param link   友情链接实体
         * @param siteId 站点id
         * @return Link
         */
        Link save(Link link, Integer siteId) throws GlobalException;

        /**
         * 启用/禁用友情链接
         *
         * @param id       主键ID
         * @param isEnable 是否启用
         * @return ResponseInfo
         * @throws GlobalException 异常
         */
        Link isEnable(Integer id, Boolean isEnable) throws GlobalException;

        /**
         * 获取指定站点和指定友情链接类型下的友情链接数量
         *
         * @param siteId     站点id
         * @param linkTypeId 友情链接分类id
         * @return 友情链接数量
         */
        int getLinkNum(Integer siteId, Integer linkTypeId);

        /**
         * 拖动排序
         *
         * @param sort 排序dto
         * @throws GlobalException 异常
         */
        void dragSort(DragSortDto sort) throws GlobalException;

        /**
         * 移动友情链接到其他类别
         *
         * @param ids        友情链接对象id数组
         * @param linkTypeId 类别id
         * @return List
         * @throws GlobalException 异常
         */
        List<Link> move(Integer[] ids, Integer linkTypeId) throws GlobalException;
}
