package com.jeecms.common.wechat.bean;

import java.io.ByteArrayOutputStream;

/**
 * 文件Bean.
 *
 * @author vioao
 */
public class MediaFile {
    private String fileName;
    private String fullName;
    private String suffix;
    private long contentLength;
    private String contentType;
    /**InputStream 首次读取至末尾，后续再次读取，stream已关闭无法操作，
     * 	此处将InputStream先转换成ByteArrayOutputStream，后续需要使用再次转换成InputeStream*/
    private ByteArrayOutputStream fileStream;
    
    private String error;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ByteArrayOutputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(ByteArrayOutputStream fileStream) {
        this.fileStream = fileStream;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    @Override
    public String toString() {
        return "MediaFile{"
                + "fileName='" + fileName + '\''
                + ", fullName='" + fullName + '\''
                + ", suffix='" + suffix + '\''
                + ", contentLength=" + contentLength
                + ", contentType='" + contentType + '\''
                + ", error='" + error + '\''
                + '}';
    }
}
