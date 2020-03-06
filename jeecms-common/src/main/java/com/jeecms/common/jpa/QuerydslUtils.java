package com.jeecms.common.jpa;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Querydsl工具类
 * 
 * @author tom
 * 
 */
public class QuerydslUtils {
	private static final Pattern ALIAS_MATCH;
	private static final Pattern COUNT_MATCH;
	private static final int VARIABLE_NAME_GROUP_INDEX = 4;
	private static final String IDENTIFIER = "[._[\\P{Z}&&\\P{Cc}&&\\P{Cf}&&\\P{P}]]+";
	private static final String COUNT_REPLACEMENT_TEMPLATE = "select count(%s) $5$6$7";
	private static final String SIMPLE_COUNT_VALUE = "$2";
	private static final String COMPLEX_COUNT_VALUE = "$3$6";
	private static final String ORDER_BY_PART = "(?iu)\\s+order\\s+by\\s+.*$";
	static final String IDENTIFIER_GROUP = String.format("(%s)", IDENTIFIER);

	static {

		StringBuilder builder = new StringBuilder();
		// from as starting delimiter
		builder.append("(?<=from)");
		// at least one space separating
		builder.append("(?:\\s)+");
		// Entity name, can be qualified (any
		builder.append(IDENTIFIER_GROUP);
		// exclude possible "as" keyword
		builder.append("(?:\\sas)*");
		// at least one space separating
		builder.append("(?:\\s)+");
		// the actual alias
		builder.append("(?!(?:where))(\\w+)");
		ALIAS_MATCH = compile(builder.toString(), CASE_INSENSITIVE);
		COUNT_MATCH = compile(builder.toString(), CASE_INSENSITIVE);
	}

	public static <T> List<T> list(JPAQuery<T> query, EntityPathBase<T> entityPath, Sort sort) {
		sorting(query, entityPath, sort);
		return query.fetch();
	}

	/**
	 * 查询list  不带排序
	 * @Title: list  
	 * @param query JPAQuery
	 * @param paginable  Paginable
	 * @param entityPath EntityPathBase
	 * @return: List 
	 */
	public static <T> List<T> list(JPAQuery<T> query, Paginable paginable, EntityPathBase<T> entityPath) {
		if (paginable != null) {
			sorting(query, entityPath, paginable.getSort());
			Integer firstResult = paginable.getOffset();
			if (firstResult != null && firstResult > 0) {
				query.offset(firstResult);
			}
			Integer maxResults = paginable.getMax();
			if (maxResults != null && maxResults > 0) {
				query.limit(maxResults);
			}
		} else {
			sorting(query, entityPath, null);
		}
		return query.fetch();
	}

	/**
	 * 查询list 带排序
	 * @Title: list  
	 * @param query JPAQuery
	 * @param paginable Paginable
	 * @param entityPath EntityPathBase
	 * @param sort Sort
	 * @return: List
	 */
	public static <T> List<T> list(JPAQuery<T> query, Paginable paginable, 
			EntityPathBase<T> entityPath, Sort sort) {
		if(sort ==null){
			sort = paginable.getSort();
		}
		sorting(query, entityPath, sort);
		Integer firstResult = paginable.getOffset();
		if (firstResult != null && firstResult > 0) {
			query.offset(firstResult);
		}
		Integer maxResults = paginable.getMax();
		if (maxResults != null && maxResults > 0) {
			query.limit(maxResults);
		}
		return query.fetch();
	}

	/**
	 * 查询单个对象 
	 * @Title: findOne  
	 * @param query JPAQuery
	 * @param entityPath EntityPathBase
	 * @return: T
	 */
	public static <T> T findOne(JPAQuery<T> query, EntityPathBase<T> entityPath) {
		Paginable paginable = new PaginableRequest(0, 1);
		Integer firstResult = paginable.getOffset();
		if (firstResult != null && firstResult > 0) {
			query.offset(firstResult);
		}
		Integer maxResults = paginable.getMax();
		if (maxResults != null && maxResults > 0) {
			query.limit(maxResults);
		}
		List<T> list = query.fetch();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static <T> Page<T> page(JPAQuery<T> query, Pageable pageable, EntityPathBase<T> entityPath, Sort sort) {
		sorting(query, entityPath, sort);
		return page(query, pageable, entityPath);
	}

	/**
	 * 查询 page
	 * @Title: page  
	 * @param query JPAQuery
	 * @param pageable Pageable
	 * @param entityPath EntityPathBase
	 * @return: Page 
	 */
	public static <T> Page<T> page(JPAQuery<T> query, Pageable pageable, EntityPathBase<T> entityPath) {
		sorting(query, entityPath, pageable.getSort());
		long total = query.fetchCount();
		List<T> content;
		if (total > pageable.getOffset()) {
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
			content = query.fetch();
		} else {
			content = Collections.emptyList();
		}
		Page<T> page = new PageImpl<T>(content, pageable, total);
		return page;
	}

	/**
	 * 排序
	 * @Title: sorting  
	 * @param query SimpleQuery
	 * @param entityPath EntityPath
	 * @param sort   Sort
	 * @return: void
	 */
	public static <T, Q extends SimpleQuery<Q>> void sorting(SimpleQuery<Q> query, EntityPath<T> entityPath,
			Sort sort) {
		if (entityPath == null || sort == null) {
			return;
		}
		PathBuilder<T> builder = new PathBuilder<T>(entityPath.getType(), entityPath.getMetadata());
		for (Order order : sort) {
			query.orderBy(toOrder(builder, order));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static OrderSpecifier<?> toOrder(PathBuilder builder, Order order) {
		Expression<Object> property = builder.get(order.getProperty());
		return new OrderSpecifier(
				order.isAscending() ? com.querydsl.core.types.Order.ASC : 
					com.querydsl.core.types.Order.DESC, property);
	}

	@Nullable
	public static String detectAlias(String query) {
		Matcher matcher = ALIAS_MATCH.matcher(query);
		return matcher.find() ? matcher.group(2) : null;
	}

	/**
	 * 获取count语句  参考 QueryUtils
	 * @Title: createCountQueryFor  
	 * @param originalQuery  源sql查询
	 * @param countProjection count分组条件
	 * @return: String
	 */
	public static String createCountQueryFor(String originalQuery, 
			@Nullable String countProjection) {
		Assert.hasText(originalQuery, "OriginalQuery must not be null or empty!");

		Matcher matcher = COUNT_MATCH.matcher(originalQuery);
		String countQuery = null;

		if (countProjection == null) {
			String variable = matcher.matches() ? matcher.group(VARIABLE_NAME_GROUP_INDEX) : null;
			boolean useVariable = variable != null && StringUtils.hasText(variable) 
					&& !variable.startsWith("new")
					&& !variable.startsWith("count(") && !variable.contains(",");

			String replacement = useVariable ? SIMPLE_COUNT_VALUE : COMPLEX_COUNT_VALUE;
			countQuery = matcher.replaceFirst(String.format(COUNT_REPLACEMENT_TEMPLATE, replacement));
		} else {
			countQuery = matcher.replaceFirst(String.format(COUNT_REPLACEMENT_TEMPLATE, countProjection));
		}

		return countQuery.replaceFirst(ORDER_BY_PART, "");
	}
}
