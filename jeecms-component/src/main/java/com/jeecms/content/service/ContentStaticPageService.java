/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.vo.PageProcessResult;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.GlobalConfigDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 内容静态页服务
 * 
 * @author: tom
 * @date: 2019年6月10日 下午3:58:27
 */
public interface ContentStaticPageService {
        /**
         * 批量生成内容静态页
         * 
         * @Title: content
         * @param siteId
         *                站点ID
         * @param channelIds
         *                栏目ID集合
         * @param ignoreException 是否忽略异常
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int 生成内容页数
         */
        int content(Integer siteId, List<Integer> channelIds, boolean ignoreException, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException;


        /**
         * 生成关联静态化页面
         * 
         * @Title: contentRelated
         * @param content
         *                内容
         * @param ignoreException 是否忽略异常
         * @param request HttpServletRequest
         * @param response HttpServletResponse       
         * @throws GlobalException GlobalException     
         * @return: boolean
         */
        boolean contentRelated(Content content, boolean ignoreException, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException;


        /**
         * 删除内容静态页以及关联重新生成栏目页和首页
         * 
         * @Title: deleteContentRelated
         * @param content
         *                内容
         * @param ignoreException 是否忽略异常
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @return: boolean
         */
        boolean deleteContentRelated(Content content, boolean ignoreException, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException;

        boolean deleteContent(Content content);

        /**
         * 批量删除内容静态页
         * 
         * @Title: content
         * @param siteId
         *                站点ID
         * @param channelIds
         *                栏目ID集合
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int 生成内容页数
         */
        int deleteContent(Integer siteId, List<Integer> channelIds, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException;

        /**
         * 批量生成栏目静态页
         * 
         * @Title: channel
         * @param siteId
         *                站点Id
         * @param channelIds
         *                栏目ID集合
         * @param ignoreException TODO
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int
         */
        int channel(Integer siteId, List<Integer> channelIds, boolean ignoreException, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException;

        /**
         * 生成栏目静态页
         * 
         * @Title: channel
         * @param channel
         *                栏目对象
         * @param containChild
         *                是否包含子栏目
         * @param ignoreException TODO
         * @throws GlobalException
         *                 GlobalException
         * @return: int
         */
        int channel(Channel channel, boolean containChild, boolean ignoreException, HttpServletRequest request, HttpServletResponse response)
                        throws GlobalException;

        /**
         * 获取栏目静态化进度
         * 
         * @Title: getChannelPageProcessResult
         * @return: PageProcessResult
         */
        PageProcessResult getChannelPageProcessResult();

        /**
         * 获取内容静态化进度
         * 
         * @Title: getContentPageProcessResult
         * @return: PageProcessResult
         */
        PageProcessResult getContentPageProcessResult();

        /**
         * 批量删除栏目静态页
         * 
         * @Title: channel
         * @param siteId
         *                站点Id
         * @param channelIds
         *                栏目ID集合
         * @param ignoreException TODO
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int
         */
        int deleteChannel(Integer siteId, List<Integer> channelIds, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException;

        /**
         * 删除栏目静态页
         * 
         * @Title: deleteChannel
         * @param channel
         *                栏目
         * @param containChild
         *                是否包含子栏目
         * @param ignoreException TODO
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int
         */
        int deleteChannel(Channel channel, boolean containChild, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException;

        /**
         * 生成首页
         * 
         * @Title: index
         * @param site
         *                站点
         * @param ignoreException 是否忽略异常
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        void index(CmsSite site, boolean ignoreException) throws GlobalException;

        /**
         * 删除首页
         * 
         * @Title: deleteIndex
         * @param site
         *                站点
         * @return: boolean
         */
        boolean deleteIndex(CmsSite site) throws  GlobalException ;

        /**
         * 生成某篇内容 的静态页
         * 
         * @Title: createContentPage
         * @param site
         *                站点
         * @param config
         *                GlobalConfigDTO配置
         * @param data
         *                前台模板所需modelmap数据
         * @param c
         *                内容
         * @param tplPath
         *                pc模板
         * @param mobileTplPath
         *                手机模板
         * @param ignoreException TODO
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        void createContentPage(CmsSite site, GlobalConfigDTO config, Map<String, Object> data, Content c,
                        String tplPath, String mobileTplPath, boolean ignoreException) throws GlobalException;

        /**
         * 生成栏目静态页第page页
         * 
         * @Title: createChannelPage
         * @param site
         *                站点
         * @param data
         *                前台模板所需modelmap数据
         * @param c
         *                栏目
         * @param filename
         *                生成静态文件名
         * @param relate TODO
         * @param page
         *                第page页
         * @param tplPath
         *                模板路径
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        void createChannelPage(CmsSite site, Map<String, Object> data, Channel c, String filename, boolean relate,
                        boolean pc,Integer page, String tplPath) throws GlobalException;
}
