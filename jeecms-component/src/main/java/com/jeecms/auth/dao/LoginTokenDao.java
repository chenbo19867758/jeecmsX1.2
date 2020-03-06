/**
 * 
 */

package com.jeecms.auth.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.jeecms.auth.dao.ext.LoginTokenDaoExt;
import com.jeecms.auth.domain.LoginToken;
import com.jeecms.common.base.dao.IBaseDao;

/**
 * 登录token dao接口
 * 
 * @author: tom
 * @date: 2018年7月19日 下午4:11:22
 * 
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface LoginTokenDao extends IBaseDao<LoginToken, Integer>, LoginTokenDaoExt {
        /**
         * 查找token
         * 
         * @param token
         *                token值
         * @return token列表
         */
        @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        List<LoginToken> findByToken(String token);
}
