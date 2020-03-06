package com.jeecms.content.service.impl;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.UploadEnum.UploadServerType;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.ApplicationContextProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.vo.PageProcessResult;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.ContentStaticPageService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.dto.GlobalConfigDTO;
import com.jeecms.system.service.CmsOrgService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 栏目生成线程
 * 
 * @author: tom
 * @date: 2019年6月14日 下午2:41:55
 */
//@Service
//@Transactional(rollbackFor = Exception.class)
public class ChannelPageCreateThreadServiceImpl extends Thread implements Serializable {
        private static final long serialVersionUID = -5999176924977413351L;

        Queue<ChannelPageParam> queue = new LinkedList<ChannelPageParam>();
        /** 栏目静态化对象 */
        Map<String, PageProcessResult> channelProcessResult = new ConcurrentHashMap<String, PageProcessResult>();
        /** 静态化总任务进度对象 */
        PageProcessResult pageProcessResult;

        public Map<String, PageProcessResult> getChannelProcessResult() {
                return channelProcessResult;
        }

        public PageProcessResult getPageProcessResult() {
                return pageProcessResult;
        }

        public void setChannelProcessResult(Map<String, PageProcessResult> channelProcessResult) {
                this.channelProcessResult = channelProcessResult;
        }

        public void setPageProcessResult(PageProcessResult pageProcessResult) {
                this.pageProcessResult = pageProcessResult;
        }

        public Queue<ChannelPageParam> getQueue() {
                return queue;
        }

        public void setQueue(Queue<ChannelPageParam> queue) {
                this.queue = queue;
        }

        public ChannelPageCreateThreadServiceImpl() {
                super();
                init();
        }

        /**
         * 构造器
         * 
         * @param channelId
         *                栏目ID
         * @param data
         *                模板所需map参数
         * @param beginPage
         *                线程处理静态页开始页码
         * @param endPage
         *                线程处理静态页结束页码
         * @param ignoreException
         *                是否忽略模板异常
         * @param processResult
         *                总进度对象
         * @param channelProcessResult
         *                栏目静态进度对象
         */
        public void initChannelParams(Integer channelId,Boolean preHasStaticChannel,HashMap<String, Object> data, int beginPage, int endPage,
                        boolean deleteJob, boolean ignoreException, CoreUser user, PageProcessResult processResult,
                        PageProcessResult channelProcessResult) {
                ChannelPageParam p = new ChannelPageParam(channelId, preHasStaticChannel,data, beginPage, endPage, deleteJob,
                                ignoreException, user, processResult, channelProcessResult);
                getQueue().add(p);
                setPageProcessResult(processResult);
                getChannelProcessResult().put(channelId.toString(), channelProcessResult);
        }

