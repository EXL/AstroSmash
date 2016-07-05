package ru.exlmoto.astrosmash;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class AstroSmashThread extends Thread {

	private boolean runState;
	private SurfaceHolder surfaceHolder;
	private AstroSmashView astroSmashView;

	public AstroSmashThread(SurfaceHolder surfaceHolder, AstroSmashView astroSmashView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.astroSmashView = astroSmashView;
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
		while (runState) {
			try {
				canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					astroSmashView.update();
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
