/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.domain.vo;

import java.io.Serializable;

import com.jeecms.channel.domain.Channel;

/**   
 * 内容索引汇总dto
 * @author: tom
 * @date:   2019年6月4日 下午4:27:56     
 */
public class ContentChannelCountVo implements Serializable {

        private static final long serialVersionUID = -6265067911977156831L;
        private Channel channel;
        private Integer count;
        
        /**
         * 统计的栏目对象
         * @Title: getChannel
         * @return: Channel
         */
        public Channel getChannel() {
                return channel;
        }
        
        /**
         * 内容索引数
         * @Title: getCount
         * @return: Integer
         */
        public Integer getCount() {
                return count;
        }
        
        public void setChannel(Channel channel) {
                this.channel = channel;
        }
        
        public void setCount(Integer count) {
                this.count = count;
        }
        
}
