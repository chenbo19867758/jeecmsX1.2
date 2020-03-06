/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jeecms.content.constants.ContentConstant;

/**
 * 内容搜索Dto
 * 
 * @author: ljw
 * @date: 2019年5月16日 上午9:44:12
 */
public class ContentSearchDto {

	/** 页码 **/
	private Integer page;
	/** 数量 **/
	private Integer size;
	/** 排序方式 **/
	private Integer orderType;
	/** 内容状态 **/
	private List<Integer> status;
	/** 内容类型 **/
	private Integer contentType;
	/** 是否我创建的 **/
	private Boolean myself;
	/** 是否重新编辑的 **/
	private Boolean update;
	/** 起始创建开始时间 **/
	private Date createStartTime;
	/** 起始创建结束时间 **/
	private Date createEndTime;
	/** 起始发布开始时间 **/
	private Date releaseStartTime;
	/** 起始发布结束时间 **/
	private Date releaseEndTime;
	/** 创建方式 **/
	private Integer createType;
	/** 内容模型 **/
	private Integer modelId;
	/** 关键字类型 **/
	private Integer keyType;
	/** 关键字key **/
	private String key;
	/** 用户ID **/
	private Integer userId;
	/** 栏目ID **/
	private Integer[] channelIds;
	/** 内容密级ID **/
	private Integer[] contentSecretIds;
	/** 发文字号-机关代字 **/
	private Integer issueOrg;
	/** 发文字号-年份 **/
	private Integer issueYear;
	/** 发文字号-顺序号 **/
	private String issueNum;
	/** 站点ID **/
	private Integer siteId;
	/** 是否置顶 */
	private Boolean isTop;
	/** 是否进入回收站 */
	private Boolean recycle;
	/** 归档开始时间 **/
	private Date fileStartTime;
	/** 归档结束时间 **/
	private Date fileEndTime;
	/** 内容id 用于排除自己 */
	private Integer contentId;
	/** 内容密级开启筛选条件 */
	private Boolean secret;
	
	/**
	 * 构造器
	 * 
	 * @param siteId           站点ID
	 * @param channelIds       栏目ID
	 * @param modelId          内容模型
	 * @param orderType        排序方式
	 * @param status           内容状态
	 * @param typeIds          内容类型
	 * @param myself           是否我创建的
	 * @param update           是否重新编辑的
	 * @param createStartTime  创建开始时间
	 * @param createEndTime    创建结束时间
	 * @param releaseStartTime 发布开始时间
	 * @param releaseEndTime   发布结束时间
	 * @param createType       创建方式
	 * @param keyType          关键字类型
	 * @param key              关键字key
	 * @param userId           用户ID
	 * @param contentSecretIds 内容密级ID
	 * @param issueOrg           发文字号-机关代字
	 * @param issueYear          发文字号-年份
	 * @param issueNum           发文字号-顺序号
	 * @param recycle          是否进入回收站
	 */
	public ContentSearchDto(Integer siteId, Integer[] channelIds, Integer modelId, Integer orderType, 
			Integer status,
			Integer typeIds, Boolean myself, Boolean update, Date createStartTime, Date createEndTime,
			Date releaseStartTime, Date releaseEndTime, Integer createType, Integer keyType, String key,
			Integer userId,
			Integer[] contentSecretIds, Integer issueOrg, Integer issueYear, String issueNum, 
			Boolean recycle) {
		super();
		this.orderType = orderType;
		setStatus(Arrays.asList(status));
		this.contentType = typeIds;
		this.myself = myself;
		this.update = update;
		this.createStartTime = createStartTime;
		this.createEndTime = createEndTime;
		this.releaseStartTime = releaseStartTime;
		this.releaseEndTime = releaseEndTime;
		this.createType = createType;
		this.modelId = modelId;
		this.keyType = keyType;
		this.key = key;
		this.userId = userId;
		this.channelIds = channelIds;
		this.contentSecretIds = contentSecretIds;
		this.setIssueOrg(issueOrg);
		this.setIssueYear(issueYear);
		this.setIssueNum(issueNum);
		this.siteId = siteId;
		this.recycle = recycle;
	}

