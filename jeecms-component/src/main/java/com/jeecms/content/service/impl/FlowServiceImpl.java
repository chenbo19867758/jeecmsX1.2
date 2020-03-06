/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service.impl;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.service.FlowService;

/**   
 * 流程服务实现
 * @author: tom
 * @date:   2019年8月26日 下午6:55:51     
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnProperty(name = "workflow.support", havingValue = "local", matchIfMissing = true)
public class FlowServiceImpl implements FlowService {
        /**
         * 中断流程
         * 
         * @Title: doInterruptFlow
         * @param flowIds
         *                工作流ID
         * @return: void
         */
        @Override
        public  void doInterruptFlow(Integer[] flowIds) throws GlobalException {

        }
        
        @Override
        public void doInterruptDataFlow(Short dataType,List<Integer>dataIds,CoreUser user) throws GlobalException {
                
        }
        
        @Override
        public long getToDealCount(Short dataType) {
                 return 0;
        }

}
