package ru.exlmoto.astrosmash.AstroSmashEngine;

import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;
import ru.exlmoto.astrosmash.R;

public class MunitionsFactory {

	public static final int INITIAL_BULLETS = 1;
	public static final int INITIAL_Y_VELOCITY = -5;
	public static final int INITIAL_VELOCITY_TIME = 500;
	public static final String BULLET_IMAGE = "/bullet.png";
	private Stack<Collidable> m_stackBullets = new Stack<Collidable>();
	private Bitmap m_bulletImage;
	private int m_nInitialYVelocity = INITIAL_Y_VELOCITY;
	private long m_nInitialVelocityTime = INITIAL_VELOCITY_TIME;

	private Context activityContext = null;

	public MunitionsFactory(int whileInt, int shipMoveDistance, long initialVelocityTime, Context context) {
		this.activityContext = context;
		this.m_nInitialYVelocity = shipMoveDistance;
		this.m_nInitialVelocityTime = initialVelocityTime;
		this.m_bulletImage = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.bullet);
		if (AstroSmashSettings.doubleFire) {
			whileInt = 2;
		}
		for (int i = 0; i < whileInt; i++) {
			Collidable localCollidable = new Collidable();
			localCollidable.setImage(this.m_bulletImage);
			localCollidable.setVelocity(0, this.m_nInitialYVelocity, this.m_nInitialVelocityTime);
			this.m_stackBullets.push(localCollidable);
		}
	}

	public MunitionsFactory(int shipMoveDistance, long initialVelocityTime, Context context) {
		this(INITIAL_BULLETS, shipMoveDistance, initialVelocityTime, context);
	}

	public Collidable getBullet() {
		Collidable localCollidable = null;
		if (!this.m_stackBullets.isEmpty()) {
			localCollidable = (Collidable)this.m_stackBullets.pop();
			localCollidable.setCollided(false);
		}
		return localCollidable;
	}

	public void putBullet(Collidable paramCollidable) {
		this.m_stackBullets.push(paramCollidable);
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/MunitionsFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
