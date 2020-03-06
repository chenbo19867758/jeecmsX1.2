/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.admin.controller.wechat;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatComment;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.WechatFansExt;
import com.jeecms.wechat.domain.WechatSendArticle;
import com.jeecms.wechat.domain.dto.WechatCommentDto;
import com.jeecms.wechat.domain.vo.ArticleVO;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatCommentService;
import com.jeecms.wechat.service.WechatSendArticleService;
import com.sun.istack.NotNull;

/**   
 * 微信留言控制器
 * @author: ljw
 * @date:   2019年5月29日 上午9:40:13     
 */
@RequestMapping("/wechatcomment")
@RestController
public class WechatCommentController extends BaseController<WechatComment, Integer> {

	@Autowired
	private WechatCommentService  wechatCommentService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	@Autowired
	private WechatSendArticleService wechatSendArticleService;
	
	/**
	 * 开启留言设置 
	 * @Title: markelect
	 * @param appId 公众号ID
	 * @param msgDataId 群发ID
	 * @param msgDataIndex 文章序号
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "/on")
	public ResponseInfo on(@NotNull String appId, 
			@NotNull Long msgDataId, @NotNull Long msgDataIndex) throws GlobalException {
		return wechatCommentService.on(appId,msgDataId,msgDataIndex);
	}
	
	/**
	 * 关闭留言设置 
	 * @Title: markelect
	 * @param appId 公众号ID
	 * @param msgDataId 群发ID
	 * @param msgDataIndex 文章序号
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "/off")
	public ResponseInfo off(@NotNull String appId, 
			@NotNull Long msgDataId, @NotNull Long msgDataIndex) throws GlobalException {
		return wechatCommentService.off(appId,msgDataId,msgDataIndex);
	}
	
	/**
	 * 评论列表分页   
	* @Title: page 
	* @param request 请求
	* @param appId 公众号APPID
	* @param msgDataId 群发ID
	* @param msgDataIndex 文章序号
	* @param commentType 是否精选评论
	* @param startTime 评论开始时间
	* @param endTime 评论结束时间
	* @param orderType 排序方式
	* @param comment 留言内容
	* @param pageable 分页
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/page")
	@MoreSerializeField({
			@SerializeField(clazz = WechatComment.class, includes = { "id", "content", "commentType", 
					"commentTime", "replyContent", "replyTime", "wechatFans", "commentType" }),
			@SerializeField(clazz = WechatFans.class, includes = { "id", "username", "subscribeTime", 
					"subscribeTimes", "sex", "tagidList",
					"headimgurl", "nickname", "openid", "fansExt", }),
			@SerializeField(clazz = WechatFansExt.class, includes = { "commentCount", "topCommentCount",
					"mesCount" }), })
	public ResponseInfo page(HttpServletRequest request,
			@NotNull String appId, @NotNull String msgDataId, 
			@NotNull Integer msgDataIndex, Boolean commentType, 
			Date startTime, Date endTime, Integer orderType,
			String comment, Pageable pageable) throws GlobalException {	
		//APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),false);
		}
		List<String> appids = new ArrayList<String>(10);
		//公众号appid必传
		appids.add(appId);
		Page<WechatComment> page = wechatCommentService.getPages(appids, commentType, startTime, endTime, 
				orderType, comment, msgDataId,msgDataIndex,pageable);
		wechatSendArticleService.read(msgDataId, msgDataIndex);
		return new ResponseInfo(page);
	}
	
	/**
	 * 设为精选 
	 * @Title: markelect
	 * @param id 留言ID
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "/markelect")
	public ResponseInfo markelect(@NotNull Integer id, @NotNull Integer fansId)
			throws GlobalException {
		return wechatCommentService.markelect(id,fansId);
	}
	
	/**
	 * 取消精选 
	 * @Title: unmarkelect
	 * @param id 留言ID
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "/unmarkelect")
	public ResponseInfo unmarkelect(@NotNull Integer id, @NotNull Integer fansId) 
			throws GlobalException {
		return wechatCommentService.unmarkelect(id,fansId);
	}
	
	/**
	 * 回复留言 
	 * @Title: reply
	 * @param dto 留言Dto
	 * @throws GlobalException 异常
	 * @return
	 */
	@PostMapping(value = "/reply")
	public ResponseInfo reply(@RequestBody WechatCommentDto dto) throws GlobalException {
		return wechatCommentService.reply(dto);
	}
	
	/**
	 * 删除回复
	 * @Title: deleteComment
	 * @param dto 留言dto
	 * @throws GlobalException 异常
	 * @return
	 */
	@DeleteMapping(value = "/reply")
	public ResponseInfo deleteReply(@RequestBody WechatCommentDto dto) throws GlobalException {
		return wechatCommentService.deleteReply(dto.getId());
	}
	
	/**
	 * 删除评论
	 * @Title: deleteComment
	 * @param dto 留言dto
	 * @throws GlobalException 异常
	 * @return
	 */
	@DeleteMapping()
	public ResponseInfo deleteComment(@RequestBody WechatCommentDto dto) throws GlobalException {
		return wechatCommentService.deleteComment(dto.getId());
	}
	
