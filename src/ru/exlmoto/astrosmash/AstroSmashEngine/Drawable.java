package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Drawable {

	private volatile Bitmap m_image = null;
	private volatile int m_xPosition = 0;
	private volatile int m_yPosition = 0;

	public void setImage(Context context, int resourceID) {
		this.m_image = BitmapFactory.decodeResource(context.getResources(), resourceID);
	}

	public void setImage(Bitmap bitmap) {
		this.m_image = bitmap;
	}

	public void setPosition(int x, int y) {
		this.m_xPosition = x;
		this.m_yPosition = y;
	}

	public void setX(int x) {
		this.m_xPosition = x;
	}

	public void setY(int y) {
		this.m_yPosition = y;
	}

	public void paint(Canvas canvas, Paint paint) {
		canvas.drawBitmap(m_image, this.m_xPosition, this.m_yPosition, paint);
	}

	protected int getWidth() {
		return this.m_image.getWidth();
	}

	protected int getHeight() {
		return this.m_image.getHeight();
	}

	public int getX() {
		return this.m_xPosition;
	}

	public int getY() {
		return this.m_yPosition;
	}

	public int getCenterX() {
		return this.m_xPosition + this.m_image.getWidth() / 2;
	}

	public int getCenterY() {
		return this.m_yPosition + this.m_image.getHeight() / 2;
	}

	protected Bitmap getImage() {
		return this.m_image;
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/Drawable.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
