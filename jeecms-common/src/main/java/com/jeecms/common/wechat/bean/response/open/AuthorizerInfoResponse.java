package com.jeecms.common.wechat.bean.response.open;

import java.util.List;

import com.jeecms.common.wechat.bean.response.open.BaseOpenResponse;

/**
 * {@link}https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx
 * 
 * @Description:获取授权方的帐号基本信息,返回结构参数
 * @author wulongwei
 * @date 2018年7月27日
 */
public class AuthorizerInfoResponse extends  BaseOpenResponse{

	/**授权方信息*/
	private AuthorizerInfo authorizerInfo;
	
	/**授权信息*/
    private AuthorizationInfo authorizationInfo;
    
    
	public AuthorizerInfo getAuthorizerInfo() {
		return authorizerInfo;
	}

	public void setAuthorizerInfo(AuthorizerInfo authorizerInfo) {
		this.authorizerInfo = authorizerInfo;
	}

	public AuthorizationInfo getAuthorizationInfo() {
		return authorizationInfo;
	}

	public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
		this.authorizationInfo = authorizationInfo;
	}

	/**授权方信息*/
	public class AuthorizerInfo {
		/**授权方昵称*/
		private String nickName;
		
		/**授权方头像*/
		private String headImg;
		
		/**授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号*/
		private ServiceTypeInfo serviceTypeInfo;
		
		/**授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，
		 * 4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证*/
		private VerifyTypeInfo verifyTypeInfo;
		
		/**授权方公众号的原始ID*/
		private String userName;
		
		/**公众号的主体名称*/
		private String principalName;
		
		/**用以了解以下功能的开通状况（0代表未开通，1代表已开通）： open_store:是否开通微信门店功能 open_scan:
		 * 是否开通微信扫商品功能 open_pay:是否开通微信支付功能 open_card:是否开通微信卡券功能 open_shake:是否开通微信摇一摇功能*/
		private BusinessInfo businessInfo;
		
		/**授权方公众号所设置的微信号，可能为空*/
		private String alias;
		
		/**二维码图片的URL，开发者最好自行也进行保存*/
		private String qrcodeUrl;
		
		
		private MiniProgramInfo miniProgramInfo;
		
		public class MiniProgramInfo{
			/**网络协议*/
			private List<Categories> categories;  
			
			private List<Network> network;
			
			private Integer visitStatus;
			
			public class Network{
				/**资源请求域名*/
				private String[] requestDomain;
				/**微信请求域名*/
				private String[] wsRequestDomain;
				/**上传域名*/
				private String[] uploadDomain;
				/**下载域名*/
				private String[] downloadDomain;
				public String[] getRequestDomain() {
					return requestDomain;
				}
				public void setRequestDomain(String[] requestDomain) {
					this.requestDomain = requestDomain;
				}
				public String[] getWsRequestDomain() {
					return wsRequestDomain;
				}
				public void setWsRequestDomain(String[] wsRequestDomain) {
					this.wsRequestDomain = wsRequestDomain;
				}
				public String[] getUploadDomain() {
					return uploadDomain;
				}
				public void setUploadDomain(String[] uploadDomain) {
					this.uploadDomain = uploadDomain;
				}
				public String[] getDownloadDomain() {
					return downloadDomain;
				}
				public void setDownloadDomain(String[] downloadDomain) {
					this.downloadDomain = downloadDomain;
				}
				
				
			}
			
			public class Categories{
				private String first;
				private String second;
				public String getFirst() {
					return first;
				}
				public void setFirst(String first) {
					this.first = first;
				}
				public String getSecond() {
					return second;
				}
				public void setSecond(String second) {
					this.second = second;
				}
				
			}

			public List<Categories> getCategories() {
				return categories;
			}

			public void setCategories(List<Categories> categories) {
				this.categories = categories;
			}

			public List<Network> getNetwork() {
				return network;
			}

			public void setNetwork(List<Network> network) {
				this.network = network;
			}

			public Integer getVisitStatus() {
				return visitStatus;
			}

			public void setVisitStatus(Integer visitStatus) {
				this.visitStatus = visitStatus;
			}
			
			
		} 
		
		
		public MiniProgramInfo getMiniProgramInfo() {
			return miniProgramInfo;
		}

		public void setMiniProgramInfo(MiniProgramInfo miniProgramInfo) {
			this.miniProgramInfo = miniProgramInfo;
		}

		public String getNickName() {
			return nickName;
		}
		
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		
		public String getHeadImg() {
			return headImg;
		}
		
		public void setHeadImg(String headImg) {
			this.headImg = headImg;
		}
		
		public ServiceTypeInfo getServiceTypeInfo() {
			return serviceTypeInfo;
		}
		
		public void setServiceTypeInfo(ServiceTypeInfo serviceTypeInfo) {
			this.serviceTypeInfo = serviceTypeInfo;
		}
		
		public VerifyTypeInfo getVerifyTypeInfo() {
			return verifyTypeInfo;
		}
		
		public void setVerifyTypeInfo(VerifyTypeInfo verifyTypeInfo) {
			this.verifyTypeInfo = verifyTypeInfo;
		}
		
		public String getUserName() {
			return userName;
		}
		
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public String getPrincipalName() {
			return principalName;
		}
		
		public void setPrincipalName(String principalName) {
			this.principalName = principalName;
		}
		
		public BusinessInfo getBusinessInfo() {
			return businessInfo;
		}
		
		public void setBusinessInfo(BusinessInfo businessInfo) {
			this.businessInfo = businessInfo;
		}
		
		public String getAlias() {
			return alias;
		}
		
		public void setAlias(String alias) {
			this.alias = alias;
		}
		
		public String getQrcodeUrl() {
			return qrcodeUrl;
		}
		
		public void setQrcodeUrl(String qrcodeUrl) {
			this.qrcodeUrl = qrcodeUrl;
		}
		
		/**授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号*/
		public class ServiceTypeInfo {
			private Short id;

			public Short getId() {
				return id;
			}

			public void setId(Short id) {
				this.id = id;
			}
			
			
		}
		
		/**授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，
		 * 4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证*/
		public class VerifyTypeInfo{
			private Short id;

			public Short getId() {
				return id;
			}

			public void setId(Short id) {
				this.id = id;
			}
			
			
		}
		
		/**用以了解以下功能的开通状况（0代表未开通，1代表已开通）*/
		public class BusinessInfo {
			
			/**open_store:是否开通微信门店功能 */
			private String openStore;
			
			/**open_scan:是否开通微信扫商品功能 */
			private String openScan;
			
			/**open_pay:是否开通微信支付功能*/
			private String openPay;
			
			/**open_card:是否开通微信卡券功能  */
			private String openCard;
			
			/** open_shake:是否开通微信摇一摇功能*/
			private String openShake;

			public String getOpenStore() {
				return openStore;
			}

			public void setOpenStore(String openStore) {
				this.openStore = openStore;
			}

			public String getOpenScan() {
				return openScan;
			}

			public void setOpenScan(String openScan) {
				this.openScan = openScan;
			}

			public String getOpenPay() {
				return openPay;
			}

			public void setOpenPay(String openPay) {
				this.openPay = openPay;
			}

			public String getOpenCard() {
				return openCard;
			}

			public void setOpenCard(String openCard) {
				this.openCard = openCard;
			}

			public String getOpenShake() {
				return openShake;
			}

			public void setOpenShake(String openShake) {
				this.openShake = openShake;
			}
			
			
			
		}
	}
   
	/**授权信息*/
	public class AuthorizationInfo {
		
		/**授权方appid  ,微信文档标注的属性名与实际返回参数不一致，
		 * 文档中标注的为authorization_appid，实际返回为authorizer_appid*/
		private String authorizerAppid;
		
		/**公众号授权给开发者的权限集列表*/
		private List<FuncInfo> funcInfo;

		public String getAuthorizerAppid() {
			return authorizerAppid;
		}

		public void setAuthorizerAppid(String authorizerAppid) {
			this.authorizerAppid = authorizerAppid;
		}

		public List<FuncInfo> getFuncInfo() {
			return funcInfo;
		}


		public void setFuncInfo(List<FuncInfo> funcInfo) {
			this.funcInfo = funcInfo;
		}




		/**公众号授权给开发者的权限集列表*/
		public class FuncInfo {
			/**ID为1到15时分别代表： 1.消息管理权限 2.用户管理权限 3.帐号服务权限 4.网页服务权限 5.微信小店权限 6.微信多客服权限 
			 * 7.群发与通知权限 8.微信卡券权限 9.微信扫一扫权限 10.微信连WIFI权限 11.素材管理权限 12.微信摇周边权限 
			 * 13.微信门店权限 14.微信支付权限 15.自定义菜单权限 请注意： 1）该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），
			 * 请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。*/
			private FuncscopeCategory funcscopeCategory;
									

			public FuncscopeCategory getFuncscopeCategory() {
				return funcscopeCategory;
			}


			public void setFuncscopeCategory(FuncscopeCategory funcscopeCategory) {
				this.funcscopeCategory = funcscopeCategory;
			}


			public class FuncscopeCategory {
				private String id;

				public String getId() {
					return id;
				}

				public void setId(String id) {
					this.id = id;
				}
				
			}
		}
	}
}
