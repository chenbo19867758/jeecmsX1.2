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

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.AbstractIdDomain;

/**
 * 栏目正文实体类
 * 
 * @author: tom
 * @date: 2019年5月23日 下午2:44:22
 */
@Entity
@Table(name = "jc_channel_txt")
public class ChannelTxt extends AbstractIdDomain<Integer> {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 栏目ID */
	private Integer channelId;
	/** 字段 */
	private String attrKey;
	/** 正文 */
	private String attrTxt;

	/** 栏目对象 */
	private Channel channel;

	
	
	public ChannelTxt(String attrKey, String attrTxt) {
		super();
		this.attrKey = attrKey;
		this.attrTxt = attrTxt;
	}

	
	
	public ChannelTxt(Integer channelId, String attrKey, String attrTxt) {
		super();
		this.channelId = channelId;
		this.attrKey = attrKey;
		this.attrTxt = attrTxt;
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

	public ChannelTxt() {
	}

	@Id
	@TableGenerator(name = "jc_channel_txt", pkColumnValue = "jc_channel_txt", 
					initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_channel_txt")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "channel_id", nullable = false, length = 11)
	public Integer getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "attr_key", nullable = false, length = 50)
	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	@Column(name = "attr_txt", nullable = false, length = 715827882)
	public String getAttrTxt() {
		return attrTxt;
	}

	public void setAttrTxt(String attrTxt) {
		this.attrTxt = attrTxt;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", insertable = false, updatable = false)
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}