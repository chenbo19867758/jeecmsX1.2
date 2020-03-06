/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.content;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.dto.ChannelBatchDto;
import com.jeecms.channel.domain.dto.ChannelStaticOpDto;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.constants.ContentConstant.ChannelStaticOperator;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.vo.PageProcessResult;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.ContentStaticPageService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 静态化控制器
 * 
 * @author: tom
 * @date: 2019年6月10日 下午6:08:31
 */
@RequestMapping("/content/staticPage")
@RestController
public class ContentStaticPageController extends BaseAdminController<Content, Integer> {

	/**
	 * 生成静态化首页
	 * 
	 * @Title: index
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/index")
	public ResponseInfo index(Integer siteId, HttpServletRequest request) throws GlobalException {
		CmsSite site = null;
		if (siteId != null) {
			site = siteService.findById(siteId);
		}
		if (site == null) {
			site = SystemContextUtils.getSite(request);
		}
		staticPageService.index(site, false);
		return new ResponseInfo();
	}

	/**
	 * 删除静态化首页
	 * 
	 * @Title: indexDelete
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping(value = "/index")
	public ResponseInfo indexDelete(Integer siteId, HttpServletRequest request) throws GlobalException {
		CmsSite site = null;
		if (siteId != null) {
			site = siteService.findById(siteId);
		}
		if (site == null) {
			site = SystemContextUtils.getSite(request);
		}
		staticPageService.deleteIndex(site);
		return new ResponseInfo();
	}

	/**
	 * 生成静态化栏目页-批量
	 * 
	 * @Title: channel
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/channelBatch")
	public ResponseInfo channel(@RequestBody @Valid ChannelBatchDto dto,
			HttpServletRequest request, HttpServletResponse response, BindingResult result) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		if(dto!=null){
			staticPageService.channel(site.getId(),dto.getChannelIds(), false, request, response);
		}
		return new ResponseInfo();
	}

	/**
	 * 栏目静态化
	 * 
	 * @Title: channelSingle
	 * @param channelId
	 *            栏目ID
	 * @param op 逗号拼接字符串
	 *            ChannelStaticOperator channel 生成栏目静态页 channelChild 生成子栏目静态页
	 *            channelContent 生成内容静态页
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PutMapping(value = "/channel")
	public ResponseInfo channel(@RequestBody ChannelStaticOpDto dto, HttpServletRequest request, HttpServletResponse response)
			throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		Channel channel = null;
		if (dto.getChannelId() != null) {
			channel = channelService.findById(dto.getChannelId() );
			List<ChannelStaticOperator> enumOps = new ArrayList<ChannelStaticOperator>();
			if (StringUtils.isNotBlank(dto.getOp())) {
				String[] opArr = dto.getOp().split(",");
				for (String opStr : opArr) {
					enumOps.add(ChannelStaticOperator.getValueOf(opStr));
				}
			}
			for (ChannelStaticOperator opEnum : enumOps) {
				if (ChannelStaticOperator.channel.equals(opEnum)) {
					staticPageService.channel(channel, true, false, request, response);
				} else if (ChannelStaticOperator.channelChild.equals(opEnum)) {
					List<Integer> channelIds = channel.getChildAllIds();
					staticPageService.channel(site.getId(), channelIds, false, request, response);
				} else if (ChannelStaticOperator.channelContent.equals(opEnum)) {
					List<Integer> channelIds = channel.getChildAllIds();
					staticPageService.content(site.getId(), channelIds, false, request, response);
				}
			}
		}
		return new ResponseInfo();
	}

	/**
	 * 查询栏目静态化进度
	 * 
	 * @Title: getChannelProcess
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/channel/process")
	public ResponseInfo getChannelProcess(HttpServletRequest request, HttpServletResponse response)
			throws GlobalException {
		PageProcessResult process = staticPageService.getChannelPageProcessResult();
		return new ResponseInfo(process);
	}

	/**
	 * 删除静态化栏目页
	 * 
	 * @Title: channelDelete
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping(value = "/channel")
	public ResponseInfo channelDelete(@RequestBody @Valid ChannelBatchDto dto,
			HttpServletRequest request, HttpServletResponse response, BindingResult result) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		if(dto!=null){
			staticPageService.deleteChannel(site.getId(), dto.getChannelIds(), false, request, response);
		}
		return new ResponseInfo();
	}

	/**
	 * 生成静态化内容页-批量
	 * 
	 * @Title: channel
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/contentBatch")
	public ResponseInfo contentBatch(@RequestBody @Valid ChannelBatchDto dto,
			HttpServletRequest request, HttpServletResponse response, BindingResult result) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		if(dto!=null){
			staticPageService.content(site.getId(), dto.getChannelIds(), false, request, response);
		}
		return new ResponseInfo();
	}

	/**
	 * 生成静态化内容页
	 * 
	 * @Title: channel
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping(value = "/content")
	public ResponseInfo content(Integer contentId, HttpServletRequest request, HttpServletResponse response)
			throws GlobalException {
		if (contentId != null) {
			staticPageService.contentRelated(contentService.findById(contentId), false, request, response);
		}
		return new ResponseInfo();
	}

	/**
	 * 删除静态化内容页-批量
	 * 
	 * @Title: channel
	 * @param request
	 *            HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@DeleteMapping(value = "/content")
	public ResponseInfo contentDelete(@RequestBody @Valid ChannelBatchDto dto,
			HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		CmsSite site = SystemContextUtils.getSite(request);
		if(dto.getChannelIds()!=null){
			staticPageService.deleteContent(site.getId(), dto.getChannelIds(), request, response);
		}
		return new ResponseInfo();
	}

	/**
	 * 查询内容静态化进度
	 * 
	 * @Title: getChannelProcess
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/content/process")
	public ResponseInfo getContentProcess(HttpServletRequest request, HttpServletResponse response)
			throws GlobalException {
		PageProcessResult process = staticPageService.getContentPageProcessResult();
		return new ResponseInfo(process);
	}

	// 生成完成和生成过程中标注状态栏目状态，还需要完成发布内容自动静态化和撤销

	@Autowired
	private ContentService contentService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ContentStaticPageService staticPageService;
	@Autowired
	private CmsSiteService siteService;

}
