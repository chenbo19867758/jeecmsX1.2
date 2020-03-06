/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.ContentExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.ContentErrorCodeEnum;
import com.jeecms.common.local.ThreadPoolService;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.web.util.UrlHelper;
import com.jeecms.common.web.util.UrlHelper.PageInfo;
import com.jeecms.component.listener.AbstractContentListener;
import com.jeecms.component.listener.ContentListener;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentSearchDto;
import com.jeecms.content.domain.vo.PageProcessResult;
import com.jeecms.content.service.ContentGetService;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.ContentStaticPageService;
import com.jeecms.content.thread.DistributionFileThread;
import com.jeecms.content.thread.RemoteFileDeleteThread;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.dto.GlobalConfigDTO;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;
import com.jeecms.system.service.SysHotWordService;
import com.jeecms.util.FrontUtils;
import com.jeecms.util.SystemContextUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 内容静态化服务实现
 * 
 * @author: tom
 * @date: 2019年6月14日 下午2:04:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentStaticPageServiceImpl extends AbstractContentListener implements ContentStaticPageService {

        Logger log = LoggerFactory.getLogger(ContentStaticPageServiceImpl.class);

        /**
         * 批量内容静态化
         */
        @Override
        public int content(Integer siteId, List<Integer> channelIds, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                long time = System.currentTimeMillis();
                int totalContentCount = contentPage(false, siteId, channelIds, ignoreException, request, response);
                time = System.currentTimeMillis() - time;
                log.info("create content page count {}, in {} ms", totalContentCount, time);
                return totalContentCount;
        }

        /**
         * 单条内容静态化
         */
        public boolean content(Content content) throws GlobalException {
                HashMap<String, Object> data = new HashMap<String, Object>();
                GlobalConfigDTO config = globalConfigService.filterConfig(content.getSite(),globalConfigService.get(),null,null);
                createContentPage(content.getSite(), config, data, content, content.getTplContentOrDefForPc(),
                                content.getTplContentOrDefForMobile(), false);
                return true;
        }

        /**
         * 内容静态化生成
         * 
         * @Title: createContent
         * @param channel
         *                要静态化的栏目
         * @param contentCount
         *                要静态化的栏目下内容总数
         * @param containChild
         *                是否包含子栏目
         * @param ignoreException
         *                是否忽略异常
         * @param processResult
         *                进度结果对象
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int
         */
        private int createOrDelContent(Channel channel,Integer contentCount, boolean containChild, boolean deleteJob,
                        boolean ignoreException, ChannelPageCreateThreadServiceImpl channelPageCreateThread,PageProcessResult processResult,
                        PageProcessResult channelProcessResult, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                CmsSite site = channel.getSite();
                HashMap<String, Object> data = new HashMap<String, Object>();
                /** 站点不需要生产静态页，则不生成 */
                if (!deleteJob && !site.getOpenStatic()) {
                        return 0;
                }
                /** 若是空则需要查询内容数量 */
                if (contentCount == null) {
                        contentCount = contentGetService.getCountByChannel(channel, containChild);
                }
                /** 为空则是初始化进度对象，有值则是批量生成栏目的静态页，总页数由批量service方法设置 */
                if (processResult == null) {
                        processResult = new PageProcessResult(0, 0);
                        if (request != null && response != null) {
                                /** 当前需要生成或删除的总内容数 */
                                sessionProvider.setAttribute(request, ContentConstant.STATIC_CURR_CONTENT_COUNT_KEY,
                                                processResult);

                        }
                }
                if (channelProcessResult == null) {
                        channelProcessResult = new PageProcessResult(contentCount, 0);
                        HashMap<Integer, PageProcessResult> channelProcessResultMap = new HashMap<Integer, PageProcessResult>();
                        channelProcessResultMap.put(channel.getId(), channelProcessResult);
                        if (request != null && response != null) {
                                /** 当前栏目栏目静态化进度 */
                                sessionProvider.setAttribute(request, ContentConstant.STATIC_EACH_CHANNEL_COUNT_KEY,
                                                channelProcessResultMap);
                        }
                }
                GlobalConfigDTO config = SystemContextUtils.getResponseConfigDto(request);
                int loop = contentCount % oneThreadProcessPages == 0 ? contentCount / oneThreadProcessPages
                                : contentCount / oneThreadProcessPages + 1;
                Integer siteId = channel.getSiteId();
                Integer channelId = channel.getId();
                Integer[] cids = channel.getChildAllIdArray();
                CoreUser user = SystemContextUtils.getCoreUser();
                for (int i = 0; i < loop; i++) {
                        int first = i * oneThreadProcessPages;
                        Paginable paginable = new PaginableRequest(first + 1, oneThreadProcessPages);
                        List<Content> contents = contentGetService.getList(
                                        new ContentSearchDto(siteId, cids, ContentConstant.ORDER_TYPE_RELEASE_TIME_DESC,
                                                        Arrays.asList(ContentConstant.STATUS_PUBLISH), false),
                                        paginable);
                        processResult.setTotalPage(processResult.getTotalPage() + contents.size());
                        if (contents.size() > 0) {
                                channelPageCreateThread.initContentParams(channelId, config, i,
                                                Content.fetchIds(contents), data, deleteJob, ignoreException, user,
                                                processResult, channelProcessResult);
                                ThreadPoolService.getInstance().execute(channelPageCreateThread);
                        }
                }
                return contentCount;
        }

        @Override
        public boolean contentRelated(Content content, boolean ignoreException, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                /** 生成内容页 */
                content(content);
                CmsSite site = content.getSite();
                CoreUser user = SystemContextUtils.getCoreUser();
                /** 自动生成栏目页 */
                ChannelPageCreateThreadServiceImpl channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                for (Channel channel : content.getRelateChannelsAll()) {
                        if (channel!=null&&site.getConfig().getStaticAutoChannel()) {
                                Integer autoChannelPage = site.getConfig().getStaticAutoChannelPage();
                                createOrDelChannel(channel,channel.getStaticChannel(), autoChannelPage, false, false, ignoreException, user,channelPageCreateThread, null,
                                                null, request, response);
                        }
                }
                /** 自动生成首页页 */
                if (site.getConfig().getStaticAutoChannel()) {
                        index(site, ignoreException);
                }
                return true;
        }

        @Override
        public boolean deleteContentRelated(Content content, boolean ignoreException, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                deleteContent(content);
                CmsSite site = content.getSite();
                CoreUser user = SystemContextUtils.getCoreUser();
                /** 自动生成栏目页 */
                ChannelPageCreateThreadServiceImpl channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                for (Channel channel : content.getRelateChannelsAll()) {
                        if (site.getConfig().getStaticAutoChannel()) {
                                Integer autoChannelPage = site.getConfig().getStaticAutoChannelPage();
                                createOrDelChannel(channel,channel.getStaticChannel(), autoChannelPage, false, false, ignoreException, user, channelPageCreateThread,null,
                                                null, request, response);
                        }
                }
                /** 自动生成首页页 */
                if (site.getConfig().getStaticAutoChannel()) {
                        index(site, ignoreException);
                }
                return true;
        }

        @Override
        public boolean deleteContent(Content content) {
                int totalPage = content.getPageCount();
                CmsSite site = content.getSite();
                for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
                        String fileName = content.getStaticFilename(true, pageNo);
                        /** pc静态页 */
                        String fileRealPath = realPathResolver.get(fileName);
                        File pcFile = new File(fileRealPath);
                        if (pcFile.exists()) {
                                pcFile.delete();
                        }
                        /** 删除远程文件 */
                        if (!UploadServerType.local.equals(site.getStaticServerType())) {
                                site.deleteRemoteFile(ContentConstant.DISTRIBUTE_TYPE_HTML, fileName);
                        }
                        /** 手机静态页 */
                        String mobileFileName = content.getStaticFilename(false, pageNo);
                        fileRealPath = realPathResolver.get(mobileFileName);
                        File mobileFile = new File(fileRealPath);
                        if (mobileFile.exists()) {
                                mobileFile.delete();
                        }
                        /** 删除远程文件 */
                        if (!UploadServerType.local.equals(site.getStaticServerType())) {
                                site.deleteRemoteFile(ContentConstant.DISTRIBUTE_TYPE_HTML, mobileFileName);
                        }
                }
                return true;
        }

        /**
         * 批量删除内容静态页
         */
        @Override
        public int deleteContent(Integer siteId, List<Integer> channelIds, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                long time = System.currentTimeMillis();
                int totalContentCount = contentPage(true, siteId, channelIds, false, request, response);
                time = System.currentTimeMillis() - time;
                log.info("delete content page count {}, in {} ms", totalContentCount, time);
                return totalContentCount;
        }

        /**
         * 内容静态化生成和删除通用方法
         * 
         * @Title: contentPage
         * @param delete
         *                true 是删除 false是生成
         * @param siteId
         *                站点ID
         * @param channelIds
         *                栏目ID集合
         * @param ignoreException
         *                是否忽略异常
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int 总内容数
         */
        private int contentPage(boolean delete,Integer siteId, List<Integer> channelIds, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                int totalContentCount = 0;
                List<Channel> channels = channelService.findAllById(channelIds);
                /**
                 * 生成内容页只需要依据最底层栏目即可，上层栏目没有内容，如果不过滤上层栏目，会导致重复生成内容浪费服务器资源
                 */
                List<Channel> bottomChannels = new ArrayList<Channel>();
                for (Channel c : channels) {
                        if (c.getIsBottom()) {
                                bottomChannels.add(c);
                        }
                }
                Map<Integer, Integer> channelContentCountMap = new HashMap<Integer, Integer>();
                HashMap<Integer, PageProcessResult> channelProcessResultMap = new HashMap<Integer, PageProcessResult>();
                for (Channel c : bottomChannels) {
                        Integer contentCount = contentGetService.getCountByChannel(c, false);
                        channelContentCountMap.put(c.getId(), contentCount);
                        /**开启了静态化并且非外部链接栏目才计算总数*/
                        if (StringUtils.isBlank(c.getLink())&&c.getOpenStatic()) {
                                totalContentCount += contentCount;
                        }
                        final PageProcessResult channelProcessResult = new PageProcessResult(contentCount, 0);
                        channelProcessResultMap.put(c.getId(), channelProcessResult);
                }
                final PageProcessResult processResult = new PageProcessResult(0, 0);
                if (request != null && response != null) {
                        /** 静态化任务总进度 */
                        sessionProvider.setAttribute(request, ContentConstant.STATIC_CURR_CONTENT_COUNT_KEY,
                                        processResult);
                        /** 当前各个栏目静态化进度 */
                        sessionProvider.setAttribute(request, ContentConstant.STATIC_EACH_CHANNEL_COUNT_KEY,
                                        channelProcessResultMap);
                }
                ChannelPageCreateThreadServiceImpl  channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                for (Channel c : bottomChannels) {
                        createOrDelContent(c, channelContentCountMap.get(c.getId()), delete, delete, ignoreException,
                                channelPageCreateThread,processResult, channelProcessResultMap.get(c.getId()), request, response);
                }
                return totalContentCount;
        }

        /**
         * 生成一篇内容
         * 
         * @Title: createContentPage
         * @param site
         *                站点
         * @param config
         *                GlobalConfigDTO 配置对象
         * @param data
         *                前台模板所需 Map参数
         * @param c
         *                内容对象
         * @param tplPath
         *                pc模板
         * @param mobileTplPath
         *                手机模板
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        public void createContentPage(CmsSite site, GlobalConfigDTO config, Map<String, Object> data, Content c,
                        String tplPath, String mobileTplPath, boolean ignoreException) throws GlobalException {
                /** 如果是外部链接或者不生成静态页面，则不生成 */
                if (!StringUtils.isBlank(c.getOutLink()) || !c.getSite().getOpenStatic()) {
                        return;
                }
                int totalPage = c.getPageCount();
                for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
                        if (site.getOpenPcStatic() && StringUtils.isNotBlank(tplPath)) {
                                createContentPage(site, config, data, c, true, ignoreException, tplPath, pageNo);
                        }
                        if (site.getOpenMobileStatic() && StringUtils.isNotBlank(mobileTplPath)) {
                                createContentPage(site, config, data, c, false, ignoreException, mobileTplPath, pageNo);
                        }
                }
        }

        /**
         * 生成内容的一页
         * 
         * @Title: createContentPage
         * @param site
         *                站点
         * @param config
         *                GlobalConfigDTO 配置对象
         * @param data
         *                前台模板所需 Map参数
         * @param c
         *                内容对象
         * @param pc
         *                是否pc静态页
         * @param ignoreException
         *                是否关联自动触发 true是关联操作 false是手动触发
         * @param tplPath
         *                模板路径
         * @param pageNo
         *                内容分页
         * @throws GlobalException
         *                 GlobalException
         * @return: void
         */
        private void createContentPage(CmsSite site, GlobalConfigDTO config, Map<String, Object> data, Content c,
                        boolean pc, boolean ignoreException, String tplPath, Integer pageNo) throws GlobalException {
                if (pc) {
                        SystemContextUtils.setPc(true);
                        SystemContextUtils.setMobile(false);
                } else {
                        SystemContextUtils.setPc(false);
                        SystemContextUtils.setMobile(true);
                }
                String urlStatic = c.getUrlStatic(pageNo);
                PageInfo info;
                info = UrlHelper.getPageInfo(urlStatic.substring(urlStatic.lastIndexOf("/")), null);
                FrontUtils.putLocation(data, urlStatic);
                FrontUtils.frontPageData(pageNo, info.getHref(), info.getHrefFormer(), info.getHrefLatter(), data);
                Pageable pageable = PageRequest.of(pageNo, 1);
                Page<Integer> page = new PageImpl<Integer>(Arrays.asList(pageNo), pageable, c.getPageCount());
                FrontUtils.frontData(data, c.getSite(), null, null, config);
                data.put("pagination", page);
                data.put("content", c);
                data.put("channel", c.getChannel());
                String txt = c.getTxtByNo(pageNo);
                // 内容加上热词
                txt = hotWordService.attachKeyword(c.getChannelId(), txt, site);
                data.put("txt", txt);
                Writer out = null;
                Template tpl;
                String real;
                String filename = c.getStaticFilename(pc, pageNo);
                real = realPathResolver.get(filename.toString());
                File f = new File(real);
                File parent = f.getParentFile();
                if (!parent.exists()) {
                        parent.mkdirs();
                }
                boolean createSucc = true;
                try {
                        init();
                        tpl = conf.getTemplate(tplPath);
                } catch (IOException e) {
                        createSucc = false;
                        log.error(e.getMessage());
                        throw new GlobalException(new ContentExceptionInfo(
                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getDefaultMessage(),
                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                }
                try {
                        // FileWriter不能指定编码确实是个问题，只能用这个代替了。
                        out = new OutputStreamWriter(new FileOutputStream(f), WebConstants.UTF8);
                        tpl.process(data, out);
                        if (!UploadServerType.local.equals(site.getStaticServerType())) {
                                site.uploadFileToRemote(ContentConstant.DISTRIBUTE_TYPE_HTML, filename,
                                                new FileInputStream(f));
                        }
                        log.info("create static file: {}", f.getAbsolutePath());
                } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                        }
                        createSucc = false;
                } catch (FileNotFoundException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                        }
                        createSucc = false;
                } catch (TemplateException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_TPL_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_TPL_ERROR.getCode()));
                        }
                        createSucc = false;
                } catch (IOException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                        }
                        createSucc = false;
                } finally {
                        if (out != null) {
                                try {
                                        out.close();
                                } catch (IOException e) {
                                        log.error(e.getMessage());
                                        if (!ignoreException) {
                                                throw new GlobalException(new ContentExceptionInfo(
                                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                                .getDefaultMessage(),
                                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                                .getCode()));
                                        }
                                }
                        }
                }
                if(createSucc){
                        c.setHasStatic(true);
                        contentService.update(c);
                }
        }

        /**
         * 批量生成栏目静态页
         */
        @Override
        public int channel(Integer siteId, List<Integer> channelIds, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                long time = System.currentTimeMillis();
                int count = 0;
                List<Channel> channels = channelService.findAllById(channelIds);
                Map<Integer, Integer> channelPageMap = new HashMap<Integer, Integer>();
                Map<Integer, Boolean> preStaticMap = new HashMap<Integer, Boolean>();
                HashMap<Integer, PageProcessResult> channelProcessResultMap = new HashMap<Integer, PageProcessResult>();
                CoreUser user = SystemContextUtils.getCoreUser();
                for (Channel c : channels) {
                        Integer channelPage = getChannelPageCount(c, false);
                        channelPageMap.put(c.getId(), channelPage);
                        /**开启了静态化并且非外部链接栏目才计算总数*/
                        if (StringUtils.isBlank(c.getLink())&&c.getOpenStatic()) {
                                count += channelPage;
                        }
                        final PageProcessResult channelProcessResult = new PageProcessResult(channelPage, 0);
                        channelProcessResultMap.put(c.getId(), channelProcessResult);
                        preStaticMap.put(c.getId(),c.getHasStaticChannel());
                        /**预先调整栏目的是否静态化状态，如果不预先调整这个状态 模板取到的url 还是动态的url*/
                        c.setHasStaticChannel(true);
                        channelService.update(c);
                        channelService.flush();
                }
                final PageProcessResult processResult = new PageProcessResult(count, 0);
                if (request != null && response != null) {
                        /** 当前栏目静态化总进度 */
                        sessionProvider.setAttribute(request, ContentConstant.STATIC_CURR_CHANNEL_COUNT_KEY,
                                        processResult);
                        /** 当前各个栏目静态化进度 */
                        sessionProvider.setAttribute(request, ContentConstant.STATIC_EACH_CHANNEL_COUNT_KEY,
                                        channelProcessResultMap);
                }
                ChannelPageCreateThreadServiceImpl channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                for (Channel c : channels) {
                        createOrDelChannel(c, preStaticMap.get(c.getId()),channelPageMap.get(c.getId()), false, false, ignoreException, user,
                                channelPageCreateThread,processResult, channelProcessResultMap.get(c.getId()), request, response);
                }
                time = System.currentTimeMillis() - time;
                log.info("create {} channel page count {}, in {} ms", channels.size(), count, time);
                return count;
        }

        /**
         * 生成栏目静态页
         */
        @Override
        public int channel(Channel channel, boolean containChild, boolean ignoreException, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                CoreUser user = SystemContextUtils.getCoreUser();
                ChannelPageCreateThreadServiceImpl channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                return createOrDelChannel(channel, channel.getStaticChannel(),null, containChild, false, ignoreException, user, channelPageCreateThread,null, null,
                                request, response);
        }

        /**
         * 生成栏目静态页
         * 
         * @Title: createOrDelChannel
         * @param channel
         *                栏目
         * @param totalPage
         *                栏目静态页数量
         * @param containChild
         *                是否包含子栏目
         * @param ignoreException
         *                是否忽略异常
         * @param processResult
         *                进度对象
         * @param request
         *                HttpServletRequest
         * @param response
         *                HttpServletResponse
         * @throws GlobalException
         *                 GlobalException
         * @return: int
         */
        private int createOrDelChannel(Channel channel,Boolean preStaticChannel, Integer totalPage, boolean containChild, boolean deleteJob,
                        boolean ignoreException, CoreUser user,
                        ChannelPageCreateThreadServiceImpl channelPageCreateThread,   PageProcessResult processResult,
                        PageProcessResult channelProcessResult, HttpServletRequest request,
                        HttpServletResponse response) throws GlobalException {
                CmsSite site = channel.getSite();
                HashMap<String, Object> data = new HashMap<String, Object>();
                /** 如果是外部链接或者不需要生产静态页，则不生成 */
                if (!deleteJob) {
                        if (!StringUtils.isBlank(channel.getLink()) || !site.getOpenStatic()) {
                                return 0;
                        }
                }
                /** 若是空则需要查询栏目页面数量 */
                if (totalPage == null) {
                        totalPage = getChannelPageCount(channel, containChild);
                }
                /** 为空则是初始化进度对象，有值则是批量生成栏目的静态页，总页数由批量service方法设置 */
                if (processResult == null) {
                        processResult = new PageProcessResult(totalPage, 0);
                        if (request != null && response != null) {
                                /** 当前静态化任务进度 */
                                sessionProvider.setAttribute(request, ContentConstant.STATIC_CURR_CHANNEL_COUNT_KEY,
                                                processResult);

                        }
                }
                if (channelProcessResult == null) {
                        channelProcessResult = new PageProcessResult(totalPage, 0);
                        HashMap<Integer, PageProcessResult> channelProcessResultMap = new HashMap<Integer, PageProcessResult>();
                        channelProcessResultMap.put(channel.getId(), channelProcessResult);
                        if (request != null && response != null) {
                                /** 当前栏目栏目静态化进度 */
                                sessionProvider.setAttribute(request, ContentConstant.STATIC_EACH_CHANNEL_COUNT_KEY,
                                                channelProcessResultMap);
                        }
                }
                int beginPage = 1;
                int endPage = 1;
                int loop = totalPage % oneThreadProcessPages == 0 ? totalPage / oneThreadProcessPages
                                : totalPage / oneThreadProcessPages + 1;
                for (int i = 0; i < loop; i++) {
                        beginPage = i * oneThreadProcessPages + 1;
                        endPage = (i + 1) * oneThreadProcessPages;
                        if (endPage > totalPage) {
                                endPage = totalPage;
                        }
                        channelPageCreateThread.initChannelParams(channel.getId(), preStaticChannel,data, beginPage, endPage, deleteJob,
                                        ignoreException, user, processResult, channelProcessResult);
                        ThreadPoolService.getInstance().execute(channelPageCreateThread);
                }
                return totalPage;
        }

        /**
         * 获取栏目静态页进度
         */
        @Override
        public PageProcessResult getChannelPageProcessResult() {
                HttpServletRequest request = RequestUtils.getHttpServletRequest();
                Object obj = sessionProvider.getAttribute(request, ContentConstant.STATIC_CURR_CHANNEL_COUNT_KEY);
                if (obj != null) {
                        return (PageProcessResult) obj;
                }
                return new PageProcessResult(0, 0);
        }

        /**
         * 获取内容静态化进度
         */
        @Override
        public PageProcessResult getContentPageProcessResult() {
                HttpServletRequest request = RequestUtils.getHttpServletRequest();
                Object obj = sessionProvider.getAttribute(request, ContentConstant.STATIC_CURR_CONTENT_COUNT_KEY);
                if (obj != null) {
                        return (PageProcessResult) obj;
                }
                return new PageProcessResult(0, 0);
        }

        /**
         * 批量删除栏目页
         */
        @Override
        public int deleteChannel(Integer siteId, List<Integer> channelIds, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                long time = System.currentTimeMillis();
                int count = 0;
                List<Channel> channels = channelService.findAllById(channelIds);
                Map<Integer, Integer> channelPageMap = new HashMap<Integer, Integer>();
                Map<Integer, Boolean> preStaticMap = new HashMap<Integer, Boolean>();
                HashMap<Integer, PageProcessResult> channelProcessResultMap = new HashMap<Integer, PageProcessResult>();
                CoreUser user = SystemContextUtils.getCoreUser();
                for (Channel c : channels) {
                        Integer channelPage = getChannelPageCount(c, false);
                        channelPageMap.put(c.getId(), channelPage);
                        count += channelPage;
                        final PageProcessResult channelProcessResult = new PageProcessResult(channelPage, 0);
                        channelProcessResultMap.put(c.getId(), channelProcessResult);
                        preStaticMap.put(c.getId(),c.getHasStaticChannel());
                        /**预先调整栏目的是否静态化状态，如果不预先调整这个状态 模板取到的url 还是动态的url*/
                        c.setHasStaticChannel(false);
                        channelService.update(c);
                }
                final PageProcessResult processResult = new PageProcessResult(count, 0);
                if (request != null && response != null) {
                        /** 当前栏目静态化总进度 */
                        sessionProvider.setAttribute(request, ContentConstant.STATIC_CURR_CHANNEL_COUNT_KEY,
                                        processResult);
                        /** 当前各个栏目静态化进度 */
                        sessionProvider.setAttribute(request, ContentConstant.STATIC_EACH_CHANNEL_COUNT_KEY,
                                        channelProcessResultMap);
                }
                ChannelPageCreateThreadServiceImpl channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                for (Channel c : channels) {
                        createOrDelChannel(c, preStaticMap.get(c.getId()),channelPageMap.get(c.getId()), false, true, ignoreException, user,
                                channelPageCreateThread,  processResult, channelProcessResultMap.get(c.getId()), request, response);
                }
                time = System.currentTimeMillis() - time;
                log.info("delete {} channel page count {}, in {} ms", channels.size(), count, time);
                return count;
        }

        /**
         * 删除单个栏目的栏目静态页
         */
        @Override
        public int deleteChannel(Channel channel, boolean containChild, boolean ignoreException,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                CoreUser user = SystemContextUtils.getCoreUser();
                ChannelPageCreateThreadServiceImpl channelPageCreateThread = new ChannelPageCreateThreadServiceImpl();
                return createOrDelChannel(channel,channel.getStaticChannel(),null, containChild, true, ignoreException, user, channelPageCreateThread,
                        null, null, request,response);
        }

        /**
         * 首页静态化 relate false用户手动执行触发则抛出异常提示用户，否则是程序自动关联则不抛出异常信息
         */
        @Override
        public void index(CmsSite site, boolean ignoreException) throws GlobalException {
                Map<String, Object> data = new HashMap<String, Object>();
                GlobalConfig config = globalConfigService.get();
                GlobalConfigDTO configDto = globalConfigService.filterConfig(site, config, null, null);
                FrontUtils.frontData(data, site, null, null, configDto);
                try {
                        if (site.getOpenPcStatic()) {
                                String tpl = site.getTplIndexOrDefForPc();
                                index(site, tpl, data, true);
                        }
                        if (site.getOpenMobileStatic()) {
                                String mobileTpl = site.getTplIndexOrDefForMobile();
                                index(site, mobileTpl, data, false);
                        }
                } catch (IOException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                        }
                } catch (TemplateException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_TPL_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_TPL_ERROR.getCode()));
                        }
                }
                Map<String, String> attr = new HashMap<String, String>(1);
                attr.put(CmsSiteConfig.STATIC_HTML_HAS_CREATE, CmsSiteConfig.TRUE_STRING);
                siteService.updateAttr(site.getId(), attr);
        }

        /**
         * 生成首页静态页
         * 
         * @Title: index
         * @param site
         *                站点
         * @param tpl
         *                首页模板
         * @param data
         *                前台模板所需model参数
         * @param pc
         *                是否pc静态页
         * @throws IOException
         *                 IOException
         * @throws TemplateException
         *                 TemplateException
         * @return: void
         */
        public void index(CmsSite site, String tpl, Map<String, Object> data, boolean pc)
                        throws IOException, TemplateException {
                if (pc) {
                        SystemContextUtils.setPc(true);
                        SystemContextUtils.setMobile(false);
                } else {
                        SystemContextUtils.setPc(false);
                        SystemContextUtils.setMobile(true);
                }
                long time = System.currentTimeMillis();
                File f;
                String filePath = site.getStaticIndexForPcPagePath();
                if (!pc) {
                        filePath = site.getStaticIndexForMobilePagePath();
                }
                String fileRealPath = realPathResolver.get(filePath);
                f = new File(fileRealPath);
                File parent = f.getParentFile();
                if (!parent.exists()) {
                        parent.mkdirs();
                }
                Writer out = null;
                init();
                try {
                        // FileWriter不能指定编码确实是个问题，只能用这个代替了。
                        out = new OutputStreamWriter(new FileOutputStream(f), WebConstants.UTF8);
                        Template template = conf.getTemplate(tpl);
                        template.process(data, out);
                } finally {
                        if (out != null) {
                                out.flush();
                                out.close();
                        }
                }
                if (!UploadServerType.local.equals(site.getStaticServerType())) {
                        ThreadPoolService.getInstance().execute(new DistributionFileThread(site,
                                        ContentConstant.DISTRIBUTE_TYPE_HTML, filePath, new FileInputStream(f)));
                }
                time = System.currentTimeMillis() - time;
                log.info("create index page, in {} ms", time);
        }

        /**
         * 删除首页
         */
        @Override
        public boolean deleteIndex(CmsSite site) throws  GlobalException{
                File pcIndexFile;
                /** 删除pc和手机端首页 */
                pcIndexFile = new File(realPathResolver.get(site.getStaticIndexForPcPagePath()));
                if (pcIndexFile.exists()) {
                        pcIndexFile.delete();
                }
                /** 删除远程文件 */
                if (!UploadServerType.local.equals(site.getStaticServerType())) {
                        ThreadPoolService.getInstance().execute(new RemoteFileDeleteThread(site,
                                        ContentConstant.DISTRIBUTE_TYPE_HTML, site.getStaticIndexForPcPagePath()));
                }
                File mobileIndexFile = new File(realPathResolver.get(site.getStaticIndexForMobilePagePath()));
                if (mobileIndexFile.exists()) {
                        mobileIndexFile.delete();
                }
                /** 删除远程文件 */
                if (!UploadServerType.local.equals(site.getStaticServerType())) {
                        ThreadPoolService.getInstance().execute(new RemoteFileDeleteThread(site,
                                        ContentConstant.DISTRIBUTE_TYPE_HTML, site.getStaticIndexForMobilePagePath()));
                }
                Map<String, String> attr = new HashMap<String, String>(1);
                attr.put(CmsSiteConfig.STATIC_HTML_HAS_CREATE, CmsSiteConfig.FALSE_STRING);
                siteService.updateAttr(site.getId(), attr);
                return true;
        }

        public void createChannelPage(CmsSite site, Map<String, Object> data, Channel c, String filename,
                        boolean relate, boolean pc, Integer page, String tplPath) throws GlobalException {
                if (pc) {
                        SystemContextUtils.setPc(true);
                        SystemContextUtils.setMobile(false);
                } else {
                        SystemContextUtils.setPc(false);
                        SystemContextUtils.setMobile(true);
                }
                Writer out = null;
                Template tpl = null;
                String real;
                real = realPathResolver.get(filename.toString());
                File f = new File(real);
                File parent = f.getParentFile();
                if (!parent.exists()) {
                        parent.mkdirs();
                }
                init();
                try {
                        tpl = conf.getTemplate(tplPath);
                } catch (IOException e) {
                        log.error(e.getMessage());
                        if (!relate) {
                                throw new GlobalException(new ContentExceptionInfo(
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getDefaultMessage(),
                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                        }
                }
                String urlStatic = c.getUrlStatic(page);
                PageInfo info;
                info = UrlHelper.getPageInfo(filename.substring(filename.lastIndexOf("/")), null);
                FrontUtils.frontPageData(page, info.getHref(), info.getHrefFormer(), info.getHrefLatter(), data);
                FrontUtils.putLocation(data, urlStatic);
                GlobalConfig config = globalConfigService.get();
                GlobalConfigDTO configDto = globalConfigService.filterConfig(c.getSite(), config, null, null);
                FrontUtils.frontData(data, c.getSite(), null, null, configDto);
                data.put("channel", c);
                if (tpl != null) {
                        try {
                                // FileWriter不能指定编码确实是个问题，只能用这个代替了。
                                out = new OutputStreamWriter(new FileOutputStream(f), WebConstants.UTF8);
                                tpl.process(data, out);
                                if (!UploadServerType.local.equals(site.getStaticServerType())) {
                                        site.uploadFileToRemote(ContentConstant.DISTRIBUTE_TYPE_HTML, filename,
                                                        new FileInputStream(f));
                                }
                                log.info("create static file: {}", f.getAbsolutePath());
                        } catch (UnsupportedEncodingException e) {
                                log.error(e.getMessage());
                                if (!relate) {
                                        throw new GlobalException(new ContentExceptionInfo(
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                        .getDefaultMessage(),
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                                }
                        } catch (FileNotFoundException e) {
                                log.error(e.getMessage());
                                if (!relate) {
                                        throw new GlobalException(new ContentExceptionInfo(
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                        .getDefaultMessage(),
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                                }
                        } catch (TemplateException e) {
                                log.error(e.getMessage());
                                if (!relate) {
                                        throw new GlobalException(new ContentExceptionInfo(
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_TPL_ERROR
                                                                        .getDefaultMessage(),
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_TPL_ERROR.getCode()));
                                }
                        } catch (IOException e) {
                                log.error(e.getMessage());
                                if (!relate) {
                                        throw new GlobalException(new ContentExceptionInfo(
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                        .getDefaultMessage(),
                                                        ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR.getCode()));
                                }
                        } finally {
                                if (out != null) {
                                        try {
                                                out.close();
                                        } catch (IOException e) {
                                                log.error(e.getMessage());
                                                throw new GlobalException(new ContentExceptionInfo(
                                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                                .getDefaultMessage(),
                                                                ContentErrorCodeEnum.CREATE_STATIC_HTML_IO_ERROR
                                                                                .getCode()));
                                        }
                                }
                        }
                }

        }

        /**
         * 获取栏目应该生成静态页的页数
         * 
         * @Title: getChannelPageCount
         * @param channel
         *                栏目对象
         * @param containChild
         *                是否包含子栏目
         * @return: int
         */
        private int getChannelPageCount(Channel channel, boolean containChild) {
                /** 列表栏目又没有富文本默认分页数1 */
                int totalPage = 1;
                /** 存在富文本,获取正文的页数(多个正文则取页数最多的正文分页数) */
                if (channel.getTxtCounts() > 0) {
                        return channel.getTxtPageCount();
                } else if (channel.getIsListChannel()) {
                        long countCount = contentGetService.getCount(new ContentSearchDto(channel.getSiteId(),
                                        channel.getChildAllIdArray(), ContentConstant.ORDER_TYPE_RELEASE_TIME_DESC,
                                        Arrays.asList(ContentConstant.STATUS_PUBLISH), false));
                        if (countCount > 0) {
                                totalPage = (int) (countCount - 1) / channel.getPageSize() + 1;
                        }
                }
                return totalPage;
        }

        @Override
        public void afterSave(Content content) throws GlobalException {
                super.afterSave(content);
                ThreadPoolService.getInstance().execute(new SaveContentPageThread(content));
                /** 发布若是生成错误则继续，不往外抛出异常 */
                /**
                if (content.isPublish()) {
                        try {
                                contentRelated(content, true, null, null);
                        } catch (Exception e) {
                                log.error(e.getMessage());
                        }
                }
                 */
        }

        class SaveContentPageThread implements  Runnable{
                private Content content;

                public SaveContentPageThread(Content content) {
                        this.content = content;
                }

                @Override
                public void run() {
                        /** 发布若是生成错误则继续，不往外抛出异常 */
                        try {
                                Thread.sleep(1000);
                        }catch (Exception e){
                                log.error(e.getMessage());
                        }
                        if (content.isPublish()) {
                                try {
                                        contentRelated(content, true, null, null);
                                } catch (Exception e) {
                                        log.error(e.getMessage());
                                }
                        }
                }
        }

        @Override
        public void afterChange(Content content, Map<String, Object> map) throws GlobalException {
                super.afterChange(content, map);
                boolean pre = (Boolean) map.get(ContentListener.CONTENT_PUBLISH);
                boolean curr = content.isPublish();
                Content c = contentService.findById(content.getId());
                /** 发布若是生成错误则继续，不往外抛出异常 */
                try {
                        if (pre && !curr) {
                                contentRelated(c, true, null, null);
                        } else if (!pre && curr) {
                                contentRelated(c, true, null, null);
                        } else if (pre && curr) {
                                contentRelated(c, true, null, null);
                        }
                } catch (Exception e) {
                        log.error(e.getMessage());
                }
        }

        @Override
        public void afterContentRecycle(List<Integer> contentIds) throws GlobalException {
                super.afterContentRecycle(contentIds);
                /** 发布若是生成错误则继续，不往外抛出异常 */
                List<Content>contents = contentService.findAllById(contentIds);
                try {
                        for (Content content : contents) {
                                deleteContentRelated(content, true, null, null);
                        }
                } catch (Exception e) {
                        log.error(e.getMessage());
                }
        }

        @Override
        public void afterDelete(List<Content> contents) throws GlobalException {
                super.afterDelete(contents);
        }

        private Configuration conf;

        private void init() {
                if (conf == null) {
                        conf = ApplicationContextProvider.getBean(Configuration.class);
                }
        }

        @Autowired
        private ContentGetService contentGetService;
        @Autowired
        private RealPathResolver realPathResolver;
        @Autowired
        private GlobalConfigService globalConfigService;
        @Autowired
        ChannelService channelService;
        @Autowired
        private SessionProvider sessionProvider;
        @Autowired
        private SysHotWordService hotWordService;
        @Value("${oneThreadProcessPages}")
        private int oneThreadProcessPages = 20;
        @Autowired
        private ContentService contentService;
        @Autowired
        private CmsSiteService siteService;

}
