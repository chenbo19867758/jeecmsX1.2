package com.jeecms.common.wechat.util.client.httpclient;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 自定义HttpClient构建工具.
 * @author: tom
 * @date:   2019年3月8日 下午4:43:43
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class HttpClientBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientBuilder.class);
    private static final int DEFAULT_MAX_TOTAL = 10;
    private static final int DEFAULT_MAX_PER_ROUTE = 100;
    private static final int DEFAULT_TIME_OUT = 60000;
    
    /** 线程池大小 */
    private final int maxTotal; 
    /** 连接超时时间 */
    private final int timeout; 
    private final int maxPerRoute;
    /** 重试次数 */
    private final int retryTimes; 
    /** http代理 */
    private final HttpHost proxy; 

    HttpClientBuilder(int maxTotal, int timeout, int maxPerRoute, int retryTimes, HttpHost proxy) {
        this.maxTotal = maxTotal;
        this.timeout = timeout;
        this.maxPerRoute = maxPerRoute;
        this.retryTimes = retryTimes;
        this.proxy = proxy;
    }

    private SSLConnectionSocketFactory buildSslConnectionSocketFactory() {
        try {
        	// 优先绕过安全证书
            return new SSLConnectionSocketFactory(createIgnoreVerifySsl()); 
        } catch (Exception e) {
            LOGGER.error("ssl connection fail", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySsl() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
            	return new  X509Certificate[0];
            }
        };

        SSLContext sc = SSLContexts.custom().build();
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
    
    
    private SSLConnectionSocketFactory createSSLConnectionSocketFactory(String mchId,String certPath) {
		try {
			//指定TLS版本
			SSLConnectionSocketFactory sslsf;
			 //指定读取证书格式为PKCS12
			 KeyStore keyStore = KeyStore.getInstance("PKCS12");
			 //读取本机存放的PKCS12证书文件
			 FileInputStream instream = new FileInputStream(new File(certPath));
			  try {
				  //指定PKCS12的密码(商户ID)
				  keyStore.load(instream, mchId.toCharArray());
			  } finally {
				  instream.close();
			  }
			  SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
			  sslsf = new SSLConnectionSocketFactory(
			  sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			  return sslsf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SSLConnectionSocketFactory.getSocketFactory();
   }
    

    private CloseableHttpClient getClient(boolean isCert,String mchId,String certPath) {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", isCert ? createSSLConnectionSocketFactory(mchId, certPath) : buildSslConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        connectionManager.setMaxTotal(maxTotal);

        org.apache.http.impl.client.HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(timeout);
        SocketConfig socketConfig = socketConfigBuilder.build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);

        if (proxy != null) {
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            httpClientBuilder.setRoutePlanner(routePlanner);
        } else {
            String proxyHost = System.getProperty("proxyHost");
            String proxyPort = System.getProperty("proxyPort");
            if (StringUtils.isNotBlank(proxyHost) && StringUtils.isNotBlank(proxyPort)) {
                DefaultProxyRoutePlanner routePlanner =
                        new DefaultProxyRoutePlanner(new HttpHost(proxyHost, Integer.valueOf(proxyPort)));
                httpClientBuilder.setRoutePlanner(routePlanner);
            }
        }

        if (retryTimes > 0) {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(retryTimes, true));
        }
        return httpClientBuilder.build();
    }


    public static HttpClientBuilder.Builder custom() {
        return new HttpClientBuilder.Builder();
    }

    public static class Builder {
        private int maxTotal;
        private int timeout;
        private int maxPerRoute;
        private int retryTimes;
        private HttpHost proxy;

        Builder() {
            this.maxTotal = DEFAULT_MAX_TOTAL;
            this.timeout = DEFAULT_TIME_OUT;
            this.maxPerRoute = DEFAULT_MAX_PER_ROUTE;
            this.retryTimes = 0;
        }

        public HttpClientBuilder.Builder timeout(final int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpClientBuilder.Builder maxTotal(final int maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public HttpClientBuilder.Builder maxPerRoute(final int maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
            return this;
        }

        public HttpClientBuilder.Builder retryTimes(final int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public HttpClientBuilder.Builder proxy(final HttpHost proxy) {
            this.proxy = proxy;
            return this;
        }

        public CloseableHttpClient build(boolean isUserCert,String mchId,String certPath) {
            return (new HttpClientBuilder(maxTotal, timeout, maxPerRoute, retryTimes, proxy)).getClient(isUserCert,mchId,certPath);
        }
    }
}
