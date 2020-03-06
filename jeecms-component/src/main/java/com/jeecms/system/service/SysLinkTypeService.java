/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SysLinkType;

/**
 * 友情链接分类Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-10
 */
public interface SysLinkTypeService extends IBaseService<SysLinkType, Integer> {

        /**
         * 新增友情链接类别
         *
         * @param typeName 友情链接类别名称
         * @param siteId   站点id
         * @return SysLinkType
         * @throws GlobalException 全局异常
         */
        SysLinkType save(String typeName, Integer siteId) throws GlobalException;

        /**
         * 修改友情链接类别
         *
         * @param id       友情链接类别id
         * @param typeName 友情链接类别名称
         * @param siteId   站点id
         * @return SysLinkType
         * @throws GlobalException 全局异常
         */
        SysLinkType update(Integer id, String typeName, Integer siteId) throws GlobalException;

        /**
         * 获取站点下友情链接数量
         *
         * @param siteId 站点id
         * @return 友情链接列表
         */
        int getAllBySiteIdNum(Integer siteId);

        /**
         * 校验类别名称是否唯一
         *
         * @param typeName 类别名称
         * @param id       友情链接类别id
         * @param siteId   站点id
         * @return true 唯一 false 不是唯一
         */
        boolean checkByTypeName(String typeName, Integer id, Integer siteId);

        /**
         * 拖动排序
         *
         * @param sort 排序Dto
         * @throws GlobalException
         */
        void dragSort(DragSortDto sort) throws GlobalException;
}
