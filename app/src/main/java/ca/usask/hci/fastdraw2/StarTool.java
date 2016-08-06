package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.Arrays;

public class StarTool extends Tool {

    Paint paint;

    public StarTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Star";
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
        float [] points1 = {start.x,start.y-r/2,(float)(start.x-r/Math.sqrt(2.0)),start.y+r/2,(float)(start.x+r/Math.sqrt(2)),start.y+r/2};
        float [] points = {start.x,start.y+r/2 + r/3,(float)(start.x-r/Math.sqrt(2.0)),start.y-r/2 + r/3 ,(float)(start.x+r/Math.sqrt(2)),start.y-r/2 + r/3};
        Path path1 = new Path();

        path1.moveTo(points[0] , points[1]);
        path1.lineTo(points[2],points[3]);
        path1.lineTo(points[4],points[5]);
        path1.lineTo(points[4],points[5]);
        path1.close();
        canvas.drawPath(path1,paint);
        path1.moveTo(points1[0] , points1[1]);
        path1.lineTo(points1[2],points1[3]);
        path1.lineTo(points1[4],points1[5]);
        path1.lineTo(points1[4],points1[5]);
        path1.close();
        canvas.drawPath(path1,paint);
    }
}
