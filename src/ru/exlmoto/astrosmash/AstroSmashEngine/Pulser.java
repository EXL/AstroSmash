package ru.exlmoto.astrosmash.AstroSmashEngine;

public class Pulser extends SwappableEnemy {

	public void tick(long paramLong, GameWorld paramGameWorld) {
		Enemy localEnemy = paramGameWorld.getShip();
		int i = localEnemy.getCenterY() - getCenterY();
		if (i != 0) {
			int j = localEnemy.getCenterX() - getCenterX();
			long l = 1024L * j / i;
			this.m_nVelocityX = ((int)(l * this.m_nVelocityY / 1024L));
		} else {
			this.m_nVelocityX = 0;
		}
		super.tick(paramLong, paramGameWorld);
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/Pulser.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
