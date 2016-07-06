package ru.exlmoto.astrosmash;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressWarnings("unused")
public class AstroSmashView extends SurfaceView implements SurfaceHolder.Callback, IGameWorldListener, Runnable {

	public static final int INITIAL_KEY_DELAY = 250;
	public static final int KEY_REPEAT_CYCLE = 75;
	private boolean m_bRunning = true;
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

	private Paint painter = null;
	private Canvas bitmapCanvas = null;
	private Canvas globalCanvas = null;
	private Bitmap gameScreen = null;

	private static Random m_random;

	private SurfaceHolder surfaceHolder = null;

	public AstroSmashView(Context context) {
		super(context);

		m_random = new Random(System.currentTimeMillis());

		InfoStrings.initializeInfo();
		AstroSmashVersion.setScreenSizes(AstroSmashVersion.ANDROID_ORIGINAL_240x320);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);

		gameScreen = Bitmap.createBitmap(AstroSmashVersion.getWidth(), AstroSmashVersion.getHeight(), Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas(gameScreen);
		painter = new Paint();

		m_gameWorld = new GameWorld(AstroSmashVersion.getWidth(), AstroSmashVersion.getHeight() - AstroSmashVersion.getCommandHeightPixels(), this, context);
		this.m_bFirstPaint = true;
		// TODO: Debug ?

		resetStartTime();
		restartGame();

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
				if (true) { // TODO: Settings Flag
					painter.setAntiAlias(true);
					painter.setFilterBitmap(true);
				}
				canvas.drawBitmap(gameScreen, 
						new Rect(0, 0, gameScreen.getWidth(), gameScreen.getHeight()),
						new Rect(0, 0, screenWidth, screenHeight),
						painter);
			}
		}
	}

	public void resetStartTime() {
		this.m_nStartTime = System.currentTimeMillis();
		this.m_nPauseTime = 0L;
		this.m_nPausedTime = 0L;
	}

	public void pause() {
		this.m_bRunning = false;
		this.m_gameThread = null;
		this.m_gameWorld.pause(true);
		//		repaint();
		//		serviceRepaints();
		if (AstroSmashVersion.getDemoFlag()) {
			this.m_nPauseTime = System.currentTimeMillis();
		}
	}

	public void exit() {
		this.m_bRunning = false;
		this.m_gameThread = null;
	}

	public void start() {
		if (AstroSmashVersion.getDemoFlag()) {
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

	public void restartGame() {
		this.m_gameWorld.reset();
		this.m_bFirstPaint = true;
	}

	public int getGameAction(int paramInt) {
		switch (paramInt)
		{
		case KeyEvent.KEYCODE_DPAD_CENTER: 
		case KeyEvent.KEYCODE_SPACE:
		case KeyEvent.KEYCODE_ENTER:
			return 9; // Fire
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_S:
		case KeyEvent.KEYCODE_2:
			return 1; // Autofire
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_A:
		case KeyEvent.KEYCODE_4:
			return 2; // Left
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_D:
		case KeyEvent.KEYCODE_6:
			return 5; // Right
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_W:
		case KeyEvent.KEYCODE_8:
			return 6; // Hyper
		case KeyEvent.KEYCODE_Q:
		case KeyEvent.KEYCODE_E:
		case KeyEvent.KEYCODE_5: 
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
		AstroSmashActivity.toDebug("KeyCode: " + keyCode + "");
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

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		AstroSmashActivity.toDebug("Surface created"); 
		screenWidth = holder.getSurfaceFrame().width();
		screenHeight = holder.getSurfaceFrame().height();
		start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		AstroSmashActivity.toDebug("Surface changed: " + width + "x" + height + "|" + screenWidth + "x" + screenHeight);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
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

	protected void clearScreen(Canvas canvas) {
		canvas.drawColor(AstroSmashVersion.WHITECOLOR);
	}

	@Override
	public void gameIsOver() {
		AstroSmashActivity.toDebug("Game Over!");
		this.m_bRunning = false;
		this.m_gameThread = null;
	}

	public static int getAbsRandomInt() {
		m_random.setSeed(System.currentTimeMillis() + m_random.nextInt());
		return Math.abs(m_random.nextInt());
	}

	public static int getRandomInt() {
		m_random.setSeed(System.currentTimeMillis() + m_random.nextInt());
		return m_random.nextInt();
	}

	@Override
	public void run() {
		try {
			long l1 = System.currentTimeMillis();
			long l2 = System.currentTimeMillis();
			long l5 = l2;
			this.m_nLastMemoryUsageTime = 0L;
			while (this.m_bRunning) {
				long l3 = System.currentTimeMillis();
				long l4 = l3 - l2;
				if (AstroSmashVersion.getDebugFlag()) {
					if ((AstroSmashVersion.getDebugMemoryFlag()) && (l3 - this.m_nLastMemoryUsageTime > AstroSmashVersion.getDebugMemoryInterval())) {
						// AstroSmashMidlet.printMemoryUsage("AstrosmashScreen.run (" + (l3 - l1) / 1000L + " secs)");
						this.m_nLastMemoryUsageTime = l3;
					}
					if (this.m_currentThread != Thread.currentThread()) {
						this.m_currentThread = Thread.currentThread();
						this.m_nThreadSwitches += 1;
						System.out.println("AstrosmashScreen.run: Game thread switch (" + this.m_nThreadSwitches + " total)");
					}
				}
				if ((AstroSmashVersion.getDemoFlag()) && (l3 - this.m_nStartTime - this.m_nPausedTime >= AstroSmashVersion.getDemoDuration() * 1000)) {
					this.m_gameWorld.suspendEnemies();
				}
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
