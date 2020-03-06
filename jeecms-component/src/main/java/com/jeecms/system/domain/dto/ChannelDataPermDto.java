/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

/**
 * 栏目权限Dto
 * @author: tom
 * @date: 2019年4月29日 上午9:48:39
 */
public class ChannelDataPermDto {
        @NotNull
        Integer channelId;
        @NotNull
        @Range(min = 2, max = 3)
        Short dataType;

        public Short getDataType() {
                return dataType;
        }

        public void setDataType(Short dataType) {
                this.dataType = dataType;
        }

}
