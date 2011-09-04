package org.android.footd;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.graphics.Point;

public class Wave {
	
	int spawnedMobs;
	int waveSize;
	
	private TiledTextureRegion mobRegion;
	
	public Wave(Engine engine, Context context) {
		waveSize = 50;
		mobRegion = Level.readSprite(engine, context, new Point(3,4), "player.png");
	}
	
	public void spawnMob(Scene scene) {
		if (spawnedMobs >= waveSize)
			return;
		
		final Path path = new Path(5)
		.to(50, 10)
		.to(50, 500)
		.to(50 - 58, 500)
		.to(50 - 58, 10)
		.to(50, 10);
		
		scene.attachChild(new Mob(0,0, 48, 64, mobRegion, path));
		spawnedMobs++;
	}

}
