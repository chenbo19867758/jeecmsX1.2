package com.jeecms.threadmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 线程消费者入口
 * @author: ztx
 * @date: 2018年8月27日 上午9:13:16
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SpringBootApplication
public class ThreadMsgApplication {
	static Logger logger = LoggerFactory.getLogger(ThreadMsgApplication.class);

	/**
	 * 线程消费者启动入口
	 */
	public static void main(String[] args) {
		SpringApplication.run(ThreadMsgApplication.class, args);
	}

}
