/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.domain.dto.GlobalConfigDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统设置service层
 *
 * @author: wulongwei
 * @date: 2019年4月13日 下午4:07:08
 */
public interface GlobalConfigService extends IBaseService<GlobalConfig, Integer> {

        /**
         * 获取全局设置
         *
         * @return GlobalConfig
         * @throws GlobalException 程序异常
         * @Title: get
         * @return: GlobalConfig 全局设置
         */
        GlobalConfig get() throws GlobalException;

        /**
         * 更新全局设置
         *
         * @param attr 扩充字段
         * @throws GlobalException 程序异常
         * @Title: updateConfigAttr
         * @return: void
         */
        void updateConfigAttr(GlobalConfigAttr attr) throws GlobalException;

        /**
         * 重组系统配置信息，返回至视图中，关键信息过滤
         *
         * @param request
         * @param config  全局配置
         * @return
         * @throws GlobalException 程序异常
         * @Title: filterConfig
         * @return: GlobalConfigDTO
         */
        GlobalConfigDTO filterConfig(HttpServletRequest request, GlobalConfig config) throws GlobalException;
        
        public GlobalConfigDTO filterConfig(CmsSite site, GlobalConfig config, String areaCity, String areaDistrict)
                        throws GlobalException;

        /**
         * 获取webapp文件下的文件夹名称
         *
         * @param relativePath 相对路径(以webapp为目标)
         * @return list
         * @throws GlobalException 程序异常
         * @Title: folderList
         * @return: List<String>
         */
        List<String> folderList(String relativePath) throws GlobalException;

        /**
         * 先判断数据库知否存在数据，如果存在则做修改操作，反之则做添加操作
         *
         * @param bean
         * @return
         * @throws GlobalException 异常
         * @Title: saveGlobalConfig
         * @return: ResponseInfo
         */
        ResponseInfo saveGlobalConfig(GlobalConfig bean) throws GlobalException;

        /**
         * 获取系统设置详细
         *
         * @return
         * @throws GlobalException 异常
         * @Title: getGlobalConfig
         * @return: GlobalConfig
         */
        GlobalConfig getGlobalConfig() throws GlobalException;

}
