/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.dao.ext;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.page.Paginable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * 栏目扩展查询
 *
 * @author: tom
 * @date: 2019年3月20日 上午8:41:45
 */
public interface ChannelDaoExt {

    /**
     * 查詢list
     *
     * @Title: findList
     * @param siteId
     *                站点ID
     * @param modelId
     *                模型ID
     * @param parentId
     *                父栏目ID（为空或者0则查询顶层栏目 ）
     * @param display
     *                是否前台显示
     * @param staticChannel
     *                是否开启静态化
     * @param paginable
     *                条数
     * @param path 栏目路径
     * @param recycle 是否回收
     * @return List
     */
    List<Channel> findList(Integer siteId, Integer modelId, Integer parentId, Boolean display,
                           Boolean staticChannel, Paginable paginable, String path, Boolean recycle);

    /**
     * 查询page
     *
     * @Title: findPage
     * @param siteId
     *                站点ID
     * @param modelId
     *                模型ID
     * @param parentId
     *                父栏目ID（为空或者0则查询顶层栏目 ）
     * @param display
     *                是否前台显示
     * @param staticChannel
     *                是否开启静态化
     * @param pageable
     *                分页组件
     * @param path 栏目路径
     * @param recycle 是否回收
     * @return List
     */
    Page<Channel> findPage(Integer siteId, Integer modelId, Integer parentId, Boolean display,
                           Boolean staticChannel, Pageable pageable, String path, Boolean recycle);

    /**
     * 根据siteId与栏目名称或者栏目路径，进行查询检索
     *
     * @Title: checkNameAndPath
     * @param name
     *                栏目名称
     * @param path
     *                栏目路径
     * @param siteId
     *                站点Id
     * @return: List
     */
    List<String> checkNameAndPath(boolean name, boolean path, Integer siteId);


    /**
     * 根据栏目路径查询对象
     * @Title: findByPath
     * @param path 栏目路径
     * @param siteId 站点ID
     * @param recycle 是否回收站
     * @return: Channel
     */
    Channel findByPath(String path, Integer siteId, Boolean recycle);

    /**
     * 根据栏目路径查询对象
     * @Title: findByPath
     * @param paths 栏目路径
     * @param siteId 站点ID
     * @param recycle 是否回收站
     * @return: Channel
     */
    List<Channel> findByPath(String[] paths, Integer siteId, Boolean recycle);
    
    Integer findBySortNum(Integer siteId,Integer parentId);

    List<Channel>  findByIds(Collection<Integer> ids);

}
