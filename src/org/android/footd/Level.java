package org.android.footd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import android.graphics.Point;

public class Level {
	public static final float CELL_SIZE = 32;
	public static final float GRID_X = 0;
	public static final float GRID_Y = 0;
	
	List<Tower> towers = new ArrayList<Tower>();
	Map<String, TowerType> towerTypes = new HashMap<String, TowerType>();
	List<Wave> waves = new ArrayList<Wave>();
	List<GridObject> entrances = new ArrayList<GridObject>();
	List<GridObject> exits = new ArrayList<GridObject>();
	
	private TextureRegion fullBackGround;
	
	GridObject[][] placedObjects;
	TiledSprite background;

	Boolean isCellFree(int x, int y){
		return placedObjects[x][y] == null;
	}
	
	
	public Level(Point size) {
		placedObjects = new GridObject[size.x][size.y];
	}


	/**
	 * creates basic level for testing purposes
	 * @param context BaseGameActivity
	 * @return the created level
	 */
	public static Level createTestLevel(Context context, Engine engine, String background) {
		Level level = new Level(new Point(10,10));
		
		TowerType flameTowerType = new TowerType();
		flameTowerType.name = "Flame Tower";
		flameTowerType.damageTypes.add(new DamageType());
		flameTowerType.speed = 100;
		flameTowerType.damage = 5;
		flameTowerType.size = new Point(1,1);
		BitmapTextureAtlas flameTowerTypeAtlas = new BitmapTextureAtlas(128, 128, TextureOptions.DEFAULT);
		flameTowerType.sprite = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flameTowerTypeAtlas, context, "helicopter_tiled.png", 0, 0, 2, 2);
		engine.getTextureManager().loadTexture(flameTowerTypeAtlas);
		level.towerTypes.put(flameTowerType.name, flameTowerType);
		
		TowerType cannonTowerType = new TowerType();
		cannonTowerType.name = "Cannon Tower";
		cannonTowerType.damageTypes.add(new DamageType());
		cannonTowerType.speed = 5;
		cannonTowerType.damage = 100;
		cannonTowerType.size = new Point(2,2);
		BitmapTextureAtlas cannonTowerTypeAtlas = new BitmapTextureAtlas(256, 128, TextureOptions.DEFAULT);
		cannonTowerType.sprite = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(cannonTowerTypeAtlas, context, "banana_tiled.png", 0, 0, 4, 2);
		engine.getTextureManager().loadTexture(cannonTowerTypeAtlas);
		level.towerTypes.put(cannonTowerType.name, cannonTowerType);
		
		// Load Background Recources
		
		BitmapTextureAtlas backGroundAtlas = new BitmapTextureAtlas(2048, 2048, TextureOptions.BILINEAR);
		level.fullBackGround = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backGroundAtlas, context, background, 0, 0);
		engine.getTextureManager().loadTexture(backGroundAtlas);
		
		level.waves.add(new Wave(engine, context));
		
				
		return level;
	}
	
	public static Level createLevelFromXML(BuildableBitmapTextureAtlas bitmapTextureAtlas, Context context) {
		return null; //TODO
	}
	
	public void init(Scene scene) {
		/* Calculate the coordinates for the face, so its centered on the camera. */
//		final int centerX = (- fullBackGround.getWidth()) / 2;
//		final int centerY = (- fullBackGround.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite backGroundSprite = new Sprite(0, 0, fullBackGround);
		scene.attachChild(backGroundSprite);
		
		for(Wave wave : waves) {
			wave.init(scene);
		}
		
	}
	
	public void addTower(Tower tower, Scene scene) {
		if(tower.type.size.x > 1 || tower.type.size.y > 1){
			for (int ix = tower.gridCoord.x; ix < tower.gridCoord.x+tower.type.size.x; ix++) {	
				for (int iy = tower.gridCoord.y; iy < tower.gridCoord.y+tower.type.size.y; iy++) {
					placedObjects[ix][iy] = tower;
				}
			}		
		} else {
			placedObjects[tower.gridCoord.x][tower.gridCoord.y] = tower;
		}
		towers.add(tower);
		tower.animate(100);
		scene.attachChild(tower);
	}
}
