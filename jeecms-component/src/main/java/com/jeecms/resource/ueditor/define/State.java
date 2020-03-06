package com.jeecms.resource.ueditor.define;

import org.json.JSONException;

/**
 * 处理状态接口
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface State {
	
	/**
	 * 是否成功
	 * TODO
	 * @Title: isSuccess  
	 * @return      
	 * @return: boolean
	 */
	public boolean isSuccess ();
	
	/**
	 * 推送信息
	 * @Title: putInfo  
	 * @param name 名称
	 * @param val  值    
	 * @return: void
	 */
	public void putInfo( String name, String val );
	
	/**
	 * 推送信息
	 * @Title: putInfo  
	 * @param name 名称
	 * @param val  值    
	 * @return: void
	 */
	public void putInfo ( String name, long val );
	
	/**
	 * 转json字符串
	 * @Title: toJSONString  
	 * @return      
	 * @return: String
	 */
	public String toJSONString ();
	
	/**
	 * 转json字符串
	 * @Title: toJSONString2  
	 * @return
	 * @throws JSONException JSON异常     
	 * @return: String
	 */
	public String toJSONString2()throws JSONException ;

}
