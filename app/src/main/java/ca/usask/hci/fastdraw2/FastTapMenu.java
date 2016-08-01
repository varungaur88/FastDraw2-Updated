package ca.usask.hci.fastdraw2;

import java.util.Observable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;

public class FastTapMenu extends Observable implements OnLayoutChangeListener, TouchEventHandler {

	private static final String TAG = FastTapMenu.class.getSimpleName();

	public static final int BG_COLOR = 0xaaffffff;
	public static final int SHOW_MENU_DELAY = 200; // ms

	private Context context;
	private DrawingView drawingView;
    protected DrawingLayer drawingLayer;
	private Paint mPaint;
	
	protected boolean menuOpen;
	protected int rows;
	protected int cols;
	protected MenuItem[] menuItems;
	protected MenuItem menuButton;
	protected MenuItem selectedItem;
	private SparseArray<Long> menuButtonPointers; // pointers captured by the menu button
	private SparseArray<Touch> recentTouches; // touches not on the menu button, for identifying expert selections where the menu is hit second
	private long overlayShownTimestamp;
	
	public FastTapMenu(Context context, DrawingView drawingView, DrawingLayer drawingLayer) {
		this.context = context;
        this.drawingView = drawingView;
        this.drawingView.addOnLayoutChangeListener(this);
        this.drawingLayer = drawingLayer;

		this.menuOpen = false;
		this.menuButtonPointers = new SparseArray<Long>();
		this.recentTouches = new SparseArray<Touch>();
		this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
	}

	public void update(long delta) {
		final long currentTime = SystemClock.uptimeMillis();
		
		if (menuButtonPointers.size() > 0) {
			menuButton.active = true;

			long max = 0;
			synchronized (menuButtonPointers) {
				for(int i = 0; i < menuButtonPointers.size(); i++) {
				   long ts = menuButtonPointers.get( menuButtonPointers.keyAt(i) );
				   if (currentTime - ts > max) {
					   max = currentTime - ts;
				   }
				}
			}
			// if a menu button pointer has been in the menu long enough, it should open
			if (max > SHOW_MENU_DELAY && !menuOpen) {
				menuOpen = true;
				StudyLogger.getInstance().createLogEvent("event.overlayshown").commit();
				overlayShownTimestamp = currentTime; // record so we can log time since overlay is displayed
			}
		} else {
			menuButton.active = false;
			if (menuOpen) {
				menuOpen = false;
				StudyLogger.getInstance().createLogEvent("event.overlayhidden").commit();
			}
		}
		for (MenuItem mi : menuItems) {
			/*Start-Not null condition added by Varun*/
			if(mi!=null) {
				mi.update(delta);
			}
		}
	}
	
