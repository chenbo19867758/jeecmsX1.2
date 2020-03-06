/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.ContentTag;
import com.jeecms.system.service.ContentTagService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * tag词controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-04-27
 */
@RequestMapping("/contentTags")
@RestController
public class ContentTagController extends BaseAdminController<ContentTag, Integer> {

        @Autowired
        private ContentTagService service;

        @PostConstruct
        public void init() {
                String[] queryParams = {};
                super.setQueryParams(queryParams);
        }


        /**
         * @Title: 列表分页
         * @param: @param request
         * @param: @param pageable
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @GetMapping(value = "/page")
        @SerializeField(clazz = ContentTag.class, includes = {"id", "tagName", "refCounter", "createTime"})
        public ResponseInfo page(HttpServletRequest request,
                                 @PageableDefault(sort = "refCounter", direction = Direction.DESC) Pageable pageable) throws GlobalException {
                Map<String, String[]> params = new HashMap<String, String[]>(2);
                if (SystemContextUtils.getSiteId(request) != null) {
                        params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
                }
                String tagName = request.getParameter("tagName");
                params.put("LIKE_tagName_String", new String[]{tagName});
                Sort sort = pageable.getSort().and(new Sort(Sort.Direction.DESC, "id"));
                Pageable page = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
                return new ResponseInfo(service.getPage(params, page, false));
        }

        /**
         * @Title: 获取详情
         * @param: @param id
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @GetMapping(value = "/{id}")
        @SerializeField(clazz = ContentTag.class, includes = {"id", "tagName", "refCounter"})
        @Override
        public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
                return super.get(id);
        }

        /**
         * @Title: 添加
         * @param: @param result
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @PostMapping()
        public ResponseInfo save(@RequestBody @Valid ContentTag contentTag,
                                 HttpServletRequest request, BindingResult result) throws GlobalException {
                validateBindingResult(result);
                service.saveBatch(contentTag, SystemContextUtils.getSiteId(request));
                return new ResponseInfo();
        }

        /**
         * 校验tag名是否适用
         *
         * @param tagName tag名称
         * @param id      tag词id
         * @param request HttpServletRequest
         * @return
         */
        @GetMapping("/tagName/unique")
        public ResponseInfo check(String tagName, Integer id, HttpServletRequest request) {
                boolean flag = service.checkTagName(tagName, id, SystemContextUtils.getSiteId(request));
                return new ResponseInfo(flag);
        }


        /**
         * @Title: 修改
         * @param: @param result
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @PutMapping()
        public ResponseInfo update(@RequestBody @Valid ContentTag contentTag, BindingResult result,
                                   HttpServletRequest request) throws GlobalException {
                validateBindingResult(result);
                if (!service.checkTagName(contentTag.getTagName(), contentTag.getId(),
                        SystemContextUtils.getSiteId(request))) {
                        return new ResponseInfo(SysOtherErrorCodeEnum.TAG_WORD_ALREADY_EXIST.getCode(),
                                SysOtherErrorCodeEnum.TAG_WORD_ALREADY_EXIST.getDefaultMessage());
                }
                return super.update(contentTag, result);
        }

        /**
         * @Title: 删除
         * @param: @param ids
         * @param: @return
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @DeleteMapping()
        public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
                return super.physicalDelete(dels.getIds());
        }
}



