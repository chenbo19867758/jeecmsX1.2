/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.SysIptables;
import com.jeecms.system.domain.dto.SysIptablesDto;
import com.jeecms.system.service.SysIptablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 防火墙配置Controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-13
 */
@RequestMapping("/iptables")
@RestController
public class SysIptablesController extends BaseController<SysIptables, Integer> {

        @Autowired
        private SysIptablesService service;

        /**
         * 获取详情
         *
         * @return ResponseInfo
         * @throws GlobalException 异常
         */
        @GetMapping()
        @SerializeField(clazz = SysIptables.class, includes = {"id", "isEnable", "limitInNetworkModel",
                "inNetworkIpJson", "limitDomain", "allowLoginHours", "allowLoginWeek"})
        public ResponseInfo get() throws GlobalException {
                List<SysIptables> list = service.findAll(true);
                SysIptables bean = null;
                if (list != null) {
                        bean = list.get(0);
                }
                return new ResponseInfo(bean);
        }

        /**
         * 修改详情
         *
         * @param dto     {@link SysIptablesDto}
         * @param request {@link HttpServletRequest}
         * @param result  {@link BindingResult}
         * @return ResponseInfo
         * @throws GlobalException 全局异常
         */
        @PutMapping()
        public ResponseInfo update(@RequestBody @Valid SysIptablesDto dto, HttpServletRequest request,
                                   BindingResult result) throws GlobalException {
                validateBindingResult(result);
                service.update(dto, request);
                return new ResponseInfo();
        }
}



