/**
 * 
 */

package com.jeecms.auth.dao.ext;

import java.util.List;

import com.jeecms.auth.domain.LoginToken;
import com.jeecms.common.page.Paginable;

/**
 * token扩展查询接口
 * 
 * @author: tom
 * @date: 2018年7月19日 下午6:42:50
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface LoginTokenDaoExt {
        /**
         * 获取token列表
         * 
         * @param paginable
         * @return token列表
         */
        public List<LoginToken> getExpireTokenList(Paginable paginable);

        /**
         * 获取token
         * 
         * @return token值
         */
        public long getExpireTokenCount();
}
