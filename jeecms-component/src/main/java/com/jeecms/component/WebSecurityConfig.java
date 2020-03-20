package com.jeecms.component;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.jeecms.auth.dto.RequestLoginUser;
import com.jeecms.auth.service.MyUserDetailsService;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.constants.TplConstants;
import com.jeecms.common.constants.WebConstants;

/**
 * 启用 Spring Security web 安全的功能 Spring Security 的配置类
 * 权限配置
 * @Author tom
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        /**
         * 注册 token 转换拦截器为 bean 如果客户端传来了 token ，那么通过拦截器解析 token 赋予用户权限
         *
         * @return AuthenticationTokenFilter
         * @throws Exception
         *                 Exception
         */
        public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
                AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
                authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
                authenticationTokenFilter.setSessionAuthenticationStrategy(
                                new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry));
                return authenticationTokenFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests()
                                /** authenticated 需要鉴权认证 */
                                .antMatchers(WebConstants.ADMIN_PREFIX + WebConstants.LOGIN_URL,
                                                WebConstants.ADMIN_PREFIX + WebConstants.LOGOUT_URL,
                                                WebConstants.ADMIN_PREFIX + WebConstants.GLOBAL_GET_URL,
                                                WebConstants.LOGOUT_URL, WebConstants.LOGIN_URL,
						/** 忽略单点登录同步 **/
						WebConstants.ADMIN_PREFIX + WebConstants.JEESSO_SYNC,
						/** 忽略单点登录修改 **/
						WebConstants.ADMIN_PREFIX + WebConstants.JEESSO_UPDATE,
						/** 忽略单点登录删除 **/
						WebConstants.ADMIN_PREFIX + WebConstants.JEESSO_DELETE,
						/** 忽略单点登录获取状态 **/
						WebConstants.ADMIN_PREFIX + WebConstants.JEESSO_STATUS,
						/**单点登录获取用户信息地址**/
						WebConstants.ADMIN_PREFIX + WebConstants.JEESSO_GETINFO)
                                .permitAll().antMatchers(adminProtectUrl, memberProtectUrl, memberCommonProtectUrl)
                                .authenticated().and().formLogin().loginPage(WebConstants.LOGIN_URL)
                                .usernameParameter(RequestLoginUser.LOGIN_IDENTITY)
                                .passwordParameter(ContentSecurityConstants.DES_PARAMETER_NAME)
                                .successHandler(mySuccessHandler).failureHandler(myFailHandler).and().rememberMe()
                                .rememberMeParameter("rememberMe").rememberMeCookieName("rememberMe")
                                .tokenRepository(persistentTokenRepository())
                                /** 有效时间：单位s 默认30天 */
                                .tokenValiditySeconds(rememberMeTokenEffectTime).userDetailsService(userDetailsService)
                                /** 配置被拦截时的处理 */
                                .and().exceptionHandling()
                                /*** 添加无权限时的处理 */
                                .authenticationEntryPoint(this.unauthorizedHandler)
                                /**
                                 * 禁用 SpringSecurity 自带的跨域处理 .csrf().disable() 采用 ContextConfig
                                 */
                                .accessDeniedHandler(this.accessDeniedHandler)
                                /**取消严格限制，手机端开发需要iframe嵌入*/
                                .and().headers().frameOptions().disable()
                                .httpStrictTransportSecurity().disable()
                                .and().csrf().disable();
                http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
                web.ignoring().antMatchers("/favicon.ico", "/static/**");
                web.ignoring().antMatchers(WebConstants.UPLOAD_PATH + "**", TplConstants.RES_PATH + "**");
                web.ignoring().mvcMatchers("/error/**");
                web.ignoring().mvcMatchers("**.shtml");
                web.ignoring().mvcMatchers(WebConstants.ADMIN_PREFIX + WebConstants.LOGIN_URL,
                                WebConstants.ADMIN_PREFIX + WebConstants.LOGOUT_URL, WebConstants.LOGOUT_URL);
        }

        @Autowired
        private DataSource dataSource;

        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
                tokenRepository.setDataSource(dataSource);
                return tokenRepository;
        }

        @Value("${cookie.rememberMeTokenEffectTime}")
        private Integer rememberMeTokenEffectTime = 3600 * 24 * 30;
        /**
         * 注入自定义的登录成功处理类
         */
        @Autowired
        private MyAuthenticationSuccessHandler mySuccessHandler;
        /**
         * 注入自定义的登录失败处理类
         */
        @Autowired
        private MyAuthenticationFailHandler myFailHandler;

        /**
         * 注册 401 处理器
         */
        @Autowired
        private EntryPointUnauthorizedHandler unauthorizedHandler;

        /**
         * 注册 403 处理器
         */
        @Autowired
        private MyAccessDeniedHandler accessDeniedHandler;

        @Autowired
        private SessionRegistry sessionRegistry;
        @Autowired
        private MyUserDetailsService userDetailsService;

        private String adminProtectUrl = WebConstants.ADMIN_PREFIX + "/**";
        private String memberProtectUrl = WebConstants.MEMBER_PREFIX + "/**";
        private String memberCommonProtectUrl = WebConstants.MEMBER_PREFIX + "-**";

}
