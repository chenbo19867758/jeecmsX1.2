package com.jeecms.wechat.dao.ext;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.MiniprogramVersion;

/**
 * 
 * @Description: 小程序代码管理dao扩展接口
 * @author: chenming
 * @date:   2019年1月26日 上午8:54:16     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MiniprogramVersionDaoExt {
	
	/**
	 * 获取提交信息
	 * @Title: getSubmitFail  
	 * @param appId
	 * @param versionType 版本类型
	 * @param status 状态
	 * @return
	 * @throws GlobalException      
	 * @return: MiniprogramVersion
	 */
	MiniprogramVersion getSubmitFail(String appId, Integer versionType, Integer status) throws GlobalException;
}
