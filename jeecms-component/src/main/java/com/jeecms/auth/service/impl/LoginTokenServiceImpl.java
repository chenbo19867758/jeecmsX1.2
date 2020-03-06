/**
 * 
 */
package com.jeecms.auth.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.dao.LoginTokenDao;
import com.jeecms.auth.domain.LoginToken;
import com.jeecms.auth.service.LoginTokenService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;

/**
 * 登录token service实现
 * 
 * @author: tom
 * @date: 2018年7月19日 下午4:16:29
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional
public class LoginTokenServiceImpl extends BaseServiceImpl<LoginToken, LoginTokenDao, Integer>
                implements LoginTokenService {
        @Override
        public LoginToken findByToken(String token) {
                List<LoginToken> tokens = dao.findByToken(token);
                return tokens != null && tokens.size() > 0 ? tokens.get(0) : null;
        }

        @Override
        public boolean existToken(String token) {
                LoginToken loginToken = findByToken(token);
                if (loginToken != null) {
                        return true;
                }
                return false;
        }

        @Override
        public LoginToken save(LoginToken bean) throws GlobalException {
                /**
                if (bean != null && existToken(bean.getToken())) {
                        physicalDelete(bean.getToken());
                }*/
                bean = super.save(bean);
                return bean;
        }

        @Override
        public LoginToken physicalDelete(String token) throws GlobalException {
                LoginToken loginToken = findByToken(token);
                if (loginToken != null) {
                        physicalDelete(loginToken);
                }
                return loginToken;
        }

        @Override
        public long getExpireTokenCount() {
                return dao.getExpireTokenCount();
        }

        @Override
        public List<LoginToken> getExpireTokenList(Paginable paginable) {
                return dao.getExpireTokenList(paginable);
        }
        
        
        
}
