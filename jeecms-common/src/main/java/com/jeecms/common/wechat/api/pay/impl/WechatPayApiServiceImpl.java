package com.jeecms.common.wechat.api.pay.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.pay.WechatPayApiService;
import com.jeecms.common.wechat.bean.request.pay.OrderqueryRequest;
import com.jeecms.common.wechat.bean.request.pay.RefundRequest;
import com.jeecms.common.wechat.bean.request.pay.RefundqueryRequest;
import com.jeecms.common.wechat.bean.request.pay.UnifiedorderRequest;
import com.jeecms.common.wechat.bean.response.pay.OrderqueryResponse;
import com.jeecms.common.wechat.bean.response.pay.RefundResponse;
import com.jeecms.common.wechat.bean.response.pay.RefundqueryResponse;
import com.jeecms.common.wechat.bean.response.pay.UnifiedorderResponse;
import com.jeecms.common.wechat.bean.response.pay.common.RefundVoucher;
import com.jeecms.common.wechat.bean.response.pay.common.RefundqueryDto;
import com.jeecms.common.wechat.bean.response.pay.common.RefundqueryVoucher;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 统一下单接口service接口实现类
 * @author: chenming
 * @date:   2018年9月12日 下午1:46:50     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class WechatPayApiServiceImpl implements WechatPayApiService{
	/** 统一下单*/
	public final String UNIFIEDORDER=Const.DoMain.MCH_URI.concat("/pay/unifiedorder");
	/** 申请退款*/
	public final String REFUND=Const.DoMain.MCH_URI.concat("/secapi/pay/refund");
	/** 查询订单*/
	public final String ORDERQUERY=Const.DoMain.MCH_URI.concat("/pay/orderquery");
	/** 证书路径*/
	public final String CERT_PATH=this.getClass().getClassLoader().getResource("").getPath()+"cert/apiclient_cert.p12";
	/** 查询退款*/
	public final String REFUNDQUERY=Const.DoMain.MCH_URI.concat("/pay/refundquery");
	
	//public final String PAYBANK=Const.DOMAIN.WECHATPAY_URI.concat("/mmpaysptrans/pay_bank");
	//public final String GETPUBLICKEY=Const.DOMAIN.RISK_URL.concat("/risk/getpublickey");
	
	@Override
	public UnifiedorderResponse unifiedOrder(UnifiedorderRequest unifiedorderRequest) throws GlobalException {
		UnifiedorderResponse unifiedorderJsapiResponse=HttpUtil.postXmlBean(UNIFIEDORDER, null, SerializeUtil.beanToXml(unifiedorderRequest), UnifiedorderResponse.class);
		if (Const.Mssage.SUCCESS.equals(unifiedorderJsapiResponse.getReturnCode())) {
			if (Const.Mssage.SUCCESS.equals(unifiedorderJsapiResponse.getResultCode())) {
				return unifiedorderJsapiResponse;
			}else {
				throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),unifiedorderJsapiResponse.getErrCodeDes()));
			}
		}else {
			throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),unifiedorderJsapiResponse.getReturnMsg()));
		}
	}

	/**
	 * 微信申请退款、查询退款的两个接口返回的有几种属性类型，有可能有多个所以需要进行额外的处理。
	 * 
	 * 首先将含有数字结尾的节点另外取出作为另外的xml字符串。
	 * 
	 * 不含有数字结尾的xml字符串可以直接通过SerializeUtil.xmlToBean方法直接解析成对象。
	 * 
	 * 含有数字结尾的xml字符串需要转成map而后通过循环的方式进行取值。
	 */
	
	
	@Override
	public RefundResponse refund(RefundRequest refundRequest,String mchId) throws Exception {
		String xml=HttpUtil.post(REFUND, mchId, this.CERT_PATH, null, SerializeUtil.beanToXml(refundRequest));
		List<String> xmlList=this.xmlList(xml);
		RefundResponse refundResponse=SerializeUtil.xmlToBean(xmlList.get(0), RefundResponse.class);
		Map<String, String> map=WechatPayApiServiceImpl.parseXml(xmlList.get(1));
		List<RefundVoucher> rVouchers=new ArrayList<>();
		for (int i = 0; i < refundResponse.getCouponRefundCount(); i++) {
			RefundVoucher rVoucher=new RefundVoucher();
			rVoucher.setCouponType(map.get("coupon_type"+"_"+i));
			rVoucher.setCouponRefundId(map.get("coupon_refund_id"+"_"+i));
			rVoucher.setCouponRefundFee(map.get("coupon_refund_fee"+"_"+i));
			rVouchers.add(rVoucher);
		}
		refundResponse.setrVouchers(rVouchers);
		if (Const.Mssage.SUCCESS.equals(refundResponse.getReturnCode())) {
			if (Const.Mssage.SUCCESS.equals(refundResponse.getResultCode())) {
				return refundResponse;
			}else {
				throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),refundResponse.getErrCodeDes()));
			}
		}else {
			throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),refundResponse.getReturnMsg()));
		}
	}

	@Override
	public OrderqueryResponse orderquery(OrderqueryRequest request) throws GlobalException {
		OrderqueryResponse response=HttpUtil.postXmlBean(ORDERQUERY, null, SerializeUtil.beanToXml(request), OrderqueryResponse.class);
		if (Const.Mssage.SUCCESS.equals(response.getReturnCode())) {
			if (Const.Mssage.SUCCESS.equals(response.getResultCode())) {
				return response;
			}else {
				throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),response.getErrCodeDes()));
			}
		}else {
			throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),response.getReturnMsg()));
		}
	}

	@Override
	public RefundqueryResponse refundquery(RefundqueryRequest request) throws Exception {
		String xml=HttpUtil.post(REFUNDQUERY, null, SerializeUtil.beanToXml(request));
		List<String> xmlList=this.xmlList(xml);
		RefundqueryResponse response=SerializeUtil.xmlToBean(xmlList.get(0), RefundqueryResponse.class);
		Map<String, String> map=WechatPayApiServiceImpl.parseXml(xmlList.get(1));
		List<RefundqueryDto> rDtos=new ArrayList<>();
		for (int i = 0; i < response.getRefundCount(); i++) {
			RefundqueryDto rDto=new RefundqueryDto();
			List<RefundqueryVoucher> rVouchers=new ArrayList<>();
			rDto.setRefundStatus(map.get("refund_status"+"_"+i));
			rDto.setRefundAccount(map.get("refund_account"+"_"+i));
			rDto.setRefundRecvAccout(map.get("refund_recv_accout"+"_"+i));
			rDto.setRefundSuccessTime(map.get("refund_success_time"+"_"+i));
			rDto.setCouponRefundFee(map.get("coupon_refund_fee"+"_"+i));
			rDto.setCouponRefundCount(map.get("coupon_refund_count"+"_"+i));
			rDto.setRefundFee(map.get("refund_fee"+"_"+i));
			rDto.setSettlementRefundFee(map.get("settlement_refund_fee"+"_"+i));
			rDto.setOutRefundNo(map.get("out_refund_no"+"_"+i));
			rDto.setRefundId(map.get("refund_id"+"_"+i));
			if (map.get("refund_channel"+"_"+i)!=null) {
				rDto.setRefundChannel(map.get("refund_channel"+"_"+i));
			}
			if (rDto.getCouponRefundCount()!=null) {
				for (int j = 0; j < Integer.valueOf(rDto.getCouponRefundCount()); j++) {
					RefundqueryVoucher rVoucher=new RefundqueryVoucher();
					rVoucher.setCouponType(map.get("coupon_type"+"_"+i+j));
					rVoucher.setCouponRefundId(map.get("coupon_refund_id"+"_"+i+j));
					rVoucher.setCouponRefundFee(Long.valueOf(map.get("coupon_refund_fee"+"_"+i+j)));
					rVouchers.add(rVoucher);
				}
			}
			rDto.setRefundVouchers(rVouchers);
			rDtos.add(rDto);
		}
		response.setRefundqueryDtos(rDtos);
		if (Const.Mssage.SUCCESS.equals(response.getReturnCode())) {
			if (Const.Mssage.SUCCESS.equals(response.getResultCode())) {
				return response;
			}else {
				throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),response.getErrCodeDes()));
			}
		}else {
			throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),response.getReturnMsg()));
		}
	}

	public static Map<String, String> parseXml(String xml) throws Exception{
		Map<String, String> map=new HashMap<String, String>(16);
		//从dom4j的jar包中，拿到SAXReader对象
		SAXReader reader=new SAXReader();
		//从read对象中，读取输入流
		Document doc=reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		//获取XML文档根元素
		Element root=doc.getRootElement();
		//获取根元素下的所有的子节点
		@SuppressWarnings("unchecked")
		List<Element> list=root.elements();
		for (Element e : list) {
			//遍历list对象，并将结果保存到集合中
			map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	public List<String> xmlList(String xml) throws Exception{
		SAXReader reader=new SAXReader();
		InputStream in=new ByteArrayInputStream(xml.getBytes("UTF-8"));
		List<String> docs=new ArrayList<>();
		Document doc=reader.read(in);
		Element root=doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> empList=root.elements();
		Iterator<Element> it=empList.iterator();
		List<Element> otherList=new ArrayList<>();
		while (it.hasNext()) {
			Element element=it.next();
			if (element.getName().matches("^.*\\d$")) {
				otherList.add(element);
				it.remove();           
			}
		}
		Document otherDoc=DocumentHelper.createDocument();
		Element otherRoot=otherDoc.addElement("root");
		for (Element element : otherList) {
			Element newElement=otherRoot.addElement(element.getName());
			newElement.addText(element.getText());
		}
		docs.add(doc.asXML());
		docs.add(otherDoc.asXML());
		return docs;
	}
	
}
