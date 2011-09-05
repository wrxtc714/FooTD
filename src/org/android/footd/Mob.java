package org.android.footd;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Point;

import com.badlogic.gdx.math.Vector2;

public class Mob extends AnimatedSprite {

	MobType type;
	int level;
	Path path;

	public Mob(MobType type, Path path) {
		super(0, 0, type.getWidth(), type.getHeight(), type.texture);
//		super(0, 0, type.texture);
		this.type = type;
		setPath(path);
	}
	
	public void animateRange(Point range) {
		animateRange(range, 200);
	}

	public void animateRange(Point range, int frameTime) {
		int rangesize = range.y-range.x+1;
		long[] times = new long[rangesize];
		for (int i = 0; i < rangesize; i++)
			times[i] = (int)(frameTime / type.speed);

		animate(times, range.x, range.y, true);
	}

	public void animateRange(String rangeName) {
		if (type.ranges.containsKey(rangeName))
			animateRange(type.ranges.get(rangeName));
	}

	public void chooseWalkAnimation(int pathIndex) {
		Vector2 currentCoord = new Vector2();
		currentCoord.x = path.getCoordinatesX()[pathIndex];
		currentCoord.y = path.getCoordinatesY()[pathIndex];

		// Loop next coord
		Vector2 nextCoord = new Vector2();
		if (path.getCoordinatesX().length - 1 > pathIndex) {
			nextCoord.x = path.getCoordinatesX()[pathIndex + 1];
			nextCoord.y = path.getCoordinatesY()[pathIndex + 1];
		} else {
			nextCoord.x = path.getCoordinatesX()[0];
			nextCoord.y = path.getCoordinatesY()[0];
		}

		// calculate normalized walk vector
		Vector2 walkVector = nextCoord.sub(currentCoord);
		walkVector = walkVector.nor();

		if (Math.abs(walkVector.x) > Math.abs(walkVector.y)) {
			// bigger X movement
			if (walkVector.x > 0) {
				animateRange("right");
			} else {
				animateRange("left");
			}
		} else {
			// bigger Y movement
			if (walkVector.y > 0) {
				animateRange("down");
			} else {
				animateRange("up");
			}
		}
	}
	public void setPath(final Path path) {
		this.path = path;
		float duration = 30 / type.speed;

		/* Add the proper animation when a waypoint of the path is passed. */
		registerEntityModifier(new LoopEntityModifier(new PathModifier(duration, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointStarted:  " + pWaypointIndex);

				if (type.ranges.isEmpty()) {
					animate((int)(200 / type.speed));
				}else if (type.ranges.containsKey("whole")) {
					animateRange(type.ranges.get("whole"));
				}else if (type.ranges.containsKey("up")) {
					chooseWalkAnimation(pWaypointIndex);
				}
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
			}
		}, EaseLinear.getInstance())));
	}
}
