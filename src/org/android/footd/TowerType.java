package org.android.footd;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Point;

public class TowerType {
	String name = "";
	int speed = 1;
	List<DamageType> damageTypes = new ArrayList<DamageType>();
	int damage = 1;
	int splashRadius = 1;
	Point size = new Point(1,1);
	int slow = 0;
	int dot = 0;
	
	TiledTextureRegion sprite;
}
