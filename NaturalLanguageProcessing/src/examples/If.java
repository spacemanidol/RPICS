package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class If {
	public static void main(String[] args) {
		int bits = 3;
		FFNN n = new FFNN(new int[] { bits, 3, 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = NeuralUtils.getBinaryCombos(bits);
		double[][] solutions = new double[problems.length][];
		for (int i = 0; i < problems.length; i++) {
			solutions[i] = new double[1];
			if (problems[i][0] == 0) {
				solutions[i][0] = problems[i][1];
			} else {
				solutions[i][0] = problems[i][2];
			}
		}
		n.trainTillPerfection(problems, solutions);
	}
}
