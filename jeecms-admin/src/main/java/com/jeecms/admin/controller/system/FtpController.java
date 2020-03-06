/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.system.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Description:ftp管理
 * @author: wlw
 * @date:
 */
@RestController
@RequestMapping("/ftp")
public class FtpController extends BaseController<UploadFtp, Integer> {

        @PostConstruct
        public void init() {
                String[] queryParams = {"ftpName_LIKE"};
                super.setQueryParams(queryParams);
        }

        /**
         * 分页查询
         *
         * @param request  HttpServletRequest
         * @param pageable 分页组件
         * @return ResponseInfo
         */
        @GetMapping("/page")
        @MoreSerializeField({@SerializeField(clazz = UploadFtp.class,
                includes = {"id", "ftpName", "ip", "ftpPath", "encoding", "url"})})
        public ResponseInfo page(HttpServletRequest request, @PageableDefault(sort = "id",
                direction = Direction.DESC) Pageable pageable) throws GlobalException {
                return super.getPage(request, pageable, false);
        }

        /**
         * 列表
         *
         * @param request   HttpServletRequest
         * @param paginable 分页组件
         * @return ResponseInfo
         * @throws GlobalException 异常
         */
        @RequestMapping(value = "/list")
        @MoreSerializeField({@SerializeField(clazz = UploadFtp.class,
                includes = {"id", "ftpName", "ip", "ftpPath", "encoding", "url"})})
        public ResponseInfo list(HttpServletRequest request, Paginable paginable) throws GlobalException {
                return super.getList(request, paginable, true);
        }

        /**
         * 通过ID删除对象
         */
        @DeleteMapping
        public ResponseInfo deleteIds(@RequestBody @Valid DeleteDto details) throws GlobalException {
                Integer[] ids = details.getIds();
                return super.deleteBeatch(ids);
        }

        /**
         * 获取单个ftp信息
         */
        @Override
        @GetMapping(value = "/{id}")
        @MoreSerializeField({@SerializeField(clazz = UploadFtp.class, includes = {"id", "ftpName",
                "ip", "ftpPath", "encoding", "url", "username", "password", "timeout", "port"})})
        public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
                return super.get(id);
        }

        /**
         * 修改ftp信息
         *
         * @param ftp
         * @param response
         * @param result
         * @return
         * @throws GlobalException
         * @Title: update
         * @return: ResponseInfo
         */
        @PutMapping
        public ResponseInfo update(@RequestBody UploadFtp ftp, HttpServletResponse response, BindingResult result)
                throws GlobalException {
                ftpService.updateFtpInfo(ftp);
                return new ResponseInfo();
        }

        /**
         * 添加ftp信息
         */
        @Override
        @PostMapping
        public ResponseInfo save(@RequestBody @Valid UploadFtp ftp, BindingResult result) throws GlobalException {
                ftpService.saveFtpInfo(ftp);
                return new ResponseInfo();
        }

        @Autowired
        private FtpService ftpService;
}
