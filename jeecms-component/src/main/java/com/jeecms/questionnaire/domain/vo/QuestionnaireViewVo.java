/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/31 11:50
 */
public class QuestionnaireViewVo {

    private Integer id;
    private String previewUrl;

    public QuestionnaireViewVo() {
        super();
    }

    public QuestionnaireViewVo(Integer id, String previewUrl) {
        this.id = id;
        this.previewUrl = previewUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
