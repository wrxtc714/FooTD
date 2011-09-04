package org.android.footd;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.graphics.Point;

public class Wave {
	
	int spawnedMobs;
	int waveSize;
	float spawnInterval;
	Path path;
	
	private TiledTextureRegion mobRegion;
	
	public Wave(Engine engine, Context context) {
		waveSize = 50;
		spawnInterval = 2f;
		mobRegion = Level.readSprite(engine, context, new Point(3,4), "player.png");
		path = new Path(5)
		.to(50, 10)
		.to(50, 500)
		.to(50 - 58, 500)
		.to(50 - 58, 10)
		.to(50, 10);
	}
	
	public void init(final Scene scene) {
		//Spawn Mobs evey time
		scene.registerUpdateHandler(new TimerHandler(spawnInterval, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				spawnMob(scene);
			}
		}));
	}
	
	public void spawnMob(Scene scene) {
		if (spawnedMobs >= waveSize)
			return;
		scene.attachChild(new Mob(0,0, 48, 64, mobRegion, path));
		spawnedMobs++;
	}

}
