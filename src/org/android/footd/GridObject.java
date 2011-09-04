package org.android.footd;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Point;

public class GridObject extends AnimatedSprite {

	Point gridCoord;
	public GridObject(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
	}
	
	public GridObject(Point gridCoord, TiledTextureRegion pTiledTextureRegion) {
		super(convertGridPointToFloatPoint(gridCoord).x, convertGridPointToFloatPoint(gridCoord).y, pTiledTextureRegion);
	}

	public static FloatPoint convertGridPointToFloatPoint(Point gridCoord){
		FloatPoint floatPoint = new FloatPoint(
				Level.GRID_X + Level.CELL_SIZE * gridCoord.x,
				Level.GRID_Y + Level.CELL_SIZE * gridCoord.y
				);
		return floatPoint;
	}
}
