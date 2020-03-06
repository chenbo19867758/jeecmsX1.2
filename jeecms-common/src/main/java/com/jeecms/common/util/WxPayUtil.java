package com.jeecms.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jeecms.common.util.WxPayConstants.SignType;
import com.jeecms.common.wechat.bean.request.pay.OrderqueryRequest;
import com.jeecms.common.wechat.bean.request.pay.RefundRequest;
import com.jeecms.common.wechat.bean.request.pay.RefundqueryRequest;
import com.jeecms.common.wechat.bean.request.pay.UnifiedorderRequest;
import com.jeecms.common.wechat.bean.response.pay.AppPayResult;
import com.jeecms.common.wechat.bean.response.pay.JsPayResult;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 微信支付官方工具类
 * 
 * @author: chenming
 * @date: 2018年10月12日 下午4:39:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class WxPayUtil {

	private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final Random RANDOM = new SecureRandom();

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param strXML
	 *            XML字符串
	 * @return XML数据转换后的Map
	 * @throws Exception
	 *             Exception
	 */
	public static Map<String, String> xmlToMap(String strXML) throws Exception {
		try {
			Map<String, String> data = new HashMap<String, String>(16);
			DocumentBuilder documentBuilder = WxPayXmlUtil.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(), element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				// do nothing
			}
			return data;
		} catch (Exception ex) {
			WxPayUtil.getLogger().warn("Invalid XML, "
					+ "can not convert to map. " + "Error message: {}. XML content: {}",
					ex.getMessage(), strXML);
			throw ex;
		}

	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data
	 *            Map类型数据
	 * @return XML格式的字符串
	 * @throws Exception
	 *             Exception
	 */
	public static String mapToXml(Map<String, String> data) throws Exception {
		org.w3c.dom.Document document = WxPayXmlUtil.newDocument();
		org.w3c.dom.Element root = document.createElement("xml");
		document.appendChild(root);
		for (String key : data.keySet()) {
			String value = data.get(key);
			if (value == null) {
				value = "";
			}
			value = value.trim();
			org.w3c.dom.Element filed = document.createElement(key);
			filed.appendChild(document.createTextNode(value));
			root.appendChild(filed);
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		//liuqidong XML External Entity Injection代码安全处理
		tf.setFeature("http://xml.org/sax/features/external-general-entities",false);
		tf.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(document);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		// .replaceAll("\n|\r", "");
		String output = writer.getBuffer().toString();
		try {
			writer.close();
		} catch (Exception ex) {
			WxPayUtil.getLogger().error(ex.getMessage());
		}
		return output;
	}

	/**
	 * 生成带有 sign 的 XML 格式字符串
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @return 含有sign字段的XML
	 */
	public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
		return generateSignedXml(data, key, SignType.MD5);
	}

	/**
	 * 生成带有 sign 的 XML 格式字符串
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名类型
	 * @return 含有sign字段的XML
	 */
	public static String generateSignedXml(final Map<String, String> data, String key, SignType signType)
			throws Exception {
		String sign = generateSignature(data, key, signType);
		data.put(WxPayConstants.FIELD_SIGN, sign);
		return mapToXml(data);
	}

	/**
	 * 判断签名是否正确
	 *
	 * @param xmlStr
	 *            XML格式数据
	 * @param key
	 *            API密钥
	 * @return 签名是否正确
	 * @throws Exception
	 *             Exception
	 */
	public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
		Map<String, String> data = xmlToMap(xmlStr);
		if (!data.containsKey(WxPayConstants.FIELD_SIGN)) {
			return false;
		}
		String sign = data.get(WxPayConstants.FIELD_SIGN);
		return generateSignature(data, key).equalsIgnoreCase(sign);
	}

	/**
	 * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @return 签名是否正确
	 * @throws Exception
	 *             Exception
	 */
	public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
		return isSignatureValid(data, key, SignType.MD5);
	}

	/**
	 * 判断签名是否正确，必须包含sign字段，否则返回false。
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名方式
	 * @return 签名是否正确
	 * @throws Exception
	 *             Exception
	 */
	public static boolean isSignatureValid(Map<String, String> data,
			String key, SignType signType) throws Exception {
		if (!data.containsKey(WxPayConstants.FIELD_SIGN)) {
			return false;
		}
		String sign = data.get(WxPayConstants.FIELD_SIGN);
		return generateSignature(data, key, signType).equalsIgnoreCase(sign);
	}

	/**
	 * 生成签名
	 *
	 * @param data
	 *            待签名数据
	 * @param key
	 *            API密钥
	 * @return 签名
	 */
	public static String generateSignature(final Map<String, String> data, String key) throws Exception {
		return generateSignature(data, key, SignType.MD5);
	}

	/**
	 * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
	 *
	 * @param data
	 *            待签名数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名方式
	 * @return 签名
	 */
	public static String generateSignature(final Map<String, String> data, String key, SignType signType)
			throws Exception {
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		for (String k : keyArray) {
			if (k.equals(WxPayConstants.FIELD_SIGN)) {
				continue;
			}
			if (data.get(k) != null) {
				// 参数值为空，则不参与签名
				if (data.get(k).trim().length() > 0) {
					sb.append(k).append("=").append(data.get(k).trim()).append("&");
				}
			}
		}
		sb.append("key=").append(key);
		if (SignType.MD5.equals(signType)) {
			return md5(sb.toString()).toUpperCase();
		} else if (SignType.HMACSHA256.equals(signType)) {
			return hmacSha256(sb.toString(), key);
		} else {
			throw new Exception(String.format("Invalid sign_type: %s", signType));
		}
	}

	/**
	 * 获取随机字符串 Nonce Str
	 *
	 * @return String 随机字符串
	 */
	public static String generateNonceStr() {
		char[] nonceChars = new char[32];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}

	/**
	 * 生成 MD5
	 *
	 * @param data
	 *            待处理数据
	 * @return MD5结果
	 */
	public static String md5(String data) throws Exception {
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 生成 HMACSHA256
	 * 
	 * @param data
	 *            待处理数据
	 * @param key
	 *            密钥
	 * @return 加密结果
	 * @throws Exception
	 *             Exception
	 */
	public static String hmacSha256(String data, String key) throws Exception {
		Mac sha256Hmac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256Hmac.init(secretKey);
		byte[] array = sha256Hmac.doFinal(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 日志
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("wxpay java sdk");
		return logger;
	}

	/**
	 * 获取当前时间戳，单位秒
	 * 
	 * @return
	 */
	public static long getCurrentTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map<String, String> unifiedorder(UnifiedorderRequest unifiedorderRequest) {
		Map<String, String> returnMap = new HashMap<>(16);
		returnMap.put("appid", unifiedorderRequest.getAppid());
		returnMap.put("total_fee", unifiedorderRequest.getTotalFee() + "");
		returnMap.put("attach", unifiedorderRequest.getAttach());
		returnMap.put("body", unifiedorderRequest.getBody());
		returnMap.put("detail", unifiedorderRequest.getDetail());
		returnMap.put("device_info", unifiedorderRequest.getDeviceInfo());
		returnMap.put("fee_type", unifiedorderRequest.getFeeType());
		returnMap.put("goods_tag", unifiedorderRequest.getGoodsTag());
		returnMap.put("limit_pay", unifiedorderRequest.getLimitPay());
		returnMap.put("mch_id", unifiedorderRequest.getMchId());
		returnMap.put("nonce_str", unifiedorderRequest.getNonceStr());
		returnMap.put("notify_url", unifiedorderRequest.getNotifyUrl());
		returnMap.put("openid", unifiedorderRequest.getOpenid());
		returnMap.put("out_trade_no", unifiedorderRequest.getOutTradeNo());
		returnMap.put("product_id", unifiedorderRequest.getProductId());
		returnMap.put("scene_info", unifiedorderRequest.getSceneInfo());
		returnMap.put("sign_type", unifiedorderRequest.getSignType());
		returnMap.put("spbill_create_ip", unifiedorderRequest.getSpbillCreateIp());
		returnMap.put("time_expire", unifiedorderRequest.getTimeExpire());
		returnMap.put("time_start", unifiedorderRequest.getTimeStart());
		returnMap.put("trade_type", unifiedorderRequest.getTradeType());
		return returnMap;
	}

	/**
	 * 转换JsPayResult 为Map
	 * 
	 * @Title: jsPayResult
	 * @param jsPayResult
	 *            JsPayResult
	 * @return: Map
	 */
	public static Map<String, String> jsPayResult(JsPayResult jsPayResult) {
		Map<String, String> returnMap = new HashMap<>(16);
		returnMap.put("appId", jsPayResult.getAppId());
		returnMap.put("nonceStr", jsPayResult.getNonceStr());
		returnMap.put("package", jsPayResult.getPackageStr());
		returnMap.put("signType", jsPayResult.getSignType());
		returnMap.put("timeStamp", jsPayResult.getTimeStamp());
		return returnMap;
	}

	
	/**
	 * 转换AppPayResult为Map
	 * @Title: appPayResult  
	 * @param appPayResult AppPayResult
	 * @return: Map 
	 */
	public static Map<String,String> appPayResult(AppPayResult appPayResult) {
		Map<String, String> returnMap = new LinkedHashMap<String,String>(16);
		returnMap.put("appid", appPayResult.getAppId());
		returnMap.put("partnerid", appPayResult.getPartnerId());
		returnMap.put("prepayid", appPayResult.getPrepayId());
		returnMap.put("package", appPayResult.getPackageStr());
		returnMap.put("nonceStr", appPayResult.getNonceStr());
		returnMap.put("timeStamp", appPayResult.getTimeStamp());
		return returnMap;
	}
	
	
	/**
	 * 转换RefundRequest 为Map
	 * 
	 * @Title: refund
	 * @param refundRequest
	 *            RefundRequest
	 * @return: Map
	 */
	public static Map<String, String> refund(RefundRequest refundRequest) {
		Map<String, String> returnMap = new HashMap<>(16);
		returnMap.put("appid", refundRequest.getAppid());
		returnMap.put("nonce_str", refundRequest.getNonceStr());
		returnMap.put("mch_id", refundRequest.getMchId());
		returnMap.put("notify_url", refundRequest.getNotifyUrl());
		returnMap.put("out_refund_no", refundRequest.getOutRefundNo());
		returnMap.put("out_trade_no", refundRequest.getOutTradeNo());
		returnMap.put("refund_account", refundRequest.getRefundAccount());
		returnMap.put("refund_desc", refundRequest.getRefundDesc());
		returnMap.put("refund_fee_type", refundRequest.getRefundFeeType());
		returnMap.put("sub_appid", refundRequest.getSubAppid());
		returnMap.put("sub_mch_id", refundRequest.getSubMchId());
		returnMap.put("transaction_id", refundRequest.getTransactionId());
		returnMap.put("refund_fee", refundRequest.getRefundFee() + "");
		returnMap.put("total_fee", refundRequest.getTotalFee() + "");
		return returnMap;
	}

	/**
	 * 转换OrderqueryRequest 为Map
	 * 
	 * @Title: orderQuery
	 * @param orderqueryRequest
	 *            OrderqueryRequest
	 * @return: Map
	 */
	public static Map<String, String> orderQuery(OrderqueryRequest orderqueryRequest) {
		Map<String, String> returnMap = new HashMap<>(16);
		returnMap.put("appid", orderqueryRequest.getAppid());
		returnMap.put("mch_id", orderqueryRequest.getMchId());
		returnMap.put("transaction_id", orderqueryRequest.getTransactionId());
		returnMap.put("out_trade_no", orderqueryRequest.getOutTradeNo());
		returnMap.put("nonce_str", orderqueryRequest.getNonceStr());
		return returnMap;
	}

	/**
	 * 转换RefundqueryRequest 为Map
	 * 
	 * @Title: refundquery
	 * @param refundqueryRequest
	 *            RefundqueryRequest
	 * @return: Map
	 */
	public static Map<String, String> refundquery(RefundqueryRequest refundqueryRequest) {
		Map<String, String> returnMap = new HashMap<>(16);
		returnMap.put("appid", refundqueryRequest.getAppid());
		returnMap.put("mch_id", refundqueryRequest.getMchId());
		returnMap.put("nonce_str", refundqueryRequest.getNonceStr());
		returnMap.put("transaction_id", refundqueryRequest.getTransactionId());
		returnMap.put("out_trade_no", refundqueryRequest.getOutTradeNo());
		returnMap.put("out_refund_no", refundqueryRequest.getOutRefundNo());
		returnMap.put("refund_id", refundqueryRequest.getRefundId());
		returnMap.put("offset", String.valueOf(refundqueryRequest.getOffset()));
		return returnMap;
	}
}
