/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 内容索引汇总dto
 * 
 * @author: tom
 * @date: 2019年6月4日 下午4:27:56
 */
public class ContentLuceneSummaryVo implements Serializable {

        private static final long serialVersionUID = 1182434288224580071L;
        
        private List<ContentChannelCountVo> result;

        public List<ContentChannelCountVo> getResult() {
                return result;
        }

        public void setResult(List<ContentChannelCountVo> result) {
                this.result = result;
        }

        public ContentLuceneSummaryVo(List<ContentChannelCountVo> result) {
                super();
                this.result = result;
        }

}
