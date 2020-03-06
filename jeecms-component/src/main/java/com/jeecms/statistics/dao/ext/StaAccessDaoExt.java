/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.statistics.dao.ext;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.vo.AccessVo;


/**
 * 忠诚度Dao
* @author ljw
* @version 1.0
* @date 2019-06-25
*/
public interface StaAccessDaoExt {

	/**
	 * 查询统计数据
	* @Title: listInfo  
	* @param dto 传输
	* @return List 
	* @throws GlobalException 异常
	 */
	List<AccessVo> getAccessVo(StatisticsDto dto) throws GlobalException;
}
