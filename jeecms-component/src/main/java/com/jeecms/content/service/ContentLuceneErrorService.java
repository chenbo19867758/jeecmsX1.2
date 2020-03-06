/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.content.domain.ContentLuceneError;

/**   
 * 内容索引异常service接口
 * @author: tom
 * @date:   2019年5月27日 上午11:18:59     
 */
public interface ContentLuceneErrorService extends IBaseService<ContentLuceneError, Integer> {
        
        public void saveError(Integer contentId, Short op);

        public boolean existError(Integer contentId, Short op);
}
