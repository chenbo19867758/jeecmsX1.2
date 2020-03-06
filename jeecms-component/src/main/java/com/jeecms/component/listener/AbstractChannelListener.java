/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.component.listener;

import java.util.List;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;

/**
 * 栏目监听抽象类
 * 
 * @author: tom
 * @date: 2019年5月17日 下午3:42:28
 */
public class AbstractChannelListener implements ChannelListener {

        @Override
        public void afterChannelSave(Channel c) throws GlobalException {

        }

        @Override
        public void beforeChannelDelete(Integer[] ids) throws GlobalException {

        }

        @Override
        public void afterChannelRecycle(List<Channel> channels) throws GlobalException {

        }

        @Override
        public void afterChannelChange(Channel c) throws GlobalException {
        }

}
