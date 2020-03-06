/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.constants;

import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 内容审核常量类
 *
 * @author: ljw
 * @date: 2019年12月16日 下午2:07:01
 */
public class ContentAuditConstants {

	/** 文本审核 **/
	public static final Integer AUDIT_TEXT = 1;
	/** 图片审核 **/
	public static final Integer AUDIT_PICTURE = 2;

	/** 文本审核场景1.色情识别 **/
	public static final int AUDIT_TEXT_SCENE_1 = 1;
	/** 文本审核场景2.暴恐识别 **/
	public static final int AUDIT_TEXT_SCENE_2 = 2;
	/** 文本审核场景3.政治敏感 **/
	public static final int AUDIT_TEXT_SCENE_3 = 3;
	/** 文本审核场景4.恶意推广 **/
	public static final int AUDIT_TEXT_SCENE_4 = 4;
	/** 文本审核场景5.低俗辱骂 **/
	public static final int AUDIT_TEXT_SCENE_5 = 5;
	/** 文本审核场景6.低质灌水 **/
	public static final int AUDIT_TEXT_SCENE_6 = 6;

	/** 图片审核场景1.暴恐违禁 **/
	public static final int AUDIT_PICTURE_SCENE_1 = 1;
	/** 图片审核场景2.文本色情 **/
	public static final int AUDIT_PICTURE_SCENE_2 = 2;
	/** 图片审核场景3.政治人物识别 **/
	public static final int AUDIT_PICTURE_SCENE_3 = 3;
	/** 图片审核场景4.官方违禁库 **/
	public static final int AUDIT_PICTURE_SCENE_4 = 4;

	/** 文本审核场景 **/
	public static Map<String, String> getTextScene() {
		Map<String, String> a = ImmutableMap.of("1", "色情识别", "2", "暴恐识别", "3", "政治敏感", "4", "恶意推广",
			"5", "低俗辱骂");
		Map<String, String> b = ImmutableMap.of("6", "低质灌水");
		Map<String, String> map = new LinkedHashMap<>(6);
		map.putAll(a);
		map.putAll(b);
		return map;
	}

	/** 图片审核场景 **/
	public static Map<String, String> getPictureScene() {
		Map<String, String> a = ImmutableMap.of("1", "暴恐违禁", "2", "文本色情", "3", "政治人物识别", "4", "官方违禁库");
		Map<String, String> map = new LinkedHashMap<>(4);
		map.putAll(a);
		return map;
	}

	/**
	 * 文本审核场景
	 *
	 * @Title: status
	 * @param status 状态值，
	 * @return
	 */
	public static String textScene(Integer status) {
		switch (status) {
			case AUDIT_TEXT_SCENE_1:
				return "色情识别";
			case AUDIT_TEXT_SCENE_2:
				return "暴恐识别";
			case AUDIT_TEXT_SCENE_3:
				return "政治敏感";
			case AUDIT_TEXT_SCENE_4:
				return "恶意推广";
			case AUDIT_TEXT_SCENE_5:
				return "低俗辱骂";
			case AUDIT_TEXT_SCENE_6:
				return "低质灌水";
			default:
				break;
		}
		return "";
	}

	/**
	 * 图片审核场景
	 *
	 * @Title: status
	 * @param status 状态值，
	 * @return
	 */
	public static String pictureScene(Integer status) {
		switch (status) {
			case AUDIT_PICTURE_SCENE_1:
				return "暴恐违禁";
			case AUDIT_PICTURE_SCENE_2:
				return "文本色情";
			case AUDIT_PICTURE_SCENE_3:
				return "政治人物识别";
			case AUDIT_PICTURE_SCENE_4:
				return "官方违禁库";
			default:
				break;
		}
		return "";
	}
}
