package com.jeecms.interact.domain.vo;/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import java.util.Arrays;
import java.util.List;

/**
 * 采集结果
 * @author: tom
 * @date: 2019/11/18 17:54   
 */
public class CollectContent {
    private String title;//标题
    private String outLink;//文章链接/百度链接
    private String releaseTime;//时间
    private String contentSourceId;//来源
    private String contxt;//内容
    private String description;//描述
    private List<String> resource;//图片(微博图集)
    private Integer forward;//转发数
    private Integer repeat;//评论数
    private Integer praised;//点赞数

    /**获取之前默认的已处理的json数据中的key*/
    public static List<String> getDefKeys(){
        return Arrays.asList("title","outLink","releaseTime","contentSourceId","contxt","description","resource","forward","repeat","praised");
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOutLink() {
        return outLink;
    }

    public void setOutLink(String outLink) {
        this.outLink = outLink;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSourceId(String contentSourceId) {
        this.contentSourceId = contentSourceId;
    }

    public String getContxt() {
        return contxt;
    }

    public void setContxt(String contxt) {
        this.contxt = contxt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getResource() {
        return resource;
    }

    public void setResource(List<String> resource) {
        this.resource = resource;
    }

    public Integer getForward() {
        return forward;
    }

    public void setForward(Integer forward) {
        this.forward = forward;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Integer getPraised() {
        return praised;
    }

    public void setPraised(Integer praised) {
        this.praised = praised;
    }
}
