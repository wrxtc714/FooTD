package org.android.footd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;

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
	public static Level createTestLevel(Context context, Engine engine) {
		Level level = new Level(new Point(10,10));
		
		TowerType flameTowerType = new TowerType();
		flameTowerType.name = "Flame Tower";
		flameTowerType.damageTypes.add(new DamageType());
		flameTowerType.speed = 100;
		flameTowerType.damage = 5;
		flameTowerType.size = new Point(1,1);
		BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(512, 256, TextureOptions.NEAREST);
		flameTowerType.sprite = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, context, "banana_tiled.png", 4, 2);
		engine.getTextureManager().loadTexture(bitmapTextureAtlas);
		level.towerTypes.put(flameTowerType.name, flameTowerType);
		
		TowerType cannonTowerType = new TowerType();
		cannonTowerType.name = "Cannon Tower";
		cannonTowerType.damageTypes.add(new DamageType());
		cannonTowerType.speed = 5;
		cannonTowerType.damage = 100;
		cannonTowerType.size = new Point(2,2);
		BuildableBitmapTextureAtlas bitmapTextureAtlas2 = new BuildableBitmapTextureAtlas(512, 256, TextureOptions.NEAREST);
		cannonTowerType.sprite = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas2, context, "helicopter_tiled.png", 2, 2);
		engine.getTextureManager().loadTexture(bitmapTextureAtlas2);
		level.towerTypes.put(cannonTowerType.name, cannonTowerType);
		
				
		return level;
	}
	
	public static Level createLevelFromXML(BuildableBitmapTextureAtlas bitmapTextureAtlas, Context context) {
		return null; //TODO
	}
	
	public void addTower(Tower tower) {
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
	}
}
