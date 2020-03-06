/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao.ext;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.auth.domain.CoreRole;

/**
 * 角色Dao
 * 
 * @author: LJW
 * @date: 2018年5月31日 下午8:48:23
 */
public interface CoreRoleDaoExt {

        /**
         * 角色列表分页
         * 
         * @param orgids
         *                组织ID集合
         * @param roleName
         *                角色名称
         * @param pageable
         *                分页对象
         * @return
         */
        Page<CoreRole> pageRole(List<Integer> orgids, String roleName, Pageable pageable);

        /**
         * 角色列表分页
         * 
         * @param orgids
         *                组织ID集合
         * @param roleName
         *                角色名称
         * @return
         */
        List<CoreRole> listRole(List<Integer> orgids, String roleName);
}
