package com.jeecms.common.util.mediautil;


/**
 * 图片数据的基本信息类
 *
 * @author Zhu Kaixiao
 * @date 2019/5/21 14:47
 **/
public class ImageMetaInfo extends MetaInfo {
    /**
     * 图片宽度，单位为px
     */
    private Integer width;
    /**
     * 图片高度，单位为px
     */
    private Integer height;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
