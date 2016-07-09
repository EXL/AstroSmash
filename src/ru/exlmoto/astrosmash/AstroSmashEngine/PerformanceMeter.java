package ru.exlmoto.astrosmash.AstroSmashEngine;

public class PerformanceMeter {

	private long m_lLastCallTime = 0L;

	public int getTimesPerSecond() {
		int i = 0;
		long l1 = System.currentTimeMillis();
		long l2 = l1 - this.m_lLastCallTime;
		if (0L != l2) {
			i = (int)(1000L / l2);
		} else {
			i = 0;
		}
		this.m_lLastCallTime = l1;
		return i;
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/PerformanceMeter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
