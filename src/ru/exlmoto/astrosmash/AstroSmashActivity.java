package ru.exlmoto.astrosmash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

public class AstroSmashActivity extends Activity {

	public static final String ASTRO_SMASH_TAG = "AstroSmash";

	private AstroSmashView astroSmashView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		astroSmashView = new AstroSmashView(this);
		setContentView(astroSmashView);
	}

	public static void toDebug(String message) {
		Log.d(ASTRO_SMASH_TAG, message);
	}

	private int convertCoordX(float xCoord) {
		return Math.round(xCoord * AstroSmashVersion.getWidth() / astroSmashView.getScreenWidth());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int touchId = event.getPointerCount() - 1;
		if (touchId < 0) {
			return false;
		}
		int action = event.getActionMasked();
		switch(action) {
		case MotionEvent.ACTION_MOVE:
			if (event.getY(touchId) > astroSmashView.getScreenRectChunkProcent()) {
				astroSmashView.setShipX(convertCoordX(event.getX(touchId)));
			} else {
				astroSmashView.fire();
			}
			break;
		}
		return true;
	}
}
