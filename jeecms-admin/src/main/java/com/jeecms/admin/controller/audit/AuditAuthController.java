/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.audit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.audit.domain.dto.AuditAuthDto;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.ChastityUtil;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.sso.dto.response.SyncResponseBaseVo;
import com.jeecms.system.domain.dto.SendValidateCodeDTO;
import com.jeecms.system.service.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.jeecms.common.exception.error.UserErrorCodeEnum.VALIDATE_CODE_UNTHROUGH;
import static com.jeecms.system.domain.dto.ValidateCodeConstants.*;

/**
 * 检测是否授权控制器
 *
 * @author: ljw
 * @date: 2019年12月23日 下午3:30:32
 */
@RequestMapping("/authorizations")
@RestController
@Validated
public class AuditAuthController {

    @Autowired
    private ChastityUtil chastityUtil;
    @Autowired
    private SmsService smsService;
    @Value("${product.version}")
    private String version;

    /**
     * 检测应用是否授权
     * @return ResponseInfo
     * @throws GlobalException 异常
     */
    @GetMapping
    public ResponseInfo auth() throws GlobalException {
        // 获取该系统AppId
        String appId = chastityUtil.getId();
        Map<String, String> params = new HashMap<String, String>(10);
        params.put("productAppId", appId);
        ResponseInfo response = HttpUtil.getJsonBean(IF_AUTH_SERVER_URL, params, ResponseInfo.class);
        if (response != null && SyncResponseBaseVo.SUCCESS_CODE.equals(response.getCode())) {
            return response;
        } else {
            throw new GlobalException(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR);
        }
    }

    /**
     * 授权登录应用
     * @param dto 传输
     * @return ResponseInfo
     * @throws GlobalException 异常
     */
    @PostMapping
    public ResponseInfo auth(@Validated @RequestBody AuditAuthDto dto) throws GlobalException {
        // 验证手机验证码
        if (StringUtils.isNoneBlank(dto.getMobile()) && StringUtils.isNoneBlank(dto.getCode())) {
            String sessionKey = WebConstants.KCAPTCHA_PREFIX + CODE_SECOND_LEVEL_IDENTITY_USER_LOGIN_PHONE
                    + dto.getMobile();
            int status = smsService.validateCode(sessionKey, dto.getCode());
            if (STATUS_PASS > status) {
                return new ResponseInfo(VALIDATE_CODE_UNTHROUGH.getCode(), VALIDATE_CODE_UNTHROUGH.getDefaultMessage(),
                        false);
            }
        }
        // 获取该系统AppId
        String appId = chastityUtil.getId();
        Map<String, String> params = new HashMap<String, String>(10);
        params.put("productAppId", appId);
        params.put("mobile", dto.getMobile());
        ResponseInfo response = HttpUtil.postJsonBeanForJSON(AUTH_SERVER_URL, null, JSON.toJSONString(params),
                ResponseInfo.class);
        if (response != null && SyncResponseBaseVo.SUCCESS_CODE.equals(response.getCode())) {
            return new ResponseInfo(dto.getMobile());
        } else {
            throw new GlobalException(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR);
        }
    }

    /**
     * 授权登录应用发送短信验证码
     *
     * @Title: sendPhoneMsg
     * @param bean   传输实体
     * @return ResponseInfo
     * @throws GlobalException 异常
     */
    @PostMapping("/sendPhoneMsg")
    public ResponseInfo sendPhoneMsg(@RequestBody SendValidateCodeDTO bean) throws GlobalException {
        bean.setType(CODE_TYPE_MEMBER_LOGIN_PHONE);
        bean.setValidateCode(null);
        return smsService.sendPhoneMsg(bean);
    }

    /**
     * 部署产品信息
     *
     * @return ResponseInfo 响应
     * @throws GlobalException 异常
     * @Title 部署产品信息
     */
    @GetMapping("/product")
    public ResponseInfo product() throws GlobalException {
        JSONObject object = new JSONObject();
        // 获取该系统AppId
        String appId = chastityUtil.getId();
		chastityUtil.getIdKey();
        object.put("version", version);
        object.put("productAppId", appId);
        return new ResponseInfo(object);
    }

    /** 请求云平台是否授权 Get **/
    private static final String IF_AUTH_SERVER_URL = "http://api.jeecms.com/MODULE-APP/client/v1/userClient";
    /** 授权 Post **/
    private static final String AUTH_SERVER_URL = "http://api.jeecms.com/MODULE-APP/client/v1/userClient";

}