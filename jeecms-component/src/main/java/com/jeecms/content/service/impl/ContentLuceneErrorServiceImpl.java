/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.content.dao.ContentLuceneErrorLogDao;
import com.jeecms.content.domain.ContentLuceneError;
import com.jeecms.content.service.ContentLuceneErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * 内容索引异常service实现类
 * 
 * @author: tom
 * @date: 2019年5月27日 上午11:20:06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentLuceneErrorServiceImpl extends BaseServiceImpl<ContentLuceneError, ContentLuceneErrorLogDao, Integer>
                implements ContentLuceneErrorService {

        private Logger logger = LoggerFactory.getLogger(ContentLuceneErrorServiceImpl.class);

        /**
         * 是否存在某内容的具体操作的异常
         */
        @Transactional(rollbackFor = Exception.class,readOnly = true)
        public boolean existError(Integer contentId, Short op) {
                Paginable paginable = new PaginableRequest(0, 1);
                List<ContentLuceneError> errors = super.dao.getList(contentId, op, paginable);
                if (errors != null && errors.size() > 0) {
                        return true;
                } else {
                        return false;
                }
        }

        /**
         * 保存内容的具体操作异常信息
         */
        public void saveError(Integer contentId, Short op) {
                if (contentId != null) {
                        // 同一个ID，同一个操作只需要保存一次即可，先检查是否存在
                        if (!existError(contentId, op)) {
                                try {
                                        ContentLuceneError error = new ContentLuceneError();
                                        error.setContentId(contentId);
                                        error.setLuceneOp(op);
                                        error.setCreateTime(Calendar.getInstance().getTime());
                                        super.save(error);
                                } catch (GlobalException e) {
                                        logger.error(e.getMessage());
                                }
                        }
                }
        }

}
