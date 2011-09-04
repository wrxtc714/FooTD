package org.android.footd;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

public class Mob extends AnimatedSprite {
	
	MobType type;
	int level;

	public Mob(float pX, float pY,float pWidth, float pHeight, TiledTextureRegion pTiledTextureRegion, Path path) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion);
		addPath(path);
	}

	public void addPath(Path path) {

		/* Add the proper animation when a waypoint of the path is passed. */
		registerEntityModifier(new LoopEntityModifier(new PathModifier(30, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				Debug.d("onPathStarted");
			}
	
			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointStarted:  " + pWaypointIndex);
				switch(pWaypointIndex) {
					case 0:
						animate(new long[]{200, 200, 200}, 6, 8, true);
						break;
					case 1:
						animate(new long[]{200, 200, 200}, 3, 5, true);
						break;
					case 2:
						animate(new long[]{200, 200, 200}, 0, 2, true);
						break;
					case 3:
						animate(new long[]{200, 200, 200}, 9, 11, true);
						break;
				}
			}
	
			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointFinished: " + pWaypointIndex);
			}
	
			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				Debug.d("onPathFinished");
			}
		}, EaseSineInOut.getInstance())));
	}
}
