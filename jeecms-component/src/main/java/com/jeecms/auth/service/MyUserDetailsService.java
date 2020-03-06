/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义UserDetailsService
 * 
 * @author: tom
 * @date: 2019年5月16日 下午7:28:12
 */
public interface MyUserDetailsService extends UserDetailsService {

        /**
         * 获取用户认证信息并设置token到认证信息中
         * 
         * @Title: loadUserByUsername
         * @param username
         *                用户名
         * @param token
         *                token
         * @throws UsernameNotFoundException
         *                 UsernameNotFoundException
         * @return: UserDetails
         */
        public UserDetails loadUserByUsername(String username, String token) throws UsernameNotFoundException;
}
