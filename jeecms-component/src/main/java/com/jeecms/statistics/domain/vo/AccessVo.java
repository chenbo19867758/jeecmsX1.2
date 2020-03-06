/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.statistics.domain.vo;

/**   
 * 忠诚度VO
 * @author: ljw
 * @date:   2019年6月27日 上午10:56:28     
 */
public class AccessVo {

	/**深度，页数，时长关键字**/
	private Integer key;
	/**统计次数**/
	private Integer count;
	
	public AccessVo() {
		
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