	/**
	 * 群发文章列表(公众号)
	 * @Title: articles
	 * @param appId 微信公众号ID
	 * @param year 年份 
	 * @param month 月份
	 * @param comment true 有留言的文章，false 全部文章
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "articles")
	@MoreSerializeField({ 
		@SerializeField(clazz = ArticleVO.class, includes = {"article", "unread", "comments"}),
		@SerializeField(clazz = WechatSendArticle.class, includes = { "id", "msgDataId", 
				"msgDataIndex", "title",
				"createTime", "appId", "open" }), 
	})
	public ResponseInfo articles(HttpServletRequest request,
			@NotNull String appId, @NotNull Integer year, @NotNull Integer month, 
			@RequestParam(name = "comment",defaultValue = "false") Boolean comment) throws GlobalException {
		// 本月的第一天
		LocalDate firstday = LocalDate.of(year, month, 1);
		// 本月的最后一天
		LocalDate lastDay = firstday.with(TemporalAdjusters.lastDayOfMonth());
		Date start = MyDateUtils.parseDate(firstday.toString());
		Date end = MyDateUtils.getFinallyDate(MyDateUtils.parseDate(lastDay.toString()));
		//APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),false);
		}
		List<WechatSendArticle> articles = wechatSendArticleService.getArticles(Arrays.asList(appId), 
				start, end);
		//留言倒序
		articles = articles.stream().sorted(Comparator.comparing(WechatSendArticle::getCreateTime).reversed())
			.collect(Collectors.toList());
		//如果有留言的文章
		if (comment) {
			articles = articles.stream().filter(x ->
			x.getMaxUserCommentId() > 0)
			.collect(Collectors.toList());
		}
		List<ArticleVO> vos = new ArrayList<ArticleVO>(10);
		for (WechatSendArticle wechatSendArticle : articles) {
			//判断角标
			Integer unread = 0;
			ArticleVO vo = new ArticleVO();
			//查询文章的最大评论数，与之前的作对比，得出未读数量
			Long userCommentId = wechatCommentService.getUserCommentId(wechatSendArticle.getMsgDataId(),
					wechatSendArticle.getMsgDataIndex());
			if (userCommentId == null) {
				userCommentId = 0L;
			}
			if (userCommentId > wechatSendArticle.getMaxUserCommentId()) {
				unread =  userCommentId.intValue() - wechatSendArticle.getMaxUserCommentId();
			}
			vo.setArticle(wechatSendArticle);
			vo.setUnread(unread);
			vo.setComments(userCommentId.intValue());
			vos.add(vo);
		}
		vos = vos.stream().sorted(Comparator.comparing(ArticleVO::getUnread).reversed())
				.collect(Collectors.toList());
		return new ResponseInfo(vos);
	}
	
	/**
	 * 群发文章列表(汇总)
	 * @Title: articlesCollect
	 * @param appId 微信公众号ID
	 * @param year 年份 
	 * @param month 月份
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "articles/collect")
	@MoreSerializeField({ 
		@SerializeField(clazz = ArticleVO.class, includes = {"article", "unread"}),
		@SerializeField(clazz = WechatSendArticle.class, includes = { "id", 
				"msgDataId", "msgDataIndex", "title",
				"createTime", "appId",}), 
	})
	public ResponseInfo articlesCollect(HttpServletRequest request,
			@NotNull String appId,@NotNull Integer year, @NotNull Integer month,
			@RequestParam(name = "comment",defaultValue = "false") Boolean comment) throws GlobalException {
		// 本月的第一天
		LocalDate firstday = LocalDate.of(year, month, 1);
		// 本月的最后一天
		LocalDate lastDay = firstday.with(TemporalAdjusters.lastDayOfMonth());
		Date start = MyDateUtils.parseDate(firstday.toString());
		Date end = MyDateUtils.parseDate(lastDay.toString());
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> appids = new ArrayList<String>(10);
		//如果公众号为空，查询该站点下的appid
		if (StringUtils.isNotBlank(appId)) {
			appids.add(appId);
		} else {
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			}
		}
		//站点没有APPID，直接返回
		if (appids.isEmpty()) {
			return new ResponseInfo();
		}
		List<WechatSendArticle> articles = wechatSendArticleService.getArticles(appids, start, end);
		//如果有留言的文章
		if (comment) {
			articles = articles.stream().filter(x ->
			x.getMaxUserCommentId() > 0)
			.collect(Collectors.toList());
		}
		List<ArticleVO> vos = new ArrayList<ArticleVO>(10);
		//判断角标
		Integer unread = 0;
		for (WechatSendArticle wechatSendArticle : articles) {
			ArticleVO vo = new ArticleVO();
			//查询文章的最大评论数，与之前的作对比，得出未读数量
			Long userCommentId = wechatCommentService.getUserCommentId(wechatSendArticle.getMsgDataId(),
					wechatSendArticle.getMsgDataIndex());
			if (userCommentId == null) {
				userCommentId = 0L;
			}
			if (userCommentId > wechatSendArticle.getMaxUserCommentId()) {
				unread =  userCommentId.intValue() - wechatSendArticle.getMaxUserCommentId();
			}
			vo.setArticle(wechatSendArticle);
			vo.setUnread(unread);
			vo.setComments(userCommentId.intValue());
			vos.add(vo);
		}
		return new ResponseInfo(vos);
	}
	
	/**
	 * 手动同步
	 * @Title: hand
	 * @param appId 公众号ID
	 * @param year 年份 
	 * @param month 月份
	 * @throws GlobalException 异常
	 * @return
	 */
	@GetMapping(value = "/hand")
	public ResponseInfo hand(HttpServletRequest request,@NotNull String appId, 
			@NotNull Integer year,@NotNull Integer month) 
			throws GlobalException {
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
		Date end = MyDateUtils.getFinallyDate(MyDateUtils.parseDate(lastDay.toString()));
		wechatCommentService.hand(Arrays.asList(appId),start,end);
		return new ResponseInfo();
	}
	
}
