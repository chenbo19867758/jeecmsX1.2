package com.jeecms.common.wechat.bean.response.applet;

import java.util.List;
/**
 * 重置域名 数据返回
 * @Description:
 * @author wulongwei
 * @date   2018年11月2日 下午2:31:32
 */
public class ModifyDomainResponse extends BaseAppletResponse{

	/** request合法域名，当action参数是get时不需要此字段 */
	private List<String> requestdomain;
	
	/** socket合法域名，当action参数是get时不需要此字段 */
	private List<String> wsrequestdomain;
	
	/** uploadFile合法域名，当action参数是get时不需要此字段 */
	private List<String> uploaddomain;
	
	/** downloadFile合法域名，当action参数是get时不需要此字段 */
	private List<String> downloaddomain;



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
		return "ModifyDomainResponse [requestdomain=" + requestdomain + ", wsrequestdomain=" + wsrequestdomain
				+ ", uploaddomain=" + uploaddomain + ", downloaddomain=" + downloaddomain + "]";
	}

	public ModifyDomainResponse(List<String> requestdomain, List<String> wsrequestdomain, List<String> uploaddomain,
			List<String> downloaddomain) {
		super();
		this.requestdomain = requestdomain;
		this.wsrequestdomain = wsrequestdomain;
		this.uploaddomain = uploaddomain;
		this.downloaddomain = downloaddomain;
	}

	public ModifyDomainResponse() {
		super();
	}
	
	
}
