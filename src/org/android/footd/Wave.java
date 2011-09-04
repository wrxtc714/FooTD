package org.android.footd;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;

import android.content.Context;
import android.graphics.Point;

public class Wave {

	int spawnedMobs = 0;
	float spawnInterval;
	Path path;
	List<Mob> mobs = new ArrayList<Mob>();

	public Wave(Engine engine, Context context) {
		spawnInterval = 2f;

		path = new Path(5)
		.to(50, 10)
		.to(50, 500)
		.to(50 - 58, 500)
		.to(50 - 58, 10)
		.to(50, 10);

		MobType guy = new MobType(engine, context, new Point(48, 64), new Point(3,4), "player.png");
		MobType banana = new MobType(engine, context, new Point(48, 64), new Point(4,2), "banana_tiled.png");
		MobType explosion = new MobType(engine, context, new Point(64, 64), new Point(4,4), "explosion2.png");

		for (int i = 0; i < 10; i++) {
			mobs.add(new Mob(guy, path));
			mobs.add(new Mob(banana, path));
			mobs.add(new Mob(explosion, path));
		}
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
		if (spawnedMobs >= mobs.size())
			return;
		scene.attachChild(mobs.get(spawnedMobs));
		spawnedMobs++;
	}

}
