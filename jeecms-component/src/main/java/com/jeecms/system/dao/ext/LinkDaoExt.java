/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.ext;


import com.jeecms.system.domain.Link;

import java.util.List;

/**
 * 友情链接扩展Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/11 15:23
 */

public interface LinkDaoExt {

    /**
     * 获取范围内sortNum的友情链接对象，去除等于id 的对象
     *
     * @param fromSortNum 拖动排序值
     * @param toSortNum   目标排序值
     * @param id          对象id
     * @param linkTypeId  分类id
     * @return 类别列表
     */
    List<Link> getListBySortNum(Integer fromSortNum, Integer toSortNum, Integer id, Integer linkTypeId);
}
