package com.lgm.baseframe.common;

import java.util.ArrayList;

/**
 * http连接管理类
 */
public class ThreadPoolManager {
	public static final int MAX_CONNECTIONS = 5;


	private ArrayList<Runnable> active = new ArrayList<Runnable>();
	private ArrayList<Runnable> queue = new ArrayList<Runnable>();

	private static ThreadPoolManager instance = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return instance;
	}

	public void push(Runnable runnable) {
		queue.add(runnable);
		if (active.size() < MAX_CONNECTIONS)
			startNext();
	}

	private void startNext() {
		if (!queue.isEmpty()) {
			Runnable next = queue.get(0);
			queue.remove(0);
			active.add(next);

			Thread thread = new Thread(next);
			thread.start();
		}
	}
	public void didComplete(Runnable runnable) {
		active.remove(runnable);
		startNext();
	}


}
