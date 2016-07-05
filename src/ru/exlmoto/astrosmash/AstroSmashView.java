package ru.exlmoto.astrosmash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AstroSmashView extends SurfaceView implements SurfaceHolder.Callback {

	private AstroSmashThread astroSmashThread = null;

	private int screenWidth;
	private int screenHeight;

	// private Context contextActivity;

	private int x_0 = 200;
	private int x_1 = x_0 + 10;
	private Paint painter;

	public AstroSmashView(Context context) {
		super(context);

		// contextActivity = context;

		getHolder().addCallback(this);

		painter = new Paint();

		astroSmashThread = new AstroSmashThread(getHolder(), this);

		setFocusable(true);
	}

	public void update() {
		if (x_1 == screenWidth) {
			x_0 = 0;
			x_1 = x_0 + 10;
		} else {
			x_0 += 1;
			x_1 += 1;
		}
	}

	public void render(Canvas canvas) {
		if (canvas != null) {
			canvas.drawColor(Color.argb(255, 26, 128, 182));

			painter.setColor(Color.YELLOW);
			canvas.drawRect(x_0, 20, x_1, 30, painter);
			canvas.drawRect(x_0 + 20, 20, x_1 + 20, 30, painter);
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
}
