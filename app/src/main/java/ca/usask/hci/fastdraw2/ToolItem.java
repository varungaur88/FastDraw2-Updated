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
	public static final ToolItem Triangle = new ToolItem("Triangle", R.drawable.triangle);
	public static final ToolItem Pentagon = new ToolItem("Pentagon", R.drawable.pentagon);
	public static final ToolItem Star = new ToolItem("Star", R.drawable.star);
	public static final ToolItem Diamond = new ToolItem("Diamond", R.drawable.diamond);
	public static final ToolItem Hexagon = new ToolItem("Hexagon", R.drawable.hexagon);
	public static final ToolItem Oval = new ToolItem("Oval", R.drawable.oval);
	public static final ToolItem Heart = new ToolItem("Heart", R.drawable.heart);
	public static final ToolItem Eraser = new ToolItem("Eraser", R.drawable.eraser);
	
	public static final ToolItem Black = new ToolItem("Black", R.drawable.black);
	public static final ToolItem Red = new ToolItem("Red", R.drawable.red);
	public static final ToolItem Green = new ToolItem("Green", R.drawable.green);
	public static final ToolItem Blue = new ToolItem("Blue", R.drawable.blue);
	public static final ToolItem ColorPicker = new ToolItem("Color Picker", R.drawable.colorpicker);

	public static final ToolItem White = new ToolItem("White", R.drawable.white);
	public static final ToolItem Yellow = new ToolItem("Yellow", R.drawable.yellow);
	public static final ToolItem Cyan = new ToolItem("Cyan", R.drawable.cyan);
	public static final ToolItem Magenta = new ToolItem("Magenta", R.drawable.magenta);
	public static final ToolItem Orange = new ToolItem("Orange", R.drawable.orange);
	public static final ToolItem Brown = new ToolItem("Brown", R.drawable.brown);
	public static final ToolItem Voilet = new ToolItem("Voilet", R.drawable.voilet);
	public static final ToolItem Gray = new ToolItem("Gray", R.drawable.gray);
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
	public static final ToolItem Image1 = new ToolItem("Image1", R.drawable.triangle);
	
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
			White,
			Yellow,
			Cyan,
			Magenta,
			Orange,
			Brown,
			Voilet,
			Gray};
	public static ToolItem[] all1 = {Paintbrush,
			Line,
			Circle,
			Rectangle,
			Fill,
			Pentagon,
			Triangle,
			Star,
			Heart,
			Diamond,
			Hexagon,
			Oval};
	public static ToolItem[] all2 = {Fine,
			Thin,
			Medium,
			Wide,
			Medium,
			Fine,
			Medium,
			Thin,
			Fine,
			Medium,
			Eraser,
			Undo};
	/*End*/
	public static ToolItem numberToAll[][] = {all,all1,all2};

	public static ToolItem[] strokeTypes = {Fine,
            Thin,
            Medium,
            Wide,
			Eraser};

    public static ToolItem[] toolTypes = {Paintbrush,
            Line,
            Circle,
            Rectangle,
            Fill,
            ColorPicker,
            CustomColor,
			Triangle,
			Pentagon,
			Star,
			Heart,
			Diamond,
			Hexagon,
			Oval};

    public static ToolItem[] colorTypes = {Black,
            Red,
            Green,
            Blue,
            White,
            Yellow,
            Cyan,
            Magenta,
			Orange,
			Brown,
			Voilet,
			Gray};

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
