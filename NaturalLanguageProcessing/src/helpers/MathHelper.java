package helpers;

public class MathHelper {
	public static double sigmoid(double x) {
		return (1.0 / (1 + Math.pow(Math.E, (-1) * x)));
	}
}
