/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import com.jeecms.system.domain.Link;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/13 17:04
 */

public class LinkVo implements Serializable {
	private String linkType;
	private Page<Link> links;

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Page<Link> getLinks() {
		return links;
	}

	public void setLinks(Page<Link> links) {
		this.links = links;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof LinkVo)) {
			return false;
		}

		LinkVo linkVo = (LinkVo) o;

		if (getLinkType() != null ? !getLinkType().equals(linkVo.getLinkType()) :
				linkVo.getLinkType() != null) {
			return false;
		}
		return getLinks() != null ? getLinks().equals(linkVo.getLinks()) : linkVo.getLinks() == null;
	}

	@Override
	public int hashCode() {
		int result = getLinkType() != null ? getLinkType().hashCode() : 0;
		result = 31 * result + (getLinks() != null ? getLinks().hashCode() : 0);
		return result;
	}
}
