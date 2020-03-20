区分：
1.前端代码放入jeecms-parent项目中启动
2.前端代码单独部署启动两种的访问方式不一样。

后台地址：http://localhost:8082/jeecms/index.html
前台地址：http://localhost:8082/login
默认用户名密码：  system  123456

前台登录访问的接口地址：http://localhost:8082/login
对应 com.jeecms.front.controller.LoginController.login

登录成功的时候访问：AuthenticationTokenFilter loginService.tryTokenLogin(httpRequest, httpResponse);

前端登录先进入：MyAuthenticationFailHandler，验证失败直接返回json数据
退出登录：
请求访问的路径：/logout
请求访问的路径：/csi-loginStatus.htm，在中继续调用 login get方式访问数据
请求访问的路径：/login
请求访问的路径：/csi-loginStatus.htm

后台登录访问的接口地址：http://localhost:8082/admin/login
对应 com.jeecms.admin.controller.auth.AdminLoginSubmitController.login

SpringBoot2 快速开发框架 
SpringMVC 5.0   
Spring5.0 
SpringSecurity  安全组件 
JPA（Hibernate实现） 
Spring-Data-JPA  持久层组件 
QueryDSL  持久层扩展 
Ehcache、Redis  缓存组件 
Lucene7.4、Elasticsearch6.8 索引组件 
Flowable6.4.2 工作流引擎 
Hanlp1.7（中文分词组件） 
Quartz 2.3.0 定时任务 
Druid1.1.6连接池 
geoip2  2.12.0 IP库 
Slf4j  日志 
Kaptcha 2.3.2 验证码 
FreeMarker 模板渲染 
JWT 0.9  TOKEN组件 
Fastjson 1.2.58  JSON组件 
Easypoi 3.2.0  导出组件 
Zxing 3.2.0 二维码组件 
sharding-sphere 3.0  数据库中间件组件 
spring-session-data-redis  session redis支持 
前端技术 
脚手架 vue-cli 3 
前端框架 vue 2.6.10 
路由 vue-router 3.0.3 
数据存储 vuex 3.0.1 
国际化 vue-i18n 8.11.2 
UI组件库 element-ui 2.11.1 
HTTP请求 axios 0.18.0 

JWT TOKEN + axios 实现权限控制。

JWT：Json web token 
JWT 参考https://www.cnblogs.com/zhuwenjoyce/p/11042425.html
实现类：TokenService

JWT包含了三个主要部分： Header(头信息).Payload(有效载荷).Signature(签名)，以" . "来进行分割

Jwt客户端存储状态可行性分析
1、前端首次访问后台，后台生成token，放在http header的Authorization里（官网推荐，可解决跨域cookie跨域问题），并且Authorization Type类型为Bearer，将token返回给前端。
2、后台生成token的过程，包括给token指定加密协议比如HS56，加密类型比如“JWT”，自定义数据比如uuid，还有最重要的是记得指定一个超级复杂的密钥，并且定期更换它，密钥用于jwt签名部分。还需要给jwt token指定一个合理的过期时间，这个也同样非常重要。
3、前端获取token后存储token，建议存入本地cookie。
4、前端再次发起后台API接口访问，此时需要带上token，也是放在http header中且Authorization Type类型为Bearer，后台需校验token的正确性后才可访问权限受限的API接口或其他资源。

问题：只要能正常解析, 就是校验成功?
答案：是的，只要能解析无异常，则认为校验成功。

JWT 安全性问题
https://zhuanlan.zhihu.com/p/93129166
安全建议
一般保证前两点基本就没什么漏洞了。
1.保证密钥的保密性
2.签名算法固定在后端，不以JWT里的算法为标准
3.避免敏感信息保存在JWT中
4.尽量JWT的有效时间足够短

FrontContextInterceptor 上下文信息拦截器 包括登录信息、权限信息、站点信息

后台登录入口：AdminLoginSubmitController.login

进入controller之前会进过三个过滤器：
AuthenticationTokenFilter
XssFilter
HeaderCorsFilter 
跨越过滤器 :
https://www.cnblogs.com/yuansc/p/9076604.html
http://www.ruanyifeng.com/blog/2016/04/cors.html