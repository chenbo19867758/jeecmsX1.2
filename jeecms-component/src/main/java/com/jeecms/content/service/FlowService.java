/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service;

import java.util.List;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.exception.GlobalException;

/**   
 * 流程接口
 * @author: tom
 * @date:   2019年8月26日 下午6:54:28     
 */
public interface FlowService {
        /**
         * 根据工作流中断流程
         * @Title: doInterruptFlow
         * @param flowIds 工作流ID
         * @throws GlobalException GlobalException
         * @return: void
         */
        void doInterruptFlow(Integer[] flowIds) throws GlobalException;
        
        /**
         * 中断某数据流程
         * @Title: doInterruptFlow
         * @param dataType 数据类型
         * @param dataIds 数据ID
         * @throws GlobalException GlobalException
         * @return: void
         */
        void doInterruptDataFlow(Short dataType,List<Integer>dataIds,CoreUser user) throws GlobalException;

       
        /**
         * 获取待处理内容数
         * @Title: getToDealCount
         * @param dataType 数据类型
         * @return: long
         */
        long getToDealCount(Short dataType);
}
