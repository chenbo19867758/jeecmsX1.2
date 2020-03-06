package com.jeecms.auth.domain;

import javax.persistence.Transient;

import com.jeecms.auth.dto.TokenDetail;
import com.jeecms.common.base.domain.RequestLoginTarget;

/**
 * 登录信息
 * 
 * @Author tom
 */
public interface LoginDetail extends TokenDetail {
        /**
         * 用户名
         * 
         * @Title: getUsername
         * @return: String
         */
        String getUsername();

        /**
         * 密码
         * 
         * @Title: getPassword
         * @return: String
         */
        String getPassword();

        /**
         * 混淆码
         * 
         * @Title: getSalt
         * @return: String
         */
        String getSalt();

        /**
         * 是否禁用
         * 
         * @Title: getEnabled
         * @return: Boolean
         */
        Boolean getEnabled();
        
        /**
         * 是否审核通过
         * @Title: getChecked
         * @return: boolean
         */
        boolean getChecked();

        /**
         * 登录来源
         * 
         * @Title: getUserSource
         * @return: RequestLoginTarget
         */
        RequestLoginTarget getUserSource();

        /**
         * 登录错误次数
         * 
         * @Title: getLoginErrorCount
         * @return: int
         */
        Integer getLoginErrorCount();

        /**
         * 是否管理员
         * 
         * @Title: getAdmin
         * @return Boolean
         */
        Boolean getAdmin();
}
