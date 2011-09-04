package org.android.footd;

import android.graphics.Point;


public class Tower extends GridObject {

	TowerType type;
	
	public Tower(Point gridCoord, TowerType type) {
		super(gridCoord, type.sprite);
		this.type = type;
	}

}
