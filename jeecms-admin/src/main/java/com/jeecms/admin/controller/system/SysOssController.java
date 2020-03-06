/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysOss;
import com.jeecms.system.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * oss云存储控制层
 *
 * @author: wulongwei
 * @date: 2019年4月9日 下午2:00:04
 */
@RestController
@RequestMapping("/sysOss")
public class SysOssController extends BaseController<SysOss, Integer> {

        @PostConstruct
        public void init() {
                String[] queryParams = {"ossType_EQ_Integer", "ossName_LIKE"};
                super.setQueryParams(queryParams);
        }

        /**
         * 获取云存储详细信息
         */
        @GetMapping(value = "/{id}")
        @SerializeField(clazz = SysOss.class, includes = {"id", "appId", "secretId", "appKey",
                "bucketName", "bucketArea", "ossType","ossTypeString", "endPoint", "accessDomain", "ossName"})
        @Override
        public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
                return super.get(id);
        }

        /**
         * 获取云存储分页信息
         *
         * @param request
         * @param pageable
         * @return
         * @throws GlobalException
         * @Title: page
         * @return: ResponseInfo
         */
        @GetMapping("/page")
        @SerializeField(clazz = SysOss.class, includes = {"id", "ossName", "ossType"})
        public ResponseInfo page(HttpServletRequest request,
                                 @PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) throws GlobalException {
                return super.getPage(request, pageable, false);
        }

        /**
         * 获取云存储分页信息
         *
         * @param request
         * @return
         * @throws GlobalException
         * @Title: page
         * @return: ResponseInfo
         */
        @GetMapping("/list")
        @SerializeField(clazz = SysOss.class, includes = {"id", "ossName", "ossType"})
        public ResponseInfo list(HttpServletRequest request) throws GlobalException {
                return super.getList(request, null, false);
        }

        /**
         * 删除一个或者多个云存储信息
         */
        @DeleteMapping
        @Override
        public ResponseInfo delete(@RequestBody @Valid DeleteDto dels, BindingResult result) throws GlobalException {
                super.validateBindingResult(result);
                return super.deleteBeatch(dels.getIds());
        }

        /**
         * 修改云存储信息
         */
        @PutMapping
        @Override
        public ResponseInfo update(@RequestBody @Valid SysOss sysOss, BindingResult result) throws GlobalException {
                super.validateBindingResult(result);
                ossService.updateSysOss(sysOss);
                return new ResponseInfo();
        }

        /**
         * 添加云存储信息
         */
        @PostMapping
        @Override
        public ResponseInfo save(@RequestBody @Valid SysOss sysOss, BindingResult result) throws GlobalException {
                super.validateBindingResult(result);
                ossService.saveSysOss(sysOss);
                return new ResponseInfo();
        }

        @Autowired
        private SysOssService ossService;
}
