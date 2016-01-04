package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class Adder {
	public static void main(String[] args) {
		int bits =7;
		FFNN n = new FFNN(new int[] { bits * 2, bits * 4, bits * 4, bits + 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = NeuralUtils.getBinaryCombos(bits * 2);
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			solutions[i] = new double[bits + 1];
			NeuralUtils.feedInt(NeuralUtils.toInt(problems[i], 0, bits)
					+ NeuralUtils.toInt(problems[i], bits, bits * 2),
					solutions[i]);
		}
		n.trainTillPerfection(problems, solutions);
	}
}
