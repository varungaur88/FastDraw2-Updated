package ca.usask.hci.fastdraw2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;

public class CustomColorTool extends Tool {

    public CustomColorTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Custom Color";
    }

    @Override
    protected void getReady() {
        this.changesCanvas = false;
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        // do nothing
    }

}
