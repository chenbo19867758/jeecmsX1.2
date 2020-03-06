/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import java.util.Arrays;

/**
 * 移动资源Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/26 16:43
 */

public class ResourcesSpaceDataShiftDto {
	private Integer[] ids;
	private Integer storeResourcesSpaceId;

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	public Integer getStoreResourcesSpaceId() {
		return storeResourcesSpaceId;
	}

	public void setStoreResourcesSpaceId(Integer storeResourcesSpaceId) {
		this.storeResourcesSpaceId = storeResourcesSpaceId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ResourcesSpaceDataShiftDto)) {
			return false;
		}

		ResourcesSpaceDataShiftDto that = (ResourcesSpaceDataShiftDto) o;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(getIds(), that.getIds())) {
			return false;
		}
		return getStoreResourcesSpaceId() != null ? getStoreResourcesSpaceId().equals(that.getStoreResourcesSpaceId()) : that.getStoreResourcesSpaceId() == null;
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(getIds());
		result = 31 * result + (getStoreResourcesSpaceId() != null ? getStoreResourcesSpaceId().hashCode() : 0);
		return result;
	}
}
