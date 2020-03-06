/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.DomainNotFoundExceptionInfo;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.JobExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysJob;
import com.jeecms.system.service.SysJobService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeecms.system.constants.SysJobConstants.EXEC_CYCLE_TYPE_CRON;
import static com.jeecms.system.constants.SysJobConstants.EXEC_CYCLE_TYPE_TIME;

/**
 * 定时任务管理controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019年6月12日 上午10:14:25
 */
@RestController
@RequestMapping("/jobs")
public class SysJobController extends BaseController<SysJob, Integer> {
	private Logger logger = LoggerFactory.getLogger(SysJobController.class);
	@Autowired
	private SysJobService service;

	/**
	 * 初始化启动所有的Job
	 */
	@PostConstruct
	public void init() {
		try {
			reStartAllJobs();
			logger.info("init job success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("init job error : " + e.getMessage());
		}
	}

	/**
	 * 根据ID重启某个Job,下个执行周期生效
	 *
	 * @param id
	 * @return
	 * @throws SchedulerException
	 * @throws GlobalException
	 * @Title: refresh
	 * @return: ResponseInfo
	 */
	@RequestMapping("/refresh")
	public ResponseInfo refresh(Integer id) throws SchedulerException, GlobalException {
		SysJob entity = service.findById(id);
		if (entity == null) {
			throw new GlobalException(new DomainNotFoundExceptionInfo());
		}
		if (entity.getStatus()) {
			service.jobReschedule(entity);
		}
		return new ResponseInfo();
	}

	/**
	 * 重启数据库中所有的Job
	 */
	@RequestMapping("/refreshAll")
	public ResponseInfo refreshAll() throws GlobalException {
		try {
			reStartAllJobs();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new GlobalException(new JobExceptionInfo());
		}
		return new ResponseInfo();
	}

	/**
	 * 添加任务，不是DB入库
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 * @Title: start
	 * @return: ResponseInfo
	 */
	@RequestMapping("/addJob")
	public ResponseInfo start(Integer id) throws Exception {
		SysJob entity = service.findById(id);
		if (entity == null) {
			throw new GlobalException(new DomainNotFoundExceptionInfo());
		}
		if (entity.getStatus()) {
			service.addJob(entity);
		}
		return new ResponseInfo();
	}

	/**
	 * 任务暂停，下个周期生效
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 * @Title: pause
	 * @return: ResponseInfo
	 */
	@RequestMapping("/pause")
	public ResponseInfo pause(Integer id) throws Exception {
		SysJob entity = service.findById(id);
		if (entity == null) {
			throw new GlobalException(new DomainNotFoundExceptionInfo());
		}
		if (entity.getStatus()) {
			service.jobPause(entity);
		}
		return new ResponseInfo();
	}

	/**
	 * * 任务恢复，下个周期生效
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 * @Title: resume
	 * @return: ResponseInfo
	 */
	@RequestMapping("/resume")
	public ResponseInfo resume(Integer id) throws Exception {
		SysJob entity = service.findById(id);
		if (entity == null) {
			throw new GlobalException(new DomainNotFoundExceptionInfo());
		}
		if (entity.getStatus()) {
			service.jobResume(entity);
		}
		return new ResponseInfo();
	}

	/**
	 * 删除定时任务
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 * @Title: deleteJob
	 * @return: ResponseInfo
	 */
	@RequestMapping("/deleteJob")
	public ResponseInfo deleteJob(Integer id) throws Exception {
		SysJob entity = service.findById(id);
		if (entity == null) {
			throw new GlobalException(new DomainNotFoundExceptionInfo());
		}
		if (entity.getStatus()) {
			service.jobDelete(entity);
		}
		return new ResponseInfo();
	}

	/**
	 * 取消任务
	 *
	 * @param ids ids
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/disabledJobs")
	public ResponseInfo disabledJobs(@RequestBody DeleteDto ids) throws GlobalException {
		List<SysJob> jobs = service.findAllById(Arrays.asList(ids.getIds()));
		for (SysJob job : jobs) {
			job.setStatus(false);
		}
		service.batchUpdate(jobs);
		return new ResponseInfo();
	}

	/**
	 * 启动任务
	 *
	 * @param ids ids
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/enabledJobs")
	public ResponseInfo enabledJobs(@RequestBody DeleteDto ids) throws GlobalException {
		List<SysJob> jobs = service.findAllById(Arrays.asList(ids.getIds()));
		for (SysJob job : jobs) {
			job.setStatus(true);
		}
		service.batchUpdate(jobs);
		return new ResponseInfo();
	}

	/**
	 * 分页列表
	 *
	 * @param request  {@link HttpServletRequest}
	 * @param pageable {@link Pageable}
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 * @throws ParseException
	 */
	@RequestMapping(value = "/page")
	@SerializeField(clazz = SysJob.class, includes = {"id", "cronType", "cronName", "createTime", "status",
			"createUser"})
	public ResponseInfo getPage(@PageableDefault(sort = {"id"}, direction = Direction.DESC) Pageable pageable,
								String cronName, HttpServletRequest request) throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(2);
		if (StringUtils.isNotBlank(cronName)) {
			params.put("LIKE_cronName_String", new String[]{cronName});
		}
		params.put("EQ_siteId_Integer", new String[]{String.valueOf(SystemContextUtils.getSiteId(request))});
		Page<SysJob> page = service.getPage(params, pageable, false);
		/*if (info.getData() != null && info.getData() instanceof Page) {
			Page page = (Page) info.getData();
			List<SysJob> jobs = page.getContent();
			List<SysJob> sysJobs = new ArrayList<SysJob>();
			for (SysJob job :
					jobs) {
				if (StringUtils.isNotBlank(job.getCron())) {
					//设置下一次执行时间
					CronExpression cronExpression = new CronExpression(job.getCron());
					job.setNextExecuteDate(cronExpression.getNextValidTimeAfter(new
							Date()));
				}
				sysJobs.add(job);
			}
			info.setData(new PageImpl(sysJobs,
					pageable, page.getTotalElements()));
		}*/

		return new ResponseInfo(page);
	}

