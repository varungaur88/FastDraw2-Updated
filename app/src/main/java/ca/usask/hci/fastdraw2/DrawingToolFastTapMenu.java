package ca.usask.hci.fastdraw2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Arrays;
/*Class reserved for Tab1-Colors*/
public class DrawingToolFastTapMenu extends FastTapMenu {
    /*Commented by Varun
    private ToolItem toolItemSelected;
    */
    private ToolItem colorItemSelected;
    /*Commented by Varun
    private ToolItem strokeItemSelected;
    private Tool toolSelected;
    */
    private int colorSelected;
    /*Commented by Varun
    private float strokeSelected;
    */

	public DrawingToolFastTapMenu(Context context, DrawingView drawingView, DrawingLayer drawingLayer) {
		super(context, drawingView, drawingLayer);
		this.cols = 4;
		this.rows = 4;
		this.menuButton = new MenuButton(this);
		this.menuItems = new MenuItem[ToolItem.all.length+1];
		int i = 0;
		for (ToolItem ti : ToolItem.all) {
			menuItems[i] = new SimpleMenuItem(ti.name, ti.name, ti.icon); // use name as ID
			i += 1;
		}
        menuItems[12] = this.menuButton;

        resetSelections();
	}
    /*Commented by Varun
    public ToolItem getToolItemSelected() {
        return toolItemSelected;
    }
    */
    public ToolItem getColorItemSelected() {
        return colorItemSelected;
    }
    /*Commented by Varun
    public ToolItem getStrokeItemSelected() {
        return strokeItemSelected;
    }
    */
    /*Commented by Varun
    public Tool getToolSelected() {
        return toolSelected;
    }*/

