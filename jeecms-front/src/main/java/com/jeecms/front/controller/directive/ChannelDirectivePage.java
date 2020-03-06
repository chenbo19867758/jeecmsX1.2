/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/25 16:23
 */
public class ChannelDirectivePage extends ChannelDirectiveAbstract {
    /**
     * 模板名称
     */
    public static final String TPL_NAME = "cms_channel_page";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        Integer siteId = getSiteId(params);
        Integer parentId = getParentId(params);
        Boolean display = getDisplay(params);
        int pageNo = FrontUtils.getPageNo(env);
        int count = FrontUtils.getCount(params);
        if (siteId == null) {
            siteId = FrontUtils.getSite(env).getId();
        }
        if (display == null) {
            display = false;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, count);
        Page<Channel> channels = channelService.findPageByParentId(siteId, parentId == null ? 0 : parentId,
                false, display, pageable);
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put(OUT_PAGINATION, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(channels));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        body.render(env.getOut());
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
    }

    @Autowired
    private ChannelService channelService;
}
