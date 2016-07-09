package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import ru.exlmoto.astrosmash.R;
import ru.exlmoto.astrosmash.AstroSmashActivity;

@SuppressWarnings("unused")
public class BackgroundManager {

	public static final String MOUNTAINIMAGE = "/mountains.png";
	private static final int MAXLIFEDISPLAY = 4;
	private static final String[] NUMBERFONT_FILENAMES = { "/no0.png", "/no1.png", "/no2.png", "/no3.png",
			"/no4.png", "/no5.png", "/no6.png", "/no7.png", "/no8.png", "/no9.png" };
	private static final int[] NUMBERFONT_FILENAMES_AID = {
			R.drawable.no0,
			R.drawable.no1,
			R.drawable.no2,
			R.drawable.no3,
			R.drawable.no4,
			R.drawable.no5,
			R.drawable.no6,
			R.drawable.no7,
			R.drawable.no8,
			R.drawable.no9
	};
	private static final String X_FILENAME = "/noX.png";
	private static final String MINUS_FILENAME = "/minus.png";
	private boolean m_bRegenerateBackground = true;
	private int m_screenWidth = 0;
	private int m_screenHeight = 0;
	private StarManager m_starManager;
	private Bitmap m_image;
	private Bitmap m_mountain;
	private int m_nLevel = 1;
	private int m_nScore = 0;
	private int m_nLives = 0;
	private Bitmap[] m_numberFonts;
	private Bitmap m_xImage;
	private Bitmap m_greenShip;
	private Bitmap m_minusImage;
	private int m_nPeakScore;

	private Context activityContext = null;

	private Canvas bitmapCanvas = null;
	private Paint bitmapPaint = null;

