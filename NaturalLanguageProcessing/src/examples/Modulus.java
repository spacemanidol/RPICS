package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class Modulus {
	public static void main(String[] args) {
		int bits = 10;
		// Only works with powers of two, for obvious reasons...
		int mod = 2048;
		int responseSize = (int) Math.ceil(Math.log(mod) / Math.log(2));
		FFNN n = new FFNN(new int[] { bits, responseSize });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 10);
		double[][] problems = NeuralUtils.getBinaryCombos(bits);
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			solutions[i] = new double[responseSize];
			NeuralUtils.feedInt(NeuralUtils.toInt(problems[i]) % mod,
					solutions[i]);
		}
		n.trainTillPerfection(problems, solutions);
	}
}
