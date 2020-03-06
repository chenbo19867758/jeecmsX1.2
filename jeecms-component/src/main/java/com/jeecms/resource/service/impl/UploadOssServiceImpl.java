package com.jeecms.resource.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.resource.dao.UploadOssDao;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.service.UploadOssService;

/**
 * 
 * @Description:UploadOss service实现类
 * @author: tom
 * @date:   2018年4月16日 下午3:55:04     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class UploadOssServiceImpl extends BaseServiceImpl<UploadOss, UploadOssDao,Integer> implements UploadOssService {

	
}
