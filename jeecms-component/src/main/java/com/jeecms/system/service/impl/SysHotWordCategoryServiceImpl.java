/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.SysHotWordCategoryDao;
import com.jeecms.system.domain.SysHotWordCategory;
import com.jeecms.system.domain.dto.HotWordCategoryDto;
import com.jeecms.system.service.SysHotWordCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 热词Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysHotWordCategoryServiceImpl extends BaseServiceImpl<SysHotWordCategory, SysHotWordCategoryDao, Integer> implements SysHotWordCategoryService {

        @Autowired
        private ChannelService channelService;

        @Override
        public SysHotWordCategory save(HotWordCategoryDto dto, Integer siteId) throws GlobalException {
                SysHotWordCategory bean = new SysHotWordCategory();
                bean.setSiteId(siteId);
                bean.setApplyScope(dto.getApplyScope());
                bean.setCateName(dto.getCateName());
                //如果是指定栏目
                if (SysHotWordCategory.CATEGORY_RANGE_CHANNEL.equals(dto.getApplyScope())) {
                        bean.setChannels(getChannels(dto.getChannelId()));
                }
                return super.save(bean);
        }

        @Override
        public SysHotWordCategory update(HotWordCategoryDto dto) throws GlobalException {
                SysHotWordCategory bean = findById(dto.getId());
                bean.setCateName(dto.getCateName());
                bean.setApplyScope(dto.getApplyScope());
                //先清空栏目信息
                bean.getRange();
                bean.getChannels().clear();
                if (SysHotWordCategory.CATEGORY_RANGE_CHANNEL.equals(dto.getApplyScope())) {
                        bean.setChannels(getChannels(dto.getChannelId()));
                }
                return super.update(bean);
        }

        @Override
        public boolean checkByCateName(String cateName, Integer id) {
                if (StringUtils.isBlank(cateName)) {
                        return true;
                }
                SysHotWordCategory hotWordCategory = dao.findByCateName(cateName);
                if (hotWordCategory == null) {
                        return true;
                } else {
                        if (id == null) {
                                return false;
                        }
                        return hotWordCategory.getId().equals(id);
                }
        }

        /**
         * 通过栏目id获取栏目
         *
         * @param channelIds 栏目id数组
         * @return channel数组
         */
        private List<Channel> getChannels(Integer[] channelIds) {
                if (channelIds != null) {
                        List<Channel> list = new ArrayList<Channel>(channelIds.length);
                        for (Integer channelId : channelIds) {
                                Channel channel = channelService.findById(channelId);
                                list.add(channel);
                        }
                        return list;
                }
                return new ArrayList<Channel>(0);
        }
}