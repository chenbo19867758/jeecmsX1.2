package com.jeecms.common.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;

import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.jeecms.common.util.MyDateUtils;

/**
 * 搜索过滤器（动态查询条件构造） 例如需要查询属性ftpName等于某值，则参数名为 query_EQ_ftpName
 * 
 * @author tom
 */
public class SearchFilter {

	public enum Operator {
		/**
		 * 等于
		 */
		EQ,
		/**
		 * 模糊查询
		 */
		LIKE,
		/**
		 * 字符串等于
		 */
		STR_EQ,
		/**
		 * 开始字符串
		 */
		STARTWITH,
		/**
		 * 结束字符串
		 */
		ENDWITH,
		/**
		 * 大于
		 */
		GT,
		/**
		 * 小于
		 */
		LT,
		/**
		 * 大于等于
		 */
		GTE,
		/**
		 * 小于等于
		 */
		LTE,
		/**
		 * 在范围内
		 */
		IN,
		/**
		 * 为空查询
		 */
		NULL,
		/**
		 * 不为空查询
		 */
		NOTNULL
	}

	public enum Type {
		/**
		 * String
		 */
		String, 
		/**
		 * Integer
		 */
		Integer, 
		/**
		 * Long
		 */
		Long, 
		/**
		 * Float
		 */
		Float, 
		/**
		 * Double
		 */
		Double, 
		/**
		 * BigDecimal
		 */
		BigDecimal, 
		/**
		 * BigInteger
		 */
		BigInteger, 
		/**
		 * Boolean
		 */
		Boolean, 
		/**
		 * Date
		 */
		Date, 
		/**
		 * Timestamp
		 */
		Timestamp;
		/**
		 * 转换
		 * @Title: convert  
		 * @param strArray 字符串数组
		 * @param type Type
		 * @param operator Operator 
		 * @return: Object[]
		 */
		public static Object[] convert(String[] strArray, Type type, Operator operator) {
			if (ArrayUtils.isEmpty(strArray)) {
				return null;
			}
			List<Object> list = new ArrayList<Object>();
			for (int i = 0, len = strArray.length; i < len; i++) {
				String str = strArray[i];
				if (StringUtils.isBlank(str)) {
					continue;
				}
				switch (type) {
					case Integer:
						list.add(NumberUtils.createInteger(str));
						break;
					case Long:
						list.add(NumberUtils.createLong(str));
						break;
					case Float:
						list.add(NumberUtils.createDouble(str));
						break;
					case Double:
						list.add(NumberUtils.createDouble(str));
						break;
					case BigDecimal:
						list.add(NumberUtils.createBigDecimal(str));
						break;
					case BigInteger:
						list.add(NumberUtils.createBigInteger(str));
						break;
					case Boolean:
						list.add(java.lang.Boolean.valueOf(str));
						break;
					case Date:
						java.util.Date d = MyDateUtils.parseDate(str, 
								MyDateUtils.COM_Y_M_D_PATTERN);
						if (str.length() <= 10 && operator == Operator.LTE) {
							d = MyDateUtils.getFinallyDate(d);
						}
						list.add(d);
						break;
					case Timestamp:
						list.add(MyDateUtils.parseDate(str, 
								MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
						break;
					default:
						list.add(str);
				}
			}
			return list.toArray();
		}
	}

	public String fieldName;
	public Object[] value;
	public Operator operator;

	
	/**
	 * 构造器
	 * @param fieldName 字段名
	 * @param operator  Operator 操作
	 * @param value 值数组
	 */
	public SearchFilter(String fieldName, Operator operator, Object[] value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * 转换获取Specification 
	 * @Title: spec  
	 * @param filters  SearchFilter
	 * @param clazz Class
	 * @param filterDel 是否过滤逻辑删除的数据  true 则过滤
	 * @return: Specification
	 */
	@SuppressWarnings("serial")
	public static <T> Specification<T> spec(final Collection<SearchFilter> filters, final Class<T> clazz,
			boolean filterDel) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (CollectionUtils.isNotEmpty(filters)) {
					List<Predicate> predicates = getPredicateList(filters, root, builder);
					if (filterDel && AbstractDelFlagDomain.class.isAssignableFrom(clazz)) {
						predicates.add(builder.equal(root.get("hasDeleted"), false));
					}
					// 将所有条件用 and 联合起来
					if (predicates.size() > 0) {
						return builder.and(predicates.toArray(
								new Predicate[predicates.size()]));
					}
				}
				Predicate pred = builder.conjunction();
				// 追加通用删除标识查询
				if (filterDel && AbstractDelFlagDomain.class.isAssignableFrom(clazz)) {
					pred = builder.and(pred, builder.equal(root.get("hasDeleted"), false));
				}
				return pred;
			}
		};
	}
	
