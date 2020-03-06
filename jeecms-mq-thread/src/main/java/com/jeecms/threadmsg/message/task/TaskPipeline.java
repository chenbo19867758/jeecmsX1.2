package com.jeecms.threadmsg.message.task;

import java.util.Collection;
import java.util.Map;

/**
 * @Description:
 * @author: ztx
 * @date: 2019年1月22日 上午11:26:45
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface TaskPipeline extends Iterable<Task> {

	/**
	 * 新增集合
	 * @Title: addAll  
	 * @param its
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline addAll(Collection<Task> its);

	/**
	 * 添加头节点
	 * @Title: addFirst  
	 * @param e
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline addFirst(Task e);

	/**
	 * 添加尾节点
	 * @Title: addLast  
	 * @param e
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline addLast(Task e);

	/**
	 * 在operation节点前添加e
	 * @Title: addBefore  
	 * @param operation  task对应的操作标识符 {@link Task#operation()}
	 * @param e
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline addBefore(int operation, Task e);

	/**
	 * 在operation节点后添加e
	 * @Title: addAfter  
	 * @param operation task对应的操作标识符 {@link Task#operation()}
	 * @param e
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline addAfter(int operation, Task e);

	/**
	 * 删除节点
	 * @Title: remove  
	 * @param oprtation
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline remove(int oprtation);

	/**
	 * 将oldTask替换为newTask
	 * @Title: replace  
	 * @param oldTask 旧节点
	 * @param newTask 新节点
	 * @return      
	 * @return: TaskPipeline
	 */
	TaskPipeline replace(Task oldTask, Task newTask);

	/**
	 * 获取头节点
	 * @Title: first  
	 * @return      
	 * @return: Task
	 */
	Task first();

	/**
	 * 获取尾节点
	 * @Title: last  
	 * @return      
	 * @return: Task
	 */
	Task last();

	/**
	 * 根据操作标识符获取Task
	 * @Title: get  
	 * @param operation task对应的操作标识符 {@link Task#operation()}
	 * @return      
	 * @return: Task
	 */
	Task get(int operation);

	/**
	 * 返回计数器
	 * @Title: num  
	 * @return      
	 * @return: int
	 */
	int num();

	/**
	 * 将{@link Task} 链表转换成{@link Map} key为操作标识符
	 * @Title: toMap  
	 * @return      
	 * @return: Map<Integer,Task>
	 */
	Map<Integer, Task> toMap();
}
