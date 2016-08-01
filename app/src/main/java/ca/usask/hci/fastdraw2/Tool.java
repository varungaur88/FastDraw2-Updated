package ca.usask.hci.fastdraw2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.SparseArray;

public abstract class Tool {

    protected DrawingLayer drawingLayer;
    protected boolean changesCanvas;
    protected SparseArray<PointF> startPointsById;
    protected SparseArray<PointF> curPointsById;
    protected SparseArray<Path> pathsById;
    protected String name;

    public Tool(DrawingLayer drawingLayer) {
        this.drawingLayer = drawingLayer;
        this.changesCanvas = true;
        startPointsById = new SparseArray<PointF>();
        curPointsById = new SparseArray<PointF>();
        pathsById = new SparseArray<Path>();
    }

    public String getName() {
        return name;
    }

    public boolean getChangesCanvas() {
        return changesCanvas;
    }

    public void touchStart(int id, float x, float y) {
        getReady();
        startPointsById.put(id, new PointF(x, y));
        curPointsById.put(id, new PointF(x, y));
        Path p = pathsById.get(id, new Path());
        p.reset();
        p.moveTo(x, y);
        pathsById.put(id, p);
    }

    public void touchMove(int id, float x, float y) {
        pathsById.get(id).lineTo(x, y);
        curPointsById.put(id, new PointF(x, y));
    }

    public void touchStop(int id, float x, float y, Bitmap bitmap) {
        touchMove(id, x, y);
    }

    public void commitToBitmap(int id, Bitmap bitmap) {
        Canvas c = new Canvas(bitmap);
        drawPointer(id, startPointsById.get(id), curPointsById.get(id), pathsById.get(id), c);
        cancelPointer(id);
    }

    public void cancelPointer(int id) {
        startPointsById.remove(id);
        curPointsById.remove(id);
        pathsById.remove(id);
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < startPointsById.size(); i++) {
            int id = startPointsById.keyAt(i);
            drawPointer(id, canvas);
        }
    }

    public void drawPointer(int id, Canvas canvas) {
        drawPointer(id, startPointsById.get(id), curPointsById.get(id), pathsById.get(id), canvas);
    }

    protected abstract void getReady();
    protected abstract void drawPointer(int id, PointF start, PointF end, Path path, Canvas canvas);
}
