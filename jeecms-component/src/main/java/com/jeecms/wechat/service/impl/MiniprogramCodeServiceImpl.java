package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.api.applet.GetTemplateDraftApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateDraftListResponse;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateListResponse;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateDraftListResponse.Draftlist;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateListResponse.TemplateList;
import com.jeecms.wechat.dao.MiniprogramCodeDao;
import com.jeecms.wechat.domain.AbstractWeChatOpen;
import com.jeecms.wechat.domain.MiniprogramCode;
import com.jeecms.wechat.service.AbstractWeChatOpenService;
import com.jeecms.wechat.service.MiniprogramCodeService;

/**
 * 小程序模版草稿箱
 * 
 * @Description:
 * @author wulongwei
 * @date 2018年11月1日 上午10:14:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MiniprogramCodeServiceImpl extends BaseServiceImpl<MiniprogramCode, MiniprogramCodeDao, Integer>
		implements MiniprogramCodeService {

	@Override
	public MiniprogramCode getNew() throws GlobalException {
		return dao.getNew();
	}

	@Override
	public void synchronousDraft(String openAppId) throws GlobalException {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(openAppId);
		GetTemplateDraftListResponse response = templateDraftApiService.getTemplateDraftList(validateToken);
		// 删除数据库中数据
		dao.deleteDraft();
		List<Draftlist> list = response.getDraftList();
		List<MiniprogramCode> codes = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			for (Draftlist draftlist : list) {
				MiniprogramCode code = new MiniprogramCode();
				code.setCodeVersion(draftlist.getUserVersion());
				code.setCodeDesc(draftlist.getUserDesc());
				code.setDraftId(Integer.parseInt(draftlist.getDraftId()));
				code.setIsNew(false);
				code.setCodeType(MiniprogramCode.DRAFT);
				code.setSubmitTime((long) Integer.parseInt(draftlist.getCreateTime()) * 1000);
				codes.add(code);
			}
			super.saveAll(codes);
		}
	}

	@Override
	public void synchronousTemplate(String openAppId) throws GlobalException {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(openAppId);
		GetTemplateListResponse response = templateDraftApiService.getTemplateList(validateToken);
		// 获取到用户设置的最新模版ID
		MiniprogramCode miniprogramCode = dao.getNew();
		int templateId = 0;
		if (miniprogramCode != null) {
			templateId = miniprogramCode.getTemplateId();
		}
		// 删除数据库中数据
		dao.deleteTemplate();
		// 将微信端的数据同步保存入数据库
		List<TemplateList> list = response.getTemplateList();
		List<MiniprogramCode> codes = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			for (TemplateList templateList : list) {
				MiniprogramCode code = new MiniprogramCode();
				code.setCodeVersion(templateList.getUserVersion());
				code.setCodeDesc(templateList.getUserDesc());
				code.setTemplateId(Integer.parseInt(templateList.getTemplateId()));
				if (templateId == Integer.parseInt(templateList.getTemplateId())) {
					code.setIsNew(true);
				} else {
					code.setIsNew(false);
				}
				code.setCodeType(MiniprogramCode.TEMPLATE);
				code.setSubmitTime((long) Integer.parseInt(templateList.getCreateTime()) * 1000);
				codes.add(code);
			}
			super.saveAll(codes);
			// 获取到最新的模版ID.如果没有设置最新模板，默认显示模板ID最大的为最新模板
			MiniprogramCode newMini = dao.getNew();
			if (newMini == null) {
				MiniprogramCode maxCode = dao.getCode();
				maxCode.setIsNew(true);
				super.update(maxCode);
			}
		}
	}

	/**
	 * 将草稿箱的模版添加到模板库
	 */
	@Override
	public void addTemplate(String openAppId, Integer id) throws GlobalException {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(openAppId);
		MiniprogramCode draft = super.get(id);
		templateDraftApiService.addToTemplate(validateToken, draft.getDraftId());
		this.synchronousDraft(openAppId);
		this.synchronousTemplate(openAppId);
	}

	/**
	 * 删除模板库的数据
	 */
	@Override
	public void deleteTemplate(String openAppId, Integer id) throws GlobalException {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(openAppId);
		MiniprogramCode miniprogramCode = super.get(id);
		templateDraftApiService.deletetemplate(validateToken, miniprogramCode.getTemplateId());
		super.physicalDelete(id);
	}

	/**
	 * 将模板库的模板设置为最新状态
	 */
	@Override
	public void updateTemplate(Integer id) throws GlobalException {
		MiniprogramCode miniprogramCode = dao.getNew();
		if (miniprogramCode != null) {
			miniprogramCode.setIsNew(false);
			super.update(miniprogramCode);
		}
		MiniprogramCode code = super.get(id);
		code.setIsNew(true);
		super.update(code);
	}

	@Override
	public String getOpenAppId() {
		String appId = null;
		List<AbstractWeChatOpen> openList = weChatOpenService.findAll(true);
		if (openList != null && openList.size() > 0) {
			appId = openList.get(0).getAppId();
		}
		return appId;
	}
	
	@Override
	public void clear() throws GlobalException {
		List<MiniprogramCode> codes = super.findAll(false);
		if (codes != null && codes.size() > 0) {
			super.physicalDeleteInBatch(codes);
		}
	}
	
	@Autowired
	private GetTemplateDraftApiService templateDraftApiService;
	@Autowired
	private AbstractWeChatOpenService weChatOpenService;

}
