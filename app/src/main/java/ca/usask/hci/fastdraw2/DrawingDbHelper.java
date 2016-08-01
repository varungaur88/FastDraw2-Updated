package ca.usask.hci.fastdraw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DrawingDbHelper extends SQLiteOpenHelper {

    private static String TAG = DrawingDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "FastDraw2DB";
    private static final String TABLE_NAME = "Drawings";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_THUMBNAIL = "thumbnail";

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
                                                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                                + KEY_NAME + " VARCHAR(100) NOT NULL,"
                                                + KEY_IMAGE + " BLOB,"
                                                + KEY_THUMBNAIL + " BLOB);";

    public DrawingDbHelper(Context context) {
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

    public ArrayList<SavedDrawing> getAll() {
        ArrayList<SavedDrawing> drawings = new ArrayList<SavedDrawing>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, KEY_NAME + " DESC");
        SavedDrawing sd = null;
        if (cursor.moveToFirst()) {
            do {
                sd = new SavedDrawing();
                sd.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                sd.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(KEY_THUMBNAIL));
                if (bytes != null) {
                    sd.setThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                } else {
                    sd.setThumbnail(null);
                }
                drawings.add(sd);
            } while(cursor.moveToNext());
        }
        return drawings;
    }

    public SavedDrawing get(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+"=?", new String[] {Long.toString(id)}, null, null, null);
        SavedDrawing sd = null;
        if (cursor.moveToFirst()) {
            sd = new SavedDrawing();
            sd.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            sd.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(KEY_THUMBNAIL));
            if (bytes != null) {
                sd.setThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            } else {
                sd.setThumbnail(null);
            }
        }
        cursor.close();
        db.close();
        return sd;
    }

    public int updateDrawing(SavedDrawing sd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, sd.getName());
        if (sd.getThumbnail() != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            sd.getThumbnail().compress(Bitmap.CompressFormat.PNG, 100, baos);
            values.put(KEY_THUMBNAIL, baos.toByteArray());
        }
        int i = db.update(TABLE_NAME, values, KEY_ID+"=?", new String[] { String.valueOf(sd.getId()) });
        db.close();

        return i;
    }

    public void addDrawing(SavedDrawing sd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, sd.getName());
        if (sd.getThumbnail() != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            sd.getThumbnail().compress(Bitmap.CompressFormat.PNG, 100, baos);
            values.put(KEY_THUMBNAIL, baos.toByteArray());
        }
        long id = db.insert(TABLE_NAME, null, values);
        sd.setId(id);
        db.close();
    }

    public void deleteDrawing(SavedDrawing sd) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{ String.valueOf(sd.getId()) });
        db.close();
    }

    public Bitmap getBitmapForDrawing(SavedDrawing sd) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+"=?", new String[] {Long.toString(sd.getId())}, null, null, null);
        Bitmap bitmap = null;
        if (cursor.moveToFirst()) {
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
            if (bytes != null) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inMutable = true;
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            }
        }
        cursor.close();
        db.close();
        return bitmap;
    }

    public int saveBitmapForDrawing(SavedDrawing sd, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        values.put(KEY_IMAGE, baos.toByteArray());

        int i = db.update(TABLE_NAME, values, KEY_ID+"=?", new String[] { String.valueOf(sd.getId()) });
        db.close();

        return i;
    }
}
