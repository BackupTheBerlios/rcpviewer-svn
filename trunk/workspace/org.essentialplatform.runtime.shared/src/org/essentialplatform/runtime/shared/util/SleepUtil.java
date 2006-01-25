package org.essentialplatform.runtime.shared.util;

public final class SleepUtil {

	private SleepUtil() {}

	/**
	 * Sleeps, and if interrupted then continues as long as necessary.
	 * @param milliseconds
	 */
	public static void sleepUninterrupted(int milliseconds) {
		long t0 = System.currentTimeMillis();
		long toGo = t0 + milliseconds - System.currentTimeMillis();
		while (toGo > 0) {
			try {
				Thread.sleep(toGo); 
			} catch (InterruptedException ex) {
				// do nothing
			}
			toGo = t0 + milliseconds - System.currentTimeMillis();
		}
	}
}
