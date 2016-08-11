package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.Arrays;

public class CircleTool extends Tool {

    Paint paint;

    public CircleTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Circle";
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void getReady() {
        paint.setColor(this.drawingLayer.getCurrentColor());
        paint.setStrokeWidth(this.drawingLayer.getCurrentStrokeWidth());
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        float cx, cy, r;
        cx = (start.x + end.x)/2;
        cy = (start.y + end.y)/2;
        r = GeometryUtils.dist(start.x, start.y, cx, cy);
        canvas.drawCircle(cx, cy, r, paint);
    }
}
