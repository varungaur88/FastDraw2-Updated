package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class LineTool extends Tool {

    Paint paint;

    public LineTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Line";
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void getReady() {
        paint.setColor(this.drawingLayer.getCurrentColor());
        paint.setStrokeWidth(this.drawingLayer.getCurrentStrokeWidth());
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        canvas.drawLine(start.x, start.y, end.x, end.y, paint);
    }
}
