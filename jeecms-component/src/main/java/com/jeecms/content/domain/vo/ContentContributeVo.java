package com.jeecms.content.domain.vo;

import java.util.List;

import com.jeecms.channel.domain.Channel;

/**
 * 查询详细投稿信息
 * 
 * @author: chenming
 * @date: 2019年7月31日 上午11:28:28
 */
public class ContentContributeVo {
	/** 栏目对象 */
	private Channel channel;
	/** 标题 */
	private String title;
	/** 摘要 */
	private String description;
	/** 作者 */
	private String author;
	/** 正文 */
	private String contxt;
	/** 内容的id值 */
	private Integer contentId;
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContxt() {
		return contxt;
	}

	public void setContxt(String contxt) {
		this.contxt = contxt;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public List<Integer> getChannelIds() {
		return getChannel().getChildAllIds();
	}

}
