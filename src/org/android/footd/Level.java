package org.android.footd;

import java.util.List;

import org.anddev.andengine.entity.sprite.TiledSprite;

public class Level {
	List<Tower> towers;
	List<Wave> waves;
	List<GridObject> entrances;
	List<GridObject> exits;
	GridObject[][] placedObjects;
	TiledSprite background;

	Boolean isCellFree(int x, int y){
		return false;
	}
}
