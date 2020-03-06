/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.dto;

import java.util.Date;
import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.LuceneExceptionInfo;
import com.jeecms.content.domain.Content;

/**
 * 内容索引对象
 * 
 * @author: tom
 * @date: 2019年5月25日 下午1:48:10
 */
public class ContentLucene {

        public static final int DEFAULT_PAGE_SIZE = 20;

        /**
         * 一次线程处理内容数量
         */
        public static final Integer THREAD_PROCESS_NUM = 1000;
        /**
         * 索引汇总栏目数量最大
         */
        public static final Integer CHANNEL_MAX = 1000;
        public static final int ONE = 1;
        public static final int ZERO = 0;

        public enum OpType {
                /** 创建 */
                CREATE,
                /** 更新整个 */
                UPDATE,
                /** 局部更新 */
                LOCAL_UPDATE,
                /** 删除 */
                DELETE;
        }

        /**
         * 创建索引对象
         * 
         * @Title: createLuceneDoc
         * @param content
         *                内容对象
         * @throws GlobalException
         *                 GlobalException
         * @return: ProductLuceneDTO
         */
        public static ContentLucene createLuceneDoc(Content content) throws GlobalException {
                ContentLucene doc = new ContentLucene();
                if (content != null) {
                        doc.setId(content.getId());
                        doc.setDescription(content.getContentExt().getDescription());
                        doc.setIssueNum(content.getContentExt().getIssueNum());
                        doc.setIssueOrg(content.getContentExt().getIssueOrg());
                        doc.setIssueYear(content.getContentExt().getIssueYear());
                        if (content.getReleaseApp()) {
                                doc.setReleaseApp(ONE);
                        } else {
                                doc.setReleaseApp(ZERO);
                        }
                        if (content.getReleaseMiniprogram()) {
                                doc.setReleaseMiniprogram(ONE);
                        } else {
                                doc.setReleaseMiniprogram(ZERO);
                        }
                        if (content.getReleasePc()) {
                                doc.setReleasePc(ONE);
                        } else {
                                doc.setReleasePc(ZERO);
                        }
                        if (content.getReleaseWap()) {
                                doc.setReleaseWap(ONE);
                        } else {
                                doc.setReleaseWap(ZERO);
                        }
                        doc.setReleaseTime(content.getReleaseTime());
                        doc.setSiteId(content.getSiteId());
                        doc.setChannelId(content.getChannelId());
                        doc.setTitle(content.getTitle());
                        doc.setTitleStr(content.getTitle());
                        doc.setStatus(content.getStatus());
                        if (content.getTop()) {
                                doc.setTop(ONE);
                        } else {
                                doc.setTop(ZERO);
                        }
                        doc.setTypeIds(content.getTypeIds());
                        doc.setTxt(getAllTxt(content.getTxts()));
                        doc.setTagIds(content.getTagIds());
                        doc.setChannelIds(content.getChannel().getNodeIdList());
                } else {
                        throw new GlobalException(new LuceneExceptionInfo());
                }
                return doc;
        }

        private static String getAllTxt(List<String> txts) {
                StringBuffer buff = new StringBuffer();
                if (txts != null && !txts.isEmpty()) {
                        for (String t : txts) {
                                buff.append(t).append(",");
                        }
                }
                return buff.toString();
        }

        /**
         * 内容ID
         */
        private Integer id;
        /**
         * 站点ID
         */
        private Integer siteId;
        /**
         * 所属栏目ID
         */
        private Integer channelId;
        /**
         * 所属栏目以及上级栏目id
         */
        private List<Integer> channelIds;

        /**
         * 内容拥有的关键词ID
         */
        private List<Integer> tagIds;
        /**
         * 内容类型ID
         */
        private List<Integer> typeIds;
        /**
         * 内容标题
         */
        private String title;
        /**
         * 内容标题(不拆词用于精准查询)
         */
        private String titleStr;
        /**
         * 发布时间
         */
        private Date releaseTime;
        /**
         * 正文富文本
         */
        private String txt;
        /**
         * 内容描述
         */
        private String description;

        /**
         * 是否置顶
         */
        private Integer top;
        /**
         * 发文字号-机关代字
         */
        private Integer issueOrg;
        /**
         * 发文字号-年份
         */
        private Integer issueYear;
        /**
         * 发文字号-顺序号
         */
        private String issueNum;
        /**
         * 是否发布至pc（0-否 1-是）
         */
        private Integer releasePc;
        /**
         * 是否发布至wap（0-否 1-是）
         */
        private Integer releaseWap;
        /**
         * 是否发布至app（0-否 1-是）
         */
        private Integer releaseApp;
        /**
         * 是否发布至小程序（0-否 1-是）
         */
        private Integer releaseMiniprogram;
        
        /**
         * 内容状态
         */
        private Integer status;

        public Integer getId() {
                return id;
        }

        public Integer getSiteId() {
                return siteId;
        }

        public Integer getChannelId() {
                return channelId;
        }
        
        public void setChannelId(Integer channelId) {
                this.channelId = channelId;
        }

        public List<Integer> getChannelIds() {
                return channelIds;
        }

        public List<Integer> getTagIds() {
                return tagIds;
        }

        public List<Integer> getTypeIds() {
                return typeIds;
        }

        public String getTitle() {
                return title;
        }

        public Date getReleaseTime() {
                return releaseTime;
        }

        public String getTxt() {
                return txt;
        }

        public String getDescription() {
                return description;
        }
        
        /**
         * @return the status
         */
        public Integer getStatus() {
                return status;
        }

        /**
         * @param status the status to set
         */
        public void setStatus(Integer status) {
                this.status = status;
        }

        public Integer getTop() {
                return top;
        }

        public Integer getIssueOrg() {
                return issueOrg;
        }

        public Integer getIssueYear() {
                return issueYear;
        }

        public String getIssueNum() {
                return issueNum;
        }

        public Integer getReleasePc() {
                return releasePc;
        }

        public Integer getReleaseWap() {
                return releaseWap;
        }

        public Integer getReleaseApp() {
                return releaseApp;
        }

        public Integer getReleaseMiniprogram() {
                return releaseMiniprogram;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        public void setChannelIds(List<Integer> channelIds) {
                this.channelIds = channelIds;
        }

        public void setTagIds(List<Integer> tagIds) {
                this.tagIds = tagIds;
        }

        public void setTypeIds(List<Integer> typeIds) {
                this.typeIds = typeIds;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public void setReleaseTime(Date releaseTime) {
                this.releaseTime = releaseTime;
        }

        public void setTxt(String txt) {
                this.txt = txt;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public void setTop(Integer top) {
                this.top = top;
        }

        public void setIssueOrg(Integer issueOrg) {
                this.issueOrg = issueOrg;
        }

        public void setIssueYear(Integer issueYear) {
                this.issueYear = issueYear;
        }

        public void setIssueNum(String issueNum) {
                this.issueNum = issueNum;
        }

        public void setReleasePc(Integer releasePc) {
                this.releasePc = releasePc;
        }

        public void setReleaseWap(Integer releaseWap) {
                this.releaseWap = releaseWap;
        }

        public void setReleaseApp(Integer releaseApp) {
                this.releaseApp = releaseApp;
        }

        public void setReleaseMiniprogram(Integer releaseMiniprogram) {
                this.releaseMiniprogram = releaseMiniprogram;
        }

        /**
         * @return the titleStr
         */
        public String getTitleStr() {
                return titleStr;
        }

        /**
         * @param titleStr the titleStr to set
         */
        public void setTitleStr(String titleStr) {
                this.titleStr = titleStr;
        }

        
}
