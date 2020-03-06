/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.system.domain.SysThird;

import java.util.List;

/**
 * 第三方登录设置的service接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-18
 */
public interface SysThirdService extends IBaseService<SysThird, Integer> {

        /**
         * 根据code查询第三方登录
         *
         * @param code 第三方登陆标识(QQ WECHAT WEIBO )
         * @Title: getCode
         * @return: Third
         */
        SysThird getCode(String code);

        /**
         * 列表
         *
         * @Title: getList
         * @return: List
         */
        List<SysThird> getList();

}
