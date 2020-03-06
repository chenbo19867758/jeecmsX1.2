/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.system.domain.SysMessage;

/**
 * 管理员接收消息状态表
* @author ljw
* @version 1.0
* @date 2019-01-23
*/
@Entity
@Table(name = "jc_sys_user_message")
public class CoreUserMessage extends AbstractDomain<Integer>  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id ;
    /** 消息id */
    private  Integer messageId ;
    /**用户ID**/
    private Integer userId;
    /**  status  1-已读   2-删除  */
    private  Integer status ;
    /**配置平台消息对象**/
    private  SysMessage sysMessage;
    
	public CoreUserMessage (){}
	
    @Id
    @Column(name = "id", nullable = false, length = 11)
    @TableGenerator(name = "jc_sys_user_message", pkColumnValue = "jc_sys_user_message", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_user_message")
    @Override
    public Integer getId () {
        return this.id;
    }

    @Override
    public void setId (Integer id) {
        this.id = id;
    }

    @Column(name = "message_id", nullable = false, length = 11)
    public Integer getMessageId () {
        return messageId;
    }

    public void setMessageId (Integer messageId) {
        this.messageId = messageId;
    }
    
    @Column(name = "status", nullable = false, length = 6)
    public Integer getStatus () {
        return status;
    }

    public void setStatus (Integer status) {
        this.status = status;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="message_id",insertable=false,updatable=false)
	public SysMessage getSysMessage() {
		return sysMessage;
	}

	public void setSysMessage(SysMessage sysMessage) {
		this.sysMessage = sysMessage;
	}

	@Column(name = "user_id", nullable = false, length = 11)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}