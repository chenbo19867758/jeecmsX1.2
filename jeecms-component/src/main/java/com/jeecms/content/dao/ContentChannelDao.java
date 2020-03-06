/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.domain.ContentChannel;

/**
 * 内容栏目关联dao接口
 * @author: chenming
 * @date:   2019年5月15日 下午5:09:47
 */
public interface ContentChannelDao extends IBaseDao<ContentChannel, Integer> {

    /**
     * 根据栏目ID和创建状态查询列表
    * @Title: findByChannelIdAndCreateTypeIn 
    * @param channelId 栏目ID
    * @param types 创建类型集合
    * @return
     */
    List<ContentChannel> findByChannelIdAndCreateTypeIn(Integer channelId, List<Integer> types);
    
    
    List<ContentChannel> findByChannelIdIn(Integer[] channelIds);
    
    
    List<ContentChannel> findByContentIdIn(Integer[] contentIds);
}
