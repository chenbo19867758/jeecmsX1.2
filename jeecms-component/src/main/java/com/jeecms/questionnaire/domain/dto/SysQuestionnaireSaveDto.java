/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.dto;

import com.jeecms.questionnaire.domain.vo.BgConfigBean;
import com.jeecms.questionnaire.domain.vo.ContConfigBean;
import com.jeecms.questionnaire.domain.vo.FontConfigBean;
import com.jeecms.questionnaire.domain.vo.HeadConfigBean;
import com.jeecms.questionnaire.domain.vo.SubConfigBean;

import java.util.Date;
import java.util.List;

/**
 * 问卷新增Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/22 10:39
 */
public class SysQuestionnaireSaveDto {
    /**
     * 标题
     */
    private String title = "这里显示标题最多50个字";
    /**
     * 描述
     */
    private String details = "这里显示描述最多150个字";
    /**
     * 状态
     */
    private int status = 0;

    /**
     * 封面图片
     */
    private Integer coverPic;
    /**
     * 是否使用验证码
     */
    private Boolean isVerification = false;
    /**
     * 提交成功后处理方式（1显示文字信息2跳转到指定页面3显示结果）
     */
    private Short processType = 1;
    /**
     * 文字信息/指定页面
     */
    private String prompt = "您的答案已经提交，感谢您的参与！";
    /**
     * 工作流id
     */
    private Integer workflowId;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 是否登录后才能投票
     */
    private Boolean answerLimit = false;
    /**
     * 用户答题次数限制
     */
    private Integer userAnswerFrequencyLimit;
    /**
     * 用户答题次数限制单位（1只能2每小时3每天）
     */
    private Short userAnswerFrequencyLimitUnit;
    /**
     * ip答题次数限制
     */
    private Integer ipAnswerFrequencyLimit = 1;
    /**
     * ip答题次数限制单位（1只能2每小时3每天）
     */
    private Short ipAnswerFrequencyLimitUnit = 1;
    /**
     * 设备答题次数限制
     */
    private Integer deviceAnswerFrequencyLimit = 1;
    /**
     * 设备答题次数限制单位（1只能2每小时3每天）
     */
    private Short deviceAnswerFrequencyLimitUnit = 1;
    /**
     * 是否只能微信使用
     */
    private Boolean isOnlyWechat = false;
    /**
     * 微信用户答题次数限制
     */
    private Integer wechatAnswerFrequencyLimit;
    /**
     * 微信用户答题次数限制（1只能2每小时3每天）
     */
    private Short wechatAnswerFrequencyLimitUnit;
    /**
     * 分享logo图
     */
    private Integer shareLogo;
    /**
     * 说明文字
     */
    private String description;

    private List<QuestionnaireSubjectDto> subjects;

    /**
     * bgConfig : {"bgType":2,"bgImage":"","alignType":1,"opacity":100,"isRepeat":1,"bgColor":"#F0F0F0"}
     * headConfig : {"bgImage":""}
     * contConfig : {"bgColor":"#ffffff","hasBorder":1,"borderColor":"#e8e8e8","borderWidth":1,"borderRadius":0}
     * fontConfig : {"titleStyle":{"fontSize":24,"fontWigth":600,"fontColor":"#333333","fontAlign":"left"},"descStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"},"stemStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"},"optStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333","fontAlign":"left"}}
     * subConfig : {"text":"","fontStyle":{"fontSize":14,"fontWigth":400,"fontColor":"#333333"},"bgColor":"#ffffff","hasBorder":1,"borderColor":"#e8e8e8","borderWidth":1,"borderRadius":0,"btnWidth":77,"btnHeight":32}
     */

    private BgConfigBean bgConfig = new BgConfigBean();
    private HeadConfigBean headConfig = new HeadConfigBean();
    private ContConfigBean contConfig = new ContConfigBean();
    private FontConfigBean fontConfig = new FontConfigBean();
    private SubConfigBean subConfig = new SubConfigBean();

    public SysQuestionnaireSaveDto() {
        super();
    }

