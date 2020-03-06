/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.channel.domain.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeecms.channel.domain.Channel;

/**
 * 栏目增强类
 * 
 * @author: tom
 * @date: 2019年8月29日 下午5:12:04
 */
public class ChannelAgent implements Serializable {
        private static final long serialVersionUID = -4175597114116170423L;
        Channel channel;

        public ChannelAgent(Channel channel) {
                super();
                this.channel = channel;
        }
        
        public ChannelAgent() {
                super();
        }
        
        /**
         * 栏目排序(排序值降序、生成时间降序)
         * 
         * @Title: sort 栏目排序
         * @param channels 栏目集合
         * @return: Collection
         */
        public List<Channel> sort(List<Channel> channels) {
                return channels.stream()
                                .sorted(Comparator.comparing(Channel::getSortNum)
                                                .thenComparing(Comparator.comparing(Channel::getCreateTime)))
                                .collect(Collectors.toList());
        }

}
