package com.jeecms.auth.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jeecms.auth.domain.LoginToken;
import com.jeecms.auth.dto.TokenDetail;
import com.jeecms.auth.dto.UserDetailImpl;
import com.jeecms.auth.service.LoginTokenService;
import com.jeecms.common.base.domain.RequestLoginTarget;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token 操作类
 * token 权限判断实现类
 * @Author tom
 */
@Component
public class TokenService {

        private Logger logger = LoggerFactory.getLogger(TokenService.class);

        @Value("${token.secret}")
        private String secret;

        @Value("${token.effectiveTime}")
        private Long expiration;

        @Autowired
        private LoginTokenService loginTokenService;

        /**
         * token 过期时间
         *
         * @return
         */
        public Date generateExpirationDate() {
                return new Date(System.currentTimeMillis() + this.expiration * 1000);
        }

        /**
         * 根据 TokenDetail 生成 Token
         * 
         * @param tokenDetail
         *                TokenDetail
         * @return
         */
        public String generateToken(TokenDetail tokenDetail) {
                Map<String, Object> claims = new HashMap<String, Object>(30);
                claims.put("sub", tokenDetail.getUsername());
                claims.put("created", this.generateCurrentDate());
                claims.put("userSource", tokenDetail.getUserSource());
                return this.generateToken(claims);
        }

        /**
         * 根据 claims 生成 Token
         * 
         * @param claims
         *                认证信息
         * @return
         */
        private String generateToken(Map<String, Object> claims) {
                try {
                        return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate())
                                        .signWith(SignatureAlgorithm.HS512, this.secret.getBytes("UTF-8")).compact();
                } catch (UnsupportedEncodingException ex) {
                        // didn't want to have this method throw the exception, would rather
                        // log it and sign the token like it was before
                        logger.warn(ex.getMessage());
                        return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate())
                                        .signWith(SignatureAlgorithm.HS512, this.secret).compact();
                }
        }

        /**
         * 获得当前时间
         *
         * @return
         */
        private Date generateCurrentDate() {
                return new Date(System.currentTimeMillis());
        }

        /**
         * 从 token 中拿到 username
         * 
         * @param token
         *                token
         * @return
         */
        public String getUsernameFromToken(String token) {
                String username;
                try {
                        final Claims claims = this.getClaimsFromToken(token);
                        username = claims.getSubject();
                } catch (Exception e) {
                        username = null;
                }
                return username;
        }

        /**
         * 从token中获取用户来源
         * 
         * @Title: getUserSource
         * @param: @param
         *                 token
         * @param: @return
         *                 {@link RequestLoginTarget}
         * @return: String
         */
        public String getUserSource(String token) {
                String userSource;
                try {
                        final Claims claims = this.getClaimsFromToken(token);
                        userSource = (String) claims.get("userSource");
                } catch (Exception e) {
                        userSource = null;
                }
                return userSource;
        }

        /**
         * 解析 token 的主体 Claims
         *
         * @param token
         *                token
         * @return
         */
        private Claims getClaimsFromToken(String token) {
                Claims claims;
                try {
                        claims = Jwts.parser().setSigningKey(this.secret.getBytes("UTF-8")).parseClaimsJws(token)
                                        .getBody();
                } catch (Exception e) {
                        claims = null;
                }
                return claims;
        }

        /**
         * 检查 token 是否处于有效期内
         * 
         * @param token
         *                token
         * @param userDetails
         *                UserDetails
         * @return
         */
        public Boolean validateToken(String token, UserDetails userDetails) {
                /** 如果数据库中token已经被清楚则是失效或者主动退出的 */
                LoginToken loginToken = loginTokenService.findByToken(token);
                if (loginToken == null) {
                        return false;
                }
                UserDetailImpl user = (UserDetailImpl) userDetails;
                final String username = this.getUsernameFromToken(token);
                final Date created = this.getCreatedDateFromToken(token);
                /** token在密码被重置之前创建的token认证为失效 */
                boolean tokenIsCreatedAfterReset = !(this.isCreatedBeforeLastPasswordReset(created,
                                user.getLastPasswordReset()));
                return (username.equals(user.getUsername()) && !(this.isTokenExpired(token)))
                                && tokenIsCreatedAfterReset;
        }

        /**
         * 获得我们封装在 token 中的 token 创建时间
         * 
         * @param token
         *                token
         * @return
         */
        public Date getCreatedDateFromToken(String token) {
                Date created;
                try {
                        final Claims claims = this.getClaimsFromToken(token);
                        created = new Date((Long) claims.get("created"));
                } catch (Exception e) {
                        created = null;
                }
                return created;
        }

        /**
         * 获得我们封装在 token 中的 token 过期时间
         * 
         * @param token
         *                token
         * @return
         */
        public Date getExpirationDateFromToken(String token) {
                Date expiration;
                try {
                        final Claims claims = this.getClaimsFromToken(token);
                        expiration = claims.getExpiration();
                } catch (Exception e) {
                        expiration = null;
                }
                return expiration;
        }

        /**
         * 检查当前时间是否在封装在 token 中的过期时间之后，若是，则判定为 token 过期
         * 
         * @param token
         *                token
         * @return
         */
        private Boolean isTokenExpired(String token) {
                final Date expiration = this.getExpirationDateFromToken(token);
                return expiration.before(this.generateCurrentDate());
        }

        /**
         * 检查 token 是否是在最后一次修改密码之前创建的（账号修改密码之后之前生成的 token 即使没过期也判断为无效）
         * 
         * @param created
         *                生成时间
         * @param lastPasswordReset
         *                最后修改密码时间
         * @return
         */
        private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
                return (lastPasswordReset != null && created.before(lastPasswordReset));
        }
}
