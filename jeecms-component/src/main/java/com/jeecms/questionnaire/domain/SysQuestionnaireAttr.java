/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/18 10:18
 */
public class SysQuestionnaireAttr implements Serializable {

    private static final int MAP_INIT_SIZE = 37;
    /**
     * 页面背景类型（1图片2颜色）
     */
    private static final String PAGE_BACKGROUND_TYPE = "page_background_type";
    /**
     * 页面背景颜色
     */

    private static final String PAGE_BACKGROUND_COLOR = "page_background_color";
    /**
     * 页面背景图
     */
    private static final String PAGE_BACKGROUND_IMAGE = "page_background_image";
    /**
     * 对齐方式(1左2中3右)
     */
    private static final String PAGE_BACKGROUND_IMAGE_ALIGNMENT = "page_background_image_alignment";
    /**
     * 是否重复
     */
    private static final String PAGE_BACKGROUND_IMAGE_REDUNDANT = "page_background_image_redundant";
    /**
     * 透明度
     */
    private static final String PAGE_BACKGROUND_IMAGE_TRANSPARENCY = "page_background_image_transparency";

    /**
     * 页眉图片
     */
    private static final String HEADER_IMAGE = "header_image";

    /**
     * 内容区背景颜色
     */
    private static final String CONTENT_BACKGROUND_COLOR = "content_area_background_color";
    /**
     * 是否使用边框（1是0否）
     */
    private static final String CONTENT_IS_USE_BORDER = "content_is_use_border";
    /**
     * 边框颜色
     */
    private static final String CONTENT_BORDER_COLOR = "content_border_color";
    /**
     * 边框粗细(px)
     */
    private static final String CONTENT_BORDER_THICKNESS = "content_border_thickness";
    /**
     * 边框圆角(px)
     */
    private static final String CONTENT_BORDER_FILLET = "content_border_fillet";

    /**
     * 标题字体大小
     */
    private static final String TITLE_FONT_SIZE = "title_font_size";
    /**
     * 标题字体颜色
     */
    private static final String TITLE_FONT_COLOR = "title_font_color";
    /**
     * 标题字体是否加粗
     */
    private static final String TITLE_FONT_IS_BOLD = "title_font_is_bold";
    /**
     * 标题字体对齐方式
     */
    private static final String TITLE_FONT_ALIGNMENT = "title_font_alignment";

    /**
     * 描述字体大小
     */
    private static final String DESCRIBE_FONT_SIZE = "describe_font_size";
    /**
     * 描述字体颜色
     */
    private static final String DESCRIBE_FONT_COLOR = "describe_font_color";
    /**
     * 描述字体是否加粗
     */
    private static final String DESCRIBE_FONT_BOLD = "describe_font_bold";
    /**
     * 描述字体对齐方式
     */
    private static final String DESCRIBE_FONT_ALIGNMENT = "describe_font_alignment";

    /**
     * 题干字体大小
     */
    private static final String SUBJECT_FONT_SIZE = "subject_font_size";
    /**
     * 题干字体颜色
     */
    private static final String SUBJECT_FONT_COLOR = "subject_font_color";
    /**
     * 题干字体是否加粗
     */
    private static final String SUBJECT_FONT_BOLD = "subject_font_bold";

    /**
     * 选项字体大小
     */
    private static final String OPTION_FONT_SIZE = "option_font_size";
    /**
     * 选项字体颜色
     */
    private static final String OPTION_FONT_COLOR = "option_font_color";
    /**
     * 选项字体是否加粗
     */
    private static final String OPTION_FONT_BOLD = "option_font_bold";

    /**
     * 提交按钮文字
     */
    private static final String SUBMIT_BUTTON_TEXT = "submit_button_text";
    /**
     * 提交按钮字体大小
     */
    private static final String SUBMIT_BUTTON_FONT_SIZE = "submit_button_font_size";
    /**
     * 提交按钮字体颜色
     */
    private static final String SUBMIT_BUTTON_FONT_COLOR = "submit_button_font_color";
    /**
     * 提交按钮字体是否加粗
     */
    private static final String SUBMIT_BUTTON_FONT_BOLD = "submit_button_font_bold";
    /**
     * 提交按钮背景颜色
     */
    private static final String SUBMIT_BUTTON_BACKGROUND_COLOR = "submit_button_background_color";
    /**
     * 提交按钮是否使用边框
     */
    private static final String SUBMIT_BUTTON_IS_BORDER = "submit_button_is_border";
    /**
     * 提交按钮边框颜色
     */
    private static final String SUBMIT_BUTTON_BORDER_COLOR = "submit_button_border_color";
    /**
     * 提交按钮边框粗细
     */
    private static final String SUBMIT_BUTTON_BORDER_THICKNESS = "submit_button_font_thickness";
    /**
     * 提交按钮边框圆角
     */
    private static final String SUBMIT_BUTTON_BORDER_FILLET = "submit_button_border_fillet";
    /**
     * 提交按钮边框宽度
     */
    private static final String SUBMIT_BUTTON_BORDER_WIDTH = "submit_button_border_width";
    /**
     * 提交按钮边框高度
     */
    private static final String SUBMIT_BUTTON_BORDER_HIGH = "submit_button_border_high";

