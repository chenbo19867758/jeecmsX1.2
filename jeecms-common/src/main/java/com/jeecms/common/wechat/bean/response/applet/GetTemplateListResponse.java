package com.jeecms.common.wechat.bean.response.applet;

import java.util.List;
/**
 * 获取代码模版库中的所有小程序代码模版  返回数据
 * @Description:
 * @author wulongwei
 * @date   2018年11月2日 下午2:30:07
 */
public class GetTemplateListResponse extends BaseAppletResponse{

	private List<TemplateList> templateList;
	
	@Override
	public String toString() {
		return "GetTemplateListResponse [template_list=" + templateList + "]";
	}

	public List<TemplateList> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<TemplateList> templateList) {
		this.templateList = templateList;
	}

	
	public GetTemplateListResponse(List<TemplateList> templateList) {
		super();
		this.templateList = templateList;
	}


	public static class TemplateList{
		/** 说开发者上传草稿时间 */
		private String createTime;

		/** 模版版本号，开发者自定义字段 */
		private String userVersion;

		/** 模版描述 开发者自定义字段 */
		private String userDesc;

		/** 模版id */
		private String templateId;

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getUserVersion() {
			return userVersion;
		}

		public void setUserVersion(String userVersion) {
			this.userVersion = userVersion;
		}

		public String getUserDesc() {
			return userDesc;
		}

		public void setUserDesc(String userDesc) {
			this.userDesc = userDesc;
		}

		public String getTemplateId() {
			return templateId;
		}

		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}

		@Override
		public String toString() {
			return "TemplateList [create_time=" + createTime + ", user_version=" + userVersion + ", user_desc="
					+ userDesc + ", template_id=" + templateId + "]";
		}

		public TemplateList(String createTime, String userVersion, String userDesc, String templateId) {
			super();
			this.createTime = createTime;
			this.userVersion = userVersion;
			this.userDesc = userDesc;
			this.templateId = templateId;
		}

		public TemplateList() {
			super();
		}
		
		
	}
}
