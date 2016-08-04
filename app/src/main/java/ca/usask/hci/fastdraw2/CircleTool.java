package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
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
        float [] points1 = {start.x,start.y-r/2,(float)(start.x-r/Math.sqrt(2.0)),start.y+r/2,(float)(start.x+r/Math.sqrt(2)),start.y+r/2};
        float [] points = {start.x,start.y+r/2 + r/3,(float)(start.x-r/Math.sqrt(2.0)),start.y-r/2 + r/3 ,(float)(start.x+r/Math.sqrt(2)),start.y-r/2 + r/3};
        int numberOfSides = 5;
        float [] points2 = new float[2*numberOfSides];
        for(int i = 0;i<2*numberOfSides;i+=2)
        {



            points2[i] = start.x + r * (float)Math.sin((2* Math.PI * i/2)/numberOfSides) ;
            points2[i+1] = start.y - r * (float)Math.cos((2* Math.PI * (i/2))/numberOfSides) ;

            Log.d("Yo",points2[i] + " " + points2[i+ 1]   );

        }
        Log.d("Yo", Arrays.toString(points2));

//        points2[0] = start.x;
//        points2[1] = start.y;
                Path path1 = new Path();


        path1.moveTo(points2[0],points2[1]);
        for(int i=2;i<numberOfSides*2;i+=2)
        {
            path1.lineTo(points2[i],points2[i+1]);

        }
        path1.close();
        canvas.drawPath(path1,paint);




//
//        Path path1 = new Path();
//        path1.moveTo(points[0] , points[1]);
//        path1.lineTo(points[2],points[3]);
//        path1.lineTo(points[4],points[5]);
//        path1.lineTo(points[4],points[5]);
//        path1.close();
//
//        path1.moveTo(points1[0] , points1[1]);
//        path1.lineTo(points1[2],points1[3]);
//        path1.lineTo(points1[4],points1[5]);
//        path1.lineTo(points1[4],points1[5]);
//        path1.close();
//
//
//
//
//
//
//
//        canvas.drawPath(path1,paint);
//
//
//        canvas.drawCircle(cx, cy, r, paint);


    }
}
