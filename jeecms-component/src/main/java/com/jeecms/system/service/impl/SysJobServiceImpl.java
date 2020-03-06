/**
 *
 */

package com.jeecms.system.service.impl;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.scheduler.IBaseJob;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.JobExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.system.constants.SysJobConstants;
import com.jeecms.system.dao.SysJobDao;
import com.jeecms.system.domain.SysJob;
import com.jeecms.system.job.factory.JobFactory;
import com.jeecms.system.service.SysJobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jeecms.system.constants.SysJobConstants.EXEC_CYCLE_TYPE_CRON;
import static com.jeecms.system.constants.SysJobConstants.EXEC_CYCLE_TYPE_TIME;

/**
 * @Description:定时任务service实现
 * @author: tom
 * @date: 2018年6月12日 上午9:45:03
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysJobServiceImpl extends BaseServiceImpl<SysJob, SysJobDao, Integer> implements SysJobService {

    private Logger logger = LoggerFactory.getLogger(SysJobServiceImpl.class);

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ChannelService channelService;

    @Override
    public void addJob(SysJob job) throws Exception {
        if (job.getStatus()) {
            logger.info(java.text.Normalizer.normalize(String.format("addJob %s", job.getCronName()),
                    java.text.Normalizer.Form.NFKD));
            try {
                // 启动调度器
                scheduler.start();
                // 构建job信息
                JobKey jobKey = getJobKey(job);
                JobDataMap map = getJobDataMap(job);
                JobDetail jobDetail = geJobDetail(jobKey, job.getClassPath(), job.getRemark(), map);
                // 按新的cronExpression表达式构建一个新的trigger
                Trigger trigger = getSimpleTrigger(job);

                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                logger.error(java.text.Normalizer.normalize(e.getMessage(),
                        java.text.Normalizer.Form.NFKD));
            }
        }
    }

    @Override
    public void jobPause(SysJob job) {
        try {
            String jobGroupName = job.getGroupName();
            String jobName = job.getCronName();
            logger.info(java.text.Normalizer.normalize(String.format("jobPause %s", job.getCronName()),
                    java.text.Normalizer.Form.NFKD));
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (SchedulerException e) {
            logger.error(java.text.Normalizer.normalize(e.getMessage(), java.text.Normalizer.Form.NFKD));
        }
    }

    @Override
    public void jobResume(SysJob job) {
        if (job.getStatus()) {
            try {
                String jobGroupName = job.getGroupName();
                String jobName = job.getCronName();
                logger.info(java.text.Normalizer.normalize(
                        String.format("jobResume %s", job.getCronName()),
                        java.text.Normalizer.Form.NFKD));
                scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
            } catch (SchedulerException e) {
                logger.error(java.text.Normalizer.normalize(e.getMessage(),
                        java.text.Normalizer.Form.NFKD));
            }
        }
    }

    @Override
    public void jobReschedule(SysJob job) {
        String jobGroupName = job.getGroupName();
        String jobName = job.getCronName();
        JobKey jobKey = getJobKey(job);
        /* 先删除任务 */
        try {
            logger.info(java.text.Normalizer.normalize(String.format("jobDelete %s", job.getCronName()),
                    java.text.Normalizer.Form.NFKD));
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(java.text.Normalizer.normalize(e.getMessage(), java.text.Normalizer.Form.NFKD));
        }
        /* 启用状态则新增任务 */
        if (job.getStatus()) {
            try {
                logger.info(java.text.Normalizer.normalize(
                        String.format("jobAddSchedule %s", job.getCronName()),
                        java.text.Normalizer.Form.NFKD));
                JobDataMap map = getJobDataMap(job);
                JobDetail jobDetail = geJobDetail(jobKey, job.getClassPath(), job.getRemark(), map);
                scheduler.scheduleJob(jobDetail, getSimpleTrigger(job));
            } catch (Exception e) {
                logger.error(java.text.Normalizer.normalize(e.getMessage(),
                        java.text.Normalizer.Form.NFKD));
            }
        }
    }

    @Override
    public Iterable<SysJob> batchUpdate(Iterable<SysJob> entities) throws GlobalException {
        List<SysJob> jobs = (List<SysJob>) super.batchUpdate(entities);
        for (SysJob job : jobs) {
            jobReschedule(job);
        }
        return jobs;
    }

    @Override
    public SysJob update(SysJob bean) throws GlobalException {
        bean.setGroupName(SysJobConstants.geiJobGroupName(bean.getCronType()));
        bean = initChannel(bean);
        SysJob job = super.update(bean);
        jobReschedule(job);
        return job;
    }

    @Override
    public SysJob updateAll(SysJob bean) throws GlobalException {
        bean.setGroupName(SysJobConstants.geiJobGroupName(bean.getCronType()));
        SysJob job = super.updateAll(bean);
        jobReschedule(job);
        return job;
    }

    @Override
    public void jobDelete(SysJob job) {
        String jobGroupName = job.getGroupName();
        String jobName = job.getCronName();
        try {
            logger.info(java.text.Normalizer.normalize(String.format("jobDelete %s", job.getCronName()),
                    java.text.Normalizer.Form.NFKD));
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (SchedulerException e) {
            logger.error(java.text.Normalizer.normalize(e.getMessage(), java.text.Normalizer.Form.NFKD));
        }
    }

    /**
     * 获取JobDataMap.(Job参数对象)
     */
    @Override
    public JobDataMap getJobDataMap(SysJob job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getCronName());
        map.put("groupName", job.getGroupName());
        map.put("cron", job.getCron());
        map.put("params", job.getParams());
        map.put("remark", job.getRemark());
        map.put("classPath", job.getClassPath());
        map.put("status", job.getStatus());
        map.put("siteId", job.getSiteId());
        if (job.getChannelIds() != null && job.getChannelIds().length > 0) {
            map.put("channelIds", Arrays.asList(job.getChannelIds()));
        } else {
            map.put("channelIds", "");
        }
        return map;
    }

    @Override
    public JobDataMap getCurrJobDataMap(SysJob job) throws SchedulerException {
        /** 构建job KEY 信息 */
        JobKey jobKey = getJobKey(job);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail != null) {
            return jobDetail.getJobDataMap();
        }
        return getJobDataMap(job);
    }

    /**
     * 获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     */
    @Override
    public JobDetail geJobDetail(JobKey jobKey, String className, String description, JobDataMap map)
            throws Exception {
        return JobBuilder.newJob(getClass(className)).withIdentity(jobKey).withDescription(description)
                .setJobData(map).storeDurably().build();
    }

    /**
     * 获取Trigger (Job的触发器,执行规则) （所有misfire的任务会马上执行)
     */
    @Override
    public Trigger getTrigger(SysJob job) {
        return TriggerBuilder.newTrigger().withIdentity(job.getCronName(), job.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }

    /**
     * 获取Trigger (Job的触发器,执行规则) （所有misfire的任务会在某个时间后开始执行)
     */
    @Override
    public Trigger getSimpleTrigger(SysJob job) {
        Date startTime = job.getStartTime();
        Date nowDate = Calendar.getInstance().getTime();
        if (startTime == null) {
            return getTrigger(job);
        }
        //如果开始时间小于当前时间，则把当前时间当作开始时间
        if (startTime.before(nowDate)) {
            startTime = nowDate;
        }
        startTime.setTime(startTime.getTime() + 6000L);
        //startTime.setTime(startTime.getTime());
        return TriggerBuilder.newTrigger().withIdentity(job.getCronName(), job.getGroupName())
                .startAt(startTime).withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }

    /**
     * 获取JobKey,包含Name和Group
     */
    @Override
    public JobKey getJobKey(SysJob job) {
        return JobKey.jobKey(job.getCronName(), job.getGroupName());
    }

    @SuppressWarnings("unchecked")
    public static Class<IBaseJob> getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Class<IBaseJob>) class1;
    }

    @Override
    public boolean checkJobExist(SysJob job) throws SchedulerException {
        JobKey jobKey = this.getJobKey(job);
        return scheduler.checkExists(jobKey);
    }

    @Override
    public boolean checkJobExist(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    @Override
    public SysJob findByName(String jobName) throws GlobalException {
        return dao.findByCronNameAndHasDeleted(jobName, false);
    }

    @Override
    public List<Integer> findCronType(Integer siteId) {
        return dao.findCronType(siteId);
    }

    @Override
    public SysJob save(SysJob job, Integer siteId) throws GlobalException {
        job = initChannel(job);
        if (findByName(job.getCronName()) != null) {
            throw new GlobalException(new JobExceptionInfo(
                    SettingErrorCodeEnum.JOB_NAME.getDefaultMessage(),
                    SettingErrorCodeEnum.JOB_NAME.getCode()));
        }
        job.setSiteId(siteId);
        job.setGroupName(SysJobConstants.geiJobGroupName(job.getCronType()));
        if (EXEC_CYCLE_TYPE_TIME.equals(job.getExecCycleType())) {
            String cron = SysJobConstants.conversionCron(job.getIntervalType(), job.getIntervalNum(), job.getStartTime());
            job.setCron(cron);
        } else if (EXEC_CYCLE_TYPE_CRON.equals(job.getExecCycleType())) {
            job.setCron(job.getCron());
        }
        job.setClassPath(SysJobConstants.getClassPath(job.getCronType()));
        save(job);
        try {
            addJob(job);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return job;
    }

    @Override
    public SysJob updateAndReschedule(SysJob job) throws GlobalException {
        SysJob sysJob = findByName(job.getCronName());
        if (sysJob != null && !sysJob.getId().equals(job.getId())) {
            throw new GlobalException(
                    new JobExceptionInfo(SettingErrorCodeEnum.JOB_NAME.getDefaultMessage(),
                            SettingErrorCodeEnum.JOB_NAME.getCode()));
        }
        SysJob job1 = findById(job.getId());
        job.setCronType(job1.getCronType());
        // 执行周期类型为设置类型时转为cron表达式
        if (EXEC_CYCLE_TYPE_TIME.equals(job.getExecCycleType())) {
            String cron = SysJobConstants.conversionCron(job.getIntervalType(),
                    job.getIntervalNum(), job.getStartTime());
            job.setCron(cron);
        }
        job.setGroupName(SysJobConstants.geiJobGroupName(job.getCronType()));
        job.setClassPath(SysJobConstants.getClassPath(job.getCronType()));
        job = update(job);
        jobReschedule(job);
        return job;
    }


    /**
     * 启动系统内置的定时任务
     */
    @PostConstruct
    private void startUpJob() {
        try {
            SysJob job = JobFactory.createStatisticsAccessJob(new Date());
            if (!checkJobExist(job)) {
                addJob(job);
            } else {
                jobDelete(job);
                addJob(job);
            }
        } catch (Exception e) {
            logger.error("生成数据统计任务启动失败, time:{}", Calendar.getInstance().getTime());
        }
        try {
            SysJob job = JobFactory.createLogStatisticsJob(new Date());
            if (!checkJobExist(job)) {
                addJob(job);
            } else {
                jobDelete(job);
                addJob(job);
            }
        } catch (Exception e) {
            logger.error("生成日志统计任务启动失败, time:{}", Calendar.getInstance().getTime());
        }
        try {
            SysJob job = JobFactory.createLogAlarmJob(new Date());
            if (!checkJobExist(job)) {
                addJob(job);
            } else {
                jobDelete(job);
                addJob(job);
            }
        } catch (Exception e) {
            logger.error("生成日志告警任务启动失败, time:{}", Calendar.getInstance().getTime());
        }
        try {
            SysJob job = JobFactory.createLogAlertJob(new Date());
            if (!checkJobExist(job)) {
                addJob(job);
            } else {
                jobDelete(job);
                addJob(job);
            }
        } catch (Exception e) {
            logger.error("生成日志预警任务启动失败, time:{}", Calendar.getInstance().getTime());
        }
        try {
            SysJob job = JobFactory.createUserSummaryJob(new Date());
            if (!checkJobExist(job)) {
                addJob(job);
            } else {
                jobDelete(job);
                addJob(job);
            }
        } catch (Exception e) {
            logger.error("粉丝统计任务启动失败, time:{}", Calendar.getInstance().getTime());
        }
    }

    private SysJob initChannel(SysJob job) {
        if (SysJobConstants.CRON_TYPE_CHANNEL == job.getCronType() || SysJobConstants.CRON_TYPE_CONTENT == job.getCronType()) {
            if (job.getIsAll() != null && job.getIsAll()) {
                List<Channel> list = channelService.findAll(false);
                StringBuilder channels = new StringBuilder();
                Integer[] channelIds = new Integer[list.size()];
                int i = 0;
                for (Channel channel : list) {
                    channels.append(channel.getId()).append(",");
                    channelIds[i] = channel.getId();
                    i++;
                }
                job.setChannels(channels.substring(0, channels.length() - 1));
                job.setChannelIds(channelIds);
            } else {
                StringBuilder channels = new StringBuilder();
                if (job.getChannelIds() != null) {
                    for (Integer channelId : job.getChannelIds()) {
                        channels.append(channelId).append(",");
                    }
                    job.setChannels(channels.substring(0, channels.length() - 1));
                }

            }
        }
        return job;
    }
}