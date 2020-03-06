/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.dao.ContentRecordDao;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentRecord;
import com.jeecms.content.domain.dto.CheckUpdateDto;
import com.jeecms.content.domain.dto.SpliceCheckUpdateDto;
import com.jeecms.content.service.CmsModelItemService;
import com.jeecms.content.service.ContentRecordService;
import com.jeecms.system.domain.GlobalConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内容操作记录service实现类
 * @author: chenming
 * @date: 2019年5月15日 下午5:29:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentRecordServiceImpl extends BaseServiceImpl<ContentRecord, ContentRecordDao, Integer>
		implements ContentRecordService {

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public String getOpreateRemark(SpliceCheckUpdateDto oldUpdateDto, SpliceCheckUpdateDto newUpdateDto,
			GlobalConfig globalConfig, List<CheckUpdateDto> dtos, List<CheckUpdateDto> newDtos,List<CmsModelItem> modelItems) 
					throws GlobalException {
		StringBuffer value = new StringBuffer();
		Map<String, CheckUpdateDto> oldTxtMap = dtos.stream().filter(CheckUpdateDto -> CheckUpdateDto.getIsTxt())
				.collect(Collectors.toMap(CheckUpdateDto::getField, c -> c));
		List<CheckUpdateDto> newTxts = newDtos.stream().filter(CheckUpdateDto -> CheckUpdateDto.getIsTxt())
				.collect(Collectors.toList());
		for (CheckUpdateDto checkUpdateDto : newTxts) {
			String field = checkUpdateDto.getField();
			CheckUpdateDto oldDto = oldTxtMap.get(field);
			if (oldDto == null) {
				value.append(",修改正文");
			} else {
				if (checkUpdateDto.getValue() != null) {
					if (!checkUpdateDto.getValue().equals(oldDto.getValue())) {
						value.append(",修改正文");
					}
				}
			}
		}
		Map<String, CheckUpdateDto> dtoMap = dtos.stream().filter(CheckUpdateDto -> !CheckUpdateDto.getIsTxt())
				.collect(Collectors.toMap(CheckUpdateDto::getField, c -> c));
		newDtos = newDtos.stream().filter(CheckUpdateDto -> !CheckUpdateDto.getIsTxt()).collect(Collectors.toList());
		for (CheckUpdateDto newCheckUpdateDto : newDtos) {
			CheckUpdateDto oldCheckUpdateDto = dtoMap.get(newCheckUpdateDto.getField());
			if (oldCheckUpdateDto == null) {
				continue;
			}
			if (newCheckUpdateDto.getValue() != null && !"null".equals(newCheckUpdateDto.getValue())) {
				// 如果是多资源
				if (newCheckUpdateDto.getIsResources()) {
					if (oldCheckUpdateDto.getValue() != null && !"null".equals(oldCheckUpdateDto.getValue())) {
						if (!newCheckUpdateDto.getValue().equals(oldCheckUpdateDto.getValue())) {
							value.append(
									",修改" + newCheckUpdateDto.getItemLabel() + "([" + oldCheckUpdateDto.getAttrResValue()
											+ "改为" + newCheckUpdateDto.getAttrResValue() + "])");
						} else {
							if (newCheckUpdateDto.getIsPhones()) {
								if (StringUtils.isNotBlank(newCheckUpdateDto.getPhoneDeptict())) {
									/**
									 * 此处仅仅是做了针对于该字段的一个对比表述 原因：1. 针对这种多资源对象场景过多的情况，仅仅是针对于一个多资源对象作为一个比对值进行比对。 2.
									 * 针对于多图片资源可以添加描述，但又根本不显示描述，如果出现描述字数太多， 从而进行对比的改字段描述过多，不仅影响性能，而且几乎可以说毫无意义
									 */
									if (!newCheckUpdateDto.getPhoneDeptict()
											.equals(oldCheckUpdateDto.getPhoneDeptict())) {
										value.append(",修改了图片描述");
									}
								} else {
									// 如果新的图片描述为空，说明如果老的图片描述不为空说明修改了，为空。。。
									if (StringUtils.isNotBlank(oldCheckUpdateDto.getPhoneDeptict())) {
										value.append(",修改了图片描述");
									}
								}
							} else {
								if (StringUtils.isNotBlank(newCheckUpdateDto.getSecret())) {
									/**
									 * 直接校验，old为空也会判断为false所以无需处理
									 */
									if (!newCheckUpdateDto.getSecret().equals(oldCheckUpdateDto.getSecret())) {
										value.append(",修改了附件密级");
									}
								} else {
									// 如果新的图片描述为空，说明如果老的图片描述不为空说明修改了，为空。。。
									if (StringUtils.isNotBlank(oldCheckUpdateDto.getSecret())) {
										value.append(",修改了附件密级");
									}
								}
							}
						}
					} else {
						value.append(",新增[" + newCheckUpdateDto.getItemLabel() + "])");
					}
				} else {
					if (!newCheckUpdateDto.getValue().equals(oldCheckUpdateDto.getValue())) {
						if (oldCheckUpdateDto.getValue() != null && !"null".equals(oldCheckUpdateDto.getValue())) {
							if (!newCheckUpdateDto.getValue().equals(oldCheckUpdateDto.getValue())) {
								if (CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE.equals(oldCheckUpdateDto.getField()) || CmsModelConstant.FIELD_SYS_TEXTLIBRARY.equals(oldCheckUpdateDto.getField())) {
									value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(["
											+ oldCheckUpdateDto.getAttrResValue() + "]改为["
											+ newCheckUpdateDto.getAttrResValue() + "])");
								} else {
									value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(["
											+ oldCheckUpdateDto.getValue() + "]改为[" + newCheckUpdateDto.getValue() + "])");
								}
							}
						} else {
							if (oldCheckUpdateDto.getField().equals(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE)  || CmsModelConstant.FIELD_SYS_TEXTLIBRARY.equals(oldCheckUpdateDto.getField())) {
								if (oldCheckUpdateDto.getField().equals(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE)) {
									value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(新增图片["
											+ newCheckUpdateDto.getAttrResValue() + "])");
								}
								if (oldCheckUpdateDto.getField().equals(CmsModelConstant.FIELD_SYS_TEXTLIBRARY)) {
									value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(新增文库["
											+ newCheckUpdateDto.getAttrResValue() + "])");
								}
							} else {
								value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(空改为["
										+ newCheckUpdateDto.getValue() + "])");
							}
						}
					}
				}
			} else {
				// 如果是多资源
				if (newCheckUpdateDto.getIsResources()) {
					if (oldCheckUpdateDto.getValue() != null && !"null".equals(oldCheckUpdateDto.getValue())) {
						value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(删除["
								+ oldCheckUpdateDto.getAttrResValue() + "])");
					}
				} else {
					if (oldCheckUpdateDto.getValue() != null && !"null".equals(oldCheckUpdateDto.getValue())) {
						if (oldCheckUpdateDto.getField().equals(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE)) {
							value.append(",修改" + newCheckUpdateDto.getItemLabel() + "(删除图片["
									+ oldCheckUpdateDto.getAttrResValue() + "])");
						} else {
							value.append(",修改" + newCheckUpdateDto.getItemLabel() + "([" + oldCheckUpdateDto.getValue()
									+ "]改为空)");
						}
					}
				}
			}
		}
		
		if (!oldUpdateDto.getReleasePc().equals(newUpdateDto.getReleasePc())) {
			value.append(",修改发布平台PC([" + oldUpdateDto.getReleasePc() + "]改为[" + newUpdateDto.getReleasePc() + "])");
		}
		if (!oldUpdateDto.getReleaseWap().equals(newUpdateDto.getReleaseWap())) {
			value.append(",修改发布平台Wap([" + oldUpdateDto.getReleaseWap() + "]改为[" + newUpdateDto.getReleaseWap() + "])");
		}
		if (!oldUpdateDto.getReleaseApp().equals(newUpdateDto.getReleaseApp())) {
			value.append(",修改发布平台App([" + oldUpdateDto.getReleaseApp() + "]改为[" + newUpdateDto.getReleaseApp() + "])");
		}
		if (!oldUpdateDto.getReleaseMiniprogram().equals(newUpdateDto.getReleaseMiniprogram())) {
			value.append(",修改发布平台小程序([" + oldUpdateDto.getReleaseMiniprogram() + "]改为["
					+ newUpdateDto.getReleaseMiniprogram() + "])");
		}
		if (!oldUpdateDto.getSourceName().equals(newUpdateDto.getSourceName())) {
			value.append(",修改来源名称([" + oldUpdateDto.getSourceName() + "]改为[" + newUpdateDto.getSourceName() + "])");
		}
		if (oldUpdateDto.getSourceLink()!=null&&!oldUpdateDto.getSourceLink().equals(newUpdateDto.getSourceLink())) {
			value.append(",修改来源链接([" + oldUpdateDto.getSourceLink() + "]改为[" + newUpdateDto.getSourceLink() + "])");
		}
		List<CmsModelItem> newModels = modelItems.stream()
				.filter(CmsModelItem -> CmsModelItem.getField().equals(CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK))
				.collect(Collectors.toList());
		CmsModelItem modelItem = null;
		if (newModels != null && newModels.size() > 0) {
			modelItem = newModels.get(0);
		}
		if (modelItem != null) {
			if (oldUpdateDto.getOutLink() != null) {
				if (!oldUpdateDto.getOutLink().equals(newUpdateDto.getOutLink())) {
					value.append(",修改" + modelItem.getItemLabel() + "([" + oldUpdateDto.getOutLink() + "]改为["
							+ newUpdateDto.getOutLink() + "])");
				}
				if (!oldUpdateDto.getIsNewTarget().equals(newUpdateDto.getIsNewTarget())) {
					value.append(
							",修改选择新窗口打开外部链接([" + oldUpdateDto.getIsNewTarget() + "]改为[" + newUpdateDto.getIsNewTarget() + "])");
				}
			}
		}

		String val = value.toString();
		return val.length() > 0 ? val.substring(1) : null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class,readOnly = true)
	public Page<ContentRecord> getPage(Integer contentId, Date startTime, Date endTime, 
			String userName, Pageable pageable) {
		return dao.getPage(contentId, startTime, endTime, userName, pageable);
	}

	@Override
	@Async("asyncServiceExecutor")
	public void checkUpdate(SpliceCheckUpdateDto oldUpdateDto, SpliceCheckUpdateDto newUpdateDto,
			GlobalConfig globalConfig, Content bean) throws GlobalException {
		List<CmsModelItem> modelItems = cmsModelItemService.findByModelId(newUpdateDto.getModelId());
		List<CheckUpdateDto> dtos = CheckUpdateDto.spliceCheckUpdateDtos(modelItems, oldUpdateDto);
		List<CheckUpdateDto> newDtos = CheckUpdateDto.spliceCheckUpdateDtos(modelItems, newUpdateDto);
		String opreateRemark = this.getOpreateRemark(oldUpdateDto, newUpdateDto, globalConfig, dtos, newDtos,
				modelItems);
		if (StringUtils.isNotBlank(opreateRemark)) {
			ContentRecord contentRecord = new ContentRecord(bean.getId(), bean.getUser().getUsername(), "修改",
					opreateRemark, null, bean);
			super.save(contentRecord);
		}
		
	}
	
	@Autowired
	private CmsModelItemService cmsModelItemService;

	

}