        public void initContentParams(Integer channelId,GlobalConfigDTO config, Integer loop, List<Integer> contentIds,
                        HashMap<String, Object> data, boolean deleteJob, boolean ignoreException, CoreUser user,
                        PageProcessResult processResult, PageProcessResult channelProcessResult) {
                ChannelPageParam p = new ChannelPageParam(channelId, config, contentIds, data, deleteJob,
                                ignoreException, user, processResult, channelProcessResult);
                getQueue().add(p);
                setPageProcessResult(processResult);
                getChannelProcessResult().put(channelId.toString(), channelProcessResult);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void run() {
                /**等内容页先生成*/
                /** 一个线程处理一个key */
                ChannelPageParam param = queue.poll();
                if (param != null) {
                        Integer channelId = param.getChannelId();
                        CoreUser user = param.getUser();
                        if (channelId != null) {
                                Channel channel = channelService.findById(channelId);
                                HashMap<String, Object> data = param.getData();
                                PageProcessResult processResult = getPageProcessResult();
                                PageProcessResult channelProcessResult = getChannelProcessResult()
                                                .get(channelId.toString());
                                if (channelProcessResult != null) {
                                        Integer beginPage = param.getBeginPage();
                                        Integer endPage = param.getEndPage();
                                        List<Integer> contentIds = param.getContentIds();
                                        try {
                                                if (contentIds != null) {
                                                        GlobalConfigDTO config = param.getConfig();
                                                        /** 内容静态化 */
                                                        if (!param.isDeleteJob()) {
                                                                doRunContentCreate(channel, config, data, contentIds,
                                                                                param.isIgnoreException(), user,
                                                                                processResult, channelProcessResult);
                                                        } else {
                                                                doRunContentDelete(channel, contentIds,
                                                                                param.isIgnoreException(), user,
                                                                                processResult, channelProcessResult,
                                                                                null, null);
                                                        }
                                                } else {
                                                        /** 栏目静态化 */
                                                        if (!param.isDeleteJob()) {
                                                                doRunChannelCreate(channel,param.getPreHasStaticChannel(), data, beginPage, endPage,
                                                                                param.isIgnoreException(), user,
                                                                                processResult, channelProcessResult);
                                                        } else {
                                                                doRunChannelDelete(channel, beginPage, endPage,
                                                                                param.isIgnoreException(), user,
                                                                                processResult, channelProcessResult);
                                                        }
                                                }
                                        } catch (Exception e) {
                                                log.error(e.getMessage());
                                        }

                                }
                        }
                        queue.remove(param);
                }
        }

        void doRunChannelCreate(Channel channel,Boolean preStaticChannel, HashMap<String, Object> data, int beginPage, int endPage,
                        boolean ignoreException, CoreUser user, PageProcessResult processResult,
                        PageProcessResult channelProcessResult) throws GlobalException {
                CmsSite site = channel.getSite();
                String tpl = channel.getTplChannelOrDefForPc();
                String mobileTpl = channel.getTplChannelOrDefForMobile();
                boolean staticError = false;
                for (int page = beginPage; page <= endPage; page++) {
                        if (site.getOpenPcStatic() && StringUtils.isNotBlank(tpl)) {
                                try {
                                        staticPageService.createChannelPage(site, data, channel,
                                                        channel.getStaticFilename(true, page), false, true, page, tpl);
                                } catch (Exception e) {
                                        staticError = true;
                                        this.log.error(e.getMessage());
                                }

                        }
                        if (site.getOpenMobileStatic() && StringUtils.isNotBlank(mobileTpl)) {
                                try {
                                        staticPageService.createChannelPage(site, data, channel,
                                                        channel.getStaticFilename(false, page), false, false, page,
                                                        mobileTpl);
                                } catch (Exception e) {
                                        staticError = true;
                                        this.log.error(e.getMessage());
                                }
                        }
                        processResult.setCurrPage(processResult.getCurrPage() + 1);
                        channelProcessResult.setCurrPage(channelProcessResult.getCurrPage() + 1);
                }
                /** 当前栏目已完成静态化 */
                afterChannelComplete(channel, false, true, user, ignoreException, channelProcessResult);
                if(staticError){
                        channel.setHasStaticChannel(preStaticChannel);
                        channelService.update(channel);
                }
        }

        void doRunChannelDelete(Channel channel, int beginPage, int endPage, boolean ignoreException, CoreUser user,
                        PageProcessResult processResult, PageProcessResult channelProcessResult)
                                        throws GlobalException {
                CmsSite site = channel.getSite();
                for (int page = beginPage; page <= endPage; page++) {
                        String pcFileName = channel.getStaticFilename(true, page);
                        String mobileFileName = channel.getStaticFilename(false, page);
                        /** pc静态页 */
                        String fileRealPath = realPathResolver.get(pcFileName);
                        File pcFile = new File(fileRealPath);
                        if (pcFile.exists()) {
                                pcFile.delete();
                        }
                        /** 删除远程文件 */
                        if (!UploadServerType.local.equals(site.getStaticServerType())) {
                                site.deleteRemoteFile(ContentConstant.DISTRIBUTE_TYPE_HTML, pcFileName);
                        }
                        /** 手机静态页 */
                        fileRealPath = realPathResolver.get(mobileFileName);
                        File mobileFile = new File(fileRealPath);
                        if (mobileFile.exists()) {
                                mobileFile.delete();
                        }
                        /** 删除远程文件 */
                        if (!UploadServerType.local.equals(site.getStaticServerType())) {
                                site.deleteRemoteFile(ContentConstant.DISTRIBUTE_TYPE_HTML, mobileFileName);
                        }
                        processResult.setCurrPage(processResult.getCurrPage() + 1);
                        channelProcessResult.setCurrPage(channelProcessResult.getCurrPage() + 1);
                }
                /** 当前栏目已完成删除静态化 */
                afterChannelComplete(channel, true, true, user, ignoreException, channelProcessResult);
        }

        void doRunContentCreate(Channel channel, GlobalConfigDTO config, HashMap<String, Object> data,
                        List<Integer> contentIds, boolean ignoreException, CoreUser user,
                        PageProcessResult processResult, PageProcessResult channelProcessResult)
                                        throws GlobalException {
                CmsSite site = channel.getSite();
                /** 当前栏目已完成静态化 */
                afterChannelComplete(channel, false, false, user, ignoreException, channelProcessResult);
                /**自动生成栏目页*/
                if (site.getConfig().getStaticAutoChannel()) {
                        staticPageService.channel(channel,false,ignoreException,null,null);
                }
                /** 自动生成首页页 */
                if (site.getConfig().getStaticAutoChannel()) {
                        staticPageService.index(site,ignoreException);
                }
                List<Content> contents = contentService.findAllById(contentIds);
                for (int page = 0; page <= contents.size(); page++) {
                        /**模板未找到也认定成功*/
                        processResult.setCurrPage(processResult.getCurrPage() + 1);
                        channelProcessResult.setCurrPage(channelProcessResult.getCurrPage() + 1);
                        Content content = contents.get(page);
                        String pcTpl = content.getTplContentOrDefForPc();
                        String mobileTpl = content.getTplContentOrDefForMobile();
                        try {
                                staticPageService.createContentPage(site, config, data, content, pcTpl, mobileTpl,
                                                ignoreException);
                                /** 内容生成完，标注内容生成状态 */
                                content.setHasStatic(true);
                                contentService.update(content);
                        } catch (GlobalException e) {
                                log.error(e.getMessage());
                        }
                }
        }

        void doRunContentDelete(Channel channel, List<Integer> contentIds, boolean ignoreException, CoreUser user,
                        PageProcessResult processResult, PageProcessResult channelProcessResult,
                        HttpServletRequest request, HttpServletResponse response) throws GlobalException {
                CmsSite site = channel.getSite();
                if (site.getConfig().getStaticAutoChannel()) {
                        staticPageService.channel(channel,false,ignoreException,request,response);
                }
                /** 自动生成首页页 */
                if (site.getConfig().getStaticAutoChannel()) {
                        staticPageService.index(site,ignoreException);
                }
                /** 当前栏目已完成删除静态化 */
                afterChannelComplete(channel, true, false, user, ignoreException, channelProcessResult);
                List<Content> contents = contentService.findAllById(contentIds);
                for (int page = 0; page < contents.size(); page++) {
                        Content content = contents.get(page);
                        //staticPageService.deleteContentRelated(content, false, request, response);
                        staticPageService.deleteContent(content);
                        /** 内容删除完，标注内容生成状态为未生成 */
                        content.setHasStatic(false);
                        contentService.update(content);
                        processResult.setCurrPage(processResult.getCurrPage() + 1);
                        channelProcessResult.setCurrPage(channelProcessResult.getCurrPage() + 1);
                }
        }

        private void afterChannelComplete(Channel channel, boolean deleteJob, boolean staticChannel, CoreUser user,
                        boolean ignoreException, PageProcessResult channelProcessResult) throws GlobalException {
                if (staticChannel) {
                        /** 当前栏目已完成栏目静态化 */
                        channel.setHasStaticChannel(!deleteJob);
                } else {
                        /** 当前栏目已完成栏目的内容静态化 */
                        channel.setHasStaticContent(!deleteJob);
                }
                try {
                        this.channelService.update(channel);
                } catch (GlobalException e) {
                        log.error(e.getMessage());
                        if (!ignoreException) {
                                throw e;
                        }
                }
                this.channelService.flush();
                getChannelProcessResult().remove(channel.getId().toString());
                /** 修改了栏目数据，需要更新栏目数据权限，否则取到的栏目数据是错误的 */
                if (user != null) {
                        user.clearPermCache();
                }
                orgService.clearAllOrgCache();
                roleService.clearAllRoleCache();
                userService.clearAllUserCache();
        }

        class ChannelPageParam {
                Integer channelId;
                /**执行静态化之前的栏目是否静态化状态，任务执行失败后还原到该状态*/
                Boolean preHasStaticChannel;
                GlobalConfigDTO config;
                List<Integer> contentIds;
                HashMap<String, Object> data;
                int beginPage;
                int endPage;
                /** 是否删除任务 */
                boolean deleteJob;
                /** 是否忽略异常 */
                boolean ignoreException;
                /** 当前操作用户 */
                CoreUser user;

                public ChannelPageParam(Integer channelId,  Boolean preHasStaticChannel,HashMap<String, Object> data, int beginPage, int endPage,
                                boolean deleteJob, boolean ignoreException, CoreUser user,
                                PageProcessResult processResult, PageProcessResult channelProcessResult) {
                        super();
                        this.channelId = channelId;
                        this.preHasStaticChannel = preHasStaticChannel;
                        this.data = data;
                        this.beginPage = beginPage;
                        this.endPage = endPage;
                        this.deleteJob = deleteJob;
                        this.ignoreException = ignoreException;
                        this.user = user;
                }

                public ChannelPageParam(Integer channelId, GlobalConfigDTO config, List<Integer> contentIds,
                                HashMap<String, Object> data, boolean deleteJob, boolean ignoreException, CoreUser user,
                                PageProcessResult processResult, PageProcessResult channelProcessResult) {
                        super();
                        this.channelId = channelId;
                        this.config = config;
                        this.contentIds = contentIds;
                        this.data = data;
                        this.deleteJob = deleteJob;
                        this.ignoreException = ignoreException;
                        this.user = user;
                }

                public GlobalConfigDTO getConfig() {
                        return config;
                }

                public List<Integer> getContentIds() {
                        return contentIds;
                }

                public void setConfig(GlobalConfigDTO config) {
                        this.config = config;
                }

                public void setContentIds(List<Integer> contentIds) {
                        this.contentIds = contentIds;
                }

                public Integer getChannelId() {
                        return channelId;
                }

                public Boolean getPreHasStaticChannel() {
                        return preHasStaticChannel;
                }

                public void setPreHasStaticChannel(Boolean preHasStaticChannel) {
                        this.preHasStaticChannel = preHasStaticChannel;
                }

                /**
                 * @return the user
                 */
                public CoreUser getUser() {
                        return user;
                }

                /**
                 * @param user
                 *                the user to set
                 */
                public void setUser(CoreUser user) {
                        this.user = user;
                }

                public HashMap<String, Object> getData() {
                        return data;
                }

                public int getBeginPage() {
                        return beginPage;
                }

                public int getEndPage() {
                        return endPage;
                }

                public void setChannelId(Integer channelId) {
                        this.channelId = channelId;
                }

                public void setData(HashMap<String, Object> data) {
                        this.data = data;
                }

                public void setBeginPage(int beginPage) {
                        this.beginPage = beginPage;
                }

                public void setEndPage(int endPage) {
                        this.endPage = endPage;
                }

                public boolean isDeleteJob() {
                        return deleteJob;
                }

                public void setDeleteJob(boolean deleteJob) {
                        this.deleteJob = deleteJob;
                }

                public boolean isIgnoreException() {
                        return ignoreException;
                }

                public void setIgnoreException(boolean ignoreException) {
                        this.ignoreException = ignoreException;
                }

        }

        private RealPathResolver realPathResolver;
        private ChannelService channelService;
        private ContentService contentService;
        private ContentStaticPageService staticPageService;
        private CmsOrgService orgService;
        private CoreRoleService roleService;
        private CoreUserService userService;

        private void init(){
                realPathResolver = ApplicationContextProvider.getBean(RealPathResolver.class);
                channelService =  ApplicationContextProvider.getBean(ChannelService.class);
                contentService =  ApplicationContextProvider.getBean(ContentService.class);
                staticPageService =  ApplicationContextProvider.getBean(ContentStaticPageService.class);
                orgService =  ApplicationContextProvider.getBean(CmsOrgService.class);
                roleService =  ApplicationContextProvider.getBean(CoreRoleService.class);
                userService =  ApplicationContextProvider.getBean(CoreUserService.class);
        }

        private Logger log = LoggerFactory.getLogger(ChannelPageCreateThreadServiceImpl.class);
}