	/** 构造函数 **/
	public ContentSearchDto(Integer siteId, Integer[] channelIds, List<Integer> status, Date releaseStartTime,
			Date releaseEndTime, Boolean recycle) {
		super();
		this.releaseStartTime = releaseStartTime;
		this.releaseEndTime = releaseEndTime;
		this.channelIds = channelIds;
		this.siteId = siteId;
		this.recycle = recycle;
		this.status = status;
	}

	/**搜索对象**/
	public static ContentSearchDto buildForSearch(Integer siteId, Integer[] channelIds, Date releaseStartTime,
			Date releaseEndTime) {
		return new ContentSearchDto(siteId, channelIds, ContentConstant.getNotAllowRepeatStatus(), 
				releaseStartTime,
				releaseEndTime, false);
	}

	/**
	 * 内容智能审核列表
	 * @param status		内容状态
	 * @param keyType		关键字类型
	 * @param key			关键字(标题)
	 * @param channelIds	栏目id集合
	 * @param siteId
	 */
	public ContentSearchDto(List<Integer> status, Integer keyType, String key, Integer[] channelIds, Integer siteId) {
		super();
		this.status = status;
		this.keyType = keyType;
		this.key = key;
		this.channelIds = channelIds;
		this.siteId = siteId;
	}

	/**
	 * 构造器
	 * 
	 * @param siteId     站点ID
	 * @param channelIds 栏目ID
	 * @param orderType  排序方式
	 * @param status     内容状态
	 * @param recycle    是否进入回收站
	 */
	public ContentSearchDto(Integer siteId, Integer[] channelIds, Integer orderType, List<Integer> status,
			Boolean recycle) {
		super();
		this.orderType = orderType;
		this.status = status;
		this.siteId = siteId;
		this.recycle = recycle;
		this.channelIds = channelIds;
	}

	public ContentSearchDto() {
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public List<Integer> getStatus() {
		return status;
	}

	public void setStatus(List<Integer> status) {
		this.status = status;
	}

	public Boolean getMyself() {
		return myself;
	}

	public void setMyself(Boolean myself) {
		this.myself = myself;
	}

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public Date getCreateStartTime() {
		return createStartTime;
	}

	public void setCreateStartTime(Date createStartTime) {
		this.createStartTime = createStartTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public Date getReleaseStartTime() {
		return releaseStartTime;
	}

	public void setReleaseStartTime(Date releaseStartTime) {
		this.releaseStartTime = releaseStartTime;
	}

	public Date getReleaseEndTime() {
		return releaseEndTime;
	}

	public void setReleaseEndTime(Date releaseEndTime) {
		this.releaseEndTime = releaseEndTime;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Integer getKeyType() {
		return keyType;
	}

	public void setKeyType(Integer keyType) {
		this.keyType = keyType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer[] getChannelIds() {
		return channelIds;
	}

	public Integer[] getContentSecretIds() {
		return contentSecretIds;
	}

	public Boolean getIsTop() {
		return isTop;
	}

	public Boolean getRecycle() {
		return recycle;
	}

	public void setRecycle(Boolean recycle) {
		this.recycle = recycle;
	}

	public void setChannelIds(Integer[] channelIds) {
		this.channelIds = channelIds;
	}

	public void setContentSecretIds(Integer[] contentSecretIds) {
		this.contentSecretIds = contentSecretIds;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getFileStartTime() {
		return fileStartTime;
	}

	public void setFileStartTime(Date fileStartTime) {
		this.fileStartTime = fileStartTime;
	}

	public Date getFileEndTime() {
		return fileEndTime;
	}

	public void setFileEndTime(Date fileEndTime) {
		this.fileEndTime = fileEndTime;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public Integer getIssueOrg() {
		return issueOrg;
	}

	public void setIssueOrg(Integer issueOrg) {
		this.issueOrg = issueOrg;
	}

	public Integer getIssueYear() {
		return issueYear;
	}

	public void setIssueYear(Integer issueYear) {
		this.issueYear = issueYear;
	}

	public String getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(String issueNum) {
		this.issueNum = issueNum;
	}

	/**开启密级筛选**/
	public Boolean getSecret() {
		if (secret == null) {
			secret = false;
		}
		return secret;
	}

	public void setSecret(Boolean secret) {
		this.secret = secret;
	}
}
