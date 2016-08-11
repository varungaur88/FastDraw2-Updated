package ca.usask.hci.fastdraw2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class SadSmileyTool extends Tool {

    Paint paint;

    public SadSmileyTool(DrawingLayer drawingLayer) {
        super(drawingLayer);
        this.name = "Sad Smiley";
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




        //canvas.drawCircle(cx, cy, r, paint);
        // happy
//        canvas.drawCircle(cx-r/3,cy - r/3,r/20,paint);
//        canvas.drawCircle(cx+r/3,cy - r/3,r/20,paint);
//        float xPosition=cx;
//        float yPosition=cy;
//        float size = r;
//        canvas.drawCircle(xPosition, yPosition, size, paint);
//        size = size * 8 / 10;
//        xPosition=xPosition-size/2;
//        yPosition=yPosition-size/2;
//        RectF rectf = new RectF (xPosition, yPosition, xPosition+size, yPosition+size);
//        canvas.drawArc(rectf, 0+0, 180, true, paint);


        canvas.drawCircle(cx-r/3,cy - r/3,r/20,paint);
        canvas.drawCircle(cx+r/3,cy - r/3,r/20,paint);
        float xPosition=cx;
        float yPosition=cy;
        float size = r;
        canvas.drawCircle(xPosition, yPosition, size, paint);
        size = size * 8 / 10;
        xPosition=xPosition-size/2;
        yPosition=yPosition-size/2;
        RectF rectf = new RectF (xPosition, yPosition+size*1/2 , xPosition+size, yPosition+3*size/2);
        canvas.drawArc(rectf, 180, 180, true, paint);
    }
}
