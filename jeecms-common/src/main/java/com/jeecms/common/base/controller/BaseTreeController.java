package com.jeecms.common.base.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractTreeDomain;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.ReflectUtils;

/**
 * Class controller层
 * 
 * @author: tom
 * @date: 2018年1月24日 下午15:40:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseTreeController<T extends AbstractTreeDomain<T, ID>, ID extends Serializable>
		extends BaseController<T, ID> {

	/**
	 * 递归获取树形结构
	 * 
	 * @param: @param
	 *             lists
	 * @param: @param
	 *             pid
	 * @param: @return
	 * @return: List tree list
	 */
	public List<T> getTree(List<T> lists, Integer pid) {
		List<T> arrys = new ArrayList<T>();
		for (T t : lists) {
			boolean check = false;
			if (t.getParentId() == pid) {
				check = true;
			}
			if (!check) {
				if (t.getParentId() != null && pid != null) {
					if (t.getParentId().intValue() == pid.intValue()) {
						check = true;
					}
				}
			}
			if (check) {
				t.setChilds(getTree(lists, (Integer) t.getId()));
				arrys.add(t);
			}
		}
		return arrys;

	}

	/**
	 * 解析子节点集合树
	 * 
	 * @Title: getChildTree （默认返回对象中的id ,parentId属性，如需要额外属性，通过fieldNames参数进行设置）
	 * @param: @param
	 *             childs
	 * @param: @param
	 *             lazyType 数据源是否需要懒加载处理
	 * @param: @param
	 *             fieldNames 显示额外的属性名称（必须是对象中的属性，支持field.field链式模式）
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: JSONArray
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getChildTree(Collection<T> childs, boolean lazyType, 
			String... fieldNames) throws GlobalException {
		JSONArray result = new JSONArray();
		if (null == childs || childs.size() == 0) {
			return result;
		}
		Integer parentId = childs.iterator().next().getParentId();
		JSONArray dataSource = new JSONArray();
		JSONObject jsonObject = null;
		if (lazyType) {
			childs = this.lazyToList(childs);
		}
		Map<Integer, JSONObject> hashDatas = new HashMap<>(childs.size());
		for (T t : childs) {
			jsonObject = new JSONObject();
			jsonObject.put("id", t.getId());
			for (String field : fieldNames) {
				jsonObject.put(field, ReflectUtils.getFieldValue(t, field));
			}
			jsonObject.put("parentId", t.getParentId());
			// 没有子节点则过滤childs
			long count = childs.stream().filter(
					c -> null != c.getParentId() 
					&& ((Integer) t.getId()).intValue() == c.getParentId().intValue()).count();
			if (count > 0) {
				jsonObject.put("children", new ArrayList<>());
			}
			dataSource.add(jsonObject);
			hashDatas.put((Integer) jsonObject.get("id"), jsonObject);
		}
		childs.clear();

		// 遍历菜单集合
		for (int i = 0; i < dataSource.size(); i++) {
			// 当前节点
			JSONObject json = (JSONObject) dataSource.get(i);
			// 当前的父节点
			JSONObject hashObject = hashDatas.get(json.get("parentId"));

			if (hashObject != null) {
				// 表示当前节点为子节点
				((List<JSONObject>) hashObject.get("children")).add(json);
			} else if (null == json.get("parentId") 
					|| parentId.intValue() == ((Integer) json.get("parentId"))) {
				// parentId为null和获取匹配parentId的节点(生成某节点的子节点树时需要用到)
				result.add(json);
			}
		}
		return result;
	}
	

	/**
	 * 解析子节点
	 */
	private List<T> lazyToList(Collection<T> datas) throws GlobalException {
		List<T> list = new ArrayList<>();
		if (null == datas) {
			return list;
		}
		for (T t : datas) {
			list.add(t);
			List<T> childs = t.getChilds();
			if (childs.size() > 0) {
				list.addAll(lazyToList(childs));
			}
		}
		return list;
	}

}
