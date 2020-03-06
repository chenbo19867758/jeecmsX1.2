package com.jeecms.common.web.freemarker;

import com.jeecms.common.web.DateTypeEditor;
import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContext;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.web.servlet.view.AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE;

/**
 * Freemarker标签工具类
 *
 * @author: tom
 * @date: 2018年12月27日 上午9:29:56
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class DirectiveUtils {
	/**
	 * 输出参数：对象数据
	 */
	public static final String OUT_BEAN = "tag_bean";
	/**
	 * 输出参数：列表数据
	 */
	public static final String OUT_LIST = "tag_list";
	/**
	 * 输出参数：分页数据
	 */
	public static final String OUT_PAGINATION = "tag_pagination";
	/**
	 * 参数：是否调用模板。
	 */
	public static final String PARAM_TPL = "tpl";
	/**
	 * 参数：次级模板名称
	 */
	public static final String PARAM_TPL_SUB = "tplSub";

	public static final String INVOKE_TYPE_USERDEFINED = "3";
	public static final String INVOKE_TYPE_SYSDEFINED = "2";
	public static final String INVOKE_TYPE_CUSTOM = "1";

	/**
	 * 将params的值复制到variable中
	 *
	 * @param env    Environment
	 * @param params Map
	 * @throws TemplateException TemplateException
	 */
	public static Map<String, TemplateModel> addParamsToVariable(Environment env, Map<String, TemplateModel> params)
			throws TemplateException {
		Map<String, TemplateModel> origMap = new HashMap<String, TemplateModel>(5);
		if (params.size() <= 0) {
			return origMap;
		}
		Set<Map.Entry<String, TemplateModel>> entrySet = params.entrySet();
		String key;
		TemplateModel value;
		for (Map.Entry<String, TemplateModel> entry : entrySet) {
			key = entry.getKey();
			value = env.getVariable(key);
			if (value != null) {
				origMap.put(key, value);
			}
			env.setVariable(key, entry.getValue());
		}
		return origMap;
	}

	/**
	 * 将variable中的params值移除
	 *
	 * @param env     Environment
	 * @param params  参数
	 * @param origMap 原参数
	 * @throws TemplateException TemplateException
	 */
	public static void removeParamsFromVariable(Environment env, Map<String, TemplateModel> params,
												Map<String, TemplateModel> origMap) throws TemplateException {
		if (params.size() <= 0) {
			return;
		}
		for (String key : params.keySet()) {
			env.setVariable(key, origMap.get(key));
		}
	}

	/**
	 * 获得RequestContext ViewResolver中的exposeSpringMacroHelpers必须为true
	 *
	 * @param env Environment
	 * @throws TemplateException TemplateException
	 */
	public static RequestContext getContext(Environment env) throws TemplateException {
		TemplateModel ctx = env.getGlobalVariable(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE);
		if (ctx instanceof AdapterTemplateModel) {
			return (RequestContext) ((AdapterTemplateModel) ctx).getAdaptedObject(RequestContext.class);
		} else {
			throw new TemplateModelException(
					"RequestContext '" + SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE + "' not found in DataModel.");
		}
	}

	/**
	 * 获取标签字符串参数值
	 *
	 * @param name   参数名
	 * @param params Map
	 * @throws TemplateException TemplateException
	 * @Title: getString
	 * @return: String
	 */
	public static String getString(String name, Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) model).getAsString();
		} else if ((model instanceof TemplateNumberModel)) {
			return ((TemplateNumberModel) model).getAsNumber().toString();
		} else {
			throw new MustStringException(name);
		}
	}

	/**
	 * 获取标签长整型参数值
	 *
	 * @param name   参数名
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getLong
	 * @return: Long
	 */
	public static Long getLong(String name, Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().longValue();
		} else {
			throw new MustNumberException(name);
		}
	}

	/**
	 * 获取标签整型参数值
	 *
	 * @param name   参数名
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getInt
	 * @return: Long
	 */
	public static Integer getInt(String name, Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			if (StringUtils.isNumeric(s)) {
				try {
					return Integer.parseInt(s);
				} catch (NumberFormatException e) {
					throw new MustNumberException(name);
				}
			} else {
				return null;
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().intValue();
		} else {
			throw new MustNumberException(name);
		}
	}

	/**
	 * 获取标签double参数值
	 *
	 * @param name   参数名
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getDouble
	 * @return: Long
	 */
	public static Double getDouble(String name, Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().doubleValue();
		} else {
			throw new MustNumberException(name);
		}
	}

	/**
	 * 获取标签整形数组参数值
	 *
	 * @param name   参数名
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getIntArray
	 * @return: Long
	 */
	public static Integer[] getIntArray(String name, Map<String, TemplateModel> params) throws TemplateException {
		String str = DirectiveUtils.getString(name, params);
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] arr = StringUtils.split(str, ',');
		Integer[] ids = new Integer[arr.length];
		int i = 0;
		try {
			for (String s : arr) {
				ids[i++] = Integer.valueOf(s);
			}
			return ids;
		} catch (NumberFormatException e) {
			throw new MustSplitNumberException(name, e);
		}
	}

	/**
	 * 获取标签布尔参数值
	 *
	 * @param name   参数名
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getBool
	 * @return: Long
	 */
	public static Boolean getBool(String name, Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model != null) {
			if (model instanceof TemplateBooleanModel) {
				return ((TemplateBooleanModel) model).getAsBoolean();
			} else if (model instanceof TemplateNumberModel) {
				if (((TemplateNumberModel) model).getAsNumber().intValue() == 0) {
					return false;
				}
				return true;
			} else if (model instanceof TemplateScalarModel) {
				String s = ((TemplateScalarModel) model).getAsString();
				// 空串应该返回null还是true呢？
				if (!StringUtils.isBlank(s)) {
					return !("0".equals(s) || "false".equalsIgnoreCase(s) || s.equalsIgnoreCase("f"));
				}
			} else {
				throw new MustBooleanException(name);
			}
		}
		return null;
		//throw new MustBooleanException(name);
	}

	/**
	 * 获取标签时间参数值
	 *
	 * @param name   参数名
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getDate
	 * @return: Long
	 */
	public static Date getDate(String name, Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model != null) {
			if (model instanceof TemplateDateModel) {
				return ((TemplateDateModel) model).getAsDate();
			} else if (model instanceof TemplateScalarModel) {
				DateTypeEditor editor = new DateTypeEditor();
				editor.setAsText(((TemplateScalarModel) model).getAsString());
				return (Date) editor.getValue();
			} else {
				throw new MustDateException(name);
			}
		} else {
			return null;
			//throw new MustDateException(name);
		}
	}

	/**
	 * 获取标签所有前缀的key
	 *
	 * @param prefix 前缀
	 * @param params map
	 * @throws TemplateException TemplateException
	 * @Title: getDate
	 * @return: Long
	 */
	public static Set<String> getKeysByPrefix(String prefix, Map<String, TemplateModel> params) {
		Set<String> keys = params.keySet();
		Set<String> startWithPrefixKeys = new HashSet<String>();
		if (keys == null) {
			return null;
		}
		for (String key : keys) {
			if (key.startsWith(prefix)) {
				startWithPrefixKeys.add(key);
			}
		}
		return startWithPrefixKeys;
	}

	/**
	 * 模板调用类型
	 */
	public enum InvokeType {
		/**
		 * body
		 */
		body,
		/**
		 * 自定义
		 */
		custom,
		/**
		 * 系统定义
		 */
		sysDefined,
		/**
		 * 用户定义
		 */
		userDefined
	}

	;

	/**
	 * 是否调用模板 0：不调用，使用标签的body；1：调用自定义模板；2：调用系统预定义模板；3：调用用户预定义模板。默认：0。
	 *
	 * @param params map
	 * @return
	 */
	public static InvokeType getInvokeType(Map<String, TemplateModel> params) throws TemplateException {
		String tpl = getString(PARAM_TPL, params);
		if (INVOKE_TYPE_USERDEFINED.equals(tpl)) {
			return InvokeType.userDefined;
		} else if (INVOKE_TYPE_SYSDEFINED.equals(tpl)) {
			return InvokeType.sysDefined;
		} else if (INVOKE_TYPE_CUSTOM.equals(tpl)) {
			return InvokeType.custom;
		} else {
			return InvokeType.body;
		}
	}
}
