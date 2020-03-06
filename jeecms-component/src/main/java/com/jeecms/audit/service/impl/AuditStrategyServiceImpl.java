/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.service.impl;

import com.jeecms.audit.dao.AuditStrategyDao;
import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.audit.domain.dto.AuditStrategySaveDto;
import com.jeecms.audit.service.AuditChannelSetService;
import com.jeecms.audit.service.AuditStrategyService;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jeecms.audit.constants.ContentAuditConstants.AUDIT_PICTURE;
import static com.jeecms.audit.constants.ContentAuditConstants.AUDIT_TEXT;

/**
 * 审核策略实现类
 *
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditStrategyServiceImpl extends BaseServiceImpl<AuditStrategy, AuditStrategyDao, Integer>
	implements AuditStrategyService {

	@Override
	public AuditStrategy save(AuditStrategySaveDto dto, Integer siteId) throws GlobalException {
		if (!unique(dto.getName(), null, siteId)) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.AUDIT_STRATEGY_NAME_ALREADY_EXIST.getDefaultMessage(),
				SysOtherErrorCodeEnum.AUDIT_STRATEGY_NAME_ALREADY_EXIST.getCode()));
		}
		List<Integer> textScene = dto.getTextScenes();
		String text = StringUtils.join(textScene, WebConstants.ARRAY_SPT);
		List<Integer> pictureScene = dto.getPictureScenes();
		String picture = StringUtils.join(pictureScene, WebConstants.ARRAY_SPT);
		AuditStrategy strategy = new AuditStrategy(dto.getName(), dto.getStatus(), siteId, text, picture);
		return save(strategy);
	}

	@Override
	public AuditStrategy updateName(String name, Integer id, Integer siteId) throws GlobalException {
		if (!unique(name, id, siteId)) {
			throw new GlobalException(new SystemExceptionInfo(
				SysOtherErrorCodeEnum.AUDIT_STRATEGY_NAME_ALREADY_EXIST.getDefaultMessage(),
				SysOtherErrorCodeEnum.AUDIT_STRATEGY_NAME_ALREADY_EXIST.getCode()));
		}
		AuditStrategy bean = dao.findByIdAndSiteIdAndHasDeleted(id, siteId, false);
		bean.setName(name);
		return update(bean);
	}

	@Override
	public AuditStrategy updateScene(Integer id, Integer type, Integer scene, Integer siteId) 
			throws GlobalException {
		AuditStrategy bean = dao.findByIdAndSiteIdAndHasDeleted(id, siteId, false);
		if (AUDIT_TEXT.equals(type)) {
			String textScene = bean.getTextScene();
			bean.setTextScene(processScene(textScene, scene));
		} else if (AUDIT_PICTURE.equals(type)) {
			String pictureScene = bean.getPictureScene();
			bean.setPictureScene(processScene(pictureScene, scene));
		}
		return update(bean);
	}

	/**
	 * 处理审核场景
	 * @param sceneString 审核场景
	 * @param scene 1文本 2图片
	 * @return
	 */
	private String processScene(String sceneString, @NotNull Integer scene) {
		String[] split = StringUtils.split(sceneString, WebConstants.ARRAY_SPT);
		if (split != null) {
			List<String> list = Arrays.asList(split);
			List<String> array = new ArrayList<>(list);
			String str = String.valueOf(scene);
			if (array.contains(str)) {
				array.remove(str);
			} else {
				array.add(str);
			}
			array = array.parallelStream().sorted().collect(Collectors.toList());
			sceneString = StringUtils.join(array, WebConstants.ARRAY_SPT);
		}
		return sceneString;
	}

	@Override
	public AuditStrategy updateStatus(Boolean status, Integer id, Integer siteId) throws GlobalException {
		AuditStrategy bean = dao.findByIdAndSiteIdAndHasDeleted(id, siteId, false);
		if (status != null && !status.equals(bean.getStatus())) {
			bean.setStatus(status);
		}
		return update(bean);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)
	public boolean unique(String name, Integer id, Integer siteId) {
		AuditStrategy bean = dao.findByNameAndSiteIdAndHasDeleted(name, siteId, false);
		if (bean == null) {
			return true;
		}
		if (id == null) {
			return false;
		} else {
			return bean.getId().equals(id);
		}
	}

	@Override
	public void delete(DeleteDto deleteDto) throws GlobalException {
		delete(deleteDto.getIds());
		List<AuditChannelSet> byStrategyIds = auditChannelSetService.findByStrategyIds(deleteDto.getIds());
		auditChannelSetService.delete(byStrategyIds);
	}

	@Override
	public List<Integer> findByChannel(Integer channelId, boolean isText) {
		AuditChannelSet auditChannelSet = auditChannelSetService.findByChannelId(channelId,true);
		if (auditChannelSet != null) {
			AuditStrategy auditStrategy = auditChannelSet.getStrategy();
			if (auditStrategy != null) {
				if (auditStrategy.getStatus()) {
					if (isText) {
						
					}
				}
			}
		}
		return null;
	}
	
	@Autowired
	private AuditChannelSetService auditChannelSetService;
}