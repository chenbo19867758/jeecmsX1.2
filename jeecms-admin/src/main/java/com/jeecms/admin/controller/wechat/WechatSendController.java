/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.wechat;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.Const;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.domain.vo.MaterialVO;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatSendService;

/**
 * 定时群发控制器/推送记录控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2018-08-08
 */
@RequestMapping("/wechatsend")
@RestController
public class WechatSendController extends BaseController<WechatSend, Integer> {

	@Autowired
	private WechatSendService wechatSendService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	@Autowired
	private WechatMaterialService wechatMaterialService;
	
	/**
	 * 获取该公众号本月群发列表
	 * 
	 * @Description: 完成
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping("/list")
	@MoreSerializeField({
			@SerializeField(clazz = WechatSend.class, includes = { "id", "status", "sendHour", "sendDate",
					"sendMinute", "material", "tagId" }),
			})
	public ResponseInfo list(HttpServletRequest request, String appId) throws GlobalException {
		// APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(), false);
		}
		// 本月的第一天
		LocalDate today = LocalDate.now();
		LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1);
		// 本月的最后一天
		LocalDate lastDay = firstday.with(TemporalAdjusters.lastDayOfMonth());
		Date start = MyDateUtils.parseDate(firstday.toString());
		Date end = MyDateUtils.getFinallyDate(MyDateUtils.parseDate(lastDay.toString()));
		List<WechatSend> list = wechatSendService.listWechatSend(Arrays.asList(appId), start, end);
		for (WechatSend wechatSend : list) {
			MaterialVO vo = new MaterialVO();
			WechatMaterial  wechatMaterial = wechatSend.getWechatMaterial();
			vo.setId(wechatMaterial.getId());
			vo.setMaterialId(wechatMaterial.getMediaId());
			vo.setMediaType(wechatMaterial.getMediaType());
			if (!Const.Mssage.REQ_MESSAGE_TYPE_NEWS.equals(wechatMaterial.getMediaType())) {
				vo.setMediaName(wechatMaterial.getRequest().getString("name"));
				vo.setMediaUrl(wechatMaterial.getRequest().getString("url"));
			}
			List<JSONObject> objects = new ArrayList<JSONObject>(8);
			JSONArray array = wechatMaterial.getRequestArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = new JSONObject();
				JSONObject object = (JSONObject) array.get(i);
				String titles = object.getString("title");
				String thumbMediaUrl = object.getString("thumbMediaUrl");
				if (!StringUtils.isNotBlank(thumbMediaUrl)) {
					//应对微信图文素材没有返回封面图片URL的情况，只返回了封面的ID，需要再查一遍
					String thumbMediaId = object.getString("thumbMediaId");
					WechatMaterial image = wechatMaterialService.getMediaId(thumbMediaId);
					thumbMediaUrl = image.getRequest().getString("url");
				}
				String url = object.getString("url");
				obj.put("title", titles);
				obj.put("thumbMediaUrl", thumbMediaUrl);
				obj.put("url", url);
				objects.add(obj);
			}
			vo.setObjects(objects);
			wechatSend.setMaterial(vo);
		}
		return new ResponseInfo(list);
	}

	/**
	 * 推送记录
	 * 
	 * @Description: 完成
	 * @param year  年
	 * @param month 月
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping("/page")
	@MoreSerializeField({
			@SerializeField(clazz = WechatSend.class, includes = { "id", "status", "sendHour", "sendDate",
					"sendMinute", "material" }),
			 })
	public ResponseInfo page(HttpServletRequest request, Integer year, Integer month, String appId, 
			String title, Pageable pageable) throws GlobalException {
		//APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),false);
		}
		// 本月的第一天
		LocalDate firstday = LocalDate.of(year, month, 1);
		// 本月的最后一天
		LocalDate lastDay = firstday.with(TemporalAdjusters.lastDayOfMonth());
		Date start = MyDateUtils.parseDate(firstday.toString());
		Date end = MyDateUtils.parseDate(lastDay.toString());
		//过滤出发送成功的数据
		Page<WechatSend> page = wechatSendService.pageWechatSend(Arrays.asList(appId), start, end,
				null, title, pageable);
		List<WechatSend> list = page.getContent();
		for (WechatSend wechatSend : list) {
			MaterialVO vo = new MaterialVO();
			WechatMaterial  wechatMaterial = wechatSend.getWechatMaterial();
			vo.setId(wechatMaterial.getId());
			vo.setMaterialId(wechatMaterial.getMediaId());
			vo.setMediaType(wechatMaterial.getMediaType());
			if (!Const.Mssage.REQ_MESSAGE_TYPE_NEWS.equals(wechatMaterial.getMediaType())) {
				vo.setMediaName(wechatMaterial.getRequest().getString("name"));
				vo.setMediaUrl(wechatMaterial.getRequest().getString("url"));
			}
			List<JSONObject> objects = new ArrayList<JSONObject>(8);
			JSONArray array = wechatMaterial.getRequestArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = new JSONObject();
				JSONObject object = (JSONObject) array.get(i);
				String titles = object.getString("title");
				String thumbMediaUrl = object.getString("thumbMediaUrl");
				if (!StringUtils.isNotBlank(thumbMediaUrl)) {
					//应对微信图文素材没有返回封面图片URL的情况，只返回了封面的ID，需要再查一遍
					String thumbMediaId = object.getString("thumbMediaId");
					WechatMaterial image = wechatMaterialService.getMediaId(thumbMediaId);
					thumbMediaUrl = image.getRequest().getString("url");
				}
				String url = object.getString("url");
				obj.put("title", titles);
				obj.put("thumbMediaUrl", thumbMediaUrl);
				obj.put("url", url);
				objects.add(obj);
			}
			vo.setObjects(objects);
			wechatSend.setMaterial(vo);
		}
		return new ResponseInfo(page);
	}
	
	/**
	 * 推送汇总
	 * 
	 * @Description: 完成
	 * @param year  年
	 * @param month 月
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping("/collect")
	@MoreSerializeField({
			@SerializeField(clazz = WechatSend.class, includes = { "id", "status", "sendHour", "sendDate",
					"sendMinute", "material", "appId", "createUser"}),
			})
	public ResponseInfo collect(HttpServletRequest request, Integer year, Integer month, String appId, 
			String title, Pageable pageable) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> appids = new ArrayList<String>(10);
		// 如果公众号为空，查询该站点下的appid
		if (StringUtils.isNotBlank(appId)) {
			appids.add(appId);
		} else {
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			// 站点没有APPID，直接返回
			if (abs.isEmpty()) {
				return new ResponseInfo();
			}
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			}
		}
		// 本月的第一天
		LocalDate firstday = LocalDate.of(year, month, 1);
		// 本月的最后一天
		LocalDate lastDay = firstday.with(TemporalAdjusters.lastDayOfMonth());
		Date start = MyDateUtils.parseDate(firstday.toString());
		Date end = MyDateUtils.parseDate(lastDay.toString());
		Page<WechatSend> page = wechatSendService.pageWechatSend(appids, start, end, 
				null, title, pageable);
		List<WechatSend> list = page.getContent();
		for (WechatSend wechatSend : list) {
			MaterialVO vo = new MaterialVO();
			WechatMaterial  wechatMaterial = wechatSend.getWechatMaterial();
			vo.setId(wechatMaterial.getId());
			vo.setMaterialId(wechatMaterial.getMediaId());
			vo.setMediaType(wechatMaterial.getMediaType());
			if (!Const.Mssage.REQ_MESSAGE_TYPE_NEWS.equals(wechatMaterial.getMediaType())) {
				vo.setMediaName(wechatMaterial.getRequest().getString("name"));
				vo.setMediaUrl(wechatMaterial.getRequest().getString("url"));
			}
			List<JSONObject> objects = new ArrayList<JSONObject>(8);
			JSONArray array = wechatMaterial.getRequestArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = new JSONObject();
				JSONObject object = (JSONObject) array.get(i);
				String titles = object.getString("title");
				String thumbMediaUrl = object.getString("thumbMediaUrl");
				if (!StringUtils.isNotBlank(thumbMediaUrl)) {
					//应对微信图文素材没有返回封面图片URL的情况，只返回了封面的ID，需要再查一遍
					String thumbMediaId = object.getString("thumbMediaId");
					WechatMaterial image = wechatMaterialService.getMediaId(thumbMediaId);
					thumbMediaUrl = image.getRequest().getString("url");
				}
				String url = object.getString("url");
				obj.put("title", titles);
				obj.put("thumbMediaUrl", thumbMediaUrl);
				obj.put("url", url);
				objects.add(obj);
			}
			vo.setObjects(objects);
			wechatSend.setMaterial(vo);
		}
		return new ResponseInfo(page);
	}

	/**
	 * 添加定时群发
	 * 
	 * @Description: 完成
	 * @param result     检测
	 * @param wechatSend 群发对象
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping()
	public ResponseInfo save(HttpServletRequest request, @RequestBody @Valid WechatSend wechatSend,
			BindingResult result) throws Exception {
		validateBindingResult(result);
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, wechatSend.getAppId());
		return wechatSendService.saveWechatSend(wechatSend);
	}
	
	/**
	 * 立即群发
	 * 
	 * @Description: 完成
	 * @param result     检测
	 * @param wechatSend 群发对象
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping("/send")
	public ResponseInfo send(HttpServletRequest request, @RequestBody @Valid WechatSend wechatSend,
			BindingResult result) throws Exception {
		validateBindingResult(result);
		wechatSend.setType(WechatConstants.SEND_TYPE_NOW);
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, wechatSend.getAppId());
		return wechatSendService.send(wechatSend);
	}

	/**
	 * 修改微信群发
	 * 
	 * @Description: 完成
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping()
	public ResponseInfo update(HttpServletRequest request, @RequestBody @Valid WechatSend wechatSend,
			BindingResult result) throws Exception {
		validateBindingResult(result);
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, wechatSend.getAppId());
		return wechatSendService.updateWechatSend(wechatSend);
	}

	/**
	 * 删除
	 * 
	 * @Title: deleteWechatSend
	 * @Description: 完成
	 * @param dels   传输对象
	 * @param result 检测
	 * @throws Exception 异常
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	public ResponseInfo deleteWechatSend(@RequestBody @Valid DeleteDto dels, BindingResult result) 
			throws Exception {
		super.validateBindingResult(result);
		return wechatSendService.deleteWechatSend(dels.getIds());
	}
	
	/**
	 * 群发详情
	 * @Description: 完成
	 * @param id   群发ID
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping("/{id}")
	@MoreSerializeField({
		@SerializeField(clazz = WechatSend.class, includes = { "id", "dateString", "arrays", 
				"appId", "pushName" }),
		})
	public ResponseInfo info(@PathVariable(name = "id") Integer id) 
			throws GlobalException {
		WechatSend send = wechatSendService.findById(id);
		JSONObject json = JSONObject.parseObject(send.getMaterialJson(), JSONObject.class);
		JSONArray array = json.getJSONArray("articles");
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			String thumbMediaUrl = object.getString("thumbMediaUrl");
			if (!StringUtils.isNotBlank(thumbMediaUrl)) {
				//应对微信图文素材没有返回封面图片URL的情况，只返回了封面的ID，需要再查一遍
				String thumbMediaId = object.getString("thumbMediaId");
				WechatMaterial image = wechatMaterialService.getMediaId(thumbMediaId);
				thumbMediaUrl = image.getRequest().getString("url");
				object.put("thumbMediaUrl", thumbMediaUrl);
			}
		}
		send.setArrays(array);
		return new ResponseInfo(send);
	}
}