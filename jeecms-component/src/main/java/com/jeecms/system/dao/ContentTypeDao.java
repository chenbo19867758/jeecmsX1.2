/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.domain.ContentType;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

/**
 * 内容类型管理Dao层
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年5月5日 上午10:04:09
 */
public interface ContentTypeDao extends IBaseDao<ContentType, Integer> {

    /**
     * 根据类型名称查询内容类型信息
     * @Title: findByTypeNameAndHasDeleted  
     * @param typeName
     * @param hasDeleted
     * @return      
     * @return: ContentType
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    ContentType findByTypeNameAndHasDeleted(String typeName, Boolean hasDeleted);
    
}