	@RequestMapping(value = "/list")
	public ResponseInfo list(HttpServletRequest request, Paginable paginable) throws GlobalException {
		return super.getList(request, paginable, true);
	}

	@Override
	@GetMapping(value = "/{id}")
	@SerializeField(clazz = SysJob.class, includes = {"id", "cronName", "cronType", "channelIds", "startTime",
			"status", "execCycleType", "intervalNum", "intervalType", "cron", "remark", "isAll"})
	public ResponseInfo get(@PathVariable(value = "id") Integer id) throws GlobalException {
		SysJob job = service.get(id);
		if (job.getIsAll() == null || !job.getIsAll()) {
			Integer[] channelIds = null;
			if (job.getChannels() != null) {
				String[] channelIdArray = job.getChannels().split(",");
				channelIds = new Integer[channelIdArray.length];
				int i = 0;
				for (String s : channelIdArray) {
					channelIds[i] = Integer.parseInt(s);
					i++;
				}
			}
			job.setChannelIds(channelIds);
		}
		return new ResponseInfo(job);
	}

	/**
	 * 保存入库并添加任务
	 */
	@PostMapping()
	public ResponseInfo save(@RequestBody @Valid SysJob job, HttpServletRequest request, BindingResult result)
			throws GlobalException {
		validateBindingResult(result);
		ResponseInfo info = valid(job.getExecCycleType(), job.getCron(), job.getIntervalType(), job.getIntervalNum());
		if (info != null) {
			return info;
		}
		service.save(job, SystemContextUtils.getSiteId(request));
		return new ResponseInfo();
	}

	/**
	 * 修改任务表并重新生成任务
	 */
	@Override
	@PutMapping()
	public ResponseInfo update(@RequestBody @Valid SysJob job, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		ResponseInfo info = valid(job.getExecCycleType(), job.getCron(), job.getIntervalType(), job.getIntervalNum());
		if (info != null) {
			return info;
		}
		service.updateAndReschedule(job);
		return new ResponseInfo();
	}

	/**
	 * 校验
	 *
	 * @param execCycleType 执行周期类型 1-设置类型 2-cron表达式
	 * @param cron          cron表达式
	 * @param intervalType  间隔类型 1-分钟 2-小时 3-天
	 * @param intervalNum   间隔时间
	 * @return
	 */
	private ResponseInfo valid(Integer execCycleType, String cron, Integer intervalType, Integer intervalNum) {
		if (EXEC_CYCLE_TYPE_CRON.equals(execCycleType) && cron == null) {
			return new ResponseInfo(SettingErrorCodeEnum.JOB_CRON_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.JOB_CRON_ALREADY_EXIST.getDefaultMessage());
		} else if (EXEC_CYCLE_TYPE_TIME.equals(execCycleType)) {
			if (intervalNum == null || intervalType == null) {
				return new ResponseInfo(SettingErrorCodeEnum.JOB_INTERVAL_ALREADY_EXIST.getCode(),
						SettingErrorCodeEnum.JOB_CRON_ALREADY_EXIST.getDefaultMessage());
			}
		}
		return null;
	}

	/**
	 * 删除任务表数据并删除任务
	 *
	 * @param dto 删除及批量删除dto
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@DeleteMapping()
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dto, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		for (Integer id : dto.getIds()) {
			service.jobDelete(service.findById(id));
		}
		// 和其他业务数据无关联，可物理删除
		super.physicalDelete(dto.getIds());
		return new ResponseInfo();
	}

	/**
	 * 重新启动所有的job
	 *
	 * @throws Exception
	 */
	private void reStartAllJobs() throws Exception {
		for (SysJob job : service.findAll(true)) {
			logger.info("Job register name : {} , group : {} , cron : {}", job.getCronName(), job.getGroupName(),
					job.getCron());
			if (job.getStatus()) {
				if (service.checkJobExist(job)) {
					service.jobReschedule(job);
				} else {
					service.addJob(job);
				}
			} else {
				logger.info("Job jump name : {} , Because {} status is {}", job.getCronName(), job.getCronName(),
						job.getStatus());
			}
		}
	}

	/**
	 * 检测job标识是否存在
	 *
	 * @param cronName job名称
	 * @param id       job对象id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@RequestMapping(value = "/jobName/unique", method = RequestMethod.GET)
	public ResponseInfo checkJobName(String cronName, Integer id) throws GlobalException {
		if (StringUtils.isBlank(cronName)) {
			return new ResponseInfo(true);
		}
		// 假设id有值，则进行修改场景下校验job标识码逻辑，反之进行新增场景下校验逻辑
		if (id != null) {
			SysJob sysJob = service.findByName(cronName);
			if (sysJob != null && !sysJob.getId().equals(id)) {
				return new ResponseInfo(false);
			}
		} else {
			if (service.findByName(cronName) != null) {
				return new ResponseInfo(false);
			}
		}
		return new ResponseInfo(true);
	}

	/**
	 * 获取所有未删除的job分组名
	 *
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/findGroupName", method = RequestMethod.GET)
	public ResponseInfo findByGroupName(HttpServletRequest request) {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(service.findCronType(siteId));
	}
}
