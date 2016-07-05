package ru.exlmoto.astrosmash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AstroSmashView extends SurfaceView implements SurfaceHolder.Callback, IGameWorldListener {

	private AstroSmashThread astroSmashThread = null;

	private int screenWidth;
	private int screenHeight;

	// private Context contextActivity;

	private int x_0 = 200;
	private int x_1 = x_0 + 10;
	private Paint painter;

	private GameWorld gameWorld = null;

	private boolean m_bFirstPaint = true;
	private long m_nStartTime;
	private long m_nPauseTime = 0L;
	private long m_nPausedTime = 0L;

	public AstroSmashView(Context context) {
		super(context);

		// contextActivity = context;

		getHolder().addCallback(this);

		AstroSmashMidlet m = new AstroSmashMidlet();

		painter = new Paint();

		gameWorld = new GameWorld(AstroSmashVersion.getWidth(), AstroSmashVersion.getHeight() - AstroSmashVersion.getCommandHeightPixels(), this, context);

		astroSmashThread = new AstroSmashThread(getHolder(), this, gameWorld);

		this.m_bFirstPaint = true;
		this.m_nStartTime = 0L;
		this.m_nPauseTime = 0L;
		this.m_nPausedTime = 0L;

		setFocusable(true);
	}

	public void render(Canvas canvas) {
		if (canvas != null) {
			if (this.m_bFirstPaint) {
				clearScreen(canvas);
				this.m_bFirstPaint = false;
			}
			this.gameWorld.paint(canvas, painter);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		AstroSmashActivity.toDebug("Surface created"); 
		screenWidth = holder.getSurfaceFrame().width();
		screenHeight = holder.getSurfaceFrame().height();

		astroSmashThread.setRunState(true);
		astroSmashThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		AstroSmashActivity.toDebug("Surface changed: " + width + "x" + height + "|" + screenWidth + "x" + screenHeight);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean shutdown = false;
		astroSmashThread.setRunState(false);
		while (!shutdown) {
			try {
				astroSmashThread.join();
				shutdown = true;
			} catch (InterruptedException e) {
				AstroSmashActivity.toDebug("Error joining to Game Thread");
			}
		}
	}

	protected void clearScreen(Canvas canvas) {
		canvas.drawColor(16777215);
	}

	@Override
	public void gameIsOver() {
		// TODO Auto-generated method stub

	}
}
