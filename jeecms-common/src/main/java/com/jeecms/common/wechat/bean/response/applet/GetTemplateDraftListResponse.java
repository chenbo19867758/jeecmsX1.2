package com.jeecms.common.wechat.bean.response.applet;

import java.util.List;
/**
 * 获取草稿箱内的所有临时代码草稿  返回数据
 * @Description:
 * @author wulongwei
 * @date   2018年11月2日 下午2:29:37
 */
public class GetTemplateDraftListResponse extends BaseAppletResponse {

	private List<Draftlist> draftList;

	public GetTemplateDraftListResponse(List<Draftlist> draftList) {
		super();
		this.draftList = draftList;
	}

	@Override
	public String toString() {
		return "GetTemplateDraftListResponse [draft_list=" + draftList + "]";
	}

	public List<Draftlist> getDraftList() {
		return draftList;
	}

	public void setDraftList(List<Draftlist> draftList) {
		this.draftList = draftList;
	}

	public static class Draftlist {
		/** 开发者上传草稿时间 */
		private String createTime;

		/** 模版版本号，开发者自定义字段 */
		private String userVersion;

		/** 模版描述 开发者自定义字段 */
		private String userDesc;

		/** 草稿id */
		private String draftId;

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

		public String getDraftId() {
			return draftId;
		}

		public void setDraftId(String draftId) {
			this.draftId = draftId;
		}

		@Override
		public String toString() {
			return "TemplateList [create_time=" + createTime + ", user_version=" + userVersion + ", user_desc="
					+ userDesc + ", draft_id=" + draftId + "]";
		}

		public Draftlist(String createTime, String userVersion, String userDesc, String draftId) {
			super();
			this.createTime = createTime;
			this.userVersion = userVersion;
			this.userDesc = userDesc;
			this.draftId = draftId;
		}

	}
}
