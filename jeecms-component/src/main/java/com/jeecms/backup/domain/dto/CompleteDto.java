package com.jeecms.backup.domain.dto;


import javax.validation.constraints.NotNull;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/5 17:42
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CompleteDto {
    @NotNull
    private Integer backupId;
    private String bakFilePath;
    private Long fileSize;
    private String errMsg;
    @NotNull
    private Boolean success;
    /**是否开启FTP或者OSS**/
    private Boolean third;
    @Override
    public String toString() {
        return "CompleteDto{" +
                "backupId=" + backupId +
                ", bakFilePath='" + bakFilePath + '\'' +
                ", fileSize=" + fileSize +
                ", errMsg='" + errMsg + '\'' +
                ", success=" + success +
                '}';
    }

    public Integer getBackupId() {
        return backupId;
    }

    public void setBackupId(Integer backupId) {
        this.backupId = backupId;
    }

    public String getBakFilePath() {
        return bakFilePath;
    }

    public void setBakFilePath(String bakFilePath) {
        this.bakFilePath = bakFilePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

	public Boolean getThird() {
		return third;
	}

	public void setThird(Boolean third) {
		this.third = third;
	}
}
