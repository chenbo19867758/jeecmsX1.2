package com.jeecms.common.wechat.bean.response.mp.share;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;

/**
 * 获取到jsapi
 * @author: chenming
 * @date:   2019年10月31日 上午10:59:29
 */
public class GetTicketResponse extends BaseResponse{
	
	private String ticket;
	
	private Integer expiresIn;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
}
