package zju.lzq.util;

import java.util.Date;

public class Timer {
	private static long id = 0;
	private long ord;
	private long startTime;
	private long stopTime;
	private boolean startFlag;
	private String className;
	private String methodName;

	public Timer(String className, String methodName) {
		++id;
		ord = id;
		this.className = className;
		this.methodName = methodName;
		init();
	}

	public void init() {
		startTime = 0;
		stopTime = 0;
		startFlag = false;
	}

	public void start() {
		if (startFlag) {
			System.out.println(getHeader() + "the timer has been started.");
		} else {
			System.out.println(getHeader() + "the timer is about to start.");
			startTime = System.currentTimeMillis();
			startFlag = true;
		}
	}

	public void stop() {
		if (!startFlag) {
			System.out.println(getHeader() + "the timer has not been started.");
		} else {
			stopTime = System.currentTimeMillis();
			System.out.println(getHeader() + "the timer is stopped.");
			System.out.println(getHeader() + "the process costs " + (stopTime - startTime) / 1000.0 + "s.");
		}
	}

	public static Timer getTimer(String className, String methodName) {
		return new Timer(className, methodName);
	}

	public static Timer getStartedTimer(String className, String methodName) {
		Timer timer = new Timer(className, methodName);
		timer.start();
		return timer;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	private String getHeader() {
		return new Date() + " [timer " + ord + "] " + className + "#" + methodName + ": ";
	}

	public boolean isStartFlag() {
		return startFlag;
	}

	public static void sleep(long seconds) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < seconds * 1000) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return "Timer [ord=" + ord + ", startTime=" + startTime + ", stopTime=" + stopTime + ", startFlag=" + startFlag
				+ ", className=" + className + ", methodName=" + methodName + "]";
	}

}
