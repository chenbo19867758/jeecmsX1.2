/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.ext;

import com.jeecms.system.domain.SysLinkType;

import java.util.List;

/**
 * 友情链接类别扩展Dao接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/11 14:38
 */

public interface SysLinkTypeDaoExt {

        /**
         * 获取大于等于sortNum的类别对象，去除等于id 的对象
         *
         * @param sortNum 排序值
         * @param id      对象id
         * @return 类别列表
         */
        List<SysLinkType> getListBySortNum(Integer sortNum, Integer id);
}
