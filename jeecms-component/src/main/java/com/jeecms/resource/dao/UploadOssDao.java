package com.jeecms.resource.dao;


import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.resource.dao.ext.UploadOssDaoExt;
import com.jeecms.resource.domain.UploadOss;

/**
 * @Description:Oss dao查询接口
 * @author: tom
 * @date: 2018年1月24日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface UploadOssDao extends IBaseDao<UploadOss, Integer>, UploadOssDaoExt{
}
