/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.member.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.member.dao.ext.UserCollectionDaoExt;
import com.jeecms.member.domain.UserCollection;

import java.util.List;


/**
* @author xiaohui
* @version 1.0
* @date 2019-04-24
*/
public interface UserCollectionDao extends IBaseDao<UserCollection, Integer>, UserCollectionDaoExt {

    /**
     * 获取内容管理的收藏
     *
     * @param userId 用户id
     * @return List
     */
    List<UserCollection> findAllByUserId(Integer userId);

}
