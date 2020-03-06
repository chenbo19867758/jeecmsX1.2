/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  
 package com.jeecms.channel.domain.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**   
 * 栏目批量操作DTO
 * @author: tom
 * @date:   2019年9月17日 下午6:23:53     
 */
public class ChannelBatchDto implements Serializable {
        private static final long serialVersionUID = 2906341930500589203L;
        List<Integer> channelIds;

        /**
         * @return the channelIds
         */
        @NotNull
        @Size(min=1)
        public List<Integer> getChannelIds() {
                return channelIds;
        }

        /**
         * @param channelIds the channelIds to set
         */
        public void setChannelIds(List<Integer> channelIds) {
                this.channelIds = channelIds;
        }
        
}
