package com.jeecms.channel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.dao.ChannelDao;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.dto.ChannelSaveAllDto;
import com.jeecms.channel.domain.dto.ChannelSaveMultipleDto;
import com.jeecms.channel.service.ChannelDtoService;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.LanguageTreatUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.FlowService;

/**
 * 栏目扩展service实现类
 * 
 * @author: chenming
 * @date: 2019年6月28日 下午2:10:22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelDtoServiceImpl implements ChannelDtoService {

	@Override
	public List<ChannelSaveAllDto> initChannelSaveAllDto(ChannelSaveMultipleDto dto) {
		// 此处clear掉所有的list是因为只要一启动list就会创建在项目结束之前不会销毁，所以此所有的list会一直叠加，所以一进入方法应当进行clear
		this.spaceNums.clear();
		this.dtoList.clear();
		this.dtos.clear();
		List<String> channelNames = dto.getChannelNames();
		/**
		 * 底下方法的实现思路 1. 将channelName的空格取出放入到spaceNums集合中 2.
		 * 将第一组(没有空格)的channelName进行初始化，初始化好的类放入dtoList和dtos中 3.
		 * 进行判断如果channelName的空格进行比对，并将数据放到dtoList中 4.
		 * 因为数据存取了两份，所以将数据比对到dtoList中本质上也是一边初始化到树形结构
		 */
		this.initChannelSaveAllDto(channelNames, dto.getSiteId(), dto.getChannelParentId(), dto.getModelId());
		List<ChannelSaveAllDto> dtos = this.dtos;
		dtos = this.initSortNum(dtos,true);
		return dtos;
	}
	
	private List<ChannelSaveAllDto> initSortNum(List<ChannelSaveAllDto> dtos,boolean isOne) {
		Integer sortNum = 0;
		if (isOne) {
			sortNum = dao.findBySortNum(dtos.get(0).getSiteId(), dtos.get(0).getChannelParentId());
			if (sortNum == null) {
				sortNum = 2;
			} else {
				sortNum+=2;
			}
			for (ChannelSaveAllDto dto : dtos) {
				dto.setSortNum(sortNum);
				sortNum+=2;
				if (dto.getChildren() != null) {
					this.initSortNum(dto.getChildren(), false);
				}
			}
		} else {
			for (ChannelSaveAllDto dto : dtos) {
				dto.setSortNum(sortNum);
				sortNum+=2;
			}
		}
		return dtos;
	}

	
	
	/**
	 * 初始化channelSaveAllDto
	 */
	private void initChannelSaveAllDto(List<String> channelNames, Integer siteId, Integer parentId, Integer modelId) {
		ChannelSaveAllDto dto = new ChannelSaveAllDto();
		// 初始化第一个栏目名称
		dto.setChannelName(channelNames.get(0));
		dto.setSiteId(siteId);
		dto.setChannelPath(LanguageTreatUtils.getPinyin(channelNames.get(0)));
		dto.setModelId(modelId);
		dto.setChannelParentId(parentId);
		spaceNums.add(0);
		dtos.add(dto);
		dtoList.add(dto);
		for (int i = 1; i < channelNames.size(); i++) {
			this.initChannelSaveAllDto(channelNames.get(i), i - 1, siteId, parentId, modelId);
		}
	}

	/**
	 * 具体初始化channelSaveAllDto的实现类
	 */
	private void initChannelSaveAllDto(String channelName, Integer i, Integer siteId, Integer parentId,
			Integer modelId) {
		// 计算出channelName的前置空格的数量
		Integer newSpaceNum = channelName.replaceAll("([ ]*).*", "$1").length();
		channelName = channelName.trim();
		ChannelSaveAllDto dto = new ChannelSaveAllDto();
		dto.setChannelName(channelName);
		dto.setSiteId(siteId);
		dto.setChannelPath(LanguageTreatUtils.getPinyin(channelName));
		dto.setModelId(modelId);
		if (newSpaceNum == 0) {
			dto.setChannelParentId(parentId);
		}
		if (newSpaceNum > 0) {
			if (newSpaceNum > spaceNums.get(i)) {
				if (dtoList.get(i).getChildren() == null) {
					List<ChannelSaveAllDto> newDtoList = new ArrayList<ChannelSaveAllDto>();
					newDtoList.add(dto);
					dtoList.get(i).setChildren(newDtoList);
				} else {
					dtoList.get(i).getChildren().add(dto);
				}
				dtoList.add(dto);
				spaceNums.add(newSpaceNum);
			} else {
				Integer spaceNumSize = spaceNums.size() - 1;
				while (spaceNumSize != 0 && spaceNums.get(spaceNumSize) >= newSpaceNum) {
					spaceNumSize--;
				}
				if (dtoList.get(spaceNumSize).getChildren() == null) {
					List<ChannelSaveAllDto> newDtoList = new ArrayList<ChannelSaveAllDto>();
					newDtoList.add(dto);
					dtoList.get(spaceNumSize).setChildren(newDtoList);
				} else {
					dtoList.get(spaceNumSize).getChildren().add(dto);
				}
				dtoList.add(dto);
				spaceNums.add(newSpaceNum);
			}
		} else {
			dtos.add(dto);
			dtoList.add(dto);
			spaceNums.add(newSpaceNum);
		}
	}

	@Override
	@Async("asyncServiceExecutor")
	public void workflowDelete(Integer[] ids, CoreUser user) throws GlobalException {
		/**
		 * 此方法放入此处是因为 1、异步方法和调用类不要在同一个类中 2、注解扫描时，要注意过滤，避免重复实例化，因为存在覆盖问题，@Async就失效了
		 * 所以此处将方法放入到此实现类中
		 */
		List<Channel> channels = dao.findByWorkflowIdInAndHasDeleted(ids, false);
		if (channels != null && channels.size() > 0) {
			for (Channel channel : channels) {
				channel.setWorkflowId(null);
			}
			channelService.batchUpdate(channels);
			channelService.flush();
			List<Integer> channelIds = channels.stream().map(Channel::getId).collect(Collectors.toList());
			List<Content> contents = contentService.findByChannels(channelIds.toArray(new Integer[channelIds.size()]));
			if (contents != null && contents.size() > 0) {
				List<Integer> contentIds = contents.stream().filter(
						content -> content.getStatus() == ContentConstant.STATUS_WAIT_PUBLISH || content.getStatus() == ContentConstant.STATUS_FLOWABLE)
						.map(Content::getId).collect(Collectors.toList());
				if (contentIds != null && contentIds.size() > 0) {
					flowService.doInterruptDataFlow(ContentConstant.WORKFLOW_DATA_TYPE_CONTENT, contentIds, user);
				}
			}
		}

	}

	/** 栏目前空格的数量集合 */
	private List<Integer> spaceNums = new ArrayList<Integer>();
	/** 普通集合 */
	private List<ChannelSaveAllDto> dtoList = new ArrayList<ChannelSaveAllDto>();
	/** 树形集合 */
	private List<ChannelSaveAllDto> dtos = new ArrayList<ChannelSaveAllDto>();
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private ChannelDao dao;
	@Autowired
	private FlowService flowService;

}
