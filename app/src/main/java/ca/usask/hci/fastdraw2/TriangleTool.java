package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class TriangleTool extends Tool {

    Paint paint;

    public TriangleTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Triangle";
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
        Path path1 = new Path();
        float [] points1 = {start.x,start.y-r/2,(float)(start.x-r/Math.sqrt(2.0)),start.y+r/2,(float)(start.x+r/Math.sqrt(2)),start.y+r/2};
        path1.moveTo(points1[0] , points1[1]);
        path1.lineTo(points1[2],points1[3]);
        path1.lineTo(points1[4],points1[5]);
        path1.lineTo(points1[4],points1[5]);
        path1.close();
        canvas.drawPath(path1,paint);
    }
}
