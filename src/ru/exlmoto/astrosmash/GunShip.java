package ru.exlmoto.astrosmash;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GunShip extends Enemy {

	public void paint(Canvas canvas, Paint paint) {
		if ((true == getCollided()) && (1 == getHitReaction())) {
			Object localObject;
			if (hasNextExplosionImage()) {
				localObject = getNextExplosionImage();
				// TODO: 33 ?
				// paramGraphics.drawImage((Image)localObject, getX(), getY(), 33);
				canvas.drawBitmap((Bitmap)localObject, getX(), getY(), paint);
			} else {
				localObject = getDeathListener();
				((IDeathListener)localObject).doneExploding(this);
			}
		} else {
			super.paint(canvas, paint);
		}
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/GunShip.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */