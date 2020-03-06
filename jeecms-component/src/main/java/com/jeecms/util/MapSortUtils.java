package com.jeecms.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map集合根据key值排序
 * 
 * @author ASUS
 *
 */
public class MapSortUtils {

	/**
	 * 根据key值降序
	 * 
	 * @param map
	 * @return
	 */
	public static <K extends Comparable<? super K>, V> Map<K, V> descByKey(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();

		map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

	/**
	 * 根据key值升序
	 * 
	 * @param map
	 * @return
	 */
	public static <K extends Comparable<? super K>, V> Map<K, V> ascByKey(Map<K, V> map) {
		Map<K, V> result = new TreeMap<>();

		map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}
}
