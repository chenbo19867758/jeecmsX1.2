package com.jeecms.system.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.system.dao.ext.SysMessageDaoExt;
import com.jeecms.system.domain.SysMessage;


/**
* @author ljw
* @version 1.0
* @date 2018-07-02
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/
public interface SysMessageDao extends IBaseDao<SysMessage, Integer> ,SysMessageDaoExt{

}
