package com.jeecms.auth.service.impl;

import com.jeecms.auth.dao.CoreRoleDao;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.dto.BeatchDto;
import com.jeecms.system.service.CmsDataPermService;
import com.jeecms.system.service.CmsOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色管理service实现类
 * 
 * @author: tom
 * @date: 2019年4月17日 下午7:49:10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoreRoleServiceImpl extends BaseServiceImpl<CoreRole, CoreRoleDao, Integer>
                implements CoreRoleService,ApplicationListener<ContextRefreshedEvent> {

        @Autowired
        private CmsOrgService cmsOrgService;
        @Autowired
        private CmsDataPermService cmsDataPermService;

        @Override
        public CoreRole saveRole(CoreRole bean) throws GlobalException {
                Integer orgid = bean.getOrgid();
                if (orgid != null) {
                        CmsOrg org = cmsOrgService.findById(orgid);
                        bean.setOrg(org);
                }
                bean = super.save(bean);
                bean.validManagerAble();
                return bean;
        }

        @Override
	public CoreRole updateRole(CoreRole bean) throws GlobalException {
		Integer orgid = bean.getOrgid();
		if (orgid != null) {
			CmsOrg org = cmsOrgService.findById(orgid);
			bean.setOrgid(orgid);
			bean.setOrg(org);
		}
		bean = super.update(bean);
		bean.validManagerAble();
		super.flush();
		/** 需要清空权限缓存数据，后续操作从新拉取 */
		bean.clearPermCache();
		return bean;
	}

        @Override
        public ResponseInfo deleteBatch(BeatchDto dto, Integer orgId) throws GlobalException {
                Integer[] ids = null;
                List<Integer> roleList = new ArrayList<Integer>(10);
                // 得到该组织以及子组织的ID集合
                CmsOrg org = cmsOrgService.findById(orgId);
                List<Integer> old = org.getChildOrgIds();
                // 得到所有的角色
                List<CoreRole> coreRoles = dao.listRole(old, null);
                // 得到所有角色ID集合
                for (CoreRole coreRole : coreRoles) {
                        roleList.add(coreRole.getId());
                }
                // 判断是否存在不可操作数据,如果存在，直接不允许操作
                for (Integer id : dto.getIds()) {
                        if (!roleList.contains(id)) {
                                return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
                                                UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage());
                        }
                }
                ids = dto.getIds().toArray(new Integer[dto.getIds().size()]);
                List<CoreRole> roles = super.findAllById(Arrays.asList(ids));
                // 判断是否可管理
                for (CoreRole role : roles) {
                        role.validManagerAble();
                }
                super.delete(ids);
                // 删除角色权限,无需删除用户
                List<CmsDataPerm> data = new ArrayList<CmsDataPerm>(10);
                CoreRole role = null;
                for (int i = 0; i < roles.size(); i++) {
                        role = roles.get(i);
                        List<CmsDataPerm> list = cmsDataPermService.findList(null, role.getId(), null, null, null, null,
                                        null, null);
                        data.addAll(list);
                }
                cmsDataPermService.delete(data);
                return new ResponseInfo();
        }

        @Override
        public ResponseInfo pageRole(List<Integer> orgids, String roleName, Pageable pageable) {
                return new ResponseInfo(dao.pageRole(orgids, roleName, pageable));
        }

        @Override
        public List<CoreRole> listRole(List<Integer> orgids, String roleName) {
                return dao.listRole(orgids, roleName);
        }

        @Override
        public void clearAllRoleCache() {
                List<CoreRole> roles = listRole(null, null);
                for (CoreRole role : roles) {
                        role.clearPermCache();
                }
        }

        /**
         * 系统启动初始化角色权限
         * 
         * @Title: initAllRoleCache
         * @return: void
         */
        public void initAllRoleCache() {
                List<CoreRole> roles = listRole(null, null);
                for (CoreRole role : roles) {
                        role.getOwnerSites();
                        role.getOwnerMenus();
                        role.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_SITE, null, null, null);
                        role.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CHANNEL, null, null, null);
                        role.getOwnerDataPermsByType(CmsDataPerm.DATA_TYPE_CONTENT, null, null, null);
                }
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
               // initAllRoleCache();
        }

}