	/**
	 * 转换获取Specification 
	 * @Title: spec  
	 * @param params Map 查询参数
	 * @param clazz  Class
	 * @param filterDel 是否过滤逻辑删除的数据  true 则过滤
	 * @return: Specification
	 */
	public static <T> Specification<T> spec(Map<String, String[]> params, final Class<T> clazz, boolean filterDel) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<T> fsp = SearchFilter.spec(filters, clazz, filterDel);
		return (Specification<T>) fsp;
	}


	/**
	 * 转换获取 Predicate
	 * @Title: getPredicateList  
	 * @param filters SearchFilter 集合
	 * @param root Root
	 * @param builder CriteriaBuilder
	 * @return: List
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<Predicate> getPredicateList(final Collection<SearchFilter> filters, Root<T> root,
			CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (SearchFilter filter : filters) {
			// nested path translate, 如Task的名为"user.name"的filedName,
			// 转换为Task.user.name属性
			String[] names = StringUtils.split(filter.fieldName, ".");
			Path exp = root;
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				if (name.startsWith("J")) {
					name = name.substring(1);
					exp = ((From) exp).join(name);
				} else {
					exp = exp.get(name);
				}
			}
			// operator
			switch (filter.operator) {
				case EQ:
					predicates.add(builder.equal(exp, filter.value[0]));
					break;
				case STR_EQ:
					predicates.add(builder.like(exp, filter.value[0].toString()));
					break;
				case LIKE:
					predicates.add(builder.like(exp, "%" + filter.value[0] + "%"));
					break;
				case STARTWITH:
					predicates.add(builder.like(exp, filter.value[0] + "%"));
					break;
				case ENDWITH:
					predicates.add(builder.like(exp, "%" + filter.value[0]));
					break;
				case GT:
					predicates.add(builder.greaterThan(exp, (Comparable) filter.value[0]));
					break;
				case LT:
					predicates.add(builder.lessThan(exp, (Comparable) filter.value[0]));
					break;
				case GTE:
					predicates.add(builder.greaterThanOrEqualTo(exp, (Comparable) filter.value[0]));
					break;
				case LTE:
					predicates.add(builder.lessThanOrEqualTo(exp, (Comparable) filter.value[0]));
					break;
				case IN:
					predicates.add(exp.in(filter.value));
					break;
				case NULL:
					predicates.add(builder.isNull(exp));
					break;
				case NOTNULL:
					predicates.add(builder.isNotNull(exp));
					break;
				default:
					predicates.add(builder.equal(exp, filter.value[0]));
					break;
			}
		}
		return predicates;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME 传递参数例如 query_LIKE_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, String[]> params) {
		if (params == null || params.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>(5);

		for (Entry<String, String[]> entry : params.entrySet()) {
			// 过滤掉空值且(NULL_开头的KEY不过滤)
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (ArrayUtils.isEmpty(values) && !key.startsWith("NULL_")) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length < 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			Operator operator = Operator.valueOf(names[0]);
			String filedName = names[1];
			Type type = Type.String;
			if (names.length >= 3) {
				type = Type.valueOf(names[2]);
			}
			Object[] ovalues = Type.convert(values, type, operator);
			if (ArrayUtils.isEmpty(ovalues) && !key.startsWith("NULL_")) {
				continue;
			}
			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, ovalues);
			filters.put(key, filter);
		}
		return filters;
	}
}
