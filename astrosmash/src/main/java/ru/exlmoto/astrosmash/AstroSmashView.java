/************************************************************************************
** The MIT License (MIT)
**
** Copyright (c) 2016 Serg "EXL" Koles
**
** Permission is hereby granted, free of charge, to any person obtaining a copy
** of this software and associated documentation files (the "Software"), to deal
** in the Software without restriction, including without limitation the rights
** to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
** copies of the Software, and to permit persons to whom the Software is
** furnished to do so, subject to the following conditions:
**
** The above copyright notice and this permission notice shall be included in all
** copies or substantial portions of the Software.
**
** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
** IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
** AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
** LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
** OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
** SOFTWARE.
************************************************************************************/

package ru.exlmoto.astrosmash;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;
import ru.exlmoto.astrosmash.AstroSmashEngine.GameWorld;
import ru.exlmoto.astrosmash.AstroSmashEngine.Version;
import ru.exlmoto.astrosmash.AstroSmashEngine.IGameWorldListener;

@SuppressWarnings("unused")
public class AstroSmashView extends SurfaceView
implements SurfaceHolder.Callback, IGameWorldListener, Runnable {

	public static final int INITIAL_KEY_DELAY = 250;
	public static final int KEY_REPEAT_CYCLE = 75;
	private boolean m_bRunning = true;
	private boolean isGameOver = false;
	private Thread m_gameThread = null;
	private GameWorld m_gameWorld = null;
	private volatile boolean m_bKeyHeldDown = false;
	private volatile boolean m_bAfterInitialWait = false;
	private volatile int m_heldDownGameAction;
	private volatile long m_initialHoldDownTime;
	private boolean m_bFirstPaint = true;
	private long m_nStartTime;
	private long m_nPauseTime = 0L;
	private long m_nPausedTime = 0L;
	Thread m_currentThread = null;
	int m_nThreadSwitches = 0;
	long m_nLastMemoryUsageTime = 0L;

	private int screenWidth;
	private int screenHeight;
	private int screenHeightPercent;

	private Paint painter = null;
	private Canvas bitmapCanvas = null;
	private Canvas globalCanvas = null;
	private Bitmap gameScreen = null;
	private Bitmap touchArrowBitmap = null;
	private Canvas touchArrowCanvas = null;

	private Rect touchRect = null;
	private Rect bitmapRect = null;
	private Rect screenRect = null;
	private Rect screenRectPercent = null;

	private int px1 = 0;
	private int px5 = 0;
	private int px25 = 0;
	private int px25double = 0;

	private int arrow_Y0 = 0;
	private int arrow_Y1 = 0;
	private int arrow_Y2 = 0;

	private int arrow_X0 = 0;
	private int arrow_X1 = 0;

	private int screenRectChunkProcent = 0;

	private static Random m_random;

	private SurfaceHolder surfaceHolder = null;

	private AstroSmashActivity astroSmashActivity = null;

	public AstroSmashView(Context context) {
		super(context);

		astroSmashActivity = (AstroSmashActivity) context;

		m_random = new Random(System.currentTimeMillis());

		switch (AstroSmashSettings.graphicsScale) {
		case AstroSmashLauncher.SCALE_120P:
			Version.setScreenSizes(Version.ANDROID_ORIGINAL_120x146);
			break;
		case AstroSmashLauncher.SCALE_176P:
			Version.setScreenSizes(Version.ANDROID_ORIGINAL_176x220);
			break;
		case AstroSmashLauncher.SCALE_240P:
			Version.setScreenSizes(Version.ANDROID_ORIGINAL_240x320);
			break;
		case AstroSmashLauncher.SCALE_480P:
			Version.setScreenSizes(Version.ANDROID_ORIGINAL_480x640);
			break;
		default:
			break;
		}

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);

		gameScreen = Bitmap.createBitmap(Version.getWidth(), Version.getHeight(), Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(gameScreen);
		painter = new Paint();

		m_gameWorld = new GameWorld(Version.getWidth(), Version.getHeight() - Version.getCommandHeightPixels(), this, context);
		this.m_bFirstPaint = true;

		restartGame(true);

		this.m_nStartTime = 0L;
		this.m_nPauseTime = 0L;
		this.m_nPausedTime = 0L;

		// Set screen on
		setKeepScreenOn(true);

		// Focus
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
	}

	public void render(Canvas canvas) {
		if (canvas != null && bitmapCanvas != null) {
			if (this.m_bFirstPaint) {
				clearScreen(bitmapCanvas);
				this.m_bFirstPaint = false;
			}
			this.m_gameWorld.paint(bitmapCanvas, painter);
			if (gameScreen != null) {
				if (AstroSmashSettings.antialiasing) {
					painter.setFilterBitmap(true);
				}
				if (AstroSmashSettings.showTouchRect) {
					canvas.drawBitmap(gameScreen,
							bitmapRect,
							screenRectPercent,
							painter);
					canvas.drawBitmap(touchArrowBitmap,
							0, screenRectChunkProcent,
							painter);
				} else {
					canvas.drawBitmap(gameScreen,
							bitmapRect,
							screenRect,
							painter);
				}
			}
		}
	}

	private int px(float dips) {
		float dp = getResources().getDisplayMetrics().density;
		return Math.round(dips * dp);
	}

	private void drawTouchArrow(Canvas canvas, Paint paint) {
		paint.setColor(Version.GREENCOLOR_DARK);
		canvas.drawRect(0, 0, screenWidth, screenHeightPercent, paint);
		paint.setStrokeCap(Cap.ROUND);
		paint.setAntiAlias(true);
		for (int i = 0; i < 2; ++i) {
			if (i == 0) {
				paint.setColor(Version.GRAYCOLOR);
				paint.setStrokeWidth(px5);
			} else {
				paint.setColor(Version.DARKCOLOR);
				paint.setStrokeWidth(px1);
			}
			canvas.drawLine(px25, arrow_Y0, arrow_X0, arrow_Y0, paint);
			canvas.drawLine(px25, arrow_Y0, px25double, arrow_Y1, paint);
			canvas.drawLine(px25, arrow_Y0, px25double, arrow_Y2, paint);
			canvas.drawLine(arrow_X0, arrow_Y0, arrow_X1, arrow_Y1, paint);
			canvas.drawLine(arrow_X0, arrow_Y0, arrow_X1, arrow_Y2, paint);
		}
	}

	public boolean isM_bRunning() {
		return m_bRunning;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void SetisGameOver(boolean gameOver) {
		isGameOver = gameOver;
	}

	public int getScreenHeightPercent() {
		return screenHeightPercent;
	}

	public void setScreenHeightPercent(int screenHeightPercent) {
		this.screenHeightPercent = screenHeightPercent;
	}

	public void resetStartTime() {
		this.m_nStartTime = System.currentTimeMillis();
		this.m_nPauseTime = 0L;
		this.m_nPausedTime = 0L;
	}

	public void pause(boolean paused) {
		AstroSmashActivity.toDebug("Paused: " + paused);
		this.m_gameWorld.pause(paused);
	}

	public void exit() {
		this.m_bRunning = false;
		this.m_gameThread = null;
	}

	public void start() {
		if (Version.getDemoFlag()) {
			if (this.m_nPauseTime > 0L) {
				this.m_nPausedTime += System.currentTimeMillis() - this.m_nPauseTime;
			}
			this.m_nPauseTime = 0L;
		}
		this.m_bRunning = true;
		this.m_gameThread = new Thread(this);
		this.m_gameThread.start();
		this.m_gameWorld.pause(false);
	}

	public int getPeakScore() {
		return this.m_gameWorld.getPeakScore();
	}

	public void restartGame(boolean constructor) {
		resetStartTime();
		this.m_gameWorld.reset();
		this.m_bFirstPaint = true;
		if (!constructor) {
			init();
			start();
		}
	}

	public int getGameAction(int paramInt) {
		switch (paramInt) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_SPACE:
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_5:
			return 9; // Fire
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_W:
		case KeyEvent.KEYCODE_8:
			return 1; // Auto Fire
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_A:
		case KeyEvent.KEYCODE_4:
			return 2; // Left
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_D:
		case KeyEvent.KEYCODE_6:
			return 5; // Right
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_S:
		case KeyEvent.KEYCODE_2:
			return 6; // Hyper
		case KeyEvent.KEYCODE_Q:
		case KeyEvent.KEYCODE_E:
		case KeyEvent.KEYCODE_7:
			return 10; // Unknown
		}
		return 0;
		// return super.getGameAction(paramInt);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		this.m_bKeyHeldDown = false;
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			int i = getGameAction(keyCode);
			if (this.m_bRunning) {
				this.m_gameWorld.handleAction(i);
			}
			this.m_heldDownGameAction = i;
			this.m_bKeyHeldDown = true;
			this.m_initialHoldDownTime = System.currentTimeMillis();
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
			localException.printStackTrace();
		}
		return super.onKeyDown(keyCode, event);
	}

	private int getPercentChunkHeight(int sideSize, int percent) {
		return Math.round(sideSize * percent / 100);
	}

	private void init() {
		screenHeightPercent = getPercentChunkHeight(screenHeight, 15);

		touchRect = new Rect(0, screenHeight - screenHeightPercent, screenWidth, screenHeight);
		bitmapRect = new Rect(0, 0, gameScreen.getWidth(), gameScreen.getHeight());
		screenRect = new Rect(0, 0, screenWidth, screenHeight);
		screenRectPercent = new Rect(0, 0, screenWidth, screenHeight - screenHeightPercent);

		px1 = px(1);
		px5 = px(5);
		px25 = px(25);
		px25double = px25 * 2;

		arrow_Y0 = touchRect.height() / 2;
		arrow_Y1 = touchRect.height() / 4;
		arrow_Y2 = touchRect.height() / 4 * 3;

		arrow_X0 = screenWidth - px25;
		arrow_X1 = screenWidth - px25double;

		screenRectChunkProcent = screenHeight - screenHeightPercent;

		touchArrowBitmap = Bitmap.createBitmap(screenWidth, screenHeightPercent, Bitmap.Config.ARGB_8888);
		touchArrowCanvas = new Canvas(touchArrowBitmap);

		drawTouchArrow(touchArrowCanvas, painter);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		AstroSmashActivity.toDebug("Surface created");
		screenWidth = holder.getSurfaceFrame().width();
		screenHeight = holder.getSurfaceFrame().height();

		init();

		start();

		if (AstroSmashActivity.paused) {
			pause(true);
		}

		if (isGameOver) {
			gameIsOver();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		AstroSmashActivity.toDebug("Surface changed: " + width + "x" + height + "|" + screenWidth + "x" + screenHeight);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		AstroSmashActivity.toDebug("Surface destroyed");
		boolean shutdown = false;
		this.m_bRunning = false;
		while (!shutdown) {
			try {
				if (m_gameThread != null) {
					this.m_gameThread.join();
				}
				shutdown = true;
			} catch (InterruptedException e) {
				AstroSmashActivity.toDebug("Error joining to Game Thread");
			}
		}
	}

	public int getScorePosition(int score) {
		for (int i = 0; i < AstroSmashLauncher.HISCORE_PLAYERS; i++) {
			if (score > AstroSmashSettings.playerScores[i]) {
				return i;
			}
		}
		return -1;
	}

	public int addScore(int score, int restartGame) {
		int i = getScorePosition(score);
		if (i == -1) {
			return i;
		}
		Intent intent = new Intent(this.getContext(), AstroSmashHighScoreDialog.class);
		intent.putExtra("peakScore", score);
		intent.putExtra("indexScore", i);
		intent.putExtra("restartGame", restartGame);
		astroSmashActivity.startActivity(intent);
		return i;
	}

	public int checkHiScores(int restartGame) {
		int peakScore = m_gameWorld.getPeakScore();
		AstroSmashActivity.toDebug("HiScore is: " + peakScore);
		return addScore(peakScore, restartGame);
	}

	public void setShipX(int xCoord) {
		this.m_gameWorld.getShip().setX(xCoord);
	}

	public void fire() {
		this.m_gameWorld.fireBullet();
	}

	protected void clearScreen(Canvas canvas) {
		canvas.drawColor(Version.WHITECOLOR);
	}

	@Override
	public void gameIsOver() {
		isGameOver = true;
		AstroSmashActivity.toDebug("Game Over!");
		AstroSmashLauncher.playGameOverSound();
		this.m_bRunning = false;
		this.m_gameThread = null;
	}

	public static int getAbsRandomInt() {
		m_random.setSeed(System.currentTimeMillis() + m_random.nextInt());
		return Math.abs(m_random.nextInt());
	}

	public static int getRandomIntBetween(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public static int getRandomInt() {
		m_random.setSeed(System.currentTimeMillis() + m_random.nextInt());
		return m_random.nextInt();
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenRectChunkProcent() {
		return screenRectChunkProcent;
	}

	@Override
	public void run() {
		try {
			// long l1 = System.currentTimeMillis();
			long l2 = System.currentTimeMillis();
			long l5 = l2;
			this.m_nLastMemoryUsageTime = 0L;
			while (this.m_bRunning) {
				long l3 = System.currentTimeMillis();
				long l4 = l3 - l2;
				//				if (AstroSmashVersion.getDebugFlag()) {
				//					if ((AstroSmashVersion.getDebugMemoryFlag()) && (l3 - this.m_nLastMemoryUsageTime > AstroSmashVersion.getDebugMemoryInterval())) {
				//						// AstroSmashMidlet.printMemoryUsage("AstrosmashScreen.run (" + (l3 - l1) / 1000L + " secs)");
				//						this.m_nLastMemoryUsageTime = l3;
				//					}
				//					if (this.m_currentThread != Thread.currentThread()) {
				//						this.m_currentThread = Thread.currentThread();
				//						this.m_nThreadSwitches += 1;
				//						System.out.println("AstrosmashScreen.run: Game thread switch (" + this.m_nThreadSwitches + " total)");
				//					}
				//				}
				//				if ((AstroSmashVersion.getDemoFlag()) && (l3 - this.m_nStartTime - this.m_nPausedTime >= AstroSmashVersion.getDemoDuration() * 1000)) {
				//					this.m_gameWorld.suspendEnemies();
				//				}
				if ((this.m_bKeyHeldDown) && (l3 - this.m_initialHoldDownTime > 250L) && (l3 - l5 > 75L)) {
					l5 = l3;
					this.m_gameWorld.handleAction(this.m_heldDownGameAction);
				}
				this.m_gameWorld.tick(l4);
				try {
					this.globalCanvas = surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) {
						render(this.globalCanvas);
					}
				} finally {
					if (this.globalCanvas != null) {
						surfaceHolder.unlockCanvasAndPost(this.globalCanvas);
					}
				}
				l2 = l3;
			}
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
			localException.printStackTrace();
		}
	}
}
