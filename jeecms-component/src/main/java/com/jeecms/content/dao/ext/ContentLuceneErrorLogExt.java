/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.dao.ext;

import java.util.List;

import com.jeecms.common.page.Paginable;
import com.jeecms.content.domain.ContentLuceneError;

/**   
 * 内容索引异常dao扩展接口
 * @author: tom
 * @date:   2019年5月27日 上午11:29:37     
 */
public interface ContentLuceneErrorLogExt {
        /**
         * 扩展查询
         * @Title: getList
         * @param  contentId 内容Id
         * @param op 动作类型 
         * @param paginable 数量
         * @return: List 
         */
        List<ContentLuceneError> getList(Integer contentId, Short op, Paginable paginable);
}
