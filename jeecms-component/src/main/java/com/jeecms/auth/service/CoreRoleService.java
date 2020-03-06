package com.jeecms.auth.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.dto.BeatchDto;

/**
 * CoreRole service接口
 * 
 * @author: tom
 * @date: 2018年1月24日 上午10:10:01
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CoreRoleService extends IBaseService<CoreRole, Integer> {

        /**
         * 角色列表分页
         * 
         * @Title: pageRole
         * @param orgids
         *                组织ID集合
         * @param roleName
         *                角色名
         * @param pageable
         *                分页对象
         * @return ResponseInfo ResponseInfo
         * @throws GlobalException
         *                 GlobalException
         */
        ResponseInfo pageRole(List<Integer> orgids, String roleName, Pageable pageable) throws GlobalException;

        /**
         * 保存角色
         * 
         * @param bean
         *                CoreRole
         * @return CoreRole CoreRole
         * @throws GlobalException
         *                 GlobalException
         */
        CoreRole saveRole(CoreRole bean) throws GlobalException;

        /**
         * 修改角色
         * 
         * @param bean
         *                CoreRole
         * @return CoreRole CoreRole
         * @throws GlobalException
         *                 GlobalException
         */
        CoreRole updateRole(CoreRole bean) throws GlobalException;

        /**
         * 批量删除（条件为具有可操作数据）
         * 
         * @Title: deleteOrg
         * @param dto
         *                批量操作对象
         * @param orgId
         *                组织ID
         * @return: ResponseInfo
         * @throws GlobalException
         *                 GlobalException
         */
        ResponseInfo deleteBatch(BeatchDto dto, Integer orgId) throws GlobalException;

        /**
         * 角色列表分页
         * 
         * @Title: pageRole
         * @param orgids
         *                组织ID集合
         * @param roleName
         *                角色名
         * @return List
         * @throws GlobalException
         *                 GlobalException
         */
        List<CoreRole> listRole(List<Integer> orgids, String roleName) throws GlobalException;
        
        /**
         * 清空所有角色权限
         * @Title: clearAllRoleCache
         * @return: void
         */
        void clearAllRoleCache();
}
