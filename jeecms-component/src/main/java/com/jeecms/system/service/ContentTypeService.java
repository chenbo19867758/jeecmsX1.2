/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.ContentType;

/**
 * 内容类型管理service
 * @author: wulongwei
 * @version 1.0
 * @date:   2019年5月5日 上午10:06:29
 */
public interface ContentTypeService extends IBaseService<ContentType, Integer>{

    /**
     * 添加内容类型信息
     * @Title: saveContentTypeInfo  
     * @param contentType
     * @return
     * @throws GlobalException      
     * @return: ResponseInfo
     */
    ResponseInfo saveContentTypeInfo(ContentType contentType) throws GlobalException;

    /**
     * 修改内容类型信息
     * @Title: updateContentTypeInfo  
     * @param contentType
     * @return
     * @throws GlobalException      
     * @return: ResponseInfo
     */
    ResponseInfo updateContentTypeInfo(ContentType contentType) throws GlobalException;
    
    /**
     * 通过类型名称查询内容类型
     * @Title: findByTypeName  
     * @param typeName
     * @return
     * @throws GlobalException      
     * @return: ContentType
     */
    ContentType findByTypeName(String typeName) throws GlobalException;
}
