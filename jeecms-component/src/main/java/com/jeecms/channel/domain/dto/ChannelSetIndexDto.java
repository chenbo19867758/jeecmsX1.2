/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.domain.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 栏目开启/关闭查询 索引 dto
 * 
 * @author: tom
 * @date: 2019年7月9日 上午11:04:50
 */
public class ChannelSetIndexDto implements Serializable {

        private static final long serialVersionUID = -8246671240783453467L;

        @NotNull
        @Size(min = 1)
        @Valid
        private List<ChannelOpenSet> channelOpens;

        public List<ChannelOpenSet> getChannelOpens() {
                return channelOpens;
        }

        public void setChannelOpens(List<ChannelOpenSet> channelOpens) {
                this.channelOpens = channelOpens;
        }

        public class ChannelOpenSet {
                Integer channelId;
                Boolean open;

                @NotNull
                public Integer getChannelId() {
                        return channelId;
                }

                @NotNull
                public Boolean getOpen() {
                        return open;
                }

                public void setChannelId(Integer channelId) {
                        this.channelId = channelId;
                }

                public void setOpen(Boolean open) {
                        this.open = open;
                }
        }
}
