package ru.exlmoto.astrosmash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
}
