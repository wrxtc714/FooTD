package org.android.footd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.graphics.Point;

public class MobType {
    public enum MobAbility { Normal, Flying, Immune }

    Map<String, Point> ranges = new HashMap<String, Point>();
    DamageType type;
    List<MobAbility> abilities;
    private float scale;
	public TiledTextureRegion texture;
	float speed;

    public MobType(Engine engine, Context context, float scale, Point tiles, String textureName, float speed) {
    	this.scale = scale;
    	this.speed = speed;
    	texture = Level.readSprite(engine, context, tiles, textureName);
    }

    public MobType(Engine engine, Context context, Point tiles, String textureName, float speed) {
    	this.scale = 1.0f;
    	this.speed = speed;
    	texture = Level.readSprite(engine, context, tiles, textureName);
    }
    
    public MobType(Engine engine, Context context, Point tiles, String textureName) {
    	this.scale = 1.0f;
    	this.speed = 1.0f;
    	texture = Level.readSprite(engine, context, tiles, textureName);
    }

    public int getWidth() {
    	return (int) (scale * texture.getTileWidth());
    }

    public int getHeight() {
    	return (int) (scale * texture.getTileHeight());
    }
}
