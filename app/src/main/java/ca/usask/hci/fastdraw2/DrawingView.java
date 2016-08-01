package ca.usask.hci.fastdraw2;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = DrawingView.class.getSimpleName();

	private MainThread thread;
    private boolean surfaceCreated;
	private int width;
	private int height;

    private DrawingLayer drawingLayer;

	public DrawingView(Context context) {
		super(context);

        this.surfaceCreated = false;
        // add the callback (this) to the surface holder, so we can intercept events
		getHolder().addCallback(this);
        // make the DrawingView focusable so it can handle events
        setFocusable(true);

        this.drawingLayer = new DrawingLayer(this);
    }

    public DrawingView(Context context, Bitmap bitmap) {
        this(context);
        this.drawingLayer.setDrawingBitmap(bitmap);
    }
	
	public int getW() {
		return this.width;
	}
	
	public int getH() {
		return this.height;
	}

    public boolean isSurfaceCreated() {
        return surfaceCreated;
    }

    public DrawingLayer getDrawingLayer() {
        return this.drawingLayer;
    }

	public void shutdownGameThread() {
		this.thread.setRunning(false);
	}

    public void startGameThread() {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created.");
        startGameThread();
        surfaceCreated = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		Log.d(TAG, "Surface destroying...");
        this.surfaceCreated = false;
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again to shut down the thread
			}
		}
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// Event handling scheme:
    	// - All touch events get sent to all objects
    	// - An object can claim a pointer, by registering itself in the pointerOwners hash map in this object
    	// - Objects check if pointers for events are owned by other objects, and if so, they should ignore them
    	//   * When they do so, they should also clean up any state they have saved for that pointer
    	//
    	// Note: This scheme can result in individual touch events being handled by multiple objects, but that is OK.
    	//       The key thing is that claiming ownership over events allows us to prevent this to happen when it is
    	//       actually undesirable (e.g., menu selections shouldn't also be registered as swipes to try and kill
    	//       enemies), while permitting it the rest of the time.

        this.drawingLayer.handleTouchEvent(event);

    	return true;
    }

    public void render(Canvas canvas) {
    	canvas.drawColor(Color.WHITE);
        this.drawingLayer.draw(canvas);
    }
    
    public void update(long delta) {
        this.drawingLayer.update(delta);
    }

    public void clearCanvas() {
        this.drawingLayer.clearCanvas();
    }

}