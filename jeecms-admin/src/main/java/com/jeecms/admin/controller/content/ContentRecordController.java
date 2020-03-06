package com.jeecms.admin.controller.content;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentRecord;
import com.jeecms.content.service.ContentRecordService;
import com.jeecms.content.service.ContentService;
import com.jeecms.system.domain.CmsDataPerm;

/**
 * 操作记录controller层
 * 
 * @author: chenming
 * @date: 2019年6月24日 下午4:06:23
 */
@RequestMapping("/contentrecord")
@RestController
public class ContentRecordController extends BaseController<ContentRecord, Integer> {

	@Autowired
	private ContentRecordService service;
	@Autowired
	private ContentService contentService;

	/**
	 * 分页查询
	 */
	@MoreSerializeField({ @SerializeField(clazz = ContentRecord.class, includes = { "operateType", "opreateRemark",
			"userName", "createTime" }), })
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseInfo page(HttpServletRequest request, @RequestParam Integer contentId,
			@RequestParam(required = false) Date startTime, @RequestParam(required = false) Date endTime,
			@RequestParam(required = false) String userName, Pageable pageable) throws GlobalException {
		Content content = contentService.findById(contentId);
		if (content == null) {
			return new ResponseInfo();
		}
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_CREATE, content.getChannelId())) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		return new ResponseInfo(service.getPage(contentId, startTime, endTime, userName, pageable));
	}
}
