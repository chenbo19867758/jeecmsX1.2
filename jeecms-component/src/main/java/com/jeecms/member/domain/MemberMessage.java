package com.jeecms.member.domain;

import java.io.Serializable;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.system.domain.SysMessage;

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

/**
* 用户接收信息状态实体类
* @author ljw
* @version 1.0
* @date 2018-09-25
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/
@Entity
@Table(name = "jc_member_message")
public class MemberMessage extends AbstractDomain<Integer>  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id ;
    /** 消息id */
    private  Integer messageId ;
    /** 用户id */
    private  Integer memberId ;
    /**  status  1-已读   2-删除  */
    private  Integer status ;   
    /**配置平台消息对象**/
    private  SysMessage sysMessage;
    
	public MemberMessage (){}
	
    @Id
    @Column(name = "id", nullable = false, length = 11)
    @TableGenerator(name = "jc_member_message", pkColumnValue = "jc_member_message", initialValue = 0, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_member_message")
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
    
    @Column(name = "member_id", nullable = false, length = 11)
    public Integer getMemberId () {
        return memberId;
    }

    public void setMemberId (Integer memberId) {
        this.memberId = memberId;
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

}