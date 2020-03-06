package com.jeecms.content.domain.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

/**
 * 比较版本接受dto
 *
 * @author: chenming
 * @date: 2019年6月21日 上午9:09:21
 */
public class VersionCompareDto {

	/**
	 * 内容id值
	 */
	@NotNull
	private Integer contentId;

	/**
	 * 版本id数组,   如传入0 代表当前版本
	 */
	@NotNull
	@Size(min = 2)
	private Integer[] versionIds;


	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public Integer[] getVersionIds() {
		return versionIds;
	}

	public void setVersionIds(Integer[] versionIds) {
		this.versionIds = versionIds;
	}

	@Override
	public String toString() {
		return "VersionCompareDto{"
				+ "contentId=" + contentId
				+ ", versionIds=" + Arrays.toString(versionIds)
				+ '}';
	}
}
