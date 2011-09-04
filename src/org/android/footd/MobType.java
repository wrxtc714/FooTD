package org.android.footd;

import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.graphics.Point;

public class MobType {
    public enum MobAbility { Normal, Flying, Immune }

    DamageType type;
    List<MobAbility> abilities;
    public Point size;
	public TiledTextureRegion texture;

    public MobType(Engine engine, Context context, Point size, Point tiles, String textureName) {
    	this.size = size;
    	texture = Level.readSprite(engine, context, tiles, textureName);
    }
}
