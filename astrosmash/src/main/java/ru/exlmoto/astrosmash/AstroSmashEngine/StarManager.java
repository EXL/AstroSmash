package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;
import ru.exlmoto.astrosmash.AstroSmashView;

public class StarManager {

	public static final int NUMBER_OF_STARS = 50;
	public static final int BLACKCOLOR = Version.BLACKCOLOR;
	public static final int WHITECOLOR = Version.WHITECOLOR;
	int[] colorArray = {
			Color.parseColor("#9AAFFF"),
			Color.parseColor("#CAD7FF"),
			Color.parseColor("#F8F7FF"),
			Color.parseColor("#F8F7FF"),
			Color.parseColor("#F8F7FF"),
			Color.parseColor("#F8F7FF"),
			Color.parseColor("#F8F7FF"),
			Color.parseColor("#FFF2A1"),
			Color.parseColor("#FFE46F"),
			Color.parseColor("#FFA040")
	};
	private int m_numStars;
	private int m_width;
	private int m_height;
	private Bitmap m_image;
	private int[] m_xPos;
	private int[] m_yPos;

	private Canvas bitmapCanvas = null;
	private Paint bitmapPaint = null;

	public StarManager(int paramInt1, int paramInt2, int paramInt3, int color) {
		this.m_numStars = paramInt3;
		this.m_width = paramInt1;
		this.m_height = paramInt2;
		this.m_image = Bitmap.createBitmap(this.m_width, this.m_height, Bitmap.Config.ARGB_8888);
		this.m_xPos = new int[this.m_numStars];
		this.m_yPos = new int[this.m_numStars];

		bitmapCanvas = new Canvas(this.m_image);
		bitmapPaint = new Paint();

		generateStars();
		generateStarImage(color);
	}

	public StarManager(int paramInt1, int paramInt2, int paramInt3) {
		this(paramInt1, paramInt2, 50, paramInt3);
	}

	public void paint(Canvas canvas, Paint paint) {
		canvas.drawBitmap(this.m_image, 0, 0, paint);
	}

	public void generateStars() {
		for (int i = 0; i < this.m_numStars; i++) {
			this.m_xPos[i] = (AstroSmashView.getAbsRandomInt() % this.m_width);
			this.m_yPos[i] = (AstroSmashView.getAbsRandomInt() % this.m_height);
		}
	}

	private int getRandomStarColor() {
		return colorArray[AstroSmashView.getRandomIntBetween(0, colorArray.length)];
	}

	public void generateStarImage(int color) {
		bitmapCanvas.drawColor(color);
		for (int i = 0; i < this.m_numStars; i++) {
			if (AstroSmashSettings.colorizeStars) {
				bitmapPaint.setColor(getRandomStarColor());
			} else {
				bitmapPaint.setColor(Version.WHITECOLOR);
			}
			bitmapCanvas.drawPoint(this.m_xPos[i], this.m_yPos[i], bitmapPaint);
		}
	}

	public Bitmap getStarImage() {
		return this.m_image;
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/StarManager.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
