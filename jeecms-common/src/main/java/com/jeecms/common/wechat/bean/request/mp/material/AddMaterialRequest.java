package com.jeecms.common.wechat.bean.request.mp.material;

/**
 * 
 * @Description: 新增其它类型永久素材：request
 * @author: chenming
 * @date:   2018年7月30日 下午2:38:34     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AddMaterialRequest extends BaseMaterialRequest{
	/**
	 * 文件路径
	 */
	private  String fileName;

	public AddMaterialRequest(String type, String fileName) {
		super(type);
		this.fileName = fileName;
	}

	public AddMaterialRequest() {
		super();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
