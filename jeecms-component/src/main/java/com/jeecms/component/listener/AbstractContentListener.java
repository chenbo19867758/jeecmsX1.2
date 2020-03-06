/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;

/**
 * 内容监听抽象类 （默认空实现，子类业务实现具体业务）
 * 
 * @author: tom
 * @date: 2019年5月11日 下午4:05:17
 */
public class AbstractContentListener implements ContentListener {

        @Override
        public Map<String, Object> preChange(Content content) {
                Map<String, Object> map = new HashMap<String, Object>(2);
                map.put(CONTENT_PUBLISH, content.isPublish());
                map.put(CONTENT_IS_DEL, content.isDelete());
                return map;
        }

        @Override
        public void afterChange(Content content, Map<String, Object> map) throws GlobalException {

        }

        @Override
        public void afterDelete(List<Content> contents) throws GlobalException {

        }

        @Override
        public void afterSave(Content content) throws GlobalException {

        }

        @Override
        public void afterContentRecycle(List<Integer> contentIds) throws GlobalException {

        }

}
