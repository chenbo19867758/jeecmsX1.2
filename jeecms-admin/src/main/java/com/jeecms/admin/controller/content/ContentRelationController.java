/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.content;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.PageableUtil;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentRelation;
import com.jeecms.content.domain.dto.ContentRelationDto;
import com.jeecms.content.domain.dto.ContentRelationDto.ContentRelationSave;
import com.jeecms.content.domain.dto.ContentRelationDto.ContentRelationSort;
import com.jeecms.content.service.ContentRelationService;
import com.jeecms.content.service.ContentService;
import com.jeecms.system.domain.CmsDataPerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容相关内容service实现类
 * 
 * @author: chenming
 * @date: 2019年6月21日 下午4:43:45
 */
@RequestMapping("/contentrelation")
@RestController
public class ContentRelationController extends BaseController<ContentRelation, Integer> {

	@Autowired
	private ContentRelationService service;
	@Autowired
	private ContentService contentService;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}

	/**
	 * 新增
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Validated(value = ContentRelationSave.class) ContentRelationDto dto)
			throws GlobalException {
		service.save(dto);
		return new ResponseInfo();
	}

	/**
	 * 列表分页
	 */
	@MoreSerializeField({ @SerializeField(clazz = ContentRelation.class, includes = { "id", "relationContent" }),
			@SerializeField(clazz = Content.class, includes = { "channel", "title" }),
			@SerializeField(clazz = Channel.class, includes = { "name" }) })
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseInfo page(HttpServletRequest request, @RequestParam Integer contentId, Pageable pageable)
			throws GlobalException {
		Map<String, String[]> params = super.getCommonParams(request);
		params.put("EQ_contentId_Integer", new String[] { String.valueOf(contentId) });
		List<Order> orders = new ArrayList<Sort.Order>(3);
		orders.add(new Order(Direction.ASC, "sortNum"));
		orders.add(new Order(Direction.DESC, "sortWeight"));
		orders.add(new Order(Direction.DESC, "createTime"));
		return new ResponseInfo(service.getPage(params, PageableUtil.by(pageable, orders), false));
	}

	/**
	 * 排序
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public ResponseInfo sort(@RequestBody @Validated(value = ContentRelationSort.class) ContentRelationDto dto,
			HttpServletRequest request) throws GlobalException {
		service.sort(dto);
		return new ResponseInfo(true);
	}

	/**
	 * 删除
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dto, BindingResult result) throws GlobalException {
		Integer[] ids = dto.getIds();
		List<ContentRelation> contentRelations = service.findAllById(Arrays.asList(ids));
		if (contentRelations == null || contentRelations.size() == 0) {
			return new ResponseInfo(false);
		}
		List<Integer> contentIds = contentRelations.stream().map(ContentRelation::getContentId)
				.collect(Collectors.toList());
		if (contentIds.size() > 1) {
			return new ResponseInfo(false);
		}
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_EDIT, contentService.findById(contentIds.get(0)).getChannelId())) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		service.delete(contentRelations);
		return new ResponseInfo(true);
	}

}
