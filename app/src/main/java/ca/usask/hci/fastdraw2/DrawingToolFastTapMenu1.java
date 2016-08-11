package ca.usask.hci.fastdraw2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Arrays;
/*Class reserved for Tab1-Tools*/
public class DrawingToolFastTapMenu1 extends FastTapMenu {

    private ToolItem toolItemSelected;
    /*Commented by Varun
    private ToolItem colorItemSelected;
    private ToolItem strokeItemSelected;*/

    private Tool toolSelected;
    /*Commented by Varun
    private int colorSelected;
    private float strokeSelected;*/

	public DrawingToolFastTapMenu1(Context context, DrawingView drawingView, DrawingLayer drawingLayer) {
		super(context, drawingView, drawingLayer);
		this.cols = 4;
		this.rows = 4;
		this.menuButton = new MenuButton(this);
		this.menuItems = new MenuItem[ToolItem.all1.length+2];
		int i = 0;
		for (ToolItem ti : ToolItem.all1) {
			menuItems[i] = new SimpleMenuItem(ti.name, ti.name, ti.icon); // use name as ID
			i += 1;
		}
        menuItems[12] = null;
        menuItems[13] = this.menuButton;
        Log.d("Tools_","1");
        resetSelections();
	}

    public ToolItem getToolItemSelected() {
        return toolItemSelected;
    }
    /*Commented by Varun
    public ToolItem getColorItemSelected() {
        return colorItemSelected;
    }*/
    /*Commented by Varun
    public ToolItem getStrokeItemSelected() {
        return strokeItemSelected;
    }*/

    public Tool getToolSelected() {
        //Log.d("Tool Selected_3",toolSelected.getName());
        return toolSelected;
    }
    /*Commented by Varun
    public int getColorSelected() {
        return colorSelected;
    }*/
    /*Commented by Varun
    public float getStrokeSelected() {
        return strokeSelected;
    }*/
    /*Commented by Varun
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
        } else {
            this.colorItemSelected = ToolItem.CustomColor;
        }
    }*/

    protected void selectItem(MenuItem mi) {
        Log.d("Tool Selected_4_blah",mi.toString());

        ToolItem ti = ToolItem.getByName(mi.id,1);
        Log.d("Tools_","5");
        this.selectByToolItem(ti);
        // Call super method after b/c it sends out a notification
        super.selectItem(mi);
        //Log.d("Tool Selected_5",toolSelected.getName());
    }

    protected void selectByToolItem(ToolItem ti) {
        Log.d("Tools_","3");
//        Log.d("Tools_",ti.toString());
        DrawingToolFastTapMenu2.flag=false;
        if (Arrays.asList(ToolItem.toolTypes).contains(ti) ) {
            this.toolItemSelected = ti;
//            Log.d("Tool Selected_1",this.toolItemSelected.name);
            if (ti == ToolItem.Paintbrush) {
                this.toolSelected = new PaintbrushTool(this.drawingLayer);
            } else if (ti == ToolItem.Line) {
                this.toolSelected = new LineTool(this.drawingLayer);
            } else if (ti == ToolItem.Rectangle) {
                this.toolSelected = new RectangleTool(this.drawingLayer);
            } else if (ti == ToolItem.Circle) {
                this.toolSelected = new CircleTool(this.drawingLayer);
            } else if (ti == ToolItem.ColorPicker) {
                this.toolSelected = new ColorPickerTool(this.drawingLayer);
            } else if (ti == ToolItem.CustomColor) {
                this.toolSelected = new CustomColorTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Triangle) {
                this.toolSelected = new TriangleTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Pentagon) {
                this.toolSelected = new PentagonTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Star) {
                this.toolSelected = new StarTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Diamond) {
                this.toolSelected = new DiamondTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Hexagon) {
                this.toolSelected = new HexagonTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Oval) {
                this.toolSelected = new OvalTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Heart) {
                this.toolSelected = new HeartTool(this.drawingLayer);
            }
            else if (ti == ToolItem.Square) {
                this.toolSelected = new SquareTool(this.drawingLayer);
            }
            //Log.d("Tool Selected_2",this.toolItemSelected.name);
            //Log.d("Tools_","4");
            //Log.d("Tools_",this.toolSelected.getName());
        }
        /*Commented by Varun
        else if (Arrays.asList(ToolItem.colorTypes).contains(ti) ) {
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
            } else if (ti == ToolItem.CustomColor) {
                this.colorSelected = 0xff000000;
            } else {
                this.colorSelected = 0xff000000;
            }

        }
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
        //Log.d("Tool Selected_6","hi");
        Log.d("Tools_","2");
        this.selectByToolItem(ToolItem.Paintbrush);
        /*Commented by Varun
        this.selectByToolItem(ToolItem.Black);
        this.selectByToolItem(ToolItem.Thin);*/
        this.setChanged();
        this.notifyObservers();
    }

    private class MenuButton extends MenuItem {

        private DrawingToolFastTapMenu1 menu;

        private Paint mLabelPaint;

        public MenuButton(DrawingToolFastTapMenu1 menu) {
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
            if(DrawingToolFastTapMenu2.flag==false) {
                canvas.drawText(menu.getToolItemSelected().name, x + width / 2f, y + 2 * height / 4f, mLabelPaint);
            }else if (DrawingToolFastTapMenu2.flag==true) {
                canvas.drawText("None", x + width / 2f, y + 2 * height / 4f, mLabelPaint);
            }
            /*Commented by Varun
            canvas.drawText(menu.getColorItemSelected().name, x + width/2f, y + 2*height/4f, mLabelPaint);
            canvas.drawText(menu.getStrokeItemSelected().name, x + width/2f, y + 3*height/4f, mLabelPaint);*/
        }

    }
	
}
