/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.system.dao.CmsDataPermCfgDao;
import com.jeecms.system.domain.CmsDataPermCfg;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.service.CmsDataPermCfgService;
import com.jeecms.system.service.CmsOrgService;

/**
 * 权限配置service实现
 * 
 * @author: tom
 * @date: 2019年4月22日 上午11:42:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CmsDataPermCfgServiceImpl extends BaseServiceImpl<CmsDataPermCfg, CmsDataPermCfgDao, Integer>
                implements CmsDataPermCfgService {

        /**
         * 保存组织新权限配置，站点不为空则是栏目、文档类
         */
        @Override
        public CmsDataPermCfg saveByOrg(CmsOrg org, CmsSite site) throws GlobalException {
                CmsDataPermCfg cfg = new CmsDataPermCfg();
                /** 沒有则复制创建 */
                cfg = new CmsDataPermCfg();
                if (org != null) {
                        CmsOrg copySrcOrg = org.getParent();
                        if (copySrcOrg == null) {
                                copySrcOrg = orgService.findDefault();
                        }
                        if (copySrcOrg.getPermCfg() != null) {
                                MyBeanUtils.copyProperties(copySrcOrg.getPermCfg(), cfg);
                        }
                        cfg.setId(null);
                        cfg.setOrg(org);
                        cfg.setOrgId(org.getId());
                        if (site != null) {
                                cfg.setSite(site);
                                cfg.setSiteId(site.getId());
                        }
                        org.getPermCfgs().add(cfg);
                        return super.save(cfg);
                }
                return cfg;
        }

        /**
         * 保存角色新权限配置，站点不为空则是栏目、文档类
         */
        @Override
        public CmsDataPermCfg saveByRole(CoreRole role, CmsSite site) throws GlobalException {
                CmsDataPermCfg cfg = new CmsDataPermCfg();
                /** 沒有则复制创建 */
                cfg = new CmsDataPermCfg();
                if (role != null) {
                        if (role.getOrg().getPermCfg() != null) {
                                MyBeanUtils.copyProperties(role.getOrg().getPermCfg(), cfg);
                        }
                        cfg.setId(null);
                        /** 清空从组织复制来的组织数据和ID */
                        cfg.setOrg(null);
                        cfg.setOrgId(null);
                        cfg.setRole(role);
                        cfg.setRoleId(role.getId());
                        role.getPermCfgs().add(cfg);
                        if (site != null) {
                                cfg.setSite(site);
                                cfg.setSiteId(site.getId());
                        }
                }
                return super.save(cfg);
        }

        /**
         * 保存用户新权限配置，站点不为空则是栏目、文档类
         */
        @Override
        public CmsDataPermCfg saveByUser(CoreUser user, CmsSite site) throws GlobalException {
                CmsDataPermCfg cfg = new CmsDataPermCfg();
                /** 沒有则复制创建 */
                cfg = new CmsDataPermCfg();
                if (user != null) {
                        if (user.getOrg().getPermCfg() != null) {
                                MyBeanUtils.copyProperties(user.getOrg().getPermCfg(), cfg);
                        }
                        /** 清空从组织复制来的组织数据和ID */
                        cfg.setOrg(null);
                        cfg.setOrgId(null);
                        cfg.setId(null);
                        cfg.setUser(user);
                        cfg.setUserId(user.getId());
                        user.getPermCfgs().add(cfg);
                        if (site != null) {
                                cfg.setSite(site);
                                cfg.setSiteId(site.getId());
                        }
                }
                return super.save(cfg);
        }
        
        @Override
        public void deleteBySiteId(Integer siteId) {
                dao.deleteBySiteId(siteId);
        }

        @Autowired
        private CmsOrgService orgService;
}
