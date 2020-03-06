/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.base.domain;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 拖拽排序dto
 *
 * @author wangqq
 * @date: 2019年6月4日 下午4:27:56
 */
public class DragSortDto implements Serializable {

	private static final long serialVersionUID = -6265067911977156831L;
	/**
	 * 拖动元素ID
	 */
	@NotNull
	private Integer fromId;
	/**
	 * 拖拽至目标ID
	 */

	private Integer toId;
	
	/**升序ID**/
	private Integer nextId;

	@NotNull
	@Digits(integer = 11, fraction = 0)
	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	@NotNull
	@Digits(integer = 11, fraction = 0)
	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public Integer getNextId() {
		return nextId;
	}

	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}

}
