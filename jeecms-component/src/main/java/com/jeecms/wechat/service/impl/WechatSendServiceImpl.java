/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *       仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.api.mp.MassManageService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.mass.MassRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;
import com.jeecms.common.wechat.bean.response.mp.user.MassResponse;
import com.jeecms.system.domain.SysJob;
import com.jeecms.system.job.factory.JobFactory;
import com.jeecms.system.service.SysJobService;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.dao.WechatSendDao;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.domain.WechatSendArticle;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatSendArticleService;
import com.jeecms.wechat.service.WechatSendService;

/**
 * 微信群发Service实现类
 * 
 * @author ljw
 * @version 1.0
 * @date 2018-08-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatSendServiceImpl extends BaseServiceImpl<WechatSend, WechatSendDao, Integer>
		implements WechatSendService {

	@Autowired
	private SysJobService jobService;
	@Autowired
	private WechatMaterialService wechatMaterialService;
	@Autowired
	private WechatSendArticleService wechatSendArticleService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	@Autowired
	private MassManageService massManageService;

	@Override
	public ResponseInfo saveWechatSend(WechatSend wechatSend) throws Exception {
		AbstractWeChatInfo abs = abstractWeChatInfoService.findAppId(wechatSend.getAppId());
		// 获取定时日期
		Date date = wechatSend.getSendDate();
		// 判断今天发送机会是否已经用完
		Integer day = 1;
		List<WechatSend> list1 = dao.selectMassService(Arrays.asList(abs.getAppId()),
				MyDateUtils.getStartDate(date), MyDateUtils.getFinallyDate(date));
		// 如果查询出的数据等于1，则表明该公众号今天已经发了
		if (list1.size() == day) {
			return new ResponseInfo(RPCErrorCodeEnum.MASS_SUBSCRIPTION_ERROR.getCode(),
					RPCErrorCodeEnum.MASS_SUBSCRIPTION_ERROR.getDefaultMessage(), false);
		}
		// 判断该公众号还有几次发送的机会
		Short type = 2;
		if (abs.getWechatType().equals(type)) {
			// 为服务号
			Boolean flag = service(abs.getAppId(), date);
			if (!flag) {
				return new ResponseInfo(RPCErrorCodeEnum.MASS_SERVICE_ERROR.getCode(),
						RPCErrorCodeEnum.MASS_SERVICE_ERROR.getDefaultMessage(), false);
			}
		}
		// 查询素材
		WechatMaterial material = wechatMaterialService.findById(wechatSend.getMaterialId());
		wechatSend.setAppId(abs.getAppId());
		wechatSend.setStatus(WechatConstants.SEND_STATUS_WAIT);
		StringBuilder builder = new StringBuilder();
		builder.append("{\"articles\":").append(material.getMaterialJson()).append("}");
		wechatSend.setMaterialJson(builder.toString());
		wechatSend = super.save(wechatSend);
		SysJob job = JobFactory.createWechatSendJob(wechatSend.getId(), wechatSend.getDateString());
		// 新增job
		jobService.addJob(job);
		return new ResponseInfo();
	}

	/**
	 * 判断服务号是否能发送信息
	 * 
	 * @Title: service
	 * @param appId 公众号ID
	 * @param date  日期
	 * @return
	 */
	public Boolean service(String appId, Date date) {
		Integer integer = 4;
		Instant instant = date.toInstant();
		// 获得本地默认时区
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
		// 本月第一天
		String startDate = localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		// 本月最后一天
		String endDate = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		List<WechatSend> list = dao.selectMassService(Arrays.asList(appId), MyDateUtils.parseDate(startDate),
				MyDateUtils.getFinallyDate(MyDateUtils.parseDate(endDate)));
		if (list.size() >= integer) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<WechatSend> listWechatSend(List<String> appids, Date start, Date end) throws GlobalException {
		List<WechatSend> list = dao.selectMassService(appids, start, end);
		return list;
	}

	@Override
	public Page<WechatSend> pageWechatSend(List<String> appids, Date start, Date end, Integer status, 
			String title, Pageable pageable) throws GlobalException {
		Page<WechatSend> page = dao.getSendPage(appids, start, end, status, title, pageable);
		return page;
	}

	@Override
	public ResponseInfo deleteWechatSend(Integer[] ids) throws Exception {
		super.delete(ids);
		for (Integer integer : ids) {
			SysJob job = JobFactory.createWechatSendJob(integer, new Date());
			if (jobService.checkJobExist(job)) {
				jobService.jobDelete(job);
			}
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo updateWechatSend(WechatSend wechatSend) throws Exception {
		if (wechatSend.getDateString().before(new Date())) {
			return new ResponseInfo(RPCErrorCodeEnum.TIME_SET_ERROR.getCode(),
					RPCErrorCodeEnum.TIME_SET_ERROR.getDefaultMessage());
		}
		WechatSend wechatSend2 = super.update(wechatSend);
		SysJob job = JobFactory.createWechatSendJob(wechatSend2.getId(), wechatSend.getDateString());
		if (!jobService.checkJobExist(job)) {
			jobService.addJob(job);
		} else {
			jobService.jobReschedule(job);
		}

		return new ResponseInfo();
	}

	@Override
	public ResponseInfo send(WechatSend wechatSend) throws Exception {
		AbstractWeChatInfo abs = abstractWeChatInfoService.findAppId(wechatSend.getAppId());
		WechatMaterial madia = wechatMaterialService.findById(wechatSend.getMaterialId());
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(wechatSend.getAppId());
		List<WechatSendArticle> articles = new ArrayList<WechatSendArticle>();
		//如果是立即发送
		if (wechatSend.getType().equals(WechatConstants.SEND_TYPE_NOW)) {
			// 判断今天发送机会是否已经用完
			Integer day = 1;
			List<WechatSend> list1 = dao.selectMassService(Arrays.asList(wechatSend.getAppId()),
					MyDateUtils.getStartDate(new Date()), MyDateUtils.getFinallyDate(new Date()));
			// 如果查询出的数据等于1，则表明该公众号今天已经发了
			if (list1.size() == day) {
				return new ResponseInfo(RPCErrorCodeEnum.MASS_SUBSCRIPTION_ERROR.getCode(),
						RPCErrorCodeEnum.MASS_SUBSCRIPTION_ERROR.getDefaultMessage(), false);
			}
			// 判断该公众号还有几次发送的机会
			Short type = 2;
			if (abs.getWechatType().equals(type)) {
				// 为服务号
				Boolean flag = service(abs.getAppId(), new Date());
				if (!flag) {
					return new ResponseInfo(RPCErrorCodeEnum.MASS_SERVICE_ERROR.getCode(),
							RPCErrorCodeEnum.MASS_SERVICE_ERROR.getDefaultMessage(), false);
				}
			}
			StringBuilder builder = new StringBuilder();
			builder.append("{\"articles\":").append(madia.getMaterialJson()).append("}");
			wechatSend.setMaterialJson(builder.toString());
			wechatSend.setMaterialId(madia.getId());
			wechatSend.setSendDate(new Date());
			wechatSend.setSendHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
			wechatSend.setSendMinute(Calendar.getInstance().get(Calendar.MINUTE));
			MassRequest massRequest = new MassRequest();
			massRequest.setMsgtype(madia.getMediaType());
			massRequest.setMediaId(madia.getMediaId());
			MassRequest.Filter filter = massRequest.new Filter();
			// 如果tagId不为空，则按照标签组群发
			if (wechatSend.getTagId() != null) {
				filter.setIsToAll(false);
				filter.setTagId(wechatSend.getTagId());
			} else {
				// 否则发送全部
				filter.setIsToAll(true);
			}
			massRequest.setFilter(filter);
			MassResponse response = massManageService.sendMass(massRequest, validToken);
			if (response.SUCCESS_CODE.equals(response.getErrcode())) {
				wechatSend.setStatus(WechatConstants.SEND_STATUS_SUCCESS);
				wechatSend.setMsgDataId(response.getMsgDataId().toString());
				super.save(wechatSend);
				// 新增群发文章列表
				AddNewsRequest obj = JSONObject.parseObject(builder.toString(), AddNewsRequest.class);
				Integer sum = obj.getArticles().size();
				for (int i = 0; i < sum; i++) {
					WechatSendArticle article = new WechatSendArticle();
					article.setAppId(wechatSend.getAppId());
					article.setMaxUserCommentId(0);
					article.setMsgDataId(wechatSend.getMsgDataId());
					article.setMsgDataIndex(i);
					article.setTitle(obj.getArticles().get(i).getTitle());
					article.setOpen(obj.getArticles().get(i).getNeedOpenComment());
					articles.add(article);
				}
				wechatSendArticleService.saveAll(articles);
			} else {
				wechatSend.setStatus(WechatConstants.SEND_STATUS_FAIL);
				super.save(wechatSend);
				return new ResponseInfo(response.getErrcode(), response.getErrmsg(),false);
			}
		} else {
			MassRequest massRequest = new MassRequest();
			massRequest.setMsgtype(madia.getMediaType());
			massRequest.setMediaId(madia.getMediaId());
			MassRequest.Filter filter = massRequest.new Filter();
			// 如果tagId不为空，则按照标签组群发
			if (wechatSend.getTagId() != null) {
				filter.setIsToAll(false);
				filter.setTagId(wechatSend.getTagId());
			} else {
				// 否则发送全部
				filter.setIsToAll(true);
			}
			massRequest.setFilter(filter);
			MassResponse response = massManageService.sendMass(massRequest, validToken);
			if (response.SUCCESS_CODE.equals(response.getErrcode())) {
				wechatSend.setStatus(WechatConstants.SEND_STATUS_SUCCESS);
				wechatSend.setMsgDataId(response.getMsgDataId().toString());
				super.update(wechatSend);
				// 新增群发文章列表
				AddNewsRequest obj = JSONObject.parseObject(wechatSend.getMaterialJson(), 
						AddNewsRequest.class);
				Integer sum = obj.getArticles().size();
				for (int i = 0; i < sum; i++) {
					WechatSendArticle article = new WechatSendArticle();
					article.setAppId(wechatSend.getAppId());
					article.setMaxUserCommentId(0);
					article.setMsgDataId(wechatSend.getMsgDataId());
					article.setMsgDataIndex(i);
					article.setTitle(obj.getArticles().get(i).getTitle());
					articles.add(article);
				}
				wechatSendArticleService.saveAll(articles);
			} else {
				wechatSend.setStatus(WechatConstants.SEND_STATUS_FAIL);
				super.update(wechatSend);
				return new ResponseInfo(response.getErrcode(), response.getErrmsg(),false);
			}
		}
		return new ResponseInfo();
	}
}