package org.android.footd;

import java.util.Map;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;

import android.widget.Toast;

public class FooTD extends BaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private ZoomCamera mZoomCamera;

	private TextureRegion mGrassTex;

	private Scene mScene;

	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	
	private TiledTextureRegion mSnapdragonTex;
	private TiledTextureRegion mHelicopterTex;
	private TiledTextureRegion mBananaTex;
	private TiledTextureRegion mFaceTex;
	private TiledTextureRegion mExploTex;
	private TiledTextureRegion mExploTex2;
	private TextureRegion mGNUTex;
	private TextureRegion mSpaceTex;
	AssetBitmapTextureAtlasSource grassSource;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTextureRegion;
	
	Map<String, BaseTextureRegion> textures;

	@Override
	public Engine onLoadEngine() {
		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mZoomCamera));

		try {
			if(MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
			} else {
				Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
			}
		} catch (final MultiTouchException e) {
			Toast.makeText(this, "Sorry your Android Version does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
		}

		return engine;
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(512,	256, TextureOptions.NEAREST);
		this.mSnapdragonTex = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, this, "snapdragon_tiled.png", 0, 0, 4, 3);
		this.mHelicopterTex = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, this, "helicopter_tiled.png", 400, 0, 2, 2);
		this.mBananaTex = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, this, "banana_tiled.png", 0, 180, 4, 2);
		this.mFaceTex = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, this, "face_box_tiled.png", 132, 180, 2, 1);
		this.mEngine.getTextureManager().loadTexture(bitmapTextureAtlas);
		
		BitmapTextureAtlas exploAtlas = new BitmapTextureAtlas(512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mExploTex = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(exploAtlas, this, "explosion.png", 0, 0, 4, 4);
		this.mExploTex2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(exploAtlas, this, "explosion2.png", 256, 0, 4, 4);
		this.mEngine.getTextureManager().loadTexture(exploAtlas);
		
		BitmapTextureAtlas backGroundAtlas = new BitmapTextureAtlas(2048, 2048, TextureOptions.BILINEAR);
		this.mGrassTex = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backGroundAtlas, this, "grass2.jpg", 0, 0);
		this.mEngine.getTextureManager().loadTexture(backGroundAtlas);
		
		BitmapTextureAtlas backGround2Atlas = new BitmapTextureAtlas(1024, 512, TextureOptions.BILINEAR);
		this.mSpaceTex = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backGround2Atlas, this, "space.jpg", 0, 0);
		this.mEngine.getTextureManager().loadTexture(backGround2Atlas);

		BitmapTextureAtlas gnuAtlas = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGNUTex = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gnuAtlas, this, "gnu.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(gnuAtlas);
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(128, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "player.png", 0, 0, 3, 4);
		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setOnAreaTouchTraversalFrontToBack();

		
		/* Calculate the coordinates for the face, so its centered on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mGrassTex.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mGrassTex.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite grass = new Sprite(centerX, centerY, this.mGrassTex);
		this.mScene.attachChild(grass);
		
		final Sprite gnu = new Sprite(0, 0, this.mGNUTex);
		this.mScene.attachChild(gnu);
		
		final Sprite space = new Sprite(0, 0, this.mSpaceTex);
		this.mScene.setBackground(new SpriteBackground(space));

		/* Quickly twinkling face. */
		final AnimatedSprite face = new AnimatedSprite(100, 50, this.mFaceTex);
		face.animate(100);
		this.mScene.attachChild(face);

		/* Continuously flying helicopter. */
		final AnimatedSprite helicopter = new AnimatedSprite(320, 50, this.mHelicopterTex);
		helicopter.animate(new long[] { 100, 100 }, 1, 2, true);
		this.mScene.attachChild(helicopter);

		/* Snapdragon. */
		final AnimatedSprite snapdragon = new AnimatedSprite(300, 200, this.mSnapdragonTex);
		snapdragon.animate(100);
		this.mScene.attachChild(snapdragon);

		/* Funny banana. */
		final AnimatedSprite banana = new AnimatedSprite(100, 220, this.mBananaTex);
		banana.animate(100);
		this.mScene.attachChild(banana);
		
		final AnimatedSprite explosion = new AnimatedSprite(300, 0, this.mExploTex);
		explosion.animate(100);
		this.mScene.attachChild(explosion);
		
		final AnimatedSprite explosion2 = new AnimatedSprite(400, 0, this.mExploTex2);
		explosion2.animate(100);
		this.mScene.attachChild(explosion2);
		
		this.mScrollDetector = new SurfaceScrollDetector(this);
		if(MultiTouch.isSupportedByAndroidVersion()) {
			try {
				this.mPinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				this.mPinchZoomDetector = null;
			}
		} else {
			this.mPinchZoomDetector = null;
		}

		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingEnabled(true);		
		
		/* Create the face and add it to the scene. */
		final AnimatedSprite player = new AnimatedSprite(10, 10, 48, 64, this.mPlayerTextureRegion);

		final Path path = new Path(5)
			.to(10, 10)
			.to(10, CAMERA_HEIGHT - 74)
			.to(CAMERA_WIDTH - 58, CAMERA_HEIGHT - 74)
			.to(CAMERA_WIDTH - 58, 10)
			.to(10, 10);

		/* Add the proper animation when a waypoint of the path is passed. */
		player.registerEntityModifier(new LoopEntityModifier(new PathModifier(30, path, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				Debug.d("onPathStarted");
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointStarted:  " + pWaypointIndex);
				switch(pWaypointIndex) {
					case 0:
						player.animate(new long[]{200, 200, 200}, 6, 8, true);
						break;
					case 1:
						player.animate(new long[]{200, 200, 200}, 3, 5, true);
						break;
					case 2:
						player.animate(new long[]{200, 200, 200}, 0, 2, true);
						break;
					case 3:
						player.animate(new long[]{200, 200, 200}, 9, 11, true);
						break;
				}
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointFinished: " + pWaypointIndex);
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				Debug.d("onPathFinished");
			}
		}, EaseSineInOut.getInstance())));
		this.mScene.attachChild(player);
		
		
		
		return this.mScene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}


	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(this.mPinchZoomDetector != null) {
			this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(this.mPinchZoomDetector.isZooming()) {
				this.mScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.isActionDown()) {
					this.mScrollDetector.setEnabled(true);
				}
				this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}
}
