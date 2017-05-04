package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;

public class Enemy extends Collidable {

	private int m_hitScore = 0;
	private int m_groundScore = 0;
	private int m_enemyTypeId = -1;
	private int m_hitReaction = 0;
	private Bitmap[] m_xPlosionImages = new Bitmap[0];
	private int m_currentExplosionImage = 0;
	private IDeathListener m_deathListener = null;

	public Enemy() {
		reset();
	}

	public void reset() {
		super.reset();
		this.m_currentExplosionImage = 0;
	}

	public void setEnemyTypeId(int typeID) {
		this.m_enemyTypeId = typeID;
	}

	public int getEnemyTypeId() {
		return this.m_enemyTypeId;
	}

	public void setHitScore(int hitScore) {
		this.m_hitScore = hitScore;
	}

	public int getHitScore() {
		return this.m_hitScore;
	}

	public void setGroundScore(int groundScore) {
		this.m_groundScore = groundScore;
	}

	public int getGroundScore() {
		return this.m_groundScore;
	}

	public void setHitReaction(int hitReaction) {
		this.m_hitReaction = hitReaction;
	}

	public int getHitReaction() {
		return this.m_hitReaction;
	}

	public void setExplosionImages(Bitmap[] explosionImages) {
		this.m_xPlosionImages = explosionImages;
	}

	public Bitmap[] getExplosionImages() {
		return this.m_xPlosionImages;
	}

	public void paint(Canvas canvas, Paint paint) {
		if ((true == getCollided()) && (1 == this.m_hitReaction)) {
			if (this.m_currentExplosionImage < this.m_xPlosionImages.length) {
				Bitmap localImage = this.m_xPlosionImages[this.m_currentExplosionImage];
				// Draw exploding images
				canvas.drawBitmap(localImage, getX() + localImage.getWidth() / 3, getY() - localImage.getHeight() / 2 + 2, paint);
				this.m_currentExplosionImage += 1;
			}
			if (this.m_currentExplosionImage >= this.m_xPlosionImages.length) {
				this.m_deathListener.doneExploding(this);
			}
		} else {
			super.paint(canvas, paint);
		}
	}

	public void tick(long paramLong, GameWorld paramGameWorld) {
		if (false == getCollided()) {
			super.tick(paramLong, paramGameWorld);
		} else if (1 != this.m_hitReaction) {
			this.m_deathListener.doneExploding(this);
		}
	}

	public void setDeathListener(IDeathListener paramIDeathListener) {
		this.m_deathListener = paramIDeathListener;
	}

	public IDeathListener getDeathListener() {
		return this.m_deathListener;
	}

	protected boolean hasNextExplosionImage() {
		return this.m_currentExplosionImage < this.m_xPlosionImages.length;
	}

	protected Bitmap getNextExplosionImage() {
		Bitmap localImage = this.m_xPlosionImages[this.m_currentExplosionImage];
		this.m_currentExplosionImage += 1;
		return localImage;
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/Enemy.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
