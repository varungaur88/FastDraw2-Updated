package ca.usask.hci.fastdraw2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class DrawingLayer implements OnLayoutChangeListener, TouchEventHandler, Observer {

    private static final String TAG = DrawingLayer.class.getSimpleName();

    private DrawingView drawingView;
    private Bitmap canvasBitmap;
    private Paint canvasPaint;
    private UndoCache undoStack;
    private DrawingToolFastTapMenu fastTapMenu;
    /*Start-Added by Varun*/
    private DrawingToolFastTapMenu1 fastTapMenu1;
    private DrawingToolFastTapMenu2 fastTapMenu2;
    /*End*/

    protected HashMap<Integer, TouchEventHandler> pointerOwners; // so fast tap menu can grab pointer focus

    private HashMap<Integer, Long> toolDownTimestamps; // for keeping track of drawing time

    public DrawingLayer(DrawingView drawingView) {
        this.drawingView = drawingView;
        this.drawingView.addOnLayoutChangeListener(this);
        this.canvasPaint = new Paint(Paint.DITHER_FLAG);
        this.undoStack = new UndoCache(drawingView.getContext(), "drawingName"); // TODO: Fix this to use the actual drawing name
        this.fastTapMenu = new DrawingToolFastTapMenu(drawingView.getContext(), drawingView, this);
        this.fastTapMenu.addObserver(this);

        /*Start-Added by Varun*/
        this.fastTapMenu1 = new DrawingToolFastTapMenu1(drawingView.getContext(), drawingView, this);
        this.fastTapMenu1.addObserver(this);
        this.fastTapMenu2 = new DrawingToolFastTapMenu2(drawingView.getContext(), drawingView, this);
        this.fastTapMenu2.addObserver(this);
        this.fastTapMenu.resetSelections();
        this.fastTapMenu1.resetSelections();
        this.fastTapMenu2.resetSelections();
        /*End*/
        this.pointerOwners = new HashMap<Integer, TouchEventHandler>();
        this.toolDownTimestamps = new HashMap<Integer, Long>();
    }

    public Tool getCurrentTool() {
        /*Modified by Varun*/
        return fastTapMenu1.getToolSelected();
    }

    public float getCurrentStrokeWidth() {
        /*Modified by Varun*/
        return fastTapMenu2.getStrokeSelected();
    }

    public int getCurrentColor() {
        return fastTapMenu.getColorSelected();
    }

    public void setCurrentColor(int color) {
        this.fastTapMenu.setColorSelected(color);
    }

    @Override
    public void update(Observable observable, Object data) {
        String id = (String)data;
        if (id == "Undo") {
            StudyLogger.getInstance().createLogEvent("event.command")
                    .attr("name", "Undo")
                    .commit();
            popUndo();
        } else { // All other selections change the menu state
            /*Start-Modified by Varun*/
            StudyLogger.getInstance().createLogEvent("event.menuupdate")
                    .attr("curtool", fastTapMenu1.getToolItemSelected().name)
                    .attr("curcolor", fastTapMenu.getColorItemSelected().name)
                    .attr("curstroke", fastTapMenu2.getStrokeItemSelected().name)
                    .commit();
            /*End*/
        }
    }

    public void pushUndo() {
        undoStack.push(canvasBitmap.copy(canvasBitmap.getConfig(), true));
    }

    public void popUndo() {
        if (!undoStack.isEmpty()) {
            canvasBitmap = undoStack.pop();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        getCurrentTool().draw(canvas);
        fastTapMenu.draw(canvas);
        /*Start-Added by Varun*/
        fastTapMenu1.draw(canvas);
        fastTapMenu2.draw(canvas);
        /*End*/
    }

    public void clearCanvas() {
        pushUndo();
        Canvas c = new Canvas(canvasBitmap);
        c.drawColor(Color.WHITE);
        StudyLogger.getInstance().createLogEvent("event.command")
                .attr("name", "Clear")
                .commit();
    }

    public Bitmap getDrawingBitmap() {
        return canvasBitmap.copy(canvasBitmap.getConfig(), false);
    }

    public void setDrawingBitmap(Bitmap bitmap) {
        this.canvasBitmap = bitmap;
    }

    public void update(long delta) {
        this.fastTapMenu.update(delta);
        /*Start-Added by Varun*/
        this.fastTapMenu1.update(delta);
        this.fastTapMenu2.update(delta);
        /*End*/
    }

    private void touchMoveUpdate(int pointerid, long timestamp, float x, float y) {
        if (!isValidPointer(pointerid)) {
            return;
        }
        getCurrentTool().touchMove(pointerid, x, y);
    }

    private boolean isValidPointer(int pointerid) {
        // if the pointer is owned by another object, the event should be ignored
        if (pointerOwners.containsKey(pointerid) && pointerOwners.get(pointerid) != this) {
            // clean up any state related to the id that we just found out we don't own
            getCurrentTool().cancelPointer(pointerid);
            if (toolDownTimestamps.containsKey(pointerid)) {
                /*
                StudyLogger.getInstance().createLogEvent("event.toolcanceled")
                        .attr("tool", getCurrentTool().getName())
                        .attr("pointerid", pointerid)
                        .commit();
                */
                toolDownTimestamps.remove(pointerid);
            }
            return false;
        }
        return true;
    }

    @Override
    public void handleTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        float x = event.getX(index);
        float y = event.getY(index);
        int id = event.getPointerId(index);
        long currentTime = event.getEventTime();
        Tool currentTool = getCurrentTool();

        if (!isValidPointer(id)) {
            this.fastTapMenu.handleTouchEvent(event);
            /*Start-Added by Varun*/
            this.fastTapMenu1.handleTouchEvent(event);
            this.fastTapMenu2.handleTouchEvent(event);
            /*End*/
            return;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                currentTool.touchStart(id, x, y);
                toolDownTimestamps.put(id, currentTime); // for measuring how long the pointer is down
                /*
                StudyLogger.getInstance().createLogEvent("event.toolstart")
                        .attr("tool", currentTool.getName())
                        .attr("pointerid", id)
                        .attr("x", x)
                        .attr("y", y)
                        .commit();
                */
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // If we own the pointer (that is, if no-one else owns it), register a swipe end
                if (pointerOwners.get(id) == null) {
                    currentTool.touchStop(id, x, y, canvasBitmap);
                    boolean changedCanvas = false;
                    if (currentTool.getChangesCanvas()) {
                        pushUndo();
                        currentTool.commitToBitmap(id, canvasBitmap);
                        changedCanvas = true;
                    }
                    long duration = currentTime - toolDownTimestamps.get(id);
                    toolDownTimestamps.remove(id);
                    StudyLogger.getInstance().createLogEvent("event.toolend")
                            .attr("name", currentTool.getName())
                            .attr("tool", currentTool.getName())
                            /*Modified by Varun*/
                            .attr("stroke", fastTapMenu2.getStrokeItemSelected().name)
                            .attr("color", fastTapMenu.getColorItemSelected().name)
                            .attr("pointerid", id)
                            .attr("x", x)
                            .attr("y", y)
                            .attr("changedcanvas", changedCanvas)
                            .attr("duration", duration)
                            .commit();
                } else {
                    currentTool.cancelPointer(id);
                    if (toolDownTimestamps.containsKey(id)) {
                        /*
                        StudyLogger.getInstance().createLogEvent("event.toolcanceled")
                                .attr("tool", getCurrentTool().getName())
                                .attr("pointerid", id)
                                .commit();
                        */
                        toolDownTimestamps.remove(id);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Move event can involve multiple pointer IDs
                final int historySize = event.getHistorySize();
                final int pointerCount = event.getPointerCount();
                long historicTime;
                // Process batched motion events
                for (int h = 0; h < historySize; h++) {
                    historicTime = event.getHistoricalEventTime(h);
                    for (int p = 0; p < pointerCount; p++) {
                        id = event.getPointerId(p);
                        x = event.getHistoricalX(p,h);
                        y = event.getHistoricalY(p,h);
                        touchMoveUpdate(id, historicTime, x, y);
                    }
                }
                // Process the latest motion event
                for (int p = 0; p < pointerCount; p++) {
                    id = event.getPointerId(p);
                    x = event.getX(p);
                    y = event.getY(p);
                    touchMoveUpdate(id, currentTime, x, y);
                }
                break;
        }

        // Pass the event on the fast tap menu
        this.fastTapMenu.handleTouchEvent(event);
        /*Start-Added by Varun*/
        this.fastTapMenu1.handleTouchEvent(event);
        this.fastTapMenu2.handleTouchEvent(event);
        /*End*/
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        // its possible that the layout is not complete in which case
        // we will get all zero values for the positions, so ignore the event
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return;
        }
        if (this.canvasBitmap == null) {
            this.canvasBitmap = Bitmap.createBitmap(this.drawingView.getW(), this.drawingView.getH(),
                    Bitmap.Config.RGB_565);
            Canvas c = new Canvas(this.canvasBitmap);
            c.drawColor(Color.WHITE);
        }
    }
}
