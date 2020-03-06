package com.jeecms.auth.dao.ext;

import java.util.List;

import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.common.exception.GlobalException;

/**
 * 菜单dao接口
 * 
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CoreMenuDaoExt {

        /**
         * 参数查询(按sortNum排序)
         * 
         * @param parentId
         *                父菜单ID
         * @param hasDeleted
         *                是否删除
         * @param platFormType
         *                类型
         * @return List<CoreMenu>
         * @throws GlobalException
         *                 全局异常
         */
        List<CoreMenu> findByParams(Integer parentId, Boolean hasDeleted) throws GlobalException;

}
