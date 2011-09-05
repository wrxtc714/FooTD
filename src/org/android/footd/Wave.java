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
	TimerHandler spawnHandler;

	public Wave(Engine engine, Context context) {
		spawnInterval = 2f;
		path = new Path(7)
		.to(50, 50)
		.to(50, 300)
		.to(500, 500)
		.to(1000, 50)
		.to(300, 50)
		.to(100, 200)
		.to(50, 50);

		// TODO: Walk animation is buggy when not using different types for each mob instance
		MobType guy = new MobType(engine, context, 2, new Point(3,4), "player.png", 1);
		guy.ranges.put("down", new Point(6,8));
		guy.ranges.put("right", new Point(3,5));
		guy.ranges.put("up", new Point(0,2));
		guy.ranges.put("left", new Point(9,11));
		mobs.add(new Mob(guy, path));
		mobs.add(new Mob(guy, path));

		MobType guy2 = new MobType(engine, context, 2, new Point(3,4), "player.png", 1);
		guy2.ranges = guy.ranges;
		mobs.add(new Mob(guy2, path));

		MobType banana = new MobType(engine, context, 2, new Point(4,2), "banana_tiled.png", 0.5f);
		MobType explosion = new MobType(engine, context, 1, new Point(4,4), "explosion2.png", 2);
		MobType star = new MobType(engine, context, 1, new Point(25,3), "star-green.png", 4);
//		star.ranges.put("whole", new Point(0,70));
		for (int i = 0; i < 5; i++) {
			mobs.add(new Mob(banana, path));
			mobs.add(new Mob(explosion, path));
			mobs.add(new Mob(star, path));
		}
	}

	public void init(final Scene scene) {
		//Spawn Mobs event
		spawnHandler = new TimerHandler(spawnInterval, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				spawnMob(scene);
			}
		});
		scene.registerUpdateHandler(spawnHandler);
	}

	public void spawnMob(Scene scene) {
		if (spawnedMobs >= mobs.size()) {
			scene.unregisterUpdateHandler(spawnHandler);
			return;
		}
		scene.attachChild(mobs.get(spawnedMobs));
		spawnedMobs++;
	}

}
