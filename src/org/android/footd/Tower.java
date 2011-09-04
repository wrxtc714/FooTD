package org.android.footd;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Point;

public class Tower extends AnimatedSprite {

	TowerType type;
	Point coord;
	
	public Tower(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
	}

}
