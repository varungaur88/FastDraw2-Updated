package ca.usask.hci.fastdraw2;

public class ToolItem {

	public String name;
	public int icon;
	
	public ToolItem(String name, int icon) {
		this.name = name;
		this.icon = icon;
	}

	public static final ToolItem Paintbrush = new ToolItem("Paintbrush", R.drawable.paintbrush);
	public static final ToolItem Line = new ToolItem("Line", R.drawable.line);
	public static final ToolItem Circle = new ToolItem("Circle", R.drawable.circle);
	public static final ToolItem Rectangle = new ToolItem("Rectangle", R.drawable.rectangle);
	public static final ToolItem Fill = new ToolItem("Fill", R.drawable.fill);
	
	public static final ToolItem Black = new ToolItem("Black", R.drawable.black);
	public static final ToolItem Red = new ToolItem("Red", R.drawable.red);
	public static final ToolItem Green = new ToolItem("Green", R.drawable.green);
	public static final ToolItem Blue = new ToolItem("Blue", R.drawable.blue);
	public static final ToolItem ColorPicker = new ToolItem("Color Picker", R.drawable.colorpicker);

	public static final ToolItem White = new ToolItem("White", R.drawable.white);
	public static final ToolItem Yellow = new ToolItem("Yellow", R.drawable.yellow);
	public static final ToolItem Cyan = new ToolItem("Cyan", R.drawable.cyan);
	public static final ToolItem Magenta = new ToolItem("Magenta", R.drawable.magenta);
	public static final ToolItem CustomColor = new ToolItem("Custom Color", R.drawable.customcolor);
		
	public static final ToolItem Fine = new ToolItem("Fine", R.drawable.fine);
	public static final ToolItem Thin = new ToolItem("Thin", R.drawable.thin);
	public static final ToolItem Medium = new ToolItem("Medium", R.drawable.medium);
	public static final ToolItem Wide = new ToolItem("Wide", R.drawable.wide);
	public static final ToolItem Undo = new ToolItem("Undo", R.drawable.undo);

	public static final ToolItem Open = new ToolItem("Open", R.drawable.open);
	public static final ToolItem Save = new ToolItem("Save", R.drawable.save);
	public static final ToolItem Clear = new ToolItem("Clear", R.drawable.clear);
	public static final ToolItem Options = new ToolItem("Options", R.drawable.options);
	
	/*Commented by Varun public static ToolItem[] all = {Paintbrush,
									Line,
									Circle,
									Fill,
									Black,
                                    White,
									Red,
									Yellow,
									Fine,
									Thin,
									Medium,
									Undo};*/
	/*Start-Added by Varun*/
	public static ToolItem[] all = {Black,
			Red,
			Green,
			Blue,
			Black,
			White,
			Yellow,
			Cyan,
			Magenta,
			Black,
			Red,
			Green};
	public static ToolItem[] all1 = {Paintbrush,
			Line,
			Circle,
			Fill,
			Undo,
			Paintbrush,
			Line,
			Fill,
			Undo,
			Paintbrush,
			Line,
			Circle};
	public static ToolItem[] all2 = {Fine,
			Thin,
			Fine,
			Medium,
			Thin,
			Medium,
			Fine,
			Medium,
			Thin,
			Fine,
			Medium,
			Thin};
	/*End*/
	public static ToolItem numberToAll[][] = {all,all1,all2};

	public static ToolItem[] strokeTypes = {Fine,
            Thin,
            Medium,
            Wide};

    public static ToolItem[] toolTypes = {Paintbrush,
            Line,
            Circle,
            Rectangle,
            Fill,
            ColorPicker,
            CustomColor};

    public static ToolItem[] colorTypes = {Black,
            Red,
            Green,
            Blue,
            White,
            Yellow,
            Cyan,
            Magenta};

    public static ToolItem[] actionTypes = {Undo};

    public static ToolItem getByName(String name,int numberOfMenu) {
        for (ToolItem ti : numberToAll[numberOfMenu]) {
            if (ti.name == name) {
                return ti;
            }
        }
        return null;
    };

}
