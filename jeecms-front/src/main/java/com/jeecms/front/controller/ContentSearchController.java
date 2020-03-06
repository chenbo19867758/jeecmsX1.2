package com.jeecms.front.controller;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.constants.ContentConstant.ReleaseTimeStage;
import com.jeecms.content.constants.ContentConstant.SearchKeyCondition;
import com.jeecms.content.constants.ContentConstant.SearchPosition;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.content.domain.vo.ContentChannelCountVo;
import com.jeecms.content.domain.vo.ContentFrontVo;
import com.jeecms.content.domain.vo.ContentLuceneSummaryVo;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.content.service.ContentLuceneService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.ContentSource;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.jeecms.common.constants.WebConstants.ARRAY_SPT;

/**
 * 内容检索
 *
 * @author: tom
 * @date: 2019年5月28日 下午3:37:05
 */
@RestController
@RequestMapping("/content")
public class ContentSearchController extends BaseController<Content, Integer> {
    private Logger logger = LoggerFactory.getLogger(ContentSearchController.class);

    /**
     * 内容检索
     *
     * @param keyword            关键词
     * @param searchPos          索引位置 title 标题、txt 正文、 titleAndTxt 标题或者正文
     * @param keyCondition       关键词条件 any 包含以下任意一个关键词 all 包含以下全部关键词 notInclude 不包含以下关键词
     * @param siteId             站点ID
     * @param channelIds         栏目ID
     * @param typeIds            类型Id
     * @param tagIds             tagId
     * @param timeStage          发布时间阶段 oneDay 一天内、oneWeek 一周内、oneMonth 一月内、oneYear
     *                           一年内、timeRage 指定时间范围段、
     * @param releaseTimeBegin   发布时间开始
     * @param releaseTimeEnd     发布时间结束
     * @param issueOrg           发文字号的机关代号
     * @param issueYear          发文字号的年份
     * @param issueNum           发文字号的顺序号
     * @param isTop              是否置顶
     * @param releaseApp         是否支持app通道
     * @param releaseMiniprogram 是否支持小程序通道
     * @param oderBy             排序 28 发布时间升序 27 发布时间降序 没有值则相关度排序
     * @param pageable           分页
     * @throws IOException     IOException
     * @throws GlobalException GlobalException
     * @Title: searchPage
     * @return: Page
     */
	@MoreSerializeField({
			@SerializeField(clazz = Content.class, includes = { "id", "title", "description", "channel", "site",
					"releaseTime", "url", "urlWhole", "views", "comments", "downs", "ups", "downloads",
					"source", "contentFrontVo","docResource" }),
			@SerializeField(clazz = ContentFrontVo.class, includes = { "modelId", "videoJson", "imageJson",
					"multiImageUploads" }),
			@SerializeField(clazz = ContentAttr.class, includes = { "resourcesSpaceData", "contentAttrRes" }),
			@SerializeField(clazz = ContentAttrRes.class, includes = { "resourcesSpaceData" }),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "resourceType", "dimensions", "url",
					"resourceDate", "suffix", "duration"}),
			@SerializeField(clazz = CmsSite.class, includes = { "id", "name" }),
			@SerializeField(clazz = Channel.class, includes = { "id", "name" }),
			@SerializeField(clazz = ContentSource.class, includes = { "sourceLink", "sourceName", "isOpenTarget" }), })
    @RequestMapping(value = "/search")
    public Page<Content> searchPage(String keyword, SearchPosition searchPos, SearchKeyCondition keyCondition,
                                    Integer siteId, String channelIds, String typeIds, String tagIds, ReleaseTimeStage timeStage,
                                    Date releaseTimeBegin, Date releaseTimeEnd, Integer issueOrg, Integer issueYear, String issueNum,
                                    Boolean isTop, Boolean releaseApp, Boolean releaseMiniprogram, Integer oderBy, Pageable pageable,
                                    HttpServletRequest request) throws IOException, GlobalException {

        if (oderBy == null) {
            oderBy = ContentConstant.ORDER_TYPE_RELATE;
        }
        if (searchPos == null) {
            searchPos = SearchPosition.titleAndTxt;
        }
        if (keyCondition == null) {
            keyCondition = SearchKeyCondition.any;
        }
        if (siteId == null) {
            siteId = SystemContextUtils.getSiteId(request);
        }
        Boolean releasePc = null;
        if (SystemContextUtils.isPc()) {
            releasePc = true;
        }
        Boolean releaseWap = null;
        if (SystemContextUtils.isMobile()) {
            releaseWap = true;
        }
        long begin = System.currentTimeMillis();
        List<Channel> channels = channelService.findList(siteId, true);
        List<Channel> excludeChannels = channels.stream()
                .filter(c -> Boolean.FALSE.equals(c.getIsOpenIndex())).collect(Collectors.toList());
        List<Integer> excludeChannelIds = Channel.fetchIds(excludeChannels);
        Page<Content> page = luceneService.searchPage(keyword, searchPos, keyCondition, siteId,
                Arrays.asList(getIntArray(channelIds)), excludeChannelIds, Arrays.asList(getIntArray(typeIds)),
                Arrays.asList(getIntArray(tagIds)), timeStage, releaseTimeBegin, releaseTimeEnd, issueOrg, issueYear,
                issueNum, isTop, releasePc, releaseWap, releaseApp, releaseMiniprogram, oderBy, pageable);
        for (Content content : page) {
			ContentFrontVo contentFrontVo = contentFrontService.initPartVo(content);
			content.setContentFrontVo(contentFrontVo);
        }
        long end = System.currentTimeMillis();
        logger.info("search times=" + (end - begin));
        return page;
    }

    @SerializeField(clazz = Channel.class, includes = {"id", "name"})
    @RequestMapping(value = "/searchSummary")
    public ContentLuceneSummaryVo searchSummary(String keyword, SearchPosition searchPos,
                                                SearchKeyCondition keyCondition, Integer siteId,
                                                @RequestParam(value = "channelIds") List<Integer> channelIds,
                                                @RequestParam(value = "typeIds") List<Integer> typeIds,
                                                @RequestParam(value = "tagIds") List<Integer> tagIds, ReleaseTimeStage timeStage, Date releaseTimeBegin,
                                                Date releaseTimeEnd, Integer issueOrg, Integer issueYear, String issueNum, Boolean isTop,
                                                Boolean releaseApp, Boolean releaseMiniprogram, Pageable pageable, HttpServletRequest request)
            throws IOException, GlobalException {

        if (searchPos == null) {
            searchPos = SearchPosition.titleAndTxt;
        }
        if (keyCondition == null) {
            keyCondition = SearchKeyCondition.any;
        }
        if (siteId == null) {
            siteId = SystemContextUtils.getSiteId(request);
        }
        Boolean releasePc = null;
        if (SystemContextUtils.isPc()) {
            releasePc = true;
        }
        Boolean releaseWap = null;
        if (SystemContextUtils.isMobile()) {
            releaseWap = true;
        }
        long begin = System.currentTimeMillis();
        List<Channel> channels = channelService.findList(siteId, true);
        List<Channel> excludeChannels = channels.stream()
                .filter(channel -> Boolean.FALSE.equals(channel.getIsOpenIndex()))
                .collect(Collectors.toList());
        List<Integer> excludeChannelIds = Channel.fetchIds(excludeChannels);
        List<ContentChannelCountVo> channelCounts = luceneService.searchSummary(keyword, searchPos, keyCondition,
                siteId, channelIds, excludeChannelIds, typeIds, tagIds, timeStage, releaseTimeBegin, releaseTimeEnd,
                issueOrg, issueYear, issueNum, isTop, releasePc, releaseWap, releaseApp, releaseMiniprogram);
        long end = System.currentTimeMillis();
        logger.info("search times=" + (end - begin));
        ContentLuceneSummaryVo vo = new ContentLuceneSummaryVo(channelCounts);
        return vo;
    }

    private Integer[] getIntArray(String str) {
        if (StringUtils.isBlank(str)) {
            return new Integer[0];
        }
        String[] arr = StringUtils.split(str, ARRAY_SPT);
        Integer[] ids = new Integer[arr.length];
        int i = 0;
        for (String s : arr) {
            ids[i++] = Integer.valueOf(s);
        }
        return ids;
    }

    @Autowired
    private ContentLuceneService luceneService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ContentFrontService contentFrontService;

}
