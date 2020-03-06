/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.auth.dao.UserModelRecordDao;
import com.jeecms.auth.domain.UserModelRecord;
import com.jeecms.auth.domain.UserModelSort;
import com.jeecms.auth.service.UserModelRecordService;
import com.jeecms.auth.service.UserModelSortService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;

/**
 * 实现类
* @author ljw
* @version 1.0
* @date 2019-12-13
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserModelRecordServiceImpl extends BaseServiceImpl<UserModelRecord,UserModelRecordDao, Integer>  
		implements UserModelRecordService {

	@Autowired
	private UserModelSortService userModelSortService;
	
	@Override
	public void collect(Date startDate, Date endDate) throws GlobalException {
		List<UserModelSort> sorts = new ArrayList<UserModelSort>(16);
		Map<String, String[]> params = new HashMap<String, String[]>(10);
		if (startDate != null && endDate != null) {
			String start = MyDateUtils.formatDate(MyDateUtils.getStartDate(startDate), 
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
			String end = MyDateUtils.formatDate(MyDateUtils.getFinallyDate(endDate), 
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN);
			// 计算时间范围浏览记录
			params.put("GTE_createTime_Timestamp", new String[] { start });
			params.put("LTE_createTime_Timestamp", new String[] { end });
			//得到数据
			List<UserModelRecord> list = super.getList(params, null, false);
			//用户分组
			if (!list.isEmpty()) {
				Map<Integer, List<UserModelRecord>> map = list.stream()
						.collect(Collectors.groupingBy(UserModelRecord::getUserId));
				//遍历用户，得到用户使用的模型数据
				for (Integer userId : map.keySet()) {
					//模型分组，得到数量
					map.get(userId).stream()
						.collect(Collectors.groupingBy(UserModelRecord::getModelId))
						.forEach((key, value) -> {
							UserModelSort sort = new UserModelSort(userId,
									key, value.size(), startDate);
							sorts.add(sort);
						});
				}
			}
			userModelSortService.saveAll(sorts);
		} 
	}

 
}