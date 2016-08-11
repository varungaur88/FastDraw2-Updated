package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class SquareTool extends Tool {

    Paint paint;

    public SquareTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Square";
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    protected void getReady() {
        paint.setColor(this.drawingLayer.getCurrentColor());;
        paint.setStrokeWidth(this.drawingLayer.getCurrentStrokeWidth());
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        float left, right, top, bottom,check=1;
        if (end.x > start.x) {
            left = start.x;
            right = end.x;
        } else {
            left = end.x;
            right = start.x;
        }
        top = start.y;

        if (end.y > start.y) {

        } else {
            check = -1;
        }

        float radius = right - left;
        canvas.drawRect(left, top, left + radius, top +  check*radius, paint);
    }
}