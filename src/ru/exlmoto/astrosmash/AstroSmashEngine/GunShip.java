package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GunShip extends Enemy {

	public void paint(Canvas canvas, Paint paint) {
		if ((true == getCollided()) && (1 == getHitReaction())) {
			if (hasNextExplosionImage()) {
				Bitmap localObject = getNextExplosionImage();
				// Gap fix
				// 296:12
				// 296:19
				// 296:29
				int gap = 0;
				switch (localObject.getHeight()) {
				case 12:
					gap = 4;
					break;
				case 19:
					gap = 11;
					break;
				case 29:
					gap = 21;
					break;
				default:
					break;
				}
				canvas.drawBitmap(localObject, getX() - localObject.getWidth() / 2, getY() - gap, paint);
			} else {
				IDeathListener localObject = getDeathListener();
				localObject.doneExploding(this);
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
