package com.jeecms.common.wechat.bean.request.applet;

import java.util.List;

/**
 * 服务器域名request参数
 * 
 * @Description:
 * @author wulongwei
 * @date 2018年10月31日 下午5:16:30
 */
public class ModifyDomainRequest {
	/** add添加, delete删除, set覆盖, get获取。当参数是get时不需要填四个域名字段 */
	private String action;
	/** request合法域名，当action参数是get时不需要此字段 */
	private List<String> requestdomain;
	/** socket合法域名，当action参数是get时不需要此字段 */
	private List<String> wsrequestdomain;
	/** uploadFile合法域名，当action参数是get时不需要此字段 */
	private List<String> uploaddomain;
	/** downloadFile合法域名，当action参数是get时不需要此字段 */
	private List<String> downloaddomain;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getRequestdomain() {
		return requestdomain;
	}

	public void setRequestdomain(List<String> requestdomain) {
		this.requestdomain = requestdomain;
	}

	public List<String> getWsrequestdomain() {
		return wsrequestdomain;
	}

	public void setWsrequestdomain(List<String> wsrequestdomain) {
		this.wsrequestdomain = wsrequestdomain;
	}

	public List<String> getUploaddomain() {
		return uploaddomain;
	}

	public void setUploaddomain(List<String> uploaddomain) {
		this.uploaddomain = uploaddomain;
	}

	public List<String> getDownloaddomain() {
		return downloaddomain;
	}

	public void setDownloaddomain(List<String> downloaddomain) {
		this.downloaddomain = downloaddomain;
	}

	@Override
	public String toString() {
		return "ModifyDomain [action=" + action + ", requestdomain=" + requestdomain + ", wsrequestdomain="
				+ wsrequestdomain + ", uploaddomain=" + uploaddomain + ", downloaddomain=" + downloaddomain + "]";
	}

	public ModifyDomainRequest(String action, List<String> requestdomain, List<String> wsrequestdomain, List<String> uploaddomain,
			List<String> downloaddomain) {
		super();
		this.action = action;
		this.requestdomain = requestdomain;
		this.wsrequestdomain = wsrequestdomain;
		this.uploaddomain = uploaddomain;
		this.downloaddomain = downloaddomain;
	}

	public ModifyDomainRequest() {
		super();
	}

}