	public BackgroundManager(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Context context) {
		AstroSmashActivity.toDebug("Background Manager: " + paramInt1 + ":" + paramInt2);
		this.activityContext = context;
		this.m_bRegenerateBackground = true;
		this.m_screenWidth = paramInt1;
		this.m_screenHeight = paramInt2;
		this.m_nLevel = paramInt3;
		this.m_nScore = paramInt4;
		this.m_nPeakScore = paramInt4;
		this.m_nLives = paramInt5;
		this.m_image = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
		this.m_mountain = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.mountains);
		int i = getGroundLevel() - this.m_mountain.getHeight() - Version.getMountainFootHeight();
		AstroSmashActivity.toDebug("Background Manager Mountian: " + i + ":" + this.m_mountain.getWidth() + ":" + this.m_mountain.getHeight());
		this.m_starManager = new StarManager(paramInt1, i, Version.BLACKCOLOR);
		this.m_numberFonts = new Bitmap[NUMBERFONT_FILENAMES.length];
		for (int j = 0; j < this.m_numberFonts.length; j++) {
			this.m_numberFonts[j] = BitmapFactory.decodeResource(activityContext.getResources(), NUMBERFONT_FILENAMES_AID[j]);
		}
		this.m_xImage = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.nox);
		this.m_greenShip = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.ship_green);
		this.m_minusImage = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.minus);

		bitmapCanvas = new Canvas(this.m_image);
		bitmapPaint = new Paint();
	}

	public void paint(Canvas canvas, Paint paint) {
		if (this.m_bRegenerateBackground) {
			regenerateBackground();
			this.m_bRegenerateBackground = false;
		}
		canvas.drawBitmap(this.m_image, 0, 0, paint);
	}

	public void setScore(int paramInt) {
		this.m_bRegenerateBackground = true;
		this.m_nScore = paramInt;
	}

	public void setLives(int paramInt) {
		this.m_bRegenerateBackground = true;
		this.m_nLives = paramInt;
	}

	public void setGameLevel(int paramInt) {
		this.m_bRegenerateBackground = true;
		this.m_nLevel = paramInt;
	}

	public int getGroundLevel() {
		int i = this.m_screenHeight - (Version.getStatisticsHeight() + Version.getGroundThickness());
		return i;
	}

	public void setPeakScore(int paramInt) {
		this.m_bRegenerateBackground = true;
		this.m_nPeakScore = paramInt;
	}

	protected void regenerateBackground() {
		int i = getGroundLevel();

		// Fill background by black color
		bitmapCanvas.drawColor(Version.BLACKCOLOR);

		// Draw stars on background
		bitmapCanvas.drawBitmap(this.m_starManager.getStarImage(), 0, 0, bitmapPaint);

		// Draw mountains (with fix)
		if (this.m_screenWidth > this.m_mountain.getHeight()) {
			for (int k = 0; k < this.m_screenWidth; k+=120) {
				bitmapCanvas.drawBitmap(this.m_mountain, k, i - Version.getMountainFootHeight() - this.m_mountain.getHeight(), bitmapPaint);
			}
		} else {
			bitmapCanvas.drawBitmap(this.m_mountain, 0, i - Version.getMountainFootHeight() - this.m_mountain.getHeight(), bitmapPaint);
		}

		// Set green color and draw ground line
		bitmapPaint.setColor(Version.GREENCOLOR);
		bitmapCanvas.drawRect(0, i, this.m_screenWidth, i + Version.getGroundThickness(), bitmapPaint);

		// Draw status screen rect
		bitmapCanvas.drawBitmap(this.m_xImage, this.m_screenWidth - Version.getStringRightPadding() - this.m_xImage.getWidth(), this.m_screenHeight - 1 - this.m_xImage.getHeight(), bitmapPaint);
		paintNumber(bitmapCanvas, bitmapPaint, this.m_nLevel, this.m_screenWidth - Version.getStringRightPadding() - this.m_xImage.getWidth(), this.m_screenHeight - 1);
		drawLives(bitmapCanvas, bitmapPaint, this.m_screenWidth / 2, this.m_screenHeight - 1);
		paintNumber(bitmapCanvas, bitmapPaint, this.m_nScore, this.m_screenWidth / 2 - Version.getStringRightPadding(), this.m_screenHeight - 1);
	}

	protected int paintNumber(Canvas canvas, Paint paint, int paramInt1, int paramInt2, int paramInt3) {
		int j = 0;
		int k = 0;
		k = paramInt1 < 0 ? 1 : 0;
		paramInt1 = Math.abs(paramInt1);
		if (0 == paramInt1) {
			canvas.drawBitmap(this.m_numberFonts[0], paramInt2 - this.m_numberFonts[0].getWidth(), paramInt3 - this.m_numberFonts[0].getHeight(), paint);
			j = this.m_numberFonts[0].getWidth();
		} else {
			while (paramInt1 > 0) {
				int i = paramInt1 % 10;
				canvas.drawBitmap(this.m_numberFonts[i], paramInt2 - j - this.m_numberFonts[i].getWidth(), paramInt3 - this.m_numberFonts[i].getHeight(), paint);
				j += this.m_numberFonts[i].getWidth();
				paramInt1 /= 10;
			}
		}
		if (k != 0) {
			canvas.drawBitmap(this.m_minusImage, paramInt2 - j - this.m_minusImage.getWidth(), paramInt3 - this.m_minusImage.getHeight(), paint);
			j += this.m_minusImage.getWidth();
		}
		return j;
	}

	protected void drawLives(Canvas canvas, Paint paint, int paramInt1, int paramInt2) {
		int i;
		int j;
		if (this.m_nLives > MAXLIFEDISPLAY) {
			i = paramInt1 + MAXLIFEDISPLAY * this.m_greenShip.getWidth();
			j = paintNumber(canvas, paint, this.m_nLives, i, paramInt2);
			canvas.drawBitmap(this.m_greenShip, i - j - this.m_greenShip.getWidth(), paramInt2  - this.m_greenShip.getHeight(), paint);
		} else {
			i = 0;
			for (j = 0; j < this.m_nLives; j++) {
				canvas.drawBitmap(this.m_greenShip, paramInt1 + i, paramInt2 - this.m_greenShip.getHeight(), paint);
				i += this.m_greenShip.getWidth();
			}
		}
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/BackgroundManager.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
