/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.wechat.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 开放平台应用配置信息(第三方服务配置)
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月7日 下午3:43:33
 */
@Entity
@Table(name = "jc_wechat_open")
public class AbstractWeChatOpen extends AbstractDomain<Integer>
        implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    /** 名称 */
    private String name;
    /** api地址(服务开发方的appid) */
    private String appId;
    /** AppSecret */
    private String appSecret;
    /** 消息加解密Key */
    private String messageDecryptKey;
    /** 消息校验Token */
    private String messageValidateToken;

    public AbstractWeChatOpen() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @TableGenerator(name = "jc_sys_wechat_open", pkColumnValue = "jc_sys_wechat_open", initialValue = 0, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_wechat_open")
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "app_id", length = 50)
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @NotBlank
    @Column(name = "u_name", length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    @Column(name = "app_secret", length = 150)
    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @NotBlank
    @Column(name = "message_decrypt_key", length = 50)
    public String getMessageDecryptKey() {
        return messageDecryptKey;
    }

    public void setMessageDecryptKey(String messageDecryptKey) {
        this.messageDecryptKey = messageDecryptKey;
    }

    @NotBlank
    @Column(name = "message_validate_token", length = 50)
    public String getMessageValidateToken() {
        return messageValidateToken;
    }

    public void setMessageValidateToken(String messageValidateToken) {
        this.messageValidateToken = messageValidateToken;
    }

}
