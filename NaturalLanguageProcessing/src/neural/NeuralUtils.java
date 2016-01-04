package neural;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class NeuralUtils {

	private static Random random = new Random(400);

	public static double random() {
		return random.nextDouble();
	}

	public static double getRandomNeuronWeight() {
		return random() * 2 - 1;
	}

	public static String randomString(int length) {
		return randomString(length, 0, 256);
	}

	public static String randomString(int length, int charLow, int charHigh) {
		char[] ret = new char[length];
		for (int i = 0; i < length; i++) {
			ret[i] = (char) ((int) (random() * (charHigh - charLow)) + charLow);
		}
		return new String(ret);
	}

	public static double[] toArray(String s) {
		double[] ret = new double[s.length() * 8];
		feedString(s, ret);
		return ret;
	}

	public static String toString(double[] values) {
		char[] out = new char[values.length / 8];
		for (int i = 0; i < out.length; i++) {
			out[i] = (char) toInt(values, i * 8, (i + 1) * 8);
		}
		return new String(out);
	}

	public static double[] getRandomArray(int size, double lowBound, double highBound) {
		double[] ret = new double[size];
		for (int i = 0; i < size; i++) {
			ret[i] = random() * (highBound - lowBound) + lowBound;
		}
		return ret;
	}

	public static double[] getRandomBinaryArray(int size) {
		double[] ret = new double[size];
		for (int i = 0; i < size; i++) {
			if (random() < 0.5) {
				ret[i] = 1;
			}
		}
		return ret;
	}

	public static String compact(double value) {
		long l = Double.doubleToLongBits(value);
		String s = "";
		for (int i = 0; i < 8; i++) {
			s += (char) (l % 256);
			l /= 256;
		}
		return s;
	}

	public static void drawLine(Graphics g, int sx, int sy, int ex, int ey, double width) {
		if (width < 0.1) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke((float) (width)));
		g2.drawLine(sx, sy, ex, ey);
	}

	public static String compact(int value) {
		String s = "";
		for (int i = 0; i < 4; i++) {
			s += (char) (value % 256);
			value /= 256;
		}
		return s;
	}

	public static double parseDouble(String compacted) {
		long l = 0;
		for (int i = 0; i < 8; i++) {
			l *= 256;
			l += compacted.charAt(7 - i);
		}
		return Double.longBitsToDouble(l);
	}

	public static int parseInt(String compacted) {
		int ret = 0;
		for (int i = 0; i < 4; i++) {
			ret *= 256;
			ret += compacted.charAt(3 - i);
		}
		return ret;
	}

	public static void feedChar(char c, double[] networkLayer) {
		feedChar(c, networkLayer, 0);
	}

	public static void feedChar(char c, double[] networkLayer, int startIndex) {
		int num = (int) c;
		for (int i = 0; i < 8; i++) {
			networkLayer[8 - i - 1 + startIndex] = num % 2;
			num /= 2;
		}
	}

	public static void feedString(String s, double[] networkLayer) {
		feedString(s, networkLayer, 0);
	}

	public static void feedString(String s, double[] networkLayer, int startIndex) {
		for (int i = 0; i < s.length(); i++) {
			feedChar(s.charAt(i), networkLayer, startIndex + i * 8);
		}
	}

	public static void feedInt(int num, double[] layer, int si, int ei) {
		for (int i = si; i < ei; i++) {
			layer[i] = num % 2;
			num /= 2;
		}
	}

	public static int toInt(double[] layer) {
		return toInt(layer, 0, layer.length);
	}

	public static int toInt(double[] layer, int si, int ei) {
		int ret = 0;
		for (int i = si; i < ei; i++) {
			ret *= 2;
			if (layer[i] >= 0.5) {
				ret++;
			}
		}
		return ret;
	}

	public static void feedInt(int num, double[] layer) {
		feedInt(num, layer, 0, layer.length);
	}

	/**
	 * Get all possible binary combinations of a given length.
	 * 
	 * @templated
	 */
	public static double[][] getBinaryCombos(int length) {
		return getBinaryCombos(0, length, 0);
	}

	/**
	 * Get all possible binary combinations of a given length.
	 * 
	 * @templated
	 */
	public static double[][] getBinaryCombos(int preSpace, int length, int postSpace) {
		double[][] ret = new double[(int) Math.pow(2, length)][preSpace + postSpace + length];
		for (int i = 1; i < ret.length; i++) {
			boolean carry = true;
			for (int j = preSpace; j < length + preSpace; j++) {
				if (carry) {
					ret[i][j] = 1 - ret[i - 1][j];
				} else {
					ret[i][j] = ret[i - 1][j];
				}
				if (ret[i][j] == 1) {
					carry = false;
				}
			}
		}
		return ret;
	}

	/**
	 * Prints the given int array.
	 * 
	 * @templated
	 */
	public static void print(double[][] values) {
		System.out.println("-----------------");
		for (int i = 0; i < values.length; i++) {
			print(values[i]);
		}
	}

	/**
	 * Prints the given int array.
	 * 
	 * @templated
	 */
	public static void print(double[] values) {
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				System.out.print(", ");
			}
			System.out.print(values[i]);
		}
		System.out.println();
	}

	public static double toDouble(boolean value) {
		return value ? 0 : 1;
	}
}
