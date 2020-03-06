package com.jeecms.front.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hankcs.hanlp.HanLP;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.LanguageTreatUtils;
import com.jeecms.content.domain.dto.TextSummaryDto;

/**
 * 语言操作controller
 * @author: chenming
 * @date:   2019年7月15日 上午10:23:19
 */
@RequestMapping(value = "/language")
@RestController
public class LanguageTreatController {
	
	/**
	 * 传入文字取其文字的首字母
	 * @Title: getPinyin  
	 * @param text	文字
	 * @return: ResponseInfo
	 */
	@GetMapping("/pinyin")
	public ResponseInfo getPinyin(@RequestParam String text) {
		return new ResponseInfo(LanguageTreatUtils.getPinyin(text));
	}
	
	
	/**
	 * 传入文字自动获取摘要
	 * @Title: getSummary  
	 * @param dto	传入文字dto
	 * @return: ResponseInfo
	 */
	@PostMapping("/summary")
	public ResponseInfo getSummary(@RequestBody @Valid TextSummaryDto dto) {
		StringBuffer buffer = new StringBuffer();
		if (StringUtils.isNotBlank(dto.getText())) {
			List<String> sentenceList = HanLP.extractSummary(dto.getText(), dto.getSize());
			for (String string : sentenceList) {
				buffer.append(string);
			}
		}
		String endStr = buffer.toString();
		if (endStr.length() > dto.getSize()) {
			dto.setSize(endStr.length());
		}
		String returnStr = buffer.toString();
		if (dto.getSize() > returnStr.length()) {
			dto.setSize(returnStr.length());
		}
		return new ResponseInfo(returnStr.substring(0, dto.getSize()));
	}
	
}
