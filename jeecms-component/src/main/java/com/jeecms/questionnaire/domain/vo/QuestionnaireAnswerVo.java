/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.util.MathUtil;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/30 14:58
 */
public class QuestionnaireAnswerVo {
    private Integer id;
    private String title;
    private Integer pageViews = 0;
    private Integer number = 0;
    private Integer status;
    private Date beginTime;
    private Date endTime;

    public QuestionnaireAnswerVo() {
        super();
    }

    public QuestionnaireAnswerVo(Integer id, String title, Integer pageViews, Integer number, Integer status, Date beginTime, Date endTime) {
        this.id = id;
        this.title = title;
        this.pageViews = pageViews;
        this.number = number;
        this.status = status;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPageViews() {
        return pageViews == null ? 0 : pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public Integer getNumber() {
        return number == null ? 0 : number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getRate() {
        if (getNumber() != 0) {
            try {
                return MathUtil.div(new BigDecimal(getNumber() * 100),
                        new BigDecimal(getPageViews()), MathUtil.SCALE_LEN_COMMON);
            } catch (IllegalAccessException e) {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }
}
