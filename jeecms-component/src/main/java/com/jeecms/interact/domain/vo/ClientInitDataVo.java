package com.jeecms.interact.domain.vo;/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

/**
 * 传给云端采集需要的栏目和模型数据
 *
 * @author: tom
 * @date: 2019/11/7 18:49
 */
public class ClientInitDataVo implements Serializable {
    String appId;
    JSONArray channels;
    JSONArray models;

    public ClientInitDataVo(String appId, JSONArray channels, JSONArray models) {
        this.appId = appId;
        this.channels = channels;
        this.models = models;
    }

    public ClientInitDataVo() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public JSONArray getChannels() {
        return channels;
    }

    public void setChannels(JSONArray channels) {
        this.channels = channels;
    }

    public JSONArray getModels() {
        return models;
    }

    public void setModels(JSONArray models) {
        this.models = models;
    }

}
