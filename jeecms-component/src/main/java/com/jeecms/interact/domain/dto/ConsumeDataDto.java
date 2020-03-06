package com.jeecms.interact.domain.dto;/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import java.io.Serializable;

/**
 * 消费任务数据
 * @author: tom
 * @date: 2019/11/21 9:57   
 */
public class ConsumeDataDto implements Serializable {
    Integer[]ids;
    Integer taskId;


    public ConsumeDataDto(Integer[] ids, Integer taskId) {
        this.ids = ids;
        this.taskId = taskId;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer[] getIds() {
        return ids;
    }

    public Integer getTaskId() {
        return taskId;
    }
}
