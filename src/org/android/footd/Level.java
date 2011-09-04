package org.android.footd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

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
	Wave currentWave;
	
	private TextureRegion fullBackGround;
	
	GridObject[][] placedObjects;
	TiledSprite background;

	Boolean isCellFree(int x, int y){
		return placedObjects[x][y] == null;
	}
	
	public Level(Point size) {
		placedObjects = new GridObject[size.x][size.y];
	}
	
	public static int toNextPowerOfTwo(int foo) {
		int powerOfTwoInt = 2;
		while (foo > powerOfTwoInt)
			powerOfTwoInt *= 2;
		return powerOfTwoInt;
	}
	
	public static Point getOptimalSamplerSize(Context context, String name) {
		//TODO hardcoded source folder. there is no BitmapTextureAtlasTextureRegionFactory.getAssetBasePath :(
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = 
			new AssetBitmapTextureAtlasSource(context, "gfx/" + name);
		
		Point size = new Point();
		size.x = toNextPowerOfTwo(bitmapTextureAtlasSource.getWidth());
		size.y = toNextPowerOfTwo(bitmapTextureAtlasSource.getHeight());
		
		return size;
	}
	
	// Tiled Sprites
	public static TiledTextureRegion readSprite(Engine engine, Context context, Point tiles, String name) {
		return readSprite(engine, context, tiles, name, TextureOptions.DEFAULT);
	}
	
	public static TiledTextureRegion readSprite(Engine engine, Context context, Point tiles, String name, final TextureOptions textureOptions) {
		Point size = getOptimalSamplerSize(context, name);
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(size.x, size.y, textureOptions);
		TiledTextureRegion sprite = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(atlas, context, name, 0, 0, tiles.x, tiles.y);
		engine.getTextureManager().loadTexture(atlas);
		return sprite;
	}
	
	// Full Sprites
	public static TextureRegion readSprite(Engine engine, Context context, String name, final TextureOptions textureOptions) {
		Point size = getOptimalSamplerSize(context, name);
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(size.x, size.y, textureOptions);
		TextureRegion sprite = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, context, name, 0, 0);
		engine.getTextureManager().loadTexture(atlas);
		return sprite;
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
		flameTowerType.sprite = readSprite(engine, context, new Point(2, 2), "helicopter_tiled.png");
		level.towerTypes.put(flameTowerType.name, flameTowerType);
		
		TowerType cannonTowerType = new TowerType();
		cannonTowerType.name = "Cannon Tower";
		cannonTowerType.damageTypes.add(new DamageType());
		cannonTowerType.speed = 5;
		cannonTowerType.damage = 100;
		cannonTowerType.size = new Point(2,2);
		cannonTowerType.sprite = readSprite(engine, context, new Point(4, 2), "banana_tiled.png");
		level.towerTypes.put(cannonTowerType.name, cannonTowerType);
		
		level.fullBackGround = readSprite(engine, context, background, TextureOptions.BILINEAR);
		level.waves.add(new Wave(engine, context));
		level.currentWave = level.waves.get(0);
		return level;
	}
	
	public static Level createLevelFromXML(BuildableBitmapTextureAtlas bitmapTextureAtlas, Context context) {
		return null; //TODO
	}
	
	public void spawnMob(Scene scene) {
		currentWave.spawnMob(scene);
	}
	
	public void init(Scene scene) {
//		final int centerX = (- fullBackGround.getWidth()) / 2;
//		final int centerY = (- fullBackGround.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite backGroundSprite = new Sprite(0, 0, fullBackGround);
		scene.attachChild(backGroundSprite);
		
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
