/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.dao.ext.ContentLuceneErrorLogExt;
import com.jeecms.content.domain.ContentLuceneError;

/**   
 * 内容索引异常dao
 * @author: tom
 * @date:   2019年5月27日 上午11:17:42     
 */
public interface ContentLuceneErrorLogDao extends ContentLuceneErrorLogExt,IBaseDao<ContentLuceneError, Integer> {


}
