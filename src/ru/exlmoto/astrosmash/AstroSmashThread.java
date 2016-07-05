package ru.exlmoto.astrosmash;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class AstroSmashThread extends Thread {

	private boolean runState;
	private SurfaceHolder surfaceHolder;
	private AstroSmashView astroSmashView;
	private GameWorld gameWorld;
	private long m_nLastMemoryUsageTime = 0L;

	private Thread m_currentThread = null;
	private int m_nThreadSwitches = 0;
	private long m_nStartTime;
	private long m_nPausedTime;
	private boolean m_bKeyHeldDown;
	private long m_initialHoldDownTime;
	private volatile int m_heldDownGameAction;

	public AstroSmashThread(SurfaceHolder surfaceHolder, AstroSmashView astroSmashView, GameWorld gameWorld) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.astroSmashView = astroSmashView;
		this.gameWorld = gameWorld;
	}

	public boolean isRunState() {
		return runState;
	}

	public void setRunState(boolean runState) {
		this.runState = runState;
	}

	@Override
	public void run() {
		Canvas canvas = null;
		long l1 = System.currentTimeMillis();
		long l2 = System.currentTimeMillis();
		long l5 = l2;
		this.m_nLastMemoryUsageTime = 0L;
		while (runState) {
			try {
				long l3 = System.currentTimeMillis();
				long l4 = l3 - l2;
				if (AstroSmashVersion.getDebugFlag()) {
					if ((AstroSmashVersion.getDebugMemoryFlag()) && (l3 - this.m_nLastMemoryUsageTime > AstroSmashVersion.getDebugMemoryInterval())) {
						//AstroSmashMidlet.printMemoryUsage("AstrosmashScreen.run (" + (l3 - l1) / 1000L + " secs)");
						this.m_nLastMemoryUsageTime = l3;
					}
					if (this.m_currentThread != Thread.currentThread()) {
						this.m_currentThread = Thread.currentThread();
						this.m_nThreadSwitches += 1;
						System.out.println("AstrosmashScreen.run: Game thread switch (" + this.m_nThreadSwitches + " total)");
					}
				}
				if ((AstroSmashVersion.getDemoFlag()) && (l3 - this.m_nStartTime - this.m_nPausedTime >= AstroSmashVersion.getDemoDuration() * 1000)) {
					this.gameWorld.suspendEnemies();
				}
				if ((this.m_bKeyHeldDown) && (l3 - this.m_initialHoldDownTime > 250L) && (l3 - l5 > 75L)) {
					l5 = l3;
					this.gameWorld.handleAction(this.m_heldDownGameAction);
				}
				l2 = l3;
				canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					this.gameWorld.tick(l4);
					astroSmashView.render(canvas);
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
