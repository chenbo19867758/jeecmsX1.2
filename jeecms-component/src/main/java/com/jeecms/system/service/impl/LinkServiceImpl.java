package com.jeecms.system.service.impl;


import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.system.dao.LinkDao;
import com.jeecms.system.domain.Link;
import com.jeecms.system.service.LinkService;
import com.jeecms.system.service.SysLinkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LinkServiceImpl extends BaseServiceImpl<Link, LinkDao, Integer> implements LinkService {

    @Autowired
    private ResourcesSpaceDataService spaceDataService;
    @Autowired
    private SysLinkTypeService linkTypeService;

    @Override
    public Link save(Link link, Integer siteId) throws GlobalException {
        link.setSiteId(siteId);
        if (link.getLinkLogo() != null) {
            ResourcesSpaceData spaceData = spaceDataService.findById(link.getLinkLogo());
            link.setResourcesSpaceData(spaceData);
        }
        link.setLinkType(linkTypeService.findById(link.getLinkTypeId()));
        link.setSortNum(getLinkNum(siteId, link.getLinkTypeId()) + 1);
        return super.save(link);
    }

    @Override
    public Link isEnable(Integer id, Boolean isEnable) throws GlobalException {
        Link link = dao.getOne(id);
        link.setIsEnable(isEnable);
        return super.update(link);
    }

    @Override
    public int getLinkNum(Integer siteId, Integer linkTypeId) {
        List<Link> links = dao.findBySiteIdAndLinkTypeId(siteId, linkTypeId);
        return links != null ? links.size() : 0;
    }

    @Override
    public void dragSort(DragSortDto sort) throws GlobalException {
        Link fromType = findById(sort.getFromId());
        Link toType = findById(sort.getToId());
        Integer fromSort = fromType.getSortNum();
        Integer toSort = toType.getSortNum();
        //向上拖动
        if (fromSort > toSort) {
            sort(fromType.getId(), fromSort, toSort, true, fromType.getLinkTypeId());
            fromType.setSortNum(toSort);
            update(fromType);
        } else {
            //向下拖动
            sort(fromType.getId(), fromSort, toSort, false, fromType.getLinkTypeId());
            fromType.setSortNum(toSort);
            update(fromType);
        }
    }

    @Override
    public List<Link> move(Integer[] ids, Integer linkTypeId) throws GlobalException {
        List<Link> links = new ArrayList<Link>();
        for (Integer id : ids) {
            Link link = findById(id);
            link.setLinkTypeId(linkTypeId);
            links.add(update(link));
        }
        return links;
    }

    /**
     * @param id          需要移动的id
     * @param fromSortNum 移动排序
     * @param toSortNum   目标排序
     * @param flag        true 加1 false 减1
     * @throws GlobalException 异常
     */
    private void sort(Integer id, Integer fromSortNum, Integer toSortNum, boolean flag, Integer linkTypeId) throws GlobalException {
        List<Link> links;
        if (flag) {
            List<Link> list = dao.getListBySortNum(fromSortNum, toSortNum, id, linkTypeId);
            links = new ArrayList<Link>(list.size());
            for (Link link : list) {
                link.setSortNum(link.getSortNum() + 1);
                links.add(link);
            }
        } else {
            List<Link> list = dao.getListBySortNum(toSortNum, fromSortNum, id, linkTypeId);
            links = new ArrayList<Link>(list.size());
            for (Link link : list) {
                link.setSortNum(link.getSortNum() - 1);
                links.add(link);
            }
        }
        batchUpdateAll(links);
    }
}
