package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class EraserTool extends Tool {

    Paint paint;

    public EraserTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Eraser";
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void getReady() {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(this.drawingLayer.getCurrentStrokeWidth());
    }

    @Override
    protected void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas) {
        canvas.drawPath(this.pathsById.get(id), paint);
    }
}
