package com.jeecms.system.dao.ext;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.MessageTplDetails;

/**
 * @Description:详细模版信息扩展 dao接口
 * @author: wlw
 * @date:
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface MessageTplDetailsExt {

	/**
	 * 获取消息模板详情
	 * @Title: findByMesCodeAndType  
	 * @param mesCode 
	 * @param mesType 模板类型
	 * @param detailMesType
	 * @param hasDeleted 是否删除
	 * @return
	 * @throws GlobalException  程序异常   
	 * @return: MessageTplDetails
	 */
	MessageTplDetails findByMesCodeAndType(String mesCode, Short mesType, Short detailMesType, boolean hasDeleted)
			throws GlobalException;

}
