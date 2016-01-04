package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class For {
	public static void main(String[] args) {
		int bits = 3;
		int outputSize = (int) Math.pow(2, bits);
		FFNN n = new FFNN(new int[] { bits, bits * bits, outputSize });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = NeuralUtils.getBinaryCombos(bits);
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			solutions[i] = new double[outputSize];
			int forVar = NeuralUtils.toInt(problems[i]);
			for (int j = 0; j < forVar; j++) {
				solutions[i][j] = 1;
			}
		}
		n.trainTillPerfection(problems, solutions);
	}
}
