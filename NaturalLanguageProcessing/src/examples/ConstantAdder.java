package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class ConstantAdder {
	public static void main(String[] args) {
		int bits = 5;
		int amountToAdd = 1;
		FFNN n = new FFNN(new int[] { bits, bits, bits });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 2);
		double[][] problems = NeuralUtils.getBinaryCombos(bits);
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			solutions[i] = problems[(i + amountToAdd) % problems.length];
		}
		n.trainTillPerfection(problems, solutions);
	}
}
