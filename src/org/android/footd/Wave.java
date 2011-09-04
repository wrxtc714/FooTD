package org.android.footd;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

public class Wave {
	
	private TiledTextureRegion mobRegion;
	
	public Wave(Engine engine, Context context) {
		BitmapTextureAtlas mobAtlas = new BitmapTextureAtlas(128, 128, TextureOptions.DEFAULT);
		mobRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mobAtlas, context, "player.png", 0, 0, 3, 4);
		engine.getTextureManager().loadTexture(mobAtlas);
		
	}
	
	public void init(Scene scene){
		for (int i = 0; i < 20; i++){
			
			int mobTranslation = i * 50;
			
			final Path path = new Path(5)
			.to(mobTranslation, 10)
			.to(mobTranslation, 500)
			.to(mobTranslation - 58, 500)
			.to(mobTranslation - 58, 10)
			.to(mobTranslation, 10);
			
			scene.attachChild(new Mob(0,0, 48, 64, mobRegion, path));
		}
		
	}

}
