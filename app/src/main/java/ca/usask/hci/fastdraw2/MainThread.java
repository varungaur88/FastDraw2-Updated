package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

	private static final String TAG = MainThread.class.getSimpleName();
	private final static int MAX_FPS = 40;
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;
	
	private SurfaceHolder surfaceHolder;
	private DrawingView drawingView;
	private boolean running;
	
	public MainThread(SurfaceHolder surfaceHolder, DrawingView drawingView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.drawingView = drawingView;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

    public boolean isRunning() {
        return this.running;
    }

	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		
		long lastUpdate;
		long curTimestamp;
		long delta;
		int sleepTime;
		
		lastUpdate = SystemClock.uptimeMillis();
		while (running) {
			canvas = null;
			// try to lock the canvas for pixel editing
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder) {
					curTimestamp = SystemClock.uptimeMillis();
					delta = curTimestamp - lastUpdate;
					// update the game state
					this.drawingView.update(delta);
					// render the game state to the screen
					if (canvas == null) {
						Log.d(TAG, "Canvas is null!");
					}
					this.drawingView.render(canvas);
					lastUpdate = curTimestamp;
					sleepTime = (int)(FRAME_PERIOD - (SystemClock.uptimeMillis() - curTimestamp));
					
					if (sleepTime > 0) {
						try {
							// send this thread to sleep, to save the battery
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}
				}
			} finally {
				// in the case of an exception, we don't want the surface to be left in an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
        Log.d(TAG, "Game loop has exited.");
	}
	
}
