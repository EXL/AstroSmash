package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.graphics.Bitmap;

public class SwappableEnemy extends Enemy {

	private Bitmap m_copyOfImage = null;
	private Bitmap m_swapImage = null;
	private int m_nSwapInterval = 0;
	private boolean m_bUseSwapImage = false;
	private long m_timeSinceLastSwap = 0L;

	public void setImage(Bitmap paramImage) {
		super.setImage(paramImage);
		this.m_copyOfImage = paramImage;
	}

	public void setSwapImage(Bitmap paramImage) {
		this.m_swapImage = paramImage;
	}

	public Bitmap getSwapImage() {
		return this.m_swapImage;
	}

	public void setSwapInterval(int paramInt) {
		this.m_nSwapInterval = paramInt;
	}

	public void tick(long paramLong, GameWorld paramGameWorld) {
		this.m_timeSinceLastSwap += paramLong;
		if (this.m_timeSinceLastSwap > this.m_nSwapInterval) {
			this.m_bUseSwapImage = (!this.m_bUseSwapImage);
			if (this.m_bUseSwapImage) {
				super.setImage(this.m_swapImage);
			} else {
				super.setImage(this.m_copyOfImage);
			}
			this.m_timeSinceLastSwap = 0L;
		}
		super.tick(paramLong, paramGameWorld);
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/SwappableEnemy.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
