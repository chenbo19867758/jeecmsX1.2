package com.jeecms.threadmsg.message.task;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.collect.ImmutableMap;

/**
 * @Description: 任务队列
 * @author: ztx
 * @date: 2019年1月21日 下午4:21:56
 * @param <T>
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class DefaultTaskPipeline implements TaskPipeline, Serializable {
	private static final long serialVersionUID = 1L;

	/** 当前任务队列对应的操作标识符 */
	private int operation;
	/** key为task的操作符,value为task */
	private Map<Integer, Node<Task>> taskOperMap = new HashMap<>();

	private Node<Task> first;
	private Node<Task> last;

	public DefaultTaskPipeline() {
		super();
	}

	public DefaultTaskPipeline(Collection<Task> tasks) {
		addAll(tasks);
	}

	/**
	 * 减去一个操作标识符
	 */
	private int reduce(int operation) {
		if ((this.operation & operation) != 0) {
			this.operation = this.operation & ~operation;
			taskOperMap.remove(operation);
		}
		return this.operation;
	}

	/**
	 * 加一个标识符
	 */
	private int add(Node<Task> node) {
		int operation = node.item.operation();
		this.operation |= operation;
		taskOperMap.put(operation, node);
		return this.operation;
	}

	@Override
	public int num() {
		return this.operation;
	}

	@Override
	public Iterator<Task> iterator() {
		return toMap().values().iterator();
	}

	@Override
	public TaskPipeline addFirst(Task e) {
		final Node<Task> f = first;
		final Node<Task> newNode = new Node<>(null, e, f);
		first = newNode;
		if (f == null) {
			// first为空时, first==last==newNode
			last = newNode;
		} else {
			f.prev = newNode;
		}
		add(newNode);
		return this;
	}

	@Override
	public TaskPipeline addLast(Task e) {
		final Node<Task> l = last;
		final Node<Task> newNode = new Node<>(l, e, null);
		last = newNode;
		if (l == null) {
			// last为空时,first==last==newNode
			first = newNode;
		} else {
			l.next = newNode;
		}
		add(newNode);
		return this;
	}

	@Override
	public TaskPipeline addBefore(int operation, Task e) {
		final Node<Task> node = getNode(operation, true);
		if (node == first) {
			addFirst(e);
			return this;
		}
		final Node<Task> oldPre = node.prev;
		final Node<Task> newNode = new Node<Task>(oldPre, e, node);
		oldPre.next = newNode;
		node.prev = newNode;
		add(newNode);
		return this;
	}

	@Override
	public TaskPipeline addAfter(int operation, Task e) {
		final Node<Task> node = getNode(operation, true);
		if (node == last) {
			addLast(e);
			return this;
		}
		final Node<Task> oldAfter = node.next;
		final Node<Task> newNode = new Node<Task>(node, e, oldAfter);
		oldAfter.prev = newNode;
		node.next = newNode;
		add(newNode);
		return this;
	}

	@Override
	public TaskPipeline remove(int oprtation) {
		Node<Task> node = getNode(oprtation, true);
		Node<Task> prev = node.prev;
		Node<Task> next = node.next;
		if (node == first) {
			first = next;
		}
		if (node == last) {
			last = prev;
		}
		prev.next = next;
		next.prev = prev;
		int operation = node.item.operation();
		reduce(operation);
		node = null;
		return this;
	}

	@Override
	public TaskPipeline replace(Task oldTask, Task newTask) {
		if (oldTask == null || newTask == null) {
			throw new IllegalArgumentException();
		}
		int oldOperation = oldTask.operation();
		Node<Task> oldNode = getNode(oldOperation, true);
		final Node<Task> newNode = new Node<Task>(newTask);
		// 判断是否为头节点或尾节点
		if (oldNode == first) {
			first = newNode;
		}
		if (oldNode == last) {
			last = newNode;
		}
		newNode.prev = oldNode.prev;
		newNode.next = oldNode.next;
		oldNode = null;
		reduce(oldOperation);
		add(newNode);
		return this;
	}

	@Override
	public Task first() {
		return first == null ? null : first.item;
	}

	@Override
	public Task last() {
		return last == null ? null : last.item;
	}

	@Override
	public Task get(int operation) {
		Node<Task> node = getNode(operation, false);
		return node == null ? null : node.item;
	}

	@Override
	public TaskPipeline addAll(Collection<Task> its) {
		Node<Task> newNode = null;
		Node<Task> prev = last == null ? null : last.prev;
		for (Task it : its) {
			newNode = new Node<Task>(prev, it, null);
			if (prev == null) {
				first = last = newNode;
			} else {
				prev.next = newNode;
			}
			prev = newNode;
			add(newNode);
		}
		return this;
	}

	@Override
	public Map<Integer, Task> toMap() {
		if (first == null) {
			return ImmutableMap.of();
		}
		Map<Integer, Task> result = new LinkedHashMap<>(taskOperMap.size());
		Node<Task> next = first;
		Task task = null;
		do {
			task = next.item;
			result.put(task.operation(), task);
			next = next.next;
		} while (next != null);
		return result;
	}

	private Node<Task> getNode(int key, boolean throwErr) {
		Node<Task> node = taskOperMap.get(key);
		if (node == null && throwErr) {
			throw new NoSuchElementException(key + "");
		}
		return node;
	}

	private static class Node<E extends Task> {
		E item;
		Node<E> next;
		Node<E> prev;

		public Node(E item) {
			this.item = item;
			this.next = null;
			this.prev = null;
		}

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

}
