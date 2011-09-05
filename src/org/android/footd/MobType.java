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
    public Point size;
	public TiledTextureRegion texture;

    public MobType(Engine engine, Context context, Point size, Point tiles, String textureName) {
    	this.size = size;
    	texture = Level.readSprite(engine, context, tiles, textureName);
    }
}
