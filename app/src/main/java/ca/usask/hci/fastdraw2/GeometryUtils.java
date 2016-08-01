package ca.usask.hci.fastdraw2;

public class GeometryUtils {

	public static float dist(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
}
