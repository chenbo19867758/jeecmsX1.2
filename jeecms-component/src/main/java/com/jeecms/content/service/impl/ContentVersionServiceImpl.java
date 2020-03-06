/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.DocCompareUtil;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.SnowFlake;
import com.jeecms.content.dao.ContentVersionDao;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentTxt;
import com.jeecms.content.domain.ContentVersion;
import com.jeecms.content.domain.dto.VersionCompareDto;
import com.jeecms.content.domain.vo.ContentVersionVo;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.ContentVersionService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 内容版本service实现类
 * @author: chenming
 * @date:   2019年5月15日 下午5:30:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentVersionServiceImpl extends BaseServiceImpl<ContentVersion,ContentVersionDao, Integer>  
		implements ContentVersionService {
	private SnowFlake snowFlake = new SnowFlake(SnowFlake.LONG_STR_CODE);

	@Override
	public void save(Map<String, String> contentTxtMap,Integer contentId,String remark) throws GlobalException {
		/**
		 * 此处操作无需处理权限，因为此处的操作只有一个地方会使用到即：开启自动保存版本功能
		 * 系统设置的权限作为最高权限，不属于个人权限，所以无需进行其它处理
		 */
		if (contentTxtMap == null) {
			contentTxtMap = new LinkedHashMap<String, String>();
		}
		ContentVersion contentVersion = new ContentVersion();
		JSONObject jsonObject = new JSONObject();
		for (String attrKey : contentTxtMap.keySet()) {
			jsonObject.put(attrKey, contentTxtMap.get(attrKey));
		}
		contentVersion.setTxt(jsonObject.toJSONString());
		contentVersion.setVersionCode(String.valueOf(snowFlake.nextId()));
		contentVersion.setContentId(contentId);
		if (StringUtils.isNotBlank(remark)) {
			contentVersion.setRemark(remark);
		}
		super.save(contentVersion);
	}

	@Override
	public void save(Content content,String remark) throws GlobalException {
		
		Map<String, String> txtMap = new LinkedHashMap<String, String>();
		for (ContentTxt contentTxt : content.getContentTxts()) {
			txtMap.put(contentTxt.getAttrKey(), contentTxt.getAttrTxt());
		}
		this.save(txtMap, content.getId(),remark);
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public ContentVersionVo controstVersion(VersionCompareDto dto) {
		Integer[] versionIds = dto.getVersionIds();
		// 来源JSON
		final Map<String, String> source = new HashMap<>(4);
		// 目标JSON
		final Map<String, String> target = new HashMap<>(4);
		Date d1 = null;
		Date d2 = null;

		// 如果版本id为0  说明是当前版本; 当前版本数据在ContentTxt中, 历史版本数据在ContentVersion中
		if (versionIds[0] == 0) {
			Content content = contentService.findById(dto.getContentId());
			Optional.ofNullable(content)
					.map(Content::getContentTxts)
					.orElse(Collections.emptyList())
					.forEach(contentTxt -> source.put(contentTxt.getAttrKey(), contentTxt.getAttrTxt()));
		} else {
			ContentVersion sourceVersion = super.findById(versionIds[0]);
			if (sourceVersion != null) {
				d1 = sourceVersion.getCreateTime();
				source.putAll(JSONObject.parseObject(sourceVersion.getTxt(), HashMap.class));
			}
		}

		if (versionIds[1] == 0) {
			Content content = contentService.findById(dto.getContentId());
			Optional.ofNullable(content)
					.map(Content::getContentTxts)
					.orElse(Collections.emptyList())
					.forEach(contentTxt -> target.put(contentTxt.getAttrKey(), contentTxt.getAttrTxt()));
		} else {
			ContentVersion targetVersion = super.findById(versionIds[1]);
			if (targetVersion != null) {
				d2 = targetVersion.getCreateTime();
				target.putAll(JSONObject.parseObject(targetVersion.getTxt(), HashMap.class));
			}
		}

		ContentVersionVo contentVersionVo = new ContentVersionVo(d1, source, d2, target);
		return contentVersionVo;
	}


	@Override
	public Map<String, Object> versionCompare(VersionCompareDto dto) {
		Map<String, Object> ret = new HashMap<>(8);
		ContentVersionVo vo = controstVersion(dto);
		Map<String, String> source = vo.getSource();
		Map<String, String> target = vo.getTarget();
		String sourceDateStr = Optional.ofNullable(vo.getSourceCreateDate())
				.map(date -> MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_D_H_M_S_PATTERN))
				.orElse("");
		String targetDateStr = Optional.ofNullable(vo.getTargetCreateDate())
				.map(date -> MyDateUtils.formatDate(date, MyDateUtils.COM_Y_M_D_H_M_S_PATTERN))
				.orElse("");

		Set<String> keySet = new HashSet<>();
		keySet.addAll(source.keySet());
		keySet.addAll(target.keySet());

		for (String key : keySet) {
			String left = Optional.ofNullable(source.getOrDefault(key, null)).orElse("");
			String right = Optional.ofNullable(target.getOrDefault(key, null)).orElse("");

			Map<String, Object> map = DocCompareUtil.textCompare(left, right);
			map.put("list1Version", sourceDateStr);
			map.put("list2Version", targetDateStr);
			ret.put(key, map);
		}

		return ret;
	}

	@Autowired
	private ContentService contentService;

}