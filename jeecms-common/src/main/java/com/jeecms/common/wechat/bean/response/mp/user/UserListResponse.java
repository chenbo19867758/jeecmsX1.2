package com.jeecms.common.wechat.bean.response.mp.user;

import java.util.List;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
/**
 * {@link}https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
 * @Description:获取帐号的关注者列表返回的接口。
 * 附：关注者数量超过10000时
 * 当公众号关注者数量超过10000时，可通过填写next_openid的值，从而多次拉取列表的方式来满足需求。
 * 具体而言，就是在调用接口时，将上一次调用得到的返回中的next_openid值，作为下一次调用中的next_openid值。
 * @author: ljw
 * @date:   2018年7月30日 上午10:05:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UserListResponse extends  BaseResponse{

	/**关注该公众账号的总用户数**/
	private Integer total;
	/**拉取的OPENID个数，最大值为10000**/
	private Integer count;
	/**列表数据，OPENID的列表**/
	private Data data;
	/**拉取列表的最后一个用户的OPENID**/
	private String nextOpenid;
	
	/**data数据对象**/
	public class Data {
		
		/**openid集合**/
		private List<String> openid;

		public List<String> getOpenid() {
			return openid;
		}

		public void setOpenid(List<String> openid) {
			this.openid = openid;
		}			
		
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getNextOpenid() {
		return nextOpenid;
	}

	public void setNextOpenid(String nextOpenid) {
		this.nextOpenid = nextOpenid;
	}
	
	
}