    private Map<String, String> attr;

    public SysQuestionnaireAttr() {
        super();
    }

    public Map<String, String> getAttr() {
        if (null == attr) {
            attr = new HashMap<>(MAP_INIT_SIZE);
        }
        return attr;
    }

    /**
     * 初始化
     */
    public void init() {
        this.setPageBackgroundType(1);
        //this.setPageBackgroundColor();
        this.setPageBackgroundImageAlignment(1);
        this.setPageBackgroundImageRedundant(true);
        this.setPageBackgroundImageTransparency(100);
        //this.setContentBackgroundColor();
        this.setContentIsUseBorder(true);
        //this.setContentBorderColor();
        this.setContentBorderThickness(1);
        this.setContentBorderFillet(0);
        this.setTitleFontSize(24);
        //this.setTitleFontColor();
        this.setTitleFontIsBold(true);
        this.setTitleFontAlignment(2);
        this.setDescribeFontSize(14);
        //this.setDescribeFontColor();
        this.setDescribeFontBold(false);
        this.setDescribeFontAlignment(2);
        this.setSubjectFontSize(14);
        //this.setSubjectFontColor();
        this.setSubjectFontBold(false);
        this.setOptionFontSize(14);
        //this.setOptionFontColor();
        this.setOptionFontBold(false);
        this.setSubmitButtonText("提交");
        this.setSubmitButtonFontSize(14);
        //this.setSubmitButtonFontColor();
        this.setSubmitButtonFontBold(false);
        //this.setSubmitButtonBackgroundColor();
        this.setSubmitButtonIsBorder(true);
        //this.setSubmitButtonBorderColor();
        this.setSubmitButtonBorderThickness(1);
        this.setSubmitButtonBorderFillet(0);
        this.setSubmitButtonBorderWidth(77);
        this.setSubmitButtonBorderHigh(32);
    }

    public void setAttr(Map<String, String> attr) {
        this.attr = attr;
    }

    public Integer getPageBackgroundType() {
        String str = this.attr.get(PAGE_BACKGROUND_TYPE);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 1;
    }

    public void setPageBackgroundType(Integer pageBackgroundType) {
        getAttr().put(PAGE_BACKGROUND_TYPE, String.valueOf(pageBackgroundType));
    }

    public String getPageBackgroundColor() {
        return this.attr.get(PAGE_BACKGROUND_COLOR);
    }

    public void setPageBackgroundColor(String pageBackgroundColor) {
        getAttr().put(PAGE_BACKGROUND_COLOR, pageBackgroundColor);
    }

    public String getPageBackgroundImage() {
        return this.attr.get(PAGE_BACKGROUND_IMAGE);
    }

    public void setPageBackgroundImage(String pageBackgroundImage) {
        getAttr().put(PAGE_BACKGROUND_IMAGE, pageBackgroundImage);
    }

    public Integer getPageBackgroundImageAlignment() {
        String str = this.attr.get(PAGE_BACKGROUND_IMAGE_ALIGNMENT);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 1;
    }

    public void setPageBackgroundImageAlignment(Integer pageBackgroundImageAlignment) {
        getAttr().put(PAGE_BACKGROUND_IMAGE_ALIGNMENT, String.valueOf(pageBackgroundImageAlignment));
    }

    public Boolean getPageBackgroundImageRedundant() {
        String str = this.attr.get(PAGE_BACKGROUND_IMAGE_REDUNDANT);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return true;
    }

    public void setPageBackgroundImageRedundant(Boolean pageBackgroundImageRedundant) {
        getAttr().put(PAGE_BACKGROUND_IMAGE_REDUNDANT, String.valueOf(pageBackgroundImageRedundant));
    }

    public Integer getPageBackgroundImageTransparency() {
        String str = this.attr.get(PAGE_BACKGROUND_IMAGE_TRANSPARENCY);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 100;
    }

    public void setPageBackgroundImageTransparency(Integer pageBackgroundImageTransparency) {
        getAttr().put(PAGE_BACKGROUND_IMAGE_TRANSPARENCY, String.valueOf(pageBackgroundImageTransparency));
    }

    public String getHeaderImage() {
        return this.attr.get(HEADER_IMAGE);
    }

    public void setHeaderImage(String headerImage) {
        getAttr().put(HEADER_IMAGE, headerImage);
    }

    public String getContentBackgroundColor() {
        return this.attr.get(CONTENT_BACKGROUND_COLOR);
    }

