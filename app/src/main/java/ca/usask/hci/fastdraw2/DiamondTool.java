package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class DiamondTool extends Tool {

    Paint paint;

    public DiamondTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Diamond";
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
        int numberOfSides = 4;
        float [] points2 = new float[2*numberOfSides];
        for(int i = 0;i<2*numberOfSides;i+=2)
        {
            points2[i] = start.x + r * (float)Math.sin((2* Math.PI * i/2)/numberOfSides) ;
            points2[i+1] = start.y - r * (float)Math.cos((2* Math.PI * (i/2))/numberOfSides) ;
        }
        Path path1 = new Path();
        path1.moveTo(points2[0],points2[1]);
        for(int i=2;i<numberOfSides*2;i+=2)
        {
            path1.lineTo(points2[i],points2[i+1]);
        }
        path1.close();
        canvas.drawPath(path1,paint);
    }
}
