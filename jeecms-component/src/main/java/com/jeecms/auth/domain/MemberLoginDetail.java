/**
 * 
 */

package com.jeecms.auth.domain;

/**
 * 会员loginDetail
 * 
 * @author: tom
 * @date: 2018年3月31日 下午3:03:05
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MemberLoginDetail extends LoginDetail {
        /**
         * 获取邮箱
         * 
         * @return 邮箱
         */
        String getEmail();

        /**
         * 获取联系方式
         * 
         * @return 联系方式
         */
        String getPhone();
}