    public int getColorSelected() {
        return colorSelected;
    }
    /*Commented by Varun
    public float getStrokeSelected() {
        return strokeSelected;
    }
    */
    public void setColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
        if (this.colorSelected == 0xff000000) {
            this.colorItemSelected = ToolItem.Black;
        } else if (this.colorSelected == 0xffff0000) {
            this.colorItemSelected = ToolItem.Red;
        } else if (this.colorSelected == 0xff00ff00) {
            this.colorItemSelected = ToolItem.Green;
        } else if (this.colorSelected == 0xff0000ff) {
            this.colorItemSelected = ToolItem.Blue;
        } else if (this.colorSelected == 0xffffffff) {
            this.colorItemSelected = ToolItem.White;
        } else if (this.colorSelected == 0xffffff00) {
            this.colorItemSelected = ToolItem.Yellow;
        } else if (this.colorSelected == 0xff00ffff) {
            this.colorItemSelected = ToolItem.Cyan;
        } else if (this.colorSelected == 0xffff00ff) {
            this.colorItemSelected = ToolItem.Magenta;
        } else if (this.colorSelected == 0xffff4f00) {
            this.colorItemSelected = ToolItem.Orange;
        } else if (this.colorSelected == 0xff964b00) {
            this.colorItemSelected = ToolItem.Brown;
        } else if (this.colorSelected == 0xff563c5c) {
            this.colorItemSelected = ToolItem.Voilet;
        } else if (this.colorSelected == 0xff808080) {
            this.colorItemSelected = ToolItem.Gray;
        }
        else {
            this.colorItemSelected = ToolItem.CustomColor;
        }
    }

    protected void selectItem(MenuItem mi) {
        ToolItem ti = ToolItem.getByName(mi.id,0);
        selectByToolItem(ti);
        // Call super method after b/c it sends out a notification
        super.selectItem(mi);
    }

    protected void selectByToolItem(ToolItem ti) {
        /*Commented by Varun
        if (Arrays.asList(ToolItem.toolTypes).contains(ti) ) {
            this.toolItemSelected = ti;

            if (ti == ToolItem.Paintbrush) {
                this.toolSelected = new PaintbrushTool(this.drawingLayer);
            } else if (ti == ToolItem.Line) {
                this.toolSelected = new LineTool(this.drawingLayer);
            } else if (ti == ToolItem.Rectangle) {
                this.toolSelected = new RectangleTool(this.drawingLayer);
            } else if (ti == ToolItem.Circle) {
                this.toolSelected = new CircleTool(this.drawingLayer);
            } else if (ti == ToolItem.Fill) {
                this.toolSelected = new BucketTool(this.drawingLayer);
            } else if (ti == ToolItem.ColorPicker) {
                this.toolSelected = new ColorPickerTool(this.drawingLayer);
            } else if (ti == ToolItem.CustomColor) {
                this.toolSelected = new CustomColorTool(this.drawingLayer);
            }

        } else*/
        if (Arrays.asList(ToolItem.colorTypes).contains(ti) ) {
            this.colorItemSelected = ti;

            if (ti == ToolItem.Black) {
                this.colorSelected = 0xff000000;
            } else if (ti == ToolItem.Red) {
                this.colorSelected = 0xffff0000;
            } else if (ti == ToolItem.Green) {
                this.colorSelected = 0xff00ff00;
            } else if (ti == ToolItem.Blue) {
                this.colorSelected = 0xff0000ff;
            } else if (ti == ToolItem.White) {
                this.colorSelected = 0xffffffff;
            } else if (ti == ToolItem.Yellow) {
                this.colorSelected = 0xffffff00;
            } else if (ti == ToolItem.Cyan) {
                this.colorSelected = 0xff00ffff;
            } else if (ti == ToolItem.Magenta) {
                this.colorSelected = 0xffff00ff;
            }
            else if (ti == ToolItem.Orange) {
                this.colorSelected = 0xffff4f00;
            }
            else if (ti == ToolItem.Brown) {
                this.colorSelected = 0xff964b00;
            }
            else if (ti == ToolItem.Voilet) {
                this.colorSelected = 0xff563c5c;
            }
            else if (ti == ToolItem.Gray) {
                this.colorSelected = 0xff808080;
            }
            else if (ti == ToolItem.CustomColor) {
                this.colorSelected = 0xff000000;
            } else {
                this.colorSelected = 0xff000000;
            }

        }
        /*Commented by Varun
        else if (Arrays.asList(ToolItem.strokeTypes).contains(ti) ) {
            this.strokeItemSelected = ti;

            if (ti == ToolItem.Fine) {
                this.strokeSelected = 1f;
            } else if (ti == ToolItem.Thin) {
                this.strokeSelected = 6f;
            } else if (ti == ToolItem.Medium) {
                this.strokeSelected = 16f;
            } else if (ti == ToolItem.Wide) {
                this.strokeSelected = 50f;
            } else {
                this.strokeSelected = 6f;
            }
        }*/
    }

    public void resetSelections() {
        /*Commented by Varun
        this.selectByToolItem(ToolItem.Paintbrush);*/
        this.selectByToolItem(ToolItem.Black);
        /*Commented by Varun
        this.selectByToolItem(ToolItem.Thin);*/
        this.setChanged();
        this.notifyObservers();
    }

    private class MenuButton extends MenuItem {

        private DrawingToolFastTapMenu menu;

        private Paint mLabelPaint;

        public MenuButton(DrawingToolFastTapMenu menu) {
            super("Menu Button");
            this.menu = menu;
            mLabelPaint = new Paint();
            mLabelPaint.setTextAlign(Paint.Align.CENTER);
            mLabelPaint.setColor(0xff000000);
            mLabelPaint.setTextSize(22);
            mLabelPaint.setAntiAlias(true);
        }

        public void draw(Canvas canvas) {
            mPaint.setColor(0xff000000);
            /*Commented by Varun
            canvas.drawText(menu.getToolItemSelected().name, x + width/2f, y + height/4f, mLabelPaint);*/
            canvas.drawText(menu.getColorItemSelected().name, x + width/2f, y + 2*height/4f, mLabelPaint);
            /*Commented by Varun
            canvas.drawText(menu.getStrokeItemSelected().name, x + width/2f, y + 3*height/4f, mLabelPaint);*/
        }

    }
	
}
