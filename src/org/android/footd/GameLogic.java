package org.android.footd;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
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
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Point;
import android.widget.Toast;

public class GameLogic extends BaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Scene scene;
	private ZoomCamera zoomCamera;
	private Level level;
	
	//mutitouch stuff
	private SurfaceScrollDetector scrollDetector;
	private PinchZoomDetector pinchZoomDetector;
	private float pinchZoomStartedCameraZoomFactor;
	
	@Override
	public Engine onLoadEngine() {		
		zoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), zoomCamera));
		
		try {
			if(MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
			} else {
//				Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
			}
		} catch (final MultiTouchException e) {
			Toast.makeText(this, "Sorry your Android Version does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
		}
		
		return engine;
	}

	@Override
	public void onLoadResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		
		level = Level.createTestLevel(this, mEngine, "grass2.jpg");
		
	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene();
		level.init(scene);
		
		level.addTower(new Tower(new Point(3,6), level.towerTypes.get("Flame Tower")), scene);
		level.addTower(new Tower(new Point(7,2), level.towerTypes.get("Cannon Tower")), scene);
		
		// TOUCH STUFF
		scrollDetector = new SurfaceScrollDetector(this);
		if(MultiTouch.isSupportedByAndroidVersion()) {
			try {
				pinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				pinchZoomDetector = null;
			}
		} else {
			pinchZoomDetector = null;
		}

		scene.setOnSceneTouchListener(this);
		scene.setTouchAreaBindingEnabled(true);		
		
		
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
		pinchZoomStartedCameraZoomFactor = zoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		zoomCamera.setZoomFactor(pinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		zoomCamera.setZoomFactor(pinchZoomStartedCameraZoomFactor * pZoomFactor);
	}


	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pinchZoomDetector != null) {
			pinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(pinchZoomDetector.isZooming()) {
				scrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.isActionDown()) {
					scrollDetector.setEnabled(true);
				}
				scrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			scrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}}
