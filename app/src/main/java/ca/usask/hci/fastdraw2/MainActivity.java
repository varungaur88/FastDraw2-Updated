package ca.usask.hci.fastdraw2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_DRAWING_ID = "ca.usask.hci.fastdraw2.DRAWING_ID";
    public static final int SEND_DATA_REQUEST_ID = 1;

    private RecyclerView mRecView;
    private SavedDrawingAdapter mRecAdapter;
    private RecyclerView.LayoutManager mRecLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view and adapter
        mRecView = (RecyclerView) findViewById(R.id.card_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecView.setLayoutManager(llm);
        mRecView.setItemAnimator(new DefaultItemAnimator());
        mRecAdapter = new SavedDrawingAdapter(null, R.layout.card_layout, this);
        mRecView.setAdapter(mRecAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new RefreshListTask()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_drawing) {
            createNewDrawing();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewDrawing() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.US);
        String name = formatter.format(new Date());
        SavedDrawing sd = new SavedDrawing();
        sd.setName(name);
        DrawingApp.drawingDb.addDrawing(sd);
        openDrawing(sd.getId());
    }

    public void openDrawing(long id) {
        Intent intent = new Intent(this, DrawingActivity.class);
        intent.putExtra(EXTRA_DRAWING_ID, id);
        startActivity(intent);
    }

    public void confirmDeleteDrawing(long id) {
        final long drawing_id = id; // for access in closure
        new AlertDialog.Builder(this)
                .setTitle("Delete drawing?")
                .setMessage("This action cannot be undone.")
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDrawing(drawing_id);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteDrawing(long id) {
        SavedDrawing sd = DrawingApp.drawingDb.get(id);
        Bitmap bitmap = DrawingApp.drawingDb.getBitmapForDrawing(sd);

        // Save a copy of the final image, in case of accidental deletion
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

        DrawingApp.drawingDb.deleteDrawing(sd);

        (new RefreshListTask()).execute();
    }

    public void sendData(long id) {
        Intent intent = DrawingApp.prepareSendDataIntent(id);
        this.startActivityForResult(Intent.createChooser(intent, "Send email..."), SEND_DATA_REQUEST_ID);
    }

    private class RefreshListTask extends AsyncTask<Void, Void, ArrayList<SavedDrawing>> {
        protected ArrayList<SavedDrawing> doInBackground(Void... params) {
            ArrayList<SavedDrawing> drawings;
            drawings = DrawingApp.drawingDb.getAll();
            return drawings;
        }

        protected void onPostExecute(ArrayList<SavedDrawing> result) {
            mRecAdapter.swapData(result);
        }
    }

}