    public void setContentBackgroundColor(String contentBackgroundColor) {
        getAttr().put(CONTENT_BACKGROUND_COLOR, contentBackgroundColor);
    }

    public Boolean getContentIsUseBorder() {
        String str = this.attr.get(CONTENT_IS_USE_BORDER);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return true;
    }

    public void setContentIsUseBorder(Boolean contentIsUseBorder) {
        getAttr().put(CONTENT_IS_USE_BORDER, String.valueOf(contentIsUseBorder));
    }

    public String getContentBorderColor() {
        return this.attr.get(CONTENT_BORDER_COLOR);
    }

    public void setContentBorderColor(String contentBorderColor) {
        getAttr().put(CONTENT_BORDER_COLOR, contentBorderColor);
    }

    public Integer getContentBorderThickness() {
        String str = this.attr.get(CONTENT_BORDER_THICKNESS);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 1;
    }

    public void setContentBorderThickness(Integer contentBorderThickness) {
        getAttr().put(CONTENT_BORDER_THICKNESS, String.valueOf(contentBorderThickness));
    }

    public Integer getContentBorderFillet() {
        String str = this.attr.get(CONTENT_BORDER_FILLET);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 0;
    }

    public void setContentBorderFillet(Integer contentBorderFillet) {
        getAttr().put(CONTENT_BORDER_FILLET, String.valueOf(contentBorderFillet));
    }

    public Integer getTitleFontSize() {
        String str = this.attr.get(TITLE_FONT_SIZE);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 24;
    }

    public void setTitleFontSize(Integer titleFontSize) {
        getAttr().put(TITLE_FONT_SIZE, String.valueOf(titleFontSize));
    }

    public String getTitleFontColor() {
        return this.attr.get(TITLE_FONT_COLOR);
    }

    public void setTitleFontColor(String titleFontColor) {
        getAttr().put(TITLE_FONT_COLOR, titleFontColor);
    }

    public Boolean getTitleFontIsBold() {
        String str = this.attr.get(TITLE_FONT_IS_BOLD);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return true;
    }

    public void setTitleFontIsBold(Boolean titleFontIsBold) {
        getAttr().put(TITLE_FONT_IS_BOLD, String.valueOf(titleFontIsBold));
    }

    public Integer getTitleFontAlignment() {
        String str = this.attr.get(TITLE_FONT_ALIGNMENT);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 2;
    }

    public void setTitleFontAlignment(Integer titleFontAlignment) {
        getAttr().put(TITLE_FONT_ALIGNMENT, String.valueOf(titleFontAlignment));
    }

    public Integer getDescribeFontSize() {
        String str = this.attr.get(DESCRIBE_FONT_SIZE);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 14;
    }

    public void setDescribeFontSize(Integer describeFontSize) {
        getAttr().put(DESCRIBE_FONT_SIZE, String.valueOf(describeFontSize));
    }

    public String getDescribeFontColor() {
        return this.attr.get(DESCRIBE_FONT_COLOR);
    }

    public void setDescribeFontColor(String describeFontColor) {
        getAttr().put(DESCRIBE_FONT_COLOR, describeFontColor);
    }

    public Boolean getDescribeFontBold() {
        String str = this.attr.get(DESCRIBE_FONT_BOLD);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return false;
    }

    public void setDescribeFontBold(Boolean describeFontBold) {
        getAttr().put(DESCRIBE_FONT_BOLD, String.valueOf(describeFontBold));
    }

    public Integer getDescribeFontAlignment() {
        String str = this.attr.get(DESCRIBE_FONT_ALIGNMENT);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 2;
    }

    public void setDescribeFontAlignment(Integer describeFontAlignment) {
        getAttr().put(DESCRIBE_FONT_ALIGNMENT, String.valueOf(describeFontAlignment));
    }

    public Integer getSubjectFontSize() {
        String str = this.attr.get(SUBJECT_FONT_SIZE);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 14;
    }

    public void setSubjectFontSize(Integer subjectFontSize) {
        getAttr().put(SUBJECT_FONT_SIZE, String.valueOf(subjectFontSize));
    }

    public String getSubjectFontColor() {
        return this.attr.get(SUBJECT_FONT_COLOR);
    }

    public void setSubjectFontColor(String subjectFontColor) {
        getAttr().put(SUBJECT_FONT_COLOR, subjectFontColor);
    }

    public Boolean getSubjectFontBold() {
        String str = this.attr.get(SUBJECT_FONT_BOLD);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return false;
    }

    public void setSubjectFontBold(Boolean subjectFontBold) {
        getAttr().put(SUBJECT_FONT_BOLD, String.valueOf(subjectFontBold));
    }

    public Integer getOptionFontSize() {
        String str = this.attr.get(OPTION_FONT_SIZE);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 14;
    }

