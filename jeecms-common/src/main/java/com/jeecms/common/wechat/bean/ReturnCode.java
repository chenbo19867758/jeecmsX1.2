package com.jeecms.common.wechat.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link}https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433747234
 * @Description: 微信公众平台接口全局返回码.
 * @author: wangqq
 * @date:   2018年7月25日 上午10:07:30     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ReturnCode {

    @SuppressWarnings("serial")
    private static final Map<Integer, String> ERR_CODE_TO_ERR_MSG = new HashMap<Integer, String>() {
        {
            put(-1, "系统繁忙");
            put(0, "请求成功");
            put(40001, "不合法的调用凭证");
            put(40002, "不合法的grant_type");
            put(40003, "不合法的OpenID");
            put(40004, "不合法的媒体文件类型");
            put(40007, "不合法的媒体文件id");
            put(40008, "不合法的消息类型");
            put(40009, "不合法的图片文件大小");
            put(40010, "不合法的语音文件大小");
            put(40011, "不合法的视频文件大小");
            put(40012, "不合法的缩略图文件大小");
            put(40013, "不合法的APPID");
            put(40014, "不合法的access_token");
            put(40015, "不合法的菜单类型");
            put(40016, "不合法的按钮个数");
            put(40017, "不合法的按钮类型");
            put(40018, "不合法的按钮名字长度");
            put(40019, "不合法的按钮KEY长度");
            put(40020, "不合法的按钮URL长度");
            put(40023, "不合法的子菜单按钮个数");
            put(40024, "不合法的子菜单按钮类型");
            put(40025, "不合法的子菜单按钮名字长度");
            put(40026, "不合法的子菜单按钮KEY长度");
            put(40027, "不合法的子菜单按钮URL长度");
            put(40029, "不合法或已过期的code");
            put(40030, "不合法的refresh_token");
            put(40036, "不合法的template_id长度");
            put(40037, "不合法的模板id");
            put(40039, "不合法的URL长度");
            put(40048, "不合法的url域名");
            put(40054, "不合法的子菜单按钮url域名");
            put(40055, "不合法的菜单按钮url域名");
            put(40066, "不合法的URL");
            put(41001, "缺失access_token参数");
            put(41002, "缺失appid参数");
            put(41003, "缺失refresh_token参数");
            put(41004, "缺失secret参数");
            put(41005, "缺失二进制媒体文件");
            put(41006, "缺失media_id参数");
            put(41007, "缺失子菜单数据");
            put(41008, "缺失code参数");
            put(41009, "缺失openid参数");
            put(41010, "缺失url参数"); 
            put(42001, "access_token超时");
            put(42002, "refresh_token超时");
            put(42003, "code超时");
            put(43001, "需要GET请求");
            put(43002, "需要POST请求");
            put(43003, "需要HTTPS请求");
            put(43004, "需要订阅关系");
            put(44001, "空白的二进制数据");
            put(44002, "POST的数据包为空");
            put(44003, "图文消息内容为空");
            put(44004, "文本消息内容为空");
            put(44005, "空白的列表");
            put(45001, "二进制文件超过限制");
            put(45002, "消息内容超过限制");
            put(45003, "标题字段超过限制");
            put(45004, "描述字段超过限制");
            put(45005, "链接字段超过限制");
            put(45006, "图片链接字段超过限制");
            put(45007, "语音播放时间超过限制");
            put(45008, "article参数超过限制");
            put(45009, "接口调动频率超过限制");
            put(45010, "建立菜单被限制");
            put(45011, "频率限制");
            put(45012, "模板大小超过限制");
            put(45016, "不能修改默认组");
            put(45017, "修改组名过长");
            put(45018, "分组数量超过上限");  
            put(50001, "接口未授权");
            
        }
    };

    /**
     * 通过返回码获取返回信息.
     *
     * @param errCode 错误码
     * @return {String}
     */
    public static String get(int errCode) {
        return ERR_CODE_TO_ERR_MSG.get(errCode);
    }
}




