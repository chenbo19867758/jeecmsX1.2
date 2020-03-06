/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.vo.CmsDataPermVo;
import com.jeecms.system.domain.vo.OrgOwnerSitePermVo;
import com.jeecms.system.domain.vo.OrgPermVo;
import com.jeecms.system.domain.vo.UserOwnerSitePermVo;
import com.jeecms.system.domain.vo.UserPermVo;

/**
 * 权限扩展服务 主要组装数据
 * 
 * @author: tom
 * @date: 2019年4月28日 下午5:14:22
 */
public interface CmsDataPermPlusService {

        /**
         * 从组织、角色、用户维度组装数据
         * 
         * @Title: assembleData
         * @param orgId
         *                角色Id
         * @param roleId
         *                角色Id
         * @param userId
         *                用户Id
         * @param siteId
         *                站点ID
         * @param dataType
         *                类型
         * @return CmsDataPermVo CmsDataPermVo
         * @throws GlobalException
         */
        CmsDataPermVo assembleData(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType) throws GlobalException;
        
        /**
         * 从组织、角色、用户维度分配权限 获取文档和栏目可选择站点数据
         * 
         * @Title: assembleData
         * @param orgId
         *                角色Id
         * @param roleId
         *                角色Id
         * @param userId
         *                用户Id
         * @param dataType
         *                类型 
         * @return CmsDataPermVo CmsDataPermVo
         */
        Set<CmsSite> getSites(Integer orgId, Integer roleId, Integer userId, Short dataType);

        /**
         * 组装栏目的权限分配页面 针对组织的权限数据(组织以及下级组织和 角色)
         * 
         * @Title: assembleDataByChannelForOrg
         * @param channel
         *                栏目
         * @param org
         *                当前登录用户所属组织
         * @param dateType
         *                2栏目 3内容
         * @return List
         */
        List<OrgPermVo> assembleDataByChannelForOrg(Channel channel, CmsOrg org, Short dateType);

        /**
         * 组装栏目的权限分配页面 针对用户的权限数据 (组织以及下级组织 的用户)
         * 
         * @Title: assembleDataByChannelForUser
         * @param channel
         *                栏目
         * @param org
         *                当前登录用户所属组织
         * @param dateType
         *                2栏目 3内容
         * @param orgid 查询的组织ID
         * @param roleid 查询的角色ID
         * @param key 用户名/邮箱等
         * @param pageable
         *                分页信息
         * @return Page
         */
        Page<UserPermVo> assembleDataByChannelForUser(Channel channel, CmsOrg org, Short dateType, Integer orgid,
                        Integer roleid, String key, Pageable pageable);

        //
        /**
         * 组装站点的站群权限分配页面 针对用户的权限数据
         * 
         * @Title: assembleDataByOwnerSiteForUser
         * @param site
         *                所选站点
         * @param org
         *                当前登录用户所属组织
         * @param orgid
         *                查询组织ID
         * @param roleid
         *                查询角色ID
         * @param key
         *                关键词查询
         * @param pageable
         *                分页
         * @return Page 返回本机组织以及下级组织 的用户 针对所选站点的站群权限
         */

        Page<UserOwnerSitePermVo> assembleDataByOwnerSiteForUser(CmsSite site, CmsOrg org, Integer orgid,
                        Integer roleid,String key, Pageable pageable);
        
        /**
         * 组装站点的站群权限  针对组织的权限数据
         * @Title: assembleDataByOwnerSiteForOrg
         * @param site 所选站点
         * @param org 当前用户组织
         * @return List 返回本机组织以及下级组织和角色的 针对所选站点的站群权限
         */
        List<OrgOwnerSitePermVo> assembleDataByOwnerSiteForOrg(CmsSite site, CmsOrg org);

        /**
         * 组装站点的站点类权限分配页面 针对用户的权限数据
         * 
         * @Title: assembleDataBySiteForUser
         * @param site
         *                所选站点
         * @param org
         *                当前登录用户所属组织
         * @param orgid
         *                查询组织ID
         * @param roleid
         *                查询角色ID
         * @param key
         *                关键词查询
         * @param pageable
         *                分页
         * @return Page 返回本机组织以及下级组织 的用户 针对所选站点的站点类权限
         */
        Page<UserPermVo> assembleDataBySiteForUser(CmsSite site, CmsOrg org, Integer orgid, Integer roleid, String key,
                        Pageable pageable);
        
        /**
         * 组装站点的站点类权限分配页面 针对组织的权限数据
         * @Title: assembleDataByOwnerSiteForOrg
         * @param site 所选站点
         * @param org 当前用户组织
         * @return List 返回本机组织以及下级组织和角色的 针对所选站点的站点类权限
         */
        List<OrgPermVo> assembleDataBySiteForOrg(CmsSite site, CmsOrg org);
}
