package ru.exlmoto.astrosmash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AstroSmashLauncher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_astrosmashlauncher);

		Button astroSmashRunButton = (Button) findViewById(R.id.gameButton);
		astroSmashRunButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), AstroSmashActivity.class);
				startActivity(intent);
			}
		});
	}
}
