package org.android.footd;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Mob extends AnimatedSprite {
	public Mob(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
	}
	MobType type;
	int level;

}
