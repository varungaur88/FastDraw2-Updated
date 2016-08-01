package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class RectangleTool extends Tool {

    Paint paint;

    public RectangleTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Rectangle";
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    protected void getReady() {
        paint.setColor(this.drawingLayer.getCurrentColor());
        paint.setStrokeWidth(this.drawingLayer.getCurrentStrokeWidth());
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        float left, right, top, bottom;
        if (end.x > start.x) {
            left = start.x;
            right = end.x;
        } else {
            left = end.x;
            right = start.x;
        }
        if (end.y > start.y) {
            top = start.y;
            bottom = end.y;
        } else {
            top = end.y;
            bottom = start.y;
        }
        canvas.drawRect(left, top, right, bottom, paint);
    }
}
