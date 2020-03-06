/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.ContentConstant.ReleaseTimeStage;
import com.jeecms.content.constants.ContentConstant.SearchKeyCondition;
import com.jeecms.content.constants.ContentConstant.SearchPosition;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.vo.ContentChannelCountVo;


/**   
 * 内容索引服务
 * @author: tom
 * @date:   2019年5月11日 下午4:20:42     
 */
public interface ContentLuceneService {
        /**
         * 创建内容索引
         * 
         * @Title: createIndex
         * @param id 内容ID
         * @throws GlobalException GlobalException
         * @return: boolean
         */
        public boolean createIndex(Integer id) throws GlobalException;

        /**
         * 批量创建索引
         * 
         * @Title: createIndexBatch
         * @param contents 内容
         * @throws GlobalException GlobalException
         * @return: boolean
         */
        public boolean createIndexBatch(Iterable<Content> contents) throws GlobalException;


        /**
         * 更新索引
         * 
         * @Title: updateIndex
         * @param id 内容id
         * @throws GlobalException GlobalException
         * @return: boolean
         */
        public boolean updateIndexById(Integer id) throws GlobalException;

        /**
         * 重新生成索引
         * @Title: resetIndex
         * @param channelId 栏目ID
         * @param siteId 站点ID
         * @param releaseTimeStart 开始发布时间
         * @param releaseTimeEnd 结束发布时间
         * @throws GlobalException GlobalException
         * @return: boolean
         */
        public boolean resetIndex(Integer channelId, Integer siteId, Date releaseTimeStart,Date releaseTimeEnd)
                        throws GlobalException;

        /**
         * 删除索引
         * 
         * @Title: delete
         * @param id 内容ID
         * @throws GlobalException GlobalException
         * @return: boolean
         */
        public boolean delete(Integer id) throws GlobalException;
        
        /**
         * 批量删除索引
         * @Title: delete  
         * @param ids IDS
         * @throws GlobalException  GlobalException    
         * @return: boolean 成功返回true
         */
        public boolean delete(Iterable<Integer> ids) throws GlobalException;
        
        /**
         * 根据条件 批量删除索引
         * @Title: deleteIndexBatch  
         * @param channelIds 栏目id
         * @param siteId 站点ID
         * @throws GlobalException  GlobalException     
         * @return: boolean 成功返回true
         */
        public boolean deleteIndexBatch(List<Integer> channelIds, Integer siteId) throws GlobalException;

        /**
         * 查询索引
         * @Title: searchPage
         * @param keyword 关键词
         * @param searchPos 索引位置  标题、正文、标题或者正文
         * @param keyCondition 关键词位置
                      any  包含以下任意一个关键词
                      all  包含以下全部关键词
                      notInclude  不包含以下关键词
         * @param siteId 站点ID
         * @param channelIds 栏目ID
         * @param excludeChannelIds 不包含的栏目ID
         * @param typeIds 类型Id
         * @param tagIds tagId
         * @param timeStage 发布时间阶段  oneDay 一天内、oneWeek 一周内、oneMonth 一月内、oneYear 一年内、timeRage 指定时间范围段、
         * @param releaseTimeBegin 发布时间开始
         * @param releaseTimeEnd 发布时间结束
         * @param issueOrg 发文字号的机关代号
         * @param issueYear 发文字号的年份
         * @param issueNum 发文字号的顺序号
         * @param isTop 是否置顶
         * @param releasePc 是否支持PC通道
         * @param releaseWap 是否支持手机通道
         * @param releaseApp 是否支持app通道
         * @param releaseMiniprogram 是否支持小程序通道
         * @param oderBy 排序
         * @param pageable 分页
         * @throws GlobalException GlobalException
         * @return: Page
         */
        public Page<Content> searchPage(String keyword,SearchPosition searchPos,SearchKeyCondition keyCondition,
                        Integer siteId, List<Integer> channelIds,List<Integer> excludeChannelIds,
                        List<Integer> typeIds, List<Integer> tagIds, 
                        ReleaseTimeStage timeStage,Date releaseTimeBegin, Date releaseTimeEnd,
                        Integer issueOrg,Integer issueYear,String issueNum,Boolean isTop,
                        Boolean releasePc,Boolean releaseWap,Boolean releaseApp,Boolean releaseMiniprogram,
                        int oderBy, Pageable pageable) throws GlobalException;
        
        /**
         * 查询内容索引栏目汇总数据
         * @Title: searchPage
         * @param keyword 关键词
         * @param searchPos 索引位置  标题、正文、标题或者正文
         * @param keyCondition 关键词位置
                      any  包含以下任意一个关键词
                      all  包含以下全部关键词
                      notInclude  不包含以下关键词
         * @param siteId 站点ID
         * @param channelIds 栏目ID
         * @param excludeChannelIds 不包含的栏目ID
         * @param typeIds 类型Id
         * @param tagIds tagId
         * @param timeStage 发布时间阶段  oneDay 一天内、oneWeek 一周内、oneMonth 一月内、oneYear 一年内、timeRage 指定时间范围段、
         * @param releaseTimeBegin 发布时间开始
         * @param releaseTimeEnd 发布时间结束
         * @param issueOrg 发文字号的机关代号
         * @param issueYear 发文字号的年份
         * @param issueNum 发文字号的顺序号
         * @param isTop 是否置顶
         * @param releasePc 是否支持PC通道
         * @param releaseWap 是否支持手机通道
         * @param releaseApp 是否支持app通道
         * @param releaseMiniprogram 是否支持小程序通道
         * @throws GlobalException GlobalException
         * @return: Page
         */
        public List<ContentChannelCountVo> searchSummary(String keyword,SearchPosition searchPos,
                        SearchKeyCondition keyCondition, Integer siteId, List<Integer> channelIds,
                        List<Integer> excludeChannelIds,List<Integer> typeIds, List<Integer> tagIds, 
                        ReleaseTimeStage timeStage,Date releaseTimeBegin, Date releaseTimeEnd,
                        Integer issueOrg,Integer issueYear,String issueNum,Boolean isTop,
                        Boolean releasePc,Boolean releaseWap,Boolean releaseApp,Boolean releaseMiniprogram
                        ) throws GlobalException;
        
        
        /**
         * 查询索引条数
         * 
         * @param keyword
         *            搜索关键词
         * @param searchPosition 搜索位置 ${@link SearchPosition }}
         * @param channelId 栏目ID
         * @param siteId 站点ID
         * @param isTermQuery 是否精准查询  true是  false 模糊匹配
         * @param status TODO
         * @return: Page
         */
        public Long searchCount(String keyword, SearchPosition searchPosition, 
                        Integer channelId, Integer siteId, boolean isTermQuery, List<Integer> status);
}
