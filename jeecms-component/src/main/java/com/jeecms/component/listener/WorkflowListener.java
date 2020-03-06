/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import com.jeecms.common.exception.GlobalException;

/**
 * 工作流监听器
 * 
 * @author: tom
 * @date: 2019年4月8日 上午9:28:03
 */
public interface WorkflowListener {
	/**
	 * 删除工作流
	 * 
	 * @Title: beforeWorkflowDelete
	 * @param ids 工作流id
	 */
	void beforeWorkflowDelete(Integer[] ids) throws GlobalException;
}
