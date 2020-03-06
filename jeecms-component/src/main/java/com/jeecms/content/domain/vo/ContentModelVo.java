/**
 * 
 */
package com.jeecms.content.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:内容模型模板选择Vo对象
 * @author: tom
 * @date: 2018年11月12日 下午2:22:02
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ContentModelVo implements Serializable {

	private static final long serialVersionUID = 5789301320470249965L;
	Integer modelId;
	String name;
	List<String> pcContentTpls;
	List<String> mobileContentTpls;

	List<String> pcChannelTpls;
	List<String> mobileChannelTpls;

	public ContentModelVo(Integer model, String name, List<String> pcContentTpls, List<String> mobileContentTpls) {
		super();
		this.modelId = model;
		this.name = name;
		this.pcContentTpls = pcContentTpls;
		this.mobileContentTpls = mobileContentTpls;
	}

	public ContentModelVo(Integer modelId, String name, List<String> pcContentTpls, List<String> mobileContentTpls,
			List<String> pcChannelTpls, List<String> mobileChannelTpls) {
		super();
		this.modelId = modelId;
		this.name = name;
		this.pcContentTpls = pcContentTpls;
		this.mobileContentTpls = mobileContentTpls;
		this.pcChannelTpls = pcChannelTpls;
		this.mobileChannelTpls = mobileChannelTpls;
	}

	public Integer getModelId() {
		return modelId;
	}

	public String getName() {
		return name;
	}

	public List<String> getMobileTpls() {
		return mobileContentTpls;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPcContentTpls() {
		return pcContentTpls;
	}

	public List<String> getMobileContentTpls() {
		return mobileContentTpls;
	}

	public List<String> getPcChannelTpls() {
		return pcChannelTpls;
	}

	public List<String> getMobileChannelTpls() {
		return mobileChannelTpls;
	}

	public void setPcContentTpls(List<String> pcContentTpls) {
		this.pcContentTpls = pcContentTpls;
	}

	public void setMobileContentTpls(List<String> mobileContentTpls) {
		this.mobileContentTpls = mobileContentTpls;
	}

	public void setPcChannelTpls(List<String> pcChannelTpls) {
		this.pcChannelTpls = pcChannelTpls;
	}

	public void setMobileChannelTpls(List<String> mobileChannelTpls) {
		this.mobileChannelTpls = mobileChannelTpls;
	}

}
