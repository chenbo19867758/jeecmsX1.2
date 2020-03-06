/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.content.dao.ContentCountWeekDao;
import com.jeecms.content.domain.ContentCountWeek;
import com.jeecms.content.service.ContentCountWeekService;
import com.jeecms.common.base.service.BaseServiceImpl;

/**
* @author ljw
* @version 1.0
* @date 2019-05-15
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentCountWeekServiceImpl extends BaseServiceImpl<ContentCountWeek,ContentCountWeekDao, Integer>  implements ContentCountWeekService {

 
}