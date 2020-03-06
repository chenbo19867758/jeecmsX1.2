package com.jeecms.auth.dto;

import com.jeecms.common.base.domain.RequestLoginTarget;

/**
 * 生成 token 所需的信息
 * 
 * @Author tom
 */
public class TokenDetailImpl implements TokenDetail {

        private final String username;

        private final RequestLoginTarget userSource;

        public TokenDetailImpl(String username, RequestLoginTarget userSource) {
                this.username = username;
                this.userSource = userSource;
        }

        @Override
        public String getUsername() {
                return this.username;
        }

        @Override
        public RequestLoginTarget getUserSource() {
                return userSource;
        }

}
