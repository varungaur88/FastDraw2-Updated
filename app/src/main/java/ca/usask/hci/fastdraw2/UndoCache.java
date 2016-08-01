package ca.usask.hci.fastdraw2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class UndoCache {

    private final static String TAG = UndoCache.class.getSimpleName();
    private static final int LOCAL_CACHE_SIZE = 4;

    private Context mContext;
    private ArrayDeque<CacheEntry> stack;
    int cacheEntries;
    private UndoCacheDbHelper db;
    private String drawingName;

    private Object cacheOrganizationLock;

    public UndoCache(Context context, String drawingName) {
        this.drawingName = drawingName;
        this.mContext = context;
        db = new UndoCacheDbHelper(context);
        stack = new ArrayDeque<CacheEntry>();
        cacheEntries = 0;
        // Clear the db
        db.clearAllForDrawing(drawingName);

        cacheOrganizationLock = new Object();
    }

    public boolean isEmpty() {
        return cacheEntries == 0;
    }

    public void push(Bitmap bitmap) {

        // Just a hacky safety precaution...
        if ( DrawingApp.memUsagePercent()  > 0.85 ) {
            bitmap.recycle();
            return;
        }

        synchronized (stack) {
            CacheEntry c = new CacheEntry(drawingName, bitmap, cacheEntries);
            stack.add(c);
            cacheEntries++;
        }
        (new OrganizeCacheTask()).execute(this);
    }

    public Bitmap pop() {
        if (cacheEntries == 0) {
            return null;
        }
        CacheEntry c;
        synchronized (stack) {
            if (stack.isEmpty()) {
                Log.d(TAG, "There are items, but they aren't swapped in!");
                return null;
            }
            c = stack.removeLast();
            cacheEntries--;
        }
        (new OrganizeCacheTask()).execute(this);
        return c.getBitmap();
    }

    protected void organizeCache() {
        synchronized (cacheOrganizationLock) {
            Log.d(TAG, "Organizing cache...");
            // If there are items that should be cached
            LinkedList<CacheEntry> toPush = new LinkedList<CacheEntry>();
            synchronized (stack) {
                while (stack.size() > LOCAL_CACHE_SIZE) {
                    toPush.add(stack.removeFirst());
                }
            }
            for (CacheEntry ce : toPush) {
                db.push(ce);
            }

            // If we need to de-cache items
            int numToPop = LOCAL_CACHE_SIZE - stack.size();
            while (numToPop > 0) {
                CacheEntry c = db.pop(this.drawingName);
                if (c != null) {
                    synchronized (stack) {
                        stack.addFirst(c);
                    }
                } else {
                    break;
                }
                numToPop--;
            }
        }
    }

    private class OrganizeCacheTask extends AsyncTask<UndoCache, Void, Void> {
        @Override
        protected Void doInBackground(UndoCache... cache) {
            Log.d(TAG, "Starting async cache organization in background...");
            cache[0].organizeCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "Cache organization in background complete!");
            System.gc();
        }
    }

    private class CacheEntry {
        private long id; // id in the database
        private Bitmap bitmap;
        private String drawingName;
        private int revision;

        public CacheEntry() {
            this.id = -1;
        }

        public CacheEntry(String drawingName, Bitmap bitmap, int revision) {
            this.id = -1;
            this.drawingName = drawingName;
            this.bitmap = bitmap;
            this.revision = revision;
        }

        public long getId() {
            return id;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getDrawingName() {
            return drawingName;
        }

        public int getRevision() {
            return revision;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void setDrawingName(String drawingName) {
            this.drawingName = drawingName;
        }

        public void setRevision(int revision) {
            this.revision = revision;
        }
    }

    private class UndoCacheDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "FastDraw2UndoDB";
        private static final String TABLE_NAME = "UndoStack";
        private static final String KEY_ID = "_id";
        private static final String KEY_DRAWING_NAME = "drawing_name";
        private static final String KEY_REVISION = "revision";
        private static final String KEY_BITMAP = "bitmap";

        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_DRAWING_NAME + " VARCHAR(100) NOT NULL,"
                + KEY_REVISION + " INTEGER NOT NULL,"
                + KEY_BITMAP + " BLOB NOT NULL);";

        public UndoCacheDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(database);
        }

        public synchronized void push(CacheEntry c) {
            Log.d(TAG, "Caching to db: " + c.getRevision());

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DRAWING_NAME, c.getDrawingName());
            values.put(KEY_REVISION, c.getRevision());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            c.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
            values.put(KEY_BITMAP, baos.toByteArray());
            long id = db.insert(TABLE_NAME, null, values);

            c.setId(id);
            c.getBitmap().recycle();
            c.setBitmap(null);

            db.close();
        }

        public synchronized CacheEntry pop(String drawingName) {
            SQLiteDatabase db;
            db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, KEY_DRAWING_NAME+"=?", new String[] { drawingName }, null, null, KEY_REVISION + " DESC");
            CacheEntry c = null;
            if (cursor.moveToFirst()) {
                c = new CacheEntry();
                c.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                c.setRevision(cursor.getInt(cursor.getColumnIndex(KEY_REVISION)));
                c.setDrawingName(cursor.getString(cursor.getColumnIndex(KEY_DRAWING_NAME)));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inMutable = true;
                c.setBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts));
            }
            cursor.close();
            db.close();

            // Delete entry from the database
            if (c != null) {
                db = this.getWritableDatabase();
                db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(c.getId())});
                db.close();
            }

            return c;
        }

        public void clearAllForDrawing(String drawingName) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, KEY_DRAWING_NAME+"=?", new String[]{ drawingName });
            db.close();
        }

    }
}
