package ly.qr.kiarelemb.data;

import method.qr.kiarelemb.utils.QRArrayUtils;

import java.awt.*;
import java.util.LinkedList;

public record ColorData(Color one, Color two, Color three, Color all) {

	public static String getColor(Color c) {
		return c.getRed() + "," + c.getGreen() + "," + c.getBlue();
	}


	public static Color parseColor(String rgb, char separator) {
		int[] values = QRArrayUtils.splitToInt(rgb, separator);
		return new Color(values[0], values[1], values[2]);
	}

	/**
	 * 将一个16进制的rgb值转成Color类
	 */
	public static Color parseColor(String hexLen) {
		final LinkedList<String> split = QRArrayUtils.splitWithLength(hexLen, 2);
		assert split.size() == 3;
		int[] rgbs = new int[3];
		int i = 0;
		for (String s : split) {
			rgbs[i++] = Integer.parseInt(s, 16);
		}
		return new Color(rgbs[0], rgbs[1], rgbs[2]);
	}

	public static int colorValueCheckBound(int value) {
		return Math.max(Math.min(value, 255), 0);
	}

	public String getOne() {
		return this.one.getRed() + "," + this.one.getGreen() + "," + this.one.getBlue();
	}

	public String getTwo() {
		return this.two.getRed() + "," + this.two.getGreen() + "," + this.two.getBlue();
	}

	public String getThree() {
		return this.three.getRed() + "," + this.three.getGreen() + "," + this.three.getBlue();
	}

	public String getAll() {
		return this.all.getRed() + "," + this.all.getGreen() + "," + this.all.getBlue();
	}
}
