/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.system.dao.SysSensitiveWordDao;
import com.jeecms.system.domain.SysSensitiveWord;
import com.jeecms.system.domain.dto.SensitiveWordDto;
import com.jeecms.system.service.SysSensitiveWordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 敏感词Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysSensitiveWordServiceImpl extends BaseServiceImpl<SysSensitiveWord, SysSensitiveWordDao, Integer> implements SysSensitiveWordService {


	@Override
	public ResponseInfo saveBatch(SensitiveWordDto dto, String userName) throws GlobalException {
		String sensitiveWord = dto.getSensitiveWord();

		String[] words = StrUtils.splitAndTrim(sensitiveWord, ",", "，");
		Set<String> set = new HashSet<String>(Arrays.asList(words));
		List<SysSensitiveWord> list = new ArrayList<SysSensitiveWord>(words.length);
		for (String str : set) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			//不存在则新增
			if (checkBySensitiveWord(str, null)) {
				SysSensitiveWord bean = new SysSensitiveWord();
				bean.setSensitiveWord(str);
				bean.setReplaceWord(dto.getReplaceWord());
				list.add(bean);
			} else {
				//存在判断处理方式，覆盖处理的话替换替换词
				if (SensitiveWordDto.DEAL_WITH_COVER.equals(dto.getDealWithType())) {
					SysSensitiveWord bean = dao.findBySensitiveWord(str);
					bean.setReplaceWord(dto.getReplaceWord());
					bean.setCreateTime(Calendar.getInstance().getTime());
					bean.setCreateUser(userName);
					update(bean);
				}
			}
		}
		super.saveAll(list);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo saveAll(List<SysSensitiveWord> list, Integer dealWithType, String userName) throws GlobalException {
		Set<SysSensitiveWord> set = new TreeSet<>(new Comparator<SysSensitiveWord>() {
			@Override
			public int compare(SysSensitiveWord o1, SysSensitiveWord o2) {
				//字符串,则按照asicc码升序排列
				return o1.getSensitiveWord().compareTo(o2.getSensitiveWord());
			}
		});
		set.addAll(list);
		List<SysSensitiveWord> adds = new ArrayList<SysSensitiveWord>();
		List<SysSensitiveWord> updates = new ArrayList<SysSensitiveWord>();
		list = new ArrayList<>(set);
		for (SysSensitiveWord word : list) {
			//不存在则新增
			if (checkBySensitiveWord(word.getSensitiveWord(), null)) {
				adds.add(word);
			} else {
				//存在判断处理方式，覆盖处理的话替换替换词
				if (SensitiveWordDto.DEAL_WITH_COVER.equals(dealWithType)) {
					SysSensitiveWord bean = dao.findBySensitiveWord(word.getSensitiveWord());
					bean.setReplaceWord(word.getReplaceWord());
					bean.setCreateTime(Calendar.getInstance().getTime());
					bean.setCreateUser(userName);
					updates.add(bean);
				}
			}
		}
		saveAll(adds);
		batchUpdateAll(updates);
		return new ResponseInfo();
	}

	@Override
	public boolean checkBySensitiveWord(String sensitiveWord, Integer id) {
		if (StringUtils.isBlank(sensitiveWord)) {
			return true;
		}
		SysSensitiveWord bean = dao.findBySensitiveWord(sensitiveWord);
		if (bean == null) {
			return true;
		} else {
			if (id == null) {
				return false;
			}
			return bean.getId().equals(id);
		}
	}
}