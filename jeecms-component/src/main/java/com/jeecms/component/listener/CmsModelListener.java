/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import java.util.Collection;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModel;

/**
 * 模型监听器
 * @author: tom
 * @date: 2019年5月15日 上午10:17:57
 */
public interface CmsModelListener {
        
        /**
         * 模型删除后处理业务（变更内容状态）
         * @Title: afterModelDelete
         * @param models Collection
         * @throws GlobalException GlobalException
         * @return: void
         */
        void afterModelDelete(Collection<CmsModel> models) throws GlobalException;

        /**
         * 模型字段删除后处理业务（工作流字段删除 变更内容状态）
         * @Title: afterModelUpdate
         * @param model CmsModel
         * @throws GlobalException GlobalException
         * @return: void
         */
        void afterModelUpdate(CmsModel model) throws GlobalException;
}
