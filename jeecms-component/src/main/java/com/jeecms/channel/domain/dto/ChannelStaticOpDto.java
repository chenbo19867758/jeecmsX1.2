package com.jeecms.channel.domain.dto;/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import java.io.Serializable;

/**
 * 栏目静态化Dto
 * @author: tom
 * @date: 2019/11/15 9:13   
 */
public class ChannelStaticOpDto implements Serializable {
    Integer channelId;
    String op;

    public ChannelStaticOpDto(Integer channelId, String op) {
        this.channelId = channelId;
        this.op = op;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
