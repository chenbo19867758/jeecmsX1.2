/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.jeecms.common.base.domain.AbstractIdDomain;

/**
 * 内容正文实体类
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 */
@Entity
@Table(name = "jc_content_txt")
public class ContentTxt extends AbstractIdDomain<Integer> {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	/** 内容ID */
	private Integer contentId;
	/** 字段 */
	private String attrKey;
	/** 正文 */
	private String attrTxt;

	/** 内容对象 */
	private Content content;

	
	
	public ContentTxt(String attrKey, String attrTxt,Integer contentId) {
		super();
		this.attrKey = attrKey;
		this.attrTxt = attrTxt;
		this.contentId = contentId;
	}

	/**
	 * ueditor采用分页
	 */
	public static String PAGE_START = "[NextPage]";
	public static String PAGE_END = "[/NextPage]";

	/**
	 * 获取正文页数
	 * 
	 * @Title: getTxtCount
	 * @return: int
	 */
	@Transient
	public int getTxtCount() {
		String txt = getAttrTxt();
		if (StringUtils.isBlank(txt)) {
			return 1;
		} else {
			return StringUtils.countMatches(txt, PAGE_START) + 1;
		}
	}

	/**
	 * 获取第pageNo页正文内容
	 * 
	 * @Title: getTxtByNo
	 * @param pageNo 第pageNo页
	 * @return: String
	 */
	public String getTxtByNo(int pageNo) {
		String txt = getAttrTxt();
		if (StringUtils.isBlank(txt) || pageNo < 1) {
			return null;
		}
		int start = 0;
		int end = 0;
		for (int i = 0; i < pageNo; i++) {
			// 如果不是第一页
			if (i != 0) {
				start = txt.indexOf(PAGE_END, end);
				if (start == -1) {
					return null;
				} else {
					start += PAGE_END.length();
				}
			}
			end = txt.indexOf(PAGE_START, start);
			if (end == -1) {
				end = txt.length();
			}
		}
		return txt.substring(start, end);
	}

	public ContentTxt() {
	}

	@Id
	@TableGenerator(name = "jc_content_txt", pkColumnValue = "jc_content_txt", initialValue = 0, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_txt")
	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "content_id", nullable = false, length = 11)
	public Integer getContentId() {
		return this.contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "attr_key", nullable = false, length = 50)
	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	@Column(name = "attr_txt", length = 715827882)
	public String getAttrTxt() {
		return attrTxt;
	}

	public void setAttrTxt(String attrTxt) {
		this.attrTxt = attrTxt;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}