package ca.usask.hci.fastdraw2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.LinkedList;
import java.util.Queue;

public class ColorPickerTool extends Tool {

    public ColorPickerTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Color Picker";
    }

    @Override
    protected void getReady() {
        this.changesCanvas = false;
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        // do nothing
    }

    @Override
    public void touchStop(int id, float x, float y, Bitmap bitmap) {
        int color = bitmap.getPixel((int)x, (int)y);
        this.drawingLayer.setCurrentColor(color);
    }

}
