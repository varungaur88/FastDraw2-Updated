package ca.usask.hci.fastdraw2;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DrawingApp extends Application {

	private static final String TAG = DrawingApp.class.getSimpleName();

    public static final int THUMBNAIL_WIDTH = 200;

    public static DrawingDbHelper drawingDb;
    public static File logDir;
    public static File imageFileDir;

	@Override
	public void onCreate() {
        super.onCreate();
        drawingDb = new DrawingDbHelper(this);
        logDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FastDraw2/logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        imageFileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FastDraw2/drawings");
        if (!imageFileDir.exists()) {
            imageFileDir.mkdirs();
        }
	}

    public static Intent prepareSendDataIntent(long id) {
        SavedDrawing sd = DrawingApp.drawingDb.get(id);
        Bitmap bitmap = DrawingApp.drawingDb.getBitmapForDrawing(sd);

        File imageFile = new File(DrawingApp.imageFileDir, sd.getName() + ".jpg");
        try {
            // output image to file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            fos.close();
        } catch (IOException e) {
            Log.d(TAG, "Error while trying to write temporary file!");
            e.printStackTrace();
        } finally {
            bitmap.recycle();
        }

        File logFile = new File(DrawingApp.logDir, sd.getName() + ".log");

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"ben.lafreniere@usask.ca"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "FastDraw log " + sd.getName());

        intent.putExtra(Intent.EXTRA_TEXT, "Drawing and log files are attached.");
        // This line throws an internal exception, due to the following issue:
        // https://code.google.com/p/android/issues/detail?id=38303

        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(Uri.fromFile(logFile));
        uris.add(Uri.fromFile(imageFile));

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        return intent;
    }

    public static double memUsagePercent() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        return ((double)usedMemInMB)/((double)maxHeapSizeInMB);
    }

}
