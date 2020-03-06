package com.jeecms.audit.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.constants.WebConstants;

public class AuditUtil {


	public static List<Integer> arrayToList(String scene) {
		String[] textScene = StringUtils.split(scene, WebConstants.ARRAY_SPT);
		List<Integer> auditScenes = new ArrayList<Integer>(textScene.length);
		for (String string : textScene) {
			auditScenes.add(Integer.valueOf(string));
		}
		return auditScenes;
	}
}
