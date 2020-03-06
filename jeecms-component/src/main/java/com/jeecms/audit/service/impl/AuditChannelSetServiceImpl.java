/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.service.impl;

import com.jeecms.audit.constants.ContentAuditConstants;
import com.jeecms.audit.dao.AuditChannelSetDao;
import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.audit.domain.dto.AuditChannelDto;
import com.jeecms.audit.domain.vo.AuditChannelTreeVo;
import com.jeecms.audit.domain.vo.AuditChannelVo;
import com.jeecms.audit.service.AuditChannelSetService;
import com.jeecms.audit.service.AuditStrategyService;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.ChannelErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 审核栏目设置实现类
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditChannelSetServiceImpl extends BaseServiceImpl<AuditChannelSet, AuditChannelSetDao, Integer>
		implements AuditChannelSetService {

	@Autowired
	private AuditStrategyService auditStrategyService;
	@Autowired
	private ChannelService channelService;

	@Override
	public List<AuditChannelVo> getList(Integer siteId) throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(3);
		// 过滤开启的策略
		params.put("EQ_status_Boolean", new String[] { "true" });
		params.put("EQ_siteId_Integer", new String[] { siteId.toString() });
		List<AuditStrategy> strategies = auditStrategyService.getList(params, null, false);
		//创建时间倒序
		strategies = strategies.stream().sorted(Comparator.comparing(AuditStrategy::getCreateTime).reversed())
				.collect(Collectors.toList());
		List<AuditChannelVo> vos = new ArrayList<AuditChannelVo>(16);
		for (AuditStrategy auditStrategy : strategies) {
			AuditChannelVo vo = new AuditChannelVo(auditStrategy.getId(), auditStrategy.getName());
			vo.setChannelSets(auditStrategy.getChannelSets());
			// 拼凑策略场景数据
			vo.setStrategyString(getStrategyString(auditStrategy));
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 拼凑策略场景数据
	 * 
	 * @Title: getStrategyString
	 * @param auditStrategy 策略对象
	 * @return
	 */
	protected String getStrategyString(AuditStrategy auditStrategy) {
		StringBuilder builder = new StringBuilder();
		// 是否开启文本审核
		if (auditStrategy.getIsText()) {
			String scene = auditStrategy.getTextScene();
			builder.append("文本：");
			if (StringUtils.isNotBlank(scene)) {
				if (scene.contains(",")) {
					String[] strings = scene.split(",");
					List<String> list = Arrays.asList(strings);
					for (String string : list) {
						builder
							.append(ContentAuditConstants
									.textScene(Integer.valueOf(string)));
						builder.append("、");
					}
				} else {
					builder.append(ContentAuditConstants
									.textScene(Integer.valueOf(scene)));
					builder.append("、");
				}
			}
		}
		String v = builder.substring(0, builder.length() != 0 ? builder.length() - 1 : 0);
		StringBuilder picture = new StringBuilder();
		// 是否开启图片审核
		if (auditStrategy.getIsPicture()) {
			String scene = auditStrategy.getPictureScene();
			picture.append(" | 图片：");
			if (StringUtils.isNotBlank(scene)) {
				if (scene.contains(",")) {
					String[] strings = scene.split(",");
					List<String> list = Arrays.asList(strings);
					for (String string : list) {
						picture
							.append(ContentAuditConstants
									.pictureScene(Integer.valueOf(string)));
						picture.append("、");
					}
				} else {
					picture.append(ContentAuditConstants.pictureScene(Integer.valueOf(scene)));
					picture.append("、");
				}
			}
		}
		return v.concat(picture.substring(0, picture.length() != 0 ? picture.length() - 1 : 0));
	}

	@Override
	public List<AuditChannelTreeVo> getChannels(Integer strategyId, Integer siteId) throws GlobalException {
		List<Channel> channels = channelService.getChanelByRecycle(siteId, false);
		Map<String, String[]> params = new HashMap<String, String[]>(3);
		// 得到策略对应的栏目设置
		params.put("EQ_strategyId_Integer", new String[] { strategyId.toString() });
		List<AuditChannelSet> sets = super.getList(params, null, false);
		//得到勾选的栏目IDs
		List<Integer> contains = sets.stream().map(AuditChannelSet::getChannelId)
				.collect(Collectors.toList());
		//得到其他策略的栏目设置
		List<AuditChannelSet> nosets = dao.findByStrategyIdNotAndHasDeleted(strategyId, false);
		//得到置灰的栏目IDs
		List<Integer> grays = nosets.stream().map(AuditChannelSet::getChannelId)
				.collect(Collectors.toList());
		return getChildTree(channels, contains, grays);
	}

	/**
	 * 栏目数据权限集合转换成带树形结构数据
	 * 
	 * @Title: getChildTree
	 * @param childs 栏目数据
	 * @return List
	 */
	public static List<AuditChannelTreeVo> getChildTree(List<Channel> childs, List<Integer> contains,
			List<Integer> grays) {
		List<AuditChannelTreeVo> result = new ArrayList<AuditChannelTreeVo>();
		if (null == childs || childs.size() == 0) {
			return result;
		}
		if (childs != null && !childs.isEmpty()) {
			Channel channel = childs.iterator().next();
			Integer parentId = null;
			if (channel != null) {
				parentId = channel.getParentId();
			}
			List<AuditChannelTreeVo> dataSource = new ArrayList<>();
			Map<Integer, AuditChannelTreeVo> hashDatas = new HashMap<>(childs.size());
			for (Channel t : childs) {
				AuditChannelTreeVo st = new AuditChannelTreeVo();
				st.setChannelId(t.getId());
				st.setChannelName(t.getName());
				st.setParentId(t.getParentId());
				//判断是否置灰
				if (grays.contains(t.getId())) {
					st.setGray(true);;
				} else {
					st.setGray(false);
				}
				//判断是否勾选
				if (contains.contains(t.getId())) {
					st.setSelect(true);
				} else {
					st.setSelect(false);
				}
				// 没有子节点则过滤childs
				long count = childs.stream().filter(
						c -> null != c.getParentId() 
						&& ((Integer) t.getId()).intValue() == c.getParentId().intValue())
						.count();
				if (count > 0) {
					st.setChildren(new ArrayList<AuditChannelTreeVo>());
				}
				dataSource.add(st);
				hashDatas.put(t.getId(), st);
			}
			childs.clear();

			// 遍历集合
			for (int i = 0; i < dataSource.size(); i++) {
				// 当前节点
				AuditChannelTreeVo json = dataSource.get(i);
				// 当前的父节点
				AuditChannelTreeVo hashObject = hashDatas.get(json.getParentId());
				if (hashObject != null) {
					// 表示当前节点为子节点
					hashObject.getChildren().add(json);
				} else if (null == json.getParentId()
						|| parentId.intValue() == ((Integer) json.getParentId())) {
					// parentId为null和获取匹配parentId的节点(生成某节点的子节点树时需要用到)
					result.add(json);
				}
			}
		}
		//判断父结点是否置灰
		checkParent(result);
		return result;
	}

	/**
	 * 判断父结点是否置灰
	 * @param vos
	 */
	protected static void checkParent(List<AuditChannelTreeVo> vos){
		if(!vos.isEmpty()) {
			for (AuditChannelTreeVo auditChannelTreeVo : vos) {
				//如果说子结点全部为置灰，则父结点也要置灰
				boolean flag = auditChannelTreeVo.getChildren() != null && !auditChannelTreeVo.getChildren().isEmpty();
				if (flag) {
					List<AuditChannelTreeVo> collect = auditChannelTreeVo.getChildren().stream()
							.filter(x -> x.getGray().equals(false))
							.collect(Collectors.toList());
					if (collect.isEmpty()) {
						auditChannelTreeVo.setGray(true);
					}
					checkParent(auditChannelTreeVo.getChildren());
				}
			}
		}
	}
	
	@Override
	public ResponseInfo saveChannelSet(AuditChannelDto dto) throws GlobalException {
		List<AuditChannelSet> sets = new ArrayList<AuditChannelSet>(16);
		if (dto.getChannels() != null && !dto.getChannels().isEmpty()) {
			//先删除该策略的栏目设置
			Map<String, String[]> params = new HashMap<String, String[]>(3);
			// 得到策略对应的栏目设置
			params.put("EQ_strategyId_Integer", new String[] { dto.getStrategyId().toString() });
			List<AuditChannelSet> list = super.getList(params, null, false);
			super.physicalDeleteInBatch(list);
			List<Channel> channels = channelService.findAllById(dto.getChannels());
			CopyOnWriteArrayList<Channel> channelList = new CopyOnWriteArrayList<>(channels);
			for (Channel channel : channelList) {
				//栏目不是底层栏目，过滤出去
				if (!channel.getIsBottom()) {
					channelList.remove(channel);
					continue;
				}
				AuditChannelSet set = new AuditChannelSet(channel.getId(),
						dto.getStrategyId(), true, true);
				//设置缓存
				set.setStrategy(auditStrategyService.findById(dto.getStrategyId()));
				set.setChannel(channel);
				sets.add(set);
			}
			super.saveAll(sets);
		} else {
			//栏目为空直接报错
			return new ResponseInfo(ChannelErrorCodeEnum.CHANNEL_IS_NOT_NULL.getCode(),
					ChannelErrorCodeEnum.CHANNEL_IS_NOT_NULL.getDefaultMessage(), false);
		}
		return new ResponseInfo();
	}

	@Override
	public AuditChannelSet findByChannelId(Integer channelId,boolean checkStatus) {
		List<AuditChannelSet> auditChannelSets = dao.findByChannelIdAndHasDeleted(channelId, false);
		if (!CollectionUtils.isEmpty(auditChannelSets)) {
			AuditChannelSet auditChannelSet = auditChannelSets.get(0);
			if (checkStatus) {
				if (auditChannelSet.getStatus()) {
					return auditChannelSet;
				}
			} else {
				return auditChannelSet;
			}
		}
		return null;
	}

	@Override
	public List<AuditChannelSet> findByStrategyIds(Integer[] strategyIds) {
		return dao.findByStrategyIdIn(strategyIds);
	}

}