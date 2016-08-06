package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class HeartTool extends Tool {

    Paint paint;

    public HeartTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Heart";
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
//        canvas.drawCircle(cx, cy, r, paint);

        Path path1 = new Path();
        float length = r/2;
        float x = start.x;
        float y = start.y;
        canvas.rotate(45,x,y);

        path1.moveTo(x,y);
        path1.lineTo(x-length, y);
        path1.arcTo(new RectF(x-length-(length/2),y-length,x-(length/2),y),90,180);
        path1.arcTo(new RectF(x-length,y-length-(length/2),x,y-(length/2)),180,180);
        path1.lineTo(x,y);
        path1.close();

        canvas.drawPath(path1, paint);
        canvas.rotate(-45,x,y);
    }
}
