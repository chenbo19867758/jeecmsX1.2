/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.IllegalParamExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysLinkType;
import com.jeecms.system.service.SysLinkTypeService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import java.util.Map;

/**
 * 友情链接分类Controller
 *
 * @author: xiaohui
 * @version: 1.0
 * @date 2019-06-10
 */
@RequestMapping("/linkTypes")
@RestController
public class SysLinkTypeController extends BaseAdminController<SysLinkType, Integer> {

        @Autowired
        private SysLinkTypeService service;

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
        @SerializeField(clazz = SysLinkType.class, includes = {"id", "typeName", "sortNum"})
        public ResponseInfo page(HttpServletRequest request, @PageableDefault(sort = "sortNum",
                direction = Direction.ASC) Pageable pageable) throws GlobalException {
                Map<String, String[]> params = getCommonParams(request);
                params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
                return super.getPage(request, pageable, false);
        }

        /**
         * 列表
         *
         * @param request {@link HttpServletRequest}
         * @return ResponseInfo
         */
        @GetMapping("/list")
        @SerializeField(clazz = SysLinkType.class, includes = {"id", "typeName"})
        public ResponseInfo list(HttpServletRequest request) {
                Map<String, String[]> params = getCommonParams(request);
                params.put("EQ_siteId_Integer", new String[]{SystemContextUtils.getSiteId(request).toString()});
                return new ResponseInfo(service.getList(params, null, false));
        }

        /**
         * @Title: 获取详情
         * @param: @param id
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @GetMapping(value = "/{id}")
        @Override
        @SerializeField(clazz = SysLinkType.class, includes = {"id", "typeName"})
        public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
                return super.get(id);
        }

        /**
         * 校验分类名称是否唯一
         *
         * @param typeName 分类名称id
         * @param id       分类id
         * @param request  {@link HttpServletRequest}
         * @return ResponseInfo
         */
        @GetMapping("/typeName/unique")
        public ResponseInfo checkByTypeName(String typeName, Integer id, HttpServletRequest request) {
                return new ResponseInfo(service.checkByTypeName(typeName, id, SystemContextUtils.getSiteId(request)));
        }

        /**
         * 添加
         *
         * @Title: 添加
         * @param: @param result
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @PostMapping()
        public ResponseInfo save(@RequestBody @Valid SysLinkType sysLinkType, HttpServletRequest request,
                                 BindingResult result) throws GlobalException {
                validateBindingResult(result);
                Integer siteId = SystemContextUtils.getSiteId(request);
                if (!service.checkByTypeName(sysLinkType.getTypeName(), null, siteId)) {
                        throw new GlobalException(new IllegalParamExceptionInfo(
                                SysOtherErrorCodeEnum.LINK_TYPE_NAME_ALREADY_EXIST.getCode(),
                                SysOtherErrorCodeEnum.LINK_TYPE_NAME_ALREADY_EXIST.getDefaultMessage()
                        ));
                }
                service.save(sysLinkType.getTypeName(), siteId);
                return new ResponseInfo();
        }


        /**
         * 修改
         *
         * @Title: 修改
         * @param: @param result
         * @param: @throws GlobalException
         * @return: ResponseInfo
         */
        @PutMapping()
        public ResponseInfo update(@RequestBody @Valid SysLinkType sysLinkType, HttpServletRequest request,
                                   BindingResult result) throws GlobalException {
                Integer id = sysLinkType.getId();
                validateId(id);
                validateBindingResult(result);
                Integer siteId = SystemContextUtils.getSiteId(request);
                if (!service.checkByTypeName(sysLinkType.getTypeName(), id, siteId)) {
                        throw new GlobalException(new IllegalParamExceptionInfo(
                                SysOtherErrorCodeEnum.LINK_TYPE_NAME_ALREADY_EXIST.getCode(),
                                SysOtherErrorCodeEnum.LINK_TYPE_NAME_ALREADY_EXIST.getDefaultMessage()
                        ));
                }
                service.update(id, sysLinkType.getTypeName(), siteId);
                return new ResponseInfo();
        }

        /**
         * @Title: 排序
         * @param: @param sort
         * @param: @return
         * @return: ResponseInfo
         */
        @PutMapping(value = "/sort")
        public ResponseInfo sort(@RequestBody @Valid DragSortDto sort, BindingResult result) throws GlobalException {
                validateBindingResult(result);
                service.dragSort(sort);
                return new ResponseInfo();
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
                return super.deleteBeatch(dels.getIds());
        }
}



