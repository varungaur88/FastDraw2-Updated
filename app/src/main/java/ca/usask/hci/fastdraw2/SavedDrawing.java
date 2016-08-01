package ca.usask.hci.fastdraw2;

import android.graphics.Bitmap;

public class SavedDrawing {

    private long id;
    private String name;
    private Bitmap thumbnail;

    public SavedDrawing() {
    }

    public String getName() {
        return name;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
