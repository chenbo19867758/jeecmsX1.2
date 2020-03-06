package com.jeecms.auth.dto;

import com.jeecms.common.base.domain.RequestLoginTarget;

/**
 * 生成 token 所需的信息
 * 
 * @Author tom
 */
public interface TokenDetail {

        /**
         * 获取用户名
         * 
         * @return 用户名
         */
        String getUsername();

        /**
         * 获取用户来源
         * 
         * @return
         */
        RequestLoginTarget getUserSource();

}
