package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.graphics.Rect;

public class Collidable extends Drawable {

	protected int m_nVelocityX = 0;
	protected int m_nVelocityY = 0;
	protected long m_nDuration = 0L;
	protected volatile boolean m_bCollided = false;
	protected long m_nAccumDistanceXTimesTime = 0L;
	protected long m_nAccumDistanceYTimesTime = 0L;

	public Collidable() {
		reset();
	}

	public void reset() {
		this.m_nVelocityX = 0;
		this.m_nVelocityY = 0;
		this.m_nDuration = 0L;
		this.m_bCollided = false;
		this.m_nAccumDistanceXTimesTime = 0L;
		this.m_nAccumDistanceYTimesTime = 0L;
	}

	public void moveX(int x) {
		int i = getX();
		setX(i + x);
	}

	public void moveY(int y) {
		int i = getY();
		setY(i + y);
	}

	public void setVelocity(int vX, int vY, long duration) {
		this.m_nVelocityX = vX;
		this.m_nVelocityY = vY;
		this.m_nDuration = duration;
	}

	public void tick(long paramLong, GameWorld paramGameWorld) {
		long l3 = this.m_nVelocityX * paramLong + this.m_nAccumDistanceXTimesTime;
		long l4 = this.m_nVelocityY * paramLong + this.m_nAccumDistanceYTimesTime;
		this.m_nAccumDistanceXTimesTime = (l3 % this.m_nDuration);
		this.m_nAccumDistanceYTimesTime = (l4 % this.m_nDuration);
		long l1 = l3 / this.m_nDuration;
		long l2 = l4 / this.m_nDuration;
		moveX((int)l1);
		moveY((int)l2);
	}

	public boolean intersects(Collidable paramCollidable) {
		return intersects(paramCollidable, 1, 1);
	}

	public boolean intersects(Collidable paramCollidable, int paramInt1, int paramInt2) {
		//		Incorrect decompiled code
		//		int i = getX();
		//		int j = i + paramInt1 * getWidth();
		//		int k = getY();
		//		int m = k + paramInt2 * getHeight();
		//		int n = paramCollidable.getX();
		//		int i1 = n + paramCollidable.getWidth();
		//		int i2 = paramCollidable.getY();
		//		int i3 = i2 + paramCollidable.getHeight();
		//		if (((n >= i) && (n < j)) ||
		//				((i1 >= i) && (i1 < j)) ||
		//				((i >= n) && (i < i1)) ||
		//				((j >= n) && (j < i1) &&
		//						(((i2 >= k) && (i2 < m)) ||
		//								((i3 >= k) && (i3 < m)) ||
		//								((k >= i2) && (k < i3)) ||
		//								((m >= i2) && (m < i3))))) {
		//			setCollided(true);
		//			paramCollidable.setCollided(true);
		//			return true;
		//		}
		//		return false;

		// Rectangles intersect
		Rect rectIn = new Rect(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		Rect rectOut = new Rect(
				paramCollidable.getX(),
				paramCollidable.getY(),
				paramCollidable.getX() + paramCollidable.getWidth(),
				paramCollidable.getY() + paramCollidable.getHeight());

		if (rectIn.intersect(rectOut)) {
			setCollided(true);
			paramCollidable.setCollided(true);
			return true;
		}
		return false;
	}

	public boolean getCollided() {
		return this.m_bCollided;
	}

	public void setCollided(boolean paramBoolean) {
		this.m_bCollided = paramBoolean;
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/Collidable.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
