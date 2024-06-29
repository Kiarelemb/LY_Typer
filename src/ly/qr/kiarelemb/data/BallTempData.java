package ly.qr.kiarelemb.data;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 22:58
 **/
public class BallTempData {
	private final double vTmp;
	private final double xPartA;
	private final double xPartB;
	private final int a;
	private final int v0;
	private final float timePartA;
	private final float timePartB;

	public BallTempData(int a, int v0, float timePartA, float timePartB) {
		this.a = a;
		this.v0 = v0;
		this.timePartA = timePartA;
		this.timePartB = timePartB;
		vTmp = v0 - Math.abs(a) * timePartA;
		xPartA = v0 * timePartA - (Math.abs(a) * timePartA * timePartA) / 2;
		xPartB = xPartA + vTmp * (timePartB - timePartA);
	}

	public double getX(double t) {
		double x;
		if (t > timePartA && t < timePartB) {
			x = xPartA + vTmp * (t - timePartA);
		} else if (t < timePartA) {
			x = v0 * t - (a * t * t) / 2;
		} else {
			double tt = t - timePartB;
			x = xPartB + vTmp * tt + (a * tt * tt) / 2;
		}
		return x;
	}
}