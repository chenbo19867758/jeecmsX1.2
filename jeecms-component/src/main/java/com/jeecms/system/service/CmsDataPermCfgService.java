/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.CmsDataPermCfg;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;

/**
 * 权限配置service
 * 
 * @author: tom
 * @date: 2019年4月22日 上午11:41:39
 */
public interface CmsDataPermCfgService extends IBaseService<CmsDataPermCfg, Integer> {
        /**
         * 保存组织的权限配置,复制父组织配置
         * 
         * @Title: saveByOrg
         * @param org 组织
         * @param site 站点（栏目、文档类则传递，菜单、站群、站点则传递null）                
         * @return CmsDataPermCfg CmsDataPermCfg
         * @throws GlobalException
         *                 GlobalException
         */
        CmsDataPermCfg saveByOrg(CmsOrg org,CmsSite site) throws GlobalException;

        /**
         * 保存角色的权限配置，复制角色所属组织配置
         * 
         * @Title: saveByRole
         * @param role   角色
         * @param site 站点（栏目、文档类则传递，菜单、站群、站点则传递null）
         * @return CmsDataPermCfg CmsDataPermCfg
         * @throws GlobalException
         *                 GlobalException
         */
        CmsDataPermCfg saveByRole(CoreRole role,CmsSite site) throws GlobalException;

        /**
         * 保存用户的权限配置 复制用户所属组织配置
         * 
         * @Title: saveByUser
         * @param user 用户
         * @param site 站点（栏目、文档类则传递，菜单、站群、站点则传递null）
         * @return CmsDataPermCfg CmsDataPermCfg
         * @throws GlobalException
         *                 GlobalException
         */
        CmsDataPermCfg saveByUser(CoreUser user,CmsSite site) throws GlobalException;
        
        void deleteBySiteId(Integer siteId);
}
