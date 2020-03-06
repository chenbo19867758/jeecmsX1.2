/**   
 * @Copyright:  江西金磊科技发展有限公司
 * All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.wechat.domain.dto;

import java.util.ArrayList;
import java.util.List;

/**   
 * 标签Dto
 * @author: ljw
 * @date:   2019年5月28日 下午2:16:00     
 */
public class WechatTagDto {
	
	/**微信公众号APPID**/
	private String appId;
	/**粉丝IDs**/
	private List<Integer> ids = new ArrayList<Integer>(10);
	/**标签IDs**/
	private List<Integer> tagids = new ArrayList<Integer>(10);
	/**标签ID**/
	private Integer tagid;
	/**标签名称**/
	private String tagname;
	
	public WechatTagDto() {}

	public Integer getTagid() {
		return tagid;
	}

	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public List<Integer> getTagids() {
		return tagids;
	}

	public void setTagids(List<Integer> tagids) {
		this.tagids = tagids;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
