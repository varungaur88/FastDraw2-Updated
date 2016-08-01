package ca.usask.hci.fastdraw2;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class SavedDrawingAdapter extends RecyclerView.Adapter<SavedDrawingAdapter.ViewHolder> {

    private final String TAG = SavedDrawingAdapter.class.getSimpleName();

    private ArrayList<SavedDrawing> drawings;

    private int rowLayout;
    private Context mContext;

    public SavedDrawingAdapter(ArrayList<SavedDrawing> drawings, int rowLayout, Context context) {
        this.drawings = drawings;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.setHasStableIds(true);
    }

    public void swapData(ArrayList<SavedDrawing> newDrawings) {
        this.drawings = newDrawings;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        final ViewHolder vh = new ViewHolder(v);

        vh.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).openDrawing(vh.getSavedDrawing().getId());
            }
        });

        vh.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).sendData(vh.getSavedDrawing().getId());
            }
        });

        vh.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).confirmDeleteDrawing(vh.getSavedDrawing().getId());
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SavedDrawing drawing = drawings.get(position);
        holder.setSavedDrawing(drawing);
        holder.name.setText(drawing.getName());
        holder.thumbnail.setImageBitmap(drawing.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return drawings == null ? 0 : drawings.size();
    }

    @Override
    public long getItemId(int position) {
        return drawings.get(position).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected ImageView thumbnail;
        protected Button editButton;
        protected Button sendButton;
        protected Button deleteButton;
        private SavedDrawing savedDrawing;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.drawing_name);
            thumbnail = (ImageView)itemView.findViewById(R.id.drawing_thumbnail);
            editButton = (Button)itemView.findViewById(R.id.drawing_edit);
            sendButton = (Button)itemView.findViewById(R.id.drawing_send);
            deleteButton = (Button)itemView.findViewById(R.id.drawing_delete);
        }

        public SavedDrawing getSavedDrawing() {
            return this.savedDrawing;
        }

        public void setSavedDrawing(SavedDrawing savedDrawing) {
            this.savedDrawing = savedDrawing;
        }
    }
}
