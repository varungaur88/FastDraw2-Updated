package ca.usask.hci.fastdraw2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DrawingActivity extends ActionBarActivity {

	private static final String TAG = DrawingActivity.class.getSimpleName();
    public static final int SEND_DATA_REQUEST_ID = 1;

	private DrawingView drawingView;
	private SavedDrawing drawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(MainActivity.EXTRA_DRAWING_ID, -1);
        this.drawing = DrawingApp.drawingDb.get(id);

        // Start logging
        StudyLogger.getInstance().startOrResumeLog(drawing.getName());
        StudyLogger.getInstance().createLogEvent("event.activitystate")
                .attr("state", "oncreate")
                .commit();

        Bitmap bitmap = null;
        bitmap = DrawingApp.drawingDb.getBitmapForDrawing(drawing);
        if (bitmap != null) {
            drawingView = new DrawingView(this, bitmap);
        } else {
            drawingView = new DrawingView(this);
        }
        setContentView(drawingView);
        Log.d(TAG, "View added");

    }

    public void saveImage() {
        Bitmap bitmap = drawingView.getDrawingLayer().getDrawingBitmap();
        DrawingApp.drawingDb.saveBitmapForDrawing(drawing, bitmap);
        // Save thumbnail
        int newWidth = DrawingApp.THUMBNAIL_WIDTH;
        int newHeight = (int)(bitmap.getHeight()*(newWidth/(float)bitmap.getWidth()));
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, newWidth, newHeight);
        drawing.setThumbnail(thumbnail);
        DrawingApp.drawingDb.updateDrawing(drawing);
    }

    public void sendData() {
        saveImage();
        Intent intent = DrawingApp.prepareSendDataIntent(drawing.getId());
        this.startActivityForResult(Intent.createChooser(intent, "Send email..."), SEND_DATA_REQUEST_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save_drawing) {
            StudyLogger.getInstance().createLogEvent("event.savedrawing").commit();
            saveImage();
        } else if (id == R.id.action_send_drawing) {
            StudyLogger.getInstance().createLogEvent("event.senddrawing")
                    .attr("from", "DrawingActivity")
                    .commit();
            sendData();
        } else if (id == R.id.action_clear) {
            this.drawingView.clearCanvas();
        }
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
    	Log.d(TAG, "Destroying...");
        StudyLogger.getInstance().createLogEvent("event.activitystate")
                .attr("state", "ondestroy")
                .commit();
    	super.onDestroy();
    }
    
    @Override
    protected void onStop() {
    	Log.d(TAG, "Stopping...");
        StudyLogger.getInstance().createLogEvent("event.activitystate")
                .attr("state", "onstop")
                .commit();
    	super.onStop();
    }
    
    @Override
    protected void onPause() {
    	Log.d(TAG, "Pausing...");
        StudyLogger.getInstance().createLogEvent("event.activitystate")
                .attr("state", "onpause")
                .commit();
        saveImage();
        drawingView.shutdownGameThread();
    	super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Starting...");
        StudyLogger.getInstance().createLogEvent("event.activitystate")
                .attr("state", "onstart")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    	Log.d(TAG, "Resuming...");
        StudyLogger.getInstance().createLogEvent("event.activitystate")
                .attr("state", "onresume")
                .commit();
        if (drawingView.isSurfaceCreated()) {
            Log.d(TAG, "Re-starting game thread...");
            drawingView.startGameThread();
        }
    }
}
