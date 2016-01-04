package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class ClimbingAdder {
	public static void main(String[] args) {
		int bits = 4;
		FFNN n = new FFNN(new int[] { bits, bits * 2, bits * 2, bits });
		for (int a = 123; a < 127; a++) {
			n.setGammaTruncation(0.1);
			n.setVerbosity(2);
			n.setDisplayVisually(true, 500, 500, 2);
			double[][] problems = NeuralUtils.getBinaryCombos(bits);
			double[][] solutions = new double[problems.length][];
			for (int i = 0; i < problems.length; i++) {
				solutions[i] = problems[(i + a) % problems.length];
			}
			n.trainTillPerfection(problems, solutions);
		}
	}
}
