package ru.exlmoto.astrosmash.AstroSmashEngine;

public class Ufo extends Enemy {

	private int m_nFireInterval = 0;
	private long m_timeSinceFired = 0L;

	public void setFireInterval(int paramInt) {
		this.m_nFireInterval = paramInt;
	}

	public void tick(long paramLong, GameWorld paramGameWorld) {
		this.m_timeSinceFired += paramLong;
		if (this.m_timeSinceFired > this.m_nFireInterval) {
			paramGameWorld.fireUfoBullet(getCenterX(), getCenterY());
			this.m_timeSinceFired = 0L;
		}
		super.tick(paramLong, paramGameWorld);
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/Ufo.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
