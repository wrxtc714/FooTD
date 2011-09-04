package org.android.footd;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
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
import org.anddev.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Point;
import android.widget.Toast;

public class GameLogic extends BaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Scene scene;
	private ZoomCamera zoomCamera;
	private Level level;
	private TiledTextureRegion mobRegion;
	
	//mutitouch stuff
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	
	@Override
	public Engine onLoadEngine() {		
		zoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), zoomCamera));
		
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
		BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(512, 256, TextureOptions.NEAREST);
		
		BitmapTextureAtlas mobAtlas = new BitmapTextureAtlas(128, 128, TextureOptions.DEFAULT);
		mobRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mobAtlas, this, "player.png", 0, 0, 3, 4);
		mEngine.getTextureManager().loadTexture(mobAtlas);
		
		
		level = Level.createTestLevel(bitmapTextureAtlas, this);
		
	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene();
		
		
		level.addTower(new Tower(new Point(3,6), level.towerTypes.get("Flame Tower")));
		level.addTower(new Tower(new Point(7,2), level.towerTypes.get("Flame Tower")));
		for (Tower tower : level.towers) {
			scene.attachChild(tower);
		}
		
		for (int i = 0; i < 20; i++){
			
			int mobTranslation = i * 50;
			
			final Path path = new Path(5)
				.to(mobTranslation, 10)
				.to(mobTranslation, CAMERA_HEIGHT - 74)
				.to(mobTranslation + CAMERA_WIDTH - 58, CAMERA_HEIGHT - 74)
				.to(mobTranslation + CAMERA_WIDTH - 58, 10)
				.to(mobTranslation, 10);
			
			scene.attachChild(new Mob(0,0, 48, 64, mobRegion, path));
		}
		
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}


	void cycle(){
		
	}
	
	//MULTITOUCH STUFF
	
	
	@Override
	public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = zoomCamera.getZoomFactor();
		zoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		mPinchZoomStartedCameraZoomFactor = zoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		zoomCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		zoomCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}


	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(mPinchZoomDetector != null) {
			mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(mPinchZoomDetector.isZooming()) {
				mScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.isActionDown()) {
					mScrollDetector.setEnabled(true);
				}
				mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}}
