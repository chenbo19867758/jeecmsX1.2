package com.jeecms.resource.ueditor;

/**
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class Encoder {

	public static String toUnicode ( String input ) {
		
		StringBuilder builder = new StringBuilder();
		char[] chars = input.toCharArray();
		
		for ( char ch : chars ) {
			
			if ( ch < 256 ) {
				builder.append( ch );
			} else {
				builder.append( "\\u" +  Integer.toHexString( ch& 0xffff ) );
			}
			
		}
		
		return builder.toString();
		
	}
	
}