    public void setOptionFontSize(Integer optionFontSize) {
        getAttr().put(OPTION_FONT_SIZE, String.valueOf(optionFontSize));
    }

    public String getOptionFontColor() {
        return this.attr.get(OPTION_FONT_COLOR);
    }

    public void setOptionFontColor(String optionFontColor) {
        getAttr().put(OPTION_FONT_COLOR, optionFontColor);
    }

    public Boolean getOptionFontBold() {
        String str = this.attr.get(OPTION_FONT_BOLD);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return false;
    }

    public void setOptionFontBold(Boolean optionFontBold) {
        getAttr().put(OPTION_FONT_BOLD, String.valueOf(optionFontBold));
    }

    public String getSubmitButtonText() {
        return this.attr.get(SUBMIT_BUTTON_TEXT);
    }

    public void setSubmitButtonText(String submitButtonText) {
        getAttr().put(SUBMIT_BUTTON_TEXT, submitButtonText);
    }

    public Integer getSubmitButtonFontSize() {
        String str = this.attr.get(SUBMIT_BUTTON_FONT_SIZE);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 14;
    }

    public void setSubmitButtonFontSize(Integer buttonFontSize) {
        getAttr().put(SUBMIT_BUTTON_FONT_SIZE, String.valueOf(buttonFontSize));
    }

    public String getSubmitButtonFontColor() {
        return this.attr.get(SUBMIT_BUTTON_FONT_COLOR);
    }

    public void setSubmitButtonFontColor(String submitButtonFontColor) {
        getAttr().put(SUBMIT_BUTTON_FONT_COLOR, submitButtonFontColor);
    }

    public Boolean getSubmitButtonFontBold() {
        String str = this.attr.get(SUBMIT_BUTTON_FONT_BOLD);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return false;
    }

    public void setSubmitButtonFontBold(Boolean submitButtonFontBold) {
        getAttr().put(SUBMIT_BUTTON_FONT_BOLD, String.valueOf(submitButtonFontBold));
    }

    public String getSubmitButtonBackgroundColor() {
        return this.attr.get(SUBMIT_BUTTON_BACKGROUND_COLOR);
    }

    public void setSubmitButtonBackgroundColor(String submitButtonBackgroundColor) {
        getAttr().put(SUBMIT_BUTTON_BACKGROUND_COLOR, submitButtonBackgroundColor);
    }

    public Boolean getSubmitButtonIsBorder() {
        String str = this.attr.get(SUBMIT_BUTTON_IS_BORDER);
        if (StringUtils.isNotBlank(str)) {
            return Boolean.valueOf(str);
        }
        return true;
    }

    public void setSubmitButtonIsBorder(Boolean submitButtonIsBorder) {
        getAttr().put(SUBMIT_BUTTON_IS_BORDER, String.valueOf(submitButtonIsBorder));
    }

    public String getSubmitButtonBorderColor() {
        return this.attr.get(SUBMIT_BUTTON_BORDER_COLOR);
    }

    public void setSubmitButtonBorderColor(String submitButtonBorderColor) {
        getAttr().put(SUBMIT_BUTTON_BORDER_COLOR, submitButtonBorderColor);
    }

    public Integer getSubmitButtonBorderThickness() {
        String str = this.attr.get(SUBMIT_BUTTON_BORDER_THICKNESS);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 1;
    }

    public void setSubmitButtonBorderThickness(Integer submitButtonBorderThickness) {
        getAttr().put(SUBMIT_BUTTON_BORDER_THICKNESS, String.valueOf(submitButtonBorderThickness));
    }

    public Integer getSubmitButtonBorderFillet() {
        String str = this.attr.get(SUBMIT_BUTTON_BORDER_FILLET);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 0;
    }

    public void setSubmitButtonBorderFillet(Integer submitButtonBorderFillet) {
        getAttr().put(SUBMIT_BUTTON_BORDER_FILLET, String.valueOf(submitButtonBorderFillet));
    }

    public Integer getSubmitButtonBorderWidth() {
        String str = this.attr.get(SUBMIT_BUTTON_BORDER_WIDTH);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 77;
    }

    public void setSubmitButtonBorderWidth(Integer submitButtonBorderWidth) {
        getAttr().put(SUBMIT_BUTTON_BORDER_WIDTH, String.valueOf(submitButtonBorderWidth));
    }

    public Integer getSubmitButtonBorderHigh() {
        String str = this.attr.get(SUBMIT_BUTTON_BORDER_HIGH);
        if (StringUtils.isNotBlank(str)) {
            return Integer.parseInt(str);
        }
        return 32;
    }

    public void setSubmitButtonBorderHigh(Integer submitButtonBorderHigh) {
        getAttr().put(SUBMIT_BUTTON_BORDER_HIGH, String.valueOf(submitButtonBorderHigh));
    }
}
