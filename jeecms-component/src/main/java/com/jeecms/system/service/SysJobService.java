/**
 *
 */
package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SysJob;
import org.quartz.*;

import java.util.List;

/**
 * @Description:定时任务service接口
 * @author: tom
 * @date: 2018年6月12日 上午9:44:19
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface SysJobService extends IBaseService<SysJob, Integer> {

        /**
         * 新增任务
         *
         * @param job
         * @throws Exception
         * @Title: addJob
         * @return: void
         */
        public void addJob(SysJob job) throws Exception;

        /**
         * 任务暂停
         *
         * @param job
         * @Title: jobPause
         * @return: void
         */
        public void jobPause(SysJob job);

        /**
         * 任务恢复
         *
         * @param job
         * @Title: jobResume
         * @return: void
         */
        public void jobResume(SysJob job);

        /**
         * 按照新的cron表达式重启任务
         *
         * @param job
         * @Title: jobReschedule
         * @return: void
         */
        public void jobReschedule(SysJob job);

        /**
         * 任务删除
         *
         * @param job
         * @Title: jobDelete
         * @return: void
         */
        public void jobDelete(SysJob job);

        /**
         * 获取job DataMap
         *
         * @param job
         * @return
         * @Title: getJobDataMap
         * @return: JobDataMap
         */
        public JobDataMap getJobDataMap(SysJob job);
        
        /**
         * 获取持久化 job DataMap
         * @Title: getCurrJobDataMap
         * @param job
         * @return: JobDataMap
         */
        public JobDataMap getCurrJobDataMap(SysJob job)  throws SchedulerException ;

        /**
         * 获取job detail
         *
         * @param jobKey
         * @param className   类名
         * @param description 描述
         * @param map
         * @return
         * @throws Exception
         * @Title: geJobDetail
         * @return: JobDetail
         */
        public JobDetail geJobDetail(JobKey jobKey, String className, String description, JobDataMap map) throws Exception;

        /**
         * 获取job trigger
         *
         * @param job
         * @return
         * @Title: getTrigger
         * @return: Trigger
         */
        public Trigger getTrigger(SysJob job);

        /**
         * 获取job trigger
         *
         * @param job
         * @return
         * @Title: getTrigger
         * @return: Trigger
         */
        Trigger getSimpleTrigger(SysJob job);

        /**
         * 获取job key
         *
         * @param job
         * @return
         * @Title: getJobKey
         * @return: JobKey
         */
        public JobKey getJobKey(SysJob job);

        /**
         * 检查job是否存在. true表示已存在
         *
         * @param job
         * @return
         * @throws SchedulerException 调度异常
         * @Title: checkJobExist
         * @return: boolean
         */
        boolean checkJobExist(SysJob job) throws SchedulerException;
        
        /**
         * 检查job是否存在. true表示已存在
         * @Title: checkJobExist
         * @param jobKey  JobKey
         * @throws SchedulerException 调度异常
         * @return: boolean
         */
        boolean checkJobExist(JobKey jobKey) throws SchedulerException;
        
        /**
         * 根据任务名称获取任务
         *
         * @param jobName 任务名称
         * @return SysJob
         * @throws GlobalException 全局异常
         */
        SysJob findByName(String jobName) throws GlobalException;

        /**
         * 获取所有未删除的job分组名称
         *
         * @param siteId 站点id
         * @return List<Integer>
         */
        List<Integer> findCronType(Integer siteId);

        /**
         * 添加任务
         *
         * @param job    任务实体
         * @param siteId 站点id
         * @return SysJob
         */
        SysJob save(SysJob job, Integer siteId) throws GlobalException;

        /**
         * 修改任务并重启任务
         *
         * @param job 任务对象
         * @return SysJob
         * @throws GlobalException 异常
         */
        SysJob updateAndReschedule(SysJob job) throws GlobalException;
}
