/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SysHotWordCategory;
import com.jeecms.system.domain.dto.HotWordCategoryDto;

/**
 * 热词分类Service接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
public interface SysHotWordCategoryService extends IBaseService<SysHotWordCategory, Integer> {

        /**
         * 新增热词分类
         *
         * @param dto    热词分类Dto
         * @param siteId 站点id
         * @return SysHotWordCategory
         * @throws GlobalException 异常
         */
        SysHotWordCategory save(HotWordCategoryDto dto, Integer siteId) throws GlobalException;

        /**
         * 修改热词分类
         *
         * @param dto    热词分类Dto
         * @return SysHotWordCategory
         * @throws GlobalException 异常
         */
        SysHotWordCategory update(HotWordCategoryDto dto) throws GlobalException;

        /**
         * 校验热词分类名是否可用
         *
         * @param cateName 分类名称
         * @param id       分类id
         * @return true 可用 false不可用
         */
        boolean checkByCateName(String cateName, Integer id);

}
