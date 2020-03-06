package com.jeecms.system.service;

import java.util.List;
import java.util.Set;

import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.dto.CmsDatePermDto;
import com.jeecms.system.domain.dto.OrgPermDto;
import com.jeecms.system.domain.dto.UserPermDto;
import com.jeecms.system.domain.dto.OwnerSitePermDto;

/**
 * 数据权限service接口
 * 
 * @author: tom
 * @date: 2018年11月5日 下午1:58:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsDataPermService extends IBaseService<CmsDataPerm, Integer> {
        /**
         * 从组织、角色、用户 分配权限
         * 
         * @Title: updateByDto
         * @param dto
         *                CmsDatePermDto
         * @throws  GlobalException GlobalException               
         */
        void updateByDto(CmsDatePermDto dto) throws GlobalException;
        
        /**
         * 从栏目分配权限 根据组织、角色
         * @Title: updateDataPermByOrg
         * @param dto OrgPermDto
         * @throws GlobalException GlobalException
         */
        void updateDataPermByOrg(OrgPermDto dto) throws GlobalException;
        
        /**
         * 从栏目分配权限 根据用户
         * @Title: updateDataPermByUser
         * @param dto OrgPermDto
         * @throws GlobalException GlobalException
         */
        void updateDataPermByUser(UserPermDto dto) throws GlobalException;
        
        /**
         * 修改站点的站群权限 -根据用户
         * @Title: updateSiteOwnerByUser
         * @param dto UserSitePermDto
         * @throws GlobalException GlobalException
         */
        void updateSiteOwner(OwnerSitePermDto dto) throws GlobalException;
        

        /**
         * 查询
         * 
         * @Title: findList
         * @param orgId
         *                组织ID
         * @param roleId
         *                角色ID
         * @param userId
         *                用户ID
         * @param siteId
         *                站点ID
         * @param dataType
         *                数据模块类型（1站点 2栏目 3文档 4组织 5可管理站点）
         * @param operation 操作
         * @param dataId 数据ID
         * @param paginable
         *                取数数据
         * @return List
         */
        List<CmsDataPerm> findList(Integer orgId, Integer roleId, Integer userId, Integer siteId,
                        Short dataType,Short operation,Integer dataId, Paginable paginable);

        /**
         * 查询单条数据
         * @Title: findOne
         * @param orgId 组织ID
         * @param roleId 角色ID
         * @param userId 用户ID
         * @param siteId 站点ID
         * @param dataType 数据模块类型（1站点 2栏目 3文档 4组织 5可管理站点）
         * @param operation 操作
         * @param dataId 数据ID
         * @return CmsDataPerm
         */
        CmsDataPerm findOne(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType,
                        Short operation,Integer dataId);
        
        /**
         * 刪除数据权限 根据站点ID
         * @Title: deleteBySiteId
         * @param siteId 站点ID
         * @return: void
         */
        void deleteBySiteId(Integer siteId);
        
        /**
         * 刪除数据权限 根据栏目ID
         * @Title: deleteByChannelId
         * @param channelId 栏目ID
         * @return: void
         */
        void deleteByChannelId(Integer channelId);
        
        void afterMenuSave(CoreMenu menu) throws GlobalException;

}
