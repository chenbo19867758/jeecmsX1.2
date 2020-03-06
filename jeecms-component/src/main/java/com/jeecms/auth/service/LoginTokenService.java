/**
 * 
 */

package com.jeecms.auth.service;

import java.util.List;

import com.jeecms.auth.domain.LoginToken;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;

/**
 * 登录token service接口
 * 
 * @author: tom
 * @date: 2018年7月19日 下午4:15:31
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface LoginTokenService extends IBaseService<LoginToken, Integer> {
        /**
         * 查询token
         * 
         * @Title: findByToken
         * @param token
         *                token
         * @return: LoginToken
         */
        public LoginToken findByToken(String token);

        /**
         * 判断token是否存在
         * 
         * @param token
         *                token值
         * @return 是或否
         */
        public boolean existToken(String token);

        /**
         * 根据token删除数据并清空缓存
         * 
         * @param token
         *                token值
         * @return 登录token
         * @throws GlobalException
         *                 全局异常
         */
        public LoginToken physicalDelete(String token) throws GlobalException;

        /**
         * 获取过期token数量
         * 
         * @Title: expireTokenCount
         * @return: long
         */
        public long getExpireTokenCount();

        /**
         * 查询过期token列表
         * 
         * @Title: getExpireTokenList
         * @param paginable
         *                Paginable
         * @return: List
         */
        public List<LoginToken> getExpireTokenList(Paginable paginable);

}