	public void draw(Canvas canvas) {
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();		
		int rowHeight = canvasHeight / rows;
		int colWidth = canvasWidth / cols;

		if (this.menuOpen) {
			// draw the background
			mPaint.setColor(BG_COLOR);
			canvas.drawRect(0, 0, canvasWidth, canvasHeight, mPaint);
		}
		
		// draw the menu items
		for (MenuItem mi : menuItems) {
			/*Start-Not null condition added by Varun*/
			if(mi!=null) {
				if (this.menuOpen || mi.flashTimeRemaining > 0 || mi == menuButton) {
					mi.draw(canvas);
				}
			}
		}
		
		// draw the grid lines
		mPaint.setColor(0x44666666);
		for (int r = 0; r < this.rows; r++) {
			canvas.drawLine(0, r*rowHeight, canvasWidth, r*rowHeight, mPaint);
		}
		for (int c = 0; c < this.cols; c++) {
			canvas.drawLine(c*colWidth, 0, c*colWidth, canvasHeight, mPaint);
		}
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        // its possible that the layout is not complete in which case
        // we will get all zero values for the positions, so ignore the event
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return;
        }
        updateLayout();
	}
	
	private void updateLayout() {
		// Do what you need to do with the height/width since they are now set
		float colWidth = drawingView.getW() / this.cols;
		float rowHeight = drawingView.getH() / this.rows;

		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				int i = r*cols + c;
				if (i < menuItems.length) {
					/*Start-Not null condition added by Varun*/
					if(menuItems[i]!=null) {
						menuItems[i].x = c * colWidth;
						menuItems[i].y = r * rowHeight;
						menuItems[i].width = colWidth;
						menuItems[i].height = rowHeight;
					}
				}
			}
		}		
	}
	
	@Override
	public void handleTouchEvent(MotionEvent event) {
		int index = event.getActionIndex();
		float x = event.getX(index);
		float y = event.getY(index);
		int id = event.getPointerId(index);
		long currentTime = event.getEventTime();
		
		// if the pointer is owned by another object, the event should be ignored
		if (drawingLayer.pointerOwners.containsKey(id) && drawingLayer.pointerOwners.get(id) != this) {
			// clean up any state related to the id that we just found out we don't own
			menuButtonPointers.remove(id);
			recentTouches.remove(id);
			return;
		}
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			if (menuButton.containsPoint(x, y)) {
				menuButtonPointers.put(id, currentTime);
				drawingLayer.pointerOwners.put(id, this); // claim the pointer id (so a spurious swipe isn't registered)
			} else {
				recentTouches.put(id, new Touch(new PointF(x,y), currentTime));
			}
			// Check if a menu selection should be registered
			if (menuButtonPointers.size() > 0 && recentTouches.size() > 0) { // If menu is active (even if not open yet), and there are recent touches
				for (int i = 0; i < recentTouches.size(); i++) {
					int recentTouchId = recentTouches.keyAt(i);
					Touch t = recentTouches.get( recentTouchId );
					// Check the most recent touch, and touches back (SHOW_MENU_DELAY) time in the past (to catch item-touch-first cases)
					if (Math.abs(currentTime - t.timestamp) < SHOW_MENU_DELAY) {
						for (MenuItem mi : menuItems) {
							/*Start-Not null condition added by Varun*/
							if(mi!=null) {
								if (mi != menuButton && mi.containsPoint(t.point.x, t.point.y)) {
									// Set the selected item
									Log.d("Mi_",mi.id);
									this.selectItem(mi);
									drawingLayer.pointerOwners.put(recentTouchId, this); // claim the pointer id (so a spurious swipe isn't registered)
									// For logging selection time, calculate the oldest touch involved in this selection
									// (which could be the touch on the item, or one of the touches on the menu square)
									long oldest = t.timestamp;
									synchronized (menuButtonPointers) {
										for (int pi = 0; pi < menuButtonPointers.size(); pi++) {
											long ts = menuButtonPointers.get(menuButtonPointers.keyAt(pi));
											if (ts < oldest) {
												oldest = ts;
											}
										}
									}
									if (menuOpen) {
										StudyLogger.getInstance().createLogEvent("event.noviceselection")
												.attr("x", t.point.x)
												.attr("y", t.point.y)
												.attr("selection", mi.id)
												.attr("timesinceoverlay", currentTime - overlayShownTimestamp)
												.attr("timesincemenupress", currentTime - oldest)
												.commit();
									} else {
										StudyLogger.getInstance().createLogEvent("event.expertselection")
												.attr("x", t.point.x)
												.attr("y", t.point.y)
												.attr("selection", mi.id)
												.attr("timesincemenupress", currentTime - oldest)
												.commit();
									}
								}
							}
						}
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			menuButtonPointers.remove(id);
			recentTouches.remove(id);
			drawingLayer.pointerOwners.remove(id);
			break;
		case MotionEvent.ACTION_CANCEL:
			menuButtonPointers.remove(id);
			recentTouches.remove(id);
			drawingLayer.pointerOwners.remove(id);
			break;
		case MotionEvent.ACTION_MOVE:
	        break;
		}
	}

	protected void selectItem(MenuItem mi) {
        // flash both the menu item and the selected item
		Log.d("Bye",mi.id);
		this.selectedItem = mi;
		this.selectedItem.flash(500);
		this.menuButton.flash(500);
		
		this.setChanged();
		this.notifyObservers(mi.id);
	}

    protected abstract class MenuItem {

        public static final int FLASH_COLOR = 0xCCE5E5E5;

        public String id;
        public float x; // top left corner
        public float y; // top left corner
        public float width;
        public float height;
        public int flashTimeRemaining;
        public boolean active;

        protected Paint mPaint;

        public MenuItem(String id) {
            this.id = id;
            this.active = false;
            this.mPaint = new Paint();
        }

        public boolean containsPoint(float px, float py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }

        public void flash(int duration) {
            this.flashTimeRemaining = duration;
        }

        public void update(long delta) {
            if (this.flashTimeRemaining > 0) {
                this.flashTimeRemaining -= delta;
            }
        }

        public void draw(Canvas canvas) {
            if (this.flashTimeRemaining > 0 || this.active) {
                mPaint.setColor(FLASH_COLOR);
                canvas.drawRect(x, y, x+width, y+height, mPaint);
            }
        };

    }

	protected class SimpleMenuItem extends MenuItem {
		
		public String label;
		public int iconResource;
		public Bitmap icon;

        private Paint mLabelPaint;

		public SimpleMenuItem(String id, String name, int iconResource) {
            super(id);

			this.label = name;
			this.iconResource = iconResource;
			this.icon = BitmapFactory.decodeResource(context.getResources(), this.iconResource);

			mLabelPaint = new Paint();
			mLabelPaint.setTextAlign(Align.CENTER);
			mLabelPaint.setColor(0xff000000);
			mLabelPaint.setTextSize(22);
			mLabelPaint.setAntiAlias(true);
            mLabelPaint.setDither(true);
		}

		public void draw(Canvas canvas) {
            super.draw(canvas);
            mPaint.setColor(0xff000000);
			// TODO: Fix this up, it's hacky
			float iconScaledWidth = icon.getWidth();
			float iconScaledHeight = icon.getHeight();
			if (iconScaledWidth > width*(3f/4f)) {
				iconScaledWidth = width*(3f/4f); // take up 3/4 of the menu item width
				iconScaledHeight = (icon.getHeight()/(float)icon.getWidth())*iconScaledWidth; // scale height to match
			}
			//canvas.drawBitmap(icon, x + (width/2f) - icon.getWidth()/2f, y + (height/2f) - icon.getHeight()*(3f/4f), mPaint);
			canvas.drawBitmap(icon, new Rect(0, 0, icon.getWidth(), icon.getHeight()),
									new RectF(x + (width/2f) - iconScaledWidth/2f, y + (height/3f) - iconScaledHeight/2f, x + (width/2f) + iconScaledWidth/2f, y + (height/3f) + iconScaledHeight/2f), mPaint);
			canvas.drawText(label, x + (width/2f), y + 3f*(height/4f), mLabelPaint);
		}
	}

}