    public SysQuestionnaireSaveDto(String title, String details, int status, Integer coverPic,
                                   Boolean isVerification, Short processType, String prompt,
                                   Integer workflowId, Date beginTime, Date endTime, Boolean answerLimit,
                                   Integer userAnswerFrequencyLimit, Short userAnswerFrequencyLimitUnit,
                                   Integer ipAnswerFrequencyLimit, Short ipAnswerFrequencyLimitUnit,
                                   Integer deviceAnswerFrequencyLimit, Short deviceAnswerFrequencyLimitUnit,
                                   Boolean isOnlyWechat, Integer wechatAnswerFrequencyLimit,
                                   Short wechatAnswerFrequencyLimitUnit, Integer shareLogo,
                                   String description, List<QuestionnaireSubjectDto> subjects,
                                   BgConfigBean bgConfig, HeadConfigBean headConfig,
                                   ContConfigBean contConfig, FontConfigBean fontConfig, SubConfigBean subConfig) {
        this.title = title;
        this.details = details;
        this.status = status;
        this.coverPic = coverPic;
        this.isVerification = isVerification;
        this.processType = processType;
        this.prompt = prompt;
        this.workflowId = workflowId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.answerLimit = answerLimit;
        this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
        this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
        this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
        this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
        this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
        this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
        this.isOnlyWechat = isOnlyWechat;
        this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
        this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
        this.shareLogo = shareLogo;
        this.description = description;
        this.subjects = subjects;
        this.bgConfig = bgConfig;
        this.headConfig = headConfig;
        this.contConfig = contConfig;
        this.fontConfig = fontConfig;
        this.subConfig = subConfig;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(Integer coverPic) {
        this.coverPic = coverPic;
    }

    public Boolean getVerification() {
        return isVerification;
    }

    public void setVerification(Boolean verification) {
        isVerification = verification;
    }

    public Short getProcessType() {
        return processType;
    }

    public void setProcessType(Short processType) {
        this.processType = processType;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getAnswerLimit() {
        return answerLimit;
    }

    public void setAnswerLimit(Boolean answerLimit) {
        this.answerLimit = answerLimit;
    }

    public Integer getUserAnswerFrequencyLimit() {
        return userAnswerFrequencyLimit;
    }

    public void setUserAnswerFrequencyLimit(Integer userAnswerFrequencyLimit) {
        this.userAnswerFrequencyLimit = userAnswerFrequencyLimit;
    }

    public Short getUserAnswerFrequencyLimitUnit() {
        return userAnswerFrequencyLimitUnit;
    }

    public void setUserAnswerFrequencyLimitUnit(Short userAnswerFrequencyLimitUnit) {
        this.userAnswerFrequencyLimitUnit = userAnswerFrequencyLimitUnit;
    }

    public Integer getIpAnswerFrequencyLimit() {
        return ipAnswerFrequencyLimit;
    }

    public void setIpAnswerFrequencyLimit(Integer ipAnswerFrequencyLimit) {
        this.ipAnswerFrequencyLimit = ipAnswerFrequencyLimit;
    }

    public Short getIpAnswerFrequencyLimitUnit() {
        return ipAnswerFrequencyLimitUnit;
    }

    public void setIpAnswerFrequencyLimitUnit(Short ipAnswerFrequencyLimitUnit) {
        this.ipAnswerFrequencyLimitUnit = ipAnswerFrequencyLimitUnit;
    }

    public Integer getDeviceAnswerFrequencyLimit() {
        return deviceAnswerFrequencyLimit;
    }

    public void setDeviceAnswerFrequencyLimit(Integer deviceAnswerFrequencyLimit) {
        this.deviceAnswerFrequencyLimit = deviceAnswerFrequencyLimit;
    }

    public Short getDeviceAnswerFrequencyLimitUnit() {
        return deviceAnswerFrequencyLimitUnit;
    }

    public void setDeviceAnswerFrequencyLimitUnit(Short deviceAnswerFrequencyLimitUnit) {
        this.deviceAnswerFrequencyLimitUnit = deviceAnswerFrequencyLimitUnit;
    }

    public Boolean getOnlyWechat() {
        return isOnlyWechat;
    }

    public void setOnlyWechat(Boolean onlyWechat) {
        isOnlyWechat = onlyWechat;
    }

    public Integer getWechatAnswerFrequencyLimit() {
        return wechatAnswerFrequencyLimit;
    }

    public void setWechatAnswerFrequencyLimit(Integer wechatAnswerFrequencyLimit) {
        this.wechatAnswerFrequencyLimit = wechatAnswerFrequencyLimit;
    }

    public Short getWechatAnswerFrequencyLimitUnit() {
        return wechatAnswerFrequencyLimitUnit;
    }

    public void setWechatAnswerFrequencyLimitUnit(Short wechatAnswerFrequencyLimitUnit) {
        this.wechatAnswerFrequencyLimitUnit = wechatAnswerFrequencyLimitUnit;
    }

    public Integer getShareLogo() {
        return shareLogo;
    }

    public void setShareLogo(Integer shareLogo) {
        this.shareLogo = shareLogo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionnaireSubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<QuestionnaireSubjectDto> subjects) {
        this.subjects = subjects;
    }

    public BgConfigBean getBgConfig() {
        return bgConfig;
    }

    public void setBgConfig(BgConfigBean bgConfig) {
        this.bgConfig = bgConfig;
    }

    public HeadConfigBean getHeadConfig() {
        return headConfig;
    }

    public void setHeadConfig(HeadConfigBean headConfig) {
        this.headConfig = headConfig;
    }

    public ContConfigBean getContConfig() {
        return contConfig;
    }

    public void setContConfig(ContConfigBean contConfig) {
        this.contConfig = contConfig;
    }

    public FontConfigBean getFontConfig() {
        return fontConfig;
    }

    public void setFontConfig(FontConfigBean fontConfig) {
        this.fontConfig = fontConfig;
    }

    public SubConfigBean getSubConfig() {
        return subConfig;
    }

    public void setSubConfig(SubConfigBean subConfig) {
        this.subConfig = subConfig;
    }
}
